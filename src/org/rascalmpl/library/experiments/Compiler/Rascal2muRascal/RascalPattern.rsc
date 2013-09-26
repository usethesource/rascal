@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalPattern

import Prelude;

import lang::rascal::\syntax::Rascal;
import experiments::Compiler::Rascal2muRascal::RascalExpression;
import experiments::Compiler::Rascal2muRascal::RascalStatement;
import experiments::Compiler::Rascal2muRascal::RascalType;

import experiments::Compiler::muRascal::AST;

import experiments::Compiler::Rascal2muRascal::TmpAndLabel;
import experiments::Compiler::Rascal2muRascal::TypeUtils;

/*********************************************************************/
/*                  Patterns                                         */
/*********************************************************************/

MuExp translatePat(p:(Pattern) `<RegExpLiteral r>`) = translateRegExpLiteral(r);

default MuExp translatePat(p:(Pattern) `<Literal lit>`) = muCreate(mkCallToLibFun("Library","MATCH_LITERAL",2), [translate(lit)]);
/*
lexical RegExpLiteral
	= "/" RegExp* "/" RegExpModifier ;

lexical NamedRegExp
	= "\<" Name "\>" 
	| [\\] [/ \< \> \\] 
	| NamedBackslash 
	| ![/ \< \> \\] ;

lexical RegExpModifier
	= [d i m s]* ;

lexical RegExp
	= ![/ \< \> \\] 
	| "\<" Name "\>" 
	| [\\] [/ \< \> \\] 
	| "\<" Name ":" NamedRegExp* "\>" 
	| Backslash 
	// | @category="MetaVariable" [\<]  Expression expression [\>] TODO: find out why this production existed 
	;
lexical NamedBackslash
	= [\\] !>> [\< \> \\] ;
*/

MuExp translateRegExpLiteral((RegExpLiteral) `/<RegExp* rexps>/<RegExpModifier modifier>`){

 swriter = nextTmp();
 fragmentCode = [];
 varrefs = [];
 str fragment = "";
 modifierString = "<modifier>";
 for(i <- [0 .. size(modifierString)]){
     fragment += "(?<modifierString[i]>)";
 }
 for(r <- rexps){
   if(size("<r>") == 1){
      fragment += "<r>";
   } else {
     if(size(fragment) > 0){
        fragmentCode += muCon(fragment);
        fragment = "";
     }
     <varref, fragmentCode1> = extractNamedRegExp(r);
     if(varref != muCon("")){ // This is a hack to handle an absent assignable variable
        varrefs += varref;
     }
     fragmentCode += fragmentCode1;
   }
 }
 if(size(fragment) > 0){
        fragmentCode += muCon(fragment);
 }
 buildRegExp = muBlock(muAssignTmp(swriter, muCallPrim("stringwriter_open", [])) + 
                       [ muCallPrim("stringwriter_add", [muTmp(swriter), exp]) | exp <- fragmentCode ] +
                       muCallPrim("stringwriter_close", [muTmp(swriter)]));
 
 return muCreate(mkCallToLibFun("Library", "MATCH_REGEXP", 3), 
                 [ buildRegExp,
                   muCallMuPrim("make_array", varrefs)
                 ]);  
}

tuple[MuExp,list[MuExp] ] extractNamedRegExp((RegExp) `\<<Name name>\>`) = 
    <muCon(""), [ muCallPrim("str_escape_for_regexp", [ translate(name) ])]>;

tuple[MuExp, list[MuExp]] extractNamedRegExp((RegExp) `\<<Name name>:<NamedRegExp* namedregexps>\>`) {
  exps = [];
  str fragment = "(";
  for(nr <- namedregexps){
      println("nr = <nr>");
      if(size("<nr>") == 1){
        fragment += "<nr>";
      } else if((NamedRegExp) `\<<Name name2>\>` := nr){
        println("Name case: <name2>");
        if(fragment != ""){
           exps += muCon(fragment);
           fragment = "";
        }
        exps += translate(name2);
      }
  }
  exps += muCon(fragment + ")");
  return <mkVarRef("<name>", name@\loc), exps>;
}


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
      fun_pat = muCreate(mkCallToLibFun("Library","MATCH_LITERAL",2), [muCon(getType(expression@\loc).name)]);
   } else {
     fun_pat = translatePat(expression);
   }
   return muCreate(mkCallToLibFun("Library","MATCH_CALL_OR_TREE",2), [muCallMuPrim("make_array", fun_pat + [ translatePat(pat) | pat <- arguments ])]);
}

// Set pattern

MuExp translatePat(p:(Pattern) `{<{Pattern ","}* pats>}`) = translateSetPat(p);

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
    muCreate(mkCallToLibFun("Library","MATCH_DESCENDANT",2), [translatePatinDescendant(pattern)]);

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

// Patterns that are part of a descendant pattern

