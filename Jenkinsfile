pipeline {
    agent any

    options {
        timestamps()
        timeout(time: 20, unit: 'MINUTES')
    }

    environment {
        APP_ENV = 'qa'
        QA_BASE_URL = 'https://rahulshettyacademy.com/client'
        BROWSER = 'chrome'
        HEADLESS = 'true'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Purchase UI tests') {
            steps {
                withCredentials([
                    string(credentialsId: 'test-user-email', variable: 'TEST_USER_EMAIL'),
                    string(credentialsId: 'test-user-password', variable: 'TEST_USER_PASSWORD')
                ]) {
                    script {
                        if (isUnix()) {
                            sh 'mvn --batch-mode --no-transfer-progress -P Purchase test'
                        } else {
                            bat 'mvn --batch-mode --no-transfer-progress -P Purchase test'
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/TEST-*.xml'
        }
    }
}
