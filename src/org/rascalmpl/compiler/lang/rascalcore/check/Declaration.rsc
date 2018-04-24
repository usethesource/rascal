module lang::rascalcore::check::Declaration
  
extend analysis::typepal::TypePal;
extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::TypePalConfig;
extend lang::rascalcore::check::ConvertType;
extend lang::rascalcore::check::Expression;


import lang::rascal::\syntax::Rascal;
import lang::rascalcore::check::ATypeExceptions;

import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::ATypeInstantiation;

import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Productions;
import lang::rascalcore::grammar::definition::Attributes;

import lang::rascalcore::check::Import;
import lang::rascalcore::check::Pattern;

import util::Reflective;
import Node;
import String;
import IO;
import Set;
import Map;
import String;
import util::Maybe;

// ---- Utilities -------------------------------------------------------------

map[str,str] getTags(Tags tags)
    =  ("<tg.name>" : tg has contents ? "<tg.contents.contents>" : "" | tg <- tags.tags);

bool ignoreCompiler(map[str,str] tagsMap)
    = !isEmpty(domain(tagsMap) &  {"ignore", "Ignore", "ignoreCompiler", "IgnoreCompiler"});

tuple[bool, str] getDeprecated(map[str,str] tagsMap){
    for(depr <- {"deprecated", "Deprecated"}){
        if(tagsMap[depr]?)
            return <true, tagsMap[depr]>;
   }
   return <false, "">;
}

Vis getVis((Visibility) `private`, Vis dv)  = privateVis();
Vis getVis((Visibility) `public`, Vis dv)   = publicVis();
Vis getVis((Visibility) ``, Vis dv)         = dv;

// ---- Rascal declarations ---------------------------------------------------

void collect(current: (Module) `<Header header> <Body body>`, Collector c){
  
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
    c.push(key_processed_modules, mname);
     
    c.push(key_current_module, mname);
    c.enterScope(current);
        collectParts(current, c);
        //collect(header, body, c);
    c.leaveScope(current);
    c.pop(key_current_module);
    getImports(c);
}

void checkModuleName(loc mloc, QualifiedName qualifiedModuleName, Collector c){
    pcfgVal = c.getStack("pathconfig");
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
        throw rascalCheckerInternalError("Inconsistent value for \"pathconfig\": <c.getStack("pathconfig")>");
    }
}

// ---- import ----------------------------------------------------------------

void collect(current: (Import) `import <ImportedModule m> ;`, Collector c){ // TODO: warn about direct self-import
    c.useViaPath(m, {moduleId()}, importPath());
    c.push(key_imported, <unescape("<m.name>"), current>);
    c.push(key_import_graph, <c.top(key_current_module), "<m.name>">);
}

//loc timestamp(loc l) = l[fragment="<lastModified(l)>"];

void getImports(Collector c){
    // Do not expand imports, while we are already doing that
    if(!isEmpty(c.getStack(key_expanding_imports)))   return;
    
    pcfgVal = c.getStack("pathconfig");
    
    if([PathConfig pcfg] := pcfgVal){ 
        allreadyImported = {};
        if(list[str] processed := c.getStack(key_processed_modules)){
            allreadyImported = toSet(processed);
        }
       
        c.push(key_expanding_imports, true);
        solve(allreadyImported){
            if(lrel[str,Tree] importedModules := c.getStack(key_imported)){
                for(<mname, importStatement> <- importedModules){
                    if(mname in allreadyImported) continue;
                    allreadyImported += mname;
                    reuse = addImport(mname, importStatement, pcfg, c);
                    if(!reuse){
                        try {
                            //mloc = timestamp(getModuleLocation(mname, pcfg)); 
                            mloc = getModuleLocation(mname, pcfg);                    
                            println("*** importing <mname> from <mloc>");
                            pt = parseModuleWithSpaces(mloc).top;
                            collect(pt, c);
                        } catch value e: {
                            c.report(error(importStatement, "Error during import of %v: %v", mname, e));
                        }
                    }
                }
            } else {
                throw rascalCheckerInternalError("Inconsistent value for \"imported\": <c.getStack("imported")>");
            }
        }
    } else if(isEmpty(pcfgVal)){
        return;
    } else {
        throw rascalCheckerInternalError("Inconsistent value for \"pathconfig\": <c.getStack("pathconfig")>");
    }
}
 
