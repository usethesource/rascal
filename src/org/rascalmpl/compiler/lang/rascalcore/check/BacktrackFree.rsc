@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
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
module lang::rascalcore::check::BacktrackFree

/*
    Check whether an expression or pattern is free of backtracking.
*/
import lang::rascal::\syntax::Rascal;
import List;
import ParseTree;

// Is an expression free of backtracking? 

// TODO: add more cases?

bool backtrackFree(Expression e){
    top-down visit(e){
    
    case (Expression) `all ( <{Expression ","}+ _> )`: 
        return true;
    case (Expression) `any ( <{Expression ","}+ _> )`: 
        return true;
    case Comprehension _:
        return true;
    case (Expression) `( <Expression _> | <Expression _> | <{Expression ","}+ _> )`:
        return true; 
    case (Expression) `<Pattern _> \<- <Expression _>`: 
        return false;   //TODO: return backtrackFree(pat);
    case (Expression) `<Pattern pat> := <Expression _>`:
        return backtrackFree(pat);
    case (Expression) `<Pattern pat> !:= <Expression _>`:
        return backtrackFree(pat);
    case (Expression) `!<Expression exp>`:
        return backtrackFree(exp);
    case (Expression) `<Expression e1> || <Expression e2>`:
        return backtrackFree(e1) && backtrackFree(e2);
    case (Expression) `<Expression e1> && <Expression e2>`:
        return backtrackFree(e1) && backtrackFree(e2);  
    case (Expression) `<Expression e1> \<==\> <Expression e2>`:
        return backtrackFree(e1) && backtrackFree(e2);  
    case (Expression) `<Expression e1> ==\> <Expression e2>`:
        return backtrackFree(e1) && backtrackFree(e2);  
    case (Expression) `<Expression cond> ? <Expression thenExp> : <Expression elseExp>`:
        return backtrackFree(cond) && backtrackFree(thenExp) && backtrackFree(elseExp);
    }
    return true;
}

/*********************************************************************/
/*                  BacktrackFree for Patterns                       */
/*********************************************************************/

// TODO: Make this more precise and complete

bool backtrackFree(p:(Pattern) `[<{Pattern ","}* pats>]`) = false;
                                                            //((Pattern) `[]` := p) || 
                                                            //((Pattern) `[Pattern pat]` := p && isMultiVar(pat)) ||
                                                            //all(pat <- pats, backtrackFree(pat));
bool backtrackFree(p:(Pattern) `{<{Pattern ","}* pats>}`) = false;
                                                            //((Pattern) `{}` := p) || 
                                                            //((Pattern) `{Pattern pat}` := p && isMultiVar(pat)) ||
                                                            //all(pat <- pats, backtrackFree(pat));
bool backtrackFree(p:(Pattern) `\<<{Pattern ","}* pats>\>`) = false; 
                                                            //((Pattern) `[]` := p) || 
                                                            //all(pat <- pats, backtrackFree(pat));
bool backtrackFree(p:(Pattern) `<Name name> : <Pattern pattern>`) = backtrackFree(pattern);
bool backtrackFree(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`) = backtrackFree(pattern);
bool backtrackFree(p:(Pattern) `[ <Type tp> ] <Pattern pattern>`) = backtrackFree(pattern);
bool backtrackFree(p:(Pattern) `<Name name>`) = true;
bool backtrackFree(p:(Pattern) `<Type tp> <Name name>`) = true;
bool backtrackFree(p:(Pattern) `<Literal lit>`) = !(lit is regExp);

bool backtrackFree(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`){
    argumentList = [arg | arg <- arguments];
    btf_args = backtrackFree(expression) && (isEmpty(argumentList) || all(arg <- argumentList, backtrackFree(arg)));
    if(btf_args){
        if((KeywordArguments[Pattern]) `<OptionalComma _> <{KeywordArgument[Pattern] ","}+ kwaList>` := keywordArguments){
            keywordArgumentList = [kwa | kwa <- kwaList];          
            return isEmpty(keywordArgumentList) || all(kwa <- keywordArgumentList, backtrackFree(kwa.expression));                       
        }
        return true;
    }
    return false;
}

