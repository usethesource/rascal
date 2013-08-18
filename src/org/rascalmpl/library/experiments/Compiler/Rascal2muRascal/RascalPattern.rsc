@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalPattern

import Prelude;

import lang::rascal::\syntax::Rascal;
import experiments::Compiler::Rascal2muRascal::RascalExpression;

import experiments::Compiler::muRascal::AST;

/*********************************************************************/
/*                  Patterns                                         */
/*********************************************************************/
 
list[MuExp] translatePat(p:(Pattern) `<BooleanLiteral b>`) = [ muCreate("MATCH_BOOL", translate(b)) ];

list[MuExp] translatePat(p:(Pattern) `<IntegerLiteral n>`) = [ muCreate("MATCH_INT", translate(n)) ];
     
list[MuExp] translatePat(p:(Pattern) `<StringLiteral s>`, Expression subject) =   [ muCreate("MATCH_STR", translate(b)) ];
     
list[MuExp] translatePat(p:(Pattern) `<QualifiedName name>`, Expression subject) {
   <scopeId, pos> = getVariableScope("<name>", name@\loc);
   return [ muCreate("MATCH_VAR", [muCon(scopeId), muCon(pos)]) ];
}   

     
list[MuExp] translatePat(p:(Pattern) `<Type tp> <Name name>`){
   <scopeId, pos> = getVariableScope("<name>", name@\loc);
   return [ muCreate("MATCH_VAR", [muCon(scopeId), muCon(pos)]) ];
}  

default list[MuExp] translatePat(Pattern p) { throw "Pattern <p> cannot be translated"; }

// List matching.
// All coroutines involved in list matching have the signature:
//		coroutine (list[&T] subject) resume tuple[bool,int](bool forward, int startPos)
// 1. Handle the list pattern
// 2. Wrap all other patterns to comply with the above signature
// 3. The cases of list variables are handled specially.

list[MuExp] translatePat(p:(Pattern) `[<{Pattern ","}* pats>]`) {
  
  return [ muCreate("MATCH_LIST", [ *translatePat(pat) | pat <- pats ]) ];
}

// Translate patterns as element of a list pattern

str translatePatAsListElem(p:(Pattern) `* <QualifiedName name>`) = 
   "coroutine (list[&T] subject) resume tuple[bool,int](bool forward, int startPos) { 
   '  while(true){
   '     int endPos = startPos;
   '     \<forward, startPos\> = yield \<true, endPos\>;
   '     if(forward && endPos \< size(subject)){
   '        endPos += 1;
   '        <name> = subject[startPos .. endPos];
   '        yield \<true, endPos\>;
   '     }
   '  } 
   '}";
  
default str translatePatAsListElem(Pattern p) = asListElem(translatePat(p));

// Wrap the translation of a pattern element as element of a list pattern

str asListElem(str code) = 
    "coroutine (list[&T] subject) resume tuple[bool,int](int startPos) { 
    '  while(true) { c = <code>.start(subject[startPos]); yield \<c.resume(), startPos + 1\>; 
    '}";
    
/*********************************************************************/
/*                  End of Patterns                                  */
/*********************************************************************/

bool backtrackFree(p:(Pattern) `[<{Pattern ","}* pats>]`) = false;
bool backtrackFree(p:(Pattern) `{<{Pattern ","}* pats>}`) = false;

default bool backtrackFree(Pattern p) = true;
