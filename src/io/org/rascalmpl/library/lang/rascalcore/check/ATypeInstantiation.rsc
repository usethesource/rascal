

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
module lang::rascalcore::check::ATypeInstantiation

import Set;
import IO;
import Node;
extend analysis::typepal::TypePal;
import lang::rascalcore::check::ATypeExceptions;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
import analysis::typepal::ExtractTModel;

public alias Bindings = map[str varName, AType varType];

// TODO: Add support for bags if we ever get around to supporting them...
// TODO: Add support for overloaded types if they can make it to here (this is
// usually invoked on specific types that are inside overloads)
public Bindings matchRascalTypeParams(AType r, AType s, Bindings b, bool bindIdenticalVars=false) {
    if (!typeContainsRascalTypeParams(r)) return b;
    return matchRascalTypeParams(r,s,b,bindIdenticalVars);
}

public Bindings matchRascalTypeParams(AType r, AType s, Bindings b, bool bindIdenticalVars) {

    // The simple case: if the receiver is a basic type or a node 
    // (i.e., has no internal structure), just do a comparability
    // check. The receiver obviously does not contain a parameter.
    if (arity(r) == 0 && comparable(s,r)) return b;

    // Another simple case: if the receiver has no type vars, then just return the current bindings.
    if (!typeContainsRascalTypeParams(r)) return b;
        
    // Handle type parameters
    if (isRascalTypeParam(r) && isRascalTypeParam(s) && getRascalTypeParamName(r) == getRascalTypeParamName(s) && getRascalTypeParamBound(r) == getRascalTypeParamBound(s) && !bindIdenticalVars) {
        return b;
    }
    
    if (isRascalTypeParam(r)) {
        varName = getRascalTypeParamName(r);
        varBound = getRascalTypeParamBound(r);
        
        if (varName in b) {
            lubbed = lub([s, b[varName]]);
            if (!asubtype(lubbed, varBound))
                throw invalidMatch("Type parameter <fmt(varName)> should be less than <fmt(varBound)>, but is bound to <fmt(lubbed)>");
            b[varName] = lubbed;
        } else {
            b[varName] = s;
        }
        
        return b;
    }
        
    // For sets and relations, check to see if the "contents" are
    // able to be matched to one another
    if ( isSetType(r) && isSetType(s) ) {
        if ( isRelType(r) && isVoidType(getSetElementType(s)) ) {
            return matchRascalTypeParams(getSetElementType(r), atuple([avoid() | idx <- index(getRelFields(r))]), b, bindIdenticalVars);
        } else if ( isVoidType(getSetElementType(s)) ) {
            return b;
        } else {    
            return matchRascalTypeParams(getSetElementType(r), getSetElementType(s), b, bindIdenticalVars);
        }
    }
        
    // For lists and list relations, check to see if the "contents" are
    // able to be matched to one another
    if ( isListType(r) && isListType(s) ) {
        if ( isListRelType(r) && isVoidType(getListElementType(s)) ) {
            return matchRascalTypeParams(getListElementType(r), atuple(atypeList([avoid() | idx <- index(getListRelFields(r))])), b, bindIdenticalVars);
        } else if ( isVoidType(getListElementType(s)) ) {
            return b;
        } else {
            return matchRascalTypeParams(getListElementType(r), getListElementType(s), b, bindIdenticalVars);
        }
    }
        
    // For maps, match the domains and ranges
    if ( isMapType(r) && isMapType(s) )
        return matchRascalTypeParams(getMapFieldsAsTuple(r), getMapFieldsAsTuple(s), b, bindIdenticalVars);
    
    // For reified types, match the type being reified
    if ( isReifiedType(r) && isReifiedType(s) )
        return matchRascalTypeParams(getReifiedType(r), getReifiedType(s), b, bindIdenticalVars);

    // For ADTs, try to match parameters when the ADTs are the same
    if ( isADTType(r) && isADTType(s) && getADTName(r) == getADTName(s) && size(getADTTypeParameters(r)) == size(getADTTypeParameters(s))) {
        rparams = getADTTypeParameters(r);
        sparams = getADTTypeParameters(s);
        for (idx <- index(rparams)) b = matchRascalTypeParams(rparams[idx], sparams[idx], b, bindIdenticalVars);
        return b;
    }
            
    // For constructors, match when the constructor name, ADT name, and arity are the same, then we can check params
    if ( isConstructorType(r) && isConstructorType(s) && getADTName(r) == getADTName(s)) {
        b = matchRascalTypeParams(getConstructorArgumentTypesAsTuple(r), getConstructorArgumentTypesAsTuple(s), b, bindIdenticalVars);
        return matchRascalTypeParams(getConstructorResultType(r), getConstructorResultType(s), b, bindIdenticalVars);
    }
    
    if ( isConstructorType(r) && isADTType(s) ) {
        return matchRascalTypeParams(getConstructorResultType(r), s, b, bindIdenticalVars);
    }
    
    // For functions, match the return types and the parameter types
    if ( isFunctionType(r) && isFunctionType(s) ) {
        b = matchRascalTypeParams(getFunctionArgumentTypesAsTuple(r), getFunctionArgumentTypesAsTuple(s), b, bindIdenticalVars);
        return matchRascalTypeParams(getFunctionReturnType(r), getFunctionReturnType(s), b, bindIdenticalVars);
    }
    
    // For tuples, check the arity then match the item types
    if ( isTupleType(r) && isTupleType(s) && getTupleFieldCount(r) == getTupleFieldCount(s) ) {
        rfields = getTupleFieldTypes(r);
        sfields = getTupleFieldTypes(s);
        for (idx <- index(rfields)) {
            if (!isVoidType(sfields[idx])) {
                b = matchRascalTypeParams(rfields[idx], sfields[idx], b, bindIdenticalVars);
            }
        }
        return b;
    }
    
    throw invalidMatch("Types <fmt(r)> and <fmt(s)> do not match");
}

