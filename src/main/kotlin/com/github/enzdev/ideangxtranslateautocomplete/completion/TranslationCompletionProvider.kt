package com.github.enzdev.ideangxtranslateautocomplete.completion

import com.github.enzdev.ideangxtranslateautocomplete.psi.translation.TranslationUtils
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.util.ProcessingContext
import com.intellij.util.ui.ColorIcon
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
                        if (json is JsonStringLiteral) // End key
                            LookupElementBuilder
                                .create(item.key)
                                .withIcon(ColorIcon(8, Color.BLUE))
                                .withTypeText(json.value, true)
                                .withPsiElement(json)
                        else LookupElementBuilder // Intermediate key
                            .create(item.key + ".") // Append '.' to continue completion
                            .withPresentableText(item.key)
                            .withIcon(ColorIcon(8, Color.CYAN))
                            .withTypeText("Translation partial key", true)
                    }
                }

            result.withPrefixMatcher(partiallyProvidedPath).addAllElements(candidates)
        }
    }
}