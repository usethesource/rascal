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
import experiments::Compiler::RVM::Interpreter::ConstantFolder;

/*
 * Compile the match operator and all possible patterns
 */

/*********************************************************************/
/*                  Match                                            */
/*********************************************************************/

MuExp translateMatch((Expression) `<Pattern pat> := <Expression exp>`)  = translateMatch(pat, exp);
   
MuExp translateMatch(e: (Expression) `<Pattern pat> !:= <Expression exp>`) =
    muCallMuPrim("not_mbool", [ makeMu("ALL", [ translateMatch(pat, exp) ], e@\loc) ]);
    
default MuExp translateMatch(Pattern pat, Expression exp) =
    muMulti(muApply(translatePat(pat), [ translate(exp) ]));

/*********************************************************************/
/*                  Patterns                                         */
/*********************************************************************/

// -- literal pattern ------------------------------------------------

default MuExp translatePat(p:(Pattern) `<Literal lit>`) = translateLitPat(lit);

MuExp translateLitPat(Literal lit) = muApply(mkCallToLibFun("Library","MATCH_LITERAL"), [translate(lit)]);

// -- regexp pattern -------------------------------------------------

MuExp translatePat(p:(Pattern) `<RegExpLiteral r>`) = translateRegExpLiteral(r);

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

MuExp translateRegExpLiteral(re: (RegExpLiteral) `/<RegExp* rexps>/<RegExpModifier modifier>`) {
   <buildRegExp,varrefs> = processRegExpLiteral(re);
   return muApply(mkCallToLibFun("Library", "MATCH_REGEXP"), 
                 [ buildRegExp,
                   muCallMuPrim("make_array", varrefs)
                 ]); 
}

MuExp translateRegExpLiteral(re: (RegExpLiteral) `/<RegExp* rexps>/<RegExpModifier modifier>`, MuExp begin, MuExp end) {
   <buildRegExp,varrefs> = processRegExpLiteral(re);
   return muApply(mkCallToLibFun("Library", "MATCH_REGEXP_IN_VISIT"), 
                 [ buildRegExp,
                   muCallMuPrim("make_array", varrefs),
                   begin,
                   end
                 ]); 
}

