module lang::rascal::iguana::IguanaParserGenerator

import Grammar;
import ParseTree;
import lang::rascal::iguana::definition::Literals;
import lang::rascal::iguana::definition::Priorities;
import lang::rascal::iguana::definition::Regular;
import lang::rascal::iguana::definition::Parameters;
import lang::rascal::iguana::definition::Tokens;  
import lang::rascal::iguana::definition::Keywords;
import lang::rascal::iguana::ConcreteSyntax;
import IO;
  
public Grammar preprocess(Grammar gr) {
iprintln("gr before: <gr.rules<0>>");
  gr = literals(gr);
  gr = flattenTokens(gr);
  gr = addHoles(gr);
  //gr = expandKeywords(gr);
  gr = expandRegularSymbols(makeRegularStubs(gr));
  gr = expandParameterizedSymbols(gr);
  gr = addNotAllowedSets(gr);
  gr = prioAssocToChoice(gr);
  return gr;
} 
