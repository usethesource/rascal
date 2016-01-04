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

module util::Reflective

import Exception;
import Message;
import ParseTree;
import IO;
import String;
import lang::rascal::\syntax::Rascal;
import lang::manifest::IO;

public Tree getModuleParseTree(str modulePath) {
    return parseModule(getModuleLocation(modulePath));
}

public Tree getModuleParseTree(str modulePath, PathConfig pcfg) {
    return parseModule(getModuleLocation(modulePath, pcfg));
}

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to evaluate}
public java lrel[str result, str out, str err] evalCommands(list[str] command, loc org);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to get back the parse tree for the given command}
public java Tree parseCommand(str command, loc location);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to get back the parse tree for the given commands}
public java Tree parseCommands(str commands, loc location);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to access the Rascal module parser}
@doc{This parses a module from a string, in its own evaluator context}
public java Tree parseModule(str moduleContent, loc location);


lang::rascal::\syntax::Rascal::Module parseModuleAndGetTop(loc moduleLoc){
    tree = parseModule(moduleLoc);
    if(tree has top){
        tree = tree.top;
    }
    if(lang::rascal::\syntax::Rascal::Module M := tree){
        return M;
    }
    throw tree;
}

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to access the Rascal module parser}
@doc{This parses a module on the search path, and loads it into the current evaluator including all of its imported modules}
public java Tree parseModule(loc location);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to access the Rascal module parser}
public java Tree parseModule(loc location, list[loc] searchPath);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to resolve a module name in the Rascal search path}
public java loc getModuleLocation(str modulePath);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to resolve a path name in the Rascal search path}
public java loc getSearchPathLocation(str filePath);

