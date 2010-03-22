module rascal::parser::Priority

import rascal::parser::Grammar;
import rascal::parser::Definition;

public rel[Production greater, Production lower] priorities(Grammar g) {
  return {};
}

public rel[Production greater, Production lower] leftAssociativity(Grammar g) {
  return {};
}

public rel[Production greater, Production lower] rightAssociativity(Grammar g) {
  return {};
}

public rel[Production greater, Production lower] nonAssociativity(Grammar g) {
  return {};
}

public int f() { return 1;}
