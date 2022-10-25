pipeline {
  agent {
    label 'master'
  }
  triggers {
        cron('H H * * *')
    }
  stages {
    stage('Checkout') {
      steps {
        checkout([$class: 'GitSCM', branches: [
          [name: '*/master']
        ], extensions: [], userRemoteConfigs: [
          [credentialsId: 'sectester123', url: 'https://github.com/sectester123/app-for-Jenkins.git']
        ]])
      }
    }
    stage('ExecuteTest') {
      steps {
        dir("${WORKSPACE}/scripts") {
          bat 'jmeter -n -t jmeter-left.jmx -l results.jtl '
        }
            perfReport 'scripts/results.jtl'
        }
     }
    }
}