module rascal::conversion::sdf2::SDF2Grammar

// this module converts SDF2 grammars to the Rascal internal grammar representation format

// Todo List:
// - aliases
// - (lexical) variables

import IO;
import String;
import Integer;
import ParseTree;
import rascal::parser::Grammar;
import rascal::parser::Definition;
import rascal::conversion::sdf2::Load;
import languages::sdf2::syntax::\Sdf2-Syntax;
test true == false;
// Resolve name clashes between the ParseTree and Grammar datatypes.

// Unfortunately we cannot yet use these aliases since they lead to ambiguities.

alias SDFSymbol = languages::sdf2::syntax::\Sdf2-Syntax::Symbol;
alias SDFProduction = languages::sdf2::syntax::\Sdf2-Syntax::Production;
alias SDFStrCon = languages::sdf2::syntax::\Sdf2-Syntax::StrCon;
alias SDFSingleQuotesStrCon = languages::sdf2::syntax::\Sdf2-Syntax::SingleQuotedStrCon;
alias SDFCharRange = languages::sdf2::syntax::\Sdf2-Syntax::CharRange;
alias SDFCharClass =languages::sdf2::syntax::\Sdf2-Syntax::CharClass;

//import rascal::conversion::sdf2::Pico;

public Grammar sdf2grammar(loc input) {
  return sdf2grammar(parse(#SDF, input)); 
}

public Grammar sdf2module2grammar(str name, list[loc] path) {
  return sdf2grammar(loadSDF2Module(name, path));
}

public Grammar sdf2grammar(SDF definition) {
  return grammar(getStartSymbols(definition), getProductions(definition));
}

public set[ParseTree::Production] getProductions(SDF definition) {
  visit (definition) {
    case (Grammar) `syntax <languages::sdf2::syntax::\Sdf2-Syntax::Production* prods>`                     : return {getProduction(prod, true) | prod <- prods};
    case (Grammar) `lexical syntax <languages::sdf2::syntax::\Sdf2-Syntax::Production* prods>`             : return {getProduction(prod, true) | prod <- prods}; 
    case (Grammar) `context-free syntax <languages::sdf2::syntax::\Sdf2-Syntax::Production* prods>`        : return {getProduction(prod, false) | prod <- prods}; 
    case (Grammar) `restrictions <languages::sdf2::syntax::\Sdf2-Syntax::Restriction* rests>`              : return {getRestrictions(rest, true) | rest <- rests};
    case (Grammar) `lexical restrictions <languages::sdf2::syntax::\Sdf2-Syntax::Restriction* rests>`      : return {getRestrictions(rest, true) | rest <- rests};
    case (Grammar) `context-free restrictions <languages::sdf2::syntax::\Sdf2-Syntax::Restriction* rests>` : return {getRestrictions(rest, false) | rest <- rests};
    case (Grammar) `priorities <{languages::sdf2::syntax::\Sdf2-Syntax::Priority ","}* prios>`             : return {getPriorities(prio,true) | prio <- prios};
    case (Grammar) `lexical priorities <{languages::sdf2::syntax::\Sdf2-Syntax::Priority ","}* prios>`     : return {getPriorities(prio,true) | prio <- prios};
    case (Grammar) `context-free priorities <{languages::sdf2::syntax::\Sdf2-Syntax::Priority ","}* prios>`: return {getPriorities(prio,false) | prio <- prios};
  }
}

//test getProductions((SDF) `definition module A exports lexical syntax A -> B`) == 

public Production getProduction(SDFProduction P, bool isLex) {
  switch (P) {
    case (Production) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol* syms> -> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sym>`:
    	return prod(getSymbols(syms, isLex),getSymbol(sym, isLex),\no-attrs());
    	
    case (Production) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol* syms> -> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sym> {<{languages::sdf2::syntax::\Sdf2-Syntax::Attribute ","}* attrs>}` :
    	return prod(getSymbols(syms, isLex),getSymbol(sym, isLex),getAttributes(attrs));
    	
    case (Production) `<IdCon id> (<{languages::sdf2::syntax::\Sdf2-Syntax::Symbol ","}* syms>) -> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sym>`:
    	throw "prefix functions not supported by SDF import yet";
    	
    case (Production) `<IdCon id> (<{languages::sdf2::syntax::\Sdf2-Syntax::Symbol ","}* syms>) -> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sym> {<{languages::sdf2::syntax::\Sdf2-Syntax::Attribute ","}* attrs>}` :
    	throw "prefix functions not supported by SDF import yet";
    	
    case (Production) `<StrCon s> (<{languages::sdf2::syntax::\Sdf2-Syntax::Symbol ","}* syms>) -> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sym>`:
    	throw "prefix functions not supported by SDF import yet";
    	
    case (Production) `<StrCon s> (<{languages::sdf2::syntax::\Sdf2-Syntax::Symbol ","}* syms>) -> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sym> {<{Attribute ","}* attrs>}` :
    	throw "prefix functions not supported by SDF import yet";
  }
}

test getProduction((Production) `PICO-ID ":" TYPE -> ID-TYPE`, true) == 
     prod([sort("PICO-ID"),lit(":"),sort("TYPE")],sort("ID-TYPE"),\no-attrs());
     
test getProduction((Production) `PICO-ID ":" TYPE -> ID-TYPE {cons("decl"), left}`, true) == 
     prod([sort("PICO-ID"),lit(":"),sort("TYPE")],sort("ID-TYPE"),attrs([term(cons("decl")),assoc(left())]));

public set[Production] getRestrictions(languages::sdf2::syntax::\Sdf2-Syntax::Restriction* restrictions, bool isLex) {
  return { getRestrictions(r) | r <- restrictions };
}

public set[Production] getRestriction(languages::sdf2::syntax::\Sdf2-Syntax::Restriction restriction, bool isLex) {
  switch (restriction) {
    case (Restriction) `-/- <languages::sdf2::syntax::\Sdf2-Syntax::Lookaheads ls>` :
    	return {};
    	
    case (Restriction) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s1> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol s2> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol* rest> -/- <languages::sdf2::syntax::\Sdf2-Syntax::Lookaheads ls>` : 
      return getRestrictions((Restriction) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s1> -/- <languages::sdf2::syntax::\Sdf2-Syntax::Lookaheads ls>`, isLex) 
           + getRestrictions((Restriction) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s2> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol* rest> -/- <languages::sdf2::syntax::\Sdf2-Syntax::Lookaheads ls>`, isLex);
           
    case (Restriction) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s1> -/- <languages::sdf2::syntax::\Sdf2-Syntax::Lookaheads ls>` :
      return {restrict(getSymbol(s1, isLex), others(getSymbol(s1, isLex)), r) | r <- getLookaheads(ls)};
  }
}

test getRestriction((Restriction) `-/- [a-z]`, true) == {};
test getRestriction((Restriction) `ID -/- [a-z]`, true) == 
     {restrict(sort("ID"),others(sort("ID")),[\char-class([range(97,122)])])};
     
// test getRestriction((Restriction) `ID NUM -/- [a-z] . [\"]`, true) 

public set[list[Symbol]] getLookaheads(languages::sdf2::syntax::\Sdf2-Syntax::Lookaheads ls) {
   switch (ls) {
     case (Lookaheads) `<languages::sdf2::syntax::\Sdf2-Syntax::CharClass c>` :
     	return {[getCharClass(c)]};
     	
     case (Lookaheads) `<languages::sdf2::syntax::\Sdf2-Syntax::CharClass c>.<languages::sdf2::syntax::\Sdf2-Syntax::Lookaheads ls>` :
     	return {[getCharClass(c)] + other | other <- getLookaheads(ls)};
     	
     case (Lookaheads) `<languages::sdf2::syntax::\Sdf2-Syntax::Lookaheads l> | <languages::sdf2::syntax::\Sdf2-Syntax::Lookaheads r>` :
     	return getLookaheads(l) + getLookaheads(r);
     	
     case (Lookaheads) `(<languages::sdf2::syntax::\Sdf2-Syntax::Lookaheads(l)>)` :
     	return getLookaheads(l);
     	
     case (Lookaheads) `[[<{languages::sdf2::syntax::\Sdf2-Syntax::Lookahead ","}* _>]]`        : throw "unsupported lookahead construction <ls>";
     default: throw "unsupported lookahead construction <ls>";
   }
}

test getLookaheads((Lookaheads) `[a-z]`) == 
     {[\char-class([range(97,122)])]};
test getLookaheads((Lookaheads) `[a-z] . [0-9]`) ==
     {[\char-class([range(97,122)]),\char-class([range(48,57)])]};
test getLookaheads((Lookaheads) `[a-z] . [0-9] | [\"]`) ==
     {[\char-class([range(97,122)]),\char-class([range(48,57)])],[\char-class([range(34,34)])]};
test getLookaheads((Lookaheads) `([a-z])`) == 
     {[\char-class([range(97,122)])]};


public set[Production] getPriorities(Priority* priorities) {
  return {getPriority(p) | p <- priorities};
}

public Production getPriority(Priority priority, bool isLex) {
   switch (priority) {
     case (Priority) `<Group g>.` : return getPriority((Priority) `<Group g>`, isLex); // we ignore non-transitivity here!
     case (Priority) `<Group g> <ArgumentIndicator i>` : return getPriority((Priority) `<Group g>`, isLex); // we ignore argument indicators here!
     case (Priority) `<Production p>` :   
       return getProduction(p, isLex);
     case (Priority) `{<Production* ps>}` : 
       return choice(definedSymbol(ps,isLex), [getProduction(p,isLex) | p <- ps]);
     case (Priority) `{<Associativity a> : <Production* ps>}` : 
       return \assoc(definedSymbol(ps, isLex), getAssociativity(a), {getPriority(p,isLex), p <- ps});
     case (Priority) `<Group g1> <Associativity a> <Group g2>` : 
       return \assoc(definedSymbol(g1, isLex), getAssociativity(a), {getPriority((Priority) `<Group g1>`, isLex), getPriority((Priority) `<Group g2>`,isLex)});
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
    case (Grammar) `context-free start-symbols <languages::sdf2::syntax::\Sdf2-Syntax::Symbol* syms>` :
    	return { getSymbol(sym, true) | sym <- syms };
    case (Grammar) `lexical start-symbols <languages::sdf2::syntax::\Sdf2-Syntax::Symbol* syms>`      :
    	return { getSymbol(sym, true) | sym <- syms };
    case (Grammar) `start-symbols <languages::sdf2::syntax::\Sdf2-Syntax::Symbol* syms>`              :
    	return { getSymbol(sym, true) | sym <- syms };
  }
}

test getStartSymbols((SDF) `definition module M exports context-free start-symbols A B C`) == 
     {sort("A"), sort("B"), sort("C")};
test getStartSymbols((SDF) `definition module M exports lexical start-symbols A B C`) == 
     {sort("A"), sort("B"), sort("C")};
test getStartSymbols((SDF) `definition module M exports start-symbols A B C`) == 
     {sort("A"), sort("B"), sort("C")};

public ParseTree::Symbol getSymbol(languages::sdf2::syntax::\Sdf2-Syntax::Symbol sym, bool isLex) {
  switch (sym) {
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::StrCon l> : <languages::sdf2::syntax::\Sdf2-Syntax::Symbol s>`:
		return label(unescape(l), getSymbol(s,isLex));
		
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::IdCon i> : <languages::sdf2::syntax::\Sdf2-Syntax::Symbol s>`:
    	return label("<i>", getSymbol(s, isLex));
    	
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::Sort n>`:
    	return sort("<n>");
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::StrCon l>`:
    	return lit(unescape(l));
    	
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::SingleQuotedStrCon l>`:
    	return cilit(unescape(l));  
    	
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::Sort n>[[<{languages::sdf2::syntax::\Sdf2-Syntax::Symbol ","}+ syms>]]`:
    	return \parametrized-sort("<n>",separgs2symbols(syms,isLex));
    	
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> ?`:
    	return opt(getSymbol(s,isLex));
    	
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::CharClass cc>`:
    	return getCharClass(cc);
  }  
  
  if (isLex) switch (sym) {
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> *`: 
    	return \iter-star(getSymbol(s,isLex));
    	
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> +`  :
    	return \iter(getSymbol(s,isLex));
    	
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> *?` :
    	return \iter-star(getSymbol(s,isLex));
    	
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> +?` :
    	return \iter(getSymbol(s,isLex));
    	
    case (Symbol) `{<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sep>} *`  :
    	return \iter-star-seps(getSymbol(s,isLex), [lit(unescape(sep))]);
    	
    case (Symbol) `{<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sep>} +`  :
    	return \iter-seps(getSymbol(s,isLex), [lit(unescape(sep))]);
    	
    case (Symbol) `{<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sep>} *?` :
    	return \iter-star-seps(getSymbol(s,isLex), [lit(unescape(sep))]);
    	
    case (Symbol) `{<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sep>} +?` :
    	return \opt(\iter-sep(getSymbol(s,isLex), lit(unescape(sep))));
    	
    default: throw "missed a case <sym>";
  } 
  else switch (sym) {  
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> *`:
    	return \iter-star-seps(getSymbol(s,isLex),[\layout()]);
    	
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> +` :
    	return \iter-seps(getSymbol(s,isLex),[\layout()]);
    	
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> *?`:
    	return \iter-star-seps(getSymbol(s,isLex),[\layout()]);
    	
    case (Symbol) `<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> +?`:
    	return \iter-seps(getSymbol(s,isLex),[\layout()]);
    	
    case (Symbol) `{<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sep>} *` :
    	return \iter-star-seps(getSymbol(s,isLex), [\layout(),getSymbol(sep, isLex),\layout()]);
    	
    case (Symbol) `{<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sep>} +` :
    	return \iter-seps(getSymbol(s,isLex), [\layout(),getSymbol(sep, isLex),\layout()]);
    	
    case (Symbol) `{<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sep>} *?`:
    	return \iter-star-seps(getSymbol(s,isLex), [\layout(),getSymbol(sep, isLex),\layout()]);
    	
    case (Symbol) `{<languages::sdf2::syntax::\Sdf2-Syntax::Symbol s> <languages::sdf2::syntax::\Sdf2-Syntax::Symbol sep>} +?`:
    	return \iter-seps(getSymbol(s,isLex), [\layout(),getSymbol(sep, isLex),\layout()]);
    default: throw "missed a case <sym>";  
  }
}

test getSymbol((Symbol) `"abc"`, false) == lit("abc");
test getSymbol((Symbol) `ABC`, false) == sort("ABC");
test getSymbol((Symbol) `'abc'`, false) == cilit("abc");
test getSymbol((Symbol) `abc : ABC`, false) == label("abc",sort("ABC"));
test getSymbol((Symbol) `"abc" : ABC`, false) == label("abc",sort("ABC"));
//test getSymbol((Symbol) `A[[B]]`, false) == \parameterized-sort([sort("B")]);   // <== parameterized sort missing
test getSymbol((Symbol) `A?`, false) == opt(sort("A"));
test getSymbol((Symbol) `[a]`, false) == \char-class([range(97,97)]);

test getSymbol((Symbol) `A*`, false) == \iter-star-seps(sort("A"),[layout()]);
test getSymbol((Symbol) `A+`, false) == \iter-seps(sort("A"),[layout()]);
test getSymbol((Symbol) `A*?`, false) == opt(\iter-star-seps(sort("A"),[layout()]));
test getSymbol((Symbol) `A+?`, false) == opt(\iter-seps(sort("A"),[layout()]));

test getSymbol((Symbol) `{A "x"}*`, false) == \iter-star-seps(sort("A"),[layout(),lit("x"),layout()]);
test getSymbol((Symbol) `{A "x"}+`, false) == \iter-seps(sort("A"),[layout(),lit("x"),layout()]);
test getSymbol((Symbol) `{A "x"}*?`, false) == opt(\iter-star-seps(sort("A"),[layout(),lit("x"),layout()]));
test getSymbol((Symbol) `{A "x"}+?`, false) == opt(\iter-seps(sort("A"),[layout(),lit("x"),layout()]));

test getSymbol((Symbol) `A*`, true) == \iter-star(sort("A"));
test getSymbol((Symbol) `A+`, true) == \iter(sort("A"));
test getSymbol((Symbol) `A*?`, true) == opt(\iter-star(sort("A")));
test getSymbol((Symbol) `A+?`, true) == opt(\iter(sort("A")));

test getSymbol((Symbol) `{A "x"}*`, true) == \iter-star-seps(sort("A"),[lit("x")]);
test getSymbol((Symbol) `{A "x"}+`, true) == \iter-seps(sort("A"),[lit("x")]);
test getSymbol((Symbol) `{A "x"}*?`, true) == opt(\iter-star-seps(sort("A"),[lit("x")]));
test getSymbol((Symbol) `{A "x"}+?`, true) == opt(\iter-seps(sort("A"),[lit("x")]));


public list[Symbol] getSymbols(languages::sdf2::syntax::\Sdf2-Syntax::Symbol* syms, bool isLex){
    return [getSymbol(sym, isLex) | sym <- syms];
}

test getSymbols((Symbol*) `A B "ab"`, true) == [sort("A"), sort("B"), lit("ab")];

private str unescape(languages::sdf2::syntax::\Sdf2-Syntax::StrCon s) {
   if ([StrCon] /\"<rest:.*>\"/ := s) {
     return visit (rest) {
       case /\\b/           => "\b"
       case /\\f/           => "\f"
       case /\\n/           => "\n"
       case /\\t/           => "\t"
       case /\\r/           => "\r"  
       case /\\\"/          => "\""  
       case /\\\\/          => "\\"
       case /\\TOP/         => "\255"
       case /\\EOF/         => "\256"
       case /\\BOT/         => "\0"
       case /\\LABEL_START/ => "\257"
     };      
   }
   throw "unexpected string format: <s>";
}

test unescape((StrCon) `"abc"`)  == "abc";
test unescape((StrCon) `"a\nc"`) == "a\nc";
test unescape((StrCon) `"a\"c"`) == "a\"c";
test unescape((StrCon) `"a\\c"`) == "a\\c";

private str unescape(languages::sdf2::syntax::\Sdf2-Syntax::SingleQuotedStrCon s) {
   if ([SingleQuotedStrCon] /\'<rest:.*>\'/ := s) {
     return visit (rest) {
       case /\\b/           => "\b"
       case /\\f/           => "\f"
       case /\\n/           => "\n"
       case /\\t/           => "\t"
       case /\\r/           => "\r"  
       case /\\\'/          => "'"  
       case /\\\\/          => "\\"
     };      
   }
   throw "unexpected string format: <s>";
}

test unescape((SingleQuotedStrCon) `'abc'`)  == "abc";
test unescape((SingleQuotedStrCon) `'a\nc'`) == "a\nc";
test unescape((SingleQuotedStrCon) `'a\'c'`) == "a'c";
test unescape((SingleQuotedStrCon) `'a\\c'`) == "a\\c";

// Unescape on Symbols. Note that the function below can currently coexist with the above to unescape functions
// since StrCon and SingleQuotedStrCons are *not* a subtype of Symbol (which they should be).
// Do a deep match (/) to skip the chain function that syntactically includes both subtypes in Symbol.

private str unescape(languages::sdf2::syntax::\Sdf2-Syntax::Symbol s) {
  if(/languages::sdf2::syntax::\Sdf2-Syntax::SingleQuotedStrCon scon := s)
  	return unescape(scon);
  if(/languages::sdf2::syntax::\Sdf2-Syntax::StrCon scon := s)
  	return unescape(scon);
  throw "unexpected string format: <s>";
}

private Symbol getCharClass(languages::sdf2::syntax::\Sdf2-Syntax::CharClass cc) {
   switch(cc) {
     case (CharClass) `[]` :
     	return \char-class([]);
     	
     case (CharClass) `[<CharRanges ranges>]` : 
     	return \char-class([getRange(r) | /languages::sdf2::syntax::\Sdf2-Syntax::CharRange r := ranges]);
     	
     case (CharClass) `(<languages::sdf2::syntax::\Sdf2-Syntax::CharClass c>)`: 
     	return getCharClass(c);
     	
     case (CharClass) `~ <languages::sdf2::syntax::\Sdf2-Syntax::CharClass c>`:
     	return complement(getCharClass(c));
     	
     case (CharClass) `<languages::sdf2::syntax::\Sdf2-Syntax::CharClass l> /\ <languages::sdf2::syntax::\Sdf2-Syntax::CharClass r>`:
     	return intersection(getCharClass(l),getCharClass(r));
     	
     case (CharClass) `<languages::sdf2::syntax::\Sdf2-Syntax::CharClass l> \/ <languages::sdf2::syntax::\Sdf2-Syntax::CharClass r>`: 
     	return union(getCharClass(l),getCharClass(r));
     	
     case (CharClass) `<languages::sdf2::syntax::\Sdf2-Syntax::CharClass l> / <languages::sdf2::syntax::\Sdf2-Syntax::CharClass r>`:
     	return difference(getCharClass(l),getCharClass(r));
     	
     default: throw "missed a case <cc>";
   }
}

//  (CharClass) `[]` == \char-class([]);  // ===> gives unsupported operation

test getCharClass((CharClass) `[]`)         == \char-class([]);
test getCharClass((CharClass) `[a]`)        == \char-class([range(97,97)]);
test getCharClass((CharClass) `[a-z]`)      == \char-class([range(97,122)]);
test getCharClass((CharClass) `[a-z0-9]`)   == \char-class([range(97,122), range(48,57)]);
test getCharClass((CharClass) `([a])`)      == \char-class([range(97,97)]);
test getCharClass((CharClass) `~[a]`)       == complement(\char-class([range(97,97)]));
test getCharClass((CharClass) `[a] /\ [b]`) == intersection(\char-class([range(97,97)]), \char-class([range(98,98)]));
test getCharClass((CharClass) `[a] \/ [b]`) == union(\char-class([range(97,97)]), \char-class([range(98,98)]));
test getCharClass((CharClass) `[a] / [b]`)  == difference(\char-class([range(97,97)]), \char-class([range(98,98)]));
      
private CharRange getRange(languages::sdf2::syntax::\Sdf2-Syntax::CharRange r) {
  switch (r) {
    case (CharRange) `<Character c>` : return range(getCharacter(c),getCharacter(c));
    case (CharRange) `<Character l> - <Character r>`: return range(getCharacter(l),getCharacter(r));
    default: throw "missed a case <r>";
  }
}

test getRange((CharRange) `a`)   == range(97,97);
test getRange((CharRange) `a-z`) == range(97,122);

private int getCharacter(Character c) {
  switch (c) {
    case [Character] /\\<oct:[0-3][0-7][0-7]>/ : return toInt("0<oct>");
    case [Character] /\\<oct:[0-7][0-7]>/      : return toInt("0<oct>");
    case [Character] /\\<oct:[0-7]>/           : return toInt("0<oct>");
    case [Character] /\\<esc:["'\-\[\]\\ ]>/   : return charAt(esc, 0);
    case [Character] /<ch:[^"'\-\[\]\\ ]>/     : return charAt(ch, 0);
    default: throw "missed a case <c>";
  }
}

test getCharacter((Character) `a`)    == charAt("a", 0);
test getCharacter((Character) `\\`)   == charAt("\\", 0);
test getCharacter((Character) `\'`)   == charAt("'", 0);
test getCharacter((Character) `\1`)   == toInt("01");
test getCharacter((Character) `\12`)  == toInt("012");
test getCharacter((Character) `\123`) == toInt("0123");

public Attributes getAttributes({Attribute ","}* mods) {
  return attrs([getAttribute(m) | Attribute m <- mods]);
}

test getAttributes(({Attribute ","}*) `left, cons("decl")`) == attrs([assoc(left()),term(cons("decl"))]);
 
private Attr getAttribute(languages::sdf2::syntax::\Sdf2-Syntax::Attribute m) {
  switch (m) {
    case (Attribute) `left`: return \assoc(left());
    case (Attribute) `right`: return \assoc(right());
    case (Attribute) `non-assoc`: return \assoc(\non-assoc());
    case (Attribute) `assoc`: return \assoc(\assoc());
    case (Attribute) `bracket`: return \bracket();
    case (Attribute) `cons(<StrCon c>)` : return term("cons"(unescape(c)));
    default: throw "missed a case <m>";
  }
}

test getAttribute((Attribute) `left`)        == \assoc(left());
test getAttribute((Attribute) `cons("abc")`) == term("cons"("abc"));
