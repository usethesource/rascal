---
name: Stable release manual testing template
about: This is a list of things to check at the time of a stable release
title: "[RELEASE] manual testing version 0.x.x"
labels: release testing
assignees: ''

---

First:

- [ ] Continuous Integration runs all unit and integration tests and fails no test
- [ ] Maximum number of compiler warnings are resolved
- [ ] Version numbers are verified manually
- [ ] Releases depend on the right versions of dependencies (manually verified)

Then the following features need manual testing in Eclipse:

- [ ] Eclipse downloads latest stable release from update site https://update.rascal-mpl.org/unstable
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
- [ ] ```
import IO;
println("Hello Rascal!");
