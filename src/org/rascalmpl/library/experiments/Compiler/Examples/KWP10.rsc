module experiments::Compiler::Examples::KWP10

data F = ff(int i, int delta = 100);

value main(list[value] args) {
    return ff(0,delta=1).delta == 1;
}