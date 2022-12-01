
@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Anastasia Izmaylova - Anastasia.Izmaylova@cwi.nl (CWI)}
@bootstrapParser
module lang::rascalcore::check::ATypeUtils

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeExceptions;
extend lang::rascalcore::check::BasicRascalConfig;

extend ParseTree;

import analysis::typepal::Messenger;
import lang::rascal::\syntax::Rascal;

import IO;
import List;
import Node;
import Set;
import String;

str unescape(str s) = replaceAll(s, "\\", "");

AType removeLabels(AType t) = unsetRec(t, "alabel");

// ---- print atypes ----------------------------------------------------------

@doc{Pretty printer for Rascal abstract types.}
str prettyAType(aint()) = "int";
str prettyAType(abool()) = "bool";
str prettyAType(areal()) = "real";
str prettyAType(arat()) = "rat";
str prettyAType(astr()) = "str";
str prettyAType(anum()) = "num";
str prettyAType(anode(list[AType fieldType] fields)) = isEmpty(fields) ? "node" : "node(<intercalate(", ", ["<prettyAType(ft)> <ft.alabel> = ..." | ft <- fields])>)";
str prettyAType(avoid()) = "void";
str prettyAType(avalue()) = "value";
str prettyAType(aloc()) = "loc";
str prettyAType(adatetime()) = "datetime";
str prettyAType(alist(AType t)) = "list[<prettyAType(t)>]";
str prettyAType(aset(AType t)) = "set[<prettyAType(t)>]";
str prettyAType(atuple(AType ts)) = "tuple[<prettyAType(ts)>]";
str prettyAType(amap(AType d, AType r)) = "map[<prettyAType(d)>, <prettyAType(r)>]";
str prettyAType(arel(AType ts)) = "rel[<prettyAType(ts)>]";
str prettyAType(alrel(AType ts)) = "lrel[<prettyAType(ts)>]";

str prettyAType(afunc(AType ret, list[AType] formals, lrel[AType fieldType, Expression defaultExp] kwFormals))
                = "<prettyAType(ret)>(<intercalate(",", [prettyAType(f) | f <- formals])><isEmpty(kwFormals) ? "" : ", "><intercalate(",", ["<prettyAType(ft)> <ft.alabel>=..." | <ft, _> <- kwFormals])>)";

str prettyAType(aalias(str aname, [], AType aliased)) = "alias <aname> = <prettyAType(aliased)>";
str prettyAType(aalias(str aname, ps, AType aliased)) = "alias <aname>[<prettyAType(ps)>] = <prettyAType(aliased)>" when size(ps) > 0;

str prettyAType(aanno(str aname, AType onType, AType annoType)) = "anno <prettyAType(annoType)> <prettyAType(onType)>@<aname>";

str prettyAType(aadt(str s, [], SyntaxRole _)) = s;
str prettyAType(aadt(str s, ps, SyntaxRole _)) = "<s>[<prettyAType(ps)>]" when size(ps) > 0;

