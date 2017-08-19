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
2) cd infrastructure/ 
   2.1) Run > docker-compose up -d (to start all the infrastructure services) 
3) Load (Chrome Plugin)Postman collection located in the root directory 
4) Go to the keycloak folder and send the getKeycloakToken request
5) You can find all the runtime bundles registered in the system by executing the request inside the gateway folder called routes
6) In order to start a new Runtime Bundle (by default there is no Runtime Bundle started) you need to:\
    6.1) cd docker-runtime-bundle/ \
    6.2) docker build -t "my-runtime-bundle" . (to build a new docker image called "my-runtime-bundle" using the Dockerfile located in current directory, denoted by '.')\
    6.3) cd .. 
7) docker-compose -f rb-docker-compose.yml up -d (to start a new runtime bunlde using the previously generated image)
    *) look into the rb-docker-compose.yml file if you want to start a different runtime bundle. This docker compose is starting
    postgresql as the data store for the process engine, you can obviously change all these configurations and also choose to share the
    same database instance for all your runtime bundles.     
    
## Question / Issues / Comments
Please feel free to open an issue or get in touch with us if you have problems running these 
examples. You can join us in [Gitter](https://gitter.im/Activiti/Activiti7?utm_source=share-link&utm_medium=link&utm_campaign=share-link) if you want assistance or have questions. 
We welcome contributions.  
