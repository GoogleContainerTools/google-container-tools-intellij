repositories {
    jcenter()
}

plugins {
    id("org.jetbrains.intellij") version "0.3.6"

    kotlin("jvm") version "1.2.61"
}

dependencies {
    compile(kotlin("stdlib"))
    compile(kotlin("runtime"))
}
