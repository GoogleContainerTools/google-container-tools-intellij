package com.google.container.tools.skaffold.editing

import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiFile

private const val SKAFFOLD_TEMPLATE_ID = "SKAFFOLD"
private const val SKAFFOLD_TEMPLATE_NAME = "Skaffold"

/**
 * Defines the [TemplateContextType] for Skaffold files.
 */
class SkaffoldContextType(
    id: String = SKAFFOLD_TEMPLATE_ID,
    presentableName: String = SKAFFOLD_TEMPLATE_NAME
) :
    TemplateContextType(id, presentableName) {

    /**
     * A file is in context for Skaffold live templates if it is a yaml file.
     */
    override fun isInContext(file: PsiFile, offset: Int): Boolean {
        return file.name.endsWith(suffix = "yaml", ignoreCase = true)
            || file.name.endsWith(suffix = "yml", ignoreCase = true)
    }
}
