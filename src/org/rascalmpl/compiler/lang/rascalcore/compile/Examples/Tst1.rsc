module lang::rascalcore::compile::Examples::Tst1

value main() { //test bool testWhileWithNoAppend() {
    return  {x = 3; while (x > 0) {x -= 1; }} == [];
}