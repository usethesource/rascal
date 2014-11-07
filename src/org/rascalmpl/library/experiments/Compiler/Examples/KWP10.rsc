module experiments::Compiler::Examples::KWP10

data F = f(int i, int delta = 100);

value main(list[value] args) {
    return f(0,delta=1).delta == 1;
}