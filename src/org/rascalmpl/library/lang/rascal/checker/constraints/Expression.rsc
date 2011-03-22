@bootstrapParser
module lang::rascal::checker::constraints::Expression

import List;
import ParseTree;
import IO;
import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::checker::constraints::Constraints;
import lang::rascal::checker::Annotations;
import lang::rascal::checker::TreeUtils;
import lang::rascal::syntax::RascalRascal;

//
// Overall driver to gather constraints on the various expressions present in Rascal.
// This calls out to specific functions below, which (generally) include the typing
// rule used to generate the constraints.
//
// TODO: We may need support for string interpolation typing in the string literals, but
// I think this is being handled just by the visit.
//
public ConstraintBase gatherExpressionConstraints(STBuilder st, ConstraintBase cs, Expression exp) {
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
            return cs;
        }

        case (Expression)`<LocationLiteral ll>`  : {
            cs.constraints = cs.constraints + TreeIsType(exp, exp@\loc, makeLocType());
            return cs;
        }

        case (Expression)`<DateTimeLiteral dtl>`  : {
            cs.constraints = cs.constraints + TreeIsType(exp, exp@\loc, makeDateTimeType());
            return cs;
        }

        // _ as a name, which, unbelievably, can be used just about anywhere (but we
        // treat it as an anonymous type here, instead of as a regular name like in
        // the current implementation)
        case (Expression)`_`: {
            <cs, t1> = makeFreshType(cs);
            cs.constraints = cs.constraints + TreeIsType(exp,exp@\loc,t1);
            if (exp@\loc in st.itemUses<0>)
                cs.constraints = cs.constraints + DefinedBy(t1,st.itemUses[exp@\loc],exp@\loc);
            return cs;
        }

        // Name
        case (Expression)`<Name n>`: {
            <cs, t1> = makeFreshType(cs);
            cs.constraints = cs.constraints + TreeIsType(exp,exp@\loc,t1);
            if (n@\loc in st.itemUses<0>)
                cs.constraints = cs.constraints + DefinedBy(t1,st.itemUses[n@\loc],n@\loc);
            return cs;
        }
        
        // QualifiedName
        case (Expression)`<QualifiedName qn>`: {
            <cs, t1> = makeFreshType(cs);
            cs.constraints = cs.constraints + TreeIsType(exp,exp@\loc,t1);
            if (qn@\loc in st.itemUses<0>)
                cs.constraints = cs.constraints + DefinedBy(t1,st.itemUses[qn@\loc],qn@\loc);
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
            cs.constraints = cs.constraints + TreeIsType(exp,exp@\loc,t1) + TreeIsType(e,e@\loc,t1);
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
            cs.constraints = cs.constraints + TreeIsType(exp,exp@\loc,makeReifiedType(convertType(t)));
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
            if (exp@\loc in st.itemUses<0>)
                cs.constraints = cs.constraints + DefinedBy(t1,st.itemUses[exp@\loc],exp@\loc);
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
      
    // TODO: This should be here, but the above is NEVER matching because of breakage inside Rascal.
    // So, fix that, and then uncomment this.  
    //throw "Error, unmatched expression <exp>";
    return cs;
}

