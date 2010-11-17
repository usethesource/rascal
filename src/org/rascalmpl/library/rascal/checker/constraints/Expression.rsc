@bootstrapParser
module rascal::checker::constraints::Expression

import List;
import ParseTree;
import IO;
import rascal::checker::Types;
import rascal::checker::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::checker::Annotations;
import rascal::checker::TreeUtils;
import rascal::syntax::RascalRascal;

//
// TODO: The expressions should all be of type type
//
// TODO: Switch to constraints
//
private ConstraintBase gatherReifiedTypeExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Type t, {Expression ","}* el) {
    if (checkForFail({ e@rtype | e <- el }))
        return collapseFailTypes({ e@rtype | e <- el });
    else
        return makeReifiedType(convertType(t), [ e@rtype | e <- el ]);
}

//
// Gather constraints for the call or tree expression, which can be either a function or 
// constructor, a node constructor, or a location
//
// (FUNCTION OR CONSTRUCTOR)
//
//      f : tf1 x ... x tfn -> tr, e1 : t1, ... , en : tn, t1 <: tf1, ..., tn <: tfn 
// ----------------------------------------------------------------------------------------------
//               f (e1, ..., en) : tr
//
// (NODE)
//
//      f : str, e1 : t1, ... , en : tn, isValidNodeName(f)
// ----------------------------------------------------------------------------------------------
//               f (e1, ..., en) : node
//
// (LOCATION)
//
//      f : loc, e1 : int, e2 : int, e3 : tuple[int,int], e4 : tuple[int,int]
// ----------------------------------------------------------------------------------------------
//               f (e1, e2, e3, e4) : loc
//
public ConstraintBase gatherCallOrTreeExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression ec, {Expression ","}* es) {
    // First, assign types for the params; each of of arbitrary type
    list[RType] params = [ ];
    for (e <- es) {
        <cs, t1> = makeFreshType(cs);
        cs.constraints = cs.constraints + TreeIsType(e,e@\loc,t1);
        params += t1;
    }

    // ec, the target, is of arbitrary type t2; the resulting type, t3, is based on the invocation of t2
    // with param types params
    <cs,ts> = makeFreshTypes(cs,2); t2 = ts[0]; t3 = ts[1];
    Constraint c1 = TreeIsType(ec,ec@\loc,t2);
    Constraint c2 = CallOrTree(t2,params,t3);
    Constraint c3 = TreeIsType(ep,ep@\loc,t3);
    cs.constraints = cs.constraints + { c1, c2, c3 };

    return cs;  
}

//
// Gather constraints for the list expression: [ e1, ..., en ]
//
//      e1 : t1, ... , en : tn, tl = lub(t1, ..., tn)
// ------------------------------------------------------
//               [ e1, ..., en ] : list[tl]
//
// NOTE: This rule is simplified a bit, below we also handle splicing
//
public ConstraintBase gatherListExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, {Expression ","}* es) {
    // The elements of the list are each of some arbitrary type
    list[RType] elements = [ makeVoidType() ]; 
    for (e <- es) { 
        <cs,t1> = makeFreshType(cs);

        // If this is an element that can be spliced, i.e. is not surrounded by list
        // brackets, indicate that in the type, so we can calculate the lub correctly
        if ((Expression)`[<{Expression ","}* el>]` !:= e) {
            t1 = SpliceableElement(t1);
        }

        cs.constraints = cs.constraints + TreeIsType(e,e@\loc,t1); 
        elements += t1;
    }

    // The list itself is the lub of the various elements
    <cs,t2> = makeFreshType(cs); 
    Constraint c1 = LubOfList(elements,t2);
    Constraint c2 = TreeIsType(ep, ep@\loc, makeListType(t2));
    cs.constraints = cs.constraints + { c1, c2 };

    return cs;
}

//
// Gather constraints for the set expression: { e1, ..., en }
//
//      e1 : t1, ... , en : tn, tl = lub(t1, ..., tn)
// ------------------------------------------------------
//               { e1, ..., en } : set[tl]
//
// NOTE: This rule is simplified a bit, below we also need to handle splicing
//
public ConstraintBase gatherSetExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, {Expression ","}* es) {
    // The elements of the set are each of some arbitrary type
    list[RType] elements = [ makeVoidType() ]; 
    for (e <- es) { 
        <cs,t1> = makeFreshType(cs);

        // If this is an element that can be spliced, i.e. is not surrounded by set
        // brackets, indicate that in the type, so we can calculate the lub correctly
        if ((Expression)`{<{Expression ","}* el>}` !:= e) {
            t1 = SpliceableElement(t1);
        }

        cs.constraints = cs.constraints + TreeIsType(e,e@\loc,t1); 
        elements += t1;
    }

    // The set itself is the lub of the various elements
    <cs,t2> = makeFreshType(cs); 
    Constraint c1 = LubOfSet(elements,t2);
    Constraint c2 = TreeIsType(ep, ep@\loc, makeSetType(t2));
    cs.constraints = cs.constraints + { c1, c2 };

    return cs;
}

//
// Gather constraints for the trivial tuple expression: < e1 >
//
//      e1 : t1
// ----------------------
//   < e1 > : tuple[t1]
//
public ConstraintBase gatherTrivialTupleExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression ei) {
    <cs,t1> = makeFreshType(cs);
    Constraint c1 = TreeIsType(ei,ei@\loc,t1); // the element is of an arbitrary type
    Constraint c2 = TreeIstype(ep,makeTupleType([t1])); // the tuple has on element of this arbitrary type 
    cs.constraints = cs.constraints + { c1, c2 };
    return cs;
}

