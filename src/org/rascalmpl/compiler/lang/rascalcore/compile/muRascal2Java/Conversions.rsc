module lang::rascalcore::compile::muRascal2Java::Conversions

import Node;
import String;
import Map;
import Set;

extend lang::rascalcore::check::CheckerCommon;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::compile::util::Names;
import lang::rascalcore::compile::muRascal2Java::JGenie;

data JGenie; // hack ot break cycle?

/*****************************************************************************/
/*  Convert AType to a Java type                                             */
/*****************************************************************************/

str atype2javatype(abool())                 = "IBool";
str atype2javatype(aint())                  = "IInteger";
str atype2javatype(areal())                 = "IReal";
str atype2javatype(arat())                  = "IRational";
str atype2javatype(anum())                  = "INumber";
str atype2javatype(astr())                  = "IString";
str atype2javatype(aloc())                  = "ISourceLocation";
str atype2javatype(adatetime())             = "IDateTime";
str atype2javatype(alist(AType t))          = "IList";
str atype2javatype(aset(AType t))           = "ISet";
str atype2javatype(arel(AType ts))          = "ISet";
str atype2javatype(alrel(AType ts))         = "IList";
str atype2javatype(atuple(AType ts))        = "ITuple";
str atype2javatype(amap(AType d, AType r))  = "IMap";