tuple[MuExp, list[MuExp]] processRegExpLiteral((RegExpLiteral) `/<RegExp* rexps>/<RegExpModifier modifier>`){
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
        	  fragmentCode += [ muCallPrim3("str_escape_for_regexp", [ translate(name) ], r@\loc)];
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
   return <buildRegExp, varrefs>;
   
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

// -- concrete syntax pattern ----------------------------------------

MuExp translatePat(p:(Pattern) `<Concrete concrete>`) = translateConcretePattern(p);

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

/*
 * The global implementation strategy for concrete patterns is as follows:
 * - parseFragment is applied to parse and translate all embedded concrete patterns in well-typed parse trees with holes
 * - next these parsed concrete patterns are compiled to RVM pattern matching code.
 * - a major part of concrete matching concerns list matching. We reuse MATCH_LIST but have added a number of library functions to handle
 *   aspects of concrete patterns:
 *   - MATCH_APPL_IN_LIST: match a list element of the form appl(prod(...), ...)
 *   - MATCH_LIT_IN_LIST: match a lsit element of the form appl(prod(lit(S),...)
 *   - MATCH_OPTIONAL_LAYOUTR_IN_LIST: skips potential layout between list elements
 *   - MATCH_CONCRETE_MULTIVAR_IN_LIST
 *   - MATCH_LAST_CONCRETE_MULTIVAR_IN_LIST
 *   - SKIP_OPTIONAL_SEPARATOR: match a separator before or after a multivariable
 *   - MATCH_CONCRETE_MULTIVAR_WITH_SEPARATORS_IN_LIST
 *   - MATCH_LAST_CONCRETE_MULTIVAR_WITH_SEPARATORS_IN_LIST
*/

MuExp translateConcretePattern(p:(Pattern) `<Concrete concrete>`) { 
  println("translateConcretePattern, concrete = <concrete>");
  fragType = getType(p@\loc);
  //println("translateConcretePattern, fragType = <fragType>");
  reifiedFragType = symbolToValue(fragType);
  //println("translateConcretePattern, reified: <reifiedFragType>");
  //g = getGrammar();
  //println("GRAMMAR:");
  //for(nt <- g) println("<nt> : <g[nt]>");
  parsedFragment = parseFragment(getModuleName(), reifiedFragType, concrete, p@\loc, getGrammar());
  println("++++ parsedFragment: <parsedFragment>");
  //iprintln(parsedFragment);
  return translateParsedConcretePattern(parsedFragment);
}

MuExp translateParsedConcretePattern(t:appl(Production prod, list[Tree] args)){
 println("translateParsedConcretePattern: <prod>");
  if(prod.def == label("hole", lex("ConcretePart"))){
     varloc = args[0].args[4].args[0]@\loc;
     <fuid, pos> = getVariableScope("ConcreteVar", varloc);
     return muApply(mkCallToLibFun("Library","MATCH_VAR"), [muVarRef("ConcreteVar", fuid, pos)]);
  }
  
  applCode = muApply(mkCallToLibFun("Library","MATCH_LITERAL"), [muCon("appl")]);
  prodCode = muApply(mkCallToLibFun("Library","MATCH_LITERAL"), [muCon(prod)]);
  argsCode = translateConcreteListPattern(prod, args);
  kwParams = muApply(mkCallToLibFun("Library","MATCH_KEYWORD_PARAMS"),  [muCallMuPrim("make_array", []), muCallMuPrim("make_array", [])]);
  return muApply(mkCallToLibFun("Library","MATCH_CALL_OR_TREE"), [muCallMuPrim("make_array", [applCode, prodCode, argsCode, kwParams] )]);
}

MuExp translateParsedConcretePattern(cc: char(int c)) {
  return muApply(mkCallToLibFun("Library","MATCH_LITERAL"), [muCon(cc)]);
}

MuExp translateParsedConcretePattern(Pattern pat:(Pattern)`type ( <Pattern s>, <Pattern d> )`) {
    throw "translateParsedConcretePattern type() case"; 
}

// The patterns callOrTree and reifiedType are ambiguous, therefore we need special treatment here.

MuExp translateParsedConcretePattern(amb(set[Tree] alts)) {
   throw "translateParsedConcretePattern: ambiguous, <alts>";
}

default MuExp translateParsedConcretePattern(Tree c) {
   iprintln(c);
   throw "translateParsedConcretePattern: Cannot handle <c> at <c@\loc>";
}

bool isLayoutPat(Tree pat) = appl(prod(layouts(_), _, _), _) := pat;

bool isSeparator(Tree pat, Symbol sep) = appl(prod(sep, _, _), _) := pat;

tuple[bool, Symbol] isIterHoleWithSeparator(Tree pat){
  if(appl(Production prod, list[Tree] args) := pat && prod.def == label("hole", lex("ConcretePart"))){
     varloc = args[0].args[4].args[0]@\loc;
     <fuid, pos> = getVariableScope("ConcreteVar", varloc);
     holeType = getType(varloc);
     if(isIterWithSeparator(holeType)){
        return <true, getSeparator(holeType)>;
     } 
  }
  return <false, \lit("NONE")>;
}

// Remove separators before and after multivariables in concrete patterns

list[Tree] removeSeparators(list[Tree] pats){
  n = size(pats);
  println("removeSeparators(<n>): <for(p <- pats){><p><}>");
  if(n <= 0){
  		return pats;
  }
  for(i <- index(pats)){
      pat = pats[i];
      <hasSeps, sep> = isIterHoleWithSeparator(pat);
      if(hasSeps){
         println("removeSeparators: <i>, <n>, <pat>");
         ilast = i;
         if(i > 2 && isSeparator(pats[i-2], sep)){ 
            ilast = i - 2; 
         }
         ifirst = i + 1;
         if(i + 2 < n && isSeparator(pats[i+2], sep)){
              ifirst = i + 3;
         }
         
         res = pats[ .. ilast] + pat + (ifirst < n ? removeSeparators(pats[ifirst ..]) : []);  
         println("removeSeparators: ifirst = <ifirst>, return: <for(p <- res){><p><}>");  
         return res;  
      }
  }
  println("removeSeparators returns: <for(p <- pats){><p><}>");
  return pats;
}

MuExp translateConcreteListPattern(Production listProd, list[Tree] pats){
 println("Before: <for(p <- pats){><p><}>");
 pats = removeSeparators(pats);
 println("After: <for(p <- pats){><p><}>");
 lookahead = computeConcreteLookahead(pats);  
 optionalLayoutPat = muApply(mkCallToLibFun("Library","MATCH_OPTIONAL_LAYOUT_IN_LIST"), []);
 return muApply(mkCallToLibFun("Library","MATCH_LIST"), [muCallMuPrim("make_array", 
         [ (i % 2 == 0) ? translatePatAsConcreteListElem(listProd, pats[i], lookahead[i]) : optionalLayoutPat | i <- index(pats) ])]);
}

// Is a symbol an iterator type?

bool isIter(\iter(Symbol symbol)) = true;
bool isIter(\iter-star(Symbol symbol)) = true;
bool isIter(\iter-seps(Symbol symbol, list[Symbol] separators)) = true;
bool isIter(\iter-star-seps(Symbol symbol, list[Symbol] separators)) = true;
default bool isIter(Symbol s) = false;

// Is a symbol an iterator type with separators?
bool isIterWithSeparator(\iter-seps(Symbol symbol, list[Symbol] separators)) = true;
bool isIterWithSeparator(\iter-star-seps(Symbol symbol, list[Symbol] separators)) = true;
default bool isIterWithSeparator(Symbol s) = false;

// What is is the minimal iteration count of a symbol?
int nIter(\iter(Symbol symbol)) = 1;
int nIter(\iter-star(Symbol symbol)) = 0;
int nIter(\iter-seps(Symbol symbol, list[Symbol] separators)) = 1;
int nIter(\iter-star-seps(Symbol symbol, list[Symbol] separators)) = 0;
default int nIter(Symbol s) { throw "Cannot determine iteration count: <s>"; }

// Get the separator of an iterator type
Symbol getSeparator(\iter-seps(Symbol symbol, list[Symbol] separators)) = separators[0];
Symbol getSeparator(\iter-star-seps(Symbol symbol, list[Symbol] separators)) = separators[0];
default Symbol getSeparator(Symbol sym) { throw "Cannot determine separator: <sym>"; }

// What is is the minimal iteration count of a pattern (as Tree)?
int nIter(Tree pat){
  if(appl(Production prod, list[Tree] args) := pat && prod.def == label("hole", lex("ConcretePart"))){
     varloc = args[0].args[4].args[0]@\loc;
     <fuid, pos> = getVariableScope("ConcreteVar", varloc);
     holeType = getType(varloc);
     if(isIterWithSeparator(holeType)){
        return nIter(holeType);
     } 
  }
  return 1;
}

MuExp translatePatAsConcreteListElem(Production listProd, t:appl(Production applProd, list[Tree] args), Lookahead lookahead){
  println("translatePatAsConcreteListElem: <listProd>, <applProd>");
    if(applProd.def == label("hole", lex("ConcretePart"))){
     varloc = args[0].args[4].args[0]@\loc;
     <fuid, pos> = getVariableScope("ConcreteVar", varloc);
     holeType = getType(varloc);
     println("holeType = <holeType>");
     println("appl = <getConstructor("appl")>");
     if(isIter(holeType)){
        if(isIterWithSeparator(holeType)){
           sep = getSeparator(holeType);
           libFun = "MATCH_<isLast(lookahead)>CONCRETE_MULTIVAR_WITH_SEPARATORS_IN_LIST";
           println("libFun = <libFun>");
           println("lookahead = <lookahead>");
           println("listProd= <listProd>");
           return muApply(mkCallToLibFun("Library", libFun), [muVarRef("ConcreteListVar", fuid, pos), muCon(nIter(holeType)), muCon(1000000), muCon(lookahead.nElem), 
                												  muCon(sep), getConstructor("appl"), muCon(listProd), muCon(regular(holeType /*listProd.symbols[0]*/))]);
        } else {
           libFun = "MATCH_<isLast(lookahead)>CONCRETE_MULTIVAR_IN_LIST";
           println("libFun = <libFun>");
           println("lookahead = <lookahead>");
           println("listProd= <listProd>");
           return muApply(mkCallToLibFun("Library", libFun), [muVarRef("ConcreteListVar", fuid, pos), muCon(nIter(holeType)), muCon(1000000), muCon(lookahead.nElem), 
           														 getConstructor("appl"), muCon(listProd), muCon(regular(holeType /*listProd.symbols[0]*/))]);
       }
     }
     return muApply(mkCallToLibFun("Library","MATCH_VAR_IN_LIST"), [muVarRef("ConcreteVar", fuid, pos)]);
  }
  return translateApplAsListElem(listProd, applProd, args);
}

MuExp translatePatAsConcreteListElem(Production listProd, cc: char(int c), Lookahead lookahead){
  return muApply(mkCallToLibFun("Library","MATCH_LITERAL_IN_LIST"), [muCon(cc)]);
}

default MuExp translatePatAsConcreteListElem(Production listProd, Tree c, Lookahead lookahead){
  return muApply(mkCallToLibFun("Library","MATCH_PAT_IN_LIST"), [translateParsedConcretePattern(c)]);
}

// Translate an appl as element of a concrete list pattern

MuExp translateApplAsListElem(Production listProd, p: prod(lit(str S), _, _), list[Tree] args) = 
 	muApply(mkCallToLibFun("Library","MATCH_LIT_IN_LIST"), [muCon(p)]);
 
default MuExp translateApplAsListElem(Production listProd, Production prod, list[Tree] args) = muApply(mkCallToLibFun("Library","MATCH_APPL_IN_LIST"), [muCon(prod), translateConcreteListPattern(listProd, args)]);

// Is an appl node a concrete multivar?

bool isConcreteMultiVar(t:appl(Production prod, list[Tree] args)){
  if(prod.def == label("hole", lex("ConcretePart"))){
     varloc = args[0].args[4].args[0]@\loc;
     holeType = getType(varloc);
     return isIter(holeType);
  }
  return false;
}

default bool isConcreteMultiVar(Tree t) = false;

// Compute a list of lookaheads for a  list of patterns.
// Recall that a Lookahead is a tuple of the form <number-of-elements-following, number-of-multi-vars-following>

list[Lookahead] computeConcreteLookahead(list[Tree] pats){
    println("computeConcreteLookahead: <for(p <- pats){><p><}>");
    nElem = 0;
    nMultiVar = 0;
    rprops = for(p <- reverse([p | p <- pats])){
                 append <nElem, nMultiVar>;
                 if(isConcreteMultiVar(p)) {nMultiVar += 1; nElem += nIter(p); } else {nElem += 1;}
             };
    println("result = <reverse(rprops)>");
    return reverse(rprops);
}
     
// -- qualified name pattern -----------------------------------------

MuExp translatePat(p:(Pattern) `<QualifiedName name>`) = translateQualifiedNamePat(name);

MuExp translateQualifiedNamePat(QualifiedName name)
{
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR"), []);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   //println("transPattern: <fuid>, <pos>");
   return muApply(mkCallToLibFun("Library","MATCH_VAR"), [muVarRef("<name>", fuid, pos)]);
} 

// -- types name pattern ---------------------------------------------
     
MuExp translatePat(p:(Pattern) `<Type tp> <Name name>`){
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_TYPED_ANONYMOUS_VAR"), [muTypeCon(translateType(tp))]);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_TYPED_VAR"), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos)]);
}  

