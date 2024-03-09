plugins {
    `java-library`
    id("xyz.jpenilla.run-paper") version "2.2.3"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "com.rokucraft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("org.xerial:sqlite-jdbc:3.45.1.0")

    // Loaded through Spigot's library loading
    library("org.flywaydb:flyway-core:10.9.1")
    library("org.incendo:cloud-paper:2.0.0-beta.3")
    library("org.jdbi:jdbi3-core:3.45.0")

    library("com.google.dagger:dagger:2.50")
    annotationProcessor("com.google.dagger:dagger-compiler:2.50")

    implementation("org.jspecify:jspecify:0.3.0")

    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.8")
}

bukkit {
    name = "RokuHorses"
    version = project.version.toString()
    main = "com.rokucraft.rokuhorses.RokuHorses"
    apiVersion = "1.19"
    author = "Aikovdp"
    softDepend = listOf("WorldGuard")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        sourceCompatibility = JavaVersion.VERSION_17
    }
}

tasks {
    runServer {
        minecraftVersion("1.19.4")

        downloadPlugins {
            modrinth("worldedit", "TdNeSMad")
            url("https://dev.bukkit.org/projects/worldguard/files/4554903/download")
        }
    }
}
