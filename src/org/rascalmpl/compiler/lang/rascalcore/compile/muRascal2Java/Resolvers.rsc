module lang::rascalcore::compile::muRascal2Java::Resolvers

import lang::rascalcore::compile::muRascal::AST;

extend lang::rascalcore::check::CheckerCommon;

import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal2Java::Conversions;
import lang::rascalcore::compile::util::Names;

//import lang::rascalcore::compile::muRascal2Java::SameJavaType;

//import IO;
import List;
import Location;
import ListRelation;
import Map;
import Node;
import Relation;
import Set;
import String;
import util::Math;

alias Name_Arity = tuple[str name, int arity];

// Get all functions and constructors from a given tmodel

rel[Name_Arity, Define] getFunctionsAndConstructors(TModel tmodel, set[loc] module_and_extend_scopes){
     return {<<def.id, size(tp has formals ? tp.formals : tp.fields)>, def> 
           | Define def <- tmodel.defines, 
             defType(AType tp) := def.defInfo,
             (def.idRole == functionId() && any(me_scope <- module_and_extend_scopes, isContainedIn(def.defined, me_scope))) || def.idRole == constructorId(),
             !(acons(AType adt, list[AType] _, list[Keyword] _) := tp && isNonTerminalType(adt))
           };
}


    
str varName(muVar(str name, str _fuid, int pos, AType _)){ // duplicate, see CodeGen
    return (name[0] != "$") ? "<getJavaName(name)><(pos >= 0 || name == "_") ? "_<abs(pos)>" : "">" : getJavaName(name);
} 

/*****************************************************************************/
/*  Convert an AType to a test for that AType (represented as VType)         */
/*****************************************************************************/

//str atype2istype(str e, AType t, JGenie jg) = "<e>.getType().comparable(<jg.shareType(t)>)";

str atype2istype(str e, overloadedAType(rel[loc, IdRole, AType] overloads), JGenie jg)
    = "<e> instanceof IConstructor && <intercalate(" || ", ["((IConstructor)<e>).getConstructorType().equivalent(<atype2vtype(tp, jg)>)" | <_, _, tp> <- overloads])>";
 
//str atype2istype(str e, t:acons(AType adt, list[AType] fields, list[Keyword] kwFields), JGenie jg) 
//    = "<e>.getConstructorType().comparable(<jg.shareType(t)>)";

str atype2istype(str e, aadt(str adtName, list[AType] parameters, contextFreeSyntax()), JGenie jg) {
    return "<e>.getType() instanceof NonTerminalType && ((NonTerminalType) <e>.getType()).getSymbol().equals(<jg.shareConstant(sort(adtName))>)";
}

str atype2istype(str e, aadt(str adtName, list[AType] parameters, lexicalSyntax()), JGenie jg) {
    return "<e>.getType() instanceof NonTerminalType && ((NonTerminalType) <e>.getType()).getSymbol().equals(<jg.shareConstant(lex(adtName))>)";
}

str atype2istype(str e, aadt(str adtName, list[AType] parameters, keywordSyntax()), JGenie jg) {
    return "<e>.getType() instanceof NonTerminalType && ((NonTerminalType) <e>.getType()).getSymbol().equals(<jg.shareConstant(keywords(adtName))>)";
}

