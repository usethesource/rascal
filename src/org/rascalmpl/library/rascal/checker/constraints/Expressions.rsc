@bootstrapParser
module rascal::checker::constraints::Expressions

import IO;
import List;
import Set;
import Message;
import Map;
import Relation;
import ParseTree;
import Reflective;
import String;

import rascal::checker::ListUtils;
import rascal::checker::Types;
import rascal::checker::SubTypes;
import rascal::checker::SymbolTable;
import rascal::checker::Signature;
import rascal::checker::TypeRules;
import rascal::checker::Namespace;
import rascal::checker::TreeUtils;

import rascal::syntax::RascalRascal;

//
// Collect constraints for the range expression: [ e1 .. e2 ]
//
//     e1 : int, e2 : int
// -------------------------------
// [ e1 .. e2 ] : list[int]
//
public Constraints gatherRangeExpressionConstraints(Constraints cs, Expression ep, Expression e1, Expression e2) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(e1,t2); 
    Constraint c3 = IsType(e2,t3);
    
    // Step 2: Add actual typing information in the constraints    
    Constraint c4 = IsType(t1,makeListType(makeIntType()));
    Constraint c5 = IsType(t2,makeIntType());
    Constraint c6 = IsType(t3,makeIntType());
    
    cs.constraints = cs.contraints + { c1, c2, c3, c4, c5, c6 };
    return cs;
}

//
// Collect constraints for the step range expression: [ e1, e2 .. e3 ]
//
//     e1 : int, e2 : int, e3 : int
// ----------------------------------------
//     [ e1, e2 .. e3 ] : list[int]
//
public Constraints gatherStepRangeExpressionConstraints(Constraints cs, Expression ep, Expression e1, Expression e2, Expression e3) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,ts> = makeFreshTypes(cs,4); t1 = ts[0]; t2 = ts[1]; t3 = ts[2]; t4 = ts[3];
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(e1,t2); 
    Constraint c3 = IsType(e2,t3); 
    Constraint c4 = IsType(e3,t4);

    // Step 2: Add actual typing information in the constraints    
    Constraint c5 = IsType(t1,makeListType(makeIntType()));
    Constraint c6 = IsType(t2,makeIntType());
    Constraint c7 = IsType(t3,makeIntType());
    Constraint c8 = IsType(t4,makeIntType());
    
    cs.constraints = cs.contraints + { c1, c2, c3, c4, c5, c6, c7, c8 };
    return cs;
}

//
// Collect constraints for the field update expression: e1.n = e2
//
// e1 : t1, e2 : t2, n fieldOf t1, t1.n : t3, t2 <: t3
// ----------------------------------------------------
//                e1.n = e2 : t1
//
public Constraints gatherFieldUpdateExpressionConstraints(Constraints cs, Expression ep, Expression el, Name n, Expression er) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,ts> = makeFreshTypes(cs,4); t1 = ts[0]; t2 = ts[1]; t3 = ts[2]; t4 = ts[3];
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(el,t2); 
    Constraint c3 = IsType(er,t3);

    // Step 2: Add actual typing information in the constraints    
    Constraint c4 = IsType(t1,t2); // the overall expression has the same type as el, i.e., x.f = 3 is of the same type as x
    Constraint c5 = FieldOf(n,t2,t4); // name n is actually a field of type t2, i.e., in x.f = 3, f is a field of x, and has type t4
    Constraint c6 = Assignable(t3,t4); // the type of expression being assigned is assignment compatible with the type of the field

    cs.constraints = cs.contraints + { c1, c2, c3, c4, c5, c6 };
    return cs;
}

//
// Collect constraints for the field access expression: e1.n
//
//  e : t1, n fieldOf t1, t1.n : t2
// ----------------------------------------
//        e.n : t2
//
public Constraints gatherFieldAccessExpressionConstraints(Constraints cs, Expression ep, Expression el, Name n) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(el,t2);
    
    // Step 2: Add actual typing information in the constraints    
    Constraint c3 = FieldOf(n,t2,t3); // name n is actually a field of type t2, i.e., in x.f, f is a field of x, and has type t3
    Constraint c4 = IsType(t1,t3); // the overall expression has the same type as the type of field n, i.e., x.f is of type field f

    cs.constraints = cs.contraints + { c1, c2, c3, c4 };
    return cs;
}

//
// Collect constraints for the field projection expression: e<f1.,,,.fn>
//
public Constraints gatherFieldProjectExpressionConstraints(Expression ep, Expression e, {Field ","}+ fl) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(e,t2);
    cs.constraints = cs.constraints + { c1, c2 };
    
    // Step 2: Constrain the actual field types
    list[RType] fieldTypes = [ ];
    for (f <- fl) {
        <cs,ts> = makeFreshTypes(cs,1); t3 = ts[0];
        Constraint c3 = NamedFieldOf(f,t2,t3);
        fieldTypes += t3;
        cs.constraints = cs.constraints + { c3 };
    }
    
    // Step 3: Constrain the expression type, based both on the type of e and the number
    // of fields.
    <cs,ts> = makeFreshTypes(cs,1); t4 = ts[0]; 
    if (size(fieldTypes) > 1) {
        Constraint c4 = IfTuple(t2,t4,TupleProjection(fieldTypes));
        Constraint c5 = IfNotTuple(t2,t4,RelationProjection(fieldTypes));
        cs.constraints = cs.constraints + { c4, c5 };
    } else {
        Constraint c4 = IfTuple(t2,t4,SingleProjection(fieldTypes[0]));
        Constraint c5 = IfNotTuple(t2,t4,SetProjection(fieldTypes[0]));    
        cs.constraints = cs.constraints + { c4, c5 };
    }

    return cs;
}

//
// Collect constraints for the subscript expression: e[e1...en]
//
// TODO: Finish changes for constraint gathering
//
public Constraints gatherSubscriptExpressionConstraints(Constraints cs, Expression ep, Expression el, {Expression ","}+ es) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(el,t2);
    cs.constraints = cs.constraints + { c1, c2 };
    
    // Step 2: Constrain the actual subscripts
    list[Expression] indices = [ e | e <- es ];
    for (e <- indices) {
        <cs,ts> = makeFreshTypes(cs,1); t3 = ts[0];
        Constraint c3 = IsType(e,t3);
        Constraint c4 = IsType(t3,makeIntType());
        cs.constraints = cs.constraints + { c3, c4 };
    }
    
    // Step 3: Add constraints based on the type of el -- for instance, subscripts on tuples can only
    // contain one index, while subscripts on relations can contain multiple indices.

