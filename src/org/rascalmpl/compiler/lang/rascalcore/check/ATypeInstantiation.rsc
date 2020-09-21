

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

extend analysis::typepal::TypePal;
 
//extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeUtils;
//extend lang::rascalcore::check::ATypeExceptions;

import Map;
import Set;
import IO;
import Node;

public alias Bindings = map[str varName, AType varType];

// TODO: Add support for bags if we ever get around to supporting them...
// TODO: Add support for overloaded types if they can make it to here (this is
// usually invoked on specific types that are inside overloads)
public Bindings matchRascalTypeParams(AType r, AType s, Bindings b) {
    //println("matchRascalTypeParams: <r>, <s>");
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
    
    res = matchRascalTypeParams0(r,s,b);
    return res;
}

public Bindings matchRascalTypeParams0(AType r, AType s, Bindings b) {

    // The simple case: if the receiver is a basic type or a node 
    // (i.e., has no internal structure), just do a comparability
    // check. The receiver obviously does not contain a parameter.
    if (arity(r) == 0 && comparable(s,r)) return b;

    // Another simple case: if the receiver has no type vars, then just return the current bindings.
    if (!typeContainsRascalTypeParams(r)) return b;
        
    // Handle type parameters
    //if (isRascalTypeParam(r) && isRascalTypeParam(s) && getRascalTypeParamName(r) == getRascalTypeParamName(s) && getRascalTypeParamBound(r) == getRascalTypeParamBound(s) && !bindIdenticalVars) {
    //    return b;
    //}
    
    if (isRascalTypeParam(r)) {
        str varName = getRascalTypeParamName(r);
        AType varBound = getRascalTypeParamBound(r);
        
        if (varName in b) {
            lubbed = alub(s, b[varName]);
            if (!asubtype(lubbed, varBound))
                throw invalidMatch("Type parameter `<varName>` should be less than <prettyAType(varBound)>, but is bound to <prettyAType(lubbed)>");
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
            return matchRascalTypeParams0(getSetElementType(r), atuple(atypeList([avoid() | idx <- index(getRelFields(r))])), b);
        } else if ( isVoidType(getSetElementType(s)) ) {
            return b;
        } else {    
            return matchRascalTypeParams0(getSetElementType(r), getSetElementType(s), b);
        }
    }
        
    // For lists and list relations, check to see if the "contents" are
    // able to be matched to one another
    if ( isListType(r) && isListType(s) ) {
        if ( isListRelType(r) && isVoidType(getListElementType(s)) ) {
            return matchRascalTypeParams0(getListElementType(r), atuple(atypeList([avoid() | idx <- index(getListRelFields(r))])), b);
        } else if ( isVoidType(getListElementType(s)) ) {
            return b;
        } else {
            return matchRascalTypeParams0(getListElementType(r), getListElementType(s), b);
        }
    }
        
    // For maps, match the domains and ranges
    if ( isMapType(r) && isMapType(s) )
        return matchRascalTypeParams0(getMapFieldsAsTuple(r), getMapFieldsAsTuple(s), b);
    
    // For reified types, match the type being reified
    if ( isReifiedType(r) && isReifiedType(s) )
        return matchRascalTypeParams0(getReifiedType(r), getReifiedType(s), b);

    // For ADTs, try to match parameters when the ADTs are the same
    if ( isADTType(r) && isADTType(s) && getADTName(r) == getADTName(s) && size(getADTTypeParameters(r)) == size(getADTTypeParameters(s))) {
        rparams = getADTTypeParameters(r);
        sparams = getADTTypeParameters(s);
        for (idx <- index(rparams)) b = matchRascalTypeParams0(rparams[idx], sparams[idx], b);
        return b;
    }
            
    // For constructors, match when the constructor name, ADT name, and arity are the same, then we can check params
    if ( isConstructorType(r) && isConstructorType(s) && getADTName(r) == getADTName(s)) {
        b = matchRascalTypeParams0(getConstructorArgumentTypesAsTuple(r), getConstructorArgumentTypesAsTuple(s), b);
        return matchRascalTypeParams0(getConstructorResultType(r), getConstructorResultType(s), b);
    }
    
    if ( isConstructorType(r) && isADTType(s) ) {
        return matchRascalTypeParams0(getConstructorResultType(r), s, b);
    }
    
    // For functions, match the return types and the parameter types
    if ( isFunctionType(r) && isFunctionType(s) ) {
        b = matchRascalTypeParams0(getFunctionArgumentTypesAsTuple(r), getFunctionArgumentTypesAsTuple(s), b);
        return matchRascalTypeParams0(getFunctionReturnType(r), getFunctionReturnType(s), b);
    }
    
    // For tuples, check the arity then match the item types
    if ( isTupleType(r) && isTupleType(s) && getTupleFieldCount(r) == getTupleFieldCount(s) ) {
        rfields = getTupleFieldTypes(r);
        sfields = getTupleFieldTypes(s);
        for (idx <- index(rfields)) {
            if (!isVoidType(sfields[idx])) {
                b = matchRascalTypeParams0(rfields[idx], sfields[idx], b);
            }
        }
        return b;
    }
    
    if(comparable(r, s)) return b;
    
    throw invalidMatch("Types <prettyAType(r)> and <prettyAType(s)> do not match");
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

AType invalidInstantiation(str pname, AType bound, AType actual){
    throw invalidInstantiation("Type parameter `<pname>` should be less than `<prettyAType(bound)>`, but is bound to `<prettyAType(actual)>`");  
}

AType instantiateRascalTypeParams(aset(AType et), Bindings bindings) 
    = makeSetType(instantiateRascalTypeParams(et,bindings));
AType instantiateRascalTypeParams(arel(AType ets), Bindings bindings) 
    = arel(instantiateRascalTypeParams(ets,bindings));
AType instantiateRascalTypeParams(atuple(AType ets), Bindings bindings) 
    = atuple(instantiateRascalTypeParams(ets,bindings));
AType instantiateRascalTypeParams(alist(AType et), Bindings bindings) 
    = makeListType(instantiateRascalTypeParams(et,bindings));
AType instantiateRascalTypeParams(alrel(AType ets), Bindings bindings) 
    = alrel(instantiateRascalTypeParams(ets,bindings));
AType instantiateRascalTypeParams(amap(AType md, AType mr), Bindings bindings) 
    = amap(instantiateRascalTypeParams(md,bindings), instantiateRascalTypeParams(mr,bindings));
AType instantiateRascalTypeParams(abag(AType et), Bindings bindings) 
    = abag(instantiateRascalTypeParams(et,bindings));
AType instantiateRascalTypeParams(AType pt:aparameter(str s, AType t), Bindings bindings)
    = pt.label? ? bindings[s][label=pt.label] : bindings[s] when s in bindings && asubtype(bindings[s],t);
AType instantiateRascalTypeParams(aparameter(str s, AType t), Bindings bindings) 
    = invalidInstantiation(s,t,bindings[s]) when s in bindings && !asubtype(bindings[s],t);
AType instantiateRascalTypeParams(AType pt:aparameter(str s, AType t), Bindings bindings) 
    = pt when s notin bindings;
AType instantiateRascalTypeParams(a: aadt(str s, list[AType] ps, SyntaxRole sr), Bindings bindings) 
    = aadt(s, [instantiateRascalTypeParams(p,bindings) | p <- ps], sr);
AType instantiateRascalTypeParams(acons(AType a, /*str name,*/ list[AType/*NamedField*/] fields, list[Keyword] kwFields, label=consName), Bindings bindings) = 
    //acons(instantiateRascalTypeParams(a,bindings), /*name,*/ [<fn, instantiateRascalTypeParams(ft,bindings)> | <fn, ft> <- fields], [<fn, instantiateRascalTypeParams(ft,bindings), de> | <fn, ft, de> <- kwFields], label=consName);
    acons(instantiateRascalTypeParams(a,bindings), /*name,*/ [instantiateRascalTypeParams(ft,bindings) | ft <- fields], [<instantiateRascalTypeParams(ft,bindings), de> | <ft, de> <- kwFields], label=consName);
    
AType instantiateRascalTypeParams(aalias(str s, list[AType] ps, AType at), Bindings bindings)
    = aalias(s, [instantiateRascalTypeParams(p,bindings) | p <- ps], instantiateRascalTypeParams(at,bindings));
AType instantiateRascalTypeParams(afunc(AType rt, list[AType] formals, list[Keyword] kwFormals, varArgs=va), Bindings bindings) = 
    //afunc(instantiateRascalTypeParams(rt,bindings),instantiateRascalTypeParams(formals,bindings), [<fn, instantiateRascalTypeParams(ft,bindings), de> | <fn, ft, de> <- kwFormals], varArgs=va);
    afunc(instantiateRascalTypeParams(rt,bindings),[instantiateRascalTypeParams(f, bindings) | f <- formals], [<instantiateRascalTypeParams(ft,bindings), de> | <ft, de> <- kwFormals], varArgs=va);
AType instantiateRascalTypeParams(areified(AType t), Bindings bindings) = areified(instantiateRascalTypeParams(t,bindings));
AType instantiateRascalTypeParams(\start(AType s), Bindings bindings) = \start(instantiateRascalTypeParams(s,bindings));
AType instantiateRascalTypeParams(\iter(AType s), Bindings bindings) = \iter(instantiateRascalTypeParams(s,bindings));
AType instantiateRascalTypeParams(\iter-star(AType s), Bindings bindings) = \iter-star(instantiateRascalTypeParams(s,bindings));
AType instantiateRascalTypeParams(\iter-seps(AType s, list[AType] seps), Bindings bindings) = \iter-seps(instantiateRascalTypeParams(s,bindings),seps);
AType instantiateRascalTypeParams(\iter-star-seps(AType s, list[AType] seps), Bindings bindings) = \iter-star-seps(instantiateRascalTypeParams(s,bindings),seps);
AType instantiateRascalTypeParams(\opt(AType s), Bindings bindings) = \opt(instantiateRascalTypeParams(s,bindings));
AType instantiateRascalTypeParams(\conditional(AType s, set[ACondition] conds), Bindings bindings) = \conditional(instantiateRascalTypeParams(s,bindings),conds);
//AType instantiateRascalTypeParams(\prod(AType def, list[AType] asymbols, set[Attr] attributes=attrs, set[SyntaxKind] syntaxKind=sk, loc src=src), Bindings bindings)
//    = \prod(instantiateRascalTypeParams(def, bindings), asymbols, attributes=attrs, syntaxKind = sk, src=src);
default AType instantiateRascalTypeParams(AType t, Bindings bindings) {
    return t;
}

AType instantiateRascalTypeParams(atypeList(list[AType] l), Bindings bindings)
    = atypeList([instantiateRascalTypeParams(t, bindings) | t <- l]);
    
default AType instantiateRascalTypeParams(value t, Bindings bindings){
    //println("instantiateRascalTypeParams undefined for: <t>");
    throw "instantiateRascalTypeParams undefined for: <t>";
}

AType xxInstantiateRascalTypeParameters(Tree selector, AType t, Bindings bindings, Solver s){
    if(isEmpty(bindings))
        return t;
    else
        return visit(t) { case param:aparameter(str pname, AType bound): {
                                if(bindings[pname]?){
                                    if(asubtype(bindings[pname], bound)){
                                        insert param.label? ? bindings[pname][label=param.label] :  bindings[pname];
                                    }
                                    else {
                                        s.report(error(selector, "Type parameter %q should be less than %t, found %t", pname, bound, bindings[pname]));
                                    }
                                  } else {
                                        insert param;
                                        //throw invalidInstantiation("Type parameter `<pname>` cannot be properly instantiated");
                                  
                                       //s.report(error(selector, "Type parameter %q cannot be properly instantiated", pname));
                                  }
                               }
                        };
}