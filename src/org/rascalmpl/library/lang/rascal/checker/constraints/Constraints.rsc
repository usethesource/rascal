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
module lang::rascal::checker::constraints::Constraints

import ParseTree;
import Message;
import  analysis::graphs::Graph;
import Node;
import Set;
import List;
import IO;

import constraints::Constraint;
import lang::rascal::types::Types;
import lang::rascal::types::Lubs;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::checker::TypeMatching;
import lang::rascal::syntax::RascalRascal;

//
// The ConstraintBase is the structure that holds all the information
// used by the constraint solver. This includes:
//
// * counter, used to provide a unique ID for inference vars and types
// * constraints, the set of constraints
// * inferredTypeMap, mapping inference vars to their actual types
// * messages, to store error messages generated during type checking
// * locTypes, which stores the type variable assigned to a given tree
//   location
//
// TODO: Note that this model does not work if we need to assign multiple
// types to the same location, say because we have a chain rule and we
// assign different types to each nonterminal. This works for Rascal, 
// though, so we will keep it. If need be, we can switch to a rel.
//
data ConstraintBase = ConstraintBase(
    int counter, 
    Constraints constraints,
    map[int,RType] inferredTypeMap,
    rel[loc, Message] messages,
    map[loc, RType] locTypes,
    rel[Constraint,str,Constraint] history
    );

public ConstraintBase makeNewConstraintBase() {
    return ConstraintBase(0, { }, ( ), { }, ( ), { });
}

public ConstraintBase makeNewConstraintBase(map[int,RType] inferenceVars, int counter) {
    return ConstraintBase(counter, { }, inferenceVars, { }, ( ), { });
}

public ConstraintBase addTypeForLoc(ConstraintBase cb, RType rt, loc l) {
    if (l in cb.locTypes && cb.locTypes[l] != rt) throw "Cannot set location type to a different value!";
    cb.locTypes[l] = rt;
    return cb;
}

public bool locHasType(ConstraintBase cb, loc l) {
    return l in cb.locTypes;
}

public RType typeForLoc(ConstraintBase cb, loc l) {
    if (l in cb.locTypes) return cb.locTypes[l];
    throw "Cannot look up type for location <l>";
}

public tuple[ConstraintBase,RType] addOrGetTypeForLoc(ConstraintBase cb, loc l) {
    if (l in cb.locTypes) return < cb, cb.locTypes[l] >;
    < cb, rt > = makeFreshType(cb);
    cb = addTypeForLoc(cb,rt,l);
    return < cb, rt >;
}

data ConstraintNode = TN(RType rt) | CN(Constraint c);

alias ConstraintGraph = Graph[ConstraintNode];

public ConstraintGraph generateConstraintGraph(ConstraintBase cb) {
	return generateConstraintGraph(cb.constraints); 
}
  
public ConstraintGraph generateConstraintGraph(Constraints cs) {
    ConstraintGraph cg = { };
    
    for (Constraint c <- cs) {
        // If a constraint depends on a type before it can be solved,
        // we represent this by adding an edge from the type to the
        // constraint. We consider the edges to be directed.
        for (ds <- dependsOn(c)) cg = cg + < TN(ds), CN(c) >;

        // If a constraint provides a (partial or complete) solution
        // for a type, we represent this be adding an edge from the
        // constraint to the type. We consider the edges to be directed.
        for (ps <- provides(c)) cg = cg + < CN(c), TN(ps) >;
    }

    return cg;    
}
  
 //
// Add a constraint onto the type for location l
//
public ConstraintBase addConstraintForLoc(ConstraintBase cb, loc l, RType rt) {
    < cb, lt > = addOrGetTypeForLoc(cb,l);
    cb.constraints = cb.constraints + ConstrainType(lt, rt, l);
    return cb;
}

//
// Add a type error with message msg at location l
//
public ConstraintBase addTypeError(ConstraintBase cb, loc l, str msg) {
    cb = cb.messages + < l, error(msg,l) >;
    return cb;
}

public ConstraintBase addTypeWarning(ConstraintBase stBuilder, loc l, str msg) {
    cb = cb.messages + < l, warning(msg,l) >;
    return cb;
}

// TODO: Should move this later
data RBuiltInOp =
      Negative()
    | Plus()
    | Minus()
    | NotIn()
    | In()
    | Lt()
    | LtEq()
    | Gt()
    | GtEq()
    | Eq()
    | NEq()
    | Intersect()
    | Product()
    | Join()
    | Div()
    | Mod()
    ;

data RAssignmentOp = 
      RADefault() 
    | RAAddition() 
    | RASubtraction() 
    | RAProduct() 
    | RADivision() 
    | RAIntersection() 
    | RAIfDefined() ;

public RAssignmentOp convertAssignmentOp(Assignment a) {
    switch(a) {
        case (Assignment)`=` : return RADefault();
        case (Assignment)`+=` : return RAAddition();
        case (Assignment)`-=` : return RASubtraction();
        case (Assignment)`*=` : return RAProduct();
        case (Assignment)`/=` : return RADivision();
        case (Assignment)`&=` : return RAIntersection();
        case (Assignment)`?=` : return RAIfDefined();
    }
}

public str prettyPrintAOp(RAssignmentOp a) {
    switch(a) {
        case RADefault() : return "=";
        case RAAddition() : return "+=";
        case RASubtraction() : return "-=";
        case RAProduct() : return "*=";
        case RADivision() : return "/=";
        case RAIntersection() : return "&=";
        case RAIfDefined() : return "?=";
    }
}