//
// Gather constraints for the reified type expression, which is like int() or list(int()) or list(#myadt).
// 
// NOTE: This is handling part of the current logic, which is where the arguments are all reified types.
// There is also logic that lets this be values instead, although that appears to be broken, and I'm not
// sure what it is expected to do. So,
//
// TODO: Add logic for arguments that are values, not reified types
//
// TODO: Add support for reified reified types
//
private ConstraintBase gatherReifiedTypeExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Type t, {Expression ","}* el) {
    // Each element of the list should be a reified type value for some arbitrary type t1, 
    // like #list[int] or list(int()), with the type then also a reified type
    list[RType] typeList = [ ];
    for (e <- el) {
        <cs,t1> = makeFreshType(cs);
        cs.constraints = cs.constraints + TreeIsType(e,e@\loc,makeReifiedType(t1));
    }
    
    // The ultimate type of the expression is then the reified type formed using type t and the type
    // parameters: for instance, if t = list, and there is a single parameter p = int, then the ultimate
    // type is type[list[int]], the reified form of list[int]
    <cs,ts> = makeFreshType(cs,2); t2 = ts[0]; t3 = ts[1];
    Constraint c1 = IsReifiedType(t2,typeList,ep@\loc,t3);
    Constraint c2 = TreeIsType(ep,ep@\loc,t3);
    cs.constraints = cs.constraints + { c1, c2 };
    
    return cs;
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
public ConstraintBase gatherCallOrTreeExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression ec, {Expression ","}* es) {
    // First, assign types for the params; each of of arbitrary type
    list[RType] params = [ ];
    for (e <- es) {
        <cs, t1> = makeFreshType(cs);
        cs.constraints = cs.constraints + TreeIsType(e,e@\loc,t1);
        params += t1;
    }

    // ec, the target (f), is of arbitrary type t2; the resulting type, t3, is based on the invocation 
    // of t2 with param types params, and is the overall result of ep (the entire expression)
    <cs,ts> = makeFreshTypes(cs,2); t2 = ts[0]; t3 = ts[1];
    Constraint c1 = TreeIsType(ec,ec@\loc,t2);
    Constraint c2 = CallOrTree(t2,params,t3,ep@\loc);
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
public ConstraintBase gatherListExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, {Expression ","}* es) {
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
    Constraint c1 = LubOfList(elements,t2,ep@\loc);
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
public ConstraintBase gatherSetExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, {Expression ","}* es) {
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
    Constraint c1 = LubOfSet(elements,t2,ep@\loc);
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
public ConstraintBase gatherTrivialTupleExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression ei) {
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
public ConstraintBase gatherTupleExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression ei, {Expression ","}* el) {
    // The elements of the tuple are each of some arbitrary type
    list[RType] elements = [ ]; 
    for (e <- el) { 
        <cs,t1> = makeFreshType(cs);
        cs.constraints = cs.constraints + TreeIsType(e,e@\loc,t1); 
        elements += t1;
    }

    // The tuple is then formed from these types
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,makeTupleType(elements));

    return cs;
}

//
// Gather constraints for a closure.
//
// TODO: Add type rule
//
public RType gatherClosureExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Type t, Parameters p, Statement+ ss) {
    list[RType] pTypes = getParameterTypes(p);
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,makeFunctionType(convertType(t), pTypes));
}

//
// Gather constraints for a void closure.
//
// TODO: Add type rule
//
public RType gatherVoidClosureExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Parameters p, Statement+ ss) {
    list[RType] pTypes = getParameterTypes(p);
    cs.constraints = cs.constraints + TreeIsType(ep,ep@\loc,makeFunctionType(makeVoidType(), pTypes));
}
 
//
// Collect constraints on the non-empty block expression: { s1 ... sn }
//
//     s1 : t1, ..., sn : tn
// -----------------------------
//     { s1 ... sn } : tn
//
public ConstraintBase gatherNonEmptyBlockExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Statement+ ss) {
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
//     e : t
// -----------------
//   visit(e) : t
//
public ConstraintBase gatherVisitExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Label l, Visit v) {
    // The type of the expression is the same as the type of the visit, which is the type of the
    // item being visited; i.e., visit("hello") { ... } is of type str. Constraints gathered on
    // the visit assign the type to the visit, so we can just constrain it here, treated as an
    // expression. 
    <cs,t1> = makeFreshType(cs);
    cs.constraints = cs.constraints + TreeIsType(v,v@\loc,t1) + TreeIsType(ep,ep@\loc,t1); 
    return cs;
}

//
// Collect constraints for the range expression: [ e1 .. e2 ]
//
//     e1 : int, e2 : int
// -------------------------------
// [ e1 .. e2 ] : list[int]
//
public ConstraintBase gatherRangeExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression e1, Expression e2) {
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
public ConstraintBase gatherStepRangeExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression e1, Expression e2, Expression e3) {
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
public ConstraintBase gatherFieldUpdateExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Name n, Expression er) {
    <cs,ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
    Constraint c1 = TreeIsType(el,el@\loc,t1); // el has an arbitrary type, t1
    Constraint c2 = TreeIsType(ep,ep@\loc,t1); // the overall expression has the same type as el, i.e., x.f = 3 is of the same type as x
    Constraint c3 = FieldOf(n,t1,t2,n@\loc); // name n is actually a field of type t1, i.e., in x.f = 3, f is a field of x, and has type t2
    Constraint c4 = TreeIsType(er,er@\loc,t3); // the expression being assigned has an arbitrary type, t3
    Constraint c5 = FieldAssignable(ep,ep@\loc,el,n,er,t3,t2); // the type of expression being assigned is assignment compatible with the type of the field

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
public ConstraintBase gatherFieldAccessExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Name n) {
    <cs,ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    
    Constraint c1 = TreeIsType(el,el@\loc,t1); // el has an arbitrary type, t1
    Constraint c2 = FieldOf(n,t1,t2,n@\loc); // name n is actually a field of type t1, i.e., in x.f, f is a field of x, and has type t2
    Constraint c3 = TreeIsType(ep,ep@\loc,t2); // the overall expression has the same type as the type of field n, i.e., x.f is the same type as field f

    cs.constraints = cs.constraints + { c1, c2, c3 };
    return cs;
}

