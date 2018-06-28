@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl (CWI)}
module lang::rascalcore::check::ConvertType

import Set;
import List;
import String;
import IO;
import Node;
import Map;

extend analysis::typepal::TypePal;

extend lang::rascalcore::check::AType;

import lang::rascalcore::check::ATypeExceptions;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::ATypeInstantiation;
import lang::rascalcore::check::TypePalConfig;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Characters;
import lang::rascalcore::grammar::definition::Literals;

public str currentAdt = "currentAdt";       // used to mark data declarations
public str inAlternative = "inAlternative"; // used to mark top-level alternative in syntax declaration
public str typeContainer = "typeContainer";

public str prettyPrintName(QualifiedName qn){
    if ((QualifiedName)`<{Name "::"}+ nl>` := qn) { 
       return replaceAll("<qn>", "\\", "");
    }
    throw "Unexpected syntax for qualified name: <qn>";
}

public str prettyPrintName(Name nm){ 
    return replaceFirst("<nm>", "\\", "");
}

public str prettyPrintBaseName(QualifiedName qn){
    if ((QualifiedName)`<{Name "::"}+ nl>` := qn) { 
        nameParts = [ n | n <- nl ];
        return replaceFirst("<nameParts[-1]>", "\\", "");
    }
    throw "Unexpected syntax for qualified name: <qn>";
}

public str prettyPrintBaseName(Name nm){ 
    return replaceFirst("<nm>", "\\", "");
}

public tuple[str qualifier, str base] splitQualifiedName(QualifiedName qn){
    if ((QualifiedName)`<{Name "::"}+ nl>` := qn) { 
        nameParts = [ replaceFirst("<n>", "\\", "") | n <- nl ];
        return size(nameParts) > 1 ? <intercalate("::", nameParts[0 .. -1]), nameParts[-1]> : <"", nameParts[0]>;
    }
    throw "Unexpected syntax for qualified name: <qn>";
}

void collect(current: (Type) `( <Type tp> )`, Collector c){
    c.fact(current, tp);
    collect(tp, c);
}

//void collect(current: (TypeVar) `& <Name name>`, Collector c){
//x = 1;
//}
//
//void collect(current: (TypeVar) `& <Name name> \<:<Type bound>`, Collector c){
//    collect(bound, c);
//}

// ---- basic types -----------------------------------------------------------

@doc{Convert from the concrete to the abstract representations of Rascal basic types.}


void collect(current: (BasicType)`bool`, Collector c){ c.fact(current, abool()); }

void collect(current: (BasicType)`int`, Collector c){ c.fact(current, aint()); }

void collect(current: (BasicType)`rat`, Collector c){ c.fact(current, arat()); }

void collect(current: (BasicType)`real`, Collector c){ c.fact(current, areal()); }

void collect(current: (BasicType)`num`, Collector c){ 
    c.fact(current, anum()); }

void collect(current: (BasicType)`str`, Collector c){ c.fact(current, astr()); }

void collect(current: (BasicType)`value`, Collector c){ c.fact(current, avalue()); }

void collect(current: (BasicType)`node`, Collector c){ c.fact(current, anode([])); }

void collect(current: (BasicType)`void`, Collector c){ c.fact(current, avoid()); }

void collect(current: (BasicType)`loc`, Collector c){ c.fact(current, aloc()); }

void collect(current: (BasicType)`datetime`, Collector c){ c.fact(current, adatetime()); }

default void collect(BasicType bt, Collector c) { c.report(error(bt, "Illegal use of type `<bt>`")); }

// ---- TypeArgs -------------------------------------------------------------

void collect(current: (TypeArg) `<Type tp>`, Collector c){
    c.fact(current, tp);
    collect(tp, c);
}

void collect(current: (TypeArg) `<Type tp> <Name name>`, Collector c){
    c.calculate("TypeArg <name>", name, [tp], AType(Solver s){
       res = (s.getType(tp)[label=unescape("<name>")]);
       return res;
     });
    c.fact(current, name);
    collect(tp, c);
}
// ---- structured types ------------------------------------------------------

@doc{Convert structured types, such as list<<int>>. Check here for certain syntactical 
conditions, such as: all field names must be distinct in a given type; lists require 
exactly one type argument; etc.}

