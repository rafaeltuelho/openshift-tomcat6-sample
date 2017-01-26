def appName="tomcat6-webapp"
def project=""
node {
  stage("Initialize") {
    project = env.PROJECT_NAME
  }
}
node("maven") {
  stage("Checkout src code from GOGs") {
    git url: "http://gogs-cicd.cloud.rramalho.com/gogs/openshift-tomcat6-sample.git", branch: "master"
  }
  stage("Build WAR") {
    sh "mvn clean package deploy -Popenshift-tomcat6 -s openshift/maven/settings.xml"
  }
}
node {
  stage("Build Image") {
    sh "oc whoami"
    sh "oc project"
    sh "oc get bc"
    //sh "oc start-build ${appName}-docker --follow -n ${project}"
    openshiftBuild buildConfig: ${appName}-docker, namespace: project, showBuildLogs: 'true'
  }
  stage("Deploy") {
    sh "oc get bc"
    openshiftDeploy deploymentConfig: appName, namespace: project
  }
}