str atype2javatype(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
                                            = "TypedFunctionInstance0\<<"IValue"/*atype2javatype(ret)*/>\>"
                                              when isEmpty(formals);
str atype2javatype(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
                                            = "TypedFunctionInstance<size(formals)>\<<"IValue"/*<atype2javatype(ret)*/>, <intercalate(", ", ["IValue"/*atype2javatype(f)*/ | f <- formals])>\>"
                                              when !isEmpty(formals);
 
str atype2javatype(anode(list[AType fieldType] fields)) 
                                            = "INode";
str atype2javatype(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) 
                                            = "IConstructor";

str atype2javatype(t: acons(AType adt, list[AType fieldType] fields, lrel[AType fieldType, Expression defaultExp] kwFields))
                                            = "IConstructor";
                 
str atype2javatype(aparameter(str pname, AType bound)) 
                                            = atype2javatype(bound);
str atype2javatype(areified(AType atype))   = "IConstructor";

str atype2javatype(avalue())                = "IValue";
str atype2javatype(avoid())                 = "void";

str atype2javatype(tvar(_))                 = "IValue";

str atype2javatype(\start(AType t))         = atype2javatype(t);

str atype2javatype(overloadedAType(rel[loc, IdRole, AType] overloads))
    = atype2javatype(lubList(toList(overloads<2>)));


default str atype2javatype(AType t) = "IConstructor"; // This covers all parse tree related constructors

/*****************************************************************************/
/*  Convert AType to a descriptor that can be used in a Java identifier      */
/*****************************************************************************/

str atype2idpart(avoid())                 = "void";
str atype2idpart(abool())                 = "bool";
str atype2idpart(aint())                  = "int";
str atype2idpart(areal())                 = "real";
str atype2idpart(arat())                  = "rat";
str atype2idpart(anum())                  = "num";
str atype2idpart(astr())                  = "str";
str atype2idpart(aloc())                  = "loc";
str atype2idpart(adatetime())             = "datetime";
str atype2idpart(alist(AType t))          = "list_<atype2idpart(t)>";
str atype2idpart(aset(AType t))           = "set_<atype2idpart(t)>";
str atype2idpart(arel(AType ts))          = "rel_<atype2idpart(ts)>";
str atype2idpart(alrel(AType ts))         = "listrel_<atype2idpart(ts)>";
str atype2idpart(atuple(AType ts))        = "tuple_<atype2idpart(ts)>";
str atype2idpart(amap(AType d, AType r))  = "map_<atype2idpart(d)>_<atype2idpart(r)>";

str atype2idpart(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
                                          = "<atype2idpart(ret)>_<intercalate("_", [atype2idpart(f) | f <- formals])>";

str atype2idpart(anode(list[AType fieldType] fields))
                                          = "node";

str atype2idpart(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) 
                                          = getJavaName(adtName);
                                              
str atype2idpart(t:acons(AType adt, list[AType fieldType] fields, lrel[AType fieldType, Expression defaultExp] kwFields))
                                          = "<getJavaName(adt.adtName, completeId=false)><t.label? ? "_" + getJavaName(getUnqualifiedName(t.label), completeId=false) : "">_<intercalate("_", [atype2idpart(f) | f <- fields])>";

str atype2idpart(overloadedAType(rel[loc, IdRole, AType] overloads)){
    resType = avoid();
    formalsType = avoid();
    for(<def, idrole, tp> <- overloads){
        resType = alub(resType, getResult(tp));
        formalsType = alub(formalsType, atypeList(getFormals(tp)));
    }
    ftype = atypeList(atypes) := formalsType ? afunc(resType, formalsType.atypes, []) : afunc(resType, [formalsType], []);
    return atype2idpart(ftype);
}

str atype2idpart(atypeList(list[AType] ts)) 
                                          = intercalate("_", [atype2idpart(t) | t <- ts]);

str atype2idpart(aparameter(str pname, AType bound)) 
                                          = "P<avalue() := bound ? "" : atype2idpart(bound)>"; 
str atype2idpart(areified(AType atype))   = "reified_<atype2idpart(atype)>";
str atype2idpart(avalue())                = "value";

str atype2idpart(\empty())                = "empty";
str atype2idpart(\opt(AType atype))       = "opt_<atype2idpart(atype)>";
str atype2idpart(\iter(AType atype))      = "iter_<atype2idpart(atype)>"; 
str atype2idpart(\iter-star(AType atype)) = "iter_star_<atype2idpart(atype)>"; 
str atype2idpart(\iter-seps(AType atype, list[AType] separators))
                                          = "iter_seps_<atype2idpart(atype)>"; 
str atype2idpart(\iter-star-seps(AType atype, list[AType] separators))
                                          = "iter_start_seps_<atype2idpart(atype)>"; 
str atype2idpart(\alt(set[AType] alternatives))
                                          = "alt_"; //TODO
str atype2idpart(\seq(list[AType] atypes))= "seq_";  //TODO
str atype2idpart(\start(AType atype))        = "start_<atype2idpart(atype)>";

default str atype2idpart(AType t) { throw "atype2idpart: cannot handle <t>"; }

/*****************************************************************************/
/*  Convert an AType to an IValue (i.e., reify the AType)                    */
/*****************************************************************************/

str lab(AType t) = t.label? ? value2IValue(t.label) : "";
str lab2(AType t) = t.label? ? ", <value2IValue(t.label)>" : "";

bool isBalanced(str s){
    pars = 0;
    brackets = 0;
    curlies = 0;
    for(int i <- [0..size(s)]){
        switch(s[i]){
            case "(": pars += 1;
            case ")": { if(pars == 0) return false; pars -= 1; }
            
            case "[": brackets += 1;
            case "]": { if(brackets == 0) return false; brackets -= 1; }
            
            case "{": curlies += 1;
            case "}": { if(curlies == 0) return false; curlies -= 1; }
        }
    }
    return pars == 0 && brackets == 0 && curlies == 0;
}

// Wrapper thats checks for balanced output
str atype2IValue(AType t,  map[AType, set[AType]] defs){
    res = atype2IValue1(t, defs);
    if(!isBalanced(res)) throw "atype2IValue: unbalanced, <t>, <res>";
    return res; 
}

str lbl(AType at) = at.label? ? "(\"<at.label>\"" : "(";

str atype2IValue1(at:avoid(), _)              = "$avoid<lbl(at)>)";
str atype2IValue1(at:abool(), _)              = "$abool<lbl(at)>)";
str atype2IValue1(at:aint(), _)               = "$aint<lbl(at)>)";
str atype2IValue1(at:areal(), _)              = "$areal<lbl(at)>)";
str atype2IValue1(at:arat(), _)               = "$arat<lbl(at)>)";
str atype2IValue1(at:anum(), _)               = "$anum<lbl(at)>)";
str atype2IValue1(at:astr(), _)               = "$astr<lbl(at)>)";
str atype2IValue1(at:aloc(), _)               = "$aloc<lbl(at)>)";
str atype2IValue1(at:adatetime(), _)          = "$adatetime<lbl(at)>)";

// TODO handle cases with label
str atype2IValue1(at:alist(AType t), map[AType, set[AType]] defs)          
    = "$alist(<atype2IValue(t, defs)><lab2(at)>)";
str atype2IValue1(at:abag(AType t), map[AType, set[AType]] defs)           
    = "$abag(<atype2IValue(t, defs)><lab2(at)>)";
str atype2IValue1(at:aset(AType t), map[AType, set[AType]] defs)           
    = "$aset(<atype2IValue(t, defs)><lab2(at)>)";
str atype2IValue1(at:arel(AType ts), map[AType, set[AType]] defs)          
    = "$arel(<atype2IValue(ts, defs)><lab2(at)>)";
str atype2IValue1(at:alrel(AType ts), map[AType, set[AType]] defs)         
    = "$alrel(<atype2IValue(ts, defs)><lab2(at)>)";

str atype2IValue1(at:atuple(AType ts), map[AType, set[AType]] defs)        
    = "$atuple(<atype2IValue(ts, defs)><lab2(at)>)";
str atype2IValue1(at:amap(AType d, AType r), map[AType, set[AType]] defs)  
    = "$amap(<atype2IValue(d, defs)>,<atype2IValue(r, defs)><lab2(at)>)"; // TODO: complete from here

str atype2IValue1(at:afunc(AType ret, list[AType] formals, list[Keyword] kwFormals), map[AType, set[AType]] defs)
    = "<atype2IValue(ret, defs)>_<intercalate("_", [atype2IValue(f,defs) | f <- formals])>";
str atype2IValue1(at:anode(list[AType fieldType] fields), map[AType, set[AType]] defs) 
    = "$anode(<lab(at)>)";
str atype2IValue1(at:aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole), map[AType, set[AType]] defs)
    = "$aadt(<value2IValue(adtName)>, <atype2IValue(parameters,defs)>, <getName(syntaxRole)>)";
str atype2IValue1(at:acons(AType adt, list[AType fieldType] fields, lrel[AType fieldType, Expression defaultExp] kwFields), map[AType, set[AType]] defs)
    = "$acons(<atype2IValue(adt, defs)>, <atype2IValue(fields, defs)>, <atype2IValue(kwFields,defs)><lab2(at)>)";
str atype2IValue1(overloadedAType(rel[loc, IdRole, AType] overloads), map[AType, set[AType]] defs){
    resType = avoid();
    formalsType = avoid();
    for(<def, idrole, tp> <- overloads){
        resType = alub(resType, getResult(tp));
        formalsType = alub(formalsType, atypeList(getFormals(tp)));
    }
    ftype = atypeList(atypes) := formalsType ? afunc(resType, formalsType.atypes, []) : afunc(resType, [formalsType], []);
    return atype2IValue(ftype, defs);
}

str atype2IValue1(at:aparameter(str pname, AType bound), map[AType, set[AType]] defs)
    = "$aparameter(<atype2IValue(bound,defs)>)"; 
str atype2IValue1(at:aprod(AProduction production), map[AType, set[AType]] defs) 
    = "$aprod(<tree2IValue(production, defs)>)";
str atype2IValue1(at:areified(AType atype), map[AType, set[AType]] definitions) 
    = "$reifiedAType((IConstructor) <atype2IValue(atype, definitions)>, <defs(definitions)>)";
str atype2IValue1(at:avalue(), _)               
     = "$avalue(<lab(at)>)";
//default str atype2IValue1(AType t, map[AType, set[AType]] defs) { throw "atype2IValue1: cannot handle <t>"; }

str atype2IValue(list[AType] ts, map[AType, set[AType]] defs) 
    = "$VF.list(<intercalate(", ", [atype2IValue(t,defs) | t <- ts])>)";
str atype2IValue(lrel[AType fieldType,Expression defaultExp] ts, map[AType, set[AType]] defs) 
    = "$VF.list(<intercalate(", ", [atype2IValue(t.fieldType,defs) | t <- ts])>)";

str atype2IValue(set[AType] ts, map[AType, set[AType]] defs) 
    = "$VF.set(<intercalate(", ", [atype2IValue(t,defs) | t <- ts])>)";

str atype2IValue(atypeList(list[AType] atypes), map[AType, set[AType]] defs) 
    = "$VF.list(<intercalate(", ", [atype2IValue(t,defs) | t <- atypes])>)";
    
str defs(map[AType, set[AType]] defs) {
    res = "$buildMap(<intercalate(", ", ["<atype2IValue(k,defs)>, $VF.set(<intercalate(", ", [ atype2IValue(elem,defs) | elem <- defs[k] ])>)" | k <- defs ])>)";
    return res;
}

/*****************************************************************************/
/*  Convert a Tree to an IValue                                              */
/*****************************************************************************/

// Wrappers that check for balanced output
str tree2IValue(Associativity t,  map[AType, set[AType]] defs){
    res = tree2IValue1(t, defs);
    if(!isBalanced(res)) throw "tree2IValue1: unbalanced, <t>, <res>";
    return res; 
}

str tree2IValue(Attr t,  map[AType, set[AType]] defs){
    res = tree2IValue1(t, defs);
    if(!isBalanced(res)) throw "tree2IValue1: unbalanced, <t>, <res>";
    return res; 
}

str tree2IValue(Tree t,  map[AType, set[AType]] defs){
    res = tree2IValue1(t, defs);
    if(!isBalanced(res)) throw "tree2IValue1: unbalanced, <t>, <res>";
    return res; 
}

str tree2IValue(AProduction t,  map[AType, set[AType]] defs){
    res = tree2IValue1(t, defs);
    if(!isBalanced(res)) throw "tree2IValue1: unbalanced, <t>, <res>";
    return res; 
}

str tree2IValue(ACharRange t,  map[AType, set[AType]] defs){
    res = tree2IValue1(t, defs);
    if(!isBalanced(res)) throw "tree2IValue1: unbalanced, <t>, <res>";
    return res; 
}

str tree2IValue(ACharRange t,  map[AType, set[AType]] defs){
    res = tree2IValue1(t, defs);
    if(!isBalanced(res)) throw "tree2IValue1: unbalanced, <t>, <res>";
    return res; 
}

// ---- Associativity ---------------------------------------------------------

str tree2IValue1(\left(), map[AType, set[AType]] defs) = "left()";
str tree2IValue1(\right(), map[AType, set[AType]] defs) = "right()";
str tree2IValue1(\assoc(), map[AType, set[AType]] defs) = "assoc()";
str tree2IValue1(\non-assoc(), map[AType, set[AType]] defs) = "non_assoc()";

// ---- Attr ------------------------------------------------------------------

str tree2IValue1(\tag(value v),  map[AType, set[AType]] defs) 
    = "tag(<value2IValue(v)>)";
str tree2IValue1(\assoc(Associativity \assoc),  map[AType, set[AType]] defs) 
    = "assoc(<tree2IValue(\assoc, defs)>)";
str tree2IValue1(\bracket(),  map[AType, set[AType]] defs)
    = "bracket())";

// ---- Tree ------------------------------------------------------------------

str tree2IValue1(tr:appl(AProduction aprod, list[Tree] args), map[AType, set[AType]] defs)
    = tr.src? ? "appl(<tree2IValue(aprod, defs)>, <tree2IValue(args, defs)>, <value2IValue(tr.src)>)"
              : "appl(<tree2IValue(aprod, defs)>, <tree2IValue(args, defs)>)";

str tree2IValue1(cycle(AType asymbol, int cycleLength), map[AType, set[AType]] defs)
    = "cycle(<atype2IValue(asymbol, defs)>, <value2IValue(cycleLength)>)";

str tree2IValue1(amb(set[Tree] alternatives), map[AType, set[AType]] defs)
    = "amb(<tree2IValue(alternatives,defs)>)";
 
str tree2IValue1(char(int character), map[AType, set[AType]] defs)
    = "tchar(<value2IValue(character)>)";
    
// ---- SyntaxRole ------------------------------------------------------------

str tree2IValue1(SyntaxRole sr, map[AType, set[AType]] defs) = "<sr>";
   
// ---- AProduction -----------------------------------------------------------

str tree2IValue1(\choice(AType def, set[AProduction] alternatives), map[AType, set[AType]] defs)
    = "choice(<atype2IValue(def, defs)>, <tree2IValue(alternatives, defs)>)";

str tree2IValue1(tr:prod(AType def, list[AType] asymbols), map[AType, set[AType]] defs){
    base = "prod(<atype2IValue(def, defs)>, <atype2IValue(asymbols, defs)>";
    kwds = tr.attributes? ? ", <tree2IValue(tr.attributes, defs)>" : "";
    if(tr.src?) kwds += ", <value2IValue(tr.src)>";
    return base + kwds + ")";
}

str tree2IValue1(regular(AType def), map[AType, set[AType]] defs)
    = "regular(<atype2IValue(def, defs)>)";

str tree2IValue1(error(AProduction prod, int dot), map[AType, set[AType]] defs)
    = "error(<tree2IValue(prod, defs)>, <value2IValue(dot)>)";

str tree2IValue1(skipped(), map[AType, set[AType]] defs)
    = "skipped()";
    
str tree2IValue1(\priority(AType def, list[AProduction] choices), map[AType, set[AType]] defs)
    = "priority(<atype2IValue(def, defs)>, <tree2IValue(choices, defs)>)";
    
str tree2IValue1(\associativity(AType def, Associativity \assoc, set[AProduction] alternatives), map[AType, set[AType]] defs)
    = "associativity(<atype2IValue(def, defs)>, <tree2IValue(\assoc, defs)>, <tree2IValue(alternatives, defs)>)";

str tree2IValue1(\others(AType def) , map[AType, set[AType]] defs)
    = "others(<atype2IValue(def, defs)>)";

str tree2IValue1(\reference(AType def, str cons), map[AType, set[AType]] defs)
    = "reference(<atype2IValue(def, defs)>, <value2IValue(cons)>)";
    
// ---- ACharRange ------------------------------------------------------------
str tree2IValue1(range(int begin, int end), map[AType, set[AType]] defs)
    = "range(<value2IValue(begin)>, <value2IValue(end)>)";
    
// ---- AType extensions for parse trees --------------------------------------
str atype2IValue1(AType::lit(str string), map[AType, set[AType]] defs)
    = "lit(<value2IValue(string)>)";

str atype2IValue1(AType::cilit(str string), map[AType, set[AType]] defs)
    = "cilit(<value2IValue(string)>)";

str atype2IValue1(AType::\char-class(list[ACharRange] ranges), map[AType, set[AType]] defs)
    = "char_class(<tree2IValue(ranges, defs)>)";    
 
str atype2IValue1(AType::\empty(), map[AType, set[AType]] defs)
    = "empty()";     

str atype2IValue1(AType::\opt(AType symbol), map[AType, set[AType]] defs)
    = "opt(<atype2IValue(symbol, defs)>)";     

str atype2IValue1(AType::\iter(AType symbol), map[AType, set[AType]] defs)
    = "iter(<atype2IValue(symbol, defs)>)";     

str atype2IValue1(AType::\iter-star(AType symbol), map[AType, set[AType]] defs)
    = "iter_star(<atype2IValue(symbol, defs)>)";   

str atype2IValue1(AType::\iter-seps(AType symbol, list[AType] separators), map[AType, set[AType]] defs)
    = "iter_seps(<atype2IValue(symbol, defs)>, <atype2IValue(separators, defs)>)";     
 
str atype2IValue1(AType::\iter-star-seps(AType symbol, list[AType] separators), map[AType, set[AType]] defs)
    = "iter_star_seps(<atype2IValue(symbol, defs)>, <atype2IValue(separators, defs)>)";   
    
str atype2IValue1(AType::\alt(set[AType] alternatives) , map[AType, set[AType]] defs)
    = "alt(<atype2IValue(alternatives, defs)>)";     

str atype2IValue1(AType::\seq(list[AType] symbols) , map[AType, set[AType]] defs)
    = "seq(<atype2IValue(symbols, defs)>)";     
 
str atype2IValue1(AType::\start(AType symbol), map[AType, set[AType]] defs)
    = "start(<atype2IValue(symbol, defs)>)";   

str atype2IValue1(AType::\conditional(AType symbol, set[ACondition] conditions), map[AType, set[AType]] defs)
    = "conditional(<atype2IValue(symbol, defs)>, <tree2IValue(conditions, defs)>)";   
    
// ---- ACondition ------------------------------------------------------------

str tree2IValue1(\follow(AType atype), map[AType, set[AType]] defs)
    = "follow(<atype2IValue(atype, defs)>)";   

str tree2IValue1(\not-follow(AType atype), map[AType, set[AType]] defs)
    = "not_follow(<atype2IValue(atype, defs)>)";
    
str tree2IValue1(\precede(AType atype), map[AType, set[AType]] defs)
    = "precede(<atype2IValue(atype, defs)>)";  

str tree2IValue1(\not-precede(AType atype), map[AType, set[AType]] defs)
    = "not_precede(<atype2IValue(atype, defs)>)"; 
    
str tree2IValue1(\delete(AType atype), map[AType, set[AType]] defs)
    = "delete(<atype2IValue(atype, defs)>)"; 
    
str tree2IValue1(\at-column(int column), map[AType, set[AType]] defs)
    = "at_column(<value2IValue(column)>)";  
    
str tree2IValue1(\begin-of-line(), map[AType, set[AType]] defs)
    = "begin_of_line()";
    
str tree2IValue1(\end-of-line(), map[AType, set[AType]] defs)
    = "end_of_line()"; 
    
str tree2IValue1(\except(str label), map[AType, set[AType]] defs)
    = "except(<value2IValue(label)>)";              
                  
//---- list/set wrappers for some parse tree constructs

str tree2IValue(list[Tree] trees, map[AType, set[AType]] defs)
    = "$VF.list(<intercalate(", ", [ tree2IValue(tr, defs) | tr <- trees ])>)";
    
str tree2IValue(set[Tree] trees, map[AType, set[AType]] defs)
    = "$VF.set(<intercalate(", ", [ tree2IValue(tr, defs) | tr <- trees ])>)";
    
str tree2IValue(set[AProduction] prods, map[AType, set[AType]] defs)
    = "$VF.set(<intercalate(", ", [ tree2IValue(pr, defs) | pr <- prods ])>)";    
    
str tree2IValue(set[Attr] attrs, map[AType, set[AType]] defs)
    = "$VF.set(<intercalate(", ", [ tree2IValue(a, defs) | a <- attrs ])>)";   
    
str tree2IValue(set[ACondition] conds, map[AType, set[AType]] defs)
    = "$VF.set(<intercalate(", ", [ tree2IValue(c, defs) | c <- conds ])>)"; 
    
str tree2IValue(list[ACharRange] ranges, map[AType, set[AType]] defs)
    = "$VF.list(<intercalate(", ", [ tree2IValue(r, defs) | r <- ranges ])>)"; 

/*****************************************************************************/
/*  Get the outermost type of an AType (used for names of primitives)        */
/*****************************************************************************/

str getOuter(avoid())                 = "avoid";
str getOuter(abool())                 = "abool";
str getOuter(aint())                  = "aint";
str getOuter(areal())                 = "areal";
str getOuter(arat())                  = "arat";
str getOuter(anum())                  = "anum";
str getOuter(astr())                  = "astr";
str getOuter(aloc())                  = "aloc";
str getOuter(adatetime())             = "adatetime";
str getOuter(alist(AType t))          = "alist";
str getOuter(aset(AType t))           = "aset";
str getOuter(arel(AType ts))          = "aset";
str getOuter(alrel(AType ts))         = "alist";
str getOuter(atuple(AType ts))        = "atuple";
str getOuter(amap(AType d, AType r))  = "amap";
str getOuter(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
                                      = "afunc";
str getOuter(anode(list[AType fieldType] fields)) 
                                      = "anode";
str getOuter(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = "aadt";
str getOuter(t:acons(AType adt, list[AType fieldType] fields, lrel[AType fieldType, Expression defaultExp] kwFields))
                                      = "acons";
str getOuter(aparameter(str pname, AType bound)) 
                                      = getOuter(bound);
str getOuter(avalue())                = "avalue";
default str getOuter(AType t)         = "avalue";

/*****************************************************************************/
/*  Convert an AType to a test for that AType (represented as VType)         */
/*****************************************************************************/

//str atype2istype(str e, AType t, JGenie jg) = "<e>.getType().comparable(<jg.shareType(t)>)";

//str atype2istype(str e, AType t)                           = atype2istype1(e, "getType()", t);
//
//
//
//str atype2isElementType(str e, AType t)                    = atype2istype1(e, "getElementType()", t);
//str atype2isKeyType(str e, AType t)                        = atype2istype1(e, "getKeyType()", t);
//str atype2isValueType(str e, AType t)                      {
//    return  atype2istype1(e, "getType().getValueType()", t);
//}
//str atype2isFieldType(str e, int i, AType t)               = atype2istype1(e, "getType().getFieldType(<i>)", t);
//str atype2isRelFieldType(str e, int i, AType t)            = atype2istype1("((ISet)<e>).asRelation()", "getElementType().getFieldType(<i>)", t);
//str atype2isLRelFieldType(str e, int i, AType t)           = atype2istype1("((IList)<e>).asRelation()", "getElementType().getFieldType(<i>)", t);
//
//
//str atype2istype1(str e, str get, avoid())                 = "<e>.<get>.isBottom()";
//str atype2istype1(str e, str get, abool())                 = "<e>.<get>.isBool()";
//str atype2istype1(str e, str get, aint())                  = "<e>.<get>.isInteger()";
//str atype2istype1(str e, str get, areal())                 = "<e>.<get>.isReal()";
//str atype2istype1(str e, str get, arat())                  = "<e>.<get>.isRational()";
//str atype2istype1(str e, str get, anum())                  = "<e>.<get>.isNumber()";
//str atype2istype1(str e, str get, astr())                  = "<e>.<get>.isString()";
//str atype2istype1(str e, str get, aloc())                  = "<e>.<get>.isSourceLocation()";
//str atype2istype1(str e, str get, adatetime())             = "<e>.<get>.isDateTime()";
//
////str atype2istype1(str e, str get, alist(AType t))          = "<e>.<get>.isList()<elem_check>"
////                                                           when tp := atype2isElementType("((IList)<e>)", t),
////                                                                elem_check := (tp == "true" ? "" : " && <tp>");
//
//str atype2istype1(str e, str get, tp:alist(AType t))          = "<e>.<get>.comparable(<atype2vtype(tp)>)";
//                                   
//
////str atype2istype1(str e, str get, alist(AType t))          = "<e>.<get>.isList()<elem_check>"
////                                                           when tp := "(<atype2isElementType("((IList)<e>)", avoid())> || <atype2isElementType("((IList)<e>)", t)>)",
////                                                                elem_check := (tp == "true" ? "" : " && <tp>");
//       
//str atype2istype1(str e, str get, tp:aset(AType t))          = "<e>.<get>.comparable(<atype2vtype(tp)>)";
//                
//                                                                                                                         
////str atype2istype1(str e, str get, aset(AType t))          = "<e>.<get>.isSet()<elem_check>"
////                                                           when tp := "(<atype2isElementType("((ISet)<e>)", avoid())> || <atype2isElementType("((ISet)<e>)", t)>)",
////                                                                elem_check := (tp == "true" ? "" : " && <tp>");
//                                                           
//
////str atype2istype1(str e, str get, aset(AType t))           = "<e>.<get>.isSet()<elem_check>"
////                                                           when tp := atype2isElementType("((ISet)<e>)", t),
////                                                                elem_check := (tp == "true" ? "" : " && <tp>");
//
////str atype2istype1(str e, str get, aset(AType t))           = "<e>.<get>.isSet()<elem_check>"
////                                                           when tp := "(((ISet)<e>).isEmpty() || <atype2isElementType("((ISet)<e>)", t)>)",
////                                                                elem_check := (tp == "true" ? "" : " && <tp>");
//
//
//str atype2istype1(str e, str get, tp:amap(AType d, AType r))   = "<e>.<get>.comparable(<atype2vtype(tp)>)";
//                                                           
//                                                                
////str atype2istype1(str e, str get, amap(AType d, AType r))   = "<e>.<get>.isMap()<key_check><val_check>"
////                                                            when tpk := atype2isKeyType("(<e>)", d),
////                                                                 key_check := (tpk == "true" ? "" : " && <tpk>"),
////                                                                 tpv := atype2isValueType("(<e>)", r),
////                                                                 val_check := (tpv == "true" ? "" : " && <tpv>")
////                                                                ;
//                                                                
//str atype2istype1(str e, str get, arel(atypeList(list[AType] ts)))         
//                                                           = "<e>.<get>.isRelation() && (((ISet)<e>).isEmpty() || ((ISet)<e>).asRelation().arity() == <size(ts)>)<field_checks>"
//                                                           when field_checks0 := intercalate(" && ", [*(tp == "true" ? [] : tp) | i <- index(ts), tp := atype2isRelFieldType(e, i, ts[i])]),
//                                                                field_checks := (isEmpty(field_checks0) ? "" : " && <field_checks0>");
//
//str atype2istype1(str e, str get, alrel(atypeList(list[AType] ts)))
//                                                           = "<e>.<get>.isListRelation() && (((IList)<e>).isEmpty() || ((IList)<e>).asRelation().arity() == <size(ts)>)<field_checks>"
//                                                           when field_checks0 := intercalate(" && ", [*(tp == "true" ? [] : tp) | i <- index(ts), tp := atype2isLRelFieldType(e, i, ts[i])]),
//                                                                field_checks := (isEmpty(field_checks0) ? "" : " && <field_checks0>");
//
//str atype2istype1(str e, str get, atuple(atypeList(list[AType] ts)))        
//                                                            = "<e>.<get>.isTuple() && ((ITuple)<e>).arity() == <size(ts)><field_checks>"
//                                                            when field_checks0 := intercalate(" && ", [*(tp == "true" ? [] : tp)| i <- index(ts), tp := atype2isFieldType("((ITuple)<e>)", i, ts[i])]),
//                                                                 field_checks  := (isEmpty(field_checks0) ? "" : " && <field_checks0>");
//
//
//
//str atype2istype1(str e, str get, afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
//                                          = "<e>.<get>.isExternalType()";
//str atype2istype1(str e, str get, anode(list[AType fieldType] fields)) 
//                                          = "<e>.<get>.isNode()";
//str atype2istype1(str e, str get, aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) 
//                                          = "<e>.<get>.isAbstractData()";
//str atype2istype1(str e, str get, t: acons(AType adt, list[AType fieldType] fields, lrel[AType fieldType, Expression defaultExp] kwFields))
//                                          = "<e>.<get>.isConstructor()";
//str atype2istype1(str e, str get, overloadedAType(rel[loc, IdRole, AType] overloads))
//                                          = "<e>.<get>.isOverloaded()";
//str atype2istype1(str e, str get, aparameter(str pname, AType bound)) = atype2istype(e, bound);
//str atype2istype1(str e, str get, areified(AType atype))   = "$isReified(<e>)";    // TODO
//str atype2istype1(str e, str get, avalue())                = "true"; // "<e>.<get>.isTop()";


// TODO complete cases and generate right code

//str atype2istype1(str e, str get, AType::\iter(AType symbol)) = "isTree(<e>)";
//
//str atype2istype1(str e, str get, AType::\iter-star(AType symbol)) = "isTree(<e>)";
//
//str atype2istype1(str e, str get, AType::\iter-seps(AType symbol, list[AType] separators)) = "isTree(<e>)";  
// 
//str atype2istype1(str e, str get, AType::\iter-star-seps(AType symbol, list[AType] separators)) = "isTree(<e>)";  
//
//str atype2istype1(str e, str get, AType::\iter-seps(AType symbol, list[AType] separators)) = "isTree(<e>)";
//
//default str atype2istype1(str e, str get, AType t)         { throw "atype2istype1: cannot handle <t>"; }

// ----

// TODO cover all cases
// Escape a string using Java escape conventions

str escapeForJ(str s){
   n = size(s);
   i = 0;
   res = "";
   while(i < n){
    c = s[i];
    switch(c){
        case "\b": res += "\\b";
        case "\t": res += "\\t";
        case "\n": res += "\\n";
        case "\r": res += "\\r";
        case "\a0C": res += "\\f";  // formfeed
        case "\'": res += "\\\'";
        case "\"": res += "\\\"";
        case "\\" : res += "\\\\";
        //case "\\": if(i+1 < n){ 
        //                c1 = s[i+1];
        //                i += 1;
        //                if(c1 == "\\"){
        //                    res += "<c><c1><c><c1>";
        //                } else if(c1 in {"b","f","t",/*"n",*/"r","\'", "\""}){
        //                    res += "<c><c><c><c1>";
        //                } else {
        //                    res += "<c><c><c1>";
        //                }
        //            } else {
        //                res += "<c><c>";
        //            }
        default: res +=  c;
     }
     i += 1;
   }
   return res;
}

str escapeForJRegExp(str s){
   n = size(s);
   i = 0;
   res = "";
   while(i < n){
    c = s[i];
    switch(c){
        case "\b": res += "\\b";
        case "\t": res += "\\t";
        case "\n": res += "\\n";
        case "\r": res += "\\r";
        case "\a0C": res += "\\f";  // formfeed
        case "\'": res += "\\\'";
        case "\"": res += "\\\"";
        //case "(": res += "\\(";
        //case ")": res += "\\)";
        //case "[": res += "\\[";
        //case "]": res += "\\]";
        //case ".": res += "\\.";
        //case "$": res += "\\$";
        //case "^": res += "\\^";
        case "\\": if(i+1 < n){ 
                        c1 = s[i+1];
                        i += 1;
                        if(c1 == "\\"){
                            res += "<c><c1><c><c1>";
                        } else if (c1 == "/"){
                            res += "/";
                        } else  if(c1 in {"b","f","t","n","r","\'", "\""}){
                            res += "<c><c1>";
                        } else {
                            res += "<c><c><c1>";
                        }
                    } else {
                        res += "<c><c>";
                    }
        default: res +=  c;
     }
     i += 1;
   }
   return res;
}
    
/*****************************************************************************/
/*  Convert a Rascal value to the equivalent IValue                          */
/*****************************************************************************/

str value2IValue(value x) = value2IValue(x, ());
str value2IValue(value x, map[value, str] constants) = doValue2IValue(x, constants);

str value2IValueRec(value x, map[value, str] constants) = constants[x] when constants[x]?;
default str value2IValueRec(value x, map[value, str] constants) = doValue2IValue(x, constants);

str doValue2IValue(bool b, map[value, str] constants) = "$VF.bool(<b>)";
str doValue2IValue(int n, map[value, str] constants) = "$VF.integer(\"<n>\")";
str doValue2IValue(real r, map[value, str] constants) = "$VF.real(<r>)";
str doValue2IValue(rat rt, map[value, str] constants) = "$VF.rational(\"<rt>\")";
str doValue2IValue(str s, map[value, str] constants) = "$VF.string(\"<escapeForJ(s)>\")";

str doValue2IValue(loc l, map[value, str] constants) {
    base = "$create_aloc($VF.string(\"<l.uri>\"))";
    return l.offset? ? "$VF.sourceLocation(<base>, <l.offset>, <l.length>, <l.begin.line>, <l.end.line>, <l.begin.column>, <l.end.column>)"
                      : base;
}

str doValue2IValue(datetime dt, map[value, str] constants) {
    if(dt.isDateTime)
        return "$VF.datetime(<dt.year>, <dt.month>, <dt.day>, <dt.hour>, <dt.minute>, <dt.second>, <dt.millisecond>, <dt.timezoneOffsetHours>, <dt.timezoneOffsetMinutes>)";
    if(dt.isDate)
        return "$VF.date(<dt.year>, <dt.month>, <dt.day>)";
    return "$VF.time(<dt.hour>, <dt.minute>, <dt.second>, <dt.millisecond>)";
}

str doValue2IValue(list[&T] lst, map[value, str] constants) = "$VF.list(<intercalate(", ", [value2IValueRec(elem, constants) | elem <- lst ])>)";
str doValue2IValue(set[&T] st, map[value, str] constants) = "$VF.set(<intercalate(", ", [value2IValueRec(elem, constants) | elem <- st ])>)";

str doValue2IValue(tuple[&A] tup, map[value, str] constants) = "$VF.tuple(<value2IValueRec(tup[0], constants)>)";
str doValue2IValue(tuple[&A,&B] tup, map[value, str] constants) = "$VF.tuple(<value2IValueRec(tup[0], constants)>, <value2IValueRec(tup[1], constants)>)";
str doValue2IValue(tuple[&A,&B,&C] tup, map[value, str] constants) = "$VF.tuple(<value2IValueRec(tup[0], constants)>, <value2IValueRec(tup[1], constants)>, <value2IValueRec(tup[2], constants)>)";
str doValue2IValue(tuple[&A,&B,&C,&D] tup, map[value, str] constants) = "$VF.tuple(<value2IValueRec(tup[0], constants)>, <value2IValueRec(tup[1], constants)>, <value2IValueRec(tup[2], constants)>, <value2IValueRec(tup[3], constants)>)";
str doValue2IValue(tuple[&A,&B,&C,&D,&E] tup, map[value, str] constants) = "$VF.tuple(<value2IValueRec(tup[0], constants)>, <value2IValueRec(tup[1], constants)>, <value2IValueRec(tup[2], constants)>, <value2IValueRec(tup[3], constants)>, <value2IValueRec(tup[4], constants)>)";
str doValue2IValue(tuple[&A,&B,&C,&D,&E,&F] tup, map[value, str] constants) = "$VF.tuple(<value2IValueRec(tup[0], constants)>, <value2IValueRec(tup[1], constants)>, <value2IValueRec(tup[2], constants)>, <value2IValueRec(tup[3], constants)>, <value2IValueRec(tup[4], constants)>, <value2IValueRec(tup[5], constants)>)";
str doValue2IValue(tuple[&A,&B,&C,&D,&E,&F,&G] tup, map[value, str] constants) = "$VF.tuple(<value2IValueRec(tup[0], constants)>, <value2IValueRec(tup[1], constants)>, <value2IValueRec(tup[2], constants)>, <value2IValueRec(tup[3], constants)>, <value2IValueRec(tup[4], constants)>, <value2IValueRec(tup[5], constants)>, <value2IValueRec(tup[6], constants)>)";
str doValue2IValue(tuple[&A,&B,&C,&D,&E,&F,&G,&H] tup, map[value, str] constants) = "$VF.tuple(<value2IValueRec(tup[0], constants)>, <value2IValueRec(tup[1], constants)>, <value2IValueRec(tup[2], constants)>, <value2IValueRec(tup[3], constants)>, <value2IValueRec(tup[4], constants)>, <value2IValueRec(tup[5], constants)>, <value2IValueRec(tup[6], constants)>, <value2IValueRec(tup[7], constants)>)";
str doValue2IValue(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I] tup, map[value, str] constants) = "$VF.tuple(<value2IValueRec(tup[0], constants)>, <value2IValueRec(tup[1], constants)>, <value2IValueRec(tup[2], constants)>, <value2IValueRec(tup[3], constants)>, <value2IValueRec(tup[4], constants)>, <value2IValueRec(tup[5], constants)>, <value2IValueRec(tup[6, constants])>, <value2IValueRec(tup[7], constants)>, <value2IValueRec(tup[8], constants)>)";
str doValue2IValue(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] tup, map[value, str] constants) = "$VF.tuple(<value2IValueRec(tup[0], constants)>, <value2IValueRec(tup[1], constants)>, <value2IValueRec(tup[2], constants)>, <value2IValueRec(tup[3], constants)>, <value2IValueRec(tup[4], constants)>, <value2IValueRec(tup[5], constants)>, <value2IValueRec(tup[6], constants)>, <value2IValueRec(tup[7], constants)>, <value2IValueRec(tup[8], constants)>, <value2IValueRec(tup[9], constants)>)";

str doValue2IValue(map[&K,&V] mp, map[value, str] constants) = "$buildMap(<intercalate(", ", ["<value2IValueRec(k, constants)>, <value2IValueRec(mp[k], constants)>" | k <- mp ])>)";

str doValue2IValue(type[&T] typeValue, map[value, str] constants) {
   return "$RVF.reifiedType(<value2IValueRec(typeValue.symbol, constants)>,<value2IValueRec(typeValue.definitions, constants)>)";
}

// the builtin reified type representations (Symbol, Production) are not necessarily declared in the current scope, so
// we lookup their constructors in the RascalValueFactory hand-written fields:
str doValue2IValue(Symbol sym, map[value, str] constants) {
   return "$RVF.constructor(org.rascalmpl.values.RascalValueFactory.Symbol_<toRascalValueFactoryName(getName(sym))><if (getChildren(sym) != []){>,<}> <intercalate(",", [value2IValueRec(child, constants) | child <- getChildren(sym)])>)";
}

str doValue2IValue(Production sym, map[value, str] constants) {
   return "$RVF.constructor(org.rascalmpl.values.RascalValueFactory.Production_<toRascalValueFactoryName(getName(sym))><if (getChildren(sym) != []){>,<}> <intercalate(",", [value2IValueRec(child, constants) | child <- getChildren(sym)])>)";
}

str doValue2IValue(Attr sym, map[value, str] constants) {
   return "$RVF.constructor(org.rascalmpl.values.RascalValueFactory.Attr_<toRascalValueFactoryName(getName(sym))><if (getChildren(sym) != []){>,<}> <intercalate(",", [value2IValueRec(child, constants) | child <- getChildren(sym)])>)";
}

str doValue2IValue(Associativity sym, map[value, str] constants) {
   return "$RVF.constructor(org.rascalmpl.values.RascalValueFactory.Associativity_<toRascalValueFactoryName(getName(sym))><if (getChildren(sym) != []){>,<}> <intercalate(",", [value2IValueRec(child, constants) | child <- getChildren(sym)])>)";
}

str doValue2IValue(CharRange sym, map[value, str] constants) {
   return "$RVF.constructor(org.rascalmpl.values.RascalValueFactory.CharRange_<toRascalValueFactoryName(getName(sym))><if (getChildren(sym) != []){>,<}> <intercalate(",", [value2IValueRec(child, constants) | child <- getChildren(sym)])>)";
}

str doValue2IValue(Production sym, map[value, str] constants) {
   return "$RVF.constructor(org.rascalmpl.values.RascalValueFactory.Production_<toRascalValueFactoryName(getName(sym))><if (getChildren(sym) != []){>,<}> <intercalate(",", [value2IValueRec(child, constants) | child <- getChildren(sym)])>)";
}

str toRascalValueFactoryName(str consName) = capitalize(visit(consName) {
    case /\-<l:[a-z]>/ => capitalize(l) 
});

str doValue2IValue(char(int i), map[value, str] constants)  = "$RVF.character(<i>)";

str doValue2IValue(Tree t:appl(Production prod, list[Tree] args), map[value, str] constants) {
    childrenContrib = isEmpty(args) ? "" : ", <intercalate(", ", [ value2IValueRec(child, constants) | child <- args ])>";
    return "$RVF.appl(<value2IValueRec(prod, constants)> <childrenContrib>)";
}

default str doValue2IValue(node nd, map[value, str] constants) {
    name = getName(nd);
   
    children = getChildren(nd);
    childrenContrib = isEmpty(children) ? "" : ", <intercalate(", ", [ value2IValueRec(child, constants) | child <- getChildren(nd) ])>";
   
    if(name in {"follow", "not-follow", "precede", "not-precede", "delete", "at-column", "begin-of-line", "end-of-line", "except"}){
         childrenContrib = intercalate(", ", [ value2IValueRec(child, constants) | child <- children ]);
         return "$RVF.constructor(org.rascalmpl.values.RascalValueFactory.Condition_<toRascalValueFactoryName(name)><if (children != []){>,<}><childrenContrib>)";
       
    } else {
        childrenContrib = isEmpty(children) ? "" : ", <intercalate(", ", [ value2IValueRec(child, constants) | child <- children ])>";
        kwparams = getKeywordParameters(nd);
        kwparamsContrib = isEmpty(kwparams) ? "" : ", keywordParameters=<kwparams>";
        name = isEmpty(name) ? "\"\"" : (name[0] == "\"" ? name : "\"<name>\"");
        return "$VF.node(<name><childrenContrib><kwparamsContrib>)";
    }
}

str doValue2IValue(aadt(str adtName, list[AType] parameters, concreteSyntax()), map[value, str] constants) 
    = "$RVF.constructor(org.rascalmpl.values.RascalValueFactory.Symbol_Sort, $VF.string(\"<adtName>\"))";

str doValue2IValue(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole), map[value, str] constants) = adtName;

str doValue2IValue(acons(AType adt,
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields), map[value, str] constants)
                 = "IConstructor";

str doValue2IValue(t:avoid(), map[value, str] constants) { throw "value2IValue: cannot handle <t>"; }
str doValue2IValue(t:areified(AType atype), map[value, str] constants) { throw "value2IValue: cannot handle <t>"; }
default str doValue2IValue(value v, map[value, str] constants) { throw "value2IValue: cannot handle <v>"; }

/*****************************************************************************/
/*  Convert a Rascal value to Java equivalent of its outer type              */
/*****************************************************************************/

str value2outertype(int n) = "IInteger";
str value2outertype(bool b) = "IBool";
str value2outertype(real r) = "IReal";
str value2outertype(rat rt) = "IRational";
str value2outertype(str s) = "IString";

str value2outertype(Tree nd) = "IConstructor";
str value2outertype(Symbol nd) = "IConstructor";
str value2outertype(Production nd) = "IConstructor";
default str value2outertype(node nd) = "INode";
str value2outertype(loc l) = "ISourceLocation";
str value2outertype(datetime dt) = "IDateTime";
str value2outertype(list[&T] lst) = "IList";
str value2outertype(set[&T] st) = "ISet";
str value2outertype(map[&K,&V] st) = "IMap";
str value2outertype(atuple(AType ts)) = "ITuple"; // ?? this is not a value
str value2outertype(tuple[&A] tup) = "ITuple";
str value2outertype(tuple[&A,&B] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D,&E] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D,&E,&F] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D,&E,&F,&G] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D,&E,&F,&G,&H] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] tup) = "ITuple";

