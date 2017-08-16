# Activiti HAL Rest Sample Using Spring Boot

This sample program demonstrates the v7 Activiti BPM Engine with deployed processes running behind a spring cloud gateway.

The REST endpoints are also secured using keycloak as an identity provider. Audit data is recored in a separate service, also running behind the gateway.

The process definitions are deployed from the process-definitions directory, which is mounted as a docker volume.

Various docker container properties can be modified by environment variables. See the properties files for the repos of the images for values marked with ${}.

## Quickstart

1) Add this entry to your hosts file - 127.0.0.1       activiti-cloud-keycloak
2) Run using docker-compose up from this directory (if you get a no such image error do docker-compose down first)
5) Go to http://localhost:8080/runtime-bundle1/v1/process-instances in browser or postman. If using postman collection hit keycloak token endpoint (on host activiti-keycloak) first.
5) For browser to reach the endpoint you'll need to enter testuser/password at the keycloak prompt
7) To create process instances you'll need to use the postman collection

## Gateway and Service Registration

The project uses spring cloud to provide a gateway and service registration.

To see registered services, go to the registry service (by default on http://localhost:8761/). To see gateway routes to services, go to the gateway routes endpoint (should be configured as 8080/application/routes in the gateway project).

## Alternative Setups

It is also possible to just start the core services directly from the main Activiti project and then start only this sample application using the IDE. To do this rabbitmq also needs to be added to hosts file or changed in application.properties to localhost.

It is also possible to run keycloak and rabbitmq standalone. See the main Activiti project's docker notes for this.

## Postman

A postman postman collection is provided which includes a call to get the keycloak token and use it on subsequent requests - note that the token does expire so can then be necessary to make the call again.

Endpoints can change so the postman collection can get out of sync with the endpoints provided by activiti-services-rest in Activiti/Activiti. If so see that project to update. Some values passed to endpoints are example values. The keycloak endpoint's format is not expected to change unless keycloak changes it.