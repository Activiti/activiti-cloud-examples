# JS client to check Activiti cloud setup

This demo just shows how to access Activiti 7 Cloud endpoints from a web client.

## How to run

First run the docker-compose to start the services and have activiti-cloud-keycloak hostname mapped in your hosts file.

This demo is tested using the indexzero http-server. First install npm then go this this directory and do:

npm install -g http-server

Then to start:

http-server -p 3000

Then open a new browser window and go to http://localhost:3000/#/main  (use incognito if you've had previous session)

Log in as testuser/password