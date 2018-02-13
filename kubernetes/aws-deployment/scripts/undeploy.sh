#!/bin/bash

# evaluate the usage of the command below
# set -e

if [[ $# -eq 0 ]] ; then
    echo 'no arguments passed'
    exit 1
fi

REPO_LOCATION=$1

echo "repository location: $REPO_LOCATION"

cd $REPO_LOCATION

kubectl delete -f auth.yml

INFRASTRUCTURE_RELEASE_NAME=$(helm ls | grep "infrastructure" | awk '{print $1}')

echo "infrastructure release name: $INFRASTRUCTURE_RELEASE_NAME"

helm delete $INFRASTRUCTURE_RELEASE_NAME

# by default there 8 pods running
delete_infrastructure_response=$(kubectl get pods | awk '{print $3}' | grep -c 'Running' || true)
while [ $delete_infrastructure_response -gt 0 ]; do	
	echo "$delete_infrastructure_response services still running"
	sleep 10
        delete_infrastructure_response=$(kubectl get pods | awk '{print $3}' | grep -c 'Running' || true)	
        continue
done
echo "$delete_infrastructure_response services are running, undeploy finished successfully"

