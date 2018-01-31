module lang::rascalcore::compile::muRascal::Implode

import lang::rascalcore::compile::muRascal::AST;
import IO;
import ValueIO;
import Set;
import List;
import String;
import Type;
import Map;
import Message;

import lang::rascalcore::compile::muRascal::MuBoolExp;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;

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

MuModule preprocess(lang::rascalcore::compile::muRascal::AST::MuPreModule pmod){
   global_functions = {};
   vardefs = ();
   functions_in_module = [];
   global_functions = { <f.name, getUID(pmod.name,f.funNames,f.name,size(f.formals))> | f <- pmod.functions };
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
       assert size(locals) == size({ *locals }) : "Incorrect number of locals in preprocess";
       
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
   return muModule(pmod.name, (), {}, [], [], types, (), [ preprocess(f, pmod.name) | f <- pmod.functions ] + functions_in_module, [], [], 0, resolver, overloaded_functions, (), {}, pmod@\location);
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
		return getFirstFrom(global_functions[name]);
	}
	throw "The function <name> does not exist!";
}

//@doc{Generates a unique scope id: non-empty 'funNames' list implies a nested function}
///*
// * NOTE: Given that the muRascal language does not support overloading, the dependency of function uids 
// *       on the number of formal parameters has been removed 
// */
//str getUID(str modName, lrel[str,int] funNames, str funName, int nformals) {
//	// Due to the current semantics of the implode
//	modName = replaceAll(modName, "::", "");
//	return "<modName>/<for(<f,n> <- funNames){><f>(<n>)/<}><funName>"; 
//}
//str getUID(str modName, [ *tuple[str,int] funNames, <str funName, int nformals> ]) 
//	= "<modName>/<for(<f,n> <- funNames){><f>(<n>)/<}><funName>";

MuFunction preprocess(lang::rascalcore::compile::muRascal::AST::Function f, str modName) {
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
  // TODO: type was added for new (experimental) type checker
   str scopeIn = (!isEmpty(f.funNames)) ? getUID(modName,f.funNames) : ""; // if not a function scope, then the root one
   // Generate a very generic function type
   ftype = Symbol::func(Symbol::\value(),[ Symbol::\value() | i <- [0..size(f.formals)] ], []);
   argNames = ["<arg>" | arg <- f.formals];
   
   list[MuExp] initializers = isEmpty(f.locals) ? [] : [ preAssignLoc(vdecl.id, vdecl.initializer) | VarDecl vdecl <- f.locals.vardecls[0], vdecl has initializer ];
   //list[MuExp] initializers = isEmpty(f.locals) ? [] : [ preAssignLoc(vdecl.id, vdecl.initializer) | VarDecl vdecl <- f.locals[0][0], vdecl has initializer ];
   body = preprocess(modName, f.funNames, f.name, size(f.formals), uid, (f is preCoroutine) ? [ guard, *initializers, *f.body, muExhaust() ] : initializers + f.body);   
   return (f is preCoroutine) ? muCoroutine(uid, f.name, scopeIn, size(f.formals), size(vardefs[uid]), f@location, refs,  muBlock(body))
                              : muFunction(uid, f.name, ftype, argNames, Symbol::\tuple([]), scopeIn, size(f.formals), size(vardefs[uid]), false, true, true, f@location, [], (), false, 0, 0, muBlock(body));
}

str fuid = "";

