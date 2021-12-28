@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalStatement

import IO;
import ValueIO;
import Node;
import Map;
import Set;
import String;
import ParseTree;
import util::Math;
import util::Reflective;

import lang::rascal::\syntax::Rascal;

import lang::rascalcore::check::AType;
//import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::Fingerprint;
import lang::rascalcore::check::BacktrackFree;

import lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel;
import lang::rascalcore::compile::Rascal2muRascal::ModuleInfo;
import lang::rascalcore::compile::Rascal2muRascal::RascalDeclaration;
import lang::rascalcore::compile::Rascal2muRascal::RascalExpression;
import lang::rascalcore::compile::Rascal2muRascal::RascalPattern;
import lang::rascalcore::compile::Rascal2muRascal::RascalType;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;

import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::util::Names;

/*
 * Translate Rascal statements to muRascal using the functions:
 * - MuExp translateStats(Statement* statements, ...)
 * - MuExp translate(Statement s, ...).
 */

/********************************************************************/
/*                  Translate statements                            */
/********************************************************************/

MuExp translateStats(Statement* statements, BTSCOPES btscopes) = muBlock([ translate(stat, btscopes) | stat <- statements ]);

// Normalize expression to statement

MuExp toStat(muIfExp(c, t, f)) = muIfElse(c, toStat(t), toStat(f));
default MuExp toStat(MuExp exp) = exp;

/********************************************************************/
/*                  Translate one statement                         */
/********************************************************************/

// -- assert statement -----------------------------------------------
	
MuExp translate(s: (Statement) `assert <Expression expression> ;`, BTSCOPES btscopes) {
    if(assertsEnabled()){
       return muIfExp(translate(expression), 
                       muCon(true),
    				   muPrim("assert_fails", abool(), [astr()], [muCon("")], s@\loc));
    }
    return muCon(true);
}    

MuExp translate(s: (Statement) `assert <Expression expression> : <Expression message>;`, BTSCOPES btscopes) {
    if(assertsEnabled()){
       return muIfExp(translate(expression), 
                       muCon(true),
    			       muPrim("assert_fails", abool(), [astr()], [translate(message)], s@\loc));
    }
    return muCon(true);
}

// -- single expression statement ------------------------------------

MuExp translate((Statement) `<Expression expression> ;`, BTSCOPES btscopes) {
    return translate(expression);
}

// -- visit statement ------------------------------------------------

MuExp translate((Statement) `<Label label> <Visit visitItself>`, BTSCOPES btscopes) = 
    translateVisit(label, visitItself, btscopes);

// -- while statement -------------------------------------------------

MuExp translate(s: (Statement) `<Label label> while ( <{Expression ","}+ conditions> ) <Statement body>`, BTSCOPES btscopes) {
    whileName = getLabel(label, "WHILE");
    whileBT = "<whileName>_BT";
    str fuid = topFunctionScope();
    enterLoop(whileName,fuid);
    
    loopBody = muBlock([]);
    conds = [c | Expression c <- conditions];
    btscopes = getBTScopesAnd(conds, whileBT, btscopes);
    conds = normalizeAnd(conds);
    if(all(Expression c <- conditions, backtrackFree(c)) && isFailFree(body)){
        enterLabelled(label, whileName);
        loopBody = translateLoopBody(body, btscopes);
        falseCont = muBreak(whileName);
        for(int i <- reverse(index(conds))){
            cond = conds[i];
            loopBody = i == 0 ? muWhileDo(whileName, translate(cond), loopBody)
                              : muIfExp(translate(cond), loopBody, falseCont);
        }
        loopBody = muEnter(whileBT, loopBody);
    } else {
        enterLabelled(label, whileName, getResume(conds[-1], btscopes));
        loopBody = muEnter(whileBT, 
                       muWhileDo(whileName, muCon(true), 
                                 translateAndConds(btscopes, 
                                                   conds, 
                                                   muBlock([ translateLoopBody(body, btscopes), muContinue(whileName) ]), 
                                                   muBlock([]), 
                                                   normalize=toStat)
                                )
                       );
    }
    
    code = muBlock([]);
    if(containsAppend(body)){     
        writer = muTmpListWriter("listwriter_<whileName>", fuid);                                                           
        code = muValueBlock(getType(s),
                       [ muConInit(writer, muPrim("open_list_writer", avalue(), [], [], s@\loc)),
                         loopBody,
                         muPrim("close_list_writer", avalue(), [avalue()], [writer], s@\loc)
                       ]);
    } else {
        //code = loopBody;
        code = muValueBlock(getType(s), [ loopBody, muCon([]) ]);
    }
   
    leaveLoop();
    leaveLabelled();
    return code;
}

MuExp translateLoopBody(Statement body, BTSCOPES btscopes){
    return translate(body, btscopes);
}

