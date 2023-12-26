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
        dir('/home/jenkins/workspace/10-tier-app/src/adservice') {
                sh "docker build . -t ${AD_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/home/jenkins/workspace/10-tier-app/src/cartservice/src') {
                sh "docker build . -t ${CART_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/home/jenkins/workspace/10-tier-app/src/currencyservice') {
                sh "docker build . -t ${CURRENCY_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/home/jenkins/workspace/10-tier-app/src/emailservice') {
                sh "docker build . -t ${EMAIL_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/home/jenkins/workspace/10-tier-app/src/paymentservice') {
                sh "docker build . -t ${PAYMENT_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/home/jenkins/workspace/10-tier-app/src/productcatalogservice') {
                sh "docker build . -t ${PRODUCT_CATALOG_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/home/jenkins/workspace/10-tier-app/src/recommendationservice') {
                sh "docker build . -t ${RECOMMENDATION_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/home/jenkins/workspace/10-tier-app/src/shippingservice') {
                sh "docker build . -t ${SHIPPING_SERVICE}:${BUILD_NUMBER}"
        }
        dir('/home/jenkins/workspace/10-tier-app/src/frontend') {
                sh "docker build . -t ${FRONTEND}:${BUILD_NUMBER}"
        }
        dir('/home/jenkins/workspace/10-tier-app/src/loadgenerator') {
                sh "docker build . -t ${LOAD_GENERATOR}:${BUILD_NUMBER}"
        }
}

def trivyimage() {
        sh '''
        trivy image ${AD_SERVICE}:${BUILD_NUMBER}
        trivy image ${CART_SERVICE}:${BUILD_NUMBER}
        trivy image ${CURRENCY_SERVICE}:${BUILD_NUMBER}
        trivy image ${EMAIL_SERVICE}:${BUILD_NUMBER}
        trivy image ${PAYMENT_SERVICE}:${BUILD_NUMBER}
        trivy image ${PRODUCT_CATALOG_SERVICE}:${BUILD_NUMBER}
        trivy image ${RECOMMENDATION_SERVICE}:${BUILD_NUMBER}
        trivy image ${SHIPPING_SERVICE}:${BUILD_NUMBER}
        trivy image ${FRONTEND}:${BUILD_NUMBER}
        trivy image ${LOAD_GENERATOR}:${BUILD_NUMBER}
        '''
}

def grype() {
        sh '''
                grype ${AD_SERVICE}:${BUILD_NUMBER}
                grype ${CART_SERVICE}:${BUILD_NUMBER}
                grype ${CURRENCY_SERVICE}:${BUILD_NUMBER}
                grype ${EMAIL_SERVICE}:${BUILD_NUMBER}
                grype ${PAYMENT_SERVICE}:${BUILD_NUMBER}
                grype ${PRODUCT_CATALOG_SERVICE}:${BUILD_NUMBER}
                grype ${RECOMMENDATION_SERVICE}:${BUILD_NUMBER}
                grype ${SHIPPING_SERVICE}:${BUILD_NUMBER}
                grype ${FRONTEND}:${BUILD_NUMBER}
                grype ${LOAD_GENERATOR}:${BUILD_NUMBER}
        '''
}

def syft() {
        sh '''
                syft ${AD_SERVICE}:${BUILD_NUMBER}
                syft ${CART_SERVICE}:${BUILD_NUMBER}
                syft ${CURRENCY_SERVICE}:${BUILD_NUMBER}
                syft ${EMAIL_SERVICE}:${BUILD_NUMBER}
                syft ${PAYMENT_SERVICE}:${BUILD_NUMBER}
                syft ${PRODUCT_CATALOG_SERVICE}:${BUILD_NUMBER}
                syft ${RECOMMENDATION_SERVICE}:${BUILD_NUMBER}
                syft ${SHIPPING_SERVICE}:${BUILD_NUMBER}
                syft ${FRONTEND}:${BUILD_NUMBER}
                syft ${LOAD_GENERATOR}:${BUILD_NUMBER}
        '''
}

