pipeline {
  agent {
    label 'master'
  }
  stages {
    stage('Checkout') {
      steps {
        checkout([$class: 'GitSCM', branches: [
          [name: '*/master']
        ], extensions: [], userRemoteConfigs: [
          [credentialsId: 'sectester123', url: 'https://github.com/sectester123/app-for-Jenkins.git']
        ]])
        bat "ls"
      }
    }
    stage('ExecuteTest') {
      steps {
        dir("${WORKSPACE}/scripts") {
          bat 'jmeter -n -t jmeter-left.jmx -l results.jtl'
        }
post {
   perfReport filterRegex: '', showTrendGraphs: true, sourceDataFiles: 'results.jtl'
    }
post {
  success {
   perfReport filterRegex: '', showTrendGraphs: true, sourceDataFiles: 'results.jtl' 
  }
}
      }
    }
  }
}