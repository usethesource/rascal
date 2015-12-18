@bootstrapParser
module experiments::Compiler::Examples::Tst6

import ParseTree;
import lang::rascal::\syntax::Rascal;

int cntAlt(Prod p){
    switch(p){
      case (Prod) `<Prod lhs> | <Prod rhs>`: return cntAlt(lhs) + cntAlt(rhs);
    }
   return 1;
}