// -- reified type pattern -------------------------------------------

MuExp translatePat(p:(Pattern) `type ( <Pattern symbol> , <Pattern definitions> )`) {    
    return muApply(mkCallToLibFun("Library","MATCH_REIFIED_TYPE"), [muCon(symbol)]);
}

// -- call or tree pattern -------------------------------------------

MuExp translatePat(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`) {
   MuExp fun_pat;
   str fun_name;
   
   argCode = [ translatePat(pat) | pat <- arguments ] + translatePatKWArguments(keywordArguments);
   //iprintln(expression);
   if(expression is qualifiedName){
      fun_name = "<getType(expression@\loc).name>";
      //fun_pat = muApply(mkCallToLibFun("Library","MATCH_LITERAL"), [muCon(fun_name)]);
      return muApply(mkCallToLibFun("Library","MATCH_SIMPLE_CALL_OR_TREE"), [muCon(fun_name), muCallMuPrim("make_array", argCode)]);
   } else if(expression is literal){ // StringConstant
      fun_name = "<expression>"[1..-1];
      return muApply(mkCallToLibFun("Library","MATCH_SIMPLE_CALL_OR_TREE"), [muCon(fun_name), muCallMuPrim("make_array", argCode)]);
   } else {
     fun_pat = translatePat(expression);
     return muApply(mkCallToLibFun("Library","MATCH_CALL_OR_TREE"), [muCallMuPrim("make_array", fun_pat + argCode)]);
   }
}

MuExp translatePatKWArguments((KeywordArguments[Pattern]) ``) =
   muApply(mkCallToLibFun("Library","MATCH_KEYWORD_PARAMS"), [muCallMuPrim("make_array", []), muCallMuPrim("make_array", [])]);

MuExp translatePatKWArguments((KeywordArguments[Pattern]) `<OptionalComma optionalComma> <{KeywordArgument[Pattern] ","}+ keywordArgumentList>`) {
   println("translatePatKWArguments: <keywordArgumentList>");
   keyword_names = [];
   pats = [];
   for(kwarg <- keywordArgumentList){
       println("kwarg = <kwarg>");
       keyword_names += muCon("<kwarg.name>");
       pats += translatePat(kwarg.expression);
   }
   return muApply(mkCallToLibFun("Library","MATCH_KEYWORD_PARAMS"), [muCallMuPrim("make_array", keyword_names), muCallMuPrim("make_array", pats)]);
}

// TODO: extend the following with all pattern cases, however this requires translating 
// from expression to pattern!

//MuExp translatePatKWValue(e: (Expression) `<Literal lit>`) = translateLitPat(lit);
//
//MuExp translatePatKWValue(e: (Expression) `<QualifiedName name>`) = translateQualifiedNamePat(name);
//
//default MuExp translatePatKWValue(e: (Expression) `<Expression exp>`) {
//  if(isConstant(exp)){
//     return muApply(mkCallToLibFun("Library","MATCH_LITERAL"), [muCon(getConstantValue(exp))]);
//  } else {
//    throw "Non-constant expressions in keyword parameters not yet supported";
//  }
//}


// -- set pattern ----------------------------------------------------

MuExp translatePat(p:(Pattern) `{<{Pattern ","}* pats>}`) = translateSetPat(p);

// Translate patterns as element of a set pattern

str isLast(bool b) = b ? "LAST_" : "";

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>`, bool last) {
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR_IN_SET"), []);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_VAR_IN_SET"), [muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `<Type tp> <Name name>`, bool last) {
   if("<name>" == "_"){
       return muApply(mkCallToLibFun("Library","MATCH_TYPED_ANONYMOUS_VAR_IN_SET"), [muTypeCon(translateType(tp))]);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_TYPED_VAR_IN_SET"), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos)]);
}  

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>*`, bool last) {
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>ANONYMOUS_MULTIVAR_IN_SET"), []);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>MULTIVAR_IN_SET"), [muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Type tp> <Name name>`, bool last) {
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>TYPED_ANONYMOUS_MULTIVAR_IN_SET"), [muTypeCon(\set(translateType(tp)))]);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>TYPED_MULTIVAR_IN_SET"), [muTypeCon(\set(translateType(tp))), muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Name name>`, bool last) {
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>ANONYMOUS_MULTIVAR_IN_SET"), []);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>MULTIVAR_IN_SET"), [muVarRef("<name>", fuid, pos)]);
} 

MuExp translatePatAsSetElem(p:(Pattern) `+<Pattern argument>`, bool last) {
  throw "splicePlus pattern";
}   

default MuExp translatePatAsSetElem(Pattern p, bool last) {
  println("translatePatAsSetElem, default: <p>");
  try {
     return  muApply(mkCallToLibFun("Library","MATCH_LITERAL_IN_SET"), [muCon(translatePatternAsConstant(p))]);
  } catch:
    return muApply(mkCallToLibFun("Library","MATCH_PAT_IN_SET"), [translatePat(p)]);
}

value getLiteralValue((Literal) `<Literal s>`) =  readTextValueString("<s>"); // TODO interpolation

bool isConstant(StringLiteral l) = l is nonInterpolated;
bool isConstant(LocationLiteral l) = l.protocolPart is nonInterpolated && l.pathPart is nonInterpolated;
default bool isConstant(Literal l) = true;


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
      str name = getName(pat, i);
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
        compiledPats +=  muApply(mkCallToLibFun("Library","MATCH_PAT_IN_SET"), [translatePat(pat)]);
        // To enable constant elimination change to:
        // compiledPats += translatePatAsSetElem(pat, false);
      }
   }
   MuExp litCode = (all(Literal lit <- literals, isConstant(lit))) ? muCon({ getLiteralValue(lit) | Literal lit <- literals })
   		           										           : muCallPrim("set_create", [ translate(lit) | Literal lit <- literals] );
   
    return muApply(mkCallToLibFun("Library","MATCH_SET"), [ litCode, muCallMuPrim("make_array", compiledPats) ]);
   
   
   //return muApply(mkCallToLibFun("Library","MATCH_SET"), [ muCallMuPrim("make_array", [ litCode, 
   //                                                                                     muCallMuPrim("make_array", compiledPats) ]) ] );
}

// -- tuple pattern --------------------------------------------------

MuExp translatePat(p:(Pattern) `\<<{Pattern ","}* pats>\>`) {
    return muApply(mkCallToLibFun("Library","MATCH_TUPLE"), [muCallMuPrim("make_array", [ translatePat(pat) | pat <- pats ])]);
}

// -- list pattern ---------------------------------------------------

MuExp translatePat(p:(Pattern) `[<{Pattern ","}* pats>]`) {
    lookahead = computeLookahead(p);  
    lpats = [pat | pat <- pats];   //TODO: should be unnnecessary
    return muApply(mkCallToLibFun("Library","MATCH_LIST"), [muCallMuPrim("make_array", [ translatePatAsListElem(lpats[i], lookahead[i]) | i <- index(lpats) ])]);
}

bool isMultiVar(p:(Pattern) `<QualifiedName name>*`) = true;
bool isMultiVar(p:(Pattern) `*<Type tp> <Name name>`) = true;
bool isMultiVar(p:(Pattern) `*<Name name>`) = true;
default bool isMultiVar(Pattern p) = false;

int nIter(p:(Pattern) `<QualifiedName name>*`) = 0;
int nIter(p:(Pattern) `*<Type tp> <Name name>`) = 0;
int nIter(p:(Pattern) `*<Name name>`) = 0;
default int nIter(Pattern p) { throw "Cannot determine iteration count: <p>"; }

// Lookahead information for a specific position in a list pattern
// nElem = the number of pattern elements following this position that are not multivars
// nMultiVar = the number of multivars following this position

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
       return muApply(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR_IN_LIST"), []);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_VAR_IN_LIST"), [muVarRef("<name>", fuid, pos)]);
} 

MuExp translatePatAsListElem(p:(Pattern) `<Type tp> <Name name>`, Lookahead lookahead) {
   if("<name>" == "_"){
       return muApply(mkCallToLibFun("Library","MATCH_TYPED_ANONYMOUS_VAR_IN_LIST"), [muTypeCon(translateType(tp))]);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_TYPED_VAR_IN_LIST"), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos)]);
} 


MuExp translatePatAsListElem(p:(Pattern) `<Literal lit>`, Lookahead lookahead) =
   muApply(mkCallToLibFun("Library","MATCH_LITERAL_IN_LIST"), [translate(lit)])
when !(lit is regExp);

MuExp translatePatAsListElem(p:(Pattern) `<QualifiedName name>*`, Lookahead lookahead) {
   if("<name>" == "_"){
       return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>ANONYMOUS_MULTIVAR_IN_LIST"), [muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>MULTIVAR_IN_LIST"), [muVarRef("<name>", fuid, pos), muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
}

MuExp translatePatAsListElem(p:(Pattern) `*<Type tp> <Name name>`, Lookahead lookahead) {
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>TYPED_ANONYMOUS_MULTIVAR_IN_LIST"), [muTypeCon(\list(translateType(tp))), muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>TYPED_MULTIVAR_IN_LIST"), [muTypeCon(\list(translateType(tp))), muVarRef("<name>", fuid, pos), muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
}

MuExp translatePatAsListElem(p:(Pattern) `*<Name name>`, Lookahead lookahead) {
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>ANONYMOUS_MULTIVAR_IN_LIST"), [muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>MULTIVAR_IN_LIST"), [muVarRef("<name>", fuid, pos), muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
} 

MuExp translatePatAsListElem(p:(Pattern) `+<Pattern argument>`, Lookahead lookahead) {
  throw "splicePlus pattern";
}   

default MuExp translatePatAsListElem(Pattern p, Lookahead lookahead) {
  println("translatePatAsListElem, default: <p>");
  //try { // Gives error for nodes (~ kw params)
  //   return  muApply(mkCallToLibFun("Library","MATCH_LITERAL_IN_LIST"), [muCon(translatePatternAsConstant(p))]);
  //} catch:
    return muApply(mkCallToLibFun("Library","MATCH_PAT_IN_LIST"), [translatePat(p)]);
}

// -- variable becomes pattern ---------------------------------------

MuExp translatePat(p:(Pattern) `<Name name> : <Pattern pattern>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muApply(mkCallToLibFun("Library","MATCH_VAR_BECOMES"), [muVarRef("<name>", fuid, pos), translatePat(pattern)]);
}

