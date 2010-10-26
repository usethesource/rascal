module rascal::syntax::Generator

import rascal::syntax::Grammar;
import rascal::syntax::Parameters;
import rascal::syntax::Regular;
import rascal::syntax::Normalization;
import rascal::syntax::Lookahead;
import rascal::syntax::Actions;
import rascal::syntax::Assimilator;
import ParseTree;
import String;
import List;
import Node;
import Set;
import IO;
import Exception;

private data Item = item(Production production, int index);

// TODO: replace this complex data structure with several simple ones
private alias Items = map[Symbol,map[Item item, tuple[str new, int itemId] new]];
public anno str Symbol@prefix;

@doc{Used in bootstrapping only, to generate a parser for Rascal modules without concrete syntax.}
public str generateRootParser(str package, str name, Grammar gr) {
  // we annotate the grammar to generate identifiers that are different from object grammar identifiers
  gr = visit (gr) { case s:sort(_) => meta(s) case s:layouts(_) => meta(s) }; 
  int uniqueItem = -3; // -1 and -2 are reserved by the SGLL implementation
  int newItem() { uniqueItem -= 1; return uniqueItem; };
  // make sure the ` sign is expected for expressions and every non-terminal which' first set is governed by Pattern or Expression, even though ` not in the language yet
  rel[Symbol,Symbol] quotes = { <x, \char-class([range(40,40),range(96,96)])> | x <- [meta(sort("Expression")),meta(sort("Pattern")),meta(sort("Command")),meta(sort("Statement")),meta(layouts("LAYOUTLIST"))]}; 
  return generate(package, name, "org.rascalmpl.parser.sgll.SGLL", newItem, false, true, quotes, gr);
}

@doc{Used to generate parser that parse object language only}
public str generateObjectParser(str package, str name, Grammar gr) {
  int uniqueItem = 2;
  int newItem() { uniqueItem += 2; return uniqueItem; };
  // make sure the < is expected for every non-terminal
  rel[Symbol,Symbol] quotes = {<x,\char-class([range(60,60)])> | Symbol x:sort(_) <- gr.rules} // any sort could start with <
                            + {<x,\char-class([range(60,60)])> | Symbol x:layouts(_) <- gr.rules}
                            + {<layouts("$QUOTES"),\char-class([range(0,65535)])>} // always expect quoting layout (because the actual content is unknown at generation time)
                            ; 
  // prepare definitions for quoting layout
  gr = compose(gr, grammar({}, layoutProductions(gr)));
  return generate(package, name, "org.rascalmpl.library.rascal.syntax.RascalRascal", newItem, false, false, quotes, gr);
}

@doc{
  Used to generate subclasses of object grammars that can be used to parse Rascal modules
  with embedded concrete syntax fragments.
}   
public str generateMetaParser(str package, str name, str super, Grammar gr) {
  int uniqueItem = 1; // we use the odd numbers here
  int newItem() { uniqueItem += 2; return uniqueItem; };
  
  fr = grammar({}, fromRascal(gr));
  tr = grammar({}, toRascal(gr));
  q = grammar({}, quotes()); // TODO parametrize quotes to use quote definitions
  l = grammar({}, layoutProductions(gr));
  
  full = compose(fr, compose(tr, compose(q, l)));
  
  return generate(package, name, super, newItem, true, false, {}, full);
}