@doc{Instantiate type parameters found inside the types.}
//AType instantiateRascalTypeParams(AType atype, Bindings bindings){
//    return visit(atype){
//        case aparameter(str pname, AType bound): {
//             if(pname in bindings){
//                actual = bindings[pname];
//                if(asubtype(actual, bound)) insert actual;  
//                throw invalidInstantiation("Type parameter <fmt(pname)> should be less than <fmt(bound)>, but is bound to <fmt(actual)>");  
//             } else {
//                throw invalidInstantiation("Type parameter <fmt(pname)> unbound");
//             }
//        }
//    }
//}

void invalidInstantiation(str pname, AType bound, AType actual){
    throw invalidInstantiation("Type parameter <fmt(pname)> should be less than <fmt(bound)>, but is bound to <fmt(actual)>");  
}

AType instantiateRascalTypeParams(AType::aset(AType et), Bindings bindings) 
    = makeSetType(instantiateRascalTypeParams(et,bindings));
AType instantiateRascalTypeParams(AType::arel(AType ets), Bindings bindings) 
    = AType::arel(instantiateRascalTypeParams(ets,bindings));
AType instantiateRascalTypeParams(AType::atuple(AType ets), Bindings bindings) 
    = AType::atuple(instantiateRascalTypeParams(ets,bindings));
AType instantiateRascalTypeParams(AType::alist(AType et), Bindings bindings) 
    = makeListType(instantiateRascalTypeParams(et,bindings));
AType instantiateRascalTypeParams(AType::alrel(AType ets), Bindings bindings) 
    = AType::alrel(instantiateRascalTypeParams(ets,bindings));
AType instantiateRascalTypeParams(AType::amap(AType md, AType mr), Bindings bindings) 
    = AType::amap(instantiateRascalTypeParams(md,bindings), instantiateRascalTypeParams(mr,bindings));
AType instantiateRascalTypeParams(AType::abag(AType et), Bindings bindings) 
    = AType::abag(instantiateRascalTypeParams(et,bindings));
AType instantiateRascalTypeParams(AType::aparameter(str s, AType t), Bindings bindings) 
    = bindings[s] when s in bindings && asubtype(bindings[s],t);
AType instantiateRascalTypeParams(AType::aparameter(str s, AType t), Bindings bindings) 
    = invalidInstantiation(s,t,bindings[s]) when s in bindings && !asubtype(bindings[s],t);
