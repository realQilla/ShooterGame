plugins {
    id("java")
}

group = "net.qilla"
version = "1.20.5-0.DEV"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots"
    }
}


dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.5-R0.1-SNAPSHOT")
    implementation("net.kyori:adventure-api:4.16.0")
}

tasks.register<Jar>("exportJar") {
    dependsOn("build")
    archiveBaseName.set(project.name)
    archiveVersion.set(project.version.toString())
    from(sourceSets.main.get().output)
    destinationDirectory.set(file("C:\\Users\\Richard\\Development\\DevServerLatest\\plugins"))
}

tasks.named("build") {
    finalizedBy("exportJar")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filesMatching("plugin.yml") {
            expand("projectVersion" to project.version)
        }
    }
}