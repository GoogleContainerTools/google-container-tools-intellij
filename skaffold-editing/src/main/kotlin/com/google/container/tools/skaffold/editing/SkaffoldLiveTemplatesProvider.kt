package com.google.container.tools.skaffold.editing

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider

class SkaffoldLiveTemplatesProvider: DefaultLiveTemplatesProvider {
    override fun getDefaultLiveTemplateFiles(): Array<String> {
        return arrayOf("liveTemplates/Skaffold")
    }

    override fun getHiddenLiveTemplateFiles(): Array<String>? {
        return emptyArray()
    }
}