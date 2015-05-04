module experiments::Compiler::Examples::Tst4

extend  experiments::Compiler::Tests::TestUtils;

test bool tst1() = 13 == 12 + 1;

value main(list[value] args) {
	return run("{ x |x \<- [1 .. 10], x % 2 == 1}");
}