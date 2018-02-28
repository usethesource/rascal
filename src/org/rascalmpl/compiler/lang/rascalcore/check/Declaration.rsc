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

void collect(current: (Module) `<Header header> <Body body>`, TBuilder tb){
  
    if(current has top) current = current.top;
    mname = prettyPrintName(header.name);
    checkModuleName(getLoc(current), header.name, tb);
    
    tagsMap = getTags(header.tags);
    
    //if(ignoreCompiler(tagsMap)) {println("*** ignore module <mname>"); return; }
    //println("*** collect module <mname>, <getLoc(current)>");
    <deprecated, deprecationMessage> = getDeprecated(tagsMap);
    
     
    tmod = deprecated ? amodule(mname, deprecationMessage=deprecationMessage) : amodule(mname);
    if(deprecated){
        tb.reportWarning(current, "Deprecated module <mname><isEmpty(deprecationMessage) ? "" : ": " + deprecationMessage>");
    }
    tb.define(mname, moduleId(), current, defType(tmod));
    tb.push(key_processed_modules, mname);
     
    tb.push(key_current_module, mname);
    tb.enterScope(current);
        collectParts(current, tb);
        //collect(header, body, tb);
    tb.leaveScope(current);
    tb.pop(key_current_module);
    getImports(tb);
}

void checkModuleName(loc mloc, QualifiedName qualifiedModuleName, TBuilder tb){
    pcfgVal = tb.getStack("pathconfig");
    if([PathConfig pcfg] := pcfgVal){ 
        mname = prettyPrintName(qualifiedModuleName);
        try {   
            mloc1 = getModuleLocation(mname, pcfg);
            if(mloc.scheme != mloc1.scheme || mloc.authority != mloc1.authority || mloc.path != mloc1.path){
                tb.reportError(qualifiedModuleName, "Module name <fmt(mname)> is incompatible with its file location");
            }
        } catch value e: {
            tb.reportError(qualifiedModuleName, "Module name <fmt(mname)> is not consistent with its file location");
        }
    } else if(isEmpty(pcfgVal)){
        return;
    } else {
        throw rascalCheckerInternalError("Inconsistent value for \"pathconfig\": <tb.getStack("pathconfig")>");
    }
}

// ---- import

void collect(current: (Import) `import <ImportedModule m> ;`, TBuilder tb){ // TODO: warn about direct self-import
    tb.useViaPath(m, {moduleId()}, importPath());
    tb.push(key_imported, <unescape("<m.name>"), current>);
    tb.push(key_import_graph, <tb.top(key_current_module), "<m.name>">);
}

loc timestamp(loc l) = l[fragment="<lastModified(l)>"];

void getImports(TBuilder tb){
    // Do not expand imports, while we are already doing that
    if(!isEmpty(tb.getStack(key_expanding_imports)))   return;
    
    pcfgVal = tb.getStack("pathconfig");
    
    if([PathConfig pcfg] := pcfgVal){ 
        allreadyImported = {};
        if(list[str] processed := tb.getStack(key_processed_modules)){
            allreadyImported = toSet(processed);
        }
       
        tb.push(key_expanding_imports, true);
        solve(allreadyImported){
            if(lrel[str,Tree] importedModules := tb.getStack(key_imported)){
                for(<mname, importStatement> <- importedModules){
                    if(mname in allreadyImported) continue;
                    allreadyImported += mname;
                    reuse = addImport(mname, importStatement, pcfg, tb);
                    if(!reuse){
                        try {
                            mloc = timestamp(getModuleLocation(mname, pcfg));                       
                            println("*** importing <mname> from <mloc>");
                            pt = parseModuleWithSpaces(mloc).top;
                            collect(pt, tb);
                        } catch value e: {
                            tb.reportError(importStatement, "Error during import of <fmt(mname)>: <e>");
                        }
                    }
                }
            } else {
                throw rascalCheckerInternalError("Inconsistent value for \"imported\": <tb.getStack("imported")>");
            }
        }
    } else if(isEmpty(pcfgVal)){
        return;
    } else {
        throw rascalCheckerInternalError("Inconsistent value for \"pathconfig\": <tb.getStack("pathconfig")>");
    }
}

