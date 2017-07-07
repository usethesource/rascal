@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Davy Landman - Davy.Landman@cwi.nl - CWI}

module util::Proposer

import IO;
import String;
import ParseTree;
import lang::rascal::format::Grammar;

data Contribution 
     =  proposer(list[CompletionProposal] (&T<:Tree input, str prefix, int requestOffset) proposer, str legalPrefixChars);
     
data CompletionProposal 
  = sourceProposal(str newText) /*1*/
  | sourceProposal(str newText, str proposal) /*2*/
  | errorProposal(str errorText) /*3*/
  ;
  
  Contribution proposer(list[CompletionProposal] (&T<:Tree input, str prefix, int requestOffset) prop, type[Tree] cc : type(\char-class(_),_))
  = proposer(prop, class2str(cc));
  
  private str class2str(type[&T <: Tree] cc) = "<for (\char-class(rs) := cc.symbol, range(b,e) <- rs, ch <- [b..e+1]) {><char(ch)><}>"; 
  
  Contribution syntaxProperties(type[&N <: Tree] g) {
  rules = { p | /p:prod(_,_,_) := g.definitions};

  return syntaxProperties(
      fences= {<b,c> | prod(_,[lit(str b),*_, lit(str c)],{\tag("fences"()), *_}) <- rules}
            + {<b,c> | prod(_,[*pre, lit(str b), *mid, lit(str c), *post],{\tag("fences"(<int i, int j>)), *_}) <- rules, size(pre) == i * 2, size(pre) + 1 + size(mid) == j * 2}
            + {<b,c> | prod(_,[lit(str b),*_,lit(str c)],{\bracket(),*_}) <- rules},
      lineComment="<if (prod(_,[lit(b),*_,c],{\tag("lineComment"()),*_}) <- rules, (c == lit("\n") || lit(_) !:= c)){><b><}>",
      blockComment= (prod(_,[lit(b),*_,lit(c)],{\tag("blockComment"()),*_}) <- rules && b != c && c != "\n") ? <b,"",c> : <"","","">
  );
}

alias ProposalFunction = list[CompletionProposal] (str prefix, int requestOffset);

ProposalFunction proposer(type[&N <: Tree] g) {
  rules = {p | /p:prod(_,_,_) := g.definitions};
  println(rules);
  prefixrules = { <x,p> | p:prod(_,[lit(x),*_],_) <- rules};
  
  println(prefixrules);
  
  str sym(lit(z)) = z;
  str sym(c:\char-class(_)) = class2str(c);
  str sym(layouts(_)) = " ";
  default str sym(Symbol s) = "\<<symbol2rascal(s)>\>";
  
  CompletionProposal toProposal(Production p) = sourceProposal("<for(s <- p.symbols){><sym(s)><}>", replaceAll(prod2rascal(p[attributes={}]),"\n"," "));
  
  return list[CompletionProposal] (str prefix, int offset) {
    return [toProposal(p) | <x,p> <- prefixrules, startsWith(x, prefix)];
  };
}

