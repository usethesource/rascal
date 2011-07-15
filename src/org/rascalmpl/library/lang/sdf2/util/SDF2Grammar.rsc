@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::sdf2::util::SDF2Grammar
     
// Convert SDF2 grammars to an (unnormalized) Rascal internal grammar representation (Grammar)
   
// Todo List:
// - Some tests are marked with @ignore (and commented out) since they trigger a Rascal bug:
//   . The expression: `(Group) `A -> B <1>`; triggers a bug in AST construction
//   . The test (Class) `[]` == \char-class([]);  // gives unsupported operation
    
import IO;
import String;
import Integer;
import ParseTree;
import Grammar;
import lang::sdf2::util::Load;
import lang::sdf2::syntax::Sdf2;   
import lang::rascal::syntax::Characters;
import lang::rascal::syntax::Normalization;
       
public Grammar sdf2grammar(loc input) {
  return sdf2grammar(parse(#SDF, input)); 
}

public Grammar sdf2module2grammar(str name, list[loc] path) {
  return sdf2grammar(loadSDF2Module(name, path));
}

// Convert given SDF definition

public Grammar sdf2grammar(SDF definition) {
  return dup(grammar(getStartSymbols(definition), 
                 getProductions(definition) 
                 + {prod([\iter-star(sort("LAYOUT"))],layouts("LAYOUTLIST"),\no-attrs())}));
}

public Grammar dup(Grammar g) {
  prods = { p | /Production p:prod(_,_,_) := g };
  
  while ({prod(l,r,a1), prod(l,r,a2), rest*} := prods) {
    prods = {prod(l,r,fuse(a1,a2)), rest};
  }
  
  g = visit(g) {
    case prod(l,r,a1) : if ({p:prod(l,r,_), _*} := prods) insert p;
  }
  
  ordered = { p | /\first(_,/Production p:prod(_,_,_)) := g }
          + { p | /\assoc(_,_,/Production p:prod(_,_,_)) := g};
  
  // for toplevel alternatives
  for (nt <- g.rules, p <- g.rules[nt], p in ordered) {
    g.rules[nt] -= {p};
  }
  
  // for nested alternatives
  g = visit(g) {
    case choice(nt,{Production p, rest*}) => choice(nt, rest) when p in ordered
  }
  
  return g;
}

public Attributes fuse(Attributes a1, Attributes a2) {
  switch (<a1,a2>) {
    case <\no-attrs(),_>         : return a2;
    case <_,\no-attrs()>         : return a1;
    case <attrs(as1),attrs(as2)> : return attrs(as1+as2);
  }
  throw "unexpected attrs <a1> or <a2>";
}

test bool test1() = sdf2grammar(
        `definition 
         module X
         exports
           context-free syntax
              "abc" -> ABC`).rules[sort("ABC")] ==
         {prod([lit("abc")],sort("ABC"),\no-attrs())};

test bool test2() = rs := sdf2grammar(
		`definition
		 module PICOID
         exports
           lexical syntax
             [a-z] [a-z0-9]* -> PICO-ID  
           lexical restrictions
             PICO-ID -/- [a-z0-9]`).rules 
     && prod([\char-class([range(97,122)]),\iter-star(\char-class([range(97,122),range(48,57)]))],sort("PICO-ID"),attrs([\lex()])) in rs[sort("PICO-ID")]          
     && restrict(sort("PICO-ID"),others(sort("PICO-ID")),{prod([\char-class([range(97,122),range(48,57)])],restricted(sort("PICO-ID")),\no-attrs())}) in rs[sort("PICO-ID")]              
     ;
     
test bool test3() = rs := sdf2grammar(
		`definition
		 module StrChar
         exports
           lexical syntax
             ~[\0-\31\n\t\"\\]          -> StrChar {cons("normal")}`).rules
     && prod([\char-class([range(26,33),range(35,91),range(93,65535)])],sort("StrChar"),attrs([term("cons"("normal")),\lex()])) in rs[sort("StrChar")]
     ;
       
public set[Production] getProductions(SDF definition) {
 res = {};
 visit (definition) {
    case (Grammar) `syntax <Prod* prods>`:
    	res += getProductions(prods, true);
    	
    case (Grammar) `lexical syntax <Prod* prods>`:
    	res += getProductions(prods, true); 
    	
    case (Grammar) `context-free syntax <Prod* prods>`:
    	res += getProductions(prods, false);
    	
    	case (Grammar) `restrictions <Restriction* rests>`:
    	res += getRestrictions(rests, false);
    	
    case (Grammar) `lexical restrictions <Restriction* rests>`:
    	res += getRestrictions(rests, true);
    	
    case (Grammar) `context-free restrictions <Restriction* rests>` :
    	res += getRestrictions(rests, false);
    	
    case (Grammar) `priorities <{Priority ","}* prios>`:
    	res += getPriorities(prios,false);
    	
    case (Grammar) `lexical priorities <{Priority ","}* prios>`:
    	res += getPriorities(prios,true);
    	
    case (Grammar) `context-free priorities <{Priority ","}* prios>`:
    	res += getPriorities(prios,false);
  };
  return res;
}

test bool test4() = getProductions((SDF) `definition module A exports syntax A -> B`) ==
     {prod([sort("A")],sort("B"),attrs([\lex()]))};
     
test bool test5() = getProductions((SDF) `definition module A exports lexical syntax A -> B`) ==
     {prod([sort("A")],sort("B"),attrs([\lex()]))};
     
test bool test6() = getProductions((SDF) `definition module A exports lexical syntax A -> B B -> C`) ==
     {prod([sort("B")],sort("C"),attrs([\lex()])),prod([sort("A")],sort("B"),attrs([\lex()]))};
     
test bool test7() = getProductions((SDF) `definition module A exports context-free syntax A -> B`) ==
     {prod([sort("A")],sort("B"),\no-attrs())};
     
test bool test8() = getProductions((SDF) `definition module A exports restrictions ID -/- [a-z]`) ==
     {restrict(sort("ID"),others(sort("ID")),{prod([\char-class([range(97,122)])],restricted(sort("ID")),\no-attrs())})};
    
test bool test9() = getProductions((SDF) `definition module A exports priorities A -> B > C -> D`) ==
     {first(sort("B"),[prod([sort("A")],sort("B"),\no-attrs()),prod([sort("C")],sort("D"),\no-attrs())])};


public set[Production] getProductions(Prod* prods, bool isLex){
	return {fixParameters(getProduction(prod, isLex)) | Prod prod <- prods};
}

Production fixParameters(Production input) {
  return innermost visit(input) {
    case prod(lhs, \parameterized-sort(str name, [pre*, sort(str x), post*]), as) =>
         prod(visit (lhs) { case sort(x) => \parameter(x) }, \parameterized-sort(name,[pre,\parameter(x),post]),as)
  }
}

public Production getProduction(Prod P, bool isLex) {
  switch (P) {
    case (Prod) `<Syms syms> -> <Sym sym>`:
    	return (isLex) ? prod(getSymbols(syms, isLex),getSymbol(sym, isLex),\attrs([\lex()]))
    	               : prod(getSymbols(syms, isLex),getSymbol(sym, isLex),\no-attrs());
      
    case (Prod) `<Syms syms> -> <Sym sym> <Attrs ats>` :
 		if(attrs([list[Attr] a]) := getAttributes(ats)) {
 		    newSym = getSymbol(sym, isLex);
    		result = (isLex) ? prod(getSymbols(syms, isLex),newSym, \attrs([a, \lex()]))
    	               	     : prod(getSymbols(syms, isLex),newSym, \attrs([a]));
    	    
    	    if (term("reject"()) in a) {
 		       return diff(newSym, \others(newSym), {result});
 		    }
 		    return result;
    	}
    	else {
    		return (isLex) ? prod(getSymbols(syms, isLex),getSymbol(sym, isLex),\attrs([\lex()]))
    	               	   : prod(getSymbols(syms, isLex),getSymbol(sym, isLex),\no-attrs());
    	}
    	
    //case (Prod) `<IdCon id> (<{Sym ","}* syms>) -> <Sym sym>`:
    //	throw "prefix functions not supported by SDF import yet";
    //	
    //case (Prod) `<IdCon id> (<{Sym ","}* syms>) -> <Sym sym> {<{Attribute ","}* attrs>}` :
    //	throw "prefix functions not supported by SDF import yet";
    //	
    //case (Prod) `<StrCon s> (<{Sym ","}* syms>) -> <Sym sym>`:
    //	throw "prefix functions not supported by SDF import yet";
    //	
    //case (Prod) `<StrCon s> (<{Sym ","}* syms>) -> <Sym sym> {<{Attribute ","}* attrs>}` :
    //	throw "prefix functions not supported by SDF import yet";
    	
    default:
    	throw "missing case <P>";
  }
}

test bool test10() = getProduction((Prod) `PICO-ID ":" TYPE -> ID-TYPE`, false) == 
     prod([sort("PICO-ID"),lit(":"),sort("TYPE")],sort("ID-TYPE"),\no-attrs());
     
test bool test11() = getProduction((Prod) `PICO-ID ":" TYPE -> ID-TYPE`, true) == 
     prod([sort("PICO-ID"),lit(":"),sort("TYPE")],sort("ID-TYPE"),attrs([\lex()]));

test bool test12() = getProduction((Prod) `PICO-ID ":" TYPE -> ID-TYPE {cons("decl"), left}`, false) ==
     prod([sort("PICO-ID"), lit(":"), sort("TYPE")],
               sort("ID-TYPE"),
               attrs([term("cons"("decl")),\assoc(left())]));
               
test bool test13() = getProduction((Prod) `[\ \t\n\r]	-> LAYOUT {cons("whitespace")}`, true) == 
	 prod([\char-class([range(32,32),range(9,9),range(10,10),range(13,13)])],sort("LAYOUT"),attrs([term("cons"("whitespace")),\lex()]));

test bool test14() = getProduction((Prod) `{~[\n]* [\n]}* -> Rest`, true) ==
     prod([\iter-star-seps(\iter-star(\char-class([range(0,9),range(11,65535)])),[\char-class([range(10,10)])])],sort("Rest"),attrs([\lex()]));


// ----- getRestrictions, getRestriction -----

public set[Production] getRestrictions(Restriction* restrictions, bool isLex) {
  return { getRestriction(r, isLex) | Restriction r <- restrictions };
}

public set[Production] getRestriction(Restriction restriction, bool isLex) {
  switch (restriction) {
    case (Restriction) `-/- <Lookaheads ls>` :
    	return {};
    	
    case (Restriction) `<Sym s1> <Sym s2> <Sym+ rest> -/- <Lookaheads ls>` : 
      return getRestriction((Restriction) `<Sym s1> -/- <Lookaheads ls>`, isLex)
           + getRestriction((Restriction) `<Sym s2> -/- <Lookaheads ls>`, isLex) 
           + {getRestriction((Restriction) `<Sym s> -/- <Lookaheads ls>`, isLex) | Sym s <- rest};
           
    case (Restriction) `<Sym s1> -/- <Lookaheads ls>` :
      return {restrict(getSymbol(s1, isLex), others(getSymbol(s1, isLex)), getLookaheads(getSymbol(s1,isLex),ls))};
      
    default:
    	throw "missing case <restriction>";
  }
}

test bool test18() = getRestriction((Restriction) `-/- [a-z]`, true) == {};

test bool test19() = getRestriction((Restriction) `ID -/- [a-z]`, true) == 
     {restrict(sort("ID"),others(sort("ID")),{prod([\char-class([range(97,122)])],restricted(sort("ID")),\no-attrs())})};
   

// ----- getLookaheads, getLookahead -----

public set[Production] getLookaheads(Symbol sym, Lookaheads ls) {
   switch (ls) {
     case (Lookaheads) `<Class c>` :
     	return {prod([getCharClass(c)],restricted(sym),\no-attrs())};
     	
     case (Lookaheads) `<Class c>.<Lookaheads ls>` :
     	return {prod([getCharClass(c)] + x, restricted(sym), \no-attrs()) | prod(list[Symbol] x, _,_) <- getLookaheads(sym,ls)};
     case (Lookaheads) `<Lookaheads l> | <Lookaheads r>` :
     	return getLookaheads(sym,l) + getLookaheads(sym,r);
     	
     case (Lookaheads) `(<Lookaheads l>)` :
     	return getLookaheads(sym,l);
     	
     case (Lookaheads) `[[<{Lookahead ","}* _>]]`:
     	throw "unsupported lookahead construction <ls>";
     	
     default: throw "unsupported lookahead construction <ls>";
   }
}
 
test bool test21() = getLookaheads(sort("X"), (Lookaheads) `[a-z]`) == 
     {prod([\char-class([range(97,122)])],restricted(sort("X")),\no-attrs())};
     
test bool test22() = getLookaheads(sort("X"), (Lookaheads) `[a-z] . [0-9]`) ==
     {prod([\char-class([range(97,122)]),\char-class([range(48,57)])],restricted(sort("X")),\no-attrs())};
       
test bool test23() = getLookaheads(sort("X"), (Lookaheads) `([a-z] . [0-9]) | [\"]`) ==
     {prod([\char-class([range(97,122)]),\char-class([range(48,57)])], restricted(sort("X")),\no-attrs()),
      prod([\char-class([range(34,34)])],restricted(sort("X")),\no-attrs())};
     
// ----- getPriorities, getPriority -----

public set[Production] getPriorities({Priority ","}* priorities, bool isLex) {
  return {getPriority(p, isLex) | Priority p <- priorities};
}

public Production getPriority(Group group, bool isLex) {
	switch (group) {
    case (Group) `<Prod p>` :   
      	return getProduction(p, isLex);
       
    case (Group) `<Group g> .` :
     	return getPriority(g, isLex); // we ignore non-transitivity here!
     	
    case (Group) `<Group g> <ArgumentIndicator i>` : 
     	return getPriority(g, isLex); // we ignore argument indicators here!
     	
    case (Group) `{<Prod* ps>}` : 
       return choice(definedSymbol(ps,isLex), {getProduction(p,isLex) | Prod p <- ps});
       
    case (Group) `{<Assoc a> : <Prod* ps>}` : 
       return \assoc(definedSymbol(ps, isLex), getAssociativity(a), {getProduction(p,isLex) | Prod p <- ps});
    
    default:
    	throw "missing case <group>";}
}
     	
test bool test24() = getPriority((Group) `A -> B`, false) == 
     prod([sort("A")],sort("B"),\no-attrs());
     
test bool test25() = getPriority((Group) `A -> B .`, false) == 
     prod([sort("A")],sort("B"),\no-attrs());
     
//@ignore
//test bool test26() = getPriority((Group) `A -> B <1>`, false) == 
//     prod([sort("A")],sort("B"),\no-attrs());
      
test bool test27() = getPriority((Group) `{A -> B C -> D}`, false) == 
	 choice(sort("B"),{prod([sort("C")],sort("D"),\no-attrs()),prod([sort("A")],sort("B"),\no-attrs())});  
	  
test bool test28() = getPriority((Group) `{left: A -> B}`, false) == 
     \assoc(sort("B"),\left(),{prod([sort("A")],sort("B"),\no-attrs())});
     
test bool test29() = getPriority((Group) `{left: A -> B B -> C}`, false) ==
     \assoc(sort("B"),\left(),{prod([sort("B")],sort("C"),\no-attrs()),prod([sort("A")],sort("B"),\no-attrs())});

public Production getPriority(Priority priority, bool isLex) {
   switch (priority) {
     case (Group) `<Group g>`:
     	return getPriority(g, isLex);
         
     case (Priority) `<Group g1> <Assoc a> <Group g2>` : 
       return \assoc(definedSymbol(g1, isLex), getAssociativity(a), {getPriority((Priority) `<Group g1>`, isLex), getPriority((Priority) `<Group g2>`,isLex)});
 
     case (Priority) `<{Group "\>"}+ groups>` : 
       return first(definedSymbol(groups,isLex), [getPriority(group,isLex) | Group group <- groups]);
   }
}

test bool test30() = getPriority((Priority) `A -> B`, false) == 
     first(sort("B"),[prod([sort("A")],sort("B"),\no-attrs())]);
     
test bool test31() = getPriority((Priority) `A -> B .`, false) == 
     first(sort("B"),[prod([sort("A")],sort("B"),\no-attrs())]);

//@ignore   
//test bool test32) = getPriority((Priority) `A -> B <1>`, false) == 
//     prod([sort("A")],sort("B"),\no-attrs());
     
test bool test33() = getPriority((Priority) `{A -> B C -> D}`, false) == 
     first(sort("B"),[choice(sort("B"),{prod([sort("C")],sort("D"),\no-attrs()),prod([sort("A")],sort("B"),\no-attrs())})]);
     
test bool test34() = getPriority((Priority) `A -> B > C -> D`, false) ==
     first(sort("B"),[prod([sort("A")],sort("B"),\no-attrs()),prod([sort("C")],sort("D"),\no-attrs())]);
     
test bool test35() = getPriority((Priority) `A -> B > C -> D > E -> F`, false) ==
     first(sort("B"),[prod([sort("A")],sort("B"),\no-attrs()),prod([sort("C")],sort("D"),\no-attrs()),prod([sort("E")],sort("F"),\no-attrs())]);

// ----- definedSymbol -----

public Symbol definedSymbol((&T <: Tree) v, bool isLex) {
  // Note that this might not work if there are different right-hand sides in the group...
  // I don't know what to do about this yet.
  if (/(Prod) `<Sym* _> -> <Sym s> <Attrs _>` := v) {
    return getSymbol(s, isLex);
  } else if (/(Prod) `<Sym* _> -> LAYOUT <Attrs _>` := v) {
    return sort("LAYOUT");
  }
  throw "could not find a defined symbol in <v>";
}

// ----- getStartSymbols -----

public set[Symbol] getStartSymbols(SDF definition) {
  result = {};
  visit(definition) {
    case (Grammar) `context-free start-symbols <Sym* syms>` :
    	result += { getSymbol(sym, true) | sym <- syms };
    case (Grammar) `lexical start-symbols <Sym* syms>`      :
    	result += { getSymbol(sym, true) | sym <- syms };
    case (Grammar) `start-symbols <Sym* syms>`              :
    	result += { getSymbol(sym, true) | sym <- syms };
  }
  return result;
}

test bool test36() = getStartSymbols((SDF) `definition module M exports context-free start-symbols A B C`) == 
     {sort("A"), sort("B"), sort("C")};
     
test bool test37() = getStartSymbols((SDF) `definition module M exports lexical start-symbols A B C`) == 
     {sort("A"), sort("B"), sort("C")};
     
test bool test38() = getStartSymbols((SDF) `definition module M exports start-symbols A B C`) == 
     {sort("A"), sort("B"), sort("C")};

// ----- getSymsols, getSymbol -----
     
public list[Symbol] getSymbols(Syms syms, bool isLex){
  if ((Syms) `<Sym* ss>` := syms) {
    return [getSymbol(sym, isLex) | sym <- ss];
  }
  return [];
}

test bool test39() = getSymbols((Syms) `A B "ab"`, true) == [sort("A"), sort("B"), lit("ab")];
     

public Symbol getSymbol(Sym sym, bool isLex) {
  switch (sym) {
    case (Sym) `LAYOUT ?`:
        return \layouts("LAYOUTLIST");
    case (Sym) `<StrCon l> : <Sym s>`:
		return label(unescape(l), getSymbol(s,isLex));
		
    case (Sym) `<IdCon i> : <Sym s>`:
    	return label("<i>", getSymbol(s, isLex));
    	
    case (Sym) `<Sort n>`:
    	return sort("<n>");
    	
   	case (Sym) `LAYOUT`:
    	return sort("LAYOUT"); 
    	
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
    	
    case (Sym) `( <Sym sym> <Sym+ syms> )`:
    	return \seq(getSymbols((Syms) `<sym> <syms>`, isLex));
    	
    case (Sym) `< <Sym sym> -LEX >`:
        return getSymbol(sym, isLex);
        
    case (Sym) `< <Sym sym> -CF >`:
        return getSymbol(sym, isLex);
       
    case (Sym) `< <Sym sym> -VAR >`:
        return getSymbol(sym, isLex);
       
  }  
  
  if (isLex) switch (sym) {
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
    	
    case (Sym) `{<Sym s> <Sym sep>} *?` :
    	return \iter-star-seps(getSymbol(s,isLex),  [getSymbol(sep, isLex)]);
    	
    case (Sym) `{<Sym s> <Sym sep>} +?` :
    	return \opt(\iter-seps(getSymbol(s,isLex),  [getSymbol(sep, isLex)]));
    	
    default: throw "missed a case <sym>";
  } 
  else switch (sym) {  
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
    	
    case (Sym) `{<Sym s> <Sym sep>} *?`:
    	return \iter-star-seps(getSymbol(s,isLex), [\layouts("LAYOUTLIST"),getSymbol(sep, isLex),\layouts("LAYOUTLIST")]);
    	
    case (Sym) `{<Sym s> <Sym sep>} +?`:
    	return \iter-seps(getSymbol(s,isLex), [\layouts("LAYOUTLIST"),getSymbol(sep, isLex),\layouts("LAYOUTLIST")]);
    default: throw "missed a case <sym>";  
  }
}

test bool test40() = getSymbol((Sym) `"abc"`, false) 		== lit("abc");
test bool test41() = getSymbol((Sym) `"a\\c"`, false) 		== lit("a\\c");
test bool test42() = getSymbol((Sym) `"a>c"`, false) 		== lit("a\>c");
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
test bool testUn2() = unescape((StrCon) `"a\nc"`) 	== "a\nc";
test bool testUn3() = unescape((StrCon) `"a\"c"`) 	== "a\"c";
test bool testUn4() = unescape((StrCon) `"a\\c"`) 	== "a\\c";
test bool testUn5() = unescape((StrCon) `"a\\\"c"`)	== "a\\\"c";

test bool testUn6() = unescape((SingleQuotedStrCon) `'abc'`)  == "abc";
test bool testUn7() = unescape((SingleQuotedStrCon) `'a\nc'`) == "a\nc";
test bool testUn8() = unescape((SingleQuotedStrCon) `'a\'c'`) == "a\'c";
test bool testUn9() = unescape((SingleQuotedStrCon) `'a\\c'`) == "a\\c";

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
       case /^\\TOP/         => "\255"
       case /^\\EOF/         => "\256"
       case /^\\BOT/         => "\0"
       case /^\\LABEL_START/ => "\257"
       case /^\</			 => "\<"
       case /^\>/			 => "\>"
     };      
}

