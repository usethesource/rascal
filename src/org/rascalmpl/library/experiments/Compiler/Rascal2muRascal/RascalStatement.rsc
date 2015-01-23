@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalStatement

import IO;
import ValueIO;
import Node;
import Map;
import Set;
import String;
import ParseTree;

import lang::rascal::\syntax::Rascal;

import experiments::Compiler::Rascal2muRascal::TmpAndLabel;
import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::Rascal2muRascal::RascalExpression;
import experiments::Compiler::Rascal2muRascal::RascalPattern;
import experiments::Compiler::Rascal2muRascal::RascalType;
import experiments::Compiler::Rascal2muRascal::TypeUtils;

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
             muWhile(whilename, muCon(true), [ muIfelse(ifname, makeMu("ALL", [ translate(c) | c <- conditions ], s@\loc), 
                                                                [ visit(translate(body)) { case muFail(whileName) => muFail(ifname) } ], 
                                                                [ muBreak(whilename) ]) ]),
             muCallPrim3("listwriter_close", [muTmp(tmp,fuid)], s@\loc)
           ];
    leaveBacktrackingScope();
    leaveBacktrackingScope();
    leaveLoop();
    return muBlock(code);
}

// Due to the similarity of some statement and their template version, we present both version together

MuExp translateTemplate(s: (StringTemplate) `while ( <Expression condition> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`, str indent, str pre, str prefuid){
    str fuid = topFunctionScope();
    whilename = nextLabel();
    ifname = nextLabel();
    result = asTmp(whilename);
    enterLoop(whilename,fuid);
    enterBacktrackingScope(whilename);
    enterBacktrackingScope(ifname);
    code = [ muAssignTmp(result,fuid,muCallPrim3("template_open", [muCon(""), muTmp(pre,prefuid)], s@\loc)), 
             muWhile(whilename, muCon(true),
                 [ muIfelse(ifname, makeMu("ALL", [ translate(condition) ], condition@\loc), 
                     [ translateStats(preStats),  
                        muAssignTmp(result,fuid,muCallPrim3("template_add", [muTmp(result,fuid), translateMiddle(body)], body@\loc)), 
                       translateStats(postStats)
                     ], [ muBreak(whilename) ]) 
                 ]),
             muCallPrim3("template_close", [muTmp(result,fuid)], s@\loc)
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
                visit(translate(body)) { case muFail(doname) => muFail(ifname) }, 
                muIfelse(ifname, makeMu("ALL", [ translate(condition) ], condition@\loc), 
                                 [ muContinue(doname) ], 
                                 [ muBreak(doname) ]) ]),
             muCallPrim3("listwriter_close", [muTmp(tmp,fuid)], s@\loc)
           ];
    leaveBacktrackingScope();
    leaveBacktrackingScope();
    leaveLoop();
    return muBlock(code);
}

MuExp translateTemplate(s: (StringTemplate) `do { < Statement* preStats> <StringMiddle body> <Statement* postStats> } while ( <Expression condition> )`, str indent, str pre, str prefuid) {
    str fuid = topFunctionScope();  
    doname = nextLabel();
    ifname = nextLabel();
    result = asTmp(doname);
    enterLoop(doname,fuid);
    enterBacktrackingScope(doname);
    enterBacktrackingScope(ifname);
    code = [ muAssignTmp(result,fuid,muCallPrim3("template_open", [muCon(""), muTmp(pre,prefuid)], s@\loc)),
             muWhile(doname, muCon(true),
                             [ translateStats(preStats),
                               muAssignTmp(result,fuid,muCallPrim3("template_add", [muTmp(result,fuid), translateMiddle(body)], body@\loc)),
                               translateStats(postStats),
                               muIfelse(ifname, makeMu("ALL", [ translate(condition) ], condition@\loc), 
                                                [ muContinue(doname) ], 
                                                [ muBreak(doname) ])]),
             muCallPrim3("template_close", [muTmp(result,fuid)], s@\loc)
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
             muWhile(forname, makeMuMulti(makeMu("ALL",[ translate(c) | c <-generators ], s@\loc), s@\loc), 
                              [ translate(body) ]),
             muCallPrim3("listwriter_close", [muTmp(tmp,fuid)], s@\loc)
           ];
    leaveBacktrackingScope();
    leaveLoop();
    return muBlock(code);
}