// ---- extend ----------------------------------------------------------------

void collect(current: (Import) `extend <ImportedModule m> ;`, Collector c){    
    c.useViaPath(m, {moduleId()}, extendPath());
    c.push(key_imported, <unescape("<m.name>"), current>);
    c.push(key_extended, unescape("<m.name>"));
    c.push(key_extend_graph, <c.top(key_current_module), "<m.name>">);
}

// ---- variable declaration --------------------------------------------------
         
void collect(current: (Declaration) `<Tags tags> <Visibility visibility> <Type varType> <{Variable ","}+ variables> ;`, Collector c){
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore <current>"); return; }
    
    for(var <- variables){
        dt = defType([varType], AType(Solver s) { return getSyntaxType(varType, s); });
        dt.vis = getVis(current.visibility, privateVis());
        if(!isEmpty(tagsMap)) dt.tags = tagsMap;
        c.define(prettyPrintName(var.name), variableId(), var.name, dt);
        
        if(var is initialized){
            c.enterLubScope(var);
                initial = var.initial;
                c.requireSubtype(initial, var.name, error(initial, "Initialization of %q should be subtype of %t, found %t", "<var.name>", var.name, initial));
                collect(var.initial, c); 
            c.leaveScope(var);
        }
    }  
    collect(tags, varType, c);  
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
    c.define("<name>", variableId(), name, defType(kwType));
    c.calculate("keyword formal", current, [kwType, expression],
        AType(Solver s){
            s.requireSubtype(expression, kwType, error(expression, "Initializing expression of type %t expected, found %t", kwType, expression));
            return s.getType(kwType);
        });
    collect(kwType, expression, c);
}
 
// ---- function declaration --------------------------------------------------

data ReturnInfo = returnInfo(Type returnType);

void collect(FunctionDeclaration decl, Collector c){
//println("********** function declaration: <decl.signature.name>");
    
    tagsMap = getTags(decl.tags);
    if(ignoreCompiler(tagsMap)) { println("ignore: function <decl.signature.name>"); return; }
    
    <deprecated, deprecationMessage> = getDeprecated(tagsMap);
    signature = decl.signature;
    fname = signature.name;
    parentScope = c.getScope();
       
    c.enterLubScope(decl);
        scope = c.getScope();
        c.setScopeInfo(scope, functionScope(), returnInfo(signature.\type));
        
        dt = defType([signature], AType(Solver s) {
                 ft = s.getType(signature);
                 if(signature.parameters is varArgs) ft.varArgs = true;
                 if(deprecated) {
                    ft.deprecationMessage = deprecationMessage;
                 }
                 return ft;
             });
        dt.vis = getVis(decl.visibility, publicVis());
        if(!isEmpty(tagsMap)) dt.tags = tagsMap;
         
        c.defineInScope(parentScope, prettyPrintName(fname), functionId(), fname, dt); 
        
        if(decl is expression || decl is conditional){
            if(containsReturn(decl.expression)){
                ; // We assume that the expression returns a value via a return (and that is checked for compatibility with return type);
                  // We do in this case not check that the type of the expression as a whole is compatible with the return type.
                  // TODO: cover the case that we leave the expression via a return AND via the value of the expression as a whole
            } else {
                c.requireEager("check on return type `<fname>`", decl.expression, [decl.expression], makeReturnRequirement(decl.expression, signature.\type));
            }
            collect(decl.expression, c);
        } 
        if(decl is conditional){
            conditions = [cond | cond <- decl.conditions];
            storeAllowUseBeforeDef(decl, decl.expression, c);
            c.requireEager("when conditions", decl.conditions, conditions,
                void (Solver s){
                for(cond <- conditions){
                    condType = s.getType(cond);
                    if(!s.isFullyInstantiated(condType)){
                        s.requireUnify(condType, abool(), error(cond, "Cannot unify condition with `bool`, found %t", cond));
                        condType = s.instantiate(condType);
                    }           
                    s.requireSubtype(cond, abool(), error(cond, "Condition should be `bool`, found %t", cond));
                }
            });
            collect(decl.conditions, c);
        }
        if(decl is \default) collect(decl.body, c);
        collect(decl.signature, c);
    c.leaveScope(decl);
}

