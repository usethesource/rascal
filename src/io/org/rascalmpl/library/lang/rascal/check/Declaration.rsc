module lang::rascal::check::Declaration

extend analysis::typepal::TypePal;

import lang::rascal::\syntax::Rascal;
import lang::rascal::check::ConvertType;
import lang::rascal::check::ATypeUtils;
import lang::rascal::check::ATypeInstantiation;

extend lang::rascal::check::AType;
extend lang::rascal::check::Scope;

// ---- Rascal declarations

str key_imported = "imported";
str key_expanding_imports = "expanding_imports";

void collect(current: (Module) `<Header header> <Body body>`, FRBuilder frb){
    mname = prettyPrintQName(convertName(header.name));
    frb.define(mname, moduleId(), current, defType(amodule(mname)));
    frb.enterScope(current);
        collectParts(current, frb);
    frb.leaveScope(current);
    getImports(frb);
}

// ---- import

void collect(current: (Import) `import <ImportedModule m> ;`, FRBuilder frb){
    frb.use_ref(m, {moduleId()}, importPath());
    frb.store(key_imported, "<m.name>");
}

void getImports(FRBuilder frb){
    // Do not expand imports, while we are already doing that
    if(!isEmpty(frb.getStored(key_expanding_imports)))   return;
    
    pcfgVal = frb.getStored("pathconfig");
    
    if({PathConfig pcfg} := pcfgVal){ 
        allReadyImported = {};
        frb.store(key_expanding_imports, true);
        solve(allReadyImported){
            if(set[str] importedModules := frb.getStored(key_imported)){
                println("importedModules: <importedModules>");
                for(mname <- importedModules - allReadyImported){
                    allReadyImported += mname;
                    mloc = getModuleLocation(mname, pcfg);
                    println("*** importing <mname> from <mloc>");
                    pt = parse(#start[Modules], mloc).top;
                    collect(pt, frb);
                }
            } else {
                throw "Inconsistent value for \"imported\": <frb.getStored("imported")>";
            }
        }
    
    } else if(isEmpty(pcfgVal)){
        return;
    } else {
        throw "Inconsistent value for \"pathconfig\": <frm.getStored("pathconfig")>";
    }
}

// ---- extend

void collect(current: (Import) `extend <ImportedModule m> ;`, FRBuilder frb){    
    frb.use_ref(m, {moduleId()}, extendPath());
    
    // When we are not expanding imports/extemds, add this one to the set
    if(isEmpty(frb.getStored(key_expanding_imports))){
        frb.store(key_imported, "<m.name>");
    }
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
           subtype(typeof(expr), expandedInitType, onError(expr, "Type of initialization should be subtype of <fmt(initType)>, found <fmt(expr)>"));
         };
         
void collect(current: (Declaration) `<Tags tags> <Visibility visibility> <Type \type> <{Variable ","}+ variables> ;`, FRBuilder frb){
    vis = getVis(current.visibility);
    if(vis == defaultVis()){
        vis = privateVis();
    }
    varType = convertType(current.\type, frb);
    scope = frb.getScope();
    
    for(var <- variables){
        dt = defType([], AType(){ return expandUserTypes(varType, scope); });
        dt.vis = vis;
        frb.define(prettyPrintQName(convertName(var.name)), variableId(), var.name, dt);
        if(var is initialized){
            frb.enterScope(var.initial, lubScope=true);
                frb.require("variable initialization", var.initial, [], makeVarInitRequirement(var.initial, varType, frb.getScope()));
                collectParts(var, frb); 
            frb.leaveScope(var.initial);
        }
    }    
}

// ---- function declaration

data ReturnInfo = returnInfo(AType retType, list[Pattern] formals, set[AType] kwTypeParams);

void collect(FunctionDeclaration decl, FRBuilder frb){
    visibility = getVis(decl.visibility);
    if(visibility == defaultVis()){
        visibility = publicVis();
    }
    signature = decl.signature;
    isVarArgs = signature.parameters is varArgs;
    fname = signature.name;
    ftypeStub = frb.newTypeVar();
    dt = defType([ftypeStub], AType() { return typeof(ftypeStub); });
    dt.vis=visibility;  // TODO: Cannot be set directly, bug in interpreter?
    frb.define(prettyPrintQName(convertName(fname)), functionId(), fname, dt);     // function is defined in outer scope, its type is filled in in inner scope
    
    
    frb.enterScope(decl, lubScope=true);
        scope = frb.getScope();
        frb.setScopeInfo(scope, functionScope(), false);
        retType = convertType(signature.\type, frb);
        <formals, kwTypeParams, kwFormals> = checkFunctionType(scope, retType, signature.parameters, frb);
        
        frb.requireEager("define return type", signature.\type, [],
            (){ if(isVarArgs){
                   unify(ftypeStub, afunc(retType, atypeList([typeof(formals[i]) | int i <- [0..-1]] + alist(typeof(formals[-1]))), kwFormals, varArgs=true));
                } else {
                   unify(ftypeStub, afunc(retType, atypeList([typeof(f) | f <- formals]), kwFormals));
                }
             });

        if(decl is expression || decl is conditional){
            frb.requireEager("check return type", decl.expression, [decl.expression], makeReturnRequirement(decl.expression, retType, formals, kwTypeParams));
        } 
        if(decl is conditional){
            conditions = [c | c <- decl.conditions];
            storeAllowUseBeforeDef(decl, decl.expression, frb);
            frb.requireEager("when conditions", decl.conditions, conditions,
                (){
                for(cond <- conditions){
                    if(isFullyInstantiated(typeof(cond))){
                        subtype(typeof(cond), abool(), onError(cond, "Condition should be `bool`, found <fmt(cond)>"));
                    } else {
                        if(!unify(typeof(cond), abool())){
                            subtype(typeof(cond), abool(), onError(cond, "Condition should be `bool`, found <fmt(cond)>"));
                        }
                    }
                }
            });
        }
        collectParts(decl, frb);
        
    frb.leaveScope(decl);
}

void defineSingleVarsInFormals(list[Pattern] pats, FRBuilder frb){
    for(pat <- pats){
        if(namePat: (Pattern) `<QualifiedName name>` := pat){
            frb.fact(pat, avalue());
            qname = convertName(name);
            if(isQualified(qname)) frb.reportError(name, "Qualifier not allowed");
            frb.define(qname.name, formalId(), name, defLub([], AType() { return avalue(); }));
        }
        if(splicePat: (Pattern) `*<QualifiedName name>` := pat || splicePat: (Pattern) `<QualifiedName name>*` := pat){            
            frb.fact(pat, avalue());
            qname = convertName(name);
            if(isQualified(qname)) frb.reportError(name, "Qualifier not allowed");
            frb.define(qname.name, formalId(), name, defLub([], AType() { return alist(avalue()); }));
        }
        if(splicePlusPat: (Pattern) `+<QualifiedName name>` := pat){
            frb.fact(pat, avalue());
            qname = convertName(name);
            if(isQualified(qname)) frb.reportError(name, "Qualifier not allowed");
            frb.define(qname.name, formalId(), name, defLub([], AType() { return alist(avalue()); }));
        }
    }
}

list[Keyword] getKeywordFormals({KeywordFormal  "," }+ keywordFormalList, FRBuilder frb){    
    return 
        for(KeywordFormal kwf <- keywordFormalList){
            fieldType = convertType(kwf.\type, frb);
            fieldName = prettyPrintQName(convertName(kwf.name));
            defaultExp = kwf.expression;
            frb.define(fieldName, formalId(), kwf.name, defType(fieldType));
            append <fieldType, fieldName, defaultExp>;
        }
}

tuple[list[Pattern] formals, set[AType] kwTypeParams, list[Keyword] kwFormals] checkFunctionType(Key scope, AType retType, Parameters params, FRBuilder frb){
    formals = [pat | Pattern pat <- params.formals.formals];
    defineSingleVarsInFormals(formals, frb);    // Take care of single variable patterns
    
    kwFormals = params.keywordFormals is \default ? getKeywordFormals(params.keywordFormals.keywordFormalList, frb) : [];
    
    kwTypeParams = {*collectRascalTypeParams(kwf.fieldType) | kwf <- kwFormals};
    frb.setScopeInfo(scope, functionScope(), returnInfo(retType, formals, kwTypeParams));
    
    frb.require("bound type parameters", params, formals,
        () { typeParamsInFunctionParams = {*collectRascalTypeParams(typeof(f)) | f <- formals} + kwTypeParams;
             typeParamsInReturn = collectRascalTypeParams(retType);
             if(!(typeParamsInReturn <= typeParamsInFunctionParams)){
                unbound = typeParamsInReturn - typeParamsInFunctionParams;
                reportError(params, "Unbound: <fmt(size(unbound), "type parameter")> <fmt(unbound)> in return type");
             }
        });
     return <formals, kwTypeParams, kwFormals>;
}

void() makeReturnRequirement(Tree expr, AType retType, list[Pattern] formals, set[str] kwTypeParams)
       = () { typeVarsInParams = {*collectRascalTypeParams(typeof(f)) | f <- formals} + kwTypeParams;
              typeVarsInReturn = collectRascalTypeParams(retType);
              if(!(typeVarsInReturn <= typeVarsInParams)){
                unbound = typeVarsInReturn - typeVarsInParams;
                reportError(expr, "Unbound: <fmt(size(unbound), "type parameter")> <fmt(unbound)> in return type");
              }
            
              if(isFullyInstantiated(typeof(expr))){
                subtype(typeof(expr), retType, onError(expr, "Return type should be subtype of <fmt(retType)>, found <fmt(expr)>"));
              } else {
              if(!unify(typeof(expr), retType)){
                 subtype(typeof(expr), retType, onError(expr, "Return type should be subtype of <fmt(retType)>, found <fmt(expr)>"));
              }
           }
         };

// ---- return statement (closely interacts with function declaration)

void collect(current: (Statement) `return <Statement statement>`, FRBuilder frb){  
    functionScopes = frb.getScopeInfo(functionScope());
    if(isEmpty(functionScopes)){
        frb.reportError(current, "Return outside a function declaration");
        collectParts(current, frb);
        return;
    }
    for(<scope, scopeInfo> <- functionScopes){
        if(returnInfo(AType retType, list[Pattern] formals, set[str] kwTypeParams) := scopeInfo){
           frb.requireEager("check return type", current, [statement], makeReturnRequirement(statement, retType, formals, kwTypeParams));
           frb.calculate("return type", current, [statement], AType(){ return typeof(statement); });
            collectParts(current, frb);
            return;
        } else {
            throw "Inconsistent info from function scope: <scopeInfo>";
        }
    }
    throw "No surrounding function scope found";
}

// ---- alias declaration

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType userType> = <Type base>;`, FRBuilder frb){
    aliasName = prettyPrintName(convertName(userType.name));
    aliasedType = convertType(base, frb);
    aliasTypeVarsAsList = getTypeParameters(userType, frb);
    scope = frb.getScope();
                     
    aliasType = aalias(aliasName, aliasTypeVarsAsList, aliasedType);
    frb.define(aliasName, aliasId(), userType.name, defType([], AType() { return expandUserTypes(aliasedType, scope); }));
} 

AType expandUserTypes(AType t, Key scope){
    return visit(t){
        case u: auser(qualName(str qual, str uname), ps): {
                //println("expandUserTypes: <u>");  // TODO: handle non-empty qualifier
                expanded = expandUserTypes(typeof(uname, scope, {dataId(), aliasId()}), scope);
               // println("expanded: <expanded>");
                if(aadt(uname, ps2, ccp) := expanded) {
                   if(size(ps) != size(ps2)) reportError(scope, "Expected <fmt(size(ps2), "type parameter")> for <fmt(expanded)>, found <size(ps)>");
                   insert aadt(uname, ps, ccp);
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

list[AType] getTypeParameters(UserType userType, FRBuilder frb){
    if(userType is parametric){
       return
            for(p <- userType.parameters){
                ptype = convertType(p, frb);
                if(!isRascalTypeParam(ptype)){
                  frb.reportError(p, "Only type parameter allowed, found <fmt(ptype)>");
                }
                append unset(ptype, "label"); // TODO: Erase labels to enable later subset check
            }
    }
    return [];
}

// ---- data declaration

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters>;`, FRBuilder frb)
    = dataDeclaration(current, [], frb);