//
// Collect constraints for the field projection expression: e<f1.,,,.fn>
//
public ConstraintBase gatherFieldProjectExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression e, {Field ","}+ fl) {
    <cs,t1> = makeFreshType(cs);
    cs.constraints = cs.constraints + TreeIsType(e,e@\loc,t1); // e is of an arbitrary type t1
    
    // the fields are all fields of type t1, and are of arbitrary type t2; we use NamedFieldOf,
    // instead of FieldOf(as used above), since the type should include the names, which are
    // propagated as part of the type in certain cases
    list[RType] fieldTypes = [ ];
    for (f <- fl) {
        <cs,t2> = makeFreshType(cs);
        if ((Field)`<Name n>` := f) {
            cs.constraints = cs.constraints + NamedFieldOf(f,t1,t2,f@\loc);
        } else if ((Field)`<IntegerLiteral il>` := f) {
            cs.constraints = cs.constraints + IndexedFieldOf(f,t1,t2,f@\loc);
        } else {
            throw "Unexpected field syntax <f>";
        }
        fieldTypes += t2;
    }
    
    // the overall type is then constrained using a FieldProjection constraint, which will
    // derive the resulting type based on the input type and the given fields
    <cs, t3> = makeFreshType(cs);
    cs.constraints = cs.constraints + FieldProjection(t1,fieldTypes,t3,ep@\loc) + TreeIsType(ep,ep@\loc,t3);

    return cs;
}

//
// Collect constraints for the subscript expression: el[e1...en]
//
public ConstraintBase gatherSubscriptExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, {Expression ","}+ es) {
    // el is of abritrary type t1
    <cs,t1> = makeFreshType(cs);
    cs.constraints = cs.constraints + TreeIsType(el,el@\loc,t1);
    
    // all indices are of arbitrary type, since we need to know the subject type first
    // before we can tell if they are of the correct type (for instance, a map will have
    // indices of the domain type, while a list will have int indices)
    list[Expression] indices = [ e | e <- es ];
    list[RType] indexTypes = [ ];
    for (e <- indices) {
        <cs,t3> = makeFreshType(cs); 
        cs.constraints = cs.constraints + TreeIsType(e,e@\loc,t3);
        indexTypes += t3;
    }

    // the result is then based on a Subscript constraint, which will derive the resulting
    // type based on the input type and the concrete fields (which can be helpful IF they
    // are literals)
    // TODO: Static analysis would help here, so we have literal integers more often
    <cs, t2> = makeFreshType(cs);
    cs.constraints = cs.constraints + Subscript(t1,indices,indexTypes,t2,ep@\loc) + TreeIsType(ep,ep@\loc,t2);        

    return cs;
}

//
// Collect constraints for the is defined expression
//
//   e : t
// ----------
//   e? : t
//
public ConstraintBase gatherIsDefinedExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression e) {
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
private ConstraintBase gatherNegationExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression e) {
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
private ConstraintBase gatherNegativeExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression e) {
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
public ConstraintBase gatherTransitiveReflexiveClosureExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression e) {
    <cs,ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    Constraint c1 = TreeIsType(e,e@\loc,makeRelType([t1,t2])); // e must be a relation of arity 2 of arbitrary types t1 and t2
    Constraint c2 = Comparable(t1,t2,e@\loc); // to create the closure, both projections must be comparable
    Constraint c3 = TreeIsType(ep,ep@\loc,makeRelType([t1,t2])); // e* must be the same type as e
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
public ConstraintBase gatherTransitiveClosureExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression e) {
    <cs,ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    Constraint c1 = TreeIsType(e,e@\loc,makeRelType([t1,t2])); // e must be a relation of arity 2 of arbitary types t1 and t2
    Constraint c2 = Comparable(t1,t2,e@\loc); // to create the closure, both projections must be comparable
    Constraint c3 = TreeIsType(ep,ep@\loc,makeRelType([t1,t2])); // e+ must be the same type as e
    cs.constraints = cs.constraints + { c1, c2, c3 };    
    return cs;
}