MuExp translateTemplate(s: (StringTemplate) `for ( <{Expression ","}+ generators> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`, str indent, str pre, str prefuid){
    str fuid = topFunctionScope();
    forname = nextLabel();
    result = asTmp(forname);
    enterLoop(forname,fuid);
    enterBacktrackingScope(forname);
    code = [ muAssignTmp(result,fuid,muCallPrim3("template_open", [muCon(""), muTmp(pre,prefuid)], s@\loc)),
             muWhile(forname, makeMuMulti(makeMu("ALL",[ translate(c) | c <-generators ], s@\loc), s@\loc), 
                     [ translateStats(preStats),  
                       muAssignTmp(result,fuid,muCallPrim3("template_add", [muTmp(result,fuid), translateMiddle(body)], body@\loc)),
                       translateStats(postStats)
                     ]),
             muCallPrim3("template_close", [muTmp(result,fuid)], s@\loc)
           ];
    leaveBacktrackingScope();
    leaveLoop();
    return muBlock(code);
} 

// -- if then statement ----------------------------------------------

MuExp translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement>`) {
    ifname = getLabel(label);
	enterBacktrackingScope(ifname);
	code = muIfelse(ifname, makeMu("ALL", [ translate(c) | c <- conditions ], s@\loc), [translate(thenStatement)], []);
    leaveBacktrackingScope();
    return code;
}
    
MuExp translateTemplate(s: (StringTemplate) `if (<{Expression ","}+ conditions> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`, str indent, str pre, str prefuid){
    str fuid = topFunctionScope();
    ifname = nextLabel();
    result = asTmp(ifname);
    enterBacktrackingScope(ifname);
    code = [ muAssignTmp(result,fuid,muCallPrim3("template_open", [muCon(""), muTmp(pre,prefuid)], s@\loc)),
             muIfelse(ifname, makeMu("ALL", [ translate(c) | c <- conditions ], s@\loc), 
                      [ translateStats(preStats),
                        muAssignTmp(result,fuid,muCallPrim3("template_add", [muTmp(result,fuid), translateMiddle(body)], body@\loc)),
                        translateStats(postStats)],
                      []),
               muCallPrim3("template_close", [muTmp(result,fuid)], s@\loc)
           ];
    leaveBacktrackingScope();
    return muBlock(code);
}    

// -- if then else statement -----------------------------------------

MuExp translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement> else <Statement elseStatement>`) {
	ifname = getLabel(label);
	code = muIfelse(ifname, makeMu("ALL",[ translate(c) | c <- conditions ], s@\loc), 
	                        { enterBacktrackingScope(ifname); [ translate(thenStatement) ]; }, 
	                        { leaveBacktrackingScope(); [ translate(elseStatement)]; });
    return code;
}
    
MuExp translateTemplate(s: (StringTemplate) `if ( <{Expression ","}+ conditions> ) { <Statement* preStatsThen> <StringMiddle thenString> <Statement* postStatsThen> }  else { <Statement* preStatsElse> <StringMiddle elseString> <Statement* postStatsElse> }`, str indent, str pre, str prefuid){
    str fuid = topFunctionScope();                    
    ifname = nextLabel();
    result = asTmp(ifname);
    code = [ muAssignTmp(result,fuid,muCallPrim3("template_open", [muCon(""), muTmp(pre,prefuid)], s@\loc)),
             muIfelse(ifname, makeMu("ALL",[ translate(c) | c <- conditions ], s@\loc), 
                      { enterBacktrackingScope(ifname);
                        [ translateStats(preStatsThen), 
                          muAssignTmp(result,fuid,muCallPrim3("template_add", [muTmp(result,fuid), translateMiddle(thenString)], thenString@\loc)),
                          translateStats(postStatsThen)
                        ];
                      },
                      { enterBacktrackingScope(ifname);
                        [ translateStats(preStatsElse), 
                          muAssignTmp(result,fuid,muCallPrim3("template_add", [muTmp(result,fuid), translateMiddle(elseString)], elseString@\loc)),
                          translateStats(postStatsElse)
                        ];
                      }),
              muCallPrim3("template_close", [muTmp(result,fuid)], s@\loc)
           ];
    leaveBacktrackingScope();
    return muBlock(code);                                             
} 

