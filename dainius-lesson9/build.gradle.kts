allprojects {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("java")
    id("jacoco")
    id("org.springframework.boot") version "3.3.4" apply false
    id("io.spring.dependency-management") version "1.1.7"
}

val javaProjects = setOf(
    "dainius-lesson9-producer",
    "dainius-lesson9-consumer"
)

val jacocoExclusions = mapOf(
    "dainius-lesson9-producer" to listOf(
        "com.balionis.dainius.lesson9.producer.Application",
        "com.balionis.dainius.lesson9.producer.configuration.*"
    ),
    "dainius-lesson9-consumer" to listOf(
        "com.balionis.dainius.lesson9.consumer.Application",
        "com.balionis.dainius.lesson9.consumer.configuration.*"
    )
)

val dockerProjects = setOf(
    "dainius-lesson9-producer",
    "dainius-lesson9-consumer"
)

subprojects {

    if (javaProjects.contains(this.project.name)) {
        apply {
            plugin("java")
            plugin("jacoco")
            plugin("org.springframework.boot")
            plugin("io.spring.dependency-management")
        }

        java {
            sourceCompatibility = JavaVersion.VERSION_17
        }

        configurations {
            compileOnly {
                extendsFrom(configurations.annotationProcessor.get())
            }
        }

        tasks.test {
            useJUnitPlatform()

            maxHeapSize = "1G"

            testLogging {
                showStandardStreams = true
                events("passed")
            }

            finalizedBy(tasks.jacocoTestCoverageVerification)
        }

        jacoco {
            toolVersion = "0.8.9"
        }

        tasks.jacocoTestReport {
            reports {
                xml.required.set(true)
                csv.required.set(false)
            }
        }
    }

    if (dockerProjects.contains(this.project.name)) {
        val dockerImage = "${this.project.name}"
        extra["dockerImage"] = dockerImage

        tasks {
            val dockerTags = listOf("latest")

            create<Exec>("dockerBuild") {
                dependsOn("build")

                description = "Builds a service's docker image."
                group = "Docker"

                environment("DOCKER_BUILDKIT", "1")
                commandLine(
                    listOf("docker", "build") +
                            dockerTags.flatMap { listOf("-t", "$dockerImage:$it") } +
                            project.projectDir.absolutePath
                )
            }
        }
    }
}
