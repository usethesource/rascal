module lang::rascalcore::compile::muRascal::interpret::Eval

import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
import List;
import Set;
import String;
import Map;
import Node;
import IO;
import Type;
import util::Reflective;

import lang::rascalcore::compile::muRascal::interpret::RValue;
import lang::rascalcore::compile::muRascal::interpret::Env;
import lang::rascalcore::compile::muRascal::interpret::Writers;
import lang::rascalcore::compile::muRascal::interpret::Iterators;
import lang::rascalcore::compile::muRascal::interpret::Constructors;
import lang::rascalcore::compile::muRascal::interpret::Primitives;
import  lang::rascalcore::compile::muRascal::interpret::Visitors;

import lang::rascalcore::compile::muRascal::interpret::JavaGen;

data DoControl
     = doReturn(RValue v, Env env)
     | doFailReturn()
     | doBreak(str label, Env env)
     | doContinue(str label, Env env)
     | doFail(str label, Env env)
     | doLeave(str label, RValue val, Env env)
     | doSucceed(str label, Env env)
     | doInsert(RValue val, Env env)
     ;

bool debug = false;

// ---- eval ------------------------------------------------------------------

map[str, MuFunction] muFunctions = ();
lrel[str name, AType funType, str scope, list[str] ofunctions, list[str] oconstructors] overloadedFunctions = [];

// ---- muModule --------------------------------------------------------------

RValue eval(MuModule m){
    muFunctions = (f.qname : f | f <- m.functions);
    overloadedFunctions = m.overloaded_functions;
    main_fun = "";
    for(f <- m.functions){
        if(f.uqname == "main") {
            env = environment((), []);
            <result, env> = call(f, [], env);
            return result;
        }
    }
    throw "No function `main` found";
}

map[str,str] resolved2overloaded = ();

JCode trans(MuModule m){
    muFunctions = (f.qname : f | f <- m.functions);
    overloadedFunctions = m.overloaded_functions;
    jg = makeJGenie();
    <typestore, kwpDecls> = generateTypeStore(m.ADTs, m.constructors);
    resolvers = "";
    for(overload:<str name, AType funType, str oname, list[str] ofunctions, list[str] oconstructors> <- overloadedFunctions){
        if(size(ofunctions) == 1 && size(oconstructors) == 0 || size(ofunctions) == 0 && size(oconstructors) == 1){
            resolved2overloaded[oname] = oname;
        } else {
            resolved2overloaded[oname] = name;
            resolvers += genResolver(overload, jg);
        }
    }
  
    className = split("::", m.name)[-1];
    res =  "package <replaceAll(m.name, "::", ".")>;
    
           'import java.util.*;
           'import io.usethesource.vallang.*;
           'import io.usethesource.vallang.type.*;
           'import shared.Maps;
           'import shared.ValueFactoryFactory;
           '
           'class <className> {
           '    static IValueFactory $VF = ValueFactoryFactory.getInstance();
           '    <typestore>
           '    <for(var <- m.module_variables){>
           '    <trans(var, jg)><}>
           '
           '    <className>(){
           '        <kwpDecls>
           '        <for(exp <- m.initialization){>
           '            <trans(exp, jg)>;
           '        <}>
           '    }
           '    <resolvers>
           '    <for(f <- m.functions){>
           '    <trans(f, jg)>
           '    <}>
           '    <jg.getConstants()>
           '}";
      return removeEmptyLines(res);
}

str removeEmptyLines(str s){
    return visit(s) { case /^\n[ ]*\n/ => "\n" };
}

tuple[str,str] generateTypeStore(set[AType] ADTs, set[AType] constructors){
    adtDecls = "";
    for(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole) <- ADTs){
        adtDecls += "static Type <adtName> = $TF.abstractDataType($TS, \"<adtName>\");\n";
    }
    consDecls = "";
    kwpDecls = "";
    for(c: acons(AType adt, list[AType] fields, list[Keyword] kwFields) <- constructors){
        adt_cons = "<adt.adtName>_<c.label>";
        fieldDecls = [ "<atype2typestore(fld)>, \"<fld.label>\"" | fld <- fields ];
        consDecls += "static Type <adt_cons> = $TF.constructor($TS, <adt.adtName>, \"<c.label>\"<isEmpty(fieldDecls) ? "" : ", <intercalate(", ", fieldDecls)>">);\n";
        for(kwField <- kwFields){
            kwpDecls += "$TS.declareKeywordParameter(<adt_cons>,\"<kwField.fieldType.label>\", <atype2typestore(kwField.fieldType)>);\n";
        }
    }
    return <"static TypeFactory $TF = TypeFactory.getInstance();
            'static TypeStore $TS = new TypeStore();
            '<adtDecls>
            '<consDecls>
            '",
            kwpDecls>;
}


// ---- muModuleVar ----------------------------------------------------------

JCode trans(MuModuleVar var, JGenie jg){
       return "<atype2java(var.atype)> <var.name>;";
}

// ---- muFunction ------------------------------------------------------------

bool constantDefaults(lrel[str name, AType atype, MuExp defaultExp] kwpDefaults){
    return all(<str name, AType atype, MuExp defaultExp> <- kwpDefaults, muCon(_) := defaultExp);
}

