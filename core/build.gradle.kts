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

project.logger.lifecycle("JUST A TEST------------------------------------------------------------")

dependencies {
    testCompile(project(":common-test-lib"))

    compile(files("../lib/ide-analytics-common-0.1.0-SNAPSHOT.jar"))
}

// Processes the analytics id environment variable value into the analyticsId property
// in config.properties
val processResources by tasks.getting(ProcessResources::class) {
    val analyticsId: String? = System.getenv("ANALYTICS_ID")
    inputs.property("analyticsId", analyticsId)
    filesMatching("**/config.properties") {
        // todo
//        expand(mapOf("analyticsId" to analyticsId))
//        expand "usageTrackerProperty": trackerProperty
        expand(project.properties)
    }
}
