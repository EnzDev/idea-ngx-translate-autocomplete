package com.github.enzdev.ideangxtranslateautocomplete.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.patterns.PlatformPatterns
import org.angular2.lang.expr.psi.Angular2Interpolation


class TranslationReferenceCompletion : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                .withParent(JSLiteralExpression::class.java)
                .inside(Angular2Interpolation::class.java),
            TranslationCompletionProvider()
        )
    }
}