bool backtrackFree((Pattern) `/ <Pattern pattern>`) = false;
bool backtrackFree((Pattern) `<RegExpLiteral r>`) = false;

bool backtrackFree(Tree t) = backtrackFreeConcrete(t);

default bool backtrackFree(Pattern p) = !isMultiVar(p);

/*********************************************************************/
/*                  BacktrackFree for Concrete Patterns   == Tree    */
/*********************************************************************/

bool backtrackFreeConcrete(appl(prod(label("concrete",sort("Pattern")),[label("concrete",lex("Concrete"))], {}),[Tree concrete1])){
    if(appl(prod(Symbol::label("parsed",Symbol::lex("Concrete")), [_],_),[Tree concrete2]) := concrete1){
        for(/appl(prod(Symbol::label("$MetaHole", Symbol _),[Symbol::sort("ConcreteHole")], {\tag("holeType"(Symbol holeType))}), [ConcreteHole _]) := concrete2){
            //println("hole: <hole>, type: <holeType>");
            if(isIterSymbol(holeType)) return false;
        }
    }
    return true;
} 

bool backtrackFreeConcrete(appl(prod(Symbol::label("$MetaHole", Symbol _),[Symbol::sort("ConcreteHole")], {\tag("holeType"(Symbol holeType))}), [ConcreteHole hole])){
    return !isIterSymbol(holeType);
}

bool backtrackFreeConcrete(appl(prod(_, [lit(_)],{}), [Tree_])) {
    return true;   
}

bool backtrackFreeConcrete(appl(prod(_, [cilit(_)],{}), [Tree_])) {
    return true;   
}

bool backtrackFreeConcrete(appl(prod(_, [\char-class(_)],{}), [Tree_])) {
    return true;   
}

bool backtrackFreeConcrete(Tree::char(_)) {
    return true;   
}

bool backtrackFreeConcrete(appl(prod(layouts(_), [_],{}), [Tree_])) {
    return true;   
}

bool backtrackFreeConcrete(appl(prod(Symbol def, list[Symbol] symbols, {}), list[Tree] args)) {
    for(/appl(prod(Symbol::label("$MetaHole", Symbol _),[Symbol::sort("ConcreteHole")], {\tag("holeType"(Symbol holeType))}), [ConcreteHole _hole]) := symbols){
            //println("hole: <hole>, type: <holeType>");
            if(isIterSymbol(holeType)) return false;
        } 
    return true;  
}

default bool backtrackFreeConcrete(Tree _) = true;

// Some utilities

bool isMultiVar(p:(Pattern) `<QualifiedName name>*`) = true;
bool isMultiVar(p:(Pattern) `*<Type tp> <Name name>`) = true;
bool isMultiVar(p:(Pattern) `*<Name name>`) = true;
default bool isMultiVar(Pattern p) = false;

bool isAnonymousMultiVar(p:(Pattern) `_*`) = true;
bool isAnonymousMultiVar(p:(Pattern) `*<Type tp> _`) = true;
bool isAnonymousMultiVar(p:(Pattern) `*_`) = true;
default bool isAnonymousMultiVar(Pattern p) = false;

bool isAnonymousVar(p:(Pattern) `_`) = true;
bool isAnonymousVar(p:(Pattern) `<Type tp> _`) = true;
default bool isAnonymousVar(Pattern p) = false;

// Is a symbol an iterator type?

bool isIterSymbol(\iter(Symbol symbol)) = true;
bool isIterSymbol(\iter-star(Symbol symbol)) = true;
bool isIterSymbol(\iter-seps(Symbol symbol, list[Symbol] separators)) = true;
bool isIterSymbol(\iter-star-seps(Symbol symbol, list[Symbol] separators)) = true;
default bool isIterSymbol(Symbol s) = false;

// Is a symbol an iterator type with separators?
bool isIterSymbolWithSeparator(\iter-seps(Symbol symbol, list[Symbol] separators)) = true;
bool isIterSymbolWithSeparator(\iter-star-seps(Symbol symbol, list[Symbol] separators)) = true;
default bool isIterSymbolWithSeparator(Symbol s) = false;

