# Config Server Example

If you want to use the spring cloud config server (which is optional) this provides a basic example.

Build config server by going to this directory and doing 'docker build . -t activiti/activiti-cloud-config-server:latest'

You can start using docker-compose -f infrastructure-with-conf-server-docker.yml up

This starts a config server pointed at the git repo in the value of SPRING_CLOUD_CONFIG_SERVER_GIT_URI. Go there to see the files.

We can verify that it's running by querying for a file e.g. curl http://localhost:8888/query/master

Start this before the infrastructure to see the infrastructure apps to pick up the config.