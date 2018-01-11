## Activiti on kubernetes and aws

Deploy Activiti to an existing kubernetes cluster on AWS

### Kube config

You need to use the right ~/.kube/config file which points to the right cluster in aws
 
### Steps

1) kubectl create -f auth.yml

2) export AUTH_ADDRESS=$(kubectl get  service activiti-cloud-sso-idm-kub -o jsonpath={.status.loadBalancer.ingress[0].hostname})

where kubectl get  service activiti-cloud-sso-idm-kub -o jsonpath={.status.loadBalancer.ingress[0].hostname}
gives you the new elb host for keycloak

3) helm install ./infrastructure/ --set keycloakURL="http://$AUTH_ADDRESS:30081/auth"

where kubectl get service entrypoint -o jsonpath={.status.loadBalancer.ingress[0].hostname}
gives you the new elb host for gateway

Now you are able to interact with Activiti API as you have keycloak and gateway urls.

