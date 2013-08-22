@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalPattern

import Prelude;

import lang::rascal::\syntax::Rascal;
import experiments::Compiler::Rascal2muRascal::RascalExpression;
import experiments::Compiler::Rascal2muRascal::RascalType;

import experiments::Compiler::muRascal::AST;

/*********************************************************************/
/*                  Patterns                                         */
/*********************************************************************/
 
list[MuExp] translatePat(p:(Pattern) `<BooleanLiteral b>`) = [ muCreate(muFun("MATCH_BOOL"), translate(b)) ];

list[MuExp] translatePat(p:(Pattern) `<IntegerLiteral n>`) = [ muCreate(muFun("MATCH_INT"), translate(n)) ];
     
list[MuExp] translatePat(p:(Pattern) `<StringLiteral s>`) =   [ muCreate(muFun("MATCH_STR"), translate(s)) ];

// TODO: add other literal, or a single literal handler
     
list[MuExp] translatePat(p:(Pattern) `<QualifiedName name>`) {
   <scopeId, pos> = getVariableScope("<name>", name@\loc);
   return [ muCreate(muFun("MATCH_VAR"), [muVarRef("<name>", scopeId, pos)]) ];
} 
     
list[MuExp] translatePat(p:(Pattern) `<Type tp> <Name name>`){
   <scopeId, pos> = getVariableScope("<name>", name@\loc);
   return [ muCreate(muFun("MATCH_VAR"), [muVarRef("<name>", scopeId, pos)]) ];
}  

// reifiedType pattern

list[MuExp] translatePat(p:(Pattern) `type ( <Pattern symbol> , <Pattern definitions> )`) {
    throw "reifiedType pattern";
}

// callOrTree pattern

list[MuExp] translatePat(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments keywordArguments> )`) {
   return [ muCreate(muFun("MATCH_CALL_OR_TREE"), [muCallPrim("$make_array", translatePat(expression) + [ *translatePat(pat) | pat <- arguments ])]) ];
}


// Set pattern

list[MuExp] translatePat(p:(Pattern) `{<{Pattern ","}* pats>}`) {
     throw "set pattern";
}

// Tuple pattern

list[MuExp] translatePat(p:(Pattern) `\<<{Pattern ","}* pats>\>`) {
    return [ muCreate(muFun("MATCH_TUPLE"), [muCallPrim("$make_array", [ *translatePat(pat) | pat <- pats ])]) ];
}


// List pattern 

list[MuExp] translatePat(p:(Pattern) `[<{Pattern ","}* pats>]`) {
  return [ muCreate(muFun("MATCH_LIST"), [muCallPrim("$make_array", [ *translatePatAsListElem(pat) | pat <- pats ])]) ];
}

// Variable becomes pattern

list[MuExp] translatePat(p:(Pattern) `<Name name> : <Pattern pattern>`) {
    <scopeId, pos> = getVariableScope("<name>", name@\loc);
    return [muCreate(muFun("MATCH_VAR_BECOMES"), [muVarRef("<name>", scopeId, pos), *translatePat(pattern)])];
}

// asType pattern

list[MuExp] translatePat(p:(Pattern) `[ <Type tp> ] <Pattern argument>`) {
    return [muCreate(muFun("MATCH_AS_TYPE"), [muTypeCon(translateType(tp)), *translatePat(argument)])];
}

// Descendant pattern

list[MuExp] translatePat(p:(Pattern) `/ <Pattern pattern>`) {
    throw "Descendant pattern";
}

// typedVariableBecomes pattern
list[MuExp] translatePat(p:(Pattern) `<Type \type> <Name name> : <Pattern pattern>`) {
    throw "typedVariableBecomes pattern";
}


// Default rule for pattern translation

default list[MuExp] translatePat(Pattern p) { throw "Pattern <p> cannot be translated"; }

// Translate patterns as element of a list pattern

list[MuExp] translatePatAsListElem(p:(Pattern) `<QualifiedName name>`) {
   <scopeId, pos> = getVariableScope("<name>", name@\loc);
   return [ muCreate(muFun("MATCH_VAR_IN_LIST"), [muVarRef("<name>", scopeId, pos)]) ];
} 

list[MuExp] translatePatAsListElem(p:(Pattern) `<QualifiedName name>*`) {
   <scopeId, pos> = getVariableScope("<name>", p@\loc);
   return [ muCreate(muFun("MATCH_MULTIVAR_IN_LIST"), [muVarRef("<name>", scopeId, pos)]) ];
}

list[MuExp] translatePatAsListElem(p:(Pattern) `*<Type \type> <Name name>`) {
   <scopeId, pos> = getVariableScope("<name>", p@\loc);
   return [ muCreate(muFun("MATCH_MULTIVAR_IN_LIST"), [muVarRef("<name>", scopeId, pos)]) ];
}

list[MuExp] translatePatAsListElem(p:(Pattern) `*<Name name>`) {
   <scopeId, pos> = getVariableScope("<name>", name@\loc);
   return [ muCreate(muFun("MATCH_MULTIVAR_IN_LIST"), [muVarRef("<name>", scopeId, pos)]) ];
} 

list[MuExp] translatePatAsListElem(p:(Pattern) `+<Pattern argument>`) {
  throw "splicePlus pattern";
}   

default list[MuExp] translatePatAsListElem(Pattern p) {
  iprintln(p);
return [ muCreate(muFun("MATCH_PAT_IN_LIST"), translatePat(p)) ];
}

/*********************************************************************/
/*                  End of Patterns                                  */
/*********************************************************************/

bool backtrackFree(p:(Pattern) `[<{Pattern ","}* pats>]`) = false;
bool backtrackFree(p:(Pattern) `{<{Pattern ","}* pats>}`) = false;

default bool backtrackFree(Pattern p) = true;
