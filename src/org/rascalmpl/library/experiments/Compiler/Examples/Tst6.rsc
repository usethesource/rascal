module experiments::Compiler::Examples::Tst6

import ParseTree;
import lang::rascal::\syntax::Rascal;

lang::rascal::\syntax::Rascal::Declaration getMain(lang::rascal::\syntax::Rascal::Module m){
    if(appl(regular(Symbol def), list[Tree] args) := m.body.toplevels){
       if(Toplevel tl := args[-1]){
          return tl.declaration;
       }
    }
    throw "Cannot match toplevels";
}