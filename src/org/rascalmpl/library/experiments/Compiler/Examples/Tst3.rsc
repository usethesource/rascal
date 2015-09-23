module experiments::Compiler::Examples::Tst3

import List;

test bool formalsAreLocal(){
    return true;
}

value main() = formalsAreLocal();