## How to use scripts

You need to use the right **~/.kube/config** file which points to the right cluster in aws
 
### undeploy

Usage example:

./undeploy.sh ${REPO_LOCATION}/kubernetes/aws-deployment/

It accepts one env variable to point to the correct location.

### deploy

Usage example:

./deploy.sh ${REPO_LOCATION}/kubernetes/aws-deployment/ 8

It accepts the first env variable to point to the correct location.
It accepts the second env variable to be aware of how many pods you are going to create. 

Only after all pods will be deployed the scripts will terminate