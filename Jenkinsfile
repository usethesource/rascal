node {
  stage 'Clone'
  checkout scm

  stage 'Build and Test'
  def mvnHome = tool 'M3'
  sh "${mvnHome}/bin/mvn -Dmaven.repo.local=/var/jenkins_home/repo -B  clean install"

  stage 'Archive'
  step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
  step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
}
