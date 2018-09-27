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

import com.google.container.tools.skaffold.findSkaffoldFiles
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.options.SettingsEditor
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Settings editor that provides a UI for Skaffold continuous development mode run
 * configuration settings (ultimately calling "skaffold dev"), also saves and retrieves
 * the settings from the project state.
 */
class SkaffoldDevSettingsEditor : SettingsEditor<RunConfiguration>() {
    override fun resetEditorFrom(s: RunConfiguration) {
        print(findSkaffoldFiles(s.project))
    }

    override fun createEditor(): JComponent = JPanel()

    override fun applyEditorTo(s: RunConfiguration) {
    }
}
