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
/*
        stage('build & test') {
            steps {
                echo 'build & test...'
                script{

                  build_tool["maven"].build_test()
                  build_tool["gradle"].build_test()
                  //sh "gradle build"
                }
            }
        }
        */
    }    
}
