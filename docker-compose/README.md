#  Easy way to run Activiti Cloud with Docker Compose

### Prerequisite

Change DOCKER_IP to your IP address in .env or keep the default value 'kubernetes.docker.internal'
if you are using [Docker Desktop](https://www.docker.com/products/docker-desktop).

### Start options
For starting the whole system:
```
make all
```
and stopping:
```
make stop
```

For starting all services without modeler:
```
make application
```
and stopping:
```
make application/stop
```

For starting modeler and keycloak:
```
make modeler
```
and stopping:
```
make modeler/stop
```

To see logs:
```
make logs
```

Other available actions:
```
make help
```

### Modeler

To access modeler please use url
```
http://$DOCKER_IP/modeling
```
For example
```
http://192.168.1.9/modeling
```
You will be redirected to keycloak where you have to use credentials *modeler/password*


### Start Process

If not installed, please download [Postman](https://www.getpostman.com/downloads), then:
```
git clone https://github.com/Activiti/activiti-cloud-examples
```
and add to Postman collection `Activiti v7 REST API.postman_collection.json`.
Then at the top right choose manage environment then use your own ip in _current value_ tab.
Then use _activiti_ as default environment.
To start work execute _getKeycloakToken hruser_ in postman keycloak folder.
Then run startProcess in rb-my-app postman folder.





