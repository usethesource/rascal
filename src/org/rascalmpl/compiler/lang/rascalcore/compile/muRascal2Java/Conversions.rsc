module lang::rascalcore::compile::muRascal2Java::Conversions

import Node;
import String;
import Map;
import Set;
//import lang::rascalcore::check::AType;
//import lang::rascalcore::check::ATypeUtils;

extend lang::rascalcore::check::CheckerCommon;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::compile::util::Names;

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

str atype2istype(str e, AType t)                           = atype2istype1(e, "getType()", t);
str atype2isElementType(str e, AType t)                    = atype2istype1(e, "getElementType()", t);
str atype2isKeyType(str e, AType t)                        = atype2istype1(e, "getKeyType()", t);
str atype2isValueType(str e, AType t)                      = atype2istype1(e, "getValueType()", t);
str atype2isFieldType(str e, int i, AType t)               = atype2istype1(e, "getType().getFieldType(<i>)", t);
str atype2isRelFieldType(str e, int i, AType t)            = atype2istype1("((ISet)<e>).asRelation()", "getElementType().getFieldType(<i>)", t);
str atype2isLRelFieldType(str e, int i, AType t)           = atype2istype1("((IList)<e>).asRelation()", "getElementType().getFieldType(<i>)", t);


str atype2istype1(str e, str get, avoid())                 = "<e>.<get>.isBottom()";
str atype2istype1(str e, str get, abool())                 = "<e>.<get>.isBool()";
str atype2istype1(str e, str get, aint())                  = "<e>.<get>.isInteger()";
str atype2istype1(str e, str get, areal())                 = "<e>.<get>.isReal()";
str atype2istype1(str e, str get, arat())                  = "<e>.<get>.isRational()";
str atype2istype1(str e, str get, anum())                  = "<e>.<get>.isNumber()";
str atype2istype1(str e, str get, astr())                  = "<e>.<get>.isString()";
str atype2istype1(str e, str get, aloc())                  = "<e>.<get>.isSourceLocation()";
str atype2istype1(str e, str get, adatetime())             = "<e>.<get>.isDateTime()";
str atype2istype1(str e, str get, alist(AType t))          = "<e>.<get>.isList()<elem_check>"
                                                           when tp := atype2isElementType("((IList)<e>)", t),
                                                                elem_check := (tp == "true" ? "" : " && <tp>");
str atype2istype1(str e, str get, aset(AType t))           = "<e>.<get>.isSet()<elem_check>"
                                                           when tp := atype2isElementType("((ISet)<e>)", t),
                                                                elem_check := (tp == "true" ? "" : " && <tp>");
str atype2istype1(str e, str get, arel(atypeList(list[AType] ts)))         
                                                           = "<e>.<get>.isRelation() && (((ISet)<e>).isEmpty() || ((ISet)<e>).asRelation().arity() == <size(ts)>)<field_checks>"
                                                           when field_checks0 := intercalate(" && ", [*(tp == "true" ? [] : tp) | i <- index(ts), tp := atype2isRelFieldType(e, i, ts[i])]),
                                                                field_checks := (isEmpty(field_checks0) ? "" : " && <field_checks0>");

str atype2istype1(str e, str get, alrel(atypeList(list[AType] ts)))
                                                           = "<e>.<get>.isListRelation() && (((IList)<e>).isEmpty() || ((IList)<e>).asRelation().arity() == <size(ts)>)<field_checks>"
                                                           when field_checks0 := intercalate(" && ", [*(tp == "true" ? [] : tp) | i <- index(ts), tp := atype2isLRelFieldType(e, i, ts[i])]),
                                                                field_checks := (isEmpty(field_checks0) ? "" : " && <field_checks0>");

str atype2istype1(str e, str get, atuple(atypeList(list[AType] ts)))        
                                                            = "<e>.<get>.isTuple() && ((ITuple)<e>).arity() == <size(ts)><field_checks>"
                                                            when field_checks0 := intercalate(" && ", [*(tp == "true" ? [] : tp)| i <- index(ts), tp := atype2isFieldType("((ITuple)<e>)", i, ts[i])]),
                                                                 field_checks  := (isEmpty(field_checks0) ? "" : " && <field_checks0>");

