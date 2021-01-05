APP_NAME = compass-ord-service
APP_PATH = components/ord-service
BUILDPACK = eu.gcr.io/kyma-project/test-infra/buildpack-java:v20210105-9ce011db
SCRIPTS_DIR = $(realpath $(shell pwd)/../..)/scripts
export COMPONENT_DIR = $(realpath $(shell pwd))

include $(SCRIPTS_DIR)/generic_make_go.mk

test-local:
	./scripts/install_dependencies.sh
	./scripts/build.sh

resolve-local:
	./scripts/install_dependencies.sh
	./scripts/build.sh

build-local:
	./scripts/build.sh -DskipTests