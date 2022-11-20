def build_tool 
pipeline {
    agent any
    tools {
        maven '3.8.6'
        gradle '7.6-rc3'
        
    }
    parameters {
        choice(name: 'Build_Tool', choices : ['maven','gradle'], description : 'Build tool for this pipeline')
    }
    stages {
        stage('Load script') {
           steps{
              script {
                build_tool = load "${params.Build_Tool}.groovy"
              }
           }
        }
        stage("gradle build & test tool") {
          when {
            expression {
                  params.Build_Tool == "gradle"
            }
          }
          steps{
            script {
              build_tool.build_test()
            }
          }
        }
        stage("maven build & test tool") {
          when {
            expression {
                  params.Build_Tool == "maven"
            }
          }
          steps{
            script {
              build_tool.build_test()
            }
          }
        }

        stage('sonar') {
            steps {
                echo 'sonar...'
                script{
                def scannerHome = tool 'local-sonar';
                withSonarQubeEnv(credentialsId:'sonartoken',installationName:'local-sonar') {
                   sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
                }
                }
            }
        }
        
        stage("gradle run"){
           steps{
              build_tool.run();
           }
        }


    }    
}
