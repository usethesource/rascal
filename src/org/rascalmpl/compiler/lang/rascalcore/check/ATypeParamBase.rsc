module lang::rascalcore::check::ATypeParamBase

extend lang::rascalcore::check::ATypeBase;

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