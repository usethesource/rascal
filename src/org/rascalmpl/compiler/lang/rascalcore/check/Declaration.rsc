module lang::rascalcore::check::Declaration
 
extend analysis::typepal::TypePal;

import lang::rascal::\syntax::Rascal;

import lang::rascalcore::check::ATypeExceptions;
import lang::rascalcore::check::ConvertType;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::ATypeInstantiation;

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::TypePalConfig;

import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Productions;

import lang::rascalcore::check::Import;
import lang::rascalcore::check::Pattern;

import util::Reflective;
import Node;
import String;
import IO;
import Set;
import Map;
import String;

// ---- Rascal declarations

void collect(current: (Module) `<Header header> <Body body>`, Collector c){
  
    if(current has top) current = current.top;
    mname = prettyPrintName(header.name);
    checkModuleName(getLoc(current), header.name, c);
    
    tagsMap = getTags(header.tags);
    
    //if(ignoreCompiler(tagsMap)) {println("*** ignore module <mname>"); return; }
    //println("*** collect module <mname>, <getLoc(current)>");
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

// ---- import

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

// ---- extend

void collect(current: (Import) `extend <ImportedModule m> ;`, Collector c){    
    c.useViaPath(m, {moduleId()}, extendPath());
    c.push(key_imported, <unescape("<m.name>"), current>);
    c.push(key_extended, unescape("<m.name>"));
    c.push(key_extend_graph, <c.top(key_current_module), "<m.name>">);
}

// ---- variable declaration

Vis getVis((Visibility) `private`)  = privateVis();
Vis getVis((Visibility) `public`)   = publicVis();
Vis getVis((Visibility) ``)         = defaultVis();

// Note: Rascal's closures are mutable, therefore we need an extra closure when creating
// several requirements from the same function context. In this way the value of expr becomes fixed
void(Solver s) makeVarInitRequirement(Expression expr, AType initType, loc scope)
    = void(Solver s) { 
           expandedInitType = expandUserTypes(initType, scope, s);
           expandedExprType = expandUserTypes(s.getType(expr), scope, s);
           s.requireSubtype(expandedExprType, expandedInitType, error(expr, "Type of initialization should be subtype of %t, found %t", expandedInitType, expandedExprType));
         };
         
void collect(current: (Declaration) `<Tags tags> <Visibility visibility> <Type \type> <{Variable ","}+ variables> ;`, Collector c){

    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore <current>"); return; }
     
    vis = getVis(current.visibility);
    if(vis == defaultVis()){
        vis = privateVis();
    }
    varType = convertType(current.\type, c);
    scope = c.getScope();
    
    for(var <- variables){
        dt = defType([], AType(Solver s){ 
            return expandUserTypes(varType, scope, s); });
        dt.vis = vis;
        if(!isEmpty(tagsMap)) dt.tags = tagsMap;
        c.define(prettyPrintName(var.name), variableId(), var.name, dt);
        
        c.require("variable type is defined", current.\type, [],
            void (Solver s){ //try 
                expandUserTypes(varType, scope, s);
                //catch TypeUnavailable(): report(error(current.\type, "Undeclared type <fmt("<current.\type>")>");
            });
        if(var is initialized){
            //if(!(var.initial is closure)) 
            c.enterLubScope(var);
                c.require("variable initialization", var.initial, [], makeVarInitRequirement(var.initial, varType, c.getScope()));
                collect(var.initial, c); 
            //if(!(var.initial is closure)) 
            c.leaveScope(var);
        }
    }  
    collect(tags, c);  
}

// ---- annotation

void collect(current: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType> @ <Name name> ;`, Collector c){
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore: <current>"); return; }
    
    vis = getVis(current.visibility);
    if(vis == defaultVis()){
        vis = publicVis();
    }
    
    at = convertType(annoType, c);
    ot = convertType(onType, c);
    qname = convertName(name);
    scope = c.getScope();
    dt = defType([], AType(Solver s) { return aanno(qname.name, expandUserTypes(ot, scope, s), expandUserTypes(at, scope, s)); });
    dt.vis=vis;
    if(!isEmpty(tagsMap)) dt.tags = tagsMap;
    c.define(qname.name, annoId(), name, dt);
    //collect(current, c); 
}
 

// ---- function declaration

data ReturnInfo = returnInfo(AType retType, list[Pattern] formals, set[AType] kwTypeParams);

void collect(FunctionDeclaration decl, Collector c){
//println("********** function declaration: <decl.signature.name>");
    
    vis = getVis(decl.visibility);
    if(vis == defaultVis()){
        vis = publicVis();
    }
    
    tagsMap = getTags(decl.tags);
    
    <deprecated, deprecationMessage> = getDeprecated(tagsMap);
    signature = decl.signature;
    isVarArgs = signature.parameters is varArgs;
    fname = signature.name;
    
    if(ignoreCompiler(tagsMap)) { println("ignore: function <fname>"); return; }
    parentScope = c.getScope();
       
    c.enterLubScope(decl);
        scope = c.getScope();
        c.setScopeInfo(scope, functionScope(), false);
        retType = convertType(signature.\type, c);
        <formals, kwTypeParams, kwFormals> = checkFunctionType(scope, retType, signature.parameters, isVarArgs, c);
        
        dt = defType([], AType(Solver s) {
                 expandedRetType = expandUserTypes(retType, scope, s);
                 ft = afunc(expandedRetType, atypeList([expandUserTypes(unset(getPatternType(f, avalue(), scope, s), "label"), scope, s) | f <- formals]), kwFormals);
                 if(isVarArgs) ft.varArgs = true;
                 if(deprecated) {
                    ft.deprecationMessage = deprecationMessage;
                 }
                 return ft;
             });
        dt.vis=vis;
        if(!isEmpty(tagsMap)) dt.tags = tagsMap;
         
        c.defineInScope(parentScope, prettyPrintName(fname), functionId(), fname, dt); 
        
        if(decl is expression || decl is conditional){
            if(containsReturn(decl.expression)){
                ; // We assume that the expression returns a value via a return (and that is checked for compatibility with return type);
                  // We do in this case not check that the type of the expression as a whole is compatible with the return type.
                  // TODO: cover the case that we leave the expression via a return AND via the value of the expression as a whole
            } else {
                c.requireEager("check on return type", decl.expression, [decl.expression], makeReturnRequirement(decl.expression, retType, formals, kwTypeParams, scope));
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
        
    c.leaveScope(decl);
}

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

bool containsReturn(Tree t) = /(Statement) `return <Statement statement>` := t;

list[Keyword] getKeywordFormals({KeywordFormal  "," }+ keywordFormalList, IdRole role, Collector c){    
    return 
        for(KeywordFormal kwf <- keywordFormalList){
            fieldName = prettyPrintName(kwf.name);
            fieldType = convertType(kwf.\type, c)[label=fieldName];
            defaultExp = kwf.expression;
            c.define(fieldName, role, kwf.name, defType(fieldType));
            append </*fieldName, */fieldType, defaultExp>;
        }
}

tuple[list[Pattern] formals, set[AType] kwTypeParams, list[Keyword] kwFormals] checkFunctionType(loc scope, AType retType, Parameters params, bool isVarArgs, Collector c){
    formals = [pat | Pattern pat <- params.formals.formals];
    beginPatternScope("parameter", c);
    if(isVarArgs){
        collect(formals[0..-1], c);
        collectAsVarArg(formals[-1], c);
    } else {
        collect(formals, c);
    }
    endPatternScope(c);
    
    kwFormals = params.keywordFormals is \default ? getKeywordFormals(params.keywordFormals.keywordFormalList, variableId(), c) : [];
    
    kwTypeParams = {*collectAndUnlabelRascalTypeParams(kwf.fieldType) | kwf <- kwFormals};
    c.setScopeInfo(scope, functionScope(), returnInfo(retType, formals, kwTypeParams));
    
    c.requireEager("bound type parameters", params, [], //formals,
        void (Solver s) { 
             expandedRetType = expandUserTypes(retType, scope, s);
             typeParamsInFunctionParams = {*collectAndUnlabelRascalTypeParams(getPatternType(f, avalue(), scope, s)) | f <- formals} + kwTypeParams;
             typeParamsInReturn = collectAndUnlabelRascalTypeParams(expandedRetType);
             if(!(typeParamsInReturn <= typeParamsInFunctionParams)){
                unbound = typeParamsInReturn - typeParamsInFunctionParams;
                s.report(error(params, "Unbound: %v type parameter(s) %t in return type", size(unbound), unbound));
             }
        });
     return <formals, kwTypeParams, kwFormals>;
}

void(Solver) makeReturnRequirement(Tree expr, AType retType, list[Pattern] formals, set[AType] kwTypeParams, loc scope)
       = void(Solver s) { 
              expandedReturnType = expandUserTypes(retType, scope, s);
              typeVarsInParams = {*collectAndUnlabelRascalTypeParams(getPatternType(f, avalue(), scope, s)) | f <- formals} + kwTypeParams;
              typeVarsInReturn = collectAndUnlabelRascalTypeParams(expandedReturnType);
              if(!(typeVarsInReturn <= typeVarsInParams)){
                unbound = typeVarsInReturn - typeVarsInParams;
                s.report(error(expr, "Unbound: %v type parameter(s) %t in return type", size(unbound), unbound));
              }
              
              texpr = expandUserTypes(s.getType(expr), scope, s);
              Bindings bindings = ();
              try   bindings = matchRascalTypeParams(texpr, expandedReturnType, bindings, bindIdenticalVars=true);
              catch invalidMatch(str reason):
                    if(asubtype(texpr, expandedReturnType))
                        s.report(error(expr, reason));
              
              try {
                 itexpr = texpr;
                 if(!isEmpty(bindings)){
                    itexpr = instantiateRascalTypeParams(texpr, bindings);
                 }
                 if(s.isFullyInstantiated(itexpr)){
                    s.requireSubtype(itexpr, expandedReturnType, error(expr, "Return type should be subtype of %t, found %t", expandedReturnType, itexpr));
                 } else
                 if(!s.unify(itexpr, expandedReturnType)){
                    s.requireSubtype(itexpr, expandedReturnType, error(expr, "Return type should be subtype of %t, found %t", expandedReturnType, itexpr));
                 }
               } catch invalidInstantiation(str msg): {
                    s.report(error(expr, msg));
               }
            
            
         };

// ---- return statement (closely interacts with function declaration)

void collect(current: (Statement) `return <Statement statement>`, Collector c){  
    functionScopes = c.getScopeInfo(functionScope());
    if(isEmpty(functionScopes)){
        c.report(error(current, "Return outside a function declaration"));
        collect(statement, c);
        return;
    }
    for(<scope, scopeInfo> <- functionScopes){
        if(returnInfo(AType retType, list[Pattern] formals, set[AType] kwTypeParams) := scopeInfo){
           c.requireEager("check return type", current, [], makeReturnRequirement(statement, retType, formals, kwTypeParams, scope));
           c.calculate("return type", current, [statement], AType (Solver s){ return s.getType(statement); });
           collect(statement, c);
           return;
        } else {
            throw rascalCheckerInternalError(getLoc(current), "Inconsistent info from function scope: <scopeInfo>");
        }
    }
    throw rascalCheckerInternalError(getLoc(current), "No surrounding function scope found");
}

// ---- alias declaration

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType userType> = <Type base>;`, Collector c){
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore: <current>"); return; }
    
    aliasName = prettyPrintName(userType.name);
    aliasedType = convertType(base, c);
    aliasTypeVarsAsList = getTypeParameters(userType, c);
    scope = c.getScope();
                     
    aliasType = aalias(aliasName, aliasTypeVarsAsList, aliasedType);
    c.define(aliasName, aliasId(), userType.name, defType([], AType(Solver s) { return expandUserTypes(aliasedType, scope, s); }));
} 


list[AType] getTypeParameters(UserType userType, Collector c){
    if(userType is parametric){
       return
            for(p <- userType.parameters){
                ptype = convertType(p, c);
                if(!isRascalTypeParam(ptype)){
                  c.report(error(p, "Only type parameter allowed, found %t", ptype));
                }
                append unset(ptype, "label"); // TODO: Erase labels to enable later subset check
            }
    }
    return [];
}

// ---- data declaration

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters>;`, Collector c)
    = dataDeclaration(tags, current, [], c);

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`, Collector c)
    = dataDeclaration(tags, current, [v | v <- variants], c);

