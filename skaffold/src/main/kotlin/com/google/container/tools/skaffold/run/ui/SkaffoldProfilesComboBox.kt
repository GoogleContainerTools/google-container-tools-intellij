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

import com.google.container.tools.skaffold.SkaffoldYamlConfiguration
import com.google.container.tools.skaffold.message
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox

class SkaffoldProfilesComboBox : JComboBox<String>() {

    private val model: DefaultComboBoxModel<String> = DefaultComboBoxModel()

    init {
        setModel(model)
    }

    fun getSelectedProfile() : String? {
        return if (selectedIndex <= 0) null else model.getElementAt(selectedIndex)
    }

    fun setSelectedProfile(profile: String) {
        for (i in 0..model.size) {
            if (model.getElementAt(i).equals(profile)) {
                model.selectedItem = profile
                break
            }
        }
    }

    fun skaffoldFileUpdated(skaffoldFile: VirtualFile?) {
        val profileSet = skaffoldFile?.let {
            val skaffoldYamlConfiguration = SkaffoldYamlConfiguration(skaffoldFile)
            skaffoldYamlConfiguration.profiles.keys
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
