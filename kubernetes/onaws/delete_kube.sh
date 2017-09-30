#!/bin/bash

kops=$(which kops)

export AWS_ACCESS_KEY_ID=
export AWS_SECRET_ACCESS_KEY=

export NAME=
export KOPS_STATE_STORE=

$kops delete cluster --name $NAME --yes