public bool aOpHasOp(RAssignmentOp a) {
    switch(a) {
        case RADefault() : return false;
        case RAIfDefined() : return false;
        default : return true;
    }
}

public RBuiltInOp opForAOp(RAssignmentOp a) {
    switch(a) {
        case RAAddition() : return Plus();
        case RASubtraction() : return Minus();
        case RAProduct() : return Product();
        case RADivision() : return Div();
        case RAIntersection() : return Intersect();
    }
}
    
//
// Constraints used for typing Rascal. We carry locations around so we can give useful 
// error messages, reporting back where a constraint was first raised.
//
// The constraints are:
//
// LubOf:           Indicates that the lub of a given list of types is another type. This unifies in the
//                  case where the list contains concrete types (non-inferred types) and the result type
//                  is either an inferred type or is the lub of the given types.
//
// LubOfList:       Identical to LubOf, but with handling for spliced types in the lub list. Specifically,
//                  a spliceable list is treated as having the type of its element.
// 
// LubOfSet:        Identical to LubOf, but with handling for spliced types in the lub list. Specifically,
//                  a spliceable set is treated as having the type of its element.
//
// BindableToCase:  Indicates that a given subject type is bindable to a case type, which includes
//                  both a case pattern and the branch result type. This is used to ensure that the
//                  case is valid, since the case should have a pattern which can possibly match
//                  the type of the subject (e.g., case true should not be used with a str subject).
//
// Assignable:      Indicates that the rvalue type should be assignable to the lvalue type, with
//                  a given result type as the result of the assignment (in cases where the result
//                  type would not be identical to either the lvalue or rvalue types). The variant
//                  given, without the result, is for situations where we don't care about the
//                  result, such as in variable declarations with assignments, where the declaration
//                  itself does not have a type.
//
// ComboAssignable:    The first type represents the name of an assignable. This constraint specifies
//                      that the name should be assignable and, if so, it will yield the result type.
//                      A name is assignable if it already has a type, and if it is something that can
//                      be assigned into as a unit with a combo assignment operator: a field or a bare
//                      name, for instance, but not a tuple, where tuple += value doesn't make any
//                      sense (i.e., the name must be an lvalue).
//    
// Returnable:      Indicates that a given type should be returnable with the given return for the
//                  current function.
//
// IsRuntimeException:  Indicates that the given type is equal to RuntimeException (this could be done
//                      with TypesAreEqual, but this is a special case and is more direct).
//
// BindsRuntimeException:   Indicates that the given pattern matches RuntimeException.
//
// CaseIsReachable: Indicates that the case type represents a case which is reachable through
//                  the visited expression. For instance, int is reachable from list[int] and
//                  from tuple[int,str], but bool is not.
//
// SubtypeOf:       Constrains one type to be a subtype of the second type.
//
// PWAResultType:   Constrains right to be the result of the Pattern With Action typing, left.
//
// IsReifiedType:   Constraints the result to be a reified type, formed as outer[params].
//
// CallOrTree:      Constrains source to be a "callable" type: constructor, function, string
//                  (for nodes), or location. The params are given, needed for overload resolution,
//                  with the result constrained by the chosen callable.
//
// FieldOf:         Constrains field t to be a proper field of type inType with resulting
//                  type fieldType.
//
// FieldAssignable: Constraints field fld to be accessible through tree lhs, with rhs, of type
//                  rvalue, assignable to the type of fld, lvalue.
//
// NamedFieldOf:    Constrains field t to be a proper field of type inType with resulting
//                  type fieldType, which should be a wrapper for a named field.
//
// IndexedFieldOf:  Constrains field t to be a proper field of type inType with resulting
//                  type fieldType, which should be a wrapper for a named field.
//
// FieldProjection: Constrains the result type to be the projection of the input type, based
//                  on the input type and the given fields
//
// Subscript:       Constrains the result type to be the subscript of the input type, based
//                  on the input type and the given fields
//
// Comparable:      Constrains the two types to be comparable, i.e., either t1 <: t2, or t2 <: t1
//
// AnnotationAssignable:    Constrains annotation ann to be accessible through tree lhs, with rhs, of type
//                          rvalue, assignable to the type of ann, lvalue.
//
// AnnOf:           Constrains annType to be the annotation type of annotation t, defined on 
//                  type inType.
//
// Composable:      Constraints left and right to be composable, with result type result
//
// Bindable:        Tree t is a pattern, and it can be bound to the subject type
//
// Enumerable:      Tree t is a pattern, and it can be bound to the subject type and enumerated over.
//
// StepItType:      The reducer will calculate a new type on each iteration (potentially); this constraint
//                  steps the type, constraining the result based on whether it changes at each iteration
//
// Failure:         Constrains a tree type to be a failure type, allows explicit indication of errors, versus
//                  just inferring them from a failed unification
//
data SolveResult = T() | F() | U() | W();

