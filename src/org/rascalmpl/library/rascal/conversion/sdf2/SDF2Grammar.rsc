module SDF2Grammar

// this module converts SDF2 grammars to the Rascal internal grammar representation format

import IO;
import rascal::parser::Definition;
import languages::sdf2::syntax::\Sdf2-Syntax;

public Grammar sdf2grammar(loc input) {
  return sdf2grammar(parse(#SDF, input)); 
}

public Grammar sdf2grammar(SDF definition) {
  return grammar(getStartSymbols(definition), getProductions(definition));
}

public set[ParseTree::Production] getProductions(SDF definition) {
  visit (definition) {
    case (Grammar) `syntax <Production* prods>`                     : return {getProduction(prod, true) | prod <- prods};
    case (Grammar) `lexical syntax <Production* prods>`             : return {getProduction(prod, true) | prod <- prods}; 
    case (Grammar) `context-free syntax <Production* prods>`        : return {getProduction(prod, false) | prod <- prods}; 
    case (Grammar) `restrictions <Restriction* rests>`              : return {getRestrictions(rest, true) | rest <- rests};
    case (Grammar) `lexical restrictions <Restriction* rests>`      : return {getRestrictions(rest, true) | rest <- rests};
    case (Grammar) `context-free restrictions <Restriction* rests>` : return {getRestrictions(rest, false) | rest <- rests};
    case (Grammar) `priorities <Priority* prios>`                   : return {getPriorities(prio,true) | prio <- prios};
    case (Grammar) `lexical priorities <Priority* prios>`           : return {getPriorities(prio,true) | prio <- prios};
    case (Grammar) `context-free priorities <Priority* prios>`      : return {getPriorities(prio,false) | prio <- prios};
  }
}

public Production getProduction(languages::sdf2::syntax::Sdf2::Production prod) {
  switch (prod) {
    case (Production) `<Symbol* syms> -> <Symbol sym>`                      : return prod(getSymbols(syms),getSymbol(sym),\no-attrs());
    case (Production) `<Symbol* syms> -> <Symbol sym> {<Attribute* attrs>}` : return prod(getSymbols(syms),getSymbol(sym),getAttributes(attrs));
    case (Production) `<IdCon id> (<{Symbol ","}* syms>) -> <Symbol sym>`                      : throw "prefix functions not supported by SDF import yet";
    case (Production) `<IdCon id> (<{Symbol ","}* syms>) -> <Symbol sym> {<Attribute* attrs>}` : throw "prefix functions not supported by SDF import yet";
    case (Production) `<StrCon s> (<{Symbol ","}* syms>) -> <Symbol sym>`                      : throw "prefix functions not supported by SDF import yet";
    case (Production) `<StrCon s> (<{Symbol ","}* syms>) -> <Symbol sym> {<Attribute* attrs>}` : throw "prefix functions not supported by SDF import yet";
  }
}

public set[Production] getRestrictions(Restriction* restrictions, bool isLex) {
  return { getRestrictions(r) | r <- restrictions };
}

public set[Production] getRestrictions(Restriction restriction, bool isLex) {
  switch (restriction) {
    case (Restriction) `-/- <Lookahead* ls>` : return {};
    case (Restriction) `<Symbol s1> <Symbol s2> <Symbol* rest> -/- <Lookaheads ls>` : 
      return getRestrictions((Restriction) `<Symbol s1> -/- <Lookaheads ls>`, isLex) 
           + getRestrictions((Restriction) `<Symbol s2> <Symbol* rest> -/- <Lookaheads ls>`, isLex);
    case (Restriction) `<Symbol s1> -/- <Lookahead* ls>` :
      return {restrict(getSymbol(s1), others(getSymbol(s1)), r) | r <- getLookaheads(ls)};
  }
}

public set[list[CharClass]] getLookaheads(Lookaheads ls) {
   switch (ls) {
     case (Lookaheads) `<CharClass c>`                   : return {[getCharClass(c)]};
     case (Lookaheads) `<CharClass c>.<Lookaheads ls>`   : return {[getCharClass(c)] + other | other <- getLookaheads(ls)};
     case (Lookaheads) `<Lookaheads l> | <Lookaheads r>` : return getLookaheads(l) + getLookaheads(r);
     case (Lookaheads) `(<Lookaheads(l)>)`               : return getLookaheads(l);
     case (Lookaheads) `[[<{Lookahead ","}* _>]]`        : throw "unsupported lookahead construction <ls>";
   }
}

public set[Production] getPriorities(Priority* priorities) {
  return {getPriority(p) | p <- priorities};
}

public Production getPriority(Priority priority, boolean isLex) {
   switch (priority) {
     case (Priority) `<Production p>` : 
       return getProduction(p, isLex);
     case (Priority) `{<Production* ps>}` : 
       return choice(definedSymbol(ps,isLex), [getProduction(p,isLex) | p <- ps]);
     case (Priority) `{<Associativity a> : <Production* ps>}` : 
       return assoc(definedSymbol(ps, isLex), getAssociativity(a), {getPriority(p,isLex), p <- ps});
     case (Priority) `<Group g1> <Associativity a> <Group g2>` : 
       return assoc(definedSymbol(g1, isLex), getAssociativity(a), {getPriority((Priority) `<Group g1>`, isLex), getPriority((Priority) `<Group g2>`,isLex)});
     case (Priority) `<{Group ">"}+ groups>` : 
       return first(definedSymbol(groups,isLex), [getPriority(group,isLex) | group <- groups]);
   }
}

public Symbol definedSymbol((&T <: Tree) v, bool isLex) {
  // Note that this might not work if there are different right-hand sides in the group...
  // I don't know what to do about this yet.
  if (/(Production) `<Symbol* _> -> <Symbol s> <Attributes _>` := groups) {
    return getSymbol(s, isLex);
  }
  throw "could not find a defined symbol in <v>";
}

public set[ParseTree::Symbol] getStartSymbols(SDF definition) {
  visit(definition) {
    case (Grammar) `context-free start-symbols <Symbol* syms>` : return { getSymbol(sym) | sym <- syms };
    case (Grammar) `lexical start-symbols <Symbol* syms>`      : return { getSymbol(sym) | sym <- syms };
    case (Grammar) `start-symbols <Symbol* syms>`              : return { getSymbol(sym) | sym <- syms };
  }
}

private ParseTree::Symbol getSymbol(languages::sdf2::syntax::Sdf2::Symbol sym, bool isLex) {
  switch (sym) {
    case (Symbol) `<StrCon l> : <Symbol s>`          : return label(unescape(l), getSymbol(s,isLex));
    case (Symbol) `<IdCon i> : <Symbol s>`           : return label("<i>", getSymbol(s, isLex));
    case (Symbol) `<Sort n>`                         : return sort("<n>");
    case (Symbol) `<StrCon l>`                       : return lit(unescape(l));
    case (Symbol) `<SingleQuotedStrCon l>`           : return cilit(unescape(l));  
    case (Symbol) `<Sort n>[[<{Symbol ","}+ syms>]]` : return \parametrized-sort("<n>",separgs2symbols(syms,isLex));
    case (Symbol) `<Symbol s> ?`                     : return opt(getSymbol(s,isLex));
    case (Symbol) `<CharClass cc>`                   : return getCharClass(cc);
  }  
  
  if (isLex) switch (sym) {
    case (Symbol) `<Symbol s> *`  : return \iter-star(getSymbol(s,isLex));
    case (Symbol) `<Symbol s> +`  : return \iter(getSymbol(s,isLex));
    case (Symbol) `<Symbol s> *?` : return \iter-star(getSymbol(s,isLex));
    case (Symbol) `<Symbol s> +?` : return \iter(getSymbol(s,isLex));
    case (Symbol) `{<Symbol s> <Symbol sep>} *`  : return \iter-star-sep(getSymbol(s,isLex), [lit(unescape(sep))]);
    case (Symbol) `{<Symbol s> <Symbol sep>} +`  : return \iter-sep(getSymbol(s,isLex), [lit(unescape(sep))]);
    case (Symbol) `{<Symbol s> <Symbol sep>} *?` : return \iter-star-sep(getSymbol(s,isLex), [lit(unescape(sep))]);
    case (Symbol) `{<Symbol s> <Symbol sep>} +?` : return \iter-sep(getSymbol(s,isLex), [lit(unescape(sep))]);
    default: throw "missed a case <sym>";
  } 
  else switch (sym) {  
    case (Symbol) `<Symbol s> *`  : return \iter-star-sep(getSymbol(s,isLex),[layout()]);
    case (Symbol) `<Symbol s> +`  : return \iter-sep(getSymbol(s,isLex),[layout()]);
    case (Symbol) `<Symbol s> *?` : return \iter-star-sep(getSymbol(s,isLex),[layout()]);
    case (Symbol) `<Symbol s> +?` : return \iter-sep(getSymbol(s,isLex),[layout()]);
    case (Symbol) `{<Symbol s> <Symbol sep>} *`  : return \iter-star-sep(getSymbol(s,isLex), [layout(),getSymbol(sep, isLex),layout()]);
    case (Symbol) `{<Symbol s> <Symbol sep>} +`  : return \iter-sep(getSymbol(s,isLex), [layout(),getSymbol(sep, isLex),layout()]);
    case (Symbol) `{<Symbol s> <Symbol sep>} *?` : return \iter-star-sep(getSymbol(s,isLex), [layout(),getSymbol(sep, isLex),layout()]);
    case (Symbol) `{<Symbol s> <Symbol sep>} +?` : return \iter-sep(getSymbol(s,isLex), [layout(),getSymbol(sep, isLex),layout()]);
    default: throw "missed a case <sym>";  
  }
}
  
private str unescape(StrCon s) {
   if ([StrCon] /\"<rest:.*>\"/ := s) {
     return visit (rest) {
       case /\\b/ => "\b"
       case /\\f/ => "\f"
       case /\\n/ => "\n"
       case /\\t/ => "\t"
       case /\\r/ => "\r"  
       case /\\\"/ => "\""  
       case /\\\'/ => "\'"
       case /\\\\/ => "\\"
     };      
   }
   throw "unexpected string format: <s>";
}

private Symbol getCharClass(CharClass cc) {
   switch(cc) {
     case (CharClass) `[<CharRange* ranges>]` : return \char-class([range(r) | r <- ranges]);
     case (CharClass) `(<CharClass c>)`: return getCharClass(c);
     case (CharClass) `~ <CharClass c>`: return complement(getCharClass(c));
     case (CharClass) `<CharClass l> /\ <CharClass r>`: return intersection(getCharClass(l),getCharClass(r));
     case (CharClass) `<CharClass l> \/ <CharClass r>`: return union(getCharClass(l),getCharClass(r));
     case (CharClass) `<CharClass l> / <CharClass r>`: return difference(getCharClass(l),getCharClass(r));
     default: throw "missed a case <cc>";
   }
}
      
private CharRange getRange(CharRange r) {
  switch (r) {
    case (CharRange) `<Character c>` : return range(getCharacter(c),getCharacter(c));
    case (CharRange) `<Character l> - <Character r>`: return range(getCharacter(l),getCharacter(r));
    default: throw "missed a case <r>";
  }
} 

private int getCharacter(Character c) {
  switch (c) {
    case [Character] /<ch:[^"'\-\[\] ]>/       : return charAt(ch, 0); 
    case [Character] /\\<esc:["'\-\[\] ]>/     : return charAt(esc, 0);
    case [Character] /\\<oct:[0-7]>/           : return toInt("0<oct>");
    case [Character] /\\<oct:[0-7][0-7]>/      : return toInt("0<oct>");
    case [Character] /\\<oct:[0-3][0-7][0-7]>/ : return toInt("0<oct>");
    default: throw "missed a case <c>";
  }
}

private Attributes getAttributes(Attribute* mods) {
  return attrs([getAttribute(m) | Attribute m <- mods]);
}
 
private Attr getAttribute(Attribute m) {
  switch (m) {
    case (Attribute) `left`: return \assoc(left());
    case (Attribute) `right`: return \assoc(right());
    case (Attribute) `non-assoc`: return \assoc(\non-assoc());
    case (Attribute) `assoc`: return \assoc(\assoc());
    case (Attribute) `bracket`: return bracket();
    case (Attribute) `cons(<StrCon c>)` : return term("cons"(unescape(c)));
    default: throw "missed a case <m>";
  }
}