@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::compile::Rascal2muRascal::ConcreteSyntax

import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
import lang::rascalcore::compile::CompileTimeError;

import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::AType;

import ParseTree;
import Message;
import String;
import IO;

// WARNING: this module is sensitive to bootstrapping dependencies and implicit contracts:
// 
// * The functionality of parseConcreteFragments depends on the shape of the Rascal grammar,
// yet this grammar is not imported here. We use the Tree meta notation from ParseTree.rs to detect production
// rules of the Rascal grammar;
// * This module must not use concrete syntax itself to avoid complex bootstrapping issues;

tuple[Tree, TModel] parseConcreteFragments(Tree M, TModel tm, AGrammar gr) {
   // here we translate internal type-checker symbols to the original Productions and Symbol 
   // to be used by the parser generator and the Rascal run-time:
   map[Symbol, Production] rules = adefinitions2definitions(gr.rules);
   
   @doc{parse fragment or store parse error in the TModel}
   Tree parseFragment(t:appl(prod(label("typed",lex("Concrete")), _, _),[_,_,Tree varsym,_,_,_,_, parts,_])) {
      try {
         sym = atype2symbol(getType(varsym@\loc));
         return appl(prod(label("parsed",Symbol::lex("Concrete")), [sym], {}), [
                   doParseFragment(sym, parts.args, rules)
                ])[@\loc=t@\loc];
      }
      catch ParseError(_) : {
        throw CompileTimeError(error("Parse error in concrete syntax fragment `<for (p <- parts){><p><}>`", t@\loc)); 
      }
      catch Ambiguity(loc _location, str nonterminal, str sentence): {
        throw CompileTimeError(error("Ambiguity in concrete syntax fragment for <nonterminal>: `<sentence>`", /*location*/ t@\loc)); 
      }
   }

   M = visit(M) {
     case Tree t:appl(p:prod(label("concrete",sort(/Expression|Pattern/)), _, _),[Tree concrete])
          => appl(p, [parseFragment(concrete)])[@\loc=t@\loc]
   }
   
   return <M, tm>;
}


Tree doParseFragment(Symbol sym, list[Tree] parts, map[Symbol, Production] rules) {
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
      
      // here we weave in a unique and indexed sub-string for which a special rule
      // was added by the parser generator: 
      holeType = atype2symbol(getType(hole.args[2]@\loc));
      return "\u0000<denormalize(holeType)>:<index>\u0000";
   }
   
   // first replace holes by indexed sub-strings
   str input = "<for (p <- parts) {><cleanPart(p)><}>";

   // now parse the input to get a Tree (or a ParseError is thrown)
   if(type[Tree] tp := type(sym, rules)){
        Tree tree = ParseTree::parse(tp, input, |todo:///|);
        return isEmpty(parts) ? tree : restoreHoles(tree, holes, parts[0]@\loc);
   } else {
        throw InternalCompilerError(error("Illegal type <sym> in concrete syntax fragment `<for (p <- parts){><p><}>`", parts[0]@\loc));
   }
}

// TODO: this is a copy of denormalize in lang::rascal::grammar::ConcreteSyntax
@doc{
  In Rascal programs with type literals, it's hard to see easily if it is a lex or sort, so we "denormalize" here.
  The same goes for the introduction of layout non-terminals in lists. We do not know which non-terminal is introduced,
  so we remove this here to create a canonical 'source-level' type.
}
private Symbol denormalize(Symbol s) = visit (s) { 
  case Symbol::\lex(n)                                                      => Symbol::\sort(n)
  case Symbol::\iter-seps(u,[Symbol::layouts(_),t,Symbol::layouts(_)])      => \iter-seps(u,[t])
  case Symbol::\iter-star-seps(u,[Symbol::layouts(_),t,Symbol::layouts(_)]) => \iter-star-seps(u,[t])
  case Symbol::\iter-seps(u,[Symbol::layouts(_)])                           => \iter(u)
  case Symbol::\iter-star-seps(u,[Symbol::layouts(_)])                      => \iter-star(u)
  case Symbol::\seq(ss)                                                     => seq([t | t <- ss, !(t is layouts)])
};

@doc{restores the parse trees for the typed holes and also recovers all location information}
Tree restoreHoles(Tree t, map[int, Tree] holes, loc begin) {
   <t, _, _, _> = restoreHoles(t, holes, begin.top, begin.offset, begin.begin.line, begin.begin.column);
   return t;
}

alias RestoreState = tuple[Tree tree, int offset, int line, int column];

RestoreState restoreHoles(t:char(int c), map[int, Tree] _, loc _, int offset, int line, int column) {
    if(c == 10 /*nl*/) return <t, offset+1, line+1, 0>;
    // in the original source text of the concrete pattern these chars would have been escaped and thus they take
    // 2 character positions instead of 1:
    if(c in {96 /*`*/, 60 /*<*/, 62 /*>*/, 92 /*/*/}) return <t, offset+2, line, column+2>;
    return <t, offset+1, line, column+1>;
}

// discover an artificial hole and find the original parse tree to replace it
RestoreState restoreHoles(Tree v:appl(prod(Symbol::label("$MetaHole", Symbol varType), _, {\tag("holeType"(Symbol ht))}), [char(0),_,_,Tree i,char(0)]),
                           map[int, Tree] holes, loc _, int _, int _, int _) { 
   Tree typedVar = holes[toInt("<i>")];
   return <appl(prod(label("$MetaHole", varType),[Symbol::sort("ConcreteHole")], {\tag("holeType"(ht))}), [typedVar])[@\loc=typedVar@\loc],
            typedVar@\loc.offset + typedVar@\loc.length,
            typedVar@\loc.end.line,
            typedVar@\loc.end.column
            >;
}

// recursion through the tree and add the position information
default RestoreState restoreHoles(Tree v:appl(Production p, list[Tree] args), map[int, Tree] holes, loc file, int offset, int line, int column) {
   curOffset = offset;
   curLine = line;
   curColumn = column;

   newArgs = for (a <- args) {
      <a, curOffset, curLine, curColumn> = restoreHoles(a, holes, file, curOffset, curLine, curColumn);
      append a;
   }

   return <appl(p, newArgs)[@\loc=file(offset, curOffset - offset, <line, column>, <curLine, curColumn>)],
            curOffset, curLine, curColumn>;
}


