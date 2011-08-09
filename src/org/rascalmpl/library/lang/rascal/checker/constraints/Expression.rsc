@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::checker::constraints::Expression

import List;
import ParseTree;
import IO;
import Set;

import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::scoping::ResolveNames;
import lang::rascal::scoping::ScopedTypes;
import lang::rascal::checker::constraints::Constraints;
import lang::rascal::checker::Annotations;
import lang::rascal::checker::TreeUtils;
import lang::rascal::checker::constraints::Pattern;
import lang::rascal::syntax::RascalRascal;

//
// Directly inside a container, can this expression be used in a splice?
//
public bool spliceableExpression(Expression e) {
	switch(e) {
        case (Expression)`<Name n>` : return true;
        case (Expression)`<QualifiedName qn>` : return true;
        default: return false;
	}
}

//
// Overall driver to gather constraints on the various expressions present in Rascal.
// This calls out to specific functions below, which (generally) include the typing
// rule used to generate the constraints.
//
// TODO: We may need support for string interpolation typing in the string literals, but
// I think this is being handled just by the visit.
//
public ConstraintBase gatherExpressionConstraints(STBuilder st, ConstraintBase cb, Expression exp) {
    switch(exp) {
        case (Expression)`<BooleanLiteral bl>` :
            return addConstraintForLoc(cb, exp@\loc, makeBoolType());

        case (Expression)`<DecimalIntegerLiteral il>`  :
            return addConstraintForLoc(cb, exp@\loc, makeIntType());

        case (Expression)`<OctalIntegerLiteral il>`  :
            return addConstraintForLoc(cb, exp@\loc, makeIntType());

        case (Expression)`<HexIntegerLiteral il>`  :
            return addConstraintForLoc(cb, exp@\loc, makeIntType());

        case (Expression)`<RealLiteral rl>`  :
            return addConstraintForLoc(cb, exp@\loc, makeRealType());

        case (Expression)`<StringLiteral sl>`  :
            return addConstraintForLoc(cb, exp@\loc, makeStrType());

        case (Expression)`<LocationLiteral ll>`  :
            return addConstraintForLoc(cb, exp@\loc, makeLocType());

        case (Expression)`<DateTimeLiteral dtl>`  :
            return addConstraintForLoc(cb, exp@\loc, makeDateTimeType());

        // _ as a name, which, unbelievably, can be used just about anywhere (but we
        // treat it as an anonymous type here, instead of as a regular name like in
        // the current implementation)
        case (Expression)`_`: {
	        if (exp@\loc in st.itemUses<0>) {
			    RType itemType = makeVoidType();
	        	definingIds = st.itemUses[exp@\loc];
			    if (size(definingIds) > 1) {
			        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
			    } else {
			        itemType = getTypeForItem(st, getOneFrom(definingIds));
			    }
		        cb = addConstraintForLoc(cb, exp@\loc, itemType);
	        } else {
	            cb = addConstraintForLoc(cb, exp@\loc, makeFailType("No definition for this variable found",exp@\loc));
	        }
            return cb;
        }

        // Name
        case (Expression)`<Name n>`: {
	        if (n@\loc in st.itemUses<0>) {
			    RType itemType = makeVoidType();
	        	definingIds = st.itemUses[n@\loc];
			    if (size(definingIds) > 1) {
			        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
			    } else {
			        itemType = getTypeForItem(st, getOneFrom(definingIds));
			    }
		        cb = addConstraintForLoc(cb, n@\loc, itemType);
	        } else {
	            cb = addConstraintForLoc(cb, n@\loc, makeFailType("No definition for this variable found",n@\loc));
	        }
            return cb;
        }
        
        // QualifiedName
        case (Expression)`<QualifiedName qn>`: {
	        if (qn@\loc in st.itemUses<0>) {
			    RType itemType = makeVoidType();
	        	definingIds = st.itemUses[qn@\loc];
			    if (size(definingIds) > 1) {
			        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
			    } else {
			        itemType = getTypeForItem(st, getOneFrom(definingIds));
			    }
		        cb = addConstraintForLoc(cb, qn@\loc, itemType);
	        } else {
	            cb = addConstraintForLoc(cb, qn@\loc, makeFailType("No definition for this variable found",qn@\loc));
	        }
            return cb;
        }

        // ReifiedType
        case (Expression)`<BasicType t> ( <{Expression ","}* el> )` :
            return gatherReifiedTypeExpressionConstraints(st,cb,exp,t,el);

        // CallOrTree
        case (Expression)`<Expression e1> ( <{Expression ","}* el> )` :
            return gatherCallOrTreeExpressionConstraints(st,cb,exp,e1,el);

        // List
        case (Expression)`[<{Expression ","}* el>]` :
            return gatherListExpressionConstraints(st,cb,exp,el);

        // Set
        case (Expression)`{<{Expression ","}* el>}` :
            return gatherSetExpressionConstraints(st,cb,exp,el);

        // Tuple, with just one element
        case (Expression)`<<Expression ei>>` :
            return gatherTrivialTupleExpressionConstraints(st,cb,exp,ei);

        // Tuple, with multiple elements
        case (Expression)`<<Expression ei>, <{Expression ","}* el>>` :
            return gatherTupleExpressionConstraints(st,cb,exp,ei,el);

        // Closure
        case (Expression)`<Type t> <Parameters p> { <Statement+ ss> }` :
            return gatherClosureExpressionConstraints(st,cb,exp,t,p,ss);

        // VoidClosure
        case (Expression)`<Parameters p> { <Statement* ss> }` :
            return gatherVoidClosureExpressionConstraints(st,cb,exp,p,ss);

        // NonEmptyBlock
        case (Expression)`{ <Statement+ ss> }` :
            return gatherNonEmptyBlockExpressionConstraints(st,cb,exp,ss);
        
        // Visit
        case (Expression) `<Label l> <Visit v>` :
            return gatherVisitExpressionConstraints(st,cb,exp,l,v);
        
        // ParenExp
        case (Expression)`(<Expression e>)` :
            return addConstraintForLoc(cb, exp@\loc, typeForLoc(cb, e@\loc));

        // Range
        case (Expression)`[ <Expression e1> .. <Expression e2> ]` :
            return gatherRangeExpressionConstraints(st,cb,exp,e1,e2);

        // StepRange
        case (Expression)`[ <Expression e1>, <Expression e2> .. <Expression e3> ]` :
            return gatherStepRangeExpressionConstraints(st,cb,exp,e1,e2,e3);

        // ReifyType
        case (Expression)`#<Type t>` :
            return addConstraintForLoc(cb, exp@\loc, makeReifiedType(convertType(t)));

        // FieldUpdate
        case (Expression)`<Expression e1> [<Name n> = <Expression e2>]` :
            return gatherFieldUpdateExpressionConstraints(st,cb,exp,e1,n,e2);

        // FieldAccess
        case (Expression)`<Expression e1> . <Name n>` :
            return gatherFieldAccessExpressionConstraints(st,cb,exp,e1,n);

        // FieldProject
        case (Expression)`<Expression e1> < <{Field ","}+ fl> >` :
            return gatherFieldProjectExpressionConstraints(st,cb,exp,e1,fl);

        // Subscript 
        case (Expression)`<Expression e1> [ <{Expression ","}+ el> ]` :
            return gatherSubscriptExpressionConstraints(st,cb,exp,e1,el);

        // IsDefined
        case (Expression)`<Expression e> ?` :
            return gatherIsDefinedExpressionConstraints(st,cb,exp,e);

        // Negation
        case (Expression)`! <Expression e>` :
            return gatherNegationExpressionConstraints(st,cb,exp,e);

        // Negative
        case (Expression)`- <Expression e>` :
            return gatherNegativeExpressionConstraints(st,cb,exp,e);

        // TransitiveReflexiveClosure
        case (Expression)`<Expression e> *` :
            return gatherTransitiveReflexiveClosureExpressionConstraints(st,cb,exp,e);

        // TransitiveClosure
        case (Expression)`<Expression e> +` :
            return gatherTransitiveClosureExpressionConstraints(st,cb,exp,e);

        // GetAnnotation
        case (Expression)`<Expression e> @ <Name n>` :
            return gatherGetAnnotationExpressionConstraints(st,cb,exp,e,n);

        // SetAnnotation
        case (Expression)`<Expression e1> [@ <Name n> = <Expression e2>]` :
            return gatherSetAnnotationExpressionConstraints(st,cb,exp,e1,n,e2);

        // Composition
        case (Expression)`<Expression e1> o <Expression e2>` :
            return gatherCompositionExpressionConstraints(st,cb,exp,e1,e2);

        // Product
        case (Expression)`<Expression e1> * <Expression e2>` :
            return gatherProductExpressionConstraints(st,cb,exp,e1,e2);

        // Join
        case (Expression)`<Expression e1> join <Expression e2>` :
            return gatherJoinExpressionConstraints(st,cb,exp,e1,e2);

        // Div
        case (Expression)`<Expression e1> / <Expression e2>` :
            return gatherDivExpressionConstraints(st,cb,exp,e1,e2);

        // Mod
        case (Expression)`<Expression e1> % <Expression e2>` :
            return gatherModExpressionConstraints(st,cb,exp,e1,e2);

        // Intersection
        case (Expression)`<Expression e1> & <Expression e2>` :
            return gatherIntersectionExpressionConstraints(st,cb,exp,e1,e2);
        
        // Plus
        case (Expression)`<Expression e1> + <Expression e2>` :
            return gatherPlusExpressionConstraints(st,cb,exp,e1,e2);

        // Minus
        case (Expression)`<Expression e1> - <Expression e2>` :
            return gatherMinusExpressionConstraints(st,cb,exp,e1,e2);

        // NotIn
        case (Expression)`<Expression e1> notin <Expression e2>` :
            return gatherNotInExpressionConstraints(st,cb,exp,e1,e2);

        // In
        case (Expression)`<Expression e1> in <Expression e2>` :
            return gatherInExpressionConstraints(st,cb,exp,e1,e2);

        // LessThan
        case (Expression)`<Expression e1> < <Expression e2>` :
            return gatherLessThanExpressionConstraints(st,cb,exp,e1,e2);

        // LessThanOrEq
        case (Expression)`<Expression e1> <= <Expression e2>` :
            return gatherLessThanOrEqualExpressionConstraints(st,cb,exp,e1,e2);

        // GreaterThan
        case (Expression)`<Expression e1> > <Expression e2>` :
            return gatherGreaterThanExpressionConstraints(st,cb,exp,e1,e2);

        // GreaterThanOrEq
        case (Expression)`<Expression e1> >= <Expression e2>` :
            return gatherGreaterThanOrEqualExpressionConstraints(st,cb,exp,e1,e2);

        // Equals
        case (Expression)`<Expression e1> == <Expression e2>` :
            return gatherEqualsExpressionConstraints(st,cb,exp,e1,e2);

        // NotEquals
        case (Expression)`<Expression e1> != <Expression e2>` :
            return gatherNotEqualsExpressionConstraints(st,cb,exp,e1,e2);

        // IfThenElse (Ternary)
        case (Expression)`<Expression e1> ? <Expression e2> : <Expression e3>` :
            return gatherIfThenElseExpressionConstraints(st,cb,exp,e1,e2,e3);

        // IfDefinedOtherwise
        case (Expression)`<Expression e1> ? <Expression e2>` :
            return gatherIfDefinedOtherwiseExpressionConstraints(st,cb,exp,e1,e2);

        // Implication
        case (Expression)`<Expression e1> ==> <Expression e2>` :
            return gatherImplicationExpressionConstraints(st,cb,exp,e1,e2);

        // Equivalence
        case (Expression)`<Expression e1> <==> <Expression e2>` :
            return gatherEquivalenceExpressionConstraints(st,cb,exp,e1,e2);

        // And
        case (Expression)`<Expression e1> && <Expression e2>` :
            return gatherAndExpressionConstraints(st,cb,exp,e1,e2);

        // Or
        case (Expression)`<Expression e1> || <Expression e2>` :
            return gatherOrExpressionConstraints(st,cb,exp,e1,e2);
        
        // Match
        case (Expression)`<Pattern p> := <Expression e>` :
            return gatherMatchExpressionConstraints(st,cb,exp,p,e);

        // NoMatch
        case (Expression)`<Pattern p> !:= <Expression e>` :
            return gatherNoMatchExpressionConstraints(st,cb,exp,p,e);

        // Enumerator
        case (Expression)`<Pattern p> <- <Expression e>` :
            return gatherEnumeratorExpressionConstraints(st,cb,exp,p,e);
        
        // Set Comprehension
        case (Expression) `{ <{Expression ","}+ el> | <{Expression ","}+ er> }` :
            return gatherSetComprehensionExpressionConstraints(st,cb,exp,el,er);

        // List Comprehension
        case (Expression) `[ <{Expression ","}+ el> | <{Expression ","}+ er> ]` :
            return gatherListComprehensionExpressionConstraints(st,cb,exp,el,er);
        
        // Map Comprehension
        case (Expression) `( <Expression ef> : <Expression et> | <{Expression ","}+ er> )` :
            return gatherMapComprehensionExpressionConstraints(st,cb,exp,ef,et,er);
        
        // Reducer 
        case (Expression)`( <Expression ei> | <Expression er> | <{Expression ","}+ egs> )` :
            return gatherReducerExpressionConstraints(st,cb,exp,ei,er,egs);
        
        // It
        case (Expression)`it` : {
	        if (exp@\loc in st.itemUses<0>) {
			    RType itemType = makeVoidType();
	        	definingIds = st.itemUses[exp@\loc];
			    if (size(definingIds) > 1) {
			        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
			    } else {
			        itemType = getTypeForItem(st, getOneFrom(definingIds));
			    }
		        cb = addConstraintForLoc(cb, exp@\loc, itemType);
	        } else {
	            cb = addConstraintForLoc(cb, exp@\loc, makeFailType("No definition for this variable found",exp@\loc));
	        }
            return cb;
        }
            
        // Any 
        case (Expression)`any(<{Expression ","}+ egs>)` :
            return gatherAnyExpressionConstraints(st,cb,exp,egs);

        // All 
        case (Expression)`all(<{Expression ","}+ egs>)` :
            return gatherAllExpressionConstraints(st,cb,exp,egs);

    }

    // Logic for handling maps -- we cannot directly match them, so instead we need to pick apart the tree
    // representing the map.
    // exp[0] is the production used, exp[1] is the actual parse tree contents
    if (prod(_,_,attrs([_*,term(cons("Map")),_*])) := exp[0])
        return gatherMapExpressionConstraints(st,cb,exp);
      
    // TODO: This should be here, but the above is NEVER matching because of breakage inside Rascal.
    // So, fix that, and then uncomment this.  
    //throw "Error, unmatched expression <exp>";
    return cb;
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
private ConstraintBase gatherReifiedTypeExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Type t, {Expression ","}* el) {
    // Each element of the list should be a reified type value for some arbitrary type rt, 
    // like #list[int] or list(int()), with the type then also a reified type
    list[RType] typeList = [ ];
    for (e <- el) {
        < cb, rt > = makeFreshType(cb);
        cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc), makeReifiedType(rt), e@\loc);
    }
    
    // The ultimate type of the expression is then the reified type formed using type t and the type
    // parameters: for instance, if t = list, and there is a single parameter p = int, then the ultimate
    // type is type[list[int]], the reified form of list[int]
    < cb, tr > = makeFreshType(cb);
    cb.constraints = cb.constraints + IsReifiedType(convertType(t), typeList, tr, ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, tr);
    
    return cb;
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
public ConstraintBase gatherCallOrTreeExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression ec, {Expression ","}* es) {
    // First, get back the types already assigned to each parameter
    list[RType] paramTypes = [ typeForLoc(cb, e@\loc) | e <- es ];

    // ec, the target (f), is of arbitrary type tt; the resulting type, tr, is based on the 
    // invocation of tt with param types paramTypes, and is the overall result of ep (the entire expression)
    RType tt = typeForLoc(cb, ec@\loc);
    < cb, tr > = makeFreshType(cb);
    cb.constraints = cb.constraints + CallOrTree(tt, paramTypes, tr, ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, tr);

    return cb;  
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
public ConstraintBase gatherListExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, {Expression ","}* es) {
    // The elements of the list are each of some arbitrary type
    list[RType] elements = [ makeVoidType() ]; 
    for (e <- es) { 
        et = typeForLoc(cb, e@\loc);

        // If this is an element that can be spliced, i.e. is not surrounded by list
        // brackets, indicate that in the type, so we can calculate the lub correctly
        if (spliceableExpression(e)) et = SpliceableElement(et);

        elements += et;
    }

    // The list itself is the lub of the various elements
    < cb, rt > = makeFreshType(cb); 
    cb.constraints = cb.constraints + LubOfList(elements,rt,ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, makeListType(rt));

    return cb;
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
public ConstraintBase gatherSetExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, {Expression ","}* es) {
    // The elements of the set are each of some arbitrary type
    list[RType] elements = [ makeVoidType() ]; 
    for (e <- es) { 
        et = typeForLoc(cb, e@\loc);

        // If this is an element that can be spliced, i.e. is not surrounded by list
        // brackets, indicate that in the type, so we can calculate the lub correctly
        if (spliceableExpression(e)) et = SpliceableElement(et);

        elements += et;
    }

    // The set itself is the lub of the various elements
    < cb, rt > = makeFreshType(cb); 
    cb.constraints = cb.constraints + LubOfSet(elements,rt,ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, makeSetType(rt));

    return cb;
}

