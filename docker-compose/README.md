#  Easy way to run activiti7 with docker compose 

### Prerequisite:
Change DOCKER_IP to your ip in .env

### Start options
For starting whole system
```
make all
```
For starting all services without modeller 
```
make application 
```

For starting modeler and keycloak
```
make modeler
```

To see logs 
```
make logs 
```

Other available actions
```
make help
```

### Modeller 
To access modeler please use url
```
http://$DOCKER_IP/modeling
```
For example
```
http://192.168.1.9/modeling
```
You will be redirected to keycloak where you have to use credentials *modeler/password* 

### Start process 
If there is no Postman intalled please download Postman https://www.getpostman.com/downloads/
Then 
```
git clone https://github.com/Activiti/activiti-cloud-examples
```
Then add to Postman collection _Activiti v7 REST API.postman_collection.json_ <br>
*Then at the top right choose manage environment then use your own ip in _current value_ tab.* <br> 
Then use _activiti_ as default environment. <br>
To start work execute _getKeycloakToken hruser_ in postman keycloak folder. <br>
Then run startProcess in rb-my-app postman folder. <br>





