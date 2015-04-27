module experiments::Compiler::Examples::Tst4

extend  experiments::Compiler::Tests::TestUtils;

value main(list[value] args) {
	return run("6") == 6;
}