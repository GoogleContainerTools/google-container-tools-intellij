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

import com.google.container.tools.skaffold.SkaffoldExecutorService
import com.google.container.tools.skaffold.SkaffoldExecutorSettings
import com.google.container.tools.skaffold.message
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtilCore
import java.io.File

/**
 * Generic [CommandLineState] implementation for running Skaffold on command line based on the given
 * [AbstractSkaffoldRunConfiguration] and current IDE project. [startProcess] checks configuration
 * and constructs command line to launch Skaffold process. Base class manages console
 * window output and graceful process shutdown (also see [KillableProcessHandler])
 */
class SkaffoldCommandLineState(
    environment: ExecutionEnvironment,
    val executionMode: SkaffoldExecutorSettings.ExecutionMode
) : CommandLineState(environment) {
    override fun startProcess(): ProcessHandler {
        val runConfiguration = environment.runnerAndConfigurationSettings?.configuration
        val projectBaseDir = environment.project.baseDir
        // ensure the configuration is valid for execution - settings are of supported type,
        // project is valid and Skaffold file is present.
        if (runConfiguration == null || runConfiguration !is AbstractSkaffoldRunConfiguration ||
            projectBaseDir == null
        ) {
            throw ExecutionException("Unsupported settings.")
        }
        if (runConfiguration.skaffoldConfigurationFilePath == null) {
            throw ExecutionException(message("skaffold.no.file.selected.error"))
        }

        val workingDirectory = File(environment.project.basePath)
        val configFile = LocalFileSystem.getInstance()
            .findFileByPath(runConfiguration.skaffoldConfigurationFilePath!!)
        // use project dir relative location for cleaner command line representation
        val skaffoldConfigurationFilePath = VfsUtilCore.getRelativeLocation(
            configFile, projectBaseDir
        )

        SkaffoldExecutorService.instance.executeSkaffold(
            SkaffoldExecutorSettings(
                executionMode,
                skaffoldConfigurationFilePath,
                workingDirectory
            )
        ).let {
            return KillableProcessHandler(it.process, it.commandLine)
        }
    }
}