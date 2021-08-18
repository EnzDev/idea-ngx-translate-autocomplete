package com.github.enzdev.ideangxtranslateautocomplete.psi.translation

import com.intellij.json.JsonFileType
import com.intellij.json.JsonUtil
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.json.psi.JsonValue
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiManager
import com.intellij.psi.ResolveResult
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope


/**
 * Provide simple shorthand to convert the List of JsonValue into a ResolveResult
 */
fun List<JsonValue>.toTypedResolveResult(): Array<ResolveResult> = this
    .map(::PsiElementResolveResult)
    .toTypedArray()

object TranslationUtils {
    fun findTranslationKey(project: Project, path: List<String>?): List<JsonValue> {
        if (path == null || path.isEmpty()) return emptyList() // Don't bother filtering on files if path is empty

        // Provide better filtering on translation files
        return FileTypeIndex.getFiles(JsonFileType.INSTANCE, GlobalSearchScope.allScope(project))
            .filter {
                it.path.subSequence(project.basePath?.length ?: 0, it.path.length).contains("/assets/")
            }.mapNotNull {
                val jsonTranslationPath = path.listIterator()

                val simpleFile: JsonFile? = PsiManager.getInstance(project).findFile(it) as JsonFile?
                var jsonValue: JsonValue? = JsonUtil.getTopLevelObject(simpleFile)

                while (jsonValue != null && jsonValue is JsonObject && jsonTranslationPath.hasNext()) {
                    jsonValue = jsonValue
                        .findProperty(jsonTranslationPath.next())
                        ?.value
                }

                if (!jsonTranslationPath.hasNext() && jsonValue is JsonStringLiteral) jsonValue else null
            }
    }

    fun findTranslationPartialKey(project: Project, path: List<String>?): Map<String, List<JsonValue>> {
        if (path == null || path.isEmpty()) return emptyMap() // Don't bother filtering on files if path is empty

        val results = mutableMapOf<String, List<JsonValue>>()

        // Provide better filtering on translation files
        FileTypeIndex.getFiles(JsonFileType.INSTANCE, GlobalSearchScope.allScope(project))
            .filter {
                it.path.subSequence(project.basePath?.length ?: 0, it.path.length).contains("/assets/")
            }.onEach {
                ProgressManager.checkCanceled()

                val jsonTranslationPath = path.listIterator()

                val simpleFile: JsonFile? = PsiManager.getInstance(project).findFile(it) as JsonFile?
                var jsonValue: JsonValue? = JsonUtil.getTopLevelObject(simpleFile)
                var constructedPath: String? = null

                while (jsonValue != null && jsonTranslationPath.hasNext()) {
                    val fragment = jsonTranslationPath.next()
                    if (jsonValue is JsonObject && jsonTranslationPath.hasNext()) { // Continue
                        jsonValue = jsonValue.findProperty(fragment)?.let { prop ->
                            constructedPath = constructedPath?.plus(".${prop.name}") ?: prop.name
                            prop.value
                        }
                    } else if (jsonValue is JsonObject) { // Find any candidate
                        jsonValue.propertyList
                            .filter { prop -> prop.name.matches("$fragment.*".toRegex()) }
                            .onEach { prop ->
                                results.compute("$constructedPath.${prop.name}") { _, list ->
                                    list?.plus(jsonValue) ?: listOf(jsonValue)
                                }
                            }
                    } else if (jsonValue is JsonStringLiteral) {
                        (constructedPath?.plus(".${jsonValue.name}") ?: jsonValue.name)?.let { path ->
                            results.compute(path) { _, list ->
                                list?.plus(jsonValue) ?: listOf(jsonValue)
                            }
                        }
                    } else {
                        break
                    }
                }
            }

        return results
    }

}
