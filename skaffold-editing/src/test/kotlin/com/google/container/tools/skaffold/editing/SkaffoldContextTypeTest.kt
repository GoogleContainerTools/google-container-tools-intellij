package com.google.container.tools.skaffold.editing

import com.google.common.truth.Truth.assertThat
import com.google.container.tools.test.ContainerToolsRule
import com.intellij.psi.PsiFile
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Rule
import org.junit.Test

/**
 * Tests for [SkaffoldContextType].
 */
class SkaffoldContextTypeTest {
    @get:Rule
    val containerToolsRule = ContainerToolsRule(this)

    private val skaffoldContextType = SkaffoldContextType()

    @MockK
    private lateinit var psiFile: PsiFile

    @Test
    fun `isInContext checks valid skaffold file extensions`() {
        every { psiFile.name } returns "yaml"
        assertThat(skaffoldContextType.isInContext(psiFile, offset = 0)).isTrue()

        every { psiFile.name } returns "yml"
        assertThat(skaffoldContextType.isInContext(psiFile, offset = 0)).isTrue()

        every { psiFile.name } returns "xml"
        assertThat(skaffoldContextType.isInContext(psiFile, offset = 0)).isFalse()
    }
}