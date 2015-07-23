@bootstrapParser
module experiments::Compiler::Examples::Tst5

import lang::rascal::\syntax::Rascal;
import List;
import IO;

int processRegExpLiteral(e: (RegExpLiteral) `/<RegExp* rexps>/<RegExpModifier modifier>`){
   iprintln(rexps);

   lrexps = [r | r <- rexps];
   
   println("lrexps = <lrexps>");
   
   return size(lrexps);
   
}

value main(list[value] args) = processRegExpLiteral((RegExpLiteral) `/abc/`);