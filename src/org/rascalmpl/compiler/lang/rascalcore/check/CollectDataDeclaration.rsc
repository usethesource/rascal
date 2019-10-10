@bootstrapParser
module lang::rascalcore::check::CollectDataDeclaration

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeExceptions;
extend lang::rascalcore::check::ATypeInstantiation;
extend lang::rascalcore::check::ATypeUtils;
extend lang::rascalcore::check::CollectType;

import lang::rascal::\syntax::Rascal;

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
    commonKeywordParameterList = getCommonKwFormals(current);
    typeParameters = getTypeParameters(userType);
    
    dt = isEmpty(typeParameters) ? defType(aadt(adtName, [], dataSyntax()))
                                 : defType(typeParameters, AType(Solver s) { return aadt(adtName, [ s.getType(tp) | tp <- typeParameters], dataSyntax()); });
    
    if(!isEmpty(commonKeywordParameterList)) dt.commonKeywordFields = commonKeywordParameterList;
    c.define(adtName, dataId(), current, dt);
       
    adtParentScope = c.getScope();
    c.enterScope(current);
        for(tp <- typeParameters){
            c.define("<tp.name>", typeVarId(), tp.name, defType(tp));
        }
        collect(typeParameters, c);
        if(!isEmpty(commonKeywordParameterList)){
            collect(commonKeywordParameterList, c);
        }
   
        // visit all the variants in the parent scope of the data declaration
        c.push(currentAdt, <current, typeParameters, commonKeywordParameterList, adtParentScope>);
            collect(variants, c);
        c.pop(currentAdt);
    c.leaveScope(current);
}
    
AType(Solver) makeFieldType(str fieldName, Tree fieldType)
    = AType(Solver s) { return s.getType(fieldType)[label=fieldName]; };

void collect(current:(Variant) `<Name name> ( <{TypeArg ","}* arguments> <KeywordFormals keywordArguments> )`, Collector c){
    formals = getFormals(current);
    kwFormals = getKwFormals(current);
       
    // Define all fields in the outer scope of the data declaration in order to be easily found there.
    
    for(ta <- formals){
        if(ta is named){
            fieldName = prettyPrintName(ta.name);
            fieldType = ta.\type;
            dt = defType([fieldType], makeFieldType(fieldName, fieldType));
            c.define(fieldName, fieldId(), ta.name, dt);
        }
    }
    
    for(KeywordFormal kwf <- kwFormals){
        fieldName = prettyPrintName(kwf.name);
        kwfType = kwf.\type;
        dt = defType([kwfType], makeFieldType(fieldName, kwfType));
        c.define(fieldName, keywordFieldId(), kwf.name, dt);    
    }

    scope = c.getScope();
    
    if(<Tree adt, list[TypeVar] dataTypeParameters, list[KeywordFormal] commonKwFormals, loc adtParentScope> := c.top(currentAdt)){
        c.enterScope(current);
            // Generate use/defs for type parameters occurring in the constructor signature
            
            set[TypeVar] declaredTVs = {};
            set[Name] usedTVNames = {};
            for(t <- formals + kwFormals){
                <d, u> = getDeclaredAndUsedTypeVars(t);
                declaredTVs += d;
                usedTVNames += u;
            }
            
            seenTypeVars = {"<tv.name>" | tv <- dataTypeParameters};
            inboundTypeVars = {};
            for(tv <- declaredTVs){
                tvname = "<tv.name>";
                if(tvname in seenTypeVars){
                    c.use(tv.name, {typeVarId()});
                } else {
                    seenTypeVars += tvname;
                    c.define(tvname, typeVarId(), tv.name, defType(tv));
                }
            }
            for(tvName <- usedTVNames){
                c.use(tvName, {typeVarId()});
            }
             
            c.defineInScope(adtParentScope, prettyPrintName(name), constructorId(), name, defType(adt + formals + kwFormals + commonKwFormals,
                AType(Solver s){
                    adtType = s.getType(adt);
                    kwFormalTypes = [<s.getType(kwf.\type)[label=prettyPrintName(kwf.name)], kwf.expression> | kwf <- kwFormals + commonKwFormals];
                    formalTypes = [f is named ? s.getType(f)[label=prettyPrintName(f.name)] : s.getType(f) | f <- formals];
                    return acons(adtType, formalTypes, kwFormalTypes)[label=prettyPrintName(name)];
                }));
            c.fact(current, name);
             // The standard rules would declare arguments and kwFormals as variableId();
            for(arg <- arguments) { c.enterScope(arg); collect(arg.\type, c); if(arg is named) { c.fact(arg, arg.\type); } c.leaveScope(arg); }
            for(kwa <- kwFormals) { c.enterScope(kwa); collect(kwa.\type, kwa.expression, c); c.fact(kwa, kwa.\type); c.leaveScope(kwa); }
        c.leaveScope(current);
    } else {
        throw "collect Variant: currentAdt not found";
    }
} 