// ---- extend

void collect(current: (Import) `extend <ImportedModule m> ;`, TBuilder tb){    
    tb.useViaPath(m, {moduleId()}, extendPath());
    tb.push(key_imported, <unescape("<m.name>"), current>);
    tb.push(key_extended, unescape("<m.name>"));
    tb.push(key_extend_graph, <tb.top(key_current_module), "<m.name>">);
}

// ---- variable declaration

Vis getVis((Visibility) `private`)  = privateVis();
Vis getVis((Visibility) `public`)   = publicVis();
Vis getVis((Visibility) ``)         = defaultVis();

// Note: Rascal's closures are mutable, therefore we need an extra closure when creating
// several requirements from the same function context. In this way the value of expr becomes fixed
void() makeVarInitRequirement(Expression expr, AType initType, Key scope)
    = () { 
           expandedInitType = expandUserTypes(initType, scope);
           expandedExprType = expandUserTypes(getType(expr), scope);
           subtype(expandedExprType, expandedInitType) || reportError(expr, "Type of initialization should be subtype of <fmt(expandedInitType)>, found <fmt(expandedExprType)>");
         };
         
void collect(current: (Declaration) `<Tags tags> <Visibility visibility> <Type \type> <{Variable ","}+ variables> ;`, TBuilder tb){

    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore <current>"); return; }
     
    vis = getVis(current.visibility);
    if(vis == defaultVis()){
        vis = privateVis();
    }
    varType = convertType(current.\type, tb);
    scope = tb.getScope();
    
    for(var <- variables){
        dt = defType([], AType(){ 
            return expandUserTypes(varType, scope); });
        dt.vis = vis;
        if(!isEmpty(tagsMap)) dt.tags = tagsMap;
        tb.define(prettyPrintName(var.name), variableId(), var.name, dt);
        
        tb. require("variable type is defined", current.\type, [],
            (){ //try 
                expandUserTypes(varType, scope);
                //catch TypeUnavailable(): reportError(current.\type, "Undeclared type <fmt("<current.\type>")>");
            });
        if(var is initialized){
            //if(!(var.initial is closure)) 
            tb.enterLubScope(var);
                tb.require("variable initialization", var.initial, [], makeVarInitRequirement(var.initial, varType, tb.getScope()));
                collect(var.initial, tb); 
            //if(!(var.initial is closure)) 
            tb.leaveScope(var);
        }
    }  
    collect(tags, tb);  
}

// ---- annotation

