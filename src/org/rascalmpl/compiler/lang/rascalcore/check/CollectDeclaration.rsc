@bootstrapParser
module lang::rascalcore::check::CollectDeclaration

/*
    Check all declarations in a module
*/

extend lang::rascalcore::check::CollectDataDeclaration;
extend lang::rascalcore::check::CollectSyntaxDeclaration;

extend lang::rascalcore::check::Fingerprint;
extend lang::rascalcore::check::PathAnalysis;

//import lang::rascalcore::check::ScopeInfo;
import lang::rascalcore::check::CollectOperators;
import lang::rascalcore::check::CollectExpression;
import lang::rascalcore::check::CollectPattern;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Attributes;
import lang::rascalcore::check::SyntaxGetters;

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

int localFunCounter = 0;

void collect(Module current: (Module) `<Header header> <Body body>`, Collector c){

    dataCounter = 0;
    variantCounter = 0;
    nalternatives = 0;
    syndefCounter = 0;
    localFunCounter = 0;
    
    mloc = getLoc(current);
    mname = prettyPrintName(header.name);
    checkModuleName(getLoc(current), header.name, c);
    
    tagsMap = getTags(header.tags);
    
    if(ignoreCompiler(tagsMap)) {
        c.report(info(current, "Ignoring module <mname>"));
        return; 
    }
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
}

