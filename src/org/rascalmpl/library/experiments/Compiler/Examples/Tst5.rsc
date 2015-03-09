module experiments::Compiler::Examples::Tst5

import IO;

tuple[int, str] f() = <13, "abc">;

value main(list[value] args) {
	<i, s> = f();
	println("i = <i>");
	println("s = <s>");
	return true;
}