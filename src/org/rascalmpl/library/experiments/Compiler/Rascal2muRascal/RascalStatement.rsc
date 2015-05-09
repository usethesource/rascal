@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalStatement

import IO;
import ValueIO;
import Node;
import Map;
import Set;
import String;
import ParseTree;
import util::Reflective;

import lang::rascal::\syntax::Rascal;

import lang::rascal::types::CheckerConfig;

import experiments::Compiler::Rascal2muRascal::TmpAndLabel;
import experiments::Compiler::Rascal2muRascal::ModuleInfo;
import experiments::Compiler::Rascal2muRascal::RascalDeclaration;
import experiments::Compiler::Rascal2muRascal::RascalExpression;
import experiments::Compiler::Rascal2muRascal::RascalPattern;
import experiments::Compiler::Rascal2muRascal::RascalType;
import experiments::Compiler::Rascal2muRascal::TypeUtils;
import experiments::Compiler::Rascal2muRascal::TypeReifier;

import experiments::Compiler::muRascal::AST;


/*
 * Translate Rascal statements to muRascal using the functions:
 * - MuExp translateStats(Statement* statements)
 * - MuExp translate(Statement s).
 */

/********************************************************************/
/*                  Translate statements                            */
/********************************************************************/

MuExp translateStats(Statement* statements) = muBlock([ translate(stat) | stat <- statements ]);

/********************************************************************/
/*                  Translate one statement                         */
/********************************************************************/

// -- assert statement -----------------------------------------------
	
MuExp translate(s: (Statement) `assert <Expression expression> ;`) = 
    muCallPrim3("assertreport", [translate(expression), muCon(""), muCon(s@\loc)], s@\loc);

MuExp translate(s: (Statement) `assert <Expression expression> : <Expression message>;`) = 
    muCallPrim3("assertreport", [translate(expression), translate(message), muCon(s@\loc)], s@\loc);

// -- single expression statement ------------------------------------

MuExp translate(s: (Statement) `<Expression expression> ;`) = 
    translate(expression);

// -- visit statement ------------------------------------------------

MuExp translate(s: (Statement) `<Label label> <Visit visitItself>`) = 
    translateVisit(label, visitItself);

// -- while do statement ---------------------------------------------

MuExp translate(s: (Statement) `<Label label> while ( <{Expression ","}+ conditions> ) <Statement body>`) {
    str fuid = topFunctionScope();
    whilename = getLabel(label);
    ifname = nextLabel();
    tmp = asTmp(whilename);
    enterLoop(whilename,fuid);
    enterBacktrackingScope(whilename);
    enterBacktrackingScope(ifname);
    code = [ muAssignTmp(tmp,fuid,muCallPrim3("listwriter_open", [], s@\loc)),
             muWhile(whilename, muCon(true), [ muIfelse(ifname, makeBoolExp("ALL", [ translate(c) | c <- conditions ], s@\loc), 
                                                                [ visit(translateLoopBody(body)) { case muFail(whileName) => muFail(ifname) } ], 
                                                                [ muBreak(whilename) ]) ]),
             muCallPrim3("listwriter_close", [muTmp(tmp,fuid)], s@\loc)
           ];
    leaveBacktrackingScope();
    leaveBacktrackingScope();
    leaveLoop();
    return muBlock(code);
}

list[MuExp] resetBlockVars(Statement body){
	introduced_vars = getAllVariablesAndFunctionsOfBlockScope(body@\loc);
	locals = [pos | <str fuid, int pos> <- introduced_vars, pos >= 0, fuid == topFunctionScope()];
	if(!isEmpty(locals)){
		//println("<body@\loc>: <introduced_vars>, <locals>");
		return [muResetLocs(locals)];
	}
	return [];
}

MuExp translateLoopBody(Statement body){
	reset_vars = resetBlockVars(body);
	return isEmpty(reset_vars) ? translate(body) : muBlock([*reset_vars, translate(body)]);	
}

// Due to the similarity of some statements and their template version, we present both versions together

