module experiments::Compiler::muRascal::Implode

import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
import Prelude;
import ParseTree;
import Ambiguity;

set[str] global_functions;

MuModule preprocess(Module pmod){
   global_functions = { "<f.name>" | f <- pmod.functions };
   println(global_functions);
   return muModule(pmod.name, [], [ preprocess(f) | f <- pmod.functions], [], []);
}

MuFunction preprocess(Function f){
   vardefs =  ("<f.locals[i]>" : i  | int i <- index(f.locals));
   
   return muFunction(f.name, f.scopeId, f.nformal, size(vardefs), preprocess(f.body, vardefs));
}

list[MuExp] preprocess(list[MuExp] exps, map[str, int] vardefs){

   return
      for(exp <- exps){
          append
            top-down visit(exp){
               case muCallPrim(str name, list[MuExp] exps)	=> muCallPrim(name[1..-1], exps)			// strip surrounding quotes
               case muCallPrim(str name, exp1)				=> muCallPrim(name[1..-1], [exp1])			// strip surrounding quotes
               case muCallPrim(str name, exp1, exp2)		=> muCallPrim(name[1..-1], [exp1, exp2])	// strip surrounding quotes
               case preIntCon(str txt)						=> muCon(toInt(txt))
               case preStrCon(str txt)						=> muCon(txt[1..-1])						// strip surrounding quotes
               case preTypeCon(str txt):                    {
               													try {
																	Symbol sym = readTextValueString(#Symbol, txt[1..-1]);
																	insert muTypeCon(sym);
																} catch IO(str msg) :
																	throw "Could not parse the string of a type constant into Symbol: <msg>";
               												}
               case preVar("true") 							=> muCon(true)
               case preVar("false") 						=> muCon(false)
     	       case preVar(str name) 						=> (name in global_functions)  ? muFun(name) : muLoc(name, vardefs[name])
     	       case preAssignLocList(str name1, str name2, MuExp exp1) 	
     	       												=> muCallPrim("$assign_pair", [muCon(vardefs[name1]), muCon(vardefs[name2]), exp1])
     	       
     	       case preAssignLoc(str name, MuExp exp1) 		=> muAssignLoc(name, vardefs[name], exp1)
     	       case preList(list[MuExp] exps)				=> muCallPrim("$make_array", exps)
     	       case preSubscript(MuExp lst, MuExp index)	=> muCallPrim("$subscript_array_int", [lst, index])
     	       case preAssignSubscript(MuExp lst, MuExp index, MuExp exp1) 
     	       												=> muCallPrim("$assign_subscript_array_int", [lst, index, exp1])
      	       case preIfthen(cond,thenPart) 				=> muIfelse(cond,thenPart, [])
      	       
      	       case preLocDeref(str name)                   => muLocDeref(name, vardefs[name])
      	       case preLocRef(str name)                     => muLocRef(name, vardefs[name])
      	       case preAssignLocDeref(str name, MuExp exp)  => muAssignLocDeref(name, vardefs[name], exp)
            };
      };      
}

MuModule parse(loc s) {
  pt = parse( #start[Module], s);
  iprintln(diagnose(pt));
  //iprintln(pt);
  ast = implode(#experiments::Compiler::muRascal::AST::Module, pt);
  iprintln(ast);
  ast2 = preprocess(ast);
  iprintln(ast2);
  return ast2;
								   
}

MuModule parse(str s) {
  pt = parse( #start[Module], s);
  iprintln(diagnose(pt));
 // iprintln(pt);
  ast = implode(#experiments::Compiler::muRascal::AST::Module, pt);
  iprintln(ast);
  ast2 = preprocess(ast);
  iprintln(ast2);
  return ast2;
								   
}
