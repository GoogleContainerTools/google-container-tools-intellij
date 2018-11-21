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

import com.android.tools.idea.diagnostics.error.AnonymousFeedback
import com.google.common.annotations.VisibleForTesting
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.util.net.HttpConfigurable
import java.io.IOException
import java.net.HttpURLConnection

/** Reports an error to Google Feedback in the background.  */
class GoogleFeedbackTask @VisibleForTesting
constructor(
    project: Project?,
    title: String,
    canBeCancelled: Boolean,
    private val throwable: Throwable?,
    private val params: Map<String, String>,
    private val errorMessage: String,
    private val errorDescription: String,
    private val appVersion: String,
    private val callback: (String) -> Unit,
    private val errorCallback: (Exception) -> Unit,
    private val feedbackSender: FeedbackSender
) : Task.Backgroundable(project, title, canBeCancelled) {

    companion object {
        @VisibleForTesting
        val CONTAINER_TOOLS_PRODUCT = "Container Tools for IntelliJ"
        @VisibleForTesting
        val CONTAINER_TOOLS_PACKAGE_NAME = "com.google.container.tools"
        private val DEFAULT_FEEDBACK_SENDER = NetworkFeedbackSender()
    }

    /**
     * Default constructor that creates a feedback task with the default [NetworkFeedbackSender].
     */
    constructor(
        project: Project?,
        title: String,
        canBeCancelled: Boolean,
        throwable: Throwable?,
        params: Map<String, String>,
        errorMessage: String,
        errorDescription: String,
        appVersion: String,
        callback: (String) -> Unit,
        errorCallback: (Exception) -> Unit
    ) : this(
        project,
        title,
        canBeCancelled,
        throwable,
        params,
        errorMessage,
        errorDescription,
        appVersion,
        callback,
        errorCallback,
        DEFAULT_FEEDBACK_SENDER
    )

    override fun run(indicator: ProgressIndicator) {
        indicator.isIndeterminate = true
        try {
            val token = feedbackSender.sendFeedback(
                CONTAINER_TOOLS_PRODUCT,
                CONTAINER_TOOLS_PACKAGE_NAME,
                throwable,
                errorMessage,
                errorDescription,
                appVersion,
                params
            )
            callback(token)
        } catch (ioe: IOException) {
            errorCallback(ioe)
        } catch (re: RuntimeException) {
            errorCallback(re)
        }
    }

    /** Interface for sending feedback crash reports.  */
    interface FeedbackSender {

        @Throws(IOException::class)
        fun sendFeedback(
            feedbackProduct: String,
            feedbackPackageName: String,
            cause: Throwable?,
            errorMessage: String,
            errorDescription: String,
            applicationVersion: String,
            keyValues: Map<String, String>
        ): String
    }

    private class ProxyHttpConnectionFactory : AnonymousFeedback.HttpConnectionFactory() {

        @Throws(IOException::class)
        override fun openHttpConnection(path: String): HttpURLConnection {
            return HttpConfigurable.getInstance().openHttpConnection(path)
        }
    }

    /**
     * [FeedbackSender] implementation that sends the feedback over the network.
     */
    private class NetworkFeedbackSender : FeedbackSender {
        companion object {
            private val connectionFactory = ProxyHttpConnectionFactory()
        }

        @Throws(IOException::class)
        override fun sendFeedback(
            feedbackProduct: String,
            feedbackPackageName: String,
            cause: Throwable?,
            errorMessage: String,
            errorDescription: String,
            applicationVersion: String,
            keyValues: Map<String, String>
        ): String {
            return AnonymousFeedback.sendFeedback(
                CONTAINER_TOOLS_PRODUCT,
                CONTAINER_TOOLS_PACKAGE_NAME,
                connectionFactory,
                cause,
                keyValues,
                errorMessage,
                errorDescription,
                applicationVersion
            )
        }
    }
}
