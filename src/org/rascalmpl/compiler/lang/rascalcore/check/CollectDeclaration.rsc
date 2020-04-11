@bootstrapParser
module lang::rascalcore::check::CollectDeclaration

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeExceptions;
extend lang::rascalcore::check::ATypeInstantiation;
extend lang::rascalcore::check::ATypeUtils;
extend lang::rascalcore::check::CollectType;
extend lang::rascalcore::check::Fingerprint;

extend lang::rascalcore::check::CollectDataDeclaration;
extend lang::rascalcore::check::CollectSyntaxDeclaration;

import lang::rascalcore::check::BasicRascalConfig;

import lang::rascalcore::check::CollectVarArgs;
import lang::rascalcore::check::ComputeType;
import lang::rascalcore::check::NameUtils;
import lang::rascalcore::check::PathAnalysis;
import lang::rascalcore::check::ScopeInfo;
import lang::rascalcore::check::SyntaxGetters;

import lang::rascal::\syntax::Rascal;

import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Attributes;

import IO;
import List;
import Map;
import Node;
import Set;
import String;

import util::Maybe;
import util::Reflective;

// ---- Utilities -------------------------------------------------------------


// ---- Rascal declarations ---------------------------------------------------

void collect(Module current: (Module) `<Header header> <Body body>`, Collector c){
  
    if(current has top) current = current.top;
    mname = prettyPrintName(header.name);
    checkModuleName(getLoc(current), header.name, c);
    
    tagsMap = getTags(header.tags);
    
    //if(ignoreCompiler(tagsMap)) {println("*** ignore module <mname>"); return; }
    <deprecated, deprecationMessage> = getDeprecated(tagsMap);
    
    tmod = deprecated ? amodule(mname, deprecationMessage=deprecationMessage) : amodule(mname);
    if(deprecated){
        c.report(warning(current, "Deprecated module %v%v", mname, isEmpty(deprecationMessage) ? "" : ": <deprecationMessage>"));
    }
    c.define(mname, moduleId(), current, defType(tmod));
     
    c.push(key_current_module, mname);
    c.enterScope(current);
        collect(header, body, c);
    c.leaveScope(current);
    c.pop(key_current_module);
}

void checkModuleName(loc mloc, QualifiedName qualifiedModuleName, Collector c){
    pcfgVal = c.getStack(key_pathconfig);
    if([PathConfig pcfg] := pcfgVal){ 
        mname = prettyPrintName(qualifiedModuleName);
        try {   
            mloc1 = getModuleLocation(mname, pcfg);
            if(mloc.scheme != mloc1.scheme || mloc.authority != mloc1.authority || mloc.path != mloc1.path){
                c.report(error(qualifiedModuleName, "Module name %v is incompatible with its file location", mname));
            }
        } catch value e: {
            c.report(error(qualifiedModuleName, "Module name %v is not consistent with its file location", mname));
        }
    } else if(isEmpty(pcfgVal)){
        return;
    } else {
        throw rascalCheckerInternalError("Inconsistent value for \"pathconfig\": <c.getStack(key_pathconfig)>");
    }
}

void collect(Header header, Collector c){
    collect(header.imports, c);
}

void collect(Body body, Collector c){
    // First collect all variable declarations to ensure that forward references to
    // variables are available during type unference inside function bodies
    for(toplevel <- body.toplevels){
        if(toplevel.declaration is variable){
            collect(toplevel.declaration, c);
        }
    }
     for(toplevel <- body.toplevels){
        if(!(toplevel.declaration is variable)){
            collect(toplevel.declaration, c);
        }
    }
    //collect(body.toplevels, c);
}

void collect(Toplevel toplevel, Collector c){
    collect(toplevel.declaration, c);
}

// ---- import ----------------------------------------------------------------

void collect(current: (Import) `import <ImportedModule m> ;`, Collector c){ // TODO: warn about direct self-import
    c.addPathToDef(m, {moduleId()}, importPath());
}
 
// ---- extend ----------------------------------------------------------------

void collect(current: (Import) `extend <ImportedModule m> ;`, Collector c){    
    c.addPathToDef(m, {moduleId()}, extendPath());
}

// ---- variable declaration --------------------------------------------------
    