bool containsReturn(Tree t) = /(Statement) `return <Statement statement>` := t;

void collect(Signature signature, Collector c){
    returnType  = signature.\type;
    parameters  = signature.parameters;
    kwFormals   = getKwFormals(parameters);
  
    for(tv <- getTypeVars(returnType)){
        c.use(tv.name, {typeVarId()});
    }
   
    c.calculate("signature", signature, [returnType, parameters],// + kwFormals<0>,
        AType(Solver s){
            tformals = s.getType(parameters);
            if(atypeList(elems) !:= tformals) tformals = atypeList([tformals]);
            res = afunc(s.getType(returnType), tformals, computeKwFormals(kwFormals, s));
            //println("signature <signature> ==\> <res>");
            return res;
        });
        
    collect(returnType, parameters, c);
    
    //if(signature has exceptions){             // TODO: reconsider
    //    collect(signature.exceptions, c);
    //}
}

list[Pattern] getFormals(Parameters parameters)
    = [pat | Pattern pat <- parameters.formals.formals];

list[KeywordFormal] getKwFormals(Parameters parameters)
    =  parameters.keywordFormals is \default ? [kwf | kwf <- parameters.keywordFormals.keywordFormalList] : [];

set[TypeVar] getTypeVars(Tree t ){
    return {tv | /TypeVar tv := t };
}

void collect(Parameters parameters, Collector c){
    formals = getFormals(parameters);
    kwFormals = getKwFormals(parameters);
   
    typeVarsInFunctionParams = [*getTypeVars(t) | t <- formals + kwFormals];
    //println("typeVarsInFunctionParams: <size(typeVarsInFunctionParams)>");
    seenTypeVars = {};
    for(tv <- typeVarsInFunctionParams){
        //println(tv);
        tvname = "<tv.name>";
        if(tvname in seenTypeVars){
            c.use(tv.name, {typeVarId()});
        } else {
            seenTypeVars += tvname;
            c.define(tvname, typeVarId(), tv.name, defType(tv));
        }
    }
   
    for(KeywordFormal kwf <- kwFormals){
        fieldName = prettyPrintName(kwf.name);
        kwfType = kwf.\type;
        c.define(fieldName, variableId(), kwf.name, defType([kwfType], AType(Solver s) { return s.getType(kwfType)[label=fieldName]; }));
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
            //for(f <- formals){
            //    c.calculate("formal `<f>`", f, [], AType(Solver s) { return getPatternType(f, avalue(), scope, s); });
            //}
            c.calculate("formals", parameters, [],
                AType(Solver s) { 
                        res = atypeList([getPatternType(f, avalue(), scope, s) | f <- formals]);
                        //res = atypeList([unset(s.getType(f), "label") | f <- formals]); /* unset ? */ 
                       //println("parameters <parameters> at <getLoc(parameters.formals)> ==\> <res>");
                        return res;
                }); 
       }
       collect(kwFormals, c);
    endPatternScope(c);
}

//void collect(current:(Formals)`<{Pattern ","}* formals>`, Collector c){
//    formalsList = [f | f <- formals];
//    if(isEmpty(formalsList)){
//        c.fact(current, atypeList([]));
//    } else {
//        c.calculate("formals `<formals>`", current, formalsList,
//            AType(Solver s) { 
//                return atypeList([unset(s.getType(f), "label") | f <- formalsList]); //unset ?
//             }); 
//        collect(formalsList, c);
//    }
//} 

