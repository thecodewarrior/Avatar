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
//    "compile"(kotlin("reflect", kotlin_version))
//    "compile"("nl.komponents.kovenant", "kovenant", "3.3.0")
//    "testCompile"("junit", "junit", "4.12")
//    "testCompile"("com.nhaarman", "mockito-kotlin-kt1.1", "1.5.0")
//    "testImplementation"("org.junit.jupiter", "junit-jupiter-api", "5.2.0-M1")
//    "testRuntimeOnly"("org.junit.jupiter", "junit-jupiter-engine", "5.2.0-M1")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

