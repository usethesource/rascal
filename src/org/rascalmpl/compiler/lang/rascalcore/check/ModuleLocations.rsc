@bootstrapParser
module lang::rascalcore::check::ModuleLocations

import IO;
import List;
import String;
import util::Reflective;
import util::FileSystem;

str makeFileName(str qualifiedModuleName, str extension = "rsc") {
    str qnameSlashes = replaceAll(qualifiedModuleName, "::", "/");
    int n = findLast(qnameSlashes, "/");
    str prefix = extension == "rsc" ? "" : "$";
    str package = extension == "rsc" ? "" : "rascal/";
    qnameSlashes = n < 0 ? "<prefix>" + qnameSlashes : qnameSlashes[0..n] + "/<prefix>" + qnameSlashes[n+1..];
    return "<package><qnameSlashes><isEmpty(extension) ? "" : ".<extension>">";
}

loc getSearchPathLoc(str filePath, PathConfig pcfg){
    for(loc dir <- pcfg.srcs + pcfg.libs){
        fileLoc = dir + filePath;
        if(exists(fileLoc)){
            //println("getRascalModuleLocation <qualifiedModuleName> =\> <fileLoc>");
            return fileLoc;
        }
    }
    throw "Module with path <filePath> not found"; 
}

@synopsis{Get the location of a named module, search for `src` in srcs and `tpl` in libs}
loc getRascalModuleLocation(str qualifiedModuleName,  PathConfig pcfg){
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
str getRascalModuleName(loc moduleLoc,  PathConfig pcfg){
    modulePath = moduleLoc.path;

    rscFile = endsWith(modulePath, "rsc");
    tplFile = endsWith(modulePath, "tpl");
    
    if(!( rscFile || tplFile )){
        throw "Not a Rascal .src or .tpl file: <moduleLoc>";
    }
    
    // Find matching .rsc file in source directories
    if(rscFile){
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
    }
    
    // Find longest matching .tpl file in library directories
  
    <modulePathNoExt, ext> = splitFileExtension(modulePath);
    while(modulePathNoExt[0] == "/"){
        modulePathNoExt = modulePathNoExt[1..];
    }
    
    modulePathAsList = split("/", modulePathNoExt);
    if(tplFile){
        lastName = modulePathAsList[-1];
        if(lastName[0] == "$"){
            modulePathAsList = [*modulePathAsList[..-1],lastName[1..]];
        }
    }
    if(modulePathAsList[0] == "rascal"){
         modulePathAsList = modulePathAsList[1..];
    }
    modulePathReversed = reverse(modulePathAsList);
    
    int longestSuffix = 0;
    for(loc dir <- pcfg.libs){
        dir = dir + "rascal";
        dpath = dir.path;
       
        while(dpath[0] == "/"){
            dpath = dpath[1..];
        }
       
        for(loc file <- find(dir, "tpl")){
            candidate = replaceFirst(file.path, dpath, "");    
            <candidate, ext> = splitFileExtension(candidate);
            while(candidate[0] == "/"){
                candidate = candidate[1..];
            }
            
            candidateAsList = split("/", candidate);
            lastName = candidateAsList[-1];
            if(lastName[0] == "$"){
                candidateAsList = [*candidateAsList[..-1],lastName[1..]];
            }
            // println("cand: <candidateAsList>, modpath: <modulePathAsList>");
            n = commonPrefix(reverse(candidateAsList), modulePathReversed);
                        
            if(n > longestSuffix){
                longestSuffix = n;
            }
        }
    }
    
    if(longestSuffix > 0){
        lastName = modulePathAsList[-1];
        if(lastName[0] == "$"){
            modulePathAsList = [*modulePathAsList[..-1],lastName[1..]];
        }
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
getRascalModuleDerivedReadLoc("List", "rsc", pathConfig());
getRascalModuleDerivedReadLoc("experiments::Compiler::Compile", "rvm", pathConfig());
getRascalModuleDerivedReadLoc("experiments::Compiler::muRascal2RVM::Library", "mu", pathConfig());
```
}
@benefits{
This function is useful for type checking and compilation tasks, when derived information related to source modules has to be read
from locations in different, configurable, directories.
}

tuple[bool, loc] getRascalModuleDerivedReadLoc(str qualifiedModuleName, str extension, PathConfig pcfg, set[str] srcExtensions = {"rsc", "mu"}, str rootDir = ""){
    fileName = makeFileName(qualifiedModuleName, extension=extension);
    //println("getRascalModuleDerivedReadLoc: <fileName>");
   
    if(extension in srcExtensions){
       for(loc dir <- pcfg.srcs){        // In a source directory?
           fileLoc = dir + rootDir + fileName;
           if(exists(fileLoc)){
             //println("getRascalModuleDerivedReadLoc: <qualifiedModuleName>, <extension> =\> <fileLoc");
             return <true, fileLoc>;
           }
       }
    } else {
      for(loc dir <- pcfg.bin + pcfg.libs){   // In a bin or lib directory?
       
        fileLoc = dir + rootDir + fileName;
        if(exists(fileLoc)){
           //println("getRascalModuleDerivedReadLoc: <qualifiedModuleName>, <extension> =\> <fileLoc>");
           return <true, fileLoc>;
        }
      }
    }
    //println("getRascalModuleDerivedReadLoc: <qualifiedModuleName>, <extension> =\> |error:///|");
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
getRascalModuleDerivedWriteLoc("List", "rvm", pathConfig());
getRascalModuleDerivedWriteLoc("experiments::Compiler::Compile", "rvm", pathConfig());
```

```rascal-shell,error
getRascalModuleDerivedWriteLoc("experiments::Compiler::muRascal2RVM::Library", "rsc", pathConfig());
```
}
@benefits{
This function is useful for type checking and compilation tasks, when derived information related to source modules has to be written
to locations in separate, configurable, directories.
}
loc getRascalModuleDerivedWriteLoc(str qualifiedModuleName, str extension, PathConfig pcfg, set[str] srcExtensions = {"rsc", "mu"}, str rootDir = ""){
    if(extension in srcExtensions){
        throw "Cannot derive writable location for module <qualifiedModuleName> with extension <extension>";
    }
    fileNameSrc = makeFileName(qualifiedModuleName);
    fileNameBin = makeFileName(qualifiedModuleName, extension=extension);
    
    bin = pcfg.bin;
    fileLocBin = bin + rootDir + fileNameBin;
    //println("getRascalModuleDerivedWriteLoc: <qualifiedModuleName>, <extension> =\> <fileLocBin>");
    return fileLocBin;
}