void collect(current: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType> @ <Name name> ;`, TBuilder tb){
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore: <current>"); return; }
    
    vis = getVis(current.visibility);
    if(vis == defaultVis()){
        vis = publicVis();
    }
    
    at = convertType(annoType, tb);
    ot = convertType(onType, tb);
    qname = convertName(name);
    scope = tb.getScope();
    dt = defType([], AType() { return aanno(qname.name, expandUserTypes(ot, scope), expandUserTypes(at, scope)); });
    dt.vis=vis;
    if(!isEmpty(tagsMap)) dt.tags = tagsMap;
    tb.define(qname.name, annoId(), name, dt);
    //collect(current, tb); 
}
 

// ---- function declaration

data ReturnInfo = returnInfo(AType retType, list[Pattern] formals, set[AType] kwTypeParams);

void collect(FunctionDeclaration decl, TBuilder tb){
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
    parentScope = tb.getScope();
       
    tb.enterLubScope(decl);
        scope = tb.getScope();
        tb.setScopeInfo(scope, functionScope(), false);
        retType = convertType(signature.\type, tb);
        <formals, kwTypeParams, kwFormals> = checkFunctionType(scope, retType, signature.parameters, isVarArgs, tb);
        
        dt = defType([], AType() {
                 expandedRetType = expandUserTypes(retType, scope);
                 ft = afunc(expandedRetType, atypeList([expandUserTypes(unset(getPatternType(f, avalue(), scope), "label"), scope) | f <- formals]), kwFormals);
                 if(isVarArgs) ft.varArgs = true;
                 if(deprecated) {
                    ft.deprecationMessage = deprecationMessage;
                 }
                 return ft;
             });
        dt.vis=vis;
        if(!isEmpty(tagsMap)) dt.tags = tagsMap;
         
        tb.defineInScope(parentScope, prettyPrintName(fname), functionId(), fname, dt); 
        
        if(decl is expression || decl is conditional){
            if(containsReturn(decl.expression)){
                ; // We assume that the expression returns a value via a return (and that is checked for compatibility with return type);
                  // We do in this case not check that the type of the expression as a whole is compatible with the return type.
                  // TODO: cover the case that we leave the expression via a return AND via the value of the expression as a whole
            } else {
                tb.requireEager("check on return type", decl.expression, [decl.expression], makeReturnRequirement(decl.expression, retType, formals, kwTypeParams, scope));
            }
            collect(decl.expression, tb);
        } 
        if(decl is conditional){
            conditions = [c | c <- decl.conditions];
            storeAllowUseBeforeDef(decl, decl.expression, tb);
            tb.requireEager("when conditions", decl.conditions, conditions,
                (){
                for(cond <- conditions){
                    condType = getType(cond);
                    if(!isFullyInstantiated(condType)){
                        unify(condType, abool()) || reportError(cond, "Cannot unify condition with `bool`, found <fmt(cond)>");
                        condType = instantiate(condType);
                    }           
                    subtype(getType(cond), abool()) || reportError(cond, "Condition should be `bool`, found <fmt(cond)>");
                }
            });
            collect(decl.conditions, tb);
        }
        if(decl is \default) collect(decl.body, tb);
        
    tb.leaveScope(decl);
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

list[Keyword] getKeywordFormals({KeywordFormal  "," }+ keywordFormalList, IdRole role, TBuilder tb){    
    return 
        for(KeywordFormal kwf <- keywordFormalList){
            fieldName = prettyPrintName(kwf.name);
            fieldType = convertType(kwf.\type, tb)[label=fieldName];
            defaultExp = kwf.expression;
            tb.define(fieldName, role, kwf.name, defType(fieldType));
            append </*fieldName, */fieldType, defaultExp>;
        }
}

tuple[list[Pattern] formals, set[AType] kwTypeParams, list[Keyword] kwFormals] checkFunctionType(Key scope, AType retType, Parameters params, bool isVarArgs, TBuilder tb){
    formals = [pat | Pattern pat <- params.formals.formals];
    beginPatternScope("parameter", tb);
    if(isVarArgs){
        collect(formals[0..-1], tb);
        collectAsVarArg(formals[-1], tb);
    } else {
        collect(formals, tb);
    }
    endPatternScope(tb);
    
    kwFormals = params.keywordFormals is \default ? getKeywordFormals(params.keywordFormals.keywordFormalList, variableId(), tb) : [];
    
    kwTypeParams = {*collectAndUnlabelRascalTypeParams(kwf.fieldType) | kwf <- kwFormals};
    tb.setScopeInfo(scope, functionScope(), returnInfo(retType, formals, kwTypeParams));
    
    tb.requireEager("bound type parameters", params, [], //formals,
        () { 
             expandedRetType = expandUserTypes(retType, scope);
             typeParamsInFunctionParams = {*collectAndUnlabelRascalTypeParams(getPatternType(f, avalue(), scope)) | f <- formals} + kwTypeParams;
             typeParamsInReturn = collectAndUnlabelRascalTypeParams(expandedRetType);
             if(!(typeParamsInReturn <= typeParamsInFunctionParams)){
                unbound = typeParamsInReturn - typeParamsInFunctionParams;
                reportError(params, "Unbound: <fmt(size(unbound), "type parameter")> <fmt(unbound)> in return type");
             }
        });
     return <formals, kwTypeParams, kwFormals>;
}

void() makeReturnRequirement(Tree expr, AType retType, list[Pattern] formals, set[AType] kwTypeParams, Key scope)
       = () { 
              expandedReturnType = expandUserTypes(retType, scope);
              typeVarsInParams = {*collectAndUnlabelRascalTypeParams(getPatternType(f, avalue(), scope)) | f <- formals} + kwTypeParams;
              typeVarsInReturn = collectAndUnlabelRascalTypeParams(expandedReturnType);
              if(!(typeVarsInReturn <= typeVarsInParams)){
                unbound = typeVarsInReturn - typeVarsInParams;
                reportError(expr, "Unbound: <fmt(size(unbound), "type parameter")> <fmt(unbound)> in return type");
              }
              
              texpr = expandUserTypes(getType(expr), scope);
              Bindings bindings = ();
              try   bindings = matchRascalTypeParams(texpr, expandedReturnType, bindings, bindIdenticalVars=true);
              catch invalidMatch(str reason):
                    if(!subtype(texpr, expandedReturnType))
                        reportError(expr, reason);
              
              try {
                 itexpr = texpr;
                 if(!isEmpty(bindings)){
                    itexpr = instantiateRascalTypeParams(texpr, bindings);
                 }
                 if(isFullyInstantiated(itexpr)){
                    subtype(itexpr, expandedReturnType) || reportError(expr, "Return type should be subtype of <fmt(expandedReturnType)>, found <fmt(itexpr)>");
                 } else
                 if(!unify(itexpr, expandedReturnType)){
                    subtype(itexpr, expandedReturnType) || reportError(expr, "Return type should be subtype of <fmt(expandedReturnType)>, found <fmt(itexpr)>");
                 }
               } catch invalidInstantiation(str msg): {
                    reportError(expr, msg);
               }
            
            
         };

// ---- return statement (closely interacts with function declaration)

void collect(current: (Statement) `return <Statement statement>`, TBuilder tb){  
    functionScopes = tb.getScopeInfo(functionScope());
    if(isEmpty(functionScopes)){
        tb.reportError(current, "Return outside a function declaration");
        collect(statement, tb);
        return;
    }
    for(<scope, scopeInfo> <- functionScopes){
        if(returnInfo(AType retType, list[Pattern] formals, set[AType] kwTypeParams) := scopeInfo){
           tb.requireEager("check return type", current, [], makeReturnRequirement(statement, retType, formals, kwTypeParams, scope));
           tb.calculate("return type", current, [statement], AType(){ return getType(statement); });
           collect(statement, tb);
           return;
        } else {
            throw rascalCheckerInternalError(getLoc(current), "Inconsistent info from function scope: <scopeInfo>");
        }
    }
    throw rascalCheckerInternalError(getLoc(current), "No surrounding function scope found");
}

// ---- alias declaration

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType userType> = <Type base>;`, TBuilder tb){
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore: <current>"); return; }
    
    aliasName = prettyPrintName(userType.name);
    aliasedType = convertType(base, tb);
    aliasTypeVarsAsList = getTypeParameters(userType, tb);
    scope = tb.getScope();
                     
    aliasType = aalias(aliasName, aliasTypeVarsAsList, aliasedType);
    tb.define(aliasName, aliasId(), userType.name, defType([], AType() { return expandUserTypes(aliasedType, scope); }));
} 


