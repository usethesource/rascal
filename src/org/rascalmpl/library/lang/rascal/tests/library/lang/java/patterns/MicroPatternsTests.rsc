module lang::rascal::tests::library::lang::java::patterns::MicroPatternsTests

import analysis::patterns::Micro;
import lang::java::patterns::JavaToMicroPatterns;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Relation;
import Type;
import IO;
import String;
import util::FileSystem; 
import util::Math;
import util::SystemAPI;

private map[str,MicroPattern] lookupPatterns
	= ( l : make(#MicroPattern, l, []) | /label(l, adt("MicroPattern", [])) := #MicroPattern.definitions );

private str fixName(/^<f:.><t:.*>[0-9]+$/) = toLowerCase(f) + t;
private str fixName(/^<f:.><t:.*>$/) = toLowerCase(f) + t;
private default str fixName("") = "";

private rel[loc, MicroPattern] breakClassNames(set[loc] entities) 
	= {<e, lookupPatterns[fixName(n)]> | e <- entities, n <- split("And", e.file)} ;

bool testClassesMatch(M3 model, set[loc] keep, set[Declaration] asts) {
	expected = breakClassNames((classes(model) + interfaces(model)) & keep);
	result = domainR(findMicroPatterns(model, asts), keep);
	if (result == expected)
		return true;
	println("Not matched:");
	iprintln(expected - result);
	println("Extra matches:");
	iprintln(result - expected);
	return false;
	
}


private loc prepareTestProject() {
	target = |file:///<getSystemProperty("java.io.tmpdir")>| + "/patterns-<arbInt()>/";
	source = |testdata:///patterns/micro/|;
	dir = "/src/org/rascalmpl/test/data/patterns/micro/";
	mkDirectory(target + dir);
	for (e <- listEntries(source)) {
		if (endsWith(e, "java_src")) {
			copyFile(source + e, ((target + dir) + e)[extension="java"]);
		}
	}
	return target;
}

void runTest() {
	testProject = prepareTestProject();
	sourcePaths = getPaths(testProject, "java");
	m3s = { *createM3FromFile(f, sourcePath=[*findRoots(sourcePaths)]) | path <- sourcePaths , f <- find(path, bool (loc l) { return l.extension == "java"; }) };
	M3 m3 = composeJavaM3(testProject, m3s);
	asts = { createAstFromFile(f, true) | path <- sourcePaths , f <- find(path, bool (loc l) { return l.extension == "java"; }) };
	keep = { e | e <- classes(m3) + interfaces(m3), startsWith(e.path,"/org/rascalmpl/test/data/patterns/micro"), e != |java+interface:///org/rascalmpl/test/data/patterns/micro/InterfaceWithMethods|};
	if (testClassesMatch(m3, keep, asts)) {
		println("Test succeeded");
	}
	else {
		println("Test failed");
	}
}
