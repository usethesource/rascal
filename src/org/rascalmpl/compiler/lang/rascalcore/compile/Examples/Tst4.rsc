@bootstrapParser
module lang::rascalcore::compile::Examples::Tst4

import ParseTree;
import IO;
import Type;

//import lang::rascal::grammar::storage::ModuleParserStorage;
//
//lexical W = [\ ];
//layout L = W*;
//lexical A = [A];
//syntax As = A+;
//
syntax B = "b";
//
//bool problem_case1(){
//  // From lang::rascal::tests::concrete::ModulesWithStoredParsers::storeParserNonModule
//
//  storeParsers(#As, |memory://test-tmp/parsersA.jar|);
//  // &U (type[&U] nonterminal, value input, loc origin) loadParsers(loc savedParsers, ...);
//  &U (type[&U] nonterminal, value input, loc origin) p = loadParsers(|memory://test-tmp/parsersA.jar|);
//
//  lhs = p(type(sort("As"), ()), "A A", |origin:///|);
//  rhs = parse(#As, "A A", |origin:///|);
//  
//  return lhs == rhs; // types of lhs and rhs should be comparable,
//                     // type[Symbol] => "Comparison not defined on `Symbol` and `As`"
//                     // type[value]  => ok
//}
//
value problem_case2(){
   // From lang::rascalcore::compile::Rascal2muRascal::ConcreteSyntax::doParseFragment
  sym = sort("B");
  rules = ();
  input = "abc";
  // Below type(sym, rules) should be <: Tree,
  // type[Symbol] => "Cannot instantiate formal parameter type `type[&T \<: Tree]`: Type parameter `T` should be less than `Tree`, but is bound to `Symbol`"
  // type[value]  => "Cannot instantiate formal parameter type `type[&T \<: Tree]`: Type parameter `T` should be less than `Tree`, but is bound to `value`"
  
  sym_type = type(sym, rules);
  iprintln(sym_type);
  if(type[Tree] t := sym_type){
    println("subtype!");
    return ParseTree::parse(t, input, |todo:///|);
  } else {
    println("nomatch for <type(sym, rules)>");
  }
  return "OK";
}