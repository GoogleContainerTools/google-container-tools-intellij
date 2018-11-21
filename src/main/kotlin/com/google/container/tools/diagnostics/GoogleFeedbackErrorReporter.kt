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

package com.google.container.tools.diagnostics

import com.google.common.annotations.VisibleForTesting
import com.google.container.tools.core.PluginInfo
import com.intellij.diagnostic.ReportMessages
import com.intellij.ide.DataManager
import com.intellij.ide.plugins.PluginManager
import com.intellij.idea.IdeaLogger
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.progress.EmptyProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.util.SystemInfo
import com.intellij.util.Consumer
import com.intellij.util.ExceptionUtil
import java.awt.Component

/**
 * This class hooks into IntelliJ's error reporting framework. It's based off of
 * [ErrorReporter.java](https://android.googlesource.com/platform/tools/adt/idea/+/studio-master-dev/android/src/com/android/tools/idea/diagnostics/error/ErrorReporter.java)
 * in Android Studio.
 */
class GoogleFeedbackErrorReporter : ErrorReportSubmitter() {

    override fun getReportActionText(): String {
        return ErrorReporterBundle.message("error.googlefeedback.message")
    }

    override fun submit(
        events: Array<IdeaLoggingEvent>,
        additionalInfo: String?,
        parentComponent: Component,
        consumer: Consumer<SubmittedReportInfo>
    ): Boolean {
        return doSubmit(
            events[0],
            parentComponent,
            consumer,
            IdeaLogger.ourLastActionId,
            additionalInfo
        )
    }

    companion object {

        @VisibleForTesting const val NONE_STRING = "__NONE___"
        @VisibleForTesting const val ERROR_MESSAGE_KEY = "error.message"
        private const val ERROR_STACKTRACE_KEY = "error.stacktrace"
        @VisibleForTesting const val ERROR_DESCRIPTION_KEY = "error.description"
        @VisibleForTesting const val LAST_ACTION_KEY = "last.action"
        private const val OS_NAME_KEY = "os.name"
        private const val JAVA_VERSION_KEY = "java.version"
        private const val JAVA_VM_VENDOR_KEY = "java.vm.vendor"
        @VisibleForTesting const val APP_NAME_KEY = "app.name"
        @VisibleForTesting const val APP_CODE_KEY = "app.code"
        @VisibleForTesting const val APP_NAME_VERSION_KEY = "app.name.version"
        @VisibleForTesting const val APP_EAP_KEY = "app.eap"
        @VisibleForTesting const val APP_INTERNAL_KEY = "app.internal"
        @VisibleForTesting const val APP_VERSION_MAJOR_KEY = "app.version.major"
        @VisibleForTesting const val APP_VERSION_MINOR_KEY = "app.version.minor"
        private const val PLUGIN_VERSION = "plugin.version"

        private fun doSubmit(
            event: IdeaLoggingEvent,
            parentComponent: Component,
            callback: Consumer<SubmittedReportInfo>,
            lastActionId: String,
            description: String?
        ): Boolean {
            val intelliJAppNameInfo = ApplicationNamesInfo.getInstance()
            val intelliJAppExtendedInfo = ApplicationInfoEx.getInstanceEx()

            val params = buildKeyValuesMap(
                event,
                description,
                lastActionId,
                intelliJAppNameInfo,
                intelliJAppExtendedInfo,
                ApplicationManager.getApplication()
            )

            val dataContext = DataManager.getInstance().getDataContext(parentComponent)
            val project = CommonDataKeys.PROJECT.getData(dataContext)

            val successCallback: (String) -> Unit = { token ->
                val reportInfo = SubmittedReportInfo(
                    null,
                    "Issue $token",
                    SubmittedReportInfo.SubmissionStatus.NEW_ISSUE
                )
                callback.consume(reportInfo)

                ReportMessages.GROUP
                    .createNotification(
                        ReportMessages.ERROR_REPORT, "Submitted", NotificationType.INFORMATION, null
                    )
                    .setImportant(false)
                    .notify(project)
            }

            val errorCallback: (Exception) -> Unit = { ex ->
                val message =
                    ErrorReporterBundle.message("error.googlefeedback.error", ex.message ?: "")
                ReportMessages.GROUP
                    .createNotification(
                        ReportMessages.ERROR_REPORT,
                        message,
                        NotificationType.ERROR,
                        NotificationListener.URL_OPENING_LISTENER
                    )
                    .setImportant(false)
                    .notify(project)
            }
            val task = GoogleFeedbackTask(
                project,
                ErrorReporterBundle.message("error.googlefeedback.submitting.error.message"),
                true,
                event.throwable,
                params,
                event.message ?: "",
                description ?: "",
                ApplicationInfo.getInstance().fullVersion,
                successCallback,
                errorCallback
            )
            if (project == null) {
                task.run(EmptyProgressIndicator())
            } else {
                ProgressManager.getInstance().run(task)
            }
            return true
        }

        @VisibleForTesting
        fun buildKeyValuesMap(
            event: IdeaLoggingEvent,
            description: String?,
            lastActionId: String,
            intelliJAppNameInfo: ApplicationNamesInfo,
            intelliJAppExtendedInfo: ApplicationInfoEx,
            application: Application
        ): Map<String, String> {

            return mapOf(
                // required parameters
                ERROR_MESSAGE_KEY to nullToNone(event.message),
                ERROR_STACKTRACE_KEY to nullToNone(getStacktrace(event)),
                // end or required parameters
                ERROR_DESCRIPTION_KEY to nullToNone(description),
                LAST_ACTION_KEY to nullToNone(lastActionId),
                OS_NAME_KEY to SystemInfo.OS_NAME,
                JAVA_VERSION_KEY to SystemInfo.JAVA_VERSION,
                JAVA_VM_VENDOR_KEY to SystemInfo.JAVA_VENDOR,
                APP_NAME_KEY to intelliJAppNameInfo.fullProductName,
                APP_CODE_KEY to intelliJAppExtendedInfo.packageCode!!,
                APP_NAME_VERSION_KEY to intelliJAppExtendedInfo.versionName,
                APP_EAP_KEY to java.lang.Boolean.toString(intelliJAppExtendedInfo.isEAP),
                APP_INTERNAL_KEY to java.lang.Boolean.toString(application.isInternal),
                APP_VERSION_MAJOR_KEY to intelliJAppExtendedInfo.majorVersion,
                APP_VERSION_MINOR_KEY to intelliJAppExtendedInfo.minorVersion,
                PLUGIN_VERSION to nullToNone(getPluginVersion()))
        }

        private fun getStacktrace(event: IdeaLoggingEvent): String? {
            return if (event.throwable != null) {
                ExceptionUtil.getThrowableText(event.throwable)
            } else {
                null
            }
        }

        private fun getPluginVersion(): String? {
            val pluginId = PluginId.getId(PluginInfo.CONTAINER_TOOLS_PLUGIN_ID)
            val ideaPluginDescriptor = PluginManager.getPlugin(pluginId)

            return if (ideaPluginDescriptor != null && !ideaPluginDescriptor.isBundled) {
                ideaPluginDescriptor.version
            } else {
                null
            }
        }

        @VisibleForTesting
        fun nullToNone(possiblyNullString: String?): String {
            return possiblyNullString ?: NONE_STRING
        }
    }
}