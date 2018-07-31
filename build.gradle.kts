import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

plugins {
    id("kotlin2js") version "1.2.51"
}

group = "co.thecodewarrior"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-js"))
}

val mainSourceSet = the<JavaPluginConvention>().sourceSets["main"]!!

tasks {
    val unpackKotlinJsStdlib by creating {
        group = "build"
        description = "Unpack the Kotlin JavaScript standard library"
        val outputDir = file("$buildDir/$name")
        val compileClasspath = configurations["compileClasspath"]
        inputs.property("compileClasspath", compileClasspath)
        outputs.dir(outputDir)
        doLast {
            val kotlinStdLibJar = compileClasspath.single {
                it.name.matches(Regex("kotlin-stdlib-js-.+\\.jar"))
            }
            copy {
                includeEmptyDirs = false
                from(zipTree(kotlinStdLibJar))
                into(outputDir)
                include("**/*.js")
                exclude("META-INF/**")
            }
        }
    }
    val assembleWeb by creating(Copy::class) {
        group = "build"
        description = "Assemble the web application"
        includeEmptyDirs = false
        from(unpackKotlinJsStdlib)
        from(mainSourceSet.output) {
            exclude("**/*.kjsm")
        }
        into("$buildDir/web")
    }
    "assemble" {
        dependsOn(assembleWeb)
    }
}
