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
    				   muCallPrim3("assert_fails", abool(), [astr()], [muCon("")], s@\loc));
    }
    return muCon(true);
}    

MuExp translate(s: (Statement) `assert <Expression expression> : <Expression message>;`) {
    if(assertsEnabled()){
       return muIfelse(translate(expression), 
                       muCon(true),
    			       muCallPrim3("assert_fails", abool(), [astr()], [translate(message)], s@\loc));
    }
    return muCon(true);
}

// -- single expression statement ------------------------------------

MuExp translate((Statement) `<Expression expression> ;`) {
    return translate(expression);
}

// -- visit statement ------------------------------------------------

MuExp translate((Statement) `<Label label> <Visit visitItself>`) = 
    translateVisit(label, visitItself);

// -- while statement -------------------------------------------------

MuExp translate(s: (Statement) `<Label label> while ( <{Expression ","}+ conditions> ) <Statement body>`) {
    whileName = getLabel(label, "WHILE");
    str fuid = topFunctionScope();
    enterLoop(whileName,fuid);
    
    whileType = getType(s@\loc);
    
    btfree = all(Expression c <- conditions, backtrackFree(c));
    enterBacktrackingScope(whileName);
    
    loopBody = muWhileDo(whileName, muCon(true), translateAndConds(whileName, [ c | c <- conditions ], translateLoopBody(body), muBreak(whileName), normalize=toStat));
    code = muBlock([]);
    if(containsAppend(body)){     
        writer = muTmpListWriter("listwriter_<whileName>", fuid);                                                           
        code = muValueBlock(getType(s),
                       [ muConInit(writer, muCallPrim3("open_list_writer", avalue(), [], [], s@\loc)),
                         loopBody,
                         muCallPrim3("close_list_writer", avalue(), [avalue()], [writer], s@\loc)
                       ]);
    } else {
        code = loopBody;
    }
    if(!btfree){
        code = updateBTScope(code, whileName, currentBacktrackingScope());
    }
    
    leaveBacktrackingScope(whileName);
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
MuExp translateTemplate(MuExp template, str indent, (StringTemplate) `while ( <Expression condition> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    whileName = nextLabel();
    str fuid = topFunctionScope();
    enterLoop(whileName,fuid);
    
    btfree = backtrackFree(condition);
    enterBacktrackingScope(whileName);
    
    code = muWhileDo(whileName, 
                     muCon(true), 
                     translateAndConds(whileName,
                                       [ condition ],
                                       muBlock([ translateStats(preStats), *translateMiddle(template, indent, body), translateStats(postStats) ]), 
                                       muBreak(whileName),
                                       normalize=toStat));
    if(!btfree){
        code = updateBTScope(code, whileName, currentBacktrackingScope());
    }
    leaveBacktrackingScope(whileName);
    leaveLoop();
    return code;
}

// -- do while statement ---------------------------------------------

MuExp translate(s: (Statement) `<Label label> do <Statement body> while ( <Expression condition> ) ;`) {
    doName = getLabel(label, "DO");
    str fuid = topFunctionScope();
    enterLoop(doName,fuid);
   
    enterBacktrackingScope(doName);
           
    loopBody = muWhileDo(doName, 
                         muCon(true), 
                         muBlock([ translateLoopBody(body), translateAndConds(doName, [condition], muContinue(doName), muBreak(doName), normalize=toStat) ]));
    code = muBlock([]);
    if(containsAppend(body)){
        writer = muTmpListWriter("listwriter_<doName>", fuid);        
        code = muValueBlock(getType(s),
                            [ muConInit(writer,muCallPrim3("open_list_writer", avalue(), [], [], s@\loc)), 
                              loopBody,
                              muCallPrim3("close_list_writer", avalue(), [avalue()], [writer], s@\loc)
                            ]);
    } else {
        code = loopBody;
    }
                          
    leaveBacktrackingScope(doName);
    leaveLoop();
    return code;
}

MuExp translateTemplate(MuExp template, str indent, (StringTemplate) `do { < Statement* preStats> <StringMiddle body> <Statement* postStats> } while ( <Expression condition> )`) {
    doName = nextLabel();
    str fuid = topFunctionScope();  
    enterLoop(doName,fuid);
   
    enterBacktrackingScope(doName);
    code = muWhileDo(doName, 
                     muCon(true),
                     muBlock([ translateStats(preStats),
                               *translateMiddle(template, indent, body),
                               translateStats(postStats),
                               translateAndConds(doName, [ condition ], muContinue(doName), muBreak(doName), normalize=toStat )
                             ]));
          
    leaveBacktrackingScope(doName);
    leaveLoop();
    return code;
}

// -- for statement --------------------------------------------------

MuExp translate(s: (Statement) `<Label label> for ( <{Expression ","}+ generators> ) <Statement body>`) {
    forName = getLabel(label, "FOR");
    str fuid = topFunctionScope();
    enterLoop(forName,fuid);
    
    btfree = all(Expression c <- generators, backtrackFree(c));
    enterBacktrackingScope(forName);
    
    loopBody = muEnter(forName, translateAndConds(forName, [ c | Expression c <- generators ], translateLoopBody(body), muFailEnd(forName)));
    code = muBlock([]);
    if(containsAppend(body)){ 
        writer = muTmpListWriter("listwriter_<forName>", fuid);                         
        code = muValueBlock(getType(s),
                            [ muConInit(writer, muCallPrim3("open_list_writer", avalue(), [], [], s@\loc)),
                              loopBody,
                              muCallPrim3("close_list_writer", avalue(), [avalue()], [writer], s@\loc)
                            ]);
    } else {
        code = muValueBlock(avoid(), [ loopBody, muCon([]) ]);
    }
    if(!btfree){
        code = updateBTScope(code, forName, currentBacktrackingScope());
    }
    leaveBacktrackingScope(forName);
    leaveLoop();
    return code;
}

// An (unprecise) check on the occurrence of nested append statements
// A more precise check would look for appends belonging to the current loop statement
bool containsAppend(Statement body) = /(Statement) `append <DataTarget dataTarget> <Statement statement>` := body;

MuExp translateTemplate(MuExp template, str indent, (StringTemplate) `for ( <{Expression ","}+ generators> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    forName = nextLabel();
    str fuid = topFunctionScope();
    enterLoop(forName,fuid);
    
    btfree = all(Expression c <- generators, backtrackFree(c));
    enterBacktrackingScope(forName);
    
    code = muEnter(forName, 
                   translateAndConds(forName, 
                                  [ c | Expression c <-generators ],
                                  muBlock([ translateStats(preStats),  
                                            *translateMiddle(template, indent, body),
                                            translateStats(postStats)
                                          ]),
                                  muFailEnd(forName)
                                 ));
                  
    if(!btfree){
        code = updateBTScope(code, forName, currentBacktrackingScope());
    }
    leaveBacktrackingScope(forName);
    leaveLoop();
    return code;
} 

// -- if then statement ----------------------------------------------

MuExp translate((Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement>`) {
    ifName = getLabel(label, "IF");
    
    btfree = all(Expression c <- conditions, backtrackFree(c));
    enterBacktrackingScope(ifName);
    
    code = translateAndConds(ifName, [c | Expression c <- conditions], translate(thenStatement), muBlock([]), normalize=toStat);
    if(!btfree){
        code = muEnter(ifName, updateBTScope(code, ifName, currentBacktrackingScope()));  
    }
    leaveBacktrackingScope(ifName);
    return code;
}

MuExp translateTemplate(MuExp template, str indent, (StringTemplate) `if (<{Expression ","}+ conditions> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    ifName = nextLabel();
    
    btfree = all(Expression c <- conditions, backtrackFree(c));
    enterBacktrackingScope(ifName);
    
    code = translateAndConds(ifName, 
                          [ c | Expression c <- conditions ], 
                          muBlock([ translateStats(preStats),
                                    *translateMiddle(template, indent, body),
                                    translateStats(postStats)
                                  ]),
                                  muBlock([]));
                                             
    if(!btfree){
        code = muEnter(ifName, updateBTScope(code, ifName, currentBacktrackingScope()));  
    }               
    leaveBacktrackingScope(ifName);
    return code;
}    

// -- if then else statement -----------------------------------------

MuExp translate((Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement> else <Statement elseStatement>`) {
    ifName = getLabel(label, "IF");
    
    btfree = all(Expression c <- conditions, backtrackFree(c));
    enterBacktrackingScope(ifName);
    
    code = translateAndConds(ifName, [c | Expression c <- conditions], translate(thenStatement), translate(elseStatement), normalize=toStat);
    if(!btfree){
        code = muEnter(ifName, updateBTScope(code, ifName, currentBacktrackingScope())); 
    }
    leaveBacktrackingScope(ifName);
    return code;
}

MuExp translateTemplate(MuExp template, str indent, (StringTemplate) `if ( <{Expression ","}+ conditions> ) { <Statement* preStatsThen> <StringMiddle thenString> <Statement* postStatsThen> }  else { <Statement* preStatsElse> <StringMiddle elseString> <Statement* postStatsElse> }`){              
    ifName = nextLabel();
    
    btfree = all(Expression c <- conditions, backtrackFree(c));
    enterBacktrackingScope(ifName);
    
    code = muValueBlock(astr(), [ translateAndConds(ifName, 
                                                 [ c | Expression c <- conditions ], 
                                                 muBlock([ translateStats(preStatsThen), 
                                                           *translateMiddle(template, indent, thenString),
                                                           translateStats(postStatsThen)
                                                         ]),
                                                 muBlock([ translateStats(preStatsElse), 
                                                           *translateMiddle(template, indent, elseString),
                                                           translateStats(postStatsElse)
                                                          ]))
                                 ]);
    if(!btfree){
        code = muEnter(ifName, updateBTScope(code, ifName, currentBacktrackingScope())); 
    }
    leaveBacktrackingScope(ifName);
    return code;                                             
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
MuExp translateSwitch((Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`) {
    str fuid = topFunctionScope();
    switchname = getLabel(label, "SWITCH");
    switchval = muTmpIValue(nextTmp("switchval"), fuid, getType(expression));
    the_cases = [ c | Case c <- cases ];

    useConcreteFingerprint = hasConcretePatternsOnly(the_cases);
    <case_code, default_code> = translateSwitchCases(switchval, fuid, useConcreteFingerprint, the_cases, muSucceedSwitchCase());
    return muBlock([muConInit(switchval, translate(expression)), muSwitch(switchval, case_code, default_code, useConcreteFingerprint)]);
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
    if(fp == getFingerprintDefault())
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
    ifname = nextLabel();
	if(pwa is arbitrary){
       enterBacktrackingScope(ifname);
	   //if(pwa.pattern is literal && !pwa.pattern.literal is regExp){
	   //      table[key] = muIfelse(muEqual(translate(pwa.pattern.literal), switchval),
	   //                            muBlock([translate(pwa.statement), succeedCase]),
	   //                            table[key] ?  muFailCase());
	   //                                    //{ enterBacktrackingScope(ifname);  translate(pwa.statement) ; }, 
    //                                    //   { leaveBacktrackingScope();  table[key] ?  muFailCase(); }); 
    //   } else {
             table[key] = translatePat(pwa.pattern, avalue(), switchval, ifname,
                                           { enterBacktrackingScope(ifname);  muBlock([translate(pwa.statement), succeedCase]) ; }, 
                                           { leaveBacktrackingScope();  table[key] ?  muFailCase(); });
       //}
       leaveBacktrackingScope(); 
	 } else  {
	    replacement = muTmpIValue(nextTmp("replacement"), fuid, getType(pwa.replacement.replacementExpression));
	    replacementCode = translate(pwa.replacement.replacementExpression);
        list[Expression] conditions = (pwa.replacement is conditional) ? [ e | Expression e <- pwa.replacement.conditions ] : [];
        replcond = muValueIsSubTypeOfValue(replacement, switchval);
        
        table[key] =  translatePat(pwa.pattern, avalue(), switchval, ifname,
                                            translateAndConds(ifname,
                                                    conditions, 
                                                    muBlock([ muVarInit(replacement, replacementCode),
                                                              muIfelse( replcond, muInsert(replacement, replacement.atype), muFailCase())
                                                            ]),
                                                    muFailCase(),
                                                    normalize=toStat), 
                                                    table[key] ? muBlock([]));
                                                      
        //table[key] = muBlock([ translateAndCondAsStat(ifname2,
        //                                            conditions, 
        //                                            muBlock([ muVarInit(replacement, replacementCode),
        //                                                      //   muInsert(replacement),
        //                                                      muIfelse( replcond, muInsert(replacement, replacement.atype), muFailCase())
        //                                                    ]),
        //                                            muFailCase()), 
        //                                            table[key] ? muBlock([]) /*muInsert(replacement)*/
        //                     ]);                                      
	 }
	     
	 return table;
}

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
   default_table = (getFingerprintDefault() : default_code);
   for(c <- reverse(cases), c is patternWithAction, isSpoiler(c.patternWithAction.pattern, fingerprint(c.patternWithAction.pattern, getType(c.patternWithAction.pattern), useConcreteFingerprint))){
	  default_table = addPatternWithActionCode(switchval, fuid, useConcreteFingerprint, c.patternWithAction, default_table, getFingerprintDefault(), succeedCase);
   }
   
   //println("TABLE DOMAIN(<size(table)>): <domain(table)>");
   return < [ muCase(key, table[key]) | key <- table], default_table[getFingerprintDefault()] >;
}

// -- fail statement -------------------------------------------------

MuExp translate(s: (Statement) `fail <Target target> ;`) {
    if(inBacktrackingScope()){
        return target is empty ? muFail(currentBacktrackingScope())
                                : haveEnteredBacktrackingScope("<target.name>") ? muFail("<target.name>") 
                                                                                : muFailReturn(getType(currentFunctionDeclaration()));
    } else {
        return muFailReturn(getType(currentFunctionDeclaration()));
    }
}
                          
// -- break statement ------------------------------------------------

MuExp translate((Statement) `break <Target target> ;`) = 
    muBreak(target is empty ? currentLoop() : "<target.name>");
 
// -- continue statement ---------------------------------------------

MuExp translate((Statement) `continue <Target target> ;`) = 
    muContinue(target is empty ? currentLoop() : "<target.name>");

// -- filter statement -----------------------------------------------

MuExp translate((Statement) `filter ;`) =
    muFilterReturn();

// -- solve statement ------------------------------------------------

MuExp translate(s: (Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`) = 
    translateSolve(s);

// TODO result variable should be initialized
MuExp translateSolve(current:(Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`) {
   str fuid = topFunctionScope();
   iterations = muTmpInt(nextTmp("iterations"), fuid);  // count number of iterations
   change = muTmpBool(nextTmp("change"), fuid);		       // keep track of any changed value
   result = muVar(nextTmp("result"), fuid, 0, getType(body));		       // result of body computation
 
   vars = [ var | QualifiedName var <- variables];
   varCode = [ translate(var) | QualifiedName var <- variables ];
   //println("varCode: <varCode>");
   varTmps = [ nextTmp("<var>") | QualifiedName var <- variables ];
   
   return muValueBlock(getType(current),
                       [ muVarInit(iterations, (bound is empty) ? muCon(1000000) : translate(bound.expression)),
    				     muRequire(muNotNegativeNativeInt(iterations), "Negative bound in solve", bound@\loc),
                         muVarInit(change, muCon(true)),
                         muWhileDo(nextLabel("while"),
                            muAndNativeBool(change, muGreaterEqNativeInt(iterations, muCon(0))), 
                            muBlock([ muAssign(change, muCon(false)),
                                      *[ muVarInit(muTmpIValue(varTmps[i], fuid, getType(vars[i])), varCode[i]) | int i <- index(varCode) ],
                                      muVarInit(result, translateLoopBody(body)),
                                     *[ muIf(muCallPrim3("notequal", abool(), [getType(vars[i]), getType(vars[i])], [muTmpIValue(varTmps[i],fuid, getType(vars[i])), varCode[i]], bound@\loc), muAssign(change, muCon(true))) 
                 			          | int i <- index(varCode)    //TODO: prefer index(variables) here
                 			          ],
                                      muIncNativeInt(iterations, muCon(-1)) 
                                    ])),
                         result
                       ]);
}

// -- try statement --------------------------------------------------

MuExp translate((Statement) `try <Statement body> <Catch+ handlers>`)
    = translateTry(body, [handler | handler <- handlers], [Statement]";");

MuExp translate((Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`)
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
    thrown = muTmpIValue(tmp, fuid, lubOfPatterns);
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

MuExp translate((Statement) `<Label label> { <Statement+ statements> }`) =
    muBlock([translate(stat) | Statement stat <- statements]);

// -- assignment statement -------------------------------------------    

MuExp translate(s: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`) = 
    translateAssignment(s); 

MuExp translateAssignment((Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`) =
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
     getOuterType(receiver) == "atuple" 
     ? assignTo(receiver,  "=", rhs_type, muCallPrim3("update", rhs_type, [getType(receiver)], [*getValues(receiver), muCon(getTupleFieldIndex(getType(receiver@\loc), "<field>")), applyOperator(operator, a, rhs_type, rhs)], a@\loc) )
     : assignTo(receiver, "=", rhs_type, muSetField(getType(a), getType(receiver), getValues(receiver)[0], "<field>", applyOperator(operator, a, rhs_type, rhs)) );

MuExp assignTo(Assignable a: (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, str operator,  AType rhs_type, MuExp rhs) = 
    assignTo(receiver,  "=", rhs_type, applyOperator(operator, a, rhs_type, rhs));
    

MuExp assignTo(a: (Assignable) `\<  <{Assignable ","}+ elements> \>`, str operator,  AType rhs_type, MuExp rhs) {
    str fuid = topFunctionScope();
    elems = [ e | Assignable e <- elements];   // hack since elements[i] yields a value result;
    nelems = size(elems); // size_assignables
    
    tmp = muTmpIValue(nextTmp(), fuid, rhs_type);
 
    return muBlock( muVarInit(tmp, applyOperator(operator, a, rhs_type, rhs)) + 
                    [ assignTo(elems[i], "=", rhs_type, muCallPrim3("subscript", getType(elems[i]), [getType(a), aint()], [tmp, muCon(i)], a@\loc) )
                    | i <- [0 .. nelems]
                    ]);
}

MuExp assignTo(Assignable a: (Assignable) `<Name name> ( <{Assignable ","}+ arguments> )`, str operator,  AType rhs_type, MuExp rhs) { 
    str fuid = topFunctionScope();
    elems = [ e | Assignable e <- arguments];  // hack since elements[i] yields a value result;
    nelems = size(arguments);// size_assignables
    tmp = muTmpIValue(nextTmp(), fuid, rhs_type);
   
    return muBlock( muVarInit(tmp, applyOperator(operator, a, rhs_type, rhs)) + 
                    [ assignTo(elems[i], "=", rhs_type, muCallPrim3("subscript", ["aadt", "aint"], [tmp, muCon(i)], a@\loc) )
                    | i <- [0 .. nelems]
                    ]);
}

MuExp assignTo(Assignable a: (Assignable) `<Assignable receiver>@<Name annotation>`,  str operator,  AType rhs_type, MuExp rhs) =
    assignTo(receiver, "=", rhs_type, muSetAnno(getValues(receiver)[0], getType(a), "<annotation>", applyOperator(operator, a, rhs_type, rhs)));
    //assignTo(receiver, "=", rhs_type, muCallPrim3("annotation_set", [*getValues(receiver), muCon("<annotation>"), applyOperator(operator, a, rhs_type, rhs)], a@\loc));

// getValues: get the current value(s) of an assignable

list[MuExp] getValues((Assignable) `<QualifiedName qualifiedName>`) = 
    [ mkVar("<qualifiedName>", qualifiedName@\loc) ];
    
list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`) {
    return [ muCallPrim3("subscript", getType(a), [getType(receiver), getType(subscript)], [*getValues(receiver), translate(subscript)], a@\loc) ];
}
    
list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) {
    ot = getType(a);
    return [ muCallPrim3("slice", ot, [ot], [ *getValues(receiver), translateOpt(optFirst), muNoValue(), translateOpt(optLast) ], a@\loc) ];
  //   [ muCallPrim3("<getOuterType(receiver)>_slice", [ *getValues(receiver), translateOpt(optFirst), muCon("false"), translateOpt(optLast) ], a@\loc) ];
 }
   
list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`) {
    ot = getType(a);
    return [ muCallPrim3("slice", ot, [ot], [ *getValues(receiver), translateOpt(optFirst),translate(second), translateOpt(optLast) ], a@\loc) ];
    //[ muCallPrim3("<getOuterType(receiver)>_slice", [ *getValues(receiver), translateOpt(optFirst),  translate(second), translateOpt(optLast) ], a@\loc) ];
}

list[MuExp] getValues(Assignable a:(Assignable) `<Assignable receiver> . <Name field>`) { 
    <consType, isKwp> =  getConstructorInfo(getType(receiver), getType(field));
    return [ muGetField(getType(a), consType, getValues(receiver)[0], unescape("<field>")) ];
}    

list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`) = 
     [ translateIfDefinedOtherwise(getValues(receiver)[0], translate(defaultExpression), a@\loc) ];

list[MuExp] getValues((Assignable) `\<  <{Assignable ","}+ elements > \>` ) = [ *getValues(elm) | Assignable elm <- elements ];

list[MuExp] getValues((Assignable) `<Name name> ( <{Assignable ","}+ arguments> )` ) = [ *getValues(arg) | Assignable arg <- arguments ];

list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver>@<Name annotation>`) = 
    [ muGetAnno(getValues(receiver)[0], getType(a), unescape("<annotation>")) ];

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

MuExp translate((Statement) `;`) = 
    muBlock([]);

//MuExp translate(s: (Statement) `global <Type \type> <{QualifiedName ","}+ names> ;`) { throw("globalDirective"); }

// -- return statement -----------------------------------------------

MuExp translate((Statement) `return <Statement statement>`) {
	// If the 'return' is used in the scope of a try-catch-finally block,
	// the respective 'finally' block must be executed before the function returns
	resultType = getType(statement);
	if(hasFinally()) { // TODO adapt
	    str fuid = topFunctionScope();
		str varname = asTmp(nextLabel());
		result = muTmpIValue(nextLabel("result"), fuid, resultType);
		return muValueBlock(resultType, [ muConInit(result, translate(statement)), muReturn1(resultType, result) ]);
	} 
	if((Statement) `<Expression expression>;` := statement && (Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>` := expression){
	   return translateBool(condition, "", muReturn1(resultType, translate(thenExp)), muReturn1(resultType, translate(elseExp)));
	} else {
	   return muReturn1(resultType, translate(statement));
	}
}

// -- throw statement ------------------------------------------------

MuExp translate(s: (Statement) `throw <Statement statement>`) = 
    muThrow(translate(statement),s@\loc);

MuExp translate(s: (Statement) `insert <DataTarget dataTarget> <Statement statement>`) // TODO: handle dataTarget
	= { fillCaseType(getType(s));//getType(statement@\loc)); 
	    muInsert(translate(statement), getType(statement@\loc));
	  };

// -- append statement -----------------------------------------------

MuExp translate(s: (Statement) `append <DataTarget dataTarget> <Statement statement>`) {
   fuid = getCurrentLoopScope(dataTarget);
   target = "listwriter_<currentLoop(dataTarget)>" ;
   return muCallPrim3("add_list_writer", getType(s), [avalue(), getType(statement)], [muTmpIValue(target, fuid, getType(statement)), translate(statement)], s@\loc);
}

// -- local function declaration statement ---------------------------------

MuExp translate(s: (Statement) `<FunctionDeclaration functionDeclaration>`) { 
    translate(functionDeclaration); return muBlock([]); 
}

// -- local variable declaration statement ---------------------------

MuExp translate(s: (Statement) `<LocalVariableDeclaration declaration> ;`) { 
    tp = declaration.declarator.\type;
    {Variable ","}+ variables = declaration.declarator.variables;
    code = for(var <- variables){
    		  append mkAssign("<var.name>", var.name@\loc, var is initialized ? translate(var.initial) : muNoValue());
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
