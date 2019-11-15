pipeline {
    agent any
    environment {
        repo_path = '$(basename $PWD)'
    }
    stages {
        stage('sync source code') {
            when{ branch 'master' }
            steps {
                sh "rsync -rva ../${repo_path} ubuntu@10.20.1.111:/home/ubuntu/"
            }
        }
        stage('build') {
            when { branch 'master' }
            steps {
                sh "ssh ubuntu@10.20.1.111 'cd ~/'knowably_master' ; mvn clean package -DskipTests'"
                sh "ssh ubuntu@10.20.1.111 'cd ~/'knowably_master'/frontend ; npm install'"
                sh "ssh ubuntu@10.20.1.111 'cd ~/'knowably_master'/frontend ; ng build'"
            }
        }
        stage('Deploy') {
            when { branch 'master' }
            steps {
                sh "ssh ubuntu@10.20.1.160 'cd ~/'cookify_master' ; docker-compose up --build -d config-server eureka-server'"
                sh "ssh ubuntu@10.20.1.160 'cd ~/'cookify_master' ; docker-compose up --build -d --no-recreate'"            }
        }
        stage('Deployment status') {
            when { branch 'master' }
            steps {
                sh "ssh ubuntu@10.20.1.111 'cd ~/'knowably_master' ; sleep 30 ; docker ps'"
            }
        }
    }
}
