node {
  try {
    stage('Clone'){
      checkout scm
    }
    
    withMaven(maven: 'M3', options: [artifactsPublisher(disabled: true), junitPublisher(disabled: false)] ) {
        stage('Build') {
          sh "mvn -Drascal.courses=--buildCourses -Drascal.boot=--validating clean test"
          sh "curl https://codecov.io/bash | bash -s - -K -X gcov -t e8b4481a-d178-4148-a4ff-502906390512"
        }
        
        stage('Packaging') {
          sh "mvn -DskipTests package"
        }
        
        stage('Deploy') {
          if (env.BRANCH_NAME == "master" || env.BRANCH_NAME == "jenkins-deploy") {
            sh "mvn -DskipTests deploy"
          }
        }
    }
    
    stage('Archive') {
      step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
    }
    
    if (currentBuild.previousBuild.result == "FAILURE") { 
      slackSend (color: '#5cb85c', message: "BUILD BACK TO NORMAL: <${env.BUILD_URL}|${env.JOB_NAME} [${env.BUILD_NUMBER}]>")
    }
  } catch (e) {
    slackSend (color: '#d9534f', message: "FAILED: <${env.BUILD_URL}|${env.JOB_NAME} [${env.BUILD_NUMBER}]>")
    throw e
  }
}
