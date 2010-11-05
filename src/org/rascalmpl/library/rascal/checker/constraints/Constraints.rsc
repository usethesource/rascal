@bootstrapParser
module rascal::checker::constraints::Constraints;

import ParseTree;
import rascal::checker::Types;
import rascal::syntax::RascalRascal;

data RConstantOp =
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
    
data Constraint =
      IsType(Tree t, RType rt)
    | IsType(RType l, RType r)
    | FieldOf(Tree t, RType rt, RType ft)
    | NamedFieldOf(Tree t, RType rt, RType ft)
    | Assignable(RType l, RType r)
    | Bindable(RType l, RType r)
    | ConstantAppliable(RConstantOp rcop, list[RType] domain, RType range)
    | SubtypeOf(RType l, RType r)
    | LubOf(list[RType] lubs, RType res)
    | Failure(Tree t, RType rt)
    | DefinedBy(Tree t, set[STItemId] defs)
    | SplicedListElement(RType rt)
    | SplicedSetElement(RType rt)
    ;

data Constraints = 
      Constraints(int freshCounter, set[Constraint] constraints);

data RType =
      FreshType(int tnum)
    | CallableType(list[RType] argTypes, RType resType)
    | TypeWithName(RType resType)
    | TupleProjection(list[RType] argTypes)
    | SingleProjection(RType resType)
    | RelationProjection(list[RType] argTypes)
    | SetProjection(RType resType)
    ;
    