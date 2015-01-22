module experiments::Compiler::muRascal::Implode

//import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
import IO;
import ValueIO;
import Set;
import List;
import String;
import Type;
//import ParseTree;
import Map;
//import Ambiguity;

import experiments::Compiler::muRascal::MuAllMuOr;
//import experiments::Compiler::Rascal2muRascal::TmpAndLabel;

rel[str,str] global_functions = {};
map[str,map[str,int]] vardefs = ();
list[MuFunction] functions_in_module = [];

private int nLabel = 0;

private str nextLabel(str prefix) {
  nLabel += 1;
  return "<prefix><nLabel>";
}

private str nextLabel() {
  nLabel += 1;
  return "<nLabel>";
}

MuModule preprocess(experiments::Compiler::muRascal::AST::Module pmod){
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
       locals = locals + ( isEmpty(f.locals) ? [] : [ vdecl.id | VarDecl vdecl <- f.locals.vardecls[0] ] );
       //locals = locals + ( isEmpty(f.locals) ? [] : [ vdecl.id | VarDecl vdecl <- f.locals[0][0] ] );
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
   return muModule(pmod.name, {}, [], types, (), [ preprocess(f, pmod.name) | f <- pmod.functions ] + functions_in_module, [], [], 0, resolver, overloaded_functions, (), pmod.\location);
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

MuFunction preprocess(experiments::Compiler::muRascal::AST::Function f, str modName) {
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
               if(preBlock(list[MuExp] exps, _) := f.guard[0].exp) {
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
   
   list[MuExp] initializers = isEmpty(f.locals) ? [] : [ preAssignLoc(vdecl.id, vdecl.initializer) | VarDecl vdecl <- f.locals.vardecls[0], vdecl has initializer ];
   //list[MuExp] initializers = isEmpty(f.locals) ? [] : [ preAssignLoc(vdecl.id, vdecl.initializer) | VarDecl vdecl <- f.locals[0][0], vdecl has initializer ];
   body = preprocess(modName, f.funNames, f.name, size(f.formals), uid, (f is preCoroutine) ? [ guard, *initializers, *f.body, muExhaust() ] : initializers + f.body);   
   return (f is preCoroutine) ? muCoroutine(uid, f.name, scopeIn, size(f.formals), size(vardefs[uid]), f.origin, refs,  muBlock(body))
                              : muFunction(uid, f.name, ftype, scopeIn, size(f.formals), size(vardefs[uid]), false, f.origin, [], (), muBlock(body));
}

str fuid = "";

list[MuExp] preprocess(str modName, lrel[str,int] funNames, str fname, int nformals, str uid, list[MuExp] body_exps){
   fuid = uid;
   println("Pre-processing a function: <uid>");
   return
      for(exp <- body_exps){
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
               case preVar(fvar(str var))                                           			=> { if(!isGlobalNonOverloadedFunction(var)) { throw "Function or coroutine <var> has not been declared!"; } muFun1(getUidOfGlobalNonOverloadedFunction(var)); }
     	       case preVar(Identifier id) 														=> muVar(id.var,uid,vardefs[uid][id.var])
     	       case preVar(lrel[str,int] funNames1, Identifier id)        						=> muVar(id.var,getUID(modName,funNames1),vardefs[getUID(modName,funNames1)][id.var])
     	       // Specific to delimited continuations (experimental)
     	       case preContLoc()                                                                => muContVar(uid)
     	       case preContVar(lrel[str,int] funNames1)                                         => muContVar(getUID(modName,funNames1)) 
     	       case preAssignLocList(Identifier id1, Identifier id2, MuExp exp1) 				=> muCallMuPrim("assign_pair", [muInt(vardefs[uid][id1.var]), muInt(vardefs[uid][id2.var]), exp1])
     	       
     	       case preAssignLoc(Identifier id, MuExp exp1) 									=> muAssign(id.var,uid,vardefs[uid][id.var], exp1)
     	       case preAssign(lrel[str,int] funNames1, 
     	       				  Identifier id, MuExp exp1)                  						=> muAssign(id.var,getUID(modName,funNames1),vardefs[getUID(modName,funNames1)][id.var],exp1)
     	       case preList(list[MuExp] exps)													=> muCallMuPrim("make_array", exps)
     	        
      	      
      	       
      	       case preLocDeref(Identifier id)                   								=> muVarDeref(id.var,uid,vardefs[uid][id.var])
      	       case preVarDeref(lrel[str,int] funNames1, Identifier id)   						=> muVarDeref(id.var,getUID(modName,funNames1),vardefs[getUID(modName,funNames1)][id.var])
      	       case preLocRef(Identifier id)                     								=> muVarRef(id.var,uid,vardefs[uid][id.var])
      	       case preVarRef(lrel[str,int] funNames1, Identifier id)     						=> muVarRef(id.var,getUID(modName,funNames1),vardefs[getUID(modName,funNames1)][id.var])
      	       case preAssignLocDeref(Identifier id, MuExp exp1)  								=> muAssignVarDeref(id.var,uid,vardefs[uid][id.var], exp1)
      	       case preAssignVarDeref(lrel[str,int] funNames1, Identifier id, MuExp exp1)       => muAssignVarDeref(id.var,getUID(modName,funNames1),vardefs[getUID(modName,funNames1)][id.var],exp1)
      	       
      	       case muCallPrim2(str name, loc src)                                            	=> muCallPrim3(name[1..-1], [], src)
               case muCallPrim3(str name, list[MuExp] exps, loc src)						    => muCallPrim3(name[1..-1], exps, src)			// strip surrounding quotes
               
               case org_exp: preMuCallPrim1(str name)                                           => muCallPrim3(name[1..-1], [],   org_exp.origin)
               case org_exp:preMuCallPrim2(str name, list[MuExp] exps)							=> muCallPrim3(name[1..-1], exps, org_exp.origin)
               
               case org_exp: preThrow(MuExp exp1)												=> muThrow(exp1, org_exp.origin)
               
               case org_exp: muCall(preVar(mvar("typeOf")), [exp1])                             => muCallPrim3("typeOf", [exp1], org_exp.origin)
               case org_exp: muCall(preVar(mvar("elementTypeOf")), [exp1])                      => muCallPrim3("elementTypeOf", [exp1], org_exp.origin)
               case org_exp: muCall(preVar(mvar("subtype")), [exp1, exp2])                      => muCallPrim3("subtype", [exp1, exp2], org_exp.origin)
               
               
               // Calls that are directly mapped to muPrimitives
               
               case muCallMuPrim(str name, list[MuExp] exps)                                    => muCallMuPrim(name[1..-1], exps)          // strip surrounding quotes
   
               case muCall(preVar(mvar("get_array")), [ar, index1])								=> muCallMuPrim("subscript_array_mint", [ar, index1])
               case muCall(preVar(mvar("get_list")), [lst, index1])								=> muCallMuPrim("subscript_list_mint", [lst, index1])
               case muCall(preVar(mvar("get_tuple")), [tup, index1])							=> muCallMuPrim("subscript_tuple_mint", [tup, index1])
               case muCall(preVar(mvar("get_mmap")), [m, key])									=> muCallMuPrim("get_mmap", [m, key])
               case muCall(preVar(mvar("put_array")), [ar, index1, exp1])						=> muCallMuPrim("assign_subscript_array_mint", [ar, index1, exp1])
               case muCall(preVar(mvar("put_list")),  [lst, index1, exp1])						=> muCallMuPrim("assign_subscript_list_mint", [lst, index1, exp1])
               case muCall(preVar(mvar("size_array")), [exp1])									=> muCallMuPrim("size_array", [exp1])
               case muCall(preVar(mvar("size_list")), [exp1])									=> muCallMuPrim("size_list", [exp1])
               case muCall(preVar(mvar("size_set")), [exp1])									=> muCallMuPrim("size_set", [exp1])
               case muCall(preVar(mvar("size_mset")), [exp1])									=> muCallMuPrim("size_mset", [exp1])
               case muCall(preVar(mvar("size_map")), [exp1])									=> muCallMuPrim("size_map", [exp1])
               case muCall(preVar(mvar("size_str")), [exp1])									=> muCallMuPrim("size_str", [exp1])
               case muCall(preVar(mvar("size_tuple")), [exp1])									=> muCallMuPrim("size_tuple", [exp1])
               case muCall(preVar(mvar("size")),[exp1])                             	        => muCallMuPrim("size",[exp1])
               case muCall(preVar(mvar("is_defined")), [exp1])									=> muCallMuPrim("is_defined", [exp1])
               case muCall(preVar(mvar("is_element")), [exp1, exp2])							=> muCallMuPrim("is_element", [exp1, exp2])
               case muCall(preVar(mvar("is_tail")), [exp1, exp2, exp3])							=> muCallMuPrim("is_tail_str_str_mint", [exp1, exp2, exp3])
               case muCall(preVar(mvar("substring")), [exp1, exp2, exp3])						=> muCallMuPrim("substring_str_mint_mint", [exp1, exp2, exp3])
               case muCall(preVar(mvar("is_element_mset")), [exp1, exp2])						=> muCallMuPrim("is_element_mset", [exp1, exp2])
               case muCall(preVar(mvar("keys")), [exp1])										=> muCallMuPrim("keys_map", [exp1])
               case muCall(preVar(mvar("mmap_contains_key")), [exp1, exp2])						=> muCallMuPrim("mmap_contains_key", [exp1, exp2])
               case muCall(preVar(mvar("values")), [exp1])										=> muCallMuPrim("values_map", [exp1])
               case muCall(preVar(mvar("set2list")), [exp1])									=> muCallMuPrim("set2list", [exp1])
               case muCall(preVar(mvar("mset2list")), [exp1])									=> muCallMuPrim("mset2list", [exp1])
               case muCall(preVar(mvar("equal")), [exp1, exp2])									=> muCallMuPrim("equal", [exp1, exp2])
               case muCall(preVar(mvar("equal_set_mset")), [exp1, exp2])						=> muCallMuPrim("equal_set_mset", [exp1, exp2])
 			   case muCall(preVar(mvar("get_children")), [exp1])								=> muCallMuPrim("get_children", [exp1])
  			   case muCall(preVar(mvar("get_children_and_keyword_mmap")), [exp1])			    => muCallMuPrim("get_children_and_keyword_mmap", [exp1])
			   case muCall(preVar(mvar("get_name")), [exp1])									=> muCallMuPrim("get_name", [exp1])
			   case muCall(preVar(mvar("get_name_and_children_and_keyword_mmap")), [exp1])		=> muCallMuPrim("get_name_and_children_and_keyword_mmap", [exp1])
			   case muCall(preVar(mvar("get_children_and_keyword_values")), [exp1])				=> muCallMuPrim("get_children_and_keyword_values", [exp1])
			   case muCall(preVar(mvar("get_keyword_mmap")), [exp1])							=> muCallMuPrim("get_keyword_mmap", [exp1])
			   case muCall(preVar(mvar("make_keyword_mmap")), [exp1, exp2])					    => muCallMuPrim("make_keyword_mmap", [exp1, exp2])
			   case muCall(preVar(mvar("get_keys_mmap")), [exp1])								=> muCallMuPrim("get_keys_mmap", [exp1])
 			   case muCall(preVar(mvar("get_children_without_layout_or_separators")), [exp1])	=> muCallMuPrim("get_children_without_layout_or_separators", [exp1])
 			   case muCall(preVar(mvar("has_label")), [exp1, exp2])								=> muCallMuPrim("has_label", [exp1, exp2])
               case muCall(preVar(mvar("typeOfMset")), [exp1])									=> muCallMuPrim("typeOfMset", [exp1])
               case muCall(preVar(mvar("make_iarray")), [exp1])									=> muCallMuPrim("make_iarray_of_size", [exp1])
               case muCall(preVar(mvar("make_array")), [exp1])									=> muCallMuPrim("make_array_of_size", [exp1])
               case muCall(preVar(mvar("max")), [exp1, exp2])									=> muCallMuPrim("max_mint_mint", [exp1, exp2])
               case muCall(preVar(mvar("min")), [exp1, exp2])									=> muCallMuPrim("min_mint_mint", [exp1, exp2])
               case muCall(preVar(mvar("starts_with")), [exp1, exp2, exp3])						=> muCallMuPrim("starts_with", [exp1, exp2, exp3])
               case muCall(preVar(mvar("sublist")), list[MuExp] exps)							=> muCallMuPrim("sublist_list_mint_mint", exps)
               case muCall(preVar(mvar("occurs")), list[MuExp] exps)							=> muCallMuPrim("occurs_list_list_mint", exps)
               case org_exp: muCall(preVar(mvar("subset")), list[MuExp] exps)					=> muCallPrim3("set_lessequal_set", exps, org_exp.origin)
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
               case org_exp: muCall(preVar(mvar("make_tuple")), list[MuExp] exps)				=> muCallPrim3("tuple_create", exps, org_exp.origin)
               case muCall(preVar(mvar("get_tuple_elements")), [exp1])							=> muCallMuPrim("get_tuple_elements", [exp1])
               case muCall(preVar(mvar("println")), list[MuExp] exps)							=> muCallMuPrim("println", exps)						
               case muCall(preVar(mvar("rint")), list[MuExp] exps) 								=> muCallMuPrim("rint", exps)
               case muCall(preVar(mvar("mint")), list[MuExp] exps) 								=> muCallMuPrim("mint", exps)
               case muCall(preVar(mvar("mstr")), list[MuExp] exps) 								=> muCallMuPrim("mstr", exps)
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
      	       case preFunNN(str modName1,  str name1, int nformals1)                  			=> muFun1(getUID(modName1,[],name1,nformals1))
      	       case preFunN(lrel[str,int] funNames1,  str name1, int nformals1)        			=> muFun2(getUID(modName,funNames1,name1,nformals1), getUID(modName,funNames1))
      	       
      	       case org_exp: muAll(list[MuExp] exps)                                            => makeMu("ALL",exps,org_exp.origin)
      	       case org_exp: muOr(list[MuExp] exps)                                             => makeMu("OR",exps,org_exp.origin)
      	       case org_exp: muOne2(list[MuExp] exps)                                           => makeMuOne("ALL",exps,org_exp.origin)
      	       
      	       /*
 				* The field 'comma' is a work around given the current semantics of implode 
 				*/
 				
 			   case preIfthen(cond,thenPart,comma)                                              => muIfelse("", cond, thenPart, [])
      	       case preIfelse(MuExp cond, list[MuExp] thenPart, bool comma1, 
      	                                  list[MuExp] elsePart, bool comma2)                    => muIfelse("", cond, thenPart, elsePart)
               case preWhile(MuExp cond, list[MuExp] body, bool comma)                          => muWhile("", cond, body)
               case preIfelse(str label, MuExp cond, list[MuExp] thenPart, bool comma1, 
                                                     list[MuExp] elsePart, bool comma2)         => muIfelse(label, cond, thenPart, elsePart)
               case preWhile(str label, MuExp cond, list[MuExp] body, bool comma)               => muWhile(label, cond, body)
               case preTypeSwitch(MuExp exp1, lrel[MuTypeCase, bool] sepCases, 
                                  MuExp \default, bool comma)                                   => muTypeSwitch(exp1, sepCases<0>, \default)
               case preBlock(list[MuExp] exps, bool comma)                                      => muBlock(exps)
               
               case preSubscript(MuExp arr, MuExp index)                                        => muCallMuPrim("subscript_array_mint", [arr, index])
               case preAssignSubscript(MuExp arr, MuExp index1, MuExp exp1)						=> muCallMuPrim("assign_subscript_array_mint", [arr, index1, exp1])
      	       
            };
      } catch e: throw "In muRascal function <modName>::<for(<f,n> <- funNames){><f>::<n>::<}><fname>::<nformals> (uid = <uid>) : <e>";   
    }    
}

