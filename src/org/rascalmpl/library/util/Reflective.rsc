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

data PathConfig =
     pathConfig(list[loc] srcPath = [|std:///|],
                list[loc] libPath = [|std:///|],
                loc binDir = |home:///bin/|,
                loc bootDir = |boot+compressed:///|
               );
                 
str makeFileName(str qualifiedModuleName, str extension = "rsc") = replaceAll(qualifiedModuleName, "::", "/") + "." + extension;

loc getSearchPathLoc(str filePath, PathConfig pcfg){
    for(loc l <- pcfg.srcPath + pcfg.libPath){
        p = l + filePath;
        if(exists(p)){
            //println("getModuleLocation <qualifiedModuleName> =\> <p>");
            return p;
        }
    }
    throw "<filePath> not found";
}

loc getModuleLocation(str qualifiedModuleName,  PathConfig pcfg){
    fileName = makeFileName(qualifiedModuleName);
    for(loc l <- pcfg.srcPath){
        p = l + fileName;
        if(exists(p)){
            //println("getModuleLocation <qualifiedModuleName> =\> <p>");
            return p;
        }
    }
    throw "<qualifiedModuleName> not found";
}

str getModuleName(loc moduleLoc,  PathConfig pcfg){
    modulePath = moduleLoc.path;
    
    if(!endsWith(modulePath, "rsc")){
        throw "Not a Rascal source file: <moduleLoc>";
    }
    for(loc l <- pcfg.srcPath){
        if(startsWith(modulePath, l.path) && moduleLoc.scheme == l.scheme){
           moduleName = replaceFirst(modulePath, l.path, "");
           moduleName = replaceLast(moduleName, ".rsc", "");
           moduleName = replaceAll(moduleName, "/", "::");
           return moduleName;
        }
    }
    throw "No module name found for <moduleLoc>";
}

bool isDefinedInSearchPath(str filePath, list[loc] searchPath, bool compressed){
    for(loc d <- searchPath){
        if(compressed){
           d.scheme = "compressed+" + d.scheme;
        }
        if(exists(d + filePath)){
            return true;
        }
    }
    return false;
}

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
    compressed = endsWith(extension, "gz");
    if(extension in srcExtensions){
       if(isDefinedInSearchPath(fileName, pcfg.srcPath, compressed)){
          loc d = pcfg.binDir;
          if(compressed){
             d.scheme = "compressed+" + d.scheme;
          }
          loc p = d + fileName;
          //println("getDerivedReadLoc: <qualifiedModuleName>, <extension> =\> <p>");
          return <true, p>;
       }
    } else {
      // A binary (possibly library) module
      for(loc l <- pcfg.binDir + pcfg.libPath){
        if(compressed){
           l.scheme = "compressed+" + l.scheme;
        }
        p = l + fileName;
        if(exists(p)){
            //println("getDerivedReadLoc: <qualifiedModuleName>, <extension> =\> <p>");
            return <true, p>;
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
    fileName = makeFileName(qualifiedModuleName, extension=extension);
    d = pcfg.binDir;
    if(endsWith(extension, "gz")){
       d.scheme = "compressed+" + d.scheme;
    }
    result = d + fileName;
    //println("getDerivedWriteLoc: <qualifiedModuleName>, <extension> =\> <result>");
    return result;
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