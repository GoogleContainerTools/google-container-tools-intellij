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

package com.google.container.tools.skaffold.run.ui

import com.google.common.truth.Truth.assertThat
import com.google.container.tools.test.ContainerToolsRule
import com.google.container.tools.test.UiTest
import org.junit.Rule
import org.junit.Test

/** Unit tests for [SkaffoldProfilesComboBox] */
class SkaffoldProfilesComboBoxTest {
    @get:Rule
    val containerToolsRule = ContainerToolsRule(this)

    @Test
    @UiTest
    fun `no skaffold yaml file results in empty disabled profiles combobox`() {
        val profilesComboBox = SkaffoldProfilesComboBox()

        assertThat(profilesComboBox.model.size).isEqualTo(0)
        assertThat(profilesComboBox.isEnabled).isFalse()
    }

    @Test
    @UiTest
    fun `no skaffold yaml file results in null profile selection`() {
        val profilesComboBox = SkaffoldProfilesComboBox()

        assertThat(profilesComboBox.getSelectedProfile()).isNull()
    }
}