list[AType] getTypeParameters(UserType userType, TBuilder tb){
    if(userType is parametric){
       return
            for(p <- userType.parameters){
                ptype = convertType(p, tb);
                if(!isRascalTypeParam(ptype)){
                  tb.reportError(p, "Only type parameter allowed, found <fmt(ptype)>");
                }
                append unset(ptype, "label"); // TODO: Erase labels to enable later subset check
            }
    }
    return [];
}

// ---- data declaration

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters>;`, TBuilder tb)
    = dataDeclaration(tags, current, [], tb);

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`, TBuilder tb)
    = dataDeclaration(tags, current, [v | v <- variants], tb);

void dataDeclaration(Tags tags, Declaration current, list[Variant] variants, TBuilder tb){
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { println("*** ignore: <current>"); return; }
    userType = current.user;
    commonKeywordParameters = current.commonKeywordParameters;
    adtName = prettyPrintName(userType.name);
    
    dataTypeVarsAsList = getTypeParameters(userType, tb);
    dataTypeVars = toSet(dataTypeVarsAsList);
    
    commonKwFields = [];
    if(commonKeywordParameters is present){
        commonKwFields = getKeywordFormals(commonKeywordParameters.keywordFormalList, fieldId(), tb);
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
                fieldType = convertType(ta.\type, tb)[label=fieldName];
                
                if(!isEmpty(fieldName)){
                    tb.define(fieldName, fieldId(), ta, defType(fieldType));
                    allConsFields += fieldType; //<fieldName, fieldType>;
                }
                allFieldTypeVars += collectAndUnlabelRascalTypeParams(fieldType);
                append fieldType; //<fieldName, fieldType>;
            }
    
       kwFields = [];
       if(v.keywordArguments is \default){
          kwFields += getKeywordFormals(v.keywordArguments.keywordFormalList, fieldId(), tb);
          for(<kwt, kwd> <- kwFields){
              allFieldTypeVars += collectAndUnlabelRascalTypeParams(kwt);
              allConsFields += kwt; //<kwn, kwt>;
          }
       }
       // TODO: is this check necessary?
       // TODO: take different bounds into account
       //if(!(dataTypeVars <= allFieldTypeVars)){
       //   tb.reportWarning(v, "Unbound type parameter <fmt(dataTypeVars - allFieldTypeVars)>");
       //}
       consType = acons(adtType, /*consName,*/ fields, kwFields, label=consName);
       //println(consType);
       tb.define(consName, constructorId(), v.name, defType(consType));
       allConstructorDefines += <consName, v.name, defType(consType)>;
    }
    dt = defType(adtType);
    dt.constructorFields=allConsFields;
    constructors = {di.atype | DefInfo di <- allConstructorDefines<2>};
    if(!isEmpty(constructors)) dt.constructors = constructors;
    if(!isEmpty(commonKwFields)) dt.commonKeywordFields = commonKwFields;
    if(!isEmpty(tagsMap)) dt.tags = tagsMap;
    tb.define(adtName, dataId(), current, dt);
    
    // Additionaly declare all constructors inside the scope of the ADT to make them reachable via fully qualified names
    tb.enterScope(current);
    for(<consName, vname, defT> <- allConstructorDefines){
        //println("local defines in <current@\loc>: <consName>, constructorId(), <vname>, <defT>");
        tb.define(consName, constructorId(), vname, defT);
    }
    tb.leaveScope(current);
}

