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
        sh 'cd HazelcastWorkDto'
        sh 'mvn clean package install'
      }
    }
    stage('Build Worker') {
      steps {
        sh 'cd HazelcastWorker'
        sh 'mvn clean package'
      }
    }
    stage('Build Source') {
      steps {
        sh 'cd KafkaSource'
        sh 'mvn clean package'
      }
    }
    stage('Build Sink') {
      steps {
        sh 'cd KafkaSink'
        sh 'mvn clean package'
      }
    }           
  }
}