public str generate(str package, str name, str super, int () newItem, bool callSuper, bool isRoot, rel[Symbol,Symbol] extraLookaheads, Grammar gr) {
    // TODO: it would be better if this was not necessary, i.e. by changing the grammar 
    // representation to grammar(set[Symbol] start, map[Symbol,Production] rules)
    println("merging composed non-terminals");
    for (Symbol nt <- gr.rules) {
      gr.rules[nt] = {choice(nt, gr.rules[nt])};
    }
    
    println("expanding parameterized symbols");
    gr = expandParameterizedSymbols(gr);
    
    println("extracting actions");
    <gr, actions> = extractActions(gr);
   
    println("generating stubs for regular");
    gr = makeRegularStubs(gr);
   
    println("establishing production set");
    uniqueProductions = {p | /Production p := gr, prod(_,_,_) := p || regular(_,_) := p, restricted(_) !:= p.rhs};
 
    println("generating item allocations");
    newItems = generateNewItems(gr, newItem);
   
    println("computing priority and associativity filter");
    rel[int parent, int child] dontNest = computeDontNests(newItems, gr);
    // this creates groups of children that forbidden below certain parents
    rel[set[int] parents, set[int] children] dontNestGroups = 
      {<c,g[c]> | rel[set[int] children, int parent] g := {<dontNest[p],p> | p <- dontNest.parent}, c <- g.children};
   
    println("computing lookahead sets");
    gr = computeLookaheads(gr, extraLookaheads);
    
    println("optimizing lookahead automaton");
    gr = compileLookaheads(gr);
   
    println("printing the source code of the parser class");
    
    return 
"
package <package>;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.sgll.stack.*;
import org.rascalmpl.parser.sgll.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.sgll.util.IntegerList;
import org.rascalmpl.parser.sgll.util.IntegerMap;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.ast.ASTFactory;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.parser.IParserInfo;

public class <name> extends <super> implements IParserInfo {
    <if (isRoot) {>
	protected static IValue _read(java.lang.String s, org.eclipse.imp.pdb.facts.type.Type type){
		try{
			return new StandardTextReader().read(vf, org.rascalmpl.values.uptr.Factory.uptr, type, new ByteArrayInputStream(s.getBytes()));
		}catch(FactTypeUseException e){
			throw new RuntimeException(\"unexpected exception in generated parser\", e);  
		}catch(IOException e){
			throw new RuntimeException(\"unexpected exception in generated parser\", e);  
		}
	}
	
	protected static final TypeFactory _tf = TypeFactory.getInstance();
	
	protected static java.lang.String _concat(String ...args) {
	  java.lang.StringBuilder b = new java.lang.StringBuilder();
	  for (java.lang.String s : args) {
	    b.append(s);
	  }
	  return b.toString();
	}
	
   
    <}>

    private static final IntegerMap _resultStoreIdMappings;
    private static final IntegerKeyedHashMap\<IntegerList\> _dontNest;
	private static final java.util.HashMap\<IConstructor, org.rascalmpl.ast.LanguageAction\> _languageActions;
	
	private static void _putDontNest(IntegerKeyedHashMap\<IntegerList\> result, int i, int j) {
    	IntegerList donts = result.get(i);
    	if(donts == null){
    		donts = new IntegerList();
    		result.put(i, donts);
    	}
    	donts.add(j);
    }
    
    protected static void _putResultStoreIdMapping(IntegerMap result, int parentId, int resultStoreId){
       result.putUnsafe(parentId, resultStoreId);
    }
    
    protected int getResultStoreId(int parentId){
       return _resultStoreIdMappings.get(parentId);
    }
    
    protected static IntegerKeyedHashMap\<IntegerList\> _initDontNest() {
      IntegerKeyedHashMap\<IntegerList\> result = <if (!isRoot) {><super>._initDontNest()<} else {>new IntegerKeyedHashMap\<IntegerList\>()<}>; 
    
      for (IValue e : (IRelation) _read(_concat(<split("<dontNest>")>), _tf.relType(_tf.integerType(),_tf.integerType()))) {
        ITuple t = (ITuple) e;
        _putDontNest(result, ((IInteger) t.get(0)).intValue(), ((IInteger) t.get(1)).intValue());
      }
      
      return result;
    }
    
    protected static IntegerMap _initDontNestGroups() {
      IntegerMap result = <if (!isRoot) {><super>._initDontNestGroups()<} else {>new IntegerMap()<}>;
      int resultStoreId = result.size();
    
      for (IValue t : (IRelation) _read(_concat(<split("<dontNestGroups>")>), _tf.relType(_tf.setType(_tf.integerType()),_tf.setType(_tf.integerType())))) {
        ++resultStoreId;

        ISet parentIds = (ISet) ((ITuple) t).get(1);
        for (IValue pid : parentIds) {
          _putResultStoreIdMapping(result, ((IInteger) pid).intValue(), resultStoreId);
        }
      }
      
      return result;
    }
    
    protected IntegerList getFilteredChildren(int parentId) {
		return _dontNest.get(parentId);
	}
    
    public org.rascalmpl.ast.LanguageAction getAction(IConstructor prod) {
      return _languageActions.get(prod);
    }
    
    protected static java.util.HashMap\<IConstructor, org.rascalmpl.ast.LanguageAction\> _initLanguageActions() {
      java.util.HashMap\<IConstructor, org.rascalmpl.ast.LanguageAction\> result = <if (!isRoot) {><super>._initLanguageActions()<} else {>new java.util.HashMap\<IConstructor, org.rascalmpl.ast.LanguageAction\>()<}>;
      ASTBuilder astBuilder = new ASTBuilder(new ASTFactory());
      IMap tmp = (IMap) _read(_concat(<split("<actions>")>), _tf.mapType(Factory.Production, Factory.Tree));
      for (IValue key : tmp) {
        result.put((IConstructor) key, (org.rascalmpl.ast.LanguageAction) astBuilder.buildValue(tmp.get(key)));
      }
      
      return result;
    }
    
    // initialize priorities and actions    
    static {
      _languageActions = _initLanguageActions();
      _dontNest = _initDontNest();
      _resultStoreIdMappings = _initDontNestGroups();
    }
    
    // Production declarations
	<for (p <- uniqueProductions) {>
	private static final IConstructor <value2id(p)> = (IConstructor) _read(\"<esc("<unmeta(p)>")>\", Factory.Production);<}>
    
	// Item declarations
	<for (Symbol s <- newItems, isNonterminal(s)) { items = newItems[s]; >
	private static class <value2id(s)> {
		<for (Item i <- items) {>
		public static final AbstractStackNode <value2id(i.production)>_<value2id(i.index)> = <items[i].new>;<}>
	}<}>
	
	public <name>(){
		super();
	}
	
	// Parse methods    
	<for (Symbol nont <- gr.rules, isNonterminal(nont)) { >
      <generateParseMethod(newItems, callSuper, choice(nont, gr.rules[nont]))>
	<}>
}
";
}  

public &T <: value unmeta(&T <: value p) {
  return visit(p) {
    case meta(s) => s
  }
}

@doc{This function generates Java code to allocate a new item for each position in the grammar.
We first collect these in a map, such that we can generate static fields. It's a simple matter of caching
constants to improve run-time efficiency of the generated parser}
private map[Symbol,map[Item,tuple[str new, int itemId]]] generateNewItems(Grammar g, int () newItem) {
  map[Symbol,map[Item,tuple[str new, int itemId]]] items = ();
  map[Item,tuple[str new, int itemId]] fresh = ();
  
  visit (g) {
    case Production p:prod([],Symbol s,_) : {
       int counter = newItem();
       items[s]?fresh += (item(p, -1):<"new EpsilonStackNode(<counter>, 0)", counter>);
    }
    case Production p:prod(list[Symbol] lhs, Symbol s,_) : 
      for (int i <- index(lhs)) 
        items[s]?fresh += (item(p, i): sym2newitem(g, lhs[i], newItem, i));
    case Production p:regular(Symbol s, _) :
      switch(s) {
        case \iter(Symbol elem) : 
          items[s]?fresh += (item(p,0):sym2newitem(g, elem, newItem, 0));
        case \iter-star(Symbol elem) : 
          items[s]?fresh += (item(p,0):sym2newitem(g, elem, newItem, 0));
        case \iter-seps(Symbol elem, list[Symbol] seps) : {
          items[s]?fresh += (item(p,0):sym2newitem(g, elem, newItem, 0));
          for (int i <- index(seps)) 
            items[s]?fresh += (item(p,i+1):sym2newitem(g, seps[i], newItem, i+1));
        }
        case \iter-star-seps(Symbol elem, list[Symbol] seps) : {
          items[s]?fresh += (item(p,0):sym2newitem(g, elem, newItem, 0));
          for (int i <- index(seps)) 
            items[s]?fresh += (item(p,i+1):sym2newitem(g, seps[i], newItem, i+1));
        } 
     }
  }
  return items;
}

private str split(str x) {
  if (size(x) <= 20000) {
    return "\"<esc(x)>\"";
  }
  else {
    return "<split(substring(x, 0,10000))>, <split(substring(x, 10000))>"; 
  }
}

@doc{this function selects all symbols for which a parse method should be generated}
private bool isNonterminal(Symbol s) {
  switch (s) {
    case \sort(_) : return true;
    case \meta(x) : return isNonterminal(x);
    case \parameterized-sort(_,_) : return true;
    case \start(_) : return true;
    case \layouts(_) : return true;
    default: return false;
  }
}

public str generateParseMethod(Items items, bool callSuper, Production p) {
  return "public void <sym2name(p.rhs)>() {
            <if (callSuper) {>super.<sym2name(p.rhs)>();<}>
            <generateExpect(items, p, false)>
          }";
}

public str generateExpect(Items items, Production p, bool reject){
    // note that this code heavily leans on the fact that production combinators are normalized 
    // (distribution and factoring laws have been applied to put a production expression in canonical form)
    
    switch (p) {
      case prod(_,_,_) : 
        if (restricted(_) !:= p.rhs) 
	       return "// <p>\n\texpect<reject ? "Reject" : "">(<value2id(p)>, <generateSymbolItemExpects(p)>);";
	    else 
	       return ""; 
      case lookahead(_, classes, Production q) :
        return "if (<generateClassConditional(classes)>) {
                  <generateExpect(items, q, reject)>
               }";
      case choice(_, {l:lookahead(_, _, q)}) :
        return generateExpect(items, l, reject);
      case choice(_, {lookahead(_, classes, Production q), set[Production] rest}) :
        return "if (<generateClassConditional(classes)>) {
                  <generateExpect(items, q, reject)>
                } else {
                  <generateExpect(items, choice(q.rhs, rest), reject)>
                }";
      case choice(_, set[Production] ps) :
        return "<for (Production q <- ps){><generateExpect(items, q, reject)>
                <}>";
      case restrict(_, Production q, set[Production] restrictions) : 
        return generateExpect(items, q, reject);
      case diff(_, Production n, set[Production] rejects) :
        return "<for (Production q <- rejects){><generateExpect(items, q, true)>
                <}>
                <generateExpect(items, n, false)>";
      case first(_, list[Production] ps) : 
        return generateExpect(items, choice(p.rhs, { q | q <- ps }), reject);
      case \assoc(_,_,set[Production] ps) :
        return generateExpect(items, choice(p.rhs, ps), reject); 
    }
    
    throw "not implemented <p>";
}