//    if (isTupleType(expType)) {
//        if (size(indexList) > 1) return makeFailType("Subscripts on tuples must contain exactly one element", ep@\loc);
//        if (! isIntType(indexList[0]@rtype) ) 
//                        return makeFailType("Subscripts on tuples must be of type int, not type <prettyPrintType(indexList[0]@rtype)>", ep@\loc);
//        return lubList(getTupleFields(expType));        
//    } else if (isRelType(expType)) {
//        if (size(indexList) > 1) return makeFailType("Subscripts on nodes must contain exactly one element", ep@\loc);
//        RType relLeftType = getRelFields(expType)[0];
//        RType indexType = lubSet({ e@rtype | e <- indexList});
//        if (! (subtypeOf(relLeftType,indexType) || subtypeOf(indexType,relLeftType))) { 
//            return makeFailType("The subscript type <prettyPrintType(indexType)> must be comparable to the type of the first projection of the relation, <prettyPrintType(relLeftType)>", ep@\loc);
//        }
//        list[RType] resultTypes = tail(getRelFields(expType));
//        if (size(resultTypes) == 1)
//            return makeSetType(resultTypes[0]);
//        else
//            return makeRelType(resultTypes);        
//    } else if (isMapType(expType)) {
//        if (size(indexList) > 1) return makeFailType("Subscripts on nodes must contain exactly one element", ep@\loc);
//        RType domainType = getMapDomainType(expType);
//        RType indexType = indexList[0]@rtype;
//        if (! (subtypeOf(domainType,indexType) || subtypeOf(indexType,domainType))) 
//            return makeFailType("The subscript type <prettyPrintType(indexType)> must be comparable to the domain type <prettyPrintType(domainType)>", ep@\loc);
//        return getMapRangeType(expType);
//    }  else if (isNodeType(expType)) {
//        if (size(indexList) > 1) return makeFailType("Subscripts on nodes must contain exactly one element", ep@\loc);
//        if (! isIntType(indexList[0]@rtype) ) return makeFailType("Subscripts on nodes must be of type int, not type <prettyPrintType(indexList[0]@rtype)>", ep@\loc);
//        return makeValueType();
//    } else if (isListType(expType)) {
//        if (size(indexList) > 1) return makeFailType("Subscripts on lists must contain exactly one element", ep@\loc);
//        if (! isIntType(indexList[0]@rtype) ) return makeFailType("Subscripts on lists must be of type int, not type <prettyPrintType(indexList[0]@rtype)>", ep@\loc);
//        return getListElementType(expType);     
//    } else {
//        return makeFailType("Subscript not supported on type <prettyPrintType(expType)>", ep@\loc);
//    }

    return cs;
}

//
// Collect constraints for the is defined expression
//
//   e : t
// ----------
//   e? : t
//
public Constraints gatherIsDefinedExpressionConstraints(Constraints cs, Expression ep, Expression e) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,l1> = makeFreshTypes(cs,2); t1 = l1[0]; t2 = l1[1];
    Constraint c1 = IsType(ep,t1); Constraint c2 = IsType(e,t2);
    
    // Step 2: Constrain the result: e? has the same type as e 
    Constraint c3 = IsType(t1,t2);
    
    cs.constraints = cs.constraints + { c1, c2, c3 };
    return cs;
}

//
// Collect constraints for the negation expression
//
//      e : bool
// --------------------
//   not e : bool
//
private Constraints gatherNegationExpressionConstraints(Constraints cs, Expression ep, Expression e) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,l1> = makeFreshTypes(cs,2); t1 = l1[0]; t2 = l1[1];
    Constraint c1 = IsType(ep,t1); Constraint c2 = IsType(e,t2);
    
    // Step 2: Constrain the expression and subexpression types: both should be bool
    Constraint c3 = IsType(t1,makeBoolType());
    Constraint c4 = IsType(t2,makeBoolType());
    
    cs.constraints = cs.constraints + { c1, c2, c3, c4 };
    return cs;
}

//
// Collect constraints for the negative expression
//
//      e : t1, -_ : t1 -> t2 defined 
// ---------------------------------------------
//          - e : t2
//
private Constraints gatherNegativeExpressionConstraints(Constraints cs, Expression ep, Expression e) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
    Constraint c1 = IsType(ep,t1); Constraint c2 = IsType(e,t2);
    
    // Step 2: Constrain the overall type: the result type is the result of applying the negative
    // operation to t2, which is t3, so the result is t3.
    Constraint c3 = ConstantAppliable(Negative(), [ t2 ], t3);
    Constraint c4 = IsType(t1,t3);
    
    cs.constraints = cs.constraints + { c1, c2, c3, c4 };
    return cs;
}

//
// Collect constraints for the transitive reflexive closure expression, e*
//
//      e : rel[t1,t2], comparable(t1,t2)
// -----------------------------------------
//      e * : rel[t1,t2]
//
public Constraints gatherTransitiveReflexiveClosureExpressionConstraints(Constraints cs, Expression ep, Expression e) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,ts> = makeFreshTypes(cs,4); t1 = ts[0]; t2 = ts[1]; t3 = ts[2]; t4 = ts[3];
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(e,t2);

    // Step 2: Constrain e, it should be a relation of arity 2, with comparable tuple fields
    Constraint c3 = IsType(t2,makeRelType(t3,t4));
    Constraint c4 = Comparable(t3,t4);
    
    // Step 3: Constrain ep, it should have a type identical to e
    Constraint c5 = IsType(t1,t2);
    
    return cs;
}

//
// Collect constraints for the transitive closure expression, e+
//
//      e : rel[t1,t2], comparable(t1,t2)
// -----------------------------------------
//      e + : rel[t1,t2]
//
public Constraints gatherTransitiveClosureExpressionConstraints(Expression ep, Expression e) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,ts> = makeFreshTypes(cs,4); t1 = ts[0]; t2 = ts[1]; t3 = ts[2]; t4 = ts[3];
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(e,t2);

    // Step 2: Constrain e, it should be a relation of arity 2, with comparable tuple fields
    Constraint c3 = IsType(t2,makeRelType(t3,t4));
    Constraint c4 = Comparable(t3,t4);
    
    // Step 3: Constrain ep, it should have a type identical to e
    Constraint c5 = IsType(t1,t2);
    
    return cs;
}

//
// TODO: To properly check this, we need to keep a map of not just the annotation names and types,
// but of which types those annotations apply to!
//
// TODO: Convert over to use constraints
//
public RType gatherGetAnnotationExpressionConstraints(Expression ep, Expression e, Name n) {
    RType rt = getTypeForName(globalSymbolTable, convertName(n), n@\loc);
    if (checkForFail({ e@rtype, rt })) return collapseFailTypes({ e@rtype, rt });
    return rt;
}

