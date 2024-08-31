module lang::rascalcore::compile::muRascal2Java::Resolvers

import lang::rascalcore::compile::muRascal::AST;

extend lang::rascalcore::check::CheckerCommon;
import lang::rascalcore::check::Fingerprint;

import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal2Java::Conversions;
import lang::rascalcore::compile::util::Names;

//import lang::rascalcore::compile::muRascal2Java::SameJavaType;

import IO;
import List;
import Location;
//import ListRelation;
import Map;
import Node;
import Relation;
import Set;
import String;
import util::Math;

alias Name_Arity = tuple[str name, int arity];

// Get all functions and constructors from a given tmodel

rel[Name_Arity, Define] getFunctionsAndConstructors(TModel tmodel, set[loc] module_and_extend_scopes){
     if(!tmodel.moduleLocs[tmodel.modelName]?){
        iprintln(tmodel);
        throw "getFunctionsAndConstructors";
     }
     mscope = tmodel.moduleLocs[tmodel.modelName];
                                
     overloads0 = {*{ ov | tuple[loc def, IdRole idRole, AType atype] ov <- ovl.overloads,(ov.idRole == functionId() || ov.idRole == constructorId()) } |  loc u <- tmodel.facts, isContainedIn(u, mscope), /ovl:overloadedAType(rel[loc def, IdRole idRole, AType atype] overloads) := tmodel.facts[u] };
     overloads_used_in_module = {<<def.id, size(tp has formals ? tp.formals : tp.fields)>, def> 
                                | tuple[loc def, IdRole idRole, AType atype] ov <- overloads0, 
                                  (ov.idRole == functionId() || ov.idRole == constructorId()),
                                  tmodel.definitions[ov.def]?,
                                  Define def := tmodel.definitions[ov.def], 
                                  defType(AType tp) := def.defInfo,
                                  !isSyntheticFunctionName(def.id)
                                };
     
     overloads_created_in_module =
           { <<def.id, size(tp has formals ? tp.formals : tp.fields)>, def> 
           | Define def <- tmodel.defines, 
             defType(AType tp) := def.defInfo,
             (def.idRole == functionId() && any(me_scope <- module_and_extend_scopes, isContainedIn(def.defined, me_scope))) || def.idRole == constructorId(),
             !(acons(AType adt, list[AType] _, list[Keyword] _) := tp && isNonTerminalAType(adt))
           };
     return overloads_used_in_module + overloads_created_in_module; 
}
 
str varName(muVar(str name, str _fuid, int pos, AType _, IdRole idRole)){ // duplicate, see CodeGen
    
    result = asJavaName(name);
    if(name[0] == "$") return result;
    if(pos >= 0 || isWildCard(name)) result += "_<abs(pos)>";
    return result;
    // TODO the above code replaces this (for the benefit of the compiler):
    // return (name[0] != "$") ? "<asJavaName(name)><(pos >= 0 || isWildCard(name)) ? "_<abs(pos)>" : "">" : asJavaName(name);
} 

/*****************************************************************************/
/*  Convert an AType to a test for that AType (represented as VType)         */
/*****************************************************************************/

//str atype2istype(str e, AType t, JGenie jg) = "<e>.getType().comparable(<jg.shareType(t)>)";

str atype2istype(str e, overloadedAType(rel[loc, IdRole, AType] overloads), JGenie jg)
    = "<e> instanceof IConstructor && <intercalate(" || ", ["((IConstructor)<e>).getConstructorType().equivalent(<atype2vtype(tp, jg)>)" | <_, _, tp> <- overloads])>";
 
//str atype2istype(str e, t:acons(AType adt, list[AType] fields, list[Keyword] kwFields), JGenie jg) 
//    = "<e>.getConstructorType().comparable(<jg.shareType(t)>)";

str atype2istype(str e, a:aadt(str adtName, list[AType] parameters, dataSyntax()), JGenie jg) {
    res = "$isSubtypeOf(<e>.getType(), <jg.accessType(a)>)";
    return res;
}


str atype2istype(str e, a:aadt(str adtName, list[AType] parameters, contextFreeSyntax()), JGenie jg) {
    res = "$isNonTerminal(<e>.getType(), <jg.accessType(a)>)";
    return res;
    //return "<e>.getType() instanceof NonTerminalType && ((NonTerminalType) <e>.getType()).getSymbol().equals(<jg.shareConstant(sort(adtName))>)";
}

