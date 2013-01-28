module ParseTreeAlgebra

import List;
import ParseTree;
import String;
import IO;


data ParseTreeAlg[&Tr, &Prod, &At, &Assoc, &Cr, &Sym, &Cond] 
		= parsetreealgebra(
			               &Tr (&Prod, list[&Tr]) applTree,
			               &Tr (&Sym, int) cycleTree,
			               &Tr (set[&Tr]) ambTree,
			               &Tr (int) charTree,
			               
			               &Prod (&Sym, list[&Sym], set[&At]) prodProduction,
			               &Prod (&Sym) regularProduction,
			               &Prod (&Prod, int) errorProduction,
			               &Prod () skippedProduction,
			               &Prod (&Sym, list[&Prod]) priorityProduction,
			               &Prod (&Sym, &Assoc, set[&Prod]) associativityProduction,
			               &Prod (&Sym) othersProduction,
			               &Prod (&Sym, str) referenceProduction,
			               
			               &At (&Assoc) assocAttr,
			               &At () bracketAttr,
			               &At (value) tagAttr,
			               
			               &Assoc () leftAssociativity,
			               &Assoc () rightAssociativity,
			               &Assoc () assocAssociativity,
			               &Assoc () \non-assocAssociativity,
			               
			               &Cr (int, int) rangeCharRange,
			               
			               &Sym (&Sym) startSymbol,
			               &Sym (str) sortSymbol,
			               &Sym (str) lexSymbol,
			               &Sym (str) layoutsSymbol,
			               &Sym (str) keywordsSymbol,
			               &Sym (str, list[&Sym]) \parameterized-sortSymbol,
			               &Sym (str, list[&Sym]) \parameterized-lexSymbol,
			               &Sym (str) parameterSymbol,
			               &Sym (str) litSymbol,
			               &Sym (str) cilitSymbol,
			               &Sym (list[&Cr]) \char-classSymbol,
			               &Sym () emptySymbol,
			               &Sym (&Sym) optSymbol,
			               &Sym (&Sym) iterSymbol,
			               &Sym (&Sym) \iter-starSymbol,
			               &Sym (&Sym, list[&Sym]) \iter-sepsSymbol,
			               &Sym (&Sym, list[&Sym]) \iter-star-sepsSymbol,
			               &Sym (set[&Sym]) altSymbol,
			               &Sym (list[&Sym]) seqSymbol,
			               &Sym (&Sym, set[&Cond]) conditionalSymbol,
			               
			               &Sym (str, &Sym) labelSymbol,
			               
			               &Cond (&Sym) followCondition,
			               &Cond (&Sym) \not-followCondition,
			               &Cond (&Sym) precedeCondition,
			               &Cond (&Sym) \not-precedeCondition,
			               &Cond (&Sym) deleteCondition,
			               &Cond (int) \at-columnCondition,
			               &Cond () \begin-of-lineCondition,
			               &Cond () \end-of-lineCondition,
			               &Cond (str) exceptCondition
			               ); 