str generateClassConditional(set[Symbol] classes) {
  if (eoi() in classes) {
    return ("lookAheadChar == 0" 
           | it + " || <generateRangeConditional(r)>"
           | \char-class(list[CharRange] ranges) <- classes, r <- ranges);
  }
  else {
    ranges = [r | \char-class(ranges) <- classes, r <- ranges];
    
    return ("<generateRangeConditional(head(ranges))>"| it + " || <generateRangeConditional(r)> "
           | r <- tail(ranges));
  } 
}

str generateRangeConditional(CharRange r) {
  switch (r) {
    case single(i) : return "(lookAheadChar == <i>)";
    case range(0,65535) : return "(true /*every char*/)";
    case range(i, i) : return "(lookAheadChar == <i>)";
    case range(i, j) : return "((lookAheadChar \>= <i>) && (lookAheadChar \<= <j>))";
    default: throw "unexpected range type: <r>";
  }
}

rel[int,int] computeDontNests(Items items, Grammar grammar) {
  // first we compute a map from productions to their last items (which identify each production)
  prodItems = ( p:items[rhs][item(p,size(lhs)-1)].itemId | /Production p:prod(list[Symbol] lhs,Symbol rhs, _) := grammar);
   
  return {computeDontNests(items, prodItems, p) | Symbol s <- grammar.rules, Production p <- grammar.rules[s]};
}

