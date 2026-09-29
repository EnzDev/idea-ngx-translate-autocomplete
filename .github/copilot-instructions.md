# Copilot Instructions for NgTranslate Toolset

## Project Overview

NgTranslate Toolset is an IntelliJ IDEA plugin that extends Angular language support for the NgxTranslate and Transloco libraries. It provides translation key referencing and autocompletion in Angular templates and TypeScript files.

## Technology Stack

- **Language**: Kotlin (JVM Toolchain 21)
- **Build Tool**: Gradle 8.11.1 with Kotlin DSL
- **Plugin Framework**: IntelliJ Platform Plugin SDK
- **Target Platform**: IntelliJ IDEA Ultimate (IU) 2025.1+
- **Dependencies**:
  - IntelliJ Platform Gradle Plugin
  - JavaScript/TypeScript support
  - AngularJS support
  - JSON support

## Project Structure

```
src/main/kotlin/fr/enzomallard/ngxtranslatetoolset/
├── completion/          # Autocompletion providers for translation keys
├── configuration/       # Plugin settings and configuration UI
├── psi/                # PSI utilities and element patterns
├── reference/          # Reference contributors and providers
└── NgTranslateToolsetBundle.kt  # Internationalization bundle
```

## Key Components

### 1. Completion System
- `TranslationReferenceCompletion.kt`: Registers completion contributors for Angular2 and TypeScript
- `TranslationCompletionProvider.kt`: Provides translation key suggestions with partial matching support

### 2. Reference System
- `TranslationReferenceContributor.kt`: Enables "Go to Definition" for translation keys
- `TranslationReferenceProviderPipe.kt`: Handles references in Angular pipe expressions
- `TranslationReferenceProviderTS.kt`: Handles references in TypeScript code

### 3. Configuration
- `NgTranslateToolsetConfigurable.kt`: Settings UI for translation folder and default language
- `NgTranslateToolsetConfiguration.kt`: Persistent storage for plugin settings

### 4. PSI Utilities
- `TranslationUtils.kt`: Core utilities for finding translation keys in JSON files
- `ElementPatterns.kt`: PSI element patterns for matching translation expressions
- `TranslationFramework.kt`: Framework definitions (NgxTranslate, Transloco)

## Build Commands

```bash
# Build the plugin
./gradlew buildPlugin

# Run tests
./gradlew test

# Run all checks (tests, code quality)
./gradlew check

# Verify plugin compatibility
./gradlew verifyPlugin

# Run the plugin in a sandboxed IDE
./gradlew runIde
```

## Testing

Currently, the project uses standard IntelliJ Platform testing infrastructure. Tests should:
- Use IntelliJ Platform test fixtures
- Test PSI element matching and resolution
- Verify completion and reference providers work correctly

## Code Conventions

### Kotlin Style
- Use Kotlin idiomatic conventions
- Prefer expression syntax over block syntax where appropriate
- Use nullable types appropriately with safe calls (`?.`) and Elvis operator (`?:`)
- Follow existing naming conventions in the codebase

### IntelliJ Platform Patterns
- Use PSI patterns to match specific code constructs
- Implement `CompletionContributor` for autocompletion features
- Implement `PsiReferenceContributor` for navigation features
- Use `PersistentStateComponent` for configuration storage
- Store project-level settings in workspace file using `@State` annotation

### File Organization
- Package by feature (completion, reference, configuration, psi)
- Keep related functionality together
- Use object declarations for utility classes

## Plugin Behavior

### Translation Key Resolution
The plugin looks for translation files in:
1. User-configured i18n folder (Settings > Tools > NgTranslate Toolset)
2. Default `assets` directory in the project

### Supported Frameworks
- **NgxTranslate**: Uses `translate` pipe and `instant()` method
- **Transloco**: Uses `transloco` pipe and `translate()` method

### Key Features
- Translation key autocompletion with partial matching (e.g., `ABC_DEF.MNOP` matches `ABC_DEF.HIJKL_MNOP`)
- "Go to Definition" navigation from translation keys to JSON definitions
- Visual indicators (colored icons) for translation keys vs. intermediate paths
- Display of translation values in completion popup

## Important Notes

### Dependencies
- The plugin depends on bundled IntelliJ plugins: JavaScript and AngularJS
- Requires JSON module for parsing translation files
- Uses Angular2 language support for template expressions

### Configuration
- Plugin configuration is stored per-project in the workspace file
- Users can configure:
  - Translation folder path (where JSON files are located)
  - Default language file (for displaying values in completions)

### Platform Compatibility
- Currently targets IntelliJ IDEA 2025.1+
- Uses sinceBuild: 251 and untilBuild: 252.*
- Verify compatibility when updating platform version

## Development Workflow

1. Make changes to Kotlin source files
2. Run `./gradlew buildPlugin` to compile
3. Run `./gradlew runIde` to test in sandboxed IDE
4. Run `./gradlew check` before committing
5. Update CHANGELOG.md following Keep a Changelog format
6. Ensure plugin.xml is updated if adding new extensions

## Common Tasks

### Adding New Completion Provider
1. Create provider class extending `CompletionProvider<CompletionParameters>`
2. Register in `TranslationReferenceCompletion.kt`
3. Define PSI patterns in `ElementPatterns.kt` if needed

### Adding New Reference Provider
1. Create provider class implementing `PsiReferenceProvider`
2. Register in `TranslationReferenceContributor.kt`
3. Implement `getReferencesByElement()` method

### Modifying Configuration UI
1. Update `NgTranslateToolsetConfigurable.kt` for UI changes
2. Update `NgTranslateToolsetConfiguration.kt` for storage changes
3. Ensure state serialization/deserialization works correctly

## Debugging Tips

- Use IntelliJ IDEA's built-in debugger with `runIde` task
- Enable internal mode for additional plugin development tools
- Check IDE logs in sandboxed IDE: Help > Show Log in Finder/Explorer
- Use PSI Viewer (View > Tool Windows > PsiViewer) to inspect PSI structure

## Resources

- [IntelliJ Platform SDK Documentation](https://plugins.jetbrains.com/docs/intellij/welcome.html)
- [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
- [PSI Cookbook](https://plugins.jetbrains.com/docs/intellij/psi-cookbook.html)
- [Plugin Repository](https://plugins.jetbrains.com/plugin/17450-ngtranslate-toolset)
