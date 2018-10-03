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
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox

/**
 * A combo box with a list of available Skaffold configuration files for a given project.
 */
class SkaffoldFilesComboBox(project: Project) : JComboBox<VirtualFile>() {
    private val skaffoldFilesMutableModel =
        DefaultComboBoxModel<VirtualFile>(findSkaffoldFiles(project).toTypedArray())

    init {
        model = skaffoldFilesMutableModel
        if (model.size > 0) selectedIndex = 0
    }

    fun setSelectedSkaffoldFile(skaffoldFile: VirtualFile) {
        skaffoldFilesMutableModel.addElement(skaffoldFile)
        selectedItem = skaffoldFile
    }
}