//
// Gather constraints for the tuple expression: < e1, ..., en >
//
//      e1 : t1, ..., en : tn
// ------------------------------------------
//   < e1, ..., en > : tuple[t1, ..., tn]
//
public ConstraintBase gatherTupleExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression ei, {Expression ","}* el) {
    // The elements of the tuple are each of some arbitrary type
    list[RType] elements = [ ]; 
    for (e <- el) { 
        <cs,t1> = makeFreshType(cs);
        cs.constraints = cs.constraints + TreeIsType(e,e@\loc,t1); 
        elements += t1;
    }

    // The tuple is then formed form these types
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,makeTupleType(elements));

    return cs;
}

//
// Typecheck a closure. The type of the closure is a function type, based on the parameter types
// and the return type. This mainly then propagages any failures in the parameters or the
// closure body.
//
// TODO: Convert to constraints!
//
public RType checkClosureExpression(Expression ep, Type t, Parameters p, Statement+ ss) {
    list[RType] pTypes = getParameterTypes(p);
    bool isVarArgs = size(pTypes) > 0 ? isVarArgsType(pTypes[size(pTypes)-1]) : false;
    set[RType] stmtTypes = { getInternalStatementType(s@rtype) | s <- ss };
    
    if (checkForFail(toSet(pTypes) + stmtTypes)) return collapseFailTypes(toSet(pTypes) + stmtTypes);

    return makeFunctionType(convertType(t), pTypes);
}

//
// Typecheck a void closure. The type of the closure is a function type, based on the parameter types
// and the void return type. This mainly then propagages any failures in the parameters or the
// closure body.
//
// TODO: Convert to constraints!
//
public RType checkVoidClosureExpression(Expression ep, Parameters p, Statement+ ss) {
    list[RType] pTypes = getParameterTypes(p);
    bool isVarArgs = size(pTypes) > 0 ? isVarArgsType(pTypes[size(pTypes)-1]) : false;
    set[RType] stmtTypes = { getInternalStatementType(s@rtype) | s <- ss };
    
    if (checkForFail(toSet(pTypes) + stmtTypes)) return collapseFailTypes(toSet(pTypes) + stmtTypes);

    return makeFunctionType(makeVoidType(), pTypes);
}
 
//
// Collect constraints on the non-empty block expression: { s1 ... sn }
//
// TODO: Add explanation
//
public ConstraintBase gatherNonEmptyBlockExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Statement+ ss) {
    list[Statement] sl = [ s | s <- ss ];
    Statement finalStatement = head(tail(sl,1));
    
    // The type of the expression is the same as the internal type of the last statement in the block
    <cs,t1> = makeFreshType(cs);
    cs.constraints = cs.constraints + TreeIsType(finalStatement,finalStatement@\loc,makeStatementType(t1))
                                    + TreeIsType(ep,ep@\loc,t1);
    return cs;
}

//
// Collect constraints on the visit expression: visit(e) { case c1 ... case cn }
// 
// TODO: Add explanation
//
public ConstraintBase gatherVisitExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Label l, Visit v) {
    // The type of the expression is the same as the type of the visit, which is the type of the
    // item being visited; i.e., visit("hello") { ... } is of type str 
    <cs,t1> = makeFreshType(cs);
    cs.constraints = cs.constraints + VisitIsType(v,v@\loc,t1) + TreeIsType(ep,ep@\loc,t1); 
    return cs;
}

//
// Collect constraints for the range expression: [ e1 .. e2 ]
//
//     e1 : int, e2 : int
// -------------------------------
// [ e1 .. e2 ] : list[int]
//
public ConstraintBase gatherRangeExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression e1, Expression e2) {
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,makeListType(makeIntType())) + 
                     TreeIsType(e1,e1@\loc,makeIntType()) + TreeIsType(e2,e2@\loc,makeIntType()); 
    return cs;
}

//
// Collect constraints for the step range expression: [ e1, e2 .. e3 ]
//
//     e1 : int, e2 : int, e3 : int
// ----------------------------------------
//     [ e1, e2 .. e3 ] : list[int]
//
public ConstraintBase gatherStepRangeExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression e1, Expression e2, Expression e3) {
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,makeListType(makeIntType())) + 
                     TreeIsType(e1,e1@\loc,makeIntType()) + TreeIsType(e2,e2@\loc,makeIntType()) +
                     TreeIsType(e3,e3@\loc,makeIntType());
    return cs;
}

//
// Collect constraints for the field update expression: el.n = er
//
// el : t1, n fieldOf t1, t1.n : t2, er : t3, assignable(t3,t2)
// -------------------------------------------------------------
//                el.n = er : t1
//
public ConstraintBase gatherFieldUpdateExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Name n, Expression er) {
    <cs,ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
    Constraint c1 = TreeIsType(el,el@\loc,t1); // el has an arbitrary type, t1
    Constraint c2 = TreeIsType(ep,ep@\loc,t1); // the overall expression has the same type as el, i.e., x.f = 3 is of the same type as x
    Constraint c3 = FieldOf(n,t1,t2); // name n is actually a field of type t1, i.e., in x.f = 3, f is a field of x, and has type t2
    Constraint c4 = TreeIsType(er,er@\loc,t3); // the expression being assigned has an arbitrary type, t3
    Constraint c5 = Assignable(t3,t2); // the type of expression being assigned is assignment compatible with the type of the field

    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5 };
    return cs;
}

