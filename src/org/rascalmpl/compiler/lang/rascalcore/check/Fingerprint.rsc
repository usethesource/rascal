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
@bootstrapParser
module lang::rascalcore::check::Fingerprint


/*
    Compute the fingerprint of a pattern. Note this should be in sync with ToplevelType.getFingerprint.
*/

extend lang::rascalcore::check::CheckerCommon;

import lang::rascal::\syntax::Rascal;

import util::Reflective;
import ValueIO;



private int fingerprintDefault = 0;

int getFingerprintDefault() = fingerprintDefault;

int fingerprint(Pattern p, AType atype, bool useConcreteFingerprint) {
    fp = fingerprint1(p, atype, useConcreteFingerprint);
    //println("fingerprint(<p>, <useConcreteFingerprint>) = \> <fp>");
    return fp;
}

private int fingerprint1(p:(Pattern) `<Literal lit>`, AType atype, bool useConcreteFingerprint) =
    getFingerprint(readTextValueString("<lit>"), useConcreteFingerprint) when !(p.literal is regExp);

private int fingerprint1(p:(Pattern) `<Concrete concrete>`, AType atype, bool useConcreteFingerprint) {
    return 0;  //TODO: fix this
    //t = parseConcrete(concrete);
    //res = isConcreteHole(t) ? fingerprintDefault : getFingerprint(parseConcrete(concrete), atype, useConcreteFingerprint);
    ////println("fingerprint <res>, <useConcreteFingerprint>, <getType(p@\loc)> for <p>"); iprintln(parseConcrete(concrete));
    //return res;
}

private int fingerprint1(p:(Pattern) `<Pattern expression> ( <{Pattern ","}+ arguments> <KeywordArguments[Pattern] keywordArguments> )`, AType atype, bool useConcreteFingerprint) { 
    args = [a | a <- arguments];    // TODO: work around!
    res = fingerprintDefault;
    if(expression is qualifiedName && (QualifiedName)`<{Name "::"}+ nl>` := expression.qualifiedName){  
       s = "<[ n | n <- nl ][-1]>";
       if(useConcreteFingerprint){  // Abstract pattern during concrete match
            res = 0; // TODO Fix this
            //pr = getLabeledProduction(s, atype);
            //res = getFingerprintNode(pr);
       } else {                     // Abstract pattern druing abstract match
            if(isSyntaxType(atype)){
            ;// an abstract pattern of a nonterminal type will use labels in a production
             // and requires an explicit match (as opposed to a selection by a fingerprint)
             // Therefore rely on the defaultFingerprint and force sequential matching during
             // handling of the default cases
            } else {
                res = getFingerprint(s[0] == "\\" ? s[1..] : s, size(args), useConcreteFingerprint);
            }
       }    
    }
    //println("fingerprint <res>, <useConcreteFingerprint>, <getType(p@\loc)> for <p>");
    return res;
}
private int fingerprint1(p:(Pattern) `{<{Pattern ","}* pats>}`, AType atype, bool useConcreteFingerprint) = getFingerprint("set", useConcreteFingerprint);
private int fingerprint1(p:(Pattern) `\<<{Pattern ","}+ pats>\>`, AType atype, bool useConcreteFingerprint) = getFingerprint("tuple", size([pat | pat <- pats]), useConcreteFingerprint);
private int fingerprint1(p:(Pattern) `[<{Pattern ","}* pats>]`, AType atype, bool useConcreteFingerprint) = getFingerprint("list", useConcreteFingerprint);
private int fingerprint1(p:(Pattern) `<Name name> : <Pattern pattern>`, AType atype, bool useConcreteFingerprint) = fingerprint1(pattern, atype, useConcreteFingerprint);
private int fingerprint1(p:(Pattern) `[ <Type tp> ] <Pattern argument>`, AType atype, bool useConcreteFingerprint) = fingerprint1(argument, atype, useConcreteFingerprint);
private int fingerprint1(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`,  AType atype, bool useConcreteFingerprint) = fingerprint1(pattern, atype, useConcreteFingerprint);
private default int fingerprint1(Pattern p, AType atype, bool useConcreteFingerprint) {
    //println("fingerprint <fingerprintDefault> (default), <getType(p@\loc)> for <p>");
    return fingerprintDefault;
}   
