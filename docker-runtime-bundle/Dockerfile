# This uses the generic Activiti Cloud Runtime Bundle
#  to create a domain specific image using the process definitions
#  located under the /processes/ directory
FROM activiti/activiti-cloud-runtime-bundle:latest
COPY /processes/ /processes/

#download postgres driver and put in lib dir
COPY download_assets.sh /root/

RUN ["chmod", "+x", "/root/download_assets.sh"]
RUN apk --no-cache add openssl

RUN /root/download_assets.sh && \
    rm /root/download_assets.sh

CMD java -jar maven/*.jar