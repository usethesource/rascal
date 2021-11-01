@bootstrapParser
module lang::rascalcore::check::BacktrackFree

import lang::rascal::\syntax::Rascal;
import List;
import ParseTree;

// Is an expression free of backtracking? 

// TODO: add more cases?

bool backtrackFree(Expression e){
    top-down visit(e){
    
    case (Expression) `all ( <{Expression ","}+ generators> )`: 
        return true;
    case (Expression) `any ( <{Expression ","}+ generators> )`: 
        return true;
    case Comprehension comprehension:
        return true;
    case (Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`:
        return true; 
    case (Expression) `<Pattern pat> \<- <Expression exp>`: 
        return false;
    case (Expression) `<Pattern pat> := <Expression exp>`:
        return backtrackFree(pat);
    case (Expression) `<Pattern pat> !:= <Expression exp>`:
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

bool backtrackFree(p:(Pattern) `[<{Pattern ","}* pats>]`) = false; // p == (Pattern) `[]` || all(pat <- pats, backtrackFree(pat));
bool backtrackFree(p:(Pattern) `{<{Pattern ","}* pats>}`) = false; //p == (Pattern) `{}` || all(pat <- pats, backtrackFree(pat));
bool backtrackFree(p:(Pattern) `\<<{Pattern ","}* pats>\>`) = false; // all(pat <- pats, backtrackFree(pat));
bool backtrackFree(p:(Pattern) `<Name name> : <Pattern pattern>`) = backtrackFree(pattern);
bool backtrackFree(p:(Pattern) `[ <Type tp> ] <Pattern pattern>`) = backtrackFree(pattern);

bool backtrackFree(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`)
    = backtrackFree(expression) && (isEmpty(argumentList) || all(arg <- argumentList, backtrackFree(arg)))
                                && (isEmpty(keywordArgumentList) || all(kwa <- keywordArgumentList, backtrackFree(kwa.expression)))
    when argumentList := [arg | arg <- arguments], 
         keywordArgumentList := (((KeywordArguments[Pattern]) `<OptionalComma optionalComma> <{KeywordArgument[Pattern] ","}+ kwaList>` := keywordArguments)
                                ? [kwa | kwa <- kwaList]
                                : []);
bool backtrackFree((Pattern) `/ <Pattern pattern>`) = false;
bool backtrackFree((Pattern) `<RegExpLiteral r>`) = false;


default bool backtrackFree(Pattern p) = !isMultiVar(p);

/*********************************************************************/
/*                  BacktrackFree for Concrete Patterns   == Tree    */
/*********************************************************************/

bool backtrackFreeConcrete(appl(prod(label("concrete",sort("Pattern")),[label("concrete",lex("Concrete"))], {}),[Tree concrete1])){
    if(e:appl(prod(Symbol::label("parsed",Symbol::lex("Concrete")), [_],_),[Tree concrete2]) := concrete1){
        for(/t:appl(prod(Symbol::label("$MetaHole", Symbol _),[Symbol::sort("ConcreteHole")], {\tag("holeType"(Symbol holeType))}), [ConcreteHole hole]) := concrete2){
            //println("hole: <hole>, type: <holeType>");
            if(isIterSymbol(holeType)) return false;
        }
    }
    return true;
} 

bool backtrackFreeConcrete(appl(prod(Symbol::label("$MetaHole", Symbol _),[Symbol::sort("ConcreteHole")], {\tag("holeType"(Symbol holeType))}), [ConcreteHole hole])){
    return !isIterSymbol(holeType);
}

bool backtrackFreeConcrete(p: appl(prod(_, [lit(_)],{}), [Tree_])) {
    return true;   
}

bool backtrackFreeConcrete(p: appl(prod(_, [cilit(_)],{}), [Tree_])) {
    return true;   
}

bool backtrackFreeConcrete(p: appl(prod(_, [\char-class(_)],{}), [Tree_])) {
    return true;   
}

bool backtrackFreeConcrete(p: char(_)) {
    return true;   
}

bool backtrackFreeConcrete(p: appl(prod(layouts(_), [_],{}), [Tree_])) {
    return true;   
}

bool backtrackFreeConcrete(p: appl(prod(Symbol def, list[Symbol] symbols, {}), list[Tree] args)) {
    for(/t:appl(prod(Symbol::label("$MetaHole", Symbol _),[Symbol::sort("ConcreteHole")], {\tag("holeType"(Symbol holeType))}), [ConcreteHole hole]) := symbols){
            //println("hole: <hole>, type: <holeType>");
            if(isIterSymbol(holeType)) return false;
        } 
    return true;  
}

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

