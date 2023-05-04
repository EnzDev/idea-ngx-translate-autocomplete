package fr.enzomallard.ngxtranslatetoolset.reference

import com.intellij.lang.javascript.hierarchy.JSHierarchyUtils
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.lang.javascript.psi.ecma6.TypeScriptFunction
import com.intellij.lang.javascript.psi.util.JSUtils
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import fr.enzomallard.ngxtranslatetoolset.psi.TranslationUtils

/**
 * Provide referencing for translation keys used in a pipe with 'translate' in html Angular template
 */
class TranslationReferenceProviderTS : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        // Check for a 'translate' function
        val function = JSHierarchyUtils
            .getTypeHierarchyTargetElement(element.parent.parent.children.first() as? JSReferenceExpression)
            .let { it as? TypeScriptFunction }
            ?.takeIf { it.name == TranslationUtils.INSTANT_KEYWORD || it.name == TranslationUtils.INSTANT_KEYWORD_2 }
            ?: return arrayOf()

        // Retrieve class from 'translate' function
        val functionClass = JSUtils.getMemberContainingClass(function)

        // Only reference if class is TranslateService
        return if (functionClass.jsType.typeText == "TranslateService") {
            arrayOf(TranslationReference(element as JSLiteralExpression, TextRange.allOf(element.text)))
        } else arrayOf()
    }
}
