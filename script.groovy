def cleanup() {
        cleanWs()
}

def checkout() {
        git branch: 'main', credentialsId: 'github', url: "$GITHUB_URL"
}

def sonarqube() {
        withSonarQubeEnv('SonarQube') {
                sh "$SCANNER_HOME/bin/sonar-scanner -Dsonar.projectkey=10-Tier -Dsonar.projectName=10Tier -Dsonar.java.binaries=."
        }
}
def owasp() {
    dependencyCheck additionalArguments: '--scan ./ --disableYarnAudit --disableNodeAudit', odcInstallation: 'DP-check'
    dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
}

def trivyfs() {
        sh "trivy fs ."
}

return this