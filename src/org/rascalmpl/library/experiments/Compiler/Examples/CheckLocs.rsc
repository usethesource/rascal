module experiments::Compiler::Examples::CheckLocs

import IO;
import ValueIO;
import experiments::Compiler::RVM::AST;

value main(){
   loc rvmLoc = |boot:///List.rvm.gz|;
   locs = {};
   p = readBinaryValueFile(#RVMModule, rvmLoc);
   visit(p){
    case loc l: { locs += |<l.scheme>://<l.path>|; }
   }
   return locs;
}