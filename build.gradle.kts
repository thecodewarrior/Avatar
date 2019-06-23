import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "dev.thecodewarrior"
version = "1.0-SNAPSHOT"

plugins {
    java
    kotlin("jvm") version "1.3.31"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("org.apache.xmlgraphics:batik-rasterizer:1.11")
    compile("org.redundent:kotlin-xml-builder:1.5.1")
    compile("io.humble:humble-video-all:0.3.0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

