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

import com.intellij.openapi.vfs.VirtualFile
import org.yaml.snakeyaml.Yaml
import java.io.ByteArrayInputStream

class SkaffoldYamlConfiguration(skaffoldYamlFile: VirtualFile) {
    private val skaffoldYamlMap = mutableMapOf<Any, Any>()

    init {
        val yamlLoader = Yaml()
        skaffoldYamlMap.putAll(yamlLoader.load(ByteArrayInputStream(skaffoldYamlFile.contentsToByteArray())))
    }

    val profiles: List<String>
        get() {
            if (skaffoldYamlMap["profiles"] is List<*>) return skaffoldYamlMap["profiles"] as List<String> else return listOf()
        }
}
