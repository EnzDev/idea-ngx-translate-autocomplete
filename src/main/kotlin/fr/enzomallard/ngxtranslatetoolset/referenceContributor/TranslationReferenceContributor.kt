package fr.enzomallard.ngxtranslatetoolset.referenceContributor

import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import org.angular2.lang.expr.psi.Angular2PipeLeftSideArgument

class TranslationReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(JSLiteralExpression::class.java)
                .inside(Angular2PipeLeftSideArgument::class.java),
            TranslationPipeReferenceProvider()
        )
    }
}
