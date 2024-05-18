module lang::rascal::tests::library::util::ReflectiveTests

import util::Reflective;

import util::FileSystem;
import IO;
import String;
import List;
    

test bool commonSuffixCommutative(list[str] a, list[str] b) = commonSuffix(a, b) == commonSuffix(b, a);
test bool cs1() = commonSuffix([], ["c"]) == 0;

test bool cs2() = commonSuffix(["c"], ["c"]) == 1;

test bool cs3() = commonSuffix(["a", "b", "c"], ["c"]) == 1;

test bool cs4() = commonSuffix(["a", "b", "c"], ["b", "c"]) == 2;
test bool cs5() = commonSuffix(["a", "b", "c"], ["a", "b", "c"]) == 3;
test bool cs6() = commonSuffix(["a", "b", "c"], ["z", "a", "b", "c"]) == 3;
test bool cs7() = commonSuffix(["a", "b", "c"], ["a", "b", "d"]) == 0;

test bool moduleExceptionWithAsSrc() {
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
      writeFile(|memory://myTestLibrary/resources/rascal/$Exception.tpl|,
        "$Exception.tpl (only file matters, content irrelevant)
        ");
    pcfg = pathConfig(libs=[|memory://myTestLibrary/resources/|]
                     );
    return getModuleName(|project://rascal/src/org/rascalmpl/library/Exception.rsc|, pcfg) 
        == "Exception";
}

test bool moduleReflectiveOnlyTpl() {
      writeFile(|memory://myTestLibrary/resources/rascal/util/Reflective.tpl|,
        "util::$Reflective.tpl (only file matters, content irrelevant)
        ");
    pcfg = pathConfig(srcs = [],
                    libs=[|memory://myTestLibrary/resources/|]
                     );
    return getModuleName(|project://rascal/src/org/rascalmpl/library/util/Reflective.rsc|, pcfg) 
            == "util::Reflective";
}

test bool longestModuleReflectiveOnlyTpl() {
      writeFile(|memory://myTestLibrary1/resources/rascal/$Reflective.tpl|,
        "$Reflective.tpl at top level (only file matters, content irrelevant)
        ");
      writeFile(|memory://myTestLibrary2/resources/rascal/util/Reflective.tpl|,
        "util::$Reflective.tpl in subdir util (only file matters, content irrelevant)
        ");
    pcfg = pathConfig(srcs= [], 
                      libs=[|memory://myTestLibrary1/resources/|, |memory://myTestLibrary2/resources/|]
                     );
    return getModuleName(|project://rascal/src/org/rascalmpl/library/util/Reflective.rsc|, pcfg) 
            == "util::Reflective";
}