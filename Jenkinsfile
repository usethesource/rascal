node {
  def mvnHome = tool 'M3'
  env.JAVA_HOME="${tool 'jdk-oracle-8'}"
  env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"

  stage 'Clone'
  checkout scm

  stage 'Build and Test'
  sh "${mvnHome}/bin/mvn -B clean install"

  stage 'Deploy'
  sh "${mvnHome}/bin/mvn -s /var/jenkins_home/usethesource-maven-settings.xml -DskipTests -B deploy"

  stage 'Archive'
  step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
  step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
}
