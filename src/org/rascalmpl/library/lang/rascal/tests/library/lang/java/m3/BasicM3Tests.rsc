module lang::rascal::tests::library::lang::java::m3::BasicM3Tests

import IO;
import util::FileSystem;
import ValueIO;
import lang::java::m3::Core;
import lang::java::m3::AST;

@javaClass{org.rascalmpl.library.lang.rascal.tests.library.lang.java.m3.SnakesAndLadders}
public java loc getSnakesAndLaddersPath();

private loc getSnakesReferenceDataLoc() = getSnakesAndLaddersPath() + "m3-results/";

@memo
public set[Declaration] getSnakesAndLaddersASTs() {
    rootPath = getSnakesAndLaddersPath();
    if (rootPath.scheme == "file") {
        libPath = {rootPath + "jexample-4.5-391.jar"};
        srcPath = {rootPath + "/src/"};
        return createAstsFromFiles(find(rootPath + "/src/", "java"), true, sourcePath = srcPath, classPath = libPath, javaVersion ="1.7");
    }
    return {};
}

@memo
public M3 getSnakesAndLaddersM3() {
    rootPath = getSnakesAndLaddersPath();
    if (rootPath.scheme == "file") {
        libPath = {rootPath + "jexample-4.5-391.jar"};
        srcPath = {rootPath + "/src/"};
        return composeJavaM3(|project://SnakesAndLadders/|, createM3sFromFiles(find(rootPath +"/src/", "java"),sourcePath = srcPath, classPath = libPath, javaVersion ="1.7"));
    }
    return {};
}

public test bool m3sAreSame() 
    = getSnakesAndLaddersPath().scheme != "file" || getSnakesAndLaddersM3() == readBinaryValueFile(#M3, |compressed+testdata:///example-project/p2-SnakesAndLadders/m3-results/m3.bin.xz|);
public test bool astsAreSame() 
    = getSnakesAndLaddersPath().scheme != "file" || getSnakesAndLaddersASTs() == readBinaryValueFile(#set[Declaration], |compressed+testdata:///example-project/p2-SnakesAndLadders/m3-results/m3ast.bin.xz|);
