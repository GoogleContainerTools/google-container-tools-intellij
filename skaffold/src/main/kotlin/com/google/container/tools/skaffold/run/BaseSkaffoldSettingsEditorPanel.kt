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

import com.google.container.tools.skaffold.message
import com.intellij.openapi.project.Project
import com.intellij.ui.layout.panel
import javax.swing.JPanel

/**
 * Base UI panel for both Skaffodl single run and continunous run configurations. Includes drop-down
 * list of all Skaffold configuration files ([SkaffoldFilesComboBox]) found in the project.
 */
class BaseSkaffoldSettingsEditorPanel(project: Project) : JPanel() {
    val skaffoldFilesComboBox: SkaffoldFilesComboBox = SkaffoldFilesComboBox(project)

    val basePanel = panel {
        row(message("skaffold.configuration.label")) { skaffoldFilesComboBox(grow) }
    }

    init {
        add(basePanel)
    }
}
