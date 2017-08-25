# JS client to check Activiti cloud setup

This demo just shows how to access Activiti 7 Cloud endpoints from a web client.

## How to run

First follow the steps to start the services using docker-compose (and have activiti-cloud-sso-idm hostname mapped in your hosts file).

This demo is tested using the indexzero http-server. First install npm then go this this directory and do:

npm install -g http-server

Then to start:

http-server -p 3000

Then open a new browser window and go to http://localhost:3000/#/main  (use incognito if you've had previous session)

Log in as testuser/password

## Notes

Routes will take time to register with the gateway. If a run-time bundle of the expected name is not deployed then certain calls will fail. The URLs can be adjusted to call different run-time bundles.

This app runs outside the gateway and also uses keycloak so it therefore shows that the gateway is enabled for CORS. See the gateway project for how to configure CORS using keycloak.cors properties (which are exposed as env variables for docker).