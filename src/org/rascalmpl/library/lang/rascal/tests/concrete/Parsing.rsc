module lang::rascal::tests::concrete::Parsing

import ParseTree;
import Exception;
import IO;
import lang::rascal::tests::concrete::OtherSyntax;

start syntax A = "a";
layout WS = [\ \t\n\r]*;

start syntax B = "b" | [a-z];

test bool strExpr() = (A) `a` := parse(#A,"a");

test bool allowAmb() = /amb(_) := parse(#B, "b", allowAmbiguity=true);

test bool disallowAmb() {
  try {
    parse(#B, "b");
    return false; // should have thrown Ambiguity exception
  }
  catch Ambiguity(_,_,_) :
    return true;
}

test bool disallowAmb2() {
  try {
    parse(#B, "b", allowAmbiguity=false);
    return false; // should have thrown Ambiguity exception
  }
  catch Ambiguity(_,_,_) :
    return true;
}

@ignoreCompiler{FIX: TC does not yet allow [A] loc}
test bool locExpr() {
  writeFile(|memory://test-tmp/locExpr.txt|,"a");
  return [A] |memory://test-tmp/locExpr.txt| == parse(#A, |memory://test-tmp/locExpr.txt|);
}

test bool parsingWithADynamicGrammar() =
  B _ := parse(visit(#B) { case "b" => "bbb" }, "bbb");
  
test bool parsingWithAGrammarFromADifferentModule() =
  Remote _ := parse(getRemoteGrammar(), "remote");
  
test bool parsingWithAParameterGrammar() {
  Tree p(type[&T <: Tree] gr, str s) = parse(gr, s);
  
  return Tree _ := p(#B, "a"); 
}
  
test bool parsingWithARemoteParameterGrammar() {
  Tree p(type[&T <: Tree] gr, str s) = parse(gr, s);
  
  return Tree _ := p(getRemoteGrammar(), "remote"); 
}

test bool parsingWithAManualGrammar() 
  = type[Tree] gr := type(sort("MySort"), (sort("MySort") : choice(sort("MySort"), 
      {prod(sort("MySort"), [lit("hello")],{})})))
  && Tree t := parse(gr, "hello")
  && "<t>" == "hello";

@ignoreCompiler{Fails because the type Symbol is assigned to type(...), bust assigning value() breaks other code}
test bool saveAndRestoreParser() {
  storeParsers(#start[A], |memory://test-tmp/parsers.jar|);
  p = loadParsers(|memory://test-tmp/parsers.jar|);

  return p(type(\start(sort("A")), ()), "a", |origin:///|) == parse(#start[A], "a", |origin:///|);
}