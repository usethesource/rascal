@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalPattern
   
import IO;
import ValueIO;
import Location;
import Node;
import Map;
import Set;
import String;
import ParseTree;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::compile::muRascal::AST;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::NameUtils;
import lang::rascalcore::compile::util::Names;

import lang::rascalcore::compile::Rascal2muRascal::ModuleInfo;
import lang::rascalcore::compile::Rascal2muRascal::RascalType;
import lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
//import lang::rascalcore::compile::Rascal2muRascal::TypeReifier;

import lang::rascalcore::compile::Rascal2muRascal::RascalExpression;

//import lang::rascalcore::compile::RVM::Interpreter::ParsingTools;

/*
 * Compile the match operator and all possible patterns
 */

/*********************************************************************/
/*                  Match                                            */
/*********************************************************************/

default MuExp translateMatch(Pattern pat, Expression exp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) =
    translatePat(pat, getType(exp), translate(exp), btscopes, trueCont, falseCont);

//MuExp translateMatch((Expression) `<Pattern pat> := <Expression exp>`, str btscope, MuExp trueCont, MuExp falseCont) 
//    = translateMatch(pat, exp, btscope, trueCont, falseCont);
//
//MuExp translateMatch(e: (Expression) `<Pattern pat> !:= <Expression exp>`, str btscope, MuExp trueCont, MuExp falseCont)
//    = translateMatch(pat, exp, btscope, falseCont, trueCont);
//    
//default MuExp translateMatch(Pattern pat, Expression exp, str btscope, MuExp trueCont, MuExp falseCont) =
//    translatePat(pat, getType(exp), translate(exp), btscope, trueCont, falseCont);

/*********************************************************************/
/*                  Get Backtracking Scopes for a Pattern            */
/*********************************************************************/

alias BTSCOPE = tuple[str enter, str resume, str \fail];  // The enter/resume/fail labels of a backtracking scope
alias BTSCOPES = map[loc,BTSCOPE];                        // Map from program fragments to backtracking scopes
alias BTINFO = tuple[BTSCOPE btscope, BTSCOPES btscopes]; // Complete backtracking information

// Getters on backtracking scopes

str getEnter(BTSCOPE btscope) = btscope.enter;
str getResume(BTSCOPE btscope) = btscope.resume;
str getFail(BTSCOPE btscope) = btscope.\fail;

str getEnter(Tree t, BTSCOPES btscopes) = btscopes[getLoc(t)].enter;
str getEnter(loc l, BTSCOPES btscopes)  = btscopes[l].enter;

str getResume(Tree t, BTSCOPES btscopes) = btscopes[getLoc(t)].resume;
str getResume(loc l, BTSCOPES btscopes)  = btscopes[l].resume;

str getFail(Tree t, BTSCOPES btscopes) = btscopes[getLoc(t)].\fail;
str getFail(loc l, BTSCOPES btscopes)  = btscopes[l].\fail;

BTINFO registerBTScope(Tree t, BTSCOPE btscope, BTSCOPES btscopes){
    btscopes[getLoc(t)] = btscope;
    return <btscope, btscopes>;
}

BTSCOPES getBTScopes(Pattern p, str enter) = getBTInfo(p, <enter, enter, enter>, ()).btscopes;
BTSCOPES getBTScopes(Pattern p, str enter, BTSCOPES btscopes) = getBTInfo(p, <enter, enter, enter>, btscopes).btscopes;

/*********************************************************************/
/*                  Patterns                                         */
/*********************************************************************/

// ==== literal pattern =======================================================

default MuExp translatePat(p:(Pattern) `<Literal lit>`, AType subjectType, MuExp subject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) = translateLitPat(lit, subjectType, subject, btscopes, trueCont, falseCont);

MuExp translatePat(Literal lit, AType subjectType, MuExp subject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) = translateLitPat(lit, subjectType, subject, btscopes, trueCont, falseCont);

MuExp translateLitPat(Literal lit, AType subjectType, MuExp subject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) = muIfelse(muEqual(translate(lit), subject), trueCont, falseCont);

// ==== regexp pattern ========================================================

MuExp translatePat(p:(Pattern) `<RegExpLiteral r>`, AType subjectType, MuExp subject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) 
    = translateRegExpLiteral(r, subjectType, subject, btscopes, trueCont, falseCont);

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

