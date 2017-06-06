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
    throw IO("Could not copy contents of <projectZip> to <targetRoot>");
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


private list[loc] snakesClassPath(loc root)
    = [root + "jexample-4.5-391.jar"];

private list[loc] snakesSourcePath(loc root)
    = [root + "/src/"];
    

public set[Declaration] getSnakesASTs(loc root) 
    = buildASTs(root + "/src/", snakesClassPath(root), snakesSourcePath(root));

public M3 getSnakesM3(loc root) 
    = buildM3(|project://snakes-and-ladders|, root + "/src/", snakesClassPath(root), snakesSourcePath(root));


set[Declaration] buildASTs(loc root, list[loc] classPath, list[loc] sourcePath) 
    = createAstsFromFiles(find(root, "java"), true, sourcePath = sourcePath, classPath = classPath, javaVersion ="1.7");

private M3 buildM3(loc projectName, loc root, list[loc] classPath, list[loc] sourcePath) 
    = composeJavaM3(projectName, createM3sFromFiles(find(root, "java"),sourcePath = sourcePath, classPath = classPath, javaVersion ="1.7"));


// unpackExampleProject("snakes-and-ladders", |testdata:///m3/snakes-and-ladders-project-source.zip|);

private bool compareM3s(loc reference, str projectName, loc sourceZip, M3 (loc) builder)
    = compareM3s(
        readBinaryValueFile(#M3, reference),
        builder(unpackExampleProject(projectName, sourceZip)) 
   );

@ignoreCompiler{M3 not yet supported}
public test bool junitM3RemainedTheSame() 
    = compareM3s(|testdata:///m3/junit4-m3s.bin|, "junit4", |testdata:///m3/junit4-project-source.zip|, getJunitM3); 
 
@ignoreCompiler{M3 not yet supported}
public test bool snakesM3RemainedTheSame() 
    = compareM3s(|testdata:///m3/snakes-and-ladders-m3s.bin|, "snakes-and-ladders", |testdata:///m3/snakes-and-ladders-project-source.zip|, getSnakesM3); 
 
 
private bool compareASTs(loc reference, str projectName, loc sourceZip, set[Declaration] (loc) builder) 
    = compareASTs(
        readBinaryValueFile(#set[Declaration], reference),
        builder(unpackExampleProject(projectName, sourceZip)) 
    );
 
@ignoreCompiler{M3 not yet supported}
public test bool junitASTsRemainedTheSame() 
    = compareASTs(|testdata:///m3/junit4-asts.bin|, "junit4", |testdata:///m3/junit4-project-source.zip|, getJunitASTs);
        
@ignoreCompiler{M3 not yet supported}
public test bool snakesASTsRemainedTheSame() 
    = compareASTs(|testdata:///m3/snakes-and-ladders-asts.bin|, "snakes-and-ladders", |testdata:///m3/snakes-and-ladders-project-source.zip|, getSnakesASTs);    


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