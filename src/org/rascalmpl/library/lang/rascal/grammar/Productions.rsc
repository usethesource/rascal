@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@doc{
 This module contains the mapping from concrete grammars to abstract grammars.
 
 It also implements a part of the semantics of Rascal's syntax definition formalism,
 i.e. by interspersing symbols with layout nodes and expanding literals.
}
@bootstrapParser
module lang::rascal::grammar::Productions
     
import Grammar;
import lang::rascal::syntax::RascalRascal;
import lang::rascal::grammar::Characters;
import lang::rascal::grammar::Normalization;
import List; 
import String;    
import ParseTree;
import IO;  
import Integer;

// normalization rules
public Production choice(Symbol s, {set[Production] a, choice(Symbol t, set[Production] b)})
  = choice(s, a+b);
  
public Production priority(Symbol s, [list[Production] a, priority(Symbol t, list[Production] b),list[Production] c])
  = priority(s,a+b+c);
   
public Production associativity(Symbol s, Associativity as, {set[Production] a, choice(Symbol t, set[Production] b)}) 
  = associativity(s, as, a+b); 
  
public Production choice(Symbol s, {others(Symbol t)}) 
  = others(t);
             
public Production associativity(Symbol rhs, Associativity a, {associativity(Symbol rhs2, Associativity b, set[Production] alts), set[Production] rest}) 
  = associativity(rhs, a, rest + alts);

public Production associativity(Symbol s, Associativity as, {set[Production] a, priority(Symbol t, list[Production] b)}) 
  = associativity(s, as, a + { e | e <- b}); 
 
public Production choice(Symbol s, {set[Production] a, others(Symbol t)}) {
  if (t == s) 
    return choice(s, a);
  else 
    fail;
}

public Production choice(Symbol s, {set[Production] a, priority(Symbol t, [list[Production] b, others(Symbol u), list[Production] c])}) 
  = priority(s, b + [choice(s, a)] + c);
  
public Production  attrs([]) 
  = \no-attrs();


public Grammar syntax2grammar(set[SyntaxDefinition] defs) {
  set[Production] prods = {};
  set[Symbol] starts = {};
  
  for (sd <- defs) {
    switch (sd) {
      case (SyntaxDefinition) `layout <Nonterminal n> = <Prod p>;` : {
        prods += prod2prod(\layouts("<n>"), p);
      }
      case (SyntaxDefinition) `start syntax <Nonterminal n> = <Prod p>;` : {
        Symbol top = sort("<n>");
        prods  += prod([top], start(top), \no-attrs()); 
        prods  += prod2prod(top, p);
      }
      case (SyntaxDefinition) `syntax <Nonterminal n> = <Prod p>;` : {
        prods += prod2prod(sort("<n>"), p);
      }
      case (SyntaxDefinition) `lexical <Nonterminal n> = <Prod p>;` : {
        prods += lexical(prod2prod(sort("<n>"), p));
      }
      case (SyntaxDefinition) `keyword <Nonterminal n> = <Prod p>;` : {
        prods += prod2prod(keywords("<n>"), p);
      }
    }
  }

  return grammar(starts, prods);
} 
   
public Grammar literals(Grammar g) {
  return compose(g, grammar({}, {prod(str2syms(s),lit(s),attrs([\literal()])) | /lit(s) <- g}
                              + {prod(cistr2syms(s),cilit(s),attrs([\ciliteral()])) | /cilit(s) <- g}));
}

public set[Production] lexical(set[Production] prods) {
  return visit(prods) {
    case p:prod(_,_,_) => attribute(p, \lex())
  } 
}

public set[Production] \layouts(set[Production] prods, str layoutName) {
  return top-down-break visit (prods) {
    case prod(list[Symbol] lhs,Symbol rhs,attrs(list[Attr] as)) => prod(intermix(lhs, layoutName),rhs,attrs(as)) 
      when start(_) !:= rhs, \lex() notin as  
    case prod(list[Symbol] lhs,Symbol rhs,\no-attrs()) => prod(intermix(lhs, layoutName),rhs,\no-attrs()) 
      when start(_) !:= rhs
  }
}  

public list[Symbol] str2syms(str x) {
  // TODO: escaping?
  if (x == "") return [];
  return [\char-class([range(c,c)]) | i <- [0..size(x)-1], int c:= charAt(x,i)]; 
}

private list[Symbol] cistr2syms(str x) {
  return for (i <- [0..size(x)-1], int c:= charAt(x,i)) {
     if (c >= 101 && c <= 132) // A-Z
        append \char-class([range(c,c),range(c+40,c+40)]);
     else if (c >= 141 && c <= 172) // a-z
        append \char-class([range(c,c),range(c-40,c-40)]);
     else 
        append \char-class([range(c,c)]);
  } 
}

private list[Symbol] intermix(list[Symbol] syms, str layoutName) {
  if (syms == []) return syms;
  return tail([\layouts(layoutName), s | s <- syms]);
}