void dataDeclaration(Tags tags, Declaration current, list[Variant] variants, Collector c){
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore: <current>"); return; }
    userType = current.user;
    commonKeywordParameters = current.commonKeywordParameters;
    adtName = prettyPrintName(userType.name);
    
    dataTypeVarsAsList = getTypeParameters(userType, c);
    dataTypeVars = toSet(dataTypeVarsAsList);
    
    commonKwFields = [];
    if(commonKeywordParameters is present){
        commonKwFields = getKeywordFormals(commonKeywordParameters.keywordFormalList, fieldId(), c);
    }
    adtType = aadt(adtName, dataTypeVarsAsList, dataSyntax());
    
    set[AType] /*rel[str, AType]*/ allConsFields = {};
    rel[str, Name, DefInfo] allConstructorDefines = {};
    for(Variant v <- variants){
        consName = prettyPrintName(v.name);
        allFieldTypeVars = {};
        fields = 
            for(TypeArg ta <- v.arguments){
                fieldName = ta has name ? prettyPrintName(ta.name) : "";
                fieldType = convertType(ta.\type, c)[label=fieldName];
                
                if(!isEmpty(fieldName)){
                    c.define(fieldName, fieldId(), ta, defType(fieldType));
                    allConsFields += fieldType; //<fieldName, fieldType>;
                }
                allFieldTypeVars += collectAndUnlabelRascalTypeParams(fieldType);
                append fieldType; //<fieldName, fieldType>;
            }
    
       kwFields = [];
       if(v.keywordArguments is \default){
          kwFields += getKeywordFormals(v.keywordArguments.keywordFormalList, fieldId(), c);
          for(<kwt, kwd> <- kwFields){
              allFieldTypeVars += collectAndUnlabelRascalTypeParams(kwt);
              allConsFields += kwt; //<kwn, kwt>;
          }
       }
       // TODO: is this check necessary?
       // TODO: take different bounds into account
       //if(!(dataTypeVars <= allFieldTypeVars)){
       //   c.report(warning(v, "Unbound type parameter <fmt(dataTypeVars - allFieldTypeVars)>");
       //}
       consType = acons(adtType, /*consName,*/ fields, kwFields, label=consName);
       //println(consType);
       c.define(consName, constructorId(), v.name, defType(consType));
       allConstructorDefines += <consName, v.name, defType(consType)>;
    }
    dt = defType(adtType);
    dt.constructorFields=allConsFields;
    constructors = {di.atype | DefInfo di <- allConstructorDefines<2>};
    if(!isEmpty(constructors)) dt.constructors = constructors;
    if(!isEmpty(commonKwFields)) dt.commonKeywordFields = commonKwFields;
    if(!isEmpty(tagsMap)) dt.tags = tagsMap;
    c.define(adtName, dataId(), current, dt);
    
    // Additionaly declare all constructors inside the scope of the ADT to make them reachable via fully qualified names
    c.enterScope(current);
    for(<consName, vname, defT> <- allConstructorDefines){
        //println("local defines in <current@\loc>: <consName>, constructorId(), <vname>, <defT>");
        c.define(consName, constructorId(), vname, defT);
    }
    c.leaveScope(current);
}