JCode trans(MuFunction fun, JGenie jg){
    ftype = fun.ftype;
    qname = replaceAll(fun.qname, "::", "_");
    uncheckedWarning = "";
    if(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals) := ftype){
        returnType = atype2java(ftype.ret);
        argTypes = intercalate(", ", ["<atype2java(f)> <f.label>" | i <- index(ftype.formals), f := ftype.formals[i]]);
        kwpActuals = "Map\<String,?\> $kwpActuals";
        kwpDefaults = fun.kwpDefaults;
        constantKwpDefaults = "";
        nonConstantKwpDefaults = "";
        mapCode = "Maps.builder()<for(<str key, AType tp, MuExp defaultExp> <- kwpDefaults){>.key(\"<key>\").value(<trans(defaultExp,jg)>)<}>.build();\n";
        if(!isEmpty(kwFormals)){
            uncheckedWarning = "@SuppressWarnings(\"unchecked\")";
            argTypes = isEmpty(argTypes) ? kwpActuals : "<argTypes>, <kwpActuals>";
            if(constantDefaults(kwpDefaults)){
                kwpDefaultsName = "<qname>_$kwpDefaults";
                jg.setKwpDefaults(kwpDefaultsName);
                constantKwpDefaults = "final Map\<String,?\> <kwpDefaultsName> = <mapCode>";
             } else {
                jg.setKwpDefaults("$kwpDefaults");
                nonConstantKwpDefaults =  "Map\<String,?\> $kwpDefaults = <mapCode>";
             }   
        }
        return "<constantKwpDefaults>
               '<uncheckedWarning>
               '<returnType> <qname>(<argTypes>){
               '      <nonConstantKwpDefaults>
               '    <trans(fun.body, jg)>
               '}";
    } else
    if(acons(AType adt, list[AType] fields, list[Keyword] kwFields) := ftype){
        returnType = "IConstructor";
        argTypes = intercalate(", ", ["<atype2java(f)> <f.label>" | f <- ftype.fields]);
        kwpActuals = "Map\<String,?\> $kwpActuals";
        kwpDefaults = fun.kwpDefaults;
        constantKwpDefaults = "";
        nonConstantKwpDefaults = "";
        mapCode = "Maps.builder()<for(<str key, AType tp, MuExp defaultExp> <- kwpDefaults){>.key(\"<key>\").value(<trans(defaultExp,jg)>)<}>.build();\n";
        if(!isEmpty(kwFields)){
            uncheckedWarning = "@SuppressWarnings(\"unchecked\")";
            argTypes = isEmpty(argTypes) ? kwpActuals : "<argTypes>, <kwpActuals>";
            if(constantDefaults(kwpDefaults)){
                kwpDefaultsName = "<qname>_$kwpDefaults";
                jg.setKwpDefaults(kwpDefaultsName);
                constantKwpDefaults = "final Map\<String,?\> <kwpDefaultsName> = <mapCode>";
             } else {
                jg.setKwpDefaults("$kwpDefaults");
                nonConstantKwpDefaults =  "Map\<String,?\> $kwpDefaults = <mapCode>";
             }   
        }
        return "<constantKwpDefaults>
               '<uncheckedWarning>
               '<returnType> <qname>(<argTypes>){
               '     <nonConstantKwpDefaults>
               '     <trans(fun.body, jg)>
               '}";
    } else
        throw "trans MuFunction: <ftype>";
}

JCode makeCall(AType resolverFunType, str of, JGenie jg){
    muFun = muFunctions[of];
    funType = muFun.ftype;
    kwpActuals = "Map\<String,?\> $kwpActuals";
    if(any(int i <- index(funType.formals), funType.formals[i] != resolverFunType.formals[i])){
        conds = [];
        actuals = [];
        for(int i <- index(resolverFunType.formals)){
            if(unsetRec(funType.formals[i]) != resolverFunType.formals[i]){
                conds += "<resolverFunType.formals[i].label>$<i> instanceof <atype2java(funType.formals[i])>";
                actuals += "(<atype2java(funType.formals[i])>) <resolverFunType.formals[i].label>$<i>";
            } else {
                actuals += "<resolverFunType.formals[i].label>$<i>";
            }
        } 
        if(!isEmpty(funType.kwFormals)){
            actuals = isEmpty(actuals) ? [kwpActuals] : actuals + kwpActuals;
        }
        base_call = "res = <replaceAll(of, "::", "_")>(<intercalate(", ", actuals)>);
                    'if(res != null) return res;
                    '";
        if(isEmpty(conds)){
            return base_call;
        } else {
            return "if(<intercalate(" && ", conds)>){
                   '    <base_call>
                   '}
                   '";
        }
    } else {
        args = intercalate(", ", ["<f.label>$<i>" | i <- index(funType.formals), f := funType.formals[i]]);
        return "res = <replaceAll(of, "::", "_")>(<args>);
               'if(res != null) return res;
               '";
    }
}

