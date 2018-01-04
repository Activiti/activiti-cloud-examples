# Twitter Campaigns Example

This example has a runtime bundle that represents a twitter marketing campaign in two processes. It uses connectors to interface to external services and to implement service tasks outside the runtime bundle.

## Setup

If you're happy to use a fake twitter feed then there's no need to register a twitter account and you can skip this step. In order to use live twitter you need a twitter app account and an app. Go to https://apps.twitter.com/ to set up (the name, description and website of the app don't matter), generate an access token for the app and put the credentials into either the connectors-docker-compose.yml file if you're using docker-compose or the connectors.yml if you're using kubernetes.

## To run with docker-compose

First do mvn clean install from this directory then do `docker-compose -f <FILE_NAME> up` for each of the docker descriptor files. Either do this in separate windows to follow the logs or use `-d` option and kitematic.

## To run with minikube

First start minikube e.g. `minikube start --memory 12000 --cpus 6 --extra-config=kubelet.CAdvisorPort=4194`

From this directory do:

`eval $(minikube docker-env)`

`mvn clean install -DskipTests`

Then do `kubectl create -f <FILE_NAME>` for each of the kubernetes descriptor files.

If you also want distributed logging then after starting minikube do

`minikube ssh`
`sudo sysctl -w vm.max_map_count=262144`

and deploy with `kubectl create -f logging/`

For distributed tracing do `kubectl create -f tracing/`

For logging you'll want as much ram as you can for for minikube. If you don't want to use it remove or change the SPRING_PROFILES_ACTIVE entries in the kub yml files to !kube. See also https://activiti.gitbooks.io/activiti-7-developers-guide/content/components/activiti-cloud-infra/logging.html

To tail pod logs do `kubectl logs -f --since=10000s <PODNAME>`

## What you'll see

In the logs for the ranking connector you'll see the tweets that are being matched to the campaign's topic. You'll also see the top-ranked authors based on tweets posted in the last period.

To access kibana go to your minikube ip and port 30339 - user elastic and pass changeme.

For zipkin go to your minikube ip and port 30335

If you look at the longest recent spans in zipkin you'll likely find matched posts, or search in kibana (under Discover) for 'sentiment score' using the default kibana index.

To see memory usage and cpu usage do `open http://$(minikube ip):4194` or `docker stats $(docker ps --format={{.Names}})`