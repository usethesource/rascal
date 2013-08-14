module experiments::CoreRascal::muRascal::Implode

import experiments::CoreRascal::muRascal::Syntax;
import experiments::CoreRascal::muRascal::AST;
import Prelude;
import ParseTree;
import Ambiguity;

MuModule preprocess(Module pmod){
   return muModule(pmod.name, [], [ preprocess(f) | f <- pmod.functions], [], []);
}

MuFunction preprocess(Function f){
   vardefs =  ("<def.name>" : toInt("<def.pos>") |  def <- f.names);
   return muFunction(f.name, f.scope, f.nformal, size(vardefs), preprocess(f.body, vardefs));
}

list[MuExp] preprocess(list[MuExp] exps, map[str, int] vardefs){

   return
      for(exp <- exps){
          append
            visit(exp){
     	       case preVar(str name) => muLoc(name , vardefs[name])
     	       case preAssignLoc(str name, MuExp exp) => muAssignLoc(name, vardefs[name], exp)
     	       case preIfthen(exp,thenPart) => muIfElse(cond,thenPart, [])
            };
      };      
}

MuModule parse(loc s) {
  pt = parse( #start[Module], s);
  println(diagnose(pt));
  return preprocess(implode(#experiments::CoreRascal::muRascal::AST::Module, pt));
								   
}