// -- as type pattern ------------------------------------------------

MuExp translatePat(p:(Pattern) `[ <Type tp> ] <Pattern argument>`) =
    muApply(mkCallToLibFun("Library","MATCH_AS_TYPE"), [muTypeCon(translateType(tp)), translatePat(argument)]);

// -- descendant pattern ---------------------------------------------

MuExp translatePat(p:(Pattern) `/ <Pattern pattern>`) =
    muApply(mkCallToLibFun("Library","MATCH_AND_DESCENT"), [translatePat(pattern)]);

// -- anti pattern ---------------------------------------------------

MuExp translatePat(p:(Pattern) `! <Pattern pattern>`) =
    muApply(mkCallToLibFun("Library","MATCH_ANTI"), [translatePat(pattern)]);

// -- typed variable becomes pattern ---------------------------------

MuExp translatePat(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muApply(mkCallToLibFun("Library","MATCH_TYPED_VAR_BECOMES"), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos), translatePat(pattern)]);
}

// -- default rule for pattern ---------------------------------------

default MuExp translatePat(Pattern p) { throw "Pattern <p> cannot be translated"; }

/**********************************************************************/
/*                 Constant Patterns                                  */
/**********************************************************************/

value translatePatternAsConstant(p:(Pattern) `<Literal lit>`) = getLiteralValue(lit) when !(lit is regExp);

