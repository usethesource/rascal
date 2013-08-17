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
            top-down visit(exp){
               case muCallPrim(str name, list[MuExp] exps)	=> muCallPrim(name[1..-1], exps)
               case muCallPrim(str name, exp1)				=> muCallPrim(name[1..-1], exp1)
               case muCallPrim(str name, exp1, exp2)		=> muCallPrim(name[1..-1], exp1, exp2)
               case preIntCon(str txt)						=> muCon(toInt(txt))
               case preStrCon(str txt)						=> muCon(txt[1..-1])	// strip surrounding quotes
               case preTypeCon(str txt):                    {
               													try {
																	Symbol sym = readTextValueString(#Symbol, txt);
																	insert muTypeCon(sym);
																} catch IO(str msg) :
																	throw "Could not parse the string of a type constant into Symbol: <msg>";
               												}
               case preVar("true") 							=> muCon(true)
               case preVar("false") 						=> muCon(false)
     	       case preVar(str name) 						=> muLoc(name , vardefs[name])
     	       case preAssignLocPair(prePair(preVar(name1), preVar(name2)), MuExp exp1) 	
     	       												=> muCallPrim("assign_pair", [muCon(vardefs[name1]), muCon(vardefs[name2]), exp1])
     	       
     	       case preAssignLoc(str name, MuExp exp1) 		=> muAssignLoc(name, vardefs[name], exp1)
     	       case prePair(exp1, exp2)						=> muCallPrim("make_tuple", [exp1, exp2, muCon(2)])
     	       case preList(list[MuExp] exps)				=> muCallPrim("make_list", [*exps, muCon(size(exps))])
      	       case preIfthen(cond,thenPart) 				=> muIfelse(cond,thenPart, [])
            };
      };      
}

MuModule parse(loc s) {
  pt = parse( #start[Module], s);
  iprintln(diagnose(pt));
  iprintln(pt);
  ast = implode(#experiments::Compiler::muRascal::AST::Module, pt);
  iprintln(ast);
  return preprocess(ast);
								   
}

MuModule parse(str s) {
  pt = parse( #start[Module], s);
  //iprintln(diagnose(pt));
 // iprintln(pt);
  ast = implode(#experiments::Compiler::muRascal::AST::Module, pt);
  iprintln(ast);
  ast2 = preprocess(ast);
  iprintln(ast2);
  return ast2;
								   
}