str value2outertype(amap(AType d, AType r)) = "IMap";
str value2outertype(arel(AType ts)) = "IRelation\<ISet\>";
str value2outertype(alrel(AType ts)) = "IRelation\<IList\>";

str value2outertype(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = adtName;

str value2outertype(acons(AType adt,
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "IConstructor";
str value2outertype(areified(AType atype)) = "IConstructor";
default str value2outertype(AType t) = "IValue";

/*******************************************************************************/
/*  Convert an AType to an equivalent type ("VType") in the Vallang type store */
/*******************************************************************************/

str refType(AType t, JGenie jg, bool inTest)
    = inTest ? "$me.<jg.shareType(t)>" : jg.shareType(t);
    
str atype2vtype(aint(), JGenie jg, bool inTest=false) = "$TF.integerType()";
str atype2vtype(abool(), JGenie jg, bool inTest=false) = "$TF.boolType()";
str atype2vtype(areal(), JGenie jg, bool inTest=false) = "$TF.realType()";
str atype2vtype(arat(), JGenie jg, bool inTest=false) = "$TF.rationalType()";
str atype2vtype(astr(), JGenie jg, bool inTest=false) = "$TF.stringType()";
str atype2vtype(anum(), JGenie jg, bool inTest=false) = "$TF.numberType()";
str atype2vtype(anode(list[AType fieldType] fields), JGenie jg, bool inTest=false) = "$TF.nodeType()";
str atype2vtype(avoid(), JGenie jg, bool inTest=false) = "$TF.voidType()";
str atype2vtype(avalue(), JGenie jg, bool inTest=false) = "$TF.valueType()";
str atype2vtype(aloc(), JGenie jg, bool inTest=false) = "$TF.sourceLocationType()";
str atype2vtype(adatetime(), JGenie jg, bool inTest=false) = "$TF.dateTimeType()";
str atype2vtype(alist(AType t), JGenie jg, bool inTest=false) = "$TF.listType(<refType(t, jg, inTest)>)";
str atype2vtype(aset(AType t), JGenie jg, bool inTest=false) = "$TF.setType(<refType(t, jg, inTest)>)";
str atype2vtype(atuple(AType ts), JGenie jg, bool inTest=false) = "$TF.tupleType(<atype2vtype(ts, jg, inTest=inTest)>)";

str atype2vtype(amap(AType d, AType r), JGenie jg, bool inTest=false) {
    return (d.label? && d.label != "_")
             ? "$TF.mapType(<refType(d, jg, inTest)>, \"<d.label>\", <refType(r, jg, inTest)>, \"<r.label>\")"
             : "$TF.mapType(<refType(d, jg, inTest)>,<refType(r, jg, inTest)>)";
}
str atype2vtype(arel(AType t), JGenie jg, bool inTest=false) = "$TF.setType($TF.tupleType(<atype2vtype(t, jg,inTest=inTest)>))";
str atype2vtype(alrel(AType t), JGenie jg, bool inTest=false) = "$TF.listType($TF.tupleType(<atype2vtype(t, jg, inTest=inTest)>))";

str atype2vtype(f:afunc(AType ret, list[AType] formals, list[Keyword] kwFormals), JGenie jg, bool inTest=false){
    vformals = isEmpty(formals) ? "$TF.tupleEmpty()" 
                                : ( (!isEmpty(formals) && any(t <- formals, t.label?)) 
                                        ? "$TF.tupleType(<intercalate(", ", [ *[refType(t, jg, inTest), "\"<t.label>\""] | t <- formals])>)"
                                        : "$TF.tupleType(<intercalate(", ", [ refType(t, jg, inTest) | t <- formals])>)"
                                  );
    vkwformals = isEmpty(kwFormals) ? "$TF.tupleEmpty()" 
                                    : "$TF.tupleType(<intercalate(", ", [ refType(t.fieldType, jg, inTest) | t <- kwFormals])>)"; 
    return "$TF.functionType(<jg.shareType(ret)>, <vformals>, <vkwformals>)";
}


