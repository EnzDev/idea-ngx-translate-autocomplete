package com.github.enzdev.ideangxtranslateautocomplete.completion

import com.github.enzdev.ideangxtranslateautocomplete.psi.translation.TranslationUtils
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.util.ProcessingContext

class TranslationCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        (parameters.originalPosition?.parent as JSLiteralExpression?)?.let { it ->
            val candidates = TranslationUtils
                .findTranslationPartialKey(it.project, it.stringValue?.split('.'))
                .map { item ->
                    LookupElementBuilder.create(item.key)
                }

            result.addAllElements(candidates)
        }
    }
}