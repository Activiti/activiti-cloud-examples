# This uses the generic Activiti Cloud Runtime Bundle
#  to create a domain specific image using the process definitions
#  located under the /processes/ directory
FROM activiti/activiti-cloud-runtime-bundle:latest
COPY /processes/ /processes/
CMD java -jar maven/*.jar