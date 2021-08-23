package fr.enzomallard.ngxtranslatetoolset.psi.translation

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
import org.jetbrains.annotations.NonNls
import java.nio.file.FileSystems

/**
 * Provide simple shorthand to convert the List of JsonValue into a ResolveResult
 */
fun List<JsonValue>.toTypedResolveResult(): Array<ResolveResult> = this
    .map(::PsiElementResolveResult)
    .toTypedArray()

object TranslationUtils {
    @NonNls
    const val TRANSLATION_KEYWORD = "translate"
    const val ICON_SIZE = 8

    // Path element filter for Translation location
    private val ASSETS_PATH = FileSystems.getDefault().getPath("assets")

    fun findTranslationKey(project: Project, path: List<String>?) =
        // Don't bother filtering on files if path is empty
        if (path == null || path.isEmpty()) emptyList<JsonStringLiteral>()
        else getJsonAssets(project).mapNotNull {
            val jsonTranslationPath = path.listIterator()

            val jsonFile: JsonFile? = PsiManager.getInstance(project).findFile(it) as JsonFile?
            var jsonValue: JsonValue? = JsonUtil.getTopLevelObject(jsonFile)

            while (jsonValue != null && jsonValue is JsonObject && jsonTranslationPath.hasNext()) {
                jsonValue = jsonValue
                    .findProperty(jsonTranslationPath.next())
                    ?.value
            }

            if (!jsonTranslationPath.hasNext() && jsonValue is JsonStringLiteral) jsonValue else null
        }

    private fun recurseKeysWithFilter(
        value: JsonValue,
        lookupKey: List<String>,
        keys: List<String> = listOf()
    ): Map<String, JsonValue> {
        val localMap = if (keys.isEmpty()) { // Prevent returning root
            mapOf()
        } else mapOf(keys.joinToString(".") to value)

        return when (value) {
            is JsonObject ->
                value.propertyList
                .filter {
                    (lookupKey.isEmpty() || it.name.matches(".*${lookupKey.first()}.*".toRegex()))
                }.map { prop ->
                    ProgressManager.checkCanceled() // Check cancel before continuing recursion
                    recurseKeysWithFilter(prop.value!!, lookupKey.drop(1), keys + prop.name)
                }.fold(localMap) { acc, map -> acc + map } // Iterate and filter key
            is JsonStringLiteral -> mapOf(keys.joinToString(".") to value)
            else -> throw IllegalArgumentException(
                "JsonValue should be JsonObject or JsonStringLiteral in translation files"
            )
        }
    }

    fun findTranslationPartialKey(project: Project, path: List<String>?): Map<String, List<JsonValue>> {
        if (path == null || path.isEmpty()) return emptyMap() // Don't bother filtering on files if path is empty

        val results = mutableMapOf<String, List<JsonValue>>()

        // Provide better filtering on translation files
        getJsonAssets(project).forEach {
            ProgressManager.checkCanceled()
            val jsonFile: JsonFile = PsiManager.getInstance(project).findFile(it) as JsonFile? ?: return@forEach
            ProgressManager.checkCanceled()

            val jsonValue: JsonValue? = JsonUtil.getTopLevelObject(jsonFile)

            if (jsonValue != null) {
                val keysForFile = recurseKeysWithFilter(jsonValue, path)
                keysForFile.forEach { (k, v) ->
                    results.compute(k) { _, l -> if (l == null) listOf(v) else l + v }
                }
            }
        }

        return results
    }

    // Provide better filtering on translation files
    private fun getJsonAssets(project: Project) = FileTypeIndex
        .getFiles(JsonFileType.INSTANCE, GlobalSearchScope.allScope(project))
        .filter {
            it.toNioPath().contains(ASSETS_PATH) // Filter JSON in assets folder
        }
}
