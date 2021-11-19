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
authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg1NDc3MWUyLTQwYWUtNDIzNy05NzA2LWQxODk0NzYzZjg3YiIsInR5cCI6IkpXVCJ9.eyJjb25zdW1lcnMiOiJbe1wiQ29uc3VtZXJJRFwiOlwiYWRtaW5cIixcIkNvbnN1bWVyVHlwZVwiOlwiU3RhdGljIFVzZXJcIixcIkZsb3dcIjpcIkpXVFwifV0iLCJleHAiOjE2MzMxMDE1MjksImlhdCI6MTYzMzA5NzkyOSwiaXNzIjoiaHR0cHM6Ly9vYXRoa2VlcGVyLmt5bWEubG9jYWwvIiwianRpIjoiODU2Y2Q1ZTgtZDg3MS00MzkxLTgwMjktMjg2ZGNlNzIzNGNmIiwibmJmIjoxNjMzMDk3OTI5LCJzY29wZXMiOiJhcHBsaWNhdGlvbjpyZWFkIGFwcGxpY2F0aW9uOndyaXRlIGFwcGxpY2F0aW9uX3RlbXBsYXRlOnJlYWQgYXBwbGljYXRpb25fdGVtcGxhdGU6d3JpdGUgaW50ZWdyYXRpb25fc3lzdGVtOnJlYWQgaW50ZWdyYXRpb25fc3lzdGVtOndyaXRlIHJ1bnRpbWU6cmVhZCBydW50aW1lOndyaXRlIGxhYmVsX2RlZmluaXRpb246cmVhZCBsYWJlbF9kZWZpbml0aW9uOndyaXRlIGV2ZW50aW5nOm1hbmFnZSB0ZW5hbnQ6cmVhZCBhdXRvbWF0aWNfc2NlbmFyaW9fYXNzaWdubWVudDpyZWFkIGF1dG9tYXRpY19zY2VuYXJpb19hc3NpZ25tZW50OndyaXRlIGFwcGxpY2F0aW9uLmF1dGhzOnJlYWQgYXBwbGljYXRpb24ud2ViaG9va3M6cmVhZCBhcHBsaWNhdGlvbl90ZW1wbGF0ZS53ZWJob29rczpyZWFkIGJ1bmRsZS5pbnN0YW5jZV9hdXRoczpyZWFkIGRvY3VtZW50LmZldGNoX3JlcXVlc3Q6cmVhZCBldmVudF9zcGVjLmZldGNoX3JlcXVlc3Q6cmVhZCBhcGlfc3BlYy5mZXRjaF9yZXF1ZXN0OnJlYWQgaW50ZWdyYXRpb25fc3lzdGVtLmF1dGhzOnJlYWQgcnVudGltZS5hdXRoczpyZWFkIGZldGNoLXJlcXVlc3QuYXV0aDpyZWFkIHdlYmhvb2tzLmF1dGg6cmVhZCIsInN1YiI6IkNpQnJkM2wzYnpVMWNYWXpNVGRoTVhrMWIzSmxjVGRqWjNacWN6TTJNVzF0YnhJRmJHOWpZV3ciLCJ0ZW5hbnQiOiJ7XCJjb25zdW1lclRlbmFudFwiOlwiM2U2NGViYWUtMzhiNS00NmEwLWIxZWQtOWNjZWUxNTNhMGFlXCIsXCJleHRlcm5hbFRlbmFudFwiOlwiM2U2NGViYWUtMzhiNS00NmEwLWIxZWQtOWNjZWUxNTNhMGFlXCJ9In0=.FsXpLQupSiw29rnUSP66yxw8qRHT88UMadFBlmtCsggOhy40Yha3r41ENCENpeK9IHmiqhxHljmCzjhvcmfvVd_b5kVhlLt0PTBLC37LBCn5NBhHMCjMRUa35NzhjHG6j5PiZvQWmNSu1J7OZNnh-X08gyBlDeQWH8gEMmxK9jNdoTj0ZVUun1o5FvjCZlRrEfAViCJlqytgXTpybgzEQ6LDiJ_47WVel5pi7lWx1JZTJ5oU-4L4IN0HokD57RNRqwzM6AaP4QQnBum9gKNZz9Ywrb4DiHMeDVAYWO4JFNRkilSYylIUIIZtd5eA3o3rDxFkN0BG5V4Y7eHgjTp0HQ
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
