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

/*
    Check type declarations
*/

extend lang::rascalcore::check::ATypeInstantiation;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Characters;
import lang::rascalcore::grammar::definition::Literals;
import lang::rascalcore::check::ScopeInfo;

import IO;
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

void collect(BasicType bt, Collector c) { collectBasicType(bt, c); }

// ---- bool
void collectBasicType(current: (BasicType)`bool`, Collector c){ c.fact(current, abool()); }

// ---- int
void collectBasicType(current: (BasicType)`int`, Collector c){ c.fact(current, aint()); }

// ---- rat
void collectBasicType(current: (BasicType)`rat`, Collector c){ c.fact(current, arat()); }

// ---- real
void collectBasicType(current: (BasicType)`real`, Collector c){ c.fact(current, areal()); }

// ---- num
void collectBasicType(current: (BasicType)`num`, Collector c){ c.fact(current, anum()); }

// ---- str
void collectBasicType(current: (BasicType)`str`, Collector c){ c.fact(current, astr()); }

// ---- value
void collectBasicType(current: (BasicType)`value`, Collector c){ c.fact(current, avalue()); }

// ---- node
void collectBasicType(current: (BasicType)`node`, Collector c){ c.fact(current, anode([])); }

// ---- void
void collectBasicType(current: (BasicType)`void`, Collector c){ c.fact(current, avoid()); }

// ---- loc
void collectBasicType(current: (BasicType)`loc`, Collector c){ c.fact(current, aloc()); }

// ---- datetime
void collectBasicType(current: (BasicType)`datetime`, Collector c){ c.fact(current, adatetime()); }

default void collectBasicType(BasicType bt, Collector c) { c.report(error(bt, "Illegal use of type `<bt>`")); }

// ---- TypeArgs -------------------------------------------------------------

void collect(current: (TypeArg) `<Type tp>`, Collector c){
    //c.push(currentAdt, <current, [tp], c.getScope()>);
        collect(tp, c);
    //c.pop(currentAdt);
    c.fact(current, tp);
}