MuExp translateRegExpLiteral(re: (RegExpLiteral) `/<RegExp* rexps>/<RegExpModifier modifier>`, AType subjectType, MuExp subject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
   str fuid = topFunctionScope();
   <buildRegExp,vars> = processRegExpLiteral(re);
   matcher = muTmpMatcher(nextTmp("matcher"), fuid);
   code = [ muConInit(matcher, muRegExpCompile(buildRegExp, subject)),
            muWhileDo("", muRegExpFind(matcher),
                        muBlock([ *[ muVarInit(vars[i], muRegExpGroup(matcher, i+1)) | i <- index(vars) ],
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
        	  fragmentCode += [ muCallPrim3("str_escape_for_regexp", astr(), [ translate(name) ], r@\loc)];
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
       buildRegExp = muValueBlock(astr(),
                                  muConInit(swriter, muCallPrim3("open_string_writer", astr(), [], [], e@\loc)) + 
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

// ==== concrete syntax pattern ===============================================
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
     
// ==== qualified name pattern ===============================================

MuExp translatePat(p:(Pattern) `<QualifiedName name>`, AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont, bool subjectAssigned=false){
   if("<name>" == "_"){
      return trueCont;
   }
   println("qualified name: <name>, <name@\loc>");
   var = mkVar(prettyPrintName(name), name@\loc);
   if(isDefinition(name@\loc) && !subjectAssigned){
    return muBlock([muVarInit(var, subjectExp), trueCont]);
   } else {
    return muIfelse(muIsInitialized(var), muIfelse(muEqual(var, subjectExp), trueCont, falseCont),
                                          muBlock([ muAssign(var, subjectExp), trueCont ]));
   }
} 

// ==== typed name pattern ====================================================
     
MuExp translatePat(p:(Pattern) `<Type tp> <Name name>`, AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont, bool subjectAssigned=false){
   trType = translateType(tp);
   if(asubtype(subjectType, trType)){
	   if("<name>" == "_"|| subjectAssigned){
	      return trueCont;
	   }
	   ppname = prettyPrintName(name);
	   <fuid, pos> = getVariableScope(ppname, name@\loc);
	   var = muVar(prettyPrintName(name), fuid, pos, trType[label=ppname]);
	   return var == subjectExp ? trueCont : muBlock([muVarInit(var, subjectExp), trueCont]);
   }
   if("<name>" == "_" || subjectAssigned){
      return muIfelse(muValueIsSubType(subjectExp, trType), trueCont, falseCont);
   }
   ppname = prettyPrintName(name);
   <fuid, pos> = getVariableScope(ppname, name@\loc);
   var = muVar(prettyPrintName(name), fuid, pos, trType[label=ppname]);
   return var == subjectExp ? muIfelse(muValueIsSubType(subjectExp, trType), trueCont, falseCont)
                            : muIfelse(muValueIsSubType(subjectExp, trType), muBlock([muVarInit(var, subjectExp), trueCont]), falseCont);
}  

// ==== reified type pattern ==================================================
//TODO
MuExp translatePat(p:(Pattern) `type ( <Pattern symbol> , <Pattern definitions> )`, AType subjectType) {    
    return muApply(mkCallToLibFun("Library","MATCH_REIFIED_TYPE"), [muCon(symbol)]);
}

// ==== call or tree pattern ==================================================

// ---- getBTInfo

BTINFO getBTInfo(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, BTSCOPE btscope, BTSCOPES btscopes) {
    //enter1 =  btscope.enter; //contains(btscope.enter, "_TPAT") ? btscope.enter : "<btscope.enter>_TPAT";
    //enter1 = backtrackFree(p) ? btscope.enter : "<btscope.enter>_CALL";
    enter1 = "<btscope.enter>_CALL";
    resume1 = btscope.resume;
    fail1 = btscope.resume;
    BTSCOPE btscope1 = <enter1, resume1, fail1>;
    <btscope1, btscopes> = getBTInfo(expression, btscope1, btscopes);
    for(pat <- arguments){
        <btscope1, btscopes> = getBTInfo(pat, btscope1, btscopes);
    }
    //if(keywordArguments is \default){
    //    for(kwpat <- keywordArguments.keywordArgumentList){
    //        <btscope1, btscopes> = getBTInfo(kwpat, btscope1, btscopes);
    //    }
    //}
    println("<p>");
    return registerBTScope(p, <enter1, btscope1.resume, btscope.resume>, btscopes);
}

MuExp translatePat(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont, bool subjectAssigned=false) {
   str fuid = topFunctionScope();
   subject = muTmpIValue(nextTmp("subject"), fuid, subjectType);
   contExp = trueCont;
   
   if((KeywordArguments[Pattern]) `<OptionalComma optionalComma> <{KeywordArgument[Pattern] ","}+ keywordArgumentList>` := keywordArguments){
        for(kwarg <- keywordArgumentList){
            kwname = prettyPrintName(kwarg.name);
            contExp = translatePat(kwarg.expression, getType(kwarg.expression), muGetKwp(subject, subjectType, kwname), btscopes, contExp, falseCont);                 
        }
   }
   
   lpats = [pat | pat <- arguments];   //TODO: should be unnnecessary
   //TODO: bound check
   body =  ( contExp | translatePat(lpats[i], getType(lpats[i]), muSubscript(subject, muCon(i)), btscopes, it, falseCont) | int i <- reverse(index(lpats)) );
   expType = getType(expression);
   subjectInit = subjectAssigned ? [] : muConInit(subject, subjectExp);
   if(expression is qualifiedName){
      fun_name = getUnqualifiedName(prettyPrintName(getType(expression).label));
      return muBlock([*subjectInit, muIfelse(muHasNameAndArity(subjectType, expType, muCon(fun_name), size(lpats), subject), body, falseCont)]);
   } else if(expression is literal){ // StringConstant
         fun_name = prettyPrintName("<expression>"[1..-1]);
         return muBlock([*subjectInit, muIfelse(muHasNameAndArity(subjectType, expType, muCon(fun_name), size(lpats), subject), body, falseCont)]);
    } else {
     fun_name_subject = muTmpIValue(nextTmp("fun_name_subject"), fuid, expType);
     return muBlock([*subjectInit,
                     muConInit(fun_name_subject, muCallPrim3("get_anode_name", astr(), [anode([])], [subject], getLoc(expression))),
                     translatePat(expression, expType, fun_name_subject, btscope, 
                                  muIfelse(muHasNameAndArity(subjectType, expType, fun_name_subject, size(lpats), subject), body, falseCont),  
                                  falseCont,
                                  subjectAssigned=true)
                    ]);
                  
   }
}

 MuExp translatePatKWArguments((KeywordArguments[Pattern]) ``, AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont, bool subjectAssigned=false)
    = trueCont;
 
 MuExp translatePatKWArguments((KeywordArguments[Pattern]) ``, AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont, bool subjectAssigned=false){
    code = trueCont;
    for(kwarg <- keywordArgumentList){
        kwtype = getType(kwarg.expression);
        kwfield = "<kwarg.name>";
        code = muIfelse(muHasKwp(subject, kwfield),
                        muIfelse(muEqual(muGetKwp(subject, kwtype, kwfield), translate(kwarg.expression)), code, falseCont),
                        falseCont);
   }
   return code;
 }

// ==== set pattern ===========================================================

// ---- getBTInfo
    
BTINFO getBTInfo(p:(Pattern) `{<{Pattern ","}* pats>}`,  BTSCOPE btscope, BTSCOPES btscopes){
    <fixedLiterals, toBeMatchedPats, fixedVars, fixedMultiVars, leftMostVar> = analyzeSetPattern(p);
   
    enterFirst = "<btscope.enter>_SET";
    resumeFirst = btscope.resume;
    failFirst = btscope.resume;
    BTSCOPE btscopeLast = <enterFirst, resumeFirst, failFirst>;
    btscopesAll = btscopes;
    for(pat <- pats){
        <btscopeLast, btscopesAll> = getBTInfoListOrSet(pat, btscopeLast, btscopesAll);
    }
    return registerBTScope(p, <enterFirst, btscopeLast.resume, btscope.resume>, btscopesAll);
}

// ---- translate set pattern

MuExp translatePat(p:(Pattern) `{<{Pattern ","}* pats>}`, AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont, bool subjectAssigned=false) 
    = translateSetPat(p, subjectType, subjectExp, btscopes, trueCont, falseCont, subjectAssigned=subjectAssigned);

// Translate patterns as element of a set pattern

str isLast(bool b) = b ? "LAST_" : "";

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>`, bool last, AType elmType, MuExp subject, MuExp prevSubject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
    return translateVarAsSetElem(mkVar(p), isDefinition(p), p@\loc, last, elmType, subject, prevSubject, btscopes, trueCont, falseCont);
}

MuExp translatePatAsSetElem(p:(Pattern) `<Type tp> <Name name>`, bool last, AType elmType, MuExp subject, MuExp prevSubject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
    return translateVarAsSetElem(mkVar(p), isDefinition(p), p@\loc, last, elmType, subject, prevSubject, btscopes, trueCont, falseCont);
}

MuExp translateVarAsSetElem(MuExp var, bool isDefinition, loc patloc, bool last, AType elmType, MuExp subject, MuExp prevSubject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
   fuid = topFunctionScope();
   elem = muTmpIValue(nextTmp("elem"), fuid, elmType);
   isWildCard = var.name == "_";
   
   my_btscope = btscopes[patloc];
   code = muBlock([]);
  
   if(isWildCard){
       code = muForAll(my_btscope.enter, elem, aset(elmType), prevSubject,
                       muBlock([ muConInit(subject, muCallPrim3("delete", aset(elmType), [aset(elmType), elmType], [prevSubject, elem], patloc)),
                                  trueCont
                               ]));     
   } else {
	   trueBlock = muBlock([ muConInit(subject, muCallPrim3("delete", aset(elmType), [aset(elmType), elmType], [prevSubject, var], patloc)),
                             trueCont
                           ]);
	   if(isDefinition){
	       code = muForAll(my_btscope.enter, elem, aset(elmType), prevSubject,
	                       muBlock([ *((isUsed(var, trueCont) || !last) ? [muVarInit(var, elem)] : []),
	                                 trueBlock
	                               ]));
	   } else {
	       code = muForAll(my_btscope.enter, elem, aset(elmType), prevSubject,
	                       muIfelse(muIsInitialized(var), muIf(muEqual(elem, var), trueBlock),
	                                                      muBlock([ muAssign(var, elem), trueBlock ])));
	   }
   }
   
   if(!asubtype(elmType, var.atype)){
        code = muIfelse(muValueIsSubType(prevSubject, aset(var.atype)), code, muFail(getFail(patloc, btscopes)));
   }
   
   return muIf(//last ? muEqualNativeInt(muSize(prevSubject, aset(elmType)), muCon(1)) 
                            muGreaterEqNativeInt(muSize(prevSubject, aset(elmType)), muCon(1)), 
                   code
                   //, falseCont
                   );
} 

MuExp translatePatAsSetElem(p:(Pattern) `_*`, bool last, AType elmType, MuExp subject, MuExp prevSubject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    return translateMultiVarAsSetElem(mkVar(p), false, p@\loc, last, elmType, subject, prevSubject, btscopes, trueCont, falseCont);
}

MuExp translatePatAsSetElem(p:(Pattern) `*_`, bool last, AType elmType, MuExp subject, MuExp prevSubject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    return translateMultiVarAsSetElem(mkVar(p), false, p@\loc, last, elmType, subject, prevSubject, btscopes, trueCont, falseCont);
}

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>*`, bool last, AType elmType, MuExp subject, MuExp prevSubject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
    return translateMultiVarAsSetElem(mkVar(p), isDefinition(name@\loc), p@\loc, last, elmType, subject, prevSubject, btscopes, trueCont, falseCont);  
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Name name>`, bool last, AType elmType, MuExp subject, MuExp prevSubject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
    return translateMultiVarAsSetElem(mkVar(p), isDefinition(name@\loc), p@\loc, last, elmType, subject, prevSubject, btscopes, trueCont, falseCont); 
 }

MuExp translatePatAsSetElem(p:(Pattern) `*<Type tp>  _`, bool last, AType elmType, MuExp subject, MuExp prevSubject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
   return translateMultiVarAsSetElem(mkVar(p), false, p@\loc, last, elmType, subject, prevSubject, btscopes, trueCont, falseCont);
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Type tp> <Name name>`, bool last, AType elmType, MuExp subject, MuExp prevSubject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
   return translateMultiVarAsSetElem(mkVar(p), true, p@\loc, last, elmType, subject, prevSubject, btscopes, trueCont, falseCont);
}

MuExp translateMultiVarAsSetElem(MuExp var, bool isDefinition, loc patsrc, bool last, AType elmType, MuExp subject, MuExp prevSubject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
   fuid = topFunctionScope();
   elem = muTmpIValue(nextTmp("elem"), fuid, aset(var.atype));
  
   my_btscope = btscopes[patsrc];
   code = muBlock([]);

   isWildCard = var.name == "_"; 
  
   if(isWildCard){
        code = muForAll(my_btscope.enter, elem, aset(elmType), muCallPrim3("subsets", aset(elmType), [aset(elmType)], [prevSubject], patsrc),
                   muBlock([ muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), aset(elmType)], [prevSubject, elem], patsrc)),
                             trueCont
                           ]));
    } else {
        if(isDefinition){
            code = muForAll(my_btscope.enter, elem, aset(elmType), muCallPrim3("subsets", aset(elmType), [aset(elmType)], [prevSubject], patsrc),
                    muBlock([ muVarInit(var, elem),
                              muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), aset(elmType)], [prevSubject, elem], patsrc)),
                              trueCont
                           ]));
        } else {
            trueBlock = muBlock([ muConInit(subject, muCallPrim3("subtract", aset(elmType), [aset(elmType), aset(elmType)], [prevSubject, elem], patsrc)),
                                  trueCont
                                 ]);
            initialized = muTmpBool("initialized", fuid);   
                 
            code = muBlock([ muConInit(initialized, muIsInitialized(var)),
                             muForAll(my_btscope.enter, elem, aset(elmType), muCallPrim3("subsets", aset(elmType), [aset(elmType)], [prevSubject], patsrc),
                                      muBlock([ muIfelse(initialized, muIf(muEqual(elem, var),  trueBlock),
                                                             muBlock([ muAssign(var, elem), trueBlock]))
                                              ]))
                           ]);
        }
    }
    
    if(!asubtype(aset(elmType), var.atype) ){
        code = muIf(muValueIsSubType(prevSubject, var.atype), 
                    code
                    //, muFail(getFail(my_btscope))
                    );
    } 
        
   return code;
}

