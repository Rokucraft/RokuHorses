plugins {
    `java-library`
    id("xyz.jpenilla.run-paper") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.rokucraft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    implementation("cloud.commandframework:cloud-paper:1.8.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    runServer {
        minecraftVersion("1.19.4")
    }
    processResources {
        val props = "version" to version
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
    shadowJar {
        isEnableRelocation = true
        relocationPrefix = "$group.$name.libs"
    }
    assemble {
        dependsOn(shadowJar)
    }
}