//
// Gather constraints for the trivial tuple expression: < e1 >
//
//      e1 : t1
// ----------------------
//   < e1 > : tuple[t1]
//
public ConstraintBase gatherTrivialTupleExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression ei) {
    return addConstraintForLoc(cb, ep@\loc, makeTupleType( [ typeForLoc(cb, ei@\loc) ] ));
}

//
// Gather constraints for the tuple expression: < e1, ..., en >
//
//      e1 : t1, ..., en : tn
// ------------------------------------------
//   < e1, ..., en > : tuple[t1, ..., tn]
//
public ConstraintBase gatherTupleExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression ei, {Expression ","}* el) {
    return addConstraintForLoc(cb, ep@\loc, makeTupleType( [ typeForLoc(cb, ei@\loc) ] + [ typeForLoc(cb, eli@\loc) | eli <- el ] ));
}

//
// Gather constraints for a closure.
//
// TODO: Add type rule
//
public RType gatherClosureExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Type t, Parameters p, Statement+ ss) {
    return addConstraintForLoc(cb, ep@\loc, makeFunctionType(convertType(t), getParameterTypes(cb,p)));
}

//
// Gather constraints for a void closure.
//
// TODO: Add type rule
//
public RType gatherVoidClosureExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Parameters p, Statement+ ss) {
    return addConstraintForLoc(cb, ep@\loc, makeFunctionType(makeVoidType(), getParameterTypes(cb,p)));
}
 
