module experiments::Compiler::Examples::OverloadingPlusVarArgs

str f(500) = "500";
str f(500,"0") = "5000";
default str f(int n, str strs...) = "<n> + <strs>";

value main(list[value] args) {
    return f(500) + "; " + f(0) + "; " + f(500,"0") + "; " + f(0,"0","0") + "; " +
           f(600) + "; " + f(600,"0");
}