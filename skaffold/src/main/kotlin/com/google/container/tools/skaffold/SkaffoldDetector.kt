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

import com.intellij.framework.FrameworkType
import com.intellij.framework.detection.DetectedFrameworkDescription
import com.intellij.framework.detection.FileContentPattern
import com.intellij.framework.detection.FrameworkDetectionContext
import com.intellij.framework.detection.FrameworkDetector
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.ElementPattern
import com.intellij.util.indexing.FileContent
import org.jetbrains.yaml.YAMLFileType

private const val SKAFFOLD_DETECTOR_ID = "skaffold-detector-id"

class SkaffoldDetector : FrameworkDetector(SKAFFOLD_DETECTOR_ID) {
    override fun createSuitableFilePattern(): ElementPattern<FileContent> =
        FileContentPattern.fileContent().withName("skaffold.yaml")

    override fun getFrameworkType(): FrameworkType? = null

    override fun detect(
        newFiles: MutableCollection<VirtualFile>,
        context: FrameworkDetectionContext
    ): MutableList<out DetectedFrameworkDescription> {
        println(newFiles)
        return mutableListOf()
    }

    override fun getFileType(): FileType = YAMLFileType.YML
}