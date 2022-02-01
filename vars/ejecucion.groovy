/*

	forma de invocación de método call:

	def ejecucion = load 'script.groovy'
	ejecucion.call()

*/

def call(){
  
pipeline {
    agent any
    environment {
        NEXUS_USER         = credentials('NEXUS_USER')
        NEXUS_PASSWORD     = credentials('NEXUS_PASS')
    }
        parameters {
        choice(
            name:'compileTool',
            choices: ['Maven', 'Gradle'],
            description: 'Seleccione herramienta de compilacion'
        )
    }
    stages {
        stage("Pipeline"){
            steps {
                script{
                     switch(params.compileTool)
                    {
                        case 'Maven':
                            //def ejecucion = load 'maven.groovy'
                            //ejecucion.call()
                            maven.call();
                        break;
                        case 'Gradle':
                            //def ejecucion = load 'gradle.groovy'
                            //ejecucion.call()
                            gradle.call();
                        break;
                    }
                }
            }
            post {
                success{
                    slackSend color: 'good', message: "Alejandro [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-credential'
                }
                failure{
                    slackSend color: 'danger', message: "Alejandro [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-credential'
                }
            }
        }
    }
}

}

return this;