str atype2istype(str e, aadt(str adtName, list[AType] parameters, lexicalSyntax()), JGenie jg) {
    return "$isNonTerminal(<e>.getType(), <jg.shareConstant(lex(adtName))>)";
    //return "<e>.getType() instanceof NonTerminalType && ((NonTerminalType) <e>.getType()).getSymbol().equals(<jg.shareConstant(lex(adtName))>)";
}

str atype2istype(str e, a:aadt(str adtName, list[AType] parameters, keywordSyntax()), JGenie jg) {
    return "$isNonTerminal(<e>.getType(), <jg.shareConstant(a)>)";
    //return "<e>.getType() instanceof NonTerminalType && ((NonTerminalType) <e>.getType()).getSymbol().equals(<jg.shareConstant(keywords(adtName))>)";
}

default str atype2istype(str e, AType t, JGenie jg) {
    if(isFunctionAType(t)){
        return "$intersectsType(<e>.getType(),<jg.accessType(t)>)";
    } else {
        return "$isSubtypeOf(<e>.getType(),<jg.accessType(t)>)";
    }
}

public set[set[Define]] mygroup(set[Define] input, bool (Define a, Define b) similar) {
      remaining = input;
      result = {};
      while(!isEmpty(remaining)){
        d = getFirstFrom(remaining);
        g = d + { e | e <- remaining, similar(e, d) };
        remaining -= g;
        result += {g};
      }
      return result;  
    }
    
// Generate all resolvers for a given module

str generateResolvers(str moduleName, map[loc, MuFunction] loc2muFunction, set[str] imports, set[str] extends, map[str,TModel] tmodels, map[str,loc] module2loc, JGenie jg){  
    //iprintln(tmodels);
    
    //tmodels = (mname : convertTModel2PhysicalLocs(tmodels[mname]) | mname <- tmodels);
    module_scope = module2loc[moduleName];
   
    loc2module = invertUnique(module2loc);
    module_scopes = domain(loc2module);
    extend_scopes = { module2loc[ext] | ext <- extends };
    import_scopes = { module2loc[imp] | imp <- imports };
    
    module_and_extend_scopes = module_scope + extend_scopes;
    
    rel[Name_Arity, Define] functions_and_constructors = { *getFunctionsAndConstructors(tmodels[mname], module_and_extend_scopes) | mname <- tmodels };
                          
    resolvers = "";
    for(<fname, farity> <- domain(functions_and_constructors), !isClosureName(fname)){
        // Group all functions of same name and arity by scope
        set[Define] defs = functions_and_constructors[<fname, farity>];
                
        defs_in_disjoint_scopes = mygroup(defs, bool(Define a, Define b) { 
                                                    return a.scope notin module_scopes && b.scope notin module_scopes && a.scope == b.scope 
                                                           || a.scope in module_scopes && b.scope in module_scopes
                                                           ;
                                              });

        // ... and generate a resolver for each group
        for(sdefs <- defs_in_disjoint_scopes){
            if(any(d <- sdefs, d.scope notin module_scopes)){
                // All local functions trated samw wrt keyword parameters
                resolvers += generateResolver(moduleName, fname, sdefs, loc2muFunction, module_scope, import_scopes, extend_scopes, tmodels[moduleName].paths, tmodels[moduleName], loc2module, jg);
                
            } else {
                // For global functions we differentiate wrt keyword oarameters
                kwpFormals = {};
                for(d <- sdefs, defType(AType tp) := d.defInfo, isFunctionAType(tp)){
                    kwpFormals += <d, tp.kwFormals>;
                }
                with_kwp = {d | <d, kwps> <- kwpFormals, !isEmpty(kwps)};
                without_kwp = sdefs - with_kwp;
                resolvers += generateResolver(moduleName, fname, with_kwp, loc2muFunction, module_scope, import_scopes, extend_scopes, tmodels[moduleName].paths, tmodels[moduleName], loc2module, jg);
                resolvers += generateResolver(moduleName, fname, without_kwp, loc2muFunction, module_scope, import_scopes, extend_scopes, tmodels[moduleName].paths, tmodels[moduleName], loc2module, jg);
            }
        }
    }
    return resolvers;
}

