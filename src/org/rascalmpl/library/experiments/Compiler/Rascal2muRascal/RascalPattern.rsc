@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalPattern

import Prelude;

import lang::rascal::\syntax::Rascal;
import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::Rascal2muRascal::RascalExpression;
import experiments::Compiler::Rascal2muRascal::RascalStatement;
import experiments::Compiler::Rascal2muRascal::RascalType;

import experiments::Compiler::muRascal::AST;

import experiments::Compiler::Rascal2muRascal::TmpAndLabel;
import experiments::Compiler::Rascal2muRascal::TypeUtils;
import experiments::Compiler::Rascal2muRascal::TypeReifier;

import experiments::Compiler::RVM::Interpreter::ParsingTools;

/*********************************************************************/
/*                  Match                                            */
/*********************************************************************/

MuExp translateMatch((Expression) `<Pattern pat> := <Expression exp>`)  = translateMatch(pat, exp);
   
MuExp translateMatch((Expression) `<Pattern pat> !:= <Expression exp>`) =
    muCallMuPrim("not_mbool", [ makeMu("ALL", [ translateMatch(pat, exp) ]) ]);
    
default MuExp translateMatch(Pattern pat, Expression exp) =
    muMulti(muCreate(mkCallToLibFun("Library","MATCH",2), [translatePat(pat), translate(exp)]));

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

map[str,str] regexpEscapes = (
"(" : "(?:",
")" : ")"
);

MuExp translateRegExpLiteral((RegExpLiteral) `/<RegExp* rexps>/<RegExpModifier modifier>`){
   str fuid = topFunctionScope();
   swriter = nextTmp();
   fragmentCode = [];
   varrefs = [];
   varnames = ();
   str fragment = "";
   modifierString = "<modifier>";
   for(i <- [0 .. size(modifierString)]){
      fragment += "(?<modifierString[i]>)";
   }
   lrexps = [r | r <- rexps];
   len = size(lrexps); // library!
   i = 0;
   while(i < len){
      r = lrexps[i];
      //println("regexp: <r>");
      if("<r>" == "\\"){
         fragment += "\\" + (i < len  - 1 ? "<lrexps[i + 1]>" : "");
         i += 2;
      } else 
      if(size("<r>") == 1){
         if("<r>" == "(" && i < (len  - 1) && "<lrexps[i + 1]>" == "?"){
           fragment += "(";
         } else {
           fragment += escape("<r>", regexpEscapes);
         }
         i += 1;
      } else {
        if(size(fragment) > 0){
            fragmentCode += muCon(fragment);
            fragment = "";
        }
        switch(r){
          case (RegExp) `\<<Name name>\>`:
        	if(varnames["<name>"]?){
        	   fragment += "\\<varnames["<name>"]>";
        	} else {
        	  fragmentCode += [ muCallPrim("str_escape_for_regexp", [ translate(name) ])];
        	}
          case (RegExp) `\<<Name name>:<NamedRegExp* namedregexps>\>`: {
         		<varref, fragmentCode1> = extractNamedRegExp(r);
         		fragmentCode += fragmentCode1;
         		varrefs += varref;
         		varnames["<name>"] = size(varrefs);
         	}
          default:
        	fragmentCode += [muCon("<r>")];
        }
        i += 1;
      }
   }
   
   if(size(fragment) > 0){
      fragmentCode += muCon(fragment);
   }
   buildRegExp = muBlock(muAssignTmp(swriter, fuid, muCallPrim("stringwriter_open", [])) + 
                       [ muCallPrim("stringwriter_add", [muTmp(swriter,fuid), exp]) | exp <- fragmentCode ] +
                       muCallPrim("stringwriter_close", [muTmp(swriter,fuid)]));
 
   return muCreate(mkCallToLibFun("Library", "MATCH_REGEXP", 3), 
                 [ buildRegExp,
                   muCallMuPrim("make_array", varrefs)
                 ]);  
}

