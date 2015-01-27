module lang::rascal::grammar::IguanaParserGenerator
 
import Grammar;
import ParseTree;
import lang::rascal::grammar::definition::Literals;
import lang::rascal::grammar::definition::Priorities;
import lang::rascal::grammar::definition::Regular;
import lang::rascal::grammar::definition::Parameters;
import lang::rascal::grammar::definition::Tokens;  
import lang::rascal::grammar::definition::Keywords;
import lang::rascal::grammar::ConcreteSyntax;
import IO;
  
public Grammar lexToToken(Grammar g) = visit(g) {
  case \lex(n) => \token(n)
};
  
public Grammar preprocess(map[Symbol,Production] definitions, bool lexToTok = false) {
  gr = grammar({}, definitions);
iprintln("gr before: <gr>");
  if (lexToTok) 
    gr = lexToToken(gr);
    
  gr = literals(gr);
  gr = flattenTokens(gr);
  //gr = addHoles(gr);
  //gr = expandKeywords(gr);
  //gr = expandRegularSymbols(makeRegularStubs(gr));
  gr = expandParameterizedSymbols(gr);
  gr = addNotAllowedSets(gr);
  gr = prioAssocToChoice(gr);
  return gr;
} 
