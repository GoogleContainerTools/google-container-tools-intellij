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

import com.google.common.truth.Truth.assertThat
import com.google.container.tools.skaffold.SkaffoldFileService
import com.google.container.tools.test.ContainerToolsRule
import com.google.container.tools.test.TestService
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/** Unit tests for [SkaffoldFilesComboBox] */
class SkaffoldFilesComboBoxTest {
    @get:Rule
    val containerToolsRule = ContainerToolsRule(this)

    @MockK
    @TestService
    lateinit var mockSkaffoldFileService: SkaffoldFileService

    private lateinit var skaffoldFilesComboBox: SkaffoldFilesComboBox

    @Before
    fun setUp() {
        skaffoldFilesComboBox = SkaffoldFilesComboBox()
    }

    @Test
    fun `empty combobox for empty project with no files`() {
        val project = containerToolsRule.ideaProjectTestFixture.project
        every { mockSkaffoldFileService.findSkaffoldFiles(project) } returns listOf()
        skaffoldFilesComboBox.setProject(project)

        assertThat(skaffoldFilesComboBox.getSelectedSkaffoldFile()).isNull()
    }
}