//
// Collect constraints on the non-empty block expression: { s1 ... sn }
//
//     s1 : t1, ..., sn : tn
// -----------------------------
//     { s1 ... sn } : tn
//
public ConstraintBase gatherNonEmptyBlockExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Statement+ ss) {
    return addConstraintForLoc(cb, ep@\loc, typeForLoc(cb, last([s | s <- ss])@\loc));
}

//
// Collect constraints on the visit expression: visit(e) { case c1 ... case cn }
// 
//     e : t
// -----------------
//   visit(e) : t
//
public ConstraintBase gatherVisitExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Label l, Visit v) {
    // The type of the expression is the same as the type of the visit, which is the type of the
    // item being visited; i.e., visit("hello") { ... } is of type str. Constraints gathered on
    // the visit assign the type to the visit, so we can just constrain it here, treated as an
    // expression.
    return addConstraintForLoc(cb, ep@\loc, typeForLoc(cb, v@\loc)); 
}

//
// Collect constraints for the range expression: [ e1 .. e2 ]
//
//     e1 : int, e2 : int
// -------------------------------
// [ e1 .. e2 ] : list[int]
//
public ConstraintBase gatherRangeExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression e1, Expression e2) {
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb,e1@\loc),makeIntType(),ep@\loc);
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb,e2@\loc),makeIntType(),ep@\loc);
    return addConstraintForLoc(cb, ep@\loc, makeListType(makeIntType()));  
}