default str atype2istype(str e, AType t, JGenie jg) {
    res = "$isComparable(<e>.getType(),<jg.shareType(t)>)";
    return res;
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
    module_scope = module2loc[moduleName];
   
    loc2module = invertUnique(module2loc);
    module_scopes = domain(loc2module);
    extend_scopes = { module2loc[ext] | ext <- extends };
    import_scopes = { module2loc[imp] | imp <- imports };
    
    module_and_extend_scopes = module_scope + /*import_scopes + */extend_scopes;
    
    rel[Name_Arity, Define] functions_and_constructors = { *getFunctionsAndConstructors(tmodels[mname], module_and_extend_scopes) | mname <- tmodels };
                          
    resolvers = "";
    for(<fname, farity> <- domain(functions_and_constructors), !isClosureName(fname)){
        // Group all functions of same name and arity by scope
        set[Define] defs = functions_and_constructors[<fname, farity>];
        
        //resolvers += generateResolver(moduleName, fname, defs, loc2muFunction, module_scope, import_scopes, extend_scopes, tmodels[moduleName].paths, loc2module, jg);
        defs_in_disjoint_scopes = mygroup(defs, bool(Define a, Define b) { 
                                                    return a.scope notin module_scopes && b.scope notin module_scopes && a.scope == b.scope 
                                                           || a.scope in module_scopes && b.scope in module_scopes
                                                           ;
                                              });
        // ... and generate a resolver for each group
        for(sdefs <- defs_in_disjoint_scopes){
            resolvers += generateResolver(moduleName, fname, sdefs, loc2muFunction, module_scope, import_scopes, extend_scopes, tmodels[moduleName].paths, loc2module, jg);
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

// Generate a resolver for a specific function

str generateResolver(str _moduleName, str functionName, set[Define] fun_defs, map[loc, MuFunction] loc2muFunction, loc module_scope, set[loc] import_scopes, set[loc] extend_scopes, Paths paths, map[loc, str] loc2module, JGenie jg){
    module_scopes = domain(loc2module);
    
    set[Define] local_fun_defs = {def | def <- fun_defs, /**/isContainedIn(def.defined, module_scope)};
    
    nonlocal_fun_defs0 = 
        for(def <- fun_defs){
            if(!isEmpty(extend_scopes) && any(ext <- extend_scopes, isContainedIn(def.defined, ext))) append def;
            if(!isEmpty(import_scopes) && any(imp <- import_scopes, isContainedIn(def.defined, imp))) append def;
        };
    nonlocal_fun_defs = toSet(nonlocal_fun_defs0);                     
    set[Define] relevant_fun_defs = local_fun_defs + nonlocal_fun_defs;
    cons_defs = { cdef | cdef <- relevant_fun_defs, defType(AType tp) := cdef.defInfo, isConstructorType(tp) };
    
    if(isEmpty(relevant_fun_defs)) return "";
    
    relevant_fun_defs -= cons_defs;
  
    acons_adt = avoid();
    acons_fields = [];
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
    
    if(!isFunctionType(resolver_fun_type)) return "";
    
    inner_scope = "";
  
   
    if(all(def <- relevant_fun_defs, def in local_fun_defs, def.scope notin module_scopes)){
        for(def <- relevant_fun_defs, isContainedIn(def.defined, module_scope)){
           if(loc2muFunction[def.defined]?){
                fun = loc2muFunction[def.defined];
                inner_scope = "<fun.scopeIn>_";
           } else {
                println("<def.defined> not found");
            }
            break;
        }
        //def = getOneFrom(relevant_fun_defs);
        //fun = loc2muFunction[def.defined];
        //inner_scope = "<fun.scopeIn>_";
    }
    resolverName = "<inner_scope><getJavaName(functionName)>";
    
    fun_kwFormals = acons_kwfields;
    for(def <- relevant_fun_defs /*local_fun_defs*/, def notin cons_defs, defType(AType tp) := def.defInfo){
        if(loc2muFunction[def.defined]?){
            fun = loc2muFunction[def.defined];
            fun_kwFormals += jg.collectKwpFormals(fun);
        } else {
            fun_kwFormals += tp.kwFormals;
        }
    }
    resolver_fun_type = resolver_fun_type[kwFormals=fun_kwFormals];
    
    resolverFormalsTypes = unsetRec(resolver_fun_type has formals ? resolver_fun_type.formals : resolver_fun_type.fields);
    resolverFormalsTypes = [ avalue() | _ <- resolverFormalsTypes ];
    arityFormalTypes = size(resolverFormalsTypes);
    returnType = resolver_fun_type has ret ? resolver_fun_type.ret : resolver_fun_type.adt;
    if(arityFormalTypes == 0 && size(relevant_fun_defs + cons_defs) > 1
       || size(cons_defs) > 1 &&  (size(acons_fields) == 1 || all(i <- index(acons_fields), j <- index(acons_fields), i != j, comparable(acons_fields[i], acons_fields[j])))
      ){
        return
            "public <atype2javatype(returnType)> <resolverName>(){ // Generated by Resolver
            '  throw new RuntimeException(\"Constructor `<functionName>` is overloaded and can only be called with qualifier\"); 
            '}
            '";
    }

    returns_void = isVoidType(returnType);
    argTypes = intercalate(", ", ["<atype2javatype(f)> $<i>" | i <- index(resolverFormalsTypes), f := resolverFormalsTypes[i]]);
 
    actuals = intercalate(", ", ["$<i>" | i <- index(resolverFormalsTypes)]);
    body = returns_void ? "" : "<atype2javatype(returnType)> $result = null;\n";
    
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
            argTypes += (isEmpty(argTypes) ? "" : ", ") +  intercalate(", ", [ "ValueRef\<<jtype>\> <varName(var)>" | var <- externalRefs, jtype := atype2javatype(var.atype)]);
        }
    }
 
    list[str] all_conds = [];
    list[str] all_calls = [];
    
    extends = {<f, t> | <f, extendPath(), t> <- paths }+;
    
    bool funBeforeExtendedBeforeDefaultBeforeConstructor(Define a, Define b){
        return    defType(AType ta) := a.defInfo 
               && defType(AType tb) := b.defInfo 
               && isFunctionType(ta) 
               && (isConstructorType(tb) 
                  || !ta.isDefault && tb.isDefault
                  || <a.scope, b.scope> in extends
                  );
    }

    for(def <- sort(relevant_fun_defs, funBeforeExtendedBeforeDefaultBeforeConstructor)){
        inner_scope = "";
        if(def.scope notin module_scopes, /*isContainedIn(def.scope, module_scope) */def in local_fun_defs){
            fun = loc2muFunction[def.defined];
            inner_scope = "<fun.scopeIn>_";
        }
        uniqueName = "<inner_scope><getJavaName(def.id, completeId=false)>_<def.defined.begin.line>A<def.defined.offset>";
        def_type = def.defInfo.atype;
        conds = [];
        call_actuals = [];
        for(int i <- index(resolverFormalsTypes)){
            if(i < arityFormalTypes && unsetRec(def_type.formals[i]) != resolverFormalsTypes[i]){
                conds +=  atype2istype("$<i>", def_type.formals[i], jg);
                call_actuals += "(<atype2javatype(def_type.formals[i])>) $<i>";
            } else {
                call_actuals += "$<i>";
            }
        }
       
       actuals_text = intercalate(", ", call_actuals);
       if(!onlyGlobalFuns){
            externalRefs = getExternalRefs(def, loc2muFunction);
            if(!isEmpty(externalRefs)){
                actuals_text += (isEmpty(actuals_text) ? "" : ", ") +  intercalate(", ", [ varName(var) | var <- externalRefs ]);
            }
       }
        
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

        
        call_code = base_call = "";
        if(isContainedIn(def.defined, module_scope)){
            pref = "";
            if(def.scope != module_scope){
                for(odef <- fun_defs){
                    if(odef.defined == def.scope){
                        pref = "<getJavaName(odef.id, completeId=false)>_<odef.defined.begin.line>A<odef.defined.offset>_";
                    }
                }
            }
            call_code = "<pref><uniqueName>(<actuals_text>)";
        } else if(isContainedIn(def.defined, def.scope), loc2module[def.scope]?){
            cst = returns_void ? "" : "(<atype2javatype(returnType)>)";
            call_code = "<cst><module2field(loc2module[def.scope])>.<resolverName>(<actuals_text>)"; // was uniqueName
        } else {
            continue;
        }
        if(returns_void){
            base_call += "try { <call_code>; return; } catch (FailReturnFromVoidException e){};\n";
        } else {
            base_call += "$result = <call_code>;
                         'if($result != null) return $result;
                         '";
        }
        all_conds += intercalate(" && ", conds);
        all_calls += base_call;
        //if(isEmpty(conds)){
        //    body += base_call;
        //} else {
        //    body += "if(<intercalate(" && ", conds)>){
        //            '    <base_call>}\n";
        //}
    }

    nconds = 0;
    for(cdef <- cons_defs){
        base_call = ""; 
        consType = cdef.defInfo.atype;
        conds = [];
        call_actuals = [];
        for(int i <- index(resolverFormalsTypes)){
            if(i < arityFormalTypes && unsetRec(consType.fields[i]) != resolverFormalsTypes[i]){
                conds +=  atype2istype("$<i>", consType.fields[i], jg);
                call_actuals += "(<atype2javatype(consType.fields[i])>) $<i>";
            } else {
                call_actuals += "$<i>";
            }
        }
   
        actuals_text = intercalate(", ", call_actuals);
       
        if(hasKeywordParameters(consType)){
            base_call = "return $VF.constructor(<jg.getATypeAccessor(consType)><atype2idpart(consType, jg)>, new IValue[]{<actuals_text>}, $kwpActuals);";
        } else {
            base_call = "return $VF.constructor(<jg.getATypeAccessor(consType)><atype2idpart(consType, jg)>, new IValue[]{<actuals_text>});";
        }
        all_conds += intercalate(" && ", conds);
        all_calls += base_call;
        if(!isEmpty(conds)) {
             nconds += 1;
        }
        //if(isEmpty(conds)){
        //    body += base_call;
        //} else {
        //    nconds += 1;
        //    body += "if(<intercalate(" && ", conds)>){
        //            '    <base_call>}\n";
        //}
    }
    
    if(size(all_conds) == 0) return "";
    
    int i = -1;
    while (i < size(all_conds) - 1){
        i += 1;
        conds = all_conds[i];
        calls = all_calls[i];
        while(i < size(all_conds) -1 && conds == all_conds[i+1]){
            calls += all_calls[i+1];
            i += 1;
        }
        if(isEmpty(conds)){
            body += calls;
        } else {
            body += "if(<conds>){
                    '    <calls>}\n";
        }
    }
    
    if(isEmpty(cons_defs) || size(cons_defs) == nconds){
        body += "throw RuntimeExceptionFactory.callFailed($VF.list(<intercalate(", ", ["$<i>" | int i <- index(resolverFormalsTypes)/*, formal := resolverFormalsTypes[i]*/ ])>));";
    }
    resolvers = "public <atype2javatype(returnType)> <resolverName>(<argTypes>){ // Generated by Resolver
                '   <body> 
                '}
                '";
   
    return resolvers;
}