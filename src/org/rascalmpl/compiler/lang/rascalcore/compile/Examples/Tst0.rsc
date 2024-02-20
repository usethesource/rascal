module lang::rascalcore::compile::Examples::Tst0

//lang::rascal::tests::concrete::ModulesWithStoredParsers

import ParseTree;
import lang::rascal::grammar::storage::ModuleParserStorage;

lexical W = [\ ];
layout L = W*;
lexical A = [A];
syntax As = A+;

value main(){ //test bool storeParserNonModule() {
  storeParsers(#As, |memory://test-tmp/parsersA.jar|);
  p = loadParsers(|memory://test-tmp/parsersA.jar|);

  value v_p = p(type(sort("As"), ()), "A A", |origin:///|);
  As v_parse = parse(#As, "A A", |origin:///|);
  
  sym = sort("S");
  rules = ();
  input = "abc";
  Tree tree = ParseTree::parse(type(sym, rules), input, |todo:///|);
  
  return v_p == v_parse;
}