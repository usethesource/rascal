@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalStatement

import IO;
import ValueIO;
import Node;
import Map;
import Set;
import String;
import ParseTree;
import util::Reflective;

import lang::rascal::\syntax::Rascal;

import lang::rascalcore::check::AType;
//import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::Fingerprint;

import lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel;
import lang::rascalcore::compile::Rascal2muRascal::ModuleInfo;
import lang::rascalcore::compile::Rascal2muRascal::RascalDeclaration;
import lang::rascalcore::compile::Rascal2muRascal::RascalExpression;
import lang::rascalcore::compile::Rascal2muRascal::RascalPattern;
import lang::rascalcore::compile::Rascal2muRascal::RascalType;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
import lang::rascalcore::compile::Rascal2muRascal::TypeReifier;

import lang::rascalcore::compile::muRascal::AST;

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
	
MuExp translate(s: (Statement) `assert <Expression expression> ;`) {
    if(assertsEnabled()){
       return muIfelse(translate(expression), 
                       muCon(true),
    				   muCallPrim3("assert_fails", ["astr"], [muCon("")], s@\loc));
    }
    return muBool(true);
}    

MuExp translate(s: (Statement) `assert <Expression expression> : <Expression message>;`) {
    if(assertsEnabled()){
       return muIfelse(translate(expression), 
                       muCon(true),
    			       muCallPrim3("assert_fails", ["astr"], [translate(message)], s@\loc));
    }
    return muBool(true);
}

// -- single expression statement ------------------------------------

MuExp translate(s: (Statement) `<Expression expression> ;`) {
    return translate(expression);
}

// -- visit statement ------------------------------------------------

MuExp translate(s: (Statement) `<Label label> <Visit visitItself>`) = 
    translateVisit(label, visitItself);

// -- while do statement ---------------------------------------------