public list[&T] dup(list[&T] lst) {
  done = {};
  return for (e <- lst, e notin done) {
    done = done + {e};
    append e;
  }
}

void collect(current:(Type)`list [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    if(size(targs) == 1){
        c.calculate("list type", current, targs, AType(Solver s){ 
            return makeListType(s.getType(targs[0])); });
    } else {
        c.report(error(current, "Type `list` should have one type argument"));
    }
    collect(tas, c);
}

void collect(current:(Type)`set [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    if(size(targs) == 1){
        c.calculate("set type", current, targs, AType(Solver s){ return makeSetType(s.getType(targs[0])); });
    } else {
        c.report(error(current, "Type `set` should have one type argument"));
    }
    collect(tas, c);
}

void collect(current:(Type)`bag [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    if(size(targs) == 1){
        c.calculate("bag type", current, targs, AType(Solver s){ return makeBagType(s.getType(targs[0])); });
    } else {
        c.report(error(current, "Type `bag` should have one type argument"));
    }
}

void collect(current:(Type)`map [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    if(size(targs) == 2){
        c.calculate("map type", current, targs, 
            AType(Solver s){
                dt = s.getType(targs[0]); rt = s.getType(targs[1]);
                if (!isEmpty(dt.label) && !isEmpty(rt.label) && dt.label != rt.label) { 
                    return makeMapType(dt, rt);
                } else if (!isEmpty(dt.label) && !isEmpty(rt.label) && dt.label == rt.label) {
                    s.report(error(tas,"Non-well-formed map type, labels must be distinct"));
                    return makeMapType(unset(dt, "label"),unset(rt,"label"));
                } else if (!isEmpty(dt.label) && isEmpty(rt.label)) {
                    s.report(warning(tas, "Field name `<dt.label>` ignored, field names must be provided for both fields or for none"));
                    return makeMapType(unset(dt, "label"),rt);
                } else if (isEmpty(dt.label) && !isEmpty(rt.label)) {
                    s.report(warning(tas, "Field name `<rt.label>` ignored, field names must be provided for both fields or for none"));
                    return makeMapType(dt, unset(rt, "label"));
                } else {
                    return makeMapType(dt,rt);
                }
        });
    } else {
        c.report(error(current, "Type `map` should have two type arguments"));
    }
    collect(tas, c);
}

void collect(current:(Type)`rel [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    c.calculate("rel type", current, targs, 
        AType(Solver s){
            l = [s.getType(ta) | ta <- targs];
            labelsList = [tp.label | tp <- l];
            nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
            distinctLabels = toSet(nonEmptyLabels);
            if (size(l) == size(distinctLabels)){
                return makeRelType(l);
            } else if(size(distinctLabels) == 0) {
                return makeRelType(l);
            } else if (size(distinctLabels) != size(nonEmptyLabels)) {
                s.report(error(tas, "Non-well-formed relation type, labels must be distinct"));
                return makeRelType([unset(tp, "label") | tp <- l]);
            } else if (size(distinctLabels) > 0) {
                s.report(warning(tas, "Field name ignored, field names must be provided for all fields or for none"));
                return makeRelType([unset(tp, "label") | tp <- l]);
            }
        });
    collect(tas, c);
}

void collect(current:(Type)`lrel [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    c.calculate("lrel type", current, targs, 
        AType(Solver s){
            l = [s.getType(ta) | ta <- targs];
            labelsList = [tp.label | tp <- l];
            nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
            distinctLabels = toSet(nonEmptyLabels);
            if (size(l) == size(distinctLabels)){
                return makeListRelType(l);
            } else if(size(distinctLabels) == 0) {
                return makeListRelType(l);
            } else if (size(distinctLabels) != size(nonEmptyLabels)) {
                s.report(error(tas, "Non-well-formed list relation type, labels must be distinct"));
                return makeListRelType([unset(tp, "label") | tp <- l]);
            } else if (size(distinctLabels) > 0) {
                s.report(warning(tas, "Field name ignored, field names must be provided for all fields or for none"));
                return makeListRelType([unset(tp, "label") | tp <- l]);
            }
        });
    collect(tas, c);
}

void collect(current:(Type)`tuple [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    c.calculate("tuple type", current, targs, 
        AType(Solver s){
            l = [s.getType(ta) | ta <- targs];
            labelsList = [tp.label | tp <- l];
            nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
            distinctLabels = toSet(nonEmptyLabels);
            if (size(l) == size(distinctLabels)){
                return makeTupleType(l);
            } else if(size(distinctLabels) == 0) {
                return makeTupleType(l);
            } else if (size(distinctLabels) != size(nonEmptyLabels)) {
                s.report(error(tas, "Non-well-formed tuple type, labels must be distinct"));
                return makeTupleType([unset(tp, "label") | tp <- l]);
            } else if (size(distinctLabels) > 0) {
                s.report(warning(tas, "Field name ignored, field names must be provided for all fields or for none"));
                return makeTupleType([unset(tp, "label") | tp <- l]);
            }  
        });
    collect(tas, c);   
} 

void collect(current:(Type)`type [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    if(size(targs) == 1){
        c.calculate("type type", current, targs, 
            AType(Solver s){
                l = [s.getType(ta) | ta <- targs];   
                if (!isEmpty(l[0].label)) {
                    s.report(warning(tas, "Field name `<l[0].label>` ignored"));
                    return areified(l[0]);
                } else {
                    return areified(l[0]);
                } 
             });    
    } else {
        c.report(error(current, "Non-well-formed type, type should have one type argument"));
    }
    
    collect(tas, c);
}      

// ---- function type ---------------------------------------------------------

AType(Solver s) makeGetTypeArg(TypeArg targ)
    = AType(Solver s) { return s.getType(targ.\type)[label="<targ.name>"]; };

@doc{Convert Rascal function types into their abstract representation.}
void collect(current: (FunctionType) `<Type t> ( <{TypeArg ","}* tas> )`, Collector c) {
    targs = [ta | ta <- tas];
    
    for(targ <- targs){
        if(targ has name){
            c.define("<targ.name>", variableId(), targ.name, defType([targ.\type], makeGetTypeArg(targ)));
            c.fact(targ, targ.name);
        }
        collect(targ.\type, c);
    }
    
    c.calculate("function type", current, t + targs,
        AType(Solver s){
            l =  l = [s.getType(ta) | ta <- targs];
            tp = s.getType(t);
            if (size(l) == 0) {
                return afunc(tp, atypeList([]), []);
            } else {
                labelsList = [tp.label | tp <- l];;
                nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
                distinctLabels = toSet(nonEmptyLabels);
                if(size(distinctLabels) == 0)
                    return afunc(tp, atypeList(l), []);
                if (size(l) == size(distinctLabels)) {
                    return afunc(tp, atypeList(l), []);
                } else if (size(distinctLabels) > 0 && size(distinctLabels) != size(labelsList)) {
                    s.report(error(current, "Non-well-formed type, labels must be distinct"));
                    return afunc(tp, atypeList([unset(tp, "label") | tp <- l]), []);
                } else if (size(l) > 0) {
                    s.report(warning(current, "Field name ignored, field names must be provided for all fields or for none"));
                    return afunc(tp, atypeList([unset(tp, "label") | tp <- l]), []);
                }
            }
        }); 
    collect(t, c);
}

// ---- user defined type -----------------------------------------------------

@doc{Convert Rascal user types into their abstract representation.}
void collect(current:(UserType) `<QualifiedName n>`, Collector c){
    <qualifier, base> = splitQualifiedName(n);
    if(isEmpty(qualifier)){
        c.use(n, {dataId(), aliasId(), lexicalId(), nonterminalId(), keywordId(), layoutId()});
    } else {
        c.useQualified([qualifier, base], n, {dataId(), aliasId(), lexicalId(), nonterminalId(), keywordId(), layoutId()}, dataOrSyntaxIds + {moduleId()});
    }
    
    c.calculate("type without parameters", current, [n],
        AType(Solver s){
            baseType = s.getType(n);
            if(aadt(adtName, params, sr) := baseType){
                nformals = size(baseType.parameters);
                if(nformals > 0) s.report(error(n, "Expected %v type parameter(s) for %q, found 0", nformals, adtName));
                return baseType;
            } else if(aalias(aname, params, aliased) := baseType){
                nformals = size(baseType.parameters);
                if(nformals > 0) s.report(error(n, "Expected %v type parameter(s) for %q, found 0", nformals, aname));
                neededTypeParams = collectRascalTypeParams(aliased);
                if(!isEmpty(neededTypeParams))
                    s.report(error(n, "Type variables in aliased type %t are unbound", aliased));
                return aliased;
            } else {
                return baseType;
            }
        });
}

void collect(current:(UserType) `<QualifiedName n>[ <{Type ","}+ ts> ]`, Collector c){
    <qualifier, base> = splitQualifiedName(n);
    if(isEmpty(qualifier)){
        c.use(n, {dataId(), aliasId(), lexicalId(), nonterminalId(), keywordId(), layoutId()});
    } else {
        c.useQualified([qualifier, base], n, {dataId(), aliasId(), lexicalId(), nonterminalId(), keywordId(), layoutId()}, dataOrSyntaxIds + {moduleId()});
    }
    actuals = [t | t <- ts];
    
    c.calculate("parameterized type with parameters", current, n + actuals,
        AType(Solver s){
            nactuals = size(actuals);
            baseType = s.getType(n);
            if(aadt(adtName, params, sr) := baseType){
                nformals = size(baseType.parameters);
                if(nactuals != nformals) s.report(error(ts, "Expected %v type parameter(s) for %v, found %v", nformals, adtName, nactuals));
                bindings = (params[i].pname : s.getType(actuals[i]) | i <- index(params));
                return xxInstantiateRascalTypeParameters(baseType, bindings, s);
            } else if(aalias(aname, params, aliased) := baseType){
                nformals = size(baseType.parameters);
                if(nactuals != nformals) s.report(error(ts, "Expected %v type parameter(s) for %v, found %v", nformals, aname, nactuals));
                
                bindings = (params[i].pname : s.getType(actuals[i]) | i <- index(params));
                return xxInstantiateRascalTypeParameters(aliased, bindings, s);
            }
            s.report(error(n, "Type %t cannot be parameterized, found %v parameter(s)", n, nactuals));
        });
    collect(ts, c);
}

// ---- Sym types -------------------------------------------------------------


AType getSyntaxType(AType t, Solver s) = stripStart(t);

AType getSyntaxType(Tree tree, Solver s) = stripStart(s.getType(tree));

private AType stripStart(AType nt) = isStartNonTerminalType(nt) ? getStartNonTerminalType(nt) : nt;

private AType stripStart(aprod(AProduction production)) = production.def;


// named non-terminals
void collect(current:(Sym) `<Nonterminal n>`, Collector c){
    c.use(n, syntaxIds);
    c.require("non-parameterized <n>", current, [n],
        void(Solver s){
            base = getSyntaxType(n, s);
            s.requireTrue(isNonTerminalType(base), error(current, "Expected a non-terminal type, found %t", base));
            nexpected = size(getADTTypeParameters(base));
            s.requireTrue(nexpected == 0, error(current, "Expected %v type parameter(s) for %q, found 0", nexpected, getADTName(base)));
        });
    //c.fact(current, n);
}

void collect(current:(Sym) `& <Nonterminal n>`, Collector c){
    c.use(n, {typeVarId()});
    c.fact(current, n);
}

void collect(current:(Sym) `<Nonterminal n>[ <{Sym ","}+ parameters> ]`, Collector c){
    params = [p | p <- parameters];
    c.use(n, syntaxIds);
    c.calculate("parameterized <n>", current, n + params, 
        AType(Solver s) { 
            base = getSyntaxType(n, s); 
            s.requireTrue(isParameterizedNonTerminalType(base), error(current, "Expected a non-terminal type, found %t", base));
            nexpected = size(getADTTypeParameters(base)); nparams = size(params);
            s.requireTrue(nexpected == nparams, error(current, "Expected %v type parameter(s) for %q, found %v", nexpected, getADTName(base), nparams));
            base.parameters = [s.getType(p) | p <- params]; // TODO: what to do when base == start(...)?
            return base;
        });
    collect(params, c);
}

void collect(current:(Sym) `start [ <Nonterminal n> ]`, Collector c){
    c.use(n, syntaxIds);
    c.calculate("start <n>", current, [n],
        AType(Solver s){
            adtType = getSyntaxType(n, s);
            s.requireTrue(isNonTerminalType(adtType), error(current, "Expected a non-terminal type, found %t", adtType));
            return \start(adtType);
        });
    collect(n, c);
}

void collect(current:(Sym) `<Sym symbol> <NonterminalLabel n>`, Collector c){
    un = unescape("<n>");
    // TODO require symbol is nonterminal
    c.define(un, fieldId(), n, defType([symbol], AType(Solver s){ 
        return getSyntaxType(symbol, s)[label=un]; }));
    c.fact(current, n);
    collect(symbol, c);
}

// literals

void collect(current:(Sym) `<Class cc>`, Collector c){
    c.fact(current, cc2ranges(cc));
}

void collect(current:(Sym) `<StringConstant l>`, Collector c){
    c.fact(current, AType::lit(unescapeLiteral(l)));
}

void collect(current:(Sym) `<CaseInsensitiveStringConstant l>`, Collector c){
    c.fact(current, AType::cilit(unescapeLiteral(l)));
}

// regular expressions

bool isIterSym((Sym) `<Sym symbol>+`) = true;
bool isIterSym((Sym) `<Sym symbol>*`) = true;
bool isIterSym((Sym) `{ <Sym symbol> <Sym sep> }+`) = true;
bool isIterSym((Sym) `{ <Sym symbol> <Sym sep> }*`) = true;
default bool isIterSym(Sym sym) = false;


void collect(current:(Sym) `<Sym symbol>+`, Collector c){
    if(isIterSym(symbol)) c.report(warning(current, "Nested iteration"));
    c.calculate("iter", current, [symbol], AType(Solver s) { 
        return \iter(getSyntaxType(symbol, s)); });
    collect(symbol, c);
}

void collect(current:(Sym) `<Sym symbol>*`, Collector c){
    if(isIterSym(symbol)) c.report(warning(current, "Nested iteration"));
    c.calculate("iterStar", current, [symbol], AType(Solver s) { return \iter-star(getSyntaxType(symbol, s)); });
    collect(symbol, c);
}

void collect(current:(Sym) `{ <Sym symbol> <Sym sep> }+`, Collector c){
    if(isIterSym(symbol)) c.report(warning(current, "Nested iteration"));
     c.calculate("iterSep", current, [symbol, sep], 
        AType(Solver s) { 
            seps = [s.getType(sep)];
            validateSeparators(current, seps, s);
            return \iter-seps(getSyntaxType(symbol, s), seps); 
        });
    collect(symbol, sep, c);
}

void collect(current:(Sym) `{ <Sym symbol> <Sym sep> }*`, Collector c){
    if(isIterSym(symbol)) c.report(warning(current, "Nested iteration"));
    c.calculate("iterStarSep", current, [symbol, sep], 
        AType(Solver s) { 
            seps = [s.getType(sep)];
            validateSeparators(current, seps, s);
            return \iter-star-seps(getSyntaxType(symbol, s), seps); 
        });
    collect(symbol, sep, c);
}

void validateSeparators(Tree current, list[AType] separators, Solver s){
    if(all(sep <- separators, isLayoutType(sep)))
        s.report(error(current, "At least one element of separators should be non-layout"));
    forbidConsecutiveLayout(current, separators, s);
}

void forbidConsecutiveLayout(Tree current, list[AType] symbols, Solver s){
    if([*_,t1, t2,*_] := symbols, isLayoutType(t1), isLayoutType(t2)){
       s.report(error(current, "Consecutive layout types %t and %t not allowed", t1, t2));
    }
}

void requireNonLayout(Tree current, AType u, str msg, Solver s){
    if(isLayoutType(u)) s.report(error(current, "Layout type %t not allowed %v", u, msg));
}

void collect(current:(Sym) `<Sym symbol>?`, Collector c){
    c.calculate("optional", current, [symbol], AType(Solver s) { return \opt(s.getType(symbol)); });
    collect(symbol, c);
}

void collect(current:(Sym) `( <Sym first> | <{Sym "|"}+ alternatives> )`, Collector c){
    alts = first + [alt | alt <- alternatives];
    c.calculate("alternative", current, alts, AType(Solver s) { return \alt({s.getType(alt) | alt <- alts}); });
    collect(alts, c);
}

void collect(current:(Sym) `( <Sym first> <Sym+ sequence> )`, Collector c){
    seqs = first + [seq | seq <- sequence];
    c.calculate("sequence", current, seqs, 
        AType(Solver s) { 
            symbols = [s.getType(seq) | seq <- seqs];
            forbidConsecutiveLayout(current, symbols, s);
            return \seq(symbols); 
        });
    collect(seqs, c);
}

void collect(current:(Sym) `()`, Collector c){
    c.fact(current, AType::empty());
}

// conditionals

void collect(current:(Sym) `<Sym symbol> @ <IntegerLiteral column>`, Collector c){
    c.calculate("column", current, [symbol], AType(Solver s) { return AType::conditional(s.getType(symbol), {ACondition::\at-column(toInt("<column>")) }); });
    collect(symbol, c);
}

void collect(current:(Sym) `<Sym symbol> $`, Collector c){
    c.calculate("end-of-line", current, [symbol], AType(Solver s) { return AType::conditional(s.getType(symbol), {ACondition::\end-of-line() }); });
    collect(symbol, c);
}

void collect(current:(Sym) `^ <Sym symbol>`, Collector c){
    c.calculate("begin-of-line", current, [symbol], AType(Solver s) { return AType::conditional(s.getType(symbol), {ACondition::\begin-of-line() }); });
    collect(symbol, c);
}

void collect(current:(Sym) `<Sym symbol> ! <NonterminalLabel n>`, Collector c){
    un = unescape("<n>");
    c.calculate("except", current, [symbol], AType(Solver s) { return AType::conditional(s.getType(symbol), {ACondition::\except(un) }); });
    collect(symbol, c);
}

void collect(current:(Sym) `<Sym symbol>  \>\> <Sym match>`, Collector c){
   c.calculate("follow", current, [symbol, match], AType(Solver s) { return AType::conditional(s.getType(symbol), {ACondition::\follow(s.getType(match)) }); });
   collect(symbol, match, c);
}

void collect(current:(Sym) `<Sym symbol>  !\>\> <Sym match>`, Collector c){
   c.calculate("follow", current, [symbol, match], AType(Solver s) { return AType::conditional(s.getType(symbol), {ACondition::\not-follow(s.getType(match)) }); });
   collect(symbol, match, c);
}

void collect(current:(Sym) `<Sym match>  \<\< <Sym symbol>`, Collector c){
   c.calculate("precede", current, [match, symbol], AType(Solver s) { return AType::conditional(s.getType(symbol), {ACondition::\precede(s.getType(match)) }); });
   collect(match, symbol, c);
}

void collect(current:(Sym) `<Sym match>  !\<\< <Sym symbol>`, Collector c){
   c.calculate("notPrecede", current, [match, symbol], AType(Solver s) { return AType::conditional(s.getType(symbol), {ACondition::\not-precede(s.getType(match)) }); });
   collect(match, symbol, c);
}

void collect(current:(Sym) `<Sym symbol> \\ <Sym match>`, Collector c){
   c.calculate("unequal", current, [symbol, match], 
        AType(Solver s) { 
            t = s.getType(match);
            if(lit(_) !:= t && (t has syntaxRole && t.syntaxRole != keywordSyntax())){
                s.report(error(match, "Exclude `\\` requires keywords as right argument, found %t", match));
            }
            return AType::conditional(s.getType(symbol), {ACondition::\delete(s.getType(match)) });
        });
   collect(symbol, match, c);
}

void collect(Sym current, Collector c){
    throw "collect Sym, missed case <current>";
}

@doc{Convert Rascal type variables into their abstract representation.}

void collect(current:(TypeVar) `& <Name n>`, Collector c){
    c.fact(current, aparameter(prettyPrintName(n),avalue()));
}

void collect(current: (TypeVar) `& <Name n> \<: <Type tp>`, Collector c){
    c.calculate("type parameter with bound", current, [tp], AType(Solver s){ return  aparameter(prettyPrintName(n), s.getType(tp)); }); 
    collect(tp, c);
}

@doc{Convert Rascal data type selectors into an abstract representation.}
@todo{Implement once this is in use.}
public AType convertDataTypeSelector(DataTypeSelector dts, Collector c) {
    switch(dts) {
        case (DataTypeSelector) `<QualifiedName n1> . <Name n2>` : throw "Not implemented";
    }
}

@doc{A parsing function, useful for generating test cases.}
public Type parseType(str s) {
    return parse(#Type, s);
}