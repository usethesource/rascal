module lang::rascalcore::compile::Rascal2muRascal::ConcreteSyntax

import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;

import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::AType;

import ParseTree;
import Message;
import String;
import IO;

tuple[Tree, TModel] parseConcreteFragments(Tree M, TModel tm, AGrammar gr) {
   // here we translate to the original Productions and Symbol to be used by the parser generator:
   map[Symbol, Production] rules = adefinitions2definitions(gr.rules);
   
   @doc{parse fragment or store parse error in the TModel}
   Tree parseFragment(Tree sym, list[Tree] parts, map[Symbol, Production] rules, bool isPattern) {
      try {
         return doParseFragment(atype2symbol(getType(sym@\loc)), parts, rules, isPattern);
      }
      catch ParseError(loc l) : {
        tm.messages += error("parse error in concrete syntax fragment `<parts>`", l);
         // TODO
        return appl(prod(sort("***Error***"),[],{}),[]); 
      }
   }

   M = visit(M) {
//////////   "(" LAYOUTLIST l1 Sym symbol LAYOUTLIST l2 ")" LAYOUTLIST l3 "`" ConcretePart* parts "`"
     case appl(prod(label("concrete",sort(str point)), _, _),[
             appl(prod(label("typed",lex("Concrete")), _, _),[_,_, Tree varsym,_,_,_,_,parts,_])
          ]) 
          => parseFragment(varsym, parts.args, rules, point == "Pattern")
        when point == "Pattern" || point == "Expression"
   }
   
   return <M, tm>;
}


Tree doParseFragment(Symbol sym, list[Tree] parts, map[Symbol, Production] rules, bool isPattern) {
   int index = 0;
   map[int, Tree] holes = ();
   
   str cleanPart(appl(prod(label("text",lex("ConcretePart")), _, _),[Tree stuff])) = "<stuff>";
   str cleanPart(appl(prod(label("lt",lex("ConcretePart")), _, _),_)) = "\<";
   str cleanPart(appl(prod(label("gt",lex("ConcretePart")), _, _),_)) = "\>";
   str cleanPart(appl(prod(label("bq",lex("ConcretePart")), _, _),_)) = "`";
   str cleanPart(appl(prod(label("bs",lex("ConcretePart")), _, _),_)) = "\\";
   str cleanPart(appl(prod(label("newline",lex("ConcretePart")), _, _),_)) = "\n";
   str cleanPart(appl(prod(label("hole",lex("ConcretePart")), _, _),[Tree hole])) {
      index += 1;
      holes[index] = hole;
      return "\u0000<atype2symbol(getType(hole.args[2]@\loc))>:<index>\u0000";
   }
   
   // first replace holes by indexed sub-strings
   str input = "<for (p <- parts) {><cleanPart(p)><}>";
   
   // now parse the input to get a Tree (or a ParseError is thrown)
   println("calling parser for <sym> with <rules>");
   Tree tree = parse(type(sym, rules), input, |todo:///|);
   
   println("tree: <tree>");
   
   // replace the indexed sub-strings back with the original holes (wrapped in an easy-to-recognize appl)
   tree = visit (tree) {
     case appl(prod(label("$MetaHole", _), _, _), [_,_,_,i,_],_) => 
          appl(prod(label("$MetaHole", isPattern ? sort("Pattern") : sort("Expression")), [sort("ConcreteHole")],{}), 
               [holes[toInt("<i>")]]) 
   }
   
   return tree;
}