private list[MuExp] preprocess(str modName, lrel[str,int] funNames, str fname, int nformals, str uid, list[MuExp] body_exps){
   fuid = uid;
   //println("Pre-processing function: <uid>");
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
               
               case org_exp: preMuCallPrim1(str name)                                           => muCallPrim3(name[1..-1], [],   org_exp@location)
               case org_exp:preMuCallPrim2(str name, list[MuExp] exps)							=> muCallPrim3(name[1..-1], exps, org_exp@location)
               
               case org_exp: preThrow(MuExp exp1)												=> muThrow(exp1, org_exp@location)
               
               case org_exp: muCall(preVar(mvar(str called_fname)), list[MuExp] exps)           => preprocessMuCall(org_exp, called_fname, exps)
               case muCallMuPrim(str name, list[MuExp] exps)                                    => muCallMuPrim(name[1..-1], exps)          // strip surrounding quotes
               
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
      	       
      	       case org_exp: preIs(MuExp lhs, "appl")											=> muCallPrim3("is_appl", [lhs], org_exp@\location)
      	       case org_exp: preIs(MuExp lhs, "lexical")										=> muCallPrim3("is_lexical", [lhs], org_exp@\location)
      	       case org_exp: preIs(MuExp lhs, "layout")											=> muCallPrim3("is_layout", [lhs], org_exp@\location)
      	       case org_exp: preIs(MuExp lhs, "concretelist")									=> muCallPrim3("is_concretelist", [lhs], org_exp@\location)
      	       case preIs(MuExp lhs, str typeName)												=> muCallMuPrim("is_<typeName>", [lhs])
      	       
      	       // Overloading
      	       case preFunNN(str modName1,  str name1, int nformals1)                  			=> muFun1(getUID(modName1,[],name1,nformals1))
      	       case preFunN(lrel[str,int] funNames1,  str name1, int nformals1)        			=> muFun2(getUID(modName,funNames1,name1,nformals1), getUID(modName,funNames1))
      	       
      	       
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
               case preTypeSwitch(MuExp exp1, lrel[MuTypeCase, bool] sepTypeCases, 
                                  MuExp \default, bool comma)                                   => muTypeSwitch(exp1, sepTypeCases<0>, \default)
               case preBlock(list[MuExp] exps, bool comma)                                      => muBlock(exps)
               
               case preSubscript(MuExp arr, MuExp index)                                        => muCallMuPrim("subscript_array_mint", [arr, index])
               case preAssignSubscript(MuExp arr, MuExp index1, MuExp exp1)						=> muCallMuPrim("assign_subscript_array_mint", [arr, index1, exp1])
      	       
            };
      } catch e: throw "In muRascal function <modName>::<for(<f,n> <- funNames){><f>::<n>::<}><fname>::<nformals> (uid = <uid>) : <e>";   
    }    
}

