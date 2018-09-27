/*
 * Copyright 2018 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        setPlugins("yaml")

        type = project.properties["ideaEdition"].toString()
        version = project.properties["ideaVersion"].toString()
        intellijRepo = project.properties["intellijRepoUrl"].toString()
    }

    spotless {
        kotlin {
            target("**/src/**/*.kt")
            // Set ktlint to follow the Android Style Guide for source files
            ktlint().userData(mapOf("android" to "true"))
        }

        kotlinGradle {
            target("**/*.gradle.kts")
            ktlint()
        }
    }

    dependencies {
        testCompile("com.google.truth:truth:+") {
            exclude(group = "com.google.guava", module = "guava")
        }
    }
}

dependencies {
    compile(project(":skaffold"))
}
