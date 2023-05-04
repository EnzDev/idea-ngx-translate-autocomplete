# NgTranslate Toolset

[![Build](https://github.com/EnzDev/idea-ngx-translate-autocomplete/actions/workflows/build.yml/badge.svg)](https://github.com/EnzDev/idea-ngx-translate-autocomplete/actions/workflows/build.yml)
[![Version](https://img.shields.io/jetbrains/plugin/v/17450-ngtranslate-toolset.svg)](https://plugins.jetbrains.com/plugin/17450-ngtranslate-toolset)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/17450-ngtranslate-toolset.svg)](https://plugins.jetbrains.com/plugin/17450-ngtranslate-toolset)

<!-- Plugin description -->
NgTranslate Toolset extends Angular language support for the NgxTranslate library.  
It provides translation key referencing and autocompletion in your angular templates. Also works for Transloco.

Plugins look in the resources directories by default, you can
configure the plugin in `Settings...` > `Tools` > `NgTranslate Toolset`.  
There, you can define where you store your translations and
you favorite language to show in autocompletion.
<!-- Plugin description end -->

## Project ToDo list
- [x] Translation key referencing to json translations
- [x] Translation key autocompletion in templates
- [x] Chose user language to display translation
- [ ] Translation key value presentation
- [ ] JSON key usage referencing
- [ ] JSON translations inspections (All keys must be defined in every translation files, same order)
- [ ] Reference renaming (recurse on the path)
