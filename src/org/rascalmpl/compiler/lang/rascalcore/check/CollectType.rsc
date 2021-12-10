@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl (CWI)}
@bootstrapParser
module lang::rascalcore::check::CollectType

extend lang::rascalcore::check::ATypeInstantiation;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Characters;
import lang::rascalcore::grammar::definition::Literals;

//import IO;
import List;
import Node;
import Set;
import String;

void collect(current: (Type) `( <Type tp> )`, Collector c){
    c.fact(current, tp);
    collect(tp, c);
}

// ---- basic types -----------------------------------------------------------

@doc{Convert from the concrete to the abstract representations of Rascal basic types.}

// ---- bool
void collect(current: (BasicType)`bool`, Collector c){ c.fact(current, abool()); }

// ---- int
void collect(current: (BasicType)`int`, Collector c){ c.fact(current, aint()); }

// ---- rat
void collect(current: (BasicType)`rat`, Collector c){ c.fact(current, arat()); }

// ---- real
void collect(current: (BasicType)`real`, Collector c){ c.fact(current, areal()); }

// ---- num
void collect(current: (BasicType)`num`, Collector c){ c.fact(current, anum()); }

// ---- str
void collect(current: (BasicType)`str`, Collector c){ c.fact(current, astr()); }

// ---- value
void collect(current: (BasicType)`value`, Collector c){ c.fact(current, avalue()); }

// ---- node
void collect(current: (BasicType)`node`, Collector c){ c.fact(current, anode([])); }

// ---- void
void collect(current: (BasicType)`void`, Collector c){ c.fact(current, avoid()); }

// ---- loc
void collect(current: (BasicType)`loc`, Collector c){ c.fact(current, aloc()); }

// ---- datetime
void collect(current: (BasicType)`datetime`, Collector c){ c.fact(current, adatetime()); }

default void collect(BasicType bt, Collector c) { c.report(error(bt, "Illegal use of type `<bt>`")); }

// ---- TypeArgs -------------------------------------------------------------

void collect(current: (TypeArg) `<Type tp>`, Collector c){
    collect(tp, c);
    c.fact(current, tp);
}

