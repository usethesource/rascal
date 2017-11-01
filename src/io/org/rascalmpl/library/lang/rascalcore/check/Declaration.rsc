module lang::rascalcore::check::Declaration

extend analysis::typepal::TypePal;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::check::ConvertType;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::ATypeInstantiation;

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::Scope;

import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Productions;

import lang::rascalcore::check::Import;

// ---- Rascal declarations

str key_imported = "imported";
str key_expanding_imports = "expanding_imports";

void collect(current: (Module) `<Header header> <Body body>`, TBuilder tb){
    mname = prettyPrintQName(convertName(header.name));
    tb.define(mname, moduleId(), current, defType(amodule(mname)));
    tb.enterScope(current);
        collectParts(current, tb);
    tb.leaveScope(current);
    getImports(tb);
}

// ---- import

str unescape(str s) = replaceAll(s, "\\", "");

void collect(current: (Import) `import <ImportedModule m> ;`, TBuilder tb){ // TODO: warn about direct self-import
    tb.useViaPath(m, {moduleId()}, importPath());
    tb.store(key_imported, unescape("<m.name>"));
}

void getImports(TBuilder tb){
    // Do not expand imports, while we are already doing that
    if(!isEmpty(tb.getStored(key_expanding_imports)))   return;
    
    pcfgVal = tb.getStored("pathconfig");
    
    if({PathConfig pcfg} := pcfgVal){ 
        allReadyImported = {};
        tb.store(key_expanding_imports, true);
        solve(allReadyImported){
            if(set[str] importedModules := tb.getStored(key_imported)){
                println("importedModules: <importedModules>");
                for(mname <- importedModules - allReadyImported){
                    allReadyImported += mname;
                    println("*** importing <mname>");
                    done = addImport(mname, pcfg, tb);
                    if(!done){
                        mloc = getModuleLocation(mname, pcfg);
                        println("*** importing <mname> from <mloc>");
                        pt = parse(#start[Modules], mloc).top;
                        collect(pt, tb);
                    }
                }
            } else {
                throw "Inconsistent value for \"imported\": <tb.getStored("imported")>";
            }
        }
    
    } else if(isEmpty(pcfgVal)){
        return;
    } else {
        throw "Inconsistent value for \"pathconfig\": <tb.getStored("pathconfig")>";
    }
}

// ---- extend

void collect(current: (Import) `extend <ImportedModule m> ;`, TBuilder tb){    
    tb.useViaPath(m, {moduleId()}, extendPath());
    
    tb.store(key_imported, unescape("<m.name>"));
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
           subtype(getType(expr), expandedInitType, onError(expr, "Type of initialization should be subtype of <fmt(initType)>, found <fmt(expr)>"));
         };
         
void collect(current: (Declaration) `<Tags tags> <Visibility visibility> <Type \type> <{Variable ","}+ variables> ;`, TBuilder tb){
    vis = getVis(current.visibility);
    if(vis == defaultVis()){
        vis = privateVis();
    }
    varType = convertType(current.\type, tb);
    scope = tb.getScope();
    
    for(var <- variables){
        dt = defType([], AType(){ return expandUserTypes(varType, scope); });
        dt.vis = vis;
        tb.define(prettyPrintQName(convertName(var.name)), variableId(), var.name, dt);
        
        tb. require("variable type is defined", current.\type, [],
            (){ try expandUserTypes(varType, scope);
                catch TypeUnavailable(): reportError(current.\type, "Undeclared type <fmt("<current.\type>")>");
            });
        if(var is initialized){
            tb.enterScope(var.initial, lubScope=true);
                tb.require("variable initialization", var.initial, [], makeVarInitRequirement(var.initial, varType, tb.getScope()));
                collect(var.initial, tb); 
            tb.leaveScope(var.initial);
        }
    }    
}

// ---- annotation

void collect(current: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType> @ <Name name> ;`, TBuilder tb){
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
    tb.define(qname.name, annoId(), name, dt);
    collectParts(current, tb); 
}
 

// ---- function declaration

data ReturnInfo = returnInfo(AType retType, list[Pattern] formals, set[AType] kwTypeParams);

void collect(FunctionDeclaration decl, TBuilder tb){
//println("********** function declaration: <decl.signature.name>");
    vis = getVis(decl.visibility);
    if(vis == defaultVis()){
        vis = publicVis();
    }
    signature = decl.signature;
    isVarArgs = signature.parameters is varArgs;
    fname = signature.name;
    ftypeStub = tb.newTypeVar();
    dt = defType([ftypeStub], AType() { return getType(ftypeStub); });
    dt.vis=vis;  // TODO: Cannot be set directly, bug in interpreter?
    tb.define(prettyPrintQName(convertName(fname)), functionId(), fname, dt);     // function is defined in outer scope, its type is filled in in inner scope
    
    tb.enterScope(decl, lubScope=true);
        scope = tb.getScope();
        tb.setScopeInfo(scope, functionScope(), false);
        retType = convertType(signature.\type, tb);
        <formals, kwTypeParams, kwFormals> = checkFunctionType(scope, retType, signature.parameters, tb);
        
        tb.requireEager("definition of return type", signature.\type, [],
            (){ expandedRetType = expandUserTypes(retType, scope);
                if(isVarArgs){
                   unify(ftypeStub, afunc(expandedRetType, atypeList([expandUserTypes(getType(formals[i]), scope) | int i <- [0..-1]] + alist(expandUserTypes(getType(formals[-1]), scope))), kwFormals, varArgs=true));
                } else {
                   unify(ftypeStub, afunc(expandedRetType, atypeList([expandUserTypes(getType(f), scope) | f <- formals]), kwFormals));
                }
             });
        
        if(decl is expression || decl is conditional){
            tb.requireEager("check on return type", decl.expression, [decl.expression], makeReturnRequirement(decl.expression, retType, formals, kwTypeParams, scope));
            collect(decl.expression, tb);
        } 
        if(decl is conditional){
            conditions = [c | c <- decl.conditions];
            storeAllowUseBeforeDef(decl, decl.expression, tb);
            tb.requireEager("when conditions", decl.conditions, conditions,
                (){
                for(cond <- conditions){
                    if(isFullyInstantiated(getType(cond))){
                        subtype(getType(cond), abool(), onError(cond, "Condition should be `bool`, found <fmt(cond)>"));
                    } else {
                        if(!unify(getType(cond), abool())){
                            subtype(getType(cond), abool(), onError(cond, "Condition should be `bool`, found <fmt(cond)>"));
                        }
                    }
                }
            });
            collect(decl.conditions, tb);
        }
        if(decl is \default) collect(decl.body, tb);
        
    tb.leaveScope(decl);
}

void collectFormals(list[Pattern] pats, TBuilder tb){
    previousNames = {};
    for(pat <- pats){
        if(namePat: (Pattern) `<QualifiedName name>` := pat){
          qname = convertName(name);
          if(qname.name != "_"){
             if("<name>" in previousNames){
                tb.use(name, {variableId()});
             } else {
                previousNames += "<name>";
                tb.fact(pat, avalue());
                if(isQualified(qname)) tb.reportError(name, "Qualifier not allowed");
                tb.define(qname.name, formalId(), name, defLub([], AType() { return avalue(); }));
             }
           }
        } else
        if(splicePat: (Pattern) `*<QualifiedName name>` := pat || splicePat: (Pattern) `<QualifiedName name>*` := pat){ 
           qname = convertName(name);
           if(qname.name != "_"){      
              if("<name>" in previousNames){
                 tb.use(name, {variableId()});
              } else {   
                 previousNames += "<name>";  
                 tb.fact(pat, avalue());
                 if(isQualified(qname)) tb.reportError(name, "Qualifier not allowed");
                 tb.define(qname.name, formalId(), name, defLub([], AType() { return alist(avalue()); }));
              }
           }
        } else
        if(splicePlusPat: (Pattern) `+<QualifiedName name>` := pat){
           qname = convertName(name);
           if(qname.name != "_"){  
              if("<name>" in previousNames){
                 tb.use(name, {variableId()});
              } else {   
                 previousNames += "<name>";
                 tb.fact(pat, avalue());
                 if(isQualified(qname)) tb.reportError(name, "Qualifier not allowed");
                 tb.define(qname.name, formalId(), name, defLub([], AType() { return alist(avalue()); }));
              }
           }
        } else {
            collect(pat, tb);
            previousNames += getAllNames(pat);
        }
    }
}

list[Keyword] getKeywordFormals({KeywordFormal  "," }+ keywordFormalList, TBuilder tb){    
    return 
        for(KeywordFormal kwf <- keywordFormalList){
            fieldType = convertType(kwf.\type, tb);
            fieldName = prettyPrintQName(convertName(kwf.name));
            defaultExp = kwf.expression;
            tb.define(fieldName, formalId(), kwf.name, defType(fieldType));
            append <fieldType, fieldName, defaultExp>;
        }
}

tuple[list[Pattern] formals, set[AType] kwTypeParams, list[Keyword] kwFormals] checkFunctionType(Key scope, AType retType, Parameters params, TBuilder tb){
    formals = [pat | Pattern pat <- params.formals.formals];
    collectFormals(formals, tb);
    
    kwFormals = params.keywordFormals is \default ? getKeywordFormals(params.keywordFormals.keywordFormalList, tb) : [];
    
    kwTypeParams = {*collectRascalTypeParams(kwf.fieldType) | kwf <- kwFormals};
    tb.setScopeInfo(scope, functionScope(), returnInfo(retType, formals, kwTypeParams));
    
    tb.require("bound type parameters", params, formals,
        () { expandedRetType = expandUserTypes(retType, scope);
             typeParamsInFunctionParams = {*collectRascalTypeParams(getType(f)) | f <- formals} + kwTypeParams;
             typeParamsInReturn = collectRascalTypeParams(expandedRetType);
             if(!(typeParamsInReturn <= typeParamsInFunctionParams)){
                unbound = typeParamsInReturn - typeParamsInFunctionParams;
                reportError(params, "Unbound: <fmt(size(unbound), "type parameter")> <fmt(unbound)> in return type");
             }
        });
     return <formals, kwTypeParams, kwFormals>;
}

void() makeReturnRequirement(Tree expr, AType retType, list[Pattern] formals, set[str] kwTypeParams, Key scope)
       = () { 
              expandedReturnType = expandUserTypes(retType, scope);
              typeVarsInParams = {*collectRascalTypeParams(getType(f)) | f <- formals} + kwTypeParams;
              typeVarsInReturn = collectRascalTypeParams(expandedReturnType);
              if(!(typeVarsInReturn <= typeVarsInParams)){
                unbound = typeVarsInReturn - typeVarsInParams;
                reportError(expr, "Unbound: <fmt(size(unbound), "type parameter")> <fmt(unbound)> in return type");
              }
              
              texpr = expandUserTypes(getType(expr), scope);
              Bindings bindings = ();
              try   bindings = matchRascalTypeParams(texpr, expandedReturnType, bindings, bindIdenticalVars=true);
              catch invalidMatch(str reason):
                    reportError(expr, reason);
              try {
                 itexpr = instantiateRascalTypeParams(texpr, bindings);
                 if(isFullyInstantiated(itexpr)){
                    subtype(itexpr, expandedReturnType, onError(expr, "Return type should be subtype of <fmt(expandedReturnType)>, found <fmt(itexpr)>"));
                 } else
                 if(!unify(itexpr, expandedReturnType)){
                    subtype(itexpr, expandedReturnType, onError(expr, "Return type should be subtype of <fmt(expandedReturnType)>, found <fmt(itexpr)>"));
                 }
               } catch invalidInstantiation(str msg): {
                    reportError(current, msg);
               }
            
            
         };

// ---- return statement (closely interacts with function declaration)

void collect(current: (Statement) `return <Statement statement>`, TBuilder tb){  
    functionScopes = tb.getScopeInfo(functionScope());
    if(isEmpty(functionScopes)){
        tb.reportError(current, "Return outside a function declaration");
        collectParts(current, tb);
        return;
    }
    for(<scope, scopeInfo> <- functionScopes){
        if(returnInfo(AType retType, list[Pattern] formals, set[str] kwTypeParams) := scopeInfo){
           tb.requireEager("check return type", current, [], makeReturnRequirement(statement, retType, formals, kwTypeParams, scope));
           tb.calculate("return type", current, [statement], AType(){ return getType(statement); });
           collectParts(current, tb);
           return;
        } else {
            throw "Inconsistent info from function scope: <scopeInfo>";
        }
    }
    throw "No surrounding function scope found";
}

// ---- alias declaration

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType userType> = <Type base>;`, TBuilder tb){
//println("********** alias: <current>");
    aliasName = prettyPrintQName(convertName(userType.name));
    aliasedType = convertType(base, tb);
    aliasTypeVarsAsList = getTypeParameters(userType, tb);
    scope = tb.getScope();
                     
    aliasType = aalias(aliasName, aliasTypeVarsAsList, aliasedType);
    tb.define(aliasName, aliasId(), userType.name, defType([], AType() { return expandUserTypes(aliasedType, scope); }));
} 

AType expandUserTypes(AType t, Key scope){
    return visit(t){
        case u: auser(str uname, ps): {
                //println("expandUserTypes: <u>");  // TODO: handle non-empty qualifier
                expanded = expandUserTypes(getType(uname, scope, {dataId(), aliasId(), nonterminalId()}), scope);
                //println("expanded: <expanded>");
                if(aadt(uname, ps2) := expanded) {
                   if(size(ps) != size(ps2)) reportError(scope, "Expected <fmt(size(ps2), "type parameter")> for <fmt(expanded)>, found <size(ps)>");
                   expanded.parameters = ps;
                   insert expanded; //aadt(uname, ps);
                } else {
                   params = toList(collectRascalTypeParams(expanded));  // TODO order issue?
                   nparams = size(params);
                   if(size(ps) != size(params)) reportError(scope, "Expected <fmt(nparams, "type parameter")> for <fmt(expanded)>, found <size(ps)>");
                   if(nparams > 0){
                      try {
                         Bindings b = (params[i].pname : ps[i] | int i <- index(params));
                         insert instantiateRascalTypeParams(expanded, b);
                       } catch invalidMatch(str reason): 
                                reportError(v, reason);
                         catch invalidInstantiation(str msg):
                                reportError(v, msg);
                   } else {
                     insert expanded;
                   }
               }
            }
    }
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
    = dataDeclaration(current, [], tb);

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`, TBuilder tb)
    = dataDeclaration(current, [v | v <- variants], tb);

void dataDeclaration(Declaration current, list[Variant] variants, TBuilder tb){
//println("********** data: <current>");
    userType = current.user;
    commonKeywordParameters = current.commonKeywordParameters;
    adtName = prettyPrintQName(convertName(userType.name));
    
    dataTypeVarsAsList = getTypeParameters(userType, tb);
    dataTypeVars = toSet(dataTypeVarsAsList);
    
    commonKwFields = [];
    if(commonKeywordParameters is present){
        commonKwFields = getKeywordFormals(commonKeywordParameters.keywordFormalList, tb);
    }
    adtType = aadt(adtName, dataTypeVarsAsList);
    
    allConsFields = {};
    allConstructorDefines = {};
    for(Variant v <- variants){
        consName = prettyPrintQName(convertName(v.name));
        allFieldTypeVars = {};
        fields = 
            for(TypeArg ta <- v.arguments){
                fieldType = convertType(ta.\type, tb);
                fieldName = ta has name ? prettyPrintQName(convertName(ta.name)) : "";
                if(!isEmpty(fieldName)){
                    tb.define(fieldName, fieldId(), ta, defType(fieldType));
                    allConsFields += <fieldName, fieldType>;
                }
                allFieldTypeVars += collectRascalTypeParams(fieldType);
                append <fieldType, fieldName>;
            }
    
       kwFields = [];
       if(v.keywordArguments is \default){
          kwFields += getKeywordFormals(v.keywordArguments.keywordFormalList, tb);
          for(<kwt, kwn,kwd> <- kwFields){
              allFieldTypeVars += collectRascalTypeParams(kwt);
              allConsFields += <kwn, kwt>;
          }
       }
       // TODO: is this check necessary?
       // TODO: take different bounds into account
       //if(!(dataTypeVars <= allFieldTypeVars)){
       //   tb.reportWarning(v, "Unbound type parameter <fmt(dataTypeVars - allFieldTypeVars)>");
       //}
       consType = acons(adtType, consName, fields, kwFields);
       //println(consType);
       tb.define(consName, constructorId(), v.name, defType(consType));
       allConstructorDefines += <consName, v.name, defType(consType)>;
    }
    dt = defType(adtType);
    dt.constructorFields=allConsFields;
    if(!isEmpty(commonKwFields)) dt.commonKeywordFields = commonKwFields;
    tb.define(adtName, dataId(), current, dt);
    
    // Additionaly declare all constructors inside the scope of the ADT to make them reachable via fully qualified names
    tb.enterScope(current);
    for(<consName, vname, defT> <- allConstructorDefines){
        tb.define(consName, constructorId(), vname, defT);
    }
    tb.leaveScope(current);
}

// ---- syntax definition

void collect(current: (SyntaxDefinition) `<Visibility vis> layout <Sym defined> = <Prod production>;`, TBuilder tb){
    //println("LAYOUT: <current>");
    nonterminalType = sym2AType(defined);
    declareSyntax(current, getVis(vis), defined, nonterminalType, production, {\layout()}, tb);
    collectParts(production, tb);
} 

void collect (current: (SyntaxDefinition) `lexical <Sym defined> = <Prod production>;`, TBuilder tb){
    //println("LEXICAL: <current>");
    nonterminalType = sym2AType(defined);
    declareSyntax(current, publicVis(), defined, nonterminalType, production, {\lexical()}, tb);
    collectParts(production, tb);
}

void collect (current: (SyntaxDefinition) `keyword <Sym defined> = <Prod production>;`, TBuilder tb){
   //println("KEYWORD: <current>");
    nonterminalType = sym2AType(defined);
    declareSyntax(current, publicVis(), defined, nonterminalType, production, {\keyword()}, tb);
    collectParts(production, tb);
} 

void collect (current: (SyntaxDefinition) `<Start strt> syntax <Sym defined> = <Prod production>;`, TBuilder tb){
    //println("SYNTAX: <current>");
    nonterminalType = sym2AType(defined);
    skind = { nonterminal() };
    if(strt is present) skind += {\start()};
    declareSyntax(current, publicVis(), defined, nonterminalType, production, skind, tb);
    collectParts(production, tb);
}

void declareSyntax(SyntaxDefinition current, Vis vis, Sym defined, AType nonterminalType, Prod production, set[SyntaxKind] syntaxKind, TBuilder tb){
    AProduction pr = prod2prod(nonterminalType, production, syntaxKind);
    //println("pr: <pr>");
    if(isADTType(nonterminalType)){
        ntName = nonterminalType.adtName;
        dt = defType(nonterminalType);
        dt.vis = vis;
        productions = choice(definedType, set[AProduction] prods) := pr ? prods : {pr};
        dt.productions = productions;
        //iprintln(productions);
        constructorFields = { *getFields(p) | AProduction p <- productions };
        if(!isEmpty(constructorFields))  dt.constructorFields = constructorFields;
                                            
        tb.define(ntName, nonterminalId(), current, dt);
        //—println("define <ntName>, nonTerminalId(), <dt>");
        allConstructorDefines = {};
        for(p <- productions){
           for(<k, c> <- prod2cons(p)){
               //println(c);
               tb.define(c.consName, constructorId(), k, defType(c));
               allConstructorDefines += <c.consName, k, defType(c)>;
            }
        }
        // Additionaly declare all constructors inside the scope of the ADT to make them reachable via fully qualified names
        tb.enterScope(current);
        for(<consName, vname, defT> <- allConstructorDefines){
            tb.define(consName, constructorId(), vname, defT);
        }
        tb.leaveScope(current);
    } else {
        tb.reportError(defined, "Lhs of syntax definition not supported");
    }
}

rel[str,AType] getFields(choice(dt, set[AProduction] alts)) = { *getFields(a) | a <- alts};

rel[str,AType] getFields(\priority(AType def, list[AProduction] choices))
    = { *getFields(c) | c <- choices };
    
rel[str,AType] getFields(\associativity(AType def, Associativity \assoc, set[AProduction] alternatives))
    = { *getFields(a) | a <- alternatives};

default rel[str,AType] getFields(AProduction p) = {<t.label, t> | s <- p.symbols, isNonTerminalType(s), s.label?, t := removeConditional(s)};

void collect(current: (Sym) `<Nonterminal nonterminal>`, TBuilder tb){
    tb.use(nonterminal, {nonterminalId()});
    collectParts(current, tb);
}

void collect(current: (Sym) `<Sym symbol> <NonterminalLabel label>`, TBuilder tb){
    tb.define("<label>", fieldId(), label, defType(sym2AType(symbol)));
    collectParts(current, tb);
}

default rel[Key, AType] prod2cons(AProduction p){
    def = p.def;
    symbols = p.symbols;
    if(def.label?){
        fields = [ <t, t.label> | s <- symbols, isNonTerminalType(s), t := removeConditional(s) ];
        return { <p.src, acons(unset(def, "label"), deescape(def.label), fields, [])> };
    }
    return {};
}

rel[Key, AType] prod2cons(choice(dt, set[AProduction] alts)) = { *prod2cons(a) | a <- alts};

rel[Key, AType] prod2cons(\priority(AType def, list[AProduction] choices))
    = { *prod2cons(c) | c <- choices };
    
rel[Key, AType] prod2cons(\associativity(AType def, Associativity \assoc, set[AProduction] alternatives))
    = { *prod2cons(a) | a <- alternatives};
   
//syntax Sym
//// named non-terminals
//    = nonterminal: Nonterminal nonterminal !>> "["
//    | parameter: "&" Nonterminal nonterminal 
//    | parametrized: Nonterminal nonterminal >> "[" "[" {Sym ","}+ parameters "]"
//    | \start: "start" "[" Nonterminal nonterminal "]"
//    | labeled: Sym symbol NonterminalLabel label
//// literals 
//    | characterClass: Class charClass 
//    | literal: StringConstant string 
//    | caseInsensitiveLiteral: CaseInsensitiveStringConstant cistring
//// regular expressions
//    | iter: Sym symbol "+" 
//    | iterStar: Sym symbol "*" 
//    | iterSep: "{" Sym symbol Sym sep "}" "+" 
//    | iterStarSep: "{" Sym symbol Sym sep "}" "*" 
//    | optional: Sym symbol "?" 
//    | alternative: "(" Sym first "|" {Sym "|"}+ alternatives ")"
//    | sequence: "(" Sym first Sym+ sequence ")"
//    // TODO: MinimalIter: Sym symbol IntegerConstant minimal "+"
//    // TODO: MinimalIterSep: "{" Sym symbol Symbol sep "}" IntegerConstant minimal "+"
//    // TODO | Permutation: "(" Sym first "~" {Sym "~"}+ participants ")"
//    // TODO | Combination: "(" Sym first "#" {Sym "#"}+ elements ")"
//    | empty: "(" ")"
//// conditionals
//    | column: Sym symbol "@" IntegerLiteral column 
//    | endOfLine: Sym symbol "$" 
//    | startOfLine: "^" Sym symbol
//    | except:   Sym symbol "!" NonterminalLabel label
//    >  
//    assoc ( 
//      left  ( follow:     Sym symbol  "\>\>" Sym match
//            | notFollow:  Sym symbol "!\>\>" Sym match
//            )
//      | 
//      right ( precede:    Sym match "\<\<" Sym symbol 
//            | notPrecede: Sym match "!\<\<" Sym symbol
//            )
//    )
//    > 
//    left unequal:  Sym symbol "\\" Sym match
//    ;

//syntax Prod
//    = reference: ":" Name referenced
//    | labeled: ProdModifier* modifiers Name name ":" Sym* syms 
//    | others: "..." 
//    | unlabeled: ProdModifier* modifiers Sym* syms
//    | @Foldable associativityGroup: Assoc associativity "(" Prod group ")" 
//    // | TODO add bracket rule for easy readability
//    > left \all   : Prod lhs "|" Prod rhs 
//    > left first : Prod lhs "\>" !>> "\>" Prod rhs
//    ;

//syntax ProdModifier
//    = associativity: Assoc associativity 
//    | \bracket: "bracket" 
//    | \tag: Tag tag;
 