//
// Collect constraints for the step range expression: [ e1, e2 .. e3 ]
//
//     e1 : int, e2 : int, e3 : int
// ----------------------------------------
//     [ e1, e2 .. e3 ] : list[int]
//
public ConstraintBase gatherStepRangeExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression e1, Expression e2, Expression e3) {
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb,e1@\loc),makeIntType(),ep@\loc);
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb,e2@\loc),makeIntType(),ep@\loc);
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb,e3@\loc),makeIntType(),ep@\loc);
    return addConstraintForLoc(cb, ep@\loc, makeListType(makeIntType()));  
}

//
// Collect constraints for the field update expression: el.n = er
//
// el : t1, n fieldOf t1, t1.n : t2, er : t3, assignable(t3,t2)
// -------------------------------------------------------------
//                el.n = er : t1
//
public ConstraintBase gatherFieldUpdateExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Name n, Expression er) {
    // el is an expression of arbitrary type tel
    tel = typeForLoc(cb, el@\loc);
    
    // field n must be a field of type tel, and is of type ft
    < cb, ft > = makeFreshType(cb);
    cb.constraints = cb.constraints + FieldOf(n, tel, ft, ep@\loc);
    
    // er is an expression of arbitrary type ter
    ter = typeForLoc(cb, er@\loc);
    
    // ter must be assignable into type ft
    cb.constraints = cb.constraints + FieldAssignable(n,ter,ft,U(),ep@\loc);
     
    // the expression has the type of tel
    cb = addConstraintForLoc(cb, ep@\loc, tel);
    
    return cb;
}

