module SDF2Grammar

import IO;
import languages::sdf2::syntax::Sdf2;

public Grammar sdf2grammar(loc input) {
  return sdf2grammar(parse(#SDF, input)); 
}

public Grammar sdf2grammar(SDF definition) {
  return grammar(getStartSymbols(definition), getProductions(definition));
}

public set[ParseTree::Production] getProductions(SDF definition) {
  return {};
}

public set[ParseTree::Symbol] getStartSymbols(SDF definition) {
  return {};
}