list[MuExp] getExternalRefs(Define fun_def, map[loc, MuFunction] loc2muFunction){
    if(loc2muFunction[fun_def.defined]?){
        fun = loc2muFunction[fun_def.defined];  
        return sort({ ev | ev <- fun.externalRefs, ev.pos >= 0, ev notin fun.formals });
    } else { 
        return [];
    }
}

list[MuExp] getExternalRefs(set[Define] relevant_fun_defs, map[loc, MuFunction] loc2muFunction){
   return sort({ *getExternalRefs(fun_def, loc2muFunction) | fun_def <- relevant_fun_defs });
}

tuple[bool,loc] findImplementingModule(set[Define] fun_defs, set[loc] import_scopes, set[loc] extend_scopes){
    for(s <- import_scopes + extend_scopes){
        if(all(fd <- fun_defs, isContainedIn(fd.defined, s))){
            return <true, s>;
        }
    }
    return <false, |unknown:///|>;

}
// Generate a resolver for a specific function

str generateResolver(str moduleName, str functionName, set[Define] fun_defs, map[loc, MuFunction] loc2muFunction, loc module_scope, set[loc] import_scopes, set[loc] extend_scopes, Paths paths, TModel tm, map[loc, str] loc2module, JGenie jg){
    //println("generate resolver for <moduleName>, <functionName>");
    
    module_scopes = domain(loc2module);
    
    set[Define] local_fun_defs = {def | def <- fun_defs, /**/isContainedIn(def.defined, module_scope)/*, "test" notin loc2muFunction[def.defined].modifiers*/ };
    
    nonlocal_fun_defs0 = 
        for(def <- fun_defs){
            if(!isEmpty(extend_scopes) && any(ext <- extend_scopes, isContainedIn(def.defined, ext))) append def;
            if(!isEmpty(import_scopes) && any(imp <- import_scopes, isContainedIn(def.defined, imp))) append def;
        };
    nonlocal_fun_defs = toSet(nonlocal_fun_defs0);                     
    set[Define] relevant_fun_defs = local_fun_defs + nonlocal_fun_defs;
    cons_defs = { cdef | cdef <- relevant_fun_defs, defType(AType tp) := cdef.defInfo, isConstructorAType(tp) };
    
    implementing_module = "";
    if(isEmpty(local_fun_defs)){
        <found, im> = findImplementingModule(relevant_fun_defs, import_scopes, extend_scopes);
        if(found){
            implementing_module = loc2module[im];
        }
    }
    
    relevant_fun_defs -= cons_defs;
    
    if(isEmpty(relevant_fun_defs)) return "";
  
    acons_adt = avoid();
    list[AType] acons_fields = [];
    acons_kwfields = [];
    
    for(cdef <- cons_defs, defType(AType ctp) := cdef.defInfo){
        acons_adt = alub(acons_adt, ctp.adt);
        acons_fields = acons_fields == [] ? ctp.fields : alubList(acons_fields, ctp.fields);
        acons_kwfields += ctp.kwFields;
    }
    
    acons_fun_type = afunc(acons_adt, acons_fields, acons_kwfields);
   
    resolver_fun_type = (avoid() | alub(it, tp) | fdef <- relevant_fun_defs, defType(AType tp) := fdef.defInfo);
    resolver_fun_type = isEmpty(relevant_fun_defs) ? acons_fun_type
                                                   : (isEmpty(cons_defs) ? resolver_fun_type
                                                                         : alub(acons_fun_type, resolver_fun_type));
    if(!isFunctionAType(resolver_fun_type)) return "";
    
    inner_scope = "";
  
    if(all(def <- relevant_fun_defs, def in local_fun_defs, def.scope notin module_scopes)){
        for(def <- relevant_fun_defs, isContainedIn(def.defined, module_scope)){
            fun = loc2muFunction[def.defined];
            inner_scope = "<fun.scopeIn>_";
            break;
        }
    }
    resolver_name = "<inner_scope><asJavaName(functionName)>";
    
    fun_kwFormals = acons_kwfields;
    for(def <- relevant_fun_defs, def notin cons_defs, defType(AType tp) := def.defInfo){
        if(loc2muFunction[def.defined]?){
            fun = loc2muFunction[def.defined];
            fun_kwFormals += jg.collectKwpFormals(fun);
        } else {
            fun_kwFormals += tp.kwFormals;
        }
    }
    
    resolver_fun_type = resolver_fun_type[kwFormals=fun_kwFormals];

    resolver_formals_types = [ avalue() | _ <- resolver_fun_type.formals ];
    resolver_arity_formal_types = size(resolver_formals_types);
    resolver_return_type = resolver_fun_type has ret ? resolver_fun_type.ret : resolver_fun_type.adt;
    
    if(resolver_arity_formal_types == 0 && size(relevant_fun_defs + cons_defs) > 1
       || size(relevant_fun_defs) == 0 
          && size(cons_defs) > 1 
          && (  size(acons_fields) == 1 
             || all(int i <- index(acons_fields), int j <- index(acons_fields), i != j, comparable(acons_fields[i], acons_fields[j]))
             )
      ){
        return
            "public <atype2javatype(resolver_return_type)> <resolver_name>(){ // Generated by Resolver
            '  throw new RuntimeException(\"Constructor `<functionName>` is overloaded and can only be called with qualifier\"); 
            '}
            '";
    }

    resolver_returns_void = isVoidAType(resolver_return_type);
    argTypes = intercalate(", ", ["<atype2javatype(f)> $P<i>" | i <- index(resolver_formals_types), f := resolver_formals_types[i]]);
 
    actuals = intercalate(", ", ["$P<i>" | i <- index(resolver_formals_types)]);
    body = resolver_returns_void ? "" : "<atype2javatype(resolver_return_type)> $result = null;\n";
    
    kwpActuals = "java.util.Map\<java.lang.String,IValue\> $kwpActuals";
    activeKwpFormals1 = { *jg.collectKwpFormals(fun) 
                        | def <- local_fun_defs,                                 
                          //def.defined in local_fun_defs, //.defined, 
                          //def notin cons_defs, 
                          loc2muFunction[def.defined]?,
                          fun := loc2muFunction[def.defined]};
   
    if(hasKeywordParameters(resolver_fun_type) || !isEmpty(activeKwpFormals1)) { //any(def <- local_fun_defs, def.scope != module_scope)){
        argTypes = isEmpty(argTypes) ? kwpActuals : "<argTypes>, <kwpActuals>";
    }
    
    onlyGlobalFuns = all(fd <- relevant_fun_defs, loc2module[fd.scope]?); // Only toplevel functions
    if(!onlyGlobalFuns){
        externalRefs = getExternalRefs(relevant_fun_defs, loc2muFunction);
     
        if(!isEmpty(externalRefs) ){
            argTypes += (isEmpty(argTypes) ? "" : ", ") +  intercalate(", ", [ /*var.idRole in assignableRoles ?*/ "ValueRef\<<jtype>\> <varName(var)>" /*:  "<jtype> <var.name>_<var.pos>"*/ | var <- externalRefs, jtype := atype2javatype(var.atype)]);
        }
    }
    
    extends = {<f, t> | <f, extendPath(), t> <- paths }+;
    
    arg_types = resolver_arity_formal_types == 0 ? [] : toList({ unsetRec(getFunctionOrConstructorArgumentTypes(ta), "alabel") | def <- relevant_fun_defs, defType(AType ta) := def.defInfo });
    
    if(!isEmpty(implementing_module)){
        if(all(fd <- relevant_fun_defs, !loc2module[fd.scope]?)){
            return "";
        }
        if(contains(argTypes, kwpActuals)){
            actuals += isEmpty(actuals) ? "$kwpActuals" : ", $kwpActuals";
        }
        pref = resolver_returns_void ? "" : "return (<atype2javatype(resolver_return_type)>)";
        body = "<pref> <module2field(implementing_module)>.<resolver_name>(<actuals>);";
        resolvers = "public <atype2javatype(resolver_return_type)> <resolver_name>(<argTypes>){ // Generated by Resolver
                    '   <body> 
                    '}
                    '";
        return resolvers;
    }
    
    arg_types = sort(arg_types, bool (list[AType] a, list[AType] b){ return a != b && asubtypeList(a, b); }); // Most specifc types first
   
    bool funBeforeExtendedBeforeDefaultBeforeConstructor(Define a, Define b){
        return    a != b
               && defType(AType ta) := a.defInfo 
               && defType(AType tb) := b.defInfo 
               && isFunctionAType(ta) 
               && (isConstructorAType(tb) 
                  || !ta.isDefault && tb.isDefault
                  || <a.scope, b.scope> in extends
                  || isBefore(a.defined, b.defined)
                  )
               ;
    }
    
    sorted_relevant_fun_defs = sort(relevant_fun_defs, funBeforeExtendedBeforeDefaultBeforeConstructor);
    
    sorted_default_fun_defs = [ def | def <- sorted_relevant_fun_defs, def.defInfo.atype.isDefault ];
    
    sorted_nondefault_fun_defs = sorted_relevant_fun_defs - sorted_default_fun_defs;
    
    map[int, lrel[str,str]] overload_table = ();
    lrel[str,str] defaults_and_constructors = [];
    
    physical2logical = invertUnique(tm.logical2physical);
    
    // Handle a function or constructor defintion
    
    void handleDef(Define def){
        inner_scope = "";
        //if(def.scope notin module_scopes, /*isContainedIn(def.scope, module_scope) */def in local_fun_defs){
        //    fun = loc2muFunction[def.defined];
        //    inner_scope = "<fun.scopeIn>_";
        //}
        uniqueName = "<inner_scope><asJavaName(def.id, completeId=false)>";
        if(physical2logical[def.defined]?){
          ph = physical2logical[def.defined];
          path = ph.path;
          if(path[0] == "/"){
            path = path[1..];
          }
          //i = findLast(path, "/");
          //path = path[i+1..];
          
          name = replaceAll(path, "/", "_");
          uniqueName = "<inner_scope><asJavaName(name, completeId=false)>";
       }
        //uniqueName = "<inner_scope><asJavaName(def.id, completeId=false)>$<def.uid>";
        //uniqueName = "<inner_scope><asJavaName(def.id, completeId=false)>_<def.defined.begin.line>A<def.defined.offset>";
        def_type = def.defInfo.atype;
        
        conds = [];
        call_actuals = [];
        def_type_formals = getFunctionOrConstructorArgumentTypes(def_type);
        for(int i <- index(resolver_formals_types)){
            if(i < resolver_arity_formal_types && unsetRec(def_type_formals[i]) != resolver_formals_types[i]){
                conds +=  atype2istype("$P<i>", def_type_formals[i], jg);
                call_actuals += "(<atype2javatype(def_type_formals[i])>) $P<i>";
            } else {
                call_actuals += "$P<i>";
            }
        }
       
       actuals_text = intercalate(", ", call_actuals);
       
       activeKwpFormals = [];
       if(def in relevant_fun_defs /*local_fun_defs*/){
          if(loc2muFunction[def.defined]?){
            fun = loc2muFunction[def.defined];
            activeKwpFormals += jg.collectKwpFormals(fun);
          } else if(defType(AType tp) := def.defInfo){
            activeKwpFormals += tp.kwFormals;
          }
        }
        if(hasKeywordParameters(def_type) || (def.scope notin module_scopes && !isEmpty(activeKwpFormals))){
            actuals_text = isEmpty(actuals_text) ? "$kwpActuals" : "<actuals_text>, $kwpActuals";
        }
       
       if(!onlyGlobalFuns){
            externalRefs = getExternalRefs(def, loc2muFunction);
            if(!isEmpty(externalRefs)){
                actuals_text += (isEmpty(actuals_text) ? "" : ", ") +  intercalate(", ", [ varName(var) | var <- externalRefs ]);
            }
       }
       key = isConstructorAType(def_type) ? 0 : (def_type.isConcreteArg ? def_type.concreteFingerprint : def_type.abstractFingerprint);
        //key = /*def_type.isDefault ? 0 :*/ (def_type.isConcreteArg ? def_type.concreteFingerprint : def_type.abstractFingerprint);
       
        call_code = base_call = "";
        cst = resolver_returns_void ? "" : "(<atype2javatype(resolver_return_type)>)";
        if(isFunctionAType(def_type)){
            if(isContainedIn(def.defined, module_scope)){
                pref = "";
                if(def.scope != module_scope){
                    for(odef <- fun_defs){
                        if(odef.defined == def.scope){
                            pref = "<asJavaName(odef.id, completeId=false)>_<odef.defined.begin.line>A<odef.defined.offset>_";
                        }
                    }
                }
                call_code = "<cst><pref><uniqueName>(<actuals_text>)";
            } else if(/*isContainedIn(def.defined, def.scope),*/ loc2module[def.scope]?){
                call_code = "<cst><module2field(loc2module[def.scope])>.<uniqueName>(<actuals_text>)"; // was uniqueName
            } else {  
               return; //do nothing
            }
            if(resolver_returns_void){
                base_call += "try { <call_code>; return; } catch (FailReturnFromVoidException e){};\n";
            } else {
                base_call += "$result = <call_code>;
                             'if($result != null) return $result;";
            }
        } else {
            if(hasKeywordParameters(def_type)){
                base_call = "return $VF.constructor(<jg.getATypeAccessor(def_type)><atype2idpart(def_type)>, new IValue[]{<actuals_text>}, $kwpActuals);";
            } else {
                base_call = "return $VF.constructor(<jg.getATypeAccessor(def_type)><atype2idpart(def_type)>, new IValue[]{<actuals_text>});";
            }
        }
        
        all_conds = intercalate(" && ", conds);
        if(key == 0){
            defaults_and_constructors += <all_conds, base_call>;
        } else {
            if(overload_table[key]?){
                overload_table[key] += <all_conds, base_call>;
            } else {
                overload_table[key] = [<all_conds, base_call>];
            }
        }
    }
    
    // Process all functions
    
    if(resolver_arity_formal_types == 0){
        for(def <- sorted_nondefault_fun_defs){
            handleDef(def);
        }
    } else {
        for(arg_type <- arg_types){
            for(def <- sorted_nondefault_fun_defs){
                if(unsetRec(getFunctionOrConstructorArgumentTypes(def.defInfo.atype), "alabel") == arg_type)
                    handleDef(def);
            }   
        }
        for(def <- sorted_default_fun_defs){
            handleDef(def);
        }
    }
    
    // Process all constructors
    
    for(cdef <- cons_defs){
        handleDef(cdef);
    }
    
    // Generate calls for one switch case
    
    str processOverloads(lrel[str conds, str call] overloads){
            calls = "";
            int i = 0;
            while(i < size(overloads)){
                <conds, call> = overloads[i];
                if(isEmpty(conds)){
                        calls += call;
                    i += 1;
                    continue;
                }
                // Share common conditions
                calls_with_same_cond = call;
                constructor_seen = contains(call, "$VF.constructor");
                
                while(i < size(overloads) -1 && conds == overloads[i+1].conds){
                    repeated_conses = constructor_seen && contains(overloads[i+1].call, "$VF.constructor");
                    i += 1;
                    if(repeated_conses){
                        calls_with_same_cond = "throw new RuntimeException(\"Constructor `<functionName>` is overloaded and can only be called with qualifier\");"; 
                    } else {
                        calls_with_same_cond += "\n<overloads[i].call>";
                    }
                }
                i += 1;
                if(!isEmpty(calls_with_same_cond)){
                    calls += "if(<conds>){
                             '  <calls_with_same_cond>
                             '}\n";
                }
            }
            return calls;
    }
        
    // Generate all cases
    
    if(resolver_arity_formal_types > 0){        
        switch_cases = "";
        noswitch = size(overload_table) == 1 || resolver_returns_void;
        
        for(key <- overload_table){
            calls = processOverloads(overload_table[key]);
            if(!isEmpty(calls)){                              
                switch_cases += noswitch ? "<calls>"
                                  : "\t<(key == 0) ? "\ndefault" : "\ncase <key>">:
                                    '\t\t<calls>\t\tbreak;";
            } 
        }
        default_and_constructor_cases = processOverloads(defaults_and_constructors);
    
        if(noswitch){
            body += switch_cases + default_and_constructor_cases;
        } else {
            if(!isEmpty(switch_cases)){
                body += "switch(Fingerprint.getFingerprint($P0)){
                        '<switch_cases>
                        '}\n";
           }
           body +=  default_and_constructor_cases;
        }
    } else {
        cases = "";
        for(key <- overload_table){
            calls = processOverloads(overload_table[key]);
            if(!isEmpty(calls)){ 
                cases += calls;
            }
        }
        default_and_constructor_cases = processOverloads(defaults_and_constructors);
        body += cases + default_and_constructor_cases;
    }
    
    if(isEmpty(cons_defs) || resolver_arity_formal_types > 0){
        body += "\nthrow RuntimeExceptionFactory.callFailed($VF.list(<intercalate(", ", ["$P<i>" | int i <- index(resolver_formals_types)/*, formal := resolver_formals_types[i]*/ ])>));";
    }
    resolvers = "public <atype2javatype(resolver_return_type)> <resolver_name>(<argTypes>){ // Generated by Resolver
                '   <body> 
                '}
                '";
   
    return resolvers;
}