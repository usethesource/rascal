module experiments::Compiler::muRascal::Implode

import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
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
               case preVar("true") 						=> muCon(true)
               case preVar("false") 					=> muCon(false)
     	       case preVar(str name) 					=> muLoc(name , vardefs[name])
     	       case preAssignLoc(str name, MuExp exp) 	=> muAssignLoc(name, vardefs[name], exp)
     	       case prePair(exp1, exp2)					=> muCallPrim("make_tuple", 2, [exp1, exp2])
     	       case preAssignLocPair(str name1, str name2, MuExp exp) 	
     	       											=> muCallPrim("assign_pair", [muCon(vardefs[name1]), muCon(vardefs[name2]), exp])
     	       case preIfthen(cond,thenPart) 			=> muIfelse(cond,thenPart, [])
            };
      };      
}

MuModule parse(loc s) {
  pt = parse( #start[Module], s);
  iprintln(diagnose(pt));
  return preprocess(implode(#experiments::Compiler::muRascal::AST::Module, pt));
								   
}