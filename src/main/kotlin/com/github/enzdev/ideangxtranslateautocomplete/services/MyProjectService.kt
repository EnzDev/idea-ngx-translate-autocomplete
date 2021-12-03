package com.github.enzdev.ideangxtranslateautocomplete.services

import com.intellij.openapi.project.Project
import com.github.enzdev.ideangxtranslateautocomplete.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
