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

import lang::rascalcore::compile::Rascal2muRascal::ModuleInfo;
import lang::rascalcore::compile::Rascal2muRascal::RascalType;
import lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
import lang::rascalcore::compile::Rascal2muRascal::TypeReifier;

import lang::rascal::types::AbstractType;

import lang::rascalcore::compile::Rascal2muRascal::RascalExpression;

import lang::rascalcore::compile::RVM::Interpreter::ParsingTools;

/*
 * Compile the match operator and all possible patterns
 */

/*********************************************************************/
/*                  Match                                            */
/*********************************************************************/

MuExp translateMatch((Expression) `<Pattern pat> := <Expression exp>`)  = translateMatch(pat, exp) ;

MuExp translateMatch(e: (Expression) `<Pattern pat> !:= <Expression exp>`) =
    muCallMuPrim("not_mbool", [ makeBoolExp("ALL", [ translateMatch(pat, exp) ], e@\loc) ]);
    
default MuExp translateMatch(Pattern pat, Expression exp) =
    muMulti(muApply(translatePat(pat, getType(exp@\loc)), [ translate(exp) ]));

/*********************************************************************/
/*                  Patterns                                         */
/*********************************************************************/

// -- literal pattern ------------------------------------------------

default MuExp translatePat(p:(Pattern) `<Literal lit>`, Symbol subjectType) = translateLitPat(lit);

MuExp translatePat(Literal lit) = translateLitPat(lit);

MuExp translateLitPat(Literal lit) = muApply(mkCallToLibFun("Library","MATCH_LITERAL"), [translate(lit)]);

// -- regexp pattern -------------------------------------------------

MuExp translatePat(p:(Pattern) `<RegExpLiteral r>`, Symbol subjectType) = translateRegExpLiteral(r);

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

MuExp translatePat(p:(Pattern) `<Concrete concrete>`, Symbol subjectType) = translateConcretePattern(p);

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
  //println("translateConcretePattern:,<getType(p@\loc)>");
  return translateParsedConcretePattern(parseConcrete(concrete), getType(p@\loc));
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

MuExp translateParsedConcretePattern(t:appl(Production prod, list[Tree] args), Symbol symbol){
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

MuExp translateParsedConcretePattern(cc: char(int c), Symbol symbol) {
  return muApply(mkCallToLibFun("Library","MATCH_LITERAL"), [muCon(cc)]);
}

MuExp translateParsedConcretePattern(Pattern pat:(Pattern)`type ( <Pattern s>, <Pattern d> )`, Symbol symbol) {
    throw "translateParsedConcretePattern type() case"; 
}

// The patterns callOrTree and reifiedType are ambiguous, therefore we need special treatment here.

MuExp translateParsedConcretePattern(amb(set[Tree] alts), Symbol symbol) {
   throw "translateParsedConcretePattern: ambiguous, <alts>";
}

default MuExp translateParsedConcretePattern(Tree c, Symbol symbol) {
   //iprintln(c);
   throw "translateParsedConcretePattern: Cannot handle <c> at <c@\loc>";
}

bool isLayoutPat(Tree pat) = appl(prod(layouts(_), _, _), _) := pat;

bool isSeparator(Tree pat, Symbol sep) = appl(prod(sep, _, _), _) := pat;

tuple[bool, Symbol] isIterHoleWithSeparator(Tree pat){
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
         [ (i % 2 == 0) ? translatePatAsConcreteListElem(pats[i], lookahead[i], isLex) : optionalLayoutPat | i <- index(pats) ])]);
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
// TODO: this does not work if the layout is there already...
Symbol getSeparator(\iter-seps(Symbol symbol, list[Symbol] separators)) = separators[0];
Symbol getSeparator(\iter-star-seps(Symbol symbol, list[Symbol] separators)) = separators[0];
default Symbol getSeparator(Symbol sym) { throw "Cannot determine separator: <sym>"; }

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
  return muApply(mkCallToLibFun("Library","MATCH_PAT_IN_LIST"), [translateParsedConcretePattern(c, getType(c@\loc))]);
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
    rprops = for(p <- reverse([p | p <- pats])){
                 append <nElem, nMultiVar>;
                 if(isConcreteMultiVar(p)) {nMultiVar += 1; nElem += nIter(p); } else {nElem += 1;}
             };
    //println("result = <reverse(rprops)>");
    return reverse(rprops);
}
     
// -- qualified name pattern -----------------------------------------

