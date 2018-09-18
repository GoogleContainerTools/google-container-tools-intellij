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

package com.google.container.tools.skaffold.run

import com.google.container.tools.skaffold.SKAFFOLD_ICON
import com.google.container.tools.skaffold.message
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import javax.swing.Icon

/**
 * [ConfigurationType] extension that registers and enables Skaffold run configuration type, including single run and
 * continuous deployment targets provided by separate [ConfigurationFactory].
 */
class SkaffoldRunConfigurationType : ConfigurationType {
    val ID = "google-container-tools-skaffold-run-config"

    override fun getIcon(): Icon {
        return SKAFFOLD_ICON
    }

    override fun getConfigurationTypeDescription(): String {
        return message("skaffold.run.config.general.description")
    }

    override fun getId(): String {
        return ID
    }

    override fun getDisplayName(): String {
        return message("skaffold.run.config.general.name")
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return emptyArray()
    }
}
