module experiments::Compiler::Examples::OverloadingDynamicCall

int f(0) = -1;
default int f(int i) = 100 + i;

str f("0") = "- 1";
default str f(str s) = "100 + <s>";

value main(list[value] args) {
	x = f;
	y = f("arg");
	return y;
}
