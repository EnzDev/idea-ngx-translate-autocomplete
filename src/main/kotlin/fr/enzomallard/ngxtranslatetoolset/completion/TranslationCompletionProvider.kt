package fr.enzomallard.ngxtranslatetoolset.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.util.ProcessingContext
import com.intellij.util.ui.ColorIcon
import fr.enzomallard.ngxtranslatetoolset.NgTranslateToolsetBundle
import fr.enzomallard.ngxtranslatetoolset.psi.translation.TranslationUtils
import java.awt.Color

class TranslationCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        (parameters.originalPosition?.parent as JSLiteralExpression?)?.let { it ->
            val partiallyProvidedPath = it.stringValue ?: return
            val candidates = TranslationUtils
                .findTranslationPartialKey(it.project, partiallyProvidedPath.split('.'))
                .flatMap { item ->
                    item.value.map { json ->
                        if (json is JsonStringLiteral) { // End key
                            LookupElementBuilder
                                .create(item.key)
                                .withIcon(ColorIcon(TranslationUtils.ICON_SIZE, Color.BLUE))
                                .withTailText("=${json.value}", true)
                                .withTypeText(NgTranslateToolsetBundle.message("translation_key_leaf"), true)
                                .withPsiElement(json)
                        } else LookupElementBuilder // Intermediate key
                            .create(item.key + ".") // Append '.' to continue completion
                            .withPresentableText(item.key)
                            .withIcon(ColorIcon(TranslationUtils.ICON_SIZE, Color.CYAN))
                            .withTypeText(NgTranslateToolsetBundle.message("translation_key_node"), true)
                    }
                }

            result.withPrefixMatcher(partiallyProvidedPath).addAllElements(candidates)
        }
    }
}