//
// TODO: To properly check this, we need to keep a map of not just the annotation names and types,
// but of which types those annotations apply to!
//
// TODO: Add type rule!
//
public ConstraintBase gatherGetAnnotationExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression e, Name n) {
    <cs, ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    Constraint c1 = TreeIsType(e,e@\loc,t1);
    Constraint c2 = AnnotationOf(n,t1,t2,n@\loc);
    Constraint c3 = TreeIsType(ep,ep@\loc,t2);
    cs.constraints = cs.constraints + { c1, c2, c3 };
    return cs;
}

//
// TODO: To properly check this, we need to keep a map of not just the annotation names and types,
// but of which types those annotations apply to!
//
// TODO: Add type rule!
//
public ConstraintBase gatherSetAnnotationExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Name n, Expression er) {
    <cs, ts> = makeFreshTypes(cs,4); t1 = ts[0]; t2 = ts[1]; t3 = ts[2]; t4 = ts[3];
    Constraint c1 = TreeIsType(el,el@\loc,t1); // el is of arbitrary type t1
    Constraint c2 = AnnotationOf(n,t1,t2,n@\loc); // n is an annotation on t1 with type t2
    Constraint c3 = TreeIsType(er,er@\loc,t3); // er is of arbitrary type t3
    Constraint c4 = AnnotationAssignable(ep,ep@\loc,el,n,er,t3,t2,t4); // for el[@n] = er, the type of er, t3, is assignable to type type of the annotation, t2, with result t4 
    Constraint c5 = TreeIsType(ep,ep@\loc,t4); // the overall exp type if the type of the assignment result
    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5 };
    return cs;
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
public ConstraintBase gatherCompositionExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    <cs,ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
    Constraint c1 = TreeIsType(el, el@\loc, t1);
    Constraint c2 = TreeIsType(er, er@\loc, t2);
    Constraint c3 = Composable(t1, t2, t3, ep@\loc);
    Constraint c4 = TreeIsType(ep, ep@\loc, t3);
    cs.constraints = cs.constraints + { c1, c2, c3, c4 };
    return cs;
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
public ConstraintBase gatherBinaryExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, RBuiltInOp rop, Expression er) {
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
public ConstraintBase gatherBinaryExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, RBuiltInOp rop, Expression er, RType resultType) {
    <cs,l1> = makeFreshTypes(cs,4); t1 = l1[0]; t2 = l1[1]; t3 = l1[2]; t4 = l1[3];
    Constraint c1 = TreeIsType(el,el@\loc,t1); // The left operand is of arbitrary type t1 
    Constraint c2 = TreeIsType(er,er@\loc,t2); // The right operand is of arbitrary type t2
    Constraint c3 = BuiltInAppliable(rop, makeTupleType([ t1, t2 ]), t3, ep@\loc); // The result of applying rop to t1 and t2 is t3
    Constraint c4 = TreeIsType(ep,ep@\loc,t3); // The overall result is t3
    Constraint c5 = TypesAreEqual(t3, resultType,ep@\loc); // resultType is "t4 given" in the rule above
    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5 };
    return cs;
}

//
// Collect constraints for the product expression e1 * e2
//
public ConstraintBase gatherProductExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Product(), er);
}

//
// Collect constraints for the join expression e1 join e2
//
public ConstraintBase gatherJoinExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Join(), er);
}

//
// Collect constraints for the div expression e1 / e2
//
public ConstraintBase gatherDivExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Div(), er);
}

//
// Collect constraints for the mod expression e1 % e2
//
public ConstraintBase gatherModExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Mod(), er);
}

//
// Collect constraints for the intersection expression e1 & e2
//
public ConstraintBase gatherIntersectionExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Intersect(), er);
}

//
// Collect constraints for the plus expression e1 + e2
//
public ConstraintBase gatherPlusExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Plus(), er);
}

//
// Collect constraints for the minus expression e1 - e2
//
public ConstraintBase gatherMinusExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, Minus(), er);
}

//
// Collect constraints for the notin expression e1 notin e2
//
public ConstraintBase gatherNotInExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, NotIn(), er, makeBoolType());
}

