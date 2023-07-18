plugins {
    `java-library`
    id("xyz.jpenilla.run-paper") version "2.0.0"
}

group = "com.rokucraft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("org.xerial:sqlite-jdbc:3.42.0.0")

    // Loaded through Spigot's library loading
    compileOnly("org.flywaydb:flyway-core:9.20.1")
    compileOnly("cloud.commandframework:cloud-paper:1.8.3")
    compileOnly("org.jdbi:jdbi3-core:3.39.1")
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
}