public ParseTreeAlg[value, Production, Attr, Associativity, CharRange, Symbol, Condition] alg 
	= parsetreealgebra(
			value (Production prod, list[value] args) { 
				args = flatten(args);
				args = [ arg | value arg <- args, !( none() := arg ) ]; // eliminating skipped nodes, e.g., corresponding to literals, layouts, empty
				if(skipped() := prod) return none();
				
				if(isRegularLists(prod)) return args; // 2. Reqular */+ lists are imploded to lists (in contrast to the original spec)
				
				if(none() := getLabel(prod) && size(args) == 1) return args[0]; // 4. No labeled with a single argument ( getLabel(...) returns no label for bracket productions )
				
				if(isOptional(prod)) return flatten(args); // 6. Optional are imploded to a list (in constrast to the original spec)
														   // 8. Flattening arguments with no labels and single lists
														   
				if(isLexical(prod) && none() := getLabel(prod)) { // 10. Unlabeled lexicals ( no coercion in contrast to the original spec)
					args = flatten(args);
					if(list[int] l := args) return /*stringChars(l)*/[ stringChars([i]) | int i <- l ];
					throw "Problems with lexicals";
				}
				
				// if(isLexical(prod) && some(str lbl) := getLabel(prod) && lbl == "cons") // 11. Lexicals cons-labeled
				
				if(isRegular(prod)) return args;
				
				if(none() := getLabel(prod) && size(args) > 1) return makeTuple(args); // 5. No label but more than one argument
				
				// if(some(str label) := getLabel(prod) && lbl == "cons") // 9. Trees cons-labeled
				
				if(some(str lbl) := getLabel(prod)) return makeNode(lbl, args);
				
				return none(); 
			},
				
			value (Symbol symbol, int cycleLength) { return none(); },
			value (set[value] alts) { return alts; }, // 3. Ambiguitites are imploded to sets
			value (int character) { return character; },
			 			                   
			Production (Symbol def, list[Symbol] symbols, set[Attr] attributes) {
				// 1. Literals, layouts and empty nodes are skipped
				return (isLiteral(def) || isLayouts(def) || isEmpty(def)) ? skipped() : prod(def, symbols, attributes); }, 
			Production::regular, 
			Production::error, 
			Production::skipped,
			Production (Symbol def, list[Production] choices) { return \priority(def, choices); },
			Production (Symbol def, Associativity \assoc, set[Production] alternatives) { return \associativity(def, \assoc, alternatives); },
			Production::\others, Production::\reference,
			                    
			Attr::\assoc, Attr::\bracket, Attr::\tag,
			                    
			Associativity::\left, Associativity::\right, Associativity::\assoc, Associativity::\non-assoc,
			                    
			CharRange::range,
			                    
			Symbol::\start, Symbol::\sort, Symbol::\lex, Symbol::\layouts, Symbol::\keywords, Symbol::\parameterized-sort, Symbol::\parameterized-lex, 
			Symbol::\parameter, Symbol::\lit, Symbol::\cilit, Symbol::\char-class, Symbol::\empty, Symbol::\opt, Symbol::\iter, Symbol::\iter-star, Symbol::\iter-seps, 
			Symbol::\iter-star-seps, Symbol::\alt, Symbol::\seq, Symbol::\conditional, Symbol::\label,
			                    
			Condition::\follow, Condition::\not-follow, Condition::\precede, Condition::\not-precede, Condition::\delete, Condition::\at-column, Condition::\begin-of-line,
			Condition::\end-of-line, Condition::\except
			                    			
	);
	
public list[value] flatten(list[value] l) = [ *( (list[value] subl := elem) ? subl : [ elem ] ) | value elem <- l ];
public bool isRegularLists(prod(Symbol def, list[Symbol] _, set[Attr] _)) = isRegularLists(def);
public default bool isRegularLists(Production prod) = false;

public bool isOptional(prod(Symbol def, list[Symbol] _, set[Attr] _)) = isOptional(def);
public default bool isOptional(Production prod) = false;

public bool isLexical(prod(Symbol def, list[Symbol] _, set[Attr] _)) = isLexical(def);
public default bool isLexical(Production prod) = false;

public bool isRegular(regular(Symbol _)) = true;
public default bool isRegular(Production prod) = false;

public bool isRegularLists(Symbol sym) = ( (\iter(Symbol _) := sym)   
     										|| (\iter-star(Symbol _) := sym)   
     										|| (\iter-seps(Symbol _, list[Symbol] _) := sym)   
     										|| (\iter-star-seps(Symbol _, list[Symbol] _) := sym) ) ? true : false;
public bool isOptional(Symbol sym) = (\opt(Symbol _) := sym) ? true : false;
public bool isLexical(Symbol sym) = (\lex(str _) := sym) ? true : false;

public bool isLiteral(Symbol sym) = ( (\lit(str _) := sym) || (\cilit(str _) := sym) ) ? true : false;
public bool isLayouts(Symbol sym) = (\layouts(str _) := sym) ? true : false;
public bool isEmpty(Symbol sym) = (\empty() := sym) ? true : false;
 
			               
data Option[&T] = some(&T opt) | none();
			               
public Option[str] getLabel(prod(Symbol def, list[Symbol] _, set[Attr] attributes)) 
	= ( !(\bracket() in attributes) ) ? getLabel(def) : none();
public default Option[str] getLabel(Production prod) = none();

public Option[str] getLabel(\label(str name, Symbol _)) = some(name);
public default Option[str] getLabel(Symbol _) = none();

public node makeNode(str name, list[value] args) = name(args);
public value makeTuple(list[value] args) = "tuple"(args);
