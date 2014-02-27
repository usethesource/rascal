module Jigll

import Grammar;
import ParseTree;
import lang::rascal::grammar::definition::Literals;
import lang::rascal::grammar::definition::Priorities;
import lang::rascal::grammar::definition::Regular;
import lang::rascal::grammar::definition::Parameters;
import lang::rascal::grammar::analyze::Lexicals;
import lang::rascal::grammar::definition::Keywords;
import IO;
import Node;
import util::FileSystem;
import lang::rascal::\syntax::Rascal;
import util::Monitor;
import Type;
import util::ValueUI;
import util::Reflective;
import Ambiguity;

list[loc] ignoreList = [|std:///lang/rascal/types/CheckTypes.rsc|];  

@javaClass{org.rascalmpl.parser.GrammarToJigll}
public java void generateGrammar(Grammar grammar);

@javaClass{org.rascalmpl.parser.GrammarToJigll}
public java void generateGraph(str f, loc l);

public void generate(type[&T <: Tree] nont) {
  gr = grammar({nont.symbol}, nont.definitions, ());
  gr = literals(gr);
  gr = expandKeywords(gr);
  gr.about["regularExpressions"] = getRegularLexicals(gr);
  gr = makeRegularStubs(expandRegularSymbols(makeRegularStubs(gr)));
  gr = expandParameterizedSymbols(gr);
  gr = addNotAllowedSets(gr);
  gr = prioAssocToChoice(gr);
 
  generateGrammar(gr);
}

public test bool StandardLibrary() = testModules([f |  /file(f) <- crawl(|std:///|), f.extension == "rsc", /experiments/ !:= f.path, !(f in ignoreList)], []);

public void compare(&T<:Tree old, &T<:Tree new) {
      if (/amb(_) := old) {
        println("Ambiguity found while parsing with old");
      }
      
      if (/amb(_) := new) {
        println("Ambiguity found while parsing with new");
      }
      
      // TODO: fix this difference some time
      new = visit (new) { 
        case conditional(s,_) => s
         
      }
      old = visit (old) { 
        case conditional(s,_) => s
        case 16777215 => 1114111 
      }
      
      if (!eq(new, old)) {
      	println("Not equal");
        text("new"(new));
        text("old"(old));
      } else {
      	println("Equal");
      }
}

public bool testModules(list[loc] files, list[loc] path) {
  generate(#start[Module]);
  errors = [];
  for (f <- files) {
    println("parsing <f>");
    
    try {
      t = jparse(#start[Module], readFile(f), f);
   
      if (/amb(_) := t) {
        println("Ambiguity found while parsing with new: <f>");
      }
      
      println("parsing with old parser <f>");
      other = parseModule(f, [|std:///|]);
      if (/amb(_) := other) {
        println("Ambiguity found while parsing with old: <f>");
      }
      
      // TODO: fix this difference some time
      new = visit (t) { 
        case conditional(s,_) => s
         
      }
      old = visit (other) { 
        case conditional(s,_) => s
        case 16777215 => 1114111 
      }
      
      if (!eq(new, old)) {
        rnew = { p | /p:prod(_,_,_) := new};
        rold = { p | /p:prod(_,_,_) := old};
        println("trees are different for <f>: <rold - rnew>, <rnew - rold>");
        //iprintln(findCauses(new, old));
        //text("new"(new));
        //text("old"(old));
      }
    }
    catch value x: {
      println("failed with <x>");
    }
  }
  
  return true;
}

public &T<:Tree jparse(type[&T <: Tree] nont, str input, loc f) {
  return jparse(nont.symbol, input, f);
}

public &T<:Tree jparse2(type[&T <: Tree] nont, loc f) {
  return jparse(nont.symbol, readFile(f), f);
}

@javaClass{org.rascalmpl.parser.GrammarToJigll}
public java &T<:Tree jparse(Symbol nonterminal, str input, loc f);

@javaClass{org.rascalmpl.parser.GrammarToJigll}
public java void save(str inpu);

@javaClass{org.rascalmpl.parser.GrammarToJigll}
public java void printGrammar();