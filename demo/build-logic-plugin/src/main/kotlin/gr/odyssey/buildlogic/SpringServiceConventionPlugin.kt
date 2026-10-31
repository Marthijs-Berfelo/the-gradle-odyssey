package gr.odyssey.buildlogic

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class SpringServiceConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val javaVersion = JavaVersion.VERSION_25

        with(project.pluginManager) {
            apply("org.jetbrains.kotlin.jvm")
            apply("org.jetbrains.kotlin.plugin.spring")
            apply("org.springframework.boot")
            apply("org.jlleitschuh.gradle.ktlint")
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

        project.tasks.withType<Test>().configureEach { test ->
            test.useJUnitPlatform()
            test.testLogging { testLogging ->
                testLogging.events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
                testLogging.exceptionFormat = TestExceptionFormat.FULL
            }
        }
    }
}
