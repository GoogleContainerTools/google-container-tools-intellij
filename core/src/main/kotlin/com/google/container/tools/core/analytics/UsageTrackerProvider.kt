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

import com.google.cloud.tools.ide.analytics.UsageTracker
import com.google.cloud.tools.ide.analytics.UsageTrackerManager
import com.google.cloud.tools.ide.analytics.UsageTrackerSettings
import com.google.container.tools.core.PluginInfo
import com.google.container.tools.core.properties.PropertiesFileFlagReader
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.PermanentInstallationID
import com.intellij.openapi.components.ServiceManager

class UsageTrackerProvider {

    val usageTracker: UsageTracker
    private val propertyReader = PropertiesFileFlagReader()

    init {
        val analyticsId = getAnalyticsId()

        val manager =
            UsageTrackerManager { analyticsId != null } // todo also test opt-in and unit-testmode

        val settings: UsageTrackerSettings = UsageTrackerSettings.Builder()
            .manager(manager)
            .analyticsId(analyticsId)
            .pageHost(PAGE_HOST)
            .platformName(PluginInfo.instance.platformPrefix)
            .platformVersion(ApplicationInfo.getInstance().strictVersion)
            .pluginName(PluginInfo.PLUGIN_NAME_EXTERNAL)
            .pluginVersion(PluginInfo.instance.pluginVersion)
            .clientId(PermanentInstallationID.get())
            .userAgent(PluginInfo.PLUGIN_USER_AGENT)
            .build()

        usageTracker = UsageTracker.create(settings)
    }

    companion object {
        private const val PAGE_HOST = "virtual.intellij"

        private const val ANALYTICS_ID_KEY = "analytics.id"
        private const val ANALYTICS_ID_PLACEHOLDER_VAL = "\${analyticsId}"

        val instance
            get() = ServiceManager.getService(UsageTrackerProvider::class.java)!!
    }

    private fun getAnalyticsId(): String? {
        val analyticsId: String? = propertyReader.getFlagString(ANALYTICS_ID_KEY)

        return if (ANALYTICS_ID_PLACEHOLDER_VAL != analyticsId) analyticsId else null
    }
}