module lang::rascal::tests::library::lang::java::m3::BasicM3Tests

import IO;
import util::FileSystem;
import ValueIO;
import Node;
import lang::java::m3::Core;
import lang::java::m3::AST;

@javaClass{org.rascalmpl.library.lang.rascal.tests.library.lang.java.m3.SnakesAndLadders}
public java loc getSnakesAndLaddersPath();

public set[Declaration] getSnakesAndLaddersASTs() {
    rootPath = getSnakesAndLaddersPath();
    libPath = [rootPath + "jexample-4.5-391.jar"];
    srcPath = [rootPath + "/src/"];
    return createAstsFromFiles(find(rootPath + "/src/", "java"), true, sourcePath = srcPath, classPath = libPath, javaVersion ="1.7");
}

public M3 getSnakesAndLaddersM3() {
    rootPath = getSnakesAndLaddersPath();
    libPath = [rootPath + "jexample-4.5-391.jar"];
    srcPath = [rootPath + "/src/"];
    return composeJavaM3(|project://SnakesAndLadders/|, createM3sFromFiles(find(rootPath +"/src/", "java"),sourcePath = srcPath, classPath = libPath, javaVersion ="1.7"));
}

@javaClass{org.rascalmpl.library.lang.rascal.tests.library.lang.java.m3.SnakesAndLadders}
private java bool equalAnnotations(value a, value b);

private bool compareASTs(set[Declaration] a, set[Declaration] b) {
    if (equalAnnotations(a,b)) {
        return true;
    }
    else {
        if (a == b) {
            throw "ASTs are equal, but not their annotations";
        }
        throw "The ASTs are different";
    }
}

private bool compareM3s(M3 a, M3 b) = a == b && getAnnotations(a) == getAnnotations(b);

private bool compareM3s(M3 a, M3 b) = a == b;

public test bool m3sAreSame() 
    = getSnakesAndLaddersPath().scheme != "unknown" && compareM3s(getSnakesAndLaddersM3(), readBinaryValueFile(#M3, |compressed+testdata:///example-project/p2-SnakesAndLadders/m3-results/m3.bin.xz|));

public test bool astsAreSame() 
    = getSnakesAndLaddersPath().scheme != "unknown" && compareASTs(getSnakesAndLaddersASTs(), readBinaryValueFile(#set[Declaration], |compressed+testdata:///example-project/p2-SnakesAndLadders/m3-results/m3ast.bin.xz|));
