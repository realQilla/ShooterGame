plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "net.qilla"
version = "1.20.6-0.DEV"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
    implementation("net.kyori:adventure-api:4.17.0")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.register<Jar>("exportJar") {
    dependsOn("build")
    archiveBaseName.set(project.name)
    archiveVersion.set(project.version.toString())
    from(sourceSets.main.get().output)
    destinationDirectory.set(file("C:\\Users\\Richard\\Development\\Servers\\Servers Proxy\\Zombies\\plugins"))
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