package com.github.enzdev.ideangxtranslateautocomplete.referenceContributor

import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.psi.xml.XmlText


class TranslationReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(JSLiteralExpression::class.java),
            TranslationPipeReferenceProvider()
        )
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(XmlText::class.java),
            TranslationAttributeReferenceProvider()
        )
    }
}
