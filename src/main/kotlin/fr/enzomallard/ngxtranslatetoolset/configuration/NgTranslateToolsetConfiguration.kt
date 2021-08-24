package fr.enzomallard.ngxtranslatetoolset.configuration

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import org.jdom.Element

@State(name = "NgTranslateToolsetConfiguration", storages = [Storage(StoragePathMacros.WORKSPACE_FILE)])
class NgTranslateToolsetConfiguration : PersistentStateComponent<Element> {
    var lang: String = ""
        private set
    var i18nPath: String = ""
        private set

    override fun getState(): Element {
        val element = Element("state")
        element.addContent(Element(ELEMENT_LANG).apply { text = lang })
        element.addContent(Element(ELEMENT_I18N_PATH).apply { text = i18nPath })
        return element
    }

    override fun loadState(state: Element) {
        lang = StringUtil.notNullize(state.getChildText(ELEMENT_LANG))
        i18nPath = StringUtil.notNullize(state.getChildText(ELEMENT_I18N_PATH))
    }

    fun saveConfiguration(newLang: String, newI18nPath: String) {
        lang = newLang
        i18nPath = newI18nPath
    }

    companion object {
        const val ELEMENT_LANG = "lang"
        const val ELEMENT_I18N_PATH = "i18nPath"

        fun getInstance(project: Project): NgTranslateToolsetConfiguration = ServiceManager
            .getService(project, NgTranslateToolsetConfiguration::class.java)

        fun getJsonTranslationPath(project: Project) = getInstance(project)
            .i18nPath
            .let { FileUtil.toSystemDependentName(it) }

         fun getJsonTranslationFile(project: Project) = getInstance(project)
            .lang
            .let { FileUtil.toSystemDependentName(it) }
    }
}