private Production prod2prod(Symbol nt, Prod p) {
  switch(p) {
    case (Prod) `<ProdModifier* ms> <Name n> : <Sym* args>` :
      return prod(args2symbols(args), nt, mods2attrs(n, ms));
    case (Prod) `<ProdModifier* ms> <Sym* args>` :
      return prod(args2symbols(args), nt, mods2attrs(ms));
    case (Prod) `<Prod l> | <Prod r>` :
      return choice(nt,{prod2prod(nt, l), prod2prod(nt, r)});
    case (Prod) `<Prod l> > <Prod r>` : 
      return priority(nt,[prod2prod(nt, l), prod2prod(nt, r)]);
    case (Prod) `left (<Prod q>)` :
      return associativity(nt, \left(), {attribute(prod2prod(nt, q), \assoc(\left()))});
    case (Prod) `right (<Prod q>)` :
      return associativity(nt, \right(), {attribute(prod2prod(nt, q), \assoc(\right()))});
    case (Prod) `non-assoc (<Prod q>)` :
      return associativity(nt, \non-assoc(), {attribute(prod2prod(nt, q), \assoc(\non-assoc()))});
    case (Prod) `assoc(<Prod q>)` :
      return associativity(nt, \left(), {attribute(prod2prod(nt, q),\assoc(\assoc()))});
    case (Prod) `...`: return \others(nt);
    case (Prod) `: <Name n>`: return \reference(nt, "<n>");
    default: throw "missed a case <p>";
  } 
}

@doc{adds an attribute to all productions it can find}
private Production attribute(Production p, Attr a) {
  return visit (p) {
    case prod(lhs,rhs,\no-attrs()) => prod(lhs, rhs, attrs([a]))
    case prod(lhs,rhs,attrs(list[Attributes] l)) => prod(lhs, rhs, attrs([l, a]))
  }
}

private list[Symbol] args2symbols(Sym* args) {
  return [arg2symbol(s) | Sym s <- args];
}

private list[Symbol] separgs2symbols({Sym ","}+ args) {
  return [arg2symbol(s) | Sym s <- args];
}
   
private Symbol arg2symbol(Sym sym) {
  switch (sym) {
    case (Sym) `^` : 
      return \start-of-line();
    case (Sym) `$` : 
      return \end-of-line();
    case (Sym) `<Sym s> >> <Sym r>` : 
      return follow(arg2symbol(s, isLex, layoutName), {arg2symbol(r, isLex, layoutName)});
    case (Sym) `<Sym s> !>> <Sym r>` : 
      return \not-follow(arg2symbol(s, isLex, layoutName), {arg2symbol(r, isLex, layoutName)});
    case (Sym) `<Sym s> << <Sym r>` : 
      return precede(arg2symbol(r, isLex, layoutName), {arg2symbol(s, isLex, layoutName)});
    case (Sym) `<Sym s> !<< <Sym r>` : 
      return \not-precede(arg2symbol(r, isLex, layoutName), {arg2symbol(s, isLex, layoutName)});
    case (Sym) `<Sym s> != <Sym r>` : 
      return \reserve(arg2symbol(s, isLex, layoutName), {arg2symbol(r, isLex, layoutName)});
    case (Sym) `@ <IntegerLiteral i>` : 
      return \at-column(toInt("<i>")); 
    case (Sym) `<Nonterminal n>`          : 
      return sort("<n>");
    case (Sym) `keyword[<Nonterminal n>]` : 
      return keywords("<n>");
    case (Sym) `layout[<Nonterminal n>]` : 
      return layouts("<n>");
    case (Sym) `start[<Nonterminal n>]` : 
      return start(sort("<n>"));
    case (Sym) `<StringConstant l>` : 
      return lit(unescape(l));
    case (Sym) `<CaseInsensitiveStringConstant l>`: 
      return cilit(unescape(l));
    case (Sym) `<ParameterizedNonterminal n>[<{Sym ","}+ syms>]` : 
      return \parameterized-sort("<n>",separgs2symbols(syms,isLex,layoutName));
    case (Sym) `<Sym s> <NonterminalLabel n>` : 
      return label("<n>", arg2symbol(s,isLex,layoutName));
    case (Sym) `<Sym s> ?`  : 
      return opt(arg2symbol(s,isLex,layoutName));
    case (Sym) `<Class cc>` : 
      return cc2ranges(cc);
    case (Sym) `&<Nonterminal n>` : 
      return \parameter("<n>");
    case (Sym) `()` : 
      return \empty();
    case (Sym) `( <Sym first> | <{Sym "|"}+ alts>)` : 
      return alt({arg2symbol(first,isLex,layoutName)} + {arg2symbol(elem,isLex,layoutName) | elem <- alts});
    case (Sym) `<Sym s> *`  : 
      return \iter-star(arg2symbol(s,isLex,layoutName));
    case (Sym) `<Sym s> +`  : 
      return \iter(arg2symbol(s,isLex,layoutName));
    case (Sym) `<Sym s> *?` : 
      return \iter-star(arg2symbol(s,isLex,layoutName));
    case (Sym) `<Sym s> +?` : 
      return \iter(arg2symbol(s,isLex,layoutName));
    case (Sym) `{<Sym s> <StringConstant sep>} *`  : 
      return \iter-star-seps(arg2symbol(s,isLex,layoutName), [lit(unescape(sep))]);
    case (Sym) `{<Sym s> <StringConstant sep>} +`  : 
      return \iter-seps(arg2symbol(s,isLex,layoutName), [lit(unescape(sep))]);
    case (Sym) `{<Sym s> <StringConstant sep>} *?` : 
      return \iter-star-seps(arg2symbol(s,isLex,layoutName), [lit(unescape(sep))]);
    case (Sym) `{<Sym s> <StringConstant sep>} +?` : 
      return \iter-seps(arg2symbol(s,isLex,layoutName), [lit(unescape(sep))]);
    case (Sym) `(<Sym first> <Sym+ sequence>)` : 
      return seq([arg2symbol(first,isLex,layoutName)] + [arg2symbol(elem,isLex,layoutName) | elem <- sequence]);
    default: 
      throw "missed a case <sym>";
  }
}
  