MuExp translatePat(p:(Pattern) `<QualifiedName name>`, Symbol subjectType) = translateQualifiedNamePat(name);

MuExp translateQualifiedNamePat(QualifiedName name)
{
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR"), []);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_VAR"), [muVarRef("<name>", fuid, pos)]);
} 

// -- typed name pattern ---------------------------------------------
     
MuExp translatePat(p:(Pattern) `<Type tp> <Name name>`, Symbol subjectType){
   trType = translateType(tp);
   str fuid; int pos;           // TODO: type was added for new (experimental) type checker
   if(subtype(subjectType, trType)){
	   if("<name>" == "_"){
	      return muApply(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR"), []);
	   }
	   <fuid, pos> = getVariableScope("<name>", name@\loc);
	   return muApply(mkCallToLibFun("Library","MATCH_NEW_VAR"), [muVarRef("<name>", fuid, pos)]);
   }
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_TYPED_ANONYMOUS_VAR"), [muTypeCon(trType)]);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_TYPED_VAR"), [muTypeCon(trType), muVarRef("<name>", fuid, pos)]);
}  

// -- reified type pattern -------------------------------------------

MuExp translatePat(p:(Pattern) `type ( <Pattern symbol> , <Pattern definitions> )`, Symbol subjectType) {    
    return muApply(mkCallToLibFun("Library","MATCH_REIFIED_TYPE"), [muCon(symbol)]);
}

// -- call or tree pattern -------------------------------------------

MuExp translatePat(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, Symbol subjectType) {
   MuExp fun_pat;
   str fun_name;
   int nKwArgs = (keywordArguments is none) ? 0 : size([kw | kw <- keywordArguments.keywordArgumentList]);
   
   noKwParams = nKwArgs == 0 ? "_NO_KEYWORD_PARAMS" : "";
   concreteMatch = isConcretePattern(p) && isNonTerminalType(subjectType) ? "_CONCRETE" : "";
   
   argCode = [ translatePat(pat, Symbol::\value()) | pat <- arguments ];
   if(nKwArgs > 0){
      argCode += translatePatKWArguments(keywordArguments); //TODO: compute type per argument
   }
   
   if(expression is qualifiedName){
      fun_name = "<getType(expression@\loc).name>";
      return muApply(mkCallToLibFun("Library", "MATCH<concreteMatch>_SIMPLE_CALL_OR_TREE<noKwParams>"), [muCon(fun_name), muCallMuPrim("make_array", argCode)]);
   } else if(expression is literal){ // StringConstant
      fun_name = "<expression>"[1..-1];
      return muApply(mkCallToLibFun("Library", "MATCH<concreteMatch>_SIMPLE_CALL_OR_TREE<noKwParams>"), [muCon(fun_name), muCallMuPrim("make_array", argCode)]);
   } else {
     fun_pat = translatePat(expression, getType(expression@\loc));
     return muApply(mkCallToLibFun("Library","MATCH_CALL_OR_TREE<noKwParams>"), [muCallMuPrim("make_array", fun_pat + argCode)]);
   }
}

MuExp translatePatKWArguments((KeywordArguments[Pattern]) ``) =
   muApply(mkCallToLibFun("Library","MATCH_KEYWORD_PARAMS"), [muCallMuPrim("make_array", []), muCallMuPrim("make_array", [])]);

MuExp translatePatKWArguments((KeywordArguments[Pattern]) `<OptionalComma optionalComma> <{KeywordArgument[Pattern] ","}+ keywordArgumentList>`) {
   //println("translatePatKWArguments: <keywordArgumentList>");
   keyword_names = [];
   pats = [];
   for(kwarg <- keywordArgumentList){
       //println("kwarg = <kwarg>");
       keyword_names += muCon("<kwarg.name>");
       pats += translatePat(kwarg.expression, Symbol::\value()); // getType(kwarg.expression@\loc)?
   }
   return muApply(mkCallToLibFun("Library","MATCH_KEYWORD_PARAMS"), [muCallMuPrim("make_array", keyword_names), muCallMuPrim("make_array", pats)]);
}

// -- set pattern ----------------------------------------------------

MuExp translatePat(p:(Pattern) `{<{Pattern ","}* pats>}`, Symbol subjectType) = translateSetPat(p, subjectType);

// Translate patterns as element of a set pattern

