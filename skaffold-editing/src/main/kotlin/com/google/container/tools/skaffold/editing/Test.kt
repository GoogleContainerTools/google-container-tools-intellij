package com.google.container.tools.skaffold.editing

import com.intellij.kubernetes.KubernetesModelAccessor
import com.intellij.kubernetes.KubernetesModelContext
import com.intellij.kubernetes.KubernetesModelProvider
import com.intellij.kubernetes.model.JsonSchemaManager
import com.intellij.kubernetes.model.KubernetesModel
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import java.io.File
import java.lang.reflect.Field
import java.nio.file.Path

class Test(val project: Project): ProjectComponent {
    init {
        //    SpecInfo.loadFromFile()
//    KubernetesModel.loadFromExternalSpec()
//    ServiceManager.getService(KubernetesModelProvider::class.java)
//        val resource= Test::class.java.getResource("definition.json").path
//        val path: Path = File(Test::class.java.classLoader.getResource("/definition.json").file).toPath()
        val path: Path = File("/Users/eshaul/dev/google-container-tools-intellij/skaffold-editing/src/main/resources/definition.json").toPath()

        val newModel: KubernetesModel =  KubernetesModel.loadFromExternalSpec(path)

        val modelProvider: KubernetesModelProvider = ServiceManager.getService(KubernetesModelProvider::class.java)
        val goodContext: KubernetesModelContext = modelProvider.useModel(project)

        val modelAccessor: KubernetesModelAccessor =
            ServiceManager.getService(project, KubernetesModelAccessor::class.java)
        val contextField: Field = modelAccessor::class.java.getDeclaredField("myModelContext")
        contextField.isAccessible = true
        contextField.set(modelAccessor, TestModelContext(goodContext, newModel))
        println(modelAccessor.model)

//        (contextField.get(modelAccessor) as KubernetesModelContext) = TestModelContext()
//        updateme = TestModelContext(updateme)
//        (contextField.get(modelAccessor) as KubernetesModelContext)= null
//        context = TestModelContext(context)
//        val model: KubernetesModel = contextField.get(modelAccessor) as KubernetesModel

//        println(model)
    }
}


class TestModelContext(val wrappedContext: KubernetesModelContext, val newModel: KubernetesModel): KubernetesModelContext {
    override val jsonSchemaManager: JsonSchemaManager
        get() = wrappedContext.jsonSchemaManager
    override val model: KubernetesModel
        get()  {
            val wrappedModel = wrappedContext.model

            return newModel
        }// todo, update the model here???

    override fun release() {
        wrappedContext.release()
    }
}

