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
import com.google.container.tools.core.PluginInfo
import com.google.container.tools.test.ContainerToolsRule
import com.google.container.tools.test.TestService
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.util.PlatformUtils
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
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
    fun `valid default skaffold labels are constructed from IDE and plugin information`() {
        // mock all sources of information that should be used for default labels
        mockkStatic(PlatformUtils::class)
        every { PlatformUtils.getPlatformPrefix() } answers { "TestIde" }
        every { mockApplicationInfo.strictVersion } answers { "999.9" }
        mockkObject(PluginInfo)
        every { PluginInfo.pluginVersion } answers { "0.1" }

        val defaultLabels: SkaffoldLabels = SkaffoldLabels.getDefaultLabels()

        assertThat(defaultLabels.labels["ide"]).isEqualTo("TestIde")
        assertThat(defaultLabels.labels["ideVersion"]).isEqualTo("999.9")
        assertThat(defaultLabels.labels["ijPluginVersion"]).isEqualTo("0.1")
    }
}
