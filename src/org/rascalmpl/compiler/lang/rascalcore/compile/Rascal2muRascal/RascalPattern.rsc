@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalPattern
   
import IO;
import ValueIO;
import Node;
import Map;
import Set;
import String;
import ParseTree;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::compile::muRascal::AST;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;

import lang::rascalcore::compile::Rascal2muRascal::ModuleInfo;
import lang::rascalcore::compile::Rascal2muRascal::RascalType;
import lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
import lang::rascalcore::compile::Rascal2muRascal::TypeReifier;

//import lang::rascal::types::AbstractType;

import lang::rascalcore::compile::Rascal2muRascal::RascalExpression;

//import lang::rascalcore::compile::RVM::Interpreter::ParsingTools;

/*
 * Compile the match operator and all possible patterns
 */

/*********************************************************************/
/*                  Match                                            */
/*********************************************************************/

MuExp translateMatch((Expression) `<Pattern pat> := <Expression exp>`, MuExp trueCont, MuExp falseCont) 
    = translateMatch(pat, exp, trueCont, falseCont) ;

MuExp translateMatch(e: (Expression) `<Pattern pat> !:= <Expression exp>`, MuExp trueCont, MuExp falseCont) =
    muCallMuPrim("not_mbool", [ makeBoolExp("ALL", [ translateMatch(pat, exp) ], e@\loc) ]);
    
default MuExp translateMatch(Pattern pat, Expression exp, MuExp trueCont, MuExp falseCont) =
    translatePat(pat, getType(exp), translate(exp), trueCont, falseCont);

/*********************************************************************/
/*                  Patterns                                         */
/*********************************************************************/

// -- literal pattern ------------------------------------------------

default MuExp translatePat(p:(Pattern) `<Literal lit>`, AType subjectType, MuExp subject, str btscope, MuExp trueCont, MuExp falseCont) = translateLitPat(lit, subjectType, subject, btscope, trueCont, falseCont);

MuExp translatePat(Literal lit, AType subjectType, MuExp subject, str btscope, MuExp trueCont, MuExp falseCont) = translateLitPat(lit, subjectType, subject, btscope, trueCont, falseCont);

MuExp translateLitPat(Literal lit, AType subjectType, MuExp subject, str btscope, MuExp trueCont, MuExp falseCont) = muIfelse(btscope, muEqual(translate(lit), subject), trueCont, falseCont);

// -- regexp pattern -------------------------------------------------

MuExp translatePat(p:(Pattern) `<RegExpLiteral r>`, AType subjectType) = translateRegExpLiteral(r);

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

tuple[MuExp, list[MuExp]] processRegExpLiteral(e: (RegExpLiteral) `/<RegExp* rexps>/<RegExpModifier modifier>`){
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
      //println("lregex[<i>]: <r>\nfragment = <fragment>\nfragmentCode = <fragmentCode>");
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
   buildRegExp = muBlockWithTmps(
                       [ <swriter, fuid> ],
                       [ ],
                       muAssignTmp(swriter, fuid, muCallPrim3("stringwriter_open", [], e@\loc)) + 
                       [ muCallPrim3("stringwriter_add", [muTmp(swriter,fuid), exp], e@\loc) | exp <- fragmentCode ] +
                       muCallPrim3("stringwriter_close", [muTmp(swriter,fuid)], e@\loc));
   return  <buildRegExp, varrefs>;   
}