// ---- syntax definition

void collect(current: (SyntaxDefinition) `<Visibility vis> layout <Sym defined> = <Prod production>;`, Collector c){
    //println("LAYOUT: <current>");
    nonterminalType = defsym2AType(defined, layoutSyntax());
    declareSyntax(current, getVis(vis), defined, nonterminalType, false, production, layoutId(), c);
    collect(production, c);
} 

void collect (current: (SyntaxDefinition) `lexical <Sym defined> = <Prod production>;`, Collector c){
    //println("LEXICAL: <current>");
    nonterminalType = defsym2AType(defined, lexicalSyntax());
    declareSyntax(current, publicVis(), defined, nonterminalType, false, production, lexicalId(), c);
    collect(production, c);
}

void collect (current: (SyntaxDefinition) `keyword <Sym defined> = <Prod production>;`, Collector c){
    //println("KEYWORD: <current>");
    nonterminalType = defsym2AType(defined, keywordSyntax());
    declareSyntax(current, publicVis(), defined, nonterminalType, false, production, keywordId(), c);
    collect(production, c);
} 

void collect (current: (SyntaxDefinition) `<Start strt> syntax <Sym defined> = <Prod production>;`, Collector c){
    //println("SYNTAX: <current>");
    nonterminalType = defsym2AType(defined, contextFreeSyntax());
    isStart = strt is present;

    declareSyntax(current, publicVis(), defined, nonterminalType, isStart, production, nonterminalId(), c);
    collect(production, c);
}