// Due to the similarity of some statements and their template version, we present both versions together
MuExp translateTemplate(MuExp template, str indent, (StringTemplate) `while ( <Expression condition> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    whileName = nextLabel();
    whileBT = "<whileName>_BT";
    str fuid = topFunctionScope();
    enterLoop(whileName,fuid);
    
    conds = [ condition ];
    btscopes = getBTScopesAnd(conds, whileBT, ());
    
    code = muEnter(whileBT, muWhileDo(whileName, 
                     muCon(true), 
                     //muBlock([ 
                               translateAndConds(btscopes,
                                                 conds,
                                                 muBlock([ translateStats(preStats, btscopes), *translateMiddle(template, indent, body), translateStats(postStats, btscopes), muContinue(whileName) ]), 
                                                 muBreak(whileName),
                                                 normalize=toStat)
                            //   ,
                             //  muBlock([]) // muBreak(whileName)
                            // ])
                             ));
    //iprintln(code);
    leaveLoop();
    return code;
}

// -- do while statement ---------------------------------------------

MuExp translate(s: (Statement) `<Label label> do <Statement body> while ( <Expression condition> ) ;`, BTSCOPES btscopes) {
    doName = getLabel(label, "DO");
    str fuid = topFunctionScope();
    enterLoop(doName,fuid);
    
    conds = [condition];
    btscopes = getBTScopesAnd(conds, doName, btscopes);
           
    loopBody = muWhileDo(doName, 
                         muCon(true), 
                         muBlock([ translateLoopBody(body, btscopes), translateAndConds(btscopes, conds, muContinue(doName), muBreak(doName), normalize=toStat) ]));
    code = muBlock([]);
    if(containsAppend(body)){
        writer = muTmpListWriter("listwriter_<doName>", fuid);        
        code = muValueBlock(getType(s),
                            [ muConInit(writer,muPrim("open_list_writer", avalue(), [], [], s@\loc)), 
                              loopBody,
                              muPrim("close_list_writer", avalue(), [avalue()], [writer], s@\loc)
                            ]);
    } else {
        code = muValueBlock(getType(s), [ loopBody, muCon([]) ]);
    }
                          
    leaveLoop();
    return code;
}

MuExp translateTemplate(MuExp template, str indent, (StringTemplate) `do { < Statement* preStats> <StringMiddle body> <Statement* postStats> } while ( <Expression condition> )`) {
    doName = nextLabel();
    str fuid = topFunctionScope();  
    enterLoop(doName,fuid);
   
    conds = [ condition ];
    btscopes = getBTScopesAnd(conds, doName, ());
    code = muWhileDo(doName, 
                     muCon(true),
                     muBlock([ translateStats(preStats, btscopes),
                               *translateMiddle(template, indent, body),
                               translateStats(postStats, btscopes),
                               translateAndConds(btscopes, conds, muContinue(doName), muBreak(doName), normalize=toStat )
                             ]));
          
    leaveLoop();
    return code;
}

// -- for statement --------------------------------------------------

MuExp translate(s: (Statement) `<Label label> for ( <{Expression ","}+ generators> ) <Statement body>`, BTSCOPES btscopes) {
    forName = getLabel(label, "FOR");
    str fuid = topFunctionScope();
    enterLoop(forName,fuid);
    
    conds = [c | Expression c <- generators];
    btscopes = getBTScopesAnd(conds, forName, btscopes);
   // iprintln(btscopes);
    loopBody = muEnter(forName, translateAndConds(btscopes, conds, translateLoopBody(body, btscopes), muFail(forName)));
    code = muBlock([]);
    if(containsAppend(body)){ 
        writer = muTmpListWriter("listwriter_<forName>", fuid);                         
        code = muValueBlock(getType(s),
                            [ muConInit(writer, muPrim("open_list_writer", avalue(), [], [], s@\loc)),
                              loopBody,
                              muPrim("close_list_writer", avalue(), [avalue()], [writer], s@\loc)
                            ]);
    } else {
        code = muValueBlock(avoid(), [ loopBody, muCon([]) ]);
    }
    
    leaveLoop();
    return code;
}

// An (unprecise) check on the occurrence of nested append statements
// A more precise check would look for appends belonging to the current loop statement
bool containsAppend(Statement body) = /(Statement) `append <DataTarget _> <Statement _>` := body;

MuExp translateTemplate(MuExp template, str indent, (StringTemplate) `for ( <{Expression ","}+ generators> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    forName = nextLabel();
    str fuid = topFunctionScope();
    enterLoop(forName,fuid);
    
    conds = [c | Expression c <- generators];
    btscopes = getBTScopesAnd(conds, forName, ());
    
    code = muEnter(forName, 
                   translateAndConds(btscopes, 
                                  conds,
                                  muBlock([ translateStats(preStats, btscopes),  
                                            *translateMiddle(template, indent, body),
                                            translateStats(postStats, btscopes)
                                          ]),
                                  muFail(forName)
                                 ));
    leaveLoop();
    return code;
} 

// -- if then statement ----------------------------------------------

MuExp translate(s:(Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement>`, BTSCOPES btscopes) {
    ifName = getLabel(label, "IF");
    conds = [c | Expression c <- conditions];
   
    code = muBlock([]);
    if(all(Expression c <- conditions, backtrackFree(c)) && isFailFree(thenStatement)){
        enterLabelled(label, ifName);
        code = translateAndConds(btscopes, 
                                 conds, 
                                 translate(thenStatement, btscopes),
                                 muBlock([]),
                                 normalize=toStat);
    } else {
        btscopes = getBTScopesAnd(conds, ifName, btscopes);
        resume = getResume(normalizeAnd(conds)[-1], btscopes);
        enterLabelled(label, ifName, resume);
        thenCode = translate(thenStatement, btscopes);
        if(hasSequentialExit(thenCode)){
            thenCode = muBlock([thenCode, muFail(ifName)]);
        }
        code = muEnter(ifName, 
                       translateAndConds(btscopes, 
                                         conds, 
                                         thenCode,
                                         muBlock([]),
                                         normalize=toStat));
    }
    leaveLabelled();
    return code;
}

MuExp translateTemplate(MuExp template, str indent, (StringTemplate) `if (<{Expression ","}+ conditions> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    ifName = nextLabel();
    
    conds = [c | Expression c <- conditions];
    btscopes = getBTScopesAnd(conds, ifName, ());
    
    code = translateAndConds(btscopes, 
                          conds, 
                          muBlock([ translateStats(preStats, btscopes),
                                    *translateMiddle(template, indent, body),
                                    translateStats(postStats, btscopes)
                                  ]),
                                  muBlock([]));                                               
    return code;
}    

// -- if then else statement -----------------------------------------

bool isFailFree(Statement s) = /(Statement) `fail <Target _>;` !:= s;

MuExp translate(s:(Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement> else <Statement elseStatement>`, BTSCOPES btscopes) {
    ifName = getLabel(label, "IF");
    conds = [c | Expression c <- conditions];
  
    code = muBlock([]);
    elseCode = translate(elseStatement, btscopes);
    
    if(all(Expression c <- conditions, backtrackFree(c)) && isFailFree(thenStatement)){
        enterLabelled(label, ifName);
        code = translateAndConds(btscopes, 
                                 conds, 
                                 muBlock([translate(thenStatement, btscopes)]), 
                                 elseCode, 
                                 normalize=toStat);
    } else {
        btscopes = getBTScopesAnd(conds, ifName, btscopes);
        resume = getResume(normalizeAnd(conds)[-1], btscopes);
        enterLabelled(label, ifName, resume);
        thenCode = translate(thenStatement, btscopes);
        if(thenCode !:= muBlock([]) && hasSequentialExit(thenCode)){
            thenCode = muBlock([thenCode, muFail(ifName)]);
        }
    
        code = muBlock([muEnter(ifName, 
                                translateAndConds(btscopes, 
                                                  conds, 
                                                  thenCode, 
                                                  muBlock([]), 
                                                  normalize=toStat)),
                       elseCode]);
    }
    leaveLabelled();
    return code;
}

MuExp translateTemplate(MuExp template, str indent, (StringTemplate) `if ( <{Expression ","}+ conditions> ) { <Statement* preStatsThen> <StringMiddle thenString> <Statement* postStatsThen> }  else { <Statement* preStatsElse> <StringMiddle elseString> <Statement* postStatsElse> }`){              
    ifName = nextLabel();
    
    conds = [c | Expression c <- conditions];
    btscopes = getBTScopesAnd(conds, ifName, ());
    
    code = muValueBlock(astr(), [ translateAndConds(btscopes, 
                                                 [ c | Expression c <- conditions ], 
                                                 muBlock([ translateStats(preStatsThen, btscopes), 
                                                           *translateMiddle(template, indent, thenString),
                                                           translateStats(postStatsThen, btscopes)
                                                         ]),
                                                 muBlock([ translateStats(preStatsElse, btscopes), 
                                                           *translateMiddle(template, indent, elseString),
                                                           translateStats(postStatsElse, btscopes)
                                                          ]))
                                 ]); 
    return code;                                             
} 

// -- switch statement -----------------------------------------------

MuExp translate(s: (Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`, BTSCOPES btscopes) = translateSwitch(s, btscopes);

/*
 * Optimized switch translation that uses a SWITCH instruction.
 * A table is constructed that maps a "fingerprint" of the switch value to a label associated with a MuExp to handle that case.
 * Special attention is needed for case patterns that spoil this simple scheme, i.e. they lead to pattern overlap, typically
 * a top level (typed variable) or a regular expression. The overlap between constructors and nodes is also considered carefully:
 * All spoiler cases are prepended to the default case.
 * 
 */
MuExp translateSwitch((Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`, BTSCOPES btscopes) {
    str fuid = topFunctionScope();
    switchName = getLabel(label, "SWITCH");
    switchVal = muTmpIValue(nextTmp("switchVal"), fuid, getType(expression));
    the_cases = [ c | Case c <- cases ];

    useConcreteFingerprint = hasConcretePatternsOnly(the_cases);
    <case_code, default_code> = translateSwitchCases(switchName, switchVal, fuid, useConcreteFingerprint, the_cases, muSucceedSwitchCase(switchName), btscopes);
    //iprintln(case_code);
    //iprintln(default_code);
    
    return muBlock([ muConInit(switchVal, translate(expression)),
                     muSwitch(switchName, switchVal, case_code, default_code, useConcreteFingerprint)
                   ]);
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

map[int, list[MuExp]] addPatternWithActionCode(str switchName, MuExp switchVal, str fuid, PatternWithAction pwa, map[int, list[MuExp]] table, int key, str caseLabel, MuExp succeedCase, BTSCOPES btscopes){
    stringVisitUpdate = inStringVisit() && pwa.pattern is literal && pwa.pattern.literal is string ? [ muStringSetMatchedInVisit(size("<pwa.pattern.literal>") - 2)] : [];
 
	if(pwa is arbitrary){    
        if(backtrackFree(pwa.pattern)){
            statCode = translate(pwa.statement, btscopes);
            if(!noSequentialExit(statCode)){
                statCode = muBlock([statCode, succeedCase]);
            }
            table[key] += [ muIf(muValueIsComparable(switchVal, getType(pwa.pattern)),
                                        muEnter(caseLabel, translatePat(pwa.pattern, getType(switchVal), switchVal, btscopes,
                                                                        muBlock([*stringVisitUpdate, statCode]),
                                                                        muBlock([]))))
                          ];                    
        } else {   
            btscopes1 = getBTScopes(pwa.pattern, caseLabel, btscopes); 
            statCode = translate(pwa.statement, btscopes1);
            if(!noSequentialExit(statCode)){
                statCode = muBlock([statCode, succeedCase]);
            }                                   
            table[key] += [ muIf(muValueIsComparable(switchVal, getType(pwa.pattern)), 
                                        muEnter(caseLabel, translatePat(pwa.pattern, getType(switchVal), switchVal, btscopes1,
                                               statCode,
                                               muBlock([]))))
                          ];  
       }

	 } else  {
	    replacement = muTmpIValue(nextTmp("replacement"), fuid, getType(pwa.replacement.replacementExpression));
	    btscopes1 = getBTScopes(pwa.pattern, caseLabel, btscopes);  
	    replacementCode = translate(pwa.replacement.replacementExpression);
        list[Expression] conditions = (pwa.replacement is conditional) ? [ e | Expression e <- pwa.replacement.conditions ] : [];
        for(cond <- conditions){
            btscopes1 = getBTScopes(cond, caseLabel, btscopes1);
        }
        replcond = muValueIsSubtypeOfValue(replacement, switchVal);
        
        table[key] += [ muEnter(caseLabel, translatePat(pwa.pattern, getType(switchVal), switchVal, btscopes1,
                                            translateAndConds(btscopes1,
                                                    conditions, 
                                                    muBlock([ *stringVisitUpdate,
                                                              muVarInit(replacement, replacementCode), 
                                                              muIfElse( replcond, muInsert(replacement.atype, replacement), muFailCase(switchName))
                                                            ]),
                                                    muBlock([]), //muFailCase(switchName),
                                                    normalize=toStat), 
                                                    muBlock([])))
                           ];                                  
	 }
	     
	 return table;
}

tuple[list[MuCase], MuExp] translateSwitchCases(str switchName, MuExp switchVal, str fuid, bool useConcreteFingerprint, list[Case] cases, MuExp succeedCase, BTSCOPES btscopes) {
  map[int,list[MuExp]] table = ();      // label + generated code per case
  MuExp default_code = muBlock([]); //muSucceedSwitchCase(switchName); // default code for default case
  for(c <- cases){
    if(c is \default){
        default_code = translate(c.statement, btscopes);
    }
  }
  default_table = (getFingerprintDefault() : []);
  for(int i <- index(cases), c := cases[i], !(c is \default), c is patternWithAction, isSpoiler(c.patternWithAction.pattern, fingerprint(c.patternWithAction.pattern, getType(c.patternWithAction.pattern), useConcreteFingerprint))){
      caseLabel = "CASE_< getFingerprintDefault()>_<i>";
      btscopes = getBTScopes(c.patternWithAction.pattern, caseLabel, btscopes);
      default_table = addPatternWithActionCode(switchName, switchVal, fuid, c.patternWithAction, default_table, getFingerprintDefault(), caseLabel, succeedCase, btscopes);
  }
  default_code = muBlock(default_table[getFingerprintDefault()] + default_code);
 
  for(int i <- index(cases)){ //c <- reverse(cases)){
      c = cases[i];
     
	  if(!(c is \default), c is patternWithAction){
	    pwa = c.patternWithAction;
	    key = fingerprint(pwa.pattern, getType(pwa.pattern), useConcreteFingerprint);
	    if(!table[key]?) table[key] = [];
	    caseLabel = "CASE_<abs(key)>_<i>";
	    btscopes = getBTScopes(c.patternWithAction.pattern, caseLabel, btscopes);
	    if(!isSpoiler(c.patternWithAction.pattern, key)){
	       table = addPatternWithActionCode(switchName, switchVal, fuid, pwa, table, key, caseLabel, succeedCase, btscopes);
	    }
	  }
   }
   
   //for(key <- table){
   // table[key] += muSucceedSwitchCase(switchName);
   //}
 
   return < [ muCase(key, muBlock(table[key]))
            | key <- table
            ], default_code 
         >;
}

// -- fail statement -------------------------------------------------

MuExp translate(s: (Statement) `fail <Target target> ;`, BTSCOPES btscopes) {
    if(target is empty){
        <found, resume> = getLabelled();
        if(found){
            return muFail(resume);
        }
        try {
            return muFail(getResume(btscopes)); // "###";
        } catch _: {
            return muFailReturn(getType(currentFunctionDeclaration()));
        }
    }
    <found, resume> = inLabelled("<target.name>");
    if(found){
        return muFail(resume); //muFail(getResume(btscopes));
    } else 
    if(haveEntered("<target.name>", btscopes)){
        return muFail("<target.name>");  //muFail(getResume(btscopes));
    } else {
        return muFailReturn(getType(currentFunctionDeclaration()));
    }
}
                          
// -- break statement ------------------------------------------------

MuExp translate((Statement) `break <Target target> ;`, BTSCOPES btscopes) = 
    muBreak(target is empty ? currentLoop() : "<target.name>");
 
// -- continue statement ---------------------------------------------

MuExp translate((Statement) `continue <Target target> ;`, BTSCOPES btscopes) = 
    muContinue(target is empty ? currentLoop() : "<target.name>");

// -- filter statement -----------------------------------------------

MuExp translate((Statement) `filter ;`, BTSCOPES btscopes) =
    muFilterReturn();

// -- solve statement ------------------------------------------------

MuExp translate(s: (Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`, BTSCOPES btscopes) = 
    translateSolve(s, btscopes);

// TODO result variable should be initialized
MuExp translateSolve((Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`, BTSCOPES btscopes) {
   str fuid = topFunctionScope();
   iterations = muTmpInt(nextTmp("iterations"), fuid);          // count number of iterations
   change = muTmpBool(nextTmp("change"), fuid);		            // keep track of any changed value
   result = muVar(nextTmp("result"), fuid, 0, getType(body));   // result of body computation
 
   vars = [ var | QualifiedName var <- variables];
   varCode = [ translate(var) | QualifiedName var <- variables ];
   //println("varCode: <varCode>");
   varTmps = [ nextTmp("<var>") | QualifiedName var <- variables ];
   
   return muBlock([ muVarInit(iterations, (bound is empty) ? muCon(1000000) : muToNativeInt(translate(bound.expression))),
    				muRequireNonNegativeBound(iterations),
                    muVarInit(change, muCon(true)),
                    muWhileDo(nextLabel("while"),
                        muAndNativeBool(change, muGreaterEqNativeInt(iterations, muCon(0))), 
                        muBlock([ muAssign(change, muCon(false)),
                                  *[ muVarInit(muTmpIValue(varTmps[i], fuid, getType(vars[i])), varCode[i]) | int i <- index(varCode) ],
                                  muVarInit(result, translateLoopBody(body, btscopes)),
                                 *[ muIf(muPrim("notequal", abool(), [getType(vars[i]), getType(vars[i])], [muTmpIValue(varTmps[i],fuid, getType(vars[i])), varCode[i]], bound@\loc), muAssign(change, muCon(true))) 
             			          | int i <- index(varCode)    //TODO: prefer index(variables) here
             			          ],
                                  muIncNativeInt(iterations, muCon(-1)) 
                                ]))
                       ]);
}

// -- try statement --------------------------------------------------

MuExp translate((Statement) `try <Statement body> <Catch+ handlers>`, BTSCOPES btscopes)
    = translateTry(body, [handler | handler <- handlers], [Statement]";", btscopes);

MuExp translate((Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`, BTSCOPES btscopes)
    = translateTry(body, [handler | handler <- handlers], finallyBody, btscopes);

MuExp translateTry(Statement body, list[Catch] handlers, Statement finallyBody, BTSCOPES btscopes){
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
    thrown = muTmpIValue(tmp, fuid, avalue()/*lubOfPatterns*/);
    thrown_as_exception = muTmpException("<tmp>_as_exception", fuid);
    bigCatch = muCatch(thrown_as_exception, thrown, translateCatches(thrown_as_exception, thrown, handlers, btscopes));
    return muTry(translate(body, btscopes), bigCatch, translate(finallyBody, btscopes));
}

MuExp translateCatches(MuExp thrown_as_exception, MuExp thrown, list[Catch] catches, BTSCOPES btscopes) {
  // Translate a list of catch blocks into one catch block
  
  // In case there is no default catch provided, re-throw the value from the catch block
  catch_code = any(Catch c <- catches, c is \default) ? muBlock([]) : muThrow(thrown_as_exception, |unknown:///|);
  for(Catch c <- reverse(catches)){
      trBody = c.body is emptyStatement ? muBlock([]) : translate(c.body, btscopes);
      MuExp exp = muBlock([]);
      if(c is binding) {
          ifname = nextLabel();
          //enterBacktrackingScope(ifname);
          patType = getType(c.pattern);
          if(c.pattern is literal) {
              exp = muIfElse(muEqual(thrown, translate(c.pattern.literal)), trBody, catch_code);
          } else if(c.pattern is typedVariable) {
              varType = translateType(c.pattern.\type);
              if("<c.pattern.name>" == "_"){
                   exp = muIfElse(muValueIsSubtypeOf(thrown, varType), 
                                  trBody,
                                  catch_code);
              } else {
                  <fuid, pos> = getVariableScope("<c.pattern.name>", c.pattern.name@\loc);
                  patVar = muVar("<c.pattern.name>", fuid, pos, varType);
                  exp = muIfElse(muValueIsSubtypeOf(thrown, varType), 
                                       muBlock([ muVarInit(patVar, thrown), trBody ]),
                                       catch_code);
              }
                            
          } else if(c.pattern is qualifiedName){	// TODO: what if qualifiedName already has a value? Check it!
              varType = getType(c.pattern);
              if("<c.pattern.qualifiedName>" == "_"){
                  exp = muBlock([trBody, catch_code]);
              } else {
                  <fuid,pos> = getVariableScope("<c.pattern.qualifiedName>", c.pattern.qualifiedName@\loc);
                  patVar = muVar("<c.pattern.qualifiedName>", fuid, pos, varType);
                  exp = muBlock([muVarInit(patVar, thrown), trBody, catch_code]);
              }
          } else {
              ifname = nextLabel();
              btscopes = getBTScopes(c.pattern, ifname, btscopes);
              exp = translatePat(c.pattern, patType, thrown, btscopes, trBody, catch_code);
          }
          catch_code = muIfElse(muValueIsSubtypeOf(thrown, patType), exp, catch_code);
          //leaveBacktrackingScope(ifname);
      } else {
        catch_code = muBlock([trBody, catch_code]);
      }
   }
   return catch_code;
}

// -- labeled statement ----------------------------------------------

MuExp translate((Statement) `<Label label> { <Statement+ statements> }`, BTSCOPES btscopes) =
    muBlock([translate(stat, btscopes) | Statement stat <- statements]);

// -- assignment statement -------------------------------------------    

MuExp translate(s: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`, BTSCOPES btscopes) { 
    result = translateAssignment(s, btscopes);
    //iprintln(result);
    return result;
} 

MuExp translateAssignment((Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`, BTSCOPES btscopes) =
    assignTo(assignable, "<operator>", getType(statement), translate(statement, btscopes));

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
    return muPrim(op1, getType(assignable), [getType(assignable), rhs_type],  [*oldval, rhs], assignable@\loc); 
}

str getAssignOp(str operator){
    return  ("=" : "replace", "+=" : "add", "-=" : "subtract", "*=" : "product", "/=" : "divide", "&=" : "intersect")[operator]; 
}
    
// assignTo: assign the rhs of the assignment (possibly modified by an assign operator) to the assignable
    
MuExp assignTo(a: (Assignable) `<QualifiedName qualifiedName>`, str operator, AType rhs_type, MuExp rhs) {
    return mkAssign("<qualifiedName>", qualifiedName@\loc, applyOperator(operator, a, rhs_type, rhs));
}

MuExp assignTo(a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, str operator,  AType rhs_type, MuExp rhs) =
     assignTo(receiver, "=", rhs_type, muPrim("update", getType(receiver), [getType(receiver)], [*getValues(receiver), translate(subscript), applyOperator(operator, a, rhs_type, rhs)], a@\loc));
    
MuExp assignTo(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, str operator,  AType rhs_type, MuExp rhs) =
    assignTo(receiver, "=", rhs_type, muPrim("<getOuterType(receiver)>_slice_<getAssignOp(operator)>", getType(receiver), [getType(receiver)], [*getValues(receiver), translateOpt(optFirst), muNoValue(), translateOpt(optLast), rhs], a@\loc) );

MuExp assignTo(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`, str operator,  AType rhs_type, MuExp rhs) =
     assignTo(receiver, "=", rhs_type, muPrim("<getOuterType(receiver)>_slice_<getAssignOp(operator)>", getType(receiver), [getType(receiver)], [*getValues(receiver), translateOpt(optFirst), translate(second), translateOpt(optLast), rhs], a@\loc));

MuExp assignTo(a: (Assignable) `<Assignable receiver> . <Name field>`, str operator,  AType rhs_type, MuExp rhs) {
    assignableType = getType(a);
    receiverType = getType(receiver);
    //println("getValues(receiver)[0]: <getValues(receiver)[0]>");
    res = 
     isTupleType(receiverType) 
     ? assignTo(receiver,  "=", receiverType, muPrim("update", receiverType, [receiverType], [*getValues(receiver), muCon(getTupleFieldIndex(receiverType, "<field>")), applyOperator(operator, a, rhs_type, rhs)], a@\loc) )
     : assignTo(receiver, "=", receiverType, muSetField(receiverType, receiverType, getValues(receiver)[0], "<field>", applyOperator(operator, a, rhs_type, rhs)) );
     return res;
}

MuExp assignTo(Assignable a: (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, str operator,  AType rhs_type, MuExp rhs) = 
    assignTo(receiver,  "=", rhs_type, applyOperator(operator, a, rhs_type, rhs));
    

MuExp assignTo(a: (Assignable) `\<  <{Assignable ","}+ elements> \>`, str operator,  AType rhs_type, MuExp rhs) {
    str fuid = topFunctionScope();
    elems = [ e | Assignable e <- elements];   // hack since elements[i] yields a value result;
    nelems = size(elems); // size_assignables
    
    tmp = muTmpIValue(nextTmp(), fuid, rhs_type);
 
    return muBlock( muVarInit(tmp, applyOperator(operator, a, rhs_type, rhs)) + 
                    [ assignTo(elems[i], "=", rhs_type, muPrim("subscript", avalue()/*getType(elems[i])*/, [getType(a), aint()], [tmp, muCon(i)], a@\loc) )
                    | i <- [0 .. nelems]
                    ]);
}

MuExp assignTo(Assignable a: (Assignable) `<Name name> ( <{Assignable ","}+ arguments> )`, str operator,  AType rhs_type, MuExp rhs) { 
    str fuid = topFunctionScope();
    elems = [ e | Assignable e <- arguments];  // hack since elements[i] yields a value result;
    nelems = size(elems);
    tmp = muTmpIValue(nextTmp(), fuid, rhs_type);
   
    return muBlock( muVarInit(tmp, applyOperator(operator, a, rhs_type, rhs)) + 
                    [ assignTo(elems[i], "=", rhs_type, muPrim("subscript", avalue(), [getType(a), aint()], [tmp, muCon(i)], a@\loc) )
                    | i <- [0 .. nelems]
                    ]);
}

MuExp assignTo(Assignable a: (Assignable) `<Assignable receiver>@<Name annotation>`, str operator, AType rhs_type, MuExp rhs) =

    getOuterType(receiver) == "atuple" 
     ? assignTo(receiver,  "=", rhs_type, muPrim("update", rhs_type, [getType(receiver)], [*getValues(receiver), muCon(getTupleFieldIndex(getType(receiver@\loc), "<annotation>")), applyOperator(operator, a, rhs_type, rhs)], a@\loc) )
     : assignTo(receiver, "=", rhs_type, muSetField(getType(a), getType(receiver), getValues(receiver)[0], "<annotation>", applyOperator(operator, a, rhs_type, rhs)) );

    //assignTo(receiver, "=", rhs_type, muSetAnno(getValues(receiver)[0], getType(a), "<annotation>", applyOperator(operator, a, rhs_type, rhs)));
    
// getValues: get the current value(s) of an assignable

list[MuExp] getValues((Assignable) `<QualifiedName qualifiedName>`) = 
    [ mkVar("<qualifiedName>", qualifiedName@\loc) ];
    
list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`) {
    return [ muPrim("subscript", avalue() /*getType(a)*/, [getType(receiver), getType(subscript)], [*getValues(receiver), translate(subscript)], a@\loc) ];
}
    
list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) {
    ot = getType(a);
    return [ muPrim("slice", ot, [ot], [ *getValues(receiver), translateOpt(optFirst), muNoValue(), translateOpt(optLast) ], a@\loc) ];
}
   
list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`) {
    ot = getType(a);
    return [ muPrim("slice", ot, [ot], [ *getValues(receiver), translateOpt(optFirst),translate(second), translateOpt(optLast) ], a@\loc) ];
}

list[MuExp] getValues(Assignable a:(Assignable) `<Assignable receiver> . <Name field>`) { 
    receiverType = getType(receiver);
    //println(receiverType);
    //println(getType(field));
    resultType = getType(a);
    ufield = unescape("<field>");
    <definingModule, consType, isKwp> =  getConstructorInfo(receiverType, getType(field), ufield);
    return isKwp ? [ muGetKwField(resultType, consType, getValues(receiver)[0], ufield, definingModule) ]
                 : [ muGetField(resultType, consType, getValues(receiver)[0], ufield) ];
    //return isKwp ? [ muGetKwField(getType(a), consType, getValues(receiver)[0], ufield) ]
    //             : [ muGetField(getType(a), consType, getValues(receiver)[0], ufield) ];
}    

list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`) = 
     [ translateIfDefinedOtherwise(getValues(receiver)[0], translate(defaultExpression), a@\loc) ];

list[MuExp] getValues((Assignable) `\<  <{Assignable ","}+ elements > \>` ) = [ *getValues(elm) | Assignable elm <- elements ];

list[MuExp] getValues((Assignable) `<Name name> ( <{Assignable ","}+ arguments> )` ) = [ *getValues(arg) | Assignable arg <- arguments ];

list[MuExp] getValues(Assignable a: (Assignable) `<Assignable receiver>@<Name annotation>`) 
//{
//    receiverType = getType(receiver);
//    ufield = unescape("<annotation>");
//    <consType, isKwp> =  getConstructorInfo(getType(receiver), getType(annotation), ufield);
//    return isKwp ? [ muGetKwField(consType, receiverType, getValues(receiver)[0], ufield) ]
//                 : [ muGetField(consType, receiverType, getValues(receiver)[0], ufield) ];
//}
    = [ muGetAnno(getValues(receiver)[0], getType(a), unescape("<annotation>")) ];

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

MuExp translate((Statement) `;`, BTSCOPES btscopes) = 
    muBlock([]);

//MuExp translate(s: (Statement) `global <Type \type> <{QualifiedName ","}+ names> ;`) { throw("globalDirective"); }

// -- return statement -----------------------------------------------
MuExp translate(s:(Statement) `return <Statement statement>`, BTSCOPES btscopes) {
    return translateReturn(getType(s), statement, btscopes);
}

MuExp  translateReturn(AType resultType, (Statement) `;`, BTSCOPES btscopes)
    = muReturn0();

//MuExp translateReturn(s:(Statement) `return <Statement statement>`, BTSCOPES btscopes)
//    = translateReturn(getType(s), statement, btscopes);
   
MuExp translateReturn(AType resultType, Expression expression, BTSCOPES btscopes) {
	// If the 'return' is used in the scope of a try-catch-finally block,
	// the respective 'finally' block must be executed before the function returns
	if((Expression) `{ <Statement+ statements> }` := expression){
	   return translateReturn(resultType, statements, btscopes);
	}
	//resultType = getType(expression);
	if(hasFinally()) { // TODO adapt
	    str fuid = topFunctionScope();
		str varname = asTmp(nextLabel());
		result = muTmpIValue(nextLabel("result"), fuid, resultType);
		return muValueBlock(resultType, [ muConInit(result, translate(statement, btscopes)), muReturn1(resultType, result) ]);
	} 
   	if(isBoolType(resultType)){
   	    res =  translate(expression);
   	    res = muReturn1(abool(), res);
   	    res = removeDeadCode(res);
   	    //iprintln(res);
   	    return res;                    
    } else
    if(isConditional(expression) && !backtrackFree(expression)){
        btscopes1 = getBTScopes(expression, nextTmp("RET"));
        res = muBlock([ muEnter(getEnter(expression, btscopes1), 
                                translateBool(expression.condition, btscopes1, muReturn1(resultType, translate(expression.thenExp)), muBlock([]))),
                        muReturn1(resultType, translate(expression.elseExp))
                      ]);
        //iprintln(res);
        return res;
    } else {
        res = muReturn1(resultType, translate(expression));
        //iprintln(res);
        //res = exitViaReturn(res) ? res : addReturnFalse(resultType, res); 
        //iprintln(res);
       return res;
    }
    
}

MuExp translateReturn(Statement+ statements, BTSCOPES btscopes){
    stats = [ stat | stat <- statements];
    return muBlock([translate(stat, btscopes) | Statement stat <- stats[0..-1]] + translateReturn(stats[-1], btscopes));
}

MuExp translateReturn(AType resultType, Expression expression){
    code = translate(expression);
    if(isBoolType(resultType)){
        switch(code){
            case muSucceed(_): return muReturn1(resultType, muCon(true));
            case muFail(_): return muReturn1(resultType, muCon(false));   
        }
    }
    return muReturn1(resultType, code);
}
    
default MuExp translateReturn(AType resultType, Statement statement, BTSCOPES btscopes){
    code = translate(statement, btscopes);
    if(isBoolType(resultType)){
        switch(code){
            case muSucceed(_): return muReturn1(resultType, muCon(true));
            case muFail(_): return muReturn1(resultType, muCon(false));   
        }
    }
    return muReturn1(resultType, code);
}

// -- throw statement ------------------------------------------------

MuExp translate(s: (Statement) `throw <Statement statement>`, BTSCOPES btscopes) = 
    muThrow(translate(statement, btscopes),s@\loc);

MuExp translate(s: (Statement) `insert <DataTarget dataTarget> <Statement statement>`, BTSCOPES btscopes) // TODO: handle dataTarget
	= { //fillCaseType(getType(s));//getType(statement@\loc)); 
	    muInsert(getType(statement@\loc), translate(statement, btscopes));
	  };

// -- append statement -----------------------------------------------

MuExp translate(s: (Statement) `append <DataTarget dataTarget> <Statement statement>`, BTSCOPES btscopes) {
   fuid = getCurrentLoopScope(dataTarget);
   target = "listwriter_<currentLoop(dataTarget)>" ;
   return muPrim("add_list_writer", getType(s), [avalue(), getType(statement)], [muTmpIValue(target, fuid, getType(statement)), translate(statement, btscopes)], s@\loc);
}

// -- local function declaration statement ---------------------------------

MuExp translate(s: (Statement) `<FunctionDeclaration functionDeclaration>`, BTSCOPES btscopes) { 
    translate(functionDeclaration); return muBlock([]); 
}

// -- local variable declaration statement ---------------------------

MuExp translate(s: (Statement) `<LocalVariableDeclaration declaration> ;`, BTSCOPES btscopes) { 
    tp = declaration.declarator.\type;
    {Variable ","}+ variables = declaration.declarator.variables;
    code = for(var <- variables){
    		  append mkAssign(unescapeName("<var.name>"), var.name@\loc, var is initialized ? translate(var.initial) : muNoValue());
             }
    return muBlock(code);
}

// -- unknown statement ----------------------------------------------

default MuExp translate(Statement s, BTSCOPES btscopes){
   throw "MISSING CASE FOR STATEMENT: <s>";
}

/*********************************************************************/
/*                  End of Statements                                */
/*********************************************************************/