str isLast(bool b) = b ? "LAST_" : "";

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>`, bool last, Symbol subjectType) {
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR_IN_SET"), []);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_VAR_IN_SET"), [muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `<Type tp> <Name name>`, bool last, Symbol subjectType) {
   trType = translateType(tp);
   //str fuid; int pos;           // TODO: type was added for new (experimental) type checker
   if(subtype(subjectType, trType)){
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

MuExp translatePatAsSetElem(p:(Pattern) `<QualifiedName name>*`, bool last, Symbol subjectType) {
   //str fuid; int pos;           // TODO: type was added for new (experimental) type checker
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>ANONYMOUS_MULTIVAR_IN_SET"), []);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>MULTIVAR_IN_SET"), [muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Type tp> <Name name>`, bool last, Symbol subjectType) {
   trType = translateType(tp);
   //str fuid; int pos;           // TODO: type was added for new (experimental) type checker
   if(subtype(subjectType, trType)){
	   if("<name>" == "_"){
	      return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>ANONYMOUS_MULTIVAR_IN_SET"), []);
	   }
	   <fuid, pos> = getVariableScope("<name>", p@\loc);
	   return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>NEW_MULTIVAR_IN_SET"), [muVarRef("<name>", fuid, pos)]);
   }
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>TYPED_ANONYMOUS_MULTIVAR_IN_SET"), [muTypeCon(\set(trType))]);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>TYPED_MULTIVAR_IN_SET"), [muTypeCon(\set(trType)), muVarRef("<name>", fuid, pos)]);
}

MuExp translatePatAsSetElem(p:(Pattern) `*<Name name>`, bool last, Symbol subjectType) {
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>ANONYMOUS_MULTIVAR_IN_SET"), []);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(last)>MULTIVAR_IN_SET"), [muVarRef("<name>", fuid, pos)]);
} 

MuExp translatePatAsSetElem(p:(Pattern) `+<Pattern argument>`, bool last, Symbol subjectType) {
  throw "splicePlus pattern";
}   

