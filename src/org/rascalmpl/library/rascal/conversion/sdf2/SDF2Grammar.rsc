module SDF2Grammar

// this module converts SDF2 grammars to the Rascal internal grammar representation format

import IO;
import rascal::parser::Definition;
import languages::sdf2::syntax::Sdf2;

public Grammar sdf2grammar(loc input) {
  return sdf2grammar(parse(#SDF, input)); 
}

public Grammar sdf2grammar(SDF definition) {
  return grammar(getStartSymbols(definition), getProductions(definition));
}

public set[ParseTree::Production] getProductions(SDF definition) {
  return getLexical(definition) 
       + getContextFree(definition) 
       + getPriorities(definition) 
       + getRestrictions(definition);
}

public set[ParseTree::Production] getLexical(SDF definition) {
  return {};
}

public set[ParseTree::Production] getContextFree(SDF definition) {
  return {};
}

public set[ParseTree::Production] getPriorities(SDF definition) {
  return {};
}

public set[ParseTree::Production] getRestrictions(SDF definition) {
  return {};
}

public set[ParseTree::Symbol] getStartSymbols(SDF definition) {
  return { getSymbol(sym, false) | /(Grammar) `context-free start-symbols <Symbol* syms>` := definition, sym <- syms}
       + { getSymbol(sym, true) | /(Grammar) `lexical start-symbols <Symbol* syms>` := definition, sym <- syms}
       + { getSymbol(sym, true) | /(Grammar) `start-symbols <Symbol* syms>` := definition, sym <- syms};
}

private ParseTree::Symbol getSymbol(languages::sdf2::syntax::Sdf2::Symbol sym, bool isLex) {
  switch (sym) {
    case (Symbol) `<Sort n>`   : return sort("<n>");
    case (Symbol) `<StrCon l>` : return lit(unescape(l));
    case (Symbol) `<SingleQuotedStrCon l>` : return cilit(unescape(l));
    case (Symbol) `<Sort n>[[<{Symbol ","}+ syms>]]` : return \parametrized-sort("<n>",separgs2symbols(syms,isLex));
    // case (Symbol) `<StrCon l> : <Symbol s>` : return label(unescape(l), getSymbol(s,isLex));
    // case (Symbol) `<IdCon i> : <Symbol s>` : return label("<i>", getSymbol(s, isLex));
    // case (Symbol) `<Symbol s> ?`  : return opt(getSymbol(s,isLex));
    // case (Symbol) `<Class cc>` : return \char-class(cc2ranges(cc));
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
       case /\\\</ => "\<"   
       case /\\\>/ => "\>"    
     };      
   }
   throw "unexpected string format: <s>";
}

private list[CharRange] cc2ranges(Class cc) {
   switch(cc) {
     case (CharClass) `[<CharRange* ranges>]` : return [range(r) | r <- ranges];
     case (CharClass) `(<CharClass c>)`: return cc2ranges(cc2ranges(c));
     case (CharClass) `~ <CharClass c>`: return complement(cc2ranges(c));
     case (CharClass) `<CharClass l> /\ <CharClass r>`: return intersection(cc2ranges(l),cc2ranges(r));
     case (CharClass) `<CharClass l> \/ <CharClass r>`: return union(cc2ranges(l),cc2ranges(r));
     case (CharClass) `<CharClass l> / <CharClass r>`: return difference(cc2ranges(l),cc2ranges(r));
     default: throw "missed a case <cc>";
   }
}
      
private CharRange range(Range r) {
  switch (r) {
    case (CharRange) `<Character c>` : return range(character(c),character(c));
    case (CharRange) `<Character l> - <Character r>`: return range(character(l),character(r));
    default: throw "missed a case <r>";
  }
} 

private int character(Character c) {
  switch (c) {
    case [Character] /<ch:[^"'\-\[\] ]>/        : return charAt(ch, 0); 
    case [Character] /\\<esc:["'\-\[\] ]>/        : return charAt(esc, 0);
    case [Character] /\\<oct:[0-7]>/           : return toInt("0<oct>");
    case [Character] /\\<oct:[0-7][0-7]>/      : return toInt("0<oct>");
    case [Character] /\\<oct:[0-3][0-7][0-7]>/ : return toInt("0<oct>");
    default: throw "missed a case <c>";
  }
}

private Attributes mods2attrs(Name name, Attribute* mods) {
  return attrs([term(cons("<name>"))]);
}

private Attributes mods2attrs(Attribute* mods) {
  return attrs([mod2attr(m) | ProdModifier m <- mods]);
}
 
private Attr mod2attr(Attribute m) {
  switch (m) {
    case (Attribute) `left`: return \assoc(left());
    case (Attribute) `right`: return \assoc(right());
    case (Attribute) `non-assoc`: return \assoc(\non-assoc());
    case (Attribute) `assoc`: return \assoc(\assoc());
    case (Attribute) `bracket`: return bracket();
    default: throw "missed a case <m>";
  }
}