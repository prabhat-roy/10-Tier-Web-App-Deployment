def gv_script
pipeline {
    agent { label 'Jenkins-Agent' }
    environment {
        NEXUS_IP = "10.0.1.9"	
        K8S_MASTER_IP ="10.0.1.6"        
        nexus_cred = "nexus"
	    NEXUS_IMAGE_URL = "${NEXUS_IP}:8082"
        PRODUCT_IMAGE_NAME = "product-catalogue"
        SHOPFRONT_IMAGE_NAME = "shopfront"
        STOCKMANAGER_IMAGE_NAME = "stock-manager"
        DOCKERHUB_NAME = "prabhatrkumaroy"
		GITHUB_URL = "https://github.com/prabhat-roy/10-Tier-Web-App-Deployment.git"
    }
    tools {
        jdk 'Java'
        maven 'Maven'
    }
    stages {
        stage("Init") {
            steps {
                script {
                    gv_script = load"script.groovy"
                }
            }
        }
        stage("Cleanup Workspace") {
            steps {
                script {
                    gv_script.cleanup()
                }
            }
        }
        stage("Checkout from Git Repo") {
            steps {
                script {
                    gv_script.checkout()
                }
            }
        }
        stage("OWASP FS Scan") {
            steps {
                script {
                    gv_script.owasp()
                }
            }
        }
        
        stage("Trivy FS Scan") {
            steps {
                script {
                    gv_script.trivyfs()
                }
            }
        }
    }
    post {
        always {
            sh "docker logout"
            deleteDir()
        }
    }
}
