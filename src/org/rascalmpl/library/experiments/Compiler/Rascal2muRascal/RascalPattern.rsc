@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalPattern

import Prelude;

import lang::rascal::\syntax::Rascal;
import experiments::Compiler::Rascal2muRascal::RascalExpression;
import experiments::Compiler::Rascal2muRascal::RascalType;

import experiments::Compiler::muRascal::AST;

import experiments::Compiler::Rascal2muRascal::TypeUtils;

/*********************************************************************/
/*                  Patterns                                         */
/*********************************************************************/

list[MuExp] translatePat(p:(Pattern) `<Literal lit>`) = [ muCreate(mkCallToLibFun("Library","MATCH_LITERAL",2), translate(lit)) ];
     
list[MuExp] translatePat(p:(Pattern) `<QualifiedName name>`) {
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return [ muCreate(mkCallToLibFun("Library","MATCH_VAR",2), [muVarRef("<name>", fuid, pos)]) ];
} 
     
list[MuExp] translatePat(p:(Pattern) `<Type tp> <Name name>`){
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return [ muCreate(mkCallToLibFun("Library","MATCH_TYPED_VAR",3), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos)]) ];
}  

// reifiedType pattern

list[MuExp] translatePat(p:(Pattern) `type ( <Pattern symbol> , <Pattern definitions> )`) {
    throw "reifiedType pattern";
}

// callOrTree pattern

list[MuExp] translatePat(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments keywordArguments> )`) {
   return [ muCreate(mkCallToLibFun("Library","MATCH_CALL_OR_TREE",2), [muCallMuPrim("make_array", translatePat(expression) + [ *translatePat(pat) | pat <- arguments ])]) ];
}


// Set pattern

list[MuExp] translatePat(p:(Pattern) `{<{Pattern ","}* pats>}`) {
     throw "set pattern";
}

// Tuple pattern

list[MuExp] translatePat(p:(Pattern) `\<<{Pattern ","}* pats>\>`) {
    return [ muCreate(mkCallToLibFun("Library","MATCH_TUPLE",2), [muCallMuPrim("make_array", [ *translatePat(pat) | pat <- pats ])]) ];
}


// List pattern 

list[MuExp] translatePat(p:(Pattern) `[<{Pattern ","}* pats>]`) =
    [ muCreate(mkCallToLibFun("Library","MATCH_LIST",2), [muCallMuPrim("make_array", [ *translatePatAsListElem(pat) | pat <- pats ])]) ];

// Variable becomes pattern

list[MuExp] translatePat(p:(Pattern) `<Name name> : <Pattern pattern>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return [muCreate(mkCallToLibFun("Library","MATCH_VAR_BECOMES",3), [muVarRef("<name>", fuid, pos), *translatePat(pattern)])];
}

// asType pattern

list[MuExp] translatePat(p:(Pattern) `[ <Type tp> ] <Pattern argument>`) =
    [muCreate(mkCallToLibFun("Library","MATCH_AS_TYPE",3), [muTypeCon(translateType(tp)), *translatePat(argument)])];

// Descendant pattern

list[MuExp] translatePat(p:(Pattern) `/ <Pattern pattern>`) =
    [muCreate(mkCallToLibFun("Library","MATCH_DESCENDANT",2), translatePat(pattern))];

// Anti-pattern
list[MuExp] translatePat(p:(Pattern) `! <Pattern pattern>`) =
    [muCreate(mkCallToLibFun("Library","MATCH_ANTI",2), translatePat(pattern))];

// typedVariableBecomes pattern
list[MuExp] translatePat(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return [muCreate(mkCallToLibFun("Library","MATCH_TYPED_VAR_BECOMES",4), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos), *translatePat(pattern)])];
}

// Default rule for pattern translation

default list[MuExp] translatePat(Pattern p) { throw "Pattern <p> cannot be translated"; }

// Translate patterns as element of a list pattern

list[MuExp] translatePatAsListElem(p:(Pattern) `<QualifiedName name>`) {
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return [ muCreate(mkCallToLibFun("Library","MATCH_VAR_IN_LIST",4), [muVarRef("<name>", fuid, pos)]) ];
} 

list[MuExp] translatePatAsListElem(p:(Pattern) `<QualifiedName name>*`) {
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return [ muCreate(mkCallToLibFun("Library","MATCH_MULTIVAR_IN_LIST",4), [muVarRef("<name>", fuid, pos)]) ];
}

list[MuExp] translatePatAsListElem(p:(Pattern) `*<Type tp> <Name name>`) {
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return [ muCreate(mkCallToLibFun("Library","MATCH_TYPED_MULTIVAR_IN_LIST",5), [muTypeCon(\list(translateType(tp))), muVarRef("<name>", fuid, pos)]) ];
}

list[MuExp] translatePatAsListElem(p:(Pattern) `*<Name name>`) {
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return [ muCreate(mkCallToLibFun("Library","MATCH_MULTIVAR_IN_LIST",4), [muVarRef("<name>", fuid, pos)]) ];
} 

list[MuExp] translatePatAsListElem(p:(Pattern) `+<Pattern argument>`) {
  throw "splicePlus pattern";
}   

default list[MuExp] translatePatAsListElem(Pattern p) {
  iprintln(p);
  return [ muCreate(mkCallToLibFun("Library","MATCH_PAT_IN_LIST",4), translatePat(p)) ];
}

/*********************************************************************/
/*                  End of Patterns                                  */
/*********************************************************************/

bool backtrackFree(p:(Pattern) `[<{Pattern ","}* pats>]`) = false;
bool backtrackFree(p:(Pattern) `{<{Pattern ","}* pats>}`) = false;

default bool backtrackFree(Pattern p) = true;
