# Open Resource Discovery Service


## Overview

The Open Resource Discovery Service provides discovery of Packages, APIs, Events and their specs of applications registered in Compass.

## Prerequisites

- Make
- Docker
- Java
- Maven

## Installation

In order to run the ORD Service a dedicated DB should be created. By default the ORD service expects
the DB to be on `localhost:5432`. More information on the default values can be found in the following
configuration file: `components/ord-service/src/main/resources/application.yaml`

After having deployed a DB the ORD Service can be deployed by simply running the following script:
`./components/ord-service/run.sh`

A suggested workflow is to run a local installation of Director with it's dedicated `run.sh` script
and then running the ORD Service `run.sh` script. The default values of the ORD Service
are configured to work with a local Director out of the box.

## Usage

Generally the ORD Service provides read-only access to Packages, APIs, Events and their specs for applications
registered in Compass. In this sense a typical usage of the ORD Service would be to
deploy a both a Director installation and ORD Service, register some applications with Packages and APIs
in Compass and then discover those resources through the ORD Service.

## Development

The ORD Service implementation is located in this repo (`kyma-incubator/ord-service`). The helm installation charts
are in the `kyma-incubator/compass` repository however. This means that after introducing new chnages
in `kyma-incubator/ord-service` we should not forget to increase the `chart/compass/values.yaml` ORD Service version
as well.

Another thing to consider is that there are E2E tests for the ORD Service, which are located outside this repository - they are instead in
`kyma-incubator/compass/tests/ord-service`. Having this in mind we should always ensure
to provide necessary E2E tests in the `kyma-incubator/compass` repository when the
ORD Service evolves and new functionalities are introduced. There is also a dedicated
E2E test image in the `chart/compass/values.yaml` file as well, so we should make sure to bump that as well.