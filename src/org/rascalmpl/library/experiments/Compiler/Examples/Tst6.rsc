module experiments::Compiler::Examples::Tst6

data F1 = f1(int N, int M = 10, bool B = false) | f1(str S);

test bool matchADTwithKeywords5() = f1(1, M=X)             := f1(1, B=false, M=20) && X == 20;