value translatePatternAsConstant(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`) =
  makeNode("<expression>", [ translatePatternAsConstant(pat) | pat <- arguments ] + translatePatKWArguments(keywordArguments));

value translatePatternAsConstant(p:(Pattern) `{<{Pattern ","}* pats>}`) = { translatePatternAsConstant(pat) | pat <- pats };

value translatePatternAsConstant(p:(Pattern) `[<{Pattern ","}* pats>]`) = [ translatePatternAsConstant(pat) | pat <- pats ];

//value translatePatternAsConstant(p:(Pattern) `\<<{Pattern ","}* pats>\>`) {
//  lpats = [ pat | pat <- pats]; // TODO
//  return ( <translatePatternAsConstant(lpats[0])> | it + <translatePatternAsConstant(lpats[i])> | i <- [1 .. size(lpats)] );
//}

value translatePatternAsConstant(p:(Pattern) `\<<Pattern  pat1>\>`) {
  return < translatePatternAsConstant(pat1) >;
}

value translatePatternAsConstant(p:(Pattern) `\<<Pattern  pat1>, <Pattern  pat2>\>`) {
  return < translatePatternAsConstant(pat1), 
           translatePatternAsConstant(pat2)
         >;
}

value translatePatternAsConstant(p:(Pattern) `\<<Pattern  pat1>, <Pattern  pat2>, <Pattern  pat3>\>`) {
  return < translatePatternAsConstant(pat1), 
           translatePatternAsConstant(pat2), 
           translatePatternAsConstant(pat3)
         >;
}

value translatePatternAsConstant(p:(Pattern) `\<<Pattern  pat1>, <Pattern  pat2>, <Pattern  pat3>, <Pattern  pat4>\>`) {
  return < translatePatternAsConstant(pat1), 
           translatePatternAsConstant(pat2), 
           translatePatternAsConstant(pat3),
           translatePatternAsConstant(pat4)
         >;
}
value translatePatternAsConstant(p:(Pattern) `\<<Pattern  pat1>, <Pattern  pat2>, <Pattern  pat3>, <Pattern  pat4>, <Pattern  pat5>\>`) {
  return < translatePatternAsConstant(pat1), 
           translatePatternAsConstant(pat2), 
           translatePatternAsConstant(pat3),
           translatePatternAsConstant(pat4),
           translatePatternAsConstant(pat5)
         >;
}

 
default value translatePatternAsConstant(Pattern p){
  throw "Not a constant pattern: <p>";
}

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

MuExp translateFormals(list[Pattern] formals, bool isVarArgs, int i, list[MuExp] kwps, node body, list[Expression] when_conditions, loc src){
   if(isEmpty(formals)) {
      if(isEmpty(when_conditions)){
  	      return muBlock([ *kwps, muReturn1(translateFunctionBody(body)) ]);
  	  } else {
  	      ifname = nextLabel();
          enterBacktrackingScope(ifname);
          conditions = [ translate(cond) | cond <- when_conditions];
          mubody = muIfelse(ifname,makeMu("ALL",conditions, src), [ *kwps, muReturn1(translateFunctionBody(body)) ], [ muFailReturn() ]);
	      leaveBacktrackingScope();
	      return mubody;
  	  }
   }
   pat = formals[0];
   if(pat is literal){
   	  // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
  	  ifname = nextLabel();
      enterBacktrackingScope(ifname);
      exp = muIfelse(ifname,muCallMuPrim("equal", [ muVar("<i>",topFunctionScope(),i), translate(pat.literal) ]),
                   [ translateFormals(tail(formals), isVarArgs, i + 1, kwps, body, when_conditions, src) ],
                   [ muFailReturn() ]
                  );
      leaveBacktrackingScope();
      return exp;
   } else {
      Name name = pat.name;
      tp = pat.\type;
      <fuid, pos> = getVariableScope("<name>", name@\loc);
      // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
  	  ifname = nextLabel();
      enterBacktrackingScope(ifname);
      exp = muIfelse(ifname,muCallMuPrim("check_arg_type", [ muVar("<i>",topFunctionScope(),i), muTypeCon( (isVarArgs && size(formals) == 1) ? Symbol::\list(translateType(tp)) : translateType(tp) ) ]),
                   [ muAssign("<name>", fuid, pos, muVar("<i>",topFunctionScope(),i)),
                     translateFormals(tail(formals), isVarArgs, i + 1, kwps, body, when_conditions, src) 
                   ],
                   [ muFailReturn() ]
                  );
      leaveBacktrackingScope();
      return exp;
    }
}

MuExp translateFunction(str fname, {Pattern ","}* formals, bool isVarArgs, list[MuExp] kwps, node body, list[Expression] when_conditions){
  bool simpleArgs = true;
  for(pat <- formals){
      if(!(pat is typedVariable || pat is literal))
      simpleArgs = false;
  }
  if(simpleArgs) { //TODO: should be: all(pat <- formals, (pat is typedVariable || pat is literal))) {	
  	return muIfelse(fname, muCon(true), [ translateFormals([formal | formal <- formals], isVarArgs, 0, kwps, body, when_conditions, formals@\loc)], [ muFailReturn() ]);
  } else {
	  list[MuExp] conditions = [];
	  int i = 0;
	  // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
      enterBacktrackingScope(fname);
      // TODO: account for a variable number of arguments
	  for(Pattern pat <- formals) {
	      conditions += muMulti(muApply(translatePat(pat), [ muVar("<i>",topFunctionScope(),i) ]));
	      i += 1;
	  };
	  conditions += [ translate(cond) | cond <- when_conditions];

	  mubody = muIfelse(fname, makeMu("ALL",conditions, formals@\loc), [ *kwps, muReturn1(translateFunctionBody(body)) ], [ muFailReturn() ]);
	  leaveBacktrackingScope();
	  return mubody;
  }
}

MuExp translateFunctionBody(Expression exp) = translate(exp);
MuExp translateFunctionBody(MuExp exp) = exp;
// TODO: check the interpreter subtyping
default MuExp translateFunctionBody(Statement* stats) = muBlock([ translate(stat) | stat <- stats ]);
default MuExp translateFunctionBody(Statement+ stats) = muBlock([ translate(stat) | stat <- stats ]);

default MuExp translateFunctionBody(node nd) {  throw "Cannot handle function body <nd>"; }

//MuExp translateFunctionBody(node nd){
//    switch(nd){
//        case Expression exp:    return translate(exp);
//        case MuExp exp:         return exp;
//        case Statement* stats:  muBlock([ translate(stat) | stat <- stats ]);
//        case Statement+ stats:  muBlock([ translate(stat) | stat <- stats ]);
//        default:
//            throw "Cannot handle function body <nd>";
//    }
//}

