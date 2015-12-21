module lang::rascal::tests::library::lang::csv::CSVIOTests

import IO;
import Type;
import lang::csv::IO;
import util::Math;
import ParseTree;

loc targetFile = |test-modules:///test.csv|;

bool readWrite(set[&T] dt) = readWrite(type(typeOf(dt), ()), dt);
bool readWrite(type[&T] returnType, set[&T1] dt) {
	if (dt == {}) return true;
	writeCSV(dt, targetFile);

	if (dt != readCSV(returnType, targetFile)) {
		throw "Default read/write params";
	}
	writeCSV(dt, targetFile, header = false);
	if (dt != readCSV(returnType, targetFile, header = false)) {
		throw "Header = false";
	}

	writeCSV(dt, targetFile, separator = ";");
	if (dt != readCSV(returnType, targetFile, separator = ";")) {
		throw "separator = ;";
	}

	if (/\node() !:= typeOf(dt)) {
	   dt = fixAmbStrings(dt);
		writeCSV(dt, targetFile);
		if (dt != readCSV(targetFile)) {
			throw "inferred types";	
		}
	}
	return true;
}

&T fixAmbStrings(&T v) {
	return visit(v) {
		case str s => "a" + s
			when /^[ \t\n]*[0-9]+[ \t\n]*$/ := s
		case str s => "a" + s
			when /^[ \t\n]*[0-9]+[ \t\n]*\.[0-9]*[ \t\n]*$/ := s
		case str s => "a" + s
			when /^[ \t\n]*[0-9][ \t\n]*+r[ \t\n]*[0-9]*[ \t\n]*$/ := s
		case str s => "a" + s
			when /^[ \t\n]*\<[ \t\<\>]*\>[ \t\n]*$/ := s
		case str s => "a" + s
			when /^[ \t\n]*\<([ \t]|[^\>,])*\>[ \t\n]*$/ := s
		case str s => "a" + s + "a"
			when /^[ \t\n]*[{\(\[][ \t\n]*[\)}\]][ \t\n]*$/ := s
	};
}

test bool csvBooleanInfer() {
    writeFile(targetFile, "col1,col2\nTRUE,True");
    return readCSV(targetFile) == {<true, true>};
}

test bool csvBoolean() {
    writeFile(targetFile, "col1,col2\nTRUE,True");
    return readCSV(#rel[bool col1, bool col2], targetFile) == {<true, true>};
}

test bool csvWithLoc(rel[loc first, int second] dt) = readWrite(dt);
test bool csvWithStr(rel[str first, int second] dt) = readWrite(dt);
test bool csvWithList(rel[list[&T] first, int second] dt) = readWrite(dt);
test bool csvWithSet(rel[set[&T] first, int second] dt) = readWrite(dt);
test bool csvWithMap(rel[map[&T, &Y] first, int second] dt) = readWrite(dt);
test bool csvWithNode(rel[node first, int second] dt) = readWrite(dt);

test bool csvRandom(rel[&T,&X] dt) = readWrite(dt);

test bool csvMoreTuples(rel[str a, str b, int c, bool d, real e] dt) = readWrite(dt);
test bool csvMoreRandomTypes(rel[&T1 a, &T2 b, int c, str d, &T3 e] dt) = readWrite(dt);


@memo str createString(int j) = ("a" | it + "<i>" | i <- [0..j]);

test bool normalData() {
	rel[str name, int arity, real tst] relDt 
		= {<createString(i % 20), i, log10(i)> | i <- [1..200]};
	return readWrite(#rel[str name, int arity, real tst], relDt);
}