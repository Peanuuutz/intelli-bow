import kotlin.collections.listOf

plugins {
    id("fabric-loom")

    val kotlinVersion: String by System.getProperties()
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("com.github.johnrengelman.shadow") version "6.1.0"
}

base {
    val archivesBaseName: String by rootProject
    this.archivesBaseName = archivesBaseName
    val modVersion: String by rootProject
    version = modVersion
    val mavenGroup: String by rootProject
    group = mavenGroup
}

minecraft {}

repositories {
    mavenCentral()
}

dependencies {
    // Main
    val minecraftVersion: String by project
    minecraft("com.mojang:minecraft:$minecraftVersion")

    val yarnVersion: String by project
    mappings("net.fabricmc:yarn:$yarnVersion:v2")
    val loaderVersion: String by project
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    val fabricVersion: String by project
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")

    val fabricKotlinVersion: String by project
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")

    val yamlktVersion: String by project
    implementation("net.mamoe.yamlkt:yamlkt:$yamlktVersion")
    shadow("net.mamoe.yamlkt:yamlkt:$yamlktVersion")

    // Test
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks {
    shadowJar {
        configurations = listOf(project.configurations.shadow.get())
        archiveClassifier.set(null as String?)

        from("LICENSE") {
            rename { "${it}_${base.archivesBaseName}" }
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        if (JavaVersion.current().isJava9Compatible) {
            options.release.set(8)
        }
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
    }

    java {
        withSourcesJar()
    }

    test {
        useJUnitPlatform()
    }
}