tuple[MuExp, list[MuExp]] extractNamedRegExp((RegExp) `\<<Name name>:<NamedRegExp* namedregexps>\>`) {
   exps = [];
   str fragment = "(";
   atStart = true;
   for(nr <- namedregexps){
       elm = "<nr>";
       if(atStart && size(trim(elm)) == 0){
       	 continue;
       }
       atStart = false;
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

MuExp translatePat(p:(Pattern) `<Concrete concrete>`, AType subjectType) = translateConcretePattern(p);

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
 *   - MATCH_OPTIONAL_LAYOUT_IN_LIST: skips potential layout between list elements
 *   - MATCH_CONCRETE_MULTIVAR_IN_LIST
 *   - MATCH_LAST_CONCRETE_MULTIVAR_IN_LIST
 *   - SKIP_OPTIONAL_SEPARATOR: match a separator before or after a multivariable
 *   - MATCH_CONCRETE_MULTIVAR_WITH_SEPARATORS_IN_LIST
 *   - MATCH_LAST_CONCRETE_MULTIVAR_WITH_SEPARATORS_IN_LIST
*/

MuExp translateConcretePattern(p:(Pattern) `<Concrete concrete>`) { 
  //println("translateConcretePattern:,<getType(p)>");
  return translateParsedConcretePattern(parseConcrete(concrete), getType(p));
}

bool isConcreteHole(appl(Production prod, list[Tree] args)) = prod.def == Symbol::label("hole", lex("ConcretePart"));

loc getConcreteHoleVarLoc(h: appl(Production prod, list[Tree] args)) {
	//println("getConcreteHoleVarLoc: <h>");
	if(args[0].args[4].args[0]@\loc?){
		return args[0].args[4].args[0]@\loc;
	}
	if(args[0].args[4]@\loc?){
		println("getConcreteHoleVarLoc: moved up one level to get loc: <h>");
		return args[0].args[4]@\loc;
	}
	println("getConcreteHoleVarLoc: Missing loc:");
	iprintln(h);
	println("hole: <args[0].args[4].args[0]>");
	iprintln(args[0].args[4].args[0]);
	println("<h@\loc?>, <(args[0])@\loc?>,  <(args[0].args[4])@\loc?>, <(args[0].args[4].args[0])@\loc?>");
	throw "getConcreteHoleVarLoc: Missing loc";
}	

MuExp translateParsedConcretePattern(t:appl(Production prod, list[Tree] args), AType symbol){
  //println("translateParsedConcretePattern: <prod>, <symbol>");
  if(isConcreteHole(t)){
     <fuid, pos> = getVariableScope("ConcreteVar",  getConcreteHoleVarLoc(t));
     return muApply(mkCallToLibFun("Library","MATCH_TYPED_VAR"), [muTypeCon(symbol), muVarRef("ConcreteVar", fuid, pos)]);
  }
  //applCode = muApply(mkCallToLibFun("Library","MATCH_LITERAL"), [muCon("appl")]);
  prodCode = muApply(mkCallToLibFun("Library","MATCH_LITERAL"), [muCon(prod)]);
  argsCode = translateConcreteListPattern(args, lex(_) := symbol);
  //kwParams = muApply(mkCallToLibFun("Library","MATCH_KEYWORD_PARAMS"),  [muCallMuPrim("make_array", []), muCallMuPrim("make_array", [])]);
  return muApply(mkCallToLibFun("Library", "MATCH_CONCRETE_TREE"), [muCon(prod), argsCode]);
}

MuExp translateParsedConcretePattern(cc: char(int c), AType symbol) {
  return muApply(mkCallToLibFun("Library","MATCH_LITERAL"), [muCon(cc)]);
}

MuExp translateParsedConcretePattern(Pattern pat:(Pattern)`type ( <Pattern s>, <Pattern d> )`, AType symbol) {
    throw "translateParsedConcretePattern type() case"; 
}

// The patterns callOrTree and reifiedType are ambiguous, therefore we need special treatment here.

MuExp translateParsedConcretePattern(amb(set[Tree] alts), AType symbol) {
   throw "translateParsedConcretePattern: ambiguous, <alts>";
}

default MuExp translateParsedConcretePattern(Tree c, AType symbol) {
   //iprintln(c);
   throw "translateParsedConcretePattern: Cannot handle <c> at <c@\loc>";
}

bool isLayoutPat(Tree pat) = appl(prod(layouts(_), _, _), _) := pat;

bool isSeparator(Tree pat, AType sep) = appl(prod(sep, _, _), _) := pat;

tuple[bool, AType] isIterHoleWithSeparator(Tree pat){
  if(t:appl(Production prod, list[Tree] args) := pat && isConcreteHole(t)){
     varloc = getConcreteHoleVarLoc(t);
     <fuid, pos> = getVariableScope("ConcreteVar", varloc);
     holeType = getType(varloc);
     if(isIterWithSeparator(holeType)){
        return <true, getSeparator(holeType)>;
     } 
  }
  return <false, \lit("NONE")>;
}

// TODO: Jurgen; better introduce _ variables instead (would simplify downstream)
// Also this precludes pattern matching _inside_ the separators which is needed
// if you want to analyze them or the comments around the separators.

// Remove separators before and after multivariables in concrete patterns

list[Tree] removeSeparators(list[Tree] pats){
  n = size(pats);
  //println("removeSeparators(<n>): <for(p <- pats){><p><}>");
  if(n <= 0){
  		return pats;
  }
  for(i <- index(pats)){
      pat = pats[i];
      <hasSeps, sep> = isIterHoleWithSeparator(pat);
      if(hasSeps){
         //println("removeSeparators: <i>, <n>, <pat>");
         ilast = i;
         if(i > 2 && isSeparator(pats[i-2], sep)){ 
            ilast = i - 2; 
         }
         ifirst = i + 1;
         if(i + 2 < n && isSeparator(pats[i+2], sep)){
              ifirst = i + 3;
         }
         
         res = pats[ .. ilast] + pat + (ifirst < n ? removeSeparators(pats[ifirst ..]) : []);  
         //println("removeSeparators: ifirst = <ifirst>, return: <for(p <- res){><p><}>");  
         return res;  
      }
  }
  //println("removeSeparators returns: <for(p <- pats){><p><}>");
  return pats;
}

MuExp translateConcreteListPattern(list[Tree] pats, bool isLex){
 //println("translateConcreteListPattern: <for(p <- pats){><p><}>, isLex = <isLex>");
 pats = removeSeparators(pats);
 //println("After: <for(p <- pats){><p><}>");
 lookahead = computeConcreteLookahead(pats);  
 if(isLex){
 	return muApply(mkCallToLibFun("Library","MATCH_LIST"), [muCallMuPrim("make_array", 
                   [ translatePatAsConcreteListElem(pats[i], lookahead[i], isLex) | i <- index(pats) ])]);
 }
 optionalLayoutPat = muApply(mkCallToLibFun("Library","MATCH_OPTIONAL_LAYOUT_IN_LIST"), []);
 return muApply(mkCallToLibFun("Library","MATCH_LIST"), [muCallMuPrim("make_array", 
         [ (i % 2 == 0) ? translatePatAsConcreteListElem(pats[i], lookahead[i], isLex) : optionalLayoutPat | int i <- index(pats) ])]);
}

// Is a symbol an iterator type?

bool isIter(\iter(AType symbol)) = true;
bool isIter(\iter-star(AType symbol)) = true;
bool isIter(\iter-seps(AType symbol, list[AType] separators)) = true;
bool isIter(\iter-star-seps(AType symbol, list[AType] separators)) = true;
default bool isIter(AType s) = false;

// Is a symbol an iterator type with separators?
bool isIterWithSeparator(\iter-seps(AType symbol, list[AType] separators)) = true;
bool isIterWithSeparator(\iter-star-seps(AType symbol, list[AType] separators)) = true;
default bool isIterWithSeparator(AType s) = false;

// What is is the minimal iteration count of a symbol?
int nIter(\iter(AType symbol)) = 1;
int nIter(\iter-star(AType symbol)) = 0;
int nIter(\iter-seps(AType symbol, list[AType] separators)) = 1;
int nIter(\iter-star-seps(AType symbol, list[AType] separators)) = 0;
default int nIter(AType s) { throw "Cannot determine iteration count: <s>"; }

// Get the separator of an iterator type
// TODO: this does not work if the layout is there already...
AType getSeparator(\iter-seps(AType symbol, list[AType] separators)) = separators[0];
AType getSeparator(\iter-star-seps(AType symbol, list[AType] separators)) = separators[0];
default AType getSeparator(AType sym) { throw "Cannot determine separator: <sym>"; }

// What is is the minimal iteration count of a pattern (as Tree)?
int nIter(Tree pat){
  if(t:appl(Production prod, list[Tree] args) := pat && isConcreteHole(t)){
     varloc = getConcreteHoleVarLoc(t);;
     <fuid, pos> = getVariableScope("ConcreteVar", varloc);
     holeType = getType(varloc);
     if(isIterWithSeparator(holeType)){
        return nIter(holeType);
     } 
  }
  return 1;
}

MuExp translatePatAsConcreteListElem(t:appl(Production applProd, list[Tree] args), Lookahead lookahead, bool isLex){
  //println("translatePatAsConcreteListElem:"); iprintln(applProd);
  //println("lex: <lex(_) := applProd.def>");
  if(lex(_) := applProd.def){
  	isLex = true;
  }
    if(isConcreteHole(t)){
     varloc = getConcreteHoleVarLoc(t);
     <fuid, pos> = getVariableScope("ConcreteVar", varloc);
     holeType = getType(varloc);
     //println("holeType = <holeType>");
    
     if(isIter(holeType)){
        if(isIterWithSeparator(holeType)){
           sep = getSeparator(holeType);
           libFun = "MATCH_<isLast(lookahead)>CONCRETE_MULTIVAR_WITH_SEPARATORS_IN_LIST";
           //println("libFun = <libFun>");
           //println("lookahead = <lookahead>");
           if(!isLex){
           		holeType = insertLayout(holeType);
           }
           return muApply(mkCallToLibFun("Library", libFun), [muVarRef("ConcreteListVar", fuid, pos), 
           													  muCon(nIter(holeType)), 
           													  muCon(1000000), 
           													  muCon(lookahead.nElem), 
                											  muCon(sep), 
                											  muCon(regular(holeType))]);
        } else {
           libFun = "MATCH_<isLast(lookahead)>CONCRETE_MULTIVAR_IN_LIST";
           //println("libFun = <libFun>");
           //println("lookahead = <lookahead>");
            if(!isLex){
           		holeType = insertLayout(holeType);
           }
           return muApply(mkCallToLibFun("Library", libFun), [muVarRef("ConcreteListVar", fuid, pos), 
           													  muCon(nIter(holeType)), 
           													  muCon(1000000), 
           													  muCon(lookahead.nElem),  
           													  muCon(regular(holeType))]);
       }
     }
     return muApply(mkCallToLibFun("Library","MATCH_VAR_IN_LIST"), [muVarRef("ConcreteVar", fuid, pos)]);
  }
  return translateApplAsListElem(applProd, args, isLex);
}

MuExp translatePatAsConcreteListElem(cc: char(int c), Lookahead lookahead, bool isLex){
  return muApply(mkCallToLibFun("Library","MATCH_LITERAL_IN_LIST"), [muCon(cc)]);
}

default MuExp translatePatAsConcreteListElem(Tree c, Lookahead lookahead, bool isLex){
  return muApply(mkCallToLibFun("Library","MATCH_PAT_IN_LIST"), [translateParsedConcretePattern(c, getType(c))]);
}

// Translate an appl as element of a concrete list pattern

MuExp translateApplAsListElem(p: prod(lit(str S), _, _), list[Tree] args, bool isLex) = 
 	muApply(mkCallToLibFun("Library","MATCH_LIT_IN_LIST"), [muCon(p)]);
 
default MuExp translateApplAsListElem(Production prod, list[Tree] args, bool isLex) = muApply(mkCallToLibFun("Library","MATCH_APPL_IN_LIST"), [muCon(prod), translateConcreteListPattern(args, isLex)]);

// Is an appl node a concrete multivar?

bool isConcreteMultiVar(t:appl(Production prod, list[Tree] args)){
  if(isConcreteHole(t)){
     varloc = getConcreteHoleVarLoc(t);
     holeType = getType(varloc);
     return isIter(holeType);
  }
  return false;
}

default bool isConcreteMultiVar(Tree t) = false;

// Compute a list of lookaheads for a  list of patterns.
// Recall that a Lookahead is a tuple of the form <number-of-elements-following, number-of-multi-vars-following>

list[Lookahead] computeConcreteLookahead(list[Tree] pats){
    //println("computeConcreteLookahead: <for(p <- pats){><p><}>");
    nElem = 0;
    nMultiVar = 0;
    rprops = for(Tree p <- reverse([p | Tree p <- pats])){
                 append <nElem, nMultiVar>;
                 if(isConcreteMultiVar(p)) {nMultiVar += 1; nElem += nIter(p); } else {nElem += 1;}
             };
    //println("result = <reverse(rprops)>");
    return reverse(rprops);
}
     
// -- qualified name pattern -----------------------------------------

MuExp translatePat(p:(Pattern) `<QualifiedName name>`, AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont){
   if("<name>" == "_"){
      return trueCont;
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muBlock([muAssign("<name>", fuid, pos, subjectExp), trueCont]);    // TODO case that name already had a value
                                                                            // TODO undefine name after contExp
} 

// -- typed name pattern ---------------------------------------------
     
MuExp translatePat(p:(Pattern) `<Type tp> <Name name>`, AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont){
   trType = translateType(tp);
   if(asubtype(subjectType, trType)){
	   if("<name>" == "_"){
	      return trueCont;
	   }
	   <fuid, pos> = getVariableScope("<name>", name@\loc);
	   return muBlock([muAssign("<name>", fuid, pos, subjectExp), trueCont]);
   }
   if("<name>" == "_"){
      return muIfelse(btscope, muValueIsSubType(subjectExp, trType), trueCont, falseCont);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muIfelse(btscope, muValueIsSubType(subjectExp, trType), muBlock([muAssign("<name>", fuid, pos, subjectExp), trueCont]), falseCont);
}  

// -- reified type pattern -------------------------------------------

MuExp translatePat(p:(Pattern) `type ( <Pattern symbol> , <Pattern definitions> )`, AType subjectType) {    
    return muApply(mkCallToLibFun("Library","MATCH_REIFIED_TYPE"), [muCon(symbol)]);
}

// -- call or tree pattern -------------------------------------------

MuExp translatePat(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont) {
   str fuid = topFunctionScope();
   subject = nextTmp("subject");
   
   if((KeywordArguments[Pattern]) `<OptionalComma optionalComma> <{KeywordArgument[Pattern] ","}+ keywordArgumentList>` := keywordArguments){
        for(kwarg <- keywordArgumentList){
            kwname = "<kwarg.name>";
            contExp = muIf(muHasKeywordArg(muTmp(subject, fuid), kwname), muBlock([translatePat(kwarg.expression, getType(kwarg.expression), muGetKeywordArg(muTmp(subject, fuid), kwname), trueCont)]), falseCont);
        }
   }
   
   lpats = [pat | pat <- arguments];   //TODO: should be unnnecessary
   body =  ( trueCont | translatePat(lpats[i], avalue(), muSubscript(muTmp(subject, fuid), muCon(i)), it, falseCont) | int i <- reverse(index(lpats)) );
     
   if(expression is qualifiedName){
      fun_name = "<getType(expression).name>";
      return muBlock([muAssignTmp(subject, fuid, subjectExp), muIfelse(btscope, muHasNameAndArity(fun_name, size(lpats), muTmp(subject ,fuid)), body, falseCont)]);
   } else if(expression is literal){ // StringConstant
      fun_name = "<expression>"[1..-1];
      return muBlock([muAssignTmp(subject, fuid, subjectExp), muIfelse(btscope, muHasNameAndArity(fun_name, size(lpats), muTmp(subject ,fuid)), body, falseCont)]);
    } else {
    // TODO
     fun_pat = translatePat(expression, getType(expression));
     return muApply(mkCallToLibFun("Library","MATCH_CALL_OR_TREE<noKwParams>"), [muCallMuPrim("make_array", fun_pat + argCode)]);
   }
}

// TODO
//MuExp translatePatKWArguments((KeywordArguments[Pattern]) ``) =
//   muApply(mkCallToLibFun("Library","MATCH_KEYWORD_PARAMS"), [muCallMuPrim("make_array", []), muCallMuPrim("make_array", [])]);
//
//MuExp translatePatKWArguments((KeywordArguments[Pattern]) `<OptionalComma optionalComma> <{KeywordArgument[Pattern] ","}+ keywordArgumentList>`) {
//   //println("translatePatKWArguments: <keywordArgumentList>");
//   keyword_names = [];
//   pats = [];
//   for(kwarg <- keywordArgumentList){
//       //println("kwarg = <kwarg>");
//       keyword_names += muCon("<kwarg.name>");
//       pats += translatePat(kwarg.expression, avalue()); // getType(kwarg.expression)?
//   }
//   return muApply(mkCallToLibFun("Library","MATCH_KEYWORD_PARAMS"), [muCallMuPrim("make_array", keyword_names), muCallMuPrim("make_array", pats)]);
//}

// -- set pattern ----------------------------------------------------

MuExp translatePat(p:(Pattern) `{<{Pattern ","}* pats>}`, AType subjectType) = translateSetPat(p, subjectType);

// Translate patterns as element of a set pattern

str isLast(bool b) = b ? "LAST_" : "";

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>`, bool last, AType subjectType) {
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR_IN_SET"), []);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_VAR_IN_SET"), [muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `<Type tp> <Name name>`, bool last, AType subjectType) {
   trType = translateType(tp);
   if(asubtype(subjectType, trType)){
	   if("<name>" == "_"){
	      return muApply(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR_IN_SET"), []);
	   }
	   <fuid, pos> = getVariableScope("<name>", name@\loc);
   	   return muApply(mkCallToLibFun("Library","MATCH_NEW_VAR_IN_SET"), [muVarRef("<name>", fuid, pos)]);
   }
   if("<name>" == "_"){
       return muApply(mkCallToLibFun("Library","MATCH_TYPED_ANONYMOUS_VAR_IN_SET"), [muTypeCon(trType)]);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_TYPED_VAR_IN_SET"), [muTypeCon(trType), muVarRef("<name>", fuid, pos)]);
}  

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>*`, bool last, AType subjectType) {
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>ANONYMOUS_MULTIVAR_IN_SET"), []);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>MULTIVAR_IN_SET"), [muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Type tp> <Name name>`, bool last, AType subjectType) {
   trType = translateType(tp);
   if(asubtype(subjectType, trType)){
	   if("<name>" == "_"){
	      return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>ANONYMOUS_MULTIVAR_IN_SET"), []);
	   }
	   <fuid, pos> = getVariableScope("<name>", p@\loc);
	   return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>NEW_MULTIVAR_IN_SET"), [muVarRef("<name>", fuid, pos)]);
   }
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>TYPED_ANONYMOUS_MULTIVAR_IN_SET"), [muTypeCon(aset(trType))]);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>TYPED_MULTIVAR_IN_SET"), [muTypeCon(aset(trType)), muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Name name>`, bool last, AType subjectType) {
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>ANONYMOUS_MULTIVAR_IN_SET"), []);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>MULTIVAR_IN_SET"), [muVarRef("<name>", fuid, pos)]);
} 

MuExp translatePatAsSetElem(p:(Pattern) `+<Pattern argument>`, bool last, AType subjectType) {
  throw "splicePlus pattern";
}   

default MuExp translatePatAsSetElem(Pattern p, bool last, AType subjectType) {
  //println("translatePatAsSetElem, default: <p>");
  try {
     return  muApply(mkCallToLibFun("Library","MATCH_LITERAL_IN_SET"), [muCon(translatePatternAsConstant(p))]);
  } catch:
    return muApply(mkCallToLibFun("Library","MATCH_PAT_IN_SET"), [translatePat(p, subjectType)]);
}

value getLiteralValue((Literal) `<Literal s>`) =  readTextValueString("<s>"); // TODO interpolation

bool isConstant(StringLiteral l) = l is nonInterpolated;
bool isConstant(LocationLiteral l) = l.protocolPart is nonInterpolated && l.pathPart is nonInterpolated;
bool isConstant(RegExpLiteral l)  = false;
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

MuExp translateSetPat(p:(Pattern) `{<{Pattern ","}* pats>}`, AType subjectType) {
   literals = [];
   compiledPats = [];
   list[Pattern] lpats = [pat | pat <- pats]; // TODO: unnnecessary
   elmType = avalue();
   if(aset(tp) := subjectType && tp != avoid()){
      elmType = tp;
   }
   
   /* remove patterns with duplicate names */
   list[Pattern] uniquePats = [];
   outer: for(int i <- index(lpats)){
      pat = lpats[i];
      str name = getName(pat, i);
      if(name != "_"){
	      for(int j <- [0 .. i]){
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
        compiledPats += translatePatAsSetElem(pat, i == lastPat, elmType);
      } else if(pat is multiVariable){
        compiledPats += translatePatAsSetElem(pat, i == lastPat, elmType);
      } else if(pat is qualifiedName){
        compiledPats += translatePatAsSetElem(pat, false, elmType);
      } else if(pat is typedVariable){
        compiledPats += translatePatAsSetElem(pat, false, elmType);   
      } else {
        compiledPats +=  muApply(mkCallToLibFun("Library","MATCH_PAT_IN_SET"), [translatePat(pat, elmType)]);
        // To enable constant elimination change to:
        // compiledPats += translatePatAsSetElem(pat, false);
      }
   }
   MuExp litCode = (all(Literal lit <- literals, isConstant(lit))) ? muCon({ getLiteralValue(lit) | Literal lit <- literals })
   		           										           : muCallPrim3("set_create", [ translate(lit) | Literal lit <- literals], p@\loc );
   
    return muApply(mkCallToLibFun("Library","MATCH_SET"), [ litCode, muCallMuPrim("make_array", compiledPats) ]);
   
   
   //return muApply(mkCallToLibFun("Library","MATCH_SET"), [ muCallMuPrim("make_array", [ litCode, 
   //                                                                                     muCallMuPrim("make_array", compiledPats) ]) ] );
}

// -- tuple pattern --------------------------------------------------

MuExp translatePat(p:(Pattern) `\<<{Pattern ","}* pats>\>`, AType subjectType, MuExp subjectExp, MuExp trueCont, MuExp falseCont) {
    if(atuple(atypeList(list[AType] elmTypes)) := subjectType && size(elmTypes) == size(pats)){
       lpats = [pat | pat <- pats];   //TODO: should be unnnecessary
       str fuid = topFunctionScope();
       subject = nextTmp();
       body =  ( trueCont | translatePat(lpats[i], elmTypes[i], muSubscript(muTmp(subject, fuid), muCon(i)), it, falseCont) | int i <- reverse(index(lpats)) );
       code = [muAssignTmp(subject, fuid, subjectExp), muIfelse(muHasTypeAndArity("tuple", size(lpats), muTmp(subject ,fuid)), body, falseCont)];
       return muBlock(code);
    }
}

// -- list pattern ---------------------------------------------------

MuExp translatePat(p:(Pattern) `[<{Pattern ","}* pats>]`, AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont) {
    lookahead = computeLookahead(p);  
    lpats = [pat | pat <- pats];   //TODO: should be unnnecessary
    elmType = avalue();
    if(alist(tp) := subjectType && tp != avoid()){
    	elmType = tp;
    }
    str fuid = topFunctionScope();
    subject = nextTmp("subject");
    cursor = nextTmp("cursor");
    sublen = nextTmp("sublen");
    body =  ( trueCont | translatePatAsListElem(lpats[i], lookahead[i], elmType, fuid, subject, sublen, cursor, it, falseCont) | int i <- reverse(index(lpats)) );
    code = [ muAssignTmp(subject, fuid, subjectExp),   
             muAssignTmp(cursor, fuid, muCon(0)), 
             muAssignTmp(sublen, fuid, muSize(muTmp(subject, fuid))),
             muIf(muAnd(muValueIsSubType(muTmp(subject ,fuid), subjectType), 
                       lookahead[0].nMultiVar == 0 && !isMultiVar(lpats[0])
                      ? muEqual(muTmp(sublen, fuid), muCon(lookahead[0].nElem + 1))
                      : muGreaterEq(muTmp(sublen, fuid), muCon(lookahead[0].nElem + 1))), body, falseCont)];
    return muBlock(code);
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
    rprops = for(Pattern p <- reverse([p | Pattern p <- pats])){
                 append <nElem, nMultiVar>;
                 if(isMultiVar(p)) nMultiVar += 1; else nElem += 1;
             };
    return reverse(rprops);
}

str isLast(Lookahead lookahead) = lookahead.nMultiVar == 0 ? "LAST_" : "";

MuExp translatePatAsListElem(p:(Pattern) `<QualifiedName name>`, Lookahead lookahead, AType subjectType, str fuid, str subject, str sublen, str cursor, MuExp trueCont, MuExp falseCont) {
    if("<name>" == "_"){
       return muBlock([muInc(cursor, fuid, muCon(1)), trueCont]);
    }
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muBlock([ muAssign("<name>", fuid, pos, muSubscript(muTmp(subject, fuid), muTmp(cursor, fuid))),
                     muInc(cursor, fuid, muCon(1)), 
                     trueCont]);
} 

MuExp translatePatAsListElem(p:(Pattern) `<Type tp> <Name name>`, Lookahead lookahead, AType subjectType, str fuid, str subject, str sublen, str cursor, MuExp trueCont, MuExp falseCont) {
   trType = translateType(tp);
   code = [];
  
   if("<name>" == "_"){
      code = [muInc(cursor, fuid, muCon(1)), trueCont];
   } else {
       str fuid; int pos;           // TODO: this keeps type checker happy, why?
       <fuid, pos> = getVariableScope("<name>", name@\loc);
       code = muBlock([ muAssign("<name>", fuid, pos, muSubscript(muTmp(subject, fuid), muTmp(cursor, fuid))),
                        muInc(cursor, fuid, muCon(1)), 
                        trueCont ]);
   }
   return asubtype(subjectType, trType) ? code : muIf(muValueIsSubType(muTmp(subject, fuid), trType), code, falseCont);
 } 

MuExp translatePatAsListElem(p:(Pattern) `<Literal lit>`, Lookahead lookahead, AType subjectType, str fuid, str subject, str sublen, str cursor, MuExp trueCont, MuExp falseCont) {
    if(lit is regExp) fail;
    return muIf(muEqual(translate(lit), muSubscript(muTmp(subject, fuid), muTmp(cursor, fuid))), 
                muBlock([ muInc(cursor, fuid, muCon(1)), trueCont ]),
                falseCont);
}

MuExp translatePatAsListElem(p:(Pattern) `_*`, Lookahead lookahead, AType subjectType, str fuid, str subject, str sublen, str cursor, MuExp trueCont, MuExp falseCont)
    = translateAnonymousMultiVar(lookahead, subjectType, fuid, subject, sublen, cursor, trueCont, falseCont);
    
MuExp translatePatAsListElem(p:(Pattern) `*_`, Lookahead lookahead, AType subjectType, str fuid, str subject, str sublen, str cursor, MuExp trueCont, MuExp falseCont)
    = translateAnonymousMultiVar(lookahead, subjectType, fuid, subject, sublen, cursor, trueCont, falseCont);

MuExp translateAnonymousMultiVar(Lookahead lookahead, AType subjectType, str fuid, str subject, str sublen, str cursor, MuExp trueCont, MuExp falseCont) {
    str fuid = topFunctionScope();
    str startcursor = nextTmp("startcursor");
    str len = nextTmp("len");
    if(lookahead.nMultiVar == 0){
        return muBlock([ muAssignTmp(cursor, fuid, muAdd(muTmp(cursor, fuid), muSub(muSub(muTmp(sublen, fuid), muTmp(cursor, fuid)), muCon(lookahead.nElem)))),
                         trueCont ]);
    }
    return muBlock([ muAssignTmp(startcursor, fuid, muTmp(cursor, fuid)),
                     muForRange(len, fuid, muCon(0), muCon(1), muSub(muTmp(sublen, fuid), muTmp(startcursor, fuid)), 
                                muBlock([ muAssignTmp(cursor, fuid, muAdd(muTmp(startcursor, fuid), muTmp(len, fuid))),
                                          trueCont ]))]); 
}

MuExp translatePatAsListElem(p:(Pattern) `<QualifiedName name>*`, Lookahead lookahead, AType subjectType, str fuid, str subject, str sublen, str cursor, MuExp trueCont, MuExp falseCont) {
    if(name == "_") fail;
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return translateNamedMultiVar("<name>", pos, lookahead, subjectType, fuid, subject, sublen, cursor, trueCont, falseCont);
}

MuExp translatePatAsListElem(p:(Pattern) `*<Name name>`, Lookahead lookahead, AType subjectType, str fuid, str subject, str sublen, str cursor, MuExp trueCont, MuExp falseCont) {
    if(name == "_") fail;
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return translateNamedMultiVar("<name>", pos, lookahead, subjectType, fuid, subject, sublen, cursor, trueCont, falseCont);
} 

MuExp translateNamedMultiVar(str name, int pos, Lookahead lookahead, AType subjectType, str fuid, str subject, str sublen, str cursor, MuExp trueCont, MuExp falseCont) {
    str fuid = topFunctionScope();
    str startcursor = nextTmp("startcursor");
    str len = nextTmp("len");

    if(lookahead.nMultiVar == 0){
        return muBlock([ muAssignTmp(startcursor, fuid, muTmp(cursor, fuid)), 
                         muAssignTmp(len, fuid, muSub(muSub(muTmp(sublen, fuid), muTmp(startcursor, fuid)), muCon(lookahead.nElem))),         
                         muAssign(name, fuid, pos, muSubList(muTmp(subject, fuid), muTmp(len, fuid))), 
                         muAssignTmp(cursor, fuid, muAdd(muTmp(startcursor, fuid), muTmp(len, fuid))),
                         trueCont ]);
    }
    return muBlock([ muAssignTmp(startcursor, fuid, muTmp(cursor, fuid)),
                    muForRange(len, fuid, muCon(0), muCon(1), muSub(muTmp(sublen, fuid), muTmp(startcursor, fuid)), 
                               muBlock([ muAssign(name, fuid, pos, muSubList(muTmp(subject, fuid), muTmp(len, fuid))),
                                         muAssignTmp(cursor, fuid, muAdd(muTmp(startcursor, fuid), muTmp(len, fuid))),
                                         trueCont ]))]);  
}

MuExp translatePatAsListElem(p:(Pattern) `*<Type tp> _`, Lookahead lookahead, AType subjectType, str fuid, str subject, str sublen, str cursor, MuExp trueCont, MuExp falseCont) {
    trType = translateType(tp);
    str fuid = topFunctionScope();
    code = muBlock([]);
  
    if(lookahead.nMultiVar == 0){
        code = muBlock([ muAssignTmp(cursor, fuid, muAdd(muTmp(cursor, fuid), muSub(muSub(muTmp(sublen, fuid), muTmp(cursor, fuid)), muCon(lookahead.nElem)))), trueCont ]);
       
    } else {
        str startcursor = nextTmp("startcursor");
        str len = nextTmp("len");
        code = muBlock([ muAssignTmp(startcursor, fuid, muTmp(cursor, fuid)),
                         muForRange(len, fuid, muCon(0), muCon(1), muSub(muTmp(sublen, fuid), muTmp(startcursor, fuid)), 
                                    muBlock([ muAssignTmp(cursor, fuid, muAdd(muTmp(startcursor, fuid), muTmp(len, fuid))), trueCont ]))
                       ]); 
    }
    return asubtype(subjectType, trType) ? code : muIfelse(muValueIsSubType(muTmp(subject, fuid), alist(trType)), code, falseCont);
}

MuExp translatePatAsListElem(p:(Pattern) `*<Type tp> <Name name>`, Lookahead lookahead, AType subjectType, str fuid, str subject, str sublen, str cursor, MuExp trueCont, MuExp falseCont) {
    if("<name>" == "_") fail;
    
    trType = translateType(tp);
    str fuid = topFunctionScope();
    str startcursor = nextTmp("startcursor");
    str len = nextTmp("len");
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    code = muBlock([]);
  
    if(lookahead.nMultiVar == 0){
        code = muBlock([ muAssignTmp(startcursor, fuid, muTmp(cursor, fuid)), 
                         muAssignTmp(len, fuid, muSub(muSub(muTmp(sublen, fuid), muTmp(startcursor, fuid)), muCon(lookahead.nElem))),         
                         muAssign("<name>", fuid, pos, muSubList(muTmp(subject, fuid), muTmp(len, fuid))), 
                         muAssignTmp(cursor, fuid, muAdd(muTmp(startcursor, fuid), muTmp(len, fuid))),
                         trueCont 
                       ]);
    } else {
       code = muBlock([ muAssignTmp(startcursor, fuid, muTmp(cursor, fuid)),
                        muForRange(len, fuid, muCon(0), muCon(1), muSub(muTmp(sublen, fuid), muTmp(startcursor, fuid)), 
                           muBlock([ muAssign("<name>", fuid, pos, muSubList(muTmp(subject, fuid), muTmp(len, fuid))),
                                     muAssignTmp(cursor, fuid, muAdd(muTmp(startcursor, fuid), muTmp(len, fuid))),
                                     trueCont ]))
                      ]);  
    }
    return asubtype(subjectType, trType) ? code : muIfelse(muValueIsSubType(muTmp(subject, fuid), alist(trType)), code, falseCont);
}

MuExp translatePatAsListElem(p:(Pattern) `+<Pattern argument>`, Lookahead lookahead, AType subjectType, MuExp trueCont, MuExp falseCont) {
    throw "splicePlus pattern";
} 

//MuExp translatePatAsListElem(p:(Pattern) `<Literal lit>`, Lookahead lookahead, AType subjectType, str fuid, str subject, str sublen, str cursor, MuExp contExp) {
//    if(!(lit is regExp)){
//        return muIf("", muEqual(translate(lit), muSubscript(muTmp(subject, fuid), muTmp(cursor, fuid))), 
//                        [ muInc(cursor, fuid, muCon(1)),
//                          contExp ]);
//    } else {
//        fail;
//    }
//}  

default MuExp translatePatAsListElem(Pattern p, Lookahead lookahead, AType subjectType, str fuid, str subject, str sublen, str cursor, MuExp trueCont, MuExp falseCont) {
   println("translatePatAsListElem, default: <p>");
   return translatePat(p, subjectType, muSubscript(muTmp(subject, fuid), muTmp(cursor, fuid)), muBlock([ muInc(cursor, fuid, muCon(1)), trueCont, elseCont ]));
}

// -- variable becomes pattern ---------------------------------------

MuExp translatePat(p:(Pattern) `<Name name> : <Pattern pattern>`, AType subjectType, MuExp subjectExp, MuExp trueCont, MuExp falseCont) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return translatePat(pattern, subjectType, subjectExp, muBlock([ muAssign("<name>", fuid, pos, subjectExp), trueCont ]), falseCont);
}

// -- as type pattern ------------------------------------------------

MuExp translatePat(p:(Pattern) `[ <Type tp> ] <Pattern argument>`, AType subjectType, MuExp subjectExp, MuExp trueCont, MuExp falseCont) =
    muIfelse(muValueIsSubType(subjectExp, translateType(tp)), trueCont, falseCont);

// -- descendant pattern ---------------------------------------------

MuExp translatePat(p:(Pattern) `/ <Pattern pattern>`, AType subjectType){
	//println("pattern <pattern>, isConcretePattern = <isConcretePattern(pattern)>");
	int i = nextVisit();	
	// Generate and add a nested function 'phi'
	str descId = topFunctionScope() + "/" + "DESC_<i>";
	//subjectType = stripStart(subjectType);
	concreteMatch = concreteTraversalAllowed(pattern, subjectType);
	descendantFun = concreteMatch && (subjectType != astr()) ? "DESCENT_AND_MATCH_CONCRETE" : "DESCENT_AND_MATCH";
	
	reachable_syms = { avalue() };
	reachable_prods = {};
    if(optimizing()){
	   tc = getTypesAndConstructors(pattern);
       <reachable_syms, reachable_prods>  = getReachableTypes(subjectType, tc.constructors, tc.types, concreteMatch);
       //println("translatePat: <reachable_syms>, <reachable_prods>");
    }
    descriptor = muCallPrim3("make_descendant_descriptor", [muCon(descId), muCon(reachable_syms), muCon(reachable_prods), muCon(concreteMatch), muCon(getDefinitions())], p@\loc);
    return muApply(mkCallToLibFun("Library",descendantFun), [translatePat(pattern, avalue()),  descriptor]);
}

// Strip start if present

AType stripStart(\start(AType s)) = s;
default AType stripStart(AType s) = s;

// Is  a pattern a concretePattern?
// Note that a callOrTree pattern always requires a visit of the production to inspect labeled fields and is etherefore
// NOT a concrete pattern

bool isConcretePattern(Pattern p) {
    tp = getType(p);
    return isNonTerminalType(tp) && !(p is callOrTree) && Symbol::sort(_) := tp;
}  
	
bool isConcreteType(AType subjectType) =
	(  isNonTerminalType(subjectType)
	|| asubtype(subjectType, adt("Tree", [])) && subjectType != adt("Tree",[])
	);
	
bool concreteTraversalAllowed(Pattern pattern, AType subjectType) =
    isConcretePattern(pattern) && isConcreteType(subjectType);
	
// get the types and constructor names from a pattern

tuple[set[AType] types, set[str] constructors] getTypesAndConstructors(p:(Pattern) `<QualifiedName qualifiedName>`) =
	<{getType(p)}, {}>;
	
tuple[set[AType] types, set[str] constructors] getTypesAndConstructors(p:(Pattern) `<Type tp> <Name name>`) =
	<{getType(p)}, {}>;	
	
tuple[set[AType] types, set[str] constructors] getTypesAndConstructors(p:(Pattern) `<Name name> : <Pattern pattern>`) =
	getTypesAndConstructors(pattern);	

tuple[set[AType] types, set[str] constructors]  getTypesAndConstructors(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`) =
	getTypesAndConstructors(pattern);
		
tuple[set[AType] types, set[str] constructors] getTypesAndConstructors(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`) =
	(expression is qualifiedName) ? <{}, {deescape("<expression>")}> : <{getType(p)}, {}>;

tuple[set[AType] types, set[str] constructors] getTypesAndConstructors(Pattern p) = <{getType(p)}, {}>;

// -- anti pattern ---------------------------------------------------

MuExp translatePat(p:(Pattern) `! <Pattern pattern>`, AType subjectType) =
    muApply(mkCallToLibFun("Library","MATCH_ANTI"), [translatePat(pattern, subjectType)]);

// -- typed variable becomes pattern ---------------------------------

MuExp translatePat(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`, AType subjectType, MuExp subjectExp, MuExp trueCont, MuExp falseCont) {
    trType = translateType(tp);
    str fuid; int pos;           // TODO: this keeps type checker happy, why?
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    if(asubtype(subjectType, trType)){  
    	return translatePat(pattern, subjectType, subjectExp, muBlock([ muAssign("<name>", fuid, pos, subjectExp), trueCont ]), falseCont);
    }
    muIfelse(muValueIsSubType(subjectExp, trType), muBlock([ muAssign("<name>", fuid, pos, subjectExp), trueCont ]), falseCont);
}

// -- default rule for pattern ---------------------------------------

default MuExp translatePat(Pattern p, AType subjectType, MuExp trueCont, MuExp falseCont) { iprintln(p); throw "Pattern <p> cannot be translated"; }

/**********************************************************************/
/*                 Constant Patterns                                  */
/**********************************************************************/
 
value translatePatternAsConstant(p:(Pattern) `<Literal lit>`) = getLiteralValue(lit) when isConstant(lit);

value translatePatternAsConstant(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`) =
  makeNode("<expression>", [ translatePatternAsConstant(pat) | Pattern pat <- arguments ] + translatePatKWArguments(keywordArguments));

value translatePatternAsConstant(p:(Pattern) `{<{Pattern ","}* pats>}`) = { translatePatternAsConstant(pat) | Pattern pat <- pats };

value translatePatternAsConstant(p:(Pattern) `[<{Pattern ","}* pats>]`) = [ translatePatternAsConstant(pat) | Pattern pat <- pats ];

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

//bool backtrackFree(p:(Pattern) `[<{Pattern ","}* pats>]`) = false;
//bool backtrackFree(p:(Pattern) `{<{Pattern ","}* pats>}`) = false;
//
//default bool backtrackFree(Pattern p) = true;