JCode genResolver(tuple[str name, AType funType, str scope, list[str] ofunctions, list[str] oconstructors] overload, JGenie jg){
   funType = overload.funType;
   anyKwParams = any(ovl <- overload.ofunctions + overload.oconstructors, hasKeywordParameters(muFunctions[ovl].ftype));
   returnType = atype2java(funType.ret);
   argTypes = intercalate(", ", ["<atype2java(f)> <f.label>$<i>" | i <- index(funType.formals), f := funType.formals[i]]);
   if(anyKwParams){
        kwpActuals = "Map\<String,?\> $kwpActuals";
        argTypes = isEmpty(argTypes) ? kwpActuals : "<argTypes>, <kwpActuals>";
   }
   signature = "<returnType> <replaceAll(overload.name, "::", "_")>(<argTypes>)";
   calls = "<returnType> res = null;\n";
   for(of <-overload.ofunctions){
        calls += makeCall(funType, of, jg);
   }
   return "<signature>{
          '    <calls>
          '    throw new RuntimeException(\"Cannot resolve call to <overload.name>\");
          '}
          '";
}

Result call(MuFunction fun, list[RValue] actuals, Env env){
    stack = [undefined() | i <- [0..fun.nlocals+1]];
    for(int i <- index(actuals)) stack[i] = actuals[i];
    try {
        if(debug) println("call <fun.qname>, <stack>");
        <result, env1> = eval(fun.body, environment(env.moduleVars, <fun.qname, stack, ()> + env.frames));
        return <result, tail(env)>;
    } catch doReturn(RValue v, Env env1):
        return <v, environment(env1.moduleVars, tail(env1.frames))>;
}

JCode call(MuFunction fun, list[str] actuals, JGenie jg){
    return "<fun.qname>(<intercalate(", ", actuals)>);";
}

// Constants

// ---- muBool ----------------------------------------------------------------

Result eval(muBool(b), Env env)                                 // muRascal Boolean constant
    = <rvalue(b), env>; 
    
JCode trans(muBool(b), JGenie jg) = "<b>"; 

// ---- muInt -----------------------------------------------------------------      
                                      
Result eval(muInt(int n), Env env)                              // muRascal integer constant
    = <rvalue(n), env>; 

JCode trans(muInt(int n), JGenie jg) = "<n>";  

// ---- muCon -----------------------------------------------------------------                                       

Result eval(muCon(value v), Env env)                            // Rascal Constant: an arbitrary IValue
    = <rvalue(v), env>; 
    
JCode trans(muCon(value v), JGenie jg) = jg.shareConstant(v);
    
Result eval(muTypeCon(AType tp), Env env)                       // Type constant
    = <rtype(tp), env>;                                        
                    
          //| muFun1(str fuid)                                    // *muRascal* function constant: functions at the root
          //| muFun2(str fuid, str scopeIn)                       // *muRascal* function constant: nested functions and closures
          //
          //| muOFun(str fuid)                                    // *Rascal* function, i.e., overloaded function at the root
          //
          //| muConstr(str fuid)                                  // Constructor
          
// Variables

//// ---- muModuleVar -----------------------------------------------------------
//
//Result eval(var:muModuleVar(str name, AType atype), Env env)    // Rascal Variable: retrieve its value
//    = < getValue(var, env), env >; 
//
//JCode trans(var:muModuleVar(str name, AType atype), JGenie jg{
//    return name;
//}

// ---- muVar -----------------------------------------------------------------

Result eval(var:muVar(str name, str fuid, int pos), Env env)    // Rascal Variable: retrieve its value
    = < getValue(var, env), env >; 

JCode trans(var:muVar(str name, str fuid, int pos), JGenie cs){
   // if(cs.currentFunction() == fuid){
        return name;
    //} else {
    //    throw "not supported";
    //}
}

// ---- muLoc -----------------------------------------------------------------

Result eval(var:muLoc(str name, int pos), Env env)    // Rascal local Variable: retrieve its value
    = < getValue(var, env), env >; 
    
JCode trans(var:muLoc(str name, int pos), JGenie jg)
    = name;
    
// ---- muVarKwp --------------------------------------------------------------

Result eval(var:muVarKwp(str fuid, str name, AType atype), Env env)          // Rascal Keyword parameter
    = < getValue(var, env), env >;   

JCode trans(var:muVarKwp(str fuid, str name, AType atype),  JGenie jg)
    = getValue(var, jg);

// ---- muTmp -----------------------------------------------------------------

Result eval(var:muTmp(str name, str fuid) , Env env)            // Temporary variable introduced by front-end
    = < getValue(var, env), env >;

// ---- muTmpInt --------------------------------------------------------------
    
Result eval(var:muTmpInt(str name, str fuid) , Env env)         // Temporary int variable introduced by front-end
    = < getValue(var, env), env >;

// ---- muTmpWriter -----------------------------------------------------------

Result eval(var:muTmpWriter(str name, str fuid) , Env env)      // Temporary writer variable introduced by front-end
    = < getValue(var, env), env >;

// ---- muAssign --------------------------------------------------------------
    
Result eval(muAssign(MuExp var, MuExp exp), Env env){           // Assignment to kind of variable
    <v, env> = eval(exp, env);
    return assignValue(var, v, env);
}

JCode trans(muAssign(MuExp var, MuExp exp), JGenie jg){
    return assignValue(var, trans(exp, jg), jg);
}


// Call/Apply/return      

Result eval(muCall(MuExp fun, list[MuExp] largs), Env env){       //  Call a *muRascal function
    actuals = for(arg <- largs){
                <v, env> = eval(arg, env);
                append v;
            }
    if(muConstr(AType ctype) := fun){
        return <rvalue(makeConstructor(ctype, actuals)), env>;
    }
    if(muConstrCompanion(str fname) := fun){
        return call(muFunctions[fname], actuals, env);
    }
    if(muCon(str s) := fun){
        if(rvalue(map[str,value] kwmap) := actuals[-1]){
            return <rvalue(makeNode(s, [v | rvalue(v) <- actuals[0..-1]], keywordParameters = kwmap)), env>;
        }
        throw "mOCall3: kwmap, <actuals>";
    }
}

