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

package com.google.container.tools.skaffold

import com.google.common.truth.Truth.assertThat
import com.intellij.mock.MockVirtualFile
import org.junit.Test

/** Unit tests for [SkaffoldYamlConfiguration] */
class SkaffoldYamlConfigurationTest {

    @Test
    fun `skaffold yaml profile names correctly load and map to profile objects`() {
        val skaffoldYamlFile = MockVirtualFile.file("skaffold.yaml")
        skaffoldYamlFile.setText("""
            apiVersion: skaffold/v1beta1
            kind: Config
            profiles:
              - name: gcb
                build:
                  googleCloudBuild:
                    projectId: k8s-skaffold
        """)

        val skaffoldYamlConfiguration = SkaffoldYamlConfiguration(skaffoldYamlFile)

        println(skaffoldYamlConfiguration.profiles)
        assertThat(skaffoldYamlConfiguration.profiles).isNotEmpty()
    }
}
