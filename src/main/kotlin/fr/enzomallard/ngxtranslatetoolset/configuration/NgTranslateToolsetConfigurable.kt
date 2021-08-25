package fr.enzomallard.ngxtranslatetoolset.configuration

import com.intellij.json.JsonFileType
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import fr.enzomallard.ngxtranslatetoolset.NgTranslateToolsetBundle
import java.awt.BorderLayout
import java.awt.Component
import javax.swing.JLabel
import javax.swing.JPanel

class NgTranslateToolsetConfigurable(private val project: Project) : SearchableConfigurable {
    private lateinit var i18nField: TextFieldWithBrowseButton
    private lateinit var langField: TextFieldWithBrowseButton

    override fun createComponent() = JPanel(BorderLayout(MAIN_PANEL_HGAP, MAIN_PANEL_VGAP)).apply {
        add(
            JPanel(VerticalFlowLayout(CONTENT_PANEL_GAP, CONTENT_PANEL_GAP)).apply {
                i18nField = createI18PathInput()
                langField = createLangInput()

                add(
                    wrapLabel(
                        i18nField,
                        NgTranslateToolsetBundle.message("configuration.label.translation_folder")
                    )
                )
                add(
                    wrapLabel(
                        langField,
                        NgTranslateToolsetBundle.message("configuration.label.default_translation")
                    )
                )
            },
            BorderLayout.NORTH
        )
    }

    private fun wrapLabel(field: Component, label: String) = JPanel(BorderLayout(LABEL_HGAP, LABEL_VGAP)).apply {
        add(JLabel(label), BorderLayout.WEST)
        add(field, BorderLayout.CENTER)
    }

    private fun createLangInput() = TextFieldWithBrowseButton().apply {

        addBrowseFolderListener(
            NgTranslateToolsetBundle.message("configuration.modal.title.default_translation"),
            NgTranslateToolsetBundle.message("configuration.modal.description.default_translation"),
            project,
            object : FileChooserDescriptor(true, false, false, false, false, false) {
                override fun isFileSelectable(file: VirtualFile) = isJsonFile(file)
            }
        )
    }

    private fun createI18PathInput() = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            NgTranslateToolsetBundle.message("configuration.modal.title.translation_folder"),
            NgTranslateToolsetBundle.message("configuration.modal.description.translation_folder"),
            project,
            object : FileChooserDescriptor(false, true, false, false, false, false) {
                override fun isFileSelectable(file: VirtualFile) = isJsonFolder(file)
            }
        )
    }

    override fun isModified(): Boolean {
        val configuration = NgTranslateToolsetConfiguration.getInstance(project)
        val modifiedLang = configuration.lang != FileUtil.toSystemIndependentName(langField.text)
        val modifiedI18n = configuration.i18nPath != FileUtil.toSystemIndependentName(i18nField.text)
        return modifiedLang || modifiedI18n
    }

    override fun apply() = NgTranslateToolsetConfiguration
        .getInstance(project)
        .saveConfiguration(
            FileUtil.toSystemIndependentName(langField.text),
            FileUtil.toSystemIndependentName(i18nField.text)
        )

    override fun reset() {
        val configuration = NgTranslateToolsetConfiguration.getInstance(project)
        i18nField.text = FileUtil.toSystemDependentName(configuration.i18nPath)
        langField.text = FileUtil.toSystemDependentName(configuration.lang)
    }

    override fun getDisplayName() = NgTranslateToolsetBundle.message("name")

    override fun getId() = "fr.enzomallard.ngxtranslatetoolset.configuration.ProjectSettingsConfigurable"

    override fun disposeUIResources() {
        i18nField.dispose()
        langField.dispose()
    }

    companion object {
        private fun isJsonFolder(file: VirtualFile) =
            file.isDirectory && file.children.all { it.fileType === JsonFileType.INSTANCE }

        private fun isJsonFile(file: VirtualFile) = file.fileType === JsonFileType.INSTANCE

        private const val MAIN_PANEL_HGAP = 10
        private const val MAIN_PANEL_VGAP = 5

        private const val CONTENT_PANEL_GAP = 4

        private const val LABEL_HGAP = 5
        private const val LABEL_VGAP = 1
    }
}
