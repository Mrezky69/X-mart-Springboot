pipeline {
    agent any
    tools {
        maven 'jenkins-maven'
    }

    environment {
        BUILD_NUMBER_ENV = "${env.BUILD_NUMBER}"
        TEXT_SUCCESS_BUILD = "[#${env.BUILD_NUMBER}] Project Name : ${JOB_NAME} is Success"
        TEXT_FAILURE_BUILD = "[#${env.BUILD_NUMBER}] Project Name : ${JOB_NAME} is Failure"
    }

    stages {
        stage('Git Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Mrezky69/X-mart-Springboot']])
                bat 'mvn clean install'
                echo 'Git Checkout Completed'
            }
        }

        stage('Build and SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('SonarQube') {
                        bat 'mvn clean verify package -Dmaven.plugin.validation=brief sonar:sonar -Dsonar.projectKey=X-marts -Dsonar.projectName="X-marts" -Dsonar.host.url=http://localhost:9000'
                        echo 'SonarQube Analysis Completed'
                    }
                }
            }
        }

        stage("Quality Gate") {
            steps {
                waitForQualityGate abortPipeline: true
                echo 'Quality Gate Completed'
            }
        }

    }
    post {
        success {
            script{
                 withCredentials([string(credentialsId: 'telebot-token', variable: 'TOKEN'), string(credentialsId: 'telegram-chat-id', variable: 'CHAT_ID')]) {
                    bat ''' curl -s -X POST https://api.telegram.org/bot"%TOKEN%"/sendMessage -d chat_id="%CHAT_ID%" -d text="%TEXT_SUCCESS_BUILD%" '''
                 }
            }
        }
        failure {
            script{
                withCredentials([string(credentialsId: 'telebot-token', variable: 'TOKEN'), string(credentialsId: 'telegram-chat-id', variable: 'CHAT_ID')]) {
                    bat ''' curl -s -X POST https://api.telegram.org/bot"%TOKEN%"/sendMessage -d chat_id="%CHAT_ID%" -d text="%TEXT_FAILURE_BUILD%" '''
                }
            }
        }
    }
}