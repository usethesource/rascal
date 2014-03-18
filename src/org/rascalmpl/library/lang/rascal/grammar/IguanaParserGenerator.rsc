module lang::rascal::grammar::IguanaParserGenerator

import Grammar;
import ParseTree;
import lang::rascal::grammar::definition::Literals;
import lang::rascal::grammar::definition::Priorities;
import lang::rascal::grammar::definition::Regular;
import lang::rascal::grammar::definition::Parameters;
import lang::rascal::grammar::analyze::Lexicals;
import lang::rascal::grammar::definition::Keywords;
import lang::rascal::grammar::ConcreteSyntax;

public Grammar preprocess(Grammar gr) {
  gr = addHoles(gr);
  gr = literals(gr);
  gr = expandKeywords(gr);
  gr.about["regularExpressions"] = getRegularLexicals(gr);
  gr = makeRegularStubs(expandRegularSymbols(makeRegularStubs(gr)));
  gr = expandParameterizedSymbols(gr);
  gr = addNotAllowedSets(gr);
  gr = prioAssocToChoice(gr);

  return gr;
} 