str atype2istype1(str e, str get, amap(AType d, AType r))   = "<e>.<get>.isMap()<key_check><val_check>"
                                                            when tpk := atype2isKeyType("((IMap)<e>)", d),
                                                                 key_check := (tpk == "true" ? "" : " && <tpk>"),
                                                                 tpv := atype2isValueType("((IMap)<e>)", r),
                                                                 val_check := (tpv == "true" ? "" : " && <tpv>")
                                                                ;

str atype2istype1(str e, str get, afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
                                          = "<e>.<get>.isExternalType()";
str atype2istype1(str e, str get, anode(list[AType fieldType] fields)) 
                                          = "<e>.<get>.isNode()";
str atype2istype1(str e, str get, aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) 
                                          = "<e>.<get>.isAbstractData()";
str atype2istype1(str e, str get, t: acons(AType adt, list[AType fieldType] fields, lrel[AType fieldType, Expression defaultExp] kwFields))
                                          = "<e>.<get>.isConstructor()";
str atype2istype1(str e, str get, overloadedAType(rel[loc, IdRole, AType] overloads))
                                          = "<e>.<get>.isOverloaded()";
str atype2istype1(str e, str get, aparameter(str pname, AType bound)) = atype2istype(e, bound);
str atype2istype1(str e, str get, areified(AType atype))   = "$isReified(<e>)";    // TODO
str atype2istype1(str e, str get, avalue())                = "true"; // "<e>.<get>.isTop()";


// TODO complete cases and generate right code

str atype2istype1(str e, str get, AType::\iter(AType symbol)) = "isTree(<e>)";

str atype2istype1(str e, str get, AType::\iter-star(AType symbol)) = "isTree(<e>)";

str atype2istype1(str e, str get, AType::\iter-seps(AType symbol, list[AType] separators)) = "isTree(<e>)";  
 
str atype2istype1(str e, str get, AType::\iter-star-seps(AType symbol, list[AType] separators)) = "isTree(<e>)";  

str atype2istype1(str e, str get, AType::\iter-seps(AType symbol, list[AType] separators)) = "isTree(<e>)";

default str atype2istype1(str e, str get, AType t)         { throw "atype2istype1: cannot handle <t>"; }

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

str value2IValue(bool b) = "$VF.bool(<b>)";
str value2IValue(int n) = "$VF.integer(\"<n>\")";
str value2IValue(real r) = "$VF.real(<r>)";
str value2IValue(rat rt) = "$VF.rational(\"<rt>\")";
str value2IValue(str s) = "$VF.string(\"<escapeForJ(s)>\")";

str value2IValue(loc l) {
    base = "$create_aloc($VF.string(\"<l.uri>\"))";
    return l.offset? ? "$VF.sourceLocation(<base>, <l.offset>, <l.length>, <l.begin.line>, <l.end.line>, <l.begin.column>, <l.end.column>)"
                      : base;
}

str value2IValue(datetime dt) {
    if(dt.isDateTime)
        return "$VF.datetime(<dt.year>, <dt.month>, <dt.day>, <dt.hour>, <dt.minute>, <dt.second>, <dt.millisecond>, <dt.timezoneOffsetHours>, <dt.timezoneOffsetMinutes>)";
    if(dt.isDate)
        return "$VF.date(<dt.year>, <dt.month>, <dt.day>)";
    return "$VF.time(<dt.hour>, <dt.minute>, <dt.second>, <dt.millisecond>)";
}

str value2IValue(list[&T] lst) = "$VF.list(<intercalate(", ", [value2IValue(elem) | elem <- lst ])>)";
str value2IValue(set[&T] st) = "$VF.set(<intercalate(", ", [value2IValue(elem) | elem <- st ])>)";

str value2IValue(rel[&A] r) = "$VF.set(<intercalate(", ", [value2IValue(elem) | elem <- r ])>)";
str value2IValue(rel[&A,&B] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>)" | tup <- r ])>)";
str value2IValue(rel[&A,&B,&C] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>)" | tup <- r ])>)";
str value2IValue(rel[&A,&B,&C,&D] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>)" | tup <- r ])>)";
str value2IValue(rel[&A,&B,&C,&D,&E] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>)" | tup <- r ])>)";
str value2IValue(rel[&A,&B,&C,&D,&E,&F] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>)" | tup <- r ])>)";
str value2IValue(rel[&A,&B,&C,&D,&E,&F,&G] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>)" | tup <- r ])>)";
str value2IValue(rel[&A,&B,&C,&D,&E,&F,&G,&H] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>)" | tup <- r ])>)";
str value2IValue(rel[&A,&B,&C,&D,&E,&F,&G,&H, &I] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>, <value2IValue(tup[8])>)" | tup <- r ])>)";
str value2IValue(rel[&A,&B,&C,&D,&E,&F,&G,&H,&I, &J] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>, <value2IValue(tup[8])>, <value2IValue(tup[9])>)" | tup <- r ])>)";

str value2IValue(lrel[&A] r) = "$VF.list(<intercalate(", ", [value2IValue(elem) | elem <- r ])>)";
str value2IValue(lrel[&A,&B] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>)" | tup <- r ])>)";
str value2IValue(lrel[&A,&B,&C] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>)" | tup <- r ])>)";
str value2IValue(lrel[&A,&B,&C,&D] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>)" | tup <- r ])>)";
str value2IValue(lrel[&A,&B,&C,&D,&E] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>)" | tup <- r ])>)";
str value2IValue(lrel[&A,&B,&C,&D,&E,&F] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>)" | tup <- r ])>)";
str value2IValue(lrel[&A,&B,&C,&D,&E,&F,&G] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>)" | tup <- r ])>)";
str value2IValue(lrel[&A,&B,&C,&D,&E,&F,&G,&H] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>)" | tup <- r ])>)";
str value2IValue(lrel[&A,&B,&C,&D,&E,&F,&G,&H,&I] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>, <value2IValue(tup[8])>)" | tup <- r ])>)";
str value2IValue(lrel[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>, <value2IValue(tup[8])>, <value2IValue(tup[9])>)" | tup <- r ])>)";

str value2IValue(tuple[&A] tup) = "$VF.tuple(<value2IValue(tup[0])>)";
str value2IValue(tuple[&A,&B] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>)";
str value2IValue(tuple[&A,&B,&C] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>)";
str value2IValue(tuple[&A,&B,&C,&D] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>)";
str value2IValue(tuple[&A,&B,&C,&D,&E] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>)";
str value2IValue(tuple[&A,&B,&C,&D,&E,&F] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>)";
str value2IValue(tuple[&A,&B,&C,&D,&E,&F,&G] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>)";
str value2IValue(tuple[&A,&B,&C,&D,&E,&F,&G,&H] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>)";
str value2IValue(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>, <value2IValue(tup[8])>)";
str value2IValue(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>, <value2IValue(tup[8])>, <value2IValue(tup[9])>)";

str value2IValue(map[&K,&V] mp) = "$buildMap(<intercalate(", ", ["<value2IValue(k)>, <value2IValue(mp[k])>" | k <- mp ])>)";

str value2IValue(type[&T] typeValue) {
   return "$RVF.reifiedType(<value2IValue(typeValue.symbol)>,<value2IValue(typeValue.definitions)>)";
}

// the builtin reified type representations (Symbol, Production) are not necessarily declared in the current scope, so
// we lookup their constructors in the RascalValueFactory hand-written fields:
str value2IValue(Symbol sym) {
   return "$RVF.constructor(org.rascalmpl.values.RascalValueFactory.Symbol_<toRascalValueFactoryName(getName(sym))><if (getChildren(sym) != []){>,<}> <intercalate(",", [value2IValue(child) | child <- getChildren(sym)])>)";
}

str value2IValue(Production sym) {
   return "$RVF.constructor(org.rascalmpl.values.RascalValueFactory.Production_<toRascalValueFactoryName(getName(sym))><if (getChildren(sym) != []){>,<}> <intercalate(",", [value2IValue(child) | child <- getChildren(sym)])>)";
}

str value2IValue(Attr sym) {
   return "$RVF.constructor(org.rascalmpl.values.RascalValueFactory.Attr_<toRascalValueFactoryName(getName(sym))><if (getChildren(sym) != []){>,<}> <intercalate(",", [value2IValue(child) | child <- getChildren(sym)])>)";
}

str value2IValue(Associativity sym) {
   return "$RVF.constructor(org.rascalmpl.values.RascalValueFactory.Associativity_<toRascalValueFactoryName(getName(sym))><if (getChildren(sym) != []){>,<}> <intercalate(",", [value2IValue(child) | child <- getChildren(sym)])>)";
}

str value2IValue(Production sym) {
   return "$RVF.constructor(org.rascalmpl.values.RascalValueFactory.Production_<toRascalValueFactoryName(getName(sym))><if (getChildren(sym) != []){>,<}> <intercalate(",", [value2IValue(child) | child <- getChildren(sym)])>)";
}

str toRascalValueFactoryName(str consName) = capitalize(visit(consName) {
    case /\-<l:[a-z]>/ => capitalize(l) 
});

default str value2IValue(node nd) {
    name = getName(nd);
    name = isEmpty(name) ? "\"\"" : (name[0] == "\"" ? name : "\"<name>\"");
    children = getChildren(nd);
    childrenContrib = isEmpty(children) ? "" : ", <intercalate(", ", [ value2IValue(child) | child <- getChildren(nd) ])>";
    kwparams = getKeywordParameters(nd);
    kwparamsContrib = isEmpty(kwparams) ? "" : ", keywordParameters=<kwparams>";
    return "$VF.node(<name><childrenContrib><kwparamsContrib>)";
}

str value2IValue(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = adtName;

str value2IValue(acons(AType adt,
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "IConstructor";

str value2IValue(t:avoid()) { throw "value2IValue: cannot handle <t>"; }
str value2IValue(t:areified(AType atype)) { throw "value2IValue: cannot handle <t>"; }
default str value2IValue(value v) { throw "value2IValue: cannot handle <v>"; }

/*****************************************************************************/
/*  Convert a Rascal value to Java equivalent of its outer type              */
/*****************************************************************************/

str value2outertype(int n) = "IInteger";
str value2outertype(bool b) = "IBool";
str value2outertype(real r) = "IReal";
str value2outertype(rat rt) = "IRational";
str value2outertype(str s) = "IString";

str value2outertype(node nd) = "INode";
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
str atype2vtype(aint()) = "$TF.integerType()";
str atype2vtype(abool()) = "$TF.boolType()";
str atype2vtype(areal()) = "$TF.realType()";
str atype2vtype(arat()) = "$TF.rationalType()";
str atype2vtype(astr()) = "$TF.stringType()";
str atype2vtype(anum()) = "$TF.numberType()";
str atype2vtype(anode(list[AType fieldType] fields)) = "$TF.nodeType()";
str atype2vtype(avoid()) = "$TF.voidType()";
str atype2vtype(avalue()) = "$TF.valueType()";
str atype2vtype(aloc()) = "$TF.sourceLocationType()";
str atype2vtype(adatetime()) = "$TF.dateTimeType()";
str atype2vtype(alist(AType t)) = "$TF.listType(<atype2vtype(t)>)";
str atype2vtype(aset(AType t)) = "$TF.setType(<atype2vtype(t)>)";
str atype2vtype(atuple(AType ts)) = "$TF.tupleType(<atype2vtype(ts)>)";

str atype2vtype(amap(AType d, AType r)) {
    return (d.label? && d.label != "_")
             ? "$TF.mapType(<atype2vtype(d)>, \"<d.label>\", <atype2vtype(r)>, \"<r.label>\")"
             : "$TF.mapType(<atype2vtype(d)>,<atype2vtype(r)>)";
}
str atype2vtype(arel(AType t)) = "$TF.setType($TF.tupleType(<atype2vtype(t)>))";
str atype2vtype(alrel(AType t)) = "$TF.listType($TF.tupleType(<atype2vtype(t)>))";

str atype2vtype(f:afunc(AType ret, list[AType] formals, list[Keyword] kwFormals)){
    vformals = isEmpty(formals) ? "$TF.tupleEmpty()" 
                                : ( (!isEmpty(formals) && any(t <- formals, t.label?)) 
                                        ? "$TF.tupleType(<intercalate(", ", [ *[atype2vtype(t), "\"<t.label>\""] | t <- formals])>)"
                                        : "$TF.tupleType(<intercalate(", ", [ atype2vtype(t) | t <- formals])>)"
                                  );
    vkwformals = isEmpty(kwFormals) ? "$TF.tupleEmpty()" 
                                    : "$TF.tupleType(<intercalate(", ", [ atype2vtype(t.fieldType) | t <- kwFormals])>)"; 
    return "$RTF.functionType(<atype2vtype(ret)>, <vformals>, <vkwformals>)";
}
      
str atype2vtype(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = getADTName(adtName);
str atype2vtype(c:acons(AType adt,
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "$TF.constructor($TS, <atype2vtype(adt)>, \"<c.label>\"<isEmpty(fields) ? "" : ", "><intercalate(", ", [ *[atype2vtype(t), "\"<t.label>\""] | t <- fields])>)";
str atype2vtype(aparameter(str pname, AType bound)) = "$TF.parameterType(\"<pname>\", <atype2vtype(bound)>)";


