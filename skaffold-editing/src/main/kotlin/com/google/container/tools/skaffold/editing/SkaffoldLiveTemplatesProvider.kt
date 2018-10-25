package com.google.container.tools.skaffold.editing

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider

private const val SKAFFOLD_LIVE_TEMPLATE_PATH = "liveTemplates/Skaffold"

/**
 * A Skaffold [DefaultLiveTemplatesProvider] that provides the live templates used for generating /
 * editing Skaffold configuration files.
 */
class SkaffoldLiveTemplatesProvider : DefaultLiveTemplatesProvider {

    override fun getDefaultLiveTemplateFiles(): Array<String> {
        return arrayOf(SKAFFOLD_LIVE_TEMPLATE_PATH)
    }

    override fun getHiddenLiveTemplateFiles(): Array<String>? {
        return emptyArray()
    }
}