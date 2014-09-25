module experiments::Compiler::Examples::KWP9

data F = f(int i, int delta = 100);

value main(list[value] args) {
    return f(0).delta == 100;
}