JCode trans(muCall(MuExp fun, list[MuExp] largs), JGenie jg){
    actuals = for(arg <- largs){
                append trans(arg, jg);
            }
    if(muConstr(AType ctype) := fun){
        return makeConstructor(ctype, actuals);
    }
    if(muConstrCompanion(str fname) := fun){
        return call(muFunctions[fname], actuals, jg);
    }
    if(muCon(str s) := fun){
        if(rvalue(map[str,value] kwmap) := actuals[-1]){
            return <rvalue(makeNode(s, [v | rvalue(v) <- actuals[0..-1]], keywordParameters = kwmap)), env>;
        }
        throw "mOCall3: kwmap, <actuals>";
    }
}

// ---- muOCall3 --------------------------------------------------------------

Result eval(muOCall3(MuExp fun, AType ftype, list[MuExp] largs, loc src), Env env){       // Call a declared *Rascal function
    actuals = for(arg <- largs){
                <v, env> = eval(arg, env);
                append v;
            }
    if(muOFun(str fname) := fun){
        for(<str name, AType funType, str oname, list[str] ofunctions, list[str] oconstructors> <- overloadedFunctions){
            if(fname == oname){
                for(of <- ofunctions){
                    try {
                        return call(muFunctions[of], actuals, env);
                    } catch doFailReturn(): /* try next alternative */;
                }
                throw "No applicable alternative for overloaded function <fname>";
            }
        }
        throw throw "No overloading alternatives found for <fname>";
    }
    if(muCon(str s) := fun){
        if(rvalue(map[str,value] kwmap) := actuals[-1]){
            return <rvalue(makeNode(s, [v | rvalue(v) <- actuals[0..-1]], keywordParameters = kwmap)), env>;
        }
        throw "mOCall3: kwmap, <actuals>";
    }
}

JCode trans(muOCall3(MuExp fun, AType ftype, list[MuExp] largs, loc src), JGenie jg){
    actuals = for(arg <- largs){
                append trans(arg, jg);
            }
    if(muOFun(str fname) := fun){;
        if(overloadedAType(overloads) := ftype){
            return "<resolved2overloaded[fname]>(<intercalate(", ", actuals)>)";
        } else {
                return "<replaceAll(fname, "::", "_")>(<intercalate(", ", actuals)>)";
        }
    }
    if(muCon(str s) := fun){
        if(rvalue(map[str,value] kwmap) := actuals[-1]){
            return <rvalue(makeNode(s, [v | rvalue(v) <- actuals[0..-1]], keywordParameters = kwmap)), env>;
            return "$VF.node((<intercalate(", ", actuals[0..-1])>, keywordParameters=<actuals[-1]>)";
        }
        throw "mOCall3: kwmap, <actuals>";
    }
    throw "muOCall3: <fun>";
}

//          | muOCall4(MuExp fun, AType types,                    // Call a dynamic *Rascal function
//                               list[MuExp] largs, loc src)

// ---- muCallPrim2 -----------------------------------------------------------

Result eval(muCallPrim2(str name, loc src), Env env){                // Call a Rascal primitive function (with empty list of arguments)
    return evalPrim(name, env);
}

JCode trans(muCallPrim2(str name, loc src), JGenie jg){
    return transPrim(name, [], src, jg);
}

// ---- muCallPrim3 -----------------------------------------------------------

Result eval(muCallPrim3(str name, list[MuExp] exps, loc src), Env env){     // Call a Rascal primitive function
    actuals = for(exp <- exps){
                <result, env> = eval(exp, env);
                append result;
            }
    return evalPrim(name, actuals, env);
}

JCode trans(muCallPrim3(str name, list[MuExp] exps, loc src), JGenie jg){
    actuals = for(exp <- exps){
                append trans(exp, jg);
              }
    return transPrim(name, actuals, jg);
}
 
Result eval(muCallMuPrim(str name, list[MuExp] exps) , Env env){           // Call a muRascal primitive function
    actuals = for(exp <- exps){
                <result, env> = eval(exp, env);
                append result;
            }
    if(debug) println("muCallMuPrim: <name>, <actuals>");
    return evalMuPrim(name, actuals, env);
}
 
//          | muCallJava(str name, str class, 
//                       AType parameterTypes,
//                       AType keywordTypes,
//                       int reflect,
//                       list[MuExp] largs)                       // Call a Java method in given class


// ---- muReturn0 -------------------------------------------------------------

Result eval(muReturn0(), Env env){                                 // Return from a function without value
    throw doReturn(undefined(), env);
}

JCode trans(muReturn0(), JGenie jg){
    return "return;";
}
    
// ---- muReturn1 -------------------------------------------------------------

Result eval(muReturn1(MuExp exp), Env env){                       // Return from a function with value
    <result, env1> = eval(exp, env);
    throw doReturn(result, env1);
}

JCode trans(muReturn1(MuExp exp), JGenie jg){
    return "return <trans(exp, jg)>;";
}

          
//          | muFilterReturn()                                    // Return for filer statement

