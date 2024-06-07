@license{
  Copyright (c) 2009-2021 NWO-I CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@contributor{Paul Klint - (CWI, Swat.engineering)}
module lang::rascal::grammar::ParserGenerator

import Grammar;
import lang::rascal::grammar::definition::Parameters;
import lang::rascal::grammar::definition::Regular;
import lang::rascal::grammar::definition::Productions;
import lang::rascal::grammar::definition::Modules;
import lang::rascal::grammar::definition::Priorities;
import lang::rascal::grammar::definition::Literals;
import lang::rascal::grammar::definition::Symbols;
import lang::rascal::grammar::definition::Keywords;
import lang::rascal::grammar::Lookahead;

import util::Monitor;
import lang::rascal::\syntax::Rascal;
import lang::rascal::grammar::ConcreteSyntax;
import ParseTree;
import String;
import List;
import Node;
import Set;
  
// TODO: replace this complex data structure with several simple ones
alias Items = map[Symbol,map[Item item, tuple[str new, int itemId] new]];

data Symbol(int id = 0, str prefix = "");

public str getParserMethodName(Sym sym) = getParserMethodName(sym2symbol(sym));
str getParserMethodName(label(_,Symbol s)) = getParserMethodName(s);
str getParserMethodName(conditional(Symbol s, _)) = getParserMethodName(s);
default str getParserMethodName(Symbol s) = value2id(s);

public str newGenerate(str package, str name, Grammar gr) {	
    return job("Generating <name>", str (void (str m, int w) worked) { 
    int uniqueItem = 1; // -1 and -2 are reserved by the SGTDBF implementation
    int newItem() { uniqueItem += 1; return uniqueItem; };
  
    worked("expanding parameterized symbols", 1);
    gr = expandParameterizedSymbols(gr);
    
    worked("generating stubs for regular", 1);
    gr = makeRegularStubs(gr);
    
    worked("generating syntax for holes", 1);
    gr = addHoles(gr);
 
    worked("generating literals", 1);
    gr = literals(gr);
    
    worked("establishing production set", 1);
    uniqueProductions = {p | /Production p := gr, prod(_,_,_) := p || regular(_) := p};
 
    worked("assigning unique ids to symbols", 1);
    Production rewrite(Production p) = 
      visit (p) { 
        case Symbol s => s[id=newItem()] 
      };
    beforeUniqueGr = gr;   
    gr.rules = (s : rewrite(gr.rules[s]) | s <- gr.rules);
        
    worked("generating item allocations", 1);
    newItems = generateNewItems(gr);
    
    worked("computing priority and associativity filter", 1);
    rel[int parent, int child] dontNest = computeDontNests(newItems, beforeUniqueGr, gr);
    // this creates groups of children that forbidden below certain parents
    rel[set[int] children, set[int] parents] dontNestGroups = 
      {<c,g[c]> | rel[set[int] children, int parent] g := {<dontNest[p],p> | p <- dontNest.parent}, c <- g.children};
   
    //println("computing lookahead sets", 1);
    //gr = computeLookaheads(gr, extraLookaheads);
    
    //println("optimizing lookahead automaton", 1);
    //gr = compileLookaheads(gr);
   
    worked("source code template", 1);
    
    return "package <package>;
           '
           'import java.io.IOException;
           'import java.io.StringReader;
           '
           'import io.usethesource.vallang.type.TypeFactory;
           'import io.usethesource.vallang.IConstructor;
           'import io.usethesource.vallang.ISourceLocation;
           'import io.usethesource.vallang.IValue;
           'import io.usethesource.vallang.IValueFactory;
           'import io.usethesource.vallang.exceptions.FactTypeUseException;
           'import io.usethesource.vallang.io.StandardTextReader;
           'import org.rascalmpl.parser.gtd.stack.*;
           'import org.rascalmpl.parser.gtd.stack.filter.*;
           'import org.rascalmpl.parser.gtd.stack.filter.follow.*;
           'import org.rascalmpl.parser.gtd.stack.filter.match.*;
           'import org.rascalmpl.parser.gtd.stack.filter.precede.*;
           'import org.rascalmpl.parser.gtd.preprocessing.ExpectBuilder;
           'import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
           'import org.rascalmpl.parser.gtd.util.IntegerList;
           'import org.rascalmpl.parser.gtd.util.IntegerMap;
           'import org.rascalmpl.values.ValueFactoryFactory;
           'import org.rascalmpl.values.RascalValueFactory;
           'import org.rascalmpl.values.parsetrees.ITree;
           '
           '@SuppressWarnings(\"all\")
           'public class <name> extends org.rascalmpl.parser.gtd.SGTDBF\<IConstructor, ITree, ISourceLocation\> {
           '  protected final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
           '
           '  protected static IValue _read(java.lang.String s, io.usethesource.vallang.type.Type type) {
           '    try {
           '      return new StandardTextReader().read(VF, org.rascalmpl.values.RascalValueFactory.uptr, type, new StringReader(s));
           '    }
           '    catch (FactTypeUseException e) {
           '      throw new RuntimeException(\"unexpected exception in generated parser\", e);  
           '    } catch (IOException e) {
           '      throw new RuntimeException(\"unexpected exception in generated parser\", e);  
           '    }
           '  }
           '	
           '  protected static java.lang.String _concat(java.lang.String ...args) {
           '    int length = 0;
           '    for (java.lang.String s :args) {
           '      length += s.length();
           '    }
           '    java.lang.StringBuilder b = new java.lang.StringBuilder(length);
           '    for (java.lang.String s : args) {
           '      b.append(s);
           '    }
           '    return b.toString();
           '  }
           '  protected static final TypeFactory _tf = TypeFactory.getInstance();
           ' 
           '  private static final IntegerMap _resultStoreIdMappings;
           '  private static final IntegerKeyedHashMap\<IntegerList\> _dontNest;
           '	
           '  protected static void _putDontNest(IntegerKeyedHashMap\<IntegerList\> result, int parentId, int childId) {
           '    IntegerList donts = result.get(childId);
           '    if (donts == null) {
           '      donts = new IntegerList();
           '      result.put(childId, donts);
           '    }
           '    donts.add(parentId);
           '  }
           '    
           '  protected int getResultStoreId(int parentId) {
           '    return _resultStoreIdMappings.get(parentId);
           '  }
           '    
           '  protected static IntegerKeyedHashMap\<IntegerList\> _initDontNest() {
           '    IntegerKeyedHashMap\<IntegerList\> result = new IntegerKeyedHashMap\<IntegerList\>(); 
           '    
           '    <if (true) { int i = 0;>
           '    <for (<f,c> <- (dontNest)) { i += 1;>
           '    <if (i % 2000 == 0) {>
           '    _initDontNest<i>(result);
           '    <if (i == 2000) {>return result;<}>
           '  }
           '  protected static void _initDontNest<i>(IntegerKeyedHashMap\<IntegerList\> result) {<}>
           '    _putDontNest(result, <f>, <c>);<}>
           '   <if (i < 2000) {>return result;<}><}>
           '  }
           '    
           '  protected static IntegerMap _initDontNestGroups() {
           '    IntegerMap result = new IntegerMap();
           '    int resultStoreId = result.size();
           '    
           '    <for (<_, parentIds> <- (dontNestGroups)) {>
           '    ++resultStoreId;
           '    <for (pid <- (parentIds)) {>
           '    result.putUnsafe(<pid>, resultStoreId);<}><}>
           '      
           '    return result;
           '  }
           '  
           '  protected boolean hasNestingRestrictions(java.lang.String name){
           '		return (_dontNest.size() != 0); // TODO Make more specific.
           '  }
           '    
           '  protected IntegerList getFilteredParents(int childId) {
           '		return _dontNest.get(childId);
           '  }
           '    
           '  // initialize priorities     
           '  static {
           '    _dontNest = _initDontNest();
           '    _resultStoreIdMappings = _initDontNestGroups();
           '  }
           '    
           '  // Production declarations
           '	<for (p <- (uniqueProductions)) {>
           '  private static final IConstructor <value2id(p)> = (IConstructor) _read(\"<esc("<p>")>\", RascalValueFactory.Production);<}>
           '    
           '  // Item declarations
           '	<for (Symbol s <- (newItems<0>), isNonterminal(s)) {
	           items = newItems[unsetRec(s)];
	           map[Production prods, list[Item] items] alts = ();
	           for(Item item <- items) {
		         Production prod = item.production;
		         if (prod in alts) {
			       alts[prod] = alts[prod] + item;
		         } else {
			     alts[prod] = [item];
		       }
	         }>
           '	
           '  protected static class <value2id(s)> {
           '    public final static AbstractStackNode\<IConstructor\>[] EXPECTS;
           '    static{
           '      ExpectBuilder\<IConstructor\> builder = new ExpectBuilder\<IConstructor\>(_dontNest, _resultStoreIdMappings);
           '      init(builder);
           '      EXPECTS = builder.buildExpectArray();
           '    }
           '    <for(Production alt <- (alts.prods)) { list[Item] lhses = alts[alt]; id = value2id(alt);>
           '    protected static final void _init_<id>(ExpectBuilder\<IConstructor\> builder) {
           '      AbstractStackNode\<IConstructor\>[] tmp = (AbstractStackNode\<IConstructor\>[]) new AbstractStackNode[<size(lhses)>];
           '      <for (Item i <- lhses) { ii = (i.index != -1) ? i.index : 0;>
           '      tmp[<ii>] = <items[unsetRec(i)].new>;<}>
           '      builder.addAlternative(<name>.<id>, tmp);
           '	}<}>
           '    public static void init(ExpectBuilder\<IConstructor\> builder){
           '      <for(Production alt <- (alts.prods)) { list[Item] lhses = alts[alt]; id = value2id(alt);>
           '        _init_<id>(builder);
           '      <}>
           '    }
           '  }<}>
           '
           '  private int nextFreeStackNodeId = <newItem()>;
           '  @Override
           '  protected int getFreeStackNodeId() {
           '    return nextFreeStackNodeId++;
           '  }
           '
           '  // Parse methods    
           '  <for (Symbol nont <- (gr.rules.sort), isNonterminal(nont)) { >
           '  <generateParseMethod(newItems, gr.rules[unsetRec(nont)])><}>
           '}";
    }, totalWork=9);      
}  

rel[int,int] computeDontNests(Items items, Grammar grammar, Grammar uniqueGrammar) {
  // first we compute a map from productions to their last items (which identify each production)
  prodItems = (p:items[getType(rhs)][item(p,size(lhs)-1)].itemId | /Production p:prod(Symbol rhs,list[Symbol] lhs, _) := grammar);
  
  // Note that we do not need identifiers for "regular" productions, because these can not be the forbidden child in a priority, assoc
  // or except filter. They can be the fathers though. 
  
  // now we get the "don't nest" relation, which is defined by associativity and priority declarations, and excepts
  dnn = doNotNest(grammar);
  
  // finally we produce a relation between item id for use in the internals of the parser
  return {<items[getType(father.def)][item(father,pos)].itemId, prodItems[child]> | <father,pos,child> <- dnn, father is prod}
       + {<getItemId(t, pos, child), prodItems[child]> | <regular(s),pos,child> <- dnn, /Symbol t := uniqueGrammar, unsetRec(t) == s};
}

int getItemId(Symbol s, int pos, prod(label(str l, Symbol _),list[Symbol] _, set[Attr] _)) {
  switch (s) {
    case Symbol::\opt(Symbol t) : return t.id; 
    case Symbol::\iter(Symbol t) : return t.id;
    case Symbol::\iter-star(Symbol t) : return t.id; 
    case Symbol::\iter-seps(Symbol t,_) : if (pos == 0) return t.id; else fail;
    case Symbol::\iter-seps(_,ss) : if (pos > 0)  return ss[pos-1].id; else fail;
    case Symbol::\iter-star-seps(Symbol t,_) : if (pos == 0) return t.id; else fail;
    case Symbol::\iter-star-seps(_,ss) : if (pos > 0) return ss[pos-1].id; else fail;
    case Symbol::\seq(ss) : return ss[pos].id;
    // note the use of the label l from the third function parameter:
    case Symbol::\alt(aa) : if (a:conditional(_,{*_,except(l)}) <- aa) return a.id; 
  }
  return s.id; // this should never happen, but let's make this robust
}



Symbol getType(Production p) = getType(p.def);
Symbol getType(label(str _, Symbol s)) = getType(s);
Symbol getType(conditional(Symbol s, set[Condition] cs)) = getType(s);
default Symbol getType(Symbol s) = unsetRec(s);


@synopsis{This function generates Java code to allocate a new item for each position in the grammar.
We first collect these in a map, such that we can generate static fields. It's a simple matter of caching
constants to improve run-time efficiency of the generated parser}
map[Symbol,map[Item,tuple[str new, int itemId]]] generateNewItems(Grammar g) {
  map[Symbol,map[Item,tuple[str new, int itemId]]] items = ();
  map[Item,tuple[str new, int itemId]] fresh = ();
  Production cl(Production p) = unsetRec(p);
  
  visit (g) {
    case Production p:prod(Symbol s,[],_) : 
       items[getType(s)]?fresh += (item(cl(p), -1):<"new EpsilonStackNode\<IConstructor\>(<s.id>, 0)", s.id>);
    case Production p:prod(Symbol s,list[Symbol] lhs, _) : {
      for (int i <- index(lhs)) { 
        items[getType(s)]?fresh += (item(cl(p), i): sym2newitem(g, lhs[i], i));
      }  
    }
    case Production p:regular(Symbol s) : {
      while (s is conditional || s is label)
        s = s.symbol;
      us = unsetRec(s);
      p = unsetRec(p);

      switch(s) {
        case \iter(Symbol elem) : 
          items[us]?fresh += (item(p,0):sym2newitem(g, elem, 0));
        case \iter-star(Symbol elem) : 
          items[us]?fresh += (item(p,0):sym2newitem(g, elem, 0));
        case \iter-seps(Symbol elem, list[Symbol] seps) : {
          items[us]?fresh += (item(p,0):sym2newitem(g, elem, 0));
          for (int i <- index(seps)) 
            items[us]?fresh += (item(p,i+1):sym2newitem(g, seps[i], i+1));
        }
        case \iter-star-seps(Symbol elem, list[Symbol] seps) : {
          items[us]?fresh += (item(p,0):sym2newitem(g, elem, 0));
          for (int i <- index(seps)) 
            items[us]?fresh += (item(p,i+1):sym2newitem(g, seps[i], i+1));
        }
        // not sure if these belong here
        case \seq(list[Symbol] elems) : {
          for (int i <- index(elems))
            items[us]?fresh += (item(p,i+1):sym2newitem(g, elems[i], i+1));
        }
        case \opt(Symbol elem) : {
          items[us]?fresh += (item(p,0):sym2newitem(g, elem, 0));
        }
        case \alt(set[Symbol] alts) : {
          for (Symbol elem <- alts) 
            items[us]?fresh += (item(p,0):sym2newitem(g, elem, 0));
        }
        case \empty() : {
           items[us]?fresh += (item(p, -1):<"new EpsilonStackNode\<IConstructor\>(<s.id>, 0)", s.id>);
        }
      }
    }
  }
  
  return items;
}

str split(str x) {
  if (size(x) <= 20000) {
    return "\"<esc(x)>\"";
  }
  else {
    return "<split(substring(x, 0,10000))>, <split(substring(x, 10000))>"; 
  }
}

@synopsis{this function selects all symbols for which a parse method should be generated}
bool isNonterminal(Symbol s) {
  switch (s) {
    case Symbol::\label(_,x) : return isNonterminal(x);
    case Symbol::\sort(_) : return true;
    case Symbol::\lex(_) : return true;
    case Symbol::\keywords(_) : return true;
    case Symbol::\parameterized-sort(_,_) : return true;
    case Symbol::\parameterized-lex(_,_) : return true;
    case Symbol::\start(_) : return true;
    case Symbol::\layouts(_) : return true;
    default: return false;
  }
}

public str generateParseMethod(Items _, Production p) {
  return "public AbstractStackNode\<IConstructor\>[] <sym2name(p.def)>() {
         '  return <sym2name(p.def)>.EXPECTS;
         '}";
}

str generateClassConditional(set[Symbol] classes) {
  if (eoi() in classes) {
    return ("lookAheadChar == 0" 
           | it + " || <generateRangeConditional(r)>"
           | \char-class(list[CharRange] ranges) <- classes, r <- ranges);
  }
  else {
    ranges = [r | \char-class(list[CharRange] ranges) <- classes, r <- ranges];
    
    return ("<generateRangeConditional(head(ranges))>"| it + " || <generateRangeConditional(r)> "
           | r <- tail(ranges));
  } 
}

str generateRangeConditional(CharRange r) {
  switch (r) {
    case range(0,0xFFFFF) : return "(true /*every char*/)";
    case range(i, i) : return "(lookAheadChar == <i>)";
    case range(i, j) : return "((lookAheadChar \>= <i>) && (lookAheadChar \<= <j>))";
    default: throw "unexpected range type: <r>";
  }
}

public str generateSeparatorExpects(Grammar grammar, list[Symbol] seps) {
   if (seps == []) {
     return "";
   }
   
   return (sym2newitem(grammar, head(seps), 1).new | it + ", <sym2newitem(grammar, seps[i+1], i+2).new>" | int i <- index(tail(seps)));
}

public str generateSequenceExpects(Grammar grammar, list[Symbol] seps) {
   if (seps == []) {
     return "";
   }
   
   return (sym2newitem(grammar, head(seps), 0).new | it + ", <sym2newitem(grammar, seps[i+1], i+1).new>" | int i <- index(tail(seps)));
}

public str generateAltExpects(Grammar grammar, list[Symbol] seps) {
   if (seps == []) {
     return "";
   }
   
   return (sym2newitem(grammar, head(seps), 0).new | it + ", <sym2newitem(grammar, seps[i+1], 0).new>" | int i <- index(tail(seps)));
}

public str literals2ints(list[Symbol] chars){
    if (chars == []) { 
      return "";
    }
    
    str result = "<head(head(chars).ranges).begin>";
    
    for (ch <- tail(chars)) {
        result += ",<head(ch.ranges).begin>";
    }
    
    return result;
}

public str ciliterals2ints(list[Symbol] chars){
    if (chars == []) { 
      return "";
    }

    result = "";

    for (\char-class(ranges) <- chars) {
      switch (ranges) {
        case [range(i, j)]: // can be i,i or two adjacent characters
          result += "{<i>, <j>},";
        case [range(int i, i), range(int j, j)]:
          result += "{<i>, <j>},";      
      }
    }
    
    return result[..-1]; // remove final comma
}

public tuple[str new, int itemId] sym2newitem(Grammar grammar, Symbol sym, int dot){
    if (Symbol::label(_,sym1) := sym)  // ignore labels 
      sym = sym1;
      
    itemId = sym.id;
    assert itemId != 0;
    
    list[str] enters = [];
    list[str] exits = [];
    filters = "";
    
    if (Symbol::conditional(_, conds) := sym) {
      conds = expandKeywords(grammar, conds);
      exits += ["new CharFollowRequirement(new int[][]{<generateCharClassArrays(ranges)>})" | follow(\char-class(ranges)) <- conds];
      exits += ["new StringFollowRequirement(new int[] {<literals2ints(str2syms(s))>})" | follow(lit(s)) <- conds]; 
      exits += ["new CaseInsensitiveStringFollowRequirement(new int[][]{<ciliterals2ints(cistr2syms(s))>})" | follow(cilit(s)) <- conds]; 
      exits += ["new CharFollowRestriction(new int[][]{<generateCharClassArrays(ranges)>})" | \not-follow(\char-class(ranges)) <- conds];
      exits += ["new StringFollowRestriction(new int[] {<literals2ints(str2syms(s))>})" | \not-follow(lit(s)) <- conds];
      exits += ["new CaseInsensitiveStringFollowRestriction(new int[][]{<ciliterals2ints(cistr2syms(s))>})" | \not-follow(cilit(s)) <- conds]; 
      exits += ["new CharMatchRestriction(new int[][]{<generateCharClassArrays(ranges)>})" | \delete(\char-class(ranges)) <- conds];
      exits += ["new StringMatchRestriction(new int[] {<literals2ints(str2syms(s))>})" | \delete(lit(s)) <- conds];
      exits += ["new CaseInsensitiveStringMatchRestriction(new int[][]{<ciliterals2ints(cistr2syms(s))>})" | \delete(cilit(s)) <- conds];
      exits += ["new AtEndOfLineRequirement()" | \end-of-line() <- conds]; 
      enters += ["new CharPrecedeRequirement(new int[][]{<generateCharClassArrays(ranges)>})" | precede(\char-class(ranges)) <- conds];
      enters += ["new StringPrecedeRequirement(new int[] {<literals2ints(str2syms(s))>})" | precede(lit(s)) <- conds]; 
      enters += ["new CaseInsensitiveStringPrecedeRequirement(new int[][]{<ciliterals2ints(cistr2syms(s))>})" | precede(cilit(s)) <- conds]; 
      enters += ["new CharPrecedeRestriction(new int[][]{<generateCharClassArrays(ranges)>})" | \not-precede(\char-class(ranges)) <- conds];
      enters += ["new StringPrecedeRestriction(new int[] {<literals2ints(str2syms(s))>})" | \not-precede(lit(s)) <- conds];
      enters += ["new CaseInsensitiveStringPrecedeRestriction(new int[][]{<ciliterals2ints(cistr2syms(s))>})" | \not-precede(cilit(s)) <- conds]; 
      enters += ["new AtColumnRequirement(<i>)" | \at-column(int i) <- conds];
      enters += ["new AtStartOfLineRequirement()" | \begin-of-line() <- conds];
      
      sym = sym.symbol;
      if (Symbol::label(_,sym1) := sym)
        sym = sym1; 
    }
    
    filters = "";
    if(enters != []){
    	filters += "new IEnterFilter[] {<head(enters)><for (enters != [], f <- tail(enters)) {>, <f><}>}";
    }else{
    	filters += "null";
    }
    
    if(exits != []){
    	filters += ", new ICompletionFilter[] {<head(exits)><for (exits != [], f <- tail(exits)) {>, <f><}>}";
    }else{
    	filters += ", null";
    }
    
    switch (sym) {
        case Symbol::\sort(_) : 
            return <"new NonTerminalStackNode\<IConstructor\>(<itemId>, <dot>, \"<sym2name(sym)>\", <filters>)", itemId>;
        case Symbol::\empty() : 
            return <"new EmptyStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(regular(sym))>, <filters>)", itemId>;
        case Symbol::\lex(_) : 
            return <"new NonTerminalStackNode\<IConstructor\>(<itemId>, <dot>, \"<sym2name(sym)>\", <filters>)", itemId>;
        case Symbol::\keywords(_) : 
            return <"new NonTerminalStackNode\<IConstructor\>(<itemId>, <dot>, \"<sym2name(sym)>\", <filters>)", itemId>;
        case Symbol::\layouts(_) :
            return <"new NonTerminalStackNode\<IConstructor\>(<itemId>, <dot>, \"<sym2name(sym)>\", <filters>)", itemId>;
        case Symbol::\parameterized-sort(_,_): 
            return <"new NonTerminalStackNode\<IConstructor\>(<itemId>, <dot>, \"<sym2name(sym)>\", <filters>)", itemId>;
        case Symbol::\parameterized-lex(_,_): 
            return <"new NonTerminalStackNode\<IConstructor\>(<itemId>, <dot>, \"<sym2name(sym)>\", <filters>)", itemId>;
        case Symbol::\parameter(_, _) :
            throw "All parameters should have been instantiated by now: <sym>";
        case Symbol::\start(_) : 
            return <"new NonTerminalStackNode\<IConstructor\>(<itemId>, <dot>, \"<sym2name(sym)>\", <filters>)", itemId>;
        case Symbol::\lit(l) : 
            if (/p:prod(lit(l,id=_),list[Symbol] chars,_) := grammar.rules[getType(sym)])
                return <"new LiteralStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(p)>, new int[] {<literals2ints(chars)>}, <filters>)",itemId>;
            else throw "literal not found in grammar: <grammar>";
        case Symbol::\cilit(l) : 
            if (/p:prod(cilit(l,id=_),list[Symbol] chars,_) := grammar.rules[getType(sym)])
                return <"new CaseInsensitiveLiteralStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(p)>, new int[] {<literals2ints(chars)>}, <filters>)",itemId>;
            else throw "ci-literal not found in grammar: <grammar>";
        case Symbol::\iter(s) : 
            return <"new ListStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(regular(sym))>, <sym2newitem(grammar, s,  0).new>, true, <filters>)",itemId>;
        case Symbol::\iter-star(s) :
            return <"new ListStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(regular(sym))>, <sym2newitem(grammar, s,  0).new>, false, <filters>)", itemId>;
        case Symbol::\iter-seps(Symbol s,list[Symbol] seps) : {
            reg = regular(sym);
            return <"new SeparatedListStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(reg)>, <sym2newitem(grammar, s,  0).new>, (AbstractStackNode\<IConstructor\>[]) new AbstractStackNode[]{<generateSeparatorExpects(grammar,seps)>}, true, <filters>)",itemId>;
        }
        case Symbol::\iter-star-seps(Symbol s,list[Symbol] seps) : {
            reg = regular(sym);
            return <"new SeparatedListStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(reg)>, <sym2newitem(grammar, s,  0).new>, (AbstractStackNode\<IConstructor\>[]) new AbstractStackNode[]{<generateSeparatorExpects(grammar,seps)>}, false, <filters>)",itemId>;
        }
        case Symbol::\opt(s) : {
            reg =  regular(sym);
            return <"new OptionalStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(reg)>, <sym2newitem(grammar, s,  0).new>, <filters>)", itemId>;
        }
        case Symbol::\alt(as) : {
            alts = [a | a <- as];
            return <"new AlternativeStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(regular(sym))>, (AbstractStackNode\<IConstructor\>[]) new AbstractStackNode[]{<generateAltExpects(grammar,  alts)>}, <filters>)", itemId>;
        }
        case Symbol::\seq(ss) : {
            return <"new SequenceStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(regular(sym))>, (AbstractStackNode\<IConstructor\>[]) new AbstractStackNode[]{<generateSequenceExpects(grammar,  ss)>}, <filters>)", itemId>;
        }
        case Symbol::\char-class(list[CharRange] ranges) : 
            return <"new CharStackNode\<IConstructor\>(<itemId>, <dot>, new int[][]{<generateCharClassArrays(ranges)>}, <filters>)", itemId>;
        default: 
            throw "unexpected symbol <sym> while generating parser code";
    }
}

public str generateCharClassArrays(list[CharRange] ranges){
    if(ranges == []) return "";
    result = "";
    if(range(from, to) := head(ranges)) 
        result += "{<from>,<to>}";
    for(range(from, to) <- tail(ranges))
        result += ",{<from>,<to>}";
    return result;
}

public str esc(Symbol s){
    return esc("<s>");
}

map[str,str] javaStringEscapes = ( "\n":"\\n", "\"":"\\\"", "\t":"\\t", "\r":"\\r","\\u":"\\\\u","\\":"\\\\");

public str esc(str s){
    return escape(s, javaStringEscapes);
}

map[str,str] javaIdEscapes     = ( "\n":"\\n", "\"":"\\\"", "\t":"\\t", "\r":"\\r","\\u":"\\\\u","\\":"\\\\", "-":"_", "_": "__"); // inlined for speed

public str escId(str s){
    return escape(s, javaIdEscapes);
}

public str sym2name(Symbol s){
    switch(s){
        case sort(x) : return "<x>";
        case label(_,x) : return sym2name(x);
        default      : return value2id(s);
    }
}

@Memo
public str value2id(value v) {
  return v2i(v);
}

str uu(value s) = escape(toBase64("<unsetRec(s)>"),("=":"00","+":"11","/":"22"));

default str v2i(value v) {
    switch (v) {
        case \start(Symbol s) : return "start__<v2i(s)>";
        case item(p:prod(Symbol u,_,_), int i) : return "<v2i(u)>.<v2i(p)>_<v2i(i)>";
        case label(str x,Symbol u) : return escId(x) + "_" + v2i(u);
        case layouts(str x) : return "layouts_<escId(x)>";
        case conditional(Symbol s,_) : return v2i(s);
        case sort(str s)   : return "<s>";
        case \lex(str s)   : return "<s>";
        case keywords(str s)   : return "<s>";
        case \parameterized-sort(str s, list[Symbol] args) : return "<s>_<uu(args)>";
        case \parameterized-lex(str s, list[Symbol] args) : return "<s>_<uu(args)>";
        case cilit(/<s:^[A-Za-z0-9\-\_]+$>/)  : return "cilit_<escId(s)>";
	        case lit(/<s:^[A-Za-z0-9\-\_]+$>/) : return "lit_<escId(s)>"; 
        case int i         : return i < 0 ? "min_<-i>" : "<i>";
        case str s         : return ("" | it + "_<charAt(s,i)>" | i <- [0..size(s)]);
        default            : return uu(v);
    }
}


Grammar makeUnique(Grammar gr) {
    int uniqueItem = 1; // -1 and -2 are reserved by the SGTDBF implementation
    int newItem() { uniqueItem += 1; return uniqueItem; };
    Production rewrite(Production p) = 
      visit(p) { 
        case Symbol s => s[id=newItem()] 
      }; 
    
    return gr[rules = (s : rewrite(gr.rules[s]) | s <- gr.rules)];
} 