MuExp translateTemplate(str indent, s: (StringTemplate) `while ( <Expression condition> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    str fuid = topFunctionScope();
    whilename = nextLabel();
    ifname = nextLabel();
    enterLoop(whilename,fuid);
    enterBacktrackingScope(whilename);
    enterBacktrackingScope(ifname);
    code = [ muWhile(whilename, muCon(true),
                 [ muIfelse(ifname, makeBoolExp("ALL", [ translate(condition) ], condition@\loc), 
                     [ translateStats(preStats),
                       *translateMiddle(indent, body),  
                       translateStats(postStats)
                     ], [ muBreak(whilename) ])
                 ]) //,  muCon(666)
           ];
    leaveBacktrackingScope();
    leaveBacktrackingScope();
    leaveLoop();
    return muBlock(code);
}

// -- do while statement ---------------------------------------------

MuExp translate(s: (Statement) `<Label label> do <Statement body> while ( <Expression condition> ) ;`) {
    str fuid = topFunctionScope();
    doname = getLabel(label);
    ifname = nextLabel();
    tmp = asTmp(doname);
    enterLoop(doname,fuid);
    enterBacktrackingScope(doname);
    enterBacktrackingScope(ifname);
    code = [ muAssignTmp(tmp,fuid,muCallPrim3("listwriter_open", [], s@\loc)), 
             muWhile(doname, muCon(true), [ 
                visit(translateLoopBody(body)) { case muFail(doname) => muFail(ifname) }, 
                muIfelse(ifname, makeBoolExp("ALL", [ translate(condition) ], condition@\loc), 
                                 [ muContinue(doname) ], 
                                 [ muBreak(doname) ]) ]),
             muCallPrim3("listwriter_close", [muTmp(tmp,fuid)], s@\loc)
           ];
    leaveBacktrackingScope();
    leaveBacktrackingScope();
    leaveLoop();
    return muBlock(code);
}

MuExp translateTemplate(str indent, s: (StringTemplate) `do { < Statement* preStats> <StringMiddle body> <Statement* postStats> } while ( <Expression condition> )`) {
    str fuid = topFunctionScope();  
    doname = nextLabel();
    ifname = nextLabel();
    enterLoop(doname,fuid);
    enterBacktrackingScope(doname);
    enterBacktrackingScope(ifname);
    code = [ muWhile(doname, muCon(true),
                             [ translateStats(preStats),
                               *translateMiddle(indent, body),
                               translateStats(postStats),
                               muIfelse(ifname, makeBoolExp("ALL", [ translate(condition) ], condition@\loc), 
                                                [ muContinue(doname) ], 
                                                [ muBreak(doname) ])]) //,  muCon(666)
           ];
    leaveBacktrackingScope();
    leaveBacktrackingScope();
    leaveLoop();
    return muBlock(code);
}

// -- for statement --------------------------------------------------

MuExp translate(s: (Statement) `<Label label> for ( <{Expression ","}+ generators> ) <Statement body>`) {
    str fuid = topFunctionScope();
    forname = getLabel(label);
    tmp = asTmp(forname);
    enterLoop(forname,fuid);
    enterBacktrackingScope(forname);
    code = [ muAssignTmp(tmp,fuid,muCallPrim3("listwriter_open", [], s@\loc)),
             muWhile(forname, makeMultiValuedBoolExp("ALL",[ translate(c) | c <-generators ], s@\loc), 
                              [ translateLoopBody(body) ]),
             muCallPrim3("listwriter_close", [muTmp(tmp,fuid)], s@\loc)
           ];
    leaveBacktrackingScope();
    leaveLoop();
    return muBlock(code);
}

MuExp translateTemplate(str indent, s: (StringTemplate) `for ( <{Expression ","}+ generators> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    str fuid = topFunctionScope();
    forname = nextLabel();
    enterLoop(forname,fuid);
    enterBacktrackingScope(forname);
    code = [ muWhile(forname, makeMultiValuedBoolExp("ALL",[ translate(c) | c <-generators ], s@\loc), 
                     [ translateStats(preStats),  
                       *translateMiddle(indent, body),
                       translateStats(postStats)
                     ]),
             muCon("")	// make sure that for results some value
           ];
    leaveBacktrackingScope();
    leaveLoop();
    return muBlock(code);
} 

// -- if then statement ----------------------------------------------

MuExp translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement>`) {
    ifname = getLabel(label);
	enterBacktrackingScope(ifname);
	code = muIfelse(ifname, makeBoolExp("ALL", [ translate(c) | c <- conditions ], s@\loc), [translate(thenStatement)], []);
    leaveBacktrackingScope();
    return code;
}
    
MuExp translateTemplate(str indent, s: (StringTemplate) `if (<{Expression ","}+ conditions> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    str fuid = topFunctionScope();
    ifname = nextLabel();
    enterBacktrackingScope(ifname);
    code = [ muIfelse(ifname, makeBoolExp("ALL", [ translate(c) | c <- conditions ], s@\loc), 
                      [ translateStats(preStats),
                        *translateMiddle(indent, body),
                        translateStats(postStats)],
                      []),
             muCon("")
           ];
    leaveBacktrackingScope();
    return muBlock(code);
}    

// -- if then else statement -----------------------------------------

MuExp translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement> else <Statement elseStatement>`) {
	ifname = getLabel(label);
	code = muIfelse(ifname, makeBoolExp("ALL",[ translate(c) | c <- conditions ], s@\loc), 
	                        { enterBacktrackingScope(ifname); [ translate(thenStatement) ]; }, 
	                        { leaveBacktrackingScope(); [ translate(elseStatement)]; });
    return code;
}
    
MuExp translateTemplate(str indent, s: (StringTemplate) `if ( <{Expression ","}+ conditions> ) { <Statement* preStatsThen> <StringMiddle thenString> <Statement* postStatsThen> }  else { <Statement* preStatsElse> <StringMiddle elseString> <Statement* postStatsElse> }`){
    str fuid = topFunctionScope();                    
    ifname = nextLabel();
    code = [ muIfelse(ifname, makeBoolExp("ALL",[ translate(c) | c <- conditions ], s@\loc), 
                      { enterBacktrackingScope(ifname);
                        [ translateStats(preStatsThen), 
                          *translateMiddle(indent, thenString),
                          translateStats(postStatsThen)
                        ];
                      },
                      { enterBacktrackingScope(ifname);
                        [ translateStats(preStatsElse), 
                          *translateMiddle(indent, elseString),
                          translateStats(postStatsElse)
                        ];
                      })
           ];
    leaveBacktrackingScope();
    return muBlock(code);                                             
} 

// -- switch statement -----------------------------------------------

MuExp translate(s: (Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`) = translateSwitch(s);


