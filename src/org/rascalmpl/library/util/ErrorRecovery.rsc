module util::ErrorRecovery

import ParseTree;
import String;
import util::Maybe;

@synopsis{Check if a parse tree contains any error nodes, the result of error recovery.}
bool hasErrors(Tree tree) = /appl(error(_, _, _), _) := tree;

@javaClass{org.rascalmpl.library.util.ErrorRecovery}
@synopsis{Find all error productions in a parse tree.}
java list[Tree] findAllErrors(Tree tree);

@synopsis{Find the first production containing an error.}
Tree findFirstError(/err:appl(error(_, _, _), _)) = err;

@synopsis{Find the best error from a tree containing errors. This function will fail if `tree` does not contain an error.}
Maybe[Tree] findBestError(Tree tree) {
  Tree disambiguated = disambiguateErrors(tree);
  if (/err:appl(error(_, _, _), _) := disambiguated) {
    return just(err);
  }

  // All errors have disappeared
  return nothing();
}

@synopsis{Get the symbol (sort) of the failing production}
Symbol getErrorSymbol(appl(error(Symbol sym, _, _), _)) = sym;

@synopsis{Get the production that failed}
Production getErrorProduction(appl(error(_, Production prod, _), _)) = prod;

@synopsis{Get the dot (position in the production) of the failing element in a production}
int getErrorDot(appl(error(_, _, int dot), _)) = dot;

@synopsis{Get the skipped tree}
Tree getSkipped(appl(error(_, _, _), [*_, skip:appl(skipped(_), _)])) = skip;

@synopsis{Get the text that failed to parse. This is only the text of the part that has been skipped to be able to continue parsing.
If you want the text of the whole error tree, you can just use string interpolation: "<error>".
}
str getErrorText(appl(error(_, _, _), [*_, appl(skipped(_), chars)])) = stringChars([c | char(c) <- chars]);

@javaClass{org.rascalmpl.library.util.ErrorRecovery}
@synopsis{Error recovery often produces ambiguous trees where errors can be recovered in multiple ways.
This filter removes error trees until no ambiguities caused by error recovery are left.
Note that regular ambiguous trees remain in the parse forest unless `allowAmbiguity` is set to false in which case an error is thrown.
}
java Tree disambiguateErrors(Tree t, bool allowAmbiguity=true);
