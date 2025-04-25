@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::compile::Examples::Tst3
import IO;
import Type;
import lang::csv::IO;
import util::Math;
import ParseTree;
import DateTime;
import util::UUID;

loc targetFile = |memory://test-tmp/csv-test-file--<"<uuidi()>">.csv|;

// bool readWrite(set[&T] dt) = readWrite(type(typeOf(dt), ()), dt);
// bool readWrite(type[&T] returnType, set[&T1] dt) {
// 	if (dt == {}) return true;
// 	writeCSV(returnType, dt, targetFile);

// 	if (dt != readCSV(returnType, targetFile)) {
// 		throw "Default read/write params";
// 	}
// 	writeCSV(returnType, dt, targetFile, header = false);
// 	if (dt != readCSV(returnType, targetFile, header = false)) {
// 		throw "Header = false";
// 	}

// 	writeCSV(returnType, dt, targetFile, separator = ";");
// 	if (dt != readCSV(returnType, targetFile, separator = ";")) {
// 		throw "separator = ;";
// 	}

// 	if (/\node() !:= typeOf(dt)) {
// 	   dt = fixAmbStrings(dt);
// 		writeCSV(returnType, dt, targetFile);
// 		if (dt != readCSV(targetFile)) {
// 		println("expected: <returnType>");
// 		println(dt);
// 		println(readCSV(targetFile));
// 			throw "inferred types";	
// 		}
// 	}
// 	return true;
// }

// &T fixAmbStrings(&T v) {
// 	return visit(v) {
// 		case str s => "a" + s
// 			when /^[ \t\n]*[0-9]+[ \t\n]*$/ := s
// 		case str s => "a" + s
// 			when /^[ \t\n]*[0-9]+[ \t\n]*\.[0-9]*[ \t\n]*$/ := s
// 		case str s => "a" + s
// 			when /^[ \t\n]*[0-9][ \t\n]*+r[ \t\n]*[0-9]*[ \t\n]*$/ := s
// 		case str s => "a" + s
// 			when /^[ \t\n]*\<[ \t\<\>]*\>[ \t\n]*$/ := s
// 		case str s => "a" + s
// 			when /^[ \t\n]*\<([ \t]|[^\>,])*\>[ \t\n]*$/ := s
// 		case str s => "a" + s + "a"
// 			when /^[ \t\n]*[{\(\[][ \t\n]*[\)}\]][ \t\n]*$/ := s
// 	};
// }

// test bool csvBooleanInfer() {
//     writeFile(targetFile, "col1,col2\nTRUE,True");
//     return readCSV(targetFile) == {<true, true>};
// }

// test bool csvBoolean() {
//     writeFile(targetFile, "col1,col2\nTRUE,True");
//     return readCSV(#rel[bool col1, bool col2], targetFile) == {<true, true>};
// }

// test bool csvDateTime() {
//     writeFile(targetFile, "col1,col2\n2012-06-24T00:59:56+02:00,<createDateTime(2012, 6, 24, 0, 59, 56, 0, 2, 00)>");
//     r = readCSV(#lrel[datetime a, datetime b], targetFile)[0];
// 	println(r);
//     return r.a == r.b;
// }

// test bool csvWithLoc(rel[loc first, int second] dt) = readWrite(dt);
// test bool csvWithStr(rel[str first, int second] dt) = readWrite(dt);
// test bool csvWithList(rel[list[&T] first, int second] dt) = readWrite(dt);
// test bool csvWithSet(rel[set[&T] first, int second] dt) = readWrite(dt);
// test bool csvWithMap(rel[map[&T, &Y] first, int second] dt) = readWrite(dt);
// test bool csvWithNode(rel[node first, int second] dt) = readWrite(dt);

// // this can not work, when reading back we do not have enough information
// // to specialize int and loc back to the original values.
// //test bool csvRandom(rel[value x, value y] dt) = readWrite(dt);

// test bool csvMoreTuples(rel[str a, str b, int c, bool d, real e] dt) = readWrite(dt);

// // this can not work, when reading back we do not have enough information
// // to specialize int and loc back to the original values.
// // test bool csvMoreRandomTypes(rel[&T1 a, &T2 b, int c, str d, &T3 e] dt) = readWrite(dt);
//  test bool csvMoreRandomTypes(rel[loc a, loc b, int c, str d, loc e] dt) = readWrite(dt);

bool checkType(type[value] expected, str input) {
    writeFile(targetFile, input);
    rtype = getCSVType(targetFile);
    println("expected.symbol: "); iprintln(expected.symbol);
    println("rtype.symbol: "); iprintln(rtype.symbol);
    println("expected.symbol == rtype.symbol: <expected.symbol == rtype.symbol>");
    return expected == rtype;
}

value main() //test bool csvTypeInference1() 
    = checkType(#rel[str,int], 
    "col1,col2
    'a,2
    '");