// Original case translation

//MuExp translateSwitch(s: (Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`) {
//    str fuid = topFunctionScope();
//    switchname = getLabel(label);
//    switchval = asTmp(switchname);
//    return muBlock([ muAssignTmp(switchval,fuid,translate(expression)), translateSwitchCases(switchval,fuid,[c | c <- cases]) ]);
//}

//MuExp translateSwitchCases(str switchval, str fuid, list[Case] cases) {
//  if(size(cases) == 0)
//      return muBlock([]);
//  c = head(cases);
//  
//  if(c is patternWithAction){
//     pwa = c.patternWithAction;
//     if(pwa is arbitrary){
//     	ifname = nextLabel();
//        cond = muMulti(muApply(translatePat(pwa.pattern), [ muTmp(switchval,fuid) ]));
//        exp = muIfelse(ifname, cond, 
//                               { enterBacktrackingScope(ifname); [ translate(pwa.statement) ]; }, 
//                               { leaveBacktrackingScope(); [ translateSwitchCases(switchval,fuid,tail(cases)) ]; });
//        return exp; 
//     } else {
//        throw "Replacement not allowed in switch statement";
//     }
//  } else {
//        return translate(c.statement);
//  }
//}

/*
 * Optimized switch translation that uses a SWITCH instruction.
 * A table is constructed that maps a "fingerprint" of the switch value to a label associated with a MuExp to handle that case.
 * Special attention is needed for case patterns that spoil this simple scheme, i.e. they lead to pattern overlap, typically
 * a top level (typed variable) or a regular expression. The overlap between constructors and nodes is also considered carefully:
 * All spoiler cases are prepended to the default case.
 * 
 */
