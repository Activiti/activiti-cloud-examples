#!/bin/bash

#evaluate the usage of the command below
#set -e

if [[ $# -eq 0 ]] ; then
    echo 'no arguments passed'
    exit 1
fi

REPO_LOCATION=$1

echo "repository location: $REPO_LOCATION"

cd $REPO_LOCATION

kubectl create -f auth.yml

AUTH_ADDRESS=$(kubectl get service activiti-cloud-sso-idm-kub -o jsonpath={.status.loadBalancer.ingress[0].hostname})

while [ -z "$AUTH_ADDRESS" ]; do     
        echo "$AUTH_ADDRESS authentication service not ready yet"
        sleep 15
        AUTH_ADDRESS=$(kubectl get service activiti-cloud-sso-idm-kub -o jsonpath={.status.loadBalancer.ingress[0].hostname})      
        continue
done
echo "authentication service is up and running: $AUTH_ADDRESS"


helm install ./infrastructure/ --set keycloakURL="http://$AUTH_ADDRESS:30081/auth"

MAX_NUMBER_PODS=$2

create_infrastructure_response=$(kubectl get pods | awk '{print $3}' | grep -c 'Running' || true)
while [ $create_infrastructure_response -lt $MAX_NUMBER_PODS ]; do     
        echo "$create_infrastructure_response pods only running"
        sleep 20
        create_infrastructure_response=$(kubectl get pods | awk '{print $3}' | grep -c 'Running' || true)       
        continue
done
echo "$create_infrastructure_response pods are running, deploy finished successfully"

echo "$(kubectl get service entrypoint -o jsonpath={.status.loadBalancer.ingress[0].hostname})"
