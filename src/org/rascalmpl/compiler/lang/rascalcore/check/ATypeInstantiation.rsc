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
@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Paul Klint - Paul Klint@cwi.nl (CWI)}
@bootstrapParser
module lang::rascalcore::check::ATypeInstantiation

/*
    Implement matching and instantiation of type parameters
    Note: it is assumed that the type parameters in receiver and sender AType have already been properly renamed
*/

extend lang::rascalcore::check::ATypeUtils;
extend lang::rascalcore::check::NameUtils;

import List;
import Map;
import Set;
import Node;
import String;

public alias Bindings = map[str varName, AType varType];

public Bindings unifyRascalTypeParams(AType r, AType s, Bindings b){
    b2 = matchRascalTypeParams(r, s, b);
    return matchRascalTypeParams(s, r, b2);
}

public Bindings unifyRascalTypeParams(list[AType] r, list[AType] s, Bindings b){
    if(size(r) != size(s))
        throw rascalCheckerInternalError("unifyRascalTypeParams: length unequal");
    idx = index(r);
    for(int i <- idx){
        b = matchRascalTypeParams(r[i], s[i], b);
        b = matchRascalTypeParams(s[i], r[i], b);
    }
    return b;
}

public Bindings matchRascalTypeParams(list[AType] r, list[AType] s, Bindings b){
    if(size(r) != size(s))
        throw rascalCheckerInternalError("matchRascalTypeParams: length unequal");
    idx = index(r);
    for(int i <- idx){
        b = matchRascalTypeParams(r[i], s[i], b);
    }
    return b;
}
// TODO: Add support for bags if we ever get around to supporting them...

public Bindings matchRascalTypeParams(AType r, AType s, Bindings b) {
    if(tvar(_) := r) throw TypeUnavailable();
    if(tvar(_) := s) throw TypeUnavailable();
    if (!typeContainsRascalTypeParams(r)) return b;
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := r){
        lb = lubList(toList(overloads<2>));
        return matchRascalTypeParams0(lb, s, b);
    }
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := s){
        lb = lubList(toList(overloads<2>));
        return matchRascalTypeParams0(r, lb, b);
    }
    
    return matchRascalTypeParams0(r,s,b);
}

