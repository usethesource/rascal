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

MuExp translateMatch((Expression) `<Pattern pat> := <Expression exp>`, str btscope, MuExp trueCont, MuExp falseCont) 
    = translateMatch(pat, exp, btscope, trueCont, falseCont);

MuExp translateMatch(e: (Expression) `<Pattern pat> !:= <Expression exp>`,str btscope, MuExp trueCont, MuExp falseCont)
    = translateMatch(pat, exp, btscope, falseCont, trueCont);
    
default MuExp translateMatch(Pattern pat, Expression exp, str btscope, MuExp trueCont, MuExp falseCont) =
    translatePat(pat, getType(exp), translate(exp), btscope, trueCont, falseCont);

/*********************************************************************/
/*                  Patterns                                         */
/*********************************************************************/

// -- literal pattern ------------------------------------------------

default MuExp translatePat(p:(Pattern) `<Literal lit>`, AType subjectType, MuExp subject, str btscope, MuExp trueCont, MuExp falseCont) = translateLitPat(lit, subjectType, subject, btscope, trueCont, falseCont);

MuExp translatePat(Literal lit, AType subjectType, MuExp subject, str btscope, MuExp trueCont, MuExp falseCont) = translateLitPat(lit, subjectType, subject, btscope, trueCont, falseCont);

MuExp translateLitPat(Literal lit, AType subjectType, MuExp subject, str btscope, MuExp trueCont, MuExp falseCont) = muIfExp(muEqual(translate(lit), subject), trueCont, falseCont);

// -- regexp pattern -------------------------------------------------
//TODO
MuExp translatePat(p:(Pattern) `<RegExpLiteral r>`, AType subjectType, MuExp subject, str btscope, MuExp trueCont, MuExp falseCont) 
    = translateRegExpLiteral(r, subjectType, subject, btscope, trueCont, falseCont);

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

MuExp translateRegExpLiteral(re: (RegExpLiteral) `/<RegExp* rexps>/<RegExpModifier modifier>`, AType subjectType, MuExp subject, str btscope, MuExp trueCont, MuExp falseCont) {
   str fuid = topFunctionScope();
   <buildRegExp,vars> = processRegExpLiteral(re);
   matcher = muTmpMatcher(nextTmp("matcher"), fuid);
   code = [ muConInit(matcher, muRegExpCompile(buildRegExp, subject)),
            muWhileDo("", muRegExpFind(matcher),
                        muBlock([ *[ muAssign(vars[i], muRegExpGroup(matcher, i)) | i <- index(vars) ],
                                  trueCont
                               ])),
            falseCont
          ];
   return muBlock(code);
}

MuExp translateRegExpLiteral(re: (RegExpLiteral) `/<RegExp* rexps>/<RegExpModifier modifier>`, MuExp begin, MuExp end) {
// TODO
   <buildRegExp,varrefs> = processRegExpLiteral(re);
   return muApply(mkCallToLibFun("Library", "MATCH_REGEXP_IN_VISIT"), 
                 [ buildRegExp,
                   muCallMuPrim("make_array", varrefs),
                   begin,
                   end
                 ]); 
}

tuple[MuExp exp, list[MuExp] vars] processRegExpLiteral(e: (RegExpLiteral) `/<RegExp* rexps>/<RegExpModifier modifier>`){
   str fuid = topFunctionScope();
   fragmentCode = [];
   vars = [];
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
         		vars += varref;
         		varnames["<name>"] = size(vars);
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
   if(all(frag <- fragmentCode, muCon(_) := frag)){
      buildRegExp = muCon(intercalate("", [s | muCon(str s) <- fragmentCode]));
      return <buildRegExp, vars>;
   } else {
       swriter = muTmpStrWriter("swriter", fuid);
       buildRegExp = muValueBlock(muConInit(swriter, muCallPrim3("open_string_writer", astr(), [], [], e@\loc)) + 
                                  [ muCallPrim3("add_string_writer", astr(), [getType(exp)], [swriter, exp], e@\loc) | exp <- fragmentCode ] +
                                  muCallPrim3("close_string_writer", astr(), [], [swriter], e@\loc));
       return  <buildRegExp, vars>; 
   }  
}

tuple[MuExp var, list[MuExp] exps] extractNamedRegExp((RegExp) `\<<Name name>:<NamedRegExp* namedregexps>\>`) {
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
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return <muVar("<name>", fuid, pos, astr()), exps>;
}

