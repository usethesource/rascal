module lang::rascalcore::compile::Examples::Tst1

import lang::rascal::\syntax::Rascal;
import IO;
//import ParseTree;

Type b = (Type) `bool`;
  
value main() {
    iprintln(b);
    return b@\loc;
}