data Constraint =
      LubOf(list[RType] typesToLub, RType lubResult, loc at)
    | LubOfList(list[RType] typesToLub, RType lubResult, loc at)
    | LubOfSet(list[RType] typesToLub, RType lubResult, loc at)
    | BindableToCase(RType subject, RType caseType, SolveResult sr, loc at)
    | Assignable(RType lvalue, RType rvalue, RType result, loc at)
    | ComboAssignable(RType lvalue, RType rvalue, RType result, loc at)
    | Returnable(RType given, RType expected, SolveResult sr, loc at)
    | IsRuntimeException(RType expType, SolveResult sr, loc at)
    | BindsRuntimeException(Tree pat, SolveResult sr, loc at)
    | CaseIsReachable(RType caseType, RType expType, SolveResult sr, loc at)
    | SubtypeOf(RType left, RType right, SolveResult sr, loc at)
    | IsReifiedType(RType outer, list[RType] params, RType result, loc at)
    | CallOrTree(RType source, list[RType] params, RType result, loc at)
    | FieldOf(Tree t, RType inType, RType fieldType, loc at)
    | FieldAssignable(Tree fld, RType rvalue, RType lvalue, SolveResult sr, loc at)
    | NamedFieldOf(Tree t, RType inType, RType fieldType, loc at)
    | IndexedFieldOf(Tree t, RType inType, RType fieldType, loc at)
    | FieldProjection(RType inType, list[RType] fieldTypes, RType result, loc at) 
    | Subscript(RType inType, list[Tree] indices, list[RType] indexTypes, RType result, loc at) 
	| Comparable(RType left, RType right, SolveResult sr, loc at)
    | AnnotationAssignable(RType rvalue, RType lvalue, RType annType, RType result, loc at)
	| AnnotationOf(Tree t, RType inType, RType annType, loc at)
	| Composable(RType left, RType right, RType result, loc at)
    | BuiltInAppliable(RBuiltInOp op, RType domain, RType range, loc at)
    | Bindable(RType patType, RType subjectType, Pattern pat, SolveResult sr, loc at)
    | Enumerable(Tree pattern, RType subject, SolveResult sr, loc at)
	| StepItType(Tree reducer, RType inType, RType outType, RType result, loc at)
    | ConstrainType(RType constrainedType, RType typeConstraint, loc at)
    | PatternReachable(RType patType, RType descType, loc at)
    | BindIfInferred(RType nameType, RType patType, loc at)
    | BindBooleanLiteral(RType patType, RType subType, SolveResult sr, loc at)
    | BindIntegerLiteral(RType patType, RType subType, SolveResult sr, loc at)
    | BindRealLiteral(RType patType, RType subType, SolveResult sr, loc at)
    | BindStringLiteral(RType patType, RType subType, SolveResult sr, loc at)
    | BindLocationLiteral(RType patType, RType subType, SolveResult sr, loc at)
    | BindDateTimeLiteral(RType patType, RType subType, SolveResult sr, loc at)
    | BindRegExpLiteral(RType patType, RType subType, SolveResult sr, loc at)
    | BindPatternName(RType patType, RType subType, SolveResult sr, loc at)
    | BindDeepPattern(RType patType, RType childType, RType subType, SolveResult sr, loc at)
    // TODO: We don't really need the following three, but they would provide more info -- implement anyway?
    //| BindPatternAsName(RType patType, RType childType, RType subType, SolveResult sr, loc at)
    //| BindTypeGuard(RType patType, RType childType, RType guardType, RType subType, SolveResult sr, loc at)
    //| BindAntiPattern(RType patType, RType childType, RType subType, SolveResult sr, loc at)
    | BindListPattern(RType patType, RType subType, list[Pattern] childPatterns, SolveResult sr, loc at)
    | BindSetPattern(RType patType, RType subType, list[Pattern] childPatterns, SolveResult sr, loc at)
    | BindReifiedTypePattern(RType patType, list[RType] childTypes, RType subType, RType outerType, list[Pattern] childPatterns, SolveResult sr, loc at)
    | BindCallOrTreePattern(RType patType, RType headType, RType subType, Pattern headPat, list[Pattern] childPatterns, SolveResult sr, loc at)
    | BindTuplePattern(RType patType, RType subType, list[Pattern] childPatterns, SolveResult sr, loc at)
    | ConstraintBundle(Constraint triggerConstraint, Constraints toAdd)
    ;

public set[RType] getInferredTypes(RType rt) {
    set[RType] res = { };
    visit(rt) {
        case t:InferenceVar(_) : res = res + t;
        case t:RInferredType(_): res = res + t;
    }
    
    return res;
}

