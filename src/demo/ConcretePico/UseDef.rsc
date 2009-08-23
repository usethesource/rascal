module demo::ConcretePico::UseDef

import languages::pico::syntax::Pico;  // Pico concrete syntax

/*
 * Compute variable uses:
 * - Associate all variable uses in an expression with the enclosing expression.
 * - Associate all variable uses in an assignment statement with that statement.
 */
rel[\PICO-ID, Tree] uses(PROGRAM P) {
  return {Id | top-down-break EXP E <- P, \PICO-ID Id <- E};
}

rel[\PICO-ID, STATEMENT] defs(PROGRAM P) { 
  return {<Id, S> | STATEMENT S <- P, [| <\PICO-ID Id> := <EXP Exp> |] := S};
}