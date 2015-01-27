module experiments::Compiler::Examples::Tst1

import Grammar;
import ParseTree;
import lang::rascal::grammar::definition::Symbols;
import lang::rascal::grammar::definition::Productions;
import IO;


public Grammar C = grammar({sort("TranslationUnit")}, (
keywords("Keyword"): choice(keywords("Keyword"),{prod(keywords("Keyword"),[lit("extern")],{}),prod(keywords("Keyword"),[lit("int")],{}),prod(keywords("Keyword"),[lit("signed")],{}),prod(keywords("Keyword"),[lit("char")],{}),prod(keywords("Keyword"),[lit("const")],{}),prod(keywords("Keyword"),[lit("typedef")],{}),prod(keywords("Keyword"),[lit("register")],{}),prod(keywords("Keyword"),[lit("union")],{}),prod(keywords("Keyword"),[lit("unsigned")],{}),prod(keywords("Keyword"),[lit("auto")],{}),prod(keywords("Keyword"),[lit("goto")],{}),prod(keywords("Keyword"),[lit("do")],{}),prod(keywords("Keyword"),[lit("continue")],{}),prod(keywords("Keyword"),[lit("for")],{}),prod(keywords("Keyword"),[lit("break")],{}),prod(keywords("Keyword"),[lit("short")],{}),prod(keywords("Keyword"),[lit("double")],{}),prod(keywords("Keyword"),[lit("struct")],{}),prod(keywords("Keyword"),[lit("case")],{}),prod(keywords("Keyword"),[lit("while")],{}),prod(keywords("Keyword"),[lit("switch")],{}),prod(keywords("Keyword"),[lit("default")],{}),prod(keywords("Keyword"),[lit("float")],{}),prod(keywords("Keyword"),[lit("long")],{}),prod(keywords("Keyword"),[lit("static")],{}),prod(keywords("Keyword"),[lit("sizeof")],{}),prod(keywords("Keyword"),[lit("volatile")],{}),prod(keywords("Keyword"),[lit("void")],{}),prod(keywords("Keyword"),[lit("enum")],{}),prod(keywords("Keyword"),[lit("if")],{}),prod(keywords("Keyword"),[lit("return")],{}),prod(keywords("Keyword"),[lit("else")],{})})
));

public set[Condition] expandKeywords(Grammar g, set[Condition] conds) {
  names = {};
  done = {};
  todo = conds;

  solve(todo) {  
    for (cond <- todo, !(cond in done)) {
      println("consider: <cond>");
      todo -= {cond};
      println("cond has symbol: <cond has symbol>");
      println("keywords(str name) := cond.symbol: <keywords(str name) := cond.symbol>");
      if (cond has symbol && keywords(str name) := cond.symbol) {
        println("consider name: <name>");
        if (name notin names) {	
        	names += {name};
        	todo += {cond[symbol=s] | choice(_, set[Production] alts) := g.rules[cond.symbol], prod(_,[s],_) <- alts};
        }
      }
      else {
        done += cond;
      }
    }
  }
  
  return done;  
}

value main(list[value] args) = expandKeywords(C, {delete(keywords("Keyword"))});
