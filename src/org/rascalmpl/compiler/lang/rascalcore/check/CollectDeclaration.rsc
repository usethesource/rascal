@bootstrapParser
module lang::rascalcore::check::CollectDeclaration

extend lang::rascalcore::check::CollectDataDeclaration;
extend lang::rascalcore::check::CollectSyntaxDeclaration;

extend lang::rascalcore::check::Fingerprint;
extend lang::rascalcore::check::PathAnalysis;

import lang::rascalcore::check::CollectOperators;
import lang::rascalcore::check::CollectExpression;
import lang::rascalcore::check::CollectVarArgs;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Attributes;

import IO;
import List;
import Map;
import Relation;
import Node;
import Set;
import String;

import util::Reflective;

// ---- Utilities -------------------------------------------------------------


// ---- Rascal declarations ---------------------------------------------------

void collect(Module current: (Module) `<Header header> <Body body>`, Collector c){

    //current = current.top;
    mname = prettyPrintName(header.name);
    //println("Type checking module  <mname>");
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
        } catch _: {
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
Expression makeKeywordDefaultExpression(Type annoType, Collector c){

    // The default expression is arbitrary since a NoSuchAnnotation will be thrown before it can be executed.
    // However, in the generated code we need a type correct default expression.
    // TODO: remove when annotations are gone

    res = (Expression) `0`;
    switch(annoType){
    case (Type) `bool`: res = (Expression) `true`;
    case (Type) `int`: res = (Expression) `0`;
    case (Type) `real`: res = (Expression) `0.0`;
    case (Type) `str`: res = (Expression) `""`;
    case (Type) `datetime`: res = (Expression) `$2020-09-10T11:17:10.760+00:00$`;
    case (Type) `loc`: res = (Expression) `|project://rascal/src/org/rascalmpl/library/Node.rsc|`;
    case (Type) `value`: res = (Expression) `true`;
    case (Type) `list[<TypeArg _>]`: res = (Expression) `[]`;
    case (Type) `set[<TypeArg _>]`: res = (Expression) `{}`;
    case (Type) `map[<TypeArg _>,<TypeArg _>]`: res = (Expression) `()`;
    case (Type) `rel[<{TypeArg ","}+ _>]`: res = (Expression) `{}`;
    case (Type) `lrel[<{TypeArg ","}+ _>]`: res = (Expression) `[]`;
    //case (Type) `Message`: {
    //        ;//res = (Expression) `error("AUTOMATICALLY GENERATED DEFAULT MESSAGE", |error:///|)`;
    //    }
    default: {
        println("WARNING: makeKeywordDefaultExpression: <annoType>");
        }
    }
    c.fact(res, annoType); 
    return res; 
}
KeywordFormal makeKeywordFormal(Type annoType, Name name, Collector c){
    Expression dflt = makeKeywordDefaultExpression(annoType, c);
    res = (KeywordFormal) `<Type annoType> <Name name> = <Expression dflt>`;
    res.\type@\loc = getLoc(annoType);  // set back locations in synthetic keywordFormal
    res.expression@\loc = getLoc(annoType);
    c.fact(res.\type, annoType);
    c.fact(res.expression, dflt);
    return res;
}
// Deprecated
void collect(current: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType> @ <Name name> ;`, Collector c){
    c.report(warning(current, "Annotations are deprecated, use keyword parameters instead"));
    
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore: <current>"); return; }
    collect(annoType, onType, c);
    
    //userType = onType has user ? onType.user 
    adtName = onType is user ? prettyPrintName(onType.user.name) : "<onType>";
    commonKeywordParameterList = [makeKeywordFormal(annoType, name, c) ];     
    dt1 = defType(aadt(adtName, [], dataSyntax()));
    
    dt1.commonKeywordFields = commonKeywordParameterList;
    c.define(adtName, dataId(), current, dt1);
   
    //pname = prettyPrintName(name);
    //dt2 = defType([annoType, onType], AType(Solver s) { return aanno(pname, s.getType(onType), s.getType(annoType)); });
    //dt2.vis = getVis(current.visibility, publicVis());
    //if(!isEmpty(tagsMap)) dt2.tags = tagsMap;
    //c.define(pname, keywordFieldId(), name, dt2);
    
    //pname = prettyPrintName(name);
    //dt = defType([annoType, onType], AType(Solver s) { return aanno(pname, s.getType(onType), s.getType(annoType)); });
    //dt.vis = getVis(current.visibility, publicVis());
    //if(!isEmpty(tagsMap)) dt.tags = tagsMap;
    //c.define(pname, annoId(), name, dt);
    //collect(annoType, onType, c); 
}

// ---- keyword Formal --------------------------------------------------------

void collect(current: (KeywordFormal) `<Type kwType> <Name name> = <Expression expression>`, Collector c){
    kwformalName = prettyPrintName(name);
    DefInfo dt = noDefInfo();
    try {
         dt = defType(c.getType(kwType)[alabel=kwformalName]);
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
    //println("collect function declaration: <decl.signature.name>, <getLoc(decl)>");
   
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
    
    bool myReturnsViaAllPath = (decl is \default) ? returnsViaAllPath(decl.body, "<fname>", c) : true;
   
    parentScope = c.getScope();
       
    c.enterLubScope(decl);
        scope = c.getScope();
        c.setScopeInfo(scope, functionScope(), returnInfo(signature.\type));
        collect(decl.signature, c);
        
        DefInfo dt = noDefInfo();
        try { // try immediate computation of the function type if all types are already available
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
             
             if(myReturnsViaAllPath){
                ft.returnsViaAllPath = true;
             }
      
             if(size(ft.formals) > 0){
                the_formals = getFormals(signature.parameters);
                ft.abstractFingerprint = fingerprint(the_formals[0], ft.formals[0], false);
                if(isConcretePattern(the_formals[0], ft.formals[0])){
                    ft.isConcreteArg = true;
                    ft.concreteFingerprint = fingerprint(the_formals[0], ft.formals[0], true);
                }
             }
             dt = defType(ft[alabel=unescape("<fname>")]);
      
        } catch TypeUnavailable():{
            //  Delayed computation of the function type if some types are anot yet available
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
                 
                 if(myReturnsViaAllPath){
                    ft.returnsViaAllPath = true;
                 }
          
                 if(size(ft.formals) > 0){
                    the_formals = getFormals(signature.parameters);
                    ft.abstractFingerprint = fingerprint(the_formals[0], ft.formals[0], false);
                    if(isConcretePattern(the_formals[0], ft.formals[0])){
                        ft.isConcreteArg = true;
                        ft.concreteFingerprint = fingerprint(the_formals[0], ft.formals[0], true);
                    }
                 }
                 return ft[alabel=unescape("<fname>")];
             });
        }
        dt.vis = getVis(decl.visibility, publicVis());
        if(!isEmpty(tagsMap)) dt.tags = tagsMap;
        alwaysSucceeds = all(pat <- getFormals(signature.parameters), pat is typedVariable) && !(decl is conditional) && !(decl is \default && /(Statement) `fail <Target _>;` := decl.body);
        if(!alwaysSucceeds) dt.canFail = true;
       
        if(!isEmpty(modifiers)) dt.modifiers = modifiers;
         
        c.defineInScope(parentScope, prettyPrintName(fname), functionId(), current, dt); 
        
        if(decl is abstract){
            if("javaClass" in tagsMap){
                if("java" notin modifiers){
                    c.report(warning(decl.signature, "Missing modifier `java`"));
                }
                if("test" in modifiers){
                    c.report(warning(decl.signature, "Modifier `test` cannot be used for Java functions"));
                }
            } else {
                c.report(warning(decl, "Empty function body"));
             }
        } else {
            if("javaClass" in tagsMap){
                c.report(warning(decl.signature, "Redundant tag `javaClass`"));
            }
            if("java" in modifiers){
                c.report(warning(decl.signature, "Redundant modifier `java`"));
            }
        }
        
        if(decl is \default){
            if(!myReturnsViaAllPath && "<signature.\type>" != "void"){
                c.report(error(decl.signature, "Missing return statement"));
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

//AType anonymizeFunctionTypes(AType t){
//    res = visit (t){ case afunc(ret, formalsList, kwformalsList) => afunc(unsetRec(ret, "alabel"), formalsList, kwformalsList) }
//    return res;
//}

void collect(Signature signature, Collector c){
    returnType  = signature.\type;
    parameters  = signature.parameters;
    kwFormals   = getKwFormals(parameters);
    
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
    
    rel[str,Type] tvbounds = handleTypeVars(signature, c);
     
    try {
        creturnType = c.getType(returnType);
        cparameters = c.getType(parameters);
        formalsList = atypeList(elems) := cparameters ? elems : [cparameters];
        kwformalsList = [<c.getType(kwf.\type)[alabel=prettyPrintName(kwf.name)], kwf.expression> | kwf <- kwFormals];
        //c.fact(signature, afunc(creturnType, formalsList, kwformalsList));
        c.fact(signature, updateBounds(afunc(creturnType, formalsList, kwformalsList), minimizeBounds(tvbounds, c)));
        return;
    } catch TypeUnavailable(): {
        c.calculate("signature", signature, [returnType, parameters, *exceptions],// + kwFormals<0>,
            AType(Solver s){
                tformals = s.getType(parameters);
                formalsList = atypeList(elems) := tformals ? elems : [tformals];
                //return afunc(s.getType(returnType), formalsList, computeKwFormals(kwFormals, s));
                return updateBounds(afunc(s.getType(returnType), formalsList, computeKwFormals(kwFormals, s)), minimizeBounds(tvbounds, s));
            });
     }
}

AType updateBounds(AType t, map[str,AType] bounds){
    return visit(t) {case aparameter(pname, bnd, alabel=L) : {
                            bnd = bounds[pname] ? avalue();
                            insert isEmpty(L) ? aparameter(pname, bnd) : aparameter(pname, bnd, alabel=L);
                        }
                    };
}

map[str,AType] minimizeBounds(rel[str,Type] typeVarBounds, Collector c){
    return propagateParams((tvname : commonLowerBound(typeVarBounds, tvname, c) | tvname <- domain(typeVarBounds)));
}

map[str,AType] minimizeBounds(rel[str,Type] typeVarBounds, Solver s){
    return propagateParams((tvname : commonLowerBound(typeVarBounds, tvname, s) | tvname <- domain(typeVarBounds)));
}

map[str, AType] propagateParams(map[str,AType] typeVarBounds){
    AType find(str tvname) = (aparameter(tvname2, _) := typeVarBounds[tvname]) ? typeVarBounds[tvname2] : typeVarBounds[tvname] ;
    
    return (tvname : find(tvname) | tvname <- typeVarBounds);
    //return (tvname : (aparameter(tvname2, _) := typeVarBounds[tvname]) ? typeVarBounds[tvname2] : typeVarBounds[tvname] | tvname <- typeVarBounds);
}

AType commonLowerBound(rel[str,Type] typeVarBounds, str tvname, Collector c){
    bounds = typeVarBounds[tvname];
   
    solve(bounds){
        for(b <- bounds){
            if(b is variable && TypeVar tv := b.typeVar){
                bounds += typeVarBounds["<tv.name>"];
            } else bounds += b;
        }
    }
    minBound = avalue();
    for(b <- bounds){
       bt = c.getType(b);
       if(asubtype(minBound, bt)){
            ;// keep smallest
        } else if(asubtype(bt, minBound)){
            minBound = bt;
        } else {
            c.report(error(b, "Bounds %t and %t for type parameter `%v` are not comparable", bt, minBound, tvname));
        }
    }
    return minBound;
}

AType commonLowerBound(rel[str,Type] typeVarBounds, str tvname,  Solver s){
    bounds = typeVarBounds[tvname];
    solve(bounds){
        for(b <- bounds){
            if(b is variable && TypeVar tv := b.typeVar){
                bounds += typeVarBounds["<tv.name>"];
            } else bounds += b;
        }
    }
    minBound = avalue();
    for(b <- bounds){
       bt = s.getType(b);
       if(asubtype(minBound, bt)){
            ;// keep smallest
        } else if(asubtype(bt, minBound)){
            minBound = bt;
        } else {
            s.report(error(b, "Bounds %t and %t for type parameter `%v` are not comparable", bt, minBound, tvname));
        }
    }
    return minBound;
}

AType(Solver) makeBoundDef(str tvname,  rel[str,Type] typeVarBounds)
    = AType(Solver s) { return aparameter(tvname, commonLowerBound(typeVarBounds, tvname, s)); };
    
rel[str,Type] handleTypeVars(Signature signature, Collector c){
    formals = getFormals(signature.parameters);
    kwFormals = getKwFormals(signature.parameters);
    returnType  = signature.\type;
   
    typeVarsInParams = {*getTypeVars(t) | t <- formals + kwFormals};
    typeVarsInReturn = getTypeVars(returnType);
    
    rel[str,Type] typeVarBounds = {};
    
    for(tv <-  typeVarsInParams + typeVarsInReturn){
        tvname = "<tv.name>";
        if(tv is bounded){
            typeVarBounds += <tvname, tv.bound>;
        }
    }
    
    tvnamesInParams = { "<tv.name>" | tv <- typeVarsInParams };
    
    seenInReturn = {};
    for(tv <- typeVarsInReturn){
        if(tv is bounded){
            for(tvbound <- getTypeVars(tv.bound)){
                c.use(tvbound.name, {typeVarId()});
            }
        }
        tvname = "<tv.name>";
        if(tvname in seenInReturn || tvname in tvnamesInParams){
            c.use(tv.name, {typeVarId()});
        } else {
            seenInReturn += tvname;
            c.define(tvname, typeVarId(), tv.name, 
                defType(toList(typeVarBounds[tvname]), makeBoundDef(tvname, typeVarBounds)));
        }
    }
    
    seenInParams = {};
    for(tv <- typeVarsInParams){
        if(tv is bounded){
            for(tvbound <- getTypeVars(tv.bound)){
                c.use(tvbound.name, {typeVarId()});
            }
        }
        tvname = "<tv.name>";
        if(tvname in seenInParams){
            c.use(tv.name, {typeVarId()});
        } else {
            seenInParams += tvname;
            c.define(tvname, typeVarId(), tv.name, 
                defType(toList(typeVarBounds[tvname]), makeBoundDef(tvname, typeVarBounds)));
        }
    }
    return typeVarBounds;
}

void collect(Parameters parameters, Collector c){
    formals = getFormals(parameters);
    kwFormals = getKwFormals(parameters);
    
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
                    formalTypes[last] = alist(unset(formalTypes[last], "alabel"), alabel=formalTypes[last].alabel);
                }
                c.fact(parameters, atypeList(formalTypes));
                for(int i <- index(formals)){
                    checkNonVoid(formals[i], formalTypes[i], c, "Formal parameter");
                }
            } catch TypeUnavailable():{
                c.calculate("formals", parameters, [],
                    AType(Solver s) { 
                        s.push(inFormals, true);
                        formalTypes = [ getPatternType(f, avalue(), scope, s) | f <- formals ];
                        s.pop(inFormals);
                        int last = size(formalTypes) -1;
                        if(parameters is varArgs){
                            formalTypes[last] = alist(unset(formalTypes[last], "alabel"), alabel=formalTypes[last].alabel);
                        }
                        for(int i <- index(formals)){
                            checkNonVoid(formals[i], formalTypes[i], c, "Formal parameter");
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

// ---- return statement (closely interacts with function declaration) --------

void collect(current: (Statement) `return <Statement statement>`, Collector c){  
    functionScopes = c.getScopeInfo(functionScope());
    assert !isEmpty(functionScopes);
    for(<_, scopeInfo> <- functionScopes){
        if(returnInfo(Type returnType) := scopeInfo){
           c.require("check return type", current, [returnType], makeReturnRequirement(statement, returnType));
           c.fact(current, returnType); // Note that type of the return statement as a whole is the function's return type
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
            append ptype; //unset(ptype, "alabel"); // TODO: Erase alabels to enable later subset check
        }
        
        return aalias(aliasName, params, s.getType(base));
    }));
    collect(typeVars + base, c);
} 