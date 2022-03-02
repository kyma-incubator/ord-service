# Default configuration
ifneq ($(strip $(DOCKER_PUSH_REPOSITORY)),)
IMG_NAME := $(DOCKER_PUSH_REPOSITORY)$(DOCKER_PUSH_DIRECTORY)/$(APP_NAME)
else
IMG_NAME := $(APP_NAME)
endif

TAG := $(DOCKER_TAG)
# BASE_PKG is a root packge of the component
BASE_PKG := github.com/kyma-incubator/ord-service
# IMG_GOPATH is a path to go path in the container
IMG_GOPATH := /workspace/go

# Other variables
# LOCAL_DIR in a local path to scripts folder
LOCAL_DIR = $(dir $(abspath $(lastword $(MAKEFILE_LIST))))
# COMPONENT_DIR is a local path to component
COMPONENT_DIR = $(shell pwd)
# COMPONENT_NAME is equivalent to the name of the component as defined in it's helm chart
COMPONENT_NAME = $(shell basename $(COMPONENT_DIR))
# WORKSPACE_LOCAL_DIR is a path to the scripts folder in the container
WORKSPACE_LOCAL_DIR = $(IMG_GOPATH)/src/$(BASE_PKG)/scripts
# WORKSPACE_COMPONENT_DIR is a path to component in hte container
WORKSPACE_COMPONENT_DIR = $(IMG_GOPATH)/src/$(BASE_PKG)/$(APP_PATH)
# DEPLOYMENT_NAME matches the component's deployment name in the cluster
DEPLOYMENT_NAME="compass-"$(COMPONENT_NAME)
# NAMESPACE defines the namespace into which the component is deployed
NAMESPACE="compass-system"

# Base docker configuration
DOCKER_CREATE_OPTS := -v $(LOCAL_DIR):$(WORKSPACE_LOCAL_DIR):delegated --rm -w $(WORKSPACE_COMPONENT_DIR) $(BUILDPACK)

# Check if running with TTY
ifeq (1, $(shell [ -t 0 ] && echo 1))
DOCKER_INTERACTIVE := -i
DOCKER_CREATE_OPTS := -t $(DOCKER_CREATE_OPTS)
else
DOCKER_INTERACTIVE_START := --attach
endif

# Buildpack directives
define buildpack-mount
.PHONY: $(1)-local $(1)
$(1):
	@echo make $(1)
	@docker run $(DOCKER_INTERACTIVE) \
		-v $(COMPONENT_DIR):$(WORKSPACE_COMPONENT_DIR):delegated \
		$(DOCKER_CREATE_OPTS) make $(1)-local
endef

define buildpack-cp-ro
.PHONY: $(1)-local $(1)
$(1):
	@echo make $(1)
	$$(eval container = $$(shell docker create $(DOCKER_CREATE_OPTS) make $(1)-local))
	@docker cp $(COMPONENT_DIR)/. $$(container):$(WORKSPACE_COMPONENT_DIR)/
	@docker start $(DOCKER_INTERACTIVE_START) $(DOCKER_INTERACTIVE) $$(container)
endef

release: resolve build-image

.PHONY: build-image
build-image: pull-licenses
	docker run --rm --privileged linuxkit/binfmt:v0.8 # https://stackoverflow.com/questions/70066249/docker-random-alpine-packages-fail-to-install
 	docker buildx create --name multi-arch-builder --use
 	docker buildx build --platform linux/amd64,linux/arm64 -t $(IMG_NAME):$(TAG) --push .
docker-create-opts:
	@echo $(DOCKER_CREATE_OPTS)

# Targets mounting sources to buildpack
MOUNT_TARGETS = build resolve pull-licenses
$(foreach t,$(MOUNT_TARGETS),$(eval $(call buildpack-mount,$(t))))

# Builds new Docker image into Minikube's Docker Registry
build-to-minikube: pull-licenses
	@eval $$(minikube docker-env) && docker build -t $(IMG_NAME) .

build-local:

test-local:

resolve-local:

pull-licenses-local:
ifdef LICENSE_PULLER_PATH
	bash $(LICENSE_PULLER_PATH)
else
	mkdir -p licenses
endif

# Targets copying sources to buildpack
COPY_TARGETS = test
$(foreach t,$(COPY_TARGETS),$(eval $(call buildpack-cp-ro,$(t))))

# Sets locally built image for a given component in Minikube cluster
deploy-on-minikube: build-to-minikube
	kubectl set image -n $(NAMESPACE) deployment/$(DEPLOYMENT_NAME) $(COMPONENT_NAME)=$(DEPLOYMENT_NAME):latest
	kubectl rollout restart -n $(NAMESPACE) deployment/$(DEPLOYMENT_NAME)
