@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
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
