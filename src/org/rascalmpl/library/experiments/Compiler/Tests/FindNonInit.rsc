@bootstrapParser
module experiments::Compiler::Tests::FindNonInit

import Prelude;
import util::FileSystem;
import lang::rascal::\syntax::Rascal;
import util::Reflective;

int nviolations = 0;

void main(list[value] args) {
   nviolations = 0;
   for(mfile <- find(|file:///Users/paulklint/git/rascal/src|, "rsc")){
       if(Module m := parseModule(mfile).top){
          reportNonInit(m);
       } else {
          println("<mfile>: NO MATCH");
       }
   }

   println("<nviolations> uninitialized variables");
 }
 
 void reportNonInit(Module m){
    visit(m){
       case v: (Variable) `<Name name>`: 
            {    nviolations += 1;
                 println("<name.origin>: <v>");
            }
      }
}