test bool un20() =Str("abc") 	== "abc";
test bool un21() =Str("a\nbc") 	== "a\nbc";
test bool un22() =Str("a\\\nbc") == "a\\\nbc";
test bool un23() =Str("a\"bc") 	== "a\"bc";
test bool un24() =Str("a\\\"bc") == "a\"bc";
test bool un25() =Str("a\\bc") 	== "a\bc";
test bool un26() =Str("a\\\\tc") == "a\\tc";
test bool un27() =Str("a\>b")    == "a\>b";
test bool un28() =Str("a\<b")    == "a\<b";

public Symbol getCharClass(Class cc) {
   switch(cc) {
     case (Class) `[]` :
     	return \char-class([]);
     	
     case (Class) `[<OptRanges ranges>]` :
     		return \char-class([getCharRange(r) | /Range r := ranges]);
     	
     case (Class) `(<Class c>)`: 
     	return getCharClass(c);
     	
     case (Class) `~ <Class c>`:
     	return complement(getCharClass(c));
     	
     case (Class) `<Class l> /\ <Class r>`:
     	return intersection(getCharClass(l),getCharClass(r));
     	
     case (Class) `<Class l> \/ <Class r>`: 
     	return union(getCharClass(l),getCharClass(r));
     	
     case (Class) `<Class l> / <Class r>`:
     	return difference(getCharClass(l),getCharClass(r));
     	
     default: throw "missed a case <cc>";
   }
}

