module lang::rascalcore::compile::muRascal2Java::Interface

import lang::rascalcore::compile::muRascal::AST;

extend lang::rascalcore::check::CheckerCommon;

import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal2Java::Conversions;
import lang::rascalcore::compile::util::Names;

import List;
import ListRelation;
import Map;
import Set;
import String;

// Generate an interface for a Rascal module

str generateInterface(str moduleName, str packageName, str className, list[MuFunction] functions, set[str] extends, map[str,TModel] tmodels, JGenie jg){
    return "<if(!isEmpty(packageName)){>package <packageName>;<}>
           'import io.usethesource.vallang.*;
           'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.*;
           '
           '@SuppressWarnings(\"unused\")
           'interface $<className>  {
           '    <generateInterfaceMethods(moduleName, functions, extends, tmodels)>
           '}";
}

lrel[str, AType] getInterfaceSignature(str moduleName, list[MuFunction] functions, set[str] extends, map[str,TModel] tmodels){
    result = [];
    signatures = {};
    for(f <- functions, isEmpty(f.scopeIn) , !("test" in f.modifiers || isSyntheticFunctionName(f.name) || isMainName(f.name))){
        signatures += <f.name, size(f.ftype.formals), f.ftype>;
    }
    
    for(ext <- extends){ // TODO also remove tests here
        for(def <- tmodels[ext].defines, defType(tp) := def.defInfo, isFunctionType(tp), !(isSyntheticFunctionName(def.id) || isMainName(def.id))){
            signatures += <def.id, size(tp.formals), tp>;
        }
    }
    names_and_arities = signatures<0,1>;
    for(<name, arity> <- names_and_arities){
        overloads = signatures[name, arity];
        result += <name, lubList(toList(overloads))>;
    }
    return sort(result);
}

str generateInterfaceMethods(str moduleName, list[MuFunction] functions, set[str] extends, map[str,TModel] tmodels){
    interface_signature = getInterfaceSignature(moduleName, functions, extends, tmodels);
    methods = [generateInterfaceMethod(name, tp) | <name, tp> <- interface_signature];
    return intercalate("\n", methods);
}

// Generate an interface method per function

str generateInterfaceMethod(str fname, AType ftype){
    method_name = getJavaName(fname);
    
    method_formals = intercalate(", ", ["<atype2javatype(ftype.formals[i])> $<i>" | i <- index(ftype.formals)]);
    if(hasKeywordParameters(ftype)){
        kwpActuals = "java.util.Map\<java.lang.String,IValue\> $kwpActuals";
        method_formals = isEmpty(method_formals) ? kwpActuals : "<method_formals>, <kwpActuals>";
    }
    
    return "<atype2javatype(ftype.ret)> <method_name>(<method_formals>);";         
}