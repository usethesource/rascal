@bootstrapParser
module lang::rascalcore::check::Fingerprint

extend analysis::typepal::TypePal;

import lang::rascal::\syntax::Rascal;
extend  lang::rascalcore::check::ATypeUtils;

// Compute the fingerprint of a pattern. Note this should be in sync with ToplevelType.getFingerprint.

private int fingerprintDefault = 0;

int fingerprint(Pattern p, AType atype, bool useConcreteFingerprint) {
    fp = fingerprint1(p, atype, useConcreteFingerprint);
    //println("fingerprint(<p>, <useConcreteFingerprint>) = \> <fp>");
    return fp;
}

int fingerprint1(p:(Pattern) `<Literal lit>`, AType atype, bool useConcreteFingerprint) =
    getFingerprint(readTextValueString("<lit>"), atype, useConcreteFingerprint) when !(p.literal is regExp);

int fingerprint1(p:(Pattern) `<Concrete concrete>`, AType atype, bool useConcreteFingerprint) {
    return 0;  //TODO: fix this
    //t = parseConcrete(concrete);
    //res = isConcreteHole(t) ? fingerprintDefault : getFingerprint(parseConcrete(concrete), atype, useConcreteFingerprint);
    ////println("fingerprint <res>, <useConcreteFingerprint>, <getType(p@\loc)> for <p>"); iprintln(parseConcrete(concrete));
    //return res;
}

int fingerprint1(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, AType atype, bool useConcreteFingerprint) { 
    args = [a | a <- arguments];    // TODO: work around!
    res = fingerprintDefault;
    if(expression is qualifiedName && (QualifiedName)`<{Name "::"}+ nl>` := expression.qualifiedName){  
       s = "<[ n | n <- nl ][-1]>";
       if(useConcreteFingerprint){  // Abstract pattern during concrete match
            pr = getLabeledProduction(s, atype);
            res = getFingerprintNode(pr);
            //println("fingerprint1: <pr>, <res>");
       } else {                     // Abstract pattern druing abstract match
            if(isNonTerminalType(atype)){
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
int fingerprint1(p:(Pattern) `{<{Pattern ","}* pats>}`, AType atype, bool useConcreteFingerprint) = getFingerprint("set", useConcreteFingerprint);
int fingerprint1(p:(Pattern) `\<<{Pattern ","}* pats>\>`, AType atype, bool useConcreteFingerprint) = getFingerprint("tuple", size([pat | pat <- pats]), useConcreteFingerprint);
int fingerprint1(p:(Pattern) `[<{Pattern ","}* pats>]`, AType atype, bool useConcreteFingerprint) = getFingerprint("list", useConcreteFingerprint);
int fingerprint1(p:(Pattern) `<Name name> : <Pattern pattern>`, AType atype, bool useConcreteFingerprint) = fingerprint1(pattern, atype, useConcreteFingerprint);
int fingerprint1(p:(Pattern) `[ <Type tp> ] <Pattern argument>`, AType atype, bool useConcreteFingerprint) = fingerprint1(argument, atype, useConcreteFingerprint);
int fingerprint1(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`,  AType atype, bool useConcreteFingerprint) = fingerprint1(pattern, atype, useConcreteFingerprint);
default int fingerprint1(Pattern p, AType atype, bool useConcreteFingerprint) {
    //println("fingerprint <fingerprintDefault> (default), <getType(p@\loc)> for <p>");
    return fingerprintDefault;
}   
