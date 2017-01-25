FROM tomcat:6.0.48-jre7
LABEL maintainer "Rafael T. C. Soares <rafaelcba@gmail.com>"
RUN echo "copy config files"
COPY tomcat6/conf/ /usr/local/tomcat/conf/
RUN echo "copy 3rd party shared libs"
COPY tomcat6/lib/ /usr/local/tomcat/lib/
RUN echo "copy app WAR file from nexus releases public repository"
RUN wget -O /tmp/samplewebapp.war http://nexus-cicd.cloud.rramalho.com/content/repositories/releases/com/openshift/samples/samplewebapp/1.0/samplewebapp-1.0.war && \
    mv /tmp/samplewebapp.war /usr/local/tomcat/webapps/
RUN echo "create a non-root tomcat user and change base image permissions for execution on Openshift enviroment..."
RUN groupadd -g 1001 -r tomcat
RUN useradd -c "Apache Tomcat user admin" -u 1001 -g tomcat -s /sbin/nologin -r -d /usr/local/tomcat/ tomcat
RUN chown -R tomcat:tomcat /usr/local/tomcat
USER tomcat
