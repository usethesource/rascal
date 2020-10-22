module lang::rascalcore::compile::muRascal2Java::Resolvers

import lang::rascalcore::compile::muRascal::AST;

extend lang::rascalcore::check::CheckerCommon;

import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal2Java::Conversions;
import lang::rascalcore::compile::util::Names;

import lang::rascalcore::compile::muRascal2Java::SameJavaType;

import IO;
import List;
import ListRelation;
import Map;
import Set;
import String;
import util::Math;

alias Name_Arity = tuple[str name, int arity];

rel[Name_Arity, Define] getFunctionsAndConstructors(TModel tmodel){
    return {<<def.id, size(tp has formals ? tp.formals : tp.fields)>, def> | def <- tmodel.defines, defType(tp) := def.defInfo, 
        def.idRole == functionId() || def.idRole == constructorId()};
    //isFunctionType(tp) || isConstructorType(tp)};
}

bool funBeforeDefaultBeforeConstructor(Define a, Define b){
    return defType(ta) := a.defInfo && defType(tb) := b.defInfo && 
                                       isFunctionType(ta) && (isConstructorType(tb) || !ta.isDefault && tb.isDefault);
}
    
bool sameInJava(list[AType] ts1, list[AType] ts2){
    return [atype2javatype(t1) | t1 <- ts1 ] == [atype2javatype(t2) | t2 <- ts2 ];
}
    
str varName(muVar(str name, str fuid, int pos, AType atype)){ // duplicate, see CodeGen
    return (name[0] != "$") ? "<getJavaName(name)><(pos >= 0 || name == "_") ? "_<abs(pos)>" : "">" : getJavaName(name);
} 

str generateResolvers(str moduleName, map[loc, MuFunction] loc2muFunction, set[str] imports, set[str] extends, map[str,TModel] tmodels, map[str,loc] module2loc){
    module_scope = module2loc[moduleName];
   
    loc2module = invertUnique(module2loc);
    extend_scopes = { module2loc[ext] | ext <- extends };
    import_scopes = { module2loc[imp] | imp <- imports };
    
    rel[Name_Arity, Define] functions_and_constructors = { *getFunctionsAndConstructors(tmodels[mname]) | mname <- tmodels };
                          
    resolvers = "";
    for(<fname, farity> <- domain(functions_and_constructors), !isMainName(fname), !isClosureName(fname)){
        set[Define] defs = functions_and_constructors[<fname, farity>];
        resolvers += generateResolver(moduleName, fname, defs, loc2muFunction, module_scope, import_scopes, extend_scopes, loc2module);
    }
    println(resolvers);
    return resolvers;
}

list[MuExp] getExternalVars(Define fun_def, map[loc, MuFunction] loc2muFunction){
    externalVars = loc2muFunction[fun_def.defined]? ? sort(loc2muFunction[fun_def.defined].externalVars) : [];
    return [ ev | ev <- externalVars, ev.pos >= 0 ];
}

list[MuExp] getExternalVars(set[Define] relevant_fun_defs, map[loc, MuFunction] loc2muFunction){
   return sort([ *getExternalVars(fun_def, loc2muFunction) | fun_def <- relevant_fun_defs]);
}

