pipeline {
    agent any

    parameters {
        choice(
            name: 'APP_ENV',
            choices: ['qa', 'dev'],
            description: 'Target application environment'
        )
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'edge', 'firefox'],
            description: 'Browser installed on the Jenkins agent'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run the browser without a visible window'
        )
        choice(
            name: 'TEST_SUITE',
            choices: ['Purchase', 'Regression', 'ErrorValidationTest', 'CucumberTests'],
            description: 'Maven test profile to run'
        )
    }

    options {
        timestamps()
        timeout(time: 20, unit: 'MINUTES')
    }

    environment {
        DEV_BASE_URL = 'https://rahulshettyacademy.com/client'
        QA_BASE_URL = 'https://rahulshettyacademy.com/client'
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
                            sh 'mvn --batch-mode --no-transfer-progress -P "$TEST_SUITE" test'
                        } else {
                            bat 'mvn --batch-mode --no-transfer-progress -P %TEST_SUITE% test'
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