//
// Collect constraints for the in expression e1 in e2
//
public ConstraintBase gatherInExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
        return gatherBinaryExpressionConstraints(st, cs, ep, el, In(), er, makeBoolType());
}

//
// Collect constraints for the Less Than expression e1 < e2
//
public ConstraintBase gatherLessThanExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cs, ep, el, Lt(), er, makeBoolType());
}

//
// Collect constraints for the Less Than or Equal expression e1 <= e2
//
public ConstraintBase gatherLessThanOrEqualExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cs, ep, el, LtEq(), er, makeBoolType());
}

//
// Collect constraints for the Greater Than expression e1 > e2
//
public ConstraintBase gatherGreaterThanExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cs, ep, el, Gt(), er, makeBoolType());
}

//
// Collect constraints for the Greater Than or Equal expression e1 >= e2
//
public ConstraintBase gatherGreaterThanOrEqualExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cs, ep, el, GtEq(), er, makeBoolType());
}

//
// Collect constraints for the Equals expression e1 == e2
//
public ConstraintBase gatherEqualsExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cs, ep, el, Eq(), er, makeBoolType());
}

//
// Collect constraints for the Not Equals expression e1 != e2
//
public ConstraintBase gatherNotEqualsExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cs, ep, el, NEq(), er, makeBoolType());
}

//
// Collect constraints for the ternary if expression eb ? et : ef
//
//      eb : bool, et : t1, ef : t2, t3 = lub(t1,t2)
// -----------------------------------------------------
//          eb ? et : ef  :  t3
//
public ConstraintBase gatherIfThenElseExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression eb, Expression et, Expression ef) {
    <cs,ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
    Constraint c1 = TreeIsType(eb,eb@\loc,makeBoolType()); // The guard is type bool 
    Constraint c2 = TreeIsType(et,et@\loc,t1); // The true branch is of arbitrary type t1
    Constraint c3 = TreeIsType(ef,ef@\loc,t2); // The false branch is of arbitrary type t2
    Constraint c4 = LubOf([t1,t2],t3,ep@\loc); // t3 is the lub of t1 and t2
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
public ConstraintBase gatherIfDefinedOtherwiseExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression ed, Expression eo) {
    <cs,ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
    Constraint c1 = TreeIsType(ed,ed@\loc,t1); // ed is of arbitrary type t1 
    Constraint c2 = TreeIsType(eo,eo@\loc,t2); // eo is of arbitrary type t2
    Constraint c3 = LubOf([t1,t2],t3,ep@\loc); // t3 is the lub of t1 and t2
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
public ConstraintBase gatherImplicationExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
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
public ConstraintBase gatherEquivalenceExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
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
public ConstraintBase gatherAndExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
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
public ConstraintBase gatherOrExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression el, Expression er) {
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
public ConstraintBase gatherMatchExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Pattern p, Expression e) {
    <cs,t1> = makeFreshType(cs);
    Constraint c1 = TreeIsType(e,e@\loc,t1);
    Constraint c2 = Bindable(p,t1,p@\loc);
    Constraint c3 = TreeIsType(ep,ep@\loc,makeBoolType());
    return cs;
}

//
// Collect constraints for the nomatch expression, p !:= e
//
//     e : t1, bindable(e,p)
// -----------------------------
//       p !:= e : bool
//
public ConstraintBase gatherNoMatchExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Pattern p, Expression e) {
    <cs,t1> = makeFreshType(cs);
    Constraint c1 = TreeIsType(e,e@\loc,t1);
    Constraint c2 = Bindable(p,t1,p@\loc);
    Constraint c3 = TreeIsType(ep,ep@\loc,makeBoolType());
    return cs;
}

//
// Enumerators act like a match, i.e., like :=, except for containers, like lists,
// sets, etc, where they "strip off" the outer layer of the subject. For instance,
// n <- 1 acts just like n := 1, while n <- [1..10] acts like [_*,n,_*] := [1..10].
//
//
public ConstraintBase gatherEnumeratorExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Pattern p, Expression e) {
    <cs, t1> = makeFreshType(cs);
    Constraint c1 = TreeIsType(e,e@\loc,t1);
    Constraint c2 = Enumerable(p,t1,p@\loc);
    Constraint c3 = TreeIsType(ep,ep@\loc,makeBoolType());
    cs.constraints = cs.constraints + { c1, c2, c3 };
    return cs;
}