// -- switch statement -----------------------------------------------

MuExp translate(s: (Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`) = translateSwitch(s);

MuExp translateSwitch(s: (Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`) {
    str fuid = topFunctionScope();
    switchname = getLabel(label);
    switchval = asTmp(switchname);
    return muBlock([ muAssignTmp(switchval,fuid,translate(expression)), translateSwitchCases(switchval,fuid,[c | c <- cases]) ]);
}

MuExp translateSwitchCases(str switchval, str fuid, list[Case] cases) {
  if(size(cases) == 0)
      return muBlock([]);
  c = head(cases);
  
  if(c is patternWithAction){
     pwa = c.patternWithAction;
     if(pwa is arbitrary){
     	ifname = nextLabel();
        cond = muMulti(muApply(translatePat(pwa.pattern), [ muTmp(switchval,fuid) ]));
        exp = muIfelse(ifname, cond, 
                               { enterBacktrackingScope(ifname); [ translate(pwa.statement) ]; }, 
                               { leaveBacktrackingScope(); [ translateSwitchCases(switchval,fuid,tail(cases)) ]; });
        return exp; 
     } else {
        throw "Replacement not allowed in switch statement";
     }
  } else {
        return translate(c.statement);
  }
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
   println("varCode: <varCode>");
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
  
  if(c is binding) {
      ifname = nextLabel();
      enterBacktrackingScope(ifname);
      list[MuExp] conds = [];
      list[MuExp] then = [];
      if(c.pattern is literal) {
          conds = [ muCallMuPrim("equal", [ muTmp(asUnwrapedThrown(varname),varfuid), translate(c.pattern.literal) ]) ];
          then = [ translate(c.body) ];
      } else if(c.pattern is typedVariable) {
          conds = [ muCallMuPrim("check_arg_type", [ muTmp(asUnwrapedThrown(varname),varfuid), muTypeCon(translateType(c.pattern.\type)) ]) ];
          <fuid,pos> = getVariableScope("<c.pattern.name>", c.pattern.name@\loc);
          then = [ muAssign("<c.pattern.name>", fuid, pos, muTmp(asUnwrapedThrown(varname),varfuid)), translate(c.body) ];
      } else {
          conds = [ muMulti(muApply(translatePat(c.pattern), [ muTmp(asUnwrapedThrown(varname),varfuid) ])) ];
          then = [ translate(c.body) ];
      }
      exp = muIfelse(ifname, makeMu("ALL",conds, c@\loc), then, [translateCatches(varname, varfuid, tail(catches), hasDefault)]);
      leaveBacktrackingScope();
      return exp;
  }
  
  // The default case will handle any thrown value
  exp = translate(c.body);
  
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
        assert size(oldval) == 1;   
        return generateIfDefinedOtherwise(oldval[0], rhs, assignable@\loc);
    }
    
    oldval = getValues(assignable);
     
    op1 = ("+=" : "add", "-=" : "subtract", "*=" : "product", "/=" : "divide", "&=" : "intersect")[operator]; //TODO: add new operator <<=
    op2 = typedBinaryOp(getOuterType(assignable), op1, rhs_type);
    
    assert size(oldval) == 1;
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
     [ generateIfDefinedOtherwise(getValues(receiver)[0], translate(defaultExpression), a@\loc) ];

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
		muBlock([ muAssignVarDeref("hasInsert",topFunctionScope(),2,muBool(true)), 
				  muReturn1(translate(statement)) ]); };

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