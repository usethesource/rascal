module rascal::checker::SubTypes

import List;
import Set;

import rascal::checker::Types;

// Encode the subtyping relation t1 <: t2
// TODO: Look through IMP and add extra subtyping rules
// TODO: Add support for functions
public bool subtypeOf(RType t1, RType t2) {
	// First, unroll aliases
	t1 = unwindAliases(t1);
	t2 = unwindAliases(t2);
	
	// If the types match, they are by default subtypes of one another: forall t : Types, t <: t
	if (t1 == t2) return true;
	
	// Void is a subtype of all types: forall t : Types, void <: t
	if (isVoidType(t1)) return true;
	
	// Value is a supertype of all types: forall t : Types, t <: value 
	if (isValueType(t2)) return true;

	// Num is a supertype of int and real
	if (isIntType(t1) && isNumType(t2)) return true;
	if (isRealType(t1) && isNumType(t2)) return true;
	
	// Inferred types can act as supertypes for all types; this specifically allows them to be used in
	// assignments, where, for a = e, type(e) must be <: type(a)
	if (isInferredType(t2)) return true;
	
	// They are also subtypes for all types
	if (isInferredType(t1)) return true;
	
	// t1 <: t2 -> list[t1] <: list[t2]
	if (isListType(t1) && isListType(t2) && subtypeOf(getListElementType(t1),getListElementType(t2))) return true;

	// t1 <: t2 -> set[t1] <: set[t2]
	if (isSetType(t1) && isSetType(t2) && subtypeOf(getSetElementType(t1),getSetElementType(t2))) return true;

	if (isContainerType(t1) && isListType(t2) && subtypeOf(getContainerElementType(t1),getListElementType(t2))) return true;

	if (isContainerType(t1) && isSetType(t2) && subtypeOf(getContainerElementType(t1),getSetElementType(t2))) return true;

	// tuples
	if (RTupleType(t1s) := t1 && RTupleType(t2s) := t2 && size(t1s) == size(t2s)) {
		return size([ n | n <- [0 .. (size(t1s)-1)], !subtypeOf(getElementType(t1s[n]),getElementType(t2s[n]))]) == 0; 
	}

	// type variables; for now, just allow them to always be subtypes of everything, and the
	// checking code will then enforce any bounds
	if (isTypeVar(t1) || isTypeVar(t2)) return true;

	// Lub Types are sub- and super-types of everything; we handle them instead
	// explicitly in the code
	if (RLubType(_) := t1 || RLubType(_) := t2) return true;
	
	// Default case: if none of the above match,  not t1 <: t2
	return false;
}

// Calculate the least upper bound of t1 and t2
public RType lub(RType t1, RType t2) {
	// First, unroll aliases
	t1a = unwindAliases(t1);
	t2a = unwindAliases(t2);
	
	// Two matching types are their own least upper bound
	if (t1a == t2a) return t1; // or t2

	// forall t2. lub(void,t2) = t2
	if (t1a == RVoidType()) return t2;

	// forall t1. lub(t1,void) = t1
	if (t2a == RVoidType()) return t1;

	// forall t1 t2. t1 == value or t2 == value -> lub(t1,t2) = value
	if (t1a == RValueType() || t2a == RValueType()) return makeValueType();

	// lub for int/real/num combinations
	if (isIntType(t1a) && isRealType(t2a)) return makeNumType();
	if (isIntType(t1a) && isNumType(t2a)) return makeNumType();
	if (isRealType(t1a) && isIntType(t2a)) return makeNumType();
	if (isRealType(t1a) && isNumType(t2a)) return makeNumType();
	if (isNumType(t1a) && isIntType(t2a)) return makeNumType();
	if (isNumType(t1a) && isRealType(t2a)) return makeNumType();
	
	// inferred types; we just return the other type if one is not inferred, but
	// we need to handle this situation elsewhere then -- if we return value here
	// we cause problems elsewhere
	if (isInferredType(t1a) && isInferredType(t2a)) return t1;
	if (isInferredType(t1a)) return t2;
	if (isInferredType(t2a)) return t1;
		
	// lub(list[t1],list[t2]) = list(lub(t1,t2))
	if (isListType(t1a) && isListType(t2a)) return makeListType(lub(getListElementType(t1a),getListElementType(t2a)));

	// lub(set[t1],set[t2]) = set[lub(t1,t2)]
	if (isSetType(t1a) && isSetType(t2a)) return makeSetType(lub(getSetElementType(t1a),getSetElementType(t2a)));

	if (isContainerType(t1a) && isListType(t2a)) return makeListType(lub(getContainerElementType(t1a),getListElementType(t2a)));

	if (isContainerType(t1a) && isSetType(t2a)) return makeSetType(lub(getContainerElementType(t1a),getSetElementType(t2a)));

	// Default case: if none of the above match,  the lub is a value
	return makeValueType();
}

// Calculate the least upper bound of an entire list of types
public RType lubList(list[RType] tl) {
	RType resultType = makeVoidType();
	for (t <- tl) resultType = lub(resultType,t);
	return resultType; // TODO: Switch to reduce syntax...
}

public RType lubSet(set[RType] tl) {
	RType resultType = makeVoidType();
	for (t <- tl) resultType = lub(resultType,t);
	return resultType; 
}
