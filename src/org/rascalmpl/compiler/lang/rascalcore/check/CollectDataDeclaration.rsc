@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
@bootstrapParser
module lang::rascalcore::check::CollectDataDeclaration

/*
    Check data declarations.
*/

extend lang::rascalcore::check::CheckerCommon;
import lang::rascalcore::compile::util::Names;

import lang::rascalcore::agrammar::definition::Attributes;
import IO;
import Map;

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
    currentModuleName = str s := c.top(key_current_module) ? s : "";
    adtName = prettyPrintName(userType.name);
    
    tagsMap = getTags(tags);
    if(hasIgnoreCompilerTag(tagsMap)) { 
        c.report(info(adtName, "Ignoring declaration of `<adtName>`"));
        return;
    }
    
    commonKeywordParameterList = getCommonKwFormals(current);

    typeParameters = getTypeParameters(userType);
    
    dt = isEmpty(typeParameters) ? defType(aadt(adtName, [], dataSyntax()))
                                 : defType(typeParameters, AType(Solver s) { return aadt(adtName, [ s.getType(tp)[closed=true] | tp <- typeParameters], dataSyntax()); });
    
    dt.md5 = normalizedMD5Hash(adtName, dataCounter);
    dataCounter += 1;
    if(!isEmpty(commonKeywordParameterList)) dt.commonKeywordFields = commonKeywordParameterList;
    c.define(userType.name, dataId(), current, dt);
       
    adtParentScope = c.getScope();

    c.push(currentAdt, <current, typeParameters, commonKeywordParameterList, adtParentScope>);
        NestedScopes nestedScopes = newNestedScopes(c);
        beginDefineOrReuseTypeParameters(c, closed=true);
            collect(typeParameters, c);
            if(!isEmpty(commonKeywordParameterList)){
                nestedScopes.enter(current.commonKeywordParameters);
                    declaredFieldNames = {};
                    for(KeywordFormal kwf <- commonKeywordParameterList){
                        // Collect default expression (of current common keyword
                        // field) in outer scope
                        collect(kwf.\type, kwf.expression, c);
                        // Define current common keyword field, and collect
                        // next common keyword fields (in subsequent iterations,
                        // if any), in inner scope(s)
                        nestedScopes.enter(kwf);
                            declaredFieldNames = defineField(
                                c, kwf, keywordFieldId(), declaredFieldNames,
                                moreHashContribs = [currentModuleName, adtName]);
                            c.fact(kwf, kwf.\type);
                    }
            }
        endDefineOrReuseTypeParameters(c);
        // Note: Don't leave scopes of common keyword fields yet, so they are
        // accessible in the variants.

        nestedScopes.enter(current);
            collect(variants, c);
        nestedScopes.leaveAll();
    c.pop(currentAdt);
}
    
AType(Solver) makeFieldType(str fieldName, Tree fieldType)
    = AType(Solver s) { return s.getType(fieldType)[alabel=fieldName]; };

AType(Solver) makeKeywordFieldType(str fieldName, KeywordFormal kwf)
    = AType(Solver s) { 
        fldType = s.getType(kwf.\type);
        defType = s.getType(kwf.expression);
        bindings = ();
        try   bindings = unifyRascalTypeParams(fldType, defType, bindings);
        catch invalidMatch(str reason):
            s.report(error(kwf, reason));
        
        if(!isEmpty(bindings)){
            try {
                fldType = instantiateRascalTypeParameters(kwf.\type, fldType, bindings, s);
            } catch invalidInstantiation(str msg): {
                s.report(error(kwf, "Cannot instantiate keyword parameter type `<prettyAType(fldType)>`: " + msg));
            }
            try {
                defType = instantiateRascalTypeParameters(kwf.expression, defType, bindings, s);
            } catch invalidInstantiation(str msg): {
                s.report(error(kwf, "Cannot instantiate type of default expression `<prettyAType(defType)>`: " + msg));
            }
        }
        s.requireSubType(defType, fldType, error(kwf.expression, "Default expression of type %t expected, found %t", fldType, defType));
  
        return fldType[alabel=fieldName]; 
      };

