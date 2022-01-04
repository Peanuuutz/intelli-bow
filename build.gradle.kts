plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("fabric-loom")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

base {
    val archivesName: String by project
    this.archivesName.set(archivesName)
}

val modVersion: String by project
version = modVersion
val mavenGroup: String by project
group = mavenGroup

minecraft {}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // Main
    val minecraftVersion: String by project
    minecraft("com.mojang:minecraft:$minecraftVersion")

    val yarnVersion: String by project
    mappings("net.fabricmc:yarn:$yarnVersion:v2")
    val loaderVersion: String by project
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    val fabricApiVersion: String by project
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    val fabricKotlinVersion: String by project
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")

    val tomlktVersion: String by project
    implementation("net.peanuuutz:tomlkt:$tomlktVersion") {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }
    shadow("net.peanuuutz:tomlkt:$tomlktVersion") {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }

    // Test
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        if (JavaVersion.current().isJava9Compatible) {
            options.release.set(8)
        }
    }

    shadowJar {
        relocate("net.peanuuutz.tomlkt", "${project.group}.intellibow.shadowed.net.peanuuutz.yamlkt")

        configurations = listOf(project.configurations.shadow.get())
        archiveClassifier.set("shadow")
    }

    remapJar {
        dependsOn(shadowJar.get())
        input.set(shadowJar.get().archiveFile)
        archiveClassifier.set("fabric")

        from("LICENSE")
    }

    test {
        useJUnitPlatform()
    }
}