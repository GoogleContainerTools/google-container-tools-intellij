repositories {
    jcenter()
}

plugins {
    id("org.jetbrains.intellij") version "0.3.7"

    kotlin("jvm") version "1.2.61"
}

subprojects {
    repositories {
        jcenter()
    }

    apply(plugin = "org.jetbrains.intellij")
    apply(plugin = "kotlin")
}