rel[int,int] computeDontNests(Items items, map[Production, int] prodItems, Production p) {
  switch (p) {
    case prod(_,_,attrs([_*,\assoc(Associativity a),_*])) : 
      return computeAssociativities(items, prodItems, a, {p});
    case prod(_,_,_) : return {};
    case regular(_,_) : return {};
    case choice(_, set[Production] alts) : 
      return { computeDontNests(items, prodItems, a) | a <- alts };
    case diff(_,Production a,_) :
      return computeDontNests(items, prodItems, a);
    case restrict(_,Production a,_) :
      return computeDontNests(items, prodItems,a);
    case first(_, list[Production] levels) : {
      return computePriorities(items, prodItems, levels);
    }
    case \assoc(_, Associativity a, set[Production] alts) : {
      return computeAssociativities(items, prodItems, a, alts);
    }
    case \lookahead(_,_,q) :
      return computeDontNests(items,prodItems,q); 
    case \others(_) : return {};
    default:
      throw "missed a case <p>";
  }
}

rel[int,int] computeAssociativities(Items items, map[Production, int] prodItems, Associativity a, set[Production] alts) {
  result = {};
  // assoc is not transitive, but it is reflexive
  
  switch (a) {
    case \left(): {
      for (Production p1 <- alts, Production p2:prod(lhs:[_*,Symbol r],Symbol rhs,_) <- alts, symbolMatch(r,rhs)) {
        result += {<items[rhs][item(p2,size(lhs)-1)].itemId,prodItems[p1]>};
      }  
    }
    case \assoc():
      for (Production p1 <- alts, Production p2:prod(lhs:[_*,Symbol r],Symbol rhs,_) <- alts, symbolMatch(r,rhs)) {
        result += {<items[rhs][item(p2,size(lhs)-1)].itemId,prodItems[p1]>};
      }
    case \right():
      for (Production p1 <- alts, Production p2:prod(lhs:[Symbol l,_*],Symbol rhs,_) <- alts, symbolMatch(l,rhs)) {
        result += {<items[rhs][item(p2,0)].itemId,prodItems[p1]>};
      }
    case \non-assoc(): {
      for (Production p1 <- alts, Production p2:prod(lhs:[_*,Symbol r],Symbol rhs,_) <- alts, symbolMatch(r,rhs)) {
        result += {<items[rhs][item(p2,size(lhs)-1)].itemId,prodItems[p1]>};
      }
      for (Production p1 <- alts, Production p2:prod(lhs:[Symbol l,_*],Symbol rhs,_) <- alts, symbolMatch(l,rhs)) {
        result += {<items[rhs][item(p2,0)].itemId,prodItems[p1]>};
      }
    }
  }
  
  return result;
}