void declareSyntax(SyntaxDefinition current, Vis vis, Sym defined, AType nonterminalType, bool isStart, Prod production, IdRole idRole, Collector c){
    //println("declareSyntax: <defined>, <nonterminalType>");
    AProduction pr = prod2prod(nonterminalType, production);
    //println("declareSyntax pr: <pr>");
    
    if(isADTType(nonterminalType)){
        ntName = nonterminalType.adtName;
       
        dt = defType(nonterminalType);
        if(isStart) dt.isStart = true;
        dt.vis = vis;
        productions = choice(definedType, set[AProduction] prods) := pr ? prods : {pr};
        
        typeVars = toSet(nonterminalType.parameters);
         
        allTypeVars = collectAndUnlabelRascalTypeParams(productions);
         if(!(typeVars >= allTypeVars)){
          c.report(warning(v, "Unbound type parameter %v", allTypeVars - typeVars));
       }
        dt.productions = productions;
        //iprintln(productions);
      
        //  Declare all named fields that occur in the productions
        constructorFields = { *getFields(p) | AProduction p <- productions };
        //println("constructorFields: <constructorFields>");
        for(<str nm, loc def, AType tp> <- constructorFields){
            c.define(nm, fieldId(), def, defType(tp));
        }
  
        if(!isEmpty(constructorFields))  dt.constructorFields = constructorFields<2>; //<0,2>;
                                            
        c.define(ntName, idRole, current, dt);
        //println("define <ntName>, dataId(), <dt>");
        allConstructorDefines = {};
        for(p <- productions){
           //println("\nprod2cons: <p> ==\>\n\t<prod2cons(p)>");
           for(<k, cns> <- prod2cons(p)){
               c.define(cns.label/*consName*/, constructorId(), k, defType(cns));
               allConstructorDefines += <cns.label/*consName*/, k, defType(cns)>;
            }
        }
        // Additionaly declare all constructors inside the scope of the ADT to make them reachable via fully qualified names
        c.enterScope(current);
        for(<consName, vname, defT> <- allConstructorDefines){
            //println("local defines in <current>: <consName>, constructorId(), <vname>, <defT>");
            c.define(consName, constructorId(), vname, defT);
        }
        c.leaveScope(current);
    } else {
        c.report(error(defined, "Lhs of syntax definition not supported"));
    }
}

