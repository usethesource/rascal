---
title: Editing eclipse.ini
---

#### Synopsis

Fixing configuration issues of Eclipse

#### Description

The `eclipse.ini` file must be edited to configure the _JVM_ that Rascal uses to run its code.
Reasons for configuring it are:
* More stackspace is required to execute the embedded parser generator
* More heap space is required to load all of the modules
* The right JVM version needs to be configured (Java _11_)

Steps to configure `eclipse.ini`
1. Find the file  first:
   * on MacOSX it may be in /Applications/Eclipse.app/Contents/MacOS/eclipse.ini
   * on Windows it may be in C:\Program Files\eclipse\eclipse.ini
   * on Linux its where you extracted the eclipse tarball.
2. then find the line that starts with `-vm` and add on _the next separate line_ the path to the java run-time binary
   * e.g. `/usr/bin/java` 
   * e.g. `C:\Program Files\Java SDK 1.8\bin\javaw` (note there is no .exe).
3. If you can’t find the line that starts with `-vm` you should add it _before the line starting with_ `-vmargs`

##### Benefits

* Typically `-Xss8m` is enough stacksize 
* Typically `-Xmx1G` is enough heap size.
* Rascal works with Java 11.

#### Pitfalls

* Re-installing Eclipse requires editing the `ini` file again.
* Upgrading Java installation, and removing the old one, might invalidate the Eclipse configuration.
* Rascal works _only_ with Java 11.