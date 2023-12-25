def gv_script
pipeline {
    agent { label 'Jenkins-Agent' }
    environment {
        SCANNER_HOME = tool "sonar-scanner"
        NEXUS_IP = "10.0.1.9"	
        K8S_MASTER_IP ="10.0.1.6"        
        nexus_cred = "nexus"
	    NEXUS_IMAGE_URL = "${NEXUS_IP}:8082"
        AD_SERVICE = "adservice"
        CART_SERVICE = "cartservice"
        CHECKOUT_SERVICE = "checkoutservice"
        CURRENCY_SERVICE = "currencyservice"
        EMAIL-SERVICE = "emailservice"
        PAYMENT_SERVICE = "paymentservice"
        PRODUCT_CATALOG_SERVICE = "productcatalogservice"
        RECOMMENDATION_SERVICE = "recommendationservice"
        SHIPPING_SERVICE = "shippingservice"
        FRONTEND = "frontend"
        LOAD_GENERATOR = "loadgenerator"
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
        stage("SonarQube Scan") {
            steps {
                script {
                    gv_script.sonarqube()
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
        stage("Docker build") {
            steps {
                script {
                    gv_script.dockerbuild()
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