//
// TODO: To properly check this, we need to keep a map of not just the annotation names and types,
// but of which types those annotations apply to!
//
// TODO: Convert over to use constraints
//
public RType gatherSetAnnotationExpressionConstraints(Expression ep, Expression el, Name n, Expression er) {
    RType rt = getTypeForName(globalSymbolTable, convertName(n), n@\loc);
    if (checkForFail({ el@rtype, rt, er@rtype })) return collapseFailTypes({ el@rtype, rt, er@rtype });
    if (! subtypeOf(er@rtype, rt)) return makeFailType("The type of <er>, <prettyPrintType(er@rtype)>, must be a subtype of the type of <n>, <prettyPrintType(rt)>", ep@\loc);
    return rt;
}

//
// Composition is defined for functions, maps, and relations.
//
// TODO: Question on order: currently the order is "backwards" from the standard mathematical
// order, i.e., r1 o r2 is r1, then r2, versus r2 first, then r1. Is this the desired behavior, or was
// this accidental? For functions the order appears to be correct, even though the implementation
// doesn't actually work. For maps the order is the same "backwards" order as it is for relations.
//
// NOTE: map composition does not maintain field names. Is this intentional?
//
// TODO: Convert over to use constraints
//
public RType gatherCompositionExpressionConstraints(Expression ep, Expression el, Expression er) {
    if (checkForFail({ el@rtype, er@rtype })) return collapseFailTypes({ el@rtype, er@rtype });
    RType leftType = el@rtype; RType rightType = er@rtype;
    if (isFunType(leftType) && isFunType(rightType)) {
        return makeFailType("Type checking this feature is not yet supported!", ep@\loc); // TODO: Implement this, including support for overloading
    } else if (isMapType(leftType) && isMapType(rightType)) {
        // Collect constraints for to make sure the fields are of the right type to compose
        RType j1 = getMapRangeType(leftType); RType j2 = getMapDomainType(rightType);
        if (! subtypeOf(j1,j2)) return makeFailType("Incompatible types in composition: <prettyPrintType(j1)> and <prettyPrintType(j2)>", ep@\loc);

        return RMapType(getMapDomainType(leftType), getMapRangeType(rightType));
    } else if (isRelType(leftType) && isRelType(rightType)) {
        list[RNamedType] leftFields = getRelFieldsWithNames(leftType); 
        list[RNamedType] rightFields = getRelFieldsWithNames(rightType);

        // Collect constraints for to make sure each relation is just arity 2
        if (size(leftFields) != 2) return makeFailType("Error in composition: <el> should be a relation of arity 2, but instead is <prettyPrintType(leftType)>", ep@\loc);
        if (size(rightFields) != 2) return makeFailType("Error in composition: <er> should be a relation of arity 2, but instead is <prettyPrintType(rightType)>", ep@\loc);

        // Collect constraints for to make sure the fields are of the right type to compose
        RType j1 = getElementType(head(tail(leftFields,1))); RType j2 = getElementType(head(rightFields));
        if (! subtypeOf(j1,j2)) return makeFailType("Incompatible types in composition: <prettyPrintType(j1)> and <prettyPrintType(j2)>", ep@\loc);

        // Collect constraints for to see if we need to drop the field names, then return the proper type
        RNamedType r1 = head(leftFields); RNamedType r2 = head(tail(rightFields,1));
        if (RNamedType(t1,n) := r1 && RNamedType(t2,n) := r2)
            return RRelType([RUnnamedType(t1),RUnnamedType(t2)]); // Both fields had the same name, so just keep the type and make unnamed fields
        else
            return RRelType([r1,r2]); // Maintain the field names, they differ
    }
    return makeFailType("Composition is not supported on types <prettyPrintType(leftType)> and <prettyPrintType(rightType)>", ep@\loc);
}

//
// Collect constraints for binary expressions
//
//      e1 : t1, e2 : t2, rop : t3 x t4 -> t5 defined, t1 <: t3, t2 <: t4
// ------------------------------------------------------------------------
//                            e1 rop e2 : t5
//
// NOTE: The subtyping check is in the ConstantAppliable logic
//
public Constraints gatherBinaryExpressionConstraints(Constraints cs, Expression ep, Expression el, RConstantOp rop, Expression er) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,l1> = makeFreshTypes(cs,4); t1 = l1[0]; t2 = l1[1]; t3 = l1[2]; t4 = l1[3];
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(el,t2); 
    Constraint c3 = IsType(er,t3);
    
    // Step 2: Constrain the result, based on the result of applying the operation
    Constraint c4 = ConstantAppliable(rop, [ t2, t3 ], t4);
    Constraint c5 = IsType(t1, t4);

    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5 };

    return cs;
}

//
// Collect constraints for binary expressions
//
//      e1 : t1, e2 : t2, rop : t3 x t4 -> t5 defined, t1 <: t3, t2 <: t4, t6 given, t5 = t6
// --------------------------------------------------------------------------------------------
//                            e1 rop e2 : t6
//
// NOTE: The subtyping check is in the ConstantAppliable logic
//
public Constraints gatherBinaryExpressionConstraints(Constraints cs, Expression ep, Expression el, RConstantOp rop, Expression er, RType resultType) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,l1> = makeFreshTypes(cs,4); t1 = l1[0]; t2 = l1[1]; t3 = l1[2]; t4 = l1[3];
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(el,t2); 
    Constraint c3 = IsType(er,t3);

    // Step 2: Constrain the result, based on the result of applying the operation, and force
    // it to also be the same as the given resultType
    Constraint c4 = ConstantAppliable(rop, [ t2, t3 ], t4);
    Constraint c5 = IsType(t1, t4);
    Constraint c6 = IsType(t1, resultType); // resultType is "t6 given" in the rule above

    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5, c6 };

    return cs;
}

//
// Collect constraints for the product expression e1 * e2
//
public Constraints gatherProductExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(cs, ep, el, Product(), er);
}

//
// Collect constraints for the join expression e1 join e2
//
public Constraints gatherJoinExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(cs, ep, el, Join(), er);
}

//
// Collect constraints for the div expression e1 / e2
//
public Constraints gatherDivExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(cs, ep, el, Div(), er);
}

//
// Collect constraints for the mod expression e1 % e2
//
public Constraints gatherModExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(cs, ep, el, Mod(), er);
}

//
// Collect constraints for the intersection expression e1 & e2
//
public Constraints gatherIntersectionExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(cs, ep, el, Intersect(), er);
}

