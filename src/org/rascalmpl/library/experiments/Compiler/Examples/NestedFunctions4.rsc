module experiments::Compiler::Examples::NestedFunctions4

int () g() {
    int k() = 666;
    int h() = k();
    return h;
}

value main(list[value] args) {
    return g()();
}