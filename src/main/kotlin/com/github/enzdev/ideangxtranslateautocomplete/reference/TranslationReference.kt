package com.github.enzdev.ideangxtranslateautocomplete.reference

import com.github.enzdev.ideangxtranslateautocomplete.psi.translation.TranslationUtils
import com.github.enzdev.ideangxtranslateautocomplete.psi.translation.toTypedResolveResult
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.psi.xml.XmlText

class TranslationPipeReference(element: JSLiteralExpression, textRange: TextRange) :
    PsiReferenceBase<PsiElement?>(element, textRange), PsiPolyVariantReference {
    private val path: List<String>? = element.stringValue?.split('.')

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> =
        TranslationUtils
            .findTranslationKey(myElement!!.project, path)
            .toTypedResolveResult()

    override fun resolve(): PsiElement? =
        multiResolve(false).firstOrNull()?.element
}

class TranslationHtmlReference(element: XmlText, textRange: TextRange) :
    PsiReferenceBase<PsiElement?>(element, textRange), PsiPolyVariantReference {
    private val path: List<String> = element.value.split('.')

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> =
        TranslationUtils
            .findTranslationKey(myElement!!.project, path)
            .toTypedResolveResult()

    override fun resolve(): PsiElement? =
        multiResolve(false).firstOrNull()?.element

}