void checkModuleName(loc mloc, QualifiedName qualifiedModuleName, Collector c){
    pcfgVal = c.getStack(key_pathconfig);
    if([PathConfig pcfg] := pcfgVal){ 
        mname = prettyPrintName(qualifiedModuleName);
        try {   
            mloc1 = getModuleLocation(mname, pcfg);
            if(mloc.scheme != mloc1.scheme || mloc.authority != mloc1.authority || mloc.path != mloc1.path){
                c.report(error(qualifiedModuleName, "Module name `%v` is incompatible with its file location %v", mname, mloc));
            }
        } catch str e: {
            c.report(error(qualifiedModuleName, "Module name `%v` is incompatible with its file location: %v", mname, e));
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
    if(ignoreCompiler(tagsMap)) { 
        c.report(info(current, "Ignoring variable declaration"));
        return; 
    }
    scope = c.getScope();
    c.enterScope(current); // wrap in extra scope to isolate variables declared in complex (function) types
        for(var <- variables){
            c.enterLubScope(var);
            dt = defType([varType], makeGetSyntaxType(varType));
            dt.vis = getVis(current.visibility, privateVis());
            dt.md5 = md5Hash("<md5Contrib4Tags(tags)><visibility><varType><var.name>");
            if(!isEmpty(tagsMap)) dt.tags = tagsMap;
            vname = prettyPrintName(var.name);
            if(isWildCard(vname)){
                c.report(error(var, "Cannot declare variable name starting with `_`"));
            }
            c.defineInScope(scope, vname, moduleVariableId(), var.name, dt);
            
            if(var is initialized){
                initial = var.initial;
                c.require("initialization of `<vname>`", initial, [initial, varType], makeVarInitRequirement(var));
                collect(initial, c); 
            }
            c.leaveScope(var);
        }
        c.fact(current, varType); 
        collect(tags, varType, c);  
    c.leaveScope(current);
}

void collect(Tag tg, Collector c){
    if(tg is expression){
        collect(tg.expression, c);
    }
}

// Deprecated
void collect(current: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType> @ <Name name> ;`, Collector c){
    c.report(warning(current, "Annotations are deprecated, use keyword parameters instead"));
    pname = prettyPrintName(name);
    
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { 
        c.report(info(current, "Ignoring anno declaration for `<pname>`"));
        return;
    }
    
    dt = defType([annoType, onType], AType(Solver s) { return aanno(pname, s.getType(onType), s.getType(annoType)); });
    dt.vis = getVis(current.visibility, publicVis());
    dt.md5 = md5Hash("<md5Contrib4Tags(tags)><visibility><annoType><onType><name>");
    if(!isEmpty(tagsMap)) dt.tags = tagsMap;
    if(isWildCard(pname)){
        c.report(error(name, "Cannot declare annotation name starting with `_`"));
    }
    c.define(pname, annoId(), current, dt);
    collect(tags, annoType, onType, c); 
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
    dt.md5 = md5Hash(unparseNoLayout(current));
    c.define(kwformalName, keywordFormalId(), current, dt);
    c.calculate("keyword formal", current, [kwType, expression],
        AType(Solver s){
            expType = s.getType(expression);
            // reduce any type parameters in default expression to avoid
            expType = visit(expType) { 
                case p:aparameter(pname, _) => avoid()
                };
            s.requireSubType(expType, kwType, error(expression, "Default expression of type %t expected, found %t", kwType, expression));
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
   
    ppfname = prettyPrintName(fname);
    modifiers = ["<m>" | m <- signature.modifiers.modifiers];
    tagsMap = getTags(decl.tags);
    if(ignoreCompiler(tagsMap)) {
        c.report(info(current, "Ignoring function declaration for `<decl.signature.name>`"));
        return;
    }
    // Make md5hash of nested functions unique with counter
    if(size(c.getStack(currentFunction)) > 0){ 
        localFunCounter += 1;
    }
    c.push(currentFunction, ppfname);
    md5Contrib = "<md5Contrib4Tags(decl.tags)><decl.visibility><md5Contrib4signature(signature)>-<localFunCounter>";
    
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
        collect(decl.tags, c);
        <tpnames, tpbounds> = collectSignature(decl.signature, c);
        //println("tpnames: <tpnames>");
        //iprintln("tpbounds:"); iprintln(tpbounds);
        //
        scope = c.getScope();
        c.setScopeInfo(scope, functionScope(), signatureInfo(signature.\type));
        
        
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
             res = ft[alabel=unescape("<fname>")];
             //println("collect FunctionDeclaration <fname>: <ft>");
             return res;
         });
        dt.vis = getVis(decl.visibility, publicVis());
        if(!isEmpty(tagsMap)) dt.tags = tagsMap;
        alwaysSucceeds = all(pat <- getFormals(signature.parameters), pat is typedVariable) && !(decl is conditional) && !(decl is \default && /(Statement) `fail <Target _>;` := decl.body);
        if(!alwaysSucceeds) dt.canFail = true;
        if(!isEmpty(modifiers)) dt.modifiers = modifiers;
        
        beginUseBoundedTypeParameters(tpbounds, c);
        
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
            if(!alwaysSucceeds){
                md5Contrib += "{<decl.body>}";
            }
        }
        
        if(decl is expression || decl is conditional){
            if(decl.expression is nonEmptyBlock){
                c.report(warning(decl.expression, "Non-empty block, maybe redundant `=` sign after function signature?"));
            }
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
            
            md5Contrib += " when <decl.conditions>";
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
        
        endUseBoundedTypeParameters(c);
        
        surroundingFuns = c.getStack(currentFunction);

        dt.md5 = md5Hash(size(surroundingFuns) == 1 ? md5Contrib : "<intercalate("/", surroundingFuns)><md5Contrib>");
        c.defineInScope(parentScope, prettyPrintName(fname), functionId(), current, dt); 
        // println("<md5Contrib> =\> <dt.md5>");
    c.leaveScope(decl);
    c.pop(currentFunction);
    if(size(c.getStack(currentFunction)) == 0){ 
        localFunCounter = 0;
    }
}

void collect(current: (FunctionBody) `{ <Statement* statements> }`, Collector c){
    collect(statements, c);
}

str md5Contrib4signature(Signature signature){
    fs = "<for(f <- signature.parameters.formals.formals){><f is typedVariable ? "<f.\type> <f.name>" : "<f>"> <}>";
    res = "<signature.modifiers><signature.\type> <signature.name>( <fs>)";
    //println("<signature> =\> <res>");
    return res;
}

tuple[set[str], rel[str,Type]] collectSignature(Signature signature, Collector c){
    returnType  = signature.\type;
    parameters  = signature.parameters;
    kwFormals   = getKwFormals(parameters);
    
    beginUseTypeParameters(c, closed=true);
        collect(returnType, c); // any type parameters in return type remain closed (closed=true);
    endUseTypeParameters(c);  
    
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
    beginDefineOrReuseTypeParameters(c, closed=false);
        collect(parameters, c); // any type parameters in parameter list remain open (closed=false);
    endDefineOrReuseTypeParameters(c);
    
    <tpnames, tpbounds> = computeBoundsAndDefineTypeParams(signature, c);
        
    c.calculate("signature", signature, [returnType, parameters, *exceptions],
        AType(Solver s){
            tformals = s.getType(parameters);
            formalsList = atypeList(elems) := tformals ? elems : [tformals];
            rt = s.getType(returnType);
            minB = minimizeBounds(tpbounds, s);
            rtU = updateBounds(rt, minB);
            formalsList = [ updateBounds(fm, minB) | fm <- formalsList ];
            kwFormalsList = [ kwf[fieldType = updateBounds(kwf.fieldType, minB)] | kwf <- computeKwFormals(kwFormals, s) ];
            ft = afunc(rt, formalsList, kwFormalsList);
            //ft = updateBounds(afunc(s.getType(returnType), formalsList, computeKwFormals(kwFormals, s)), minB);
            return ft;
        });
        
    return <tpnames, tpbounds>;
}

@synopsis{Given a type t and a map of named bounds, update the bound in all type parameters occurring in t}
private AType updateBounds(AType t, map[str,AType] bounds){
    maxT = isSyntaxType(t) ? treeType : avalue();
    return visit(t) {case aparameter(pname, bnd, alabel=L,closed=closed) : {
                            bnd = bounds[pname] ? maxT;
                            insert isEmpty(L) ? aparameter(pname, bnd, closed=closed) : aparameter(pname, bnd, alabel=L, closed=closed);
                        }
                    };
}

@synopsis{Minimize the bounds of all type parameters in bounds map}
private map[str,AType] minimizeBounds(rel[str,Type] typeParamBounds, Solver s){
    return propagateParams((tpname : commonLowerBound(typeParamBounds, tpname, s) | tpname <- domain(typeParamBounds)));
}

@synopsis{Propagate type parameters in a bounds map}
private map[str, AType] propagateParams(map[str,AType] typeParamBounds){
    AType find(str tpname) = (aparameter(tpname2, _) := typeParamBounds[tpname]) ? typeParamBounds[tpname2] : typeParamBounds[tpname] ;
    
    return (tpname : find(tpname) | tpname <- typeParamBounds);
}

@synopsis{Compute the common lower bound for type parameter `tpname`}
private AType commonLowerBound(rel[str,Type] typeParamBounds, str tpname,  Solver s){
    bounds = typeParamBounds[tpname];
    solve(bounds){
        for(b <- bounds){
            if(b is variable && TypeVar tp := b.typeVar){
                bounds += typeParamBounds["<tp.name>"];
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
            s.report(error(b, "Bounds %t and %t for type parameter `&%v` are not comparable", bt, minBound, tpname));
        }
    }
    return minBound;
}

@synopsis{Create a function for computing the type of type var `tpname`, given a bounds map}
private AType(Solver) makeBoundDef(TypeVar tvar,  rel[str,Type] typeParamBounds, bool closed=false)
    = AType(Solver s) { 
        tpname = "<tvar.name>";
        tp = aparameter(tpname, commonLowerBound(typeParamBounds, tpname, s), closed=closed);
        s.fact(tvar, tp);
        return tp;
      };
      
private AType(Solver) makeTypeGetter(TypeVar tvar, bool closed = false)
    = AType(Solver s) { return s.getType(tvar.name)[closed=true]; };
    
@synopsis{Compute a bounds map for a signature and define all type parameters accordingly}
private tuple[set[str], rel[str,Type]] computeBoundsAndDefineTypeParams(Signature signature, Collector c){
    formals = getFormals(signature.parameters);
    kwFormals = getKwFormals(signature.parameters);
    returnType  = signature.\type;
   
    typeParamsInReturn = getTypeParams(returnType);
    
    // Type parameters have to be handled in a different way than other variables.
    // Ordinary entities like variables and function names are declared or used when we encounter them 
    // during collection.  We cannot declare or use type parameters as they are encountered during collect 
    // since their open/closed status has to be taken into account and that depends on the more global 
    // context of the signature or function declaration in which they occur. That is why we explicitly create
    //  uses and defs nof type parameters.
   
    typeParamsInParameters = [*getTypeParams(t) | t <- formals + kwFormals];
    
    rel[str,Type] typeParamBounds = {};
    
    for(TypeVar tp <-  typeParamsInReturn + typeParamsInParameters){
        tpname = "<tp.name>";
        if(tp is bounded){
            typeParamBounds += <tpname, tp.bound>;
        }
    }
    
    seenInReturn = {};
    for(tp <- typeParamsInReturn){
        if(tp is bounded){
            for(tpbound <- getTypeParams(tp.bound)){
                c.use(tpbound.name, {typeVarId()});
            }
        }
        c.use(tp.name, {typeVarId()});
        c.calculate("typevar in result type", tp, [tp.name], makeTypeGetter(tp,closed=true));
    }
    
    seenInParams = {};
    for(tp <- typeParamsInParameters){
        if(tp is bounded){
            for(tpbound <- getTypeParams(tp.bound)){
                c.use(tpbound.name, {typeVarId()});
            }
        }
        tpname = "<tp.name>";
        if(tpname in seenInParams){
            c.use(tp.name, {typeVarId()});
            c.fact(tp, tp.name);
        } else {
            seenInParams += tpname;
            c.define(tpname, typeVarId(), tp.name, 
                defType(toList(typeParamBounds[tpname]), makeBoundDef(tp, typeParamBounds, closed=false)));
            c.fact(tp, tp.name);
        }
    }

    // Due to their special treatment, missing type parameters are not detected by typepal but have to
    // detected explicitly.
    
    missing = seenInReturn - seenInParams;
    if(!isEmpty(missing)){
        missing = {"&<m>" | m <- missing };
        c.report(error(signature, "Type parameter(s) %v in return type of function %q not bound by its formal parameters", missing, signature.name));
    }
    
    tpNames = {"<tp.name>" | TypeVar tp <- typeParamsInParameters};
    return <tpNames, typeParamBounds>;
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
            
            c.calculate("formals", parameters, [],
                AType(Solver s) {    
                    formalTypes = [ getPatternType(f, avalue(), scope, s) | f <- formals ];
                    int last = size(formalTypes) -1;
                    if(parameters is varArgs){
                        formalTypes[last] = alist(unset(formalTypes[last], "alabel"), alabel=formalTypes[last].alabel);
                    }
                    for(int i <- index(formals)){
                        checkNonVoid(formals[i], formalTypes[i], s, "Formal parameter");
                    }
                    return atypeList(formalTypes);  //TODO: what happened to the kw parameters in this type?
                });
       }
       collect(kwFormals, c);
    endPatternScope(c);
}

void(Solver) makeReturnRequirement(Tree returnExpr, Type returnType)
    = void(Solver s){
        returnRequirement(returnExpr, s.getType(returnType), s);
    };
    
void(Solver) makeReturnRequirement(Tree returnExpr, AType returnAType)
    = void(Solver s){
        returnRequirement(returnExpr, returnAType, s);
    };

void returnRequirement(Tree returnExpr, AType declaredReturnType, Solver s){  
    returnExprType = s.getType(returnExpr);
    msg = p:/aparameter(_,_) := declaredReturnType
          ? error(returnExpr, "Returned type %t is not always a subtype of expected return type %t", returnExprType, declaredReturnType)
          : error(returnExpr, "Return type %t expected, found %t", declaredReturnType, returnExprType);
          
    bindings = ();
    rsuffix = "r";
    dsuffix = "d";
    returnExprTypeU = makeUniqueTypeParams(returnExprType, rsuffix);
    declaredReturnTypeU  = makeUniqueTypeParams(declaredReturnType, dsuffix);
    try {
        bindings = matchRascalTypeParams(returnExprTypeU, declaredReturnTypeU, bindings);
    } catch invalidMatch(str reason):
        s.report(error(returnExpr, reason));
    
    try {
        returnExprTypeU = instantiateRascalTypeParameters(returnExpr, returnExprTypeU, bindings, s);
    } catch invalidInstantiation(str msg): {
        s.report(error(returnExpr, "Cannot instantiate return type `<prettyAType(returnExprType)>`: " + msg));
    }
    
    s.requireSubType(deUnique(returnExprTypeU), deUnique(declaredReturnTypeU), msg);
    if(!isVoidAType(declaredReturnTypeU)){
        checkNonVoid(returnExpr, returnExprTypeU, s, "Return value");
    }
 }

// ---- return statement (closely interacts with function declaration) --------

void collect(current: (Statement) `return <Statement statement>`, Collector c){  
    functionScopes = c.getScopeInfo(functionScope());
    assert !isEmpty(functionScopes);
    for(<_, scopeInfo> <- functionScopes){
        if(signatureInfo(Type returnType) := scopeInfo){
           c.require("check return type", current, [statement], makeReturnRequirement(statement, returnType));
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
    aliasName = prettyPrintName(name);
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) {
        c.report(info(current, "Ignoring alias declaration for `<aliasName>`"));
        return;
    }
    if(isWildCard(aliasName)){
        c.report(error(name, "Cannot declare alias name starting with `_`"));
    }
    
    c.define(aliasName, aliasId(), current, defType([base], AType(Solver s) { return s.getType(base); })[md5 = md5Hash("<md5Contrib4Tags(tags)><visibility><name><base>")]);
    c.enterScope(current);
        collect(tags, base, c);
    c.leaveScope(current);
} 

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> alias <QualifiedName name>[ <{Type ","}+ parameters> ] = <Type base>;`, Collector c){
    aliasName = prettyPrintName(name);
    tagsMap = getTags(tags);
    
    if(ignoreCompiler(tagsMap)) {
        c.report(info(current, "Ignoring alias declaration for `<aliasName>`"));
        return;
    }
    
    if(isWildCard(aliasName)){
        c.report(error(name, "Cannot declare alias name starting with `_`"));
    }
   
    typeParams  = for(tp <- parameters){
        if(!(tp has typeVar)) c.report(error(tp, "Only type parameter allowed"));
        append tp.typeVar;
    }
    
    c.define(aliasName, aliasId(), name, defType(typeParams + base, AType(Solver s){ 
        bindings = ();
        params = for(int i <- index(typeParams)){
            ptype = s.getType(typeParams[i]);
            if(!isRascalTypeParam(ptype)){
                  s.report(error(typeParams[i], "Only type parameter allowed, found %t", ptype));
            }
            append ptype;
        }
        
        return aalias(aliasName, params, s.getType(base));
    })[md5 = md5Hash("<md5Contrib4Tags(tags)><visibility><name><parameters><base>")]);
    
    collect(tags, c);
    
    beginDefineOrReuseTypeParameters(c, closed=false);
        collect(typeParams, c);
    endDefineOrReuseTypeParameters(c);  
     
    beginUseTypeParameters(c, closed=true);
        collect(base, c);
    endUseTypeParameters(c);
      
        
} 