rel[int,int] computePriorities(Items items, map[Production, int] prodItems, list[Production] levels) {
  // collect basic filter
  ordering = { <p1,p2> | [pre*,Production p1, Production p2, post*] := levels };

  // flatten nested structure to obtain direct relations
  todo = ordering;
  ordering = {};
  while (todo != {}) {
    <prio,todo> = takeOneFrom(todo);
    switch (prio) {
      case <choice(_,set[Production] alts),Production p2> :
        todo += alts * {p2};
      case <Production p1, choice(_,set[Production] alts)> :
        todo += {p1} * alts;
      case <\assoc(_,_,set[Production] alts),Production p2> :
        todo += alts * {p2};
      case <Production p1, \assoc(_,_,set[Production] alts)> :
        todo += {p1} * alts;
      default:
        ordering += prio;
    }
  }
  
  ordering = ordering+; // priority is transitive

  result = {};
  for (<Production p1, Production p2> <- ordering) {
    switch (p1) {
      case prod(lhs:[Symbol l,_*,Symbol r],Symbol rhs,_) :
        if (symbolMatch(l,rhs) && symbolMatch(l,rhs)) {
          result += {<items[rhs][item(p1,0)].itemId,prodItems[p2]>,<items[rhs][item(p1,size(lhs) - 1)].itemId,prodItems[p2]>};   
        }
        else fail;
      case prod(lhs:[Symbol l,_*],Symbol rhs,_) :
        if (symbolMatch(l,rhs)) {
          result += {<items[rhs][item(p1,0)].itemId,prodItems[p2]>};   
        }
        else fail;
      case prod(lhs:[_*,Symbol r],Symbol rhs,_) :
        if (symbolMatch(r,rhs)) {
          result += {<items[rhs][item(p1,size(lhs) - 1)].itemId,prodItems[p2]>};   
        }
        else fail;
    }
  }
  
  // and we recurse to find the nested associativity declarations
  return result + { computeDontNests(items, prodItems, l) | l <- levels };
}                     

private bool symbolMatch(Symbol checked, Symbol referenced) {
  return referenced == checked || label(_, referenced) := checked;
}

@doc{
  generate stack nodes for the restrictions. Note that although the abstract grammar for restrictions
  may seem pretty general (i.e. any symbol can be a restriction), we actually only allow finite languages
  defined as either sequences of character classes or literals.
}
public str generateRestrictions(Grammar grammar, int() id, set[Production] restrictions) {
  result = "new IMatchableStackNode[] {"; 
  
  // not that only single symbol restrictions are allowed at the moment.
  // the run-time only supports character-classes and literals BTW, which should
  // be validated by a static checker for Rascal.  
  las = [ l | /Production p:prod([Symbol l],_,_) <- restrictions];

  if (las != []) {
    result += ("<sym2newitem(grammar, head(las), id, 0).new>" | it + ", <sym2newitem(grammar, l, id, 0).new>" | l <- tail(las));
  }
  else if (size(las) != size(restrictions)) {
    println("WARNING: some restrictions could not be implemented because they are not single token lookaheads: <restrictions>");
  }
  
  result += "}";
  return result;
}

