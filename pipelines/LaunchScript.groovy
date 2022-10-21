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
        bat "dir"
      }
    }
    stage('ExecuteTest') {
      steps {
        dir("${WORKSPACE}/scripts") {
          bat 'jmeter -n -t jmeter-left.jmx -l results.jtl'
        }
    stage('AnalyzeResults') {
      steps {
        perfReport filterRegex: '', showTrendGraphs: true, sourceDataFiles: 'results.jtl' 
  }
}
      }
    }
  }
}