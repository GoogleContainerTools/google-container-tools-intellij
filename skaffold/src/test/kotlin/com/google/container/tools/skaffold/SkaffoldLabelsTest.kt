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
import com.google.container.tools.test.ContainerToolsRule
import com.google.container.tools.test.TestService
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.util.PlatformUtils
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import org.junit.Rule
import org.junit.Test

/** Unit tests for [SkaffoldLabels] class. */
class SkaffoldLabelsTest {
    @get:Rule
    val containerToolsRule = ContainerToolsRule(this)

    @MockK
    @TestService
    private lateinit var mockApplicationInfo: ApplicationInfo

    @Test
    fun `default labels are constructed from IDE and plugin information`() {
        mockkStatic(PlatformUtils::class)
        every { PlatformUtils.getPlatformPrefix() } answers { "TestIde" }
        every { mockApplicationInfo.strictVersion } answers { "999.9" }

        val defaultLabels = SkaffoldLabels.getDefaultLabels()

        assertThat(defaultLabels.getLabels()["ide"]).isEqualTo("TestIde")
        assertThat(defaultLabels.getLabels()["ideVersion"]).isEqualTo("999.9")
    }
}