//
// Collect constraints for the plus expression e1 + e2
//
public Constraints gatherPlusExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(cs, ep, el, Plus(), er);
}

//
// Collect constraints for the minus expression e1 - e2
//
public Constraints gatherMinusExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(cs, ep, el, Minus(), er);
}

//
// Collect constraints for the notin expression e1 notin e2
//
public Constraints gatherNotInExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(cs, ep, el, NotIn(), er, makeBoolType());
}

//
// Collect constraints for the in expression e1 in e2
//
public Constraints gatherInExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(cs, ep, el, In(), er, makeBoolType());
}

//
// Collect constraints for the Less Than expression e1 < e2
//
public Constraints gatherLessThanExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(cs, ep, el, Lt(), er, makeBoolType());
}

//
// Collect constraints for the Less Than or Equal expression e1 <= e2
//
public Constraints gatherLessThanOrEqualExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(cs, ep, el, LtEq(), er, makeBoolType());
}

//
// Collect constraints for the Greater Than expression e1 > e2
//
public Constraints gatherGreaterThanExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(cs, ep, el, Gt(), er, makeBoolType());
}

//
// Collect constraints for the Greater Than or Equal expression e1 >= e2
//
public Constraints gatherGreaterThanOrEqualExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(cs, ep, el, GtEq(), er, makeBoolType());
}

//
// Collect constraints for the Equals expression e1 == e2
//
public Constraints gatherEqualsExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(cs, ep, el, Eq(), er, makeBoolType());
}

//
// Collect constraints for the Not Equals expression e1 != e2
//
public Constraints gatherNotEqualsExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(cs, ep, el, NEq(), er, makeBoolType());
}

//
// Collect constraints for the ternary if expression eb ? et : ef
//
//      eb : bool, et : t1, ef : t2, t3 = lub(t1,t2)
// -----------------------------------------------------
//          eb ? et : ef  :  t3
//
public Constraints gatherIfThenElseExpressionConstraints(Constraints cs, Expression ep, Expression eb, Expression et, Expression ef) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,l1> = makeFreshTypes(cs,5); t1 = l1[0]; t2 = l1[1]; t3 = l1[2]; t4 = l1[3]; t5 = l1[4];
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(eb,t2); 
    Constraint c3 = IsType(et,t3); 
    Constraint c4 = IsType(ef,t4);
    
    // Step 2: Assign actual types to the contraints. The guard is bool, and, while each branch is
    // arbitrary, the result is the lub of the two branches.
    Constraint c5 = IsType(t2,makeBoolType());
    Constraint c6 = LubOf([t3,t4],t5);
    Constraint c7 = IsType(t1,t5);

    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5, c6, c7 };
    return cs;
}

//
// Collect constraints for the if defined / otherwise expression e ? eo
//
//      ed : t1, eo : t2, t3 = lub(t1, t2)
// -----------------------------------------
//          ed ? eo  : t3
//
public Constraints gatherIfDefinedOtherwiseExpressionConstraints(Constraints cs, Expression ep, Expression ed, Expression eo) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,l1> = makeFreshTypes(cs,4); t1 = l1[0]; t2 = l1[1]; t3 = l1[2]; t4 = l1[3];
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(ed,t2); 
    Constraint c3 = IsType(eo,t3);
    
    // Step 2: Constrain the results. The result of e ? e' is the lub of e and e', since either
    // could be the actual result of the expression. 
    Constraint c4 = LubOf([t2,t3],t4);
    Constraint c5 = IsType(t1,t4);

    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5 };
    return cs;
}

//
// Collect constraints for the logical implication expression e ==> e'
//
//      el : bool, er : bool
// -----------------------------------------
//          el ==> er : bool
//
public Constraints gatherImplicationExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,l1> = makeFreshTypes(cs,3); t1 = l1[0]; t2 = l1[1]; t3 = l1[2];
    Constraint c1 = IsType(ep,t1); Constraint c2 = IsType(el,t2); Constraint c3 = IsType(er,t3);

    // Step 2: Constrain the results. The operands and the full expression all are of type bool.
    Constraint c4 = IsType(t1, makeBoolType());
    Constraint c5 = IsType(t2, makeBoolType());
    Constraint c6 = IsType(t3, makeBoolType());

    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5, c6 };
    return cs;
}

//
// Collect constraints for the logical equivalence expression e <==> e'
//
//      el : bool, er : bool
// -----------------------------------------
//          el <==> er : bool
//
public Constraints gatherEquivalenceExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,l1> = makeFreshTypes(cs,3); t1 = l1[0]; t2 = l1[1]; t3 = l1[2];
    Constraint c1 = IsType(ep,t1); Constraint c2 = IsType(el,t2); Constraint c3 = IsType(er,t3);

    // Step 2: Constrain the results. The operands and the full expression all are of type bool.
    Constraint c4 = IsType(t1, makeBoolType());
    Constraint c5 = IsType(t2, makeBoolType());
    Constraint c6 = IsType(t3, makeBoolType());

    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5, c6 };
    return cs;
}

//
// Collect constraints for the logical and expression e && e'
//
//      el : bool, er : bool
// -----------------------------------------
//          el && er : bool
//
public Constraints gatherAndExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,l1> = makeFreshTypes(cs,3); t1 = l1[0]; t2 = l1[1]; t3 = l1[2];
    Constraint c1 = IsType(ep,t1); Constraint c2 = IsType(el,t2); Constraint c3 = IsType(er,t3);

    // Step 2: Constrain the results. The operands and the full expression all are of type bool.
    Constraint c4 = IsType(t1, makeBoolType());
    Constraint c5 = IsType(t2, makeBoolType());
    Constraint c6 = IsType(t3, makeBoolType());

    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5, c6 };
    return cs;
}

//
// Collect constraints for the logical or expression e || e'
//
//      el : bool, er : bool
// -----------------------------------------
//          el || er : bool
//
public Constraints gatherOrExpressionConstraints(Constraints cs, Expression ep, Expression el, Expression er) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,l1> = makeFreshTypes(cs,3); t1 = l1[0]; t2 = l1[1]; t3 = l1[2];
    Constraint c1 = IsType(ep,t1); Constraint c2 = IsType(el,t2); Constraint c3 = IsType(er,t3);

    // Step 2: Constrain the results. The operands and the full expression all are of type bool.
    Constraint c4 = IsType(t1, makeBoolType());
    Constraint c5 = IsType(t2, makeBoolType());
    Constraint c6 = IsType(t3, makeBoolType());

    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5, c6 };
    return cs;
}