//
// Collect constraints for the field access expression: el.n
//
//  el : t1, n fieldOf t1, t1.n : t2
// ----------------------------------------
//        el.n : t2
//
public ConstraintBase gatherFieldAccessExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Name n) {
    // el is an expression of arbitrary type tel
    tel = typeForLoc(cb, el@\loc);
    
    // field n must be a field of type tel, and is of type ft
    < cb, ft > = makeFreshType(cb);
    cb.constraints = cb.constraints + FieldOf(n, tel, ft, ep@\loc);
    
    // the expression has the type of field n, ft
    cb = addConstraintForLoc(cb, ep@\loc, ft);
    
    return cb;
}

//
// Collect constraints for the field projection expression: e<f1.,,,.fn>
//
public ConstraintBase gatherFieldProjectExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression e, {Field ","}+ fl) {
    // e is an expression of arbitrary type te
    te = typeForLoc(cb, e@\loc);
    
    // the fields are all fields of type te, and are of arbitrary type tf; we use NamedFieldOf,
    // instead of FieldOf(as used above), since the type should include the names, which are
    // propagated as part of the type in certain cases
    list[RType] fieldTypes = [ ];
    for (f <- fl) {
        < cb, tf > = makeFreshType(cb);
        if ((Field)`<Name n>` := f) {
            cb.constraints = cb.constraints + NamedFieldOf(f,te,tf,f@\loc);
        } else if ((Field)`<IntegerLiteral il>` := f) {
            cb.constraints = cb.constraints + IndexedFieldOf(f,te,tf,f@\loc);
        } else {
            throw "Unexpected field syntax <f>";
        }
        fieldTypes += tf;
    }
    
    // the overall type is then constrained using a FieldProjection constraint, which will
    // derive the resulting type based on the input type and the given fields
    < cb, tr > = makeFreshType(cb);
    cb.constraints = cb.constraints + FieldProjection(te,fieldTypes,tr,ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, tr);

    return cb;
}

//
// Collect constraints for the subscript expression: el[e1...en]
//
public ConstraintBase gatherSubscriptExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, {Expression ","}+ es) {
    // el is of abritrary type tel
    tel = typeForLoc(cb, el@\loc);
    
    // all indices are of arbitrary type, since we need to know the subject type first
    // before we can tell if they are of the correct type (for instance, a map will have
    // indices of the domain type, while a list will have int indices)
    list[Expression] indices = [ e | e <- es ];
    list[RType] indexTypes = [ typeForLoc(cb,e@\loc) | e <- indices ];

    // the result is then based on a Subscript constraint, which will derive the resulting
    // type based on the input type and the concrete fields (which can be helpful IF they
    // are literals)
    // TODO: Static analysis would help here, so we have literal integers more often
    < cb, tr > = makeFreshType(cb);
    cb.constraints = cb.constraints + Subscript(tel,indices,indexTypes,tr,ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, tr);
    
    return cb;
}

//
// Collect constraints for the is defined expression
//
//   e : t
// ----------
//   e? : t
//
public ConstraintBase gatherIsDefinedExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression e) {
    return addConstraintForLoc(cb, ep@\loc, typeForLoc(cb, e@\loc));
}

//
// Collect constraints for the negation expression
//
//      e : bool
// --------------------
//   not e : bool
//
private ConstraintBase gatherNegationExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression e) {
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb,e@\loc),makeBoolType(), ep@\loc);
    return addConstraintForLoc(cb, ep@\loc, makeBoolType());
}

//
// Collect constraints for the negative expression
//
//      e : t1, -_ : t1 -> t2 defined 
// ---------------------------------------------
//          - e : t2
//
private ConstraintBase gatherNegativeExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression e) {
    te = typeForLoc(cb, e@\loc);
    
    < cb, tr > = makeFreshType(cb);
    cb.constraints = cb.constraints + BuiltInAppliable(Negative(), makeTupleType([ te ]), tr, ep@\loc);
    
    cb = addConstraintForLoc(cb, ep@\loc, tr);
    return cb;
}

//
// Collect constraints for the transitive reflexive closure expression, e*
//
//      e : rel[t1,t2], comparable(t1,t2)
// -----------------------------------------
//      e * : rel[t1,t2]
//
public ConstraintBase gatherTransitiveReflexiveClosureExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression e) {
    < cb, ts > = makeFreshTypes(cb,2); t1 = ts[0]; t2 = ts[1];
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb,e@\loc), makeRelType([t1,t2]));
    cb.constraints = cb.constraints + Comparable(t1,t2,U(),e@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, makeRelType([t1,t2]));
    return cb;
}

//
// Collect constraints for the transitive closure expression, e+
//
//      e : rel[t1,t2], comparable(t1,t2)
// -----------------------------------------
//      e + : rel[t1,t2]
//
public ConstraintBase gatherTransitiveClosureExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression e) {
    < cb, ts > = makeFreshTypes(cb,2); t1 = ts[0]; t2 = ts[1];
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb,e@\loc), makeRelType([t1,t2]), ep@\loc);
    cb.constraints = cb.constraints + Comparable(t1,t2,U(),ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, makeRelType([t1,t2]));
    return cb;
}

