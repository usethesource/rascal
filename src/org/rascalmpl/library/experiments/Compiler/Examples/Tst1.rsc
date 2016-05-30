module experiments::Compiler::Examples::Tst1

import ParseTree;

test bool main(){
    Tree pt = parse(#C, "axaaa");
    rprintln(pt);
    return c(A a, As as) := pt;
}