MuExp translate(s: (Statement) `<Label label> while ( <{Expression ","}+ conditions> ) <Statement body>`) {
    str fuid = topFunctionScope();
    whilename = getLabel(label, "WHILE");
    ifname = nextLabel();
    enterLoop(whilename,fuid);
    enterBacktrackingScope(whilename);
    enterBacktrackingScope(ifname);
    
    loopBody = muWhileDo(whilename, muCon(true), translateConds(ifname, [ c | c <- conditions ], 
                                                                      visit(translateLoopBody(body)) { case muFail(whileName) => muFail(ifname) }, 
                                                                      muBreak(whilename)));
    code = muBlock([]);
    if(containsAppend(body)){     
        writer = muTmpListWriter(nextTmp("writer"), fuid);                                                           
        code = muValueBlock([ muConInit(writer, muCallPrim3("open_list_writer", avalue(), [], [], s@\loc)),
                         loopBody,
                         muCallPrim3("close_list_writer", avalue(), [], [writer], s@\loc)
                       ]);
    } else {
        code =  muValueBlock([ loopBody, muCon([]) ]);
    }
    leaveBacktrackingScope();
    leaveBacktrackingScope();
    leaveLoop();
    return code;
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
MuExp translateTemplate(MuExp template, str indent, s: (StringTemplate) `while ( <Expression condition> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    str fuid = topFunctionScope();
    whilename = nextLabel();
    ifname = nextLabel();
    enterLoop(whilename,fuid);
    enterBacktrackingScope(whilename);
    enterBacktrackingScope(ifname);
    
    loopBody = muWhileDo(whilename, muCon(true), translateConds(ifname, [ condition ],
                                                                      visit( muBlock([ translateStats(preStats),
                                                                                       *translateMiddle(template, indent, body),  
                                                                                       translateStats(postStats) ]) )
                                                                        { case muFail(whileName) => muFail(ifname) }, 
                                                                      muBreak(whilename)));
    code = [ loopBody,  
              muCon(666)// make sure that while returns some value
           ];
    leaveBacktrackingScope();
    leaveBacktrackingScope();
    leaveLoop();
    return muBlock(code);
}

// -- do while statement ---------------------------------------------

MuExp translate(s: (Statement) `<Label label> do <Statement body> while ( <Expression condition> ) ;`) {
    str fuid = topFunctionScope();
    doname = getLabel(label, "DO");
    ifname = nextLabel();
    enterLoop(doname,fuid);
    enterBacktrackingScope(doname);
    enterBacktrackingScope(ifname);
           
    loopBody = muWhileDo(doname, muCon(true), 
                               muBlock([ visit(translateLoopBody(body)) { case muFail(doname) => muFail(ifname) }, 
                                         translateConds(ifname, [condition], muContinue(doname), muBreak(doname)) ]));
    code = muBlock([]);
    if(containsAppend(body)){
        writer = muTmpListWriter(nextTmp("writer"), fuid);        
        code = muValueBlock([ muConInit(writer,muCallPrim3("open_list_writer", avoid(), [], [], s@\loc)), 
                              loopBody,
                              muCallPrim3("close_list_writer", avalue(), [], [writer], s@\loc)
                            ]);
    } else {
        code = muValueBlock([ loopBody, muCon([]) ]);
    }
                          
    leaveBacktrackingScope();
    leaveBacktrackingScope();
    leaveLoop();
    return code;
}

MuExp translateTemplate(MuExp template, str indent, s: (StringTemplate) `do { < Statement* preStats> <StringMiddle body> <Statement* postStats> } while ( <Expression condition> )`) {
    str fuid = topFunctionScope();  
    doname = nextLabel();
    ifname = nextLabel();
    enterLoop(doname,fuid);
    enterBacktrackingScope(doname);
    enterBacktrackingScope(ifname);
    code = [ muWhileDo(doname, muCon(true),
                             muBlock([ translateStats(preStats),
                                       *translateMiddle(template, indent, body),
                                        translateStats(postStats),
                                        translateConds(ifname, [ condition ], muContinue(doname), muBreak(doname) )
                                     ])),  
             muCon(666)	// make sure that do returns some value
           ];
    leaveBacktrackingScope();
    leaveBacktrackingScope();
    leaveLoop();
    return muBlock(code);
}

// -- for statement --------------------------------------------------

MuExp translate(s: (Statement) `<Label label> for ( <{Expression ","}+ generators> ) <Statement body>`) {
    str fuid = topFunctionScope();
    forName = getLabel(label, "FOR");
    enterLoop(forName,fuid);
    enterBacktrackingScope(forName);
    
    loopBody = muEnter(forName, 
                        translateConds(forName, [ c | Expression c <- generators ],
                                             translateLoopBody(body),
                                             muFail(forName)));
    code = muBlock([]);
    if(containsAppend(body)){ 
        writer = muTmpListWriter("listwriter_<forName>", fuid);                         
        code = muValueBlock([ muConInit(writer, muCallPrim3("open_list_writer", avalue(), [], [], s@\loc)),
                              loopBody,
                              muCallPrim3("close_list_writer", avalue(), [], [writer], s@\loc)
                            ]);
    } else {
        code = muValueBlock([ loopBody, muCon([]) ]);
    }
    leaveBacktrackingScope();
    leaveLoop();
    return code;
}

// An (unprecise) check on the occurrence of nested append statements
// A more precise check would look for appends belonging to the current loop statement
bool containsAppend(Statement body) = /(Statement) `append <DataTarget dataTarget> <Statement statement>` := body;

MuExp translateTemplate(MuExp template, str indent, s: (StringTemplate) `for ( <{Expression ","}+ generators> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    str fuid = topFunctionScope();
    forname = nextLabel();
    enterLoop(forname,fuid);
    enterBacktrackingScope(forname);
    code = [ muEnter(forname, translateConds(forname, [ c | Expression c <-generators ],
                                    muBlock([ translateStats(preStats),  
                                              *translateMiddle(template, indent, body),
                                              translateStats(postStats)
                                            ]),
                                    muFail(forname)
                             )),
             muCon("")	// make sure that for returns some value
           ];
    leaveBacktrackingScope();
    leaveLoop();
    return muBlock(code);
} 

// -- if then statement ----------------------------------------------

MuExp translateConds(list[Expression] conds, MuExp trueCont, MuExp falseCont){
     btscope = nextTmp("BT_OUTER");
    return translateConds(btscope, conds, trueCont, falseCont);
}

MuExp translateConds(str btscope, list[Expression] conds, MuExp trueCont, MuExp falseCont){
    if(isEmpty(conds)) return trueCont;
    trueCont = translateBool(conds[-1], btscope, trueCont, falseCont);
    return translateConds(btscope, conds[0..-1], trueCont, falseCont);
}

MuExp updateBTScope(MuExp exp, str fromScope, str toScope)
    = visit(exp){
        //case muSucceed(fromScope) => muSucceed(toScope)
        case muContinue(fromScope) => muContinue(toScope)
        case muFail(fromScope) => muFail(toScope)
    };

MuExp translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement>`) {
    ifname = getLabel(label, "IF");
    enterBacktrackingScope(ifname);
    code = translateConds(ifname, [c | Expression c <- conditions], translate(thenStatement), muBlock([]));
    leaveBacktrackingScope();
    return code;
}

MuExp translateTemplate(MuExp template, str indent, s: (StringTemplate) `if (<{Expression ","}+ conditions> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    str fuid = topFunctionScope();
    ifname = nextLabel();
    enterBacktrackingScope(ifname);
    code = [ translateConds(ifname,  [ c | Expression c <- conditions ], 
                      muBlock([ translateStats(preStats),
                                *translateMiddle(template, indent, body),
                                translateStats(postStats)
                              ]),
                      muBlock([])),
             muCon("")
           ];
    leaveBacktrackingScope();
    return muBlock(code);
}    

// -- if then else statement -----------------------------------------

MuExp translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement> else <Statement elseStatement>`) {
    ifname = getLabel(label, "IF");
    enterBacktrackingScope(ifname);
    code = translateConds(ifname, [c | Expression c <- conditions], translate(thenStatement), translate(elseStatement));
    leaveBacktrackingScope();
    return code;
}

MuExp translateTemplate(MuExp template, str indent, s: (StringTemplate) `if ( <{Expression ","}+ conditions> ) { <Statement* preStatsThen> <StringMiddle thenString> <Statement* postStatsThen> }  else { <Statement* preStatsElse> <StringMiddle elseString> <Statement* postStatsElse> }`){
    str fuid = topFunctionScope();                    
    ifname = nextLabel();
    code = [ translateConds(ifname, [ c | Expression c <- conditions ], 
                      { enterBacktrackingScope(ifname);
                        muBlock([ translateStats(preStatsThen), 
                                  *translateMiddle(template, indent, thenString),
                                  translateStats(postStatsThen)
                                ]);
                      },
                      { enterBacktrackingScope(ifname);
                        muBlock([ translateStats(preStatsElse), 
                                  *translateMiddle(template, indent, elseString),
                                  translateStats(postStatsElse)
                                ]);
                      })
           ];
    leaveBacktrackingScope();
    return muValueBlock(code);                                             
} 

// -- switch statement -----------------------------------------------

MuExp translate(s: (Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`) = translateSwitch(s);

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
    switchname = getLabel(label, "SWITCH");
    switchval = muTmp(nextTmp("switchval"), fuid, getType(expression));
    the_cases = [ c | Case c <- cases ];

    useConcreteFingerprint = hasConcretePatternsOnly(the_cases);
    <case_code, default_code> = translateSwitchCases(switchval, fuid, useConcreteFingerprint, the_cases, muSucceedSwitchCase());
    return muSwitch(muConInit(switchval, translate(expression)), case_code, default_code, useConcreteFingerprint);
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

bool isSpoiler(Pattern pattern, int fp){
    if(fp == fingerprintDefault)
    	return true;
	if(pattern is variableBecomes || pattern is typedVariableBecomes)
		return isSpoiler(pattern.pattern, fp);
	if(pattern is splice || pattern is splicePlus || pattern is asType) 
		return isSpoiler(pattern.argument, fp);
		
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

map[int, MuExp] addPatternWithActionCode(MuExp switchval, str fuid, bool useConcreteFingerprint, PatternWithAction pwa, map[int, MuExp] table, int key, MuExp succeedCase){
	if(pwa is arbitrary){
	   ifname = nextLabel();
       enterBacktrackingScope(ifname);
	   if(pwa.pattern is literal && !pwa.pattern.literal is regExp){
	         table[key] = muIfelse(muEqual(translate(pwa.pattern.literal), switchval),
	                               muBlock([translate(pwa.statement), succeedCase]),
	                               table[key] ?  muFailCase());
	                                       //{ enterBacktrackingScope(ifname);  translate(pwa.statement) ; }, 
                                        //   { leaveBacktrackingScope();  table[key] ?  muFailCase(); }); 
       } else {
             table[key] = translatePat(pwa.pattern, avalue(), switchval, ifname,
                                           { enterBacktrackingScope(ifname);  muBlock([translate(pwa.statement), succeedCase]) ; }, 
                                           { leaveBacktrackingScope();  table[key] ?  muFailCase(); });
       }
       leaveBacktrackingScope(); 
	 } else {
	    ifname2 = nextLabel();
	    replacement= muTmp(nextTmp("replacement"), fuid, getType(pwa.replacement.replacementExpression));
	    replacementCode = translate(pwa.replacement.replacementExpression);
        list[Expression] conditions = [];
        if(pwa.replacement is conditional) {
            conditions = [ e | Expression e <- pwa.replacement.conditions ];
        }
        replcond = muValueIsSubTypeOfValue(replacement, switchval);
                                                      
        table[key] = muBlock([ translateConds(conditions, 
                                      muBlock([ muVarInit(replacement, replacementCode),
                                                  //   muInsert(replacement),
                                                muIfelse( replcond, muInsert(replacement), muFailCase())
                                              ]),
                                      muFailCase()), 
                                table[key] ? muBlock([]) /*muInsert(replacement)*/
                                ]);                                      
	 }
	     
	 return table;
}

private int fingerprintDefault = 0; //getFingerprint("default", false);

tuple[list[MuCase], MuExp] translateSwitchCases(MuExp switchval, str fuid, bool useConcreteFingerprint, list[Case] cases, MuExp succeedCase) {
  map[int,MuExp] table = ();		// label + generated code per case
  
  default_code = muBlock([]); //muAssignTmp(switchval, fuid, muCon(777));	// default code for default case
   
  for(c <- reverse(cases)){
	  if(c is patternWithAction){
	    pwa = c.patternWithAction;
	    key = fingerprint(pwa.pattern, getType(pwa.pattern), useConcreteFingerprint);
	    if(!isSpoiler(c.patternWithAction.pattern, key)){
	       table = addPatternWithActionCode(switchval, fuid, useConcreteFingerprint, pwa, table, key, succeedCase);
	    }
	  } else {
	       default_code = muBlock([translate(c.statement), succeedCase]); //muBlock([muAssignTmp(switchval, fuid, translate(c.statement)), muCon(true)]);
	  }
   }
   default_table = (fingerprintDefault : default_code);
   for(c <- reverse(cases), c is patternWithAction, isSpoiler(c.patternWithAction.pattern, fingerprint(c.patternWithAction.pattern, getType(c.patternWithAction.pattern), useConcreteFingerprint))){
	  default_table = addPatternWithActionCode(switchval, fuid, useConcreteFingerprint, c.patternWithAction, default_table, fingerprintDefault, succeedCase);
   }
   
   //println("TABLE DOMAIN(<size(table)>): <domain(table)>");
   return < [ muCase(key, table[key]) | key <- table], default_table[fingerprintDefault] >;
}


// -- fail statement -------------------------------------------------

MuExp translate(s: (Statement) `fail <Target target> ;`) {
    if(inBacktrackingScope()){
        return target is empty ? muFail(currentBacktrackingScope())
                                : haveEnteredBacktrackingScope("<target.name>") ? muFail("<target.name>") : muFailReturn();
    }
}
                          
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

// TODO result variable should be initialized
MuExp translateSolve(s: (Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`) {
   str fuid = topFunctionScope();
   iterations = muTmpInt(nextTmp("iterations"), fuid);  // count number of iterations
   change = muTmpBool(nextTmp("change"), fuid);		       // keep track of any changed value
   result = muVar(nextTmp("result"), fuid, 0, getType(body));		       // result of body computation
 
   vars = [ var | QualifiedName var <- variables];
   varCode = [ translate(var) | QualifiedName var <- variables ];
   //println("varCode: <varCode>");
   varTmps = [ nextTmp("<var>") | QualifiedName var <- variables ];
   
   return muValueBlock([ muVarInit(iterations, (bound is empty) ? muCon(1000000) : translate(bound.expression)),
    				     muRequire(muNotNegative(iterations), "Negative bound in solve", bound@\loc),
                         muVarInit(change, muCon(true)),
                         muWhileDo(nextLabel("while"),
                            muAnd(change, muGreaterEqInt(iterations, muCon(0))), 
                            muBlock([ muAssign(change, muCon(false)),
                                      *[ muVarInit(muTmp(varTmps[i], fuid, getType(vars[i])), varCode[i]) | int i <- index(varCode) ],
                                      muVarInit(result, translateLoopBody(body)),
                                     *[ muIf(muCallPrim3("notequal", ["avalue", "avalue"], [muTmp(varTmps[i],fuid, getType(vars[i])), varCode[i]], bound@\loc), muAssign(change, muCon(true))) 
                 			          | int i <- index(varCode)    //TODO: prefer index(variables) here
                 			          ],
                                      muIncVar(iterations, muCon(-1)) 
                                    ])),
                         result
                       ]);
}

// -- try statement --------------------------------------------------

MuExp translate(s: (Statement) `try <Statement body> <Catch+ handlers>`)
    = translateTry(body, [handler | handler <- handlers], [Statement]";");

MuExp translate(s: (Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`)
    = translateTry(body, [handler | handler <- handlers], finallyBody);

MuExp translateTry(Statement body, list[Catch] handlers, Statement finallyBody){
    if(body is emptyStatement){
        return muCon(666);
    }
    list[Catch] defaultCases = [ handler | Catch handler <- handlers, handler is \default ];
    list[Catch] otherCases   = [ handler | Catch handler <- handlers, !(handler is \default) ];
    patterns = [ handler.pattern | Catch handler <- otherCases ];
    
    // If there is no default catch, compute lub of pattern types,
    // this gives optimization of the handler search based on types
    lubOfPatterns = !isEmpty(defaultCases) ? avalue() : avoid();
    if(isEmpty(defaultCases)) {
        lubOfPatterns = ( lubOfPatterns | alub(it, getType(p@\loc)) | Pattern p <- patterns );
    }
    // Introduce temporary variables that are bound within a catch block to a thrown exception and to its contained value
    str fuid = topFunctionScope();
    tmp = nextTmp("thrown");
    thrown = muTmp(tmp, fuid, lubOfPatterns);
    thrown_as_exception = muTmpException("<tmp>_as_exception", fuid);
    bigCatch = muCatch(thrown_as_exception, thrown, translateCatches(thrown_as_exception, thrown, handlers));
    return muTry(translate(body), bigCatch, translate(finallyBody));
}

MuExp translateCatches(MuExp thrown_as_exception, MuExp thrown, list[Catch] catches) {
  // Translate a list of catch blocks into one catch block
 
  catch_code =
      for(Catch c <- reverse(catches)){
          trBody = c.body is emptyStatement ? muBlock([]) : translate(c.body);
          exp = muBlock([]);
          if(c is binding) {
              ifname = nextLabel();
              enterBacktrackingScope(ifname);
              patType = getType(c.pattern);
              if(c.pattern is literal) {
                  exp = muIf(muEqual(thrown, translate(c.pattern.literal)), trBody);
              } else if(c.pattern is typedVariable) {
                  varType = translateType(c.pattern.\type);
                  <fuid, pos> = getVariableScope("<c.pattern.name>", c.pattern.name@\loc);
                  patVar = muVar("<c.pattern.name>", fuid, pos, varType);
                  exp = muIf(muValueIsSubType(thrown, varType), 
                                 muBlock([ muVarInit(patVar, thrown), trBody ]));
                                
              } else if(c.pattern is qualifiedName){	// TODO: what if qualifiedName already has a value? Check it!
                  varType = getType(c.pattern);
                  <fuid,pos> = getVariableScope("<c.pattern.qualifiedName>", c.pattern.qualifiedName@\loc);
                  patVar = muVar("<c.pattern.qualifiedName>", fuid, pos, varType);
                  exp = muBlock([muAssign(patVar, thrown), trBody]);
              } else {
                  exp = translatePat(c.pattern, patType, thrown, ifname, trBody, muBlock([]));
              }
              exp = muIf(muValueIsSubType(thrown, patType), exp);
              leaveBacktrackingScope();
          } else {
            exp = muBlock([exp, trBody]);
          }
          append exp;
       }
   // In case there is no default catch provided, re-throw the value from the catch block
   return muBlock(catch_code + muThrow(thrown_as_exception, |unknown:///|));
}

// -- labeled statement ----------------------------------------------

MuExp translate(s: (Statement) `<Label label> { <Statement+ statements> }`) =
    muBlock([translate(stat) | Statement stat <- statements]);

// -- assignment statement -------------------------------------------    

MuExp translate(s: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`) = 
    translateAssignment(s); 

MuExp translateAssignment(s: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`) =
    assignTo(assignable, "<operator>", getType(statement), translate(statement));

// apply assignment operator 
    
MuExp applyOperator(str operator, Assignable assignable, AType rhs_type, MuExp rhs) {
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
    
    assert size(oldval) == 1 : "applyOperator";
    return muCallPrim3(op1, getType(assignable), [getType(assignable), rhs_type],  [*oldval, rhs], assignable@\loc); 
}

str getAssignOp(str operator){
    return  ("=" : "replace", "+=" : "add", "-=" : "subtract", "*=" : "product", "/=" : "divide", "&=" : "intersect")[operator]; 
}
    
// assignTo: assign the rhs of the assignment (possibly modified by an assign operator) to the assignable
    
MuExp assignTo(a: (Assignable) `<QualifiedName qualifiedName>`, str operator, AType rhs_type, MuExp rhs) {
    return mkAssign("<qualifiedName>", qualifiedName@\loc, applyOperator(operator, a, rhs_type, rhs));
}

MuExp assignTo(a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, str operator,  AType rhs_type, MuExp rhs) =
     assignTo(receiver, "=", rhs_type, muCallPrim3("update", getType(receiver), [getType(receiver)], [*getValues(receiver), translate(subscript), applyOperator(operator, a, rhs_type, rhs)], a@\loc));
    
MuExp assignTo(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, str operator,  AType rhs_type, MuExp rhs) =
    assignTo(receiver, "=", rhs_type, muCallPrim3("<getOuterType(receiver)>_slice_<getAssignOp(operator)>", getType(receiver), [getType(receiver)], [*getValues(receiver), translateOpt(optFirst), muNoValue(), translateOpt(optLast), rhs], a@\loc) );

MuExp assignTo(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`, str operator,  AType rhs_type, MuExp rhs) =
     assignTo(receiver, "=", rhs_type, muCallPrim3("<getOuterType(receiver)>_slice_<getAssignOp(operator)>", getType(receiver), [getType(receiver)], [*getValues(receiver), translateOpt(optFirst), translate(second), translateOpt(optLast), rhs], a@\loc));

MuExp assignTo(a: (Assignable) `<Assignable receiver> . <Name field>`, str operator,  AType rhs_type, MuExp rhs) =
     getOuterType(receiver) == "tuple" 
     ? assignTo(receiver,  "=", rhs_type, muCallPrim3("<getOuterType(receiver)>_update", [*getValues(receiver), muCon(getTupleFieldIndex(getType(receiver@\loc), "<field>")), applyOperator(operator, a, rhs_type, rhs)], a@\loc) )
     : assignTo(receiver, "=", rhs_type, muFieldUpdate(getOuterType(receiver), getType(receiver), getValues(receiver)[0], "<field>", applyOperator(operator, a, rhs_type, rhs)) );

MuExp assignTo(Assignable a: (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, str operator,  AType rhs_type, MuExp rhs) = 
    assignTo(receiver,  "=", rhs_type, applyOperator(operator, a, rhs_type, rhs));
    

MuExp assignTo(a: (Assignable) `\<  <{Assignable ","}+ elements> \>`, str operator,  AType rhs_type, MuExp rhs) {
    str fuid = topFunctionScope();
    elems = [ e | Assignable e <- elements];   // hack since elements[i] yields a value result;
    nelems = size(elems); // size_assignables
    
    tmp = muTmp(nextTmp(), fuid, rhs_type);
 
    return muBlock( muVarInit(tmp, applyOperator(operator, a, rhs_type, rhs)) + 
                    [ assignTo(elems[i], "=", rhs_type, muCallPrim3("subscript", getType(elems[i]), [getType(a), aint()], [tmp, muCon(i)], a@\loc) )
                    | i <- [0 .. nelems]
                    ]);
}

MuExp assignTo(Assignable a: (Assignable) `<Name name> ( <{Assignable ","}+ arguments> )`, str operator,  AType rhs_type, MuExp rhs) { 
    str fuid = topFunctionScope();
    elems = [ e | Assignable e <- arguments];  // hack since elements[i] yields a value result;
    nelems = size(arguments);// size_assignables
    tmp = muTmp(nextTmp(), fuid);
   
    return muBlock( muVarInit(tmp, applyOperator(operator, a, rhs_type, rhs)) + 
                    [ assignTo(elems[i], "=", rhs_type, muCallPrim3("subscript", ["aadt", "aint"], [tmp, muCon(i)], a@\loc) )
                    | i <- [0 .. nelems]
                    ]);
}

MuExp assignTo(Assignable a: (Assignable) `<Assignable receiver>@<Name annotation>`,  str operator,  AType rhs_type, MuExp rhs) =
     assignTo(receiver, "=", rhs_type, muCallPrim3("annotation_set", [*getValues(receiver), muCon("<annotation>"), applyOperator(operator, a, rhs_type, rhs)], a@\loc));

// getValues: get the current value(s) of an assignable

list[MuExp] getValues(Assignable a: (Assignable) `<QualifiedName qualifiedName>`) = 
    [ mkVar("<qualifiedName>", qualifiedName@\loc) ];
    
list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`) {
    return [ muCallPrim3("subscript", getType(a), [getType(receiver), getType(subscript)], [*getValues(receiver), translate(subscript)], a@\loc) ];
    //otr = getOuterType(receiver);
    //subscript_op = "<otr>_subscript";
    //if(otr notin {"map"}){
    //   subscript_op += "_<getOuterType(subscript)>";
    //}
    //return [ muCallPrim3(subscript_op, [*getValues(receiver), translate(subscript)], a@\loc) ];
}
    
list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) = 
     [ muCallPrim3("<getOuterType(receiver)>_slice", [ *getValues(receiver), translateOpt(optFirst), muCon("false"), translateOpt(optLast) ], a@\loc) ];
    
list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`) = 
     [ muCallPrim3("<getOuterType(receiver)>_slice", [ *getValues(receiver), translateOpt(optFirst),  translate(second), translateOpt(optLast) ], a@\loc) ];

list[MuExp] getValues(Assignable a:(Assignable) `<Assignable receiver> . <Name field>`) { 
    outerType = getOuterType(receiver);
    cde = outerType == "adt" ? [ muCon(getConstantConstructorDefaultExpressions(receiver@\loc)) ] : [ ];
   
    return [ muCallPrim3("<outerType>_field_access", [ *getValues(receiver), muCon(unescape("<field>")), *cde], a@\loc) ];
}    

list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`) = 
     [ translateIfDefinedOtherwise(getValues(receiver)[0], translate(defaultExpression), a@\loc) ];

list[MuExp] getValues(Assignable a:(Assignable) `\<  <{Assignable ","}+ elements > \>` ) = [ *getValues(elm) | Assignable elm <- elements ];

list[MuExp] getValues(Assignable a:(Assignable) `<Name name> ( <{Assignable ","}+ arguments> )` ) = [ *getValues(arg) | Assignable arg <- arguments ];

list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver>@<Name annotation>`) = 
    [ muCallPrim3("annotation_get", [ *getValues(receiver), muCon("<annotation>")], a@\loc) ];

default list[MuExp] getValues(Assignable a) {
    throw "getValues: unhandled case <a>";
}

// getReceiver: get the final receiver of an assignable

Assignable getReceiver(Assignable a: (Assignable) `<QualifiedName qualifiedName>`) = a;
Assignable getReceiver(Assignable a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`) = getReceiver(receiver);
Assignable getReceiver(Assignable a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) = getReceiver(receiver);
Assignable getReceiver(Assignable a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`) = getReceiver(receiver);  
Assignable getReceiver(Assignable a: (Assignable) `<Assignable receiver> . <Name field>`) = getReceiver(receiver); 
Assignable getReceiver(Assignable a: (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`) = getReceiver(receiver); 
Assignable getReceiver(Assignable a: (Assignable) `<Name name> ( <{Assignable ","}+ arguments> )`) = a;
Assignable getReceiver(Assignable a: (Assignable) `\< <{Assignable ","}+ elements> \>`) =  a;
Assignable getReceiver(Assignable a: (Assignable) `<Assignable receiver>@<Name annotation>`) = getReceiver(receiver); 

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
		return muValueBlock( [ muConInit(muTmp(varname, fuid), translate(statement)), muReturn1(muTmp(varname,fuid)) ]);
	} 
	return muReturn1(translate(statement));
}