//
// TODO: To properly check this, we need to keep a map of not just the annotation names and types,
// but of which types those annotations apply to!
//
// TODO: Add type rule!
//
public ConstraintBase gatherGetAnnotationExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression e, Name n) {
    // e is an expression of arbitrary type te
    te = typeForLoc(cb, e@\loc);
    
    // n must be an annotation on type te, and is of type at
    < cb, at > = makeFreshType(cb);
    cb.constraints = cb.constraints + AnnotationOf(n, te, at, n@\loc);
    
    // the overall type is the type of the annotation
    cb = addConstraintForLoc(cb, ep@\loc, at);

    return cb;
}

//
// TODO: To properly check this, we need to keep a map of not just the annotation names and types,
// but of which types those annotations apply to!
//
// TODO: Add type rule!
//
public ConstraintBase gatherSetAnnotationExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Name n, Expression er) {
    // el is an expression of arbitrary type tel
    tel = typeForLoc(cb, el@\loc);
    
    // n must be an annotation on type tel, and is of type at
    < cb, at > = makeFreshType(cb);
    cb.constraints = cb.constraints + AnnotationOf(n, tel, at, n@\loc);
    
    // er is an expression of arbitrary type ter
    ter = typeForLoc(cb, er@\loc);

    // assignment to the annotation -- assigning a value of type ter to
    // an annotation of type at on type tel yields a result of type rt
    < cb, rt > = makeFreshType(cb);
    cb.constraints = cb.constraints + AnnotationAssignable(ter,tel,at,rt,ep@\loc);
        
    // the overall type is the type of the annotation
    cb = addConstraintForLoc(cb, ep@\loc, at);

    return cb;
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
public ConstraintBase gatherCompositionExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    tel = typeForLoc(cb, el@\loc);
    ter = typeForLoc(cb, er@\loc);
    < cb, tr > = makeFreshType(cb);
    cb.constraints = cb.constraints + Composable(tel, ter, tr, ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, tr);
    return cb;
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
public ConstraintBase gatherBinaryExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, RBuiltInOp rop, Expression er) {
    tel = typeForLoc(cb, el@\loc);
    ter = typeForLoc(cb, er@\loc);
    < cb, tr > = makeFreshType(cb);
    cb.constraints = cb.constraints + BuiltInAppliable(rop, makeTupleType([ tel, ter ]), tr, ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, tr);
    return cb;
}

//
// Collect constraints for binary expressions
//
//      e1 : t1, e2 : t2, rop : t1 x t2 -> t3 defined, t4 given, t3 = t4
// --------------------------------------------------------------------------------------------
//                            e1 rop e2 : t4
//
public ConstraintBase gatherBinaryExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, RBuiltInOp rop, Expression er, RType resultType) {
    tel = typeForLoc(cb, el@\loc);
    ter = typeForLoc(cb, er@\loc);
    < cb, tr > = makeFreshType(cb);
    cb.constraints = cb.constraints + BuiltInAppliable(rop, makeTupleType([ tel, ter ]), tr, ep@\loc);
    cb.constraints = cb.constraints + ConstrainType(tr, resultType, ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, resultType);
    return cb;
}

//
// Collect constraints for the product expression e1 * e2
//
public ConstraintBase gatherProductExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, Product(), er);
}

//
// Collect constraints for the join expression e1 join e2
//
public ConstraintBase gatherJoinExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, Join(), er);
}

//
// Collect constraints for the div expression e1 / e2
//
public ConstraintBase gatherDivExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, Div(), er);
}

//
// Collect constraints for the mod expression e1 % e2
//
public ConstraintBase gatherModExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, Mod(), er);
}

//
// Collect constraints for the intersection expression e1 & e2
//
public ConstraintBase gatherIntersectionExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, Intersect(), er);
}

//
// Collect constraints for the plus expression e1 + e2
//
public ConstraintBase gatherPlusExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, Plus(), er);
}

//
// Collect constraints for the minus expression e1 - e2
//
public ConstraintBase gatherMinusExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, Minus(), er);
}

//
// Collect constraints for the notin expression e1 notin e2
//
public ConstraintBase gatherNotInExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, NotIn(), er, makeBoolType());
}

//
// Collect constraints for the in expression e1 in e2
//
public ConstraintBase gatherInExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, In(), er, makeBoolType());
}

//
// Collect constraints for the Less Than expression e1 < e2
//
public ConstraintBase gatherLessThanExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, Lt(), er, makeBoolType());
}

//
// Collect constraints for the Less Than or Equal expression e1 <= e2
//
public ConstraintBase gatherLessThanOrEqualExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, LtEq(), er, makeBoolType());
}

//
// Collect constraints for the Greater Than expression e1 > e2
//
public ConstraintBase gatherGreaterThanExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, Gt(), er, makeBoolType());
}

//
// Collect constraints for the Greater Than or Equal expression e1 >= e2
//
public ConstraintBase gatherGreaterThanOrEqualExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, GtEq(), er, makeBoolType());
}

//
// Collect constraints for the Equals expression e1 == e2
//
public ConstraintBase gatherEqualsExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, Eq(), er, makeBoolType());
}

//
// Collect constraints for the Not Equals expression e1 != e2
//
public ConstraintBase gatherNotEqualsExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    return gatherBinaryExpressionConstraints(st, cb, ep, el, NEq(), er, makeBoolType());
}

