## Activiti on kubernetes and aws

Deploy Activiti to an existing kubernetes cluster on AWS.

### Kube config

You need to use the right **~/.kube/config** file which points to the right cluster in aws
 
### Steps

1. `kubectl create -f auth.yml`

2. `export AUTH_ADDRESS=$(kubectl get  service activiti-cloud-sso-idm-kub -o jsonpath={.status.loadBalancer.ingress[0].hostname})`

   Command below gives you the new elb host for keycloak
   
   `kubectl get  service activiti-cloud-sso-idm-kub -o jsonpath={.status.loadBalancer.ingress[0].hostname}`

3. `helm install ./infrastructure/ --set keycloakURL="http://$AUTH_ADDRESS:30081/auth"`

    Command below gives you the new elb host for gateway. You can find the port on the descriptor file.
    
    `kubectl get service entrypoint -o jsonpath={.status.loadBalancer.ingress[0].hostname}`

Now you are able to interact with Activiti API as you have keycloak and gateway urls.

### Logging and Tracing tools

1. `kubectl create -f tracing/`

    Command below gives you the elb host of the zipkin dashboard. You can find the port on the descriptor file.

    `kubectl get service zipkin -o jsonpath={.status.loadBalancer.ingress[0].hostname}`

2.  `kubectl create -f logging/`

    Command below gives you the elb host of the kibana dashboard. You can find the port on the descriptor file.
    
    `kubectl get service kibana -o jsonpath={.status.loadBalancer.ingress[0].hostname}`

### Deploy to different cloud providers

The same configuration above applies to different environments, for instance PKS.
The only difference in the procedure above woud be that AWS works with hostname while PKS with IP addresses, so for PKS you need to use substitute
ingress[0].hostname with ingress[0].ip