void collect(current:(Variant) `<Name name> ( <{TypeArg ","}* arguments> <KeywordFormals keywordArguments> )`, Collector c){
    
    if(<Declaration adt, list[TypeVar] _dataTypeParameters, list[KeywordFormal] commonKwFormals, loc adtParentScope> := c.top(currentAdt) 
       && str currentModuleName := c.top(key_current_module)
       && str adtName := "<adt.user.name>"
       ){
        formals = getFormals(current);
        consArity = size(formals);
        kwFormals = getKwFormals(current);
        
        declaredFieldNames = {};
           
        // Define all fields in the outer scope of the data declaration in order to be easily found there.
        
        for(TypeArg ta <- formals, ta is named){
            declaredFieldNames = defineField(c, ta, fieldId(), declaredFieldNames,
                moreHashContribs = [currentModuleName, adtName, name, current]);
        }
        
        for(KeywordFormal kwf <- kwFormals){
            declaredFieldNames = defineField(c, kwf, keywordFieldId(), declaredFieldNames,
                moreHashContribs = [currentModuleName, adtName, dataCounter, name, consArity]);
        }
    
        scope = c.getScope();
        c.enterScope(current);
            args = "<for(arg <- arguments){><arg is named ? "<arg.\type> <arg.name>" : "<arg>"> <}>";
            c.defineInScope(adtParentScope, name, constructorId(), name, defType(adt + formals + kwFormals + commonKwFormals,
                AType(Solver s){
                    adtType = s.getType(adt);
                    kwFormalTypes = [kwField(s.getType(kwf.\type)[alabel=prettyPrintName(kwf.name)], prettyPrintName(kwf.name), currentModuleName, kwf.expression) | kwf <- kwFormals /*+ commonKwFormals*/];
                    formalTypes = [f is named ? s.getType(f)[alabel=prettyPrintName(f.name)] : s.getType(f) | f <- formals];
                    return acons(adtType, formalTypes, kwFormalTypes)[alabel=asUnqualifiedName(prettyPrintName(name))];
                })[md5 = normalizedMD5Hash(currentModuleName, adtName, dataCounter, name, args)]);
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

private alias NestedScopes = tuple[void(Tree) enter, void() leaveAll];

private NestedScopes newNestedScopes(Collector c) {
    str state = "ENTERING";
    list[Tree] nestedScopes = [];

    void enter(Tree t) {
        assert state == "ENTERING";
        c.enterScope(t);
        nestedScopes += t;
    }

    void leaveAll() {
        assert state == "ENTERING";
        state = "LEAVING";
        for (Tree t <- reverse(nestedScopes)) {
            c.leaveScope(t);
        };
    }

    return <enter, leaveAll>;
}

private set[str] defineField(Collector c, Tree fieldDef, IdRole fieldIdRole, set[str] declaredFieldIds, list[value] moreHashContribs = []) {
    if ((TypeArg) `<Type fieldType> <Name name>` := fieldDef ||
        (KeywordFormal) `<Type fieldType> <Name name> = <Expression _>` := fieldDef) {

        str fieldOrgId = "<name>";
        str fieldId = prettyPrintName(fieldOrgId);
        if (fieldId in declaredFieldIds) c.report(error(fieldDef, "Double declaration of field `%v`", fieldId));
        declaredFieldIds += fieldId;

        DefInfo fieldDefInfo = defType([fieldType], makeFieldType(fieldId, fieldType));
        fieldDefInfo.md5 = normalizedMD5Hash([fieldId, fieldType, *moreHashContribs]);

        c.define(name, fieldIdRole, fieldDef, fieldDefInfo);
    } else {
        throw "Cannot define field: `<fieldDef>`";
    }

    return declaredFieldIds;
}