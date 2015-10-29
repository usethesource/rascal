@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Anastasia Izmaylova - Anastasia.Izmaylova@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::types::Util

import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::\syntax::Rascal;

public rel[RName,loc] getIntroducedNames((Assignable)`( <Assignable a> )`) = getIntroducedNames(a);
public rel[RName,loc] getIntroducedNames((Assignable)`<QualifiedName qn>`) = { < convertName(qn), qn.origin > };
public rel[RName,loc] getIntroducedNames((Assignable)`<Assignable a> [ <Expression _> ]`) = { };
public rel[RName,loc] getIntroducedNames((Assignable)`<Assignable a> [ <OptionalExpression _> .. <OptionalExpression _> ]`) = { };
public rel[RName,loc] getIntroducedNames((Assignable)`<Assignable a> [ <OptionalExpression _> , <Expression _> .. <OptionalExpression _> ]`) = { };
public rel[RName,loc] getIntroducedNames((Assignable)`<Assignable a> . <Name _>`) = { };
public rel[RName,loc] getIntroducedNames((Assignable)`<Assignable a> ? <Expression _>`) = { };
public rel[RName,loc] getIntroducedNames((Assignable)`<Name _> ( <{Assignable ","}+ al> )`) = { }; // NOTE: We should remove this case, it is unsafe
public rel[RName,loc] getIntroducedNames((Assignable)`\< <{Assignable ","}+ al> \>`) = { *getIntroducedNames(ali) | ali <- al };
public rel[RName,loc] getIntroducedNames((Assignable)`<Assignable a>@<Name _>`) = { };
public default rel[RName,loc] getIntroducedNames(Assignable a) { throw "Case not handled: <a>"; }

public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`{ <{Pattern ","}* ps> }`) = { *getPatternNames(psi) | psi <- ps };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`[ <{Pattern ","}* ps> ]`) = { *getPatternNames(psi) | psi <- ps };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<QualifiedName qn>`) = { < convertName(qn), qn.origin > };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<QualifiedName qn>*`) = { < convertName(qn), qn.origin > };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`* <QualifiedName qn>`) = { < convertName(qn), qn.origin > };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`* <Type t> <Name n>`) = { < convertName(n), n.origin > };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`+ <QualifiedName qn>`) = { < convertName(qn), qn.origin > };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`+ <Type t> <Name n>`) = { < convertName(n), n.origin > };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`- <Pattern p>`) = getPatternNames(p);
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<IntegerLiteral il>`) = { };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<RealLiteral rl>`) = { };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<BooleanLiteral bl>`) = { };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<DateTimeLiteral dtl>`) = { };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<RationalLiteral rl>`) = { };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<RegExpLiteral rl>`) = regExpPatternNames(rl);
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<StringLiteral sl>`) = { };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<LocationLiteral ll>`) = { };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`\< <Pattern p1>, <{Pattern ","}* ps> \>`) = getPatternNames(p1) + { *getPatternNames(psi) | psi <- ps };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<Type t> <Name n>`) = { < convertName(n), n.origin> };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`( <{Mapping[Pattern] ","}* mps> )`) = { *getPatternNames(pd), *getPatternNames(pr) | (Mapping[Pattern])`<Pattern pd> : <Pattern pr>` <- mps };
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`type ( <Pattern s>, <Pattern d> )`) = getPatternNames(s) + getPatternNames(d);
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<Concrete concrete>`) = { < convertName(n), n.origin > | /(ConcreteHole)`\<<Sym sym> <Name n>\>` := concrete };	
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<Name n> : <Pattern p>`) = { < convertName(n), n.origin > } + getPatternNames(p);
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`[ <Type t> ] <Pattern p>`) = getPatternNames(p);
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`/ <Pattern p>`) = getPatternNames(p);
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`! <Pattern p>`) = getPatternNames(p);
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<Type t> <Name n> : <Pattern p>`) = { < convertName(n), n.origin > } + getPatternNames(p);
public rel[RName,loc] getPatternNames(Pattern pat:(Pattern)`<Pattern p> ( <{Pattern ","}* ps> <KeywordArguments[Pattern] keywordArguments>)`) = 
	getPatternNames(p) + 
	{ *getPatternNames(psi) | psi <- ps } + 
	{ < convertName(kn), kn.origin > | (KeywordArguments[Pattern])`<OptionalComma oc> <{KeywordArgument[Pattern] ","}+ kargs>` := keywordArguments, ka:(KeywordArgument[Pattern])`<Name kn> = <Pattern kp>` <- kargs };
public default rel[RName,loc] getPatternNames(Pattern p) { throw "Unhandled pattern <p>"; }

public rel[RName,loc] regExpPatternNames(RegExpLiteral rl) {
    rel[RName,loc] names = { };
        
    top-down visit(rl) {
        case \appl(\prod(lex("RegExp"),[_,\lex("Name"),_,_,_],_),list[Tree] prds) : {
        	if (Name regExpVarName := prds[1]) { 
        		names += < convertName(regExpVarName), prds[1].origin >;
        	}
        }
    }
    
    return names;
}
