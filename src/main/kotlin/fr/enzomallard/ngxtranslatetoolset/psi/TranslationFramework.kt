package fr.enzomallard.ngxtranslatetoolset.psi

import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern.Capture
import org.angular2.lang.expr.psi.Angular2PipeReferenceExpression

data class TranslationFramework(val pipeName: String, val functionName: String) {
    val callPattern: Capture<JSReferenceExpression>
        get() = PlatformPatterns.psiElement(JSReferenceExpression::class.java)
            .withChild(
                PlatformPatterns.psiElement()
                    .withText(functionName)
            )

    val pipePattern: Capture<Angular2PipeReferenceExpression>
        get() = PlatformPatterns.psiElement(Angular2PipeReferenceExpression::class.java)
            .withChild(
                PlatformPatterns.psiElement()
                    .withText(pipeName)
            )
}