def dockerscout() {
        withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
                sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
                sh "docker scout quickview ${AD_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout quickview ${CART_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout quickview ${CURRENCY_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout quickview ${EMAIL_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout quickview ${PAYMENT_SERVICE}:${BUILD_NUMBER}" 
                sh "docker scout quickview ${PRODUCT_CATALOG_SERVICE}:${BUILD_NUMBER}" 
                sh "docker scout quickview ${RECOMMENDATION_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout quickview ${SHIPPING_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout quickview ${FRONTEND}:${BUILD_NUMBER}"
                sh "docker scout quickview ${LOAD_GENERATOR}:${BUILD_NUMBER}"
                sh "docker scout cves ${AD_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout cves ${CART_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout cves ${CURRENCY_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout cves ${EMAIL_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout cves ${PAYMENT_SERVICE}:${BUILD_NUMBER}" 
                sh "docker scout cves ${PRODUCT_CATALOG_SERVICE}:${BUILD_NUMBER}" 
                sh "docker scout cves ${RECOMMENDATION_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout cves ${SHIPPING_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout cves ${FRONTEND}:${BUILD_NUMBER}"
                sh "docker scout cves ${LOAD_GENERATOR}:${BUILD_NUMBER}"
                sh "docker scout recommendations ${AD_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout recommendations ${CART_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout recommendations ${CURRENCY_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout recommendations ${EMAIL_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout recommendations ${PAYMENT_SERVICE}:${BUILD_NUMBER}" 
                sh "docker scout recommendations ${PRODUCT_CATALOG_SERVICE}:${BUILD_NUMBER}" 
                sh "docker scout recommendations ${RECOMMENDATION_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout recommendations ${SHIPPING_SERVICE}:${BUILD_NUMBER}"
                sh "docker scout recommendations ${FRONTEND}:${BUILD_NUMBER}"
                sh "docker scout recommendations ${LOAD_GENERATOR}:${BUILD_NUMBER}"
        }
}
def dockerrun() {
    sh '''
        docker run -dt  ${AD_SERVICE}:${BUILD_NUMBER}
        docker run -dt  ${CART_SERVICE}:${BUILD_NUMBER}
        docker run -dt  ${CURRENCY_SERVICE}:${BUILD_NUMBER}
        docker run -dt  ${EMAIL_SERVICE}:${BUILD_NUMBER}
        docker run -dt  ${PAYMENT_SERVICE}:${BUILD_NUMBER}
        docker run -dt  ${PRODUCT_CATALOG_SERVICE}:${BUILD_NUMBER}
        docker run -dt  ${RECOMMENDATION_SERVICE}:${BUILD_NUMBER}
        docker run -dt  ${SHIPPING_SERVICE}:${BUILD_NUMBER}
        docker run -dt  ${FRONTEND}:${BUILD_NUMBER}
        docker run -dt  ${LOAD_GENERATOR}:${BUILD_NUMBER}
        docker ps -aq | xargs docker stop
        '''
}

def dockernexus() {
        withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'nexusPassword', usernameVariable: 'nexusUser')]) {
                sh "docker image tag ${AD_SERVICE}:${BUILD_NUMBER} ${NEXUS_IMAGE_URL}/${AD_SERVICE}:${BUILD_NUMBER}"
                sh "docker image tag ${CART_SERVICE}:${BUILD_NUMBER} ${NEXUS_IMAGE_URL}/${CART_SERVICE}:${BUILD_NUMBER}"
                sh "docker image tag ${CURRENCY_SERVICE}:${BUILD_NUMBER} ${NEXUS_IMAGE_URL}/${CURRENCY_SERVICE}:${BUILD_NUMBER}"
                sh "docker image tag ${EMAIL_SERVICE}:${BUILD_NUMBER} ${NEXUS_IMAGE_URL}/${EMAIL_SERVICE}:${BUILD_NUMBER}"
                sh "docker image tag ${PAYMENT_SERVICE}:${BUILD_NUMBER} ${NEXUS_IMAGE_URL}/${PAYMENT_SERVICE}:${BUILD_NUMBER}"
                sh "docker image tag ${PRODUCT_CATALOG_SERVICE}:${BUILD_NUMBER} ${NEXUS_IMAGE_URL}/${PRODUCT_CATALOG_SERVICE}:${BUILD_NUMBER}"
                sh "docker image tag ${RECOMMENDATION_SERVICE}:${BUILD_NUMBER} ${NEXUS_IMAGE_URL}/${RECOMMENDATION_SERVICE}:${BUILD_NUMBER}"
                sh "docker image tag ${SHIPPING_SERVICE}:${BUILD_NUMBER} ${NEXUS_IMAGE_URL}/${SHIPPING_SERVICE}:${BUILD_NUMBER}"
                sh "docker image tag ${FRONTEND}:${BUILD_NUMBER} ${NEXUS_IMAGE_URL}/${FRONTEND}:${BUILD_NUMBER}"
                sh "docker image tag ${LOAD_GENERATOR}:${BUILD_NUMBER} ${NEXUS_IMAGE_URL}/${LOAD_GENERATOR}:${BUILD_NUMBER}"
                sh "docker login -u ${env.nexusUser} -p ${env.nexusPassword} ${NEXUS_IMAGE_URL}"
                sh "docker push ${NEXUS_IMAGE_URL}/${AD_SERVICE}:${BUILD_NUMBER}"
                sh "docker push ${NEXUS_IMAGE_URL}/${CART_SERVICE}:${BUILD_NUMBER}"
                sh "docker push ${NEXUS_IMAGE_URL}/${CURRENCY_SERVICE}:${BUILD_NUMBER}"
                sh "docker push ${NEXUS_IMAGE_URL}/${EMAIL_SERVICE}:${BUILD_NUMBER}"
                sh "docker push ${NEXUS_IMAGE_URL}/${PAYMENT_SERVICE}:${BUILD_NUMBER}"
                sh "docker push ${NEXUS_IMAGE_URL}/${PRODUCT_CATALOG_SERVICE}:${BUILD_NUMBER}"
                sh "docker push ${NEXUS_IMAGE_URL}/${RECOMMENDATION_SERVICE}:${BUILD_NUMBER}"
                sh "docker push ${NEXUS_IMAGE_URL}/${SHIPPING_SERVICE}:${BUILD_NUMBER}"
                sh "docker push ${NEXUS_IMAGE_URL}/${FRONTEND}:${BUILD_NUMBER}"
                sh "docker push ${NEXUS_IMAGE_URL}/${LOAD_GENERATOR}:${BUILD_NUMBER}"
        }
}

def removedocker() {
                sh "docker system prune --force --all"
                sh "docker system prune --force --all --volumes"
}
return this