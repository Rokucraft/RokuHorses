plugins {
    `java-library`
    id("xyz.jpenilla.run-paper") version "2.0.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "com.rokucraft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("org.xerial:sqlite-jdbc:3.45.1.0")

    // Loaded through Spigot's library loading
    library("org.flywaydb:flyway-core:10.8.1")
    library("org.incendo:cloud-paper:2.0.0-beta.3")
    library("org.jdbi:jdbi3-core:3.45.0")

    library("com.google.dagger:dagger:2.50")
    annotationProcessor("com.google.dagger:dagger-compiler:2.50")

    implementation("org.jspecify:jspecify:0.3.0")
}

bukkit {
    name = "RokuHorses"
    version = project.version.toString()
    main = "com.rokucraft.rokuhorses.RokuHorses"
    apiVersion = "1.19"
    author = "Aikovdp"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks {
    runServer {
        minecraftVersion("1.19.4")
    }
}