private str unescape(StringConstant s) {
   if ([StringConstant] /\"<rest:.*>\"/ := s) {
     return visit (rest) {
       case /\\b/ => "\b"
       case /\\f/ => "\f"
       case /\\n/ => "\n"
       case /\\t/ => "\t"
       case /\\r/ => "\r"  
       case /\\\"/ => "\""  
       case /\\\'/ => "\'"
       case /\\\\/ => "\\"
       case /\\\</ => "\<"   
       case /\\\>/ => "\>"    
     };      
   }
   throw "unexpected string format: <s>";
}

private Symbol cc2ranges(Class cc) {
   switch(cc) {
     case (Class) `[<Range* ranges>]` : return \char-class([range(r) | r <- ranges]);
     case (Class) `(<Class c>)`: return cc2ranges(c);
     case (Class) `! <Class c>`: return complement(cc2ranges(c));
     case (Class) `<Class l> && <Class r>`: return intersection(cc2ranges(l), cc2ranges(r));
     case (Class) `<Class l> || <Class r>`: return union(cc2ranges(l), cc2ranges(r));
     case (Class) `<Class l> - <Class r>`: return difference(cc2ranges(l), cc2ranges(r));
     default: throw "missed a case <cc>";
   }
}
      
private CharRange range(Range r) {
  switch (r) {
    case (Range) `<Char c>` : return range(character(c),character(c));
    case (Range) `<Char l> - <Char r>`: return range(character(l),character(r));
    default: throw "missed a case <r>";
  }
} 
 
private int character(Char c) {
  switch (c) {
    case [Char] /\\n/ : return charAt("\n", 0);
    case [Char] /\\t/ : return charAt("\t", 0);
    case [Char] /\\b/ : return charAt("\b", 0);
    case [Char] /\\r/ : return charAt("\r", 0);
    case [Char] /\\f/ : return charAt("\f", 0);
    case [Char] /\\\>/ : return charAt("\>", 0);
    case [Char] /\\\</ : return charAt("\<", 0);
    case [Char] /<ch:[^"'\-\[\]\\\>\< ]>/        : return charAt(ch, 0); 
    case [Char] /\\<esc:["'\-\[\]\\ ]>/        : return charAt(esc, 0);
    case [Char] /\\[u]+<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ : return toInt("0x<hex>");
    case [Char] /\\<oct:[0-3][0-7][0-7]>/ : return toInt("0<oct>");
    case [Char] /\\<oct:[0-7][0-7]>/      : return toInt("0<oct>");
    case [Char] /\\<oct:[0-7]>/           : return toInt("0<oct>");
    default: throw "missed a case <c>";
  }
}

private Attributes mods2attrs(Name name, ProdModifier* mods) {
  return attrs([term("cons"("<name>"))] + [ mod2attr(m) | m <- mods]);
}

private Attributes mods2attrs(ProdModifier* mods) {
  return attrs([mod2attr(m) | ProdModifier m <- mods]);
}
 
private Attr mod2attr(ProdModifier m) {
  switch (m) {
    case (ProdModifier) `lex`: return \lex();
    case (ProdModifier) `left`: return \assoc(\left());
    case (ProdModifier) `right`: return \assoc(\right());
    case (ProdModifier) `non-assoc`: return \assoc(\non-assoc());
    case (ProdModifier) `assoc`: return \assoc(\assoc());
    case (ProdModifier) `bracket`: return \bracket();
    case (ProdModifier) `@ <Name n> = <StringConstant s>` : return \term("<n>"(unescape(s)));
    case (ProdModifier) `@ <Name n> = <Literal l>` : return \term("<n>"("<l>"));
    case (ProdModifier) `@ <Name n>` : return \term("<n>"());
    case (ProdModifier) `@ <Name n> <TagString s>` : return \term("<n>"("<s>"));
    default: throw "missed a case <m>";
  }
}


