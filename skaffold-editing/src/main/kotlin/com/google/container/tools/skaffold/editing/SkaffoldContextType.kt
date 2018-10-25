package com.google.container.tools.skaffold.editing

import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiFile

class SkaffoldContextType(id: String = "SKAFFOLD", presentableName: String = "Skaffold") :
    TemplateContextType(id, presentableName) {
    override fun isInContext(file: PsiFile, offset: Int): Boolean {
        return file.name.endsWith("yaml") || file.name.endsWith("yml")
    }
}