//
// Collect constraints for the field access expression: el.n
//
//  el : t1, n fieldOf t1, t1.n : t2
// ----------------------------------------
//        el.n : t2
//
public ConstraintBase gatherFieldAccessExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Name n) {
    <cs,ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    
    Constraint c1 = TreeIsType(el,el@\loc,t1); // el has an arbitrary type, t1
    Constraint c2 = FieldOf(n,t1,t2); // name n is actually a field of type t1, i.e., in x.f, f is a field of x, and has type t2
    Constraint c3 = TreeIsType(ep,ep@\loc,t2); // the overall expression has the same type as the type of field n, i.e., x.f is the same type as field f

    cs.constraints = cs.constraints + { c1, c2, c3 };
    return cs;
}

//
// Collect constraints for the field projection expression: e<f1.,,,.fn>
//
public ConstraintBase gatherFieldProjectExpressionConstraints(Expression ep, Expression e, {Field ","}+ fl) {
    <cs,t1> = makeFreshType(cs);
    cs.constraints = cs.constraints + TreeIsType(e,e@\loc,t1); // e is of an arbitrary type t1
    
    // the fields are all fields of type t1, and are of arbitrary type t2; we use NamedFieldOf,
    // instead of FieldOf(as used above), since the type should include the names, which are
    // propagated as part of the type in certain cases
    list[RType] fieldTypes = [ ];
    for (f <- fl) {
        <cs,t2> = makeFreshType(cs);
        cs.constraints = cs.constraints + NamedFieldOf(f,t1,t2);
        fieldTypes += t2;
    }
    
    // The overall expression type is based on the type of e, t1, and the types of each of the
    // fields. If we are projecting out of a tuple, we either get a new tuple (if we project
    // more than 1 field) or the type of the tuple field (if we just project one field). If we
    // are projecting out of a map or relation, the result is either a set (if we project just
    // one field) or a relation (if we project multiple fields). 
    if (size(fieldTypes) > 1) {
        Constraint c1 = IfTuple(t1,TreeIsType(ep,ep@\loc,TupleProjection(fieldTypes)));
        Constraint c2 = IfMap(t1,TreeIsType(ep,ep@\loc,RelationProjection(fieldTypes)));
        Constraint c3 = IfRelation(t1,TreeIsType(ep,ep@\loc,RelationProjection(fieldTypes)));
        cs.constraints = cs.constraints + { c1, c2, c3 };
    } else {
        Constraint c1 = IfTuple(t1,TreeIsType(ep,ep@\loc,SingleProjection(fieldTypes[0])));
        Constraint c2 = IfMap(t1,TreeIsType(ep,ep@\loc,SetProjection(fieldTypes[0])));    
        Constraint c3 = IfRelation(t1,TreeIsType(ep,ep@\loc,SetProjection(fieldTypes[0])));    
        cs.constraints = cs.constraints + { c1, c2, c3 };
    }

    return cs;
}

//
// Collect constraints for the subscript expression: el[e1...en]
//
// TODO: Finish changes for constraint gathering
//
public ConstraintBase gatherSubscriptExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, {Expression ","}+ es) {
    <cs,t1> = makeFreshType(cs);
    Constraint c1 = TreeIsType(el,el@\loc,t1); // el is of arbitrary type t1 
    cs.constraints = cs.constraints + c1;
    
    // Step 2: Constrain the actual subscripts
    list[Expression] indices = [ e | e <- es ];
    for (e <- indices) {
        <cs,ts> = makeFreshTypes(cs,1); t3 = ts[0];
        Constraint c3 = TreeIsType(e,e@\loc,t3);
        Constraint c4 = TypesAreEqual(t3,makeIntType());
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
public ConstraintBase gatherIsDefinedExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression e) {
    // e and e? both have the same arbitrary type
    <cs,t1> = makeFreshType(cs);
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,t1) + TreeIsType(e,e@\loc,t1); 
    return cs;
}

//
// Collect constraints for the negation expression
//
//      e : bool
// --------------------
//   not e : bool
//
private ConstraintBase gatherNegationExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression e) {
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,makeBoolType()) + TreeIsType(e,e@\loc,makeBoolType());
    return cs;
}

//
// Collect constraints for the negative expression
//
//      e : t1, -_ : t1 -> t2 defined 
// ---------------------------------------------
//          - e : t2
//
private ConstraintBase gatherNegativeExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression e) {
    <cs,ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    Constraint c1 = TreeIsType(e,e@\loc,t1); // e has arbitrary type t1
    Constraint c2 = BuiltInAppliable(Negative(), makeTupleType([ t1 ]), t2, ep@\loc); // applying builtin operator - to t1 yields t2
    Constraint c3 = TreeIsType(ep,ep@\loc,t2); // so the overall result of the expression is t2
    cs.constraints = cs.constraints + { c1, c2, c3 };

    return cs;
}

//
// Collect constraints for the transitive reflexive closure expression, e*
//
//      e : rel[t1,t2], comparable(t1,t2)
// -----------------------------------------
//      e * : rel[t1,t2]
//
public ConstraintBase gatherTransitiveReflexiveClosureExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression e) {
    <cs,ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    Constraint c1 = TreeIsType(e,e@\loc,makeRelType(t1,t2)); // e must be a relation of arity 2 of arbitrary types t1 and t2
    Constraint c2 = Comparable(t1,t2); // to create the closure, both projections must be comparable
    Constraint c3 = TreeIsType(ep,ep@\loc,makeRelType(t1,t2)); // e* must be the same type as e
    cs.constraints = cs.constraints + { c1, c2, c3 };    
    return cs;
}

