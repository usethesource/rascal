@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
@bootstrapParser
module lang::rascalcore::compile::muRascal2Java::Interface

import lang::rascalcore::compile::muRascal::AST;

extend lang::rascalcore::check::CheckerCommon;

import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal2Java::Conversions;
import lang::rascalcore::compile::util::Names;

import List;
import Location;
////import ListRelation;
import Map;
import Set;
import String;

// Generate an interface for a Rascal module

str generateInterface(str moduleName, str packageName, list[MuFunction] functions, set[MODID] imports, set[MODID] extends, map[MODID,TModel] tmodels, PathConfig pcfg, JGenie jg){
    return "<if(!isEmpty(packageName)){>package <packageName>;<}>
           'import io.usethesource.vallang.*;
           'import org.rascalmpl.runtime.function.*;
           '
           '@SuppressWarnings(\"unused\")
           'public interface <asBaseInterfaceName(moduleName)>  {
           '    <generateInterfaceMethods(moduleName, functions, imports, extends, tmodels, pcfg, jg)>
           '}";
}

lrel[str, AType] getInterfaceSignature(str moduleName, list[MuFunction] functions, set[MODID]  _imports, set[MODID] extends, map[MODID,TModel] tmodels, PathConfig pcfg, JGenie jg){
    lrel[str, AType] result = [];
    rel[str, int, AType] signatures = {};
    // mscope = tmodels[moduleName].moduleLocs[moduleName];
    MODID moduleId = moduleName2moduleId(moduleName);
   
    
    for(f <- functions, isGlobalScope(f.scopeIn), jg.isContainedIn(f.funId, moduleId),
                                            !( //"test" in f.modifiers 
                                                isSyntheticFunctionName(f.name) 
                                             || isMainName(f.name)
                                             )
       ){
        signatures += <f.name, getArity(f.ftype), f.ftype>;
    }
    // iprintln(signatures);
    
    for(ext <- extends, ext in tmodels){
        // escope = tmodels[ext].moduleLocs[ext];   
        //escope = moduleName2moduleId(ext);  
        for(def <- tmodels[ext].defines, defType(AType tp) := def.defInfo, 
            def.idRole == functionId() ,//|| def.idRole == constructorId(),
            def.scope == ext,
            // getRascalModuleName(def.scope, pcfg) == ext,//def.scope == escope, //isContainedIn(def.defined, escope),
            !(tp has isTest && tp.isTest),
            !isNonTerminalAType(tp), !isLexicalAType(tp),
            !(isSyntheticFunctionName(def.id) || isMainName(def.id))){
            
            signatures += <def.id, getArity(tp), tp>;
        }
    }
    //iprintln(signatures);

    names_and_arities = signatures<0,1>;
    for(<name, arity> <- names_and_arities){
        overloads = signatures[name, arity];
        overloads_with_kwparams = { tp | tp <- overloads, !isEmpty(tp.kwFormals) };
        overloads_without_kwparams = overloads - overloads_with_kwparams;
        if(!isEmpty(overloads_with_kwparams)){
            result += <name, lubList(toList(overloads_with_kwparams))>;
        }
        if(!isEmpty(overloads_without_kwparams)){
            result += <name, lubList(toList(overloads_without_kwparams))>;
        }
    }
    return sort(result);
}

str generateInterfaceMethods(str moduleName, list[MuFunction] functions, set[MODID] imports, set[MODID] extends, map[MODID,TModel] tmodels, PathConfig pcfg, JGenie jg){
    interface_signature = getInterfaceSignature(moduleName, functions, imports, extends, tmodels, pcfg, jg);
    methods = [generateInterfaceMethod(name, tp) | <name, tp> <- interface_signature];
    return intercalate("\n", methods);
}

// Generate an interface method per function

str generateInterfaceMethod(str fname, AType ftype){
    //println("generateInterfaceMethod: <fname>, <ftype>");
    method_name = asJavaName(fname);
    
    formals = getFormals(ftype);
    method_formals = intercalate(", ", ["IValue $<i>" | i <- index(formals)]);
    //method_formals = intercalate(", ", ["<atype2javatype(formals[i])> $<i>" | i <- index(formals)]);
    if(hasKeywordParameters(ftype)){
        kwpActuals = "java.util.Map\<java.lang.String,IValue\> $kwpActuals";
        method_formals = isEmpty(method_formals) ? kwpActuals : "<method_formals>, <kwpActuals>";
    }
    
    ret = getResult(ftype);
    
    return "<isVoidAType(ret) ? "void" : "IValue"> <method_name>(<method_formals>);";     
    //return "<atype2javatype(ret)> <method_name>(<method_formals>);";         
}