module experiments::Compiler::Examples::DescentLiteral

value main(list[value] args) {
    n = 0;
    for(/1 := [1,2,3,[1,2,3,[1,2,3]]]) {
        n += 1;
    }
    return n;
}