// -- throw statement ------------------------------------------------

MuExp translate(s: (Statement) `throw <Statement statement>`) = 
    muThrow(translate(statement),s@\loc);

MuExp translate(s: (Statement) `insert <DataTarget dataTarget> <Statement statement>`) // TODO: handle dataTarget
	= { fillCaseType(getType(s));//getType(statement@\loc)); 
	    muInsert(translate(statement));
	  };

// -- append statement -----------------------------------------------

MuExp translate(s: (Statement) `append <DataTarget dataTarget> <Statement statement>`) {
   fuid = getCurrentLoopScope(dataTarget);
   target = "listwriter_<currentLoop(dataTarget)>" ;
   return muCallPrim3("add_list_writer", getType(s), [getType(statement)], [muTmp(target, fuid, getType(statement)), translate(statement)], s@\loc);
}

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

MuExp returnFromFunction(MuExp body, bool isMemo, loc src) {
  res = muReturn1(body);
  if(isMemo){
     res = visit(res){
     	case muReturn1(e) => muReturn1(muCallPrim3("memoize", [body], src))
     }
  }
  return res;   
}
         
MuExp functionBody(MuExp body, bool isMemo, loc src) =
   isMemo ? muValueBlock([muCallPrim3("check_memo", [body], src), body])  // Directly mapped to the CHECKMEMO instruction that returns when args are in cache
          : body;

