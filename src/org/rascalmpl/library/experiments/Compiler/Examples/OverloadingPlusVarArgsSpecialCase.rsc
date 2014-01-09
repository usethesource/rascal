module experiments::Compiler::Examples::OverloadingPlusVarArgsSpecialCase

list[str] f(str strs...) = strs;

value main(list[value] args) {
    return f(["0","0"]) + f("1","1");
}