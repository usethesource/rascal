module lang::rascalcore::check::CollectLiteral

extend lang::rascalcore::check::CheckerCommon;
//extend lang::rascalcore::check::PathAnalysis;
import lang::rascal::\syntax::Rascal;
import Exception;

import ValueIO;
import Set;

// ---- Rascal literals

void collect(IntegerLiteral il, Collector c){
    c.fact(il, aint());
}

void collect(RealLiteral current, Collector c){
    c.fact(current, areal());
}

void collect(BooleanLiteral current, Collector c){
    c.fact(current, abool());
 }

void collect(DateTimeLiteral current, Collector c){
    c.fact(current, adatetime());
    try {
        readTextValueString("<current>");   // ensure that the datetime literal is valid
    } catch IO(_): {
        c.report(error(current, "Malformed datetime literal %q", current));
    }
}

void collect(RationalLiteral current, Collector c){
    c.fact(current, arat());
}

// ---- string literals and templates
void collect(current:(Literal)`<StringLiteral sl>`, Collector c){
    c.fact(current, astr());
    collect(sl, c);
}

void collect(StringCharacter current, Collector c){ }

// ---- Concrete literals

void collect((Expression) `<Concrete concrete>`, Collector c){
    c.fact(concrete, concrete.symbol);
    c.push(inConcreteLiteral, true);
    collect(concrete.symbol, c);
    collect(concrete.parts, c);
    c.pop(inConcreteLiteral);
    checkSupportedByParserGenerator(concrete.symbol, c);
}

void collect((Pattern) `<Concrete concrete>`, Collector c){
    c.fact(concrete, concrete.symbol);
    collect(concrete.symbol, c);
    collect(concrete.parts, c);
    checkSupportedByParserGenerator(concrete.symbol, c);
}

void collect(current: (ConcreteHole) `\< <Sym symbol> <Name name> \>`, Collector c){
    varType = symbol;
    uname = prettyPrintName(name);
    if(!isEmpty(c.getStack(inConcreteLiteral))){    // We are inside a concrete literal expression
                                                    // This hole must be a use
       c.useLub(name, {formalId(), patternVariableId(), variableId(), moduleVariableId()});
    } else {                                        // We are inside a concrele Literal pattern
                                                    // This hole can be a use or define
        if(!isWildCard(uname)){
           if(uname in c.getStack(patternNames)){
              c.useLub(name, {formalOrPatternFormal(c)});
           } else {
               c.push(patternNames, uname);
               c.define(uname, formalOrPatternFormal(c), name, defLub([symbol], AType(Solver s) { return s.getType(symbol); }));
           }
        }
    }
    c.fact(current, symbol);
    collect(symbol, c);
}
