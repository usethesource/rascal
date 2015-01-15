module experiments::Compiler::Examples::QL::lang::qla::TypeOf

import experiments::Compiler::Examples::QL::lang::qla::AST;
import experiments::Compiler::Examples::QL::lang::qla::Resolve;
import Node;

// Extend with numeric and error
data QType = numeric() | error();
  
str type2str(QType t) = getName(t);

QType lub(QType::money(), QType::integer()) = numeric();
QType lub(QType::integer(), QType::money()) = numeric();
QType lub(QType t1, QType t2) = t1 when t1 == t2;
default QType lub(QType t1, QType t2) = error();

bool compatible(QType t1, QType t2) = true when t1 == t2;
bool compatible(QType::integer(), QType::money()) = true;
bool compatible(QType::money(), QType::integer()) = true;
bool compatible(QType::integer(), QType::numeric()) = true;
bool compatible(QType::numeric(), QType::integer()) = true;
bool compatible(QType::money(), QType::numeric()) = true;
bool compatible(QType::numeric(), QType::money()) = true;
default bool compatible(QType t1, QType t2) = false;


QType numeric(Expr lhs, Expr rhs, Info i)
  = lub(typeOf(lhs, i), typeOf(rhs, i));

QType typeOf(var(x), Info i) = t 
  when
    d <- i.refs.use[x.origin], t <- i.refs.def[d];  
   
QType typeOf(Expr::integer(_), Info i) = integer();
QType typeOf(\true(), Info i) = boolean();
QType typeOf(\false(), Info i) = boolean();
QType typeOf(Expr::\string(_), Info i) = string();
QType typeOf(not(_), Info i) = boolean();

QType typeOf(mul(lhs, rhs), Info i) = numeric(lhs, rhs, i);
QType typeOf(div(lhs, rhs), Info i) = numeric(lhs, rhs, i);
QType typeOf(add(lhs, rhs), Info i) = numeric(lhs, rhs, i);
QType typeOf(sub(lhs, rhs), Info i) = numeric(lhs, rhs, i);
  
QType typeOf(gt(_, _), Info i)  = boolean();
QType typeOf(lt(_, _), Info i)  = boolean();
QType typeOf(leq(_, _), Info i) = boolean();
QType typeOf(geq(_, _), Info i) = boolean();
QType typeOf(eq(_, _), Info i)  = boolean();
QType typeOf(neq(_, _), Info i) = boolean();
QType typeOf(and(_, _), Info i) = boolean();
QType typeOf(or(_, _), Info i)  = boolean();

default QType typeOf(Expr _, Info i) = error();