// -- concrete syntax pattern ----------------------------------------
//TODO
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
   var = muVar("<name>", fuid, pos, subjectType);
   //return muIfEqualOrAssign(muVar("<name>", fuid, pos, subjectType), subjectExp, trueCont);    // TODO undefine name after trueExp
   if(isDefinition(name@\loc)){
    return muBlock([muVarInit(var, subjectExp), trueCont]);
   } else {
    return muIfelse(muEqual(var, subjectExp), trueCont, falseCont);
   }
} 

// -- typed name pattern ---------------------------------------------
     
MuExp translatePat(p:(Pattern) `<Type tp> <Name name>`, AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont){
   trType = translateType(tp);
   if(asubtype(subjectType, trType)){
	   if("<name>" == "_"){
	      return trueCont;
	   }
	   <fuid, pos> = getVariableScope("<name>", name@\loc);
	   return muBlock([muVarInit(muVar("<name>", fuid, pos, trType), subjectExp), trueCont]);
   }
   if("<name>" == "_"){
      return muIfelse(muValueIsSubType(subjectExp, trType), trueCont, falseCont);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muIfelse(muValueIsSubType(subjectExp, trType), muBlock([muAssign(muVar("<name>", fuid, pos, trType), subjectExp), trueCont]), falseCont);
}  

// -- reified type pattern -------------------------------------------
//TODO
MuExp translatePat(p:(Pattern) `type ( <Pattern symbol> , <Pattern definitions> )`, AType subjectType) {    
    return muApply(mkCallToLibFun("Library","MATCH_REIFIED_TYPE"), [muCon(symbol)]);
}

// -- call or tree pattern -------------------------------------------

MuExp translatePat(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont) {
   str fuid = topFunctionScope();
   subject = muTmpIValue(nextTmp("subject"), fuid, subjectType);
   contExp = trueCont;
   
   if((KeywordArguments[Pattern]) `<OptionalComma optionalComma> <{KeywordArgument[Pattern] ","}+ keywordArgumentList>` := keywordArguments){
        for(kwarg <- keywordArgumentList){
            kwname = "<kwarg.name>";
            contExp = muIfelse(muHasKwp(subject, kwname), muBlock([translatePat(kwarg.expression, getType(kwarg.expression), muGetKwp(subject, subjectType, kwname), btscope, trueCont, falseCont)]), falseCont);
        }
   }
   
   lpats = [pat | pat <- arguments];   //TODO: should be unnnecessary
   body =  ( contExp | translatePat(lpats[i], getType(lpats[i]), muSubscript(subject, muCon(i)), btscope, it, falseCont) | int i <- reverse(index(lpats)) );
     
   if(expression is qualifiedName){
      fun_name = "<getType(expression).label>";
      return muBlock([muConInit(subject, subjectExp), muIfelse(muHasNameAndArity(subjectType, fun_name, size(lpats), subject), body, falseCont)]);
   } else if(expression is literal){ // StringConstant
      fun_name = "<expression>"[1..-1];
      return muBlock([muConInit(subject, subjectExp), muIfelse(muHasNameAndArity(subjectType, fun_name, size(lpats), subject), body, falseCont)]);
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

MuExp translatePat(p:(Pattern) `{<{Pattern ","}* pats>}`, AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont) 
    = translateSetPat(p, subjectType, subjectExp, btscope, trueCont, falseCont);

// Translate patterns as element of a set pattern

str isLast(bool b) = b ? "LAST_" : "";

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>`, bool last, AType elmType, MuExp subject, MuExp prevSubject, str btscope, MuExp trueCont, MuExp falseCont) {
   str fuid = topFunctionScope();
   elem = muTmpIValue(nextTmp("elem"), fuid, elmType);
   if("<name>" == "_"){
          return muForAll("", elem, prevSubject,
                          muBlock([ *(last ? [] : [muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), elmType], [prevSubject, elem], p@\loc))]),
                                    trueCont]));
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   var = muVar("<name>", fuid, pos, elmType);
   return muForAll("", elem, prevSubject,
                   muBlock([ *(isUsed(var, trueCont) ? [muAssign(var, elem)] : []),
                             *(last ? [] : [muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), elmType], [prevSubject, var], p@\loc))]),
                             trueCont]));
}

MuExp translatePatAsSetElem(p:(Pattern) `<Type tp> <Name name>`, bool last, AType elmType, MuExp subject, MuExp prevSubject, str btscope, MuExp trueCont, MuExp falseCont) {
   trType = translateType(tp); 
   str fuid = topFunctionScope();
   elem = muTmpIValue(nextTmp("elem"), fuid, avalue());
   
   if(asubtype(elmType, trType)){
	   if("<name>" == "_"){
	      return muForAll("", elem, prevSubject,
                          muBlock([ *(last ? [] : [muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), elmType], [prevSubject, elem], p@\loc))]),
                                    trueCont]));
	   }
	   <fuid, pos> = getVariableScope("<name>", name@\loc);
	   var = muVar("<name>", fuid, pos, trType);
	  
	   return muForAll("", elem, prevSubject,
	                   muBlock([ *(isUsed(var, trueCont) ? [muAssign(var, elem)] : []),
	                             *(last ? [] : [muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), elmType], [prevSubject, var], p@\loc))]),
	                             trueCont]));
   } else {
       if("<name>" == "_"){
          return muForAll("", elem, prevSubject,
                          muBlock([ muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), elmType], [prevSubject, elem], p@\loc)),
                                    muIfelse(muValueIsSubType(subject, aset(trType)), trueCont, muFail(""))
                                  ]));
                                     
       } else {
           <fuid, pos> = getVariableScope("<name>", name@\loc);
           var = muVar("<name>", fuid, pos, trType);
          
           return muForAll("", elem, prevSubject,
                           muBlock([ muAssign(var, elem),
                                     muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), elmType], [prevSubject, var], p@\loc)),
                                     muIfelse(muValueIsSubType(subject, aset(trType)), trueCont, muFail(""))
                                   ]));
       }   
     }
} 

MuExp translatePatAsSetElem(p:(Pattern) `_*`, bool last, AType elmType, MuExp subject, MuExp prevSubject, str btscope, MuExp trueCont, MuExp falseCont)
    = translateAnonymousMultiVar(last, elmType, subject, prevSubject, btscope, trueCont, falseCont);
 
MuExp translatePatAsSetElem(p:(Pattern) `*_`, bool last, AType elmType, MuExp subject, MuExp prevSubject, str btscope, MuExp trueCont, MuExp falseCont)
    = translateAnonymousMultiVar(last, elmType, subject, prevSubject, btscope, trueCont, falseCont);
 
MuExp translateAnonymousMultiVar(bool last, AType elmType, MuExp subject, MuExp prevSubject, str btscope, MuExp trueCont, MuExp falseCont){
    str fuid = topFunctionScope();
    elem = muTmpIValue(nextTmp("elem"), fuid, aset(elmType));
    my_btscope = nextTmp("ANONYMOUS_MULTIVAR");
    enterBacktrackingScope(my_btscope);

    code = muForAll(my_btscope, elem, prevSubject,
                    muBlock([ *(last ? [] : [muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), elmType],[prevSubject, elem], p@\loc))]),
                              trueCont
                            ]));
    leaveBacktrackingScope();
    return code;
}

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>*`, bool last, AType elmType, MuExp subject, MuExp prevSubject, str btscope, MuExp trueCont, MuExp falseCont) {
    if(name == "_") fail;
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return translateNamedMultiVar(muVar("<name>", fuid, pos, aset(elmType)), last, elmType, subject, prevSubject, btscope, trueCont, falseCont);
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Name name>`, bool last, AType elmType, MuExp subject, MuExp prevSubject, str btscope, MuExp trueCont, MuExp falseContt) {
    if(name == "_") fail;
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return translateNamedMultiVar(muVar("<name>", fuid, pos, aset(elmType)), last, elmType, subject, prevSubject, btscope, trueCont, falseCont);
}

MuExp translateNamedMultiVar(MuExp var, bool last, AType elmType, MuExp subject, MuExp prevSubject, str btscope, MuExp trueCont, MuExp falseContt) {
  str fuid = topFunctionScope();
  elem = muTmpIValue(nextTmp("elem"), fuid, aset(elmType));
  my_btscope = nextTmp("<name>_MULTIVAR");
  enterBacktrackingScope(my_btscope);
  code = muForAll(my_btscope, elem, muCallPrim3("subsets", aset(elmType), [aset(elmType)], [prevSubject], p@\loc),
                  muBlock([ *(isUsed(var, trueCont) ? [muAssign(var, elem)] : []),
                            *(last ? [] : [muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), aset(elmType)], [prevSubject, var], p@\loc))]),
                            trueCont
                          ]));
   leaveBacktrackingScope();
   return code;
} 

MuExp translatePatAsSetElem(p:(Pattern) `*<Type tp>  _`, bool last, AType elmType, MuExp subject, MuExp prevSubject, str btscope, MuExp trueCont, MuExp falseCont) {
   trType = translateType(tp);
   str fuid = topFunctionScope();
   elem = muTmpIValue(nextTmp("elem"), fuid, aset(trType));
   my_btscope = nextTmp("ANONYMOUS_MULTIVAR");
   enterBacktrackingScope(my_btscope);
   code = muBlock([]);

   if(asubtype(elmType, trType)){
        code = muForAll(my_btscope, elem, prevSubject,
                        muBlock([ *(last ? [] : [muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), aset(elmType)], [prevSubject, elem], p@\loc))]),
                                  trueCont
                                ]));
   } else {
        code = muForAll(my_btscope, elem, prevSubject,
                        muBlock([ muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), aset(elmType)], [prevSubject, elem], p@\loc)),
                                  muIfelse(muValueIsSubType(subject, aset(trType)), trueCont, muFail(my_btscope))
                                ]));
   }
   leaveBacktrackingScope();
   return code;
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Type tp> <Name name>`, bool last, AType elmType, MuExp subject, MuExp prevSubject, str btscope, MuExp trueCont, MuExp falseCont) {
   if(name == "_") fail;
   trType = translateType(tp);
   str fuid = topFunctionScope();
   elem = muTmpIValue(nextTmp("elem"), fuid, aset(trType));
   my_btscope = nextTmp("<name>_MULTIVAR");
   enterBacktrackingScope(my_btscope);
   code = muBlock([]);
   
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   var = muVar("<name>", fuid, pos, aset(trType));

   if(asubtype(elmType, trType)){
       code = muForAll(my_btscope, elem, muCallPrim3("subsets", aset(elmType), [aset(elmType)], [prevSubject], p@\loc),
                       muBlock([ *(isUsed(var, trueCont) ? [muAssign(var, elem)] : []),
                                 *(last ? [] : [muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), aset(elmType)], [prevSubject, var], p@\loc))]),
                                 trueCont
                               ]));
   } else {
       code = muForAll(my_btscope, elem, muCallPrim3("subsets", aset(elmType) [aset(elmType)], [prevSubject], p@\loc),
                       muBlock([ muAssign(var, elem),
                                 muConInit(subject, muCallPrim3("subtract",  aset(elmType), [aset(elmType), aset(elmType)], [prevSubject, var], p@\loc)),
                                 muIfelse(muValueIsSubType(subject, aset(trType)), trueCont, muFail(my_btscope))
                               ]));
   }
   leaveBacktrackingScope();
   return code;
}