rel[str,loc,AType] getFields(choice(AType dt, set[AProduction] alts)) = { *getFields(a) | a <- alts};

rel[str,loc,AType] getFields(\priority(AType def, list[AProduction] choices))
    = { *getFields(c) | c <- choices };
    
rel[str,loc,AType] getFields(\associativity(AType def, Associativity \assoc, set[AProduction] alternatives))
    = { *getFields(a) | a <- alternatives};

rel[str,loc,AType] getFields(\reference(AType def, str cons)) = {};

default rel[str,loc,AType] getFields(AProduction p) // TODO add no no label case
    = {<t.label, p.src[query = "label=<t.label>"], t> | s <- p.asymbols, t := removeConditional(s), (isNonTerminalType(t) || auser(_,_) := t),  t.label?};

void collect(current: (Sym) `<Nonterminal nonterminal>`, Collector c){
    c.use(nonterminal, syntaxIds);
    collect(nonterminal, c);
}

void collect(current: (Sym) `<Sym symbol> <NonterminalLabel label>`, Collector c){
    c.define("<label>", fieldId(), label, defType(sym2AType(symbol)));
    collect(symbol, label, c);
}

bool isIterSym((Sym) `<Sym symbol>+`) = true;
bool isIterSym((Sym) `<Sym symbol>*`) = true;
bool isIterSym((Sym) `{ <Sym symbol> <Sym sep> }+`) = true;
bool isIterSym((Sym) `{ <Sym symbol> <Sym sep> }*`) = true;
default bool isIterSym(Sym sym) = false;

