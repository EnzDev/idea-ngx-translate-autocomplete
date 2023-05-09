package fr.enzomallard.ngxtranslatetoolset.psi

import com.intellij.lang.javascript.psi.JSArgumentList
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSLiteralExpression
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern.Capture
import org.angular2.lang.expr.psi.Angular2PipeExpression
import org.angular2.lang.expr.psi.Angular2PipeReferenceExpression

object ElementPatterns {
    private val TS_CALL_EXP_INSTANT = PlatformPatterns
        .psiElement(JSCallExpression::class.java)
        .withChild(
            PlatformPatterns.or(
                PlatformPatterns
                    .psiElement(JSReferenceExpression::class.java)
                    .withChild(
                        PlatformPatterns
                            .psiElement()
                            .withText(TranslationUtils.INSTANT_KEYWORD)
                    ),
                PlatformPatterns
                    .psiElement(JSReferenceExpression::class.java)
                    .withChild(
                        PlatformPatterns
                            .psiElement()
                            .withText(TranslationUtils.INSTANT_KEYWORD_2)
                    )
            )
        )

    private val HTML_PIPE_EXP_TRANSLATE: Capture<Angular2PipeExpression> = PlatformPatterns
        .psiElement(Angular2PipeExpression::class.java)
        .withChild(
            PlatformPatterns.or(
                PlatformPatterns
                    .psiElement(Angular2PipeReferenceExpression::class.java)
                    .withText(TranslationUtils.TRANSLATION_KEYWORD),
                PlatformPatterns
                    .psiElement(Angular2PipeReferenceExpression::class.java)
                    .withText(TranslationUtils.TRANSLATION_KEYWORD_2)
            )
        )

    val HTML_TRANSLATION_PLATFORM_PATTERN: Capture<JSLiteralExpression> =
        PlatformPatterns
            .psiElement(JSLiteralExpression::class.java)
            .inside(HTML_PIPE_EXP_TRANSLATE)

    val TS_TRANSLATION_PLATFORM_PATTERN: Capture<JSLiteralExpression> = PlatformPatterns
        .psiElement(JSLiteralExpression::class.java)
        .withParent(
            PlatformPatterns.psiElement(JSArgumentList::class.java)
                .withParent(TS_CALL_EXP_INSTANT)
        )
}
