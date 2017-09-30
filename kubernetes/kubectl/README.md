## Kubectl option

Steps to run:

1. If you don't have kubectl tied to an AWS cluster, first install minikube to run locally - https://github.com/kubernetes/minikube
2. Go to this directory.
3. minikube start
4. minikube dashboard
5. kubectl create -f infrastructure.yml
6. kubectl create -f runtime-bundle.yml (but get infrastructure running first)

To find entrypoint IP look at the dashboard IP or do 'minikube service entrypoint'. Use this IP and port 30080 in Postman and demo-ui-client for all gateway URLs (i.e. all but SSO).

To use AWS cluster skip steps 1-4 and instead update the infrastructure.yml file, search for 'minikube' to find sections to modify (instructions in file).

The IP from the minikube service entrypoint should be added in the etc/hosts file as mapped to activiti-cloud-sso-idm-kub.

For SSO also change activiti-cloud-sso-idm to activiti-cloud-sso-idm-kub and port from 8180 to 30081 in keycloak.json in the demo UI client.

To stop infra do kubectl delete -f infrastructure.yml

To use a locally-built version of an image set imagePullPolicy: Never in yml file and run eval $(minikube docker-env) before building. Note minikube docker deamon may be different version so builds within it may not work the same.

You'll need to increase the number of cpus and ram available to minikube from the defaults - we suggest at least 4 cpus and 4gb