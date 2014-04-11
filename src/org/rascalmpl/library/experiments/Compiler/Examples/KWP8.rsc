module experiments::Compiler::Examples::KWP8

int f(int i, int delta = 100 + i) = g();

int g() = h(665);

int h(int step, int delta = 1 + step) {
    return delta; 
}

value main(list[value] args) {
    return "<f(0,delta = 999)> + <f(100)>";
}