test bool testCC1() = ((Class) `[]`)         == \char-class([]);
test bool testCC2() = ((Class) `[a]`)        == \char-class([range(97,97)]);
test bool testCC3() = ((Class) `[a-z]`)      == \char-class([range(97,122)]);
test bool testCC4() = ((Class) `[a-z0-9]`)   == \char-class([range(97,122), range(48,57)]);
test bool testCC5() = ((Class) `([a])`)      == \char-class([range(97,97)]);
test bool testCC6() = ((Class) `~[a]`)       == complement(\char-class([range(97,97)]));
test bool testCC7() = ((Class) `[a] /\ [b]`) == intersection(\char-class([range(97,97)]), \char-class([range(98,98)]));
test bool testCC8() = ((Class) `[a] \/ [b]`) == union(\char-class([range(97,97)]), \char-class([range(98,98)]));
test bool testCC9() = ((Class) `[a] / [b]`)  == difference(\char-class([range(97,97)]), \char-class([range(98,98)]));
test bool testCC10() = ((Class) `[\n]`)       == \char-class([range(10,10)]);
test bool testCC11() = ((Class) `[\t\n]`)     == \char-class([range(9,9), range(10,10)]);
test bool testCC12() = ((Class) `~[\0-\31\n\t\"\\]`) ==
     complement(\char-class([range(0,25),range(10,10),range(9,9),range(34,34),range(92,92)]));