list[Keyword] computeKwFormals(list[KeywordFormal] kwFormals, Solver s){
    return [<s.getType(kwf.\type)[label=prettyPrintName(kwf.name)], kwf.expression> | kwf <- kwFormals];
}

void(Solver) makeReturnRequirement(Tree expr, Type returnType)
    = void(Solver s) { 
        actualRetType = s.getType(returnType);
          
        exprType = s.getType(expr);
        Bindings bindings = ();
        try   bindings = matchRascalTypeParams(exprType, actualRetType, bindings, bindIdenticalVars=true);
        catch invalidMatch(str reason):
              if(asubtype(exprType, actualRetType))
                s.report(error(expr, reason));
          
        iexprType = xxInstantiateRascalTypeParameters(exprType, bindings, s);

        if(s.isFullyInstantiated(iexprType)){
            s.requireSubtype(iexprType, actualRetType, error(expr, "Return type should be subtype of %t, found %t", actualRetType, iexprType));
        } else
            if(!s.unify(iexprType, actualRetType)){
                s.requireSubtype(iexprType, actualRetType, error(expr, "Return type should be subtype of %t, found %t", actualRetType, iexprType));
        }   
     };

// ---- return statement (closely interacts with function declaration) --------

void collect(current: (Statement) `return <Statement statement>`, Collector c){  
    functionScopes = c.getScopeInfo(functionScope());
    if(isEmpty(functionScopes)){
        c.report(error(current, "Return outside a function declaration"));
        collect(statement, c);
        return;
    }
    for(<scope, scopeInfo> <- functionScopes){
        if(returnInfo(Type returnType) := scopeInfo){
           c.requireEager("check return type", current, [returnType], makeReturnRequirement(statement, returnType));
           c.calculate("return type", current, [statement], AType (Solver s){ return s.getType(statement); });
           collect(statement, c);
           return;
        } else {
            throw rascalCheckerInternalError(getLoc(current), "Inconsistent info from function scope: <scopeInfo>");
        }
    }
    throw rascalCheckerInternalError(getLoc(current), "No surrounding function scope found");
}

