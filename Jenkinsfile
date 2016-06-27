node {
  def mvnHome = tool 'M3'
  env.JAVA_HOME="${tool 'jdk-oracle-8'}"
  env.PATH="${env.JAVA_HOME}/bin:${mvnHome}/bin:${env.PATH}"

  stage 'Clone'
  checkout scm

  stage 'Build'
  sh "mvn -DskipTest -B clean compile"

  stage 'Test'
  sh "mvn -B test"

  stage 'Packaging'
  sh "mvn -DskipTest -B package"

  stage 'Deploy'
  sh "mvn -s ${env.HOME}/usethesource-maven-settings.xml -DskipTests -B deploy"

  stage 'Archive'
  step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
  step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
}
