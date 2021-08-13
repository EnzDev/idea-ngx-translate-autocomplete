package com.github.enzdev.ideangxtranslateautocomplete.services

import com.github.enzdev.ideangxtranslateautocomplete.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
