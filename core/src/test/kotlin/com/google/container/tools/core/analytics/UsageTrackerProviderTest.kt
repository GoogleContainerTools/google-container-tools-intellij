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

package com.google.container.tools.core.analytics

import com.google.cloud.tools.ide.analytics.UsageTrackerSettings
import com.google.common.truth.Truth.assertThat
import com.google.container.tools.core.PluginInfo
import com.google.container.tools.test.ContainerToolsRule
import com.google.container.tools.test.TestService
import io.mockk.impl.annotations.MockK
import org.junit.Rule
import org.junit.Test

/**
 * Tests for [UsageTrackerProvider].
 */
class UsageTrackerProviderTest {
    @get:Rule
    val containerToolsRule = ContainerToolsRule(this)

    @MockK
    @TestService
    private lateinit var usageTrackerManagerService: UsageTrackerManagerService

    @MockK
    @TestService
    private lateinit var pluginInfo: PluginInfo

    @Test
    fun `usage tracker provides initializes usage tracker`() {
        assertThat(UsageTrackerProvider().usageTracker).isNotNull()
    }

    @Test
    fun `usage tracker with tracking not enabled returns no-op tracker`() {
        val usageTrackerProvider = UsageTrackerProvider()
        usageTrackerProvider.usageTrackerSettings =
            createUsageTrackerSettings(isTrackingEnabled = false)

        assertThat(usageTrackerProvider.usageTracker.javaClass.simpleName)
            .isEqualTo("NoOpUsageTracker")
    }

    @Test
    fun `usage tracker with tracking enabled returns google tracker`() {
        val usageTrackerProvider = UsageTrackerProvider()
        usageTrackerProvider.usageTrackerSettings =
            createUsageTrackerSettings(isTrackingEnabled = true)

        assertThat(usageTrackerProvider.usageTracker.javaClass.simpleName)
            .isEqualTo("GoogleUsageTracker")
    }

    private fun createUsageTrackerSettings(isTrackingEnabled: Boolean): UsageTrackerSettings =
        UsageTrackerSettings.Builder()
            .manager { isTrackingEnabled }
            .analyticsId("analytics-id")
            .pageHost("page-host")
            .platformName("platform-name")
            .platformVersion("platform-version")
            .pluginName("plugin-name")
            .pluginVersion("plugin-version")
            .clientId("client-id")
            .userAgent("user-agent")
            .build()
}
