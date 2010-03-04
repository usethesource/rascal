module org::rascalmpl::checker::SubTypes

import org::rascalmpl::checker::Types;

// Encode the subtyping relation t1 <: t2
public bool subtypeOf(RType t1, RType t2) {

	// If the types match, they are by default subtypes of one another
	if (t1 == t2) return true;
	
	// Void is a subtype of all types: forall t : Types, void <: t
	if (t1 == RTypeBasic(RVoidType())) return true;
	
	// Value is a supertype of all types: forall t : Types, t <: value 
	if (t2 == RTypeBasic(RValueType())) return true;
	
	// t1 <: t2 -> list[t1] <: list[t2]
	if (isListType(t1) && isListType(t2) && subtypeOf(getListElementType(t1),getListElementType(t2))) return true;

	// t1 <: t2 -> set[t1] <: set[t2]
	if (isSetType(t1) && isSetType(t2) && subtypeOf(getSetElementType(t1),getSetElementType(t2))) return true;

	// Default case: if none of the above match,  not t1 <: t2
	return false;
}

// Calculate the least upper bound of t1 and t2
public RType lub(RType t1, RType t2) {

	// Two matching types are their own least upper bound
	if (t1 == t2) return t1; // or t2

	// forall t2. lub(void,t2) = t2
	if (t1 == RTypeBasic(RVoidType())) return t2;

	// forall t1. lub(t1,void) = t1
	if (t2 == RTypeBasic(RVoidType())) return t1;

	// forall t1 t2. t1 == value or t2 == value -> lub(t1,t2) = value
	if (t1 == RTypeBasic(RValueType()) || t2 == RTypeBasic(RValueType())) return makeValueType();

	// lub(list[t1],list[t2]) = list(lub(t1,t2))
	if (isListType(t1) && isListType(t2)) return makeListType(lub(getListElementType(t1),getListElementType(t2)));

	// lub(set[t1],set[t2]) = set[lub(t1,t2)]
	if (isSetType(t1) && isSetType(t2)) return makeSetType(lub(getListElementType(t1),getListElementType(t2)));

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