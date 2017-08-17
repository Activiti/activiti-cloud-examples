# Activiti Cloud Example

This example shows how to start the Activiti Cloud Infrastructure and how you can provide your Domain Specific Runtime Bundles.
These Runtime Bundles provides us with a way to isolate Process Runtimes. Each Runtime Bundle will be in charge of executing a certain sets of 
Process Definitions. 

The REST endpoints are also secured using keycloak as an identity provider. 

Audit data is recored in a separate service, also running behind the gateway.

The process definitions are deployed from the process-definitions directory, which is mounted as a docker volume.

Various docker container properties can be modified by environment variables. See the properties files for the repos of the images for values marked with ${}.

## Quickstart

1) Add this entry to your hosts (/etc/hosts) file - 127.0.0.1       activiti-cloud-keycloak
2) Run using docker-compose up -d from this directory to start all the infrastructure services \
(rabbit-mq, activiti-cloud-query, activiti-cloud-audit, activiti-cloud-keycloak, activiit-cloud-registry, activiti-cloud-gateway, postgresql)
3) Load (Chrome Plugin)Postman collection located in this directory 
4) Go to the keycloak folder and send the getKeycloakToken request
5) You can find all the runtime bundles registered in the system by executing the request inside the gateway folder called routes
6) In order to start a new Runtime Bundle (by default there is no Runtime Bundle) you need to:\
    6.1) cd runtime-bundle-docker-example/ \
    6.2) docker build -t "myrb1" . (to build a new docker image called "myrb1" using the Dockerfile located in that directory)\
    6.3) docker run --network=activiticloudexample_default --link=activiti-cloud-keycloak:activiti-cloud-keycloak --link=rabbitmq:rabbitmq --link=activiti-cloud-registry:acti-cloud-registry --link=rb-postgres \
            --name=activiti-cloud-runtime-bundle1 -e "HOST=activiti-cloud-runtime-bundle1" \
            -e "NAME=bundle1" -"PORT=8081" myrb1 \
    *) Notice that links are necessary to make sure that our runtime bundle can "talk" with other services. \
            -e set environment variables needed to differentiate runtime bundles between each other.      