MuExp translatePatAsSetElem(p:(Pattern) `+<Pattern argument>`, bool last, AType elmType, MuExp subject, MuExp prevSubject, str btscope, MuExp trueCont, MuExp falseCont) {
  throw "splicePlus pattern <p>";
}   

default MuExp translatePatAsSetElem(Pattern p, bool last, AType elmType, MuExp subject, MuExp prevSubject, str btscope, MuExp trueCont, MuExp falseCont) {
  try {
        pcon = muCon(translatePatternAsConstant(p));
        return muIfelse(muCallPrim3("in", abool(), [elmType, aset(elmType)], [pcon, prevSubject], p@\loc),
                        muBlock([ *(last ? [] : [muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), elmType], [prevSubject, pcon], p@\loc))]),
                                  trueCont ]),
                        falseCont);                            
  } catch: {
        str fuid = topFunctionScope();
        elem = muTmpIValue(nextTmp("elem"), fuid, aset(elmType));
        my_btscope = nextTmp("PAT_IN_SET");
        enterBacktrackingScope(my_btscope);
        code = muForAll(my_btscope, elem, muCallPrim3("subsets", aset(elmType), [aset(elmType)], [prevSubject], p@\loc),
                        muBlock([ muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType)], [prevSubject, elem], p@\loc)),
                                  translatePat(p, elmType, elem, my_btscope, trueCont, falseCont)
                                ]));
        leaveBacktrackingScope();
        return code;
  }
}

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

