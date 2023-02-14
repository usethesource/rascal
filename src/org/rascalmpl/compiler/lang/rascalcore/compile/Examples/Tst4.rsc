@bootstrapParser
module lang::rascalcore::compile::Examples::Tst4

import IO;
import Type;
import lang::csv::IO;
import ParseTree;
import DateTime;
import util::UUID;

loc targetFile = |test-temp:///csv-test-file--<"<uuidi()>">.csv|;

bool readWrite(set[&T] dt) = readWrite(type(typeOf(dt), ()), dt);
bool readWrite(type[&T] returnType, set[&T1] dt) {
    if (dt == {}) return true;
    writeCSV(returnType, dt, targetFile);

    if (dt != readCSV(returnType, targetFile)) {
        throw "Default read/write params";
    }
    writeCSV(returnType, dt, targetFile, header = false);
    if (dt != readCSV(returnType, targetFile, header = false)) {
        throw "Header = false";
    }

    writeCSV(returnType, dt, targetFile, separator = ";");
    if (dt != readCSV(returnType, targetFile, separator = ";")) {
        throw "separator = ;";
    }

    if (/\node() !:= typeOf(dt)) {
       dt = fixAmbStrings(dt);
        writeCSV(returnType, dt, targetFile);
        if (dt != readCSV(targetFile)) {
        println("expected: <returnType>");
        println(dt);
        println(readCSV(targetFile));
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

test bool csvDateTime() {
    writeFile(targetFile, "col1,col2\n2012-06-24T00:59:56+02:00,<createDateTime(2012, 6, 24, 0, 59, 56, 0, 2, 00)>");
    r = readCSV(#lrel[datetime a, datetime b], targetFile)[0];
    println(r);
    return r.a == r.b;
}
