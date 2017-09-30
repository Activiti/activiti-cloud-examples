#!/bin/bash

kubectl=$(which kubectl)

export VERSION=v1.6.3

$kubectl create -f https://raw.githubusercontent.com/kubernetes/kops/master/addons/kubernetes-dashboard/${VERSION}.yaml