MuExp translateFormals(list[Pattern] formals, bool isVarArgs, bool isMemo, int i, node body, list[Expression] when_conditions, loc src){
   if(isEmpty(formals)) {
      if(isEmpty(when_conditions)){
        return muBlock([ returnFromFunction(translateFunctionBody(body), isMemo, src) ]);
    } else {
        ifname = nextLabel();
        enterBacktrackingScope(ifname);
        conditions = [ translate(cond) | Expression cond <- when_conditions];
        // TODO
        mubody = muValueBlock([muIfelse(ifname,makeBoolExp("ALL",conditions, src), [ returnFromFunction(translateFunctionBody(body), isMemo, src) ], [ muFailReturn() ]) ]);
        leaveBacktrackingScope();
        return mubody;
    }
   }
   pat = formals[0];
   
   if(pat is literal){
     // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
      ifname = nextLabel();
      enterBacktrackingScope(ifname);
      
      patTest =  pat.literal is regExp ? muMulti(muApply(translatePat(pat, getType(pat@\loc)), [muVar("$<i>",topFunctionScope(),i) ]))
                                       : muEqual(muVar("$<i>",topFunctionScope(),i, getType(formals[i])), translate(pat.literal));
      
      exp = muIfelse(patTest, translateFormals(tail(formals), isVarArgs, isMemo, i + 1, body, when_conditions, src),
                              muFailReturn()
                  );
      leaveBacktrackingScope();
      return exp;
   } else {
      Name name = pat.name;
      tp = pat.\type;
      fuid = getVariableScope("<name>", name@\loc);
      pos = getPositionInScope("<name>", name@\loc);
      // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
      ifname = nextLabel();
      enterBacktrackingScope(ifname);
     
                                     
      exp = muBlock([ muCheckArgTypeAndCopy("<name>", i, (isVarArgs && size(formals) == 1) ? alist(translateType(tp)) : translateType(tp), pos),
                      translateFormals(tail(formals), isVarArgs, isMemo, i + 1, body, when_conditions, src)
                    ]);
      leaveBacktrackingScope();
      //println("translateFormals returns:");
      //iprintln(exp);
      return exp;
    }
}

