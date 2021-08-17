package com.github.enzdev.ideangxtranslateautocomplete.referenceContributor

import com.github.enzdev.ideangxtranslateautocomplete.reference.TranslationHtmlReference
import com.github.enzdev.ideangxtranslateautocomplete.reference.TranslationPipeReference
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.html.HtmlTag
import com.intellij.psi.xml.XmlText
import com.intellij.util.ProcessingContext

/**
 * Provide referencing for translation keys used in a pipe with 'translate' in html Angular template
 */
class TranslationPipeReferenceProvider : PsiReferenceProvider() {

    // TODO: Implement i18n field replacement with variable names
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        val leftSide = element as JSLiteralExpression

        // Check for translation
        // Bubble up until right side is matched to translate
        var parentPointer: PsiElement = leftSide
        var validated = false
        while (parentPointer !is HtmlTag || !validated) {
            parentPointer = parentPointer.parent
            if (parentPointer is HtmlTag) { // Check for expr|translate
                validated = parentPointer
                    .children
                    .filterIsInstance(HtmlTag::class.java)
                    .firstOrNull()
                    ?.textMatches("translate")
                    ?: false
            }
        }

        return if (validated) arrayOf(TranslationPipeReference(element, element.textRange)) else arrayOf()
    }
}

/**
 * Provide referencing for translation keys used in a html tag with a 'translate' attribute in html Angular template
 */
class TranslationAttributeReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        val text = element as XmlText

        // Check for translation
        // Bubble up to find tag with translate attribute
        var parentPointer: PsiElement = text
        var validated = false
        while (parentPointer !is HtmlTag || !validated) { // Should not visit more than one html tag
            parentPointer = parentPointer.parent
            if (parentPointer is HtmlTag) { // Check for HtmlTag
                validated = parentPointer
                    .children
                    .firstOrNull { it.text.matches("translate=?".toRegex()) }
                    ?.let { true }
                    ?: false
            }
        }

        return if (validated) arrayOf(TranslationHtmlReference(element, element.textRange)) else arrayOf()
    }
}