void collect(current: (Declaration) `<Tags tags> <Visibility visibility> <Type varType> <{Variable ","}+ variables> ;`, Collector c){
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore <current>"); return; }
    scope = c.getScope();
    c.enterScope(current); // wrap in extra scope to isolate variables declared in complex (function) types
        for(var <- variables){
            c.enterLubScope(var);
            dt = defType([varType], makeGetSyntaxType(varType));
            dt.vis = getVis(current.visibility, privateVis());
            if(!isEmpty(tagsMap)) dt.tags = tagsMap;
            c.defineInScope(scope, prettyPrintName(var.name), variableId(), var.name, dt);
            
            if(var is initialized){
                initial = var.initial;
                c.require("initialization of `<var.name>`", initial, [initial, varType], makeVarInitRequirement(var));
                collect(initial, c); 
            }
            c.leaveScope(var);
        }
        c.fact(current, varType); 
        collect(tags, varType, c);  
    c.leaveScope(current);
}

void collect(Tag tg, Collector c){
    if(tg has expression){
        collect(tg.expression, c);
    }
}

// ---- annotation ------------------------------------------------------------

void collect(current: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType> @ <Name name> ;`, Collector c){
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore: <current>"); return; }
    
    pname = prettyPrintName(name);
    dt = defType([annoType, onType], AType(Solver s) { return aanno(pname, s.getType(onType), s.getType(annoType)); });
    dt.vis = getVis(current.visibility, publicVis());
    if(!isEmpty(tagsMap)) dt.tags = tagsMap;
    c.define(pname, annoId(), name, dt);
    collect(annoType, onType, c); 
}

// ---- keyword Formal --------------------------------------------------------

void collect(current: (KeywordFormal) `<Type kwType> <Name name> = <Expression expression>`, Collector c){
    kwformalName = prettyPrintName(name);
    DefInfo dt;
    try {
         dt = defType(c.getType(kwType)[label=kwformalName]);
    } catch TypeUnavailable(): {
         dt = defType([kwType], makeFieldType(kwformalName, kwType));
    }
    c.define(kwformalName, keywordFormalId(), current, dt);
    c.calculate("keyword formal", current, [kwType, expression],
        AType(Solver s){
            s.requireSubType(expression, kwType, error(expression, "Initializing expression of type %t expected, found %t", kwType, expression));
            return s.getType(kwType);
        });
    c.enterScope(kwType);   // Wrap the type in a subscope to avoid name clashes caused by names introduced in function types
        collect(kwType, c);
    c.leaveScope(kwType);
    collect(expression, c);
}
 
// ---- function declaration --------------------------------------------------

void collect(current: (FunctionDeclaration) `<FunctionDeclaration decl>`, Collector c){
//println("********** function declaration: <decl.signature.name>, <getLoc(decl)>");
    signature = decl.signature;
    fname = signature.name;
    modifiers = ["<m>" | m <- signature.modifiers.modifiers];
    tagsMap = getTags(decl.tags);
    if(ignoreCompiler(tagsMap)) { println("ignore: function <decl.signature.name>"); return; }
    
    <expected, expectedTagString> = getExpected(decl.tags);
    if(expected){
        expectedName = expectedTagString.contents;
        if("test" notin modifiers){
            c.report(warning(signature, "Modifier `test` is missing"));
        }
        c.use(expectedName, {dataId(), constructorId()});
        c.requireSubType(expectedName, aadt("RuntimeException", [], dataSyntax()), error(expectedName, "Expected `RuntimeException`, found %t", expectedName));
    }
    
    <deprecated, deprecationMessage> = getDeprecated(tagsMap);
   
    parentScope = c.getScope();
       
    c.enterLubScope(decl);
        scope = c.getScope();
        c.setScopeInfo(scope, functionScope(), returnInfo(signature.\type));
        collect(decl.signature, c);
        
        DefInfo dt;
        try {
            ft = c.getType(decl.signature);
            if(signature.parameters is varArgs) {
                    ft.varArgs = true;
             }
                 
             if(deprecated) {
                ft.deprecationMessage = deprecationMessage;
             }
             
             if("default" in modifiers){
                ft.isDefault = true;
             }
             
             if("test" in modifiers){
                ft.isTest = true;
                c.requireEqual(ft.ret, abool(), error(decl, "Test should have return type `bool`, found %t", ft.ret));
             }
      
             if(size(ft.formals) > 0){
                the_formals = getFormals(signature.parameters);
                ft.abstractFingerprint = fingerprint(the_formals[0], ft.formals[0], false);
                if(isConcretePattern(the_formals[0], ft.formals[0])){
                    ft.isConcreteArg = true;
                    ft.concreteFingerprint = fingerprint(the_formals[0], ft.formals[0], true);
                }
             }
             dt = defType(ft);
      
        } catch TypeUnavailable():{
        
            dt = defType([signature], AType(Solver s) {
                 ft = s.getType(signature);
                
                 if(signature.parameters is varArgs) {
                    ft.varArgs = true;
                 }
                 
                 if(deprecated) {
                    ft.deprecationMessage = deprecationMessage;
                 }
                 
                 if("default" in modifiers){
                    ft.isDefault = true;
                 }
                 
                 if("test" in modifiers){
                    ft.isTest = true;
                    s.requireEqual(ft.ret, abool(), error(decl, "Test should have return type `bool`, found %t", ft.ret));
                 }
          
                 if(size(ft.formals) > 0){
                    the_formals = getFormals(signature.parameters);
                    ft.abstractFingerprint = fingerprint(the_formals[0], ft.formals[0], false);
                    if(isConcretePattern(the_formals[0], ft.formals[0])){
                        ft.isConcreteArg = true;
                        ft.concreteFingerprint = fingerprint(the_formals[0], ft.formals[0], true);
                    }
                 }
                 return ft;
             });
        }
        dt.vis = getVis(decl.visibility, publicVis());
        if(!isEmpty(tagsMap)) dt.tags = tagsMap;
        alwaysSucceeds = all(pat <- getFormals(signature.parameters), pat is typedVariable) && !(decl is conditional) && !(decl is \default && /(Statement) `fail <Target target>;` := decl.body);
        if(!alwaysSucceeds) dt.canFail = true;
       
        if(!isEmpty(modifiers)) dt.modifiers = modifiers;
         
        c.defineInScope(parentScope, prettyPrintName(fname), functionId(), current, dt); 
        
        if(decl is abstract && "javaClass" notin tagsMap){
            c.report(warning(decl, "Empty function body"));
        }
        if(decl is \default){
            if(!returnsViaAllPath(decl.body, "<fname>", c) && "<signature.\type>" != "void"){
                c.report(error(decl, "Missing return statement"));
            }
        }
        
        if(decl is expression || decl is conditional){
            if(containsReturn(decl.expression)){
                ; // We assume that the expression returns a value via a return (and that is checked for compatibility with return type);
                  // We do in this case not check that the type of the expression as a whole is compatible with the return type.
                  // TODO: cover the case that we leave the expression via a return AND via the value of the expression as a whole
            } else {
                c.require("check on return type `<fname>`", decl.expression, [decl.expression], makeReturnRequirement(decl.expression, signature.\type));
            }
            collect(decl.expression, c);
        } 
        if(decl is conditional){
            conditions = [cond | cond <- decl.conditions];
            storeAllowUseBeforeDef(decl, decl.expression, c);
            c.require("when conditions", decl.conditions, conditions,
                void (Solver s){
                for(cond <- conditions){
                    condType = s.getType(cond);
                    if(!s.isFullyInstantiated(condType)){
                        s.requireUnify(condType, abool(), error(cond, "Cannot unify condition with `bool`, found %t", cond));
                        condType = s.instantiate(condType);
                    }           
                    s.requireSubType(cond, abool(), error(cond, "Condition should be `bool`, found %t", cond));
                }
            });
            collect(decl.conditions, c);
        }
        if(decl is \default) collect(decl.body, c);
        
    c.leaveScope(decl);
}

void collect(current: (FunctionBody) `{ <Statement* statements> }`, Collector c){
    collect(statements, c);
}

void collect(Signature signature, Collector c){
    returnType  = signature.\type;
    parameters  = signature.parameters;
    kwFormals   = getKwFormals(parameters);
  
    for(tv <- getTypeVars(returnType)){
        c.use(tv.name, {typeVarId()});
    }
    
    exceptions = [];
    
    if(signature is withThrows){
         exceptions = [ except | except <- signature.exceptions ];
         for(Type except <- exceptions){
            if(except is user){
                c.use(except, {constructorId()});
            } else {
                exceptions = [];
                c.report(error(except, "User defined data type expected, found `<except>`"));
            }
        }
    }
    
    collect(returnType, parameters, c);
     
    try {
        creturnType = c.getType(returnType);
        cparameters = c.getType(parameters);
        formalsList = atypeList(elems) := cparameters ? elems : [cparameters];
        kwformalsList = [<c.getType(kwf.\type)[label=prettyPrintName(kwf.name)], kwf.expression> | kwf <- kwFormals];
        c.fact(signature, afunc(creturnType, formalsList, kwformalsList));
        return;
    } catch TypeUnavailable(): {
        c.calculate("signature", signature, [returnType, parameters, *exceptions],// + kwFormals<0>,
            AType(Solver s){
                tformals = s.getType(parameters);
                formalsList = atypeList(elems) := tformals ? elems : [tformals];
                res = afunc(s.getType(returnType), formalsList, computeKwFormals(kwFormals, s));
                //println("signature <signature> ==\> <res>");
                return res;
            });
     }
}

void collect(Parameters parameters, Collector c){
    formals = getFormals(parameters);
    kwFormals = getKwFormals(parameters);
   
    typeVarsInFunctionParams = [*getTypeVars(t) | t <- formals + kwFormals];
    seenTypeVars = {};
    for(tv <- typeVarsInFunctionParams){
        if(tv is bounded){
            for(tvbound <- getTypeVars(tv.bound)){
                c.use(tvbound.name, {typeVarId()});
            }
        }
        tvname = "<tv.name>";
        if(tvname in seenTypeVars){
            c.use(tv.name, {typeVarId()});
        } else {
            seenTypeVars += tvname;
            c.define(tvname, typeVarId(), tv.name, defType(tv));
        }
    }
    
    beginPatternScope("parameter", c);
        if(parameters is varArgs){
            collect(formals[0..-1], c);
            collectAsVarArg(formals[-1], c);
        } else {
          collect(formals, c);
       }
       if(isEmpty(formals)){
            c.fact(parameters, atypeList([]));
       } else {
            scope = c.getScope();
            try {
                formalTypes = [ c.getType(f) | f <- formals ];
                int last = size(formalTypes) -1;
                if(parameters is varArgs){
                    formalTypes[last] = alist(unset(formalTypes[last], "label"), label=formalTypes[last].label);
                }
                c.fact(parameters, atypeList(formalTypes));
            } catch TypeUnavailable():{
                c.calculate("formals", parameters, [],
                    AType(Solver s) { 
                        formalTypes = [ getPatternType(f, avalue(), scope, s) | f <- formals ];
                        int last = size(formalTypes) -1;
                        if(parameters is varArgs){
                            formalTypes[last] = alist(unset(formalTypes[last], "label"), label=formalTypes[last].label);
                        }
                        return atypeList(formalTypes);
                    }); 
           }
       }
       collect(kwFormals, c);
    endPatternScope(c);
}

void(Solver) makeReturnRequirement(Tree returnExpr, Type declaredReturnType)
    = void(Solver s){
        theDeclaredReturnType = s.getType(declaredReturnType);
        returnRequirement(returnExpr, theDeclaredReturnType, s);
    };
    
void(Solver) makeReturnRequirement(Tree returnExpr, AType declaredReturnAType)
    = void(Solver s){
        returnRequirement(returnExpr, declaredReturnAType, s);
    };

void returnRequirement(Tree returnExpr, AType theDeclaredReturnType, Solver s){
      
    returnExprType = s.getType(returnExpr);
    Bindings bindings = ();
    try   bindings = matchRascalTypeParams(returnExprType, theDeclaredReturnType, bindings);
    catch invalidMatch(str reason):
          s.report(error(returnExpr, reason));
      
    actualReturnType = returnExprType;
    try {
        actualReturnType = xxInstantiateRascalTypeParameters(returnExpr, returnExprType, bindings, s);
    } catch invalidInstantiation(str reason):{
        s.report(error(returnExpr, "Returned type %t does not match declared type %t: %v", returnExprType, theDeclaredReturnType, reason));
    }

    if(s.isFullyInstantiated(actualReturnType)){
        s.requireTrue(s.equal(actualReturnType, avoid()) && s.equal(theDeclaredReturnType, avoid()) ||
                     !s.equal(actualReturnType, avoid()) && s.subtype(actualReturnType, theDeclaredReturnType), error(returnExpr, "Return type %t expected, found %t", theDeclaredReturnType, actualReturnType));
    } else
        if(!s.unify(actualReturnType, theDeclaredReturnType)){
        s.requireTrue(s.equal(actualReturnType, avoid()) && s.equal(theDeclaredReturnType, avoid()) ||
                     !s.equal(actualReturnType, avoid()) && s.subtype(actualReturnType, theDeclaredReturnType), error(returnExpr, "Return type %t expected, found %t", theDeclaredReturnType, actualReturnType));
    }   
 }
 
 //void(Solver s) makeReturnRequirement(Tree returnExpr, Type theDeclaredReturnType)
 //   = void(Solver s){
 //       theDeclaredReturnType = s.getType(theDeclaredReturnType);
 //         
 //       returnExprType = s.getType(returnExpr);
 //       Bindings bindings = ();
 //       try   bindings = matchRascalTypeParams(returnExprType, theDeclaredReturnType, bindings, bindIdenticalVars=true);
 //       catch invalidMatch(str reason):
 //             s.report(error(returnExpr, reason));
 //         
 //       actualReturnType = xxInstantiateRascalTypeParameters(returnExpr, returnExprType, bindings, s);
 //   
 //       if(s.isFullyInstantiated(actualReturnType)){
 //           s.requireTrue(s.equal(actualReturnType, avoid()) && s.equal(theDeclaredReturnType, avoid()) ||
 //                        !s.equal(actualReturnType, avoid()) && s.subtype(actualReturnType, theDeclaredReturnType), error(returnExpr, "Return type %t expected, found %t", theDeclaredReturnType, actualReturnType));
 //       } else
 //           if(!s.unify(actualReturnType, theDeclaredReturnType)){
 //           s.requireTrue(s.equal(actualReturnType, avoid()) && s.equal(theDeclaredReturnType, avoid()) ||
 //                        !s.equal(actualReturnType, avoid()) && s.subtype(actualReturnType, theDeclaredReturnType), error(returnExpr, "Return type %t expected, found %t", theDeclaredReturnType, actualReturnType));
 //       }   
 //    };

// ---- return statement (closely interacts with function declaration) --------

void collect(current: (Statement) `return <Statement statement>`, Collector c){  
    functionScopes = c.getScopeInfo(functionScope());

    for(<scope, scopeInfo> <- functionScopes){
        if(returnInfo(Type returnType) := scopeInfo){
           c.require("check return type", current, [returnType], makeReturnRequirement(statement, returnType));
           c.fact(current, statement);
           collect(statement, c);
           return;
        } else {
            throw rascalCheckerInternalError(getLoc(current), "Inconsistent info from function scope: <scopeInfo>");
        }
    }
    throw rascalCheckerInternalError(getLoc(current), "No surrounding function scope found for return");
}

// ---- alias declaration -----------------------------------------------------

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> alias <QualifiedName name> = <Type base>;`, Collector c){
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore: <current>"); return; }
    
    aliasName = prettyPrintName(name);
    c.define(aliasName, aliasId(), current, defType([base], AType(Solver s) { return s.getType(base); }));
    collect(base, c);
} 

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> alias <QualifiedName name>[ <{Type ","}+ parameters> ] = <Type base>;`, Collector c){
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore: <current>"); return; }
    
    aliasName = prettyPrintName(name);
       
    typeVars  = for(tp <- parameters){
        if(!(tp has typeVar)) c.report(error(tp, "Only type parameter allowed"));
        append tp.typeVar;
    }
    
    for(tv <- typeVars){
        c.define("<tv.name>", typeVarId(), tv.name, defType(tv));
    }
    
    c.define(aliasName, aliasId(), name, defType(typeVars + base, AType(Solver s){ 
        bindings = ();
        params = for(int i <- index(typeVars)){
            ptype = s.getType(typeVars[i]);
            if(!isRascalTypeParam(ptype)){
                  s.report(error(typeVars[i], "Only type parameter allowed, found %t", ptype));
            }
            append ptype; //unset(ptype, "label"); // TODO: Erase labels to enable later subset check
        }
        
        return aalias(aliasName, params, s.getType(base));
    }));
    collect(typeVars + base, c);
} 