//
// Extracts the type variables (either added by the inferencer or during
// name resolution) that must be solved before this constraint can be
// solved.
//
public set[RType] dependsOn(LubOf(ttl,_,_)) = { getInferredTypes(t) | t <- ttl };
public set[RType] dependsOn(LubOfList(ttl,_,_)) = { getInferredTypes(t) | t <- ttl };
public set[RType] dependsOn(LubOfSet(ttl,_,_)) = { getInferredTypes(t) | t <- ttl };
public set[RType] dependsOn(BindableToCase(ts,tc,_,_)) = getInferredTypes(ts) + getInferredTypes(tc);
public set[RType] dependsOn(Assignable(l,r,_,_)) = getInferredTypes(l) + getInferredTypes(r);
public set[RType] dependsOn(ComboAssignable(l,r,_,_)) = getInferredTypes(l) + getInferredTypes(r);
public set[RType] dependsOn(Returnable(g,e,_,_)) = getInferredTypes(g) + getInferredTypes(e);
public set[RType] dependsOn(IsRuntimeException(e,_,_)) = getInferredTypes(e);
public set[RType] dependsOn(BindsRuntimeException(_,_,_)) = { };
public set[RType] dependsOn(CaseIsReachable(ct,et,_,_)) = getInferredTypes(ct) + getInferredTypes(et);
public set[RType] dependsOn(SubtypeOf(lt,rt,_,_)) = getInferredTypes(lt) + getInferredTypes(rt);
public set[RType] dependsOn(IsReifiedType(ot,pts,_,_)) = getInferredTypes(ot) + { getInferredTypes(t) | t <- pts };
public set[RType] dependsOn(CallOrTree(st,pts,_,_)) = getInferredTypes(st) + { getInferredTypes(t) | t <- pts };
public set[RType] dependsOn(FieldOf(_,t,_,_)) = getInferredTypes(t);
public set[RType] dependsOn(FieldAssignable(_,rv,lv,_,_)) = getInferredTypes(rv) + getInferredTypes(lv);
public set[RType] dependsOn(NamedFieldOf(_,t,_,_)) = getInferredTypes(t);
public set[RType] dependsOn(IndexedFieldOf(_,t,_,_)) = getInferredTypes(t);
public set[RType] dependsOn(FieldProjection(t,fts,_,_)) = getInferredTypes(t) + { getInferredTypes(ft) | ft <- fts };
public set[RType] dependsOn(Subscript(t,_,fts,_,_)) = getInferredTypes(t) + { getInferredTypes(ft) | ft <- fts };
public set[RType] dependsOn(Comparable(lt,rt,_,_)) = getInferredTypes(lt) + getInferredTypes(rt);
public set[RType] dependsOn(AnnotationAssignable(rv,lv,at,_,_)) = getInferredTypes(rv) + getInferredTypes(lv) + getInferredTypes(at);
public set[RType] dependsOn(AnnotationOf(_,t,_,_)) = getInferredTypes(t);
public set[RType] dependsOn(Composable(lt,rt,_,_)) = getInferredTypes(lt) + getInferredTypes(rt);
public set[RType] dependsOn(BuiltInAppliable(_,d,_,_)) = getInferredTypes(d);
public set[RType] dependsOn(Bindable(pt,st,_,_,_)) = getInferredTypes(pt) + getInferredTypes(st);
public set[RType] dependsOn(Enumerable(_,t,_,_)) = getInferredTypes(t);
public set[RType] dependsOn(StepItType(_,inT,outT,_,_)) = getInferredTypes(inT) + getInferredTypes(outT);
public set[RType] dependsOn(ConstrainType(_,tc,_)) = getInferredTypes(tc);
public set[RType] dependsOn(BindBooleanLiteral(pt,st,_,_)) = getInferredTypes(pt) + getInferredTypes(st);
public set[RType] dependsOn(BindIntegerLiteral(pt,st,_,_)) = getInferredTypes(pt) + getInferredTypes(st);
public set[RType] dependsOn(BindRealLiteral(pt,st,_,_)) = getInferredTypes(pt) + getInferredTypes(st);
public set[RType] dependsOn(BindStringLiteral(pt,st,_,_)) = getInferredTypes(pt) + getInferredTypes(st);
public set[RType] dependsOn(BindLocationLiteral(pt,st,_,_)) = getInferredTypes(pt) + getInferredTypes(st);
public set[RType] dependsOn(BindDateTimeLiteral(pt,st,_,_)) = getInferredTypes(pt) + getInferredTypes(st);
public set[RType] dependsOn(BindRegExpLiteral(pt,st,_,_)) = getInferredTypes(pt) + getInferredTypes(st);
public set[RType] dependsOn(BindPatternName(pt,st,_,_)) = getInferredTypes(pt) + getInferredTypes(st);
public set[RType] dependsOn(BindDeepPattern(pt,ct,st,_,_)) = getInferredTypes(pt) + getInferredTypes(st) + getInferredTypes(ct);
public set[RType] dependsOn(BindListPattern(pt,st,_,_,_)) = getInferredTypes(pt) + getInferredTypes(st);
public set[RType] dependsOn(BindSetPattern(pt,st,_,_,_)) = getInferredTypes(pt) + getInferredTypes(st);
public set[RType] dependsOn(BindReifiedTypePattern(pt,cts,st,ot,_,_,_)) = getInferredTypes(pt) + getInferredTypes(st) + getInferredTypes(ot) + { getInferredTypes(ctsi) | ctsi <- cts };
public set[RType] dependsOn(BindCallOrTreePattern(pt,ht,st,_,_,_,_)) = getInferredTypes(pt) + getInferredTypes(st) + getInferredTypes(ht);
public set[RType] dependsOn(BindTuplePattern(pt,st,_,_,_)) = getInferredTypes(pt) + getInferredTypes(st);

public default set[RType] dependsOn(Constraint c) { throw "Unimplemented: <c>"; }

