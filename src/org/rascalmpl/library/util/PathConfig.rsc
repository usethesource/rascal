@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@synopsis{Standard intermediate storage format for configuring language processors (such as interpreters, type-checkers and compilers)}
@description{
The module offers the ((PathConfig)) datatype which standardizes the configuration of source code projects.
Together with ((LanguageFileConfig)) automatic mappings from source files to target files, and back, can
be computed.

The following reusable invertible mapping functions are provided for the sake of convenience and
for the sake of consistent interpretation of a ((PathConfig)) instance. We map fully qualified
module names to their corresponding file locations on disk:

| qualified module name (`str`) to file path (`loc`) | file path (`loc`) to qualified module name (`str`)  | ((PathConfig)) field used |
| ----------------- |------------------- | -------------         | 
| ((srcsFile))    | ((srcsModule))   | `srcs`                |
| ((binFile))    | ((binModule))   | `bin`                 |
| ((libsFile))   | ((libsModule))  | `libs`                | 
}
module util::PathConfig

import Exception;
import IO;
import Location;
import Message;
import String;
import util::UUID;

@synopsis{General configuration (via locations) of a language processor.}
@description{
A PathConfig is the result of dependency resolution and other configuration steps. Typically,
IDEs produce the information to fill a PathConfig, such that language tools can consume it
transparently. A PathConfig is also a log of the configuration process. Typically a single
((pathConfig)) instance configures the language processor for a single source project tree.

* `projectRoot` is the root directory of the source project tree that is being configured.
* `srcs` list of root directories to search for source files; to interpret or to compile.
* `ignores` list of directories and files to not compile or not interpret (these are typically subtracted from the `srcs` tree, and/or skipped when the compiler arrives there.)
* `bin` is the target root directory for the output of a compiler. Typically this directory would be linked into a zip or a jar or some other executable archive later.
* `libs` is a list of binary dependencies (typically jar files or bin folders) on other projects, for checking and linking purposes. Each entry is expected to return `true` for ((isDirectory)).
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

@synopsis{Defines the parameters of mappings between qualified module names and source, target, and library files.}
@description{
For most languages a single `fileConfig()` instance is enough to define:
* the mapping from source files and source folders to fully qualified module names, and back: ((srcsModule)) and ((srcsFile))
* the mapping from binary library files to fully qualified module names and back: ((libsModule)) and ((libsFile))
* the mapping from source files to target files in the bin folder, and back: ((binFile)) and ((binModule))

Together with a ((PathConfig)) instance, the above six functions can be re-used to build a language processor that supports:
* execution (testing) of generated files from the `bin` folder, using `libs` as run-time dependencies
* using binary compile-time libraries, using `libs` to find binary interfaces to previously generated targets
* packaging binary (generated) files as `jar` files to be re-used later as `libs` dependencies
* modular language processors that work incrementally per changed source file or changed dependency
}
@benefits{
* one ((fileConfig)) constant can be reused for configure all six different mapping functions.  
* a simple `fileConfig()` constant is configured for the Rascal compiler by default (.tpl files as binary extension).
* the mapping functions that use ((LanguageFileConfig)) can always use the same ((PathConfig)) instance for one project.
* more complex mappings can be made by combining the six functions. For example first retrieving the module name using `srcsModule` and then 
seeing if it exists also in one of the libraries using `libsFile`.
}
@pitfalls{
* If a compiler produces multiple target files from a single source file, then you might have to configure
different instances of ((fileConfig)) for every target `binExt`.
* If the mapping between qualified module names and source files or binary files is different ---it has more parameters than defined by ((LanguageFileConfig))--- then you have to write your own
versions of ((srcsModule)), ((srcsFile)), ((libsModule)), ((libsFile)), ((binFile)) and ((binModule)).
}
data LanguageFileConfig = fileConfig(
    str packageSep = "::",
    str binExt     = "tpl",
    str targetRoot = "rascal", 
    str targetEsc  = "$",
    str srcsExt    = "rsc"
);

@synopsis{Produces the latest up-to-date file to load for a given module name, searching in the bin folder, the srcs folder and the libraries.}
@description{
We find the right file to source for the given `moduleName`:
1. If the binary target file is younger than the source file, the binary target wins
2. If a binary target is found, without a corresponding source unit, we try the libraries instead because a source module can have been deleted.
3. If a source file is found, without a binary target, this source file is returned.
4. Otherwise we search in the libraries for a binary file and return it.
5. We throw ((PathNotFound)) if a module can not be resolved using either the bin, srcs, or libs, and also
if the only place we found the module in was a bin folder (signifying a deleted source module). 

In other words, ((latestModuleFile)) prefers newer binaries over older source files, and source files over library modules.
If a module is present in both libraries and the current project, then the current project's sources shadow the libraries.

This function is based on the core features of ((srcsFile)), ((binFile)), and ((libsFile)).
}
@benefits{
* Finicky issues with file IO are dealt with here in a language parametric way, based on ((LanguageFileConfig)) and ((relativize)).
* Provides the basic setup for creating a programming language or DSL with independent libraries/components to depend on.
* Use `returnValue.extension == fcfg.binExt` to detect if you need to read a binary or parse the source code.
* The `PathNotFound` exception should be caught by the code that processes `import` statements in your language.
}
loc latestModuleFile(str qualifiedModuleName, PathConfig pcfg, LanguageFileConfig fcfg) throws PathNotFound {
    loc source(str name) {
        try { return srcsFile(name, pcfg, fcfg); } catch PathNotFound(_): return |notFound:///|;
    }
    loc target(str name) {
        try { return binFile(name, pcfg, fcfg); } catch PathNotFound(_): return |notFound:///|;
    }

    switch (<source(qualifiedModuleName), target(qualifiedModuleName)>) {
        case <|notFound:///|, |notFound:///|>: 
            return libsFile(qualifiedModuleName, pcfg, fcfg);
        case <|notFound:///|, loc _tgt      >: 
            return libsFile(qualifiedModuleName, pcfg, fcfg);
        case <loc src       , |notFound:///|>: 
            return src;
        case <loc src       , loc tgt       >: 
            return lastModified(src) > lastModified(tgt) ? src : tgt;
    }

    assert false : "unreachable code";
    throw PathNotFound(|module:///| + qualifiedModuleName);   
}

@synopsis{Compute a fully qualified module name for a module file, relative to the source roots of a project}
@description{
* ((srcsModule)) is the inverse of ((srcsFile))
}
str srcsModule(loc moduleFile, PathConfig pcfg, LanguageFileConfig fcfg) throws PathNotFound 
    = srcsModule(moduleFile, pcfg.srcs, fcfg);

str srcsModule(loc moduleFile, list[loc] srcs, LanguageFileConfig fcfg) throws PathNotFound {
    loc relative = relativize(srcs, moduleFile);
    relative.extension = "";

    return replaceAll(relative.path[1..], "/", fcfg.packageSep);
}

@synopsis{Compute a fully qualified module name for a library file, relative to the library roots of a project}
@description{
* ((libsModule)) is the inverse of ((libsFile))
}
str libsModule(loc libsFile, PathConfig pcfg, LanguageFileConfig fcfg) throws PathNotFound 
    = libsModule(libsFile, pcfg.libs, fcfg);

str libsModule(loc libsFile, list[loc] libs, LanguageFileConfig fcfg) throws PathNotFound {
  loc relative       = relativize(libs, libsFile);

  relative.file      = relative.file[size(fcfg.targetEsc)..];
  relative.path      = relative.path[1 + size(fcfg.targetRoot)..];
  relative.extension = "";

  return replaceAll(relative.path[1..], "/", fcfg.packageSep);
}

@synopsis{Find out in which library file a module was implemented.}
@description{
* ((libsFile)) is the inverse of ((libsModule))
* the computed file has to exist in at least one of the library modules. Otherwise ((PathNotFound)) is thrown.
}
loc libsFile(str qualifiedModuleName, PathConfig pcfg, LanguageFileConfig fcfg) throws PathNotFound
    = libsFile(qualifiedModuleName, pcfg.libs, fcfg);

loc libsFile(str qualifiedModuleName, list[loc] libs, LanguageFileConfig fcfg) throws PathNotFound {
    loc relativeFile       = |relative:///| + fcfg.targetRoot + replaceAll(qualifiedModuleName, fcfg.packageSep, "/");
    relativeFile.extension = fcfg.binExt;
    relativeFile.file      = "<fcfg.targetEsc><relativeFile.file>";

    return resolve(libs, relativeFile);
}

@synopsis{Find out in which source file a module was implemented.}
@description{
* ((srcsFile)) is the inverse of ((srcsModule))
* throws ((PathNotFound)) if the designated source file does not exist, unless `force == true`.
* if `force` then the first element of the `srcs` path is used as parent to the new module file.
}
loc srcsFile(str qualifiedModuleName, PathConfig pcfg, LanguageFileConfig fcfg, bool force = false) throws PathNotFound
    = srcsFile(qualifiedModuleName, pcfg.srcs, fcfg, force = force);

loc srcsFile(str qualifiedModuleName, list[loc] srcs, LanguageFileConfig fcfg, bool force = false) throws PathNotFound {
    loc relative = |relative:///| + replaceAll(qualifiedModuleName, fcfg.packageSep, "/");
    relative.extension = fcfg.srcsExt;
    return resolve(srcs, relative, force = force);
}

@synopsis{Compute the binary file location for a fully qualified source module name}
@description{
* ((binFile)) is the inverse of ((binModule)).
* the returned target location does not have to exist yet.
}
loc binFile(str srcsModule, PathConfig pcfg, LanguageFileConfig fcfg)
    = binFile(srcsModule, pcfg.bin, fcfg);

@synopsis{Compute a target file name for a generated folder with a given extension}
loc binFile(str srcsModule, loc generated, LanguageFileConfig fcfg) {
    relative           = |relative:///| + replaceAll(srcsModule, fcfg.packageSep, "/");
    relative.file      = "<fcfg.targetEsc><relative.file>";
    relative.extension = fcfg.binExt;

    return generated + fcfg.targetRoot + relative.path;
}

@synopsis{Computing a fully qualified module name back from a file in the bin folder}
@description{
* ((binModule)) is the inverse of ((binFile))
}
str binModule(loc binFile, PathConfig pcfg, LanguageFileConfig fcfg) throws PathNotFound
  = binModule(binFile, pcfg.bin, fcfg);

@synopsis{Recovers the original module name back from a file that was generated.}
str binModule(loc targetFile, loc bin, LanguageFileConfig fcfg) throws PathNotFound {
    relative           = relativize(bin, targetFile);
    relative.extension = "";
    relative.file      = relative.file[size(fcfg.targetEsc)..];
    relative.path      = relative.path[1 + size(fcfg.targetRoot)..];

    return replaceAll(relative.path[1..], "/", fcfg.packageSep);
} 

// below we have some core tests of the above features  

private loc testLibraryLoc = |memory://myTestLibrary-<uuid().authority>/|;

test bool inverseBinFileModule() {
    pcfg = pathConfig(
        bin=testLibraryLoc + "target/classes",
        srcs=[|project://rascal/src/org/rascalmpl/library/|]
    );
    fcfg = fileConfig();

    tgt = binFile("util::Monitor", pcfg, fcfg);
    writeFile(tgt, "blabla");

    return binModule(tgt, pcfg, fcfg) == "util::Monitor";
}

test bool inverseSrcsFileModule() {
    pcfg = pathConfig(
        bin=testLibraryLoc + "target/classes",
        srcs=[testLibraryLoc + "src/main/rascal"]
    );

    writeFile(pcfg.srcs[0] + "util/Monitor.rsc", "module util::Monitor");
    fcfg = fileConfig();

    src = srcsFile("util::Monitor", pcfg, fcfg);

    return srcsModule(src, pcfg, fcfg) == "util::Monitor";
}

test bool inverseLibsFileModule() {
    pcfg = pathConfig(
        bin=testLibraryLoc + "target/classes",
        libs=[testLibraryLoc + "libs"]
    );
    fcfg = fileConfig(binExt="tpl");

    writeFile(testLibraryLoc + "libs" + "rascal/util/$Monitor.tpl", "blabla");
    lib = libsFile("util::Monitor", pcfg, fcfg);

    return libsModule(lib, pcfg, fcfg) == "util::Monitor";
}

test bool moduleExceptionWithSrc() {
    pcfg = pathConfig(srcs=[|project://rascal/src/org/rascalmpl/library/|]);
    fcfg = fileConfig();
    return srcsModule(|project://rascal/src/org/rascalmpl/library/Exception.rsc|, pcfg, fcfg) 
        == "Exception";
}

test bool moduleReflectiveWithSrc() {
    pcfg = pathConfig(srcs=[|project://rascal/src/org/rascalmpl/library/|]);
    fcfg = fileConfig();

    return srcsModule(|project://rascal/src/org/rascalmpl/library/util/Reflective.rsc|, pcfg, fcfg) 
        == "util::Reflective";
}

test bool moduleExceptionOnlyTplModule() {
    tplFile = testLibraryLoc + "/lib/rascal/$Exception.tpl";
    writeFile(tplFile, "$Exception.tpl (only file matters, content irrelevant)");
    pcfg = pathConfig(libs=[testLibraryLoc + "/lib/"]);
    fcfg = fileConfig();

    return libsModule(tplFile, pcfg, fcfg) == "Exception";
}

test bool moduleExceptionOnlyTplFile() {
    tplFile = testLibraryLoc + "/lib/rascal/$Exception.tpl";
    writeFile(tplFile, "$Exception.tpl (only file matters, content irrelevant)");
    pcfg = pathConfig(libs=[testLibraryLoc + "/lib/"]);
    fcfg = fileConfig(binExt="tpl");

    return libsFile("Exception", pcfg, fcfg) == tplFile;
}

test bool moduleReflectiveOnlyTplModule() {
    writeFile(testLibraryLoc + "/libs/rascal/util/$Reflective.tpl",
        "util::Reflective (only file matters, content irrelevant)");
    pcfg = pathConfig(srcs = [],
                    libs=[testLibraryLoc + "libs"]
                     );
    fcfg = fileConfig();

    return libsModule(testLibraryLoc + "libs/rascal/util/$Reflective.tpl", pcfg, fcfg) 
            == "util::Reflective";
}

test bool moduleReflectiveOnlyTplFile() {
    libFile = testLibraryLoc + "/libs/rascal/util/$Reflective.tpl";
    writeFile(libFile, "util::$Reflective.tpl (only file matters, content irrelevant)");
    pcfg = pathConfig(srcs = [],
                    libs=[testLibraryLoc + "libs"]
                     );
    fcfg = fileConfig(binExt="tpl");

    return libsFile("util::Reflective", pcfg, fcfg) == libFile;
}

test bool longestModuleReflectiveOnlyTpl() {
    writeFile(testLibraryLoc + "/1/libs/rascal/$Reflective.tpl", "$Reflective.tpl at top level (only file matters, content irrelevant)");
    writeFile(testLibraryLoc + "/2/libs/rascal/util/$Reflective.tpl",
        "util::$Reflective.tpl in subdir util (only file matters, content irrelevant)");
    pcfg = pathConfig(srcs= [], 
                      libs=[testLibraryLoc + "1/libs", testLibraryLoc + "2/libs"]
                     );
    fcfg = fileConfig(binExt="tpl");
    return libsFile("util::Reflective", pcfg, fcfg) == testLibraryLoc + "2/libs/rascal/util/$Reflective.tpl";
}

test bool moduleOnlyInSecondSrc() {
    testLibrarySrc = testLibraryLoc + "src/org/rascalmpl/library/";
    ESrc = testLibrarySrc + "E.rsc";
    writeFile(ESrc,  "module E");

    pcfg = pathConfig(srcs=[|project://rascal/src/org/rascalmpl/library/|, testLibrarySrc]);
    fcfg = fileConfig();
    return srcsModule(ESrc, pcfg, fcfg) == "E";
}