//
// Collect constraints for the ternary if expression eb ? et : ef
//
//      eb : bool, et : t1, ef : t2, t3 = lub(t1,t2)
// -----------------------------------------------------
//          eb ? et : ef  :  t3
//
public ConstraintBase gatherIfThenElseExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression eb, Expression et, Expression ef) {
    RType teb = typeForLoc(cb, eb@\loc);
    RType tet = typeForLoc(cb, et@\loc);
    RType tef = typeForLoc(cb, ef@\loc);
    < cb, rt > = makeFreshType(cb);
    cb.constraints = cb.constraints + LubOf([tet,tef], rt, ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, rt);
    return cb;
}

//
// Collect constraints for the if defined / otherwise expression e ? eo
//
//      ed : t1, eo : t2, t3 = lub(t1, t2)
// -----------------------------------------
//          ed ? eo  : t3
//
public ConstraintBase gatherIfDefinedOtherwiseExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression ed, Expression eo) {
    RType ted = typeForLoc(cb, ed@\loc);
    RType teo = typeForLoc(cb, eo@\loc);
    < cb, rt > = makeFreshType(cb);
    cb.constraints = cb.constraints + LubOf([ted,teo], rt, ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, rt);
    return cb;
}

//
// Collect constraints for the logical implication expression e ==> e'
//
//      el : bool, er : bool
// -----------------------------------------
//          el ==> er : bool
//
public ConstraintBase gatherImplicationExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, el@\loc), makeBoolType(), ep@\loc);
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, er@\loc), makeBoolType(), ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, makeBoolType());
    return cb;
}

//
// Collect constraints for the logical equivalence expression e <==> e'
//
//      el : bool, er : bool
// -----------------------------------------
//          el <==> er : bool
//
public ConstraintBase gatherEquivalenceExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, el@\loc), makeBoolType(), ep@\loc);
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, er@\loc), makeBoolType(), ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, makeBoolType());
    return cb;
}

//
// Collect constraints for the logical and expression e && e'
//
//      el : bool, er : bool
// -----------------------------------------
//          el && er : bool
//
public ConstraintBase gatherAndExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, el@\loc), makeBoolType(), ep@\loc);
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, er@\loc), makeBoolType(), ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, makeBoolType());
    return cb;
}

//
// Collect constraints for the logical or expression e || e'
//
//      el : bool, er : bool
// -----------------------------------------
//          el || er : bool
//
public ConstraintBase gatherOrExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression el, Expression er) {
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, el@\loc), makeBoolType(), ep@\loc);
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, er@\loc), makeBoolType(), ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, makeBoolType());
    return cb;
}

//
// Collect constraints for the match expression, p := e
//
//     e : t1, bindable(e,p)
// -----------------------------
//       p := e : bool
//
public ConstraintBase gatherMatchExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Pattern p, Expression e) {
    te = typeForLoc(cb, e@\loc);
    cb = gatherPatternConstraints(st, cb, p);
	tp = typeForLoc(cb, p@\loc);
    cb.constraints = cb.constraints + Bindable(tp, te, p, U(), ep@\loc);
    return addConstraintForLoc(cb, ep@\loc, makeBoolType());
}

//
// Collect constraints for the nomatch expression, p !:= e
//
//     e : t1, bindable(e,p)
// -----------------------------
//       p !:= e : bool
//
public ConstraintBase gatherNoMatchExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Pattern p, Expression e) {
    te = typeForLoc(cb, e@\loc);
    cb = gatherPatternConstraints(st, cb, p);
	tp = typeForLoc(cb, p@\loc);
    cb.constraints = cb.constraints + Bindable(tp, te, p, U(), ep@\loc);
    return addConstraintForLoc(cb, ep@\loc, makeBoolType());
}

//
// Enumerators act like a match, i.e., like :=, except for containers, like lists,
// sets, etc, where they "strip off" the outer layer of the subject. For instance,
// n <- 1 acts just like n := 1, while n <- [1..10] acts like [_*,n,_*] := [1..10].
//
//
public ConstraintBase gatherEnumeratorExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Pattern p, Expression e) {
    te = typeForLoc(cb, e@\loc);
    cb.constraints = cb.constraints + Enumerable(p,te,U(),p@\loc);
    return addConstraintForLoc(cb, ep@\loc, makeBoolType());
}

public ConstraintBase gatherSetComprehensionExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, {Expression ","}+ els, {Expression ","}+ ers) {
    // The elements of the set are each of some arbitrary type; determine the type of each
    // result, so we can compute the LUB to figure the actual type of the set
    list[RType] elements = [ makeVoidType() ]; 
    for (e <- els) { 
        et = typeForLoc(cb, e@\loc);

        // If this is an element that can be spliced, i.e. is not surrounded by set
        // brackets, indicate that in the type, so we can calculate the lub correctly
        if (spliceableExpression(e)) et = SpliceableElement(et);

        elements += et;
    }

    // The generators are all patterns that should be of type bool
    for (e <- ers) cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc), makeBoolType(), ep@\loc);
    
    // The set element type is the lub of the various elements
    < cb, rt > = makeFreshType(cb); 
    cb.constraints = cb.constraints + LubOfSet(elements,rt,ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, makeSetType(rt));

    return cb;
}

