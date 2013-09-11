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

MuExp translatePat(p:(Pattern) `<Literal lit>`) = muCreate(mkCallToLibFun("Library","MATCH_LITERAL",2), [translate(lit)]);

MuExp translatePat(p:(Pattern) `<Concrete concrete>`) { throw("Concrete"); }
     
MuExp translatePat(p:(Pattern) `<QualifiedName name>`) {
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   println("transPattern: <fuid>, <pos>");
   return muCreate(mkCallToLibFun("Library","MATCH_VAR",2), [muVarRef("<name>", fuid, pos)]);
} 
     
MuExp translatePat(p:(Pattern) `<Type tp> <Name name>`){
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_TYPED_VAR",3), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos)]);
}  

// reifiedType pattern

MuExp translatePat(p:(Pattern) `type ( <Pattern symbol> , <Pattern definitions> )`) {
    throw "reifiedType pattern";
}

// callOrTree pattern

MuExp translatePat(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments keywordArguments> )`) {
   MuExp fun_pat;
   if(expression is qualifiedName){
      fun_pat = muCreate(mkCallToLibFun("Library","MATCH_LITERAL",2), [muCon("<expression>")]);
   } else {
     fun_pat = translatePat(expression);
   }
   return muCreate(mkCallToLibFun("Library","MATCH_CALL_OR_TREE",2), [muCallMuPrim("make_array", fun_pat + [ translatePat(pat) | pat <- arguments ])]);
}


// Set pattern

MuExp translatePat(p:(Pattern) `{<{Pattern ","}* pats>}`) {
     throw "set pattern";
}

// Tuple pattern

MuExp translatePat(p:(Pattern) `\<<{Pattern ","}* pats>\>`) {
    return muCreate(mkCallToLibFun("Library","MATCH_TUPLE",2), [muCallMuPrim("make_array", [ translatePat(pat) | pat <- pats ])]);
}


// List pattern 

MuExp translatePat(p:(Pattern) `[<{Pattern ","}* pats>]`) =
    muCreate(mkCallToLibFun("Library","MATCH_LIST",2), [muCallMuPrim("make_array", [ translatePatAsListElem(pat) | pat <- pats ])]);

// Variable becomes pattern

MuExp translatePat(p:(Pattern) `<Name name> : <Pattern pattern>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muCreate(mkCallToLibFun("Library","MATCH_VAR_BECOMES",3), [muVarRef("<name>", fuid, pos), translatePat(pattern)]);
}

// asType pattern

MuExp translatePat(p:(Pattern) `[ <Type tp> ] <Pattern argument>`) =
    muCreate(mkCallToLibFun("Library","MATCH_AS_TYPE",3), [muTypeCon(translateType(tp)), translatePat(argument)]);

// Descendant pattern

MuExp translatePat(p:(Pattern) `/ <Pattern pattern>`) =
    muCreate(mkCallToLibFun("Library","MATCH_DESCENDANT",2), [translatePat(pattern)]);

// Anti-pattern
MuExp translatePat(p:(Pattern) `! <Pattern pattern>`) =
    muCreate(mkCallToLibFun("Library","MATCH_ANTI",2), [translatePat(pattern)]);

// typedVariableBecomes pattern
MuExp translatePat(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muCreate(mkCallToLibFun("Library","MATCH_TYPED_VAR_BECOMES",4), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos), translatePat(pattern)]);
}

// Default rule for pattern translation

default MuExp translatePat(Pattern p) { throw "Pattern <p> cannot be translated"; }

// Translate patterns as element of a list pattern

MuExp translatePatAsListElem(p:(Pattern) `<QualifiedName name>`) {
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_VAR_IN_LIST",4), [muVarRef("<name>", fuid, pos)]);
} 

MuExp translatePatAsListElem(p:(Pattern) `<QualifiedName name>*`) {
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_MULTIVAR_IN_LIST",4), [muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsListElem(p:(Pattern) `*<Type tp> <Name name>`) {
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_TYPED_MULTIVAR_IN_LIST",5), [muTypeCon(\list(translateType(tp))), muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsListElem(p:(Pattern) `*<Name name>`) {
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_MULTIVAR_IN_LIST",4), [muVarRef("<name>", fuid, pos)]);
} 

MuExp translatePatAsListElem(p:(Pattern) `+<Pattern argument>`) {
  throw "splicePlus pattern";
}   

default MuExp translatePatAsListElem(Pattern p) {
  return muCreate(mkCallToLibFun("Library","MATCH_PAT_IN_LIST",4), [translatePat(p)]);
}

/*********************************************************************/
/*                  End of Patterns                                  */
/*********************************************************************/

bool backtrackFree(p:(Pattern) `[<{Pattern ","}* pats>]`) = false;
bool backtrackFree(p:(Pattern) `{<{Pattern ","}* pats>}`) = false;

default bool backtrackFree(Pattern p) = true;


/*********************************************************************/
/*                  Signature Patterns                               */
/*********************************************************************/

MuExp translateFormals(list[Pattern] formals, int i, MuExp body){
   if(isEmpty(formals))
      return muReturn(body);
   
   pat = formals[0];
   if(pat is literal){
      return muIfelse(muOne([ muCallMuPrim("equal", [muLoc("<i>",i), translate(pat.literal)]) ]),
                   [ translateFormals(tail(formals), i + 1, body) ],
                   [ muFailReturn() ]
                  );
   } else {
      name = pat.name;
      tp = pat.\type;
      <fuid, pos> = getVariableScope("<name>", name@\loc);
      return muIfelse(muOne([ muCallMuPrim("check_arg_type", [ muLoc("<i>",i), muTypeCon(translateType(tp)) ]) ]),
                   [ muAssign("<name>", fuid, pos, muLoc("<i>", i)),
                     translateFormals(tail(formals), i + 1, body) 
                   ],
                   [ muFailReturn() ]
                  );
    }
}

MuExp translateFunction({Pattern ","}* formals, MuExp body){
  bool b = true;
  for(pat <- formals){
      if(!(pat is typedVariable || pat is literal))
      b = false;
  }
  if(b){    //TODO: should be: all(pat <- formals, (pat is typedVariable || pat is literal))){
     return translateFormals([formal | formal <- formals], 0, body);
  } else {
	  list[MuExp] conditions = [];
	  int i = 0;
	  for(Pattern pat <- formals) {
	      conditions += muMulti(muCreate(mkCallToLibFun("Library","MATCH",2), [ *translatePat(pat), muLoc("<i>",i) ]));
	      i += 1;
	  };
	  return muIfelse(muOne(conditions), [ muReturn(body) ], [ muFailReturn() ]);
  }
}