//
// Collect constraints for the match expression, p := e
//
//     e : t1, bindable(e,p)
// -----------------------------
//       p := e : bool
//
public Constraints gatherMatchExpressionConstraints(Constraints cs, Expression ep, Pattern p, Expression e) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,ts> = makeFreshTypes(cs,4); t1 = ts[0]; t2 = ts[1]; t3 = ts[2]; t4 = ts[3]; 
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(e,t2); 

    // Step 2: Constrain the results. The full result is a bool, while the pattern has a
    // pattern type, containing the pattern, and the subject must be bindable to this
    // pattern.
    Constraint c3 = IsType(t1, makeBoolType());
    Constraint c4 = IsType(c,t3);
    Constraint c5 = IsType(t3,PatternType(t4));
    Constraint c6 = Bindable(t2,t4);

    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5, c6 };
    return cs;
}

//
// Collect constraints for the nomatch expression, p !:= e
//
//     e : t1, bindable(e,p)
// -----------------------------
//       p !:= e : bool
//
public Constraints gatherNoMatchExpressionConstraints(Constraints cs, Expression ep, Pattern p, Expression e) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,ts> = makeFreshTypes(cs,4); t1 = ts[0]; t2 = ts[1]; t3 = ts[2]; t4 = ts[3]; 
    Constraint c1 = IsType(ep,t1); 
    Constraint c2 = IsType(e,t2); 

    // Step 2: Constrain the results. The full result is a bool, while the pattern has a
    // pattern type, containing the pattern, and the subject must be bindable to this
    // pattern.
    Constraint c3 = IsType(t1, makeBoolType());
    Constraint c4 = IsType(c,t3);
    Constraint c5 = IsType(t3,PatternType(t4));
    Constraint c6 = Bindable(t2,t4);

    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5, c6 };
    return cs;
}

