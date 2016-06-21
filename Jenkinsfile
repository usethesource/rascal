node {
  stage 'Clone'
  checkout scm

  stage 'Build and Test'
  def mvnHome = tool 'M3'
  env.JAVA_HOME="${tool 'jdk-oracle-8'}"
  env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
  sh "${mvnHome}/bin/mvn -s /var/jenkins_home/usethesource-maven-settings.xml -B  clean install"

  stage 'Archive'
  step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
  step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
}
