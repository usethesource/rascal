module experiments::Compiler::Examples::NestedFunctions2

str f(0) { res = g("0"); return "f(0); " + res; }
str f(1) { res = g("1"); fail; return "f(1); " + res; }
default str f(int n) { res = g("<n>"); return "default f(1);" + res; }

str g("0") = "g(\"0\")";
str g("1") = "g(\"1\")";
default str g(str s) = "default g(<s>);";

value main(list[value] args) {
    return f(0) + ";; " + f(1) + ";; " + f(5);
}