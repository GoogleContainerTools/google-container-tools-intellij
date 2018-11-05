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

import com.google.container.tools.getPluginVersion
import com.google.container.tools.skaffold.SkaffoldLabels.Companion.getDefaultLabels
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.util.PlatformUtils
import java.util.Collections

const val IDE_LABEL = "ide"
const val IDE_VERSION_LABEL = "ideVersion"
const val PLUGIN_VERSION_LABEL = "ijPluginVersion"

/**
 * Maintains list of Kubernetes labels - string based key-value pairs used by Skaffold to apply
 * to deployments. [getDefaultLabels] function supplies default set of labels used for every
 * deployment.
 */
class SkaffoldLabels {
    companion object {
        /**
         * Creates default set of labels for all Skaffold-based deployments. Includes IDE type and
         * version, plugin version.
         */
        fun getDefaultLabels(): SkaffoldLabels {
            val defaultLabels = SkaffoldLabels()
            with(defaultLabels) {
                addLabel(IDE_LABEL, PlatformUtils.getPlatformPrefix())
                addLabel(IDE_VERSION_LABEL, ApplicationInfo.getInstance().getStrictVersion())
                addLabel(PLUGIN_VERSION_LABEL, getPluginVersion())
            }

            return defaultLabels
        }
    }

    private val labels: MutableMap<String, String> = mutableMapOf()

    fun addLabel(name: String, value: String) {
        labels.put(name, value)
    }

    fun getLabels() = Collections.unmodifiableMap(labels)
}
