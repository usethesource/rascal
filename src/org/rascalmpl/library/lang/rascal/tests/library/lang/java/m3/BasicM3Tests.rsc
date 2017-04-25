module lang::rascal::tests::library::lang::java::m3::BasicM3Tests

import List;
import Message;
import IO;
import util::FileSystem;
import Exception;
import ValueIO;
import Node;
import lang::java::m3::Core;
import lang::java::m3::AST;

@javaClass{org.rascalmpl.library.lang.rascal.tests.library.lang.java.m3.SnakesAndLadders}
public java loc getSnakesAndLaddersPath(); 

public loc unpackExampleProject(str name, loc projectZip) {
    targetRoot = |tmp:///<name>|;
    sourceRoot = projectZip[scheme = "jar+<projectZip.scheme>"][path = projectZip.path + "!/"];
    if (copyDirectory(sourceRoot, targetRoot)) {
        return targetRoot;
    }
    throw io("Could not copy contents of <projectZip> to <targetRoot>");
}


private list[loc] junitClassPath(loc root)
    = [
        root + "/lib/hamcrest-hamcrest-core-1.3.jar", 
        root + "/lib/hamcrest-library-1.3.jar"
    ];

private list[loc] junitSourcePath(loc root) 
    = [
        root + "/src/main/java/", 
        root + "/src/test/java/"
    ];

public set[Declaration] getJunitASTs(loc root) 
    = buildASTs(root + "/src/", junitClassPath(root), junitSourcePath(root));

public M3 getJunitM3(loc root) 
    = buildM3(|project://junit4|, root + "/src/", junitClassPath(root), junitSourcePath(root));

set[Declaration] buildASTs(loc root, list[loc] classPath, list[loc] sourcePath) 
    = createAstsFromFiles(find(root, "java"), true, sourcePath = sourcePath, classPath = classPath, javaVersion ="1.7");

private M3 buildM3(loc projectName, loc root, list[loc] classPath, list[loc] sourcePath) 
    = composeJavaM3(projectName, createM3sFromFiles(find(root, "java"),sourcePath = sourcePath, classPath = classPath, javaVersion ="1.7"));


// TODO: refactor out test similarities
@ignoreCompiler{M3 not yet supported}
public test bool junitM3RemainedTheSame() 
    = compareM3s(
        readBinaryValueFile(#M3, |testdata:///m3/junit4-m3s.bin|),
        getJunitM3(unpackExampleProject("junit4", |testdata:///m3/junit4-project-source.zip|)) 
   );
 
@ignoreCompiler{M3 not yet supported}
public test bool junitASTsRemainedTheSame() 
    = compareASTs(
        readBinaryValueFile(#set[Declaration], |testdata:///m3/junit4-asts.bin|),
        getJunitASTs(unpackExampleProject("junit4", |testdata:///m3/junit4-project-source.zip|)) 
    );
  
   



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

private bool compareASTs(set[Declaration] a, set[Declaration] b) = a == b;

private bool compareMessages(Message a, Message b) {
	return "<a>" < "<b>";
} 

// TODO: think if this can be replaced by the generic diff function.
private bool compareM3s(M3 a, M3 b) {
	aKeys = getKeywordParameters(a);
	bKeys = getKeywordParameters(b);
	for (ak <- aKeys) {
		if (!(ak in bKeys)) {
			throw "<ak>  missing in reference";
		}
		if (aKeys[ak] != bKeys[ak]) {
			if (set[value] aks := aKeys[ak] && set[value] bks := bKeys[ak]) {
				println("Missing in relation to reference: ");
				iprintln(bks - aks);
				println("More than reference:");
				iprintln(aks - bks);
			}
			else if (list[Message] akl := aKeys[ak] && list[Message] bkl := bKeys[ak]) {
				if (size(akl) != size(bkl)) {
					throw "Different sized lists";
				}
				akl = sort(akl, compareMessages);
				bkl = sort(bkl, compareMessages);
				if (akl == bkl) {
					//No worries, just sorting!;
					continue;
				}
				for (i <- [0..size(akl)]) {
					if (akl[i] != bkl[i]) {
						println("<i> differs");
						iprintln([(akl[i]), (bkl[i])]);
					}
				}
			}
			throw "<ak> has different value";
		}
	}
	for (bk <- bKeys) {
		if (!(bk in aKeys)) {
			throw "<bk> missing in result";
		}
	}
	return true;
}

@ignoreCompiler{M3 not yet supported}
public test bool m3sAreSame() 
    = getSnakesAndLaddersPath().scheme != "unknown" && compareM3s(getSnakesAndLaddersM3(), readBinaryValueFile(#M3, |compressed+testdata:///example-project/p2-SnakesAndLadders/m3-results/m3.bin.xz|));


@ignoreCompiler{M3 not yet supported}
public test bool astsAreSame() 
    = getSnakesAndLaddersPath().scheme != "unknown" && compareASTs(getSnakesAndLaddersASTs(), readBinaryValueFile(#set[Declaration], |compressed+testdata:///example-project/p2-SnakesAndLadders/m3-results/m3ast.bin.xz|));
