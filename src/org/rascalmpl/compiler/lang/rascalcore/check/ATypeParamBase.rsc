module lang::rascalcore::check::ATypeParamBase

extend lang::rascalcore::check::ATypeBase;
extend lang::rascalcore::check::ATypeExceptions;

public alias Bindings = map[str varName, AType varType];

AType makeClosedTypeParams(AType t){
    return visit(t) { case par:aparameter(_, _) => par[closed=true] };
}

void requireClosedTypeParams(AType t){
    if(hasOpenTypeParams(t)){
        throw "requireClosedTypeParams: <t>";
    }
}

bool hasOpenTypeParams(AType t){
    return /aparameter(_,_,closed=false) := t;
}

// Make all type parameters unique with given suffix
AType makeUniqueTypeParams(AType t, str suffix){
    return visit(t) { case param:aparameter(str pname, AType _bound): {
                                if(findLast(pname, ".") < 0){
                                    param.pname = param.pname + "." + suffix;
                                    insert param;
                                }
                          }
                     };
}

// Make all type parameters unique with given suffix
list[AType] makeUniqueTypeParams(list[AType] ts, str suffix){
    return [ makeUniqueTypeParams(t, suffix) | t <- ts ];
}

// Reverse the makeUnique operation
str deUnique(str s) {
    i = findLast(s, ".");
    return i > 0 ? s[0..i] : s;
}

AType deUnique(AType t){
    return visit(t) { case param:aparameter(str pname, AType _bound): {
                                param.pname = deUnique(pname);
                                insert param;
                       }
                    };
}

Bindings deUniqueTypeParams(Bindings b){ 
    return (deUnique(key) : deUnique(b[key]) | key <- b);
}

@doc{
.Synopsis
Determine if the given type is an type variable (parameter).
}
bool isRascalTypeParam(aparameter(_,_)) = true;
default bool isRascalTypeParam(AType _) = false;

@doc{Create a type representing a type parameter (type variable).}
AType makeTypeVar(str varName) = aparameter(varName, avalue());

@doc{Create a type representing a type parameter (type variable) and bound.}
AType makeTypeVarWithBound(str varName, AType varBound) = aparameter(varName, varBound);

@doc{Get the name of a Rascal type parameter.}
str getRascalTypeParamName(AType t) {
    if (aparameter(tvn,_) := t) return tvn;
    throw rascalCheckerInternalError("getRascalTypeParamName given unexpected type: <prettyAType(t)>");
}

@doc{Get the bound of a type parameter.}
AType getRascalTypeParamBound(AType t) {
    if (aparameter(_,tvb) := t) return tvb;
    throw rascalCheckerInternalError("getRascalTypeParamBound given unexpected type: <prettyAType(t)>");
}

@doc{Get all the type parameters inside a given type.}
set[AType] collectRascalTypeParams(AType t) {
    
    return { rt | / AType rt : aparameter(_,_) := t };
    //return { unset(rt, "alabel") | / AType rt : aparameter(_,_) := t }; // TODO: "alabel" is unset to enable subset check later, reconsider
}

@doc{Get all the type parameters inside a given type.}
set[AType] collectAndUnlabelRascalTypeParams(AType t) {
   return { unset(rt, "alabel") | / AType rt : aparameter(_,_) := t }; // TODO: "alabel" is unset to enable subset check later, reconsider
}

@doc{Get all the type parameters inside a given set of productions.}
set[AType] collectAndUnlabelRascalTypeParams(set[AProduction] prods) {
   return { unset(rt, "alabel") | / AType rt : aparameter(_,_) := prods }; // TODO: "alabel" is unset to enable subset check later, reconsider
}

@doc{Provide an initial type map from the type parameters in the type to void.}
map[str,AType] initializeRascalTypeParamMap(AType t) {
    set[AType] rt = collectRascalTypeParams(t);
    return ( getRascalTypeParamName(tv) : makeVoidType() | tv <- rt );
}

@doc{See if a type contains any type parameters.}
bool typeContainsRascalTypeParams(AType t) = size(collectRascalTypeParams(t)) > 0;

@doc{Return the names of all type variables in the given type.}
set[str] typeParamNames(AType t) {
    return { tvn | aparameter(tvn,_) <- collectRascalTypeParams(t) };
}

@doc{Set "closed" to its default in all type parameters occurring in a value}
&T uncloseTypeParams(&T v)
    = visit(v) { case p:aparameter(_,_,closed=true) => unset(p,"closed") };
    