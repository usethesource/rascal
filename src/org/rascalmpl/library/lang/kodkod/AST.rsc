module lang::kodkod::AST

data Expr 
  = \closure(Expr \arg)
  | \difference(Expr lhs, Expr rhs)
  | \intersection(Expr lhs, Expr rhs)
  | \join(Expr lhs, Expr rhs)
  | \override(Expr lhs, Expr rhs)
  | \product(Expr lhs, Expr rhs)
  | \difference(list[Expr] args)
  | \intersection(set[Expr] args)
  | \join(list[Expr] args)
  | \override(list[Expr] args)
  | \product(list[Expr] args)
  | \transpose(list[Expr] args)
  | \union(set[Expr] args)
  | \reflexiveClosure(Expr \arg)
  | \transpose(Expr lhs, Expr rhs)
  | \union(Expr lhs, Expr rhs)
  | \evar(str name)
  | \comprehension(list[Decl] decls, Formula formula)
  | \ite(Formula condition, Expr \true, Expr \false)
  | \cast(Calc calc, Cast op)
  | \constant(str name, int arity)
  | \projection(Expr expr, list[Calc] columns)
  | \relation(rel[Expr, Expr] r)
  ;

data Decl
  = decl(str var, Expr expr, Multiplicity mult)
  ;
  
data Multiplicity
  = \lone()
  | \one()
  | \some()
  | \set()
  | \no()
  ;
    
data Cast 
  = bit() 
  | \int()
  ;
  
data Formula
  = \and(Formula lhs, Formula rhs)
  | \or(Formula lhs, Formula rhs)
  | \and(set[Formula] args)
  | \or(set[Formula] args)
  | \if(Formula lhs, Formula rhs)
  | \iff(Formula lhs, Formula rhs)
  | \eq(Calc lhs, Calc rhs)
  | \gt(Calc lhs, Calc rhs)
  | \lt(Calc lhs, Calc rhs)
  | \lte(Calc lhs, Calc rhs)
  | \multiplicity(Expr exp, Multiplicity mult)
  | \not(Formula arg)
  | \quantified(list[Decl] decls, Formula arg, Quantifier quantifier) 
  | \predicate(Expr, RelationPredicate pred)
  ;
  
data RelationPredicate
  = acyclic()
  | function()
  | totalOrdering()
  ;
  
data Quantifier
  = \all()
  | \some()
  ;
  
data Calc
  = \abs(Calc \arg)
  | \not(Calc \arg)
  | \neg(Calc \arg)
  | \sgn(Calc \arg)
  | \div(Calc lhs, Calc rhs)
  | \mod(Calc lhs, Calc rhs)
  | \mul(Calc lhs, Calc rhs)
  | \bor(Calc lhs, Calc rhs)
  | \add(Calc lhs, Calc rhs)
  | \sha(Calc lhs, Calc rhs)
  | \shl(Calc lhs, Calc rhs)
  | \shr(Calc lhs, Calc rhs)
  | \xor(Calc lhs, Calc rhs)
  | \add(list[Calc] args)
  | \div(list[Calc] args)
  | \mod(list[Calc] args)
  | \mul(list[Calc] args)
  | \bor(list[Calc] args)
  | \sha(list[Calc] args)
  | \shl(list[Calc] args)
  | \shr(list[Calc] args)
  | \xor(set[Calc] args)
  | \if(Formula condition, Calc \true, Calc \false)
  | \sum(Calc \arg, list[Decl] decls)
  | \cvar(str name)
  ;