module experiments::Compiler::muRascal::Implode

import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
import Prelude;
import ParseTree;
import Ambiguity;

rel[str,str] global_functions = {};
map[str,map[str,int]] vardefs = ();

MuModule preprocess(Module pmod){
   global_functions = {};
   vardefs = ();
   global_functions = { <f.name, getUID(pmod.name,f.funNames,f.name,f.nformals)> | f <- pmod.functions };
   println(global_functions);
   for(f <- pmod.functions) {
       uid = getUID(pmod.name,f.funNames,f.name,f.nformals);
       vdfs = ("<f.locals[i]>" : i  | int i <- index(f.locals));
       vardefs =  vardefs + (uid : vdfs);
   }
   map[str,Symbol] types = ();
   for(t <- pmod.types) {
       try {
           Symbol sym = readTextValueString(#Symbol, replaceAll((t.\type)[1..-1],"\\",""));
           if(sym has name) {
               types[sym.name] = sym;
           }
       } catch IO(str msg) : {
           throw "Could not parse the string of a type constant into Symbol: <msg>";
       }
   }
   resolver = ();
   overloaded_functions = [];
   return muModule(pmod.name, [], types, [ preprocess(f, pmod.name) | f <- pmod.functions ], [], [], resolver, overloaded_functions, ());
}

bool isGlobalNonOverloadedFunction(str name) {
	if(isEmpty(global_functions[name])) {
		return false;
	}
	else {
		if(size(global_functions[name]) > 1) {
			throw "The function <name> is overloaded function! Please, disambiguate a function with its uid (uid) and use ++ operator.";
		}
		return true;
	} 
}

str getUidOfGlobalNonOverloadedFunction(str name) {
	if(isGlobalNonOverloadedFunction(name)) {
		return getOneFrom(global_functions[name]);
	}
	throw "The function <name> does not exist!";
}

@doc{Generates a unique scope id: non-empty 'funNames' list implies a nested function}
str getUID(str modName, lrel[str,int] funNames, str funName, int nformals) {
	// Due to the current semantics of the implode
	modName = replaceAll(modName, "::", "");
	return "<modName>/<for(<f,n> <- funNames){><f>(<n>)/<}><funName>(<nformals>)"; 
}
str getUID(str modName, [ *tuple[str,int] funNames, <str funName, int nformals> ]) 
	= "<modName>/<for(<f,n> <- funNames){><f>(<n>)/<}><funName>(<nformals>)";

MuFunction preprocess(Function f, str modName){
   uid = getUID(modName,f.funNames,f.name,f.nformals);
   
   // Guard specific check
   insertGuard = false;
   if(f is preCoroutine) {
       guards = [];
       visit(f.body) { case e:muGuard(_): guards += e; }
       if(size(guards) > 1) {
           throw "More than one guard expression has been found in <uid>!";
       }
       if(size(guards) == 1 && guards[0] notin f.body) {
           throw "Guard expression has to be at top-level within the body of a coroutine: <uid>!";
       }
       if(size(guards) == 1) {
           for(MuExp e <- f.body) {
               if(e in guards) {
                   break;
               }
               if(!isEmpty([ exp | /MuExp exp := e, exp is muReturn || exp is muYield ])) {
                   throw "Yield or return has been found before a guard expression: <uid>";
               }
           }
       } else {
           insertGuard = true;
       }
   }
   
   scopeIn = (!isEmpty(f.funNames)) ? getUID(modName,f.funNames) : ""; // if not a function scope, then the root one
   // Generate a very generic function type
   ftype = Symbol::func(Symbol::\value(),[ Symbol::\value() | i <- [0..f.nformals + 1] ]);
   
   body = preprocess(modName, f.funNames, f.name, f.nformals, uid, f.body);
   return (f is preCoroutine) ? muCoroutine(uid, scopeIn, f.nformals, size(vardefs[uid]), muBlock(insertGuard ? [ muGuard(muBool(true)), *body, muExhaust() ] : [ *body, muExhaust() ]))
                              : muFunction(uid, ftype, scopeIn, f.nformals, size(vardefs[uid]), |rascal:///|, [], (), muBlock(body));
}

list[MuExp] preprocess(str modName, lrel[str,int] funNames, str fname, int nformals, str uid, list[MuExp] exps){
   println("Pre-processing a function: <uid>");
   return
      for(exp <- exps){
        try {
          append
            top-down visit(exp){
            	// Constructs to be resolved by preprocessing
               case preIntCon(str txt)												=> muInt(toInt(txt))
               case preStrCon(str txt)												=> muCon(txt[1..-1])						// strip surrounding quotes
               case preTypeCon(str txt):                    						{
               																			try {
																							Symbol sym = readTextValueString(#Symbol, txt[1..-1]);
																							insert muTypeCon(sym);
																						} catch IO(str msg) :
																						throw "Could not parse the string of a type constant into Symbol: <msg>";
               																		}
               case preVar("true") 													=> muBool(true)
               case preVar("false") 												=> muBool(false)
     	       case preVar(str name) 												=> (isGlobalNonOverloadedFunction(name)) ? muFun(getUidOfGlobalNonOverloadedFunction(name)) : muLoc(name, vardefs[uid][name])
     	       case preVar(lrel[str,int] funNames, str name)        				=> muVar(name,getUID(modName,funNames),vardefs[getUID(modName,funNames)][name])
     	       case preAssignLocList(str name1, str name2, MuExp exp1) 	
     	       																		=> muCallMuPrim("assign_pair", [muInt(vardefs[uid][name1]), muInt(vardefs[uid][name2]), exp1])
     	       
     	       case preAssignLoc(str name, MuExp exp) 								=> muAssignLoc(name, vardefs[uid][name], exp)
     	       case preAssign(lrel[str,int] funNames, 
     	       				  str name, MuExp exp)                  				=> muAssign(name,getUID(modName,funNames),vardefs[getUID(modName,funNames)][name],exp)
     	       case preList(list[MuExp] exps)										=> muCallMuPrim("make_array", exps)
     	       case preSubscriptArray(MuExp ar, MuExp index)						=> muCallMuPrim("subscript_array_mint", [ar, index])
     	       case preSubscriptList(MuExp lst, MuExp index)						=> muCallMuPrim("subscript_list_mint", [lst, index])
     	       case preSubscriptTuple(MuExp tup, MuExp index)						=> muCallMuPrim("subscript_tuple_mint", [tup, index])
     	       
     	       case preAssignSubscriptArray(MuExp ar, MuExp index, MuExp exp1) 		=> muCallMuPrim("assign_subscript_array_mint", [ar, index, exp1])
     	       case preAssignSubscriptList(MuExp lst, MuExp index, MuExp exp1) 		=> muCallMuPrim("assign_subscript_list_mint", [lst, index, exp1])
     	        
      	       case preIfthen(cond,thenPart) 										=> muIfelse("", cond, thenPart, [])
      	       
      	       case preLocDeref(str name)                   						=> muLocDeref(name, vardefs[uid][name])
      	       case preVarDeref(lrel[str,int] funNames, str name)   				=> muVarDeref(name,getUID(modName,funNames),vardefs[getUID(modName,funNames)][name])
      	       case preLocRef(str name)                     						=> muLocRef(name, vardefs[uid][name])
      	       case preVarRef(lrel[str,int] funNames, str name)     				=> muVarRef(name,getUID(modName,funNames),vardefs[getUID(modName,funNames)][name])
      	       case preAssignLocDeref(str name, MuExp exp)  						=> muAssignLocDeref(name, vardefs[uid][name], exp)
      	       case preAssignVarDeref(lrel[str,int] funNames,
      	       						  str name, muExp exp)          				=> muAssignVarDeref(name,getUID(modName,funNames),vardefs[getUID(modName,funNames)][name],exp)
      	       
      	       case muCallPrim(str name)                                            => muCallPrim(name[1..-1], [])
               case muCallPrim(str name, list[MuExp] exps)							=> muCallPrim(name[1..-1], exps)			// strip surrounding quotes
               case muCallMuPrim(str name, list[MuExp] exps)						=> muCallMuPrim(name[1..-1], exps)			// strip surrounding quotes
               
               // Calls that are directly mapped to muPrimitives
               
               case muCall(preVar("size_array"), [exp1])							=> muCallMuPrim("size_array", [exp1])
               case muCall(preVar("size_list"), [exp1])								=> muCallMuPrim("size_list", [exp1])
               case muCall(preVar("size_set"), [exp1])								=> muCallMuPrim("size_set", [exp1])
               case muCall(preVar("size_mset"), [exp1])								=> muCallMuPrim("size_mset", [exp1])
               case muCall(preVar("size_map"), [exp1])								=> muCallMuPrim("size_map", [exp1])
               case muCall(preVar("size_tuple"), [exp1])							=> muCallMuPrim("size_tuple", [exp1])
               
               case muCall(preVar("size"),[exp1])                                   => muCallMuPrim("size",[exp1])
               
               case muCall(preVar("is_defined"), [exp1])							=> muCallMuPrim("is_defined", [exp1])
               case muCall(preVar("is_element"), [exp1, exp2])						=> muCallMuPrim("is_element", [exp1, exp2])
               case muCall(preVar("keys"), [exp1])									=> muCallMuPrim("keys_map", [exp1])
               case muCall(preVar("values"), [exp1])								=> muCallMuPrim("values_map", [exp1])
               case muCall(preVar("set2list"), [exp1])								=> muCallMuPrim("set2list", [exp1])
               case muCall(preVar("mset2list"), [exp1])								=> muCallMuPrim("mset2list", [exp1])
               case muCall(preVar("equal"), [exp1, exp2])							=> muCallMuPrim("equal", [exp1, exp2])
               case muCall(preVar("get_name_and_children"), [exp1])					=> muCallMuPrim("get_name_and_children", [exp1])
               case muCall(preVar("typeOf"), [exp1])								=> muCallPrim("typeOf", [exp1])
               case muCall(preVar("subtype"), [exp1, exp2])         				=> muCallPrim("subtype", [exp1, exp2])
               case muCall(preVar("make_iarray"), [exp1])							=> muCallMuPrim("make_iarray_of_size", [exp1])
               case muCall(preVar("make_array"), [exp1])							=> muCallMuPrim("make_array_of_size", [exp1])
               case muCall(preVar("starts_with"), [exp1, exp2, exp3])				=> muCallMuPrim("starts_with", [exp1, exp2, exp3])
               case muCall(preVar("sublist"), list[MuExp] exps)						=> muCallMuPrim("sublist_list_mint_mint", exps)
               case muCall(preVar("subset"), list[MuExp] exps)						=> muCallPrim("set_lessequal_set", exps)
              //case muCall(preVar("mset_copy"), list[MuExp] exps) 					=> muCallMuPrim("mset_copy", exps)
               case muCall(preVar("mset_destructive_subtract_mset"), list[MuExp] exps)	=> muCallMuPrim("mset_destructive_subtract_mset", exps)
               case muCall(preVar("mset_destructive_add_mset"), list[MuExp] exps)		=> muCallMuPrim("mset_destructive_add_mset", exps)
               case muCall(preVar("mset_destructive_add_elm"), list[MuExp] exps)		=> muCallMuPrim("mset_destructive_add_elm", exps)
               case muCall(preVar("mset_destructive_subtract_elm"), list[MuExp] exps)	=> muCallMuPrim("mset_destructive_subtract_elm", exps)
               case muCall(preVar("mset_destructive_subtract_set"), list[MuExp] exps)	=> muCallMuPrim("mset_destructive_subtract_set", exps)
               //case muCall(preVar("set_subtract_elm"), list[MuExp] exps)			=> muCallPrim("set_subtract_elm", exps)			
               case muCall(preVar("mset"), list[MuExp] exps) 						=> muCallMuPrim("mset", exps)
               case muCall(preVar("set"), list[MuExp] exps) 						=> muCallMuPrim("set", exps)
             
               case muCall(preVar("make_mset"), list[MuExp] exps)					=> muCallMuPrim("make_mset", exps)
               case muCall(preVar("get_tuple_elements"), [exp1])					=> muCallMuPrim("get_tuple_elements", [exp1])
               case muCall(preVar("println"), list[MuExp] exps)						=> muCallMuPrim("println", exps)
               												
               case muCall(preVar("rint"), list[MuExp] exps) 						=> muCallMuPrim("rint", exps)
               case muCall(preVar("mint"), list[MuExp] exps) 						=> muCallMuPrim("mint", exps)
               
               // Syntactic constructs that are mapped to muPrimitives
      	       case preLess(MuExp lhs, MuExp rhs)									=> muCallMuPrim("less_mint_mint", [lhs, rhs])
      	       case preLessEqual(MuExp lhs, MuExp rhs)								=> muCallMuPrim("less_equal_mint_mint", [lhs, rhs])
      	       case preEqual(MuExp lhs, MuExp rhs)									=> muCallMuPrim("equal_mint_mint", [lhs, rhs])
      	       case preNotEqual(MuExp lhs, MuExp rhs)								=> muCallMuPrim("not_equal_mint_mint", [lhs, rhs])
      	       case preGreater(MuExp lhs, MuExp rhs)								=> muCallMuPrim("greater_mint_mint", [lhs, rhs])
      	       case preGreaterEqual(MuExp lhs, MuExp rhs)							=> muCallMuPrim("greater_equal_mint_mint", [lhs, rhs])
      	       case preAddition(MuExp lhs, MuExp rhs)								=> muCallMuPrim("addition_mint_mint", [lhs, rhs])
      	       case preSubtraction(MuExp lhs, MuExp rhs)							=> muCallMuPrim("subtraction_mint_mint", [lhs, rhs])
      	       case preDivision(MuExp lhs, MuExp rhs)								=> muCallMuPrim("division_mint_mint", [lhs, rhs])
      	       case preModulo(MuExp lhs, MuExp rhs)									=> muCallMuPrim("modulo_mint_mint", [lhs, rhs])
      	       case prePower(MuExp lhs, MuExp rhs)									=> muCallMuPrim("power_mint_mint", [lhs, rhs])
      	       case preAnd(MuExp lhs, MuExp rhs)									=> muCallMuPrim("and_mbool_mbool", [lhs, rhs])
      	       case preOr(MuExp lhs, MuExp rhs)									    => muCallMuPrim("or_mbool_mbool", [lhs, rhs])
      	       case preIs(MuExp lhs, str typeName)									=> muCallMuPrim("is_<typeName>", [lhs])
      	       
      	       // Overloading
      	       case preFunNN(str modName,  str name, int nformals)                  => muFun(getUID(modName,[],name,nformals))
      	       case preFunN(lrel[str,int] funNames,  str name, int nformals)        => muFun(getUID(modName,funNames,name,nformals), getUID(modName,funNames))
      	       
            };
      } catch e: throw "In muRascal function <modName>::<for(<f,n> <- funNames){><f>::<n>::<}><fname>::<nformals> (uid = <uid>) : <e>";   
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
