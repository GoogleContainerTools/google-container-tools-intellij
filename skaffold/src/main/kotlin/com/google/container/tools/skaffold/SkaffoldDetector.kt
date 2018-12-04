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

import com.google.container.tools.core.PLUGIN_NOTIFICATION_DISPLAY_GROUP_ID
import com.google.container.tools.skaffold.run.AbstractSkaffoldRunConfiguration
import com.google.container.tools.skaffold.run.SkaffoldDevConfiguration
import com.google.container.tools.skaffold.run.SkaffoldDevConfigurationFactory
import com.google.container.tools.skaffold.run.SkaffoldRunConfigurationType
import com.intellij.execution.RunManager
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.Project

class SkaffoldDetector(val project: Project) : ProjectComponent {
    private val NOTIFICATION_GROUP = NotificationGroup(
        PLUGIN_NOTIFICATION_DISPLAY_GROUP_ID,
        NotificationDisplayType.BALLOON,
        true,
        null,
        SKAFFOLD_ICON
    )

    override fun projectOpened() {
        println("projectOpened: $project")
        println(SkaffoldFileService.instance.findSkaffoldFiles(project))
        val allConfigurationsList = RunManager.getInstance(project).allConfigurationsList
        println("allConfigs: $allConfigurationsList")
        print("skaffold list: ")
        val skaffoldList = allConfigurationsList.filter { it is AbstractSkaffoldRunConfiguration }
        println(skaffoldList)
        if (skaffoldList.isEmpty()) {
            showPromptForSkaffoldConfigurations(project)
            val devSettings = SkaffoldDevConfiguration(
                project,
                SkaffoldDevConfigurationFactory(SkaffoldRunConfigurationType()),
                "development on Kubernetes"
            )
            val settingsProfile = RunnerAndConfigurationSettingsImpl(
                RunManagerImpl.getInstanceImpl(project),
                devSettings
            )
            RunManager.getInstance(project).addConfiguration(settingsProfile)
            RunManager.getInstance(project).selectedConfiguration = settingsProfile
            println("added config for skaffold")
        }
    }

    private fun showPromptForSkaffoldConfigurations(project: Project) {
        val notification = NOTIFICATION_GROUP.createNotification(
            "Skaffold Configuration Detected", null,
            "Skaffold configuration file(s) detected, would you like to create run configurations for it?",
            NotificationType.INFORMATION
        )
        notification.notify(project)

        notification.addAction(object : AnAction("Create development on Kubernetes configuration") {
            override fun actionPerformed(e: AnActionEvent?) {
            }
        })

        notification.addAction(object :
            AnAction("Create single deploy to Kubernetes configuration") {
            override fun actionPerformed(e: AnActionEvent?) {
            }
        })

        notification.addAction(object : AnAction("Skip") {
            override fun actionPerformed(e: AnActionEvent?) {
            }
        })
    }
}
