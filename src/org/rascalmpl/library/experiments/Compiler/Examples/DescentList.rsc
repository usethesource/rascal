module experiments::Compiler::Examples::DescentList

value main(list[value] args) {
    n = 0;
    for(/v:[*value x] := [ 1, [2], [3,[4,6,[7]]], [[8,[9]],[[[10]]]] ]) {
        n += 1;
    }
    return n;
}