str atype2vtype(aadt(str adtName, list[AType] parameters, dataSyntax()), JGenie jg, bool inTest=false)
    = (inTest ? "$me." : "") + getADTName(adtName);
str atype2vtype(aadt(str adtName, list[AType] parameters, contextFreeSyntax()), JGenie jg, bool inTest=false)    
    = "$TF.fromSymbol($RVF.constructor(org.rascalmpl.values.RascalValueFactory.Symbol_Sort, $VF.string(\"<adtName>\")), $TS, p -\> Collections.emptySet())";
    
str atype2vtype(aadt(str adtName, list[AType] parameters, lexicalSyntax()), JGenie jg, bool inTest=false){    
    return (inTest ? "$me." : "") + getADTName(adtName);
    //return "$TF.constructor($TS, org.rascalmpl.values.RascalValueFactory.Symbol, \"lex\", $VF.string(\"<adtName>\"))";
}
    
str atype2vtype(aadt(str adtName, list[AType] parameters, keywordSyntax()), JGenie jg, bool inTest=false)    
    = "$TF.constructor($TS, org.rascalmpl.values.RascalValueFactory.Symbol, \"keywords\", $VF.string(\"<adtName>\"))";
str atype2vtype(aadt(str adtName, list[AType] parameters, layoutSyntax()), JGenie jg, bool inTest=false)    
    = "$TF.constructor($TS, org.rascalmpl.values.RascalValueFactory.Symbol, \"layout\", $VF.string(\"<adtName>\"))";
                                 
