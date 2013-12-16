module experiments::Compiler::Examples::KWP4

int f(int i, int delta = 0) = g();

int g() = h();

int h(int delta = 1) {
    return delta; 
}

value main(list[value] args) {
    return f(0,delta = 5);
}

public value expectedResult = 5;