MuExp translateSwitch(s: (Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`) {
    str fuid = topFunctionScope();
    switchname = getLabel(label);
    switchval = asTmp(switchname);
    the_cases = [ c | Case c <- cases ];

    useConcreteFingerprint = hasConcretePatternsOnly(the_cases);
    <case_code, default_code> = translateSwitchCases(switchval, fuid, useConcreteFingerprint, the_cases);
    return muSwitch(muAssignTmp(switchval,fuid,translate(expression)), useConcreteFingerprint, case_code, default_code, muTmp(switchval, fuid));
}

/*
 *	In the context of switches, a type is "spoiled" when overlap between case patterns will
 *  prevent a direct selection of the relevant case based on the switch value alone.
 *  Typical examples:
 *  - a regexp pattern will spoil type str
 *  - a pattern `int n` will spoil type int
 *  - a pattern 'node nd` will spoil all ADT cases
 *  - a pattern `str s(3)` will spoil type node
 *
 */

bool isSpoiler(Pattern pattern){
	if(pattern is variableBecomes || pattern is typedVariableBecomes)
		return isSpoiler(pattern.pattern);
	if(pattern is splice || pattern is splicePlus || pattern is asType) 
		return isSpoiler(pattern.argument);
		
	return 
 	      pattern is qualifiedName
 	   || pattern is multiVariable
 	   || pattern is negative
 	   || pattern is literal && pattern.literal is regExp
 	   || pattern is typedVariable
 	   || pattern is callOrTree && !(pattern.expression is qualifiedName)
 	   || pattern is descendant 
 	   || pattern is anti
 	   || getOuterType(pattern) == "node"
 	   ;
}

map[int, MuExp] addPatternWithActionCode(str switchval, str fuid, bool useConcreteFingerprint, PatternWithAction pwa, map[int, MuExp] table, int key){
	if(pwa is arbitrary){
	   ifname = nextLabel();
	   cond = pwa.pattern is literal && !pwa.pattern.literal is regExp
	          ? muCallPrim3("equal", [translate(pwa.pattern.literal), muTmp(switchval,fuid)], pwa@\loc)
	   		  : muMulti(muApply(translatePat(pwa.pattern, \value()), [ muTmp(switchval,fuid) ]));
	   table[key] = muIfelse(ifname, cond, 
	                         { enterBacktrackingScope(ifname); [ muAssignTmp(switchval, fuid, translate(pwa.statement)), muCon(true)]; }, 
	                         { leaveBacktrackingScope(); [ table[key] ?  muCon(false)]; }); 
	 } else {
	   throw "Replacement not allowed in switch statement";
	 }
	 return table;
}

private int fingerprintDefault = 0; //getFingerprint("default", false);

tuple[list[MuCase], MuExp] translateSwitchCases(str switchval, str fuid, bool useConcreteFingerprint, list[Case] cases) {
  map[int,MuExp] table = ();		// label + generated code per case
  
  default_code = muAssignTmp(switchval, fuid, muCon(777));	// default code for default case
   
  for(c <- reverse(cases)){
	  if(c is patternWithAction){
	    if(!isSpoiler(c.patternWithAction.pattern)){
	       pwa = c.patternWithAction;
	       key = fingerprint(pwa.pattern, useConcreteFingerprint);
	       table = addPatternWithActionCode(switchval, fuid, useConcreteFingerprint, pwa, table, key);
	    }
	  } else {
	       default_code = muBlock([muAssignTmp(switchval, fuid, translate(c.statement)), muCon(true)]);
	  }
   }
   default_table = (fingerprintDefault : default_code);
   for(c <- reverse(cases), c is patternWithAction, isSpoiler(c.patternWithAction.pattern)){
	  default_table = addPatternWithActionCode(switchval, fuid, useConcreteFingerprint, c.patternWithAction, default_table, fingerprintDefault);
   }
   
   println("TABLE DOMAIN(<size(table)>): <domain(table)>");
   return < [ muCase(key, table[key]) | key <- table], default_table[fingerprintDefault] >;
}

// Compute the fingerprint of a pattern. Note this should be in sync with ToplevelType.getFingerprint.

int fingerprint(p:(Pattern) `<Literal lit>`, bool useConcreteFingerprint) =
	getFingerprint(readTextValueString("<lit>"), useConcreteFingerprint) when !(p.literal is regExp);

int fingerprint(p:(Pattern) `<Concrete concrete>`, bool useConcreteFingerprint) {
	res = getFingerprint(parseConcrete(concrete), useConcreteFingerprint);
	//println("fingerprint <res>, <getType(p@\loc)> for <p>");
	return res;
}

int fingerprint(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, bool useConcreteFingerprint) { 
	args = [a | a <- arguments];	// TODO: work around!
	res = fingerprintDefault;
	if(expression is qualifiedName && (QualifiedName)`<{Name "::"}+ nl>` := expression.qualifiedName){	
	   s = "<[ n | n <- nl ][-1]>";
	   if(useConcreteFingerprint){	// Abstract pattern during concrete match
	   		pr = getLabeledProduction(s, getType(p@\loc));
	   		res = getFingerprintNode(pr);
	   		//println("getProduction= <pr>, <res>");
	   } else
	   	 	res = getFingerprint(s[0] == "\\" ? s[1..] : s, size(arguments), useConcreteFingerprint);
	}
	//println("fingerprint <res>, <getType(p@\loc)> for <p>");
	return res;
}
int fingerprint(p:(Pattern) `{<{Pattern ","}* pats>}`, bool useConcreteFingerprint) = getFingerprint("set", useConcreteFingerprint);
int fingerprint(p:(Pattern) `\<<{Pattern ","}* pats>\>`, bool useConcreteFingerprint) = getFingerprint("tuple", size(pats), useConcreteFingerprint);
int fingerprint(p:(Pattern) `[<{Pattern ","}* pats>]`, bool useConcreteFingerprint) = getFingerprint("list", useConcreteFingerprint);
int fingerprint(p:(Pattern) `<Name name> : <Pattern pattern>`, bool useConcreteFingerprint) = fingerprint(pattern, useConcreteFingerprint);
int fingerprint(p:(Pattern) `[ <Type tp> ] <Pattern argument>`, bool useConcreteFingerprint) = fingerprint(argument, useConcreteFingerprint);
int fingerprint(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`, bool useConcreteFingerprint) = fingerprint(pattern, useConcreteFingerprint);
default int fingerprint(Pattern p, bool useConcreteFingerprint) {
	//println("fingerprint <fingerprintDefault> (default), <getType(p@\loc)> for <p>");
	return fingerprintDefault;
}	

// -- fail statement -------------------------------------------------

MuExp translate(s: (Statement) `fail <Target target> ;`) = 
     inBacktrackingScope() ? muFail(target is empty ? currentBacktrackingScope() : "<target.name>")
                           : muFailReturn();
                          
// -- break statement ------------------------------------------------

MuExp translate(s: (Statement) `break <Target target> ;`) = 
    muBreak(target is empty ? currentLoop() : "<target.name>");
 
// -- continue statement ---------------------------------------------

MuExp translate(s: (Statement) `continue <Target target> ;`) = 
    muContinue(target is empty ? currentLoop() : "<target.name>");

// -- filter statement -----------------------------------------------

MuExp translate(s: (Statement) `filter ;`) =
    muFilterReturn();

// -- solve statement ------------------------------------------------

MuExp translate(s: (Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`) = 
    translateSolve(s);

MuExp translateSolve(s: (Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`) {
   str fuid = topFunctionScope();
   iterations = nextTmp("iterations");  // count number of iterations
   change = nextTmp("change");		    // keep track of any changed value
   result = nextTmp("result");		    // result of body computation
 
   varCode = [ translate(var) | var <- variables ];
   //println("varCode: <varCode>");
   tmps = [ nextTmp("<var>") | var <- variables ];
   return muBlock([ muAssignTmp(iterations, fuid, (bound is empty) ? muCon(1000000) : translate(bound.expression)),
    				muCallPrim3("non_negative", [muTmp(iterations,fuid)], bound@\loc),
                    muAssignTmp(change, fuid, muCon(true)),
                    muWhile(nextLabel("while"),
                            muCallMuPrim("and_mbool_mbool", [muTmp(change,fuid), muCallPrim3("int_greater_int", [muTmp(iterations,fuid), muCon(0)], bound@\loc) ]), 
                            [ muAssignTmp(change, fuid, muCon(false)),
                            *[ muAssignTmp(tmps[i], fuid, varCode[i]) | int i <- index(varCode) ],
                              muAssignTmp(result, fuid, translate(body)),
                              *[ muIfelse(nextLabel("notequal-vars"), muCallPrim3("notequal", [muTmp(tmps[i],fuid), varCode[i]], bound@\loc), [muAssignTmp(change,fuid,muCon(true))], []) 
                 			   | int i <- index(varCode)
                 			   ],
                              muAssignTmp(iterations, fuid, muCallPrim3("int_subtract_int", [muTmp(iterations,fuid), muCon(1)], s@\loc)) 
                            ]),
                    muTmp(result,fuid)
           ]);
}

// -- try statement --------------------------------------------------

MuExp translate(s: (Statement) `try <Statement body> <Catch+ handlers>`) {
    if(body is emptyStatement){
    	return muCon(666);
    }
    list[Catch] defaultCases = [ handler | Catch handler <- handlers, handler is \default ];
    list[Catch] otherCases   = [ handler | Catch handler <- handlers, !(handler is \default) ];
    patterns = [ handler.pattern | Catch handler <- otherCases ];
    
    // If there is no default catch, compute lub of pattern types,
    // this gives optimization of the handler search based on types
    lubOfPatterns = !isEmpty(defaultCases) ? Symbol::\value() : Symbol::\void();
    if(isEmpty(defaultCases)) {
    	lubOfPatterns = ( lubOfPatterns | lub(it, getType(p@\loc)) | Pattern p <- patterns );
    }
    // Introduce a temporary variable that is bound within a catch block to a thrown value
    str fuid = topFunctionScope();
    varname = asTmp(nextLabel());
    bigCatch = muCatch(varname,fuid,lubOfPatterns, translateCatches(varname, fuid, [ handler | handler <- handlers ], !isEmpty(defaultCases)));
    exp = muTry(translate(body), bigCatch, muBlock([]));
    
	return exp;
}

MuExp translate(s: (Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`) {
	// The stack of try-catch-finally block is managed to check whether there is a finally block 
	// that must be executed before 'return', if any; 
	// in this case, the return expression has to be first evaluated, stored in a temporary variable 
	// and returned after the 'finally' block has been executed
	if(body is emptyStatement){
    	return muCon(666);
    }
	enterTryCatchFinally();
	MuExp tryCatch = translate((Statement) `try <Statement body> <Catch+ handlers>`);
	leaveTryCatchFinally();
	MuExp finallyExp = translate(finallyBody);
	// Introduce a temporary variable that is bound within a catch block to a thrown value
	str fuid = topFunctionScope();
	str varname = asTmp(nextLabel());
	return muTry(muTry(tryCatch.exp, tryCatch.\catch, muBlock([])), 
				 muCatch(varname, fuid, Symbol::\value(), muBlock([finallyExp, muThrow(muTmp(varname,fuid), finallyBody@\loc)])), 
				 finallyExp); 
}

MuExp translateCatches(str varname, str varfuid, list[Catch] catches, bool hasDefault) {
  // Translate a list of catch blocks into one catch block
  if(size(catches) == 0) {
  	  // In case there is no default catch provided, re-throw the value from the catch block
      return muThrow(muTmp(varname,varfuid), |unknown:///|);
  }
  
  c = head(catches);
  
  trBody = c.body is emptyStatement ? muCon(666) : translate(c.body);
  if(c is binding) {
      ifname = nextLabel();
      enterBacktrackingScope(ifname);
      list[MuExp] conds = [];
      list[MuExp] then = [];
      if(c.pattern is literal) {
          conds = [ muCallMuPrim("equal", [ muTmp(asUnwrappedThrown(varname),varfuid), translate(c.pattern.literal) ]) ];
          then = [ trBody ];
      } else if(c.pattern is typedVariable) {
          conds = [ muCallMuPrim("check_arg_type", [ muTmp(asUnwrappedThrown(varname),varfuid), muTypeCon(translateType(c.pattern.\type)) ]) ];
          <fuid,pos> = getVariableScope("<c.pattern.name>", c.pattern.name@\loc);
          then = [ muAssign("<c.pattern.name>", fuid, pos, muTmp(asUnwrappedThrown(varname),varfuid)), trBody ];
      } else if(c.pattern is qualifiedName){	// TODO: what if qualifiedName already has a value? Check it!
      	  conds = [ muCon(true) ];
      	  <fuid,pos> = getVariableScope("<c.pattern.qualifiedName>", c.pattern.qualifiedName@\loc);
          then = [ muAssign("<c.pattern.qualifiedName>", fuid, pos, muTmp(asUnwrappedThrown(varname),varfuid)), trBody ]; 
      } else {
          conds = [ muMulti(muApply(translatePat(c.pattern, \value()), [ muTmp(asUnwrappedThrown(varname),varfuid) ])) ];
          then = [ trBody ];
      }
      exp = muIfelse(ifname, makeBoolExp("ALL",conds, c@\loc), then, [translateCatches(varname, varfuid, tail(catches), hasDefault)]);
      leaveBacktrackingScope();
      return exp;
  }
  
  // The default case will handle any thrown value
  exp = trBody;
  
  return exp;
}

// -- labeled statement ----------------------------------------------

MuExp translate(s: (Statement) `<Label label> { <Statement+ statements> }`) =
    muBlock([translate(stat) | stat <- statements]);

// -- assignment statement -------------------------------------------    

MuExp translate(s: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`) = 
    translateAssignment(s); 

MuExp translateAssignment(s: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`) =
    assignTo(assignable, "<operator>", getOuterType(statement), translate(statement));

// apply assignment operator 
    
MuExp applyOperator(str operator, Assignable assignable, str rhs_type, MuExp rhs) {
    if(operator == "="){
        return rhs;
    }
   
    if(operator == "?="){
        oldval = getValues(assignable);
        assert size(oldval) == 1 : "applyOperator";   
        return translateIfDefinedOtherwise(oldval[0], rhs, assignable@\loc);
    }
    
    oldval = getValues(assignable);
     
    op1 = ("+=" : "add", "\<\<=" : "add", "\>\>=" : "add", "-=" : "subtract", "*=" : "product", "/=" : "divide", "&=" : "intersect")[operator];
    op2 = typedBinaryOp(getOuterType(assignable), op1, rhs_type);
    
    assert size(oldval) == 1 : "applyOperator";
    return muCallPrim3("<op2>", [*oldval, rhs], assignable@\loc);    
}

str getAssignOp(str operator){
    return  ("=" : "replace", "+=" : "add", "-=" : "subtract", "*=" : "product", "/=" : "divide", "&=" : "intersect")[operator]; 
}
    
// assignTo: assign the rhs of the assignment (possibly modified by an assign operator) to the assignable
    
MuExp assignTo(a: (Assignable) `<QualifiedName qualifiedName>`, str operator, str rhs_type, MuExp rhs) {
    return mkAssign("<qualifiedName>", qualifiedName@\loc, applyOperator(operator, a, rhs_type, rhs));
}

MuExp assignTo(a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, str operator,  str rhs_type, MuExp rhs) =
     assignTo(receiver, "=", rhs_type, muCallPrim3("<getOuterType(receiver)>_update", [*getValues(receiver), translate(subscript), applyOperator(operator, a, rhs_type, rhs)], a@\loc));
    
MuExp assignTo(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, str operator,  str rhs_type, MuExp rhs) =
    assignTo(receiver, "=", rhs_type, muCallPrim3("<getOuterType(receiver)>_slice_<getAssignOp(operator)>", [*getValues(receiver), translateOpt(optFirst), muCon(false), translateOpt(optLast), rhs], a@\loc) );

MuExp assignTo(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`, str operator,  str rhs_type, MuExp rhs) =
     assignTo(receiver, "=", rhs_type, muCallPrim3("<getOuterType(receiver)>_slice_<getAssignOp(operator)>", [*getValues(receiver), translateOpt(optFirst), translate(second), translateOpt(optLast), rhs], a@\loc));

MuExp assignTo(a: (Assignable) `<Assignable receiver> . <Name field>`, str operator,  str rhs_type, MuExp rhs) =
     getOuterType(receiver) == "tuple" 
     ? assignTo(receiver,  "=", rhs_type, muCallPrim3("<getOuterType(receiver)>_update", [*getValues(receiver), muCon(getTupleFieldIndex(getType(receiver@\loc), "<field>")), applyOperator(operator, a, rhs_type, rhs)], a@\loc) )
     : assignTo(receiver, "=", rhs_type, muCallPrim3("<getOuterType(receiver)>_field_update", [*getValues(receiver), muCon("<field>"), applyOperator(operator, a, rhs_type, rhs)], a@\loc) );

MuExp assignTo(a: (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, str operator,  str rhs_type, MuExp rhs) = 
    assignTo(receiver,  "=", rhs_type, applyOperator(operator, a, rhs_type, rhs));
    

MuExp assignTo(a: (Assignable) `\<  <{Assignable ","}+ elements> \>`, str operator,  str rhs_type, MuExp rhs) {
    str fuid = topFunctionScope();
    nelems = size(elements); // size_assignables
    str tmp_name = nextTmp();
    elems = [ e | e <- elements];   // hack since elements[i] yields a value result;
    return muBlock(
              muAssignTmp(tmp_name, fuid, applyOperator(operator, a, rhs_type, rhs)) + 
              [ assignTo(elems[i], "=", rhs_type, muCallPrim3("tuple_subscript_int", [muTmp(tmp_name,fuid), muCon(i)], a@\loc) )
              | i <- [0 .. nelems]
              ]);
}

MuExp assignTo(a: (Assignable) `<Name name> ( <{Assignable ","}+ arguments> )`, str operator,  str rhs_type, MuExp rhs) { 
    str fuid = topFunctionScope();
    nelems = size(arguments);// size_assignables
    str tmp_name = nextTmp();
    elems = [ e | e <- arguments];  // hack since elements[i] yields a value result;
    return muBlock(
              muAssignTmp(tmp_name, fuid, applyOperator(operator, a, rhs_type, rhs)) + 
              [ assignTo(elems[i], "=", rhs_type, muCallPrim3("adt_subscript_int", [muTmp(tmp_name,fuid), muCon(i)], a@\loc) )
              | i <- [0 .. nelems]
              ]);
}

MuExp assignTo(a: (Assignable) `<Assignable receiver> @ <Name annotation>`,  str operator,  str rhs_type, MuExp rhs) =
     assignTo(receiver, "=", rhs_type, muCallPrim3("annotation_set", [*getValues(receiver), muCon("<annotation>"), applyOperator(operator, a, rhs_type, rhs)], a@\loc));

// getValues: get the current value(s) of an assignable

list[MuExp] getValues(a: (Assignable) `<QualifiedName qualifiedName>`) = 
    [ mkVar("<qualifiedName>", qualifiedName@\loc) ];
    
list[MuExp] getValues(a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`) {
    otr = getOuterType(receiver);
    subscript_op = "<otr>_subscript";
    if(otr notin {"map"}){
       subscript_op += "_<getOuterType(subscript)>";
    }
    return [ muCallPrim3(subscript_op, [*getValues(receiver), translate(subscript)], a@\loc) ];
}
    
list[MuExp] getValues(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) = 
    //translateSlice(getValues(receiver), translateOpt(optFirst), muCon(false),  translateOpt(optLast));
     [ muCallPrim3("<getOuterType(receiver)>_slice", [ *getValues(receiver), translateOpt(optFirst), muCon("false"), translateOpt(optLast) ], a@\loc) ];
    
list[MuExp] getValues(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`) = 
    //translateSlice(getValues(receiver), translateOpt(optFirst), translate(second),  translateOpt(optLast));
     [ muCallPrim3("<getOuterType(receiver)>_slice", [ *getValues(receiver), translateOpt(optFirst),  translate(second), translateOpt(optLast) ], a@\loc) ];

list[MuExp] getValues(a:(Assignable) `<Assignable receiver> . <Name field>`) = 
    [ muCallPrim3("<getOuterType(receiver)>_field_access", [ *getValues(receiver), muCon(unescape("<field>"))], a@\loc) ];

list[MuExp] getValues(a: (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`) = 
     [ translateIfDefinedOtherwise(getValues(receiver)[0], translate(defaultExpression), a@\loc) ];

list[MuExp] getValues(a:(Assignable) `\<  <{Assignable ","}+ elements > \>` ) = [ *getValues(elm) | elm <- elements ];

list[MuExp] getValues(a:(Assignable) `<Name name> ( <{Assignable ","}+ arguments> )` ) = [ *getValues(arg) | arg <- arguments ];

list[MuExp] getValues(a: (Assignable) `<Assignable receiver> @ <Name annotation>`) = 
    [ muCallPrim3("annotation_get", [ *getValues(receiver), muCon("<annotation>")], a@\loc) ];

// getReceiver: get the final receiver of an assignable

Assignable getReceiver(a: (Assignable) `<QualifiedName qualifiedName>`) = a;
Assignable getReceiver(a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`) = getReceiver(receiver);
Assignable getReceiver(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) = getReceiver(receiver);
Assignable getReceiver(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`) = getReceiver(receiver);  
Assignable getReceiver(a: (Assignable) `<Assignable receiver> . <Name field>`) = getReceiver(receiver); 
Assignable getReceiver(a: (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`) = getReceiver(receiver); 
Assignable getReceiver(a: (Assignable) `<Name name> ( <{Assignable ","}+ arguments> )`) = a;
Assignable getReceiver(a: (Assignable) `\< <{Assignable ","}+ elements> \>`) =  a;
Assignable getReceiver(a: (Assignable) `<Assignable receiver> @ <Name annotation>`) = getReceiver(receiver); 

// -- empty statement ------------------------------------------------

MuExp translate(s: (Statement) `;`) = 
    muBlock([]);

//MuExp translate(s: (Statement) `global <Type \type> <{QualifiedName ","}+ names> ;`) { throw("globalDirective"); }

// -- return statement -----------------------------------------------

MuExp translate(s: (Statement) `return <Statement statement>`) {
	// If the 'return' is used in the scope of a try-catch-finally block,
	// the respective 'finally' block must be executed before the function returns
	if(hasFinally()) {
	    str fuid = topFunctionScope();
		str varname = asTmp(nextLabel());
		return muBlock([ muAssignTmp(varname, fuid, translate(statement)), muReturn1(muTmp(varname,fuid)) ]);
	} 
	return muReturn1(translate(statement));
}

// -- throw statement ------------------------------------------------

MuExp translate(s: (Statement) `throw <Statement statement>`) = 
    muThrow(translate(statement),s@\loc);

MuExp translate(s: (Statement) `insert <DataTarget dataTarget> <Statement statement>`) // TODO: handle dataTarget
	= { fillCaseType(getType(statement@\loc)); 
	    muInsert(translate(statement));
	  };

// -- append statement -----------------------------------------------

MuExp translate(s: (Statement) `append <DataTarget dataTarget> <Statement statement>`) =
   muCallPrim3("listwriter_add", [muTmp(asTmp(currentLoop(dataTarget)),getCurrentLoopScope(dataTarget)), translate(statement)], s@\loc);

// -- function declaration statement ---------------------------------

MuExp translate(s: (Statement) `<FunctionDeclaration functionDeclaration>`) { 
    translate(functionDeclaration); return muBlock([]); 
}

// -- local variable declaration statement ---------------------------

MuExp translate(s: (Statement) `<LocalVariableDeclaration declaration> ;`) { 
    tp = declaration.declarator.\type;
    {Variable ","}+ variables = declaration.declarator.variables;
    code = for(var <- variables){
    			if(var is initialized)
    				append mkAssign("<var.name>", var.name@\loc, translate(var.initial));
             }
    return muBlock(code);
}

// -- unknown statement ----------------------------------------------

default MuExp translate(Statement s){
   throw "MISSING CASE FOR STATEMENT: <s>";
}

/*********************************************************************/
/*                  End of Statements                                */
/*********************************************************************/


MuExp translateFormals(list[Pattern] formals, bool isVarArgs, int i, list[MuExp] kwps, node body, list[Expression] when_conditions, loc src){
   if(isEmpty(formals)) {
      if(isEmpty(when_conditions)){
        return muBlock([ *kwps, muReturn1(translateFunctionBody(body)) ]);
    } else {
        ifname = nextLabel();
        enterBacktrackingScope(ifname);
        conditions = [ translate(cond) | cond <- when_conditions];
        mubody = muIfelse(ifname,makeBoolExp("ALL",conditions, src), [ *kwps, muReturn1(translateFunctionBody(body)) ], [ muFailReturn() ]);
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
      exp = muIfelse(ifname, muCallMuPrim("check_arg_type_and_copy", [ muCon(i), 
                                                        muTypeCon( (isVarArgs && size(formals) == 1) ? Symbol::\list(translateType(tp)) : translateType(tp) ), 
                                                        muCon(pos)
                                                    ]),
             [ translateFormals(tail(formals), isVarArgs, i + 1, kwps, body, when_conditions, src) ],
             [ muFailReturn() ]);
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
         conditions += muMulti(muApply(translatePat(pat, \value()), [ muVar("<i>",topFunctionScope(),i) ]));
         i += 1;
      };
      conditions += [ translate(cond) | cond <- when_conditions];

      mubody = muIfelse(fname, makeBoolExp("ALL",conditions, formals@\loc), [ *kwps, muReturn1(translateFunctionBody(body)) ], [ muFailReturn() ]);
      leaveBacktrackingScope();
      return mubody;
  }
}

//MuExp translateFunctionBody(Expression exp) = translate(exp); // when bprintln("translateFunctionBody: <exp>");
//MuExp translateFunctionBody(MuExp exp) = exp;
//// TODO: check the interpreter subtyping
//default MuExp translateFunctionBody(Statement* stats) = muBlock([ translate(stat) | stat <- stats ]);
//default MuExp translateFunctionBody(Statement+ stats) = muBlock([ translate(stat) | stat <- stats ]);
//
//default MuExp translateFunctionBody(node nd) {  throw "Cannot handle function body <nd>"; }

MuExp translateFunctionBody(node nd){
    if(Expression exp := nd){
    	//println("translateFunctionBody: Expression");
    	return translate(exp);
    }
    if(MuExp exp := nd){
    	//println("translateFunctionBody: MuExp");
    	return exp;
    }
    if(Statement* stats := nd){
    	//println("translateFunctionBody: Statement*");
    	return muBlock([ translate(stat) | stat <- stats ]);
    }
    if(Statement+ stats := nd){
    	//println("translateFunctionBody: Statement+");
    	return muBlock([ translate(stat) | stat <- stats ]);
    }
    println("translateFunctionBody: Cannot handle function body <nd>");
    
    throw "Cannot handle function body <nd>";
    //switch(nd){
    //    case Expression exp:    return translate(exp);
    //    case MuExp exp:         return exp;
    //    case Statement* stats:  return muBlock([ translate(stat) | stat <- stats ]);
    //    case Statement+ stats:  return muBlock([ translate(stat) | stat <- stats ]);
    //    default:
    //        throw "Cannot handle function body <nd>";
    //}
}
