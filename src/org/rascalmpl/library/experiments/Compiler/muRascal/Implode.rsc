module experiments::Compiler::muRascal::Implode

import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
import Prelude;
import ParseTree;
import Ambiguity;

import experiments::Compiler::muRascal::MuAllMuOr;

rel[str,str] global_functions = {};
map[str,map[str,int]] vardefs = ();
list[MuFunction] functions_in_module = [];

int nLabel = 0;

str nextLabel(str prefix) {
  nLabel += 1;
  return "<prefix><nLabel>";
}

MuModule preprocess(Module pmod){
   global_functions = {};
   vardefs = ();
   functions_in_module = [];
   global_functions = { <f.name, getUID(pmod.name,f.funNames,f.name,size(f.formals))> | f <- pmod.functions };
   println(global_functions);
   for(f <- pmod.functions) {
       uid = getUID(pmod.name,f.funNames,f.name,size(f.formals));
       /*
        * Variable declarations may appear in:
        *     (1) function/coroutine signatures (formals) 
        *     (2) guard expressions
        *     (3) function/coroutine bodies 
        */
       list[Identifier] locals = f.formals;
       if(f is preCoroutine) {
           locals = locals + ( isEmpty(f.guard) ? [] : (f.guard[0] has locals ? [ vdecl.id | VarDecl vdecl <- f.guard[0].locals ] : []) );
       }
       locals = locals + ( isEmpty(f.locals) ? [] : [ vdecl.id | VarDecl vdecl <- f.locals[0][0] ] );
       assert size(locals) == size({ *locals });
       
       vdfs = ("<locals[i].var>" : i  | int i <- index(locals));
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
   return muModule(pmod.name, [], types, [ preprocess(f, pmod.name) | f <- pmod.functions ] + functions_in_module, [], [], resolver, overloaded_functions, ());
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
/*
 * NOTE: Given that the muRascal language does not support overloading, the dependency of function uids 
 *       on the number of formal parameters has been removed 
 */
str getUID(str modName, lrel[str,int] funNames, str funName, int nformals) {
	// Due to the current semantics of the implode
	modName = replaceAll(modName, "::", "");
	return "<modName>/<for(<f,n> <- funNames){><f>(<n>)/<}><funName>"; 
}
str getUID(str modName, [ *tuple[str,int] funNames, <str funName, int nformals> ]) 
	= "<modName>/<for(<f,n> <- funNames){><f>(<n>)/<}><funName>";

MuFunction preprocess(Function f, str modName) {
   uid = getUID(modName,f.funNames,f.name,size(f.formals));
   
   // Collects all the declared reference parameters
   list[int] refs = [];
   if(f is preCoroutine) {
       refs = [ vardefs[uid][name] | rvar(str name) <- f.formals ];
   }
   // Guard specific check
   MuExp guard = muGuard(muBool(true));
   if(f is preCoroutine) {
       if(!isEmpty(f.guard)) {
           if(f.guard[0] has locals) {
               list[MuExp] block = [ preAssignLoc(vdecl.id, vdecl.initializer) | VarDecl vdecl <- f.guard[0].locals ];
               if(preBlock(<list[MuExp] exps, _>) := f.guard[0].exp) {
                   block = block + exps;
               } else {
                   block = block + [ f.guard[0].exp ];
               }
               guard = muGuard(muBlock(block));
           } else {
               guard = muGuard(f.guard[0].exp);
           }
       }
   }
   
   scopeIn = (!isEmpty(f.funNames)) ? getUID(modName,f.funNames) : ""; // if not a function scope, then the root one
   // Generate a very generic function type
   ftype = Symbol::func(Symbol::\value(),[ Symbol::\value() | i <- [0..size(f.formals)] ]);
   
   list[MuExp] initializers = isEmpty(f.locals) ? [] : [ preAssignLoc(vdecl.id, vdecl.initializer) | VarDecl vdecl <- f.locals[0][0], vdecl has initializer ];
   body = preprocess(modName, f.funNames, f.name, size(f.formals), uid, (f is preCoroutine) ? [ guard, *initializers, *f.body, muExhaust() ] : initializers + f.body);   
   return (f is preCoroutine) ? muCoroutine(uid, scopeIn, size(f.formals), size(vardefs[uid]), refs, muBlock(body))
                              : muFunction(uid, ftype, scopeIn, size(f.formals), size(vardefs[uid]), false, |rascal:///|, [], (), muBlock(body));
}

str fuid = "";

list[MuExp] preprocess(str modName, lrel[str,int] funNames, str fname, int nformals, str uid, list[MuExp] exps){
   fuid = uid;
   println("Pre-processing a function: <uid>");
   return
      for(exp <- exps){
        try {
          append
            top-down visit(exp){
            	// Constructs to be resolved by preprocessing
               case preIntCon(str txt)															=> muInt(toInt(txt))
               case preStrCon(str txt)															=> muCon(txt[1..-1])						// strip surrounding quotes
               case preTypeCon(str txt):                    										{
               																							try {
																											Symbol sym = readTextValueString(#Symbol, txt[1..-1]);
																											insert muTypeCon(sym);
																										} catch IO(str msg) :
																										throw "Could not parse the string of a type constant into Symbol: <msg>";
               																						}
               case preVar(mvar("true")) 														=> muBool(true)
               case preVar(mvar("false")) 														=> muBool(false)
               case preVar(fvar(str var))                                           			=> { if(!isGlobalNonOverloadedFunction(var)) { throw "Function or coroutine <var> has not been declared!"; } muFun(getUidOfGlobalNonOverloadedFunction(var)); }
     	       case preVar(Identifier id) 														=> muVar(id.var,uid,vardefs[uid][id.var])
     	       case preVar(lrel[str,int] funNames, Identifier id)        						=> muVar(id.var,getUID(modName,funNames),vardefs[getUID(modName,funNames)][id.var])
     	       // Specific to delimited continuations (experimental)
     	       case preContLoc()                                                                => muContVar(uid)
     	       case preContVar(lrel[str,int] funNames)                                          => muContVar(getUID(modName,funNames)) 
     	       case preAssignLocList(Identifier id1, Identifier id2, MuExp exp1) 				=> muCallMuPrim("assign_pair", [muInt(vardefs[uid][id1.var]), muInt(vardefs[uid][id2.var]), exp1])
     	       
     	       case preAssignLoc(Identifier id, MuExp exp) 										=> muAssign(id.var,uid,vardefs[uid][id.var], exp)
     	       case preAssign(lrel[str,int] funNames, 
     	       				  Identifier id, MuExp exp)                  						=> muAssign(id.var,getUID(modName,funNames),vardefs[getUID(modName,funNames)][id.var],exp)
     	       case preList(list[MuExp] exps)													=> muCallMuPrim("make_array", exps)
     	        
      	       case preIfthen(cond,thenPart,comma) 													=> muIfelse("", cond, thenPart, [])
      	       
      	       case preLocDeref(Identifier id)                   								=> muVarDeref(id.var,uid,vardefs[uid][id.var])
      	       case preVarDeref(lrel[str,int] funNames, Identifier id)   						=> muVarDeref(id.var,getUID(modName,funNames),vardefs[getUID(modName,funNames)][id.var])
      	       case preLocRef(Identifier id)                     								=> muVarRef(id.var,uid,vardefs[uid][id.var])
      	       case preVarRef(lrel[str,int] funNames, Identifier id)     						=> muVarRef(id.var,getUID(modName,funNames),vardefs[getUID(modName,funNames)][id.var])
      	       case preAssignLocDeref(Identifier id, MuExp exp)  								=> muAssignVarDeref(id.var,uid,vardefs[uid][id.var], exp)
      	       case preAssignVarDeref(lrel[str,int] funNames, Identifier id, MuExp exp)         => muAssignVarDeref(id.var,getUID(modName,funNames),vardefs[getUID(modName,funNames)][id.var],exp)
      	       
      	       case muCallPrim(str name)                                            			=> muCallPrim(name[1..-1], [])
               case muCallPrim(str name, list[MuExp] exps)										=> muCallPrim(name[1..-1], exps)			// strip surrounding quotes
               case muCallMuPrim(str name, list[MuExp] exps)									=> muCallMuPrim(name[1..-1], exps)			// strip surrounding quotes
               
               // Calls that are directly mapped to muPrimitives
               
               case muCall(preVar(mvar("get_array")), [ar, index])								=> muCallMuPrim("subscript_array_mint", [ar, index])
               case muCall(preVar(mvar("get_list")), [lst, index])								=> muCallMuPrim("subscript_list_mint", [lst, index])
               case muCall(preVar(mvar("get_tuple")), [tup, index])								=> muCallMuPrim("subscript_tuple_mint", [tup, index])
               case muCall(preVar(mvar("get_map")), [m, key])									=> muCallPrim("map_subscript", [m, key])
               
               case muCall(preVar(mvar("put_array")), [ar, index, exp1])						=> muCallMuPrim("assign_subscript_array_mint", [ar, index, exp1])
               case muCall(preVar(mvar("put_list")), [lst, index, exp1])						=> muCallMuPrim("assign_subscript_list_mint", [lst, index, exp1])
               
               
               case muCall(preVar(mvar("size_array")), [exp1])									=> muCallMuPrim("size_array", [exp1])
               case muCall(preVar(mvar("size_list")), [exp1])									=> muCallMuPrim("size_list", [exp1])
               case muCall(preVar(mvar("size_set")), [exp1])									=> muCallMuPrim("size_set", [exp1])
               case muCall(preVar(mvar("size_mset")), [exp1])									=> muCallMuPrim("size_mset", [exp1])
               case muCall(preVar(mvar("size_map")), [exp1])									=> muCallMuPrim("size_map", [exp1])
               case muCall(preVar(mvar("size_tuple")), [exp1])									=> muCallMuPrim("size_tuple", [exp1])
               
               case muCall(preVar(mvar("size")),[exp1])                             			=> muCallMuPrim("size",[exp1])
               
               case muCall(preVar(mvar("is_defined")), [exp1])									=> muCallMuPrim("is_defined", [exp1])
               case muCall(preVar(mvar("is_element")), [exp1, exp2])							=> muCallMuPrim("is_element", [exp1, exp2])
               case muCall(preVar(mvar("is_element_mset")), [exp1, exp2])						=> muCallMuPrim("is_element_mset", [exp1, exp2])
               case muCall(preVar(mvar("keys")), [exp1])										=> muCallMuPrim("keys_map", [exp1])
               case muCall(preVar(mvar("map_contains_key")), [exp1, exp2])						=> muCallMuPrim("map_contains_key", [exp1, exp2])
               case muCall(preVar(mvar("values")), [exp1])										=> muCallMuPrim("values_map", [exp1])
               case muCall(preVar(mvar("set2list")), [exp1])									=> muCallMuPrim("set2list", [exp1])
               case muCall(preVar(mvar("mset2list")), [exp1])									=> muCallMuPrim("mset2list", [exp1])
               case muCall(preVar(mvar("equal")), [exp1, exp2])									=> muCallMuPrim("equal", [exp1, exp2])
               case muCall(preVar(mvar("equal_set_mset")), [exp1, exp2])						=> muCallMuPrim("equal_set_mset", [exp1, exp2])
			  
 			   case muCall(preVar(mvar("get_children")), [exp1])								=> muCallMuPrim("get_children", [exp1])
  			   case muCall(preVar(mvar("get_children_and_keyword_params_as_values")), [exp1])	=> muCallMuPrim("get_children_and_keyword_params_as_values", [exp1])
  			   case muCall(preVar(mvar("get_children_and_keyword_params_as_map")), [exp1])		=> muCallMuPrim("get_children_and_keyword_params_as_map", [exp1])
	
			   case muCall(preVar(mvar("get_name")), [exp1])									=> muCallMuPrim("get_name", [exp1])
			   case muCall(preVar(mvar("get_name_and_children_and_keyword_params_as_map")), [exp1])	
			   																					=> muCallMuPrim("get_name_and_children_and_keyword_params_as_map", [exp1])
 			   case muCall(preVar(mvar("get_children_without_layout_or_separators")), [exp1])	=> muCallMuPrim("get_children_without_layout_or_separators", [exp1])
 			   case muCall(preVar(mvar("has_label")), [exp1, exp2])								=> muCallMuPrim("has_label", [exp1, exp2])
			 
               case muCall(preVar(mvar("typeOf")), [exp1])										=> muCallPrim("typeOf", [exp1])
               case muCall(preVar(mvar("typeOfMset")), [exp1])									=> muCallMuPrim("typeOfMset", [exp1])
               
               case muCall(preVar(mvar("elementTypeOf")), [exp1])										=> muCallPrim("elementTypeOf", [exp1])
               case muCall(preVar(mvar("subtype")), [exp1, exp2])         						=> muCallPrim("subtype", [exp1, exp2])
               case muCall(preVar(mvar("make_iarray")), [exp1])									=> muCallMuPrim("make_iarray_of_size", [exp1])
               case muCall(preVar(mvar("make_array")), [exp1])									=> muCallMuPrim("make_array_of_size", [exp1])
               case muCall(preVar(mvar("max")), [exp1, exp2])									=> muCallMuPrim("max_mint_mint", [exp1, exp2])
               case muCall(preVar(mvar("min")), [exp1, exp2])									=> muCallMuPrim("min_mint_mint", [exp1, exp2])
               case muCall(preVar(mvar("starts_with")), [exp1, exp2, exp3])						=> muCallMuPrim("starts_with", [exp1, exp2, exp3])
               case muCall(preVar(mvar("sublist")), list[MuExp] exps)							=> muCallMuPrim("sublist_list_mint_mint", exps)
               case muCall(preVar(mvar("occurs")), list[MuExp] exps)							=> muCallMuPrim("occurs_list_list_mint", exps)
               case muCall(preVar(mvar("subset")), list[MuExp] exps)							=> muCallPrim("set_lessequal_set", exps)
               case muCall(preVar(mvar("subset_set_mset")), list[MuExp] exps)					=> muCallMuPrim("set_is_subset_of_mset", exps)
               case muCall(preVar(mvar("mset_destructive_subtract_mset")), list[MuExp] exps)	=> muCallMuPrim("mset_destructive_subtract_mset", exps)
               case muCall(preVar(mvar("mset_destructive_add_mset")), list[MuExp] exps)		 	=> muCallMuPrim("mset_destructive_add_mset", exps)
               case muCall(preVar(mvar("mset_destructive_add_elm")), list[MuExp] exps)		 	=> muCallMuPrim("mset_destructive_add_elm", exps)
               case muCall(preVar(mvar("mset_destructive_subtract_elm")), list[MuExp] exps)	 	=> muCallMuPrim("mset_destructive_subtract_elm", exps)
               case muCall(preVar(mvar("mset_destructive_subtract_set")), list[MuExp] exps)	 	=> muCallMuPrim("mset_destructive_subtract_set", exps)
               
               case muCall(preVar(mvar("mset_subtract_mset")), list[MuExp] exps)	            => muCallMuPrim("mset_subtract_mset", exps)
               case muCall(preVar(mvar("mset_subtract_elm")), list[MuExp] exps)	 	            => muCallMuPrim("mset_subtract_elm", exps)
               case muCall(preVar(mvar("mset_subtract_set")), list[MuExp] exps)	 	            => muCallMuPrim("mset_subtract_set", exps)
               		
               case muCall(preVar(mvar("mset")), list[MuExp] exps) 								=> muCallMuPrim("mset", exps)
               case muCall(preVar(mvar("mset_empty")), list[MuExp] exps) 						=> muCallMuPrim("mset_empty", exps)
               case muCall(preVar(mvar("set")), list[MuExp] exps) 								=> muCallMuPrim("set", exps)
             
               case muCall(preVar(mvar("make_mset")), list[MuExp] exps)							=> muCallMuPrim("make_mset", exps)
               case muCall(preVar(mvar("make_tuple")), list[MuExp] exps)							=> muCallPrim("tuple_create", exps)
               case muCall(preVar(mvar("get_tuple_elements")), [exp1])							=> muCallMuPrim("get_tuple_elements", [exp1])
               case muCall(preVar(mvar("println")), list[MuExp] exps)							=> muCallMuPrim("println", exps)
               												
               case muCall(preVar(mvar("rint")), list[MuExp] exps) 								=> muCallMuPrim("rint", exps)
               case muCall(preVar(mvar("mint")), list[MuExp] exps) 								=> muCallMuPrim("mint", exps)
               case muCall(preVar(mvar("undefine")), list[MuExp] exps) 							=> muCallMuPrim("undefine", exps)
               
               // Syntactic constructs that are mapped to muPrimitives
      	       case preLess(MuExp lhs, MuExp rhs)												=> muCallMuPrim("less_mint_mint", [lhs, rhs])
      	       case preLessEqual(MuExp lhs, MuExp rhs)											=> muCallMuPrim("less_equal_mint_mint", [lhs, rhs])
      	       case preEqual(MuExp lhs, MuExp rhs)												=> muCallMuPrim("equal_mint_mint", [lhs, rhs])
      	       case preNotEqual(MuExp lhs, MuExp rhs)											=> muCallMuPrim("not_equal_mint_mint", [lhs, rhs])
      	       case preGreater(MuExp lhs, MuExp rhs)											=> muCallMuPrim("greater_mint_mint", [lhs, rhs])
      	       case preGreaterEqual(MuExp lhs, MuExp rhs)										=> muCallMuPrim("greater_equal_mint_mint", [lhs, rhs])
      	       case preAddition(MuExp lhs, MuExp rhs)											=> muCallMuPrim("addition_mint_mint", [lhs, rhs])
      	       case preSubtraction(MuExp lhs, MuExp rhs)										=> muCallMuPrim("subtraction_mint_mint", [lhs, rhs])
      	       case preMultiplication(MuExp lhs, MuExp rhs)										=> muCallMuPrim("multiplication_mint_mint", [lhs, rhs])
      	       case preDivision(MuExp lhs, MuExp rhs)											=> muCallMuPrim("division_mint_mint", [lhs, rhs])
      	       case preModulo(MuExp lhs, MuExp rhs)												=> muCallMuPrim("modulo_mint_mint", [lhs, rhs])
      	       case prePower(MuExp lhs, MuExp rhs)												=> muCallMuPrim("power_mint_mint", [lhs, rhs])
      	       
      	       case preAnd(MuExp lhs, MuExp rhs)												=> muIfelse(nextLabel("L_AND"), lhs, [rhs], [muCon(false)])      	       
      	       case preOr(MuExp lhs, MuExp rhs)									    			=> muIfelse(nextLabel("L_OR"), lhs, [muCon(true)], [rhs])
      	       
      	       case preIs(MuExp lhs, str typeName)												=> muCallMuPrim("is_<typeName>", [lhs])
      	       
      	       // Overloading
      	       case preFunNN(str modName,  str name, int nformals)                  			=> muFun(getUID(modName,[],name,nformals))
      	       case preFunN(lrel[str,int] funNames,  str name, int nformals)        			=> muFun(getUID(modName,funNames,name,nformals), getUID(modName,funNames))
      	       
      	       case muAll(list[MuExp] exps)                                                     => makeMu("ALL",exps)
      	       case muOr(list[MuExp] exps)                                                      => makeMu("OR",exps)
      	       case muOne(list[MuExp] exps)                                                     => makeMuOne("ALL",exps)
      	       
      	       /*
 				* The field 'comma' is a work around given the current semantics of implode 
 				*/
      	       case preIfelse(MuExp cond, list[MuExp] thenPart, bool comma, 
      	                                  list[MuExp] elsePart, bool comma)                     => muIfelse("", cond, thenPart, elsePart)
               case preWhile(MuExp cond, list[MuExp] body, bool comma)                          => muWhile("", cond, body)
               case preIfelse(str label, MuExp cond, list[MuExp] thenPart, bool comma, 
                                                     list[MuExp] elsePart, bool comma)          => muIfelse(label, cond, thenPart, elsePart)
               case preWhile(str label, MuExp cond, list[MuExp] body, bool comma)               => muWhile(label, cond, body)
               case preTypeSwitch(MuExp exp, lrel[MuTypeCase, bool] sepCases, 
                                  MuExp \default, bool comma)                                   => muTypeSwitch(exp, sepCases<0>, \default)
               case preBlock(list[MuExp] exps, bool comma)                                      => muBlock(exps)
               
               case preSubscript(MuExp arr, MuExp index)                                        => muCallMuPrim("subscript_array_mint", [arr, index])
               case preAssignSubscript(MuExp arr, MuExp index, MuExp exp)						=> muCallMuPrim("assign_subscript_array_mint", [arr, index, exp])
      	       
            };
      } catch e: throw "In muRascal function <modName>::<for(<f,n> <- funNames){><f>::<n>::<}><fname>::<nformals> (uid = <uid>) : <e>";   
    }    
}

MuExp generateMu("ALL", list[MuExp] exps, list[bool] backtrackfree) {
    str all_uid = "Library/<fuid>/ALL_<getNextAll()>(0)";
    localvars = [ muVar("c_<i>", all_uid, i)| int i <- index(exps) ];
    list[MuExp] body = [ muYield() ];
    for(int i <- index(exps)) {
        int j = size(exps) - 1 - i;
        if(backtrackfree[j]) {
            body = [ muIfelse(nextLabel(), exps[j], body, [ muCon(222) ]) ];
        } else {
            body = [ muAssign("c_<j>", all_uid, j, muCreate(exps[j])), muWhile(nextLabel(), muNext(localvars[j]), body), muCon(222) ];
        }
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    functions_in_module += muCoroutine(all_uid, fuid, 0, size(localvars), [], muBlock(body));
    return muMulti(muApply(muFun(all_uid, fuid), []));
}

MuExp generateMu("OR", list[MuExp] exps, list[bool] backtrackfree) {
    str or_uid = "Library/<fuid>/Or_<getNextOr()>(0)";
    localvars = [ muVar("c_<i>", or_uid, i)| int i <- index(exps) ];
    list[MuExp] body = [];
    for(int i <- index(exps)) {
        if(backtrackfree[i]) {
            body += muIfelse(nextLabel(), exps[i], [ muYield() ], [ muCon(222) ]);
        } else {
            body = body + [ muCall(exps[i],[]) ];
        }
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    functions_in_module += muCoroutine(or_uid, fuid, 0, size(localvars), [], muBlock(body));
    return muMulti(muApply(muFun(or_uid, fuid), []));
}

// Produces multi- or backtrack-free expressions
MuExp makeMu(str muAllOrMuOr, list[MuExp] exps) {
    tuple[MuExp e,list[MuFunction] functions] res = makeMu(muAllOrMuOr,fuid,exps);
    functions_in_module = functions_in_module + res.functions;
    return res.e;
}

MuExp makeMuMulti(MuExp exp) {
    tuple[MuExp e,list[MuFunction] functions] res = makeMuMulti(exp,fuid);
    functions_in_module = functions_in_module + res.functions;
    return res.e;
}

MuExp makeMuOne(str muAllOrMuOr, list[MuExp] exps) {
    tuple[MuExp e,list[MuFunction] functions] res = makeMuOne(muAllOrMuOr,fuid,exps);
    functions_in_module = functions_in_module + res.functions;
    return res.e;
}


MuModule parse(loc s) {
  pt = parse( #start[Module], s);
  dia = diagnose(pt);
  if(dia != []){
     iprintln(dia);
     throw  "*** Ambiguities in muRascal code, see above report";
  }
  ast = implode(#experiments::Compiler::muRascal::AST::Module, pt);
  ast2 = preprocess(ast);
  // iprintln(ast2);
  return ast2;						   
}

MuModule parse(str s) {
  pt = parse( #start[Module], s);
  dia = diagnose(pt);
  if(dia != []){
     iprintln(dia);
     throw  "*** Ambiguities in muRascal code, see above report";
  }   
  ast = implode(#experiments::Compiler::muRascal::AST::Module, pt);
  ast2 = preprocess(ast);
  return ast2;							   
}