MuExp translatePatAsSetElem(p:(Pattern) `+<Pattern argument>`, bool last, AType elmType, MuExp subject, MuExp prevSubject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
  throw "splicePlus pattern <p>";
}   

default MuExp translatePatAsSetElem(Pattern p, bool last, AType elmType, MuExp subject, MuExp prevSubject, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
  try {
        pcon = muCon(translatePatternAsConstant(p));
        return muIfelse(muCallPrim3("in", abool(), [elmType, aset(elmType)], [pcon, prevSubject], p@\loc),
                        muBlock([ muConInit(subject, muCallPrim3("delete", aset(elmType), [aset(elmType), elmType], [prevSubject, pcon], p@\loc)),
                                  trueCont ]),
                        //muBlock([ *(last ? [] : [muConInit(subject, muCallPrim3("delete", aset(elmType), [aset(elmType), elmType], [prevSubject, pcon], p@\loc))]),
                        //          trueCont ]),
                         /*muFail(my_btscope)*/falseCont);                            
  } catch: {
        str fuid = topFunctionScope();
        elem = muTmpIValue(nextTmp("elem"), fuid, elmType);
        my_btscope = btscopes[getLoc(p)];
        // TODO length check?
        code = muForAll(my_btscope.enter, elem, aset(elmType), prevSubject,
                        translatePat(p, elmType, elem, btscopes, 
                            muBlock([ muConInit(subject, muCallPrim3("delete", aset(elmType), [aset(elmType), elmType], [prevSubject, elem], p@\loc)),
                                      trueCont ]),
                            //muBlock([ *(last ? [] : [muConInit(subject, muCallPrim3("delete", aset(elmType), [aset(elmType), elmType], [prevSubject, elem], p@\loc))]),
                            //        trueCont ]),
                            muFail(my_btscope.\fail)/*falseCont*/
                            ));
      
        return code;
  }
}

/*
 * Get the name of a pattern at position k, when no name, return "_<k>".
 */
