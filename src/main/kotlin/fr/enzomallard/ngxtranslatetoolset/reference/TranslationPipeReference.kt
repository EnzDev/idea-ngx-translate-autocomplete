package fr.enzomallard.ngxtranslatetoolset.reference

import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import fr.enzomallard.ngxtranslatetoolset.psi.translation.TranslationUtils
import fr.enzomallard.ngxtranslatetoolset.psi.translation.toTypedResolveResult

class TranslationPipeReference(element: JSLiteralExpression, textRange: TextRange) :
    PsiReferenceBase.Poly<PsiElement?>(element, textRange, false), PsiPolyVariantReference {
    private val path: List<String>? = element.stringValue?.split('.')

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> =
        TranslationUtils
            .findTranslationKey(myElement!!.project, path)
            .toTypedResolveResult()
}
