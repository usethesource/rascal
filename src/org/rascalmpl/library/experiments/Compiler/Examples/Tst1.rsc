module experiments::Compiler::Examples::Tst1

data Point = point(int i, str color = "red");

value main(list[value] args) {
    return point(3);
}
