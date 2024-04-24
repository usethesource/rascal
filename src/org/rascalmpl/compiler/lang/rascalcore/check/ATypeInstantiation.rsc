

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
    Implement instantiation of type parameters
*/

extend lang::rascalcore::check::ATypeUtils;
extend lang::rascalcore::check::NameUtils;

import Map;
import Set;
//import IO;
import Node;

public alias Bindings = map[str varName, AType varType];

public Bindings unifyRascalTypeParams(AType r, AType s, Bindings b){
    b2 = matchRascalTypeParams(r, s, b);
    return matchRascalTypeParams(s, r, b2);

}
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
    if ( isSetAType(r) && isSetAType(s) ) {
        if ( isRelAType(r) && isVoidAType(getSetElementType(s)) ) {
            return matchRascalTypeParams0(getSetElementType(r), atuple(atypeList([avoid() | _ <- index(getRelFields(r))])), b);
        //} else if ( isVoidAType(getSetElementType(s)) ) {
        //    return b;
        } else {    
            return matchRascalTypeParams0(getSetElementType(r), getSetElementType(s), b);
        }
    }
        
    // For lists and list relations, check to see if the "contents" are
    // able to be matched to one another
    if ( isListAType(r) && isListAType(s) ) {
        if ( isListRelAType(r) && isVoidAType(getListElementType(s)) ) {
            return matchRascalTypeParams0(getListElementType(r), atuple(atypeList([avoid() | _ <- index(getListRelFields(r))])), b);
        //} else if ( isVoidAType(getListElementType(s)) ) {
        //    return b;
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
    if ( isFunctionAType(r) && isFunctionAType(s) ) {
        b = matchRascalTypeParams0(getFunctionArgumentTypesAsTuple(r), getFunctionArgumentTypesAsTuple(s), b);
        return matchRascalTypeParams0(getFunctionReturnType(r), getFunctionReturnType(s), b);
    }
    
    // For tuples, check the arity then match the item types
    if ( isTupleAType(r) && isTupleAType(s) && getTupleFieldCount(r) == getTupleFieldCount(s) ) {
        rfields = getTupleFieldTypes(r);
        sfields = getTupleFieldTypes(s);
        for (idx <- index(rfields)) {
            //if (!isVoidAType(sfields[idx])) {
                b = matchRascalTypeParams0(rfields[idx], sfields[idx], b);
            //}
        }
        return b;
    }
    
    if(comparable(r, s)) return b;
    
    throw invalidMatch("Types <prettyAType(r)> and <prettyAType(s)> do not match");
}

AType invalidInstantiation(str pname, AType bound, AType actual){
    throw invalidInstantiation("Type parameter `<pname>` should be less than `<prettyAType(bound)>`, but is bound to `<prettyAType(actual)>`");  
}
    
@doc{Instantiate type parameters found inside an atype.}
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
                                        s.report(error(selector, "Type parameter %q should be less than %t, found %t", pname, bound, bindings[pname]));
                                    }
                                  } else {
                                        insert param;
                                  }
                               }
                        };
}

tuple[AType left, AType right] instantiateRascalTypeParameters(Tree selector, AType ltype, AType rtype, Bindings bindings, Solver s){
    return <instantiateRascalTypeParameters(selector, ltype, bindings, s),
            instantiateRascalTypeParameters(selector, rtype, bindings, s)
           >;
}