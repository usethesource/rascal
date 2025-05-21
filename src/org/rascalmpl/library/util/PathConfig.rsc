@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@synopsis{Standard intermediate storage format for configuring file-based language processors (such as interpreters, type-checkers and compilers)}
module util::PathConfig

import Exception;
import IO;
import Location;
import Message;
import String;

@synopsis{General configuration (via path references) of a compiler or interpreter.}
@description{
A PathConfig is the result of dependency resolution and other configuration steps. Typically,
IDEs produce the information to fill a PathConfig, such that language tools can consume it
transparantly. A PathConfig is also a log of the configuration process. 

* `srcs` list of root directories to search for source files; to interpret or to compile.
* `ignores` list of directories and files to not compile or not interpret (these are typically subtracted from the `srcs` tree, or skipped when the compiler arives there.)
* `bin` is the target root directory for the output of a compiler. Typically this directory would be linked into a zip or a jar or an executable file later.
* `libs` is a list of binary dependency files (typically jar files or target folders) on other projects, for checking and linking purposes.
* `resources` is a list of files or folders that will be copied *by the compiler* to the bin folder, synchronized with its other (binary) output files..
* `messages` is a list of info, warning and error messages informing end-users about the quality of the configuration process. Typically missing dependencies would be reported here, and clashing versions.
}
@benefits{
* `main` functions which have a keyword parameter of type ((PathConfig)) are automatically augmented with commandline parameters for every field of ((PathConfig))
* `messages` can be printed in a standard way using ((mainMessageHandler))
* ((PathConfig)) is a reusable bridge between language processing functions and different execution environments such as VScode, the commandline or Maven.
* ((PathConfig)) makes all configuration processors of file-based language processors explicit and transparent
* ((PathConfig)) is programming language and domain-specific language independent
* This module contains *bidirectional* transformation functions between fully qualified module names and their file locations in source folders and library dependencies.
}
data PathConfig = pathConfig(
    loc projectRoot        = |unknown:///|,
    list[loc] srcs         = [],  
    list[loc] ignores      = [],  
    loc bin                = |unknown:///|,
    list[loc] resources    = [],
    list[loc] libs         = [],          
    list[Message] messages = []
);

@synopsis{Defines the parameters of mappings between qualified module names and source and target files or library files.}
@description{
For most languages a single `fileConfig()` instance is enough to define:
* the mapping from source files and source folders to fully qualified module names, and back: ((sourceModule)) and ((sourceFile))
* the mapping from binary library files to fully qualified module names and back: ((libraryModule)) and ((libraryFile))
* the mapping from source files to target folder in the bin folder, and back: ((targetFile)) and ((targetModule))
}
@benefits{
* one ((fileConfig)) constant can be reused for configure all six different mapping functions.  
* a simple `fileConfig()` constant is configured for the Rascal compiler by default (.tpl files as binary extension).
* the mapping functions that use ((LanguageFileConfig)) can always use the same ((PathConfig)) instance.
}
@pitfalls{
* If a compiler produces multiple target files from a single source file, then you might have to configure
different instances of ((fileConfig)) for every target ((binaryExt)).
* if the mapping between qualified module names and source files or binary files is different ---it has more parameters than defined by ((LanguageFileConfig))--- then you have to write your own
versions of ((sourceModule)), ((sourceFile)), ((libraryModule)), ((libraryFile)), ((targetFile)) and ((targetModule)).
}
data LanguageFileConfig = fileConfig(
    str packageSep = "::",
    str binaryExt  = "class",
    str binaryRoot = "rascal",
    str binaryEsc  = "$",
    str sourceExt  = "rsc"
);

@synopsis{Compute a fully qualified module name for a module file, relative to the source roots of a project}
@description{
* ((sourceModule)) is the inverse of ((sourceFile))
}
str sourceModule(loc moduleFile, PathConfig pcfg, LanguageFileConfig fcfg) throws PathNotFound 
    = sourceModule(moduleFile, pcfg.srcs, fcfg);

str sourceModule(loc moduleFile, list[loc] srcs, LanguageFileConfig fcfg) throws PathNotFound
    = replaceAll(relativize(srcs, moduleFile)[extension=""].path[1..], "/", fcfg.packageSep);

@synopsis{Compute a fully qualified module name for a library file, relative to the library roots of a project}
@description{
* ((libraryModule)) is the inverse of ((libraryFile))
}
str libraryModule(loc libraryFile, PathConfig pcfg, LanguageFileConfig fcfg) throws PathNotFound 
    = libraryModule(libraryFile, pcfg.libs, fcfg);

str libraryModule(loc libraryFile, list[loc] libs, LanguageFileConfig fcfg) throws PathNotFound {
  loc relative       = relativize(libs, libraryFile);
  relative.file      = "<fcfg.binaryEsc><relative.file>";
  relative.extension = "";

  str moduleName = relative.path[1 + size(fcfg.binaryRoot)];
  return replaceAll(moduleName, "/", fcfg.packageSep);
}

@synopsis{Find out in which library file a module was implemented.}
@description{
* ((libraryFile)) is the inverse of ((libraryModule))
}
loc libraryFile(str qualifiedModuleName, PathConfig pcfg, LanguageFileConfig fcfg) throws PathNotFound
    = libraryFile(qualifiedModuleName, pcfg.libs, fcfg);

loc libraryFile(str qualifiedModuleName, list[loc] libs, LanguageFileConfig fcfg) throws PathNotFound {
    loc relativeFile       = |relative:///| + fcfg.binaryRoot + replaceAll(qualifiedModuleName, fcfg.packageSep, "/");
    relativeFile.extension = fcfg.binaryExt;
    relativeFile.file      = "<fcfg.binaryEsc><relativeFile.file>";

    return resolve(libs, relativeFile);
}

@synopsis{Find out in which source file a module was implemented.}
@description{
* ((sourceFile)) is the inverse of ((sourceModule))
}
loc sourceFile(str qualifiedModuleName, PathConfig pcfg, LanguageFileConfig fcfg) throws PathNotFound
    = sourceFile(qualifiedModuleName, pcfg.srcs, fcfg);

loc sourceFile(str qualifiedModuleName, list[loc] srcs, LanguageFileConfig fcfg) throws PathNotFound {
    loc relative = |relative:///| + fcfg.binaryRoot + replaceAll(qualifiedModuleName, fcfg.packageSep, "/");
    relative.extension = fcfg.sourceExt;
    return resolve(srcs, relative);
}

@synopsis{Compute the binary file location for a fully qualified source module name}
@description{
* ((targetFile)) is the inverse of ((targetModule))
}
loc targetFile(str sourceModule, PathConfig pcfg, LanguageFileConfig fcfg)
    = targetFile(sourceModule, pcfg.bin, fcfg);

loc targetFile(str sourceModule, loc bin, LanguageFileConfig fcfg)
    = (bin + fcfg.binaryRoot + replaceAll(sourceModule, fcfg.packageSep, "/"))[extension=fcfg.binaryExt];

@synopsis{Computing a fully qualified module name back from a file in the target folder}
@description{
* ((targetModule)) is the inverse of ((targetFile))
}
str targetModule(loc targetFile, PathConfig pcfg, LanguageFileConfig fcfg) throws PathNotFound
  = targetModule(targetFile, pcfg.bin, fcfg);

str targetModule(loc targetFile, loc bin, LanguageFileConfig fcfg) throws PathNotFound
    = replaceAll(relativize(bin, targetFile)[extension=""].path[1 + size(fcfg.binaryRoot)], "/", fcfg.packageSep);
  