// ---- syntax definition

void collect(current: (SyntaxDefinition) `<Visibility vis> layout <Sym defined> = <Prod production>;`, TBuilder tb){
    //println("LAYOUT: <current>");
    nonterminalType = defsym2AType(defined, layoutSyntax());
    declareSyntax(current, getVis(vis), defined, nonterminalType, false, production, layoutId(), tb);
    collect(production, tb);
} 

void collect (current: (SyntaxDefinition) `lexical <Sym defined> = <Prod production>;`, TBuilder tb){
    //println("LEXICAL: <current>");
    nonterminalType = defsym2AType(defined, lexicalSyntax());
    declareSyntax(current, publicVis(), defined, nonterminalType, false, production, lexicalId(), tb);
    collect(production, tb);
}

void collect (current: (SyntaxDefinition) `keyword <Sym defined> = <Prod production>;`, TBuilder tb){
   //println("KEYWORD: <current>");
    nonterminalType = defsym2AType(defined, keywordSyntax());
    declareSyntax(current, publicVis(), defined, nonterminalType, false, production, keywordId(), tb);
    collect(production, tb);
} 

void collect (current: (SyntaxDefinition) `<Start strt> syntax <Sym defined> = <Prod production>;`, TBuilder tb){
    //println("SYNTAX: <current>");
    nonterminalType = defsym2AType(defined, contextFreeSyntax());
    isStart = strt is present;

    declareSyntax(current, publicVis(), defined, nonterminalType, isStart, production, nonterminalId(), tb);
    collect(production, tb);
}