public ConstraintBase gatherListComprehensionExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, {Expression ","}+ els, {Expression ","}+ ers) {
    // The elements of the list are each of some arbitrary type; determine the type of each
    // result, so we can compute the LUB to figure the actual type of the list
    list[RType] elements = [ makeVoidType() ]; 
    for (e <- els) { 
        et = typeForLoc(cb, e@\loc);

        // If this is an element that can be spliced, i.e. is not surrounded by list
        // brackets, indicate that in the type, so we can calculate the lub correctly
        if (spliceableExpression(e)) et = SpliceableElement(et);

        elements += et;
    }

    // The generators are all patterns that should be of type bool
    for (e <- ers) cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc), makeBoolType(), ep@\loc);
    
    // The list element type is the lub of the various elements
    < cb, rt > = makeFreshType(cb); 
    cb.constraints = cb.constraints + LubOfList(elements,rt,ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, makeListType(rt));

    return cb;
}

public ConstraintBase gatherMapComprehensionExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression ef, Expression et, {Expression ","}+ ers) {
    RType eft = typeForLoc(cb, ef@\loc);
    RType ett = typeForLoc(cb, et@\loc);
    for (e <- ers) cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc), makeBoolType(), ep@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, makeMapType(eft, ett));
    return cb;
}

//
// NOTE: We cannot actually type this statically, since the type of the "it" expression is implicit and the type of
// the result is based only indirectly on the type of er. If we could type this, we could probably solve the halting
// problem ;)
//
public ConstraintBase gatherReducerExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, Expression ei, Expression er, {Expression ","}+ ers) {
    eit = typeForLoc(cb, ei@\loc);
    ert = typeForLoc(cb, er@\loc);
    for (e <- ers) cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc),makeBoolType(), ep@\loc);

    < cb, rt > = makeFreshType(cb);
    cb.constraints = cb.constraints + StepItType(er, eit, rt, ert, er@\loc);
    cb = addConstraintForLoc(cb, ep@\loc, rt);
     
    return cb;
}

//
// Collect constraints for the all expression
//
//      e1 : bool, ..., en : bool
// -----------------------------------------
//          all(e1...en) : bool
//
public ConstraintBase gatherAllExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, {Expression ","}+ ers) {
    // All the expressions inside the all construct must be of type bool    
    for (er <- ers)
        cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, er@\loc),makeBoolType(), ep@\loc);

    cb = addConstraintForLoc(cb, ep@\loc, makeBoolType());
    
    return cb;
}
        
//
// Collect constraints for the any expression
//
//      e1 : bool, ..., en : bool
// -----------------------------------------
//          any(e1...en) : bool
//
public ConstraintBase gatherAnyExpressionConstraints(STBuilder st, ConstraintBase cb, Expression ep, {Expression ","}+ ers) {
    // All the expressions inside the all construct must be of type bool    
    for (er <- ers)
        cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, er@\loc),makeBoolType(), ep@\loc);

    cb = addConstraintForLoc(cb, ep@\loc, makeBoolType());
    
    return cb;
}

//
// Collect constraints for the map expression
//
//      d1 : td1, r1 : tr1, ..., dn : tdn, rn : trn, td = lub(td1..tdn), tr = lub(tr1..trn)
// ----------------------------------------------------------------------------------------
//                       ( d1 : r1, ..., dn : rn ) : map[td,tr]
//
public ConstraintBase gatherMapExpressionConstraints(STBuilder st, ConstraintBase cb, Expression exp) {
    // Each element of the domain and range of the map can be of arbitrary type.
    list[RType] domains = [ makeVoidType() ]; list[RType] ranges = [ makeVoidType() ];
    list[tuple[Expression mapDomain, Expression mapRange]] mapContents = getMapExpressionContents(exp);
    for (<md,mr> <- mapContents) { 
        domains += typeForLoc(cb, md@\loc); 
        ranges += typeForLoc(cb, mr@\loc);
    }

    // The ultimate result of the expression is a map with a domain type equal to the lub of the
    // given domain types and a range type equal to the lub of the given range types.
    < cb, tdom > = makeFreshType(cb);
    cb.constraints = cb.constraints + LubOf(domains, tdom, exp@\loc);
    
    < cb, tran > = makeFreshType(cb);
    cb.constraints = cb.constraints + LubOf(ranges, tran, exp@\loc);
    
    cb = addConstraintForLoc(cb, exp@\loc, makeMapType(tdom,tran));

    return cb;
}

//
// Calculate the list of types assigned to a list of parameters. This only works for
// old-style parameters, which include type annotations.
//
// TODO: This needs to be updated to handle the new parameter dispatch style.
//
public list[RType] getParameterTypes(ConstraintBase cb, Parameters p) {
    list[RType] pTypes = [];

    if ((Parameters)`( <Formals f> )` := p && (Formals)`<{Pattern ","}* fs>` := f) {
        for ((Pattern)`<Type t> <Name n>` <- fs) {
                pTypes += typeForLoc(cb, n@\loc);
        }
    } else if ((Parameters)`( <Formals f> ... )` := p && (Formals)`<{Pattern ","}* fs>` := f) {
        for ((Pattern)`<Type t> <Name n>` <- fs) {
                pTypes += typeForLoc(cb, n@\loc);
        }
        // For varargs, mark the last parameter as the variable size parameter; if we have no
        // parameters, then we add one, a varargs which accepts anything
        if (size(pTypes) > 0)
            pTypes[size(pTypes)-1] = RListType(last(pTypes));
        else
            pTypes = [ RListType(RVoidType()) ];
    }

    return pTypes;
}