MuExp preprocessMuCall(MuExp org_exp, str fname, list[MuExp] exps){
    switch(fname){
    // Calls that are directly mapped to RascalPrimitives
                   
    case "typeOf":                  return muCallPrim3("typeOf", exps, org_exp@location);
    case "elementTypeOf":           return muCallPrim3("elementTypeOf", exps, org_exp@location);
    case "value_is_subtype":        return muCallPrim3("subtype_value_type", exps, org_exp@location);
    case "subtype":                 return muCallPrim3("subtype", exps, org_exp@location);
    case "is_element":              return muCallPrim3("elm_in_set", exps, org_exp@\location);
    case "subset":                  return muCallPrim3("set_lessequal_set", exps, org_exp@location);
    case "make_tuple":              return muCallPrim3("tuple_create", exps, org_exp@location);
               
    // Calls that are directly mapped to muPrimitives
                  
    case "get_array":               return muCallMuPrim("subscript_array_mint", exps);
    case "get_list":                return muCallMuPrim("subscript_list_mint", exps);
    case "get_tuple":               return muCallMuPrim("subscript_tuple_mint", exps);
    case "get_mmap":                return muCallMuPrim("get_mmap", exps);
    case "put_array":               return muCallMuPrim("assign_subscript_array_mint", exps);
    case "put_list":                return muCallMuPrim("assign_subscript_list_mint", exps);
    case "size_array":              return muCallMuPrim("size_array", exps);
    case "size_list":               return muCallMuPrim("size_list", exps);
    case "size_set":                return muCallMuPrim("size_set", exps);
    case "size_mset":               return muCallMuPrim("size_mset", exps);
    case "size_map":                return muCallMuPrim("size_map", exps);
    case "size_str":                return muCallMuPrim("size_str", exps);
    case "size_tuple":              return muCallMuPrim("size_tuple", exps);
    case "size":                    return muCallMuPrim("size",exps);
    case "is_defined":              return muCallMuPrim("is_defined", exps);
    case "is_tail":                 return muCallMuPrim("is_tail_str_str_mint", exps);
    case "substring":               return muCallMuPrim("substring_str_mint_mint", exps);
    case "is_element_mset":         return muCallMuPrim("is_element_mset", exps);
    case "keys":                    return muCallMuPrim("keys_map", exps);
    case "mmap_contains_key":       return muCallMuPrim("mmap_contains_key", exps);
    case "values":                  return muCallMuPrim("values_map", exps);
    case "set2list":                return muCallMuPrim("set2list", exps);
    case "mset2list":               return muCallMuPrim("mset2list", exps);
    case "equal":                   return muCallMuPrim("equal", exps);
    case "match":                   return muCallMuPrim("match", exps);
    case "equal_set_mset":          return muCallMuPrim("equal_set_mset", exps);
    case "get_children":            return muCallMuPrim("get_children", exps);
    case "get_children_and_keyword_mmap":
                                    return muCallPrim3("get_children_and_keyword_mmap", exps, exps[0]@\location);
    case "get_name":                return muCallMuPrim("get_name", exps);
    case "get_name_and_children_and_keyword_mmap":
                                    return muCallPrim3("get_name_and_children_and_keyword_mmap", exps, exps[0]@\location);
    case "get_name_and_children":   return muCallMuPrim("get_name_and_children", exps);
    case "get_children_and_keyword_values":
                                    return muCallPrim3("get_children_and_keyword_values", exps, exps[0]@\location);
    case "make_keyword_mmap":       return muCallMuPrim("make_keyword_mmap", exps);
    case "get_keys_mmap":           return muCallMuPrim("get_keys_mmap", exps);
    case "get_children_without_layout_or_separators_with_keyword_map":
                                    return muCallMuPrim("get_children_without_layout_or_separators_with_keyword_map", exps);
    case "get_children_without_layout_or_separators_without_keyword_map":
                                    return muCallMuPrim("get_children_without_layout_or_separators_without_keyword_map", exps);
    case "has_label":               return muCallMuPrim("has_label", exps);
    case "typeOfMset":              return muCallMuPrim("typeOfMset", exps);
    case "make_iarray":             return muCallMuPrim("make_iarray_of_size", exps);
    case "make_array":              return muCallMuPrim("make_array_of_size", exps);
    case "max":                     return muCallMuPrim("max_mint_mint", exps);
    case "min":                     return muCallMuPrim("min_mint_mint", exps);
    case "starts_with":             return muCallMuPrim("starts_with", exps);
    case "sublist":                 return muCallMuPrim("sublist_list_mint_mint", exps);
    case "occurs":                  return muCallMuPrim("occurs_list_list_mint", exps);
    case "subset_set_mset":         return muCallMuPrim("set_is_subset_of_mset", exps);
    case "mset_destructive_subtract_mset":
                                    return muCallMuPrim("mset_destructive_subtract_mset", exps);
    case "mset_destructive_add_mset":
                                    return muCallMuPrim("mset_destructive_add_mset", exps);
    case "mset_destructive_add_elm":return muCallMuPrim("mset_destructive_add_elm", exps);
    case "mset_destructive_subtract_elm":
                                    return muCallMuPrim("mset_destructive_subtract_elm", exps);
    case "mset_destructive_subtract_set":
                                    return muCallMuPrim("mset_destructive_subtract_set", exps);
    case "mset_set_subtract_set":   return muCallMuPrim("mset_set_subtract_set", exps);
    case "mset_subtract_mset":      return muCallMuPrim("mset_subtract_mset", exps);
    case "mset_subtract_elm":       return muCallMuPrim("mset_subtract_elm", exps);
    case "mset_subtract_set":       return muCallMuPrim("mset_subtract_set", exps);
    case "mset":                    return muCallMuPrim("mset", exps);
    case "mset_empty":              return muCallMuPrim("mset_empty", exps);
    case "set":                     return muCallMuPrim("set", exps);
    case "make_mset":               return muCallMuPrim("make_mset", exps);
               
    case "get_tuple_elements":      return muCallMuPrim("get_tuple_elements", exps);
    case "println":                 return muCallMuPrim("println", exps);                        
    case "rint":                    return muCallMuPrim("rint", exps);
    case "mint":                    return muCallMuPrim("mint", exps);
    case "mstr":                    return muCallMuPrim("mstr", exps);
    case "undefine":                return muCallMuPrim("undefine", exps);
               
    case "iterator":                return muCallMuPrim("make_iterator", exps);
    case "hasNext":                 return muCallMuPrim("iterator_hasNext", exps);
    case "getNext":                 return muCallMuPrim("iterator_next", exps);
    case "descendant_iterator":     return muCallMuPrim("make_descendant_iterator", exps);
               
    // Macro-like function calls that are direcetly translated to a muExpression
    case "GET_SUBJECT_LIST":        return muCallMuPrim("subscript_array_mint", [exps[0], muCon(0)]);
    case "GET_SUBJECT_CURSOR":      return muCallMuPrim("subscript_array_mint", [exps[0], muCon(1)]);
    case "MAKE_SUBJECT":            return muCallMuPrim("make_subject", exps);
    }
   return org_exp;
} 

// Produces multi-valued or backtrack-free expressions
private MuExp makeBoolExp(str operator, list[MuExp] exps, loc src) {
    tuple[MuExp e,list[MuFunction] functions] res = makeBoolExp(operator,fuid,exps,src);
    functions_in_module = functions_in_module + res.functions;
    return res.e;
}

private MuExp makeSingleValuedBoolExp(str operator, list[MuExp] exps, loc src) {
    tuple[MuExp e,list[MuFunction] functions] res = makeSingleValuedBoolExp(operator,fuid,exps,src);
    functions_in_module = functions_in_module + res.functions;
    return res.e;
}

