module experiments::Compiler::Examples::Tst1

syntax As = "a"* as;


// Concrete lists

value f() = [As] "aaa";

value main() = f();