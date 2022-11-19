pipeline {
    agent any
    tools {
        maven '3.8.6'
        gradle '7.6-rc3'
    }
    stages {
        stage('build & test') {
            steps {
                echo 'build & test...'
                sh "gradle build"
            }
        }
        stage('sonar') {
            steps {
                echo 'sonar...'
                withSonarQubeEnv(credentialsId:'sonartoken',installationName:'local-sonar') {
                   sh 'mvn sonar:sonar'
                }
            }
        }
        stage('run') {
            steps {
                echo 'run...'
                sh "gradle bootRun &"            
            }
        }
        stage('wait serivice start') {
           steps{
           timeout(5) {
             waitUntil {
               script {
                 def r = sh script: "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'", returnStdout: true
                 return (r == 0);
               }
             }
          }
          }
        }
        stage('test api rest') {
           steps{
               echo 'test...'
               sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
          }
        } 
        stage('nexus') {
           steps{
            echo 'nexus...'
            step(
             [$class: 'NexusPublisherBuildStep',
                 nexusInstanceId: 'nexus01',
                 nexusRepositoryId: 'devops-usach-nexus',
                 packages: [[$class: 'MavenPackage',
                       mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.22'],
                       mavenAssetList: [
                          [classifier: '', extension: 'jar', filePath: "${WORKSPACE}/build/DevOpsUsach2020-0.0.1.jar"]
                       ] 
                   ]
                 ]
               ]
             )
           }
        }
        
    }    
}
