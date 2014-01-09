module experiments::Compiler::Examples::NestedFunctions1

int f() {
    int g() = 101;
    int () k() {
        int () h() = g;
        return h();
    };
    return k()();
}

value main(list[value] args) {
    return f();
}