private str getName(Pattern pat, int k){
  if(pat is splice){
     arg = pat.argument;
     return arg is qualifiedName ? prettyPrintName(arg.qualifiedName) : prettyPrintName(arg.name);
  } else if(pat is multiVariable){
    return prettyPrintName(pat.qualifiedName); 
  } else if(pat is qualifiedName){
    return prettyPrintName(pat.qualifiedName);  
  } else if(pat is typedVariable){
    return prettyPrintName(pat.name);
  } else {
    return "_<k>";
  } 
}

private bool isDefinition(Pattern pat){
  if(pat is splice){
     return isDefinition(pat.argument);
  } else if(pat is multiVariable){
    return "<pat.qualifiedName>" == "_" || isDefinition(pat.qualifiedName@\loc); 
  } else if(pat is qualifiedName){
    return "<pat>" == "_" || isDefinition(pat.qualifiedName@\loc);  
  } else if(pat is typedVariable){
    return true;
  } else 
    return false;
}

private bool isDefinedOutsidePat(loc def, Pattern container){
    try {
        defined = getDefinition(def); 
        return !isContainedIn(defined, container@\loc);
    } catch: {
         return false;
    }
}

private bool allVarsDefinedOutsidePat(Pattern pat, Pattern container){
  if(pat is splice){
     return allVarsDefinedOutsidePat(pat.argument, container);
  } else if(pat is multiVariable){
        if("<pat.qualifiedName>" == "_") return false;
        return isDefinedOutsidePat(pat.qualifiedName@\loc, container);
  } else if(pat is qualifiedName){
        if("<pat>" == "_") return false;
        return isDefinedOutsidePat(pat.qualifiedName@\loc, container);  
  } else if(pat is typedVariable){
    return false;
  } else {
    bool found = true;
    visit(pat){
        case (Pattern) `<QualifiedName qualifiedName>`:  found = found && isDefinedOutsidePat(qualifiedName@\loc, container);
        case (Pattern) `<QualifiedName qualifiedName>*`: found = found && isDefinedOutsidePat(qualifiedName@\loc, container);
    }
    return found;
    }
}

private MuExp mkVar(Pattern pat){
   if(pat is splice){
     return mkVar(pat.argument);
  } else if(pat is multiVariable){
        if("<pat.qualifiedName>" == "_"){
            return muVar("_", topFunctionScope(), -1, avalue());
        } else {
            return mkVar("<pat.qualifiedName>", pat.qualifiedName@\loc);
        }
  } else if(pat is qualifiedName){
        if("<pat>" == "_"){
             return muVar("_", topFunctionScope(), -1, avalue());
        } else {
            return mkVar("<pat>", pat@\loc);
        }
  } else if(pat is typedVariable){
        if("<pat.name>" == "_"){
             return muVar("_", topFunctionScope(), -1, avalue());
         } else {
            return mkVar("<pat.name>", pat.name@\loc);
         }
  } else 
    throw "mkVar: <pat>";
}

tuple[list[MuExp] literals, list[Pattern] toBeMatched, list[Pattern] vars, list[Pattern] multiVars, int leftMostVar] analyzeSetPattern(p:(Pattern) `{<{Pattern ","}* pats>}`){
   list[Pattern] lpats = [pat | pat <- pats]; // TODO: unnnecessary

   /* collect literals and already defined vars/multivars; also remove patterns with duplicate names */
   fixedLiterals = [];                  // constant elements in the set pattern
   list[Pattern] toBeMatchedPats = [];  // the list of patterns that will ultimately be matched
   fixedVars = [];                      // var pattern elements with already (previosuly) defined value
   fixedMultiVars = [];                 // multi-var pattern elements with already (previosuly) defined value
   int leftMostVar = -1;                // index of leftmost multi-variable
   
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
              if(pat is literal){
                fixedLiterals += isConstant(pat.literal) ? muCon(getLiteralValue(pat.literal)) : translate(pat.literal);
              } else if(pat is splice || pat is multiVariable){
                if(allVarsDefinedOutsidePat(pat, p)){
                    fixedMultiVars += pat;
                } else {
                    if(leftMostVar == -1) leftMostVar = size(toBeMatchedPats);
                    toBeMatchedPats += pat;
                }
              } else if(pat is qualifiedName){
                if(allVarsDefinedOutsidePat(pat, p)){
                    fixedVars += pat;
               } else {
                    if(leftMostVar == -1) leftMostVar = size(toBeMatchedPats);
                    toBeMatchedPats += pat;               
                }
              } else if(pat is typedVariable){
                    if(leftMostVar == -1) leftMostVar = size(toBeMatchedPats);
                    toBeMatchedPats += pat;
              } else { 
                try {
                    fixedLiterals += muCon(translatePatternAsConstant(pat));
                } catch: {
                    toBeMatchedPats += pat;
                }
              }
           } 
   return <fixedLiterals, toBeMatchedPats, fixedVars, fixedMultiVars, leftMostVar>;
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

