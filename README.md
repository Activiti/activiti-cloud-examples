# Activiti Cloud Example

This example shows how to start the Activiti Cloud Infrastructure and how you can provide your Domain Specific Runtime Bundles.
These Runtime Bundles provides us with a way to isolate Process Runtimes. Each Runtime Bundle will be in charge of executing a certain sets of 
Process Definitions. 

This example shows how to start the infrastructure services using Docker Compose and how to create and run your own runtime bundles (as many as you want).
The services started by docker compose are: rabbit-mq, activiti-cloud-query, activiti-cloud-audit, activiti-cloud-keycloak, activiit-cloud-registry, activiti-cloud-gateway, postgresql.

Now when you want to create your own Runtime Bundles you will want to build and run your own docker images. Inside the runtime-bundle-docker-example a Dockerfile is provided which extends our base Runtime Bundle Docker Image and it defines which process definitions will 
be included. You can also modify the containers by setting any of the variables defined using ${} in the activiti-cloud projects.

## Quickstart

1) Add this entry to your hosts (/etc/hosts) file - 127.0.0.1       activiti-cloud-keycloak
2) Run using docker-compose up -d from this directory to start all the infrastructure services 
3) Load (Chrome Plugin)Postman collection located in this directory 
4) Go to the keycloak folder and send the getKeycloakToken request
5) You can find all the runtime bundles registered in the system by executing the request inside the gateway folder called routes
6) In order to start a new Runtime Bundle (by default there is no Runtime Bundle) you need to:\
    6.1) cd runtime-bundle-docker-example/ \
    6.2) docker build -t "my-runtime-bundle" . (to build a new docker image called "my-runtime-bundle" using the Dockerfile located in current directory, denoted by '.')\
    6.3) docker run --network=infrastructure_default --link=activiti-cloud-keycloak:activiti-cloud-keycloak --link=rabbitmq:rabbitmq --link=activiti-cloud-registry:acti-cloud-registry --link=rb-postgres \
            --name=activiti-cloud-runtime-bundle1 -e "ACT_RB_HOST=activiti-cloud-runtime-bundle1" \
            -e "ACT_RB_APP_NAME=bundle1" -e "ACT_RB_PORT=8081" my-runtime-bundle \
    *) Notice that links are necessary to make sure that our runtime bundle can "talk" with other services. \
            -e set environment variables needed to differentiate runtime bundles between each other.    
    6.4) To start another runtime bundle run the previous command again but replacing all the '1's with '2's

