module experiments::Compiler::Examples::Tst3

import Node;

data ABCD = a(int x, int y) | b(int x, int y) | c(int x, int y) | d(int x, int y);


value visit14() {
    return visit({ [ a(1,1) ], [ b(2,2) ], [ c(3,3) ] }) {
                //case set[list[ABCD]] s => s + { [ d(5,5) ] }  // should not match
                //case list[ABCD] l => l + [ d(4,4) ]           // should match only for [ c(3,3) ]
                //case a(int x, int y) => a(x + 1000, y + 1000) // should match
                //case b(int x, int y) => b(x + 1000, y + 1000) // should not match
                case 2 => 102
            } 
            ;
}