#!/usr/bin/env groovy

pipeline {

    agent any
    
    stages {
        
        stage('Build') {

            tools {
            
                jdk "jdk-16.0.1+9"
            }
            
            steps {
                
                withCredentials([
                    file(credentialsId: 'mod_build_secrets', variable: 'ORG_GRADLE_PROJECT_secretFile'),
                    file(credentialsId: 'java_keystore', variable: 'ORG_GRADLE_PROJECT_keyStore')
                ]) {
            
                    echo 'Building project.'
                    sh 'chmod +x gradlew'
                    sh './gradlew clean build publish curseforge --stacktrace --warn'
                }
            }
        }
    }
}