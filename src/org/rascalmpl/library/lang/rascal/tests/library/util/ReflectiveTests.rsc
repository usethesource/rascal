module lang::rascal::tests::library::util::ReflectiveTests

import IO;
import List;
import String;
import util::FileSystem;
import util::Reflective;
import util::UUID;
    
private loc testLibraryLoc = |memory://myTestLibrary-<uuid().authority>/|;

test bool commonSuffixCommutative(list[str] a, list[str] b) = commonSuffix(a, b) == commonSuffix(b, a);
test bool cs1() = commonSuffix([], ["c"]) == 0;

test bool cs2() = commonSuffix(["c"], ["c"]) == 1;

test bool cs3() = commonSuffix(["a", "b", "c"], ["c"]) == 1;

test bool cs4() = commonSuffix(["a", "b", "c"], ["b", "c"]) == 2;
test bool cs5() = commonSuffix(["a", "b", "c"], ["a", "b", "c"]) == 3;
test bool cs6() = commonSuffix(["a", "b", "c"], ["z", "a", "b", "c"]) == 3;
test bool cs7() = commonSuffix(["a", "b", "c"], ["a", "b", "d"]) == 0;

test bool moduleExceptionWithSrc() {
    pcfg = pathConfig(srcs=[|project://rascal/src/org/rascalmpl/library/|]);
    return getModuleName(|project://rascal/src/org/rascalmpl/library/Exception.rsc|, pcfg) 
            == "Exception";
}

test bool moduleReflectiveWithSrc() {
    pcfg = pathConfig(srcs=[|project://rascal/src/org/rascalmpl/library/|]);
    return getModuleName(|project://rascal/src/org/rascalmpl/library/util/Reflective.rsc|, pcfg) 
            == "util::Reflective";
}

test bool moduleExceptionOnlyTpl() {
      writeFile(testLibraryLoc + "/resources/rascal/$Exception.tpl",
        "$Exception.tpl (only file matters, content irrelevant)
        ");
    pcfg = pathConfig(libs=[testLibraryLoc + "/resources/"]
                     );
    return getModuleName(|project://rascal/src/org/rascalmpl/library/Exception.rsc|, pcfg) 
        == "Exception";
}

test bool moduleReflectiveOnlyTpl() {
      writeFile(testLibraryLoc + "/resources/rascal/util/Reflective.tpl",
        "util::$Reflective.tpl (only file matters, content irrelevant)
        ");
    pcfg = pathConfig(srcs = [],
                    libs=[testLibraryLoc + "/resources/"]
                     );
    return getModuleName(|project://rascal/src/org/rascalmpl/library/util/Reflective.rsc|, pcfg) 
            == "util::Reflective";
}

test bool longestModuleReflectiveOnlyTpl() {
      writeFile(testLibraryLoc + "/1/resources/rascal/$Reflective.tpl",
        "$Reflective.tpl at top level (only file matters, content irrelevant)
        ");
      writeFile(testLibraryLoc + "/2/resources/rascal/util/Reflective.tpl",
        "util::$Reflective.tpl in subdir util (only file matters, content irrelevant)
        ");
    pcfg = pathConfig(srcs= [], 
                      libs=[testLibraryLoc + "1/resources/", testLibraryLoc + "/2/resources/"]
                     );
    return getModuleName(|project://rascal/src/org/rascalmpl/library/util/Reflective.rsc|, pcfg) 
            == "util::Reflective";
}


test bool moduleOnlyInSecondSrc(){
  testLibrarySrc = testLibraryLoc + "src/org/rascalmpl/library/";
  writeFile(testLibrarySrc + "E.rsc",
        "module E");

   pcfg = pathConfig(srcs=[|project://rascal/src/org/rascalmpl/library/|, testLibrarySrc]);
   return getModuleName(testLibraryLoc + "E", pcfg) 
            == "E";
}