def gv_script
pipeline {
    agent { label 'Jenkins-Agent' }
    environment {
        NEXUS_IP = "10.0.1.9"	
        K8S_MASTER_IP ="10.0.1.6"
        GITHUB_URL = "https://github.com/prabhat-roy/10-Tier-Web-App-Deployment.git"        
        nexus_cred = "nexus"
	    NEXUS_IMAGE_URL = "${NEXUS_IP}:8082"
        SCANNER_HOME = tool "sonar-scanner"
        AD_SERVICE = "adservice"
        CART_SERVICE = "cartservice"
        CHECKOUT_SERVICE = "checkoutservice"
        CURRENCY_SERVICE = "currencyservice"
        EMAIL_SERVICE = "emailservice"
        PAYMENT_SERVICE = "paymentservice"
        PRODUCT_CATALOG_SERVICE = "productcatalogservice"
        RECOMMENDATION_SERVICE = "recommendationservice"
        SHIPPING_SERVICE = "shippingservice"
        FRONTEND = "frontend"
        LOAD_GENERATOR = "loadgenerator"
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
        stage("Trivy Image Scan") {
            steps {
                script {
                    gv_script.trivyimage()
                }
            }
        }
        stage("Grype Image Scan") {
            steps {
                script {
                    gv_script.grype()
                }
            }
        }
        stage("Syft Image Scan") {
            steps {
                script {
                    gv_script.syft()
                }
            }
        }
        stage("Docker Scout Image Scan") {
            steps {
                script {
                    gv_script.dockerscout()
                }
            }
        }
        stage("Docker Run Test") {
            steps {
                script {
                    gv_script.dockerrun()
                }
            }
        }
        stage("Docker Image upload To Nexus") {
            steps {
                script {
                    gv_script.dockernexus()
                }
            }
        }
        stage("Container Removal") {
            steps {
                script {
                    gv_script.removedocker()
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