// ---- muKwpDefaults ---------------------------------------------------------

//Result eval(muKwpDefaults(lrel[str name, AType tp, MuExp defaultExp] kwpMap), Env env){
//   resMap = ();
//   for(<str name, AType tp, MuExp defaultExp> <- kwpMap){
//        <v, env> = eval(defaultExp, env);
//        resMap[name] = <tp, v>;
//   }
//   return <rvalue(resMap), env>;
//}
//
//JCode trans(muKwpDefaults(lrel[str name, AType tp, MuExp defaultExp] kwpMap), JGenie jg){
//    if(isEmpty(kwpMap)) return "Collections.emptyMap()";
//    return "Map\<String,IValue\> $kwpDefaults = Maps.builder()<for(<str key, AType tp, MuExp defaultExp> <- kwpMap){>.key(\"<key>\").value(<trans(defaultExp,jg)>)<}>.build();\n";
//}

// ---- muKwpActuals ----------------------------------------------------------

Result eval(muKwpActuals(lrel[str name, MuExp exp] kwpActuals), Env env){
    resMap = ();
    for(<str name, MuExp exp> <- kwpActuals){
        <v, env> = eval(exp, env);
        resMap[name] = v;
    }
    return <rvalue(resMap), env>;
}


JCode trans(muKwpActuals(lrel[str name, AType atype, MuExp exp] kwpActuals), JGenie jg){
    if(isEmpty(kwpActuals)) return "Collections.emptyMap()";
    return "Maps.builder()<for(<str key,  MuExp exp> <- kwpActuals){>.key(\"<key>\").value((IValue)<trans(exp,jg)>)<}>.build()";
}

// ---- muKwpMap --------------------------------------------------------------

Result eval(muKwpMap(MuExp actualKWs, lrel[str kwName, AType atype,  MuExp defaultExp] kwpDefaults), Env env){
    <rvalue(vkwmap), env> = eval(actualKWs, env);
    if(map[str,value] actualKwpMap := vkwmap){
        compleleKwpMap = ();
        for(int i <- index(kwpDefaults)){
            <kwpField, kwpDefaultExp> = kwpDefaults[i];
            if(actualKwpMap[kwpField]?){
                compleleKwpMap[kwpField] = actualKwpMap[kwpField];
            } else {
                <rvalue(v), env> = eval(kwpDefaultExp, env);
                compleleKwpMap[kwpField] = v;
            }
        }
        return <rvalue(compleleKwpMap), env>;
    }
    throw "muKwpMap: wrong type for kwmap: <kwmap>";
}

JCode trans(muKwpMap(MuExp actualKWs, lrel[str kwName, AType atype, MuExp defaultExp] kwpDefaults), JGenie jg){
    kwpDefaultsVar = jg.getKwpDefaults();
    kwpActuals = trans(actualKWs, jg);
   //if(isEmpty(actualKWs)) return kwpDefaults;
    return "<kwpActuals>.isEmpty() ? (Map\<String,IValue\>)<kwpDefaultsVar> : Maps.builder()<for(<str key,  AType atype, MuExp exp> <- kwpDefaults){>.key(\"<key>\").value(<kwpActuals>.containsKey(\"<key>\") ? (IValue)<kwpActuals>.get(\"<key>\") : (IValue)<trans(exp,jg)>)<}>.build()";

}

Result eval(muInsert(MuExp exp), Env env){                      // Insert statement   
    <v, env> = eval(exp, env);
    throw doInsert(v, env);
}     

// Assignment, If and While
                                                             
Result eval(muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart), Env env){// If-then-else expression
    <b, env> = eval(cond, env);
    return rvalue(true) := b ? eval(thenPart, env) : eval(elsePart, env);
}

JCode trans(muIfelse(str btscope, MuExp cond, MuExp thenPart, MuExp elsePart), JGenie jg){
    return "<trans(cond, jg)>.getValue() ? <trans(thenPart, jg)> : <trans(elsePart, jg)>";
}

Result eval(muIfelse(str flabel, MuExp cond, MuExp thenPart, MuExp elsePart), Env env){// If-then-else expression
    if(debug) println("muIfElse, <flabel>, <cond>");
    <b, env> = eval(cond, env);
    if(rvalue(true) := b){
        try {
            return eval(thenPart, env);
        } catch doFail(str flabel1, Env env1): {
            if(flabel != "" && flabel1 == flabel){
                return eval(elsePart, env1);
            } else {
                throw doFail(flabel1, env1);
            }
        }
    } else {
        return eval(elsePart, env);
    }
}

Result eval(muIf(MuExp cond,  MuExp thenPart), Env env){ // If-then expression
    <b, env> = eval(cond, env);
    return rvalue(true) := b ? eval(thenPart, env) : <rvalue(undefined()), env>;
}
                                
Result eval(mw: muWhile(str bclabel, MuExp cond, MuExp body), Env env){    // While-Do expression
    <b, env> = eval(cond, env);
    while(rvalue(true) := b) {
        try {
            <v, env> = eval(body, env);
            <b, env> = eval(cond, env);
       } catch doBreak(bclabel, env1): return <undefined(), env1>;
         catch doContinue(bclabel, env1): return eval(mw, env1);
    }
    return <undefined(), env>;
}

