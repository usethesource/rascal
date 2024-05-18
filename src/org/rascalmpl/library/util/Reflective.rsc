@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl (CWI)}
@bootstrapParser
module util::Reflective


import IO;
import List;
import ParseTree;
import String;
import util::FileSystem;

import lang::rascal::\syntax::Rascal;
import lang::manifest::IO;

@synopsis{Returns the system-dependent line separator string}
@javaClass{org.rascalmpl.library.util.Reflective}
public java str getLineSeparator();

@javaClass{org.rascalmpl.library.util.Reflective}
public java lrel[str result, str out, str err] evalCommands(list[str] command, loc org);

@synopsis{Just parse a module at a given location without any furter processing (i.e., fragment parsing) or side-effects (e.g. module loading)}
public lang::rascal::\syntax::Rascal::Module parseModule(loc location) = parseModuleWithSpaces(location).top;

@synopsis{Parse a module (including surounding spaces) at a given location without any furter processing (i.e., fragment parsing) or side-effects (e.g. module loading)}
@javaClass{org.rascalmpl.library.util.Reflective}
public java start[Module] parseModuleWithSpaces(loc location);

data RascalConfigMode
    = compiler()
    | interpreter()
    ;

data PathConfig 
    // Defaults should be in sync with org.rascalmpl.library.util.PathConfig
  = pathConfig(list[loc] srcs = [|std:///|],        // List of directories to search for source files
               list[loc] ignores = [],              // List of locations to ignore from the source files
               loc bin = |home:///bin/|,            // Global directory for derived files outside projects
               list[loc] libs = [|lib://rascal/|],          // List of directories to search source for derived files
               list[loc] javaCompilerPath = [], // TODO: must generate the same defaults as in PathConfig 
               list[loc] classloaders = [|system:///|]      // TODO: must generate the same defaults as in PathConfig
              );

data RascalManifest
  = rascalManifest(
      str \Project-Name = "Project",
      str \Main-Module = "Plugin",
      str \Main-Function = "main", 
      list[str] Source = ["src"],
      str Bin = "bin",
      list[str] \Required-Libraries = [],
      list[str] \Required-Dependencies = []
    ); 

data JavaBundleManifest
  = javaManifest(
      str \Manifest-Version = "",
      str \Bundle-SymbolicName = "",
      str \Bundle-RequiredExecutionEnvironment = "JavaSE-1.8",
      list[str] \Require-Bundle = [],
      str \Bundle-Version = "0.0.0.qualifier",
      list[str] \Export-Package = [],
      str \Bundle-Vendor = "",
      str \Bundle-Name = "",
      list[str] \Bundle-ClassPath = [],
      list[str] \Import-Package = [] 
    );
          
loc metafile(loc l) = l + "META-INF/RASCAL.MF";
 
@synopsis{Converts a PathConfig and replaces all references to roots of projects or bundles
  by the folders which are nested under these roots as configured in their respective
  META-INF/RASCAL.MF files.}
PathConfig applyManifests(PathConfig cfg) {
   mf = (l:readManifest(#RascalManifest, metafile(l)) | l <- cfg.srcs + cfg.libs + [cfg.bin], exists(metafile(l)));

   list[loc] expandSrcs(loc p) = [ p + s | s <- mf[p].Source] when mf[p]?;
   default list[loc] expandSrcs(loc p, str _) = [p];
   
   list[loc] expandlibs(loc p) = [ p + s | s <- mf[p].\Required-Libraries] when mf[p]?;
   default list[loc] expandlibs(loc p, str _) = [p];
    
   loc expandBin(loc p) = p + mf[p].Bin when mf[p]?;
   default loc expandBin(loc p) = p;
   
   cfg.srcs = [*expandSrcs(p) | p <- cfg.srcs];
   cfg.libs = [*expandlibs(p) | p <- cfg.libs];
   cfg.bin  = expandBin(cfg.bin);
   
   // TODO: here we add features for Require-Libs by searching in a repository of installed
   // jars. This has to be resolved recursively.
   
   return cfg;
}

str makeFileName(str qualifiedModuleName, str extension = "rsc") = 
    replaceAll(qualifiedModuleName, "::", "/") + (isEmpty(extension) ? "" : ("." + extension));

loc getSearchPathLoc(str filePath, PathConfig pcfg){
    for(loc dir <- pcfg.srcs + pcfg.libs){
        fileLoc = dir + filePath;
        if(exists(fileLoc)){
            //println("getModuleLocation <qualifiedModuleName> =\> <fileLoc>");
            return fileLoc;
        }
    }
    throw "Module with path <filePath> not found"; 
}

@synopsis{Get the location of a named module, search for `src` in srcs and `tpl` in libs}
loc getModuleLocation(str qualifiedModuleName,  PathConfig pcfg){
    fileName = makeFileName(qualifiedModuleName, extension="rsc");
    for(loc dir <- pcfg.srcs){
        fileLoc = dir + fileName;
        if(exists(fileLoc)){
            return fileLoc;
        }
    }
    fileName = makeFileName(qualifiedModuleName, extension="tpl");
    for(loc dir <- pcfg.libs){
        fileLoc = dir + fileName;
        if(exists(fileLoc)){
            return fileLoc;
        }
    }
    throw "Module `<qualifiedModuleName>` not found;\n<pcfg>";
}

tuple[str,str] splitFileExtension(str path){
    int n = findLast(path, ".");
    if(n < 0) return <path, "">;
    return <path[0 .. n], path[n+1 .. ]>;
}

@synopsis{Determine length of common suffix of list of strings}
int commonSuffix(list[str] dir, list[str] m)
    = commonPrefix(reverse(dir), reverse(m));

@synopsis{Determine length of common prefix of list of strings}
int commonPrefix(list[str] rdir, list[str] rm){
    for(int i <- index(rm)){
        if(i >= size(rdir)){
            return i;
        } else if(rdir[i] != rm[i]){
            return i;
        } else {
            continue;
        }
    }
    return size(rm);
}

@synopsis{Find the module name corresponding to a given module location via its (src or tpl) location}
str getModuleName(loc moduleLoc,  PathConfig pcfg){
    modulePath = moduleLoc.path;
    
    if(!endsWith(modulePath, "rsc")){
        throw "Not a Rascal source file: <moduleLoc>";
    }
    <modulePathNoExt, ext> = splitFileExtension(modulePath);
    if(modulePathNoExt[0] == "/"){
        modulePathNoExt = modulePathNoExt[1..];
    }
    modulePathAsList = split("/", modulePathNoExt);
    modulePathAsListReversed = reverse(modulePathAsList);

    for(loc dir <- pcfg.srcs){
        if(moduleLoc.authority == dir.authority && startsWith(modulePath, dir.path)) {
           moduleName = replaceFirst(modulePath, dir.path, "");
           <moduleName, ext> = splitFileExtension(moduleName);
           if(moduleName[0] == "/"){
              moduleName = moduleName[1..];
           }
           moduleName = replaceAll(moduleName, "/", "::");
           return moduleName;
        }
    }

    int longestSuffix = 0;
    for(loc dir <- pcfg.libs){
        dir = dir + "rascal";
        for(loc file <- find(dir, "tpl")){
            candidate = replaceFirst(file.path, dir.path, "");
            candidate = replaceLast(candidate, "$", "");
            if(candidate[0] == "/"){
                candidate = candidate[1..];
            }
            <candidate, ext> = splitFileExtension(candidate);
            candidateAsList = split("/", candidate);
            n = commonPrefix(reverse(candidateAsList), modulePathAsListReversed);
            //println("<candidateAsList>, <modulePathAsList> =\> <n>");
            if(n > longestSuffix){
                longestSuffix = n;
            }
        }
    }
    if(longestSuffix > 0){
        return intercalate("::", modulePathAsList[size(modulePathAsList) - longestSuffix .. ]);
    }
    throw "No module name found for <moduleLoc>;\nsrcs=<pcfg.srcs>;\nlibs=<pcfg.libs>";
}

@synopsis{Derive a location from a given module name for reading}
@description{
Given a module name, a file name extension, and a PathConfig,
a path name is constructed from the module name + extension.

If a file F with this path exists in one of the directories in the PathConfig,
then the pair <true, F> is returned. Otherwise <false, some error location> is returned.

For a source extension (typically "rsc" or "mu" but this can be configured) srcs is searched, otherwise binPath + libs.
}
@examples{
```rascal-shell
import util::Reflective;
getDerivedReadLoc("List", "rsc", pathConfig());
getDerivedReadLoc("experiments::Compiler::Compile", "rvm", pathConfig());
getDerivedReadLoc("experiments::Compiler::muRascal2RVM::Library", "mu", pathConfig());
```
}
@benefits{
This function is useful for type checking and compilation tasks, when derived information related to source modules has to be read
from locations in different, configurable, directories.
}

tuple[bool, loc] getDerivedReadLoc(str qualifiedModuleName, str extension, PathConfig pcfg, set[str] srcExtensions = {"rsc", "mu"}, str rootDir = ""){
    fileName = makeFileName(qualifiedModuleName, extension=extension);
    //println("getDerivedReadLoc: <fileName>");
   
    if(extension in srcExtensions){
       for(loc dir <- pcfg.srcs){        // In a source directory?
           fileLoc = dir + rootDir + fileName;
           if(exists(fileLoc)){
             //println("getDerivedReadLoc: <qualifiedModuleName>, <extension> =\> <fileLoc");
             return <true, fileLoc>;
           }
       }
    } else {
      for(loc dir <- pcfg.bin + pcfg.libs){   // In a bin or lib directory?
       
        fileLoc = dir + rootDir + fileName;
        if(exists(fileLoc)){
           //println("getDerivedReadLoc: <qualifiedModuleName>, <extension> =\> <fileLoc>");
           return <true, fileLoc>;
        }
      }
    }
    //println("getDerivedReadLoc: <qualifiedModuleName>, <extension> =\> |error:///|");
    return <false, |error:///|>;
}


@synopsis{Derive a location from a given module name for writing}
@description{
Given a module name, a file name extension, and a PathConfig,
a path name is constructed from the module name + extension.

For source modules, a writable location cannot be derived.
For other modules, a location for this path in bin will be returned.
}
@examples{
```rascal-shell
import util::Reflective;
getDerivedWriteLoc("List", "rvm", pathConfig());
getDerivedWriteLoc("experiments::Compiler::Compile", "rvm", pathConfig());
```

```rascal-shell,error
getDerivedWriteLoc("experiments::Compiler::muRascal2RVM::Library", "rsc", pathConfig());
```
}
@benefits{
This function is useful for type checking and compilation tasks, when derived information related to source modules has to be written
to locations in separate, configurable, directories.
}
loc getDerivedWriteLoc(str qualifiedModuleName, str extension, PathConfig pcfg, set[str] srcExtensions = {"rsc", "mu"}, str rootDir = ""){
    if(extension in srcExtensions){
        throw "Cannot derive writable location for module <qualifiedModuleName> with extension <extension>";
    }
    fileNameSrc = makeFileName(qualifiedModuleName);
    fileNameBin = makeFileName(qualifiedModuleName, extension=extension);
    
    bin = pcfg.bin;
    fileLocBin = bin + rootDir + fileNameBin;
    //println("getDerivedWriteLoc: <qualifiedModuleName>, <extension> =\> <fileLocBin>");
    return fileLocBin;
}

@javaClass{org.rascalmpl.library.util.Reflective}
public java PathConfig getProjectPathConfig(loc projectRoot, RascalConfigMode mode = compiler());

@synopsis{Is the current Rascal code executed by the compiler or the interpreter?}
@javaClass{org.rascalmpl.library.util.Reflective}
public java bool inCompiledMode();

@synopsis{Give a textual diff between two values.}
@javaClass{org.rascalmpl.library.util.Reflective}
public java str diff(value old, value new);

@synopsis{Watch value val: 
- running in interpreted mode: write val to a file, 
- running in compiled mode: compare val with previously written value}
@javaClass{org.rascalmpl.library.util.Reflective}
public java &T watch(type[&T] tp, &T val, str name);

@javaClass{org.rascalmpl.library.util.Reflective}
public java &T watch(type[&T] tp, &T val, str name, value suffix);

@synopsis{Compute a fingerprint of a value for the benefit of the compiler and the compiler runtime}
@javaClass{org.rascalmpl.library.util.Reflective}
public java int getFingerprint(value val, bool concretePatterns);

@synopsis{Compute a fingerprint of a value and arity modifier for the benefit of the compiler and the compiler runtime}
@javaClass{org.rascalmpl.library.util.Reflective}
public java int getFingerprint(value val, int arity, bool concretePatterns);

@synopsis{Compute a fingerprint of a complete node for the benefit of the compiler and the compiler runtime}
@javaClass{org.rascalmpl.library.util.Reflective}
public java int getFingerprintNode(node nd);

@synopsis{Get the internal hash code of a value. For the benefit of debugging the Rascal implementation.}
@description{
This function is useless for Rascal programmer's as it is a part of the under-the-hood implementation of values.
You can use a value directly as a lookup key. The internal data-structures probably use this hashCode for
optimal lookups in `O(log(size))`. 

We use this function to diagnose possible performance issues caused by hash collisions.
}
@javaClass{org.rascalmpl.library.util.Reflective}
public java int getHashCode(value v);

@synopsis{Throw a raw Java NullPointerException, to help simulate an unexpected exception in test scenarios}
@javaClass{org.rascalmpl.library.util.Reflective}
java void throwNullPointerException();

@synopsis{Return a list of all Rascal reserved identifiers (a.k.a. keywords)}
set[str] getRascalReservedIdentifiers() = { n | /lit(n) := #RascalKeywords.definitions[keywords("RascalKeywords")]};
    
@javaClass{org.rascalmpl.library.util.Reflective}
java str getRascalVersion();   

@synopsis{Create a folder structure for an empty Rascal project with Maven support}
void newRascalProject(loc folder, str group="org.rascalmpl", str version="0.1.0-SNAPSHOT") {
    if (exists(folder)) {
        throw "<folder> exists already. Please provide an non-existing and empty folder name";
    }
    str name = folder.file;

    if (/[^a-z0-9\-_]/ := name) {
        throw "Folder <name> should have only lowercase characters, digits and dashes from [a-z0-9\\-]";
    }
    
    mkDirectory(pomFile(folder).parent);
    writeFile(pomFile(folder), pomXml(name, group, version));
    mkDirectory(metafile(folder).parent);
    writeFile(metafile(folder), rascalMF(name));
    mkDirectory(folder + "src/main/rascal");
    writeFile((folder + "src/main/rascal") + "Main.rsc", emptyModule());
}

private loc pomFile(loc folder) = folder + "pom.xml";

private str emptyModule() = "module Main
                            '
                            'import IO;
                            '
                            'int main(int testArgument=0) {
                            '    println(\"argument: \<testArgument\>\");
                            '    return testArgument;
                            '}
                            '";

private str rascalMF(str name) 
  = "Manifest-Version: 0.0.1
    'Project-Name: <name>
    'Source: src/main/rascal
    'Require-Libraries: 
    ";

private str pomXml(str name, str group, str version)  
  = "\<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
    '  \<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
    '  xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\>
    '  \<modelVersion\>4.0.0\</modelVersion\>
    '
    '  \<groupId\><group>\</groupId\>
    '  \<artifactId\><name>\</artifactId\>
    '  \<version\><version>\</version\>
    '
    '  \<properties\>
    '    \<project.build.sourceEncoding\>UTF-8\</project.build.sourceEncoding\>
    '  \</properties\>
    '
    '  \<repositories\>
    '    \<repository\>
    '        \<id\>usethesource\</id\>
    '        \<url\>https://releases.usethesource.io/maven/\</url\>
    '    \</repository\>
    '  \</repositories\>
    '
    '  \<pluginRepositories\>
    '    \<pluginRepository\>
    '       \<id\>usethesource\</id\>
    '       \<url\>https://releases.usethesource.io/maven/\</url\>
    '    \</pluginRepository\>
    '  \</pluginRepositories\>
    '
    '  \<dependencies\>
    '    \<dependency\>
    '      \<groupId\>org.rascalmpl\</groupId\>
    '      \<artifactId\>rascal\</artifactId\>
    '      \<version\><getRascalVersion()>\</version\>
    '    \</dependency\>
    '  \</dependencies\>
    '
    '  \<build\>
    '    \<plugins\>
    '      \<plugin\>
    '        \<groupId\>org.apache.maven.plugins\</groupId\>
    '        \<artifactId\>maven-compiler-plugin\</artifactId\>
    '        \<version\>3.8.0\</version\>
    '        \<configuration\>
    '          \<compilerArgument\>-parameters\</compilerArgument\> 
    '          \<release\>11\</release\>
    '        \</configuration\>
    '      \</plugin\>
    '      \<plugin\>
    '        \<groupId\>org.rascalmpl\</groupId\>
    '        \<artifactId\>rascal-maven-plugin\</artifactId\>
    '        \<version\>0.8.2\</version\>
    '        \<configuration\>
    '          \<errorsAsWarnings\>true\</errorsAsWarnings\>
    '          \<bin\>${project.build.outputDirectory}\</bin\>
    '          \<srcs\>
    '            \<src\>${project.basedir}/src/main/rascal\</src\>
    '          \</srcs\>
    '        \</configuration\>
    '      \</plugin\>
    '    \</plugins\>
    '  \</build\>
    '\</project\>
    ";