str atype2vtype(c:acons(AType adt,
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields), JGenie jg, bool inTest=false){
    res = "$TF.constructor($TS, <jg.shareType(adt)>, \"<c.label>\"<isEmpty(fields) ? "" : ", "><intercalate(", ", [ *[refType(t, jg, inTest), "\"<t.label>\""] | t <- fields])>)";
    return res;
}
str atype2vtype(aparameter(str pname, AType bound), JGenie jg, bool inTest=false) = "$TF.parameterType(\"<pname>\", <refType(bound, jg, inTest)>)";


str atype2vtype(atypeList(list[AType] atypes), JGenie jg, bool inTest=false)
    = (atypes[0].label? && atypes[0].label != "_")
         ? intercalate(", ", [*[refType(t, jg, inTest), "\"<t.label>\""] | t <- atypes])
         : intercalate(", ", [refType(t, jg, inTest) | t <- atypes]);
                       
str atype2vtype(areified(AType atype), JGenie jg, bool inTest=false) {
 //   dfs = collectNeededDefs(atype);
    return "$RTF.reifiedType(<refType(atype, jg, inTest)>)";
  //return atype2IValue(atype, dfs);
   // return "$reifiedAType(<atype2IValue(atype, dfs)>, <value2IValue(dfs)>)";
}

default str atype2vtype(AType t, JGenie jg, bool inTest=false) {
    return "$TF.valueType()";
}