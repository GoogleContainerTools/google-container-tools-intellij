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

class SkaffoldFilesTest {
    @Test
    fun `empty project return empty list`() {
    }

    @Test
    fun `valid skaffold file is recognized`() {
        val skaffoldFile: MockVirtualFile = MockVirtualFile.file("skaffold.yaml")
        skaffoldFile.setText("apiVersion: skaffold/v1alpha2")

        assertThat(isSkaffoldFile(skaffoldFile)).isTrue()
    }

    @Test
    fun `kubernetes file is not valid skaffold file`() {
        val k8sFile: MockVirtualFile = MockVirtualFile.file("deploy.yaml")
        k8sFile.setText("apiVersion: apps/v1")

        assertThat(isSkaffoldFile(k8sFile)).isFalse()
    }
}