default MuExp translatePatAsSetElem(Pattern p, bool last, Symbol subjectType) {
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

MuExp translateSetPat(p:(Pattern) `{<{Pattern ","}* pats>}`, Symbol subjectType) {
   literals = [];
   compiledPats = [];
   list[Pattern] lpats = [pat | pat <- pats]; // TODO: unnnecessary
   elmType = Symbol::\value();
   if(Symbol::\set(tp) := subjectType && tp != Symbol::\void()){
      elmType = tp;
   }
   
   /* remove patterns with duplicate names */
   list[Pattern] uniquePats = [];
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

MuExp translatePat(p:(Pattern) `\<<{Pattern ","}* pats>\>`, Symbol subjectType) {
    list[MuExp] translatedPats;
    if(\tuple(list[Symbol] elmTypes) := subjectType && size(elmTypes) == size(pats)){
       lpats = [pat | pat <- pats];   //TODO: should be unnnecessary
       translatedPats = [ translatePat(lpats[i], elmTypes[i]) | int i <- index(lpats) ];
    } else {
    	translatedPats = [ translatePat(pat, Symbol::\value()) | pat <- pats ];
    }
    return muApply(mkCallToLibFun("Library","MATCH_TUPLE"), [muCallMuPrim("make_array", translatedPats)]);
}

// -- list pattern ---------------------------------------------------

MuExp translatePat(p:(Pattern) `[<{Pattern ","}* pats>]`, Symbol subjectType) {
    lookahead = computeLookahead(p);  
    lpats = [pat | pat <- pats];   //TODO: should be unnnecessary
    elmType = Symbol::\value();
    if(Symbol::\list(tp) := subjectType && tp != Symbol::\void()){
    	elmType = tp;
    }
    return muApply(mkCallToLibFun("Library","MATCH_LIST"), [muCallMuPrim("make_array", [ translatePatAsListElem(lpats[i], lookahead[i], elmType) | i <- index(lpats) ])]);
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

MuExp translatePatAsListElem(p:(Pattern) `<QualifiedName name>`, Lookahead lookahead, Symbol subjectType) {
   if("<name>" == "_"){
       return muApply(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR_IN_LIST"), []);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_VAR_IN_LIST"), [muVarRef("<name>", fuid, pos)]);
} 

MuExp translatePatAsListElem(p:(Pattern) `<Type tp> <Name name>`, Lookahead lookahead, Symbol subjectType) {
   trType = translateType(tp);
   str fuid; int pos;           // TODO: this keeps type checker happy, why?
   if(subtype(subjectType, trType)){
	   if("<name>" == "_"){
	       return muApply(mkCallToLibFun("Library","MATCH_ANONYMOUS_VAR_IN_LIST"), []);
	   }
	   <fuid, pos> = getVariableScope("<name>", name@\loc);
	   return muApply(mkCallToLibFun("Library","MATCH_NEW_VAR_IN_LIST"), [muVarRef("<name>", fuid, pos)]);
   }
   if("<name>" == "_"){
       return muApply(mkCallToLibFun("Library","MATCH_TYPED_ANONYMOUS_VAR_IN_LIST"), [muTypeCon(trType)]);
   }
   <fuid, pos> = getVariableScope("<name>", name@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_TYPED_VAR_IN_LIST"), [muTypeCon(trType), muVarRef("<name>", fuid, pos)]);
} 


MuExp translatePatAsListElem(p:(Pattern) `<Literal lit>`, Lookahead lookahead, Symbol subjectType) =
   muApply(mkCallToLibFun("Library","MATCH_LITERAL_IN_LIST"), [translate(lit)])
when !(lit is regExp);

MuExp translatePatAsListElem(p:(Pattern) `<QualifiedName name>*`, Lookahead lookahead, Symbol subjectType) {
   if("<name>" == "_"){
       return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>ANONYMOUS_MULTIVAR_IN_LIST"), [muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>MULTIVAR_IN_LIST"), [muVarRef("<name>", fuid, pos), muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
}

MuExp translatePatAsListElem(p:(Pattern) `*<Type tp> <Name name>`, Lookahead lookahead, Symbol subjectType) {
   trType = translateType(tp);
   str fuid; int pos;           // TODO: this keeps type checker happy, why?
   if(subtype(subjectType, trType)){
	   if("<name>" == "_"){
	       return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>ANONYMOUS_MULTIVAR_IN_LIST"), [muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
	   }
	   <fuid, pos> = getVariableScope("<name>", p@\loc);
	   return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>NEW_MULTIVAR_IN_LIST"), [muVarRef("<name>", fuid, pos), muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
   }
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>TYPED_ANONYMOUS_MULTIVAR_IN_LIST"), [muTypeCon(\list(trType)), muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>TYPED_MULTIVAR_IN_LIST"), [muTypeCon(\list(trType)), muVarRef("<name>", fuid, pos), muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
}

MuExp translatePatAsListElem(p:(Pattern) `*<Name name>`, Lookahead lookahead, Symbol subjectType) {
   if("<name>" == "_"){
      return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>ANONYMOUS_MULTIVAR_IN_LIST"), [muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
   }
   <fuid, pos> = getVariableScope("<name>", p@\loc);
   return muApply(mkCallToLibFun("Library","MATCH_<isLast(lookahead)>MULTIVAR_IN_LIST"), [muVarRef("<name>", fuid, pos), muCon(0), muCon(1000000), muCon(lookahead.nElem)]);
} 

MuExp translatePatAsListElem(p:(Pattern) `+<Pattern argument>`, Lookahead lookahead, Symbol subjectType) {
  throw "splicePlus pattern";
}   

default MuExp translatePatAsListElem(Pattern p, Lookahead lookahead, Symbol subjectType) {
  //println("translatePatAsListElem, default: <p>");
  //try { // Gives error for nodes (~ kw params)
  //   return  muApply(mkCallToLibFun("Library","MATCH_LITERAL_IN_LIST"), [muCon(translatePatternAsConstant(p))]);
  //} catch:
    return muApply(mkCallToLibFun("Library","MATCH_PAT_IN_LIST"), [translatePat(p, subjectType)]);
}

// -- variable becomes pattern ---------------------------------------

MuExp translatePat(p:(Pattern) `<Name name> : <Pattern pattern>`, Symbol subjectType) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muApply(mkCallToLibFun("Library","MATCH_VAR_BECOMES"), [muVarRef("<name>", fuid, pos), translatePat(pattern, subjectType)]);
}

// -- as type pattern ------------------------------------------------

MuExp translatePat(p:(Pattern) `[ <Type tp> ] <Pattern argument>`, Symbol subjectType) =
    muApply(mkCallToLibFun("Library","MATCH_AS_TYPE"), [muTypeCon(translateType(tp)), translatePat(argument, subjectType)]);

// -- descendant pattern ---------------------------------------------

MuExp translatePat(p:(Pattern) `/ <Pattern pattern>`, Symbol subjectType){
	//println("pattern <pattern>, isConcretePattern = <isConcretePattern(pattern)>");
	int i = nextVisit();	
	// Generate and add a nested function 'phi'
	str descId = topFunctionScope() + "/" + "DESC_<i>";
	//subjectType = stripStart(subjectType);
	concreteMatch = concreteTraversalAllowed(pattern, subjectType);
	descendantFun = concreteMatch && (subjectType != \str()) ? "DESCENT_AND_MATCH_CONCRETE" : "DESCENT_AND_MATCH";
	
	reachable_syms = { Symbol::\value() };
	reachable_prods = {};
    if(optimizing()){
	   tc = getTypesAndConstructors(pattern);
       <reachable_syms, reachable_prods>  = getReachableTypes(subjectType, tc.constructors, tc.types, concreteMatch);
       //println("translatePat: <reachable_syms>, <reachable_prods>");
    }
    descriptor = muCallPrim3("make_descendant_descriptor", [muCon(descId), muCon(reachable_syms), muCon(reachable_prods), muCon(concreteMatch), muCon(getDefinitions())], p@\loc);
    return muApply(mkCallToLibFun("Library",descendantFun), [translatePat(pattern, Symbol::\value()),  descriptor]);
}

// Strip start if present

Symbol stripStart(\start(Symbol s)) = s;
default Symbol stripStart(Symbol s) = s;

// Is  a pattern a concretePattern?
// Note that a callOrTree pattern always requires a visit of the production to inspect labeled fields and is etherefore
// NOT a concrete pattern

bool isConcretePattern(Pattern p) {
    tp = getType(p@\loc);
    return isNonTerminalType(tp) && !(p is callOrTree) && Symbol::sort(_) := tp;
}  
	
bool isConcreteType(Symbol subjectType) =
	(  isNonTerminalType(subjectType)
	|| subtype(subjectType, adt("Tree", [])) && subjectType != adt("Tree",[])
	);
	
bool concreteTraversalAllowed(Pattern pattern, Symbol subjectType) =
    isConcretePattern(pattern) && isConcreteType(subjectType);
	
// get the types and constructor names from a pattern

tuple[set[Symbol] types, set[str] constructors] getTypesAndConstructors(p:(Pattern) `<QualifiedName qualifiedName>`) =
	<{getType(p@\loc)}, {}>;
	
tuple[set[Symbol] types, set[str] constructors] getTypesAndConstructors(p:(Pattern) `<Type tp> <Name name>`) =
	<{getType(p@\loc)}, {}>;	
	
tuple[set[Symbol] types, set[str] constructors] getTypesAndConstructors(p:(Pattern) `<Name name> : <Pattern pattern>`) =
	getTypesAndConstructors(pattern);	

tuple[set[Symbol] types, set[str] constructors]  getTypesAndConstructors(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`) =
	getTypesAndConstructors(pattern);
		
tuple[set[Symbol] types, set[str] constructors] getTypesAndConstructors(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`) =
	(expression is qualifiedName) ? <{}, {deescape("<expression>")}> : <{getType(p@\loc)}, {}>;

tuple[set[Symbol] types, set[str] constructors] getTypesAndConstructors(Pattern p) = <{getType(p@\loc)}, {}>;

// -- anti pattern ---------------------------------------------------

MuExp translatePat(p:(Pattern) `! <Pattern pattern>`, Symbol subjectType) =
    muApply(mkCallToLibFun("Library","MATCH_ANTI"), [translatePat(pattern, subjectType)]);

// -- typed variable becomes pattern ---------------------------------

MuExp translatePat(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`, Symbol subjectType) {
    trType = translateType(tp);
    str fuid; int pos;           // TODO: this keeps type checker happy, why?
    if(subtype(subjectType, trType)){
       <fuid, pos> = getVariableScope("<name>", name@\loc);
    	return muApply(mkCallToLibFun("Library","MATCH_VAR_BECOMES"), [muVarRef("<name>", fuid, pos), translatePat(pattern, subjectType)]);
    }
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muApply(mkCallToLibFun("Library","MATCH_TYPED_VAR_BECOMES"), [muTypeCon(trType), muVarRef("<name>", fuid, pos), translatePat(pattern, subjectType)]);
}

// -- default rule for pattern ---------------------------------------

default MuExp translatePat(Pattern p, Symbol subjectType) { iprintln(p); throw "Pattern <p> cannot be translated"; }

/**********************************************************************/
/*                 Constant Patterns                                  */
/**********************************************************************/

value translatePatternAsConstant(p:(Pattern) `<Literal lit>`) = getLiteralValue(lit) when isConstant(lit);

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
