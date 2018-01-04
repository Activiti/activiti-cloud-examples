## Kubectl option

Steps to run:

1. If you don't have kubectl tied to an AWS cluster, first install minikube to run locally - https://github.com/kubernetes/minikube
2. Go to this directory.
3. minikube start --memory 8000 --cpus 4 --extra-config=kubelet.CAdvisorPort=4194
4. minikube dashboard
5. kubectl create -f infrastructure.yml
6. kubectl create -f runtime-bundle.yml (but get infrastructure running first)

To find entrypoint IP look at the dashboard IP or do 'minikube service entrypoint'. Use this IP and port 30080 in Postman and demo-ui-client for all gateway URLs (i.e. all but SSO).

To use AWS cluster skip steps 1-4 and instead update the infrastructure.yml file, search for 'minikube' to find sections to modify (instructions in file). On AWS you should see external endpoints on the 'Services' page. If you don't have an external endpoints column it's likely you didn't update the yml files to use LoadBalancer before deploy. You should be able to ping to get the IP of the IDM. You'll need the gateway entrypoint for use in postman.

The IP from the minikube service entrypoint should be added in the etc/hosts file as mapped to activiti-cloud-sso-idm-kub.

For SSO also change activiti-cloud-sso-idm to activiti-cloud-sso-idm-kub and port from 8180 to 30081 in keycloak.json in the demo UI client.

To stop infra do kubectl delete -f infrastructure.yml

To use a locally-built version of an image set imagePullPolicy: Never in yml file and run `eval $(minikube docker-env)` before building. Note minikube docker deamon may be different version so builds within it may not work the same.

To see memory usage and cpu usage do `open http://$(minikube ip):4194`