MuExp translateSetPat(p:(Pattern) `{<{Pattern ","}* pats>}`, AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont) {
   literalPats = [];
   compiledPats = [];
   list[Pattern] lpats = [pat | pat <- pats]; // TODO: unnnecessary
   elmType = avalue();
   if(aset(tp) := subjectType && tp != avoid()){
      elmType = tp;
   }
   
   /* remove patterns with duplicate names */
   list[Pattern] uniquePats = [];
   list[Pattern] setVars = [];
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
      if(pat is splice || pat is multiVariable){
        setVars += pat;
      } else { 
        uniquePats += pat;
      }
   }   
 
   uniquePats += setVars;   // move all set variables to the end
   
   str fuid = topFunctionScope();
   subject = muTmpIValue(nextTmp("subject"), fuid, subjectType);
   literals = muTmpIValue(nextTmp("literals"), fuid, elmType);
   
   subjects = [ muTmpIValue(nextTmp("subject"), fuid, subjectType) |int i <- reverse(index(uniquePats)) ];
    
   lastPat = size(uniquePats) - 1;
   setPatTrueCont = trueCont;
   setPatSubject = subject;
   leftMostVar = -1;
   for(int i <- reverse(index(uniquePats))){
      pat = uniquePats[i];
      isLastPat = (i == lastPat);
      currentSubject = subjects[i];
      previousSubject = (i==0) ? subject : subjects[i-1];
     // println("i = <i>; leftMostVar = <leftMostVar>, <pat>, current=<currentSubject.name>, previous=<previousSubject.name>");
      if(pat is literal){
         literalPats += pat.literal;
      } else if(pat is splice || pat is multiVariable || pat is qualifiedName || pat is typedVariable){
         leftMostVar = i;
         setPatTrueCont= translatePatAsSetElem(pat, isLastPat, elmType, currentSubject, previousSubject, btscope, setPatTrueCont, falseCont);
      } else {
        setPatTrueCont = translatePatAsSetElem(pat, isLastPat, elmType, currentSubject, previousSubject, btscope, setPatTrueCont, falseCont);
        //compiledPats +=  muApply(mkCallToLibFun("Library","MATCH_PAT_IN_SET"), [translatePat(pat, elmType)]);
        // To enable constant elimination change to:
        // compiledPats += translatePatAsSetElem(pat, false);
      }
   }
   MuExp litCode = (all(Literal lit <- literalPats, isConstant(lit))) ? muCon({ getLiteralValue(lit) | Literal lit <- literalPats })
   		           										              : muCallPrim3("create_set", aset(elmType), [aset(elmType), elmType], [ translate(lit) | Literal lit <- literalPats], p@\loc );
  
   code = [ muVarInit(subject, subjectExp),
            muConInit(literals, litCode),
            muIfelse(muCallPrim3("subset", aset(elmType), [aset(elmType), aset(elmType)], [literals, subject], p@\loc),
                     muBlock([ *(leftMostVar < 0 ? [] : [muConInit(leftMostVar == 0 ? subject : subjects[leftMostVar-1], muCallPrim3("subtract", aset(elmType), [aset(elmType), aset(elmType)], [subject, literals], p@\loc))]),
                                setPatTrueCont]),
                                falseCont)
          ];
   return muBlock(code);
}