str generateResolver(str moduleName, str functionName, set[Define] fun_defs, map[loc, MuFunction] loc2muFunction, loc module_scope, set[loc] import_scopes, set[loc] extend_scopes, map[loc, str] loc2module){
    
    local_fun_defs = {def | def <- fun_defs, isContainedIn(def.defined, module_scope)};
    nonlocal_fun_defs = {def | def <- fun_defs, !isEmpty(extend_scopes) && any(imp <- extend_scopes, isContainedIn(def.defined, imp)) };                             
    relevant_fun_defs = local_fun_defs + nonlocal_fun_defs;
    cons_defs = { cdef | cdef <- relevant_fun_defs, defType(tp) := cdef.defInfo, isConstructorType(tp) };
    
    relevant_fun_defs -= cons_defs;
    
    if(isEmpty(relevant_fun_defs)) return "";
   
    resolver_fun_type = (avoid() | alub(it, tp) | fdef <- relevant_fun_defs, defType(tp) := fdef.defInfo);
    if(!isFunctionType(resolver_fun_type)) return "";
  
    resolverFormalsTypes = unsetRec(resolver_fun_type has formals ? resolver_fun_type.formals : resolver_fun_type.fields);
    arityFormalTypes = size(resolverFormalsTypes);
    returnType = resolver_fun_type has ret ? resolver_fun_type.ret : resolver_fun_type.adt;
    returns_void = isVoidType(returnType);
    argTypes = intercalate(", ", ["<atype2javatype(f)> $<i>" | i <- index(resolverFormalsTypes), f := resolverFormalsTypes[i]]);
 
    actuals = intercalate(", ", ["$<i>" | i <- index(resolverFormalsTypes)]);
    body = returns_void ? "" : "<atype2javatype(returnType)> $result = null;\n";
    
    kwpActuals = "java.util.Map\<java.lang.String,IValue\> $kwpActuals";
    if(hasKeywordParameters(resolver_fun_type)){
        argTypes = isEmpty(argTypes) ? kwpActuals : "<argTypes>, <kwpActuals>";
    }
   
    externalVars = getExternalVars(relevant_fun_defs, loc2muFunction);
    if(!isEmpty(externalVars)){
        argTypes += (isEmpty(argTypes) ? "" : ", ") +  intercalate(", ", [ "ValueRef\<<jtype>\> <varName(var)>" | var <- externalVars, jtype := atype2javatype(var.atype)]);
    }
          
    for(def <- sort(relevant_fun_defs, funBeforeDefaultBeforeConstructor)){
        uniqueName = "<getJavaName(def.id, completeId=false)>_<def.defined.begin.line>A<def.defined.offset>";
        def_type = def.defInfo.atype;
        conds = [];
        call_actuals = [];
        for(int i <- index(resolverFormalsTypes)){
            if(i < arityFormalTypes && unsetRec(def_type.formals[i]) != resolverFormalsTypes[i]){
                conds +=  atype2istype("$<i>", def_type.formals[i]);
                call_actuals += "(<atype2javatype(def_type.formals[i])>) $<i>";
            } else {
                call_actuals += "$<i>";
            }
        }
       
        actuals_text = intercalate(", ", call_actuals);
        if(hasKeywordParameters(def_type)){
            actuals_text = isEmpty(actuals_text) ? "$kwpActuals" : "<actuals_text>, $kwpActuals";
        }

        externalVars = getExternalVars(def, loc2muFunction);
        if(!isEmpty(externalVars)){
            actuals_text += (isEmpty(actuals_text) ? "" : ", ") +  intercalate(", ", [ varName(var) | var <- externalVars ]);
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
        } else if(isContainedIn(def.defined, def.scope)){
            call_code = "<module2field(loc2module[def.scope])>.<uniqueName>(<actuals_text>)";
        } else {
            println("HELP");
        }
        if(returns_void){
            base_call += "try { <call_code>; return; } catch (FailReturnFromVoidException e){};\n";
        } else {
            base_call += "$result = <call_code>;
                         'if($result != null) return $result;
                         '";
        }
        if(isEmpty(conds)){
            body += base_call;
        } else {
            body += "if(<intercalate(" && ", conds)>){
                    '    <base_call>}\n";
        }
    }
    switch(size(cons_defs)){
        case 0: body += "throw RuntimeExceptionFactory.callFailed($VF.list(<intercalate(", ", ["$<i>" | int i <- index(resolverFormalsTypes), formal := resolverFormalsTypes[i] ])>));";
        case 1: {
            consType = getFirstFrom(cons_defs).defInfo.atype;
            actuals = [ "$<i>" | i <- index(consType.fields) ];
            kwActuals = consType.kwFields;
            body += isEmpty(kwActuals) ? "return $VF.constructor(<atype2idpart(consType)>, new IValue[]{<intercalate(", ", actuals)>});"
                                       : "return $VF.constructor(<atype2idpart(consType)>, new IValue[]{<intercalate(", ", actuals)>}, $kwpActuals);";
        }
        default: {
            throw "Cannot handle more than one constructor";
        }
    }
    resolvers = "public <atype2javatype(returnType)> <getJavaName(functionName)>(<argTypes>){
                '   <body> 
                '}
                '";
    if(!isEmpty(local_fun_defs) && !isEmpty(nonlocal_fun_defs)){
        local_fun_type = (avoid() | alub(it, tp) | fdef <- local_fun_defs, defType(tp) := fdef.defInfo);
        localFormalsTypes = unsetRec(local_fun_type has formals ? local_fun_type.formals : local_fun_type.fields);
        if(unsetRec(localFormalsTypes) != resolverFormalsTypes, !sameInJava(localFormalsTypes, resolverFormalsTypes)){
            resolvers += generateResolver(moduleName, functionName, local_fun_defs, loc2muFunction, module_scope, import_scopes, extend_scopes, loc2module);
        }
    }
   
    return resolvers;
}