str prettyAType(t: acons(AType adt, /*str consName,*/ 
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "<prettyAType(adt)>::<t.alabel>(<intercalate(", ", ["<prettyAType(ft)><ft.alabel? ? " <ft.alabel>" : "">" | ft <- fields])><isEmpty(kwFields) ? "" : ", "><intercalate(",", ["<prettyAType(ft)> <ft.alabel>=..." | <ft, _> <- kwFields])>)";

str prettyAType(amodule(str mname)) = "module <mname>";         
str prettyAType(aparameter(str pn, AType t)) = t == avalue() ? "&<pn>" : "&<pn> \<: <prettyAType(t)>";
str prettyAType(areified(AType t)) = "type[<prettyAType(t)>]";

// utilities
str prettyAType(overloadedAType(rel[loc, IdRole, AType] overloads))
                = intercalateOr([prettyAType(t1) | t1 <- {t | <_, _, t> <- overloads} ]);

str prettyAType(list[AType] atypes) = intercalate(", ", [prettyAType(t) | t <- atypes]);

str prettyAType(Keyword kw) = "<prettyAType(kw.fieldType) <kw.fieldType.alabel/*fieldName*/> = <kw.defaultExp>";

// non-terminal symbols
str prettyAType(\prod(AType s, list[AType] fs/*, SyntaxRole _*/)) = "<prettyAType(s)> : (<intercalate(", ", [ prettyAType(f) | f <- fs ])>)"; //TODO others

// terminal symbols
str prettyAType(AType::alit(str string)) = string;
str prettyAType(AType::acilit(str string)) = string;
str prettyAType(cc: \achar-class(list[ACharRange] ranges)) { 
    return cc == anyCharType ? "![]" : "[<intercalate(" ", [ "<stringChar(r.begin)>-<stringChar(r.end)>" | r <- ranges ])>]";
}

str prettyAType(\start(AType symbol)) = "start[<prettyAType(symbol)>]";

// regular symbols
str prettyAType(AType::\aempty()) = "()";
str prettyAType(\opt(AType symbol)) = "<prettyAType(symbol)>?";
str prettyAType(\iter(AType symbol)) = "<prettyAType(symbol)>+";
str prettyAType(\iter-star(AType symbol)) = "<prettyAType(symbol)>*";
str prettyAType(\iter-seps(AType symbol, list[AType] separators)) = "{<prettyAType(symbol)> <intercalate(" ", [ prettyAType(sep) | sep <- separators ])>}+";
str prettyAType(\iter-star-seps(AType symbol, list[AType] separators)) = "{<prettyAType(symbol)> <intercalate(" ", [ prettyAType(sep) | sep <- separators ])>}*";
str prettyAType(\alt(set[AType] alternatives)) = "( <intercalate(" | ", [ prettyAType(a) | a <- alternatives ])> )" when size(alternatives) > 1;
str prettyAType(\seq(list[AType] sequence)) = "( <intercalate(" ", [ prettyAType(a) | a <- sequence ])> )" when size(sequence) > 1;
str prettyAType(\conditional(AType symbol, set[ACondition] conditions)) = "<prettyAType(symbol)> { <intercalate(" ", [ prettyPrintCond(cond) | cond <- conditions ])> }";
default str prettyAType(AType s) = "<s>"; //"<type(s,())>";

private str prettyPrintCond(ACondition::\follow(AType symbol)) = "\>\> <prettyAType(symbol)>";
private str prettyPrintCond(ACondition::\not-follow(AType symbol)) = "!\>\> <prettyAType(symbol)>";
private str prettyPrintCond(ACondition::\precede(AType symbol)) = "<prettyAType(symbol)> \<\<";
private str prettyPrintCond(ACondition::\not-precede(AType symbol)) = "<prettyAType(symbol)> !\<\<";
private str prettyPrintCond(ACondition::\delete(AType symbol)) = "???";
private str prettyPrintCond("a-at-column"(int column)) = "@<column>";
private str prettyPrintCond("a-begin-of-line"()) = "^";
private str prettyPrintCond("a-end-of-line"()) = "$";
private str prettyPrintCond("a-except"(str label)) = "!<label>";

@doc{Rascal abstract types to classic Symbols}
Symbol atype2symbol(AType tp){
    //println("atype2symbol: <tp>");
    res = atype2symbol1(tp);
    a = tp.alabel?;
    return a ? Symbol::\label(tp.alabel, res) : res;
}

Symbol atype2symbol1(aint()) = Symbol::\int();
Symbol atype2symbol1(abool()) = Symbol::\bool();
Symbol atype2symbol1(areal()) = Symbol::\real();
Symbol atype2symbol1(arat()) = \rat();
Symbol atype2symbol1(astr()) = \str();
Symbol atype2symbol1(anum()) = Symbol::\num();
Symbol atype2symbol1(anode( list[AType fieldType] fields)) = Symbol::\node();
Symbol atype2symbol1(avoid()) = Symbol::\void();
Symbol atype2symbol1(avalue()) = Symbol::\value();
Symbol atype2symbol1(aloc()) = Symbol::\loc();
Symbol atype2symbol1(adatetime()) = \datetime();
Symbol atype2symbol1(alist(AType t)) = \list(atype2symbol(t));
Symbol atype2symbol1(aset(AType t)) = \set(atype2symbol(t));
Symbol atype2symbol1(atuple(atypeList(list[AType] ts))) = \tuple([atype2symbol(t) | t <- ts]);
Symbol atype2symbol1(amap(AType d, AType r)) = \map(atype2symbol(d), atype2symbol(r));
Symbol atype2symbol1(arel(atypeList(list[AType] ts))) = \set(\tuple([atype2symbol(t) | t <- ts]));
Symbol atype2symbol1(alrel(atypeList(list[AType] ts))) = \list(\tuple([atype2symbol(t) | t <- ts]));

// TODO: kwFormals are lost here because not supported by old run-time system
Symbol atype2symbol1(afunc(AType ret, list[AType] formals, lrel[AType fieldType, Expression defaultExp] kwFormals))
  = \func(atype2symbol(ret), [atype2symbol(f) | f <- formals], [ atype2symbol(f) | <f, _> <- kwFormals]);

Symbol atype2symbol1(aalias(str aname, [], AType aliased)) = \alias(aname,[],atype2symbol(aliased));
Symbol atype2symbol1(aalias(str aname, ps, AType aliased)) = \alias(aname,[atype2symbol(p) | p<-ps], atype2symbol(aliased)) when size(ps) > 0;

//Symbol atype2symbol1(aanno(str aname, AType onType, AType annoType)) = \anno(aname,atype2symbol(annoType), atype2symbol(onType));

Symbol atype2symbol1(aadt(str s, [], contextFreeSyntax()))  = Symbol::\sort(s);
Symbol atype2symbol1(aadt(str s, [], lexicalSyntax()))      = Symbol::\lex(s);
Symbol atype2symbol1(aadt(str s, [], keywordSyntax()))      = Symbol::\keywords(s);
Symbol atype2symbol1(aadt(str s, [], layoutSyntax()))       = Symbol::\layouts(s);
Symbol atype2symbol1(aadt(str s, list[AType] ps, dataSyntax())) = Symbol::adt(s, [atype2symbol(p) | p <- ps]);

Symbol atype2symbol1(aadt(str s, ps, contextFreeSyntax())) = \parameterized-sort(s, [atype2symbol(p) | p <- ps]) when size(ps) > 0;
Symbol atype2symbol1(aadt(str s, ps, lexicalSyntax())) = \parameterized-lex(s, [atype2symbol(p) | p <- ps]) when size(ps) > 0; 

Symbol atype2symbol1(t: acons(AType adt,
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
 = Symbol::cons(atype2symbol(adt), t.alabel, [atype2symbol(f) | f <- fields]); // we loose kw fields here

Symbol atype2symbol1(aparameter(str pn, AType t)) = Symbol::\parameter(pn, atype2symbol(t));
Symbol atype2symbol1(areified(AType t)) = Symbol::reified(atype2symbol(t));

// utilities

// overloaded types (union types) do not exist at the Rascal runtime level,
// so we lub the alternatives here which is what happens at run-time as well.
Symbol atype2symbol1(overloadedAType(rel[loc, IdRole, AType] overloads))
                = atype2symbol((\avoid() | alub(it, alt) | <_, _, alt> <- overloads));

list[Symbol] atypes2symbols(list[AType] atypes) = [atype2symbol(t) | t <- atypes];

Symbol atype2symbol1(Keyword kw) = atype2symbol(kw.fieldType);

// non-terminal symbols

// prods can not be Symbols, so we reduce this to the non-terminal
Symbol atype2symbol1(\prod(AType s, list[AType] _)) = atype2symbol(s);

// terminal symbols
Symbol atype2symbol1(alit(str string)) = Symbol::\lit(string);
Symbol atype2symbol1(acilit(str string)) = Symbol::\cilit(string);
//Symbol atype2symbol1("lit"(str string)) = Symbol::\lit(string);
//Symbol atype2symbol1("cilit"(str string)) = Symbol::\cilit(string);
Symbol atype2symbol1(\achar-class(list[ACharRange] ranges)) = Symbol::\char-class([range(r.begin, r.end) | r <- ranges ]);

Symbol atype2symbol1(\start(AType symbol)) = Symbol::\start(atype2symbol(symbol));

// regular symbols
Symbol atype2symbol1(\aempty()) = Symbol::\empty();
Symbol atype2symbol1(\opt(AType symbol)) = Symbol::\opt(atype2symbol(symbol));
Symbol atype2symbol1(\iter(AType symbol)) = Symbol::\iter(atype2symbol(symbol));
Symbol atype2symbol1(\iter-star(AType symbol)) = Symbol::\iter-star(atype2symbol(symbol));
Symbol atype2symbol1(\iter-seps(AType symbol, list[AType] separators)) = Symbol::\iter-seps(atype2symbol(symbol), [atype2symbol(sep) | sep <- separators ]);
Symbol atype2symbol1(\iter-star-seps(AType symbol, list[AType] separators)) = Symbol::\iter-star-seps(atype2symbol(symbol), [ atype2symbol(sep) | sep <- separators ]);
Symbol atype2symbol1(\alt(set[AType] alternatives)) = Symbol::\alt({ atype2symbol(a) | a <- alternatives });
Symbol atype2symbol1(\seq(list[AType] sequence)) = Symbol::\seq([atype2symbol(a) | a <- sequence ]);
Symbol atype2symbol1(\conditional(AType symbol, set[ACondition] conditions)) = Symbol::\conditional(atype2symbol(symbol), { acond2cond(cond) | cond <- conditions });

Symbol atype2symbol1(aprod(p:prod(AType def, list[AType] atypes))) {
    return atype2symbol(def);
//    t0 = atypes[0];
//    s0 = atype2symbol(t0);
//    res = Symbol::cons(atype2symbol(def), p.alabel, [s0]);
//    return res;
////    res = Production::prod(atype2symbol(def), [ atype2symbol(t) | t <- atypes, bprintln(t) ]);   //atype2symbol(def);
}
Production atype2symbol1(regular(AType def)) = \regular(atype2symbol(def));

//Symbol atype2symbol1(regular(AType def)) = atype2symbol(def);
default Symbol atype2symbol1(AType s)  { throw "could not convert <s> to Symbol"; }

private Condition acond2cond(ACondition::\follow(AType symbol)) = Condition::\follow(atype2symbol(symbol));
private Condition acond2cond(ACondition::\not-follow(AType symbol)) = Condition::\not-follow(atype2symbol(symbol));
private Condition acond2cond(ACondition::\precede(AType symbol)) = Condition::\precede(atype2symbol(symbol));
private Condition acond2cond(ACondition::\not-precede(AType symbol)) = Condition::\not-precede(atype2symbol(symbol));
private Condition acond2cond(ACondition::\delete(AType symbol)) = Condition::\delete(atype2symbol(symbol));
private Condition acond2cond("a-at-column"(int column)) = Condition::\at-column(column);
private Condition acond2cond("a-begin-of-line"()) = Condition::\begin-of-line();
private Condition acond2cond("a-end-of-line"()) = Condition::\end-of-line();
private Condition acond2cond("a-except"(str label)) = Condition::\except(label);

//private default Condition acond2cond("at-column"(int column)) = Condition::\at-column(column);
//private default Condition acond2cond("begin-of-line"()) = Condition::\begin-of-line();
//private default Condition acond2cond("end-of-line"()) = Condition::\end-of-line();
//private default Condition acond2cond("except"(str label)) = Condition::\except(label);



map[Symbol, Production] adefinitions2definitions(map[AType sort, AProduction def] defs) { 
    res = (atype2symbol(k) : aprod2prod(defs[k]) | k <- defs);
    return res;
}

map[Symbol, Production] adefinitions2definitions(map[AType, set[AType]] defs) {
 res = (atype2symbol(k) : Production::choice(atype2symbol(k), aprods2prods(defs[k])) | k <- defs);
 return res;
}

set[Production] aprods2prods(set[AType] alts) = {aprod2prod(p) | p <- alts/*, acons(AType adt, list[AType] fields, list[Keyword] kwFields) !:= p*/}; // TODO test added???
 
Production aprod2prod(AType::aprod(AProduction prod)) = aprod2prod(prod);

default Production aprod2prod(AType t) {
  throw "internal error: do not know how to translate a <t> to a production rule?!";
}

Production aprod2prod(p:AProduction::prod(AType lhs, list[AType] atypes, attributes=set[AAttr] as))
  = Production::prod((p.alabel?) ? Symbol::label(p.alabel, atype2symbol(lhs)) : atype2symbol(lhs), [atype2symbol(e) | e <- atypes], { aattr2attr(a) | a <- as }
  ); 
  
Production aprod2prod(AProduction::choice(AType def, set[AProduction] alts)) 
  = Production::choice(atype2symbol(def), {aprod2prod(p) | p <- alts});  
  
Production aprod2prod(AProduction::\associativity(AType def, AAssociativity \assoc, set[AProduction] alternatives))
  = Production::\associativity(atype2symbol(def), AAssociativity2Associativity(\assoc), {aprod2prod(a) | a <- alternatives});
  
Production aprod2prod(AProduction::priority(AType def, list[AProduction] alts)) 
  = Production::priority(atype2symbol(def), [aprod2prod(p) | p <- alts]);
  
Production aprod2prod(AProduction::reference(AType def, str cons))
  = Production::reference(atype2symbol(def), cons);    
    
Production aprod2prod(a:acons(AType adt, list[AType] fields, list[Keyword] kwFields)) {
    //res = Production::\cons(atype2symbol(adt), [atype2symbol(f) | f <- fields], [atype2symbol(g) | g <- kwFields], {});
    //if(a.alabel?) res = Symbol::label(a.alabel, res);
    res = Production::\cons((a.alabel?) ? Symbol::label(a.alabel, atype2symbol(adt)) : atype2symbol(adt), 
                            [atype2symbol(f) | f <- fields], 
                            [Symbol::label(g.fieldType.alabel, atype2symbol(g.fieldType)) | g <- kwFields], {});
    return res;
}

Attr aattr2attr(atag(value \tag)) = Attr::\tag(\tag);
Attr aattr2attr(\aassoc(AAssociativity \assoc)) = Attr::\assoc(AAssociativity2Associativity(\assoc));
Attr aattr2attr(\abracket()) = Attr::\bracket();

Associativity AAssociativity2Associativity(aleft()) = Associativity::\left();
Associativity AAssociativity2Associativity(aright()) = Associativity::\right();
Associativity AAssociativity2Associativity(aassoc()) = Associativity::\assoc();
Associativity AAssociativity2Associativity(\a-non-assoc()) = Associativity::\non-assoc();
    
//////////////////////////////////////////////////////////////////////////////////    
 
 /*****************************************************************************/
/*  Convert a Symbol to an AType                                              */
/*****************************************************************************/
            
AType symbol2atype(Symbol symbol){
    return symbol2atype1(symbol);
}

AType symbol2atype1(\int()) = aint();
AType symbol2atype1(\bool()) = abool();
AType symbol2atype1(\real()) = areal();
AType symbol2atype1(\rat()) = arat();
AType symbol2atype1(\str()) = astr();
AType symbol2atype1(\num()) = anum();
AType symbol2atype1(\node()) = anode([]);
AType symbol2atype1(\void()) = avoid();
AType symbol2atype1(\value()) = avalue();
AType symbol2atype1(\loc()) = aloc();
AType symbol2atype1(\datetime()) = adatetime();

AType symbol2atype1(Symbol::label(str label, symbol)){
    return symbol2atype(symbol)[alabel=label];
}

AType symbol2atype1(\set(Symbol symbol)) = aset(symbol2atype(symbol));
AType symbol2atype1(\rel(list[Symbol] symbols)) = arel(atypeList(symbol2atype(symbols)));
AType symbol2atype1(\lrel(list[Symbol] symbols)) = alrel(atypeList(symbol2atype(symbols)));
AType symbol2atype1(\tuple(list[Symbol] symbols)) = atuple(atypeList(symbol2atype(symbols)));  
AType symbol2atype1(\list(Symbol symbol)) = alist(symbol2atype(symbol));
AType symbol2atype1(\map(Symbol from, Symbol to)) = amap(symbol2atype(from), symbol2atype(to));
AType symbol2atype1(\bag(Symbol symbol)) = abag(symbol2atype(symbol));
AType symbol2atype1(\adt(str name, list[Symbol] parameters)) = aadt(name, symbol2atype(parameters), dataSyntax());
AType symbol2atype1(\cons(Symbol \adt, str name, list[Symbol] parameters)) = acons(symbol2atype(\adt), symbol2atype(parameters), [])[alabel=name];

     //| \alias(str name, list[Symbol] parameters, Symbol aliased)
     //| \func(Symbol ret, list[Symbol] parameters, list[Symbol] kwTypes)
     //| \overloaded(set[Symbol] alternatives)
     //| \var-func(Symbol ret, list[Symbol] parameters, Symbol varArg)
     //| \reified(Symbol symbol)

AType symbol2atype1(Symbol::sort(str name)) = aadt(name, [], contextFreeSyntax());
AType symbol2atype1(Symbol::lex(str name)) = aadt(name, [], lexicalSyntax());
AType symbol2atype1(Symbol::keywords(str name)) = aadt(name, [], keywordSyntax());
AType symbol2atype1(Symbol::layouts(str name)) = AType::layouts(name);

AType symbol2atype1(Symbol::\parameterized-sort(str name, list[Symbol] parameters)) =
    aadt(name, symbol2atype(parameters), contextFreeSyntax());
AType symbol2atype1(Symbol::\parameterized-lex(str name, list[Symbol] parameters)) =
    aadt(name, symbol2atype(parameters), lexicalSyntax());

// ---- Associativity ---------------------------------------------------------

//Associativity symbol2atype1(\left(), map[AType, set[AType]] defs) = Associativity::\left();
//Associativity symbol2atype1(\right(), map[AType, set[AType]] defs) = Associativity::\right();
//Associativity symbol2atype1(\assoc(), map[AType, set[AType]] defs) = Associativity::\assoc();
//Associativity symbol2atype1(\non-assoc(), map[AType, set[AType]] defs) = Associativity::\non-assoc();

// ---- Attr ------------------------------------------------------------------

//str symbol2atype1(\tag(value v),  map[AType, set[AType]] defs) 
//    = "tag(<value2IValue(v)>)";
//str symbol2atype1(\assoc(Associativity \assoc),  map[AType, set[AType]] defs) 
//    = "assoc(<symbol2atype(\assoc, defs)>)";
//str symbol2atype1(\bracket(),  map[AType, set[AType]] defs)
//    = "abracket())";

// ---- Tree ------------------------------------------------------------------

//str symbol2atype1(tr:appl(AProduction aprod, list[Tree] args), map[AType, set[AType]] defs)
//    = tr@\loc? ? "appl(<symbol2atype(aprod, defs)>, <symbol2atype(args, defs)>, <value2IValue(tr@\loc)>)"
//              : "appl(<symbol2atype(aprod, defs)>, <symbol2atype(args, defs)>)";
//
//str symbol2atype1(cycle(AType asymbol, int cycleLength), map[AType, set[AType]] defs)
//    = "cycle(<atype2IValue(asymbol, defs)>, <value2IValue(cycleLength)>)";
//
//str symbol2atype1(amb(set[Tree] alternatives), map[AType, set[AType]] defs)
//    = "aamb(<symbol2atype(alternatives,defs)>)";
 
//AType symbol2atype1(char(int character))
//    = char(character);
    
// ---- SyntaxRole ------------------------------------------------------------

//str symbol2atype1(SyntaxRole sr, map[AType, set[AType]] defs) = "<sr>";
   
// ---- AProduction -----------------------------------------------------------

//str symbol2atype1(\choice(AType def, set[AProduction] alternatives), map[AType, set[AType]] defs)
//    = "choice(<atype2IValue(def, defs)>, <symbol2atype(alternatives, defs)>)";
//
//str symbol2atype1(tr:prod(AType def, list[AType] asymbols), map[AType, set[AType]] defs){
//    base = "prod(<atype2IValue(def, defs)>, <atype2IValue(asymbols, defs)>";
//    kwds = tr.attributes? ? ", <symbol2atype(tr.attributes, defs)>" : "";
//    if(tr@\loc?) kwds += ", <value2IValue(tr@\loc)>";
//    return base + kwds + ")";
//}

AProduction symbol2atype1(Production::regular(Symbol def))
    = AType::regular(symbol2atype(def));

//str symbol2atype1(error(AProduction prod, int dot), map[AType, set[AType]] defs)
//    = "error(<symbol2atype(prod, defs)>, <value2IValue(dot)>)";
//
//str symbol2atype1(skipped(), map[AType, set[AType]] defs)
//    = "skipped()";
//    
//str symbol2atype1(\priority(AType def, list[AProduction] choices), map[AType, set[AType]] defs)
//    = "priority(<atype2IValue(def, defs)>, <symbol2atype(choices, defs)>)";
//    
//str symbol2atype1(\associativity(AType def, Associativity \assoc, set[AProduction] alternatives), map[AType, set[AType]] defs)
//    = "associativity(<atype2IValue(def, defs)>, <symbol2atype(\assoc, defs)>, <symbol2atype(alternatives, defs)>)";
//
//str symbol2atype1(\others(AType def) , map[AType, set[AType]] defs)
//    = "others(<atype2IValue(def, defs)>)";
//
//str symbol2atype1(\reference(AType def, str cons), map[AType, set[AType]] defs)
//    = "reference(<atype2IValue(def, defs)>, <value2IValue(cons)>)";
    
// ---- ACharRange ------------------------------------------------------------
//str symbol2atype1(range(int begin, int end), map[AType, set[AType]] defs)
//    = "range(<value2IValue(begin)>, <value2IValue(end)>)";
    
// ---- AType to Symbol for parse trees ----------------------------------------

list[Symbol] nonLayout(list[Symbol] separators)
    = [s | s <- separators,  \layouts(str _) !:= s ];

AType symbol2atype1(Symbol::lit(str string))
    = AType::alit(string);

AType symbol2atype1(Symbol::cilit(str string))
    = AType::acilit(string);

AType symbol2atype1(Symbol::\char-class(list[CharRange] ranges))
    = AType::\achar-class([ charrange2acharrange(r) | r <- ranges ]);    
 
AType symbol2atype1(Symbol::\empty())
    = AType::aempty();     

AType symbol2atype1(Symbol::\opt(Symbol symbol))
    = AType::opt(symbol2atype(symbol));     

AType symbol2atype1(Symbol::\iter(Symbol symbol))
    = AType::iter(symbol2atype(symbol));     

AType symbol2atype1(Symbol::\iter-star(Symbol symbol))
    = AType::\iter-star(symbol2atype(symbol));   

AType symbol2atype1(Symbol::\iter-seps(Symbol symbol, list[Symbol] separators))
    = AType::\iter-seps(symbol2atype(symbol), symbol2atype(nonLayout(separators)));
    
AType symbol2atype1(Symbol::\iter-star-seps(Symbol symbol, list[Symbol] separators))
    = AType::\iter-star-seps(symbol2atype(symbol), symbol2atype(nonLayout(separators)));   
    
AType symbol2atype1(Symbol::\alt(set[Symbol] alternatives)){
    return  AType::alt(symbol2atype(alternatives));  
}   

AType symbol2atype1(Symbol::\seq(list[Symbol] symbols))
    = AType::seq(symbol2atype(symbols));     
 
AType symbol2atype1(Symbol::\start(Symbol symbol))
    = AType::\start(symbol2atype1(symbol));  
    
list[AType] symbol2atype(list[Symbol] symbols) = [ symbol2atype(s) | s <- symbols ]; 

set[AType] symbol2atype(set[Symbol] symbols) = { symbol2atype(s) | s <- symbols }; 

ACharRange charrange2acharrange(CharRange::range(int begin, int end))
    = ACharRange::arange(begin, end);
    

AType symbol2atype1(Symbol::\conditional(Symbol symbol, set[Condition] conditions))
    = AType::conditional(symbol2atype(symbol), condition2acondition(conditions));   
    
// ---- ACondition ------------------------------------------------------------

ACondition condition2acondition(Condition::\follow(Symbol symbol))
    = ACondition::follow(symbol2atype(symbol));   

ACondition condition2acondition(Condition::\not-follow(Symbol symbol))
    = ACondition::\not-follow(symbol2atype(symbol));
    
ACondition condition2acondition(Condition::\precede(Symbol symbol))
    = ACondition::precede(symbol2atype(symbol));  

ACondition condition2acondition(Condition::\not-precede(Symbol symbol))
    = ACondition::\not-precede(symbol2atype(symbol)); 
    
ACondition condition2acondition(Condition::\delete(Symbol symbol))
    = ACondition::delete(symbol2atype(symbol)); 
    
ACondition condition2acondition(Condition::\at-column(int column))
    = ACondition::\a-at-column(column);  
    
ACondition condition2acondition(Condition::\begin-of-line())
    = ACondition::\a-begin-of-line();
    
ACondition condition2acondition(Condition::\end-of-line())
    = ACondition::\a-end-of-line(); 
    
ACondition condition2acondition(Condition::\except(str label))
    = ACondition::\a-except(label); 
    
set[ACondition] condition2acondition(set[Condition] conditions)
    = { condition2acondition(cond) | Condition cond <- conditions };
    

// ---- Predicates, selectors and constructors --------------------------------

// ---- utilities

@doc{Unwraps parameters and conditionals from a type.}
AType unwrapType(p: aparameter(_,tvb)) = p.alabel? ? unwrapType(tvb)[alabel=p.alabel] : unwrapType(tvb);
AType unwrapType(\conditional(AType sym,  set[ACondition] _)) = unwrapType(sym);
default AType unwrapType(AType t) = t;

bool allLabelled(list[AType] tls) = size(tls) == size([tp | tp <- tls, !isEmpty(tp.alabel)]);

bool isArithType(AType t) = isIntType(t) || isRealType(t) || isRatType(t) || isNumType(t);

// ---- int

@doc{
.Synopsis
Determine if the given type is an int.
}
bool isIntType(aparameter(_,AType tvb)) = isIntType(tvb);
bool isIntType(aint()) = true;
default bool isIntType(AType _) = false;

@doc{Create a new int type.}
AType makeIntType() = aint();

// ---- bool 
@doc{
.Synopsis
Determine if the given type is a bool.
}
bool isBoolType(aparameter(_,AType tvb)) = isBoolType(tvb);
bool isBoolType(abool()) = true;
default bool isBoolType(AType _) = false;

@doc{Create a new bool type.}
AType makeBoolType() = abool();

// ---- real

@doc{
.Synopsis
Determine if the given type is a real.
}
bool isRealType(aparameter(_,AType tvb)) = isRealType(tvb);
bool isRealType(areal()) = true;
default bool isRealType(AType _) = false;

@doc{Create a new real type.}
AType makeRealType() = areal();

// ---- rat

@doc{
.Synopsis
Determine if the given type is a rational.
}
bool isRatType(aparameter(_,AType tvb)) = isRatType(tvb);
bool isRatType(arat()) = true;
default bool isRatType(AType _) = false;

@doc{Create a new rat type.}
AType makeRatType() = arat();

// ---- str

@doc{
.Synopsis
Determine if the given type is a string.
}
bool isStrType(aparameter(_,AType tvb)) = isStrType(tvb);
bool isStrType(astr()) = true;
default bool isStrType(AType _) = false;

@doc{Create a new str type.}
AType makeStrType() = astr();

// ---- num

@doc{
.Synopsis
Determine if the given type is a num.
}
bool isNumType(aparameter(_,AType tvb)) = isNumType(tvb);
bool isNumType(anum()) = true;
default bool isNumType(AType _) = false;

bool isNumericType(AType t) = isIntType(t) || isRealType(t) || isRatType(t) || isNumType(t);

@doc{Create a new num type.}
AType makeNumType() = anum();

// ---- node

@doc{
.Synopsis
Determine if the given type is a node.
}
bool isNodeType(aparameter(_,AType tvb)) = isNodeType(tvb);
bool isNodeType(anode(_)) = true;
bool isNodeType(aadt(_,_,_)) = true;
default bool isNodeType(AType _) = false;

@doc{Create a new node type.}
AType makeNodeType() = anode([]);

@doc{Get the keywords of a node as a tuple.}
AType getNodeFieldsAsTuple(AType t) {
    if (anode(flds) := unwrapType(t)) return atuple(atypeList([ tp | tp <- flds, !isEmpty(tp.alabel) ]));
    throw rascalCheckerInternalError("getNodeFieldsAsTuple called with unexpected type <prettyAType(t)>");
}  

// ---- hasKeywordParameters 

bool hasKeywordParameters(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
    = !isEmpty(kwFormals);
    
bool hasKeywordParameters(acons(AType adt, list[AType] fields, list[Keyword] kwFields))
    = !isEmpty(kwFields);
    
bool hasKeywordParameters(overloadedAType(overloads))
    = any(<loc _, IdRole _, AType t> <- overloads, hasKeywordParameters(t));

default bool hasKeywordParameters(AType t) = false;


// ---- void

@doc{
.Synopsis
Determine if the given type is a void.
}
bool isVoidType(aparameter(_,AType tvb)) = isVoidType(tvb);
bool isVoidType(avoid()) = true;
default bool isVoidType(AType _) = false;

@doc{Create a new void type.}
AType makeVoidType() = avoid();

// ---- value

@doc{
.Synopsis
Determine if the given type is a value.
}
bool isValueType(aparameter(_,AType tvb)) = isValueType(tvb);
bool isValueType(avalue()) = true;
default bool isValueType(AType _) = false;

@doc{Create a new value type.}
AType makeValueType() = avalue();

// ---- loc

@doc{
.Synopsis
Determine if the given type is a loc.
}
bool isLocType(aparameter(_,AType tvb)) = isLocType(tvb);
bool isLocType(aloc()) = true;
default bool isLocType(AType _) = false;

@doc{Create a new loc type.}
AType makeLocType() = aloc();

// ---- datetime

@doc{
.Synopsis
Determine if the given type is a `datetime`.
}
bool isDateTimeType(aparameter(_,AType tvb)) = isDateTimeType(tvb);
bool isDateTimeType(adatetime()) = true;
default bool isDateTimeType(AType _) = false;

@doc{Create a new datetime type.}
AType makeDateTimeType() = adatetime();

// ---- set

@doc{
.Synopsis
Determine if the given type is a set.
}
bool isSetType(aparameter(_,AType tvb)) = isSetType(tvb);
bool isSetType(aset(_)) = true;
bool isSetType(arel(_)) = true;
default bool isSetType(AType _) = false;

@doc{Create a new set type, given the element type of the set.}
AType makeSetType(AType elementType) {
    return isTupleType(elementType) ? makeRelTypeFromTuple(elementType) : aset(elementType);
}

@doc{Get the element type of a set.}
AType getSetElementType(AType t) {
    if (aset(et) := unwrapType(t)) return et;
    if (arel(ets) := unwrapType(t)) return atuple(ets);
    throw rascalCheckerInternalError("Error: Cannot get set element type from type <prettyAType(t)>");
}

@doc{Get the element type of any AType}
AType getElementType(alist(AType et)) = et;
AType getElementType(aset(AType et)) = et;
AType getElementType(amap(AType kt, AType vt)) = kt;
AType getElementType(abag(AType et)) = et;
default AType getElementType(AType t) = avalue();

// ---- rel

@doc{
.Synopsis
Determine if the given type is a relation.
}
bool isRelType(aparameter(_,AType tvb)) = isRelType(tvb);
bool isRelType(arel(_)) = true;
bool isRelType(aset(AType tp)) = true when isTupleType(tp);
default bool isRelType(AType _) = false;

@doc{Ensure that sets of tuples are treated as relations.}
AType aset(AType t) = arel(atypeList(getTupleFields(t))) when isTupleType(t);

@doc{Create a new rel type, given the element types of the fields. Check any given alabels for consistency.}
AType makeRelType(AType elementTypes...) {
    set[str] labels = { tp.alabel | tp <- elementTypes, !isEmpty(tp.alabel) };
    if (size(labels) == 0 || size(labels) == size(elementTypes))
        return arel(atypeList(elementTypes));
    else
        throw rascalCheckerInternalError("For rel types, either all fields much be given a distinct label or no fields should be labeled."); 
}

@doc{Create a new rel type based on a given tuple type.}
AType makeRelTypeFromTuple(AType t) = arel(atypeList(getTupleFields(t)));

@doc{Get the type of the relation fields as a tuple.}
AType getRelElementType(AType t) {
    if (arel(ets) := unwrapType(t)) return atuple(ets);
    if (aset(tup) := unwrapType(t)) return tup;
    throw rascalCheckerInternalError("Cannot get relation element type from type <prettyAType(t)>");
}

bool hasField(AType t, str name){
    switch(unwrapType(t)){
        case arel(atypeList(tls)): return any(tp <- tls, tp.alabel == name);
        case alrel(atypeList(tls)): return any(tp <- tls, tp.alabel == name);
        case atuple(atypeList(tls)): return any(tp <- tls, tp.alabel == name);
        case amap(dm, rng): return dm.alabel == name || rng.alabel == name;
        case acons(_,list[AType] cts,_): return any(tp <- cts, tp.alabel == name);
    }
    return false;
}

@doc{Get whether the rel has field names or not.}
bool relHasFieldNames(AType t) {
    if (arel(atypeList(tls)) := unwrapType(t)) return size(tls) == size([tp | tp <- tls, !isEmpty(tp.alabel)]);
    throw rascalCheckerInternalError("relHasFieldNames given non-Relation type <prettyAType(t)>");
}

@doc{Get the field names of the rel fields.}
list[str] getRelFieldNames(AType t) {
    if (arel(atypeList(tls)) := unwrapType(t)){
        return [tp.alabel | tp <- tls];
    }
    throw rascalCheckerInternalError("getRelFieldNames given non-Relation type <prettyAType(t)>");
}

@doc{Get the fields of a relation.}
list[AType] getRelFields(AType t) {
    if (arel(atypeList(tls)) := unwrapType(t)) return tls;
    if (aset(atuple(atypeList(tls))) := unwrapType(t)) return tls;
    throw rascalCheckerInternalError("getRelFields given non-Relation type <prettyAType(t)>");
}

// ---- lrel

@doc{
.Synopsis
Determine if the given type is a list relation.
}
bool isListRelType(aparameter(_,AType tvb)) = isListRelType(tvb);
bool isListRelType(alrel(_)) = true;
bool isListRelType(alist(AType tp)) = true when isTupleType(tp);
default bool isListRelType(AType _) = false;

@doc{Ensure that lists of tuples are treated as list relations.}
AType alist(AType t) = alrel(atypeList(getTupleFields(t))) when isTupleType(t);

@doc{Create a new list rel type, given the element types of the fields. Check any given labels for consistency.}
AType makeListRelType(AType elementTypes...) {
    set[str] labels = { tp.alabel | tp <- elementTypes, !isEmpty(tp.alabel) };
    if (size(labels) == 0 || size(labels) == size(elementTypes)) 
        return alrel(atypeList(elementTypes));
    else
        throw rascalCheckerInternalError("For lrel types, either all fields much be given a distinct label or no fields should be labeled."); 
}

@doc{Create a new lrel type based on a given tuple type.}
AType makeListRelTypeFromTuple(AType t) = alrel(atypeList(getTupleFields(t)));

@doc{Get the type of the list relation fields as a tuple.}
AType getListRelElementType(AType t) {
    if (alrel(ets) := unwrapType(t)) return atuple(ets);
    if (alist(tup) := unwrapType(t)) return tup;
    throw rascalCheckerInternalError("Cannot get list relation element type from type <prettyAType(t)>");
}

@doc{Get the field names of the list rel fields.}
list[str] getListRelFieldNames(AType t) {
    if (alrel(atypeList(tls)) := unwrapType(t)){
        return [tp.alabel | tp <- tls];
    }
    throw rascalCheckerInternalError("getListRelFieldNames given non-List-Relation type <prettyAType(t)>");
}

@doc{Get the fields of a list relation.}
list[AType] getListRelFields(AType t) {
    if (alrel(atypeList(tls)) := unwrapType(t)) return tls;
    if (alist(atuple(atypeList(tls))) := unwrapType(t)) return tls;
    throw rascalCheckerInternalError("getListRelFields given non-List-Relation type <prettyAType(t)>");
}

// ---- tuple

@doc{
.Synopsis
Determine if the given type is a tuple.
}
bool isTupleType(aparameter(_,AType tvb)) = isTupleType(tvb);
bool isTupleType(atuple(_)) = true;
default bool isTupleType(AType _) = false;

@doc{Create a new tuple type, given the element types of the fields. Check any given labels for consistency.}
AType makeTupleType(AType elementTypes...) {
    set[str] labels = { tp.alabel | tp <- elementTypes, !isEmpty(tp.alabel) };
    if(size(labels) > 0 && size(labels) != size(elementTypes))
        elementTypes = [unset(e, "alabel") | e <- elementTypes];
    return atuple(atypeList(elementTypes));
    //else
    //    throw rascalCheckerInternalError("For tuple types, either all fields much be given a distinct label or no fields should be labeled."); 
}

@doc{Indicate if the given tuple has a field of the given name.}
bool tupleHasField(AType t, str fn) {
    return atuple(atypeList(tas)) := unwrapType(t) && fn in { tp.alabel | tp <- tas, tp.alabel? } ;
}

@doc{Indicate if the given tuple has a field with the given field offset.}
bool tupleHasField(AType t, int fn) {
    return atuple(atypeList(tas)) := unwrapType(t) && 0 <= fn && fn < size(tas);
}

@doc{Get the type of the tuple field with the given name.}
AType getTupleFieldType(AType t, str fn) {
    if (atuple(atypeList(tas)) := unwrapType(t)) {
        for(tp <- tas){
            if(tp.alabel == fn) return tp;
        }
        throw rascalCheckerInternalError("Tuple <prettyAType(t)> does not have field <fn>");
    }
    throw rascalCheckerInternalError("getTupleFieldType given unexpected type <prettyAType(t)>");
}

@doc{Get the type of the tuple field at the given offset.}
AType getTupleFieldType(AType t, int fn) {
    if (atuple(atypeList(tas)) := t) {
        if (0 <= fn && fn < size(tas)) return unwrapType(tas[fn]);
        throw rascalCheckerInternalError("Tuple <prettyAType(t)> does not have field <fn>");
    }
    throw rascalCheckerInternalError("getTupleFieldType given unexpected type <prettyAType(t)>");
}

@doc{Get the types of the tuple fields, with labels removed}
list[AType] getTupleFieldTypes(AType t) {
    if (atuple(atypeList(tas)) := t)
        return tas;
    throw rascalCheckerInternalError("Cannot get tuple field types from type <prettyAType(t)>"); 
}

@doc{Get the fields of a tuple as a list.}
list[AType] getTupleFields(AType t) {
    if (atuple(atypeList(tas)) := unwrapType(t)) return tas;
    throw rascalCheckerInternalError("Cannot get tuple fields from type <prettyAType(t)>"); 
}

@doc{Get the number of fields in a tuple.}
int getTupleFieldCount(AType t) = size(getTupleFields(t));

@doc{Does this tuple have field names?}
bool tupleHasFieldNames(AType t) {
    if (atuple(atypeList(tas)) := unwrapType(t)) return size(tas) == size([tp | tp <- tas, !isEmpty(tp.alabel)]);
    throw rascalCheckerInternalError("tupleHasFieldNames given non-Tuple type <prettyAType(t)>");
}

@doc{Get the names of the tuple fields.}
list[str] getTupleFieldNames(AType t) {
    if (atuple(atypeList(tls)) := unwrapType(t)) {
        if (allLabelled(tls)) {
            return [tp.alabel | tp <- tls];
        }
        throw rascalCheckerInternalError("getTupleFieldNames given tuple type without field names: <prettyAType(t)>");        
    }
    throw rascalCheckerInternalError("getTupleFieldNames given non-Tuple type <prettyAType(t)>");
}

@doc{Get the name of the tuple field at the given offset.}
str getTupleFieldName(AType t, int idx) {
    list[str] names = getTupleFieldNames(t);
    if (0 <= idx && idx < size(names)) return names[idx];
    throw rascalCheckerInternalError("getTupleFieldName given invalid index <idx>");
}

// ---- node

// ---- list

@doc{
.Synopsis
Determine if the given type is a list.
}
bool isListType(aparameter(_,AType tvb)) = isListType(tvb);
bool isListType(alist(_)) = true;
bool isListType(alrel(_)) = true;
default bool isListType(AType _) = false;

@doc{Create a new list type, given the element type of the list.}
AType makeListType(AType elementType) {
    return isTupleType(elementType) ? makeListRelTypeFromTuple(elementType) : alist(elementType);
} 

@doc{Get the element type of a list.}
AType getListElementType(AType t) {
    if (alist(et) := unwrapType(t)) return et;
    if (alrel(ets) := unwrapType(t)) return atuple(ets);  
    return avalue();  
    //throw rascalCheckerInternalError("Cannot get list element type from type <prettyAType(t)>");
}  

// ---- map

@doc{
.Synopsis
Determine if the given type is a map.
}
bool isMapType(x:aparameter(_,AType tvb)) = isMapType(tvb);
bool isMapType(x:amap(_,_)) = true;
default bool isMapType(AType x) = false;

@doc{Create a new map type, given the types of the domain and range. Check to make sure field names are used consistently.}
AType makeMapType(AType domain, AType range) {
    if(!isEmpty(domain.alabel) && !isEmpty(range.alabel)){
        if(domain.alabel != range.alabel) return amap(domain, range);
        throw rascalCheckerInternalError("The field names of the map domain and range must be distinct; found `<domain.alabel>`");
    }
    else if(!isEmpty(domain.alabel)) return amap(unset(domain,"alabel"),range);
    else if(!isEmpty(range.alabel)) return amap(domain,unset(range, "alabel"));
    return amap(domain, range);
}

@doc{Get the domain and range of the map as a tuple.}
AType getMapFieldsAsTuple(AType t) {
    if (amap(dt,rt) := unwrapType(t)) return atuple(atypeList([dt,rt]));
    throw rascalCheckerInternalError("getMapFieldsAsTuple called with unexpected type <prettyAType(t)>");
}       

@doc{Check to see if a map defines a field (by name).}
bool mapHasField(AType t, str fn) = tupleHasField(getMapFieldsAsTuple(t),fn);

@doc{Check to see if a map defines a field (by index).}
bool mapHasField(AType t, int fn) = tupleHasField(getMapFieldsAsTuple(t),fn);

@doc{Return the type of a field defined on a map (by name).}
AType getMapFieldType(AType t, str fn) = getTupleFieldType(getMapFieldsAsTuple(t),fn);

@doc{Return the type of a field defined on a map (by index).}
AType getMapFieldType(AType t, int fn) = getTupleFieldType(getMapFieldsAsTuple(t),fn);

@doc{Get the fields in a map as a list of fields.}
list[AType] getMapFields(AType t) = getTupleFields(getMapFieldsAsTuple(t));

@doc{Check to see if the map has field names.}
bool mapHasFieldNames(AType t) = tupleHasFieldNames(getMapFieldsAsTuple(t));

@doc{Get the field names from the map fields.}
list[str] getMapFieldNames(AType t) {
    if ([dm, rng] := getMapFields(t)) {
        return [ dm.alabel, rng.alabel ];
    }
    throw rascalCheckerInternalError("getMapFieldNames given map type without field names: <prettyAType(t)>");        
}

@doc{Get the field name for the field at a specific index.}
str getMapFieldName(AType t, int idx) = getMapFieldNames(t)[idx];

@doc{Get the domain type of the map.}    
AType getMapDomainType(AType t) = unwrapType(getMapFields(t)[0]);

@doc{Get the range type of the map.}
AType getMapRangeType(AType t) = unwrapType(getMapFields(t)[1]);

// ---- bag

@doc{
.Synopsis
Determine if the given type is a bag (bags are not yet implemented).
}
bool isBagType(aparameter(_,AType tvb)) = isBagType(tvb);
bool isBagType(abag(_)) = true;
default bool isBagType(AType _) = false;

@doc{Create a new bag type, given the element type of the bag.}
AType makeBagType(AType elementType) = abag(elementType);


@doc{Get the element type of a bag.}
AType getBagElementType(AType t) {
    if (abag(et) := unwrapType(t)) return et;
    throw rascalCheckerInternalError("Cannot get set element type from type <prettyAType(t)>");
}

@doc{
.Synopsis
Determine if the given type is an Abstract Data Type (ADT).
}
bool isADTType(aparameter(_,AType tvb)) = isADTType(tvb);
bool isADTType(aadt(_,_,_)) = true;
bool isADTType(areified(_)) = true;
bool isADTType(\start(AType s)) = isADTType(s);
default bool isADTType(AType _) = false;

@doc{Create a new parameterized ADT type with the given type parameters}
AType makeParameterizedADTType(str n, AType p...) = aadt(n,p, dataSyntax());

@doc{Create a new ADT type with the given name.}
AType makeADTType(str n) = aadt(n,[], dataSyntax());

@doc{Get the name of the ADT.}
str getADTName(AType t) {
    if (aadt(n,_,_) := unwrapType(t)) return n;
    if (acons(a,_,_) := unwrapType(t)) return getADTName(a);
    if (\start(ss) := unwrapType(t)) return getADTName(ss);
    if (areified(_) := unwrapType(t)) return "type";
    throw rascalCheckerInternalError("getADTName, invalid type given: <prettyAType(t)>");
}

@doc{Get the type parameters of an ADT.}
list[AType] getADTTypeParameters(AType t) {
    if (aadt(_,ps,_) := unwrapType(t)) return ps;
    if (acons(a,_,_) := unwrapType(t)) return getADTTypeParameters(a);
    if (\start(ss) := unwrapType(t)) return getADTTypeParameters(ss);
    if (areified(_) := unwrapType(t)) return [];
    throw rascalCheckerInternalError("getADTTypeParameters given non-ADT type <prettyAType(t)>");
}

bool isTypeParameter(aparameter(_,_)) = true;
default bool isTypeParameter(AType t) = false;

@doc{Return whether the ADT has type parameters.}
bool adtHasTypeParameters(AType t) = size(getADTTypeParameters(t)) > 0;

bool isOverloadedAType(overloadedAType(rel[loc, IdRole, AType] overloads)) = true;
default bool isOverloadedType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a constructor.
}
bool isConstructorType(aparameter(_,AType tvb)) = isConstructorType(tvb);
bool isConstructorType(acons(AType _, /*str _,*/ _, _)) = true;
default bool isConstructorType(AType _) = false;

@doc{Get the ADT type of the constructor.}
AType getConstructorResultType(AType ct) {
    if (acons(a,_,_) := unwrapType(ct)) return a;
    throw rascalCheckerInternalError("Cannot get constructor ADT type from non-constructor type, got <prettyAType(ct)>");
}

@doc{Get a list of the argument types in a constructor.}
list[AType] getConstructorArgumentTypes(AType ct) {
    if (acons(_,list[AType] cts,_) := unwrapType(ct)) return cts;
    throw rascalCheckerInternalError("Cannot get constructor arguments from non-constructor type, got <prettyAType(ct)>");
}

@doc{Get a tuple with the argument types as the fields.}
AType getConstructorArgumentTypesAsTuple(AType ct) {
    return atuple(atypeList(getConstructorArgumentTypes(ct)));
}

@doc{
.Synopsis
Determine if the given type is a function.
}
bool isFunctionType(aparameter(_,AType tvb)) = isFunctionType(tvb);
bool isFunctionType(afunc(_,_,_)) = true;
default bool isFunctionType(AType _) = false;

@doc{Get a list of arguments for the function.}
list[AType] getFunctionArgumentTypes(AType ft) {
    if (afunc(_, ats, _) := unwrapType(ft)) return ats;
    throw rascalCheckerInternalError("Cannot get function arguments from non-function type, got <prettyAType(ft)>");
}

set[AType] getFunctionTypeParameters(AType ft){
    if (af: afunc(_, _, _) := unwrapType(ft)){
       return {p | /p:aparameter(_,_) := af };
    }
    throw rascalCheckerInternalError("Cannot get Type parameters from non-function type, got <prettyAType(ft)>");
}

set[AType] getTypeParameters(AType t){
    return {p | /p:aparameter(_,_) := t };
}

@doc{Get a list of arguments for overloaded function/constructors}
list[AType] getFunctionOrConstructorArgumentTypes(AType ft) {
    if (afunc(_, ats, _) := unwrapType(ft)) return ats;
    if (acons(_,list[AType] cts,_) := unwrapType(ft)) return cts;
    if (overloadedAType(rel[loc def, IdRole role, AType atype] overloads) := unwrapType(ft)){
       arities = { size(getFormals(tp)) | tp <- overloads<2> };
       assert size(arities) == 1;
       ar = getFirstFrom(arities);
       resType = (avoid() | alub(it, getResult(tp) )| tp <- overloads<2>);
       formalsTypes = [avoid() | _ <- [0 .. ar]];
       
       formalsTypes = (formalsTypes | alubList(it, getFormals(tp)) | tp <- overloads<2>);
       return formalsTypes;
        //result = avoid();
        //for(tuple[loc def, IdRole role, AType atype] ovl <- overloads){
        //    result = alub(result, atypeList(getFunctionOrConstructorArgumentTypes(ovl.atype)));
        //}
        //if(atypeList(list[AType] argTypes) := result) return argTypes;
        //throw rascalCheckerInternalError("Expected atypeList, found <prettyAType(result)>");
    }
    throw rascalCheckerInternalError("Cannot get function/constructor arguments from type, got <prettyAType(ft)>");
}

@doc{Get a list of keyword parameters for overloaded function/constructors}
list[Keyword] getFunctionOrConstructorKeywords(AType ft) {
    if (afunc(_, _, list[Keyword] kws) := unwrapType(ft)) return kws;
    if (acons(_,_,list[Keyword] kws) := unwrapType(ft)) return kws;
    if (overloadedAType(rel[loc def, IdRole role, AType atype] overloads) := unwrapType(ft)){
       kws = { *getFunctionOrConstructorKeywords(tp) | tp <- overloads<2> };
       return toList(kws);
    }
    throw rascalCheckerInternalError("Cannot get function/constructor keyword arguments from type, got <prettyAType(ft)>");
}

@doc{Get the arguments for a function in the form of a tuple.}
AType getFunctionArgumentTypesAsTuple(AType ft) {
    if (afunc(_, ats, _) := unwrapType(ft)) return atuple(atypeList(ats));
    throw rascalCheckerInternalError("Cannot get function arguments from non-function type, got <prettyAType(ft)>");
}

@doc{Get the return type for a function.}
AType getFunctionReturnType(AType ft) {
    if (afunc(rt, _, _) := unwrapType(ft)) return rt;
    if(overloadedAType(rel[loc, IdRole, AType] _) := unwrapType(ft)) {
        return getResult( unwrapType(ft));
    }
    throw rascalCheckerInternalError("Cannot get function return type from non-function type, got <prettyAType(ft)>");
}

int getArity(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals)) = size(formals);
int getArity(acons(AType adt, list[AType] fields, list[Keyword] kwFields)) = size(fields);
default int getArity(AType t) {
    throw rascalCheckerInternalError("Can only get arity from function or constructor type, got <prettyAType(t)>");
}

list[AType] getFormals(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals)) = formals;
list[AType] getFormals(acons(AType adt, list[AType] fields, list[Keyword] kwFields)) = fields;
list[AType] getFormals(aprod(prod(AType def, list[AType] atypes))) = [t | t <- atypes, isADTType(t)];
list[AType] getFormals(aprod(\associativity(AType def, AAssociativity \assoc, set[AProduction] alternatives))) = getFormals(aprod(getFirstFrom(alternatives)));
list[AType] getFormals(overloadedAType(rel[loc, IdRole, AType] overloads)) = (getFormals(getFirstFrom(overloads<2>)) | alubList(it, getFormals(tp) )| tp <- overloads<2>);
default list[AType] getFormals(AType t){
    iprintln(t);
    throw rascalCheckerInternalError("Can only get formals from function or constructor type, got <prettyAType(t)>");
}

AType getResult(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals)) = ret;
AType getResult(acons(AType adt, list[AType] fields, list[Keyword] kwFields)) = adt;
AType getResult(aprod(prod(AType def, list[AType] atypes))) = def;
AType getResult(aprod(\associativity(AType def, AAssociativity \assoc, set[AProduction] alternatives))) = getResult(aprod(getFirstFrom(alternatives)));
AType getResult(overloadedAType(rel[loc, IdRole, AType] overloads)) = (avoid() | alub(it, getResult(tp) )| tp <- overloads<2>);

default AType getResult(AType t){
    throw rascalCheckerInternalError("Can only get result type from function or constructor type, got <prettyAType(t)>");
}

@doc{
.Synopsis
Determine if the given type is a reified type.
}
bool isReifiedType(aparameter(_,AType tvb)) = isReifiedType(tvb);
bool isReifiedType(areified(_)) = true;
default bool isReifiedType(AType _) = false;

@doc{Create a type representing the reified form of the given type.}
AType makeReifiedType(AType mainType) = areified(mainType);

@doc{Get the type that has been reified and stored in the reified type.}
AType getReifiedType(AType t) {
    if (areified(rt) := unwrapType(t)) return rt;
    throw rascalCheckerInternalError("getReifiedType given unexpected type: <prettyAType(t)>");
}

@doc{
.Synopsis
Determine if the given type is an type variable (parameter).
}
bool isRascalTypeParam(aparameter(_,_)) = true;
default bool isRascalTypeParam(AType _) = false;

@doc{Create a type representing a type parameter (type variable).}
AType makeTypeVar(str varName) = aparameter(varName, avalue());

@doc{Create a type representing a type parameter (type variable) and bound.}
AType makeTypeVarWithBound(str varName, AType varBound) = aparameter(varName, varBound);

@doc{Get the name of a Rascal type parameter.}
str getRascalTypeParamName(AType t) {
    if (aparameter(tvn,_) := t) return tvn;
    throw rascalCheckerInternalError("getRascalTypeParamName given unexpected type: <prettyAType(t)>");
}

@doc{Get the bound of a type parameter.}
AType getRascalTypeParamBound(AType t) {
    if (aparameter(_,tvb) := t) return tvb;
    throw rascalCheckerInternalError("getRascalTypeParamBound given unexpected type: <prettyAType(t)>");
}

@doc{Get all the type parameters inside a given type.}
set[AType] collectRascalTypeParams(AType t) {
    
    return { rt | / AType rt : aparameter(_,_) := t };
    //return { unset(rt, "alabel") | / AType rt : aparameter(_,_) := t }; // TODO: "alabel" is unset to enable subset check later, reconsider
}

@doc{Get all the type parameters inside a given type.}
set[AType] collectAndUnlabelRascalTypeParams(AType t) {
   return { unset(rt, "alabel") | / AType rt : aparameter(_,_) := t }; // TODO: "alabel" is unset to enable subset check later, reconsider
}

@doc{Get all the type parameters inside a given set of productions.}
set[AType] collectAndUnlabelRascalTypeParams(set[AProduction] prods) {
   return { unset(rt, "alabel") | / AType rt : aparameter(_,_) := prods }; // TODO: "alabel" is unset to enable subset check later, reconsider
}

@doc{Provide an initial type map from the type parameters in the type to void.}
map[str,AType] initializeRascalTypeParamMap(AType t) {
    set[AType] rt = collectRascalTypeParams(t);
    return ( getRascalTypeParamName(tv) : makeVoidType() | tv <- rt );
}

@doc{See if a type contains any type parameters.}
bool typeContainsRascalTypeParams(AType t) = size(collectRascalTypeParams(t)) > 0;

@doc{Return the names of all type variables in the given type.}
set[str] typeParamNames(AType t) {
    return { tvn | aparameter(tvn,_) <- collectRascalTypeParams(t) };
}

// ---- element & container types

@doc{Is this type a non-container type?}
bool isElementType(AType t) = 
    isIntType(t) || isBoolType(t) || isRealType(t) || isRatType(t) || isStrType(t) || 
    isNumType(t) || isNodeType(t) || isVoidType(t) || isValueType(t) || isLocType(t) || 
    isDateTimeType(t) || isTupleType(t) || isADTType(t) || isConstructorType(t) ||
    isFunctionType(t) || isReifiedType(t) || isNonTerminalType(t);

@doc{Is this type a container type?}
bool isContainerType(AType t) =
    isSetType(t) || isListType(t) || isMapType(t) || isBagType(t);
    
bool isEnumeratorType(AType t) =
    isSetType(t) || isListType(t) || isMapType(t) || isADTType(t) || isTupleType(t) || isNodeType(t) ||
    isIterType(t) || isOptType(t);
    
AType getEnumeratorElementType(AType t) = getListElementType(t) when isListType(t);
AType getEnumeratorElementType(AType t) = getSetElementType(t) when isSetType(t);
AType getEnumeratorElementType(AType t) = getMapDomainType(t) when isMapType(t);
AType getEnumeratorElementType(AType t) = avalue() when isADTType(t) || isTupleType(t) || isNodeType(t);
AType getEnumeratorElementType(AType t) = getIterElementType(t) when isIterType(t);
AType getEnumeratorElementType(AType t) = getOptType(t) when isOptType(t);
default AType getEnumeratorElementType(AType t) = avalue();


// All elements of a syntax rule (see Sym in Rascal grammar)

// ---- named nonterminals

@doc{Synopsis: Determine if the given type is a nonterminal.}
bool isNonTerminalType(aparameter(_,AType tvb)) = isNonTerminalType(tvb);
bool isNonTerminalType(AType::\conditional(AType ss,_)) = isNonTerminalType(ss);
bool isNonTerminalType(t:aadt(adtName,_,SyntaxRole sr)) = isConcreteSyntaxRole(sr); // || adtName == "Tree";
bool isNonTerminalType(acons(AType adt, list[AType] fields, list[Keyword] kwFields)) = isNonTerminalType(adt);
bool isNonTerminalType(AType::\start(AType ss)) = isNonTerminalType(ss);
bool isNonTerminalType(AType::aprod(AProduction p)) = isNonTerminalType(p.def);

bool isNonTerminalType(AType::\iter(AType t)) = isNonTerminalType(t);
bool isNonTerminalType(AType::\iter-star(AType t)) = isNonTerminalType(t);
bool isNonTerminalType(AType::\iter-seps(AType t,_)) = isNonTerminalType(t);
bool isNonTerminalType(AType::\iter-star-seps(AType t,_)) = isNonTerminalType(t);
bool isNonTerminalType(AType::\aempty()) = false;
bool isNonTerminalType(AType::\opt(AType t)) = isNonTerminalType(t);
//bool isNonTerminalType(AType::\alt(set[AType] alternatives)) = any(a <- alternatives, isNonTerminalType(a));
//bool isNonTerminalType(AType::\seq(list[AType] atypes)) = any(t <- atypes, isNonTerminalType(t));
default bool isNonTerminalType(AType t) = false;   

bool isParameterizedNonTerminalType(AType t) = isNonTerminalType(t) && t has parameters;

bool isNonParameterizedNonTerminalType(AType t) = isNonTerminalType(t) && (t has parameters ==> isEmpty(t.parameters));

// start
bool isStartNonTerminalType(aparameter(_,AType tvb)) = isStartNonTerminalType(tvb);
bool isStartNonTerminalType(AType::\start(_)) = true;
default bool isStartNonTerminalType(AType s) = false;    

AType getStartNonTerminalType(aparameter(_,AType tvb)) = getStartNonTerminalType(tvb);
AType getStartNonTerminalType(AType::\start(AType s)) = s;
default AType getStartNonTerminalType(AType s) {
    throw rascalCheckerInternalError("<prettyAType(s)> is not a start non-terminal type");
}

//TODO labelled

bool isLexicalType(aparameter(_,AType tvb)) = isLexicalType(tvb);
bool isLexicalType(AType::\conditional(AType ss,_)) = isLexicalType(ss);
bool isLexicalType(t:aadt(adtName,_,SyntaxRole sr)) = sr == lexicalSyntax(); // || adtName == "Tree";
bool isLexicalType(acons(AType adt, list[AType] fields, list[Keyword] kwFields)) = isLexicalType(adt);
bool isLexicalType(AType::\start(AType ss)) = isLexicalType(ss);

bool isLexicalType(AType:alit(str string)) = true;
bool isLexicalType(AType:acilit(str string)) = true;
bool isLexicalType(AType:\achar-class(list[ACharRange] ranges)) = true;
   
bool isLexicalType(AType::\iter(AType s)) = isLexicalType(s);
bool isLexicalType(AType::\iter-star(AType s)) = isLexicalType(s);
bool isLexicalType(AType::\iter-seps(AType s,_)) = isLexicalType(s);
bool isLexicalType(AType::\iter-star-seps(AType s,_)) = isLexicalType(s);

bool isLexicalType(seq(list[AType] symbols)) = all(s <- symbols, isLexicalType(s));
bool isLexicalType(alt(set[AType] alternatives)) = any(s <- alternatives, isLexicalType(s));
bool isLexicalType(AType t) = false;

// ---- literal/terminal
@doc{Synopsis: Determine if the given type is a terminal symbol (a literal or character class).}
bool isTerminalType(aparameter(_,AType tvb)) = isTerminalType(tvb);
bool isTerminalType(AType::\conditional(AType ss,_)) = isTerminalType(ss);
bool isTerminalType(alit(_)) = true;
bool isTerminalType(acilit(_)) = true;
bool isTerminalType(\achar-class(_)) = true;
default bool isTerminalType(AType _) = false;

// char-class

bool isCharClass(\achar-class(list[ACharRange] ranges)) = true;
default bool isCharClass(AType tp) = false;

bool isAnyCharType(aparameter(_,AType tvb)) = isAnyCharType(tvb);
default bool isAnyCharType(AType t) = t == anyCharType;

// ---- layout
@doc{Synopsis: Determine if the given type is a layout type.}
bool isLayoutType(aparameter(_,AType tvb)) = isLayoutType(tvb);

bool isLayoutType(AType::\conditional(AType ss,_)) = isLayoutType(ss);
bool isLayoutType(t:aadt(adtName,_,SyntaxRole sr)) = sr == layoutSyntax();
bool isLayoutType(AType::\start(AType ss)) = isLayoutType(ss);
bool isLayoutType(AType::\iter(AType s)) = isLayoutType(s);
bool isLayoutType(AType::\iter-star(AType s)) = isLayoutType(s);
bool isLayoutType(AType::\iter-seps(AType s,_)) = isLayoutType(s);
bool isLayoutType(AType::\iter-star-seps(AType s,_)) = isLayoutType(s);

bool isLayoutType(AType::\opt(AType s)) = isLayoutType(s);
bool isLayoutType(AType::\alt(set[AType] alts)) = any(a <- alts, isLayoutType(a));
bool isLayoutType(AType::\seq(list[AType] symbols)) = all(s <- symbols, isLayoutType(s));
default bool isLayoutType(AType _) = false;

// ---- isConcretePattern

bool isConcretePattern(Pattern p, AType tp) {
    return isNonTerminalType(tp) && !(p is callOrTree) /*&& Symbol::sort(_) := tp*/;
} 

// -- isSyntaxType

bool isSyntaxType(AType tp) {
    res = isTerminalType(tp) || isNonTerminalType(tp) || isRegExpType(tp);
    return res;
}

// ---- Regular expressions 

bool isRegExpType(AType tp) 
    = isEmpty(tp) || isIterType(tp) || isOptType(tp) || isAltType(tp) || isSeqType(tp);

bool isIterType(aparameter(_,AType tvb)) = isIterType(tvb);
bool isIterType(AType::\iter(_)) = true;
bool isIterType(AType::\iter-star(_)) = true;
bool isIterType(AType::\iter-seps(_,_)) = true;
bool isIterType(AType::\iter-star-seps(_,_)) = true;
bool isIterType(AType::\opt(_)) = true;
default bool isIterType(AType _) = false;    

AType getIterElementType(aparameter(_,AType tvb)) = getIterElementType(tvb);
AType getIterElementType(AType::\iter(AType i)) = i;
AType getIterElementType(AType::\iter-star(AType i)) = i;
AType getIterElementType(AType::\iter-seps(AType i,_)) = i;
AType getIterElementType(AType::\iter-star-seps(AType i,_)) = i;
AType getIterElementType(AType::\opt(AType i)) = i;
default AType getIterElementType(AType i) {
    throw rascalCheckerInternalError("<prettyAType(i)> is not an iterable non-terminal type");
}
 
int getIterOrOptDelta(aparameter(_,AType tvb)) = getIterOrOptDelta(tvb);
int getIterOrOptDelta(AType::\iter(AType i)) = isLexicalType(i) ? 1 : 2;
int getIterOrOptDelta(AType::\iter-star(AType i)) = isLexicalType(i) ? 1 : 2;
int getIterOrOptDelta(AType::\iter-seps(AType i,_)) = isLexicalType(i) ? 2 : 4;
int getIterOrOptDelta(AType::\iter-star-seps(AType i,_)) = isLexicalType(i) ? 2 : 4;
int getIterOrOptDelta(AType::\opt(AType i)) = 1;
default int getIterOrOptDelta(AType i) {
    throw rascalCheckerInternalError("<prettyAType(i)> is not an iterable non-terminal type or optional");
}

// empty

bool isEmpty(AType::aempty()) = true;
default bool isEmpty(AType tp) = false;

// opt
bool isOptType(aparameter(_,AType tvb)) = isOptType(tvb);
bool isOptType(AType::\opt(AType ot)) = true;
default bool isOptType(AType _) = false;

AType getOptType(aparameter(_,AType tvb)) = getOptType(tvb);
AType getOptType(AType::\opt(AType ot)) = ot;
default AType getOptType(AType ot) {
    throw rascalCheckerInternalError("<prettyAType(ot)> is not an optional non-terminal type");
}

// alt
bool isAltType(aparameter(_,AType tvb)) = isAltType(tvb);
bool isAltType(AType:\alt(_)) = true;
default bool isAltType(AType _) = false;

set[AType] getAltTypes(aparameter(_,AType tvb)) = getAltTypes(tvb);
set[AType] getAltTypes(alt(set[AType] atypes)) = atypes;
default set[AType] getAltTypes(AType t){
    throw rascalCheckerInternalError("<prettyAType(t)> is not a alt non-terminal type");
}

// seq
bool isSeqType(aparameter(_,AType tvb)) = isSeqType(tvb);
bool isSeqType(AType:\seq(_)) = true;
default bool isSeqType(AType _) = false;

list[AType] getSeqTypes(aparameter(_,AType tvb)) = getSeqTypes(tvb);
list[AType] getSeqTypes(seq(list[AType] atypes)) = atypes;
default list[AType] getSeqTypes(AType t){
    throw rascalCheckerInternalError("<prettyAType(t)> is not a seq non-terminal type");
}



AType getSyntaxType(AType t, Solver _) = stripStart(removeConditional(t));

AType getSyntaxType(Tree tree, Solver s) = stripStart(removeConditional(s.getType(tree)));

AType stripStart(AType nt) = isStartNonTerminalType(nt) ? getStartNonTerminalType(nt) : nt;

AType stripStart(aprod(AProduction production)) = production.def;

AType removeConditional(cnd:conditional(AType s, set[ACondition] _)) = cnd.alabel? ? s[alabel=cnd.alabel] : s;
default AType removeConditional(AType s) = s;

//@doc{Determine the size of a concrete list}
//int size(appl(regular(\iter(Symbol symbol)), list[Tree] args)) = size(args);
//int size(appl(regular(\iter-star(Symbol symbol)), list[Tree] args)) = size(args);
//
//int size(appl(regular(\iter-seps(Symbol symbol, list[Symbol] separators)), list[Tree] args)) = size_with_seps(size(args), size(separators));
//int size(appl(regular(\iter-star-seps(Symbol symbol, list[Symbol] separators)), list[Tree] args)) = size_with_seps(size(args), size(separators));
//
//int size(appl(prod(Symbol symbol, list[Symbol] symbols, set[AAttr] attributes), list[Tree] args)) = 
//    \label(str _, Symbol symbol1) := symbol && [Symbol _] := symbols
//    ? size(appl(Production::prod(symbol1, symbols, {}), args))
//    : size(args[0]);
//
//default int size(Tree t) {
//    iprintln(t);
//    throw "Size of tree not defined for \"<t>\"";
//}
//
//private int size_with_seps(int len, int lenseps) = (len == 0) ? 0 : 1 + (len / (lenseps + 1));