@doc{
  generate stack nodes for the restrictions. Note that although the abstract grammar for restrictions
  may seem pretty general (i.e. any symbol can be a restriction), we actually only allow finite languages
  defined as either sequences of character classes or literals.
}
public str generateRestrictions(set[Production] restrictions) {
  result = "new IMatchableStackNode[] {"; 
  
  // not that only single symbol restrictions are allowed at the moment.
  // the run-time only supports character-classes and literals BTW, which should
  // be validated by a static checker for Rascal.  
  las = [ p | /Production p:prod([Symbol l],_,_) <- restrictions];
 
  if (las != []) {
    result += ("<value2id(item(head(las),0))>" | it + ", <value2id(item(l,0))>" | l <- tail(las));
  }
  else if (size(las) != size(restrictions)) {
    println("WARNING: some restrictions could not be implemented because they are not single token lookaheads: <restrictions>");
  }
  
  result += "}";
  return result;
}

public str generateSymbolItemExpects(Production prod){
    if(prod.lhs == []){
        return value2id(item(prod, -1));
    }
    
    return ("<value2id(item(prod, 0))>" | it + ",\n\t\t" + value2id(item(prod, i+1)) | int i <- index(tail(prod.lhs)));
}

public str generateSeparatorExpects(Grammar grammar, int() id, list[Symbol] seps) {
   if (seps == []) {
     return "";
   }
   
   return (sym2newitem(grammar, head(seps), id, 1).new | it + ", <sym2newitem(grammar, seps[i+1], id, i+2).new>" | int i <- index(tail(seps)));
}

public str literals2ints(list[Symbol] chars){
    if(chars == []) return "";
    
    str result = "<head(head(chars).ranges).start>";
    
    for(ch <- tail(chars)){
        result += ",<head(ch.ranges).start>";
    }
    
    return result;
}

// TODO
public str ciliterals2ints(list[Symbol] chars){
    throw "case insensitive literals not yet implemented by parser generator";
}

public tuple[str new, int itemId] sym2newitem(Grammar grammar, Symbol sym, int() id, int dot){
    itemId = id();
    
    switch ((meta(_) := sym) ? sym.wrapped : sym) {
        case \label(_,s) : 
            return sym2newitem(grammar, s, id, dot); // ignore labels
        case \sort(n) : 
            return <"new NonTerminalStackNode(<itemId>, <dot> <generateRestrictions(grammar, sym, id)>, \"<sym2name(sym)>\")", itemId>;
        case \layouts(_) :
            return <"new NonTerminalStackNode(<itemId>, <dot> <generateRestrictions(grammar, sym, id)>, \"<sym2name(sym)>\")", itemId>;
        case \parameterized-sort(n,args): 
            return <"new NonTerminalStackNode(<itemId>, <dot> <generateRestrictions(grammar, sym, id)>, \"<sym2name(sym)>\")", itemId>;
        case \parameter(n) :
            throw "all parameters should have been instantiated by now";
        case \start(s) : 
            return <"new NonTerminalStackNode(<itemId>, <dot> <generateRestrictions(grammar, sym, id)>, \"<sym2name(sym)>\")", itemId>;
        case \lit(l) : 
            if (/p:prod(list[Symbol] chars,sym,attrs([term("literal"())])) := grammar.rules[sym])
                return <"new LiteralStackNode(<itemId>, <dot>, <value2id(p)> <generateRestrictions(grammar, sym, id)>, new char[] {<literals2ints(chars)>})",itemId>;
            else throw "literal not found in grammar: <grammar>";
        case \cilit(l) : 
            if (/p:prod(list[Symbol] chars,sym,attrs([term("literal"())])) := grammar.rules[sym])
                return <"new CaseInsensitiveLiteralStackNode(<itemId>, <dot>, <value2id(p)> <generateRestrictions(grammar, sym, id)>, new char[] {<literals2ints(chars)>})",itemId>;
            else throw "ci-literal not found in grammar: <grammar>";
        case \iter(s) : 
            return <"new ListStackNode(<itemId>, <dot>, <value2id(regular(sym,\no-attrs()))> <generateRestrictions(grammar, sym, id)>, <sym2newitem(grammar, s, id, 0).new>, true)",itemId>;
        case \iter-star(s) :
            return <"new ListStackNode(<itemId>, <dot>, <value2id(regular(sym,\no-attrs()))> <generateRestrictions(grammar, sym, id)>, <sym2newitem(grammar, s, id, 0).new>, false)", itemId>;
        case \iter-seps(Symbol s,list[Symbol] seps) : {
            reg = regular(sym,\no-attrs());
            return <"new SeparatedListStackNode(<itemId>, <dot>, <value2id(reg)> <generateRestrictions(grammar, sym, id)>, <sym2newitem(grammar, s, id, 0).new>, new AbstractStackNode[]{<generateSeparatorExpects(grammar,id,seps)>}, true)",itemId>;
        }
        case \iter-star-seps(Symbol s,list[Symbol] seps) : {
            reg = regular(sym,\no-attrs());
            return <"new SeparatedListStackNode(<itemId>, <dot>, <value2id(reg)> <generateRestrictions(grammar, sym, id)>, <sym2newitem(grammar, s, id, 0).new>, new AbstractStackNode[]{<generateSeparatorExpects(grammar,id,seps)>}, false)",itemId>;
        }
        case \opt(s) : {
            reg =  regular(sym,\no-attrs());
            return <"new OptionalStackNode(<itemId>, <dot>, <value2id(reg)> <generateRestrictions(grammar, sym, id)>, <sym2newitem(grammar, s, id, 0).new>)", itemId>;
        }
        case \char-class(list[CharRange] ranges) : 
            return <"new CharStackNode(<itemId>, <dot>, new char[][]{<generateCharClassArrays(ranges)>})", itemId>;
        case \start-of-line() : 
            return <"new StartOfLineStackNode(<itemId>, <dot>)", itemId>;
        case \end-of-line() :
            return <"new EndOfLineStackNode(<itemId>, <dot>)", itemId>;
        case \at-column(int column) :
            return <"new AtColumnStackNode(<itemId>, <dot>, <column>)",itemId>; 
        default: 
            throw "not yet implemented <sym>";
    }
}