void collect(current: (TypeArg) `<Type tp> <Name name>`, Collector c){
    //c.push(currentAdt, <current, [tp], c.getScope()>);
        collect(tp, c);
    //c.pop(currentAdt);
    try {
        c.fact(name, c.getType(tp)[alabel=unescape("<name>")]);
    } catch TypeUnavailable(): {
        c.calculate("TypeArg <name>", name, [tp], AType(Solver s){
           return (s.getType(tp)[alabel=unescape("<name>")]);
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
    if (!isEmpty(dt.alabel) && !isEmpty(rt.alabel) && dt.alabel != rt.alabel) { 
        return <[], makeMapType(dt, rt)>;
    } else if (!isEmpty(dt.alabel) && !isEmpty(rt.alabel) && dt.alabel == rt.alabel) {
        return <[error(tas,"Non-well-formed map type, labels must be distinct")], makeMapType(unset(dt, "alabel"),unset(rt,"alabel"))>;
    } else if (!isEmpty(dt.alabel) && isEmpty(rt.alabel)) {
        return <[warning(tas, "Field name `<dt.alabel>` ignored, field names must be provided for both fields or for none")], makeMapType(unset(dt, "alabel"),rt)>;
    } else if (isEmpty(dt.alabel) && !isEmpty(rt.alabel)) {
        return <[warning(tas, "Field name `<rt.alabel>` ignored, field names must be provided for both fields or for none")], makeMapType(dt, unset(rt, "alabel"))>;
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
    //c.push(currentAdt, <current, targs, c.getScope()>);
        collect(targs, c);
    //c.pop(currentAdt);
   
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
    labelsList = [tp.alabel | tp <- fieldTypes];
    nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
    distinctLabels = toSet(nonEmptyLabels);
    if (size(fieldTypes) == size(distinctLabels)){
        return <[], makeRelType(fieldTypes)>;
    } else if(size(distinctLabels) == 0) {
        return <[], makeRelType(fieldTypes)>;
    } else if (size(distinctLabels) != size(nonEmptyLabels)) {
        return <[error(tas, "Non-well-formed relation type, labels must be distinct")], makeRelType([unset(tp, "alabel") | tp <- fieldTypes])>;
    } else if (size(distinctLabels) > 0) {
        return <[warning(tas, "Field name ignored, field names must be provided for all fields or for none")], makeRelType([unset(tp, "alabel") | tp <- fieldTypes])>;
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
    labelsList = [tp.alabel | tp <- fieldTypes];
    nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
    distinctLabels = toSet(nonEmptyLabels);
    if (size(fieldTypes) == size(distinctLabels)){
        return <[], makeListRelType(fieldTypes)>;
    } else if(size(distinctLabels) == 0) {
        return <[], makeListRelType(fieldTypes)>;
    } else if (size(distinctLabels) != size(nonEmptyLabels)) {
        return <[error(tas, "Non-well-formed list relation type, labels must be distinct")], makeListRelType([unset(tp, "alabel") | tp <- fieldTypes])>;
    } else if (size(distinctLabels) > 0) {
        return <[warning(tas, "Field name ignored, field names must be provided for all fields or for none")], makeListRelType([unset(tp, "alabel") | tp <- fieldTypes])>;
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
    labelsList = [tp.alabel | tp <- fieldTypes];
    nonEmptyLabels = [ lbl | lbl <- labelsList, !isEmpty(lbl) ];
    distinctLabels = toSet(nonEmptyLabels);
    msgs = [];
    for(int i <- index(fieldTypes)){
        if(isVoidAType(fieldTypes[i])){
            msgs += error(tas, "Non-well-formed tuple type, field #%v should not be `void`", i);
        }
    }
    if (size(fieldTypes) == size(distinctLabels)){
        return <msgs, makeTupleType(fieldTypes)>;
    } else if(size(distinctLabels) == 0) {
        return <msgs, makeTupleType(fieldTypes)>;
    } else if (size(distinctLabels) != size(nonEmptyLabels)) {
        return <msgs+[error(tas, "Non-well-formed tuple type, labels must be distinct")], makeTupleType([unset(tp, "alabel") | tp <- fieldTypes])>;
    } else if (size(distinctLabels) > 0) {
        return <msgs+[warning(tas, "Field name ignored, field names must be provided for all fields or for none")], makeTupleType([unset(tp, "alabel") | tp <- fieldTypes])>;
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
    if (!isEmpty(fieldType.alabel)) {
        return <[warning(tas, "Field name `<fieldType.alabel>` ignored")], areified(fieldType)>;
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
    = AType(Solver s) { return s.getType(targ.\type)[alabel="<targ.name>"]; };
    
tuple[list[FailMessage] msgs, AType atype] handleFunctionType({TypeArg ","}* _, AType returnType, list[AType] argTypes){  
    return <[], afunc(returnType, argTypes, [])>;
}

@doc{Convert Rascal function types into their abstract representation.}
void collect(current: (FunctionType) `<Type t> ( <{TypeArg ","}* tas> )`, Collector c) {
    
    //println("collect: <current>");
    // return type, any type parameters should be closed
    beginUseTypeParameters(c, closed=true);
        collect(t, c);
    endUseTypeParameters(c);
    
    targs = [ta | ta <- tas];
    //<inBody, tpbounds> = useBoundedTypeParameters(c);
    //println("collect: <current>, inBody: <inBody>");
    // When a function type occurs in a body, just use closed type parameters
    /*if(inBody) beginUseTypeParameters(c, closed=true); else */beginDefineOrReuseTypeParameters(c,closed=false);
        for(targ <- targs){
            collect(targ.\type, c);
            c.fact(targ, targ.\type);
            if(targ has name) {
                c.define("<targ.name>", formalId(), targ.name, defType([targ.\type], makeGetTypeArg(targ)));
                c.fact(targ, targ.name);
             }
        }
    /*if(inBody) endUseTypeParameters(c); else */endDefineOrReuseTypeParameters(c);
    
    c.calculate("function type", current, t + targs,
        AType(Solver s){
            <msgs, result> = handleFunctionType(tas, s.getType(t), [s.getType(ta) | ta <- targs]);
            for(m <- msgs) s.report(m);
            s.fact(current, result);
            //println("collect: <current> =\> <result>, inBody: <inBody>");
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
                return instantiateRascalTypeParameters(ts, baseType, bindings, s);
            } else if(aalias(aname, params, aliased) := baseType){
                nformals = size(baseType.parameters);
                if(nactuals != nformals) s.report(error(ts, "Expected %v type parameter(s) for %v, found %v", nformals, aname, nactuals));
                
                bindings = (params[i].pname : s.getType(actuals[i]) | i <- index(params));
                return instantiateRascalTypeParameters(ts, aliased, bindings, s);
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
            s.requireTrue(isNonTerminalAType(base), error(current, "Expected a non-terminal type, found %t", base));
            nexpected = size(getADTTypeParameters(base));
            s.requireTrue(nexpected == 0, error(current, "Expected %v type parameter(s) for %q, found 0", nexpected, getADTName(base)));
        });
    //c.fact(current, n);
}

void collect(current:(Sym) `& <Nonterminal n>`, Collector c){
    pname = prettyPrintName("<n>");
    
    if(<true, bool closed> := defineOrReuseTypeParameters(c)){
        if(c.isAlreadyDefined(pname, n)){
            c.use(n, {typeVarId() });
            //println("Use <pname> at <current@\loc>");
        } else {
            c.define(pname, typeVarId(), n, defType(aparameter(pname,treeType, closed=closed)));
            //println("Define <pname> at <current@\loc>");
        }
        c.fact(current, n);
        return;
    }  else
    if(<true, bool _> := useTypeParameters(c)){
        c.use(n, {typeVarId() });
        //println("Use <pname> at <current@\loc>");
        c.fact(current, n);
        return;
    }     
    
    //c.use(n, {typeVarId()});
    ////c.fact(current, n);
    //closed = !insideSignature(c);
    c.fact(current, aparameter(prettyPrintName("<n>"),treeType(),closed=true));
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
    beginDefineOrReuseTypeParameters(c, closed=false);
        collect(params, c);
    endDefineOrReuseTypeParameters(c);
}

void collect(current:(Sym) `start [ <Nonterminal n> ]`, Collector c){
    c.use(n, syntaxRoles);
    c.calculate("start <n>", current, [n],
        AType(Solver s){
            adtType = getSyntaxType(n, s);
            s.requireTrue(isNonTerminalAType(adtType), error(current, "Expected a non-terminal type, found %t", adtType));
            return \start(adtType);
        });
    collect(n, c);
}

void collect(current:(Sym) `<Sym symbol> <NonterminalLabel n>`, Collector c){
    un = unescape("<n>");
    // TODO require symbol is nonterminal
    c.define(un, fieldId(), n, defType([symbol], 
        AType(Solver s){ 
            res = s.getType(symbol)[alabel=un]; 
          return res;
        })[md5=md5Hash(current)]);
      
        //return getSyntaxType(symbol, s)[alabel=un]; }));
    c.fact(current, n);
    collect(symbol, c);
}

// ---- literals

void collect(current:(Sym) `<Class cc>`, Collector c){
    c.fact(current, cc2ranges(cc));
}

void collect(current:(Sym) `<StringConstant l>`, Collector c){
    c.fact(current, AType::alit(unescapeLiteral(l)));
}

void collect(current:(Sym) `<CaseInsensitiveStringConstant l>`, Collector c){
    c.fact(current, AType::acilit(unescapeLiteral(l)));
}

// ---- regular expressions

bool isIterSym((Sym) `<Sym symbol>+`) = true;
bool isIterSym((Sym) `<Sym symbol>*`) = true;
bool isIterSym((Sym) `{ <Sym symbol> <Sym sep> }+`) = true;
bool isIterSym((Sym) `{ <Sym symbol> <Sym sep> }*`) = true;
default bool isIterSym(Sym sym) = false;


bool isLexicalContext(Collector c){
    adtStack = c.getStack(currentAdt);
    if(!isEmpty(adtStack) && <Tree adt, list[KeywordFormal] _, _> := adtStack[0]){
        return !(adt is language);
    } 
    return false;
}

void collect(current:(Sym) `<Sym symbol>+`, Collector c){
    if(isIterSym(symbol)) c.report(warning(current, "Nested iteration"));
    isLexical = isLexicalContext(c);
    c.calculate("iter", current, [symbol], AType(Solver s) { 
        symbol_type = s.getType(symbol);
        return isLexical ? \iter(symbol_type, isLexical=true) : \iter(symbol_type);
        //return isLexical ? \iter(getSyntaxType(symbol, s), isLexical=true) : \iter(getSyntaxType(symbol, s));
    });
    collect(symbol, c);
}

void collect(current:(Sym) `<Sym symbol>*`, Collector c) {
    if(isIterSym(symbol)) c.report(warning(current, "Nested iteration"));
    isLexical = isLexicalContext(c);
    c.calculate("iterStar", current, [symbol], AType(Solver s) { 
        symbol_type = s.getType(symbol);
        return isLexical ? \iter-star(symbol_type, isLexical=true) : \iter-star(symbol_type);
        //return isLexical ? \iter-star(getSyntaxType(symbol, s), isLexical=true) : \iter-star(getSyntaxType(symbol, s));
    });
    collect(symbol, c);
}

void collect(current:(Sym) `{ <Sym symbol> <Sym sep> }+`, Collector c){
    if(isIterSym(symbol)) c.report(warning(current, "Nested iteration"));
    isLexical = isLexicalContext(c);
    c.calculate("iterSep", current, [symbol, sep], 
        AType(Solver s) { 
            seps = [s.getType(sep)];
            validateSeparators(current, seps, s);
            symbol_type = s.getType(symbol);
            return isLexical ? \iter-seps(symbol_type, seps, isLexical=true) : \iter-seps(symbol_type, seps);
            //return isLexical ? \iter-seps(getSyntaxType(symbol, s), seps, isLexical=true) : \iter-seps(getSyntaxType(symbol, s), seps);
        });
    collect(symbol, sep, c);
}

void collect(current:(Sym) `{ <Sym symbol> <Sym sep> }*`, Collector c){
    if(isIterSym(symbol)) c.report(warning(current, "Nested iteration"));
    isLexical = isLexicalContext(c);
    c.calculate("iterStarSep", current, [symbol, sep], 
        AType(Solver s) { 
            seps = [s.getType(sep)];
            validateSeparators(current, seps, s);
            symbol_type = s.getType(symbol);
            return isLexical ? \iter-star-seps(symbol_type, seps, isLexical=true) : \iter-star-seps(symbol_type, seps);
            //return isLexical ? \iter-star-seps(getSyntaxType(symbol, s), seps, isLexical=true) : \iter-star-seps(getSyntaxType(symbol, s), seps);
        });
    collect(symbol, sep, c);
}

void validateSeparators(Tree current, list[AType] separators, Solver s){
    if(all(sep <- separators, isLayoutAType(sep)))
        s.report(warning(current, "At least one element of separators should be non-layout")); // TODO make error
    forbidConsecutiveLayout(current, separators, s);
}

void forbidConsecutiveLayout(Tree current, list[AType] symbols, Solver s){
    if([*_,t1, t2,*_] := symbols, isLayoutAType(t1), isLayoutAType(t2)){
       s.report(error(current, "Consecutive layout types %t and %t not allowed", t1, t2));
    }
}

void collect(current:(Sym) `<Sym symbol>?`, Collector c){
    c.calculate("optional", current, [symbol], AType(Solver s) { return \opt(s.getType(symbol)); });
    collect(symbol, c);
}

void collect(current:(Sym) `( <Sym first> | <{Sym "|"}+ alternatives> )`, Collector c){
    alts = first + [alt | alt <- alternatives];
    c.calculate("alternative", current, alts, AType(Solver s) { return AType::alt({s.getType(alt) | alt <- alts}); });
    collect(alts, c);
}

void collect(current:(Sym) `( <Sym first> <Sym+ sequence> )`, Collector c){
    seqs = first + [seq | seq <- sequence];
    c.calculate("sequence", current, seqs, 
        AType(Solver s) { 
            symbols = [s.getType(seq) | seq <- seqs];
            forbidConsecutiveLayout(current, symbols, s);
            return AType::seq(symbols); 
        });
    collect(seqs, c);
}

void collect(current:(Sym) `()`, Collector c){
    c.fact(current, AType::aempty());
}

// ---- conditionals

void collect(current:(Sym) `<Sym symbol> @ <IntegerLiteral column>`, Collector c){
    c.calculate("column", current, [symbol], AType(Solver s) { return AType::conditional(s.getType(symbol), {ACondition::\a-at-column(toInt("<column>")) }); });
    collect(symbol, c);
}

void collect(current:(Sym) `<Sym symbol> $`, Collector c){
    c.calculate("end-of-line", current, [symbol], AType(Solver s) { return AType::conditional(s.getType(symbol), {ACondition::\a-end-of-line() }); });
    collect(symbol, c);
}

void collect(current:(Sym) `^ <Sym symbol>`, Collector c){
    c.calculate("begin-of-line", current, [symbol], AType(Solver s) { return AType::conditional(s.getType(symbol), {ACondition::\a-begin-of-line() }); });
    collect(symbol, c);
}

void collect(current:(Sym) `<Sym symbol> ! <NonterminalLabel n>`, Collector c){
    // TODO: c.use(n, {productionId()});
    un = unescape("<n>");
    c.calculate("except", current, [symbol], AType(Solver s) { 
        res = AType::conditional(s.getType(symbol), {ACondition::\a-except(un) }); 
        return res;
    });
    collect(symbol, c);
}
    
bool isTerminal((Sym) `<Sym symbol> @ <IntegerLiteral _>`) = isTerminal(symbol);
bool isTerminal((Sym) `<Sym symbol> $`) = isTerminal(symbol);
bool isTerminal((Sym) `^ <Sym symbol>`) = isTerminal(symbol);
bool isTerminal((Sym) `<Sym symbol> ! <NonterminalLabel _>`) = isTerminal(symbol);

default bool isTerminal(Sym s)
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
            res = AType::conditional(s.getType(symbol), {ACondition::\not-follow(s.getType(match)) }); 
            return res;
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
   c.calculate("exclude", current, [symbol, match], 
        AType(Solver s) { 
            t = s.getType(match);
            if(alit(_) !:= t && (t has syntaxRole && t.syntaxRole != keywordSyntax())){
                s.report(error(match, "Exclude (`\\`) requires keywords as right argument, found %t", match));
            }
            return AType::conditional(s.getType(symbol), {ACondition::\delete(s.getType(match)) });
        });
   collect(symbol, match, c);
}

void collect(Sym current, Collector c){
    throw "collect Sym, missed case <current>";
}

bool debugTP = false;
@doc{Convert Rascal type variables into their abstract representation.}

void collect(current:(TypeVar) `& <Name n>`, Collector c){
    pname = prettyPrintName(n);
    
    if(<true, bool closed> := defineOrReuseTypeParameters(c)){
        if(c.isAlreadyDefined(pname, n)){
            c.use(n, {typeVarId() });
            if(debugTP)println("Use <pname> at <current@\loc>");
        } else {
            bound = avalue();
            if(isEmpty(c.getStack(currentAdt))){
                ;
             } else if(<Tree adt, _, _, _> := c.top(currentAdt)){
                bound = SyntaxDefinition _ := adt ? treeType : avalue();
            } else {
                throw "collect TypeVar: currentAdt not found";
            }
            c.define(pname, typeVarId(), n, defType(aparameter(pname, bound, closed=closed)));
            if(debugTP)println("Define <pname> at <current@\loc>, closed=<closed>");
        }
        c.calculate("xxx", current, [n], AType (Solver s) { return s.getType(n)[closed=closed]; });
        return;
      
    } else if(<true, bool closed> := useTypeParameters(c)){
        c.use(n, {typeVarId() });
        if(debugTP)println("Use <pname> at <current@\loc>, closed=<closed>");
        c.calculate("xxx", current, [n], AType (Solver s) { return s.getType(n)[closed=closed]; });
        return;
    } else {        
        if(<true, rel[str, Type] tpbounds> := useBoundedTypeParameters(c)){
            if(tpbounds[pname]?){
                bnds = toList(tpbounds[pname]);
                if(debugTP)println("collect: Adding calculator for <pname>");
                c.calculate("type parameter with bound", current, bnds,
                    AType(Solver s){ 
                        new_bnd = (avalue() | aglb(it, s.getType(bnd)) | bnd <- bnds);
                        return  aparameter(pname, new_bnd, closed=true);
                    });  
            } else {
                if(debugTP)println("collect: fact for <pname>, closed=<closed>");
                c.fact(current, aparameter(pname, avalue(), closed=true));
            }
            return;
        }
    }
    if(debugTP)println("collect: postponing processing of <pname>");
}

void collect(current: (TypeVar) `& <Name n> \<: <Type tp>`, Collector c){
    pname = prettyPrintName(n);
    
    if(<true, bool closed> := defineOrReuseTypeParameters(c)){
        if(c.isAlreadyDefined(pname, n)){
            c.use(n, {typeVarId() });
            if(debugTP)println("Use <pname> at <current@\loc>");
        } else { 
            c.define(pname, typeVarId(), n, defTypeCall([getLoc(tp)], AType(Solver s) {return aparameter(pname,s.getType(tp), closed=closed); }));
            if(debugTP)println("Define <pname> at <current@\loc>");
        }
        c.fact(current, n);
    } else if(<true, bool closed> := useTypeParameters(c)){
        c.use(n, {typeVarId() });
        c.calculate("xxx", current, [n], AType (Solver s) { return s.getType(n)[closed=closed]; });
        if(debugTP)println("Use <pname> at <current@\loc>");
    } else if(<true, rel[str, Type] tpbounds> := useBoundedTypeParameters(c)){
        if(tpbounds[pname]?){
            bnds = toList(tpbounds[pname]);
            c.calculate("type parameter with bound", n, bnds, 
                AType(Solver s){ 
                    new_bnd = (avalue() | aglb(it, s.getType(bnd)) | bnd <- bnds);
                    return  aparameter(prettyPrintName(n), s.getType(new_bnd), closed=true);
                });  
        } else {
            c.calculate("type parameter with bound", n, [tp], AType(Solver s){ return  aparameter(prettyPrintName(n), s.getType(tp), closed=true); });
        }  
        c.fact(current, n);
    }
    
    collect(tp, c);
}

@doc{A parsing function, useful for generating test cases.}
public Type parseType(str s) {
    return parse(#Type, s);
}