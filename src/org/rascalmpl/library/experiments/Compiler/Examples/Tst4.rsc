module experiments::Compiler::Examples::Tst4

import IO;
import util::Reflective;

value main(list[value] args) {
	println("inCompiledMode: <inCompiledMode()>");
	return watch(#map[str, int], ("a" : 0, "b" : 1), "Z");
}