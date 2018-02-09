## Activiti on Kubernetes on AWS

First you need to have an AWS cluster to be able to use kops. See for example https://github.com/kubernetes/kops/blob/master/docs/aws.md to set this up or the setup notes below.

Once you have followed kops setup to get name, state store, access key etc. then you will be able to fill in the blank values in the scripts in this directory.

Run start_kube.sh

Wait. It can be a long wait. You’ll know the cluster is running by doing:

kubectl get pods --all-namespaces

(See https://kubernetes.io/docs/user-guide/kubectl-cheatsheet/)

When it is you’ll want a dashboard. Run add-dashboard.sh

To get to the dashboard you can go to the below (where NAME is the value of the variable from start_kube.sh):

https://api.NAME/api/v1/namespaces/kube-system/services/kubernetes-dashboard/proxy/#!/overview?namespace=default

To access this you’ll need credentials, which are in:

cat ~/.kube/config

Then you can carry on with kubectl or helm README instructions

### Getting Started with Kubernetes on AWS

Another good way to get started with setting up Kubernetes on AWS is the [HeptIO AWS Quick Start](https://aws.amazon.com/quickstart/architecture/heptio-kubernetes/) to setup Kubernetes on AWS.

You can use their [deployment guide](https://s3.amazonaws.com/quickstart-reference/heptio/latest/doc/heptio-kubernetes-on-the-aws-cloud.pdf) to perform the following...

1 Launch the Quick Start within an existing VPC

2 Choose Weave for Pod networking

Once setup, you can install [Helm on AWS](http://docs.heptio.com/content/tutorials/aws-qs-helm-wordpress.html#initialize-helm-and-tiller) and implement the [RBAC fix](http://docs.heptio.com/content/tutorials/aws-qs-helm-wordpress.html#implement-tiller-rbac-fix).

See also https://github.com/Alfresco/alfresco-anaxes-shipyard/blob/master/docs/running-a-cluster.md