module experiments::Compiler::Examples::AnotherFor

value main(list[value] args) {
    res1 = for(2 > 1) append 0;
    res1 = res1 + [ 1 | 2 > 1 ];
    res2 = for(2 < 1) append 2;
    res2 = res2 + [ 3 | 2 < 1 ];
    return res1 + res2;
}