module experiments::Compiler::Examples::Tst2a

extend experiments::Compiler::Examples::Tst2;

import ValueIO;

value main(list[value] args) {
	dval = d(13, ("a": 1, "b" : 2), "xyz");
	writeBinaryValueFile(|home:///tmp|, dval);
	dval1 = readBinaryValueFile(#DATA, |home:///tmp|);
	return dval == dval1;
 }