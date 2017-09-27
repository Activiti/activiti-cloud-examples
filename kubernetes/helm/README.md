## Helm

### Installing services

A helm install will have a name - either allocated automatically or you can specify (using --name). To install without a name do:

helm install ./activitiinfrachart

from this directory. The output will give you a release name (in the 'NAME:' section).

Use this release name when installing activitirbchart by doing

helm install --set infra.release.name=namefrompreviousoutput ./activitirbchart

The runtime bundle will now be installed by helm as a further release but it will access services from the infrastructure release.

The activitirbchart is set up with an example rb - to use your own you'll need to specify that name as the value of the --image.repository parameter (and the image.tag parameter if not 'latest')

If you run the same command again you should get a second instance of the RB and zuul should loadbalance between them.

To remove a release do helm delete releasename . To see all releases do helm list --all

### Accessing services

We need a hosts entry to be able to access the auth server via the correct url. For this we need to know its external endpoint.

To find the external endpoint do ‘kubectl get services --output=wide’ (or by looking at Services in Kubernetes Dashboard and then External Endpoints) and find the activiti-cloud-sso-idm-kub external endpoint.

A ping should reveal the IP address (note do not specify http in the host) to go in the hosts file (the IP will appear in brackets after hostname).

So the host file entry to set should look like e.g.

52.20.102.24  trendsetting-grasshopper-activiti-cloud-sso-idm-kub

When using helm then the hostname to go in the hosts file is prefixed with the release name. This is only when using helm. This is because of the use of {{ .Release.Name }}- in the helm yaml file.

The external endpoint for the entry point (gateway) should be used for the gateway and other endpoints.

### Notes

The values.yaml file for a chart specify default values for parameters. The use of the parameter can be found in the .yml files in the templates directory for the chart. More parameters can be added by following this pattern. Note that some defaults may not be appropriate for production. For example, using tag 'latest' for infrastructure docker images takes the latest tag which might be a snapshot. See the tags of the docker images in https://hub.docker.com/u/activiti/dashboard/ to find the tag of the most recent release version.

To enable debug logging add a debug param e.g. helm install --set infra.release.name=wobbly-mouse --set rb.debug=true ./activitirbchart

For more using helm see https://github.com/kubernetes/helm/blob/master/docs/using_helm.md

For more on helm install command see  https://github.com/kubernetes/helm/blob/master/docs/helm/helm_install.md

For general introduction to helm charts see https://docs.helm.sh/chart_template_guide/#getting-started-with-a-chart-template