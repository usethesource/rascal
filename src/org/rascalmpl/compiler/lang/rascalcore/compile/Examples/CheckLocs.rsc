module lang::rascalcore::compile::Examples::CheckLocs

import IO;
import ValueIO;
import lang::rascalcore::compile::RVM::AST;

value main(){
   loc rvmLoc = |boot:///List.rvm.gz|;
   locs = {};
   p = readBinaryValueFile(#RVMModule, rvmLoc);
   visit(p){
    case loc l: { locs += |<l.scheme>://<l.path>|; }
   }
   return locs;
}