Result eval(mw: muForAny(str btscope, MuExp var, MuExp iterable, MuExp body), Env env){    // for-each expression
    <rvalue(iterable_val), env> = eval(iterable, env);
    the_iterator = makeIterator(iterable_val);
    bresult = false;
    while(the_iterator.hasNext()){
        //result = rvalue(true);
        v = the_iterator.getNext();
        <v, env> = assignValue(var, v, env);
        try {
            //<v, env> = eval(body, env);
            <rvalue(vb), env> = eval(body, env);
            if(bool b := vb) bresult = bresult || b;
       } catch doBreak(btscope, env1): return <result, env1>;
         catch doContinue(btscope, env1): ;
         catch doFail(btscope, env1): {
            if(debug) println("muForEach: doFail");
            env = env1;
         } catch doSucceed(btscope, env1): {
            bresult = true;
            env = env1;
         }
    }
    return <bresult ? rvalue(true) : rvalue(false), env>;
    //return <undefined(), env>;
}

Result eval(mw: muForAll(str btscope, MuExp var, MuExp iterable, MuExp body), Env env){    // for-each expression
    <rvalue(iterable_val), env> = eval(iterable, env);
    the_iterator = makeIterator(iterable_val);
    bresult = true;
    while(the_iterator.hasNext()){
        //result = rvalue(true);
        v = the_iterator.getNext();
        <v, env> = assignValue(var, v, env);
        println("forAll: <var> = <v>");
        try {
            //<v, env> = eval(body, env);
            <rvalue(vb), env> = eval(body, env);
            println("body: <vb> for <v>");
            if(bool b := vb) bresult = bresult && b;
       } catch doBreak(btscope, env1): return <result, env1>;
         catch doContinue(btscope, env1): ;
         catch doFail(btscope, env1): {
            if(debug) println("muForAll: doFail");
            bresult = false;
            env = env1;
         } catch doSucceed(btscope, env1): {
            env = env1;
         }
    }
    println("forALL: <bresult>");
    return <bresult ? rvalue(true) : rvalue(false), env>;
   // throw doLeave(btscope,  bresult ? rvalue(true) : rvalue(false), env);
    //return <undefined(), env>;
}

Result eval(muEnter(btscope, MuExp exp), Env env){
    if (debug) println("muEnter, <btscope>, <exp>");
    try {
        return eval(exp, env);
    } catch doLeave(btscope, RValue r, Env env1): {
        if (debug) println("muEnter: doLeave, <btscope>, <r>");
        return <r, env1>;
      } catch doFail(btscope, env1): {
        if (debug) println("muEnter: doFail, <btscope>");
        return <rvalue(false), env1>;
      } catch doSucceed(btscope, env1):
        return <rvalue(true), env1>;
}

Result eval(muLeave(btscope, MuExp exp),Env env){
    <v, env> = eval(exp, env);
    throw doLeave(btscope, v, env);
}

Result eval(mw: muFor(str label, MuExp var, MuExp fromExp, MuExp byExp, MuExp toExp, list[MuExp] body), Env env){    // for  expression
    <rvalue(from), env> = eval(fromExp, env);
    <rvalue(by), env> = eval(byExp, env);
    <rvalue(to), env> = eval(toExp, env);
    result = undefined();
    while(i < to){
        <v, env> = assignValue(var, rvalue(i), env);
        try {
            <result, env> = eval(body, env);
            i += by;
       } catch doBreak(label): return <undefined(), env>;
         catch doContinue(label): return eval(mw, env);
         catch doFail(label): ;
    }
    return <result, env>;
}

Result eval(muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp exp), Env env){
    <rvalue(vfirst), env> = eval(first, env);
    <rvalue(vsecond), env> = eval(second, env);
    <rvalue(vlast), env> = eval(last, env);
    result = undefined();
    if(num nfirst := vfirst, num nsecond := vsecond, num nlast := vlast){
        if(nsecond == 0){
            nsecond = nfirst < nlast ? nfirst + 1 : nfirst - 1;
        }
        for(x <- [nfirst, nsecond .. nlast]){
            <v, env> = assignValue(var, rvalue(x), env);
            try {
                <result, env> = eval(exp, env);
            } catch doBreak(label): return <undefined(), env>;
              catch doContinue(label): return eval(mw, env);
              catch doFail(label): ;
        }
        return <result, env>;
    } else 
        throw "muForRange: <vfirst>, <vsecond>, <vlast>";
}
         
Result eval(muSwitch(MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint), Env env){      // switch over cases for specific value
    <rvalue(swval), env> = eval(exp, env);
    fp = getFingerprint(swval, useConcreteFingerprint);
    for(muCase(int fingerprint, MuExp exp) <- cases){
        if (debug) println("fp = <fp>; case <fingerprint>, <exp>");
        if(fingerprint == fp){
            if (debug) println("found");
            return eval(exp, env);
        }
    }
    return eval(defaultExp, env);
} 

Result eval(muVisit(MuExp exp, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor), Env env){      // switch over cases for specific value
    <rvalue(subject), env> = eval(exp, env);
     return traverse(subject, cases, defaultExp, vdescriptor, env);
} 

Result eval(muEndCase(), Env env){                                        // Marks the exit point of a case
    throw "muEndCase cannot be executed";
}

Result eval(muBreak(str label), Env env){                                  // Break statement
    throw doBreak(label, env);
}

Result eval(muContinue(str label), Env env){                              // Continue statement
    throw doContinue(labe, env);
}

