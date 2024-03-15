@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@synopsis{Convert SDF2 grammars to an (unnormalized) Rascal internal grammar representation (Grammar).}
module lang::sdf2::util::SDF2Grammar
       
// Todo List:
// - Some tests are marked with @ignore (and commented out) since they trigger a Rascal bug:
//   . The expression: `(Group) `A -\> B <1>`; triggers a bug in AST construction
//   . The test (Class) `[]` == \char-class([]);  // gives unsupported operation
                    
import IO;
import String; 
import Set;
import List;
import Map;
import ParseTree;
import Grammar;
import lang::rascal::grammar::definition::Characters; 
import lang::sdf2::\syntax::Sdf2;   

public Symbol label(str s, conditional(Symbol t, set[Condition] cs)) = conditional(label(s, t), cs);
public Symbol conditional(conditional(Symbol s, set[Condition] c1), set[Condition] c2) = conditional(s, c1 + c2);

public GrammarDefinition sdf2grammar(loc input) {
  return sdf2grammar("Main", parse(#SDF, input)); 
}

public GrammarDefinition sdf2grammar(str main, loc input) {
  return sdf2grammar(main, parse(#SDF, input)); 
}

public GrammarDefinition sdf2grammar(loc input) {
  return sdf2grammar(parse(#SDF, input)); 
}

public GrammarDefinition sdf2grammar(SDF def) {
  return sdf2grammar("Main", def);
}
 
public Grammar::Grammar injectStarts(Grammar::Grammar g) = visit (g) {
	case Production p => p[def = \start(p.def)]
		when p.def in g.starts
};

public GrammarDefinition sdf2grammar(str main, SDF def) {
  if ((SDF) `definition <Module* mods>` := def) {
    ms = ();
    for (Module m <- mods) {
      gm = getModule(m);
      ms[gm.name] = gm;
    } 
    
    main = moduleName(main);
    
    if (main notin ms) 
      throw "Main module <main> not found";
    
    res = definition(main, ms);
    res = split(res);
    res = addLexicalChaining(res);
    //res = resolve(res);
    res = applyConditions(res, (s:c | c:conditional(s,_) <- getConditions(def)));
    res = removeDirectProductionCycle(res);
    
    return res;
  }
  
  throw "Unknown format for SDF2";
}

private GrammarDefinition split(GrammarDefinition def) = visit(def) { case Grammar::Grammar g => split(g) };

private GrammarDefinition removeDirectProductionCycle(GrammarDefinition def) {
	def = visit (def) {
		case Production p => p[alternatives = { a | a <- p.alternatives, !isProductionCycle(a, p)}]
			when p has def && p has alternatives 
		case Production p => p[choices = [ c | c <- p.choices, !isProductionCycle(c, p)]]
			when p has def && p has choices 
	};
	return visit(def) {
		case Grammar::Grammar g => removeEmptyProductions(g)
	};
}

private bool isProductionCycle(\prod(_, [sing], _), Production b) {
	if (s := strip(sing) && s has name && bs := strip(b.def) && bs has name)
		return s.name == bs.name;
	else
		return false;
}
private default bool isProductionCycle(Production a, Production b) = false;

private GrammarDefinition addLexicalChaining(GrammarDefinition def) {
	set[Symbol] sSorts = { s | /Grammar::Grammar g := def, s <- g.rules, (s is \sort || s is \parameterized-sort) };
	set[Symbol] lSorts = { s | /Grammar::Grammar g := def, s <- g.rules, !(s is \sort || s is \parameterized-sort)};
	overlap = {s.name | s <- sSorts} & {s.name | s <- lSorts};
	if (overlap != {}) {
		// first replace the lexicals l with LEX_l
		def = visit(def) {
			case Grammar::Grammar g : {
				for (s <- g.rules, !(s is \sort || s is \parameterized-sort), s.name in overlap, p := g.rules[s]) {
					newSymbol = \lex("LEX_<s.name>");
					g.rules[newSymbol] = visit(p) {
						case \priority(_, l) => \priority(newSymbol, l)
						case \associativity(_, a, l) => \associativity(newSymbol, a, l)
						case \cons(_, ss, a) => \cons(newSymbol, ss, a)
						case \func(_, ss, kws) => \func(newSymbol, ss, kws)
						case \choice(_, ps) => \choice(newSymbol, ps)
					};
					g.rules = delete(g.rules, s);
				}	
				insert g;
			}
		};
		// now add the chain rules to one of the grammars
		chains = grammar({}, (\sort(n) : \prod(\sort(n), [\lex("LEX_<n>")], {}) | n <- overlap));
		def = top-down-break visit(def) {
			case Grammar::Grammar g => compose(g, chains)
		};
		
	}
	return def;
}

Symbol striprec(Symbol s) = visit(s) { case Symbol t => strip ( t ) };
Symbol strip(label(str _, Symbol s)) = strip(s);
Symbol strip(conditional(Symbol s, set[Condition] _)) = strip(s);
default Symbol strip(Symbol s) = s;



private Grammar::Grammar split(Grammar::Grammar g) {
  for (nt <- g.rules, cur :=  g.rules[nt], sorts := {strip(s) | /prod(s,_,_) := cur}, size(sorts) > 1) {
    for (Symbol s <- sorts) {
      newp = keep(cur, s);
      if (g.rules[s]? && s != strip(cur.def))
        g.rules[s].alternatives += newp.alternatives;
      else
        g.rules[s] = newp;
    }
  }
  return removeEmptyProductions(g);
}

data Production = temp();

private bool isNotEmpty(Production p) {
	if (p has alternatives) {
		return p.alternatives != {};		
	}
	if (p has choices) {
		return p.choices != [];	
	}
	return true;
}
private Grammar::Grammar removeEmptyProductions(Grammar::Grammar g) {
	g = visit(g) {
		case list[Production] l => [p | p <- l, isNotEmpty(p)]
		case set[Production] l => {p | p <- l, isNotEmpty(p)}
	};
	return g[rules = ( s : p | s <- g.rules, p := g.rules[s], isNotEmpty(p))];
}

private Production keep(Production source, Symbol s) = visit(source) {
	case \priority(_, l) => \priority(s, l)
	case \associativity(_, a, l) => \associativity(s, a, l)
	case \cons(_, ss, a) => \cons(s, ss, a)
	case \func(_, ss, kws) => \func(s, ss, kws)
	case \choice(_, ps) => \choice(s, ps)
	case list[Production] ps => [p | p <- ps, strip(p.def) == s]
		when size(ps) > 0 // bug #208
	case set[Production] ps => {p | p <- ps, strip(p.def) == s}
		when size(ps) > 0
};

private GrammarModule getModule(Module m) {
  if (/(Module) `module <ModuleName mn> <ImpSection* _> <Sections _>` := m) {
    name = moduleName("<mn.id>");
    println("processing <name>");
    prods = getProductions(m);
    imps = getImports(m); 
   
    // note that imports in SDF2 have the semantics of extends in Rascal
    return \module(name, {}, imps, illegalPriorities(dup(grammar(getStartSymbols(m), prods))));
    //return \module(name, {}, imps, grammar({}, prods));
  }
  
  throw "can not find module name in <m>";
}

public str moduleName(/<pre:.*>\/<post:.*>/) = moduleName("<pre>::<post>");
public str moduleName(/languages::<rest:.*>/) = moduleName("lang::<rest>");
public default str moduleName(str i)  = i;

private set[str] getImports(Module m) {
  return { moduleName("<name.id>") | /Import i := m,  /ModuleName name := i}; 
}

public GrammarDefinition applyConditions(GrammarDefinition d,  map[Symbol from,Symbol to] conds) {
  Symbol app(Symbol s) { 
    if (s is label) 
      return label(s.name, app(s.symbol));
    else if (s in conds) 
      return conds[s];
    else
      return s;
  }
    
  return visit(d) {
    case prod(Symbol e, list[Symbol] ss, set[Attr] as) => prod(e, [app(s) | s <- ss], as)
  }
}

public Grammar::Grammar illegalPriorities(Grammar::Grammar g) {
  extracted = {};
  g = innermost visit (g) {
    case \priority(Symbol def, list[Production] ps) : 
      if ([*pre,p:prod(Symbol other, _, _),*post] := ps, !sameType(def, other)) {
        println("WARNING: extracting production from non-recursive priority chain");
        extracted += p[attributes = p.attributes + \tag("NotSupported"("priority with <pre> <post>"))];
        insert priority(def, pre + post);
      }
      else fail;
    case \associativity(Symbol def, Associativity a, set[Production] q) :
      if ({*_, p:prod(Symbol other, _, _)} := q, !sameType(def, other)) {
        println("WARNING: extracting production from non-recursive associativity group");
        extracted += p[attributes = p.attributes + \tag("NotSupported"("<a> associativity with <other>"))];
        insert associativity(def, a, q);
      }
      else fail;
  }
  
  return compose(g, grammar({}, extracted));
}

public &T dup(&T g) {
  prods = { p | /Production p:prod(_,_,_) := g };
  
  // first we fuse the attributes (SDF2 semantics) and the cons names
  solve (prods) {
    if ({prod(l,r,a1), prod(l,r,a2), *rest} := prods) {
      prods = {prod(l,r,a1 + a2), *rest};
    }
    if ({prod(label(n,l),r,a1), prod(l,r,a2), *rest} := prods) {
      prods = {prod(label(n,l),r,a1 + a2), *rest};
    }
    if ({prod(label(n,l),r,a1), prod(label(_,l),r,a2), *rest} := prods) {
      prods = {prod(label(n,l),r,a1 + a2), *rest};
    }
  } 
  
  // now we replace all uses of prods by their fused counterparts
  g = visit(g) {
    case prod(l,r,_) : 
      if ({p:prod(l,r,_), *_} := prods) 
        insert p;
      else if ({p:prod(label(_,l),r,_),*_} := prods)
        insert p;
      else fail;
    case prod(label(_,l),r,_) :
      if ({p:prod(label(_,l),r,_),*_} := prods)
        insert p;
      else fail;
  }
  
  return g;
}

test bool test1() = sdf2grammar(
        (SDF) `definition  module X exports context-free syntax    "abc" -\> ABC`).modules["X"].grammar.rules[sort("ABC")] ==
         choice(sort("ABC"), {prod(sort("ABC"),[lit("abc")],{})});

// \char-class([range(97,122),range(48,57)])
test bool test2() = rs := sdf2grammar(
		(SDF) `definition
		      'module PICOID
          'exports
          'lexical syntax
          '   [a-z] [a-z0-9]* -\> PICO-ID  
          'lexical restrictions
          '  PICO-ID -/- [a-z0-9]`).modules["PICOID"].grammar.rules 
     && prod(lex("PICO-ID"),[\char-class([range(97,122)]),\conditional(\iter-star(\char-class([range(97,122),range(48,57)])),{\not-follow(\char-class([range(97,122),range(48,57)]))})],{}) == rs[lex("PICO-ID")]          
     ;
     
test bool test3() = rs := sdf2grammar(
		(SDF) `definition
		      'module StrChar
          'exports
          ' lexical syntax
          '   ~[\\0-\\31\\n\\t\\"\\\\]          -\> StrChar {cons("normal")}`).modules["StrChar"].grammar.rules
     && prod(label("normal",sort("StrChar")),[\char-class([range(26,33),range(35,91),range(93,65535)])],{}) == rs[sort("StrChar")]
     ;
       
public set[Production] getProductions(Module \mod) {
 res = {};
 visit (\mod) {
    case (Grammar) `syntax <Prod* prods>`: 
    	res += getProductions(prods, true);
    case (Grammar) `lexical syntax <Prod* prods>`: 
    	res += getProductions(prods, true); 
    case (Grammar) `context-free syntax <Prod* prods>`: 
    	res += getProductions(prods, false);
    case (Grammar) `priorities <{Priority ","}* prios>`:
    	res += getPriorities(prios,true);
    case (Grammar) `lexical priorities <{Priority ","}* prios>`: 
    	res += getPriorities(prios,true);
    case (Grammar) `context-free priorities <{Priority ","}* prios>`: 
    	res += getPriorities(prios,false);
  }; 
  
  return res;
}

set[Production] getProductions(SDF sd) = {*getProductions(m) | m <- sd.def.modules};

test bool test4() = getProductions((SDF) `definition module A exports syntax A -\> B`) ==
     {prod(sort("B"),[sort("A")],{})};
     
test bool test5() = getProductions((SDF) `definition module A exports lexical syntax A -\> B`) ==
     {prod(lex("B"),[sort("A")],{})};
     
test bool test6() = getProductions((SDF) `definition module A exports lexical syntax A -\> B B -\> C`) ==
     {prod(lex("C"),[sort("B")],{}),prod(lex("B"),[sort("A")],{})};
     
test bool test7() = getProductions((SDF) `definition module A exports context-free syntax A -\> B`) ==
     {prod(sort("B"),[sort("A")],{})};
     
test bool test9() = getProductions((SDF) `definition module A exports priorities A -\> B \> C -\> D`) ==
     {prod(sort("B"),[sort("A")],{}),prod(sort("D"),[sort("C")],{})};

test bool test9_2() = getProductions((SDF) `definition module A exports priorities B "*" B -\> B \> B "+" B -\> B`) ==
     {priority(sort("B"),[prod(sort("B"),[sort("B"),lit("*"),sort("B")],{}),prod(sort("B"),[sort("B"),lit("+"),sort("B")],{})])};


public set[Production] getProductions(Prod* prods, bool isLex){
	return {*fixParameters(getProduction(prod, isLex)) | Prod prod <- prods};
}

set[Production] fixParameters(set[Production] input) {
  return innermost visit(input) {
    case prod(\parameterized-sort(str name, [*pre, sort(str x), *post]),lhs,  as) =>
         prod(\parameterized-sort(name,[*pre,\parameter(x,adt("Tree",[])),*post]),visit (lhs) { case sort(x) => \parameter(x,adt("Tree",[])) }, as)
  }
}

private str labelName("") = "";
private default str labelName(str s) = toLowerCase(s[0]) + (size(s) > 1 ? s[1..] : "");

public set[Production] getProduction(Prod P, bool isLex) {
  switch (P) {
    case (Prod) `<Syms syms> -\> LAYOUT <Attrs ats>` :
        return {prod(layouts("LAYOUTLIST"),[\iter-star(\lex("LAYOUT"))],{}),
                prod(\lex("LAYOUT"), getSymbols(syms, isLex),getAttributes(ats))};
    case (Prod) `<Syms syms> -\> <Sym sym> {<{Attribute ","}* _>, reject, <{Attribute ","}* _> }` :
        return {prod(keywords(getSymbol(sym, isLex).name + "Keywords"), getSymbols(syms, isLex), {})};
    case (Prod) `<Syms syms> -\> <Sym sym> {<{Attribute ","}* x>, cons(<StrCon n>), <{Attribute ","}* y> }` :
        return {prod(label(labelName(unescape(n)),getSymbol(sym, isLex)), getSymbols(syms, isLex), getAttributes((Attrs) `{<{Attribute ","}* x>, <{Attribute ","}* y> }`))};
    case (Prod) `<Syms syms> -\> <Sym sym> <Attrs ats>` : 
        return {prod(getSymbol(sym, isLex), getSymbols(syms, isLex),getAttributes(ats))};
    default: {
        println("WARNING: not importing <P>");
    	return {prod(sort("IGNORED"),[],{\tag("NotSupported"("<P>"))})};
    }
  }
}

test bool test10() = getProduction((Prod) `PICO-ID ":" TYPE -\> ID-TYPE`, false) == 
     {prod(sort("ID-TYPE"),[sort("PICO-ID"),lit(":"),sort("TYPE")],{})};
     
test bool test11() = getProduction((Prod) `PICO-ID ":" TYPE -\> ID-TYPE`, true) == 
     {prod(lex("ID-TYPE"),[sort("PICO-ID"),lit(":"),sort("TYPE")],{})};

test bool test12() = getProduction((Prod) `PICO-ID ":" TYPE -\> ID-TYPE {cons("decl"), left}`, false) ==
     {prod(sort("ID-TYPE"),[sort("PICO-ID"), lit(":"), sort("TYPE")],{\assoc(left())})};
               
test bool test13() = getProduction((Prod) `[\\ \\t\\n\\r]	-\> LAYOUT {cons("whitespace")}`, true) == 
	 {prod(\lex("LAYOUT"),[\char-class([range(32,32),range(9,9),range(10,10),range(13,13)])],{})};
 
test bool test14() = getProduction((Prod) `{~[\\n]* [\\n]}* -\> Rest`, true) ==
     {prod(sort("Rest"),[\iter-star-seps(\iter-star(\char-class([range(0,9),range(11,65535)])),[\char-class([range(10,10)])])],{})};


public set[Symbol] getConditions(SDF m) {
  res = {};
  visit (m) {
    case (Grammar) `restrictions <Restriction* rests>`:
      res += getRestrictions(rests, true);
    case (Grammar) `lexical restrictions <Restriction* rests>`:
      res += getRestrictions(rests, true);
    case (Grammar) `context-free restrictions <Restriction* rests>` :
      res += getRestrictions(rests, false);
    case (Prod) `<Syms _> -\> <Sym sym> {<{Attribute ","}* _>, reject, <{Attribute ","}* _> }` :
      res += {conditional(getSymbol(sym, false), {\delete(keywords(getSymbol(sym, false).name + "Keywords"))})
               ,conditional(getSymbol(sym, true), {\delete(keywords(getSymbol(sym, true).name + "Keywords"))})};
   }
   
   while ({conditional(s, cs1), conditional(s, cs2), *rest} := res)
       res = rest + {conditional(s, cs1 + cs2)};
   
   //iprintln(res);
   return res;
}
      
    
public set[Symbol] getRestrictions(Restriction* restrictions, bool isLex) {
//println("looping over < restrictions>");
  res = { *getRestriction(r, isLex) | Restriction r <- restrictions };
  //println("collected: <res>");
  return res;
}

public set[Symbol] getRestriction(Restriction restriction, bool isLex) {
  println("getting rest: <restriction>");
  switch (restriction) {
    case (Restriction) `-/- <Lookaheads _>` :
    	return {};
    
    case (Restriction) `LAYOUT? -/- <Lookaheads ls>` :
      return {conditional(\iter-star(\lex("LAYOUT")), {\not-follow(l) | l <- getLookaheads(ls) })};
      
    case (Restriction) `<Sym s1> -/- <Lookaheads ls>` : 
      return {conditional(getSymbol(s1, isLex), {\not-follow(l) | l <- getLookaheads(ls) })};
  	
    case (Restriction) `<Sym s1> <Sym+ rest> -/- <Lookaheads ls>` : 
      return  getRestriction((Restriction) `<Sym s1> -/- <Lookaheads ls>`, isLex)
           + {*getRestriction((Restriction) `<Sym s> -/- <Lookaheads ls>`, isLex) | Sym s <- rest};
    
             
       
    default: {
       println("WARNING: ignored <restriction>");
       return {};
    }
  }
}

test bool test18() = getRestriction((Restriction) `-/- [a-z]`, true) == {};

test bool test19() = getRestriction((Restriction) `ID -/- [a-z]`, true) == 
     {conditional(sort("ID"),{\not-follow(\char-class([range(97,122)]))})};
   

// ----- getLookaheads, getLookahead -----

public set[Symbol] getLookaheads(Lookaheads ls) {
   switch (ls) {
     case (Lookaheads) `<Class c>` :
     	return {getCharClass(c)};
     	
     case (Lookaheads) `<Class l> . <Lookaheads r>` : {
     	rs = getLookaheads(r);
     	if (size(rs) == 1)
	      return {\seq([getCharClass(l), *rs])};
	    else
	      return {\seq([getCharClass(l), \alt(rs)])};
     }
     	  	
     case (Lookaheads) `<Lookaheads l> | <Lookaheads r>` :
     	return getLookaheads(l) + getLookaheads(r);
     	
     case (Lookaheads) `(<Lookaheads l>)` :
     	return getLookaheads(l);
     	
     default: {
        println("Warning: ignored <ls>");
        return {};
     }
   }
}
 
test bool test21() = getLookaheads((Lookaheads) `[a-z]`) == 
     {\char-class([range(97,122)])};
     
test bool test22() = getLookaheads((Lookaheads) `[a-z] . [0-9]`) ==
     {};
       
test bool test23() = getLookaheads((Lookaheads) `[a-z]  | [\\"]`) ==
     {\char-class([range(97,122)]),
      \char-class([range(34,34)])};
      
// ----- getPriorities, getPriority -----

public set[Production] getPriorities({Priority ","}* priorities, bool isLex) {
  return {getPriority(p, isLex) | Priority p <- priorities};
}

public Production getPriority(Group group, bool isLex) {
	switch (group) {
    case (Group) `<Prod p>` :   
      	return choice(definedSymbol(p, isLex), getProduction(p, isLex));
       
    case (Group) `<Group g> .` :
     	return getPriority(g, isLex); // we ignore non-transitivity here!
     	
    case (Group) `<Group g> <ArgumentIndicator _>` : 
     	return getPriority(g, isLex); // we ignore argument indicators here!
     	
    case (Group) `{<Prod* ps>}` : 
       return choice(definedSymbol(ps,isLex), {*getProduction(p,isLex) | Prod p <- ps});
       
    case (Group) `{<Assoc a> : <Prod* ps>}` : 
       return \associativity(definedSymbol(ps, isLex), getAssociativity(a), {*getProduction(p,isLex) | Prod p <- ps});
    
    default:
    	throw "missing case <group>";}
}
     	
test bool test24() = getPriority((Group) `A -\> B`, false) == 
     prod(sort("B"),[sort("A")],{});
     
test bool test25() = getPriority((Group) `A -\> B .`, false) ==  
     prod(sort("B"),[sort("A")],{});
         
test bool test26() = getPriority((Group) `A -\> B \<1\>`, false) == 
     prod(sort("B"),[sort("A")],{});
         
test bool test27() = getPriority((Group) `{A -\> B C -\> D}`, false) == 
	 choice(sort("B"),{prod(sort("D"),[sort("C")],{}),prod(sort("B"),[sort("A")],{})});  
	   
test bool test28() = getPriority((Group) `{left: A -\> B}`, false) == 
     \associativity(sort("B"),\left(),{prod(sort("B"),[sort("A")],{})});
     
test bool test29() = getPriority((Group) `{left: A -\> B B -\> C}`, false) ==
     \associativity(sort("B"),\left(),{prod(sort("C"),[sort("B")],{}),prod(sort("B"),[sort("A")],{})});

public Production getPriority(Priority p, bool isLex) {
   switch (p) {
     case (Priority) `<Group g>`:
     	return getPriority(g, isLex);
         
     case (Priority) `<Group g1> <Assoc a> <Group g2>` : 
       return \associativity(definedSymbol(g1, isLex), getAssociativity(a), {getPriority((Priority) `<Group g1>`, isLex), getPriority((Priority) `<Group g2>`,isLex)});
 
     case (Priority) `<{Group "\>"}+ groups>` : 
       return priority(definedSymbol(groups,isLex), [getPriority(group ,isLex) | Group group <- groups]);
   }
   throw "could not get priority of <p>";
}


test bool test30() = getPriority((Priority) `A -\> B`, false) == 
     priority(sort("B"),[prod(sort("B"),[sort("A")],{})]);
     
test bool test31() = getPriority((Priority) `A -\> B .`, false) == 
     priority(sort("B"),[prod(sort("B"),[sort("A")],{})]);

test bool test32() = getPriority((Priority) `A -\> B \<1\>`, false) == 
     prod(sort("B"),[sort("A")],{});
     
test bool test33() = getPriority((Priority) `{A -\> B C -\> D}`, false) == 
     priority(sort("B"),[choice(sort("B"),{prod(sort("D"),[sort("C")],{}),prod(sort("B"),[sort("A")],{})})]);
     
test bool test34() = getPriority((Priority) `A -\> B \> C -\> D`, false) ==
     priority(sort("B"),[prod(sort("B"),[sort("A")],{}),prod(sort("D"),[sort("C")],{})]);
     
test bool test35() = getPriority((Priority) `A -\> B \> C -\> D \> E -\> F`, false) ==
     priority(sort("B"),[prod(sort("B"),[sort("A")],{}),prod(sort("D"),[sort("C")],{}),prod(sort("F"),[sort("E")],{})]);


// ----- definedSymbol -----

public Symbol definedSymbol((&T <: Tree) v, bool isLex) {
  // Note that this might not work if there are different right-hand sides in the group...
  // I don't know what to do about this yet.
  if (/(Prod) `<Sym* _> -\> <Sym s> <Attrs _>` := v) {
    return getSymbol(s, isLex);
  } else if (/(Prod) `<Sym* _> -\> LAYOUT <Attrs _>` := v) {
    return \lex("LAYOUT");
  }
  throw "could not find a defined symbol in <v>";
}

// ----- getStartSymbols -----

public set[Symbol] getStartSymbols(Module \mod) {
  result = {};
  visit(\mod) {
    case (Grammar) `context-free start-symbols <Sym* syms>` :
    	result += { getSymbol(sym, false) | sym <- syms };
    case (Grammar) `lexical start-symbols <Sym* syms>`      :
    	result += { getSymbol(sym, true) | sym <- syms };
    case (Grammar) `start-symbols <Sym* syms>`              :
    	result += { getSymbol(sym, true) | sym <- syms };
  }
  return result;
}

test bool test36() = getStartSymbols((Module) `module M exports context-free start-symbols A B C`) == 
     {sort("A"), sort("B"), sort("C")};
     
test bool test37() = getStartSymbols((Module) `module M exports lexical start-symbols A B C`) == 
     {sort("A"), sort("B"), sort("C")};
     
test bool test38() = getStartSymbols((Module) `module M exports start-symbols A B C`) == 
     {sort("A"), sort("B"), sort("C")};

public list[Symbol] getSymbols((Syms) `<Sym* ss>`, bool isLex) = [getSymbol(sym, isLex) | sym <- ss];

test bool test39() = getSymbols((Syms) `A B "ab"`, true) == [sort("A"), sort("B"), lit("ab")];
     
public Symbol getSymbol(Sym sym, bool isLex) {
  switch (sym) {
    case (Sym) `LAYOUT ?`:
        return \layouts("LAYOUTLIST");
    case (Sym) `<StrCon l> : <Sym s>`:
		return label(labelName(unescape(l)), getSymbol(s,isLex));
		
    case (Sym) `<IdCon i> : <Sym s>`:
    	return label(labelName("<i>"), getSymbol(s, isLex));
    	
   	case (Sym) `LAYOUT`:
    	return \lex("LAYOUT"); 
    	
    case (Sym) `<StrCon l>`:
          	return lit(unescape(l));
    	
    case (Sym) `<SingleQuotedStrCon l>`:
    	return cilit(unescape(l));  
    	
    case (Sym) `<Sort n>[[<{Sym ","}+ syms>]]`:
    	return \parameterized-sort("<n>",separgs2symbols(syms,isLex));
    	
    case (Sym) `<Sym s> ?`:
    	return opt(getSymbol(s,isLex));
    	
    case (Sym) `<Class cc>`:
    	return getCharClass(cc);
    	
    case (Sym) `\< <Sym s> -LEX \>`:
        return getSymbol(s, true);
        
    case (Sym) `\< <Sym s> -CF \>`:
        return getSymbol(s, false);
       
    case (Sym) `\< <Sym s> -VAR \>`:
        return getSymbol(s, isLex);
        
    case (Sym) `<Sym lhs> | <Sym rhs>` : 
        return alt({getSymbol(lhs, isLex), getSymbol(rhs, isLex)});
       
  }  
  
  if (isLex) switch (sym) {
    case (Sym) `<Sort n>`:
        return lex("<n>");
        
    case (Sym) `<Sym s> *`: 
    	return \iter-star(getSymbol(s,isLex));
    	
    case (Sym) `<Sym s> +`  :
    	return \iter(getSymbol(s,isLex));
    	
    case (Sym) `<Sym s> *?` :
    	return \iter-star(getSymbol(s,isLex));
    	
    case (Sym) `<Sym s> +?` :
    	return \iter(getSymbol(s,isLex));
    	
    case (Sym) `{<Sym s> <Sym sep>} *`  :
    	return \iter-star-seps(getSymbol(s,isLex), [getSymbol(sep, isLex)]);
    	
    case (Sym) `{<Sym s> <Sym sep>} +`  :
    	return \iter-seps(getSymbol(s,isLex), [getSymbol(sep, isLex)]);
    	
    case (Sym) `<Sym s>?` :
        return \opt(getSymbol(s, isLex));
        
    case (Sym) `(<Sym first> <Sym+ rest>)` :
        return seq([getSymbol(first, isLex)] + [getSymbol(e, isLex) | e <- rest]);

    case (Sym) `(<Sym s>)` :
        return getSymbol(s, isLex);
    	
    default: throw "missed a case <sym>";
  } 
  else switch (sym) {  
    case (Sym) `<Sort n>`:
        return sort("<n>");
        
    case (Sym) `<Sym s> *`:
    	return \iter-star-seps(getSymbol(s,isLex),[\layouts("LAYOUTLIST")]);
    	
    case (Sym) `<Sym s> +` :
    	return \iter-seps(getSymbol(s,isLex),[\layouts("LAYOUTLIST")]);
    	
    case (Sym) `<Sym s> *?`:
    	return \iter-star-seps(getSymbol(s,isLex),[\layouts("LAYOUTLIST")]);
    	
    case (Sym) `<Sym s> +?`:
    	return \iter-seps(getSymbol(s,isLex),[\layouts("LAYOUTLIST")]);
    	
    case (Sym) `{<Sym s> <Sym sep>} *` :
    	return \iter-star-seps(getSymbol(s,isLex), [\layouts("LAYOUTLIST"),getSymbol(sep, isLex),\layouts("LAYOUTLIST")]);
    	
    case (Sym) `{<Sym s> <Sym sep>} +` :
    	return \iter-seps(getSymbol(s,isLex), [\layouts("LAYOUTLIST"),getSymbol(sep, isLex),\layouts("LAYOUTLIST")]);
    	
    case (Sym) `(<Sym first> <Sym+ rest>)` :
        return seq([getSymbol(first, isLex)] + [\layouts("LAYOUTLIST"), getSymbol(e, isLex) | e <- rest]);
        
    case (Sym) `(<Sym first> | <Sym second>)` :
         return alt({getSymbol(first, isLex), getSymbol(second, isLex)});
    case (Sym) `(<Sym single>)`: 
    	return getSymbol(single, isLex);
    default: throw "missed a case <sym>";  
  }
}  

public Symbol alt({alt(set[Symbol] ss), *Symbol rest}) = alt(ss + rest);

test bool test40() = getSymbol((Sym) `"abc"`, false) 		== lit("abc");
test bool test41() = getSymbol((Sym) `"a\\\\c"`, false) 		== lit("a\\c");
test bool test42() = getSymbol((Sym) `"a\>c"`, false) 		== lit("a\>c");
test bool test43() = getSymbol((Sym) `ABC`, false) 			== sort("ABC");
test bool test44() = getSymbol((Sym) `'abc'`, false) 		== cilit("abc");
test bool test45() = getSymbol((Sym) `abc : ABC`, false) 	== label("abc",sort("ABC"));
test bool test46() = getSymbol((Sym) `"abc" : ABC`, false) 	== label("abc",sort("ABC"));
test bool test47() = getSymbol((Sym) `A[[B]]`, false) 		== \parameterized-sort("A", [sort("B")]);
test bool test48() = getSymbol((Sym) `A?`, false) 			== opt(sort("A"));
test bool test49() = getSymbol((Sym) `[a]`, false) 			== \char-class([range(97,97)]);
test bool test50() = getSymbol((Sym) `A*`, false) 			== \iter-star-seps(sort("A"),[\layouts("LAYOUTLIST")]);
test bool test51() = getSymbol((Sym) `A+`, false) 			== \iter-seps(sort("A"),[\layouts("LAYOUTLIST")]);
test bool test52() = getSymbol((Sym) `A*?`, false) 			== opt(\iter-star-seps(sort("A"),[\layouts("LAYOUTLIST")]));
test bool test53() = getSymbol((Sym) `A+?`, false) 			== opt(\iter-seps(sort("A"),[\layouts("LAYOUTLIST")]));
test bool test54() = getSymbol((Sym) `{A "x"}*`, false) 		== \iter-star-seps(sort("A"),[\layouts("LAYOUTLIST"),lit("x"),\layouts("LAYOUTLIST")]);
test bool test55() = getSymbol((Sym) `{A "x"}+`, false) 		== \iter-seps(sort("A"),[\layouts("LAYOUTLIST"),lit("x"),\layouts("LAYOUTLIST")]);
test bool test56() = getSymbol((Sym) `{A "x"}*?`, false) 	== opt(\iter-star-seps(sort("A"),[\layouts("LAYOUTLIST"),lit("x"),\layouts("LAYOUTLIST")]));
test bool test57() = getSymbol((Sym) `{A "x"}+?`, false) 	== opt(\iter-seps(sort("A"),[\layouts("LAYOUTLIST"),lit("x"),\layouts("LAYOUTLIST")]));
test bool test58() = getSymbol((Sym) `A*`, true) 			== \iter-star(sort("A"));
test bool test59() = getSymbol((Sym) `A+`, true) 			== \iter(sort("A"));
test bool test60() = getSymbol((Sym) `A*?`, true) 			== opt(\iter-star(sort("A")));
test bool test61() = getSymbol((Sym) `A+?`, true) 			== opt(\iter(sort("A")));
test bool test62() = getSymbol((Sym) `{A "x"}*`, true) 		== \iter-star-seps(sort("A"),[lit("x")]);
test bool test63() = getSymbol((Sym) `{A "x"}+`, true) 		== \iter-seps(sort("A"),[lit("x")]);
test bool test64() = getSymbol((Sym) `{A "x"}*?`, true) 		== opt(\iter-star-seps(sort("A"),[lit("x")]));
test bool test65() = getSymbol((Sym) `{A "x"}+?`, true) 		== opt(\iter-seps(sort("A"),[lit("x")]));

//test getSymbol((Sym) `<LAYOUT? -CF>`, true) ==
//test getSymbol((Sym) `<RegExp -LEX>`, true) ==

// ----- unescape -----
// Take a string constant and replace all escaped characters by the character itself.

// Unescape on Symbols. Note that the function below can currently coexist with the two unescape functions
// since StrCon and SingleQuotedStrCons are *not* a subtype of Symbol (which they should be).
// Do a deep match (/) to skip the chain function that syntactically includes both subtypes in Symbol.

private str unescape(Sym s) {
  if (/SingleQuotedStrCon scon := s) {
  	return unescape(scon);
  }
  if (/StrCon scon := s) {
  	return unescape(scon);
  }
  throw "unexpected string format: <s>";
}

public str unescape(StrCon s) { 
  if ([StrCon] /^\"<chars:.*>\"$/ := s)
    	return unescapeStr(chars);
  throw "unexpected string format: <s>";
}

private str unescape(SingleQuotedStrCon s) {
   if ([SingleQuotedStrCon] /^\'<chars:.*>\'$/ := s)
     return unescapeStr(chars);
   throw "unexpected string format: <s>";
} 

test bool testUn1() = unescape((StrCon) `"abc"`)  	== "abc";
test bool testUn2() = unescape((StrCon) `"a\\nc"`) 	== "a\nc";
test bool testUn3() = unescape((StrCon) `"a\\"c"`) 	== "a\"c";
test bool testUn4() = unescape((StrCon) `"a\\\\c"`) 	== "a\\c";
test bool testUn5() = unescape((StrCon) `"a\\\\\\"c"`)	== "a\\\"c";

test bool testUn6() = unescape((SingleQuotedStrCon) `'abc'`)  == "abc";
test bool testUn7() = unescape((SingleQuotedStrCon) `'a\\nc'`) == "a\nc";
test bool testUn8() = unescape((SingleQuotedStrCon) `'a\\'c'`) == "a\'c";
test bool testUn9() = unescape((SingleQuotedStrCon) `'a\\\\c'`) == "a\\c";

// unescapeStr: do the actual unescaping on a string
// Also takes care of escaping of < and > characters as required for Rascal strings (TO DO/CHECK)

public str unescapeStr(str chars){
   return visit (chars) {
       case /^\\b/           => "\b"
       case /^\\t/           => "\t"
       case /^\\n/           => "\n"
       case /^\\f/           => "\f"
       case /^\\r/           => "\r"  
       case /^\\\'/          => "\'"
       case /^\\"/           => "\""
       case /^\\\\/          => "\\"
       case /^\\TOP/         => "\u00FF"
       case /^\\EOF/         => "\u00A0"
       case /^\\BOT/         => "\u0000"
       case /^\\LABEL_START/ => "\u00A1"
       case /^\</			 => "\<"
       case /^\>/			 => "\>"
     };  
}

test bool un20() =unescapeStr("abc") 	== "abc";
test bool un21() =unescapeStr("a\nbc") 	== "a\nbc";
test bool un22() =unescapeStr("a\\\nbc") == "a\\\nbc";
test bool un23() =unescapeStr("a\"bc") 	== "a\"bc";
test bool un24() =unescapeStr("a\\\"bc") == "a\"bc";
test bool un25() =unescapeStr("a\\bc") 	== "a\bc";
test bool un26() =unescapeStr("a\\\\tc") == "a\\tc";
test bool un27() =unescapeStr("a\>b")    == "a\>b";
test bool un28() =unescapeStr("a\<b")    == "a\<b";

public Symbol getCharClass(Class cc) {
   switch(cc) {
     case (Class) `[]` :
     	return \char-class([]);
     	
     case (Class) `[<Range* ranges>]` :
     		return \new-char-class([getCharRange(r) | /Range r := ranges]);
     	
     case (Class) `(<Class c>)`: 
     	return getCharClass(c);
     	
     case (Class) `~ <Class c>`:
     	return complement(getCharClass(c));
     	
     case (Class) `<Class l> /\\ <Class r>`:
     	return intersection(getCharClass(l),getCharClass(r));
     	
     case (Class) `<Class l> \\/ <Class r>`: 
     	return union(getCharClass(l),getCharClass(r));
     	
     case (Class) `<Class l> / <Class r>`:
     	return difference(getCharClass(l),getCharClass(r));
     	
     default: throw "missed a case <cc>";
   }
}

test bool testCC1() = getCharClass((Class) `[]`)         == \char-class([]);
test bool testCC2() = getCharClass((Class) `[a]`)        == \char-class([range(97,97)]);
test bool testCC3() = getCharClass((Class) `[a-z]`)      == \char-class([range(97,122)]);
test bool testCC4() = getCharClass((Class) `[a-z0-9]`)   == \char-class([range(97,122), range(48,57)]);
test bool testCC5() = getCharClass((Class) `([a])`)      == \char-class([range(97,97)]);
test bool testCC6() = getCharClass((Class) `~[a]`)       == complement(\char-class([range(97,97)]));
test bool testCC7() = getCharClass((Class) `[a] /\\ [b]`) == intersection(\char-class([range(97,97)]), \char-class([range(98,98)]));
test bool testCC8() = getCharClass((Class) `[a] \\/ [b]`) == union(\char-class([range(97,97)]), \char-class([range(98,98)]));
test bool testCC9() = getCharClass((Class) `[a] / [b]`)  == difference(\char-class([range(97,97)]), \char-class([range(98,98)]));
test bool testCC10() = getCharClass((Class) `[\\n]`)       == \char-class([range(10,10)]);
test bool testCC11() = getCharClass((Class) `[\\t\\n]`)     == \char-class([range(9,9), range(10,10)]);
test bool testCC12() = getCharClass((Class) `~[\\0-\\31\\n\\t\\"\\\\]`) ==
     complement(\char-class([range(0,25),range(10,10),range(9,9),range(34,34),range(92,92)]));
test bool testCC13() = getCharClass((Class) `[\\"]`)       == \char-class([range(34,34)]);

// ----- getCharRange -----
      
public CharRange getCharRange(Range r) {
  switch (r) {
    case (Range) `<Character c>` : return range(getCharacter(c),getCharacter(c));
    case (Range) `<Character l> - <Character m>`: return range(getCharacter(l),getCharacter(m));
    default: throw "missed a case <r>";
  }
}

test bool testCR1() = getCharRange((Range) `a`)   	== range(97,97);
test bool testCR2() = getCharRange((Range) `a-z`) 	== range(97,122);
test bool testCR3() = getCharRange((Range) `\\n`)  	==  range(10,10);
test bool testCR4() = getCharRange((Range) `\\1-\\31`)	==  range(1,25);

public int getCharacter(Character c) {
  switch (c) {
    case [Character] /\\<dec:[0-9][0-9][0-9]>/ : return toInt("<dec>");
    case [Character] /\\<dec:[0-9][0-9]>/      : return toInt("<dec>");
    case [Character] /\\<dec:[0-9]>/           : return toInt("<dec>");
    case [Character] /\\t/                     : return 9;
    case [Character] /\\n/                     : return 10;
    case [Character] /\\r/                     : return 13;
    case [Character] /\\ /                     : return 32;
    case [Character] /\\<esc:["'\-\[\]\\ ]>/   : return charAt(esc, 0);
    case [Character] /<ch:[^"'\-\[\]\\ ]>/     : return charAt(ch, 0);
    
    default: throw "missed a case <c>";
  }
}
 
//test bool testCCX1() = ((Character) `a`)    == charAt("a", 0);
//test bool testCCX2() = ((Character) `\\\\`)   == charAt("\\", 0);
//test bool testCCX3() = ((Character) `\\'`)   == charAt("\'", 0);
//test bool testCCX4() = ((Character) `\\1`)   == 1;
//test bool testCCX5() = ((Character) `\\12`)  == 12;
//test bool testCCX6() = ((Character) `\\123`) == 123;
//test bool testCCX7() = ((Character) `\\n`)   == 10; 

// ----- getAttributes, getAttribute, getAssociativity -----

public set[Attr] getAttributes(Attrs as) {
  if ((Attrs) `{ <{Attribute ","}* mods> }` := as) {
	  return {*getAttribute(m) | Attribute m <- mods};
  }
  return {};
}
   
test bool testAs() = getAttributes((Attrs) `{left, cons("decl")}`) == {\assoc(\left()),\tag("cons"("decl"))};

public set[Attr] getAttribute(Attribute m) {
  switch (m) {
    case (Attribute) `<Assoc as>`:
     	return {\assoc(getAssociativity(as))};
     	
    case (Attribute) `bracket`:
    	return {\bracket()};
    
    case (Attribute) `cons(<StrCon _>)` : 
        return {};
        
    case (Attribute) `memo`:
    	return {\tag("NotSupported"("memo"))};
    	
    case (Attribute) `prefer`:
        return {\tag("prefer"())};
        
    case (Attribute) `avoid` :
        return {\tag("avoid"())};
    	
    case (Attribute) `reject` :
        return {\tag("reject"())};
        
    case (Attribute) `category(<StrCon a>)` :
        return {\tag("category"(unescape(a)))};
        
    case (Attribute) `<IdCon c>(<StrCon a>)` : 
        return {\tag("NotSupported"("<c>"(unescape(a))))};
        
    case (Attribute) `<ATerm t>`:
        return {\tag("NotSupported"("<t>"))};
        
    default: 
        return {};
  }
}

test bool testAs2() = getAttribute((Attribute) `left`)        == {\assoc(\left())};
 
private Associativity getAssociativity(Assoc as){
  switch (as) {
    case (Assoc) `left`:        
    	return \left();
    case (Assoc) `right`:
    	return \right();
    case (Assoc) `non-assoc`:
    	return \non-assoc();
    case (Assoc) `assoc`:
    	return \assoc();
    default:
    	throw "missed a case <as>";
  }
}
 
test bool testAssoc() = getAssociativity((Assoc) `left`) == \left();

private list[Symbol] separgs2symbols({Sym ","}+ args, bool isLex) {
  return [ getSymbol(s, isLex) | Sym s <- args ];
}