public ConstraintBase gatherSetComprehensionExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, {Expression ","}+ els, {Expression ","}+ ers) {
    list[RType] elements = [ makeVoidType() ]; 
    for (e <- els) { 
        <cs,t1> = makeFreshType(cs);

        // If this is an element that can be spliced, i.e. is not surrounded by set
        // brackets, indicate that in the type, so we can calculate the lub correctly
        if ((Expression)`{<{Expression ","}* el>}` !:= e) {
            t1 = SpliceableElement(t1);
        }

        cs.constraints = cs.constraints + TreeIsType(e,e@\loc,t1); 
        elements += t1;
    }

    for (e <- ers) cs.constraints = cs.constraints + TreeIsType(e,e@\loc,makeBoolType());
    
    // The set itself is the lub of the various elements
    <cs,t2> = makeFreshType(cs); 
    Constraint c1 = LubOfSet(elements,t2,ep@\loc);
    Constraint c2 = TreeIsType(ep, ep@\loc, makeSetType(t2));
    cs.constraints = cs.constraints + { c1, c2 };

    return cs;
}

public ConstraintBase gatherListComprehensionExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, {Expression ","}+ els, {Expression ","}+ ers) {
    list[RType] elements = [ makeVoidType() ]; 
    for (e <- els) { 
        <cs,t1> = makeFreshType(cs);

        // If this is an element that can be spliced, i.e. is not surrounded by list
        // brackets, indicate that in the type, so we can calculate the lub correctly
        if ((Expression)`[<{Expression ","}* el>]` !:= e) {
            t1 = SpliceableElement(t1);
        }

        cs.constraints = cs.constraints + TreeIsType(e,e@\loc,t1); 
        elements += t1;
    }

    for (e <- ers) cs.constraints = cs.constraints + TreeIsType(e,e@\loc,makeBoolType());

    // The list itself is the lub of the various elements
    <cs,t2> = makeFreshType(cs); 
    Constraint c1 = LubOfList(elements,t2,ep@\loc);
    Constraint c2 = TreeIsType(ep, ep@\loc, makeListType(t2));
    cs.constraints = cs.constraints + { c1, c2 };

    return cs;
}

public ConstraintBase gatherMapComprehensionExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression ef, Expression et, {Expression ","}+ ers) {
    <cs,ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    cs.constraints = cs.constraints + TreeIsType(ef,ef@\loc,t1) + TreeIsType(et,et@\loc,t2) + 
                                      TreeIsType(ep,ep@\loc,makeMapType(t1,t2));
    for (e <- ers) cs.constraints = cs.constraints + TreeIsType(e,e@\loc,makeBoolType());
    return cs;
}

//
// NOTE: We cannot actually type this statically, since the type of the "it" expression is implicit and the type of
// the result is based only indirectly on the type of er. If we could type this, we could probably solve the halting
// problem ;)
//
public ConstraintBase gatherReducerExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, Expression ei, Expression er, {Expression ","}+ ers) {
    <cs,ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
    Constraint c1 = TreeIsType(ei,ei@\loc,t1);
    Constraint c2 = TreeIsType(er,er@\loc,t2);
    Constraint c3 = StepItType(er,er@\loc,t1,t3,t2);
    Constraint c4 = TreeIsType(ep,ep@\loc,t2);
    cs.constraints = cs.constraints + { c1, c2, c3, c4 };
    for (e <- ers) cs.constraints = cs.constraints + TreeIsType(e,e@\loc,makeBoolType());
    return cs;
}

//
// Collect constraints for the all expression
//
//      e1 : bool, ..., en : bool
// -----------------------------------------
//          all(e1...en) : bool
//
public ConstraintBase gatherAllExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, {Expression ","}+ ers) {
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
public ConstraintBase gatherAnyExpressionConstraints(STBuilder st, ConstraintBase cs, Expression ep, {Expression ","}+ ers) {
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
public ConstraintBase gatherMapExpressionConstraints(STBuilder st, ConstraintBase cs, Expression exp) {
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
    Constraint c1 = LubOf(domains,t3,exp@\loc);
    Constraint c2 = LubOf(domains,t4,exp@\loc);
    Constraint c3 = TreeIsType(exp, exp@\loc, makeMapType(t3,t4));
    cs.constraints = cs.constraints + { c1, c2, c3 };

    return cs;
}
