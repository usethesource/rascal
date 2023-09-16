@bootstrapParser
module lang::rascalcore::compile::muRascal2Java::Interface

import lang::rascalcore::compile::muRascal::AST;

extend lang::rascalcore::check::CheckerCommon;

import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal2Java::Conversions;
import lang::rascalcore::compile::util::Names;

import List;
import Location;
import ListRelation;
import Map;
import Set;
import String;

// Generate an interface for a Rascal module

str generateInterface(str moduleName, str packageName, list[MuFunction] functions, set[str] imports, set[str] extends, map[str,TModel] tmodels, JGenie _jg){
    return "<if(!isEmpty(packageName)){>package <packageName>;<}>
           'import io.usethesource.vallang.*;
           'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function.*;
           '
           '@SuppressWarnings(\"unused\")
           'public interface <asBaseInterfaceName(moduleName)>  {
           '    <generateInterfaceMethods(moduleName, functions, imports, extends, tmodels)>
           '}";
}

lrel[str, AType] getInterfaceSignature(str moduleName, list[MuFunction] functions, set[str]  _imports, set[str] extends, map[str,TModel] tmodels){
    
    result = [];
    rel[str, int, AType] signatures = {};
    mscope = tmodels[moduleName].moduleLocs[moduleName];
   
    
    for(f <- functions, isEmpty(f.scopeIn), isContainedIn(f.src, mscope),
                                            !( //"test" in f.modifiers 
                                                isSyntheticFunctionName(f.name) 
                                             || isMainName(f.name)
                                             )
       ){
        signatures += <f.name, getArity(f.ftype), f.ftype>;
    }
    //iprintln(signatures);
    
    for(ext <- extends){
        escope = tmodels[ext].moduleLocs[ext];
        for(def <- tmodels[ext].defines, defType(AType tp) := def.defInfo, 
            def.idRole == functionId() ,//|| def.idRole == constructorId(),
            def.scope == escope, //isContainedIn(def.defined, escope),
            !(tp has isTest && tp.isTest),
            !isNonTerminalAType(tp), !isLexicalType(tp),
            !(isSyntheticFunctionName(def.id) || isMainName(def.id))){
            
            signatures += <def.id, getArity(tp), tp>;
        }
    }
    //iprintln(signatures);

    names_and_arities = signatures<0,1>;
    for(<name, arity> <- names_and_arities){
        overloads = signatures[name, arity];
        result += <name, lubList(toList(overloads))>;
    }
    return sort(result);
}

str generateInterfaceMethods(str moduleName, list[MuFunction] functions, set[str] imports, set[str] extends, map[str,TModel] tmodels){
    interface_signature = getInterfaceSignature(moduleName, functions, imports, extends, tmodels);
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