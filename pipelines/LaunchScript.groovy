pipeline {
  agent {
    label 'balancer'
  }
  stages {
    stage('Checkout') {
      steps {
        checkout([$class: 'GitSCM', branches: [
          [name: '*/QA_Performance']
        ], extensions: [], userRemoteConfigs: [
          [credentialsId: 'NewUser1234', url: 'https://NewUser1234@bitbucket.org/db_ecommerce/commerce-cloud.git']
        ]])
        sh "ls -lart ./*"
      }
    }
    stage('ExecuteTest') {
      steps {
        dir("${WORKSPACE}/QA/Jmeter") {
          sh 'jmeter -n -t Master.jmx -l results.jtl'
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