MuExp generateMu("ALL", list[MuExp] exps, list[bool] backtrackfree, loc src) {
    str all_uid = "<fuid>/ALL_<getNextAll()>";
    localvars = [ muVar("c_<i>", all_uid, i)| int i <- index(exps) ];
    list[MuExp] body = [ muYield0() ];
    for(int i <- index(exps)) {
        int j = size(exps) - 1 - i;
        if(backtrackfree[j]) {
            body = [ muIfelse(nextLabel(), exps[j], body, [ muCon(222) ]) ];
        } else {
            body = [ muAssign("c_<j>", all_uid, j, muCreate1(exps[j])), muWhile(nextLabel(), muNext1(localvars[j]), body), muCon(222) ];
        }
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    												//TODO scopeIn argument is missing
    functions_in_module += muCoroutine(all_uid, fuid, "", 0, size(localvars), src, [], muBlock(body));
    return muMulti(muApply(muFun2(all_uid, fuid), []));
}

MuExp generateMu("OR", list[MuExp] exps, list[bool] backtrackfree, loc src) {
    str or_uid = "<fuid>/Or_<getNextOr()>";
    localvars = [ muVar("c_<i>", or_uid, i)| int i <- index(exps) ];
    list[MuExp] body = [];
    for(int i <- index(exps)) {
        if(backtrackfree[i]) {
            body += muIfelse(nextLabel(), exps[i], [ muYield0() ], [ muCon(222) ]);
        } else {
            body = body + [ muCall(exps[i],[]) ];
        }
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    												//TODO scopeIn argument is missing
    functions_in_module += muCoroutine(or_uid, fuid, "", 0, size(localvars),  src, [], muBlock(body));
    return muMulti(muApply(muFun2(or_uid, fuid), []));
}

// Produces multi- or backtrack-free expressions
MuExp makeMu(str muAllOrMuOr, list[MuExp] exps, loc src) {
    tuple[MuExp e,list[MuFunction] functions] res = makeMu(muAllOrMuOr,fuid,exps,src);
    functions_in_module = functions_in_module + res.functions;
    return res.e;
}

MuExp makeMuMulti(MuExp exp, loc src) {
    tuple[MuExp e,list[MuFunction] functions] res = makeMuMulti(exp,fuid,src);
    functions_in_module = functions_in_module + res.functions;
    return res.e;
}

MuExp makeMuOne(str muAllOrMuOr, list[MuExp] exps, loc src) {
    tuple[MuExp e,list[MuFunction] functions] res = makeMuOne(muAllOrMuOr,fuid,exps,src);
    functions_in_module = functions_in_module + res.functions;
    return res.e;
}

// Temporary copy of TmpAndLabel.rsc

private int allCounter = 0;								// *** state

private int getNextAll() {
    int counter = allCounter;
    allCounter = allCounter + 1;
    return counter;
}

private void resetAllCounter() {
    allCounter = 0;
}

private int orCounter = 0;								// *** state

private int getNextOr() {
    int counter = orCounter;
    orCounter = orCounter + 1;
    return counter;
}

private void resetOrCounter() {
    orCounter = 0;
}



