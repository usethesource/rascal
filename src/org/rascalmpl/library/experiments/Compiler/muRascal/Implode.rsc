module experiments::Compiler::muRascal::Implode

import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
import Prelude;
import ParseTree;
import Ambiguity;

set[str] global_functions;

MuModule preprocess(Module pmod){
   global_functions = { "<f.name>" | f <- pmod.functions };
   return muModule(pmod.name, [], [ preprocess(f) | f <- pmod.functions], [], []);
}

MuFunction preprocess(Function f){
   vardefs =  ("<f.locals[i]>" : i  | int i <- index(f.locals));
   
   return muFunction(f.name, f.scopeId, f.nformal, size(vardefs), preprocess(f.name, f.body, vardefs));
}

list[MuExp] preprocess(str fname, list[MuExp] exps, map[str, int] vardefs){

   return
      for(exp <- exps){
        try {
          append
            top-down visit(exp){
            	// Constructs to be resolved by preprocessing
               case preIntCon(str txt)								=> muInt(toInt(txt))
               case preStrCon(str txt)								=> muCon(txt[1..-1])						// strip surrounding quotes
               case preTypeCon(str txt):                    		{
               															try {
																			Symbol sym = readTextValueString(#Symbol, txt[1..-1]);
																			insert muTypeCon(sym);
																		} catch IO(str msg) :
																			throw "Could not parse the string of a type constant into Symbol: <msg>";
               														}
               case preVar("true") 									=> muBool(true)
               case preVar("false") 								=> muBool(false)
     	       case preVar(str name) 								=> (name in global_functions)  ? muFun(name) : muLoc(name, vardefs[name])
     	       case preAssignLocList(str name1, str name2, MuExp exp1) 	
     	       														=> muCallMuPrim("assign_pair", [muInt(vardefs[name1]), muInt(vardefs[name2]), exp1])
     	       
     	       case preAssignLoc(str name, MuExp exp1) 				=> muAssignLoc(name, vardefs[name], exp1)
     	       case preList(list[MuExp] exps)						=> muCallMuPrim("make_array", exps)
     	       case preSubscript(MuExp lst, MuExp index)			=> muCallMuPrim("subscript_array_or_list_mint", [lst, index])
     	       case preAssignSubscript(MuExp lst, MuExp index, MuExp exp1) 
     	       														=> muCallMuPrim("assign_subscript_array_mint", [lst, index, exp1])
      	       case preIfthen(cond,thenPart) 						=> muIfelse(cond,thenPart, [])
      	       
      	       case preLocDeref(str name)                   		=> muLocDeref(name, vardefs[name])
      	       case preLocRef(str name)                     		=> muLocRef(name, vardefs[name])
      	       case preAssignLocDeref(str name, MuExp exp)  		=> muAssignLocDeref(name, vardefs[name], exp)
      	       
               case muCallPrim(str name, list[MuExp] exps)			=> muCallPrim(name[1..-1], exps)			// strip surrounding quotes
               case muCallMuPrim(str name, list[MuExp] exps)		=> muCallMuPrim(name[1..-1], exps)			// strip surrounding quotes
               
               // Calls that are directly mapped to muPrimitives
               
               case muCall(preVar("size"), [exp1])					=> muCallMuPrim("size_array_or_list", [exp1])
               case muCall(preVar("equal"), [exp1, exp2])			=> muCallMuPrim("equal", [exp1, exp2])
               case muCall(preVar("get_name_and_children"), [exp1])	=> muCallMuPrim("get_name_and_children", [exp1])
               case muCall(preVar("typeOf"), [exp1])				=> muCallPrim("typeOf", [exp1])
               case muCall(preVar("make_array"), [exp1])			=> muCallMuPrim("make_array_of_size", [exp1])
               case muCall(preVar("sublist"), list[MuExp] exps)		=> muCallMuPrim("sublist_list_mint_mint", exps)
               case muCall(preVar("get_tuple_elements"), [exp1])	=> muCallMuPrim("get_tuple_elements", [exp1])
               case muCall(preVar("println"), list[MuExp] exps)		=> muCallMuPrim("println", exps)
               												
               case muCall(preVar("rint"), list[MuExp] exps) 		=> muCallMuPrim("rint", exps)
               case muCall(preVar("mint"), list[MuExp] exps) 		=> muCallMuPrim("mint", exps)
               
               // Syntactic constructs that are mapped to muPrimitives
      	       case preLess(MuExp lhs, MuExp rhs)					=> muCallMuPrim("less_mint_mint", [lhs, rhs])
      	       case preLessEqual(MuExp lhs, MuExp rhs)				=> muCallMuPrim("less_equal_mint_mint", [lhs, rhs])
      	       case preEqual(MuExp lhs, MuExp rhs)					=> muCallMuPrim("equal_mint_mint", [lhs, rhs])
      	       case preNotEqual(MuExp lhs, MuExp rhs)				=> muCallMuPrim("not_equal_mint_mint", [lhs, rhs])
      	       case preGreater(MuExp lhs, MuExp rhs)				=> muCallMuPrim("greater_mint_mint", [lhs, rhs])
      	       case preGreaterEqual(MuExp lhs, MuExp rhs)			=> muCallMuPrim("greater_equal_mint_mint", [lhs, rhs])
      	       case preAddition(MuExp lhs, MuExp rhs)				=> muCallMuPrim("addition_mint_mint", [lhs, rhs])
      	       case preSubtraction(MuExp lhs, MuExp rhs)			=> muCallMuPrim("subtraction_mint_mint", [lhs, rhs])
      	       case preAnd(MuExp lhs, MuExp rhs)					=> muCallMuPrim("and_mbool_mbool", [lhs, rhs])
      	       case preIs(MuExp lhs, str typeName)					=> muCallMuPrim("is_<typeName>", [lhs])
      	       
      	       case wh: muWhile(l, c, s): {
      	         iprintln(wh);
      	       }
            };
      } catch e: throw "In muRascal function <fname> : <e>";   
    }    
}

MuModule parse(loc s) {
  pt = parse( #start[Module], s);
  dia = diagnose(pt);
  if(dia != []){
     iprintln(dia);
     throw  "*** Ambiguities in muRascal code, see above report";
  }   
  //iprintln(pt);
  ast = implode(#experiments::Compiler::muRascal::AST::Module, pt);
  //iprintln(ast);
  ast2 = preprocess(ast);
  //iprintln(ast2);
  return ast2;						   
}

MuModule parse(str s) {
  pt = parse( #start[Module], s);
  dia = diagnose(pt);
  if(dia != []){
     iprintln(dia);
     throw  "*** Ambiguities in muRascal code, see above report";
  }   
 // iprintln(pt);
  ast = implode(#experiments::Compiler::muRascal::AST::Module, pt);
  //iprintln(ast);
  ast2 = preprocess(ast);
  //iprintln(ast2);
  return ast2;							   
}