data PathConfig 
  = pathConfig(list[loc] srcPath = [|std:///|],        // List of directories to search for source files
               list[loc] libPath = [|boot:///stdlib|, |std:///|],        
                                                        // List of directories to search source for derived files
               //list[loc] projectPath = [],             // List of directories to search for source or derived files in projects
                                                        // Note: each directory should include the project name as last path element
               loc binDir = |home:///bin/|,            // Global directory for derived files outside projects
               loc bootDir = |boot+compressed:///|     // Directory with Rascal boot files
              );

data RascalManifest
  = rascalManifest(
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

@doc{
  Converts a PathConfig and replaces all references to roots of projects or bundles
  by the folders which are nested under these roots as configured in their respective
  META-INF/RASCAL.MF files.
}
PathConfig applyManifests(PathConfig cfg) {
   mf = (l:readManifest(#RascalManifest, metafile(l)) | l <- cfg.srcPath + cfg.libPath + [cfg.binDir], exists(metafile(l)));

   list[loc] expandSrcPath(loc p) = [ p + s | s <- mf[p].Source] when mf[p]?;
   default list[loc] expandSrcPath(loc p, str _) = [p];
   
   list[loc] expandLibPath(loc p) = [ p + s | s <- mf[p].\Required-Libraries] when mf[p]?;
   default list[loc] expandLibPath(loc p, str _) = [p];
    
   loc expandBinDir(loc p) = p + mf[p].Bin when mf[p]?;
   default loc expandBinDir(loc p) = p;
   
   cfg.srcPath = [*expandSrcPath(p) | p <- cfg.srcPath];
   cfg.libPath = [*expandLibPath(p) | p <- cfg.libPath];
   cfg.binDir  = expandBinDir(cfg.binDir);
   
   // TODO: here we add features for Required-Libraries by searching in a repository of installed
   // jars. This has to be resolved recursively.
   
   return cfg;
}

str makeFileName(str qualifiedModuleName, str extension = "rsc") = replaceAll(qualifiedModuleName, "::", "/") + "." + extension;

loc getSearchPathLoc(str filePath, PathConfig pcfg){
    for(loc dir <- pcfg.srcPath + pcfg.libPath){
        fileLoc = dir + filePath;
        if(exists(fileLoc)){
            //println("getModuleLocation <qualifiedModuleName> =\> <fileLoc>");
            return fileLoc;
        }
    }
    throw "Module with path <filePath> not found";
}

loc getModuleLocation(str qualifiedModuleName,  PathConfig pcfg){
    fileName = makeFileName(qualifiedModuleName);
    //for(loc dir <- pcfg.projectPath){
    //    fileLoc = dir + ("src/" + fileName);
    //    if(exists(fileLoc)){
    //        println("getModuleLocation <qualifiedModuleName> =\> <fileLoc>");
    //        return fileLoc;
    //    }
    //}
    for(loc dir <- pcfg.srcPath){
        fileLoc = dir + fileName;
        if(exists(fileLoc)){
            //println("getModuleLocation <qualifiedModuleName> =\> <fileLoc>");
            return fileLoc;
        }
    }
    throw "Module <qualifiedModuleName> not found";
}

str getModuleName(loc moduleLoc,  PathConfig pcfg){
    modulePath = moduleLoc.path;
    
    if(!endsWith(modulePath, "rsc")){
        throw "Not a Rascal source file: <moduleLoc>";
    }
    //if(moduleLoc.scheme == "project"){
    //    for(loc dir <- pcfg.projectPath && dir.file == moduleLoc.authority){
    //        dir.path = dir.path + "/" + modulePath;
    // 
    //        println("modulePath = <modulePath>, dir = <dir>");
    //        if(exists(dir)){ 
    //           moduleName = replaceFirst(modulePath, "/src/", "");
    //           moduleName = replaceLast(moduleName, ".rsc", "");
    //           moduleName = replaceAll(moduleName, "/", "::");
    //           return moduleName;
    //        }
    //    }
    //    throw "No module name found for <moduleLoc>";
    //}
    for(loc dir <- pcfg.srcPath){
        if(startsWith(modulePath, dir.path) && moduleLoc.scheme == dir.scheme){
           moduleName = replaceFirst(modulePath, dir.path, "");
           moduleName = replaceLast(moduleName, ".rsc", "");
           moduleName = replaceAll(moduleName, "/", "::");
           return moduleName;
        }
    }
    throw "No module name found for <moduleLoc>";
}

//bool isDefinedInSearchPath(str filePath, list[loc] searchPath, bool compressed){
//    for(loc d <- searchPath){
//        if(compressed){
//           d.scheme = "compressed+" + d.scheme;
//        }
//        if(exists(d + filePath)){
//            return true;
//        }
//    }
//    return false;
//}

@doc{   
Synopsis: Derive a location from a given module name for reading

Description:
Given a module name, a file name extension, and a PathConfig,
a path name is constructed from the module name + extension.

If a file F with this path exists in one of the directories in the PathConfig,
then the pair <true, F> is returned. Otherwise <false, some error location> is returned.

For a source extension (typically "rsc" or "mu" but this can be configured) srcPath is searched, otherwise binPath + libPath.

Examples:
<screen>
import util::Reflective;
getDerivedReadLoc("List", "rsc", pathConfig());
getDerivedReadLoc("experiments::Compiler::Compile", "rvm", pathConfig());
getDerivedReadLoc("experiments::Compiler::muRascal2RVM::Library", "mu", pathConfig());
</screen>

Benefits:
This function is useful for type checking and compilation tasks, when derived information related to source modules has to be read
from locations in different, configurable, directories.
}

tuple[bool, loc] getDerivedReadLoc(str qualifiedModuleName, str extension, PathConfig pcfg, set[str] srcExtensions = {"rsc", "mu"}){
    fileName = makeFileName(qualifiedModuleName, extension=extension);
    //println("getDerivedReadLoc: <fileName>");
   
    if(extension in srcExtensions){
       //for(loc dir <- pcfg.projectPath){    // In a project directory?
       //    fileLoc = dir + ("src/" + fileName);
       //    if(exists(fileLoc)){
       //       println("getDerivedReadLoc <qualifiedModuleName> =\> <fileLoc>");
       //       return <true, fileLoc>;
       //    }
       //}
       for(loc dir <- pcfg.srcPath){        // In a source directory?
           fileLoc = dir + fileName;
           if(exists(fileLoc)){
             //println("getDerivedReadLoc: <qualifiedModuleName>, <extension> =\> <fileLoc");
             return <true, fileLoc>;
           }
       }
    } else {
      // A binary (possibly library) module
      compressed = endsWith(extension, "gz");
      //for(loc dir <- pcfg.projectPath){     // In a project directory
      //     fileLoc = dir + ("bin/" + fileName);
      //     if(exists(fileLoc)){
      //        if(compressed){
      //           fileLoc.scheme = "compressed+" + fileLoc.scheme;
      //        }
      //        println("getDerivedReadLoc <qualifiedModuleName> =\> <fileLoc>");
      //        return <true, fileLoc>;
      //     }
      //}
      for(loc dir <- pcfg.binDir + pcfg.libPath){   // In a bin or lib directory?
       
        fileLoc = dir + fileName;
        if(exists(fileLoc)){
           if(compressed){
              fileLoc.scheme = "compressed+" + fileLoc.scheme;
           }
           //println("getDerivedReadLoc: <qualifiedModuleName>, <extension> =\> <fileLoc>");
           return <true, fileLoc>;
        }
      }
    }
    //println("getDerivedReadLoc: <qualifiedModuleName>, <extension> =\> |error:///|");
    return <false, |error:///|>;
}

@doc{   
Synopsis: Derive a location from a given module name for writing

Description:
Given a module name, a file name extension, and a PathConfig,
a path name is constructed from the module name + extension.

For source modules, a writable location cannot be derived.
For other modules, a location for this path in binDir will be returned.

Examples:
<screen>
import util::Reflective;
getDerivedWriteLoc("List", "rvm", pathConfig());
getDerivedWriteLoc("experiments::Compiler::Compile", "rvm", pathConfig());
getDerivedWriteLoc("experiments::Compiler::muRascal2RVM::Library", "mu", pathConfig());
</screen>

Benefits:
This function is useful for type checking and compilation tasks, when derived information related to source modules has to be written
to locations in separate, configurable, directories.
}
loc getDerivedWriteLoc(str qualifiedModuleName, str extension, PathConfig pcfg, set[str] srcExtensions = {"rsc", "mu"}){
    if(extension in srcExtensions){
        throw "Cannot derive writable location for module <qualifiedModuleName> with extension <extension>";
    }
    fileNameSrc = makeFileName(qualifiedModuleName);
    fileNameBin = makeFileName(qualifiedModuleName, extension=extension);
    compressed = endsWith(extension, "gz");
    
    //for(loc dir <- pcfg.projectPath){
    //    fileLocSrc = dir + ("src/" + fileNameSrc);
    //    if(exists(fileLocSrc)){
    //       loc fileLocBin = dir + ("bin/" + fileNameBin);
    //       if(compressed){
    //          fileLocBin.scheme = "compressed+" + fileLocBin.scheme;
    //       }
    //    
    //       //println("getDerivedWriteLoc <qualifiedModuleName> =\> <fileLocBin>");
    //       return fileLocBin;
    //    }
    //}
    
    bindir = pcfg.binDir;
    if(compressed){
       bindir.scheme = "compressed+" + bindir.scheme;
    }
    fileLocBin = bindir + fileNameBin;
    //println("getDerivedWriteLoc: <qualifiedModuleName>, <extension> =\> <fileLocBin>");
    return fileLocBin;
}

@doc{Is the current Rascal code executed by the compiler or the interpreter?}
@javaClass{org.rascalmpl.library.util.Reflective}
public java bool inCompiledMode();

@doc{Give a textual diff between two values.}
@javaClass{org.rascalmpl.library.util.Reflective}
public java str diff(value old, value new);

@doc{Watch value val: 
- running in interpreted mode: write val to a file, 
- running in compiled mode: compare val with previously written value}
@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to resolve a module name in the Rascal search path}
public java &T watch(type[&T] tp, &T val, str name);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to resolve a module name in the Rascal search path}
public java &T watch(type[&T] tp, &T val, str name, value suffix);

@doc{Compute a fingerprint of a value for the benefit of the compiler and the compiler runtime}
@javaClass{org.rascalmpl.library.util.Reflective}
public java int getFingerprint(value val, bool concretePatterns);

@doc{Compute a fingerprint of a value and arity modifier for the benefit of the compiler and the compiler runtime}
@javaClass{org.rascalmpl.library.util.Reflective}
public java int getFingerprint(value val, int arity, bool concretePatterns);

@doc{Compute a fingerprint of a complete node for the benefit of the compiler and the compiler runtime}
@javaClass{org.rascalmpl.library.util.Reflective}
public java int getFingerprintNode(node nd);