//
// Collect constraints for the transitive closure expression, e+
//
//      e : rel[t1,t2], comparable(t1,t2)
// -----------------------------------------
//      e + : rel[t1,t2]
//
public ConstraintBase gatherTransitiveClosureExpressionConstraints(Expression ep, Expression e) {
    <cs,ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    Constraint c1 = TreeIsType(e,e@\loc,makeRelType(t1,t2)); // e must be a relation of arity 2 of arbitary types t1 and t2
    Constraint c2 = Comparable(t1,t2); // to create the closure, both projections must be comparable
    Constraint c3 = TreeIsType(ep,ep@\loc,makeRelType(t1,t2)); // e+ must be the same type as e
    cs.constraints = cs.constraints + { c1, c2, c3 };    
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
// NOTE: The subtyping check is in the BuiltInAppliable logic
//
public ConstraintBase gatherBinaryExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, RBuiltInOp rop, Expression er) {
    <cs,l1> = makeFreshTypes(cs,4); t1 = l1[0]; t2 = l1[1]; t3 = l1[2]; t4 = l1[3];
    Constraint c1 = TreeIsType(el,el@\loc,t1); // The left operand is of arbitrary type t1 
    Constraint c2 = TreeIsType(er,er@\loc,t2); // The right operand is of arbitrary type t2
    Constraint c3 = BuiltInAppliable(rop, makeTupleType([ t1, t2 ]), t3, ep@\loc); // The result of applying rop to t1 and t2 is t3
    Constraint c4 = TreeIsType(ep,ep@\loc,t3); // The overall result is t3
    cs.constraints = cs.constraints + { c1, c2, c3, c4 };
    return cs;
}

//
// Collect constraints for binary expressions
//
//      e1 : t1, e2 : t2, rop : t1 x t2 -> t3 defined, t4 given, t3 = t4
// --------------------------------------------------------------------------------------------
//                            e1 rop e2 : t4
//
// NOTE: The subtyping check is in the BuiltInAppliable logic
//
public ConstraintBase gatherBinaryExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, RBuiltInOp rop, Expression er, RType resultType) {
    <cs,l1> = makeFreshTypes(cs,4); t1 = l1[0]; t2 = l1[1]; t3 = l1[2]; t4 = l1[3];
    Constraint c1 = TreeIsType(el,el@\loc,t1); // The left operand is of arbitrary type t1 
    Constraint c2 = TreeIsType(er,er@\loc,t2); // The right operand is of arbitrary type t2
    Constraint c3 = BuiltInAppliable(rop, makeTupleType([ t1, t2 ]), t3, ep@\loc); // The result of applying rop to t1 and t2 is t3
    Constraint c4 = TreeIsType(ep,ep@\loc,t3); // The overall result is t3
    Constraint c5 = TypesAreEqual(t3, resultType); // resultType is "t4 given" in the rule above
    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5 };
    return cs;
}

//
// Collect constraints for the product expression e1 * e2
//
public ConstraintBase gatherProductExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Product(), er);
}

//
// Collect constraints for the join expression e1 join e2
//
public ConstraintBase gatherJoinExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Join(), er);
}

//
// Collect constraints for the div expression e1 / e2
//
public ConstraintBase gatherDivExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Div(), er);
}

//
// Collect constraints for the mod expression e1 % e2
//
public ConstraintBase gatherModExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Mod(), er);
}

//
// Collect constraints for the intersection expression e1 & e2
//
public ConstraintBase gatherIntersectionExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Intersect(), er);
}

//
// Collect constraints for the plus expression e1 + e2
//
public ConstraintBase gatherPlusExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Plus(), er);
}

//
// Collect constraints for the minus expression e1 - e2
//
public ConstraintBase gatherMinusExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Minus(), er);
}

//
// Collect constraints for the notin expression e1 notin e2
//
public ConstraintBase gatherNotInExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, NotIn(), er, makeBoolType());
}

//
// Collect constraints for the in expression e1 in e2
//
public ConstraintBase gatherInExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, In(), er, makeBoolType());
}

//
// Collect constraints for the Less Than expression e1 < e2
//
public ConstraintBase gatherLessThanExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cs, ep, el, Lt(), er, makeBoolType());
}

//
// Collect constraints for the Less Than or Equal expression e1 <= e2
//
public ConstraintBase gatherLessThanOrEqualExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cs, ep, el, LtEq(), er, makeBoolType());
}

//
// Collect constraints for the Greater Than expression e1 > e2
//
public ConstraintBase gatherGreaterThanExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cs, ep, el, Gt(), er, makeBoolType());
}

//
// Collect constraints for the Greater Than or Equal expression e1 >= e2
//
public ConstraintBase gatherGreaterThanOrEqualExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cs, ep, el, GtEq(), er, makeBoolType());
}

//
// Collect constraints for the Equals expression e1 == e2
//
public ConstraintBase gatherEqualsExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cs, ep, el, Eq(), er, makeBoolType());
}

//
// Collect constraints for the Not Equals expression e1 != e2
//
public ConstraintBase gatherNotEqualsExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cs, ep, el, NEq(), er, makeBoolType());
}

