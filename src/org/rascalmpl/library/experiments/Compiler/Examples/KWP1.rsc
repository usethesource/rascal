module experiments::Compiler::Examples::KWP1

str f(int i, str k = "empty", int j = 0) {
    return "<i>, <j>, <k>";
}

value main(list[value] args) {
    return f(0, j = 100, k = "aaa");
}