AType instantiateRascalTypeParams(AType pt:aparameter(str s, AType t), Bindings bindings) 
    = pt when s notin bindings;
AType instantiateRascalTypeParams(AType::aadt(str s, list[AType] ps), Bindings bindings) 
    = AType::aadt(s,[instantiateRascalTypeParams(p,bindings) | p <- ps]);
AType instantiateRascalTypeParams(AType::acons(AType a, str name, list[NamedField] fields, list[Keyword] kwFields), Bindings bindings) = 
    AType::acons(instantiateRascalTypeParams(a,bindings), name, [<instantiateRascalTypeParams(ft,bindings), nm> | <ft, nm> <- fields], [<instantiateRascalTypeParams(ft,bindings), fn, de> | <fn, ft, de> <- kwFields]);
AType instantiateRascalTypeParams(AType::aalias(str s, list[AType] ps, AType at), Bindings bindings)
    = AType::aalias(s, [instantiateRascalTypeParams(p,bindings) | p <- ps], instantiateRascalTypeParams(at,bindings));
AType instantiateRascalTypeParams(AType::afunc(AType rt, list[AType] formals, list[Keyword] kwFormals, varArgs=va), Bindings bindings) = 
    AType::afunc(instantiateRascalTypeParams(rt,bindings),[instantiateRascalTypeParams(f,bindings) | f <- formals], [<instantiateRascalTypeParams(ft,bindings), fn, de> | <fn, ft, de> <- kws], varArgs=va);
//AType instantiateRascalTypeParams(\var-func(AType rt, list[AType] ps, AType va), Bindings bindings) = \var-func(instantiateRascalTypeParams(rt,bindings),[instantiateRascalTypeParams(p,bindings) | p <- ps],instantiateRascalTypeParams(va,bindings));
//AType instantiateRascalTypeParams(AType::areified(AType t), Bindings bindings) = AType::areified(instantiateRascalTypeParams(t,bindings));
//AType instantiateRascalTypeParams(AType::\aparameterized-sort(str n, list[AType] ts), Bindings bindings) = AType::aparameterized-sort(n, [instantiateRascalTypeParams(p,bindings) | p <- ts]);
//AType instantiateRascalTypeParams(AType::\aparameterized-lex(str n, list[AType] ts), Bindings bindings) = AType::aparameterized-lex(n, [instantiateRascalTypeParams(p,bindings) | p <- ts]);
//AType instantiateRascalTypeParams(AType::\start(AType s), Bindings bindings) = AType::\start(instantiateRascalTypeParams(s,bindings));
//AType instantiateRascalTypeParams(AType::\iter(AType s), Bindings bindings) = AType::\iter(instantiateRascalTypeParams(s,bindings));
//AType instantiateRascalTypeParams(AType::\iter-star(AType s), Bindings bindings) = AType::\iter-star(instantiateRascalTypeParams(s,bindings));
//AType instantiateRascalTypeParams(AType::\iter-seps(AType s, list[AType] seps), Bindings bindings) = AType::\iter-seps(instantiateRascalTypeParams(s,bindings),seps);
//AType instantiateRascalTypeParams(AType::\iter-star-seps(AType s, list[AType] seps), Bindings bindings) = AType::\iter-star-seps(instantiateRascalTypeParams(s,bindings),seps);
//AType instantiateRascalTypeParams(AType::\opt(AType s), Bindings bindings) = AType::\opt(instantiateRascalTypeParams(s,bindings));
//AType instantiateRascalTypeParams(AType::\conditional(AType s, set[Condition] conds), Bindings bindings) = AType::\conditional(instantiateRascalTypeParams(s,bindings),conds);
//AType instantiateRascalTypeParams(AType::\prod(AType s, str name, list[AType] parameters, set[Attr] attributes), Bindings bindings) = AType::\prod(instantiateRascalTypeParams(s,bindings),name,parameters,attributes);
default AType instantiateRascalTypeParams(AType t, Bindings bindings) {
    return t;
}

AType instantiateRascalTypeParams(atypeList(list[AType] l), Bindings bindings)
    = atypeList([instantiateRascalTypeParams(t, bindings) | t <- l]);