//
// Collect constraints for the ternary if expression eb ? et : ef
//
//      eb : bool, et : t1, ef : t2, t3 = lub(t1,t2)
// -----------------------------------------------------
//          eb ? et : ef  :  t3
//
public ConstraintBase gatherIfThenElseExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression eb, Expression et, Expression ef) {
    <cs,ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
    Constraint c1 = TreeIsType(eb,eb@\loc,makeBoolType()); // The guard is type bool 
    Constraint c2 = TreeIsType(et,et@\loc,t1); // The true branch is of arbitrary type t1
    Constraint c3 = TreeIsType(ef,ef@\loc,t2); // The false branch is of arbitrary type t2
    Constraint c4 = LubOf([t1,t2],t3); // t3 is the lub of t1 and t2
    Constraint c5 = TreeIsType(ep,ep@\loc,t3); // The result is t3, the lub of t1 and t2    
    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5 };
    return cs;
}

//
// Collect constraints for the if defined / otherwise expression e ? eo
//
//      ed : t1, eo : t2, t3 = lub(t1, t2)
// -----------------------------------------
//          ed ? eo  : t3
//
public ConstraintBase gatherIfDefinedOtherwiseExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression ed, Expression eo) {
    <cs,ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
    Constraint c1 = TreeIsType(ed,ed@\loc,t1); // ed is of arbitrary type t1 
    Constraint c2 = TreeIsType(eo,eo@\loc,t2); // eo is of arbitrary type t2
    Constraint c3 = LubOf([t1,t2],t3); // t3 is the lub of t1 and t2
    Constraint c4 = TreeIsType(ep,ep@\loc,t3); // The result is t3, the lub of t1 and t2
    cs.constraints = cs.constraints + { c1, c2, c3, c4 };
    return cs;
}

//
// Collect constraints for the logical implication expression e ==> e'
//
//      el : bool, er : bool
// -----------------------------------------
//          el ==> er : bool
//
public ConstraintBase gatherImplicationExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,makeBoolType()) + 
                     TreeIsType(el,el@\loc,makeBoolType()) + TreeIsType(er,er@\loc,makeBoolType());
    return cs;
}

//
// Collect constraints for the logical equivalence expression e <==> e'
//
//      el : bool, er : bool
// -----------------------------------------
//          el <==> er : bool
//
public ConstraintBase gatherEquivalenceExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,makeBoolType()) + 
                     TreeIsType(el,el@\loc,makeBoolType()) + TreeIsType(er,er@\loc,makeBoolType());
    return cs;
}

//
// Collect constraints for the logical and expression e && e'
//
//      el : bool, er : bool
// -----------------------------------------
//          el && er : bool
//
public ConstraintBase gatherAndExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,makeBoolType()) + 
                     TreeIsType(el,el@\loc,makeBoolType()) + TreeIsType(er,er@\loc,makeBoolType());
    return cs;
}

//
// Collect constraints for the logical or expression e || e'
//
//      el : bool, er : bool
// -----------------------------------------
//          el || er : bool
//
public ConstraintBase gatherOrExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,makeBoolType()) + 
                     TreeIsType(el,el@\loc,makeBoolType()) + TreeIsType(er,er@\loc,makeBoolType());
    return cs;
}

//
// Collect constraints for the match expression, p := e
//
//     e : t1, bindable(e,p)
// -----------------------------
//       p := e : bool
//
public ConstraintBase gatherMatchExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Pattern p, Expression e) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,ts> = makeFreshTypes(cs,4); t1 = ts[0]; t2 = ts[1]; t3 = ts[2]; t4 = ts[3]; 
    Constraint c1 = TreeIsType(ep,ep@\loc,t1); 
    Constraint c2 = TreeIsType(e,e@\loc,t2); 

    // Step 2: Constrain the results. The full result is a bool, while the pattern has a
    // pattern type, containing the pattern, and the subject must be bindable to this
    // pattern.
    Constraint c3 = TypesAreEqual(t1, makeBoolType());
    Constraint c4 = TreeIsType(c,c@\loc,t3);
    Constraint c5 = TypesAreEqual(t3,PatternType(t4));
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
public ConstraintBase gatherNoMatchExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, Pattern p, Expression e) {
    // Step 1: Constrain the overall expression and subexpressions
    <cs,ts> = makeFreshTypes(cs,4); t1 = ts[0]; t2 = ts[1]; t3 = ts[2]; t4 = ts[3]; 
    Constraint c1 = TreeIsType(ep,ep@\loc,t1); 
    Constraint c2 = TreeIsType(e,e@\loct2); 

    // Step 2: Constrain the results. The full result is a bool, while the pattern has a
    // pattern type, containing the pattern, and the subject must be bindable to this
    // pattern.
    Constraint c3 = TypesAreEqual(t1, makeBoolType());
    Constraint c4 = TreeIsType(c,c@\loc,t3);
    Constraint c5 = TypesAreEqual(t3,PatternType(t4));
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
                if (isSetType(replaceInferredTypes(eType)) && (Expression)`{<{Expression ","}* el>}` := e) {
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
                if (isListType(replaceInferredTypes(eType)) && (Expression)`[<{Expression ","}* el>]` := e) {
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
public ConstraintBase gatherAllExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, {Expression ","}+ ers) {
    // All the expressions inside the all construct must be of type bool    
    for (er <- ers) {
        cs.constraints = cs.constraints + TreeIsType(er,er@\loc,makeBoolType());
    }

    // The overall result is also of type bool
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,makeBoolType());
    
    return cs;
}
        
//
// Collect constraints for the any expression
//
//      e1 : bool, ..., en : bool
// -----------------------------------------
//          any(e1...en) : bool
//
public ConstraintBase gatherAnyExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression ep, {Expression ","}+ ers) {
    // All the expressions inside the any construct must be of type bool    
    for (er <- ers) {
        cs.constraints = cs.constraints + TreeIsType(er,er@\loc,makeBoolType());
    }

    // The overall result is also of type bool
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,makeBoolType());
    
    return cs;
}

