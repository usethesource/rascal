module experiments::Compiler::Examples::NestedFunctions3

str f(0) { res = g("0"); return "f(0); " + res; }
str f(1) { 
    str res; 
    try { 
        res = g("1"); 
    } catch str s: { 
        res = "catched(<s>); g(\"1\")"; 
    }; 
    fail; 
    return "f(1); " + res; 
}
default str f(int n) { 
    str res; 
    try {
        res = g("<n>");
    } catch str s: {
        res = "catched(<s>); g(<n>)";
    }
    return "default f(<n>); " + res; 
}

str g("0") = "g(\"0\")";
str g("1") { throw "Try to catch me!!!"; return "g(\"1\")"; }
default str g(str s) = "default g(<s>)";

value main(list[value] args) {
    return f(0) + " ;; " + f(1) + " ;; " + f(5);
}