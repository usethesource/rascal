module lang::rascalcore::compile::Examples::Tst0


import util::Reflective;
import util::FileSystem;
import IO;
import String;
import List;

tuple[str,str] splitFileExtension(str path){
    int n = findLast(path, ".");
    if(n < 0) return <path, "">;
    return <path[0 .. n], path[n+1 .. ]>;
}

int commonSuffix(list[str] dir, list[str] m)
    = commonPrefix(reverse(dir), reverse(m));

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

test bool commonSuffixCommutative(list[str] a, list[str] b) = commonSuffix(a, b) == commonSuffix(b, a);
test bool cs1() = commonSuffix([], ["c"]) == 0;

test bool cs2() = commonSuffix(["c"], ["c"]) == 1;

test bool cs3() = commonSuffix(["a", "b", "c"], ["c"]) == 1;

test bool cs4() = commonSuffix(["a", "b", "c"], ["b", "c"]) == 2;
test bool cs5() = commonSuffix(["a", "b", "c"], ["a", "b", "c"]) == 3;
test bool cs6() = commonSuffix(["a", "b", "c"], ["z", "a", "b", "c"]) == 3;
test bool cs7() = commonSuffix(["a", "b", "c"], ["a", "b", "d"]) == 0;

str getModuleNameNew(loc moduleLoc,  PathConfig pcfg){
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
        for(loc file <- find(dir, "tpl")){
            candidate = replaceFirst(file.path, dir.path, "");
            <candidate, ext> = splitFileExtension(candidate);
            candidateAsList = split("/", candidate);
            n = commonPrefix(reverse(candidateAsList), modulePathAsListReversed);
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
    
test bool moduleExceptionWithAsSrc() {
    pcfg = pathConfig(srcs=[|project://rascal/src/org/rascalmpl/library/|]);
    return getModuleNameNew(|project://rascal/src/org/rascalmpl/library/Exception.rsc|, pcfg) 
            == "Exception";
}

test bool moduleReflectiveWithSrc() {
    pcfg = pathConfig(srcs=[|project://rascal/src/org/rascalmpl/library/|]);
    return getModuleNameNew(|project://rascal/src/org/rascalmpl/library/util/Reflective.rsc|, pcfg) 
            == "util::Reflective";
}

test bool moduleExceptionOnlyTpl() {
      writeFile(|memory://myTestLibrary/resources/Exception.tpl|,
        "module Exception TPL (only file matters, content irrelevant)
        ");
    pcfg = pathConfig(libs=[|memory://myTestLibrary/resources/|]
                     );
    return getModuleNameNew(|project://rascal/src/org/rascalmpl/library/Exception.rsc|, pcfg) 
        == "Exception";
}

test bool moduleReflectiveOnlyTpl() {
      writeFile(|memory://myTestLibrary/resources/util/Reflective.tpl|,
        "module Reflective TPL (only file matters, content irrelevant)
        ");
    pcfg = pathConfig(srcs = [],
                    libs=[|memory://myTestLibrary/resources/|]
                     );
    return getModuleNameNew(|project://rascal/src/org/rascalmpl/library/util/Reflective.rsc|, pcfg) 
            == "util::Reflective";
}

test bool longestModuleReflectiveOnlyTpl() {
      writeFile(|memory://myTestLibrary1/resources/Reflective.tpl|,
        "module Reflective TPL at top level (only file matters, content irrelevant)
        ");
      writeFile(|memory://myTestLibrary2/resources/util/Reflective.tpl|,
        "module Reflective TPL in subdir util (only file matters, content irrelevant)
        ");
    pcfg = pathConfig(srcs= [], 
                      libs=[|memory://myTestLibrary1/resources/|, |memory://myTestLibrary2/resources/|]
                     );
    return getModuleNameNew(|project://rascal/src/org/rascalmpl/library/util/Reflective.rsc|, pcfg) 
            == "util::Reflective";
}