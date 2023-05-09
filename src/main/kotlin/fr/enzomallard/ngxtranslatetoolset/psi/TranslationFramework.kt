package fr.enzomallard.ngxtranslatetoolset.psi

import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.patterns.PlatformPatterns
import org.angular2.lang.expr.psi.Angular2PipeReferenceExpression

data class TranslationFramework(val pipeName: String, val functionName: String) {
    val callPattern
        get() = PlatformPatterns.psiElement(JSReferenceExpression::class.java)
            .withChild(
                PlatformPatterns.psiElement()
                    .withText(functionName)
            )

    val pipePattern
        get() = PlatformPatterns.psiElement(Angular2PipeReferenceExpression::class.java)
            .withChild(
                PlatformPatterns.psiElement()
                    .withText(pipeName)
            )
}