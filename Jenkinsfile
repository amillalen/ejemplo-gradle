def build_tool

def output_dir =[:]

def last_stage

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
                output_dir["gradle"] = "${WORKSPACE}/build/libs/"
                output_dir["maven"]  = "${WORKSPACE}/build/"
              }
           }
        }
        stage("gradle build & test") {
          when {
            expression {
                  params.Build_Tool == "gradle"
            }
          }
          steps{
            script {
              last_stage = env.STAGE_NAME
              build_tool.build_test()
            }
          }
        }
        stage("maven build & test") {
          when {
            expression {
                  params.Build_Tool == "maven"
            }
          }
          steps{
            script {
              last_stage = env.STAGE_NAME
              build_tool.build_test()
            }
          }
        }

        stage('sonar') {
            steps {
                echo 'sonar...'
                script{
                last_stage = env.STAGE_NAME
                def scannerHome = tool 'local-sonar';
                withSonarQubeEnv(credentialsId:'sonartoken',installationName:'local-sonar') {
                   sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
                }
                }
            }
        }
        
        stage("maven run"){
           when {
             expression {
                  params.Build_Tool == "maven"
             }
           }
           steps{
             script{
               last_stage = env.STAGE_NAME
               build_tool.run_app();
             }
           }
        }


        stage("gradle run"){
           when {
             expression {
                  params.Build_Tool == "gradle"
             }
           }
           steps{
             script{
               last_stage = env.STAGE_NAME
               build_tool.run_app();
             }
           }
        }

        stage('wait serivice start') {
           steps{
           timeout(5) {
             waitUntil {
               script {
                 def exitCode = sh script:"grep -s Started /tmp/mscovid.log", returnStatus:true
                 return (exitCode == 0);
               }
             }
          }
          }
        }
        stage('test api rest') {
           steps{
               script { last_stage = env.STAGE_NAME  }
               echo 'test...'
               sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
          }
        } 


        stage('nexus') {
           steps{
            script{ last_stage = env.STAGE_NAME }
            echo 'nexus...'
            step(
             [$class: 'NexusPublisherBuildStep',
                 nexusInstanceId: 'nexus01',
                 nexusRepositoryId: 'devops-usach-nexus',
                 packages: [[$class: 'MavenPackage',
                       mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1'],
                       mavenAssetList: [
                          [classifier: '', extension: 'jar', filePath: "${output_dir[params.Build_Tool]}/DevOpsUsach2020-0.0.1.jar"]
                       ] 
                   ]
                 ]
               ]
             )
           }
        }

        stage('Paso Notificación Slack') {
            steps {
                echo 'Notificando por Slack...'
                slackSend channel: 'C044QF4MH4N', message: "Build Success: [Nombre Alumno][${JOB_NAME}][${params.Build_Tool}] Ejecución exitosa."
            }
        }



    }   

    post {
        failure {
            steps {
                echo 'Notificando de falla por Slack...'
                slackSend channel: 'C044QF4MH4N', message: "Build Failure: [Nombre Alumno][${JOB_NAME}][${params.Build_Tool}] Ejecución fallida en stage[${last_stage}]."
            }
        }
    }


 
}
