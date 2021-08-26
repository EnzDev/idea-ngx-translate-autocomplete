package fr.enzomallard.ngxtranslatetoolset.reference

import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import fr.enzomallard.ngxtranslatetoolset.psi.TranslationUtils
import fr.enzomallard.ngxtranslatetoolset.psi.toTypedResolveResult

class TranslationReference(element: JSLiteralExpression, textRange: TextRange) :
    PsiReferenceBase.Poly<PsiElement?>(element, textRange, false), PsiPolyVariantReference {
    private val path: List<String>? = element.stringValue?.split('.')

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> =
        TranslationUtils
            .findTranslationKey(ModuleUtilCore.findModuleForPsiElement(element), myElement!!.project, path)
            .toTypedResolveResult()
}
