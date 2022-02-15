import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "dev.roddyra.addressbook"
version = "1.1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    mavenLocal()
}



tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainModule.set("me.RoddyRappaport.addressbook")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT") // The Spigot API with no shadowing. Requires the OSS repo.
}