void collect(current: (Sym) `<Sym symbol>+`, Collector c){
    if(isIterSym(symbol)) c.report(warning(current, "Nested iteration"));
    collect(symbol, c);
}

void collect(current: (Sym) `<Sym symbol>*`, Collector c){
    if(isIterSym(symbol)) c.report(warning(current, "Nested iteration"));
    collect(symbol, c);
}

void collect(current: (Sym) `{ <Sym symbol> <Sym sep> }+`, Collector c){
    if(isIterSym(symbol)) c.report(warning(current, "Nested iteration"));
    collect(symbol, c);
}

void collect(current: (Sym) `{ <Sym symbol> <Sym sep> }*`, Collector c){
    if(isIterSym(symbol)) c.report(warning(current, "Nested iteration"));
    collect(symbol, c);
}

default rel[loc, AType] prod2cons(AProduction p){
    def = p.def;
    symbols = p.asymbols;
    if(def.label?){
        defLabel = def.label;
        fields = [ t | s <- symbols, t := removeConditional(s), (isNonTerminalType(t) || auser(nm, _) := t), t.label?];
        //fields = [ <t.label, t> | s <- symbols, t := removeConditional(s), (isNonTerminalType(t) || auser(nm, _) := t), t.label?];
        def = \start(sdef) := def ? sdef : unset(def, "label");
        return { <p.src, acons(def, /*deescape(defLabel),*/ fields, [], label=deescape(defLabel))> };
    }
    return {};
}

rel[loc, AType] prod2cons(choice(AType dt, set[AProduction] alts)) = { *prod2cons(a) | a <- alts};

rel[loc, AType] prod2cons(\priority(AType def, list[AProduction] choices))
    = { *prod2cons(c) | c <- choices };
    
rel[loc, AType] prod2cons(\associativity(AType def, Associativity \assoc, set[AProduction] alternatives))
    = { *prod2cons(a) | a <- alternatives};
    
rel[loc, AType] prod2cons(\reference(AType def, str cons)) = {}; // TODO: implement