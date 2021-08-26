package fr.enzomallard.ngxtranslatetoolset.reference

import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.html.HtmlTag
import com.intellij.util.ProcessingContext
import fr.enzomallard.ngxtranslatetoolset.psi.TranslationUtils
import org.angular2.lang.expr.psi.Angular2PipeExpression
import org.angular2.lang.expr.psi.Angular2PipeReferenceExpression

/**
 * Provide referencing for translation keys used in a pipe with 'translate' in html Angular template
 */
class TranslationReferenceProviderPipe : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        if (element !is JSLiteralExpression) {
            return arrayOf()
        }

        // Check for translation
        // Bubble up until right side is matched to translate
        var parentPointer: PsiElement = element
        var validated = false
        while (parentPointer !is HtmlTag && !validated) {
            parentPointer = parentPointer.parent
            if (parentPointer is Angular2PipeExpression) { // Check for expr|translate
                validated = parentPointer
                    .children
                    .filterIsInstance<Angular2PipeReferenceExpression>()
                    .firstOrNull()
                    ?.textMatches(TranslationUtils.TRANSLATION_KEYWORD)
                    ?: false
            }
        }

        return if (validated) arrayOf(TranslationReference(element, TextRange.allOf(element.text)))
        else arrayOf()
    }
}