MuExp translateFunction(str fname, {Pattern ","}* formals, bool isVarArgs, node body, bool isMemo, list[Expression] when_conditions){
  bool simpleArgs = true;
  for(pat <- formals){
      if(!(pat is typedVariable || pat is literal))
      simpleArgs = false;
  }
  if(simpleArgs) { //TODO: should be: all(pat <- formals, (pat is typedVariable || pat is literal))) {
     return functionBody(muIfelse( muCon(true), muBlock([ translateFormals([formal | formal <- formals], isVarArgs, isMemo, 0, /*kwps,*/ body, when_conditions, formals@\loc)]), muFailReturn()),
                         isMemo, formals@\loc);
  } else {
     //list[MuExp] conditions = [];
     //int i = 0;
     // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
     enterBacktrackingScope(fname);
     // TODO: account for a variable number of arguments
       formalsList = [f | f <- formals];
       
      conditions = (returnFromFunction(translateFunctionBody(body), isMemo, formals@\loc)
                   | translatePat(formalsList[i], getType(formalsList[i]), muVar("$<i>",topFunctionScope(),i, avalue()), fname, it, muFailReturn()) 
                   | i <- index(formalsList));
      mubody = functionBody(conditions, isMemo, formals@\loc);
      leaveBacktrackingScope();
      return mubody;
      
     //for(Pattern pat <- formals) {
     //    conditions += muMulti(muApply(translatePat(pat, avalue()), [ muVar("$<i>",topFunctionScope(),i) ]));
     //    i += 1;
     // };
     // conditions += [ translate(cond) | cond <- when_conditions];
// TODO
      //mubody = functionBody(muBlock([ muIfelse(fname, makeBoolExp("ALL",conditions, formals@\loc), [ returnFromFunction(translateFunctionBody(body), isMemo, formals@\loc) ], [ muFailReturn() ])]),
      //                      isMemo, formals@\loc);
      //leaveBacktrackingScope();
      //return mubody;
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
    	return muBlock([ translate(stat) | Statement stat <- stats ]);
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