// ---- alias declaration -----------------------------------------------------

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> alias <QualifiedName name> = <Type base>;`, Collector c){
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore: <current>"); return; }
    
    aliasName = prettyPrintName(name);
    c.define(aliasName, aliasId(), name, defType([base], AType(Solver s) { return s.getType(base); }));
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

list[TypeVar] getTypeParameters(UserType userType)
    = userType is parametric ? [p.typeVar | p <- userType.parameters] : [];

list[Sym] getTypeParameters(Sym sym)
    =  [p |/Sym p := sym, p is parameter];

list[KeywordFormal] getCommonKwFormals(Declaration decl)
   = decl.commonKeywordParameters is present ?  [kwf | kwf <- decl.commonKeywordParameters.keywordFormalList] : [];

// ---- data declaration ------------------------------------------------------
bool inADTdeclaration(Collector c){
    return <Tree adt, list[KeywordFormal] commonKwFormals, loc adtParentScope> := c.top(currentAdt);
}

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters>;`, Collector c){
    return dataDeclaration(tags, current, [], c);
}
void collect (current: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`, Collector c)
    = dataDeclaration(tags, current, [v | v <- variants], c);

void dataDeclaration(Tags tags, Declaration current, list[Variant] variants, Collector c){
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore: <current>"); return; }
    
    userType = current.user;
    adtName = prettyPrintName(userType.name);
    commonKeywordParameters = getCommonKwFormals(current);
    typeParameters = getTypeParameters(userType);
    
    dt = isEmpty(typeParameters) ? defType(aadt(adtName, [], dataSyntax()))
                                 : defType(typeParameters, AType(Solver s) { return aadt(adtName, [ s.getType(tp) | tp <- typeParameters], dataSyntax()); });
    
    if(!isEmpty(commonKeywordParameters)) dt.commonKeywordFields = commonKeywordParameters;
    c.define(adtName, dataId(), current, dt);
       
    adtParentScope = c.getScope();
    c.enterScope(current);
        for(tp <- typeParameters){
            c.define("<tp.name>", typeVarId(), tp.name, defType(tp));
        }
        collect(typeParameters, c);
        collect(current.commonKeywordParameters, c);
   
        // visit all the variants in the parent scope of the data declaration
        c.push(currentAdt, <current, commonKeywordParameters, adtParentScope>);
            collect(variants, c);
        c.pop(currentAdt);
    c.leaveScope(current);
}

//void collect(current: (TypeArg) `<Type tp>`, Collector c){
//    c.fact(current, tp);
//    collect(tp, c);
//}

void collect(current: (TypeArg) `<Type tp> <Name name>`, Collector c){
	c.calculate("TypeArg <name>", name, [tp], AType(Solver s){
	   res = (s.getType(tp)[label=unescape("<name>")]);
	   return res;
     });
	c.fact(current, name);
	collect(tp, c);
}

list[TypeArg] getFormals(Variant variant)
    = [ta | TypeArg ta <- variant.arguments];

list[KeywordFormal] getKwFormals(Variant variant)
    =  variant.keywordArguments is \default ? [kwf | kwf <- variant.keywordArguments.keywordFormalList] : [];

void collect(current:(Variant) `<Name name> ( <{TypeArg ","}* arguments> <KeywordFormals keywordArguments> )`, Collector c){
    formals = getFormals(current);
    kwFormals = getKwFormals(current);
   
    //typeVarsInConstructorParams = {*getTypeVars(t) | t <- formals + kwFormals};
    //typeVarNamesInConstructor = {"<tv.name>" | tv <- typeVarsInConstructorParams};
    
    // Define all fields in the outer scope of the data declaration in order to be easily found there.
    
    for(ta <- formals){
        if(ta is named){
            fieldName = prettyPrintName(ta.name);
            fieldType = ta.\type;
            c.define(fieldName, fieldId(), ta.name, defType([fieldType], AType(Solver s) { return s.getType(fieldType)[label=fieldName]; }));
        }
    }
    
    for(KeywordFormal kwf <- kwFormals){
        fieldName = prettyPrintName(kwf.name);
        kwfType = kwf.\type;
        c.define(fieldName, fieldId(), kwf.name, defType([kwfType], AType(Solver s) { return s.getType(kwfType)[label=fieldName]; }));    
    }

    scope = c.getScope();
    if(<Tree adt, list[KeywordFormal] commonKwFormals, loc adtParentScope> := c.top(currentAdt)){
        c.defineInScope(adtParentScope, prettyPrintName(name), constructorId(), name, defType(adt + formals + kwFormals + commonKwFormals,
            AType(Solver s){
                adtType = s.getType(adt);
                //typeVarNamesInADT = {"<tv.pname>" | tv <- adtType.parameters};
                //if(!(typeVarNamesInADT <= typeVarNamesInConstructor)) s.report(warning(current, "Type parameter(s) %q not defined in constructor %q", typeVarNamesInADT - typeVarNamesInConstructor, "<name>"));
                kwFormals2 = [<s.getType(kwf.\type)[label=prettyPrintName(kwf.name)], kwf.expression> | kwf <- kwFormals + commonKwFormals];
                return acons(adtType, [unset(s.getType(f), "label") | f <- formals], kwFormals2);
            }));
        c.fact(current, name);
    } else {
        throw "collect Variant: currentAdt not found";
    }
    // The standard rules would declare arguments and kwFormals as variableId();
    for(arg <- arguments) { collect(arg.\type, c); c.fact(arg, arg.\type); }
    for(kwa <- kwFormals) { collect(kwa.\type, kwa.expression, c); c.fact(kwa, kwa.\type); }
} 

// ---- syntax definition -----------------------------------------------------

void collect(current: (SyntaxDefinition) `<Visibility vis> layout <Sym defined> = <Prod production>;`, Collector c){
    declareSyntax(current, layoutSyntax(), layoutId(), c, vis=getVis(vis, publicVis()));
} 

void collect (current: (SyntaxDefinition) `lexical <Sym defined> = <Prod production>;`, Collector c){
    declareSyntax(current, lexicalSyntax(), lexicalId(), c);
}

void collect (current: (SyntaxDefinition) `keyword <Sym defined> = <Prod production>;`, Collector c){
    declareSyntax(current, keywordSyntax(), keywordId(), c);
} 

void collect (current: (SyntaxDefinition) `<Start strt> syntax <Sym defined> = <Prod production>;`, Collector c){
    declareSyntax(current, contextFreeSyntax(), nonterminalId(), c, isStart=strt is present);
}

void declareSyntax(SyntaxDefinition current, SyntaxRole syntaxRole, IdRole idRole, Collector c, bool isStart=false, Vis vis=publicVis()){
    //println("declareSyntax: <current>");
    Sym defined = current.defined;
    Prod production = current.production;
    nonterminalType = defsym2AType(defined, syntaxRole);
     
    if(isADTType(nonterminalType)){
        adtName = nonterminalType.adtName;
       
        typeParameters = getTypeParameters(defined);
        if(!isEmpty(typeParameters)){
            nonterminalType = nonterminalType[parameters=[ aparameter("<tp.nonterminal>", avalue())| tp <- typeParameters ]];
        }
        
        dt = defType(isStart ? \start(nonterminalType) : nonterminalType);
        dt.vis = vis;        
        
        // Define the syntax symbol itself and all labelled alternatives as constructors
        c.define(adtName, idRole, current, dt);

        adtParentScope = c.getScope();
        c.enterScope(current);
            for(tp <- typeParameters){
                c.define("<tp.nonterminal>", typeVarId(), tp.nonterminal, defType(aparameter("<tp.nonterminal>", avalue())));
            }
            //collect(typeParameters, c);
            
            // visit all the productions in the parent scope of the syntax declaration
            c.push(currentAdt, <current, [], adtParentScope>);
                collect(production, c);
            c.pop(currentAdt);
        c.leaveScope(current);
    } else {
        c.report(error(defined, "Lhs of syntax definition not supported"));
    }
}

// ---- Prod ------------------------------------------------------------------
        
AProduction getProd(AType adtType, Tree tree, Solver s){
    symType = s.getType(tree);
    if(aprod(AProduction p) := symType) return p;    
    return prod(adtType, [symType], src=getLoc(tree));
}

void collect(current: (Prod) `: <Name referenced>`, Collector c){
    throw "reference not yet implemented";
}

AProduction computeProd(Tree current, AType adtType, ProdModifier* modifiers, list[Sym] symbols, Solver s){
    args = [s.getType(sym) | sym <- symbols];   
    m2a = mods2attrs(modifiers);
    src=getLoc(current);
    p = isEmpty(m2a) ? prod(adtType, args, src=src) : prod(adtType, args, attributes=m2a, src=src);
    
    forbidConsecutiveLayout(current, args, s);
    if(!isEmpty(args)){
        requireNonLayout(current, args[0], "at begin of production", s);
        requireNonLayout(current, args[-1], "at end of production", s);
    }
    return associativity(adtType, \mods2assoc(modifiers), p);
}

void collect(current: (Prod) `<ProdModifier* modifiers> <Name name> : <Sym* syms>`, Collector c){
    symbols = [sym | sym <- syms];
    
    typeParametersInSymbols = {*getTypeParameters(sym) | sym <- symbols };
    for(tv <- typeParametersInSymbols){
        c.use(tv.nonterminal, {typeVarId()});
    }
    
    if(<Tree adt, list[KeywordFormal] commonKwFormals, loc adtParentScope> := c.top(currentAdt)){
        // Compute the production type
        c.calculate("named production", current, adt + symbols,
            AType(Solver s){
                p = computeProd(current, s.getType(adt)[label=unescape("<name>")], modifiers, symbols, s);      
                return aprod(p); 
            });
            
        // Define the constructor (using a location annotated with "cons" to differentiate from the above)
        c.defineInScope(adtParentScope, "<name>", constructorId(), getLoc(current)[fragment="cons"], defType([current], 
            AType(Solver s){
                ptype = s.getType(current);
                if(aprod(AProduction cprod) := ptype){
                    def = cprod.def;
                    fields = [ t | sym <- symbols, tsym := s.getType(sym), t := removeConditional(tsym), isNonTerminalType(t), t.label?];
                    def = \start(sdef) := def ? sdef : unset(def, "label");
                    return acons(def, fields, [], label=unescape("<name>"));
                 } else throw "Unexpected type of production: <ptype>";
            }));
        collect(symbols, c);
    } else {
        throw "collect Named Prod: currentAdt not found";
    }
}

void collect(current: (Prod) `<ProdModifier* modifiers> <Sym* syms>`, Collector c){
    symbols = [sym | sym <- syms];
    typeParametersInSymbols = {*getTypeParameters(sym) | sym <- symbols };
    for(tv <- typeParametersInSymbols){
        c.use(tv.nonterminal, {typeVarId()});
    }
 
    if(<Tree adt, list[KeywordFormal] commonKwFormals, loc adtParentScope> := c.top(currentAdt)){
        c.calculate("unnamed production", current, adt + symbols,
            AType(Solver s){
                return aprod(computeProd(current, s.getType(adt), modifiers, symbols, s));
            });
        collect(symbols, c);
    } else {
        throw "collect Named Prod: currentAdt not found";
    }
}

private AProduction associativity(AType nt, nothing(), AProduction p) = p;
private default AProduction associativity(AType nt, just(Associativity a), AProduction p) = associativity(nt, a, {p});


void collect(current: (Prod) `<Assoc ass> ( <Prod group> )`, Collector c){
    asc = Associativity::\left();
    switch("<ass>"){
    case "assoc":       asc = Associativity::\left();
    case "left":        asc = Associativity::\left();
    case "non-assoc":   asc = Associativity::\left();
    case "right":       asc = Associativity::\left();
    }
    
    if(<Tree adt, list[KeywordFormal] commonKwFormals, loc adtParentScope> := c.top(currentAdt)){
        c.calculate("assoc", current, [adt, group],
            AType(Solver s){
                adtType = s.getType(adt);
                return aprod(associativity(adtType, asc, {getProd(adtType, group, s)}));
            });
        collect(group, c);
    } else {
        throw "collect Named Prod: currentAdt not found";
    }
}

void collect(current: (Prod) `<Prod lhs> | <Prod rhs>`,  Collector c){
    if(<Tree adt, list[KeywordFormal] commonKwFormals, loc adtParentScope> := c.top(currentAdt)){
        c.calculate("alt production", current, [adt, lhs, rhs],
            AType(Solver s){
                adtType = s.getType(adt);
                return aprod(choice(adtType, {getProd(adtType, lhs, s), getProd(adtType, rhs, s)}));
            });
        c.push(inAlternative, true);
            collect(lhs, rhs, c);
        c.pop(inAlternative);
        if(isEmpty(c.getStack(inAlternative))){
              c.define("production", nonterminalId(), current, defType(current));
        }
    } else {
        throw "collect alt: currentAdt not found";
    }
}
 
void collect(current: (Prod) `<Prod lhs> \> <Prod rhs>`,  Collector c){
    if(<Tree adt, list[KeywordFormal] commonKwFormals, loc adtParentScope> := c.top(currentAdt)){
        c.calculate("first production", current, [adt, lhs, rhs],
            AType(Solver s){
                adtType = s.getType(adt);
                return aprod(priority(adtType, [getProd(adtType, lhs, s), getProd(adtType, rhs, s)]));
            });
        collect(lhs, rhs, c);
    } else {
        throw "collect alt: currentAdt not found";
    }
}

default void collect(Prod current, Collector c){
    throw "collect Prod, missed case <current>";
}