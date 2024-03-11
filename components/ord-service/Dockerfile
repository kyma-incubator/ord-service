FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

ENV BASE_APP_DIR /go/src/github.com/kyma-incubator/ord-service/components/ord-service
WORKDIR ${BASE_APP_DIR}

RUN apk --no-cache add git

COPY scripts ./scripts

# Copy dependency files
COPY pom.xml .

RUN ./scripts/install_dependencies.sh

RUN mvn dependency:resolve

#
# Copy files
#

COPY . .

#
# Build app
#

RUN ./run.sh --skip-deps --no-start --skip-tests && \
    VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -DforceStdout -q) && \
    mkdir /app && mv ./target/ord-service-$VERSION.jar /app/ord-service.jar

FROM eclipse-temurin:17.0.9_9-jre-alpine
LABEL source = git@github.com:kyma-incubator/compass.git
WORKDIR /app

#
# Copy binary
#

COPY --from=builder /app/ord-service.jar /app/

#
# Install curl & CA certs
#

RUN apk --no-cache add curl ca-certificates

#
# Run app
#

CMD ["java", "-jar", "ord-service.jar"]
