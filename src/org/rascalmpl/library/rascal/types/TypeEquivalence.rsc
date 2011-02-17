module rascal::types::TypeEquivalence

import rascal::types::Types;
import rascal::types::TypeExceptions;
import rascal::types::SubTypes;

//
// Returns true when two types are comparable. Two types are comparable if they are
// the same type, or if one is a subtype of the other. The equality check is built
// in to the subtype check, if t1 == t2 then t1 <: t2. This matches the definition
// of comparable given in PDB.
//
public bool comparable(RType t1, RType t2) {
    return subtypeOf(t1,t2) || subtypeOf(t2,t1);
}

//
// Check to see if two types are equivalent. Two types are equivalent if they are both
// subtypes of the other. This matches the definition of equivalent given in PDB.
//
public bool equivalent(RType t1, RType t2) {
    return subtypeOf(t1,t2) && subtypeOf(t2,t1);
}

//
// TODO: Add LOTS of tests!
//
