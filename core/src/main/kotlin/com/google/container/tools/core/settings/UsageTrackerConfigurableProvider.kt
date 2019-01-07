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

package com.google.container.tools.core.settings

import com.google.container.tools.core.analytics.UsageTrackerManagerService
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurableProvider

/**
 * Class that provides the configurable which creates the "Usage Tracking" menu item under the
 * top-level "Google" menu item.
 */
class UsageTrackerConfigurableProvider : ConfigurableProvider() {

    override fun createConfigurable(): Configurable? = UsageTrackerConfigurable()

    /**
     * Only create the menu item if usage tracking is available. For example, if running in dev
     * mode with no analytics ID environment variable configured, hide the usage track menu item.
     */
    override fun canCreateConfigurable(): Boolean =
        UsageTrackerManagerService.instance.isUsageTrackingAvailable()
}
