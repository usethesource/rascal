---
name: Stable release manual testing template
about: This is a list of things to do and to check at the time of a stable release
title: "[RELEASE] version 0.x.x"
labels: release testing
assignees: ''

---

# Preliminaries

* Every time this document says "release X" ; we mean to execute the instructions of this Wiki page: https://github.com/usethesource/rascal/wiki/How-to-make-a-release-of-a-Rascal-implemenation-project
* The current release instructions are focused on the Rascal commandline tools and the Eclipse IDE plugin
* If you edit this template, then please push relevant improvements to the template itself for future reference.

# Pre-releasing dependent tools in unstable

First a "pre-release" of the supporting compiler/typechecker tools must be done, so we know we are releasing a consistently compiled standard library.

- [ ] typepal and rascal-core compile in the continuous integration environment and no tests fail
- [ ] release typepal
- [ ] release rascal-core
- [ ] bump typepal and rascal-core versions in rascal-maven-plugin to latest releases
- [ ] bump typepal and rascal-core versions in rascal-eclipse to latests SNAPSHOT releases
- [ ] release rascal-maven-plugin
- [ ] bump rascal-maven-plugin dependency in rascal and rascal-eclipse project
- [ ] fix new errors and warnings in rascal and rascal-eclipse project

# Manual version checks

- [ ] Continuous Integration runs all unit and integration tests and fails no test
- [ ] Maximum number of compiler warnings are resolved
- [ ] Version numbers are verified manually

# Manual feature tests

- [ ] Eclipse download and install latest unstable release from update site https://releases.usethesource.io/maven/org/rascalmpl/rascal-update-site/
- [ ] Open a Rascal REPL using the toolbar button
- [ ] Can create new Rascal project using the wizard
- [ ] Can create new Rascal module using the wizard
- [ ] Can edit Rascal file in Rascal project
- [ ] Save on Rascal file triggers type-checker
- [ ] Rascal outline works
- [ ] Rascal navigator works
- [ ] Rascal navigator displays working sets
- [ ] Rascal navigator displays interpreter's search path
- [ ] Clicking links in REPL opens editors and websites
- [ ] `rascal>1 + 1` on the REPL
- [ ] `import IO; println("Hello Rascal!");`
- [ ] in editor, click on use of name jumps to definition
- [ ] jump-to-definition also works to library modules and inside library modules
- [ ] clicking in outline jumps to editor to right position
- [ ] syntax highlighting in editor works
- [ ] add dependency on another project by editing `RASCAL.MF`: `Required-Libraries: |lib://otherProject|`, import a module and test the type-checker as well as the interpreter for correct resolution
- [ ] `import demo::lang::Pico::Plugin; registerPico();` and test the editor of the example pico files (syntax highlighting, menu options)
- [ ] `import demo::lang::Pico::Plugin; rascal>:edit  demo::lang::Pico::Plugin`
- [ ] use util::IDEServices:
   - [ ] registerDiagnostics with more than one file
   - [ ] test edit function (for example with vis::Graph examples that use it)
   - [ ] test showInteractiveContent function (for example with vis::Chart examples)

# Actual release

- [ ] release rascal project (when resolving SNAPSHOT dependencies choose the right versions of vallang etc, and make sure to bump the new rascal SNAPSHOT release one minor version)
- [ ] bootstrap documentation site
   - [ ] bump rascal version in rascal-maven-project, mvn install
   - [ ] bump rascal version in rascal-website project
   - [ ] set rascal-maven-project snapshot dependency in rascal-website project
   - [ ] run `mvn clean package` in rascal-website project
   - [ ] fix errors, possibly release rascal project patch versions, and repeat the above.
   - [ ] release rascal-maven-project
   - [ ] bump rascal-maven-project dependency in rascal-website project
   - [ ] run `mvn clean package` in rascal-website project and `git push` to publish new docs
- [ ] release rascal-eclipse project (take care to choose the right release versions of typepal and rascal-core you release earlier and choose their new SNAPSHOT dependencies to the latest)
- [ ] change the configuration of the stable version in `update-site-nexus-link-script/refresh-nexus-data` to the released version
- [ ] test the stable update site at https://update.rascal-mpl.org/stable
- [ ] write release notes and publish on the usethesource.io blog

# Downstream implications

The following items can be executed asynchronously, but are nevertheless not to be forgotten:

- [ ] change dependencies on rascal-eclipse and rascal in rascal-eclipse-libraries and the projects it depends on
- [ ] change dependencies of typepal to latest rascal and rascal-eclipse
- [ ] change dependency of rascal-core to latest stable rascal
- [ ] change dependency in all projects on new rascal-maven-project
- [ ] change dependency of rascal-maven-project and rascal in rascal-tutor
- [ ] release rascal-tutor
- [ ] bump dependency of rascal-tutor in rascal-maven-project
- [ ] release rascal-maven-project again
