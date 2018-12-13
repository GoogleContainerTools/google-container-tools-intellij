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

package com.google.container.tools.skaffold.run.ui

import com.google.common.annotations.VisibleForTesting
import com.google.container.tools.skaffold.SkaffoldYamlConfiguration
import com.google.container.tools.skaffold.message
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox

/**
 * Combobox for selecting Skaffold profiles. Default profile is assumed to be always present for a
 * valid Skaffold file. On Skaffold file change parses the YAML with [SkaffoldYamlConfiguration]
 * and refreshes profiles list. Disabled when only default profile is present.
 */
class SkaffoldProfilesComboBox : JComboBox<String>() {
    private val log = Logger.getInstance(this::class.java)

    @VisibleForTesting
    internal val model: DefaultComboBoxModel<String> = DefaultComboBoxModel()

    init {
        setModel(model)
        isEnabled = false
    }

    fun getSelectedProfile(): String? {
        return if (selectedIndex <= 0) null else model.getElementAt(selectedIndex)
    }

    /** Selects profile if it exists in the profile list. Non existing profile is ignored. */
    fun setSelectedProfile(profile: String) {
        for (i in 0 until model.size) {
            if (model.getElementAt(i).equals(profile)) {
                model.selectedItem = profile
                break
            }
        }
    }

    fun skaffoldFileUpdated(skaffoldFile: VirtualFile?) {
        val profileSet: Set<String> = skaffoldFile?.let {
            try {
                val skaffoldYamlConfiguration = SkaffoldYamlConfiguration(skaffoldFile)
                skaffoldYamlConfiguration.profiles.keys
            } catch (e: Exception) {
                // malformed YAML - clear and disable profiles selection.
                log.warn("invalid skaffold file, unable to load profiles", e)
                model.removeAllElements()
                isEnabled = false
                return
            }
        } ?: setOf()

        updateModel(profileSet)
    }

    private fun updateModel(profiles: Set<String>) {
        model.removeAllElements()
        model.addElement(message("skaffold.default.profile.name"))
        profiles.forEach { model.addElement(it) }
        // disable selection in case only default profile is available
        isEnabled = profiles.size > 0
    }
}
