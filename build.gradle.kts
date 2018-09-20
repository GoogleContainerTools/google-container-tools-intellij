plugins {
    id("org.jetbrains.intellij") version "0.3.7"
    id("com.diffplug.gradle.spotless") version "3.14.0"

    kotlin("jvm") version "1.2.61"
}

allprojects {
    repositories {
        jcenter()
    }

    apply(plugin = "org.jetbrains.intellij")
    apply(plugin = "kotlin")
    apply(plugin = "com.diffplug.gradle.spotless")

    intellij {
        type = "IC"
        version = "2018.2"
    }

    spotless {
        kotlin {
            ktlint()
        }

        kotlinGradle {
            target("**/*.kts", "**/src/**/*.kt")
            ktlint()
        }
    }
}

dependencies {
    compile(project(":skaffold"))
}
