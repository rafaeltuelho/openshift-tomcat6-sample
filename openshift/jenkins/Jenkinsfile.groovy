def appName="tomcat6-webapp"
def buildConfigName="${appName}-docker"
def project=""
def projectGroupId=""
def projectArtifactFinalName=""
def projectArtifactReleaseVersion=""

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
    echo " =========== ^^^^^^^^^^^^ Reading pom.xml "
    pom = readMavenPom file: 'pom.xml'
    projectGroupId = pom.groupId
    projectArtifactFinalName = pom.artifactId
    projectArtifactReleaseVersion = pom.version
    echo " projectGroupId: ${projectGroupId}"
    echo " projectArtifactFinalName: ${projectArtifactFinalName}"
    echo " projectArtifactReleaseVersion: ${projectArtifactReleaseVersion}"

    sh "mvn clean package deploy -Popenshift-tomcat6,openshift-nexus -s openshift/maven/settings.xml"
  }
}
node {
  stage("Build Image") {
    //sh "oc whoami"
    //sh "oc project"
    //sh "oc get bc"
    openshiftBuild bldCfg: buildConfigName, namespace: project, showBuildLogs: 'true',
      env : [
        [ name : 'projectGroupId', value : projectGroupId.replace('.', '/') ],
        [ name : 'projectArtifactFinalName', value : projectArtifactFinalName ],
        [ name : 'projectArtifactReleaseVersion', value : projectArtifactReleaseVersion ]
      ]
  }
  stage("Deploy") {
    //sh "oc get bc"
    openshiftDeploy deploymentConfig: appName, namespace: project
  }
}