public Bindings matchRascalTypeParams0(AType r, AType s, Bindings b) {

    // The simple case: if the receiver is a basic type or a node 
    // (i.e., has no internal structure), just do a comparability
    // check. The receiver obviously does not contain a parameter.
    if (arity(r) == 0 && comparable(s,r)) return b;

    // Another simple case: if the receiver has no type vars, then just return the current bindings.
    if (!typeContainsRascalTypeParams(r)) return b;
    
    if (isRascalTypeParam(r)) {
        str varName = getRascalTypeParamName(r);
        AType varBound = getRascalTypeParamBound(r);
        if(isRascalTypeParam(s)){
            if (varName in b) {
                lubbed = alub(s, b[varName]);
                if (!asubtype(lubbed, varBound))
                    throw invalidMatch("Type parameter `<deUnique(varName)>` should be less than <prettyAType(varBound)>, but is bound to <prettyAType(lubbed)>");
                b[varName] = lubbed;
            } else {
                b[varName] = s;
            }
        } else {
            if (varName in b) {
                if(!isRascalTypeParam(b[varName])){
                    b = matchRascalTypeParams0(b[varName], s, b);
                    r1 = instantiateRascalTypeParameters(b[varName], b);
                    lubbed = alub(s, r1);
                    if (!asubtype(lubbed, varBound))
                        throw invalidMatch("Type parameter `<deUnique(varName)>` should be less than <prettyAType(varBound)>, but is bound to <prettyAType(lubbed)>");
                    b[varName] = lubbed;
                 }
            } else {
                b[varName] = s;
            }
        }
        return b;
    }
        
    // For sets and relations, check to see if the "contents" are
    // able to be matched to one another
    if ( isSetAType(r) && isSetAType(s) ) {
        if ( isRelAType(r) && isVoidAType(getSetElementType(s)) ) {
            return matchRascalTypeParams0(getSetElementType(r), atuple(atypeList([avoid() | _ <- index(getRelFields(r))])), b);
        } else {    
            return matchRascalTypeParams0(getSetElementType(r), getSetElementType(s), b);
        }
    }
        
    // For lists and list relations, check to see if the "contents" are
    // able to be matched to one another
    if ( isListAType(r) && isListAType(s) ) {
        if ( isListRelAType(r) && isVoidAType(getListElementType(s)) ) {
            return matchRascalTypeParams0(getListElementType(r), atuple(atypeList([avoid() | _ <- index(getListRelFields(r))])), b);
        } else {
            return matchRascalTypeParams0(getListElementType(r), getListElementType(s), b);
        }
    }
        
    // For maps, match the domains and ranges
    if ( isMapAType(r) && isMapAType(s) )
        return matchRascalTypeParams0(getMapFieldsAsTuple(r), getMapFieldsAsTuple(s), b);
    
    // For reified types, match the type being reified
    if ( isReifiedAType(r) && isReifiedAType(s) )
        return matchRascalTypeParams0(getReifiedType(r), getReifiedType(s), b);

    // For ADTs, try to match parameters when the ADTs are the same
    if ( isADTAType(r) && isADTAType(s) && getADTName(r) == getADTName(s) && size(getADTTypeParameters(r)) == size(getADTTypeParameters(s))) {
        rparams = getADTTypeParameters(r);
        sparams = getADTTypeParameters(s);
        for (idx <- index(rparams)) b = matchRascalTypeParams0(rparams[idx], sparams[idx], b);
        return b;
    }
            
    // For constructors, match when the constructor name, ADT name, and arity are the same, then we can check params
    if ( isConstructorAType(r) && isConstructorAType(s) && getADTName(r) == getADTName(s)) {
        b = matchRascalTypeParams0(getConstructorArgumentTypesAsTuple(r), getConstructorArgumentTypesAsTuple(s), b);
        return matchRascalTypeParams0(getConstructorResultType(r), getConstructorResultType(s), b);
    }
    
    if ( isConstructorAType(r) && isADTAType(s) ) {
        return matchRascalTypeParams0(getConstructorResultType(r), s, b);
    }
    
    // For functions, match the return types and the parameter types
    // TODO: kewyword params?
    if ( isFunctionAType(r) && isFunctionAType(s) ) {
        b = matchRascalTypeParams0(getFunctionArgumentTypesAsTuple(r), getFunctionArgumentTypesAsTuple(s), b);
        return matchRascalTypeParams0(getFunctionReturnType(r), getFunctionReturnType(s), b);
    }
    
    // For tuples, check the arity then match the item types
    if ( isTupleAType(r) && isTupleAType(s) && getTupleFieldCount(r) == getTupleFieldCount(s) ) {
        rfields = getTupleFieldTypes(r);
        sfields = getTupleFieldTypes(s);
        for (idx <- index(rfields)) {
            b = matchRascalTypeParams0(rfields[idx], sfields[idx], b);
        }
        return b;
    }
    
    if(comparable(r, s)) return b;
    
    throw invalidMatch("Types <prettyAType(r)> and <prettyAType(s)> do not match");
}

AType invalidInstantiation(str pname, AType bound, AType actual){
    throw invalidInstantiation("Type parameter `<pname>` should be less than `<prettyAType(bound)>`, but is bound to `<prettyAType(actual)>`");  
}

AType makeClosedTypeParams(AType t){
    return visit(t) { case par:aparameter(_,_) => par[closed=true] };
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

// NOTE used during match, no bounds check is needed since that is already done during the match
AType instantiateRascalTypeParameters(AType t, Bindings bindings){
    if(isEmpty(bindings))
        return t;
    else
        return visit(t) { case param:aparameter(str pname, AType _bound): {
                                if(bindings[pname]?){
                                    repl = param.alabel? ? bindings[pname][alabel=param.alabel] :  bindings[pname]; //TODO simplified for compiler
                                    insert repl;
                                  } else {
                                    insert param;
                                  }
                               }
                        };
}

@doc{Instantiate type parameters found inside one atype.}
AType instantiateRascalTypeParameters(Tree selector, AType t, Bindings bindings, Solver s){
    if(isEmpty(bindings))
        return t;
    else
        return visit(t) { case param:aparameter(str pname, AType bound): {
                                if(bindings[pname]?){
                                    if(asubtype(bindings[pname], bound)){
                                        repl = param.alabel? ? bindings[pname][alabel=param.alabel] :  bindings[pname]; //TODO simplified for compiler
                                        insert repl;
                                    }
                                    else {
                                        s.report(error(selector, "Type parameter %q should be less than %t, found %t", deUnique(pname), bound, bindings[pname]));
                                    }
                                  } else {
                                        insert param;
                                  }
                               }
                        };
}

@doc{Instantiate type parameters found inside two atypes.}
tuple[AType left, AType right] instantiateRascalTypeParameters(Tree selector, AType ltype, AType rtype, Bindings bindings, Solver s){
    return <instantiateRascalTypeParameters(selector, ltype, bindings, s),
            instantiateRascalTypeParameters(selector, rtype, bindings, s)
           >;
}