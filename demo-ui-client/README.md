# ADF/APS JavaScript Application with Angular CLI to check Activiti cloud setup

This demo just shows how to access Activiti 7 Cloud endpoints from a web client.

It is build from a minimal ready-to-use Angular CLI project template pre-configured with ADF 2.0.0 components.

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 1.5.0

## Quick start

First follow the steps to start the services using docker-compose (and have activiti-cloud-sso-idm hostname mapped in your hosts file).

Alternatively use the steps in the kubernetes folder to start using minikube. If doing so then change gatewayUrl in main-controller and auth server url in keycloak.json (see kubernetes README).


```sh
npm install
npm start
```

Then open a new browser window and go to http://localhost:3000/#/main  (use incognito if you've had previous session)

Log in as testuser/password

## Notes

Routes will take time to register with the gateway. If a run-time bundle of the expected name is not deployed then certain calls will fail. The URLs can be adjusted to call different run-time bundles.

This app runs outside the gateway and also uses keycloak so it therefore shows that the gateway is enabled for CORS. See the gateway project for how to configure CORS using keycloak.cors properties (which are exposed as env variables for docker).

## Supported ADF component libraries

This project has all the existing ADF component libraries already pre-configured.

The main focus of the project is:

- ADF integration and setup
- Basic demonstration of working components

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

### Proxy settings

The template provides certain proxy settings to allow running web application locally without CORS setup.
You can find details in the `proxy.conf.json` file.

List of URLs being proxied:

- `/activiti-app` -> `http://0.0.0.0:9999`

## Code scaffolding

Run `ng generate component component-name -m app.module` to generate a new component. You can also use `ng generate directive|pipe|service|class|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `-prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).
Before running the tests make sure you are serving the app via `ng serve`.

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