MuExp translateSetPat(p:(Pattern) `{<{Pattern ","}* pats>}`, AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont, bool subjectAssigned=false) {
  
   //list[Pattern] lpats = [pat | pat <- pats]; // TODO: unnnecessary
   elmType = (aset(tp) := subjectType && tp != avoid()) ? tp : avalue();
   typecheckNeeded = !asubtype(getType(p), subjectType);
   my_btscope = btscopes[getLoc(p)];
   btscope = my_btscope.enter;
   
   fixedLiterals = [];                  // constant elements in the set pattern
   list[Pattern] toBeMatchedPats = [];  // the list of patterns that will ultimately be matched
   list[Pattern] fixedVars = [];        // var pattern elements with already (previosuly) defined value
   list[Pattern] fixedMultiVars = [];   // multi-var pattern elements with already (previosuly) defined value
   int leftMostVar = -1;                // index of leftmost multi-variable
   
   <fixedLiterals, toBeMatchedPats, fixedVars, fixedMultiVars, leftMostVar> = analyzeSetPattern(p);
   rightMostPat = size(toBeMatchedPats) - 1;
   
   str fuid = topFunctionScope();
   subject = muTmpIValue(nextTmp("subject"), fuid, subjectType); // <<< type?
   fixed = muTmpIValue(nextTmp("fixed"), fuid, subjectType);     // <<<
   subjects = [ muTmpIValue(nextTmp("subject"), fuid, subjectType) | int i <- reverse(index(toBeMatchedPats)) ];
   
   for(int i <- index(toBeMatchedPats)){
        println("<i>: <toBeMatchedPats[i]> =\> <subjects[i]>");
   }
   
   MuExp fixedParts = muCon({con | muCon(value con) <- fixedLiterals });
    
   for(vp <- fixedVars){
       fixedParts = muCallPrim3("add", aset(elmType), [aset(elmType), elmType], [fixedParts, mkVar(vp)], p@\loc);
   }
   for(vp <- fixedMultiVars){
       fixedParts = muCallPrim3("add", aset(elmType), [aset(elmType), aset(elmType)], [fixedParts, mkVar(vp)], p@\loc);
   }
   subject_minus_fixed = muCallPrim3("subtract", aset(elmType), [aset(elmType), aset(elmType)], [subject, fixed], p@\loc);
   
   MuExp setPatTrueCont =
        isEmpty(subjects) ? ( ( isEmpty(fixedLiterals) && isEmpty(fixedVars) && isEmpty(fixedMultiVars) )
                            ? muIfExp(muEqualNativeInt(muSize(subject, aset(avalue())), muCon(0)), trueCont,  muFail(getFail(my_btscope)))
                            : muIfExp(muEqualNativeInt(muSize(subject_minus_fixed, aset(avalue())), muCon(0)), trueCont, muFail(getFail(my_btscope)))
                            )
                          : muIfExp(muEqualNativeInt(muSize(subjects[-1], aset(avalue())), muCon(0)), trueCont,  muFail(getResume(my_btscope)))
                          ;
   
   for(int i <- reverse(index(toBeMatchedPats))){
      pat = toBeMatchedPats[i];
      isRightMostPat = (i == rightMostPat);
      currentSubject = subjects[i];
      previousSubject = (i == 0) ? subject : subjects[i-1];
      println("i = <i>, pat = <pat>, prevSubject = <previousSubject>, currentSubject = <currentSubject>");
      setPatTrueCont = translatePatAsSetElem(pat, isRightMostPat, elmType, currentSubject, previousSubject, btscopes, setPatTrueCont, muFail(getFail(my_btscope)));
   }
   
   block = muBlock([]);
   if(isEmpty(fixedLiterals) && isEmpty(fixedVars) && isEmpty(fixedMultiVars)){
        block = setPatTrueCont;
   } else {
        block = muBlock([ muConInit(fixed, fixedParts),
                          muIfelse(muCallPrim3("subset", aset(elmType), [aset(elmType), aset(elmType)], [fixed, subject], p@\loc),
                                   muBlock([ *(leftMostVar < 0 ? [] : [leftMostVar == 0  ? muAssign(subject, subject_minus_fixed) : muConInit(subjects[leftMostVar-1], subject)]),
                                             setPatTrueCont]),
                                   muFail(getFail(my_btscope)))
                        ]);
   }
   iprintln(block);
   //block = updateBTScope(muEnter(my_btscope.enter, block), btscope, getResumptionScope(my_btscope));
   iprintln(block);
   return muBlock([ muVarInit(subject, subjectExp),
                    *( typecheckNeeded ? [muIf( muValueIsSubType(subject, subjectType),
                                                    block)
                                                    //, falseCont
                                         ]
                                       : [ block
                                          //, falseCont 
                                          ])
                       ]);
}

// ==== tuple pattern =========================================================

// ---- getBTInfo

BTINFO getBTInfo(p:(Pattern) `\<<{Pattern ","}* pats>\>`, BTSCOPE btscope, BTSCOPES btscopes) {
    enterFirst = "<btscope.enter>_TUP";
    resumeFirst = btscope.resume;
    failFirst = btscope.resume;
    BTSCOPE btscopeLast = <enterFirst, resumeFirst, failFirst>;
    btscopesAll = btscopes;
    for(pat <- pats){
        <btscopeLast, btscopesAll> = getBTInfo(pat, btscopeLast, btscopesAll);
    }
    return registerBTScope(p, <enterFirst, btscopeLast.resume, btscope.resume>, btscopes);
}

// ---- translate tuple pattern

MuExp translatePat(p:(Pattern) `\<<{Pattern ","}* pats>\>`, AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
    lpats = [pat | pat <- pats];   //TODO: should be unnnecessary
    elmTypes = [getType(pat) | pat <- lpats];
    patType = atuple(atypeList(elmTypes));
    
    str fuid = topFunctionScope();
    subject = muTmpIValue(nextTmp("tuple_subject"), fuid, subjectType);
    //TODO: bound check
    body =  ( trueCont | translatePat(lpats[i], elmTypes[i], muSubscript(subject, muCon(i)), btscopes, it, falseCont) | int i <- reverse(index(lpats)) ); // TODO only first with btscope?
    code = [ muConInit(subject, subjectExp), muIfelse(muHasTypeAndArity(patType, size(lpats), subject), body, falseCont)];
    return muBlock(code);
}

// ==== list pattern ==========================================================

//  List pattern [L0, L1, ..., Ln]
//                                                 +-----------+
//            +----------------------------------->| falseCont |
//            |                                    +-----------+
//            |
//        +--F--+----------------------------------------------+
//        |     |                                              |
//  ----->| L0  R <------+                                     |
//        |     |        |                                     |
//        |-----+        |                                     |
//        |  |        +--F--+-------------------------------+  |
//        |  |        |     |                               |  |
//        |  +------->| L1  R <------+                      |  |
//        |           |     |        |                      |  |
//        |           +-----+        |                      |  |
//        |           |  |           |                      |  |
//        |           |  |    ...    |                      |  |
//        |           |  |        +--F--+----------------+  |  |
//        |           |  |        |     |                |  |  |
//        |           |  +------->| Ln  R <------+       |  |  |
//        |           |           |     |        |       |  |  |
//        |           |           +-----+        |       |  |  |
//        |           |           |  |           |       |  |  |
//        |           |           |  |     +-----F----+  |  |  |
//        |           |           |  +---->| trueCont |  |  |  |
//        |           |           |        +----------+  |  |  |
//        |           |           +----------------------+  |  |
//        |           |                                     |  |
//        |           +-------------------------------------+  |
//        |                                                    |
//        +----------------------------------------------------+    

// ---- getBTInfo 

BTINFO getBTInfo(p:(Pattern) `[<{Pattern ","}* pats>]`,  BTSCOPE btscope, BTSCOPES btscopes){
    enterFirst = "<btscope.enter>_LIST";
    resumeFirst = btscope.resume;
    failFirst = btscope.resume;
    BTSCOPE btscopeLast = <enterFirst, resumeFirst, failFirst>;
    btscopesAll = btscopes;
    for(pat <- pats){
        <btscopeLast, btscopesAll> = getBTInfoListOrSet(pat, btscopeLast, btscopesAll);
    }
    return registerBTScope(p, <enterFirst, btscopeLast.resume, btscope.resume>, btscopesAll);
}

str nameSuffix(str s, Name name){
    sname = "<name>";
    return sname == "_" ? "_<s><nextTmp(sname)>" : "_<s>_<sname>";
}

str nameSuffix(str s, QualifiedName name){
    sname = "<name>";
    return sname == "_" ? "_<s><nextTmp(sname)>" : "_<s>_<sname>";
}

BTINFO getBTInfoListOrSet(p:(Pattern) `<QualifiedName name>`, BTSCOPE btscope, BTSCOPES btscopes){
    btscope.enter += nameSuffix("VAR", name); //"_VAR_<name>";
    return registerBTScope(p, btscope, btscopes);
}    
BTINFO getBTInfoListOrSet(p:(Pattern) `<Type tp> <Name name>`, BTSCOPE btscope, BTSCOPES btscopes) {
    btscope.enter += nameSuffix("VAR", name); ; //"_VAR_<name>";
    return registerBTScope(p, btscope, btscopes);
}
    
