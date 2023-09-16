@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::rascalcore::grammar::ParserGenerator

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeUtils;

import lang::rascalcore::grammar::definition::Parameters;
import lang::rascalcore::grammar::definition::Regular;
import lang::rascalcore::grammar::definition::Priorities;
import lang::rascalcore::grammar::definition::Literals;
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Keywords;
import lang::rascalcore::grammar::Lookahead;

import util::Monitor;
import lang::rascal::\syntax::Rascal;
import lang::rascalcore::grammar::ConcreteSyntax;

import String;
import List;
import Node;
import Set;
import Map;
import Node;
import IO;
import Exception;
import Message;
  
// TODO: replace this complex data structure with several simple ones
alias Items = map[AType,map[Item item, tuple[str new, int itemId] new]];

data AType(int id = 0, str prefix = "");

public str getParserMethodName(Sym sym) = getParserMethodName(sym2AType(sym));
str getParserMethodName(conditional(AType s, _)) = getParserMethodName(s);
default str getParserMethodName(AType s) = value2id(s);

public tuple[list[Message], str] newGenerate(str package, str name, AGrammar gr) {	
    startJob("Generating parser <package>.<name>");
    int uniqueItem = 1; // -1 and -2 are reserved by the SGTDBF implementation
    int newItem() { uniqueItem += 1; return uniqueItem; };

    event("expanding parameterized symbols");
    gr = expandParameterizedSymbols(gr);
    
    event("generating stubs for regular");
    gr = makeRegularStubs(gr);
    
    event("generating syntax for holes");
    gr = addHoles(gr);
    
    event("expanding parameterized symbols");
    gr = expandParameterizedSymbols(gr);
    
    event("generating literals");
    gr = literals(gr);
 
    event("establishing production set");
    uniqueProductions = {p | /AProduction p := gr, prod(_,_) := p || regular(_) := p};
 
    event("assigning unique ids to symbols");
    AProduction rewrite(AProduction p) = 
      visit (p) { 
        case AType s => s[id=newItem()] 
      };
    beforeUniqueGr = gr;  
   
    gr.rules = (s : rewrite(gr.rules[s]) | s <- gr.rules);
        
    event("generating item allocations");
    newItems = generateNewItems(gr);
    
    event("computing priority and associativity filter");
    rel[int parent, int child] dontNest = {};
    <msgs, dontNest> = computeDontNests(newItems, beforeUniqueGr, gr);
    // this creates groups of children that forbidden below certain parents
    rel[set[int] children, set[int] parents] dontNestGroups = 
      {<c,g[c]> | rel[set[int] children, int parent] g := {<dontNest[p],p> | p <- dontNest.parent}, c <- g.children};
   
    //println("computing lookahead sets");
    //gr = computeLookaheads(gr, extraLookaheads);
    
    //println("optimizing lookahead automaton");
    //gr = compileLookaheads(gr);
   
    event("printing the source code of the parser class");
    
    src =  "package <package>;
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
           'import org.rascalmpl.core.parser.gtd.stack.*;
           'import org.rascalmpl.core.parser.gtd.stack.filter.*;
           'import org.rascalmpl.core.parser.gtd.stack.filter.follow.*;
           'import org.rascalmpl.core.parser.gtd.stack.filter.match.*;
           'import org.rascalmpl.core.parser.gtd.stack.filter.precede.*;
           'import org.rascalmpl.core.parser.gtd.preprocessing.ExpectBuilder;
           'import org.rascalmpl.core.parser.gtd.util.IntegerKeyedHashMap;
           'import org.rascalmpl.core.parser.gtd.util.IntegerList;
           'import org.rascalmpl.core.parser.gtd.util.IntegerMap;
           'import org.rascalmpl.core.values.ValueFactoryFactory;
           'import org.rascalmpl.core.values.RascalValueFactory;
           'import org.rascalmpl.core.values.parsetrees.ITree;
           '
           '@SuppressWarnings(\"all\")
           'public class <name> extends org.rascalmpl.core.parser.gtd.SGTDBF\<IConstructor, ITree, ISourceLocation\> {
           '  protected final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
           '
           '  protected static IValue _read(java.lang.String s, io.usethesource.vallang.type.Type type) {
           '    try {
           '      System.err.println(s);
           '      return new StandardTextReader().read(VF, org.rascalmpl.core.values.RascalValueFactory.uptr, type, new StringReader(s));
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
           '    <for (<childrenIds, parentIds> <- (dontNestGroups)) {>
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
           '  private static final IConstructor <value2id(p)> = (IConstructor) _read(\"<esc("<prettyAsClassic(p)>")>\", RascalValueFactory.Production);<}>
           '    
           '  // Item declarations
           '	<for (AType s <- (newItems<0>), isNonTerminalAType(s)) {
               s = unset(s, "alabel");
	           items = newItems[s];
	          
	           map[AProduction prods, list[Item] items] alts = ();
	           for(Item item <- items) {
		         AProduction prod = unsetRec(item.aproduction, {"alabel", "src"});
		    
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
           '      ExpectBuilder\<IConstructor\> builder = new ExpectBuilder\<IConstructor\>(_resultStoreIdMappings);
           '      init(builder);
           '      EXPECTS = builder.buildExpectArray();
           '    }
           '    <for(AProduction alt <- (alts.prods)) { list[Item] lhses = alts[alt]; id = value2id(alt);>
           '    protected static final void _init_<id>(ExpectBuilder\<IConstructor\> builder) {
           '      AbstractStackNode\<IConstructor\>[] tmp = (AbstractStackNode\<IConstructor\>[]) new AbstractStackNode[<size(lhses)>];
           '      <for (Item i <- lhses) { ii = (i.index != -1) ? i.index : 0;>
           '      tmp[<ii>] = <items[i].new>;<}>
           '      builder.addAlternative(<name>.<id>, tmp);
           '	}<}>
           '    public static void init(ExpectBuilder\<IConstructor\> builder){
           '      <for(AProduction alt <- (alts.prods)) { list[Item] lhses = alts[alt]; id = value2id(alt);>
           '        _init_<id>(builder);
           '      <}>
           '    }
           '  }<}>
           '	
           '  // Parse methods    
           '  <for (AType nont <- (gr.rules.sort), isNonTerminalAType(nont)) { >
           '  <generateParseMethod(newItems, gr.rules[nont])><}>
           '}";
   endJob(true);
   return <msgs, src>;
}  

tuple[list[Message], rel[int,int]] computeDontNests(Items items, AGrammar grammar, AGrammar uniqueGrammar) {
  // first we compute a map from productions to their last items (which identify each production)
  prodItems = (p1 : items[getType(rhs)][item(p1, size(lhs)-1)].itemId | /AProduction p:prod(AType rhs,list[AType] lhs) := grammar, p1:= unsetRec(p, {"id"}));
  
  // Note that we do not need identifiers for "regular" productions, because these can not be the forbidden child in a priority, assoc
  // or except filter. They can be the fathers though. 
  
  // now we get the "don't nest" relation, which is defined by associativity and priority declarations, and excepts
  <msgs, dnn> = doNotNest(grammar);
  
  // finally we produce a relation between item id for use in the internals of the parser
  return < msgs,
           {<items[getType(father.def)][item(father,pos)].itemId, prodItems[child]> | <father,pos,child> <- dnn, father is prod}
          + {<getItemId(t, pos, child), prodItems[child]> | <regular(s),pos,child> <- dnn, defined <- uniqueGrammar.rules, /AType t := uniqueGrammar, t == s}
         >;
}

int getItemId(AType s, int pos, prod(AType u,list[AType] _)) {
  switch (s) {
    case \opt(t) : return t.id; 
    case \iter(t) : return t.id;
    case \iter-star(t) : return t.id; 
    case \iter-seps(t,ss) : if (pos == 0) return t.id; else fail;
    case \iter-seps(t,ss) : if (pos > 0)  return ss[pos-1].id; else fail;
    case \iter-star-seps(t,ss) : if (pos == 0) return t.id; else fail;
    case \iter-star-seps(t,ss) : if (pos > 0) return ss[pos-1].id; else fail;
    case \seq(ss) : return ss[pos].id;
    // note the use of the label l from the third function parameter:
    case \alt(aa) : if (AType a:conditional(_,{_*,except(l)}) <- aa, l == u.label) return a.id; 
    default: return s.id; // this should never happen, but let's make this robust
  }  
  return s.id;  // this should never happen, but guarantee a return value
}



AType getType(AProduction p) = getType(p.def);
AType getType(conditional(AType s, set[ACondition] cs)) = getType(s);
default AType getType(AType s) = unsetRec(s, {"id", "alabel"});


@doc{This function generates Java code to allocate a new item for each position in the grammar.
We first collect these in a map, such that we can generate static fields. It's a simple matter of caching
constants to improve run-time efficiency of the generated parser}
map[AType,map[Item,tuple[str new, int itemId]]] generateNewItems(AGrammar g) {
  map[AType,map[Item,tuple[str new, int itemId]]] items = ();
  map[Item,tuple[str new, int itemId]] fresh = ();
  AProduction cl(AProduction p) = unsetRec(p, {"id"});
  
  visit (g) {
    case AProduction p:prod(AType s,[]) : 
       items[getType(s)]?fresh += (item(cl(p), -1):<"new EpsilonStackNode\<IConstructor\>(<s.id>, 0)", s.id>);
    case AProduction p:prod(AType s,list[AType] lhs) : {
      for (int i <- index(lhs)) { 
        items[getType(s)]?fresh += (item(cl(p), i): sym2newitem(g, lhs[i], i));
      }  
    }
    case AProduction p:regular(AType s) : {
      while (s is conditional/* || s is label*/){
        s = s.symbol;
      }
      us = unsetRec(s, {"id"});
      p = unsetRec(p, {"id"});

      switch(s) {
        case \iter(AType elem) : 
          items[us]?fresh += (item(p,0):sym2newitem(g, elem, 0));
        case \iter-star(AType elem) : 
          items[us]?fresh += (item(p,0):sym2newitem(g, elem, 0));
        case \iter-seps(AType elem, list[AType] seps) : {
          items[us]?fresh += (item(p,0):sym2newitem(g, elem, 0));
          for (int i <- index(seps)) 
            items[us]?fresh += (item(p,i+1):sym2newitem(g, seps[i], i+1));
        }
        case \iter-star-seps(AType elem, list[AType] seps) : {
          items[us]?fresh += (item(p,0):sym2newitem(g, elem, 0));
          for (int i <- index(seps)) 
            items[us]?fresh += (item(p,i+1):sym2newitem(g, seps[i], i+1));
        }
        // not sure if these belong here
        case \seq(list[AType] elems) : {
          for (int i <- index(elems))
            items[us]?fresh += (item(p,i+1):sym2newitem(g, elems[i], i+1));
        }
        case \opt(AType elem) : {
          items[us]?fresh += (item(p,0):sym2newitem(g, elem, 0));
        }
        case \alt(set[AType] alts) : {
          for (AType elem <- alts) 
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

@doc{this function selects all symbols for which a parse method should be generated}
bool isNonterminal(AType s) {
   return (aadt(_,parameters, sr) := s && sr in { contextFreeSyntax(), lexicalSyntax(), keywordSyntax(), layoutSyntax() })
          || \start(_) := s
          ;
}

public str generateParseMethod(Items items, AProduction p) {
  //println("generateParseMethod: <p>, <sym2name(p.def)>");
  return "public AbstractStackNode\<IConstructor\>[] <sym2name(p.def)>() {
         '  return <sym2name(p.def)>.EXPECTS;
         '}";
}

str generateClassConditional(set[AType] classes) {
  if (eoi() in classes) {
    return ("lookAheadChar == 0" 
           | it + " || <generateRangeConditional(r)>"
           | \char-class(list[ACharRange] ranges) <- classes, r <- ranges);
  }
  else {
    ranges = [r | \char-class(ranges1) <- classes, r <- ranges1];
    
    return ("<generateRangeConditional(head(ranges))>"| it + " || <generateRangeConditional(r)> "
           | r <- tail(ranges));
  } 
}

str generateRangeConditional(ACharRange r) {
  switch (r) {
    case arange(0,0xFFFFF) : return "(true /*every char*/)";
    case arange(i, i) : return "(lookAheadChar == <i>)";
    case arange(i, j) : return "((lookAheadChar \>= <i>) && (lookAheadChar \<= <j>))";
    default: throw "unexpected range type: <r>";
  }
}

public str generateSeparatorExpects(AGrammar grammar, list[AType] seps) {
   if (seps == []) {
     return "";
   }
   
   return (sym2newitem(grammar, head(seps), 1).new | it + ", <sym2newitem(grammar, seps[i+1], i+2).new>" | int i <- index(tail(seps)));
}

public str generateSequenceExpects(AGrammar grammar, list[AType] seps) {
   if (seps == []) {
     return "";
   }
   
   return (sym2newitem(grammar, head(seps), 0).new | it + ", <sym2newitem(grammar, seps[i+1], i+1).new>" | int i <- index(tail(seps)));
}

public str generateAltExpects(AGrammar grammar, list[AType] seps) {
   if (seps == []) {
     return "";
   }
   
   return (sym2newitem(grammar, head(seps), 0).new | it + ", <sym2newitem(grammar, seps[i+1], 0).new>" | int i <- index(tail(seps)));
}

public str literals2ints(list[AType] chars){
    if (chars == []) { 
      return "";
    }
    
    str result = "<head(head(chars).ranges).begin>";
    
    for (ch <- tail(chars)) {
        result += ",<head(ch.ranges).begin>";
    }
    
    return result;
}

// TODO
public str ciliterals2ints(list[AType] chars){
    throw "case insensitive literals not yet implemented by parser generator";
}

public tuple[str new, int itemId] sym2newitem(AGrammar grammar, AType sym, int dot){
    if (sym.label?)  // ignore labels 
      sym = unset(sym, "alabel");
      
    itemId = sym.id;
    assert itemId != 0;
    
    list[str] enters = [];
    list[str] exits = [];
    filters = "";
    
    if (conditional(def, conds) := sym) {
      exits += ["new CharFollowRequirement(new int[][]{<generateCharClassArrays(ranges)>})" | follow(\char-class(ranges)) <- conds];
      exits += ["new StringFollowRequirement(new int[] {<literals2ints(str2syms(s))>})" | follow(lit(s)) <- conds]; 
      exits += ["new CharFollowRestriction(new int[][]{<generateCharClassArrays(ranges)>})" | \not-follow(\char-class(ranges)) <- conds];
      exits += ["new StringFollowRestriction(new int[] {<literals2ints(str2syms(s))>})" | \not-follow(lit(s)) <- conds];
      exits += ["new CharMatchRestriction(new int[][]{<generateCharClassArrays(ranges)>})" | \delete(\char-class(ranges)) <- conds];
      exits += ["new StringMatchRestriction(new int[] {<literals2ints(str2syms(s))>})" | \delete(lit(s)) <- conds];
      exits += ["new AtEndOfLineRequirement()" | \end-of-line() <- conds]; 
      enters += ["new CharPrecedeRequirement(new int[][]{<generateCharClassArrays(ranges)>})" | precede(\char-class(ranges)) <- conds];
      enters += ["new StringPrecedeRequirement(new int[] {<literals2ints(str2syms(s))>})" | precede(lit(s)) <- conds]; 
      enters += ["new CharPrecedeRestriction(new int[][]{<generateCharClassArrays(ranges)>})" | \not-precede(\char-class(ranges)) <- conds];
      enters += ["new StringPrecedeRestriction(new int[] {<literals2ints(str2syms(s))>})" | \not-precede(lit(s)) <- conds];
      enters += ["new AtColumnRequirement(<i>)" | \at-column(int i) <- conds];
      enters += ["new AtStartOfLineRequirement()" | \begin-of-line() <- conds];
      
      sym = sym.symbol;
      if (sym.label?)  // ignore labels 
        sym = unset(sym, "alabel");
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
        case aadt(n, args, contextFreeSyntax()):    // formerly: sort/parameterized-sort
            return <"new NonTerminalStackNode\<IConstructor\>(<itemId>, <dot>, \"<sym2name(sym)>\", <filters>)", itemId>;
        
        case \empty() : 
            return <"new EmptyStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(regular(sym))>, <filters>)", itemId>;
        case aadt(n, args, lexicalSyntax()):        // formerly: lex/parameterized-lex
            return <"new NonTerminalStackNode\<IConstructor\>(<itemId>, <dot>, \"<sym2name(sym)>\", <filters>)", itemId>;
        case aadt(n, [], keywordSyntax()):          // formerly: keywords
            return <"new NonTerminalStackNode\<IConstructor\>(<itemId>, <dot>, \"<sym2name(sym)>\", <filters>)", itemId>;
        case aadt(n, [], layoutSyntax()):           // formerly: layouts
            return <"new NonTerminalStackNode\<IConstructor\>(<itemId>, <dot>, \"<sym2name(sym)>\", <filters>)", itemId>;
         
        case \aparameter(n, b) :
            throw "All parameters should have been instantiated by now: <sym>";
        
        case \start(s) : 
            return <"new NonTerminalStackNode\<IConstructor\>(<itemId>, <dot>, \"<sym2name(sym)>\", <filters>)", itemId>;
        case \lit(str l1) : 
            if (/p:prod(lit(str l2,id=_),list[AType] chars) := grammar.rules[getType(sym)], l1 == l2)
                return <"new LiteralStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(p)>, new int[] {<literals2ints(chars)>}, <filters>)",itemId>;
            else throw "literal not found in grammar: <grammar>";
        case \cilit(str l1) : 
            if (/p:prod(cilit(str l2,id=_),list[AType] chars) := grammar.rules[getType(sym)], l1 == l2)
                return <"new CaseInsensitiveLiteralStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(p)>, new int[] {<literals2ints(chars)>}, <filters>)",itemId>;
            else throw "ci-literal not found in grammar: <grammar>";
        case \iter(s) : 
            return <"new ListStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(regular(sym))>, <sym2newitem(grammar, s,  0).new>, true, <filters>)",itemId>;
        case \iter-star(s) :
            return <"new ListStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(regular(sym))>, <sym2newitem(grammar, s,  0).new>, false, <filters>)", itemId>;
        case \iter-seps(AType s,list[AType] seps) : {
            reg = regular(sym);
            return <"new SeparatedListStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(reg)>, <sym2newitem(grammar, s,  0).new>, (AbstractStackNode\<IConstructor\>[]) new AbstractStackNode[]{<generateSeparatorExpects(grammar,seps)>}, true, <filters>)",itemId>;
        }
        case \iter-star-seps(AType s,list[AType] seps) : {
            reg = regular(sym);
            return <"new SeparatedListStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(reg)>, <sym2newitem(grammar, s,  0).new>, (AbstractStackNode\<IConstructor\>[]) new AbstractStackNode[]{<generateSeparatorExpects(grammar,seps)>}, false, <filters>)",itemId>;
        }
        case \opt(s) : {
            reg =  regular(sym);
            return <"new OptionalStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(reg)>, <sym2newitem(grammar, s,  0).new>, <filters>)", itemId>;
        }
        case \alt(as) : {
            alts = [a | a <- as];
            return <"new AlternativeStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(regular(sym))>, (AbstractStackNode\<IConstructor\>[]) new AbstractStackNode[]{<generateAltExpects(grammar,  alts)>}, <filters>)", itemId>;
        }
        case \seq(ss) : {
            return <"new SequenceStackNode\<IConstructor\>(<itemId>, <dot>, <value2id(regular(sym))>, (AbstractStackNode\<IConstructor\>[]) new AbstractStackNode[]{<generateSequenceExpects(grammar,  ss)>}, <filters>)", itemId>;
        }
        case \char-class(list[ACharRange] ranges) : 
            return <"new CharStackNode\<IConstructor\>(<itemId>, <dot>, new int[][]{<generateCharClassArrays(ranges)>}, <filters>)", itemId>;
        default: 
            throw "unexpected symbol <sym> while generating parser code";
    }
}

public str generateCharClassArrays(list[ACharRange] ranges){
    if(ranges == []) return "";
    result = "";
    if(range(from, to) := head(ranges)) 
        result += "{<from>,<to>}";
    for(range(from, to) <- tail(ranges))
        result += ",{<from>,<to>}";
    return result;
}

public str esc(AType s){
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

public str sym2name(AType s){
    //return value2id(unset(s, "label"));
     res = aadt(x, [], contextFreeSyntax()) := s ? "<x>" :  value2id(unset(s, "alabel"));
     //println("sym2name: <s> ==\> <res>");
     return res;
}

@Memo
public str value2id(value v) {
  return v2i(v);
}

str uu(value s) = escape(toBase64("<node nd := s ? unsetRec(nd) : ((list[node] lnd := s) ? [unsetRec(nd) | node nd <- lnd] : s)>"),("=":"00","+":"11","/":"22"));

str srolePrefix(contextFreeSyntax()) = "";
str srolePrefix(lexicalSyntax()) = "lexical_";
str srolePrefix(layoutSyntax()) = "layouts_";
str srolePrefix(keywordSyntax()) = "";

str v2i(value v){
    res = xxv2i(v);
    //println("v2i(<v> ==\> <res>");
    return res;
}
default str xxv2i(value v) {
    switch (v) {
        case AType u : if(u.label? && !isEmpty(u.label)) return escId(u.label) + "_" + xxv2i(unset(u, "alabel"));else fail;
        case \start(AType s) : return "start__<xxv2i(s)>";
        case s:aadt(nm, list[AType] args, layoutSyntax()) :  
            return "layouts_<escId(nm)>";
        case s:aadt(nm, list[AType] args, syntaxRole) :  
            return srolePrefix(syntaxRole) + (isEmpty(args) ? "<nm>"  : "<nm>_<uu(args)>");
      
        case item(p:prod(AType u,_), int i) : return "<xxv2i(u)>.<xxv2i(p)>_<xxv2i(i)>";
   
        case conditional(AType s,_) : return xxv2i(s);
        case cilit(/<s:^[A-Za-z0-9\-\_]+$>/)  : return "cilit_<escId(s)>";
        case lit(/<s:^[A-Za-z0-9\-\_]+$>/) : return "lit_<escId(s)>"; 
        case int i         : return i < 0 ? "min_<-i>" : "<i>";
        case str s         : return ("" | it + "_<charAt(s,i)>" | i <- [0..size(s)]);
        
        default            : return uu(v);
    }
}

str parserName(str mname) = replaceAll(replaceAll(mname, "\\\\", "_"), "::", "_") + "Parser";

list[Message] saveParser(str pname, str parserClass, loc where, bool verbose){
    try {
        dest = where +"/<pname>.java";
        if(verbose) println("Write parser for <pname> to <dest>");
        writeFile(dest, parserClass);
        return [];
    } catch e: {
        return [error("<e>", |unknown:///|(0,0,<0,0>,<0,0>))];
    }
}

str prettyAsClassic(AProduction p)
    = "<atype2symbol(p)>";
