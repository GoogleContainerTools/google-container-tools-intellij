package com.google.container.tools.skaffold.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import javax.swing.Icon

open class SkaffoldRunConfigurationType : ConfigurationType {
    override fun getIcon(): Icon {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getConfigurationTypeDescription(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getId(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDisplayName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return emptyArray()
    }
}