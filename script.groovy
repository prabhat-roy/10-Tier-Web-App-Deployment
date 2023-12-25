def cleanup() {
        cleanWs()
}

def checkout() {
        git branch: 'main', credentialsId: 'github', url: "$GITHUB_URL"
}

def sonarqube() {
        withSonarQubeEnv('SonarQube') {
                sh "$SCANNER_HOME/bin/sonar-scanner -Dsonar.projectKey=10-Tier -Dsonar.projectName=10Tier -Dsonar.java.binaries=."
        }
}
def owasp() {
    dependencyCheck additionalArguments: '--scan ./ --disableYarnAudit --disableNodeAudit', odcInstallation: 'DP-check'
    dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
}

def trivyfs() {
        sh "trivy fs ."
}

def dockerbuild() {
        dir('/root/workspace/10-tier-app/src/adservice') {
                sh "docker build . -t ${NEXUS_IMAGE_URL}/${AD_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/root/workspace/10-tier-app/src/cartservice/src') {
                sh "docker build . -t ${NEXUS_IMAGE_URL}/${CART_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/root/workspace/10-tier-app/src/currencyservice') {
                sh "docker build . -t ${NEXUS_IMAGE_URL}/${CURRENCY_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/root/workspace/10-tier-app/src/emailservice') {
                sh "docker build . -t ${NEXUS_IMAGE_URL}/${EMAIL_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/root/workspace/10-tier-app/src/paymentservice') {
                sh "docker build . -t ${NEXUS_IMAGE_URL}/${PAYMENT_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/root/workspace/10-tier-app/src/productcatalogservice') {
                sh "docker build . -t ${NEXUS_IMAGE_URL}/${PRODUCT_CATALOG_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/root/workspace/10-tier-app/src/recommendationservice') {
                sh "docker build . -t ${NEXUS_IMAGE_URL}/${RECOMMENDATION_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/root/workspace/10-tier-app/src/shippingservice') {
                sh "docker build . -t ${NEXUS_IMAGE_URL}/${SHIPPING_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/root/workspace/10-tier-app/src/frontend') {
                sh "docker build . -t ${NEXUS_IMAGE_URL}/${FRONTEND}:${BUILD_NUMBER}"
        }
        dir('/root/workspace/10-tier-app/src/loadgenerator') {
                sh "docker build . -t ${NEXUS_IMAGE_URL}/${LOAD_GENERATOR}:${BUILD_NUMBER}"
        }
}

return this