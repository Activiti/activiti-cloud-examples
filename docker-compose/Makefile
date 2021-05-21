SHELL := /bin/bash
VERSION := 1.0.1
# If you need to put your Makefile in a different directory to your compose 
# file, you will need to edit the variable below
DOCKER_FILE := docker-compose.yml
COMPOSE := docker-compose
SERVICE_COMMANDS := pull build down kill rm ps stop events unpause restart port pause create
COMPOSE_COMMANDS := config version $(SERVICE_COMMANDS)
SERVICES := $(shell $(COMPOSE) config --services)
include .env

.PHONY: $(SERVICES)

help: ## This info
	@echo '_________________'
	@echo '| Make targets: |'
	@echo '-----------------'
	@cat Makefile | grep -E '^[a-zA-Z\/_-]+:.*?## .*$$' | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
	@echo 

all: up ## Brings up all services, see docker-compose.yml

infra: nginx keycloak ## Brings up infrastructure services
	@echo
	@echo
	@echo Keycloak path  $(DOCKER_IP)/auth
	@echo
infra/stop: ## Stops infrastructure services
	make nginx/stop keycloak/stop 

application: infra rabbitmq ## Brings up Activiti application services
	make example-runtime-bundle example-cloud-connector activiti-cloud-query
	@echo Runtime bundle entry point: http://$(DOCKER_IP)/rb

application/stop: ## Stops up Activiti application services
	make rabbitmq/stop
	make example-runtime-bundle/stop example-cloud-connector/stop activiti-cloud-query/stop

modeler: infra ## Brings up Modeler services, see docker-compose.yml
	make activiti-cloud-modeling activiti-cloud-modeling-backend 
	@echo
	@echo
	@echo Modeling app: http://$(DOCKER_IP)/modeling
	@echo	
modeler/stop:  ## Stop Modeler services
	make activiti-cloud-modeling/stop activiti-cloud-modeling-backend/stop

up: ## Bring system up, see docker-compose.yml
	@echo
	@echo
	@echo Keycloak path:  http://$(DOCKER_IP)/auth
	@echo Modeling app: http://$(DOCKER_IP)/modeling
	@echo Runtime bundle entry point: http://$(DOCKER_IP)/rb
	@echo	
	$(COMPOSE) up -d

logs: ## Follow all container logs in terminal
	$(COMPOSE) logs -f

clean: ## Clean Docker System Temporary Files
	docker system prune -foreach

reboot: ## Reboot All Services
	make down up

$(SERVICES):
	make $@/up

$(COMPOSE_COMMANDS):
	$(COMPOSE) "$@"

$(foreach service,$(SERVICES),$(service)/ssh): ## Start a bash shell in the given container
	$(COMPOSE) exec $(word 1, $(subst /, ,$@)) bash

$(foreach service,$(SERVICES),$(service)/reboot): ## Stop, remove then up a container
	$(eval TASK := $(subst /, ,$@))
	make $(word 1, $(TASK))/stop $(word 1, $(TASK))/rm $(word 1, $(TASK))/up


$(foreach service,$(SERVICES),$(service)/up): ## Start a container in the background
	$(COMPOSE) up -d $(word 1, $(subst /, ,$@))

$(foreach service,$(SERVICES),$(service)/logs): ## Attach to the container's log output
	$(COMPOSE) logs -f $(word 1, $(subst /, ,$@))

$(foreach service,$(SERVICES),$(foreach command,$(SERVICE_COMMANDS),$(service)/$(command))):
	$(eval TASK := $(subst /, ,$@))
	$(COMPOSE) $(word 2, $(TASK)) $(word 1, $(TASK))
