@ignoreCompiler{Test fail; Waiting for Eclipse integration}
module lang::rascal::tests::library::lang::java::m3::BasicM3Tests

import util::Reflective;
import List;
import Message;
import IO;
import util::FileSystem;
import ValueIO;
import Node;
import String;
import lang::java::m3::Core;
import lang::java::m3::AST;

@javaClass{org.rascalmpl.library.lang.rascal.tests.library.lang.java.m3.SnakesAndLadders}
public java loc getSnakesAndLaddersPath(); 

public loc unpackExampleProject(str name, loc projectZip) {
    targetRoot = |tmp:///<name>|;
    sourceRoot = projectZip[scheme = "jar+<projectZip.scheme>"][path = projectZip.path + "!/"];
    copy(sourceRoot, targetRoot, recursive=true, overwrite=true);

	return targetRoot;
}

private bool compareJarM3s(loc reference, loc jar, M3 (loc) builder)
    = compareM3s(
        readBinaryValueFile(#M3, reference),
        builder(jar)
   );


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

private M3 buildM3FromJar(loc jar) 
    = createM3FromJar(jar);
    
public M3 getHamcrestM3(loc root) 
    = buildM3FromJar(root);
    
private bool compareJarM3s(loc reference, loc jar, M3 (loc) builder)
    = compareM3s(
        readBinaryValueFile(#M3, reference),
        builder(jar) 
   );

public test bool hamcrestJarM3RemainedTheSame()
	= compareJarM3s(|testdata:///m3/hamcrest-library-1.3-m3.bin|, |testdata:///m3/hamcrest-library-1.3.jar|, getHamcrestM3);
	
// TODO: think if this can be replaced by the generic diff function.
public bool compareM3s(M3 a, M3 b) {
	aKeys = getKeywordParameters(a);
	bKeys = getKeywordParameters(b);
	for (ak <- aKeys) {
	
		if (!(ak in bKeys)) {
			throw "<ak>  missing in reference";
		}
		if (aKeys[ak] != bKeys[ak]) {
			if (set[value] aks := aKeys[ak] && set[value] bks := bKeys[ak]) {
				println("<ak>: Missing in relation to reference: ");
				iprintln(aks - bks);
				println("<ak> More than reference:");
				iprintln(bks - aks);
			}
			else if (list[Message] akl := aKeys[ak] && list[Message] bkl := bKeys[ak]) {
				// In case of different size tell the difference.
				if (size(akl) != size(bkl)) {
					println("Missing messages with regards to original relation: ");
					iprintln(bkl - akl);
					println("Additional messages with regards to original relation: ");
					iprintln(akl - bkl);
				}
				//Otherwise, check if all values remain the same.
				else {
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