Result eval(muSucceed(str label), Env env){
    throw doSucceed(label, env);
}

Result eval(muFail(str label), Env env){                                   // Fail statement
    if (debug) println("muFail: <label>");
    throw doFail(label, env);
}

// ---- muFailReturn ----------------------------------------------------------

Result eval(muFailReturn(), Env env){                                      // Failure from function body
    throw doFailReturn();
}

JCode trans(muFailReturn(),  JGenie jg)
    = "return null";
          
// Lists of expressions
         
Result eval(muBlock(list[MuExp] exps), Env env)                   // A list of expressions, only last value remains
    = eval(exps, env);
    
JCode trans(muBlock(list[MuExp] exps), JGenie jg){
    return "<for(exp <- exps){> <trans(exp, jg)><}>";
}
    
Result eval(muBlockWithTmps(lrel[str name, str fuid] tmps, lrel[str name, str fuid] tmpRefs, list[MuExp] exps), Env env) // A block with scoped temporary variables and temporaries used as reference
    = eval(exps, env); // TODO: implement fully

//          // Exceptions
//          
//          | muThrow(MuExp exp, loc src)
//          
//          // Exception handling try/catch
//          
//          | muTry(MuExp exp, MuCatch \catch, MuExp \finally)
//          

Result eval(list[MuExp] exps, env){ 
    result = undefined();
    for(exp <- exps){
        if(debug) println("eval: <exp>");
        <result, env> = eval(exp, env);
    }
    return <result, env>;
}

//  

// ---- muCheckArgTypeAndCopy -------------------------------------------------

Result eval(muCheckArgTypeAndCopy(str name, int from, AType tp, int to), Env env){
    arg = env.frames[0].stack[from];
    if(getType(arg) != tp) return <rvalue(false), env>;
    env.frames[0].stack[to] = arg;
    return <rvalue(true), env>;
} 

JCode trans(muCheckArgTypeAndCopy(str name, int from, AType tp, int to), JGenie jg)
    = "";
    //= "<atype2java(tp)> <name>;
    //  'if(<name>$<from>.getType().isSubtypeOf(<atype2typestore(tp)>)).getValue()){
    //  '   <name> = <name>$<from>;
    //  '} else {
    //  '   return null;
    //  '}";     

Result eval(muEqual(MuExp exp1, MuExp exp2), Env env){
    <rvalue(v1), env> = eval(exp1, env);
    <rvalue(v2), env> = eval(exp2, env);
    return <v1 == v2 ? rvalue(true) : rvalue(false), env>;
 }
    
Result eval(muHasType(str typeName, MuExp exp), Env env){
    <rvalue(v), env> = eval(exp, env);
    return <getName(typeof(v)) == typeName ? rvalue(true) : rvalue(false), env>;
}

Result eval(muValueIsSubType(MuExp exp, AType tp), Env env){
    <rvalue(v), env> = eval(exp, env);
    return <subtype(typeOf(v), atype2symbol(tp)) ? rvalue(true) : rvalue(false), env>;
}

Result eval(muValueIsSubTypeOfValue(MuExp exp1, MuExp exp2), Env env){
    <rvalue(v1), env> = eval(exp1, env);
    <rvalue(v2), env> = eval(exp2, env);
    return <subtype(typeOf(v1), typeOf(v2)) ? rvalue(true) : rvalue(false), env>;
}

Result eval(muHasTypeAndArity(str typeName, int arity, MuExp exp), Env env){
    <rvalue(v), env> = eval(exp, env);
    if(getName(typeOf(v)) != typeName) return <rvalue(false), env>;
    return <getSize(v) == arity ? rvalue(true) : rvalue(false), env>;
}

Result eval(muHasNameAndArity(str name, int arity, MuExp exp), Env env){
    <rvalue(v), env> = eval(exp, env);
    if(constructor(AType ctype, list[value] fields, map[str,value] kwparams) := v){
        if(ctype.label != name) return <rvalue(false), env>;
        return <size(fields) == arity ? rvalue(true) : rvalue(false), env>;
    }
    if(node nd := v){
        if(getName(nd) != name) return <rvalue(false), env>;
        return <getSize(nd) == arity ? rvalue(true) : rvalue(false), env>;
    }
    throw "muHasNameAndArity: <name>, <arity>, <v>";
}

int getSize(value v){
    int n = 0;
    switch(v){
        case list[&T] lst: n = size(lst);
        case set[&T] st: n = size(st);
        case map[&K,&V] mp: n = size(mp);
        case constructor(AType ctype, list[value] fields, map[str,value] kwparams): n = size(fields);
        case node nd: n = arity(nd);
        case tuple[&A] tup: n = 1;
        case tuple[&A,&B] tup: n = 2;
        case tuple[&A,&B,&C] tup: n = 3;
        case tuple[&A,&B,&C,&D] tup: n = 4;
        case tuple[&A,&B,&C,&D,&E] tup: n = 5;
        case tuple[&A,&B,&C,&D,&E,&F] tup: n = 6;
        case tuple[&A,&B,&C,&D,&E,&F,&G] tup: n = 7;
        case tuple[&A,&B,&C,&D,&E,&F,&G,&H] tup: n = 8;
        case tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I] tup: n = 9;
        case tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] tup: n = 10;
        default: throw "muHasTypeAndArity: <typeName>, <arity>, <v>";    
    }
    return n;
}

