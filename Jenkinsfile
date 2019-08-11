pipeline {
  agent any
  stages {
    stage('Git') {
      steps {
        git(url: 'https://github.com/wlanboy/KafkaHazelcastWorker.git', branch: 'master')
      }
    }
    stage('Build DTO') {
      steps {
        echo 'HazelcastWorkDto'
        sh 'mvn -f HazelcastWorkDto/pom.xml clean package install'
      }
    }
    stage('Build Worker') {
      steps {
        echo 'HazelcastWorker'
        sh 'mvn -f HazelcastWorker/pom.xml clean package'
      }
    }
    stage('Build Source') {
      steps {
        echo 'KafkaSource'
        sh 'mvn -f KafkaSource/pom.xml clean package'
      }
    }
    stage('Build Sink') {
      steps {
        echo 'KafkaSink'
        sh 'mvn -f KafkaSink/pom.xml clean package'
      }
    }           
  }
}