str atype2vtype(atypeList(list[AType] atypes))
    = (atypes[0].label? && atypes[0].label != "_")
         ? intercalate(", ", [*[atype2vtype(t), "\"<t.label>\""] | t <- atypes])
         : intercalate(", ", [atype2vtype(t) | t <- atypes]);
                       
str atype2vtype(areified(AType atype)) {
 //   dfs = collectNeededDefs(atype);
    return "$RTF.reifiedType(<atype2vtype(atype)>)";
  //return atype2IValue(atype, dfs);
   // return "$reifiedAType(<atype2IValue(atype, dfs)>, <value2IValue(dfs)>)";
}

default str atype2vtype(AType t) = "$TF.valueType()";

///******************************************************************************/
///*  Convert an AType to Java code that creates that type via the ATypeFactory */
///*****************************************************************************/
//
//// TODO complete all cases
//str atype2vtype(a: aint()) = "AType_aint<a.label? "_lab" : "">";
//str atype2vtype(a: abool()) = "AType_abool<a.label? "_lab" : "">";
//str atype2vtype(a: areal()) = "AType_aint<a.label? "_lab" : "">";
//str atype2vtype(a: arat()) = "AType_aint<a.label? "_lab" : "">";
//str atype2vtype(a: astr()) = "AType_aint<a.label? "_lab" : "">";
//str atype2vtype(a: anum()) = "AType_aint<a.label? "_lab" : "">";
//str atype2vtype(a: anode(list[AType fieldType] fields)) = "AType_aint<a.label? "_lab" : "">";
//str atype2vtype(a: avoid()) = "AType_aint<a.label? "_lab" : "">";
//str atype2vtype(a: avalue()) = "AType_aint<a.label? "_lab" : "">";
//str atype2vtype(a: aloc()) = "AType_aint<a.label? "_lab" : "">";
//str atype2vtype(a: adatetime()) = "AType_aint<a.label? "_lab" : "">";
//str atype2vtype(a: alist(AType t)) = "AType_aint<a.label? "_lab" : "">";
//str atype2vtype(aset(AType t)) = a.label? ? "aset_lab(<atype2vtype(t)>, <a.label>)" : "alist(<atype2vtype(t)>)";
//str atype2vtype(atuple(AType ts)) = "$TF.tupleType(<atype2vtype(ts)>)";
//str atype2vtype(amap(AType d, AType r)) = "$TF.mapType(<atype2vtype(d)>,<atype2vtype(r)>)";
//str atype2vtype(arel(AType t)) = "$TF.setType($TF.tupleType(<atype2vtype(t)>))";
//str atype2vtype(alrel(AType t)) = "$TF.listType($TF.tupleType(<atype2vtype(t)>))";
//str atype2vtype(a: aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = "getADTName(adtName);
//str atype2vtype(acons(AType adt,
//                list[AType fieldType] fields,
//                lrel[AType fieldType, Expression defaultExp] kwFields))
//                 = "IConstructor";
//
//str atype2vtype(atypeList(list[AType] atypes)) = intercalate(", ", [atype2vtype(t) | t <- atypes]);
//str atype2vtype(areified(AType atype)) = "AType";
//default str atype2vtype(AType t) = "$TF.valueType()";
