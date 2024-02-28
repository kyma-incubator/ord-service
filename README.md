## :warning: This repository has been deprecated and will be archived soon. :warning:

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

A suggested workflow is to run a local installation of Director with its dedicated `run.sh` script
and then running the ORD Service `run.sh` script. The default values of the ORD Service
are configured to work with a local Director out of the box.

In order to call the ORD service APIs in this local setup, send the following header:

```json
authorization: Bearer eyAiYWxnIjogIlJTMjU2IiwgImtpZCI6ICI4NTQ3NzFlMi00MGFlLTQyMzctOTcwNi1kMTg5NDc2M2Y4N2IiLCAidHlwIjogIkpXVCIgfQo.eyAiY29uc3VtZXJzIjogIlt7XCJDb25zdW1lcklEXCI6XCJhZG1pblwiLFwiQ29uc3VtZXJUeXBlXCI6XCJTdGF0aWMgVXNlclwiLFwiRmxvd1wiOlwiSldUXCJ9XSIsICJleHAiOiAxNjMzMTAxNTI5LCAiaWF0IjogMTYzMzA5NzkyOSwgImlzcyI6ICJodHRwczovL29hdGhrZWVwZXIua3ltYS5sb2NhbC8iLCAianRpIjogIjg1NmNkNWU4LWQ4NzEtNDM5MS04MDI5LTI4NmRjZTcyMzRjZiIsICJuYmYiOiAxNjMzMDk3OTI5LCAic2NvcGVzIjogImFwcGxpY2F0aW9uOnJlYWQgYXBwbGljYXRpb246d3JpdGUgYXBwbGljYXRpb25fdGVtcGxhdGU6cmVhZCBhcHBsaWNhdGlvbl90ZW1wbGF0ZTp3cml0ZSBpbnRlZ3JhdGlvbl9zeXN0ZW06cmVhZCBpbnRlZ3JhdGlvbl9zeXN0ZW06d3JpdGUgcnVudGltZTpyZWFkIHJ1bnRpbWU6d3JpdGUgbGFiZWxfZGVmaW5pdGlvbjpyZWFkIGxhYmVsX2RlZmluaXRpb246d3JpdGUgZXZlbnRpbmc6bWFuYWdlIHRlbmFudDpyZWFkIGF1dG9tYXRpY19zY2VuYXJpb19hc3NpZ25tZW50OnJlYWQgYXV0b21hdGljX3NjZW5hcmlvX2Fzc2lnbm1lbnQ6d3JpdGUgYXBwbGljYXRpb24uYXV0aHM6cmVhZCBhcHBsaWNhdGlvbi53ZWJob29rczpyZWFkIGFwcGxpY2F0aW9uX3RlbXBsYXRlLndlYmhvb2tzOnJlYWQgYnVuZGxlLmluc3RhbmNlX2F1dGhzOnJlYWQgZG9jdW1lbnQuZmV0Y2hfcmVxdWVzdDpyZWFkIGV2ZW50X3NwZWMuZmV0Y2hfcmVxdWVzdDpyZWFkIGFwaV9zcGVjLmZldGNoX3JlcXVlc3Q6cmVhZCBpbnRlZ3JhdGlvbl9zeXN0ZW0uYXV0aHM6cmVhZCBydW50aW1lLmF1dGhzOnJlYWQgZmV0Y2gtcmVxdWVzdC5hdXRoOnJlYWQgd2ViaG9va3MuYXV0aDpyZWFkIGludGVybmFsX3Zpc2liaWxpdHk6cmVhZCIsICJzdWIiOiAiQ2lCcmQzbDNielUxY1hZek1UZGhNWGsxYjNKbGNUZGpaM1pxY3pNMk1XMXRieElGYkc5allXdyIsICJ0ZW5hbnQiOiJ7XCJjb25zdW1lclRlbmFudFwiOlwiM2U2NGViYWUtMzhiNS00NmEwLWIxZWQtOWNjZWUxNTNhMGFlXCIsXCJleHRlcm5hbFRlbmFudFwiOlwiM2U2NGViYWUtMzhiNS00NmEwLWIxZWQtOWNjZWUxNTNhMGFlXCJ9IiB9Cg.
```

## Usage

Generally the ORD Service provides read-only access to Packages, APIs, Events and their specs for applications
registered in Compass. In this sense a typical usage of the ORD Service would be to
deploy both a Director installation and ORD Service, register some applications with Packages and APIs
in Compass and then discover those resources through the ORD Service.

In order to call the APIs of the ORD service, send the following header:

```json
Tenant: <tenant-id>
```

## Development

The ORD Service implementation is located in this repo (`kyma-incubator/ord-service`). The helm installation charts
are in the `kyma-incubator/compass` repository however. This means that after introducing new changes
in `kyma-incubator/ord-service` we should not forget to increase the `chart/compass/values.yaml` ORD Service version
as well.

Another thing to consider is that there are E2E tests for the ORD Service, which are located outside this repository - they are instead in
`kyma-incubator/compass/tests/ord-service`. Having this in mind we should always ensure
to provide necessary E2E tests in the `kyma-incubator/compass` repository when the
ORD Service evolves and new functionalities are introduced. There is also a dedicated
E2E test image in the `chart/compass/values.yaml` file as well, so we should make sure to bump that as well.
