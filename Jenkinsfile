node {
  env.JAVA_HOME="${tool 'jdk-oracle-8'}"
  env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
  try {
    stage('Clone'){
      checkout scm
    }
    
    withMaven(maven: 'M3', jdk: 'jdk-oracle-8', options: [artifactsPublisher(disabled: true), junitPublisher(disabled: false)] ) {
        stage('Compile & Bootstrap') {
          sh "mvn -Drascal.boot=--validating -Drascal.boot.memory=4 clean compile"
        }

        stage('Generate Tutor') {
          sh "mvn -Drascal.courses=--buildCourses -Drascal.boot.memory=4  compile"
        }

        stage('Run Tests') {
          sh "mvn -Drascal.test.memory=4 test"
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
    
    if (currentBuild.previousBuild.result == "FAILURE") { 
      slackSend (color: '#5cb85c', message: "BUILD BACK TO NORMAL: <${env.BUILD_URL}|${env.JOB_NAME} [${env.BUILD_NUMBER}]>")
    }
  } catch (e) {
    slackSend (color: '#d9534f', message: "FAILED: <${env.BUILD_URL}|${env.JOB_NAME} [${env.BUILD_NUMBER}]>")
    throw e
  }
}