//
// Collect constraints for the map expression
//
//      d1 : td1, r1 : tr1, ..., dn : tdn, rn : trn, td = lub(td1..tdn), tr = lub(tr1..trn)
// ----------------------------------------------------------------------------------------
//                       ( d1 : r1, ..., dn : rn ) : map[td,tr]
//
public ConstraintBase gatherMapExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression exp) {
    // Each element of the domain and range of the map can be of arbitrary type.
    list[RType] domains = [ makeVoidType() ]; list[RType] ranges = [ makeVoidType() ];
    for (<md,mr> <- mapContents) { 
        <cs,ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
        cs.constraints = cs.constraints + TreeIsType(md,md@\loc,t1) + TreeIsType(mr,mr@\loc,t2);
        domains += t1; ranges += t2;
    }

    // The ultimate result of the expression is a map with a domain type equal to the lub of the
    // given domain types and a range type equal to the lub of the given range types.
    <cs,ts> = makeFreshTypes(cs,2); t3 = ts[0]; t4 = ts[1];
    Constraint c1 = LubOf(domains,t3);
    Constraint c2 = LubOf(domains,t4);
    Constraint c3 = TreeIsType(exp, exp@\loc, makeMapType(t3,t4));
    cs.constraints = cs.constraints + { c1, c2, c3 };

    return cs;
}

