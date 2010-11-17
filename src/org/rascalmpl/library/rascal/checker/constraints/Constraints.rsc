@bootstrapParser
module rascal::checker::constraints::Constraints

import ParseTree;
import constraints::Constraint;
import rascal::checker::Types;
import rascal::checker::SymbolTable;
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
    
data Constraint =
      TreeIsType(Tree t, loc at, RType treeType) // Tree t at loc at is of type rt

    | TypesAreEqual(RType left, RType right) // Type t1 and t2 are identical 

    | LubOf(list[RType] typesToLub, RType lubResult) // The lub of the types in lubs is res
    | LubOfList(list[RType] typesToLub, RType lubResult) // The lub of the types in lubs is res
    | LubOfSet(list[RType] typesToLub, RType lubResult) // The lub of the types in lubs is res

    | Assignable(RType rvalue, RType lvalue) // Type rvalue is assignable to type lvalue

    | FieldOf(Tree t, RType inType, RType fieldType)
    
    | NamedFieldOf(Tree t, RType inType, RType fieldType)
    
	| IfTuple(RType guard, Constraint guardedConstraint)
    | IfMap(RType guard, Constraint guardedConstraint)
    | IfRelation(RType guard, Constraint guardedConstraint)
    
    | CallOrTree(RType source, list[RType] params, RType result)
        
	| Comparable(RType left, RType right)
	
    | BuiltInAppliable(RBuiltInOp op, RType domain, RType range, loc at)
	
    | Failure(Tree t, loc at, RType failureType)
    
    | DefinedBy(RType lvalue, set[STItemId] definingIds)

    | Bindable(RType left, RType right)
    
    | SubtypeOf(RType left, RType right)
    
    | VisitIsType(Tree t, loc at, RType visitType)
    ;

public str prettyPrintConstraint(Constraint c) {
    switch(c) {
        case TreeIsType(t,at,rt) : return "<t> : <rt> at <at>";
        
        case TypesAreEqual(l,r) : return "<l> = <r>";
        
        case BuiltInAppliable(op,d,r,at) : return "<op> : <d> -\> <r> at <at>";
        
        default : return "<c>";    
    }
}

data ConstraintBase = 
      ConstraintBase(int freshCounter, 
                     Constraints constraints,
                     Constraints treeTypes);

data RType =
      InferenceVar(int tnum)
    | TupleProjection(list[RType] argTypes)
    | SingleProjection(RType resType)
    | RelationProjection(list[RType] argTypes)
    | SetProjection(RType resType)
    | CaseType(Tree casePattern, RType caseType)
    | AssignableType(Tree assignablePattern)
    | TypeWithName(RType resType)
    | SpliceableElement(RType rt)
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
    return ConstraintBase(0, { }, { });
}