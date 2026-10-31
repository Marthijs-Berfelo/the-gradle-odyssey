package gr.odyssey.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class OpenApiCodegenConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val javaVersion = JavaVersion.VERSION_25

        with(project.pluginManager) {
            apply("org.jetbrains.kotlin.jvm")
            apply("org.openapi.generator")
        }

        project.extensions.configure<JavaPluginExtension> {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        project.repositories.mavenCentral()

        project.extensions.configure<KotlinJvmProjectExtension> {
            compilerOptions {
                jvmTarget.set(JvmTarget.fromTarget(javaVersion.majorVersion))
                freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
            }
        }

        project.configureApiGeneration()
    }
}
