import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java")
    id("jacoco")
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.openapi.generator") version "7.2.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.slf4j:slf4j-api:2.0.11")
    runtimeOnly("ch.qos.logback:logback-core:1.4.14")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.14")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")

    testImplementation("org.mockito:mockito-core:5.3.1")
    testImplementation("org.mockito:mockito-junit-jupiter:5.3.1")

    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.20")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
}

openApiValidate {
    inputSpec.set("$rootDir/specs/dainius-lesson5-api.yaml")
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/specs/dainius-lesson5-api.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("com.balionis.dainius.lesson5.generated.api")
    modelPackage.set("com.balionis.dainius.lesson5.generated.model")
    configOptions.set(mapOf(
        "dateLibrary" to "java8",
        "generateApis" to "true",
        "generateApiTests" to "false",
        "generateModels" to "true",
        "generateModelTests" to "false",
        "generateModelDocumentation" to "false",
        "generateSupportingFiles" to "false",
        "hideGenerationTimestamp" to "true",
        "interfaceOnly" to "true",
        "library" to "spring-boot",
        "serializableModel" to "true",
        "useBeanValidation" to "true",
        "useTags" to "true",
        "implicitHeaders" to "true",
        "openApiNullable" to "false",
        "oas3" to "true"
    ))
}

sourceSets {
    main {
        java {
            srcDirs(listOf("${buildDir}/generated/src/main/java"))
        }
    }
}

tasks.compileJava {
    dependsOn(tasks.openApiValidate, tasks.openApiGenerate)
}

jacoco {
    toolVersion = "0.8.9"
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

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            element = "CLASS"
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.90".toBigDecimal()
            }
            excludes = listOf(
                "com.balionis.dainius.lesson5.Application",
                "com.balionis.dainius.lesson5.configuration.*",
                "com.balionis.dainius.lesson5.generated.*"
            )
        }
    }
    dependsOn(tasks.test)
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacoco-html")
    }

    dependsOn(tasks.test)
}

tasks.named<BootJar>("bootJar") {
    mainClass.set("com.balionis.dainius.lesson5.Application")
    archiveFileName.set("${archiveBaseName.get()}-all.${archiveExtension.get()}");
}

tasks {

    create<Exec>("dockerBuild") {
        dependsOn("bootJar")

        description = "Build a service docker image"
        group = "Docker"
        commandLine("docker","build","-t","${this.project.name}:latest",".")
    }

    create<Exec>("dockerComposeUp") {
        description = "Start a service on local docker"
        group = "Docker"
        commandLine("docker-compose","-f","docker-compose.yml","build")
        commandLine("docker-compose","-f","docker-compose.yml","up","-d")
    }

    create<Exec>("dockerComposeDown") {
        description = "Stop a service on local docker"
        group = "Docker"
        commandLine("docker-compose","-f","docker-compose.yml","down")
    }
}