Result eval(muSize(MuExp exp), Env env){
    <rvalue(v), env> = eval(exp, env);    
    return <rvalue(getSize(v)), env>;
}

Result eval(muSubscript(MuExp exp, MuExp idx), Env env){
    <rvalue(v), env> = eval(exp, env);
    <rvalue(vidx), env> = eval(idx, env);
    if(int n := vidx){
       switch(v){
            case list[&T] lst: return <rvalue(lst[n]), env>;
            case constructor(AType ctype, list[value] fields, map[str,value] kwparams): return <rvalue(fields[n]), env>;
            case node nd: return <rvalue(getChildren(nd)[n]), env>;       
            case tuple[&A] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D,&E] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D,&E,&F] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D,&E,&F,&G] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D,&E,&F,&G,&H] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] tup:return <rvalue(tup[n]), env>;
            default: throw "muSubscript: <v>, <n>";    
        }
    }
    throw "muSubscript: non-integer index <vidx>";    
}

Result eval(muIncVar(MuExp var, MuExp exp), Env env){
    <rvalue(v), env> = eval(exp, env);
    vtmp = getValue(var, env);
    if(rvalue(int n) := vtmp, int inc := v){
        return assignValue(var, rvalue(n + inc), env);
    }
    throw "muInc: <tmpName>, <vtmp>, <v>";
}

Result eval(muSubInt(MuExp exp1, MuExp exp2), Env env){
    <rvalue(v1), env> = eval(exp1, env);
    <rvalue(v2), env> = eval(exp2, env);
    if(int n1 := v1, int n2 := v2){
        return <rvalue(n1 - n2), env>;
    }
    throw "muSub: <v1>, <v2>";
}

Result eval(muAddInt(MuExp exp1, MuExp exp2), Env env){
    <rvalue(v1), env> = eval(exp1, env);
    <rvalue(v2), env> = eval(exp2, env);
    if(int n1 := v1, int n2 := v2){
        return <rvalue(n1 + n2), env>;
    }
    throw "muAdd: <v1>, <v2>";
}

Result eval(muGreaterEq(MuExp exp1, MuExp exp2), Env env){
    <rvalue(v1), env> = eval(exp1, env);
    <rvalue(v2), env> = eval(exp2, env);
    if(int n1 := v1, int n2 := v2){
        return <rvalue(n1 >= n2), env>;
    }
    throw "muGreaterEq: <v1>, <v2>";
}

Result eval(muAnd(MuExp exp1, MuExp exp2), Env env){
    <rvalue(v1), env> = eval(exp1, env);
    <rvalue(v2), env> = eval(exp2, env);
    if(bool b1 := v1, bool b2 := v2){
        return <rvalue(b1 && b2), env>;
    }
    throw "muAnd: <v1>, <v2>";
}

Result eval(muSubList(MuExp lst, MuExp from, MuExp len), Env env){
    <rvalue(vlst), env> = eval(lst, env);
    <rvalue(vfrom), env> = eval(from, env);
    <rvalue(vlen), env> = eval(len, env);
    if(list[&T] lst := vlst, int from := vfrom, int len := vlen){
        return <rvalue(lst[from .. len+1]), env>;
    }
    throw "muSubList: <vlist>, <vfrom>, <vlen>";
}

Result eval(muPrintln(str s), Env env){
    println(s);
    return <undefined(), env>;
}

Result eval(muHasKwp(MuExp exp, str kwname), Env env){
    <rvalue(v), env> = eval(exp, env);
    if(constructor(AType ctype, list[value] fields, map[str,value] kwparams) := v){
        return <kwparams[kwname]? ? rvalue(true) : rvalue(false) , env>;
    } else
    if(node nd := v){
        if(map[str, RValue] kwmap := getChildren(nd)[-1]){
            return <kwmap[kwname]? ? rvalue(true) : rvalue(false) , env>;
        }
    }
    return <rvalue(false), env>;
}

Result eval(muGetKwp(MuExp exp, str kwname), Env env){
    <rvalue(v), env> = eval(exp, env);
    if(constructor(AType ctype, list[value] fields, map[str,value] kwparams) := v){
        return <kwparams[kwname], env>;
    } else
    if(node nd := v){
        if(map[str, value] kwmap := getChildren(nd)[-1]){
            return <rvalue(kwmap[kwname]), env>;
        }
    }
    throw "muGetKwpArg: <v>";
}

Result eval(muHasKwpWithValue(MuExp exp, str kwname, MuExp req), Env env){
    <rvalue(v), env> = eval(exp, env);
    if(constructor(AType ctype, list[value] fields, map[str,value] kwparams) := v){
        if(kwparams[kwname]?){
            <rvalue(vres), env> = eval(req, env);
            return < kwparams[kwname] == vres ? rvalue(true) : rvalue(false), env >;
        } else {
            return <rvalue(false) , env>;
        }
    }
    if(node nd := v){
        if(map[str, value] kwmap := getChildren(nd)[-1]){
           if(kwmap[kwname]?){
                <rvalue(vres), env> = eval(req, env);
                return < kwmap[kwname] == vres ? rvalue(true) : rvalue(false), env >;
           } else {
            return < rvalue(false) , env>;
           }   
        }
    }
    return <rvalue(false), env>;
}