public str generateRestrictions(Grammar grammar, Symbol sym, int() id) {
  try {
    if (/restrict(sym, _, set[Production] restrictions) := grammar.rules[sym]) {
      return ", <generateRestrictions(grammar, id, restrictions)>";
    }
    else {
       return "";
    }
  } catch NoSuchKey(_) : return "";
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

private map[str,str] javaStringEscapes = ( "\n":"\\n", "\"":"\\\"", "\t":"\\t", "\r":"\\r","\\u":"\\\\u","\\":"\\\\");

public str esc(str s){
    return escape(s, javaStringEscapes);
}

private map[str,str] javaIdEscapes = javaStringEscapes + ("-":"_");

public str escId(str s){
    return escape(s, javaIdEscapes);
}

public str sym2name(Symbol s){
    switch(s){
        case sort(x) : return "<x>";
        case meta(x) : return "$<sym2name(x)>";
        default      : return value2id(s);
    }
}

public str value2id(value v) {
  return v2i(v);
}

str v2i(value v) {
    switch (v) {
        case item(p:prod(_,Symbol u,_), int i) : return "<v2i(u)>.<v2i(p)>_<v2i(i)>";
        case label(str x,Symbol u) : return escId(x) + "_" + v2i(u);
        case layouts(str x) : return "layouts_<escId(x)>";
        case "cons"(str x) : return "cons_<escId(x)>";
        case sort(str s)   : return "<s>";
        case meta(Symbol s) : return "$<v2i(s)>";
        case \parameterized-sort(str s, list[Symbol] args) : return ("<s>_" | it + "_<v2i(arg)>" | arg <- args);
        case cilit(/<s:^[A-Za-z0-9\-\_]+$>/)  : return "cilit_<escId(s)>";
	    case lit(/<s:^[A-Za-z0-9\-\_]+$>/) : return "lit_<escId(s)>"; 
        case int i         : return i < 0 ? "min_<-i>" : "<i>";
        case str s         : return ("" | it + "_<charAt(s,i)>" | i <- [0..size(s)-1]);
        case str s()       : return escId(s);
        case node n        : return "<escId(getName(n))>_<("" | it + "_" + v2i(c) | c <- getChildren(n))>";
        case list[value] l : return ("" | it + "_" + v2i(e) | e <- l);
        default            : throw "value not supported <v>";
    }
}