BTINFO getBTInfoListOrSet(p:(Pattern) `<Literal lit>`, BTSCOPE btscope, BTSCOPES btscopes)
    = registerBTScope(p, btscope, btscopes);

BTINFO getBTInfoListOrSet(p:(Pattern) `<QualifiedName name>*`, BTSCOPE btscope, BTSCOPES btscopes) {
    enter1 = btscope.enter + nameSuffix("MVAR", name); ; //"_MVAR_<name>";
    resume1 = enter1;
    fail1 = btscope.resume;
    return registerBTScope(p, <enter1, resume1, fail1>, btscopes);
}

BTINFO getBTInfoListOrSet(p:(Pattern) `*<Name name>`, BTSCOPE btscope, BTSCOPES btscopes) {
    enter1 = btscope.enter + nameSuffix("MVAR", name); ; //"_MVAR_<name>";
    resume1 = enter1;
    fail1 = btscope.resume;
    return registerBTScope(p, <enter1, resume1, fail1>, btscopes);
}

BTINFO getBTInfoListOrSet(p:(Pattern) `*<Type tp> <Name name>`, BTSCOPE btscope, BTSCOPES btscopes) {
    enter1 = btscope.enter + nameSuffix("MVAR", name); ; //"_MVAR_<name>";
    resume1 = enter1;
    fail1 = btscope.resume;
    return registerBTScope(p, <enter1, resume1, fail1>, btscopes);
}

default BTINFO getBTInfoListOrSet(Pattern p, BTSCOPE btscope, BTSCOPES btscopes) = getBTInfo(p, btscope, btscopes);  

// ---- translate list pattern

MuExp translatePat(p:(Pattern) `[<{Pattern ","}* pats>]`, AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont, bool subjectAssigned=false) {
    iprintln(btscopes);
    lookahead = computeLookahead(p);  
    lpats = [pat | pat <- pats];   //TODO: should be unnnecessary
    npats = size(lpats);
    elmType = avalue();
    if(alist(tp) := subjectType && tp != avoid()){
    	elmType = tp;
    }
    
    if(npats == 0){
        return muIfExp(muEqualNativeInt(muSize(subjectExp, alist(elmType)), muCon(0)), trueCont, falseCont);
    }
    
    str fuid = topFunctionScope();
    
    subject = muTmpIValue(nextTmp("subject"), fuid, alist(elmType));
    cursor = muTmpInt(nextTmp("cursor"), fuid);
    sublen = muTmpInt(nextTmp("sublen"), fuid);
    typecheckNeeded = asubtype(getType(p), subjectType);
  
    iprintln(trueCont);
    trueCont = muIfelse(muEqualNativeInt(cursor, sublen), trueCont, muFail(getResume(lpats[-1], btscopes)));
    for(i <- reverse(index(lpats))){
        println(btscopes[getLoc(lpats[i])]);
        trueCont = translatePatAsListElem(lpats[i], lookahead[i], subjectType, subject, sublen, cursor, btscopes, trueCont, i == 0 ? falseCont : muFail(getFail(lpats[i], btscopes)));
        iprintln(trueCont);
    }
    
    body = trueCont;
    
    i_am_multivar = !isEmpty(lookahead) && isMultiVar(lpats[0]);
    size_test = isEmpty(lookahead) ? muEqualNativeInt(sublen, muCon(0))
                                   : (lookahead[0].nMultiVar == 0 && !i_am_multivar) ? muEqualNativeInt(sublen, muCon(lookahead[0].nElem + 1))                                                   
                                                                                     : muGreaterEqNativeInt(sublen, muCon(lookahead[0].nElem + (i_am_multivar ? 0 : 1)));      
    block = muBlock([ muConInit(sublen, muSize(subject, alist(elmType))),
                      muIf(size_test, 
                               body
                               //, muFail(getFail(p, btscopes))) // <<<<<<<<<<<
                               //, falseCont
                               )
                    ]);
    iprintln(block);
    code = muBlock([ *(subjectAssigned ? [muVarInit(subject, subjectExp)] : [muConInit(subject, subjectExp)]),   
                     muVarInit(cursor, muCon(0)), 
                     *(typecheckNeeded ? [muIf( muValueIsSubType(subject, subjectType),
                                                    block)]                                                  
                                       : [ block ])
                     ]);
    iprintln(code);
    return code;
}

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

MuExp translatePatAsListElem(p:(Pattern) `<QualifiedName name>`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
    if("<name>" == "_"){
       return muIf(muLessNativeInt(cursor, sublen),
                   muBlock([ muIncNativeInt(cursor, muCon(1)), 
                             trueCont
                           ]));
    }
    var = mkVar(prettyPrintName(name), name@\loc);
    if(isDefinition(name@\loc)){
        return muIf(muLessNativeInt(cursor, sublen),
                    muBlock([ muVarInit(var, muSubscript(subject, cursor)),
                              muIncNativeInt(cursor, muCon(1)), 
                              trueCont
                            ]));
                  
    } else {
        return muIf(muLessNativeInt(cursor, sublen),
                    muIfelse(muIsInitialized(var), 
                             muIf(muEqual(var, muSubscript(subject, cursor)),
                                  muBlock([ muIncNativeInt(cursor, muCon(1)), 
                                            trueCont
                                          ])),
                             muBlock([ muAssign(var, muSubscript(subject, cursor)), 
                                       muIncNativeInt(cursor, muCon(1)), 
                                       trueCont
                                     ])));
    }
} 

MuExp translatePatAsListElem(p:(Pattern) `<Type tp> <Name name>`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
   trType = translateType(tp);
  
   if("<name>" == "_"){
      code = muIf(muLessNativeInt(cursor, sublen),
                  muBlock([ muIncNativeInt(cursor, muCon(1)), 
                            trueCont 
                          ]));
      if(!asubtype(subjectType, alist(trType))){
            code = muIf(muValueIsSubType(subject, trType), code);
      }
      return code;
   } else {
       var = mkVar(prettyPrintName(name), name@\loc);
       var.atype = getType(tp);
       check = muLessNativeInt(cursor, sublen);
       if(!asubtype(subjectType, alist(trType))){ 
          check = muAndNativeBool(check, muValueIsSubType(muSubscript(subject, cursor), trType));
       }
       
       code = muIfelse(check,
                   muBlock([ muVarInit(var, muSubscript(subject, cursor)),
                             muIncNativeInt(cursor, muCon(1)), 
                             trueCont 
                           ])
                           , falseCont // <<<
                           );
       return code;
   }
 } 

MuExp translatePatAsListElem(p:(Pattern) `<Literal lit>`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
    if(lit is regExp) fail;
    return muBlock([ muIf(muAndNativeBool(muLessNativeInt(cursor, sublen), muEqual(translate(lit), muSubscript(subject, cursor))), 
                          muBlock([ muIncNativeInt(cursor, muCon(1)), 
                                    trueCont
                                  ])
                     //, falseCont // <<<<
                     )
                   ]);
}

