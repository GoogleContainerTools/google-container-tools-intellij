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
