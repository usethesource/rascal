@bootstrapParser
module rascal::checker::constraints::Constraints

import ParseTree;
import constraints::Constraint;
import rascal::types::Types;
import rascal::scoping::SymbolTable;
import rascal::syntax::RascalRascal;

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

private map[RBuiltInOp,str] opStr = ( Negative() : "-", Plus() : "+", Minus() : "-", NotIn() : "notin", In() : "in",
                                      Lt() : "\<", LtEq() : "\<=", Gt() : "\>", GtEq() : "\>=", Eq() : "==", NEq() : "!=",
                                      Intersect() : "&", Product() : "*", Join() : "join", Div() : "/", Mod() : "%" ); 


public str prettyPrintBuiltInOp(RBuiltInOp bop) {
    if (bop in opStr) return opStr[bop];
    throw "Operator <bop> is not in the operator/string map";
}

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
// TreeIsType:      Indicates that a given tree should have the specified type.
//
// TypesAreEqual:   Indicates that two types should be equal. A typical example would be t1 = int,
//                  which could then be used to unify other instances of t1 with int in the other
//                  constraints.
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
// IsNameAssignable:    The first type represents the name of an assignable. This constraint specifies
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
// DefinedBy:       Constraints the given type to that specified by the given scope ids
//
data Constraint =
      TreeIsType(Tree t, loc at, RType treeType)
    | LocIsType(loc at, RType locType)
    | TypesAreEqual(RType left, RType right, loc at) 
    | LubOf(list[RType] typesToLub, RType lubResult, loc at)
    | LubOfList(list[RType] typesToLub, RType lubResult, loc at)
    | LubOfSet(list[RType] typesToLub, RType lubResult, loc at)
    | BindableToCase(RType subject, RType caseType, loc at)
    | Assignable(Tree parent, loc at, Tree lhs, Tree rhs, RType rvalue, RType lvalue, RType result)
    | Assignable(Tree parent, loc at, Tree lhs, Tree rhs, RType rvalue, RType lvalue)
    | IsNameAssignable(RType assignableType, RType resultType, loc at)
    | Returnable(Tree parent, loc at, Tree returned, RType given, RType expected)
    | IsRuntimeException(RType expType, loc at)
    | BindsRuntimeException(Tree pat, loc at)
    | CaseIsReachable(RType caseType, RType expType, loc at)
    | SubtypeOf(RType left, RType right, loc at)
    | PWAResultType(RType left, RType right, loc at)
    | IsReifiedType(RType outer, list[RType] params, loc at, RType result)
    | CallOrTree(RType source, list[RType] params, RType result, loc at)
    | FieldOf(Tree t, RType inType, RType fieldType, loc at)
    | FieldAssignable(Tree parent, loc at, Tree lhs, Tree fld, Tree rhs, RType rvalue, RType lvalue)
    | NamedFieldOf(Tree t, RType inType, RType fieldType, loc at)
    | IndexedFieldOf(Tree t, RType inType, RType fieldType, loc at)
    | FieldProjection(RType inType, list[RType] fieldTypes, RType result, loc at) 
    | Subscript(RType inType, list[Tree] indices, list[RType] indexTypes, RType result, loc at) 
	| Comparable(RType left, RType right, loc at)
    | AnnotationAssignable(Tree parent, loc at, Tree lhs, Tree ann, Tree rhs, RType rvalue, RType lvalue)
	| AnnotationOf(Tree t, RType inType, RType annType, loc at)
	| Composable(RType left, RType right, RType result, loc at)
    | BuiltInAppliable(RBuiltInOp op, RType domain, RType range, loc at)
    | Bindable(Tree pattern, RType subject, loc at)
    | Enumerable(Tree pattern, RType subject, loc at)
	| StepItType(Tree reducer, loc at, RType inType, RType outType, RType result)
    | Failure(Tree t, loc at, RType failureType)
    | DefinedBy(RType lvalue, set[STItemId] definingIds, loc at)
    ;

// The location in the tree at which this constraint is registered (NOT CURRENTY USED).
anno loc Constraint@at;

// Constraints gathered at this node of the tree (NOT CURRENTLY USED).
anno set[Constraint] Tree@constraints;

public str prettyPrintConstraint(Constraint c) {
    switch(c) {
        case TreeIsType(t,at,rt) : return "<t> : <rt> at <at>";
        
        case TypesAreEqual(l,r,at) : return "<l> = <r>";
        
        case BuiltInAppliable(op,d,r,at) : return "<op> : <d> -\> <r> at <at>";
        
        default : return "<c>";    
    }
}

data ConstraintBase = 
      ConstraintBase(int freshCounter, 
                     Constraints constraints,
                     Constraints treeTypes,
                     map[int,RType] inferenceVarMap);

data RType =
      InferenceVar(int tnum)
    | TupleProjection(list[RType] argTypes)
    | SingleProjection(RType resType)
    | RelationProjection(list[RType] argTypes)
    | SetProjection(RType resType)
    | CaseType(RType replacementPattern, RType caseType)
    | DefaultCaseType(RType caseType)
    | AssignableType(Tree assignablePattern)
    | TypeWithName(RNamedType typeWithName)
    | SpliceableElement(RType rt)
    | ReplacementType(Tree pattern, RType replacementType)
    | NoReplacementType(Tree pattern, RType resultType)
    ;
    
public tuple[ConstraintBase,list[RType]] makeFreshTypes(ConstraintBase cs, int n) {
    list[RType] ftlist = [InferenceVar(c) | c <- [cs.freshCounter .. (cs.freshCounter+n-1)] ];
    cs.freshCounter = cs.freshCounter + n;
    return < cs, ftlist >;
}

public tuple[ConstraintBase,RType] makeFreshType(ConstraintBase cs) {
    RType freshType = InferenceVar(cs.freshCounter);
    cs.freshCounter = cs.freshCounter + 1;
    return < cs, freshType >;
}

public ConstraintBase makeNewConstraintBase() {
    return ConstraintBase(0, { }, { }, ( ));
}