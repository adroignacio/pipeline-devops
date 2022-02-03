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
        text description: 'Enviar los stages separados por ";" ... Vacío si necesita todos los stages', name: 'stages'
    }
    stages {
        stage("Pipeline"){
            steps {
                script{
                     switch(params.compileTool)
                    {
                        case 'Maven':
                            maven.call(params.stages);
                        break;
                        case 'Gradle':
                            gradle.call(params.stages);
                        break;
                    }
                }
            }
            post {
                success{
                    slackSend color: 'good', message: "Alejandro [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-credential'
                }
                failure{
                    slackSend color: 'danger', message: "Alejandro [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.TAREA}]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-credential'
                }
            }
        }
    }
}

}

return this;