void collect (current: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`, FRBuilder frb)
    = dataDeclaration(current, [v | v <- variants], frb);

void dataDeclaration(Declaration current, list[Variant] variants, FRBuilder frb){
    userType = current.user;
    commonKeywordParameters = current.commonKeywordParameters;
    adtName = prettyPrintQName(convertName(userType.name));
    
    dataTypeVarsAsList = getTypeParameters(userType, frb);
    dataTypeVars = toSet(dataTypeVarsAsList);
    
    commonKwFields = [];
    if(commonKeywordParameters is present){
        commonKwFields = getKeywordFormals(commonKeywordParameters.keywordFormalList, frb);
    }
    adtType = aadt(adtName, dataTypeVarsAsList, commonKwFields);
    frb.define(adtName, dataId(), userType.name, defType(adtType));
    
    for(Variant v <- variants){
        consName = prettyPrintQName(convertName(v.name));
        allFieldTypeVars = {};
        fields = 
            for(TypeArg ta <- v.arguments){
                fieldType = convertType(ta.\type, frb);
                fieldName = ta has name ? prettyPrintQName(convertName(ta.name)) : "";
                if(!isEmpty(fieldName)){
                    frb.define(fieldName, fieldId(), ta, defType(fieldType));
                }
                allFieldTypeVars += collectRascalTypeParams(fieldType);
                append <fieldType, fieldName>;
            }
    
       kwFields = [];
       if(v.keywordArguments is \default){
          kwFields += getKeywordFormals(v.keywordArguments.keywordFormalList, frb);
          for(<kwt, kwn,kwd> <- kwFields){
              allFieldTypeVars += collectRascalTypeParams(kwt);
          }
       }
       
       // TODO: take different bounds into account
       if(!(dataTypeVars <= allFieldTypeVars)){
          frb.reportError(v, "Unbound type parameter <fmt(dataTypeVars - allFieldTypeVars)>");
       }
       consType = acons(adtType, consName, fields, kwFields);
       frb.define(consName, constructorId(), v.name, defType(consType));
    }
}

// ---- syntax definition

//=  @Foldable \layout  : Visibility vis "layout"  Sym defined "=" Prod production ";" 
//    |  @Foldable \lexical : "lexical" Sym defined "=" Prod production ";" 
//    |  @Foldable \keyword : "keyword" Sym defined "=" Prod production ";"
//    |  @Foldable language: Start start "syntax" Sym defined "=" Prod production ";" ;

void collect(current: (SyntaxDefinition) `<Visibility vis> layout <Sym defined> = <Prod production>;`, FRBuilder frb){

} 

void collect (current: (SyntaxDefinition) `lexical <Sym defined> = <Prod production>;`, FRBuilder frb){

} 

void collect (current: (SyntaxDefinition) `keyword <Sym defined> = <Prod production>;`, FRBuilder frb){

} 

void collect (current: (SyntaxDefinition) `<Start strt> syntax <Sym defined> = <Prod production>;`, FRBuilder frb){
    isStart = strt is present;
    if(defined is nonterminal){
        ntName = "<defined.nonterminal>";
        frb.define(ntName, nonterminalId(), defined, defType(nonterminal(ntName)));
        for(nt <- getNonTerminals(production)){
            frb.use(nt, {nonterminalId()}); 
        }
    } else if(defined is labelled){
        lbl = "<defined.label>";
        sym = defined.symbol;
    }
    collectParts(current, frb);
} 


list[Nonterminal] getParameters((Sym) `<Nonterminal _>[<{Sym ","}+ params>]`) = [ t | (Sym) `&<Nonterminal t>` <- params];
default list[Nonterminal] getParameters(Sym _) = []; 


list[Nonterminal] getNonTerminals(Prod production) {
    return 
        for(/Sym sym := production){
            if(sym is nonterminal) append <"", sym.nonterminal>;
            if(sym is labelled) append < "<sym.label>", sym.symbol>;
    
        };
}
   
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
 