MuExp translatePatinDescendant(p:(Pattern) `<Literal lit>`) = muCreate(mkCallToLibFun("Library","MATCH_AND_DESCENT",2), [muCreate(mkCallToLibFun("Library","MATCH_AND_DESCENT_LITERAL",2), [translate(lit)])]);

default MuExp translatePatinDescendant(Pattern p) = translatePat(p);

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

// Translate Set pattern

// Translate patterns as element of a set pattern

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>`) {
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_VAR_IN_SET",2), [muVarRef("<name>", fuid, pos)]);
} 

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>*`) {
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_MULTIVAR_IN_SET",2), [muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Type tp> <Name name>`) {
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_TYPED_MULTIVAR_IN_SET",3), [muTypeCon(\set(translateType(tp))), muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Name name>`) {
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_MULTIVAR_IN_SET",2), [muVarRef("<name>", fuid, pos)]);
} 

MuExp translatePatAsSetElem(p:(Pattern) `+<Pattern argument>`) {
  throw "splicePlus pattern";
}   

default MuExp translatePatAsSetElem(Pattern p) {
  return muCreate(mkCallToLibFun("Library","MATCH_PAT_IN_SET",2), [translatePat(p)]);
}

MuExp translateSetPat(p:(Pattern) `{<{Pattern ","}* pats>}`) {
   literals = [];
   multiVars = [];
   otherPats = [];
   for(pat <- pats){
      if(pat is literal){
         literals += translate(pat.literal);
      } else if(pat is splice || pat is multiVariable){
         multiVars += translatePatAsSetElem(pat);
      } else {
        otherPats +=  muCreate(mkCallToLibFun("Library","MATCH_PAT_IN_SET",2), [translatePat(pat)]);
      }
   }
   return muCreate(mkCallToLibFun("Library","MATCH_SET",2), [ muCallMuPrim("make_array", [ muCallPrim("set_create", literals), 
                                                                                           muCallMuPrim("make_array", otherPats + multiVars) ]) ] );
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

MuExp translateFormals(list[Pattern] formals, int i, node body){
   if(isEmpty(formals))
      return muReturn(translateFunctionBody(body));
   
   pat = formals[0];
   if(pat is literal){
   	  // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
  	  ifname = nextLabel();
      enterBacktrackingScope(ifname);
      exp = muIfelse(ifname,muAll([ muCallMuPrim("equal", [muLoc("<i>",i), translate(pat.literal)]) ]),
                   [ translateFormals(tail(formals), i + 1, body) ],
                   [ muFailReturn() ]
                  );
      leaveBacktrackingScope();
      return exp;
   } else {
      name = pat.name;
      tp = pat.\type;
      <fuid, pos> = getVariableScope("<name>", name@\loc);
      // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
  	  ifname = nextLabel();
      enterBacktrackingScope(ifname);
      exp = muIfelse(ifname,muAll([ muCallMuPrim("check_arg_type", [ muLoc("<i>",i), muTypeCon(translateType(tp)) ]) ]),
                   [ muAssign("<name>", fuid, pos, muLoc("<i>", i)),
                     translateFormals(tail(formals), i + 1, body) 
                   ],
                   [ muFailReturn() ]
                  );
      leaveBacktrackingScope();
      return exp;
    }
}

MuExp translateFunction({Pattern ","}* formals, node body, list[Expression] when_conditions){
  bool b = true;
  for(pat <- formals){
      if(!(pat is typedVariable || pat is literal))
      b = false;
  }
  if(b) { //TODO: should be: all(pat <- formals, (pat is typedVariable || pat is literal))) {
  	
  	 if(isEmpty(when_conditions)){
  	    return  translateFormals([formal | formal <- formals], 0, body);
  	  } else {
  	    ifname = nextLabel();
        enterBacktrackingScope(ifname);
        conditions += [ translate(cond) | cond <- when_conditions];
        mubody = muIfelse(ifname,muAll(conditions), [ muReturn(translateFunctionBody(body)) ], [ muFailReturn() ]);
	    leaveBacktrackingScope();
	    return mubody;
  	  }
  } else {
	  list[MuExp] conditions = [];
	  int i = 0;
	  // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
  	  ifname = nextLabel();
      enterBacktrackingScope(ifname);
	  for(Pattern pat <- formals) {
	      conditions += muMulti(muCreate(mkCallToLibFun("Library","MATCH",2), [ *translatePat(pat), muLoc("<i>",i) ]));
	      i += 1;
	  };
	  conditions += [ translate(cond) | cond <- when_conditions];

	  mubody = muIfelse(ifname,muAll(conditions), [ muReturn(translateFunctionBody(body)) ], [ muFailReturn() ]);
	  leaveBacktrackingScope();
	  return mubody;
  }
}

MuExp translateFunctionBody(Expression exp) = translate(exp);
MuExp translateFunctionBody(MuExp exp) = exp;
// TODO: check the interpreter subtyping
default MuExp translateFunctionBody(Statement* stats) = muBlock([ translate(stat) | stat <- stats ]);
