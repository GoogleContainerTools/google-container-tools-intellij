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

import com.intellij.debugger.engine.RemoteStateState
import com.intellij.debugger.impl.GenericDebuggerRunner
import com.intellij.execution.ExecutionManager
import com.intellij.execution.configurations.RemoteConnection
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.impl.ExecutionManagerImpl
import com.intellij.execution.impl.RunConfigurationLevel
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.execution.remote.RemoteConfiguration
import com.intellij.execution.runners.DefaultProgramRunner
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.RunContentDescriptor

class SkaffoldDebugProgramRunner : DefaultProgramRunner() {
    override fun canRun(executorId: String, profile: RunProfile): Boolean {
        return profile is AbstractSkaffoldRunConfiguration && executorId == DefaultDebugExecutor.EXECUTOR_ID
//        return false
    }

    override fun getRunnerId(): String {
        return "SkaffoldDebugProgramRunner"
    }

    // do stuff before the command line state is executed
    override fun doExecute(
        state: RunProfileState,
        environment: ExecutionEnvironment
    ): RunContentDescriptor? {

        val javaCommandLine = state as SkaffoldCommandLineState

//        attachVirtualMachine(state, environment, RemoteConnection(true, "127.0.0.1", "5006", false), true)
        val remote505 = RemoteConnection(true, "127.0.0.1", "5005", false)
        val remote506 = RemoteConnection(true, "127.0.0.1", "5006", false)
        val runnerAndConfig = RunnerAndConfigurationSettingsImpl(
            (environment.runnerAndConfigurationSettings as RunnerAndConfigurationSettingsImpl).manager,
            RemoteConfiguration(environment.project, SkaffoldDebugRunConfigurationType()),
            false,
            RunConfigurationLevel.WORKSPACE
        )
        runnerAndConfig.name = "debug: catalog:5005"
        val newEnv1 = ExecutionEnvironment(
            environment.executor,
            environment.runner,
            runnerAndConfig,
            environment.project
        )
        val newEnv2 = ExecutionEnvironment(
            environment.executor,
            environment.runner,
            runnerAndConfig,
            environment.project
        )

        val gen = object : GenericDebuggerRunner() {
            override fun doExecute(
                state: RunProfileState,
                environment: ExecutionEnvironment
            ): RunContentDescriptor? {
                return attachVirtualMachine(
                    RemoteStateState(environment.project, remote505),
                    newEnv1,
                    remote505,
                    true
                )
            }
        }
        gen.execute(newEnv1)

        runnerAndConfig.name = "debug: order:5006"

        val gen2 = object : GenericDebuggerRunner() {
            override fun doExecute(
                state: RunProfileState,
                environment: ExecutionEnvironment
            ): RunContentDescriptor? {
                return attachVirtualMachine(
                    RemoteStateState(environment.project, remote506),
                    newEnv2,
                    remote506,
                    true
                )
            }
        }
        gen2.execute(newEnv2)
//        newEnv.contentToReuse
        // See StopAction
        //  ExecutionManagerImpl.stopProcess(getRecentlyStartedContentDescriptor(dataContext));
//        environment.dataContext = listOf(gen, gen2)

        state.killCallack = {
           ExecutionManagerImpl.stopProcess(newEnv1.contentToReuse)
            ExecutionManagerImpl.stopProcess(newEnv2.contentToReuse)

            ExecutionManager.getInstance(environment.project).contentManager.removeRunContent(newEnv1.executor, newEnv1.contentToReuse!!)
            ExecutionManager.getInstance(environment.project).contentManager.removeRunContent(newEnv2.executor, newEnv2.contentToReuse!!)
        }

        return super.doExecute(
            state,
            environment
//            ExecutionEnvironment(
//                environment.executor,
////                DefaultDebugExecutor.getDebugExecutorInstance(),
//                environment.runner,
//                environment.runnerAndConfigurationSettings!!,
//                environment.project
//            )
        )
//        val allconfigs = RunManager.getInstance(environment.project).allConfigurationsList
//        return attachVirtualMachine(state, environment, RemoteConnection(true, "127.0.0.1", "5005", false), true)
    }
}