void collect(current: (TypeArg) `<Type tp> <Name name>`, Collector c){
    collect(tp, c);
    try {
        c.fact(name, c.getType(tp)[label=unescape("<name>")]);
    } catch TypeUnavailable(): {
        c.calculate("TypeArg <name>", name, [tp], AType(Solver s){
           return (s.getType(tp)[label=unescape("<name>")]);
         });
    }
    c.fact(current, name);
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

// ---- list

void collect(current:(Type)`list [ < {TypeArg ","}+ tas > ]`, Collector c){
   targs = [ta | ta <- tas];
    
   collect(targs[0], c);
   try {
        c.fact(current, makeListType(c.getType(targs[0])));
   } catch TypeUnavailable():{
        c.calculate("list type", current, targs, AType(Solver s){ return makeListType(s.getType(targs[0])); });
   }
   if(size(targs) != 1){
        c.report(error(current, "Type `list` should have one type argument"));
   }
}

// ---- set

void collect(current:(Type)`set [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    collect(targs[0], c);
  
    try {
        c.fact(current, makeSetType(c.getType(targs[0])));
    } catch TypeUnavailable():{
        c.calculate("set type", current, targs, AType(Solver s){ return makeSetType(s.getType(targs[0])); });
    }
    if(size(targs) != 1){
        c.report(error(current, "Type `set` should have one type argument"));
    }
}

// ---- bag TODO

void collect(current:(Type)`bag [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    collect(targs[0], c); 
    try {
        c.fact(current, makeBagType(c.getType(targs[0])));
    } catch TypeUnavailable(): {
        c.calculate("bag type", current, targs, AType(Solver s){ return makeBagType(s.getType(targs[0])); });
    }
    if(size(targs) != 1){
        c.report(error(current, "Type `bag` should have one type argument"));
    }
}

// ---- map

tuple[list[FailMessage] msgs, AType atype] handleMapFields({TypeArg ","}+ tas, AType dt, AType rt){
    if (!isEmpty(dt.label) && !isEmpty(rt.label) && dt.label != rt.label) { 
        return <[], makeMapType(dt, rt)>;
    } else if (!isEmpty(dt.label) && !isEmpty(rt.label) && dt.label == rt.label) {
        return <[error(tas,"Non-well-formed map type, labels must be distinct")], makeMapType(unset(dt, "label"),unset(rt,"label"))>;
    } else if (!isEmpty(dt.label) && isEmpty(rt.label)) {
        return <[warning(tas, "Field name `<dt.label>` ignored, field names must be provided for both fields or for none")], makeMapType(unset(dt, "label"),rt)>;
    } else if (isEmpty(dt.label) && !isEmpty(rt.label)) {
        return <[warning(tas, "Field name `<rt.label>` ignored, field names must be provided for both fields or for none")], makeMapType(dt, unset(rt, "label"))>;
    } else {
        return <[], makeMapType(dt,rt)>;
    }
}

void collect(current:(Type)`map [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    
    if(size(targs) != 2){
        c.report(error(current, "Type `map` should have two type arguments"));
        c.fact(current, amap(avalue(), avalue()));
        return;
    }
    collect(targs, c);
   
    try {
        <msgs, result> = handleMapFields(tas, c.getType(targs[0]), c.getType(targs[1]));
        for(m <- msgs) c.report(m);
        c.fact(current, result);
    } catch TypeUnavailable(): {
        c.calculate("map type", current, targs[0..2], 
            AType(Solver s){
                <msgs, result> = handleMapFields(tas, s.getType(targs[0]), s.getType(targs[1]));
                for(m <- msgs) s.report(m);
                return result;                    
        });
    }
    
}

// ---- rel

tuple[list[FailMessage] msgs, AType atype] handleRelFields({TypeArg ","}+ tas, list[AType] fieldTypes){
    labelsList = [tp.label | tp <- fieldTypes];
    nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
    distinctLabels = toSet(nonEmptyLabels);
    if (size(fieldTypes) == size(distinctLabels)){
        return <[], makeRelType(fieldTypes)>;
    } else if(size(distinctLabels) == 0) {
        return <[], makeRelType(fieldTypes)>;
    } else if (size(distinctLabels) != size(nonEmptyLabels)) {
        return <[error(tas, "Non-well-formed relation type, labels must be distinct")], makeRelType([unset(tp, "label") | tp <- fieldTypes])>;
    } else if (size(distinctLabels) > 0) {
        return <[warning(tas, "Field name ignored, field names must be provided for all fields or for none")], makeRelType([unset(tp, "label") | tp <- fieldTypes])>;
    }
    return <[], avoid()>;
}

void collect(current:(Type)`rel [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    collect(tas, c);
    try {
        <msgs, result> = handleRelFields(tas,  [c.getType(ta) | ta <- targs]);
        for(m <- msgs) c.report(m);
        c.fact(current, result);
    } catch TypeUnavailable(): {
        c.calculate("rel type", current, targs, 
            AType(Solver s){
                 <msgs, result> = handleRelFields(tas,  [s.getType(ta) | ta <- targs]);
                 for(m <- msgs) s.report(m);
                 return result;
            });
    }
}

// ---- lrel

tuple[list[FailMessage] msgs, AType atype] handleListRelFields({TypeArg ","}+ tas, list[AType] fieldTypes){
    labelsList = [tp.label | tp <- fieldTypes];
    nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
    distinctLabels = toSet(nonEmptyLabels);
    if (size(fieldTypes) == size(distinctLabels)){
        return <[], makeListRelType(fieldTypes)>;
    } else if(size(distinctLabels) == 0) {
        return <[], makeListRelType(fieldTypes)>;
    } else if (size(distinctLabels) != size(nonEmptyLabels)) {
        return <[error(tas, "Non-well-formed list relation type, labels must be distinct")], makeListRelType([unset(tp, "label") | tp <- fieldTypes])>;
    } else if (size(distinctLabels) > 0) {
        return <[warning(tas, "Field name ignored, field names must be provided for all fields or for none")], makeListRelType([unset(tp, "label") | tp <- fieldTypes])>;
    }
    return <[], avoid()>;
}

void collect(current:(Type)`lrel [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    collect(tas, c);
    try {
        <msgs, result> = handleListRelFields(tas,  [c.getType(ta) | ta <- targs]);
        for(m <- msgs) c.report(m);
        c.fact(current, result);
    } catch TypeUnavailable(): {
        c.calculate("lrel type", current, targs, 
            AType(Solver s){
                 <msgs, result> = handleListRelFields(tas,  [s.getType(ta) | ta <- targs]);
                 for(m <- msgs) s.report(m);
                 return result;
            });
    }
}

// ---- tuple

tuple[list[FailMessage] msgs, AType atype] handleTupleFields({TypeArg ","}+ tas, list[AType] fieldTypes){
    labelsList = [tp.label | tp <- fieldTypes];
    nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
    distinctLabels = toSet(nonEmptyLabels);
    msgs = [];
    for(int i <- index(fieldTypes)){
        if(isVoidType(fieldTypes[i])){
            msgs += error(tas, "Non-well-formed tuple type, field #%v should not be `void`", i);
        }
    }
    if (size(fieldTypes) == size(distinctLabels)){
        return <msgs, makeTupleType(fieldTypes)>;
    } else if(size(distinctLabels) == 0) {
        return <msgs, makeTupleType(fieldTypes)>;
    } else if (size(distinctLabels) != size(nonEmptyLabels)) {
        return <msgs+[error(tas, "Non-well-formed tuple type, labels must be distinct")], makeTupleType([unset(tp, "label") | tp <- fieldTypes])>;
    } else if (size(distinctLabels) > 0) {
        return <msgs+[warning(tas, "Field name ignored, field names must be provided for all fields or for none")], makeTupleType([unset(tp, "label") | tp <- fieldTypes])>;
    } 
    return <[], avoid()>; 
}

void collect(current:(Type)`tuple [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    collect(tas, c);  
    try {
        <msgs, result> = handleTupleFields(tas, [c.getType(ta) | ta <- targs]);
        for(m <- msgs) c.report(m);
        c.fact(current, result);
    } catch TypeUnavailable():{
        c.calculate("tuple type", current, targs, 
            AType(Solver s){
                <msgs, result> = handleTupleFields(tas, [s.getType(ta) | ta <- targs]);
                for(m <- msgs) s.report(m);
                return result;
            });
    }
} 

// ---- type

tuple[list[FailMessage] msgs, AType atype] handleTypeField({TypeArg ","}+ tas, AType fieldType){  
    if (!isEmpty(fieldType.label)) {
        return <[warning(tas, "Field name `<fieldType.label>` ignored")], areified(fieldType)>;
    } else {
        return <[], areified(fieldType)>;
    } 
}

void collect(current:(Type)`type [ < {TypeArg ","}+ tas > ]`, Collector c){
    targs = [ta | ta <- tas];
    collect(targs[0], c);
   
    try {
        <msgs, result> = handleTypeField(tas, c.getType(targs[0]));
        for(m <- msgs) c.report(m);
        c.fact(current, result);
    } catch TypeUnavailable(): {
        c.calculate("type type", current, targs[0..1], 
            AType(Solver s){
                <msgs, result> = handleTypeField(tas,  s.getType(targs[0]));
                for(m <- msgs) s.report(m);
                return result;
             });
    }  
    if(size(targs) != 1){
        c.report(error(current, "Non-well-formed type, type should have one type argument"));
    }
}      

// ---- function type ---------------------------------------------------------

AType(Solver _) makeGetTypeArg(TypeArg targ)
    = AType(Solver s) { return s.getType(targ.\type)[label="<targ.name>"]; };
    
tuple[list[FailMessage] msgs, AType atype] handleFunctionType({TypeArg ","}* _, AType returnType, list[AType] argTypes){  
    return <[], afunc(returnType, argTypes, [])>;
}

@doc{Convert Rascal function types into their abstract representation.}
void collect(current: (FunctionType) `<Type t> ( <{TypeArg ","}* tas> )`, Collector c) {
    targs = [ta | ta <- tas];
    
    resolvedArgTypes = [];
    for(targ <- targs){
        collect(targ.\type, c);
        try {
            argType = c.getType(targ.\type);
            c.fact(targ, argType);
            if(targ has name) {
                labelledArgType = argType[label="<targ.name>"];
                resolvedArgTypes += labelledArgType;
                c.define("<targ.name>", formalId(), targ.name, defType(labelledArgType));
                c.fact(targ, argType);
            } else {
                resolvedArgTypes += argType;
            }
        } catch TypeUnavailable(): {
            c.fact(targ, targ.\type);
            if(targ has name) {
                c.define("<targ.name>", formalId(), targ.name, defType([targ.\type], makeGetTypeArg(targ)));
                c.fact(targ, targ.name);
             }
        }
    }
    collect(t, c);
    if(size(targs) == size(resolvedArgTypes)){
        try {
            <msgs, result> = handleFunctionType(tas, c.getType(t), resolvedArgTypes);
            for(m <- msgs) c.report(m);
            c.fact(current, result);
            return;
        } catch TypeUnavailable(): /* fall through when a type is not available */;
    }
    
    c.calculate("function type", current, t + targs,
        AType(Solver s){
            <msgs, result> = handleFunctionType(tas, s.getType(t), [s.getType(ta) | ta <- targs]);
            for(m <- msgs) s.report(m);
            s.fact(current, result);
            return result;
        });
}

// ---- user defined type -----------------------------------------------------

tuple[list[FailMessage] msgs, AType atype] handleUserType(QualifiedName n, AType baseType){  
    if(aadt(adtName, _, _) := baseType){
        nformals = size(baseType.parameters);
        if(nformals > 0) return <[error(n, "Expected %v type parameter(s) for %q, found 0", nformals, adtName)], baseType>;
        return <[], baseType>;
    } else if(aalias(aname, _, aliased) := baseType){
        nformals = size(baseType.parameters);
        msgs = [];
        if(nformals > 0) msgs += error(n, "Expected %v type parameter(s) for %q, found 0", nformals, aname);
        neededTypeParams = collectRascalTypeParams(aliased);
        if(!isEmpty(neededTypeParams))
           msgs += error(n, "Type variables in aliased type %t are unbound", aliased);
        return<msgs, aliased>;
    } else {
        return <[], baseType>;
    }
}

@doc{Convert Rascal user types into their abstract representation.}
void collect(current:(UserType) `<QualifiedName n>`, Collector c){
    <qualifier, base> = splitQualifiedName(n);
    if(isEmpty(qualifier)){
        c.use(n, {dataId(), aliasId(), lexicalId(), nonterminalId(), keywordId(), layoutId()});
    } else {
        c.useQualified([qualifier, base], n, {dataId(), aliasId(), lexicalId(), nonterminalId(), keywordId(), layoutId()}, dataOrSyntaxRoles + {moduleId()});
    }
    
    try {
        <msgs, result> = handleUserType(n,  c.getType(n));
        for(m <- msgs) c.report(m);
        c.fact(current, result);
    } catch TypeUnavailable(): 
    
    c.calculate("type without parameters", current, [n],
        AType(Solver s){
            <msgs, result> = handleUserType(n, s.getType(n));
            for(m <- msgs) s.report(m);
            return result;
        });
}

void collect(current:(UserType) `<QualifiedName n>[ <{Type ","}+ ts> ]`, Collector c){
    <qualifier, base> = splitQualifiedName(n);
    if(isEmpty(qualifier)){
        c.use(n, {dataId(), aliasId(), lexicalId(), nonterminalId(), keywordId(), layoutId()});
    } else {
        c.useQualified([qualifier, base], n, {dataId(), aliasId(), lexicalId(), nonterminalId(), keywordId(), layoutId()}, dataOrSyntaxRoles + {moduleId()});
    }
    actuals = [t | t <- ts];
    
    c.calculate("parameterized type with parameters", current, n + actuals,
        AType(Solver s){
            nactuals = size(actuals);
            baseType = s.getType(n);
            if(aadt(adtName, params, _) := baseType){
                nformals = size(baseType.parameters);
                if(nactuals != nformals) s.report(error(ts, "Expected %v type parameter(s) for %v, found %v", nformals, adtName, nactuals));
                bindings = (params[i].pname : s.getType(actuals[i]) | i <- index(params));
                return xxInstantiateRascalTypeParameters(ts, baseType, bindings, s);
            } else if(aalias(aname, params, aliased) := baseType){
                nformals = size(baseType.parameters);
                if(nactuals != nformals) s.report(error(ts, "Expected %v type parameter(s) for %v, found %v", nformals, aname, nactuals));
                
                bindings = (params[i].pname : s.getType(actuals[i]) | i <- index(params));
                return xxInstantiateRascalTypeParameters(ts, aliased, bindings, s);
            }
            s.report(error(n, "Type %t cannot be parameterized, found %v parameter(s)", n, nactuals));
            return avoid();
        });
    collect(ts, c);
}

// ---- Sym types -------------------------------------------------------------

// ---- named non-terminals

void collect(current:(Sym) `<Nonterminal n>`, Collector c){
    c.use(n, syntaxRoles);
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
    c.use(n, syntaxRoles);
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
    c.use(n, syntaxRoles);
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

// ---- literals

void collect(current:(Sym) `<Class cc>`, Collector c){
    c.fact(current, cc2ranges(cc));
}

void collect(current:(Sym) `<StringConstant l>`, Collector c){
    c.fact(current, AType::lit(unescapeLiteral(l)));
}

void collect(current:(Sym) `<CaseInsensitiveStringConstant l>`, Collector c){
    c.fact(current, AType::cilit(unescapeLiteral(l)));
}

// ---- regular expressions

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

void collect(current:(Sym) `<Sym symbol>*`, Collector c) {
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
        s.report(warning(current, "At least one element of separators should be non-layout")); // TODO make error
    forbidConsecutiveLayout(current, separators, s);
}

void forbidConsecutiveLayout(Tree current, list[AType] symbols, Solver s){
    if([*_,t1, t2,*_] := symbols, isLayoutType(t1), isLayoutType(t2)){
       s.report(error(current, "Consecutive layout types %t and %t not allowed", t1, t2));
    }
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

// ---- conditionals

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
    // TODO: c.use(n, {productionId()});
    un = unescape("<n>");
    c.calculate("except", current, [symbol], AType(Solver s) { return AType::conditional(s.getType(symbol), {ACondition::\except(un) }); });
    collect(symbol, c);
}

bool isTerminal(Sym s)
    = s is literal || s is caseInsensitiveLiteral || s is characterClass;

void collect(current:(Sym) `<Sym symbol>  \>\> <Sym match>`, Collector c){
   c.calculate("follow", current, [symbol, match], 
        AType(Solver s) { 
            s.requireTrue(isTerminal(match), warning(match, "Followed By (`\>\>`) requires literal or character class, found %v", match));
            return AType::conditional(s.getType(symbol), {ACondition::\follow(s.getType(match)) }); 
        });
   collect(symbol, match, c);
}

void collect(current:(Sym) `<Sym symbol>  !\>\> <Sym match>`, Collector c){
   c.calculate("notFollow", current, [symbol, match], 
        AType(Solver s) { 
            s.requireTrue(isTerminal(match), warning(match, "Not Followed By (`!\>\>`) requires literal or character class, found %v", match));
            return AType::conditional(s.getType(symbol), {ACondition::\not-follow(s.getType(match)) }); 
        });
   collect(symbol, match, c);
}

void collect(current:(Sym) `<Sym match>  \<\< <Sym symbol>`, Collector c){
   c.calculate("precede", current, [match, symbol], 
        AType(Solver s) { 
            s.requireTrue(isTerminal(match), warning(match, "Preceded By (`\<\<`) requires literal or character class, found %v", match));
            return AType::conditional(s.getType(symbol), {ACondition::\precede(s.getType(match)) }); 
        });
   collect(match, symbol, c);
}

void collect(current:(Sym) `<Sym match>  !\<\< <Sym symbol>`, Collector c){
   c.calculate("notPrecede", current, [match, symbol], 
        AType(Solver s) { 
            s.requireTrue(isTerminal(match), warning(match, "Not Preceded By (`!\<\<`) requires literal or character class, found %v", match));
            return AType::conditional(s.getType(symbol), {ACondition::\not-precede(s.getType(match)) }); 
        });
   collect(match, symbol, c);
}

void collect(current:(Sym) `<Sym symbol> \\ <Sym match>`, Collector c){
   c.calculate("unequal", current, [symbol, match], 
        AType(Solver s) { 
            t = s.getType(match);
            if(lit(_) !:= t && (t has syntaxRole && t.syntaxRole != keywordSyntax())){
                s.report(error(match, "Exclude (`\\`) requires keywords as right argument, found %t", match));
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
    collect(tp, c);
    try {
        c.fact(current,  aparameter(prettyPrintName(n), c.getType(tp)));
    } catch TypeUnavailable(): {
        c.calculate("type parameter with bound", current, [tp], AType(Solver s){ return  aparameter(prettyPrintName(n), s.getType(tp)); });  
    }
}

@doc{A parsing function, useful for generating test cases.}
public Type parseType(str s) {
    return parse(#Type, s);
}