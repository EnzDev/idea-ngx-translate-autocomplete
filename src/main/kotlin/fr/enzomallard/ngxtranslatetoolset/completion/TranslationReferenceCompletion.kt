package fr.enzomallard.ngxtranslatetoolset.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import fr.enzomallard.ngxtranslatetoolset.psi.ElementPatterns

class TranslationReferenceCompletion : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withParent(ElementPatterns.HTML_TRANSLATION_PLATFORM_PATTERN),
            TranslationCompletionProvider()
        )

        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withParent(ElementPatterns.TS_TRANSLATION_PLATFORM_PATTERN),
            TranslationCompletionProvider()
        )
    }
}