tuple[MuExp, list[MuExp]] extractNamedRegExp((RegExp) `\<<Name name>:<NamedRegExp* namedregexps>\>`) {
   exps = [];
   str fragment = "(";
   for(nr <- namedregexps){
       elm = "<nr>";
       if(size(elm) == 1){
         fragment += escape(elm, regexpEscapes);
       } else if(elm[0] == "\\"){
         fragment += elm[0..];
       } else if((NamedRegExp) `\<<Name name2>\>` := nr){
         //println("Name case: <name2>");
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

MuExp translatePat(p:(Pattern) `<Concrete concrete>`) = translateConcretePattern(p);
     
MuExp translatePat(p:(Pattern) `<QualifiedName name>`) {
   if("<name>" == "_"){
      return muCreate(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR",1), []);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   //println("transPattern: <fuid>, <pos>");
   return muCreate(mkCallToLibFun("Library","MATCH_VAR",2), [muVarRef("<name>", fuid, pos)]);
} 
     
MuExp translatePat(p:(Pattern) `<Type tp> <Name name>`){
   if("<name>" == "_"){
      return muCreate(mkCallToLibFun("Library","MATCH_TYPED_ANONYMOUS_VAR",2), [muTypeCon(translateType(tp))]);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_TYPED_VAR",3), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos)]);
}  

// reifiedType pattern

MuExp translatePat(p:(Pattern) `type ( <Pattern symbol> , <Pattern definitions> )`) {    
    return muCreate(mkCallToLibFun("Library","MATCH_REIFIED_TYPE",2), [muCon(symbol)]);
}

// callOrTree pattern

MuExp translatePat(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments keywordArguments> )`) {
   MuExp fun_pat;
   if(expression is qualifiedName){
      fun_pat = muCreate(mkCallToLibFun("Library","MATCH_LITERAL",2), [muCon(getType(expression@\loc).name)]);
   } else {
     fun_pat = translatePat(expression);
   }
   return muCreate(mkCallToLibFun("Library","MATCH_CALL_OR_TREE",2), [muCallMuPrim("make_array", fun_pat + [ translatePat(pat) | pat <- arguments ] 
                                                                                                 //+ translatePatKWArguments(keywordArguments)
                                                                                  )]);
}

MuExp translatePatKWArguments((KeywordArguments) ``) = muCon(());

MuExp translatePatKWArguments((KeywordArguments) `<OptionalComma optionalComma> <{KeywordArgument ","}+ keywordArgumentList>`) {
   keyword_names = [];
   pats = [];
   for(kwarg <- keywordArgumentList){
       keyword_names += muCon("<kwarg.name>");
       pats += translatePatKWValue(kwarg.expression);
   }
   return muCreate(mkCallToLibFun("Library","MATCH_MAP",3), [muCallMuPrim("make_array", keyword_names), muCallMuPrim("make_array", pats)]);
}

MuExp translatePatKWValue(e: (Expression) `<Literal lit>`) = muCreate(mkCallToLibFun("Library","MATCH_LITERAL",2), [translate(lit)]);

MuExp translatePatKWValue(e: (Expression) `<QualifiedName name>`) {
  if("<name>" == "_"){
      return muCreate(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR",1), []);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   //println("transPattern: <fuid>, <pos>");
   return muCreate(mkCallToLibFun("Library","MATCH_VAR",2), [muVarRef("<name>", fuid, pos)]);
}


// Set pattern

MuExp translatePat(p:(Pattern) `{<{Pattern ","}* pats>}`) = translateSetPat(p);

// Tuple pattern

MuExp translatePat(p:(Pattern) `\<<{Pattern ","}* pats>\>`) {
    return muCreate(mkCallToLibFun("Library","MATCH_TUPLE",2), [muCallMuPrim("make_array", [ translatePat(pat) | pat <- pats ])]);
}

// List pattern 

MuExp translatePat(p:(Pattern) `[<{Pattern ","}* pats>]`) {
    lookahead = computeLookahead(p);  
    lpats = [pat | pat <- pats];   //TODO: should be unnnecessary
    return muCreate(mkCallToLibFun("Library","MATCH_LIST",2), [muCallMuPrim("make_array", [ translatePatAsListElem(lpats[i], lookahead[i]) | i <- index(lpats) ])]);
}

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

/**********************************************************************/
/*                 Constant Patterns                                  */
/**********************************************************************/

value translatePatternAsConstant(p:(Pattern) `<Literal lit>`) = getLiteralValue(lit) when !(lit is regExp);

value translatePatternAsConstant(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments keywordArguments> )`) =
  makeNode("<expression>", [ translatePatternAsConstant(pat) | pat <- arguments ]);

value translatePatternAsConstant(p:(Pattern) `{<{Pattern ","}* pats>}`) = { translatePatternAsConstant(pat) | pat <- pats };

value translatePatternAsConstant(p:(Pattern) `[<{Pattern ","}* pats>]`) = [ translatePatternAsConstant(pat) | pat <- pats ];

value translatePatternAsConstant(p:(Pattern) `\<<{Pattern ","}* pats>\>`) {
  lpats = [ pat | pat <- pats]; // TODO
  return ( <translatePatternAsConstant(lpats[0])> | it + <translatePatternAsConstant(lpats[i])> | i <- [1 .. size(lpats)] );
}
 
default value translatePatternAsConstant(Pattern p){
  throw "Not a constant pattern: <p>";
}

/*********************************************************************/
/*                  Concrete Pattern                                */
/*********************************************************************/
/*
lexical Concrete 
  = typed: "(" LAYOUTLIST l1 Sym symbol LAYOUTLIST l2 ")" LAYOUTLIST l3 "`" ConcretePart* parts "`";

lexical ConcretePart
  = @category="MetaSkipped" text   : ![`\<\>\\\n]+ !>> ![`\<\>\\\n]
  | newline: "\n" [\ \t \u00A0 \u1680 \u2000-\u200A \u202F \u205F \u3000]* "\'"
  | @category="MetaVariable" hole : ConcreteHole hole
  | @category="MetaSkipped" lt: "\\\<"
  | @category="MetaSkipped" gt: "\\\>"
  | @category="MetaSkipped" bq: "\\`"
  | @category="MetaSkipped" bs: "\\\\"
  ;
  
syntax ConcreteHole 
  = \one: "\<" Sym symbol Name name "\>"
  ;
*/

MuExp translateConcretePattern(p:(Pattern) `<Concrete concrete>`) { 
  println("**** Grammar");
  iprintln(getGrammar(config));
  parsedFragment = parseFragment(getModuleName(), concrete, p@\loc, getGrammar(config));
  println("**** parsedFragment");
  iprintln(parsedFragment);
  return translateParsedConcretePattern(parsedFragment);
}

MuExp translateParsedConcretePattern(t:appl(Production prod, list[Tree] args)){
  if(prod.def == label("hole", lex("ConcretePart"))){
     varloc = args[0].args[4].args[0]@\loc;
     <fuid, pos> = getVariableScope("ConcreteVar", varloc);
     return muCreate(mkCallToLibFun("Library","MATCH_VAR",2), [muVarRef("ConcreteVar", fuid, pos)]);
  }
  
  applCode = muCreate(mkCallToLibFun("Library","MATCH_LITERAL",2), [muCon("appl")]);
  prodCode = muCreate(mkCallToLibFun("Library","MATCH_LITERAL",2), [muCon(prod)]);
  argsCode = translateConcreteListPattern(args);
  return muCreate(mkCallToLibFun("Library","MATCH_CALL_OR_TREE",2), [muCallMuPrim("make_array", [applCode, prodCode, argsCode] )]);
}

MuExp translateParsedConcretePattern(cc: char(int c)) {
  return muCreate(mkCallToLibFun("Library","MATCH_LITERAL",2), [muCon(cc)]);
}

default MuExp translateParsedConcretePattern(Tree c) {
   iprintln(c);
   throw "translateParsedConcretePattern: Cannot handle <c>";
}

MuExp translateConcreteListPattern(list[Tree] pats){
 lookahead = computeConcreteLookahead(pats);  
 arb = muCreate(mkCallToLibFun("Library","MATCH_ARB_IN_LIST",3), []);
 return muCreate(mkCallToLibFun("Library","MATCH_LIST",2), [muCallMuPrim("make_array", 
         [ (i % 2 == 0) ? translatePatAsConcreteListElem(pats[i], lookahead[i]) : arb | i <- index(pats) ])]);
}

bool isIter(\iter(Symbol symbol)) = true;
bool isIter(\iter-star(Symbol symbol)) = true;
bool isIter(\iter-seps(Symbol symbol, list[Symbol] separators)) = true;
bool isIter(\iter-star-seps(Symbol symbol, list[Symbol] separators)) = true;
default bool isIter(Symbol s) = false;

MuExp translatePatAsConcreteListElem(t:appl(Production prod, list[Tree] args), Lookahead lookahead){
  if(prod.def == label("hole", lex("ConcretePart"))){
     //println(t@\holeType);
     varloc = args[0].args[4].args[0]@\loc;
     <fuid, pos> = getVariableScope("ConcreteVar", varloc);
    holeType = getType(varloc);
     if(isIter(holeType))
        return muCreate(mkCallToLibFun("Library","MATCH_MULTIVAR_IN_LIST",5), [muVarRef("ConcreteListVar", fuid, pos), muCon(lookahead.nElem)]);
     return muCreate(mkCallToLibFun("Library","MATCH_VAR_IN_LIST",4), [muVarRef("ConcreteVar", fuid, pos)]);
  }
  
  return muCreate(mkCallToLibFun("Library","MATCH_APPL_IN_LIST",5), [muCon(prod), translateConcreteListPattern(args)]);
}

MuExp translatePatAsConcreteListElem(cc: char(int c), Lookahead lookahead){
  return muCreate(mkCallToLibFun("Library","MATCH_LITERAL_IN_LIST",4), [muCon(cc)]);
}

default MuExp translatePatAsConcreteListElem(Tree c, Lookahead lookahead){
  return muCreate(mkCallToLibFun("Library","MATCH_PAT_IN_LIST",4), [translateParsedConcretePattern(c)]);
}

bool isConcreteMultiVar(t:appl(Production prod, list[Tree] args)){
  if(prod.def == label("hole", lex("ConcretePart"))){
     varloc = args[0].args[4].args[0]@\loc;
     holeType = getType(varloc);
     return isIter(holeType);
  }
  return false;
}

default bool isConcreteMultiVar(Tree t) = false;

list[Lookahead] computeConcreteLookahead(list[Tree] pats){
println("computeConcreteLookahead: <pats>");
    nElem = 0;
    nMultiVar = 0;
    rprops = for(p <- reverse([p | p <- pats])){
                 append <nElem, nMultiVar>;
                 if(isConcreteMultiVar(p)) nMultiVar += 1; else nElem += 1;
             };
    println("result = <reverse(rprops)>");
    return reverse(rprops);
}

/*********************************************************************/
/*                  Descendant Pattern                               */
/*********************************************************************/

MuExp translatePatinDescendant(p:(Pattern) `<Literal lit>`) = muCreate(mkCallToLibFun("Library","MATCH_AND_DESCENT",2), [muCreate(mkCallToLibFun("Library","MATCH_AND_DESCENT_LITERAL",2), [translate(lit)])]);

default MuExp translatePatinDescendant(Pattern p) = translatePat(p);

/*********************************************************************/
/*                  List Pattern                                     */
/*********************************************************************/

bool isMultiVar(p:(Pattern) `<QualifiedName name>*`) = true;
bool isMultiVar(p:(Pattern) `*<Type tp> <Name name>`) = true;
bool isMultiVar(p:(Pattern) `*<Name name>`) = true;
default bool isMultiVar(Pattern p) = false;

alias Lookahead = tuple[int nElem, int nMultiVar];

list[Lookahead] computeLookahead((Pattern) `[<{Pattern ","}* pats>]`){
    nElem = 0;
    nMultiVar = 0;
    rprops = for(p <- reverse([p | p <- pats])){
                 append <nElem, nMultiVar>;
                 if(isMultiVar(p)) nMultiVar += 1; else nElem += 1;
             };
    return reverse(rprops);
}

str isLast(Lookahead lookahead) = lookahead.nMultiVar == 0 ? "LAST_" : "";

MuExp translatePatAsListElem(p:(Pattern) `<QualifiedName name>`, Lookahead lookahead) {
   if("<name>" == "_"){
       return muCreate(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR_IN_LIST",3), []);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_VAR_IN_LIST",4), [muVarRef("<name>", fuid, pos)]);
} 

MuExp translatePatAsListElem(p:(Pattern) `<Type tp> <Name name>`, Lookahead lookahead) {
   if("<name>" == "_"){
       return muCreate(mkCallToLibFun("Library","MATCH_TYPED_ANONYMOUS_VAR_IN_LIST",4), [muTypeCon(translateType(tp))]);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_TYPED_VAR_IN_LIST",5), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos)]);
} 


MuExp translatePatAsListElem(p:(Pattern) `<Literal lit>`, Lookahead lookahead) =
   muCreate(mkCallToLibFun("Library","MATCH_LITERAL_IN_LIST",4), [translate(lit)])
when !(lit is regExp);

MuExp translatePatAsListElem(p:(Pattern) `<QualifiedName name>*`, Lookahead lookahead) {
   if("<name>" == "_"){
       return muCreate(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>ANONYMOUS_MULTIVAR_IN_LIST",4), [muCon(lookahead.nElem)]);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>MULTIVAR_IN_LIST",5), [muVarRef("<name>", fuid, pos), muCon(lookahead.nElem)]);
}

MuExp translatePatAsListElem(p:(Pattern) `*<Type tp> <Name name>`, Lookahead lookahead) {
   if("<name>" == "_"){
      return muCreate(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>TYPED_ANONYMOUS_MULTIVAR_IN_LIST",5), [muTypeCon(\list(translateType(tp))), muCon(lookahead.nElem)]);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>TYPED_MULTIVAR_IN_LIST",6), [muTypeCon(\list(translateType(tp))), muVarRef("<name>", fuid, pos), muCon(lookahead.nElem)]);
}

MuExp translatePatAsListElem(p:(Pattern) `*<Name name>`, Lookahead lookahead) {
   if("<name>" == "_"){
      return muCreate(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>ANONYMOUS_MULTIVAR_IN_LIST",4), [muCon(lookahead.nElem)]);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>MULTIVAR_IN_LIST",5), [muVarRef("<name>", fuid, pos), muCon(lookahead.nElem)]);
} 

MuExp translatePatAsListElem(p:(Pattern) `+<Pattern argument>`, Lookahead lookahead) {
  throw "splicePlus pattern";
}   

default MuExp translatePatAsListElem(Pattern p, Lookahead lookahead) {
  try {
     return  muCreate(mkCallToLibFun("Library","MATCH_LITERAL_IN_LIST",4), [muCon(translatePatternAsConstant(p))]);
  } catch:
    return muCreate(mkCallToLibFun("Library","MATCH_PAT_IN_LIST",4), [translatePat(p)]);
}

/*********************************************************************/
/*                  Set Pattern                                     */
/*********************************************************************/

// Translate patterns as element of a set pattern

str isLast(bool b) = b ? "LAST_" : "";

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>`, bool last) {
   if("<name>" == "_"){
      return muCreate(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR_IN_SET",2), []);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_VAR_IN_SET",3), [muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `<Type tp> <Name name>`, bool last) {
   if("<name>" == "_"){
       return muCreate(mkCallToLibFun("Library","MATCH_TYPED_ANONYMOUS_VAR_IN_SET",3), [muTypeCon(translateType(tp))]);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_TYPED_VAR_IN_SET",4), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos)]);
}  

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>*`, bool last) {
   if("<name>" == "_"){
      return muCreate(mkCallToLibFun("Library","MATCH_<isLast(last)>ANONYMOUS_MULTIVAR_IN_SET",2), []);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_<isLast(last)>MULTIVAR_IN_SET",3), [muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Type tp> <Name name>`, bool last) {
   if("<name>" == "_"){
      return muCreate(mkCallToLibFun("Library","MATCH_<isLast(last)>TYPED_ANONYMOUS_MULTIVAR_IN_SET",3), [muTypeCon(\set(translateType(tp)))]);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_<isLast(last)>TYPED_MULTIVAR_IN_SET",4), [muTypeCon(\set(translateType(tp))), muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Name name>`, bool last) {
   if("<name>" == "_"){
      return muCreate(mkCallToLibFun("Library","MATCH_<isLast(last)>ANONYMOUS_MULTIVAR_IN_SET",2), []);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muCreate(mkCallToLibFun("Library","MATCH_<isLast(last)>MULTIVAR_IN_SET",3), [muVarRef("<name>", fuid, pos)]);
} 

MuExp translatePatAsSetElem(p:(Pattern) `+<Pattern argument>`, bool last) {
  throw "splicePlus pattern";
}   

default MuExp translatePatAsSetElem(Pattern p, bool last) {
  try {
     return  muCreate(mkCallToLibFun("Library","MATCH_LITERAL_IN_SET",3), [muCon(translatePatternAsConstant(p))]);
  } catch:
    return muCreate(mkCallToLibFun("Library","MATCH_PAT_IN_SET",3), [translatePat(p)]);
  //return muCreate(mkCallToLibFun("Library","MATCH_PAT_IN_SET",3), [translatePat(p)]);
}

value getLiteralValue((Literal) `<Literal s>`) =  readTextValueString("<s>"); // TODO interpolation

bool isConstant(StringLiteral l) = l is nonInterpolated;
bool isConstant(LocationLiteral l) = l.protocolPart is nonInterpolated && l.pathPart is nonInterpolated;
default bool isConstant(Literal l) = true;

/*
MuExp translateMatch(p:(Pattern) `{<{Pattern ","}* pats>}`, Expression exp){
   literals = [];
   vars = [];
   compiledVars = [];
   multiVars = [];
   compiledMultiVars = [];
   otherPats = [];
   int lastMulti = -1;
   lpats = [pat | pat <- pats]; // TODO: unnnecessary
   for(i <- [size(lpats) - 1 .. -1]){
       if(lpats[i] is splice || lpats[i] is multiVariable){
          lastMulti = i;
          break;
       }
    }   
   for(i <- index(lpats)){
      pat = lpats[i];
      if(pat is literal){
         literals += pat.literal;
      } else if(pat is splice || pat is multiVariable){
         multiVars += pat;
         compiledMultiVars += translatePatAsSetElem(pat, i == lastMulti);
      } else if(pat is qualifiedName){
         vars += pat;
         compiledVars += translatePatAsSetElem(pat, false);
      } else if(pat is typedVariable){
        vars += pat;
        compiledVars += translatePatAsSetElem(pat, false);
      } else {
        otherPats +=  muCreate(mkCallToLibFun("Library","MATCH_PAT_IN_SET",3), [translatePat(pat)]);
      }
   }
   MuExp litCode;
   if(all(lit <- literals, isConstant(lit))){
   		litCode = muCon({ getLiteralValue(lit) | lit <- literals });
   } else {
   		litCode = muCallPrim("set_create", [ translate(lit) | lit <- literals] );
   }
   
   translatedPatterns = otherPats + compiledVars + compiledMultiVars;
   
   //println("translatedPatterns = <translatedPatterns>");
   
   if(size(otherPats) == 0){
      if(size(multiVars) == 1 && size(vars) == 0){
         if((Pattern) `*<Type typ> <Name name>` := multiVars[0]){	
      	    // literals and single typed splice var
           name = multiVars[0];
           <fuid, pos> = getVariableScope("<name>", name@\loc);
           subject = nextTmp();
           return muBlock([ muAssignTmp(subject, translate(exp)),
                       muIfelse( "SetPat",
                                 muCallMuPrim("and_mbool_mbool", [ muCallPrim("subtype", [muTypeCon(getType(p@\loc)), muTypeCon(getType(exp@\loc))]),
                                                                   muCallPrim("set_lessequal_set",  [ litCode, muTmp(subject) ])
                                                                  ]),
                                 [  muAssignTmp(subject, muCallPrim("set_subtract_set",  [ muTmp(subject), litCode ])),
                                    muIfelse("YYY", muCallPrim("subtype", [muTypeCon(\set(translateType(typ))), muCallPrim("typeOf", [ muTmp(subject) ]) ]),
                                             [ mkAssign("<name>", name@\loc, muTmp(subject)),
                                               muCon(true)
                                             ],
                                             [ muCon(false) ]
                                             )
                                 ],
                                 [ muCon(false) ]
                               )
                     ]);
           } else {
   		     // literals and single multivar or untyped splice
             name = multiVars[0];
             <fuid, pos> = getVariableScope("<name>", name@\loc);
             subject = nextTmp();
             return muBlock([ muAssignTmp(subject, translate(exp)),
                       muIfelse( "SetPat",
                                 muCallMuPrim("and_mbool_mbool", [ muCallPrim("subtype", [muTypeCon(getType(p@\loc)), muTypeCon(getType(exp@\loc))]),
                                                                   muCallPrim("set_lessequal_set",  [ litCode, muTmp(subject) ])
                                                                  ]),
                                 [ mkAssign("<name>", name@\loc, muCallPrim("set_subtract_set",  [ muTmp(subject), litCode ])),
                                            muCon(true)
                                 ],
                                 [ muCon(false) ]
                               )
                     ]);
             }
      }
   
      if(size(multiVars) == 0 && size(vars) == 1){
         if(vars[0] is qualifiedname){	
            // literals and single qualified name
            name = vars[0];
            <fuid, pos> = getVariableScope("<name>", name@\loc);
            subject = nextTmp();
            return muBlock([ muAssignTmp(subject, translate(exp)),
                       muIfelse( "SetPat",
                                 muCallMuPrim("and_mbool_mbool", [ muCallPrim("subtype", [muTypeCon(getType(p@\loc)), muTypeCon(getType(exp@\loc))]),
                                                                   muCallPrim("set_lessequal_set",  [ litCode, muTmp(subject) ])
                                                                  ]),
                                 [  muAssignTmp(subject, muCallPrim("set_subtract_set",  [ muTmp(subject), litCode ])),
                                    muIfelse("XXX",
                                              muCallPrim("int_equal_int", [muCallPrim("set_size", [ muTmp(subject)] ), muCon(1)]),
                                              [ mkAssign("<name>", name@\loc, muCallPrim("set2elm", [ muTmp(subject) ])),
                                                muCon(true)
                                              ],
                                              [ muCon(false) ]
                                           )
                                 ],
                                 [ muCon(false) ]
                               )
                     ]);
         }
   
      if(vars[0] is typedVariable){	
         // literals and single typed name
         typedvar = vars[0];
         typ = typedvar.\type;
         name = typedvar.name;
         <fuid, pos> = getVariableScope("<name>", name@\loc);
         subject = nextTmp();
         elm = nextTmp();
         return muBlock([ muAssignTmp(subject, translate(exp)),
                       muIfelse( "SetPat",
                                 muCallMuPrim("and_mbool_mbool", [ muCallPrim("subtype", [muTypeCon(getType(p@\loc)), muTypeCon(getType(exp@\loc))]),
                                                                   muCallPrim("set_lessequal_set",  [ litCode, muTmp(subject) ])
                                                                  ]),
                                 [  muAssignTmp(subject, muCallPrim("set_subtract_set",  [ muTmp(subject), litCode ])),
                                    muIfelse("XXX",
                                              muCallPrim("int_equal_int", [muCallPrim("set_size", [ muTmp(subject)] ), muCon(1)]),
                                              [ muAssignTmp(elm,  muCallPrim("set2elm", [ muTmp(subject) ])),
                                                muIfelse("YYY", muCallPrim("subtype", [muTypeCon(translateType(typ)), muCallPrim("typeOf", [ muTmp(elm) ])]),
                                                          [ mkAssign("<name>", name@\loc, muTmp(elm)),
                                                            muCon(true)
                                                          ],
                                                          [ muCon(false) ]
                                                          )
                                              ],
                                              [ muCon(false) ]
                                           )
                                 ],
                                 [ muCon(false) ]
                               )
                     ]);
      }
     }
   }
   
   println("translateMatch: SET general case");
   
   patCode = muCreate(mkCallToLibFun("Library","MATCH_SET",2), [ muCallMuPrim("make_array", [ litCode, 
                                                                                               muCallMuPrim("make_array", translatedPatterns) ]) ] );
                                                                                           
   return muMulti(muCreate(mkCallToLibFun("Library","MATCH",2), [patCode, translate(exp)]));
}
*/

/*
 * Get the name of a pattern at position k, when no name, return "_<k>".
 */
private str getName(Pattern pat, int k){
  if(pat is splice){
     arg = pat.argument;
     return arg is qualifiedName ? "<arg>" : "<arg.name>";
  } else if(pat is multiVariable){
    return "<pat.qualifiedName>"; 
  } else if(pat is qualifiedName){
    return "<pat>";  
  } else if(pat is typedVariable){
    return "<pat.name>";
  } else {
    return "_<k>";
  } 
}

/*
 * Translate a set pattern: 
 * - since this is a set, for patterns with the same name, duplicates are removed.
 * - all literal patterns are separated
 * - all other patterns are compiled in order
 * - if the last pattern is a multi-variable it is treated specially.
 * Note: there is an unused optimization here: if the last multi-var in the pattern is followed by other patterns
 * AND these patterns do not refer to that variable, then the multi-var can be moved to the end of the pattern.
*/

MuExp translateSetPat(p:(Pattern) `{<{Pattern ","}* pats>}`) {
   literals = [];
   compiledPats = [];
   lpats = [pat | pat <- pats]; // TODO: unnnecessary
   
   /* remove patterns with duplicate names */
   uniquePats = [];
   outer: for(i <- index(lpats)){
      pat = lpats[i];
      name = getName(pat, i);
      if(name != "_"){
	      for(j <- [0 .. i]){
	          if(getName(lpats[j], j) == name){
	             continue outer;
	          }
	      }
      }
      uniquePats += pat;
   }   
    
   lastPat = size(uniquePats) - 1;
   for(i <- index(uniquePats)){
      pat = uniquePats[i];
      if(pat is literal){
         literals += pat.literal;
      } else if(pat is splice){
        compiledPats += translatePatAsSetElem(pat, i == lastPat);
      } else if(pat is multiVariable){
        compiledPats += translatePatAsSetElem(pat, i == lastPat);
      } else if(pat is qualifiedName){
        compiledPats += translatePatAsSetElem(pat, false);
      } else if(pat is typedVariable){
        compiledPats += translatePatAsSetElem(pat, false);   
      } else {
        compiledPats +=  muCreate(mkCallToLibFun("Library","MATCH_PAT_IN_SET",3), [translatePat(pat)]);
      }
   }
   MuExp litCode = (all(lit <- literals, isConstant(lit))) ? muCon({ getLiteralValue(lit) | lit <- literals })
   		           										   : muCallPrim("set_create", [ translate(lit) | lit <- literals] );
   
   return muCreate(mkCallToLibFun("Library","MATCH_SET",2), [ muCallMuPrim("make_array", [ litCode, 
                                                                                           muCallMuPrim("make_array", compiledPats) ]) ] );
}


/*********************************************************************/
/*                  End of Special Pattern Cases                     */
/*********************************************************************/

/*********************************************************************/
/*                  BacktrackFree for Patterns                       */
/*********************************************************************/

// TODO: Make this more precise

bool backtrackFree(p:(Pattern) `[<{Pattern ","}* pats>]`) = false;
bool backtrackFree(p:(Pattern) `{<{Pattern ","}* pats>}`) = false;

default bool backtrackFree(Pattern p) = true;


/*********************************************************************/
/*                  Signature Patterns                               */
/*********************************************************************/

MuExp translateFormals(list[Pattern] formals, bool isVarArgs, int i, list[MuExp] kwps, node body){
   if(isEmpty(formals))
      return muBlock([ *kwps, muReturn(translateFunctionBody(body)) ]);
   pat = formals[0];
   if(pat is literal){
   	  // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
  	  ifname = nextLabel();
      enterBacktrackingScope(ifname);
      exp = muIfelse(ifname,muCallMuPrim("equal", [ muVar("<i>",topFunctionScope(),i), translate(pat.literal) ]),
                   [ translateFormals(tail(formals), isVarArgs, i + 1, kwps, body) ],
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
      exp = muIfelse(ifname,muCallMuPrim("check_arg_type", [ muVar("<i>",topFunctionScope(),i), muTypeCon( (isVarArgs && size(formals) == 1) ? Symbol::\list(translateType(tp)) : translateType(tp) ) ]),
                   [ muAssign("<name>", fuid, pos, muVar("<i>",topFunctionScope(),i)),
                     translateFormals(tail(formals), isVarArgs, i + 1, kwps, body) 
                   ],
                   [ muFailReturn() ]
                  );
      leaveBacktrackingScope();
      return exp;
    }
}

MuExp translateFunction({Pattern ","}* formals, bool isVarArgs, list[MuExp] kwps, node body, list[Expression] when_conditions){
  bool b = true;
  for(pat <- formals){
      if(!(pat is typedVariable || pat is literal))
      b = false;
  }
  if(b) { //TODO: should be: all(pat <- formals, (pat is typedVariable || pat is literal))) {
  	
  	 if(isEmpty(when_conditions)){
  	    return  translateFormals([formal | formal <- formals], isVarArgs, 0, kwps, body);
  	  } else {
  	    ifname = nextLabel();
        enterBacktrackingScope(ifname);
        conditions = [ translate(cond) | cond <- when_conditions];
        mubody = muIfelse(ifname,makeMu("ALL",conditions), [ *kwps, muReturn(translateFunctionBody(body)) ], [ muFailReturn() ]);
	    leaveBacktrackingScope();
	    return mubody;
  	  }
  } else {
	  list[MuExp] conditions = [];
	  int i = 0;
	  // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
  	  ifname = nextLabel();
      enterBacktrackingScope(ifname);
      // TODO: account for a variable number of arguments
	  for(Pattern pat <- formals) {
	      conditions += muMulti(muCreate(mkCallToLibFun("Library","MATCH",2), [ *translatePat(pat), muVar("<i>",topFunctionScope(),i) ]));
	      i += 1;
	  };
	  conditions += [ translate(cond) | cond <- when_conditions];

	  mubody = muIfelse(ifname,makeMu("ALL",conditions), [ *kwps, muReturn(translateFunctionBody(body)) ], [ muFailReturn() ]);
	  leaveBacktrackingScope();
	  return mubody;
  }
}

MuExp translateFunctionBody(Expression exp) = translate(exp);
MuExp translateFunctionBody(MuExp exp) = exp;
// TODO: check the interpreter subtyping
default MuExp translateFunctionBody(Statement* stats) = muBlock([ translate(stat) | stat <- stats ]);
default MuExp translateFunctionBody(Statement+ stats) = muBlock([ translate(stat) | stat <- stats ]);