void declareSyntax(SyntaxDefinition current, Vis vis, Sym defined, AType nonterminalType, bool isStart, Prod production, IdRole idRole, TBuilder tb){
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
          tb.reportWarning(v, "Unbound type parameter <fmt(allTypeVars - typeVars)>");
       }
        dt.productions = productions;
        //iprintln(productions);
      
        //  Declare all named fields that occur in the productions
        constructorFields = { *getFields(p) | AProduction p <- productions };
        //println("constructorFields: <constructorFields>");
        for(<str nm, loc def, AType tp> <- constructorFields){
            tb.define(nm, fieldId(), def, defType(tp));
        }
  
        if(!isEmpty(constructorFields))  dt.constructorFields = constructorFields<2>; //<0,2>;
                                            
        tb.define(ntName, idRole, current, dt);
        //println("define <ntName>, dataId(), <dt>");
        allConstructorDefines = {};
        for(p <- productions){
           //println("\nprod2cons: <p> ==\>\n\t<prod2cons(p)>");
           for(<k, c> <- prod2cons(p)){
               tb.define(c.label/*consName*/, constructorId(), k, defType(c));
               allConstructorDefines += <c.label/*consName*/, k, defType(c)>;
            }
        }
        // Additionaly declare all constructors inside the scope of the ADT to make them reachable via fully qualified names
        tb.enterScope(current);
        for(<consName, vname, defT> <- allConstructorDefines){
            //println("local defines in <current>: <consName>, constructorId(), <vname>, <defT>");
            tb.define(consName, constructorId(), vname, defT);
        }
        tb.leaveScope(current);
    } else {
        tb.reportError(defined, "Lhs of syntax definition not supported");
    }
}

rel[str,loc,AType] getFields(choice(AType dt, set[AProduction] alts)) = { *getFields(a) | a <- alts};

rel[str,loc,AType] getFields(\priority(AType def, list[AProduction] choices))
    = { *getFields(c) | c <- choices };
    
rel[str,loc,AType] getFields(\associativity(AType def, Associativity \assoc, set[AProduction] alternatives))
    = { *getFields(a) | a <- alternatives};
    
default rel[str,loc,AType] getFields(AProduction p) // TODO add no no label case
    = {<t.label, p.src[query = "label=<t.label>"], t> | s <- p.asymbols, t := removeConditional(s), (isNonTerminalType(t) || auser(_,_) := t),  t.label?};

void collect(current: (Sym) `<Nonterminal nonterminal>`, TBuilder tb){
    tb.use(nonterminal, syntaxIds);
    collect(nonterminal, tb);
}

void collect(current: (Sym) `<Sym symbol> <NonterminalLabel label>`, TBuilder tb){
    tb.define("<label>", fieldId(), label, defType(sym2AType(symbol)));
    collect(symbol, label, tb);
}

bool isIterSym((Sym) `<Sym symbol>+`) = true;
bool isIterSym((Sym) `<Sym symbol>*`) = true;
bool isIterSym((Sym) `{ <Sym symbol> <Sym sep> }+`) = true;
bool isIterSym((Sym) `{ <Sym symbol> <Sym sep> }*`) = true;
default bool isIterSym(Sym sym) = false;

void collect(current: (Sym) `<Sym symbol>+`, TBuilder tb){
    if(isIterSym(symbol)) tb.reportWarning(current, "Nested iteration");
    collect(symbol, tb);
}

void collect(current: (Sym) `<Sym symbol>*`, TBuilder tb){
    if(isIterSym(symbol)) tb.reportWarning(current, "Nested iteration");
    collect(symbol, tb);
}

void collect(current: (Sym) `{ <Sym symbol> <Sym sep> }+`, TBuilder tb){
    if(isIterSym(symbol)) tb.reportWarning(current, "Nested iteration");
    collect(symbol, tb);
}

void collect(current: (Sym) `{ <Sym symbol> <Sym sep> }*`, TBuilder tb){
    if(isIterSym(symbol)) tb.reportWarning(current, "Nested iteration");
    collect(symbol, tb);
}

default rel[Key, AType] prod2cons(AProduction p){
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

rel[Key, AType] prod2cons(choice(AType dt, set[AProduction] alts)) = { *prod2cons(a) | a <- alts};

rel[Key, AType] prod2cons(\priority(AType def, list[AProduction] choices))
    = { *prod2cons(c) | c <- choices };
    
rel[Key, AType] prod2cons(\associativity(AType def, Associativity \assoc, set[AProduction] alternatives))
    = { *prod2cons(a) | a <- alternatives};