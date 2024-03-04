@bootstrapParser
module lang::rascalcore::check::CollectDataDeclaration

/*
    Check data declarations.
*/

extend lang::rascalcore::check::CheckerCommon;
import lang::rascalcore::compile::util::Names;

import lang::rascalcore::grammar::definition::Attributes;
import lang::rascal::\syntax::Rascal;
import IO;

// ---- data declaration ------------------------------------------------------

bool inADTdeclaration(Collector c){
    return <Tree _, list[KeywordFormal] _, loc _> := c.top(currentAdt);
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
    
    dt.md5 = md5Hash("<current>");
    if(!isEmpty(commonKeywordParameterList)) dt.commonKeywordFields = commonKeywordParameterList;
    c.define(adtName, dataId(), current, dt);
       
    adtParentScope = c.getScope();
    c.enterScope(current);
        for(tp <- typeParameters){
            c.define("<tp.name>", typeVarId(), tp.name, defType(tp));
        }
        
        c.push(currentAdt, <current, typeParameters, commonKeywordParameterList, adtParentScope>);
            collect(typeParameters, c);
            if(!isEmpty(commonKeywordParameterList)){
                collect(commonKeywordParameterList, c);
            }
       
            // visit all the variants in the parent scope of the data declaration
            collect(variants, c);
        c.pop(currentAdt);
    c.leaveScope(current);
}
    
AType(Solver) makeFieldType(str fieldName, Tree fieldType)
    = AType(Solver s) { return s.getType(fieldType)[alabel=fieldName]; };

void collect(current:(Variant) `<Name name> ( <{TypeArg ","}* arguments> <KeywordFormals keywordArguments> )`, Collector c){
    formals = getFormals(current);
    kwFormals = getKwFormals(current);
    
    declaredFieldNames = {};
       
    // Define all fields in the outer scope of the data declaration in order to be easily found there.
    
    for(ta <- formals){
        if(ta is named){
            fieldName = prettyPrintName(ta.name);
            if(fieldName in declaredFieldNames) c.report(error(ta, "Double declaration of field `%v`", fieldName));
            declaredFieldNames += fieldName;
            fieldType = ta.\type;
            dt = defType([fieldType], makeFieldType(fieldName, fieldType));
            dt.md5 = md5Hash("<c.getScope()><current><ta>");
            //println("<ta>, md5 = <dt.md5>");
            c.define(fieldName, fieldId(), ta.name, dt);
        }
    }
    
    for(KeywordFormal kwf <- kwFormals){
        fieldName = prettyPrintName(kwf.name);
        if(fieldName in declaredFieldNames) c.report(error(kwf, "Double declaration of field `%v`", fieldName));
        declaredFieldNames += fieldName;
        kwfType = kwf.\type;
        dt = defType([kwfType], makeFieldType(fieldName, kwfType));
        dt.md5 = md5Hash("<c.getScope()><current><kwfType><fieldName>");
        c.define(fieldName, keywordFieldId(), kwf.name, dt);    
    }

    scope = c.getScope();
    
    if(<Tree adt, list[TypeVar] dataTypeParameters, list[KeywordFormal] commonKwFormals, loc adtParentScope> := c.top(currentAdt) &&
       str currentModuleName := c.top(key_current_module)){
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
                    kwFormalTypes = [kwField(s.getType(kwf.\type)[alabel=prettyPrintName(kwf.name)], prettyPrintName(kwf.name), currentModuleName, kwf.expression) | kwf <- kwFormals /*+ commonKwFormals*/];
                    formalTypes = [f is named ? s.getType(f)[alabel=prettyPrintName(f.name)] : s.getType(f) | f <- formals];
                    return acons(adtType, formalTypes, kwFormalTypes)[alabel=asUnqualifiedName(prettyPrintName(name))];
                })[md5=md5Hash(current)]);
            c.fact(current, name);
             // The standard rules would declare arguments and kwFormals as variableId();
            for(arg <- arguments) { c.enterScope(arg); collect(arg.\type, c); if(arg is named) { c.fact(arg, arg.\type); } c.leaveScope(arg); }
            for(kwa <- kwFormals) { c.enterScope(kwa); collect(kwa.\type, kwa.expression, c); c.fact(kwa, kwa.\type); c.leaveScope(kwa); }
        c.leaveScope(current);
    } else {
        throw "collect Variant: currentAdt not found";
    }
} 