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

int dataCounter = 0;

void dataDeclaration(Tags tags, Declaration current, list[Variant] variants, Collector c){
    userType = current.user;
    adtName = prettyPrintName(userType.name);
    
    tagsMap = getTags(tags);
    if(ignoreCompiler(tagsMap)) { 
        c.report(info(current, "Ignoring declaration of `<adtName>`"));
        return;
    }
    
    commonKeywordParameterList = getCommonKwFormals(current);

    typeParameters = getTypeParameters(userType);
    
    dt = isEmpty(typeParameters) ? defType(aadt(adtName, [], dataSyntax()))
                                 : defType(typeParameters, AType(Solver s) { return aadt(adtName, [ s.getType(tp)[closed=true] | tp <- typeParameters], dataSyntax()); });
    
    dt.md5 = md5Hash("<adtName><dataCounter>");
    dataCounter += 1;
    if(!isEmpty(commonKeywordParameterList)) dt.commonKeywordFields = commonKeywordParameterList;
    c.define(adtName, dataId(), current, dt);
       
    adtParentScope = c.getScope();
    c.enterScope(current);
        c.push(currentAdt, <current, typeParameters, commonKeywordParameterList, adtParentScope>);
            beginDefineOrReuseTypeParameters(c, closed=true);
                collect(typeParameters, c);
                if(!isEmpty(commonKeywordParameterList)){
                    collect(commonKeywordParameterList, c);
                }
            endDefineOrReuseTypeParameters(c);
       
            // visit all the variants in the parent scope of the data declaration
            collect(variants, c);
        c.pop(currentAdt);
    c.leaveScope(current);
}
    
AType(Solver) makeFieldType(str fieldName, Tree fieldType)
    = AType(Solver s) { return s.getType(fieldType)[alabel=fieldName]; };

int variantCounter = 0;

void collect(current:(Variant) `<Name name> ( <{TypeArg ","}* arguments> <KeywordFormals keywordArguments> )`, Collector c){
    
    if(<Declaration adt, list[TypeVar] dataTypeParameters, list[KeywordFormal] commonKwFormals, loc adtParentScope> := c.top(currentAdt) 
       && str currentModuleName := c.top(key_current_module)
       && str adtName := "<adt.user.name>"
       ){
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
                dt.md5 = md5Hash("<currentModuleName><adtName><dataCounter><variantCounter><fieldType> <fieldName>");
                c.define(fieldName, fieldId(), ta.name, dt);
            }
        }
        
        for(KeywordFormal kwf <- kwFormals){
            fieldName = prettyPrintName(kwf.name);
            if(fieldName in declaredFieldNames) c.report(error(kwf, "Double declaration of field `%v`", fieldName));
            declaredFieldNames += fieldName;
            kwfType = kwf.\type;
            dt = defType([kwfType], makeFieldType(fieldName, kwfType));
            dt.md5 = md5Hash("<currentModuleName><adtName><dataCounter><variantCounter><kwfType><fieldName>");
            c.define(fieldName, keywordFieldId(), kwf.name, dt);  
            c.requireSubType(kwf.expression, kwfType, error(kwf, "Default expression of type %t expected, found %t", kwfType, kwf.expression));
        }
    
        scope = c.getScope();
        c.enterScope(current);
            args = "<for(arg <- arguments){><arg is typedVariable ? "<arg.\type> <arg.name>" : "<arg>"> <}>";
            md5Contrib = "<currentModuleName><adtName><dataCounter><name><variantCounter>( <args>)";
            //println("<current>: <md5Contrib>");
            c.defineInScope(adtParentScope, prettyPrintName(name), constructorId(), name, defType(adt + formals + kwFormals + commonKwFormals,
                AType(Solver s){
                    adtType = s.getType(adt);
                    kwFormalTypes = [kwField(s.getType(kwf.\type)[alabel=prettyPrintName(kwf.name)], prettyPrintName(kwf.name), currentModuleName, kwf.expression) | kwf <- kwFormals /*+ commonKwFormals*/];
                    formalTypes = [f is named ? s.getType(f)[alabel=prettyPrintName(f.name)] : s.getType(f) | f <- formals];
                    return acons(adtType, formalTypes, kwFormalTypes)[alabel=asUnqualifiedName(prettyPrintName(name))];
                })[md5 = md5Hash(md5Contrib)]);
            variantCounter += 1;
            c.fact(current, name);
            beginUseTypeParameters(c, closed=false);
                 // The standard rules would declare arguments and kwFormals as variableId();
                for(arg <- arguments) { c.enterScope(arg); collect(arg.\type, c); if(arg is named) { c.fact(arg, arg.\type); } c.leaveScope(arg); }
                for(kwa <- kwFormals) { c.enterScope(kwa); collect(kwa.\type, kwa.expression, c); c.fact(kwa, kwa.\type); c.leaveScope(kwa); }
            endUseTypeParameters(c);
        c.leaveScope(current);
    } else {
        throw "collect Variant: currentAdt not found";
    }
} 