//
// Enumerators act like a match, i.e., like :=, except for containers, like lists,
// sets, etc, where they "strip off" the outer layer of the subject. For instance,
// n <- 1 acts just like n := 1, while n <- [1..10] acts like [_*,n,_*] := [1..10].
//
// TODO: Convert over to constraints!
//
public RType gatherEnumeratorExpressionConstraints(Expression ep, Pattern p, Expression e) {
    if (checkForFail({ p@rtype, e@rtype })) { 
        return collapseFailTypes({ p@rtype, e@rtype });
    } 
    
    RType expType = e@rtype;

    // TODO: Nodes
    // TODO: ADTs
    // TODO: Any other special cases?   
    if (isListType(expType)) {
            RType et = getListElementType(expType);
        RType boundType = bindInferredTypesToPattern(et, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(et, boundType)) return makeFailType("The list element type of the subject, <prettyPrintType(et)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else if (isSetType(expType)) {
            RType et = getSetElementType(expType);
        RType boundType = bindInferredTypesToPattern(et, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(et, boundType)) return makeFailType("The set element type of the subject, <prettyPrintType(et)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else if (isBagType(expType)) {
            RType et = getBagElementType(expType);
        RType boundType = bindInferredTypesToPattern(et, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(et, boundType)) return makeFailType("The bag element type of the subject, <prettyPrintType(et)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else if (isContainerType(expType)) {
            RType et = getContainerElementType(expType);
        RType boundType = bindInferredTypesToPattern(et, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(et, boundType)) return makeFailType("The container element type of the subject, <prettyPrintType(et)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else if (isRelType(expType)) {
            RType et = getRelElementType(expType);
        RType boundType = bindInferredTypesToPattern(et, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(et, boundType)) return makeFailType("The relation element type of the subject, <prettyPrintType(et)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else if (isMapType(expType)) {
            RType dt = getMapDomainType(expType);
        RType boundType = bindInferredTypesToPattern(dt, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(dt, boundType)) return makeFailType("The domain type of the map, <prettyPrintType(dt)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else if (isTupleType(expType)) {
            RType tt = lubList(getTupleFields(expType));
        RType boundType = bindInferredTypesToPattern(tt, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(tt, boundType)) return makeFailType("The least upper bound of the tuple element types, <prettyPrintType(tt)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else {
        RType boundType = bindInferredTypesToPattern(expType, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(expType, boundType)) return makeFailType("The type of the subject, <prettyPrintType(expType)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    }
    
    println("Unhandled enumerator case, <p> \<- <e>");
    return makeBoolType();
}

//
// TODO: Convert over to constraints!
//
public RType gatherSetComprehensionExpressionConstraints(Expression ep, {Expression ","}+ els, {Expression ","}+ ers) {
    set[Expression] allExps = { e | e <- els } + { e | e <- ers };
    if (checkForFail({ e@rtype | e <- allExps })) {
        return collapseFailTypes({ e@rtype | e <- allExps });
    } else {
        set[RType] genFailures = { 
            makeFailType("Expression should have type <prettyPrintType(makeBoolType())>, but instead has type <prettyPrintType(e@rtype)>",e@\loc) |
                e <- ers, !isBoolType(e@rtype)
        };
        if (size(genFailures) == 0) {
            list[RType] setTypes = [ ];
            for (e <- els) {
                    RType eType = e@rtype;
                if (isSetType(replaceInferredTypes(eType)) && `{<{Expression ","}* el>}` := e) {
                    setTypes = setTypes + [ replaceInferredTypes(eType) ];
                } else if (isSetType(replaceInferredTypes(eType))) {
                    setTypes = setTypes + [ getSetElementType(replaceInferredTypes(eType)) ];
                } else {
                    setTypes = setTypes + [ replaceInferredTypes(eType) ];
                }
            }
            return makeSetType(lubList(setTypes));
        } else {
            return collapseFailTypes(genFailures);
        }
    }
}

//
// TODO: Convert over to constraints!
//
public RType gatherListComprehensionExpressionConstraints(Expression ep, {Expression ","}+ els, {Expression ","}+ ers) {
    set[Expression] allExps = { e | e <- els } + { e | e <- ers };
    if (checkForFail({ e@rtype | e <- allExps }))
        return collapseFailTypes({ e@rtype | e <- allExps });
    else {
        set[RType] genFailures = { 
            makeFailType("Expression should have type <prettyPrintType(makeBoolType())>, but instead has type <prettyPrintType(e@rtype)>",e@\loc) |
                e <- ers, !isBoolType(e@rtype)
        };
        if (size(genFailures) == 0) {
            list[RType] listTypes = [ ];
            for (e <- els) {
                    RType eType = e@rtype;
                if (isListType(replaceInferredTypes(eType)) && `[<{Expression ","}* el>]` := e) {
                    listTypes = listTypes + [ replaceInferredTypes(eType) ];
                } else if (isListType(replaceInferredTypes(eType))) {
                    listTypes = listTypes + [ getListElementType(replaceInferredTypes(eType)) ];
                } else {
                    listTypes = listTypes + [ replaceInferredTypes(eType) ];
                }
            }
            return makeListType(lubList(listTypes));
        } else {
            return collapseFailTypes(genFailures);
        }
    }
}

//
// TODO: Convert over to constraints!
//
public RType gatherMapComprehensionExpressionConstraints(Expression ep, Expression ef, Expression et, {Expression ","}+ ers) {
    set[Expression] allExps = { ef } + { et } + { e | e <- ers };
    if (checkForFail({ e@rtype | e <- allExps }))
        return collapseFailTypes({ e@rtype | e <- ers });
    else {
        set[RType] genFailures = { 
            makeFailType("Expression should have type <prettyPrintType(makeBoolType())>, but instead has type <prettyPrintType(e@rtype)>",e@\loc) |
                e <- ers, !isBoolType(e@rtype)
        };
        if (size(genFailures) == 0) {
            return makeMapType(replaceInferredTypes(ef@rtype), replaceInferredTypes(et@rtype));
        } else {
            return collapseFailTypes(genFailures);
        }
    }
}

//
// NOTE: We cannot actually type this statically, since the type of the "it" expression is implicit and the type of
// the result is based only indirectly on the type of er. If we could type this, we could probably solve the halting
// problem ;)
//
// TODO: Convert over to constraints!
//
public RType gatherReducerExpressionConstraints(Expression ep, Expression ei, Expression er, {Expression ","}+ ers) {
    list[RType] genTypes = [ e@rtype | e <- ers ];
    if (checkForFail(toSet(genTypes + ei@rtype + er@rtype))) return collapseFailTypes(toSet(genTypes + ei@rtype + er@rtype));

    return makeValueType(); // for now, since it could be anything
}

//
// Collect constraints for the all expression
//
//      e1 : bool, ..., en : bool
// -----------------------------------------
//          all(e1...en) : bool
//
public Constraints gatherAllExpressionConstraints(Constraints cs, Expression ep, {Expression ","}+ ers) {
    <cs, l1> = makeFreshTypes(cs,1); t1 = l1[0];
    Constraint c1 = IsType(ep,t1);
    Constraint c2 = IsType(t1,makeBoolType());
    cs.constraints = cs.constraints + { c1, c2 };

    for (er <- ers) {
        <cs, l2> = makeFreshTypes(cs,1); t2 = l2[0];
        Constraint c3 = IsType(er,t2);
        Constraint c4 = IsType(t2,makeBoolType());
        cs.constraints = cs.constraints + { c3, c4 };
    }

    return cs;
}
        
//
// Collect constraints for the any expression
//
//      e1 : bool, ..., en : bool
// -----------------------------------------
//          any(e1...en) : bool
//
public Constraints gatherAnyExpressionConstraints(Constraints cs, Expression ep, {Expression ","}+ ers) {
    <cs, l1> = makeFreshTypes(cs,1); t1 = l1[0];
    Constraint c1 = IsType(ep,t1);
    Constraint c2 = IsType(t1,makeBoolType());
    cs.constraints = cs.constraints + { c1, c2 };

    for (er <- ers) {
        <cs, l2> = makeFreshTypes(cs,1); t2 = l2[0];
        Constraint c3 = IsType(er,t2);
        Constraint c4 = IsType(t2,makeBoolType());
        cs.constraints = cs.constraints + { c3, c4 };
    }

    return cs;
}

//
// Collect constraints for the map expression
//
//      d1 : td1, r1 : tr1, ..., dn : tdn, rn : trn, td = lub(td1..tdn), tr = lub(tr1..trn)
// ----------------------------------------------------------------------------------------
//                       ( d1 : r1, ..., dn : rn ) : map[td,tr]
//
public Constraints gatherMapExpressionConstraints(Constraints cs, Expression exp) {
    list[tuple[Expression mapDomain, Expression mapRange]] mapContents = getMapExpressionContents(exp);
    <cs,l1> = makeFreshTypes(cs,3); t1 = l1[0]; t2 = l1[1]; t3 = l1[2];
    Constraint c1 = IsType(ep,t1);
    Constraint c2 = IsType(t2,makeVoidType());
    Constraint c3 = IsType(t3,makeVoidType());
    cs.constraints = cs.constraints + { c1, c2, c3 };

    list[RType] domains = [ t2 ]; list[RType] ranges = [ t3 ];
    for (<md,mr> <- mapContents) { 
        <cs,l2> = makeFreshTypes(cs,2); t4 = l2[0]; t5 = l2[1];
        Constraint c4 = IsType(md,t4);
        Constraint c5 = IsType(mr,t5);
        domains += t4; ranges += t5;
        cs.constraints = cs.constraints + { c4, c5 };
    }

    <cs,l3> = makeFreshTypes(cs,2); t6 = l3[0]; t7 = l3[1];
    Constraint c6 = LubOf(domains,t6);
    Constraint c7 = LubOf(domains,t7);
    Constraint c8 = IsType(t1, makeMapType(t6,t7));
    cs.constraints = cs.constraints + { c6, c7, c8 };

    return cs;
}

public Constraints gatherExpressionConstraints(Constraints cs, Expression exp) {
    switch(exp) {
        case (Expression)`<BooleanLiteral bl>` : {
            <cs,l1> = makeFreshTypes(cs,1); t1 = l1[0];
            Constraint c1 = IsType(exp,t1);
            Constraint c2 = IsType(t1,makeBoolType());
            cs.constraints = cs.constraints + { c1, c2 };
            return cs;
        }

        case (Expression)`<DecimalIntegerLiteral il>`  : {
            <cs,l1> = makeFreshTypes(cs,1); t1 = l1[0];
            Constraint c1 = IsType(exp,t1);
            Constraint c2 = IsType(t1,makeIntType());
            cs.constraints = cs.constraints + { c1, c2 };
            return cs;
        }

        case (Expression)`<OctalIntegerLiteral il>`  : {
            <cs,l1> = makeFreshTypes(cs,1); t1 = l1[0];
            Constraint c1 = IsType(exp,t1);
            Constraint c2 = IsType(t1,makeIntType());
            cs.constraints = cs.constraints + { c1, c2 };
            return cs;
        }

        case (Expression)`<HexIntegerLiteral il>`  : {
            <cs,l1> = makeFreshTypes(cs,1); t1 = l1[0];
            Constraint c1 = IsType(exp,t1);
            Constraint c2 = IsType(t1,makeIntType());
            cs.constraints = cs.constraints + { c1, c2 };
            return cs;
        }

        case (Expression)`<RealLiteral rl>`  : {
            <cs,l1> = makeFreshTypes(cs,1); t1 = l1[0];
            Constraint c1 = IsType(exp,t1);
            Constraint c2 = IsType(t1,makeRealType());
            cs.constraints = cs.constraints + { c1, c2 };
            return cs;
        }

        case (Expression)`<StringLiteral sl>`  : {
            <cs,l1> = makeFreshTypes(cs,1); t1 = l1[0];
            Constraint c1 = IsType(exp,t1);
            Constraint c2 = IsType(t1,makeRealType());
            cs.constraints = cs.constraints + { c1, c2 };

                list[Tree] ipl = prodFilter(sl, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ip <- ipl) {
                <cs,l2> = makeFreshTypes(cs,1); t2 = l2[0];
                Constraint c3 = IsType(ip,t2);
                Constraint c4 = IsType(t2,makeStrType());
                cs.constraints = cs.constraints + { c3, c4 };
            }

            return cs;
        }

        case (Expression)`<LocationLiteral ll>`  : {
            <cs,l1> = makeFreshTypes(cs,1); t1 = l1[0];
            Constraint c1 = IsType(exp,t1);
            Constraint c2 = IsType(t1,makeLocType());
            cs.constraints = cs.constraints + { c1, c2 };

            list[Expression] ipl = prodFilter(ll, bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd; });
            for (ip <- ipl) {
                <cs,l2> = makeFreshTypes(cs,1); t2 = l2[0];
                Constraint c3 = IsType(ip,t2);
                Constraint c4 = IsType(t2,makeStrType());
                cs.constraints = cs.constraints + { c3, c4 };
            }

            return cs;
        }

        case (Expression)`<DateTimeLiteral dtl>`  : {
            <cs,l1> = makeFreshTypes(cs,1); t1 = l1[0];
            Constraint c1 = IsType(exp,t1);
            Constraint c2 = IsType(t1,makeDateTimeType());
            cs.constraints = cs.constraints + { c1, c2 };
            return cs;
        }

        // _ as a name, should only be in patterns, but include just in case...
        case (Expression)`_`: {
            Constraint c1 = Failure(exp,makeFailType("The anonymous name _ can only be used inside a pattern",exp@\loc));
            cs.constraints = cs.constraints + { c1 };
            return cs;
        }

        // Name
        case (Expression)`<Name n>`: {
            <cs, l1> = makeFreshTypes(cs,1); t1 = l1[0];
            Constraint c1 = IsType(exp,t1);
            Constraint c2 = DefinedBy(t1,n@nameIds);
            cs.constraints = cs.constraints + { c1, c2 };
            return cs;
        }
        
        // QualifiedName
        case (Expression)`<QualifiedName qn>`: {
            <cs, l1> = makeFreshTypes(cs,1); t1 = l1[0];
            Constraint c1 = IsType(exp,t1);
            Constraint c2 = DefinedBy(t1,qn@nameIds);
            cs.constraints = cs.constraints + { c1, c2 };
            return cs;
        }

        // ReifiedType
        case `<BasicType t> ( <{Expression ","}* el> )` :
            return gatherReifiedTypeExpressionConstraints(cs,exp,t,el);

        // CallOrTree
        case `<Expression e1> ( <{Expression ","}* el> )` :
            return gatherCallOrTreeExpressionConstraints(cs,exp,e1,el);

        // List
        case `[<{Expression ","}* el>]` :
            return gatherListExpressionConstraints(cs,exp,el);

        // Set
        case `{<{Expression ","}* el>}` :
            return gatherSetExpressionConstraints(cs,exp,el);

        // Tuple, with just one element
        case (Expression)`<<Expression ei>>` :
            return gatherTrivialTupleExpressionConstraints(cs,exp,ei);

        // Tuple, with multiple elements
        case `<<Expression ei>, <{Expression ","}* el>>` :
            return gatherTupleExpressionConstraints(cs,exp,ei,el);

        // Closure
        case `<Type t> <Parameters p> { <Statement+ ss> }` :
            return gatherClosureExpressionConstraints(cs,exp,t,p,ss);

        // VoidClosure
        case `<Parameters p> { <Statement* ss> }` :
            return gatherVoidClosureExpressionConstraints(cs,exp,p,ss);

        // NonEmptyBlock
        case `{ <Statement+ ss> }` :
            return gatherNonEmptyBlockExpressionConstraints(cs,exp,ss);
        
        // Visit
        case (Expression) `<Label l> <Visit v>` :
            return gatherVisitExpressionConstraints(cs,exp,l,v);
        
        // ParenExp
        case `(<Expression e>)` : {
            <cs, l1> = makeFreshTypes(cs, 2); t1 = l1[0]; t2 = l1[1];
            Constraint c1 = IsType(exp,t1);
            Constraint c2 = IsType(e,t2);
            Constraint c3 = IsType(t1,t2);
            cs.constraints = cs.constraints + { c1, c2, c3 };
            return cs;
        }

        // Range
        case `[ <Expression e1> .. <Expression e2> ]` :
            return gatherRangeExpressionConstraints(cs,exp,e1,e2);

        // StepRange
        case `[ <Expression e1>, <Expression e2> .. <Expression e3> ]` :
            return gatherStepRangeExpressionConstraints(cs,exp,e1,e2,e3);

        // ReifyType
        case (Expression)`#<Type t>` : {
            <cs, l1> = makeFreshTypes(cs, 1); t1 = l1[0];
            Constraint c1 = IsType(exp,t1);
            Constraint c2 = IsType(t1, RTypeStructured(RStructuredType(RTypeType(),[RTypeArg(convertType(t))])));
            cs.constraints = cs.constraints + { c1, c2 };
            return cs;
        }

        // FieldUpdate
        case `<Expression e1> [<Name n> = <Expression e2>]` :
            return gatherFieldUpdateExpressionConstraints(cs,exp,e1,n,e2);

        // FieldAccess
        case `<Expression e1> . <Name n>` :
            return gatherFieldAccessExpressionConstraints(cs,exp,e1,n);

        // FieldProject
        case `<Expression e1> < <{Field ","}+ fl> >` :
            return gatherFieldProjectExpressionConstraints(cs,exp,e1,fl);

        // Subscript 
        case `<Expression e1> [ <{Expression ","}+ el> ]` :
            return gatherSubscriptExpressionConstraints(cs,exp,e1,el);

        // IsDefined
        case `<Expression e> ?` :
            return gatherIsDefinedExpressionConstraints(cs,exp,e);

        // Negation
        case `! <Expression e>` :
            return gatherNegationExpressionConstraints(cs,exp,e);

        // Negative
        case `- <Expression e> ` :
            return gatherNegativeExpressionConstraints(cs,exp,e);

        // TransitiveReflexiveClosure
        case `<Expression e> * ` :
            return gatherTransitiveReflexiveClosureExpressionConstraints(cs,exp,e);

        // TransitiveClosure
        case `<Expression e> + ` :
            return gatherTransitiveClosureExpressionConstraints(cs,exp,e);

        // GetAnnotation
        case `<Expression e> @ <Name n>` :
            return gatherGetAnnotationExpressionConstraints(cs,exp,e,n);

        // SetAnnotation
        case `<Expression e1> [@ <Name n> = <Expression e2>]` :
            return gatherSetAnnotationExpressionConstraints(cs,exp,e1,n,e2);

        // Composition
        case `<Expression e1> o <Expression e2>` :
            return gatherCompositionExpressionConstraints(cs,exp,e1,e2);

        // Product
        case `<Expression e1> * <Expression e2>` :
            return gatherProductExpressionConstraints(cs,exp,e1,e2);

        // Join
        case `<Expression e1> join <Expression e2>` :
            return gatherJoinExpressionConstraints(cs,exp,e1,e2);

        // Div
        case `<Expression e1> / <Expression e2>` :
            return gatherDivExpressionConstraints(cs,exp,e1,e2);

        // Mod
        case `<Expression e1> % <Expression e2>` :
            return gatherModExpressionConstraints(cs,exp,e1,e2);

        // Intersection
        case `<Expression e1> & <Expression e2>` :
            return gatherIntersectionExpressionConstraints(cs,exp,e1,e2);
        
        // Plus
        case `<Expression e1> + <Expression e2>` :
            return gatherPlusExpressionConstraints(cs,exp,e1,e2);

        // Minus
        case `<Expression e1> - <Expression e2>` :
            return gatherMinusExpressionConstraints(cs,exp,e1,e2);

        // NotIn
        case `<Expression e1> notin <Expression e2>` :
            return gatherNotInExpressionConstraints(cs,exp,e1,e2);

        // In
        case `<Expression e1> in <Expression e2>` :
            return gatherInExpressionConstraints(cs,exp,e1,e2);

        // LessThan
        case `<Expression e1> < <Expression e2>` :
            return gatherLessThanExpressionConstraints(cs,exp,e1,e2);

        // LessThanOrEq
        case `<Expression e1> <= <Expression e2>` :
            return gatherLessThanOrEqualExpressionConstraints(cs,exp,e1,e2);

        // GreaterThan
        case `<Expression e1> > <Expression e2>` :
            return gatherGreaterThanExpressionConstraints(cs,exp,e1,e2);

        // GreaterThanOrEq
        case `<Expression e1> >= <Expression e2>` :
            return gatherGreaterThanOrEqualExpressionConstraints(cs,exp,e1,e2);

        // Equals
        case `<Expression e1> == <Expression e2>` :
            return gatherEqualsExpressionConstraints(cs,exp,e1,e2);

        // NotEquals
        case `<Expression e1> != <Expression e2>` :
            return gatherNotEqualsExpressionConstraints(cs,exp,e1,e2);

        // IfThenElse (Ternary)
        case `<Expression e1> ? <Expression e2> : <Expression e3>` :
            return gatherIfThenElseExpressionConstraints(cs,exp,e1,e2,e3);

        // IfDefinedOtherwise
        case `<Expression e1> ? <Expression e2>` :
            return gatherIfDefinedOtherwiseExpressionConstraints(cs,exp,e1,e2);

        // Implication
        case `<Expression e1> ==> <Expression e2>` :
            return gatherImplicationExpressionConstraints(cs,exp,e1,e2);

        // Equivalence
        case `<Expression e1> <==> <Expression e2>` :
            return gatherEquivalenceExpressionConstraints(cs,exp,e1,e2);

        // And
        case `<Expression e1> && <Expression e2>` :
            return gatherAndExpressionConstraints(cs,exp,e1,e2);

        // Or
        case `<Expression e1> || <Expression e2>` :
            return gatherOrExpressionConstraints(cs,exp,e1,e2);
        
        // Match
        case `<Pattern p> := <Expression e>` :
            return gatherMatchExpressionConstraints(cs,exp,p,e);

        // NoMatch
        case `<Pattern p> !:= <Expression e>` :
            return gatherNoMatchExpressionConstraints(cs,exp,p,e);

        // Enumerator
        case `<Pattern p> <- <Expression e>` :
            return gatherEnumeratorExpressionConstraints(cs,exp,p,e);
        
        // Set Comprehension
        case (Expression) `{ <{Expression ","}+ el> | <{Expression ","}+ er> }` :
            return gatherSetComprehensionExpressionConstraints(cs,exp,el,er);

        // List Comprehension
        case (Expression) `[ <{Expression ","}+ el> | <{Expression ","}+ er> ]` :
            return gatherListComprehensionExpressionConstraints(cs,exp,el,er);
        
        // Map Comprehension
        case (Expression) `( <Expression ef> : <Expression et> | <{Expression ","}+ er> )` :
            return gatherMapComprehensionExpressionConstraints(cs,exp,ef,et,er);
        
        // Reducer 
        case `( <Expression ei> | <Expression er> | <{Expression ","}+ egs> )` :
            return gatherReducerExpressionConstraints(cs,exp,ei,er,egs);
        
        // It
        case `it` : {
            <cs, l1> = makeFreshTypes(cs,1); t1 = l1[0];
            Constraint c1 = IsType(exp,t1);
            Constraint c2 = DefinedBy(t1,exp@nameIds);
            cs.constraints = cs.constraints + { c1, c2 };
            return cs;
        }
            
        // All 
        case `all ( <{Expression ","}+ egs> )` :
            return gatherAllExpressionConstraints(cs,exp,egs);

        // Any 
        case `any ( <{Expression ","}+ egs> )` :
            return gatherAnyExpressionConstraints(cs,exp,egs);
    }

    // Logic for handling maps -- we cannot directly match them, so instead we need to pick apart the tree
    // representing the map.
    // exp[0] is the production used, exp[1] is the actual parse tree contents
    if (prod(_,_,attrs([_*,term(cons("Map")),_*])) := exp[0])
        return gatherMapExpressionConstraints(cs,exp);
}
