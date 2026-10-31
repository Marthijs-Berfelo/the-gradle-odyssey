package gr.odyssey.buildlogic

import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

private const val TASK_GROUP = "openapi tools"

fun Project.configureApiGeneration(
    specPath: String = "openapi/odyssey-api.yaml",
    serverOutputDir: String = "generated/api/server",
    clientOutputDir: String = "generated/api/client",
    generateServer: Boolean = true,
    generateClient: Boolean = true,
) {
    val odysseySpec =
        layout.projectDirectory
            .file("src/main/resources/$specPath")
            .asFile.path
    val serverDir =
        layout.buildDirectory
            .dir(serverOutputDir)
            .get()
            .asFile.path
    val clientDir =
        layout.buildDirectory
            .dir(clientOutputDir)
            .get()
            .asFile.path
    val generatedSourceDirs = mutableListOf<String>()
    val codegenTasks = mutableListOf<TaskProvider<GenerateTask>>()

    if (generateServer) {
        val generateServerApi =
            tasks.register<GenerateTask>("generateServerApi") {
                description = "Generates servers for the Odyssey API"
                group = TASK_GROUP
                generatorName.set("kotlin-spring")
                inputSpec.set(odysseySpec)
                outputDir.set(serverDir)
                apiPackage.set("gr.odyssey.api.server")
                modelPackage.set("gr.odyssey.api.server.model")
                configOptions.set(
                    mapOf(
                        "interfaceOnly" to "true",
                        "useSpringBoot4" to "true",
                        "useJackson3" to "true",
                        "useSwaggerUI" to "true",
                        "suspendFunctions" to "true",
                        "reactive" to "true",
                    ),
                )
            }
        generatedSourceDirs += "$serverDir/src/main/kotlin"
        codegenTasks += generateServerApi
    }

    if (generateClient) {
        val generateClientApi =
            tasks.register<GenerateTask>("generateClientApi") {
                description = "Generates clients for the Odyssey API"
                group = TASK_GROUP
                generatorName.set("kotlin-spring")
                inputSpec.set(odysseySpec)
                outputDir.set(clientDir)
                apiPackage.set("gr.odyssey.api.client")
                modelPackage.set("gr.odyssey.api.client.model")
                configOptions.set(
                    mapOf(
                        "library" to "spring-declarative-http-interface",
                        "declarativeInterfaceReactiveMode" to "coroutines",
                        "useSpringBoot4" to "true",
                        "useJackson3" to "true",
                        "suspendFunctions" to "true",
                        "reactive" to "true",
                        "useFlowForArrayReturnType" to "false",
                    ),
                )
            }
        generatedSourceDirs += "$clientDir/src/main/kotlin"
        codegenTasks += generateClientApi
    }

    the<KotlinJvmProjectExtension>().sourceSets.named("main") { sourceSet ->
        sourceSet.kotlin.srcDirs(generatedSourceDirs)
    }

    tasks.named("compileKotlin") { task ->
        task.dependsOn(codegenTasks)
    }
}
