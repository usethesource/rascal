module experiments::Compiler::Examples::Tst1

data X(int left = 10, int leftsq = left * left) = ly(int leftcb = leftsq * left);

value main() = ly();