// Multi variables

bool isUsed(MuExp var, MuExp exp){
    return true;
    nm = var.name;
    return /nm := exp; // In some cases, the type in the var can still be a type var, so only look for the var name;
}

MuExp translatePatAsListElem(p:(Pattern) `<QualifiedName name>*`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
    return translateMultiVarAsListElem(mkVar(p), isDefinition(name@\loc), lookahead, subjectType, subject, sublen, cursor, getEnter(p, btscopes), trueCont, falseCont);
}

MuExp translatePatAsListElem(p:(Pattern) `*<Name name>`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
    return translateMultiVarAsListElem(mkVar(p), isDefinition(name@\loc), lookahead, subjectType, subject, sublen, cursor, getEnter(p, btscopes), trueCont, falseCont);
} 

MuExp translatePatAsListElem(p:(Pattern) `*<Type tp> <Name name>`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
    return translateMultiVarAsListElem(mkVar(p), isDefinition(name@\loc), lookahead, subjectType, subject, sublen, cursor, getEnter(p, btscopes), trueCont, falseCont);
}

MuExp translatePatAsListElem(p:(Pattern) `+<Pattern argument>`, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
    throw "splicePlus pattern";
} 

// TODO: optimize last multivar in list pattern

MuExp translateMultiVarAsListElem(MuExp var, bool isDefinition, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, str enter, MuExp trueCont, MuExp falseCont) {
    fuid =  topFunctionScope();
    startcursor = muTmpInt(nextTmp("<var.name>_start"), fuid);
    len = muTmpInt(nextTmp("<var.name>_len"), fuid);
    prevlen = muTmpInt(nextTmp("<var.name>_prevlen"), fuid);
    //var.atype = alist(avalue()); // = muVar(prettyPrintName("<name>"), fuid, pos, alist(avalue()));
    varPresent = var.name != "_";
   
    code = muBlock([]);
    if(lookahead.nMultiVar == 0 && !(varPresent && isUsed(var, trueCont))){
        code = muBlock([ muConInit(startcursor, cursor), 
                         muConInit(len, muSubNativeInt(muSubNativeInt(sublen, startcursor), muCon(lookahead.nElem))),         
                         muAssign(cursor, muAddNativeInt(startcursor, len)),
                         muEnter(enter, trueCont)
                       ]);
    } else {
        if(isDefinition || !varPresent){
           code = muBlock([ muConInit(startcursor, cursor),
                            muForRangeInt(enter, len, 0, 1, muSubNativeInt(sublen, startcursor), 
                                          muBlock([ *( (varPresent && isUsed(var, trueCont)) ? [muConInit(var, muSubList(subject, startcursor, len))] : [] ),
                                                    muAssign(cursor, muAddNativeInt(startcursor, len)),
                                                    trueCont
                                                  ])),
                            falseCont   // <<<<
        
                          ]);
        } else {
           code = muBlock([ muConInit(startcursor, cursor),
                            muConInit(len, muSubNativeInt(muSubNativeInt(sublen, startcursor), muCon(lookahead.nElem))),   
                            muConInit(prevlen, muSize(var, subjectType)),
                            muIfelse(muGreaterEqNativeInt(len, prevlen), 
                                 muIfelse(muIsInitialized(var),
                                          muIf(muEqual(var, muSubList(subject, startcursor, prevlen)),
                                               muBlock([ muAssign(cursor, muAddNativeInt(startcursor, prevlen)),
                                                         trueCont
                                                       ])),
                                          muBlock([ muAssign(var, muSubList(subject, startcursor, prevlen)),
                                                    muAssign(cursor, muAddNativeInt(startcursor, prevlen)),
                                                    trueCont
                                                  ]))
                                 , falseCont // <<<<
                                 )
                             , falseCont    // <<<<     
                          ]);
        }
   }
    if(!asubtype(subjectType, var.atype)){
        code = muIf(muValueIsSubType(subject, var.atype), code);
    }
    return muEnter(enter, code); 
}

default MuExp translatePatAsListElem(Pattern p, Lookahead lookahead, AType subjectType, MuExp subject, MuExp sublen, MuExp cursor, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
  try {
        pcon = muCon(translatePatternAsConstant(p));
        return muIfelse(muAndNativeBool(muLessNativeInt(cursor, sublen), muEqual(muSubscript(subject, cursor), pcon)),         
                        muBlock([ muAssign(cursor, muAddNativeInt(cursor, muCon(1))),
                                  trueCont ])
                        , falseCont // <<<<
                        );                            
  } catch: {
  // bound check?
    return translatePat(p, getListElementType(subjectType), muSubscript(subject, cursor), btscopes, muValueBlock(avalue(), [ muIncNativeInt(cursor, muCon(1)), trueCont]), falseCont);
   
   }
}

// -- variable becomes pattern ---------------------------------------

BTINFO getBTInfo(p:(Pattern) `<Name name> : <Pattern pattern>`,  BTSCOPE btscope, BTSCOPES btscopes) {
    <btscope1, btscopes1> = getBTInfo(pattern, btscope, btscopes);
    return registerBTScope(p, btscope1, btscopes1);
}

MuExp translatePat(p:(Pattern) `<Name name> : <Pattern pattern>`, AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont, bool subjectAssigned=false) {
    if(subjectAssigned){
         return translatePat(pattern, subjectType, subjectExp, btscopes, trueCont, falseCont, subjectAssigned=false);
    } else {
         //<fuid, pos> = getVariableScope(prettyPrintName(name), name@\loc);
        var = mkVar(prettyPrintName(name), name@\loc);
        asg = isDefinition(name@\loc) ? muVarInit(var, subjectExp) : muAssign(var, subjectExp);
        return translatePat(pattern, subjectType, subjectExp, btscopes, muValueBlock(avalue(), [ asg, trueCont ]), falseCont, subjectAssigned=subjectAssigned);
    }
}

// -- as type pattern ------------------------------------------------

BTINFO getBTInfo(p:(Pattern) `[ <Type tp> ] <Pattern argument>`,  BTSCOPE btscope, BTSCOPES btscopes) {
    <btscope1, btscopes1> = getBTInfo(argument, btscope, btscopes);
    return registerBTScope(p, btscope1, btscopes1);
}

MuExp translatePat(p:(Pattern) `[ <Type tp> ] <Pattern argument>`, AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont, bool subjectAssigned=false) =
    muIfelse(muValueIsSubType(subjectExp, translateType(tp)), trueCont, falseCont);

// -- descendant pattern ---------------------------------------------

BTINFO getBTInfo(p:(Pattern) `/ <Pattern pattern>`,  BTSCOPE btscope, BTSCOPES btscopes) {
    enter_desc = "<btscope.enter>_DESC";
    enter_pat = "<btscope.enter>_DESC_PAT";
    BTSCOPE my_btscope = <enter_desc, enter_desc, enter_desc>;
    <btscope1, btscopes1> = getBTInfo(pattern, <enter_pat, enter_pat, enter_pat>, btscopes);
   //my_btscope.enter = btscope1.enter;
    //my_btscope.resume = btscope1.enter;
    //my_btscope.\fail = btscope1.enter;
    return registerBTScope(p, my_btscope, btscopes1);
}

