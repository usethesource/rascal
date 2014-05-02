module experiments::Compiler::Examples::KWP7

str f(int i, int j, str k = "<i>, <j>") = k;

value main(list[value] args) {
    return f(1,2) + " ; " + f(3,4) + " ; " + f(1,3, k = "1 + 3");
}