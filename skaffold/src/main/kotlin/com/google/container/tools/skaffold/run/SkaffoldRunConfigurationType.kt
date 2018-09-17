package com.google.container.tools.skaffold.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.icons.AllIcons
import javax.swing.Icon

open class SkaffoldRunConfigurationType : ConfigurationType {
    val ID = "google-container-tools-skaffold-run-config"

    override fun getIcon(): Icon {
        return AllIcons.General.Balloon
    }

    override fun getConfigurationTypeDescription(): String {
        return "Skaffold"
    }

    override fun getId(): String {
        return ID
    }

    override fun getDisplayName(): String {
        return "Skaffold"
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return emptyArray()
    }
}