public ConstraintBase gatherExpressionConstraints(SymbolTable st, ConstraintBase cs, Expression exp) {
    println("Gathering constraints for expression <exp>");
    switch(exp) {
        case (Expression)`<BooleanLiteral bl>` : {
            cs.constraints = cs.constraints + TreeIsType(exp, exp@\loc, makeBoolType());
            return cs;
        }

        case (Expression)`<DecimalIntegerLiteral il>`  : {
            cs.constraints = cs.constraints + TreeIsType(exp, exp@\loc, makeIntType());
            return cs;
        }

        case (Expression)`<OctalIntegerLiteral il>`  : {
            cs.constraints = cs.constraints + TreeIsType(exp, exp@\loc, makeIntType());
            return cs;
        }

        case (Expression)`<HexIntegerLiteral il>`  : {
            cs.constraints = cs.constraints + TreeIsType(exp, exp@\loc, makeIntType());
            return cs;
        }

        case (Expression)`<RealLiteral rl>`  : {
            cs.constraints = cs.constraints + TreeIsType(exp, exp@\loc, makeRealType());
            return cs;
        }

        case (Expression)`<StringLiteral sl>`  : {
            cs.constraints = cs.constraints + TreeIsType(exp, exp@\loc, makeStrType());

            list[Tree] ipl = prodFilter(sl, bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ip <- ipl) {
                cs.constraints = cs.constraints + TreeIsType(ip,ip@\loc,makeStrType());
            }

            return cs;
        }

        case (Expression)`<LocationLiteral ll>`  : {
            cs.constraints = cs.constraints + TreeIsType(exp, exp@\loc, makeLocType());

            list[Expression] ipl = prodFilter(ll, bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd; });
            for (ip <- ipl) {
                cs.constraints = cs.constraints + TreeIsType(ip,ip@\loc,makeStrType());
            }

            return cs;
        }

        case (Expression)`<DateTimeLiteral dtl>`  : {
            cs.constraints = cs.constraints + TreeIsType(exp, exp@\loc, makeDateTimeType());
            return cs;
        }

        // _ as a name, should only be in patterns, but include just in case...
        case (Expression)`_`: {
            cs.constraints = cs.constraints + Failure(exp,exp@\loc,makeFailType("The anonymous name _ can only be used inside a pattern",exp@\loc));
            return cs;
        }

        // Name
        case (Expression)`<Name n>`: {
            <cs, t1> = makeFreshType(cs);
            cs.constraints = cs.constraints + TreeIsType(exp,exp@\loc,t1);
            // TODO: This is a hack to get around some Rascal breakage. The code
            // commented out below should be used instead.
            if (n@\loc in st.itemUses) {
                cs.constraints = cs.constraints + DefinedBy(t1,(st.itemUses)[n@\loc]);
            }   
            //if ( (n@nameIds)? )
            //    cs.constraints = cs.constraints + DefinedBy(t1,n@nameIds);
            return cs;
        }
        
        // QualifiedName
        case (Expression)`<QualifiedName qn>`: {
            <cs, t1> = makeFreshType(cs);
            cs.constraints = cs.constraints + TreeIsType(exp,exp@\loc,t1);
            // TODO: This is a hack to get around some Rascal breakage. The code
            // commented out below should be used instead.
            if (qn@\loc in st.itemUses) {
                cs.constraints = cs.constraints + DefinedBy(t1,(st.itemUses)[qn@\loc]);
            }   
            //if ( (an@nameIds)? )
            //    cs.constraints = cs.constraints + DefinedBy(t1,qn@nameIds);
            return cs;
        }

        // ReifiedType
        case (Expression)`<BasicType t> ( <{Expression ","}* el> )` :
            return gatherReifiedTypeExpressionConstraints(st,cs,exp,t,el);

        // CallOrTree
        case (Expression)`<Expression e1> ( <{Expression ","}* el> )` :
            return gatherCallOrTreeExpressionConstraints(st,cs,exp,e1,el);

        // List
        case (Expression)`[<{Expression ","}* el>]` :
            return gatherListExpressionConstraints(st,cs,exp,el);

        // Set
        case (Expression)`{<{Expression ","}* el>}` :
            return gatherSetExpressionConstraints(st,cs,exp,el);

        // Tuple, with just one element
        case (Expression)`<<Expression ei>>` :
            return gatherTrivialTupleExpressionConstraints(st,cs,exp,ei);

        // Tuple, with multiple elements
        case (Expression)`<<Expression ei>, <{Expression ","}* el>>` :
            return gatherTupleExpressionConstraints(st,cs,exp,ei,el);

        // Closure
        case (Expression)`<Type t> <Parameters p> { <Statement+ ss> }` :
            return gatherClosureExpressionConstraints(st,cs,exp,t,p,ss);

        // VoidClosure
        case (Expression)`<Parameters p> { <Statement* ss> }` :
            return gatherVoidClosureExpressionConstraints(st,cs,exp,p,ss);

        // NonEmptyBlock
        case (Expression)`{ <Statement+ ss> }` :
            return gatherNonEmptyBlockExpressionConstraints(st,cs,exp,ss);
        
        // Visit
        case (Expression) `<Label l> <Visit v>` :
            return gatherVisitExpressionConstraints(st,cs,exp,l,v);
        
        // ParenExp
        case (Expression)`(<Expression e>)` : {
            <cs, t1> = makeFreshType(cs);
            Constraint c1 = TreeIsType(exp,exp@\loc,t1);
            Constraint c2 = TreeIsType(e,e@\loc,t1);
            cs.constraints = cs.constraints + { c1, c2 };
            return cs;
        }

        // Range
        case (Expression)`[ <Expression e1> .. <Expression e2> ]` :
            return gatherRangeExpressionConstraints(st,cs,exp,e1,e2);

        // StepRange
        case (Expression)`[ <Expression e1>, <Expression e2> .. <Expression e3> ]` :
            return gatherStepRangeExpressionConstraints(st,cs,exp,e1,e2,e3);

        // ReifyType
        case (Expression)`#<Type t>` : {
            <cs, t1> = makeFreshType(cs);
            Constraint c1 = TreeIsType(exp,exp@\loc,t1);
            Constraint c2 = IsType(t1, RTypeStructured(RStructuredType(RTypeType(),[RTypeArg(convertType(t))])));
            cs.constraints = cs.constraints + { c1, c2 };
            return cs;
        }

        // FieldUpdate
        case (Expression)`<Expression e1> [<Name n> = <Expression e2>]` :
            return gatherFieldUpdateExpressionConstraints(st,cs,exp,e1,n,e2);

        // FieldAccess
        case (Expression)`<Expression e1> . <Name n>` :
            return gatherFieldAccessExpressionConstraints(st,cs,exp,e1,n);

        // FieldProject
        case (Expression)`<Expression e1> < <{Field ","}+ fl> >` :
            return gatherFieldProjectExpressionConstraints(st,cs,exp,e1,fl);

        // Subscript 
        case (Expression)`<Expression e1> [ <{Expression ","}+ el> ]` :
            return gatherSubscriptExpressionConstraints(st,cs,exp,e1,el);

        // IsDefined
        case (Expression)`<Expression e> ?` :
            return gatherIsDefinedExpressionConstraints(st,cs,exp,e);

        // Negation
        case (Expression)`! <Expression e>` :
            return gatherNegationExpressionConstraints(st,cs,exp,e);

        // Negative
        case (Expression)`- <Expression e> ` :
            return gatherNegativeExpressionConstraints(st,cs,exp,e);

        // TransitiveReflexiveClosure
        case (Expression)`<Expression e> * ` :
            return gatherTransitiveReflexiveClosureExpressionConstraints(st,cs,exp,e);

        // TransitiveClosure
        case (Expression)`<Expression e> + ` :
            return gatherTransitiveClosureExpressionConstraints(st,cs,exp,e);

        // GetAnnotation
        case (Expression)`<Expression e> @ <Name n>` :
            return gatherGetAnnotationExpressionConstraints(st,cs,exp,e,n);

        // SetAnnotation
        case (Expression)`<Expression e1> [@ <Name n> = <Expression e2>]` :
            return gatherSetAnnotationExpressionConstraints(st,cs,exp,e1,n,e2);

        // Composition
        case (Expression)`<Expression e1> o <Expression e2>` :
            return gatherCompositionExpressionConstraints(st,cs,exp,e1,e2);

        // Product
        case (Expression)`<Expression e1> * <Expression e2>` :
            return gatherProductExpressionConstraints(st,cs,exp,e1,e2);

        // Join
        case (Expression)`<Expression e1> join <Expression e2>` :
            return gatherJoinExpressionConstraints(st,cs,exp,e1,e2);

        // Div
        case (Expression)`<Expression e1> / <Expression e2>` :
            return gatherDivExpressionConstraints(st,cs,exp,e1,e2);

        // Mod
        case (Expression)`<Expression e1> % <Expression e2>` :
            return gatherModExpressionConstraints(st,cs,exp,e1,e2);

        // Intersection
        case (Expression)`<Expression e1> & <Expression e2>` :
            return gatherIntersectionExpressionConstraints(st,cs,exp,e1,e2);
        
        // Plus
        case (Expression)`<Expression e1> + <Expression e2>` :
            return gatherPlusExpressionConstraints(st,cs,exp,e1,e2);

        // Minus
        case (Expression)`<Expression e1> - <Expression e2>` :
            return gatherMinusExpressionConstraints(st,cs,exp,e1,e2);

        // NotIn
        case (Expression)`<Expression e1> notin <Expression e2>` :
            return gatherNotInExpressionConstraints(st,cs,exp,e1,e2);

        // In
        case (Expression)`<Expression e1> in <Expression e2>` :
            return gatherInExpressionConstraints(st,cs,exp,e1,e2);

        // LessThan
        case (Expression)`<Expression e1> < <Expression e2>` :
            return gatherLessThanExpressionConstraints(st,cs,exp,e1,e2);

        // LessThanOrEq
        case (Expression)`<Expression e1> <= <Expression e2>` :
            return gatherLessThanOrEqualExpressionConstraints(st,cs,exp,e1,e2);

        // GreaterThan
        case (Expression)`<Expression e1> > <Expression e2>` :
            return gatherGreaterThanExpressionConstraints(st,cs,exp,e1,e2);

        // GreaterThanOrEq
        case (Expression)`<Expression e1> >= <Expression e2>` :
            return gatherGreaterThanOrEqualExpressionConstraints(st,cs,exp,e1,e2);

        // Equals
        case (Expression)`<Expression e1> == <Expression e2>` :
            return gatherEqualsExpressionConstraints(st,cs,exp,e1,e2);

        // NotEquals
        case (Expression)`<Expression e1> != <Expression e2>` :
            return gatherNotEqualsExpressionConstraints(st,cs,exp,e1,e2);

        // IfThenElse (Ternary)
        case (Expression)`<Expression e1> ? <Expression e2> : <Expression e3>` :
            return gatherIfThenElseExpressionConstraints(st,cs,exp,e1,e2,e3);

        // IfDefinedOtherwise
        case (Expression)`<Expression e1> ? <Expression e2>` :
            return gatherIfDefinedOtherwiseExpressionConstraints(st,cs,exp,e1,e2);

        // Implication
        case (Expression)`<Expression e1> ==> <Expression e2>` :
            return gatherImplicationExpressionConstraints(st,cs,exp,e1,e2);

        // Equivalence
        case (Expression)`<Expression e1> <==> <Expression e2>` :
            return gatherEquivalenceExpressionConstraints(st,cs,exp,e1,e2);

        // And
        case (Expression)`<Expression e1> && <Expression e2>` :
            return gatherAndExpressionConstraints(st,cs,exp,e1,e2);

        // Or
        case (Expression)`<Expression e1> || <Expression e2>` :
            return gatherOrExpressionConstraints(st,cs,exp,e1,e2);
        
        // Match
        case (Expression)`<Pattern p> := <Expression e>` :
            return gatherMatchExpressionConstraints(st,cs,exp,p,e);

        // NoMatch
        case (Expression)`<Pattern p> !:= <Expression e>` :
            return gatherNoMatchExpressionConstraints(st,cs,exp,p,e);

        // Enumerator
        case (Expression)`<Pattern p> <- <Expression e>` :
            return gatherEnumeratorExpressionConstraints(st,cs,exp,p,e);
        
        // Set Comprehension
        case (Expression) `{ <{Expression ","}+ el> | <{Expression ","}+ er> }` :
            return gatherSetComprehensionExpressionConstraints(st,cs,exp,el,er);

        // List Comprehension
        case (Expression) `[ <{Expression ","}+ el> | <{Expression ","}+ er> ]` :
            return gatherListComprehensionExpressionConstraints(st,cs,exp,el,er);
        
        // Map Comprehension
        case (Expression) `( <Expression ef> : <Expression et> | <{Expression ","}+ er> )` :
            return gatherMapComprehensionExpressionConstraints(st,cs,exp,ef,et,er);
        
        // Reducer 
        case (Expression)`( <Expression ei> | <Expression er> | <{Expression ","}+ egs> )` :
            return gatherReducerExpressionConstraints(st,cs,exp,ei,er,egs);
        
        // It
        case (Expression)`it` : {
            <cs, t1> = makeFreshType(cs);
            cs.constraints = cs.constraints + TreeIsType(exp,exp@\loc,t1);
            // TODO: This is a hack to get around some Rascal breakage. The code
            // commented out below should be used instead.
            if (exp@\loc in st.itemUses) {
                cs.constraints = cs.constraints + DefinedBy(t1,(st.itemUses)[exp@\loc]);
            }   
            //if ( (exp@nameIds)? )
            //    cs.constraints = cs.constraints + DefinedBy(t1,exp@nameIds);
            return cs;
        }
            
        // Any 
        case (Expression)`any(<{Expression ","}+ egs>)` :
            return gatherAnyExpressionConstraints(st,cs,exp,egs);

        // All 
        case (Expression)`all(<{Expression ","}+ egs>)` :
            return gatherAllExpressionConstraints(st,cs,exp,egs);

    }

    // Logic for handling maps -- we cannot directly match them, so instead we need to pick apart the tree
    // representing the map.
    // exp[0] is the production used, exp[1] is the actual parse tree contents
    if (prod(_,_,attrs([_*,term(cons("Map")),_*])) := exp[0])
        return gatherMapExpressionConstraints(st,cs,exp);
        
    throw "Error, unmatched expression <exp>";
}
