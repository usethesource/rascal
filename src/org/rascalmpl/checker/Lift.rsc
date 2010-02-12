module org::rascalmpl::checker::Lift

import languages::rascal::syntax::Rascal;
import ParseTree;

// This module will contain functionality to lift 
// embedded concrete syntax fragments to expressions
// and patterns over the UPTR data-type

public Expression lift(Tree e) {
  l = e@\loc;

  if (appl(prod, args) := e) {
    return `appl(<lift(prod,l)>,<lift(args)>)`;
    1;
  }  

  throw "missed a case: <e>";
}

public Expression lift(Production p, loc l) {
  if (prod(lhs, rhs, attrs) := p) {
    // return `prod(<lift(lhs,l)>,<lift(rhs,l)>,<lift(attrs,l)>)`;
     1;
  }

  throw "missed a case: <p>";
}

public Expression lift(list[Symbol] syms, loc l) {
  // {Expression ","}* result = ({Expression ","}*) ``;

  for (sym <- syms) {
 1;   
  }

  // return (Expression) `[ <{Expression ","}* result> ]`@[\loc=l];
}

public Pattern lift(Pattern e) {

  return e;
}