test bool testCC13() = ((Class) `[\"]`)       == \char-class([range(34,34)]);

// ----- getCharRange -----
      
public CharRange getCharRange(Range r) {
  switch (r) {
    case (Range) `<Character c>` : return range(getCharacter(c),getCharacter(c));
    case (Range) `<Character l> - <Character r>`: return range(getCharacter(l),getCharacter(r));
    default: throw "missed a case <r>";
  }
}

test bool testCR1() = getCharRange((Range) `a`)   	== range(97,97);
test bool testCR2() = getCharRange((Range) `a-z`) 	== range(97,122);
test bool testCR3() = getCharRange((Range) `\n`)  	==  range(10,10);
test bool testCR4() = getCharRange((Range) `\1-\31`)	==  range(1,25);

public int getCharacter(Character c) {
  switch (c) {
    case [Character] /\\<oct:[0-3][0-7][0-7]>/ : return toInt("0<oct>");
    case [Character] /\\<oct:[0-7][0-7]>/      : return toInt("0<oct>");
    case [Character] /\\<oct:[0-7]>/           : return toInt("0<oct>");
    case [Character] /\\t/                     : return 9;
    case [Character] /\\n/                     : return 10;
    case [Character] /\\r/                     : return 13;
    case [Character] /\\ /                     : return 32;
    case [Character] /\\<esc:["'\-\[\]\\ ]>/   : return charAt(esc, 0);
    case [Character] /<ch:[^"'\-\[\]\\ ]>/     : return charAt(ch, 0);
    
    default: throw "missed a case <c>";
  }
}
 
test bool testCCX1() = ((Character) `a`)    == charAt("a", 0);
test bool testCCX2() = ((Character) `\\`)   == charAt("\\", 0);
test bool testCCX3() = ((Character) `\'`)   == charAt("\'", 0);
test bool testCCX4() = ((Character) `\1`)   == toInt("01");
test bool testCCX5() = ((Character) `\12`)  == toInt("012");
test bool testCCX6() = ((Character) `\123`) == toInt("0123");
test bool testCCX7() = ((Character) `\n`)   == 10; 

// ----- getAttributes, getAttribute, getAssociativity -----

public Attributes getAttributes(Attrs as) {
  if ((Attrs) `{ <{Attribute ","}* mods> }` := as) {
	  return attrs([getAttribute(m) | Attribute m <- mods]);
  }
  return \no-attrs();
}
   
test bool testAs() = getAttributes((Attrs) `{left, cons("decl")}`) == attrs([\assoc(\left()),term("cons"("decl"))]);

public Attr getAttribute(Attribute m) {
  switch (m) {
    case (Attribute) `<Assoc as>`:
     	return \assoc(getAssociativity(as));
     	
    case (Attribute) `bracket`:
    	return \bracket();
    
    case (Attribute) `cons(<StrCon c>)` : {
    	return term("cons"(unescape(c)));
    	}
    case (Attribute) `memo`:
    	return term("memo"());
    	
    case (Attribute) `prefer`:
        return term("prefer"());
        
    case (Attribute) `avoid` :
        return term("avoid"());
    	
    case (Attribute) `reject` :
        return term("reject"());
        
    case (Attribute) `<IdCon c>(<StrCon a>)` : 
        return term("<c>"(unescape(a)));
        
    case (Attribute) `<ATerm t>`:
        return term("<t>");
        
    default: throw "missed a case <m>";
  }
}

test bool testAs2() = getAttribute((Attribute) `left`)        == \assoc(\left());
test bool testAs3() = getAttribute((Attribute) `cons("abc")`) == term("cons"("abc"));
 
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
