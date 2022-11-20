def build_tool = [:]
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
                build_tool["maven"] = load "maven.groovy"
                build_tool["gradle"] = load "gradle.groovy"
              }
           }
        }
        stage("select build & test tool") {
          steps{
            script {
              build_tool[params.Build_Tool].build_test()
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
