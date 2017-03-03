FROM tomcat:6.0.48-jre7
LABEL maintainer "Rafael T. C. Soares <rafaelcba@gmail.com>"
RUN echo "copy config files"
COPY tomcat6/conf/ $CATALINA_HOME/conf/
RUN echo "copy 3rd party shared libs"
COPY tomcat6/lib/ $CATALINA_HOME/lib/
RUN echo "copy app WAR file from nexus releases public repository"
#RUN wget -O /tmp/samplewebapp.war http://nexus-cicd.cloud.rramalho.com/content/repositories/releases/com/openshift/samples/samplewebapp/1.0/samplewebapp-1.0.war && \
RUN wget -O /tmp/samplewebapp.war ${MAVEN_MIRROR_URL}/content/repositories/releases/${projectGroupId}/${projectArtifactFinalName}/${projectArtifactReleaseVersion}/${projectArtifactFinalName}-${projectArtifactReleaseVersion}.war && \
    mv /tmp/samplewebapp.war $CATALINA_HOME/webapps/
RUN echo "create a non-root tomcat user and change base image permissions for execution on Openshift enviroment..."
RUN groupadd -g 1001 -r tomcat
RUN useradd -c "Apache Tomcat user admin" -u 1001 -g tomcat -s /sbin/nologin -r -d $CATALINA_HOME/ tomcat
RUN chown -R tomcat:tomcat $CATALINA_HOME
# ensure the Arbitrary User used by openshift will be able to execute the process.
# see "Support Arbitrary User IDs" on https://docs.openshift.com/container-platform/3.4/creating_images/guidelines.html#openshift-container-platform-specific-guidelines
RUN echo "ensure the Arbitrary User used by openshift will be able to execute the process...."
RUN chgrp -R 0 $CATALINA_HOME
RUN chmod -R g+rw $CATALINA_HOME
RUN find $CATALINA_HOME -type d -exec chmod g+x {} +
USER tomcat
