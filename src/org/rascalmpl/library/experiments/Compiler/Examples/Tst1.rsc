module experiments::Compiler::Examples::Tst1

import IO;
import Type;
import lang::csv::IO;
import util::Math;
//import ParseTree;
import DateTime;

loc targetFile = |home:///csv-test-file.csv|;

test bool csvBooleanInfer() {
    writeFile(targetFile, "col1,col2\nTRUE,True");
    return readCSV(targetFile) == {<true, true>};
}

test bool csvBoolean() {
    writeFile(targetFile, "col1,col2\nTRUE,True");
    return readCSV(#rel[bool col1, bool col2], targetFile) == {<true, true>};
}

test bool csvDateTime() {
    writeFile(targetFile, "col1,col2\n2012-06-24T00:59:56Z,<createDateTime(2012, 6, 24, 0, 59, 56, 0)>");
    r = readCSV(#lrel[datetime a, datetime b], targetFile)[0];
    return r.a == r.b;
}