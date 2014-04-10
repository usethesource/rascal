module experiments::Compiler::Examples::QL::lang::qla::CheckExpr

import experiments::Compiler::Examples::QL::lang::qla::AST;
import experiments::Compiler::Examples::QL::lang::qla::TypeOf;
import experiments::Compiler::Examples::QL::lang::qla::Resolve;

import Message;
import Relation;
import Set;

//Helper function to do automatic calling of tc on sub-expressions
// and to prevent type-checking if the sub-expressions have errors.
set[Message] tc(Expr e, QType mgt, Info i, Expr kids...) {
  errs = ( {} | it + tc(k, i) | k <- kids );
  if (errs == {}) {
    return {error("Invalid operand type (expected <type2str(mgt)>)", k@location)
               | k <- kids, !compatible(typeOf(k, i), mgt) };
  }
  return errs;
}

set[Message] checkEq(Expr e, Info i, Expr lhs, Expr rhs) {
  errs = tc(lhs, i) + tc(rhs, i);
  if (errs == {}, !compatible(typeOf(lhs, i), typeOf(rhs, i))) {
    return {error("Incompatible types", e@location)};
  }
  return errs;
}

set[Message] tc(e:var(x), Info i) = {error("Undefined name", e@location)}
  when i.refs.use[x@location] == {}; 

set[Message] tc(e:eq(lhs, rhs), Info i)   = checkEq(e, i, lhs, rhs);
set[Message] tc(e:neq(lhs, rhs), Info i) = checkEq(e, i, lhs, rhs);
  
set[Message] tc(n:not(e), Info i)        = tc(n, boolean(), i, e);
set[Message] tc(e:mul(lhs, rhs), Info i) = tc(e, numeric(), i, lhs, rhs);
set[Message] tc(e:div(lhs, rhs), Info i) = tc(e, numeric(), i, lhs, rhs);
set[Message] tc(e:add(lhs, rhs), Info i) = tc(e, numeric(), i, lhs, rhs);
set[Message] tc(e:sub(lhs, rhs), Info i) = tc(e, numeric(), i, lhs, rhs);
set[Message] tc(e:gt(lhs, rhs), Info i)  = tc(e, numeric(), i, lhs, rhs);
set[Message] tc(e:geq(lhs, rhs), Info i) = tc(e, numeric(), i, lhs, rhs);
set[Message] tc(e:lt(lhs, rhs), Info i)  = tc(e, numeric(), i, lhs, rhs);
set[Message] tc(e:leq(lhs, rhs), Info i) = tc(e, numeric(), i, lhs, rhs);
set[Message] tc(e:and(lhs, rhs), Info i) = tc(e, boolean(), i, lhs, rhs);
set[Message] tc(e:or(lhs, rhs), Info i)  = tc(e, boolean(), i, lhs, rhs);

default set[Message] tc(Expr _, Info i) = {}; 