// -- tuple pattern --------------------------------------------------

MuExp translatePat(p:(Pattern) `\<<{Pattern ","}* pats>\>`, AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont) {
    lpats = [pat | pat <- pats];   //TODO: should be unnnecessary
    elmTypes = [getType(pat) | pat <- lpats];
    patType = atuple(atypeList(elmTypes));
    
    str fuid = topFunctionScope();
    subject = muTmpIValue(nextTmp("tuple_subject"), fuid, subjectType);
    body =  ( trueCont | translatePat(lpats[i], elmTypes[i], muSubscript(subject, muCon(i)), btscope, it, falseCont) | int i <- reverse(index(lpats)) ); // TODO only first with btscope?
    code = [muConInit(subject, subjectExp), muIfelse(muHasTypeAndArity(patType, size(lpats), subject), body, falseCont)];
    return muBlock(code);
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
    my_btscope = nextTmp("LISTMATCH");
    enterBacktrackingScope(my_btscope);
    subject = muTmpIValue(nextTmp("subject"), fuid, subjectType);
    cursor = muTmpInt(nextTmp("cursor"), fuid);
    sublen = muTmpInt(nextTmp("sublen"), fuid);
    
    typecheckNeeded = asubtype(getType(p), subjectType);
    
    body =  ( trueCont | translatePatAsListElem(lpats[i], lookahead[i], elmType, subject, sublen, cursor, my_btscope, it, falseCont) | int i <- reverse(index(lpats)) );
    
    block = muBlock([ muConInit(sublen, muSize(subject, subjectType)),
                      muIfelse(isEmpty(lookahead) ? muEqualNativeInt(sublen, muCon(0))
                                                  : (lookahead[0].nMultiVar == 0 && !isMultiVar(lpats[0])) ? muEqualNativeInt(sublen, muCon(lookahead[0].nElem + 1))
                                                                                                           : muGreaterEqNativeInt(sublen, muCon(lookahead[0].nElem + 1)), 
                               body, 
                               falseCont),
                      falseCont
                    ]);
    
    code = muEnter(my_btscope,
             muBlock([ muConInit(subject, subjectExp),   
                       muVarInit(cursor, muCon(0)), 
                       *(typecheckNeeded ? [muIfelse( muValueIsSubType(subject, subjectType),
                                                      block,
                                                      falseCont)]
                                                     
                                         : [ block ])
                     ]));
    leaveBacktrackingScope();
    return code;
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

MuExp translatePatAsListElem(p:(Pattern) `<QualifiedName name>`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, str btscope, MuExp trueCont, MuExp falseCont) {
    if("<name>" == "_"){
       return muBlock([muIncNativeInt(cursor, muCon(1)), trueCont]);
    }
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    var =  muVar("<name>", fuid, pos, subjectType);
    if(isDefinition(name@\loc)){
        return muBlock([ muVarInit(var, muSubscript(subject, cursor)),
                         muIncNativeInt(cursor, muCon(1)), 
                         trueCont]);
    } else {
        return muIfelse(muEqual(var, muSubscript(subject, cursor)),
                       muBlock([ muIncNativeInt(cursor, muCon(1)), trueCont]),
                       falseCont);
    }
} 

MuExp translatePatAsListElem(p:(Pattern) `<Type tp> <Name name>`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, str btscope, MuExp trueCont, MuExp falseCont) {
   trType = translateType(tp);
   code = muBlock([]);
  
   if("<name>" == "_"){
      code = muBlock([muIncNativeInt(cursor, muCon(1)), trueCont]);
   } else {
       str fuid; int pos;           // TODO: this keeps type checker happy, why?
       <fuid, pos> = getVariableScope("<name>", name@\loc);
       code = muBlock([ muVarInit(muVar("<name>", fuid, pos, trType), muSubscript(subject, cursor)),
                        muIncNativeInt(cursor, muCon(1)), 
                        trueCont ]);
   }
   return asubtype(subjectType, trType) ? code : muIfelse(muValueIsSubType(subject, trType), code, falseCont);
 } 

MuExp translatePatAsListElem(p:(Pattern) `<Literal lit>`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, str btscope, MuExp trueCont, MuExp falseCont) {
    if(lit is regExp) fail;
    return muIfelse(muEqual(translate(lit), muSubscript(subject, cursor)), 
                    muBlock([ muIncNativeInt(cursor, muCon(1)), trueCont ]),
                    falseCont);
}

MuExp translatePatAsListElem(p:(Pattern) `_*`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor,str btscope, MuExp trueCont, MuExp falseCont)
    = translateAnonymousMultiVar(lookahead, subjectType, subject, sublen, cursor, btscope, trueCont, falseCont);
    
MuExp translatePatAsListElem(p:(Pattern) `*_`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, str btscope, MuExp trueCont, MuExp falseCont)
    = translateAnonymousMultiVar(lookahead, subjectType, subject, sublen, cursor, btscope, trueCont, falseCont);

MuExp translateAnonymousMultiVar(Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, str btscope, MuExp trueCont, MuExp falseCont) {
    str fuid = topFunctionScope();
    startcursor = muTmpInt(nextTmp("startcursor"), fuid);
    len = muTmpInt(nextTmp("len"), fuid);
    if(lookahead.nMultiVar == 0){
        return muBlock([ muIncNativeInt(cursor, muSubNativeInt(muSubNativeInt(sublen, cursor), muCon(lookahead.nElem))),
                         trueCont ]);
    }
    
    my_btscope = nextTmp("ANONYMOUS_MULTIVAR");
    enterBacktrackingScope(my_btscope);
    code = muBlock([ muConInit(startcursor, cursor),
                     muForRangeInt(my_btscope, len, 0, 1, muSubNativeInt(sublen, startcursor), 
                                muBlock([ muAssign(cursor, muAddNativeInt(startcursor, len)),
                                          trueCont ]))]); 
    leaveBacktrackingScope();
    return code;
}

MuExp translatePatAsListElem(p:(Pattern) `<QualifiedName name>*`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, str btscope, MuExp trueCont, MuExp falseCont) {
    if(name == "_") fail;
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return translateNamedMultiVar("<name>", pos, lookahead, subjectType, subject, sublen, cursor,btscope, trueCont, falseCont);
}

MuExp translatePatAsListElem(p:(Pattern) `*<Name name>`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, str btscope, MuExp trueCont, MuExp falseCont) {
    if(name == "_") fail;
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return translateNamedMultiVar("<name>", pos, lookahead, subjectType, subject, sublen, cursor, btscope, trueCont, falseCont);
} 

bool isUsed(MuExp var, MuExp exp)
    = /var := exp;

MuExp translateNamedMultiVar(str name, int pos, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, str btscope, MuExp trueCont, MuExp falseCont) {
    str fuid = topFunctionScope();
    startcursor = muTmpInt(nextTmp("startcursor"), fuid);
    len = muTmpInt(nextTmp("len"), fuid);
    var = muVar(name, fuid, pos, alist(subjectType));
    my_btscope = nextTmp("<name>_LISTVAR");
    enterBacktrackingScope(my_btscope);
    code = muBlock([]);
    if(lookahead.nMultiVar == 0){
        code = muBlock([ muConInit(startcursor, cursor), 
                         muConInit(len, muSubNativeInt(muSubNativeInt(sublen, startcursor), muCon(lookahead.nElem))),         
                         *(isUsed(var, trueCont) ? [muConInit(var, muSubList(subject, startcursor, len))] : []),
                         muAssign(cursor, muAddNativeInt(startcursor, len)),
                         trueCont
                       ]);
    } else {
        code = muBlock([ muConInit(startcursor, cursor),
                         muForRangeInt(my_btscope, len, 0, 1, muSubNativeInt(sublen, startcursor), 
                                       muBlock([ *(isUsed(var, trueCont) ? [muConInit(var, muSubList(subject, startcursor, len))] : [] ),
                                                 muAssign(cursor, muAddNativeInt(startcursor, len)),
                                                 trueCont
                                               ]))
                       ]);  
    }
    code = muEnter(my_btscope, code);
    leaveBacktrackingScope();
    return code;
}

MuExp translatePatAsListElem(p:(Pattern) `*<Type tp> _`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, str btscope, MuExp trueCont, MuExp falseCont) {
    trType = translateType(tp);
    str fuid = topFunctionScope();
    code = muBlock([]);
  
    if(lookahead.nMultiVar == 0){
        code = muBlock([ muIncNativeInt(cursor, muSubNativeInt(muSubNativeInt(sublen, cursor), muCon(lookahead.nElem))), trueCont ]);
       
    } else {
        startcursor = muTmpInt(nextTmp("startcursor"), fuid);
        len = muTmpInt(nextTmp("len"), fuid);
        code = muBlock([ muConInit(startcursor, cursor),
                         muForRangeInt(btscope, len, 0, 1, muSubNativeInt(sublen, startcursor), 
                                    muBlock([ muAssign(cursor, muAddNativeInt(startcursor, len)), trueCont ]))
                       ]); 
    }
    return asubtype(subjectType, trType) ? code : muIfelse(muValueIsSubType(subject, alist(trType)), code, falseCont);
}

MuExp translatePatAsListElem(p:(Pattern) `*<Type tp> <Name name>`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, str btscope, MuExp trueCont, MuExp falseCont) {
    if("<name>" == "_") fail;
    
    trType = translateType(tp);
    str fuid = topFunctionScope();
    startcursor = muTmpInt(nextTmp("startcursor"), fuid);
    len = muTmpInt(nextTmp("len"), fuid);
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    var = muVar("<name>", fuid, pos, alist(subjectType));
    
    my_btscope = nextTmp("<name>_LISTVAR");
    enterBacktrackingScope(my_btscope);
    code = muBlock([]);
  
    if(lookahead.nMultiVar == 0){
        code = muBlock([ muConInit(startcursor, cursor), 
                         muConInit(len, muSubNativeInt(muSubNativeInt(sublen, startcursor), muCon(lookahead.nElem))),         
                         *( isUsed(var, trueCont) ? [muConInit(var, muSubList(subject, startcursor, len))] : [] ),
                         muAssign(cursor, muAddNativeInt(startcursor, len)),
                         trueCont 
                       ]);
    } else {
        code = muBlock([ muConInit(startcursor, cursor),
                         muForRangeInt(my_btscope, len, 0, 1, muSubNativeInt(sublen, startcursor), 
                                      muBlock([ *( isUsed(var, trueCont) ? [muConInit(var, muSubList(subject, startcursor, len))] : [] ),
                                                 muAssign(cursor, muAddNativeInt(startcursor, len)),
                                                 trueCont 
                                              ]))
                      ]);  
    }
    code = asubtype(subjectType, trType) ? code : muIfelse(muValueIsSubType(subject, alist(trType)), code, falseCont);
    //code = muEnter(my_btscope, code);
    leaveBacktrackingScope();
    return code;
}

MuExp translatePatAsListElem(p:(Pattern) `+<Pattern argument>`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, str btscope, MuExp trueCont, MuExp falseCont) {
    throw "splicePlus pattern";
} 

default MuExp translatePatAsListElem(Pattern p, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, str btscope, MuExp trueCont, MuExp falseCont) {
   println("translatePatAsListElem, default: <p>");
   return translatePat(p, subjectType, muSubscript(subject, cursor), btscope, muValueBlock([ muIncNativeInt(cursor, muCon(1)), trueCont]), falseCont);
}

// -- variable becomes pattern ---------------------------------------

MuExp translatePat(p:(Pattern) `<Name name> : <Pattern pattern>`, AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return translatePat(pattern, subjectType, subjectExp, btscope, muValueBlock([ muAssign(muVar("<name>", fuid, pos, getType(pattern)), subjectExp), trueCont ]), falseCont);
}

// -- as type pattern ------------------------------------------------

MuExp translatePat(p:(Pattern) `[ <Type tp> ] <Pattern argument>`, AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont) =
    muIfelse(muValueIsSubType(subjectExp, translateType(tp)), trueCont, falseCont);

// -- descendant pattern ---------------------------------------------

MuExp translatePat(p:(Pattern) `/ <Pattern pattern>`,  AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont){
	
	my_btscope = nextTmp("DESCENDANT");
	enterBacktrackingScope(btscope);
	concreteMatch = concreteTraversalAllowed(pattern, subjectType);

//TODO: add descendant info	
	reachable_syms = { avalue() };
	reachable_prods = {};
    if(optimizing()){
	   tc = getTypesAndConstructors(pattern);
       <reachable_syms, reachable_prods>  = getReachableTypes(subjectType, tc.constructors, tc.types, concreteMatch);
       //println("translatePat: <reachable_syms>, <reachable_prods>");
    }
    descriptor = descendantDescriptor(concreteMatch);
    fuid = topFunctionScope();
    elem = muTmpIValue("elem", fuid, avalue());
    code = 
        muForAll(my_btscope, elem, muDescendantMatchIterator(subjectExp, descriptor),
                translatePat(pattern, avalue(), elem, my_btscope, trueCont, muFail(my_btscope))
             );
    leaveBacktrackingScope();
    return code;
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

MuExp translatePat(p:(Pattern) `! <Pattern pattern>`, AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont)
    = translatePat(pattern, subjectType, subjectExp, btscope, falseCont, trueCont);

// -- typed variable becomes pattern ---------------------------------

MuExp translatePat(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`, AType subjectType, MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont) {
    trType = translateType(tp);
  
    if("<name>" == "_"){
         trPat = translatePat(pattern, subjectType, subjectExp, btscope, trueCont, falseCont);
         return asubtype(subjectType, trType) ? trPat : muIfelse(muValueIsSubType(subjectExp, trType), trPat, falseCont);
    }
    str fuid; int pos;           // TODO: this keeps type checker happy, why?
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    var = muVar("<name>", fuid, pos, trType);
    trPat = translatePat(pattern, subjectType, subjectExp, btscope, muValueBlock([ muAssign(var, subjectExp), trueCont ]), falseCont);
    return asubtype(subjectType, trType) ? trPat :  muIfelse(muValueIsSubType(subjectExp, trType), trPat, falseCont);
}

// -- default rule for pattern ---------------------------------------

default MuExp translatePat(Pattern p, AType subjectType,  MuExp subjectExp, str btscope, MuExp trueCont, MuExp falseCont) { 
    iprintln(p); 
    throw "Pattern <p> cannot be translated"; 
}

/*****************************************************************************/
/*                      Constant Patterns                                    */
/* - try to translate a pattern to a constant (and throw an exception when   */
/*   this is impossible                                                      */
/*****************************************************************************/

value getLiteralValue((Literal) `<Literal s>`) =  readTextValueString("<s>") when isConstant(s);

bool isConstant(StringLiteral l) = l is nonInterpolated;
bool isConstant(LocationLiteral l) = l.protocolPart is nonInterpolated && l.pathPart is nonInterpolated;
bool isConstant(RegExpLiteral l)  = false;
default bool isConstant(Literal l) = true;
 
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

bool backtrackFree(p:(Pattern) `[<{Pattern ","}* pats>]`) = false;
bool backtrackFree(p:(Pattern) `{<{Pattern ","}* pats>}`) = false;

default bool backtrackFree(Pattern p) = true;