MuExp translatePat(p:(Pattern) `/ <Pattern pattern>`,  AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
	p_btscope = btscopes[getLoc(p)];
	concreteMatch = concreteTraversalAllowed(pattern, subjectType);

	reachable_syms = { avalue() };
	reachable_prods = {};
    if(optimizing()){
	   tc = getTypesAndConstructors(pattern);
       <reachable_syms, reachable_prods>  = getReachableTypes(subjectType, tc.constructors, tc.types, concreteMatch);
    }
    descriptor = descendantDescriptor(concreteMatch, reachable_syms, reachable_prods, getReifiedDefinitions());
    fuid = topFunctionScope();
    elem = muTmpIValue("elem", fuid, avalue());
    elmType = avalue(); // TODO: make more precise?
    code = 
        muForAll(p_btscope.enter, elem, aset(elmType), muDescendantMatchIterator(subjectExp, descriptor),
                translatePat(pattern, avalue(), elem, btscopes, trueCont, muFail(p_btscope.resume) /*falseCont*/)
             ); 
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
	(expression is qualifiedName) ? <{}, {prettyPrintName("<expression>")}> : <{getType(p)}, {}>;

tuple[set[AType] types, set[str] constructors] getTypesAndConstructors(Pattern p) = <{getType(p)}, {}>;

// -- anti pattern ---------------------------------------------------
BTINFO getBTInfo(p:(Pattern) `! <Pattern pattern>`,  BTSCOPE btscope, BTSCOPES btscopes) {
    <btscope1, btscopes1> = getBTInfo(pattern, btscope, btscopes);
    return registerBTScope(p, btscope1, btscopes1);
}

MuExp translatePat(p:(Pattern) `! <Pattern pattern>`, AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont, bool subjectAssigned=false){
    iprintln(btscopes);
    //return muCallPrim3("not", abool(), [abool()], [translatePat(pattern, subjectType, subjectExp, btscopes, trueCont, falseCont, subjectAssigned=subjectAssigned)], p@\loc);
    //return  translatePat(pattern, subjectType, subjectExp, btscopes, muFail(getEnter(pattern, btscopes)), muSucceed(getEnter(pattern, btscopes)), subjectAssigned=subjectAssigned);
    my_btscope = btscopes[getLoc(p)];
    return negate(my_btscope.enter, muEnter(my_btscope.enter, translatePat(pattern, subjectType, subjectExp, btscopes, muSucceed(my_btscope.enter), muFail(my_btscope.\fail))));
}
// -- typed variable becomes pattern ---------------------------------

BTINFO getBTInfo(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`, BTSCOPE btscope, BTSCOPES btscopes) {
        <btscope1, btscopes1> = getBTInfo(pattern, btscope, btscopes);
        return registerBTScope(p, btscope1, btscopes1);
}

MuExp translatePat(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`, AType subjectType, MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont, bool subjectAssigned=false) {
    trType = translateType(tp);
  
    if("<name>" == "_"){
         trPat = translatePat(pattern, subjectType, subjectExp, btscopes, trueCont, falseCont, subjectAssigned=subjectAssigned);
         return asubtype(subjectType, trType) ? trPat : muIfelse(muValueIsSubType(subjectExp, trType), trPat, falseCont);
    }
    str fuid = ""; int pos=0;           // TODO: this keeps type checker happy, why?
    <fuid, pos> = getVariableScope(prettyPrintName(name), name@\loc);
    ppname = prettyPrintName(name);
    var = muVar(ppname, fuid, pos, trType[label=ppname]);
    trueCont2 = trueCont;
    if(subjectExp != var){
        trueCont2 =  muValueBlock(avalue(), [ /*subjectAssigned ? muAssign(var, subjectExp) :*/ muVarInit(var, subjectExp), trueCont ]);
    } 
    trPat = translatePat(pattern, subjectType, subjectExp, btscopes, trueCont2, falseCont, subjectAssigned=subjectAssigned);
    return asubtype(subjectType, trType) ? trPat :  muIfelse(muValueIsSubType(subjectExp, trType), trPat, falseCont);
}

// -- default rule for pattern ---------------------------------------

default BTINFO getBTInfo(Pattern p, BTSCOPE btscope, BTSCOPES btscopes)
    = registerBTScope(p, btscope, btscopes);

default MuExp translatePat(Pattern p, AType subjectType,  MuExp subjectExp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) { 
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

value translatePatternAsConstant(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`) {
  if(!isEmpty("<keywordArguments>")) throw "Not a constant pattern: <p>";
  if(isADTType(getType(p))) throw "ADT pattern not considered constant: <p>";
  return makeNode("<expression>", [ translatePatternAsConstant(pat) | Pattern pat <- arguments ]);
}

value translatePatternAsConstant(p:(Pattern) `{<{Pattern ","}* pats>}`) {
    res = { translatePatternAsConstant(pat) | Pattern pat <- pats };
    return res;
}

value translatePatternAsConstant(p:(Pattern) `[<{Pattern ","}* pats>]`) = [ translatePatternAsConstant(pat) | Pattern pat <- pats ];

value translatePatternAsConstant(p:(Pattern) `\<<{Pattern ","}* pats>\>`) {
  lpats = [ pat | pat <- pats]; // TODO
  return ( <translatePatternAsConstant(lpats[0])> | it + <translatePatternAsConstant(lpats[i])> | i <- [1 .. size(lpats)] );
}

default value translatePatternAsConstant(Pattern p){
  throw "Not a constant pattern: <p>";
}

/*********************************************************************/
/*                  BacktrackFree for Patterns                       */
/*********************************************************************/

// TODO: Make this more precise and complete

bool backtrackFree(p:(Pattern) `[<{Pattern ","}* pats>]`) = false; // p == (Pattern) `[]` || all(pat <- pats, backtrackFree(pat));
bool backtrackFree(p:(Pattern) `{<{Pattern ","}* pats>}`) = false; //p == (Pattern) `{}` || all(pat <- pats, backtrackFree(pat));
bool backtrackFree(p:(Pattern) `<Name name> : <Pattern pattern>`) = backtrackFree(pattern);
bool backtrackFree(p:(Pattern) `[ <Type tp> ] <Pattern pattern>`) = backtrackFree(pattern);

bool backtrackFree(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`)
    = backtrackFree(expression) && (isEmpty(argumentList) || all(arg <- argumentList, backtrackFree(arg)))
                                && (isEmpty(keywordArgumentList) || all(kwa <- keywordArgumentList, backtrackFree(kwa.expression)))
    when argumentList := [arg | arg <- arguments], 
         keywordArgumentList := (((KeywordArguments[Pattern]) `<OptionalComma optionalComma> <{KeywordArgument[Pattern] ","}+ kwaList>` := keywordArguments)
                                ? [kwa | kwa <- kwaList]
                                : []);

default bool backtrackFree(Pattern p) = !isMultiVar(p);