//
// Extracts the type variables that this constraint provides the
// solution for.
//
public set[RType] provides(LubOf(_,lr,_)) = getInferredTypes(lr);
public set[RType] provides(LubOfList(_,lr,_)) = getInferredTypes(lr);
public set[RType] provides(LubOfSet(_,lr,_)) = getInferredTypes(lr);
public set[RType] provides(BindableToCase(_,_,_,_)) = { };
public set[RType] provides(Assignable(lt,_,rt,_)) = getInferredTypes(lt) + getInferredTypes(rt);
public set[RType] provides(ComboAssignable(_,_,rt,_)) = getInferredTypes(rt);
public set[RType] provides(Returnable(_,_,_,_)) = { };
public set[RType] provides(IsRuntimeException(_,_,_)) = { };
public set[RType] provides(BindsRuntimeException(_,_,_)) = { };
public set[RType] provides(CaseIsReachable(_,_,_,_)) = { };
public set[RType] provides(SubtypeOf(_,_,_,_)) = { };
public set[RType] provides(IsReifiedType(_,_,rt,_)) = getInferredTypes(rt);
public set[RType] provides(CallOrTree(_,_,rt,_)) = getInferredTypes(rt);
public set[RType] provides(FieldOf(_,_,ft,_)) = getInferredTypes(ft);
public set[RType] provides(FieldAssignable(_,_,_,_,_)) = { };
public set[RType] provides(NamedFieldOf(_,_,ft,_)) = getInferredTypes(ft);
public set[RType] provides(IndexedFieldOf(_,_,ft,_)) = getInferredTypes(ft);
public set[RType] provides(FieldProjection(_,_,rt,_)) = getInferredTypes(rt);
public set[RType] provides(Subscript(_,_,_,rt,_)) = getInferredTypes(rt);
public set[RType] provides(Comparable(_,_,_,_)) = { };
public set[RType] provides(AnnotationAssignable(_,_,_,rt,_)) = getInferredTypes(rt);
public set[RType] provides(AnnotationOf(_,_,rt,_)) = getInferredTypes(rt);
public set[RType] provides(Composable(_,_,rt,_)) = getInferredTypes(rt);
public set[RType] provides(BuiltInAppliable(_,_,r,_)) = getInferredTypes(r);
public set[RType] provides(Bindable(pt,_,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(Enumerable(_,_,_,_)) = { };
public set[RType] provides(StepItType(_,_,_,rt,_)) = getInferredTypes(rt);
public set[RType] provides(ConstrainType(ct,_,_)) = getInferredTypes(ct);
public set[RType] provides(BindBooleanLiteral(pt,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(BindIntegerLiteral(pt,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(BindRealLiteral(pt,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(BindStringLiteral(pt,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(BindLocationLiteral(pt,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(BindDateTimeLiteral(pt,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(BindRegExpLiteral(pt,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(BindPatternName(pt,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(BindDeepPattern(pt,_,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(BindListPattern(pt,_,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(BindSetPattern(pt,_,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(BindReifiedTypePattern(pt,_,_,_,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(BindCallOrTreePattern(pt,_,_,_,_,_,_)) = getInferredTypes(pt);
public set[RType] provides(BindTuplePattern(pt,_,_,_,_)) = getInferredTypes(pt);
public default set[RType]  provides(Constraint c) { throw "Unimplemented: <c>"; }

//
// Do we have enough information to solve this constraint? We can figure this
// using dependsOn, which gives us the inference types this constraint depends
// on. If it depends on no inference types, we can solve it.
//
public bool hasFailures(Constraint c) {
    return size({ ft | /ft:RFailType(_) <- c }) > 0;
}

public set[RType] getFailures(Constraint c) {
    return { ft | /ft:RFailType(_) <- c };
}

public bool hasFailures(RType rt) {
    return isFailType(rt) || (size({ ft | /ft:RFailType(_) <- rt }) > 0);
}

public set[RType] getFailures(RType rt) {
    return { ft | /ft:RFailType(_) <- rt } + (isFailType(rt) ? { rt } : { });
}

public bool solvable(ConstrainType(tl,tr,_)) = !hasFailures(tl) && !hasFailures(tr) && unifyTypes(tl,tr)[1];
public bool solvable(Assignable(tl,tr,_,_)) = !hasFailures(tl) && !hasFailures(tr) && size(getInferredTypes(tr)) == 0;
// Bindable is solvable if we have a concrete subject; some of the dependencies may also be solved during the bind,
// so all the dependencies are also provides, and we cannot wait for them to have actual types before solving.
public bool solvable(Bindable(_,sub,_,sr,_)) = size(getInferredTypes(sub)) == 0 && U() := sr;
public bool solvable(BindBooleanLiteral(pt,st,sr,_)) = size(getInferredTypes(st)) == 0 && size(getInferredTypes(pt)) == 0 && U() := sr;
public bool solvable(BindIntegerLiteral(pt,st,sr,_)) = size(getInferredTypes(st)) == 0 && size(getInferredTypes(pt)) == 0 && U() := sr;
public bool solvable(BindRealLiteral(pt,st,sr,_)) = size(getInferredTypes(st)) == 0 && size(getInferredTypes(pt)) == 0 && U() := sr;
public bool solvable(BindStringLiteral(pt,st,sr,_)) = size(getInferredTypes(st)) == 0 && size(getInferredTypes(pt)) == 0 && U() := sr;
public bool solvable(BindLocationLiteral(pt,st,sr,_)) = size(getInferredTypes(st)) == 0 && size(getInferredTypes(pt)) == 0 && U() := sr;
public bool solvable(BindDateTimeLiteral(pt,st,sr,_)) = size(getInferredTypes(st)) == 0 && size(getInferredTypes(pt)) == 0 && U() := sr;
public bool solvable(BindRegExpLiteral(pt,st,sr,_)) = size(getInferredTypes(st)) == 0 && size(getInferredTypes(pt)) == 0 && U() := sr;
public bool solvable(BindPatternName(pt,st,sr,_)) = size(getInferredTypes(st)) == 0 && U() := sr;
public bool solvable(BindDeepPattern(_,ct,st,sr,_)) = size(getInferredTypes(st)) == 0 && U() := sr;
public bool solvable(BindListPattern(_,st,_,U(),_)) = size(getInferredTypes(st)) == 0;
public bool solvable(BindListPattern(pt,st,_,W(),_)) = size(getInferredTypes(st)) == 0 && size(getInferredTypes(pt)) == 0;
public bool solvable(BindSetPattern(_,st,_,U(),_)) = size(getInferredTypes(st)) == 0;
public bool solvable(BindSetPattern(pt,st,_,W(),_)) = size(getInferredTypes(st)) == 0 && size(getInferredTypes(pt)) == 0;
public bool solvable(BindReifiedTypePattern(_,cts,st,ot,_,sr,_)) = size(getInferredTypes(st) + getInferredTypes(ot)) == 0 && U() := sr; // TODO: Handle cts as well
public bool solvable(BindCallOrTreePattern(_,ht,st,_,_,sr,_)) = size(getInferredTypes(ht)) == 0 && size(getInferredTypes(st)) == 0 && U() := sr;
public bool solvable(BindTuplePattern(_,st,_,U(),_)) = size(getInferredTypes(st)) == 0;
public bool solvable(BindTuplePattern(pt,st,_,W(),_)) = size(getInferredTypes(st)) == 0 && size(getInferredTypes(pt)) == 0;
public default bool solvable(Constraint c) = size(dependsOn(c)) == 0 && !hasFailures(c);

//
// Is this constraint solved? i.e., are all inference types now resolved?
//
public bool solved(BindableToCase(_,_,SolveResult sr,_)) = sr := T() || sr := F();
public bool solved(Returnable(_,_,SolveResult sr,_)) = sr := T() || sr := F();
public bool solved(IsRuntimeException(_,SolveResult sr,_)) = sr := T() || sr := F();
public bool solved(BindsRuntimeException(_,SolveResult sr,_)) = sr := T() || sr := F();
public bool solved(CaseIsReachable(_,_,SolveResult sr,_)) = sr := T() || sr := F();
public bool solved(SubtypeOf(_,_,SolveResult sr,_)) = sr := T() || sr := F();
public bool solved(FieldAssignable(_,_,_,SolveResult sr,_)) = sr := T() || sr := F();
public bool solved(Comparable(_,_,SolveResult sr,_)) = sr := T() || sr := F();
public bool solved(Enumerable(_,_,SolveResult sr,_)) = sr := T() || sr := F();
public bool solved(Assignable(_,_,RFailType(_),_)) = true;
public bool solved(ConstrainType(_,RFailType(_),_)) = true;
public bool solved(ConstrainType(RType rt, rt, _)) = true;
public bool solved(Bindable(_,_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindBooleanLiteral(_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindIntegerLiteral(_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindRealLiteral(_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindStringLiteral(_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindLocationLiteral(_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindDateTimeLiteral(_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindRegExpLiteral(_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindPatternName(_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindDeepPattern(_,_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindListPattern(_,_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindSetPattern(_,_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindReifiedTypePattern(_,_,_,_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindCallOrTreePattern(_,_,_,_,_,sr,_)) = sr := T() || sr := F();
public bool solved(BindTuplePattern(_,_,_,sr,_)) = sr := T() || sr := F();
public default bool solved(Constraint c) = size(provides(c) + dependsOn(c)) == 0;

//
// Given two constraints, return a mapping of type variables from the original
// constraint to the provided type values in the modified constraint.
//
public tuple[rel[RType,RType],bool] mappings(LubOf(_,lt,_), LubOf(_,rt,_)) = unifyTypes(lt,rt); 
public tuple[rel[RType,RType],bool] mappings(LubOfList(_,lt,_), LubOfList(_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(LubOfSet(_,lt,_), LubOfSet(_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindableToCase(_,_,_,_),BindableToCase(_,_,_,_)) = < { }, true >;
public tuple[rel[RType,RType],bool] mappings(ComboAssignable(_,_,lt,_),ComboAssignable(_,_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(Returnable(_,_,_,_),Returnable(_,_,_,_)) = < { }, true >;
public tuple[rel[RType,RType],bool] mappings(IsRuntimeException(_,_,_),IsRuntimeException(_,_,_)) = < { }, true >;
public tuple[rel[RType,RType],bool] mappings(BindsRuntimeException(_,_,_),BindsRuntimeException(_,_,_)) = < { }, true >;
public tuple[rel[RType,RType],bool] mappings(CaseIsReachable(_,_,_,_),CaseIsReachable(_,_,_,_)) = < { }, true >;
public tuple[rel[RType,RType],bool] mappings(SubtypeOf(_,_,_,_),SubtypeOf(_,_,_,_)) = < { }, true >;
public tuple[rel[RType,RType],bool] mappings(IsReifiedType(_,_,lt,_),IsReifiedType(_,_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(CallOrTree(_,_,lt,_),CallOrTree(_,_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(FieldOf(_,_,lt,_),FieldOf(_,_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(FieldAssignable(_,_,_,_,_),FieldAssignable(_,_,_,_,_)) = < { }, true >;
public tuple[rel[RType,RType],bool] mappings(NamedFieldOf(_,_,lt,_),NamedFieldOf(_,_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(IndexedFieldOf(_,_,lt,_),IndexedFieldOf(_,_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(FieldProjection(_,_,lt,_),FieldProjection(_,_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(Subscript(_,_,_,lt,_),Subscript(_,_,_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(Comparable(_,_,_,_),Comparable(_,_,_,_)) = < { }, true >;
public tuple[rel[RType,RType],bool] mappings(AnnotationAssignable(_,_,_,lt,_),AnnotationAssignable(_,_,_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(AnnotationOf(_,_,lt,_),AnnotationOf(_,_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(Composable(_,_,lt,_),Composable(_,_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BuiltInAppliable(_,_,lt,_),BuiltInAppliable(_,_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(Enumerable(_,_,_,_),Enumerable(_,_,_,_)) = < { }, true >;
public tuple[rel[RType,RType],bool] mappings(StepItType(_,_,_,lt,_),StepItType(_,_,_,rt,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(ConstrainType(lt,_,_),ConstrainType(rt,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(Bindable(lt,_,_,_,_),Bindable(rt,_,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindBooleanLiteral(lt,_,_,_),BindBooleanLiteral(rt,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindIntegerLiteral(lt,_,_,_),BindIntegerLiteral(rt,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindRealLiteral(lt,_,_,_),BindRealLiteral(rt,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindStringLiteral(lt,_,_,_),BindStringLiteral(rt,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindLocationLiteral(lt,_,_,_),BindLocationLiteral(rt,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindDateTimeLiteral(lt,_,_,_),BindDateTimeLiteral(rt,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindRegExpLiteral(lt,_,_,_),BindRegExpLiteral(rt,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindPatternName(lt,_,_,_),BindPatternName(rt,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindDeepPattern(lt,_,_,_,_),BindDeepPattern(rt,_,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindListPattern(lt,_,_,_,_),BindListPattern(rt,_,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindSetPattern(lt,_,_,_,_),BindSetPattern(rt,_,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindReifiedTypePattern(lt,_,_,_,_,_,_),BindReifiedTypePattern(rt,_,_,_,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindCallOrTreePattern(lt,_,_,_,_,_,_),BindCallOrTreePattern(rt,_,_,_,_,_,_)) = unifyTypes(lt,rt);
public tuple[rel[RType,RType],bool] mappings(BindTuplePattern(lt,_,_,_,_),BindTuplePattern(rt,_,_,_,_)) = unifyTypes(lt,rt);

public tuple[rel[RType,RType],bool] mappings(Assignable(lt1,_,rt1,_),Assignable(lt2,_,rt2,_)) {
     < r1, b1 > = unifyTypes(lt1,lt2);
     < r2, b2 > = unifyTypes(rt1,rt2);
     return < r1 + r2, b1 && b2 > ;
}

public default tuple[rel[RType,RType],bool] mappings(Constraint cl, Constraint cr) { throw "mappings unimplemented for constraint pair: <cl>, <cr>"; }

//
// Given a set of bindings, instantiate all the type variables in the constraint
//
public Constraint instantiate(Constraint c, rel[RType from, RType to] fromTo) {
    for ( tf <- fromTo<0> ) {
        if (size(fromTo[tf]) > 1) {
            if (size({ tt | tt <- fromTo[tf], isInferenceType(tt)}) > 0) throw "Should not instantiate types with inference types.";
            set[RType] toLub = fromTo[tf];
            set[RType] failures = { tt | tt <- toLub, isFailType(tt) };
            if (size(failures) > 0)
                c = instantiate(c, tf, collapseFailTypes(failures));
            else
                c = instantiate(c, tf, lubSet(toLub));
        } else {
            c = instantiate(c, tf, getOneFrom(fromTo[tf]));
        }
    }
    return c;
}

public RType instantiate(RType rt, rel[RType from, RType to] fromTo) {
    for ( tf <- fromTo<0> ) {
        if (size(fromTo[tf]) > 1) {
            if (size({ tt | tt <- fromTo[tf], isInferenceType(tt)}) > 0) throw "Should not instantiate types with inference types.";
            set[RType] toLub = fromTo[tf];
            set[RType] failures = { tt | tt <- toLub, isFailType(tt) };
            if (size(failures) > 0)
                rt = instantiateInferenceVar(rt, tf, collapseFailTypes(failures));
            else
                rt = instantiateInferenceVar(rt, tf, lubSet(toLub));
        } else {
            rt = instantiateInferenceVar(rt, tf, getOneFrom(fromTo[tf]));
        }
    }
    return rt;
}

public Constraint instantiate(Constraint c, RType from, RType to) = instantiateInferenceVar(c, from, to);

data RType =
      InferenceVar(int tnum)
    | TupleProjection(list[RType] argTypes)
    | SingleProjection(RType resType)
    | RelationProjection(list[RType] argTypes)
    | SetProjection(RType resType)
    | CaseType(RType caseType)
    | DefaultCaseType()
    | TypeWithName(RNamedType typeWithName)
    | SpliceableElement(RType rt)
    | ReplacementType(Tree pattern, RType replacementType)
    | NoReplacementType(Tree pattern)
    ;

public tuple[ConstraintBase,list[RType]] makeFreshTypes(ConstraintBase cs, int n) {
    list[RType] ftlist = [InferenceVar(c) | c <- [cs.counter .. (cs.counter+n-1)] ];
    for (ivt:InferenceVar(c) <- ftlist) cs.inferredTypeMap[c] = ivt; 
    cs.counter = cs.counter + n;
    return < cs, ftlist >;
}

public tuple[ConstraintBase,RType] makeFreshType(ConstraintBase cs) {
    RType freshType = InferenceVar(cs.counter);
    cs.inferredTypeMap[cs.counter] = freshType;
    cs.counter = cs.counter + 1;
    return < cs, freshType >;
}

//
// Given a specific inference var (infv) and a type to assign to this
// var (conc), replace infv by conc wherever it appears in types inside
// the constraint c.
//
public Constraint instantiateInferenceVar(Constraint c, RType infv, RType conc) {
    if (arity(c) == 0) return c;
    for (idx <- [0..arity(c)-1]) {
        if (RType rt := c[idx]) c[idx] = instantiateInferenceVar(rt,infv,conc);
        // TODO: The following is a workaround for bug 1153. Once that is fixed
        // we no longer need to break the test up in this manner.
        //if (list[RType] rtl := c[idx], size(rtl) > 0, RType _ := rtl[0]) {
        if (list[node] rtln := c[idx], size(rtln) > 0, RType _ := rtln[0], list[RType] rtl := c[idx]) {
        	//println("Invoking with <c[idx]>");
            c[idx] = [ instantiateInferenceVar(rt,infv,conc) | RType rt <- rtl ];
        } 
    }
    return c;        
}

public Constraint instantiateInferenceVar(Tree t, RType t1, RType t2) {
    throw "Unexpected call, passed tree <t>, type <t1>, and type <t2>";
}

//
// Same as above, but instead of operating over a constraint this operates
// over a type, which may contain the inference var inside (the element of a list
// type, the return type of a function type, etc).
//
// NOTE: We always bubble failures up to the top of the type, so we never
// return something like list[fail].
//
public RType instantiateInferenceVar(RType rt, RType infv, RType conc) {
    rt2 =  visit(rt) { case infv => conc };
    if (hasFailures(rt2)) return collapseFailTypes(getFailures(rt2));
    return rt2; 
}

//
// Using the information we have already gathered, so through the type and
// replace all uses of inference vars with what they are mapped to (if a mapping
// has been recorded).
//
public Constraint instantiateAllInferenceVars(STBuilder st, ConstraintBase cb, Constraint c) {
    if (arity(c) == 0) return c;
    for (idx <- [0..arity(c)-1]) {
        if (RType rt := c[idx]) c[idx] = instantiateAllInferenceVars(st,cb,rt);
    }
    return c;        
}

//
// Same as above, but for a specific type, not a constraint
//
public RType instantiateAllInferenceVars(STBuilder st, ConstraintBase cb, RType rt) {
    // we use innermost here, since we could replace a type with another type that also
    // has inferred variables that must be replaced...
    return innermost visit(rt) { 
        case InferenceVar(vnum) : if (vnum in cb.inferredTypeMap) insert(cb.inferredTypeMap[vnum]);
        case RInferredType(vnum) : if (vnum in cb.inferredTypeMap) insert(cb.inferredTypeMap[vnum]);
    }
    return rt;
}

//
// A type is an inference type if it contains any uninstantiated inference
// type variables.
//
// TODO: Should we merge the inference type vars from the name resolution
// phase in with the ones we track here? That may simplify things a bit, at
// least by removing some redundancy...
//
public bool isInferenceType(STBuilder st, ConstraintBase cb, RType rt) {
    visit(rt) {
        case InferenceVar(n) : {
            if (n notin cb.inferredTypeMap) return true;
            if (cb.inferredTypeMap[n] == rt) return true;
            return isInferenceType(st, cb, cb.inferredTypeMap[n]);
        }
        
        case RInferredType(n) : {
            if (n notin cb.inferredTypeMap) return true;
            if (cb.inferredTypeMap[n] == rt) return true;
            return isInferenceType(st, cb, cb.inferredTypeMap[n]);
        } 
    }
    
    return false;
} 

public bool isInferenceType(RType rt) {
    visit(rt) {
        case InferenceVar(n) : return true;
        case RInferredType(n) : return true;
    }
    
    return false;
} 

public bool containsFailTypes(STBuilder st, ConstraintBase cb, RType rt) {
    visit(rt) {
        case RFailType(_) : return true; 
    }
    
    return false;
}

//
// A type is concrete if it contains no inference vars, i.e., if it is not an inference type
//
public bool isConcreteType(STBuilder st, ConstraintBase cb, RType rt) = !isInferenceType(st,cb,rt);

//
// A type is a standard Rascal type if it is one of the types expressible in Rascal syntax,
// versus one of the new types added by the checker.
//
public bool isRegularType(STBuilder st, ConstraintBase cb, RType rt) {
    visit(rt) {
        case InferenceVar(n) : {
            if (n notin cb.inferredTypeMap) return false;
            return isRegularType(st, cb, cb.inferredTypeMap[n]);
        }
        case RInferredType(_) : return false;
        case TupleProjection(_) : return false;
        case SingleProjection(_) : return false;
        case RelationProjection(_) : return false;
        case SetProjection(_) : return false;
        case CaseType(_,_) : return false;
        case DefaultCaseType(_) : return false;
        case AssignableType(_) : return false;
        case TypeWithName(_) : return false;
        case SpliceableElement(_) : return false;
        case ReplacementType(_,_) : return false;
        case NoReplacementType(_,_) : return false;
        case RFailType(_) : return false;
        case RStatementType(_) : return false;                
    }
    return true;
}
