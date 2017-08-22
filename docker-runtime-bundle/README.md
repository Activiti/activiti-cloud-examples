# Docker Runtime Bundle

The dockerfile builds an image by extending a standard runtime bundle image.

The standard image looks in a lib directory for jars by using spring boot's loader.path. This is used to add a postgres driver jar, which is downloaded from the web and placed in the lib directory during build. Alternatively you could out a jar in a lib directory along with the Dockerfile and include a 'COPY /lib/ /lib/' command in the Dockerfile analogous to how the processes are copied.

It is also possible to include a properties file at this level (which can override properties from the file in the base image) by creating a /config/ directory containing an application.properties and copying this in the docker build, as spring boot will scan that location - see http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-application-property-files . This may be more convenient than setting config values in environment variables (using the variable names set with ${} in the base image application.properties) but only allows values to be set at build time, whereas env variables can be set in the docker run command.