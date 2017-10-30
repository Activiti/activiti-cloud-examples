FROM hyness/spring-cloud-config-server
MAINTAINER https://gitter.im/Activiti/Activiti7

HEALTHCHECK --interval=10s --timeout=3s CMD curl -f http://localhost:8888/health || exit 1

EXPOSE 8888