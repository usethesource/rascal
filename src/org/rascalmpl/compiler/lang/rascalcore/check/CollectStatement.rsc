@bootstrapParser
module lang::rascalcore::check::CollectStatement

/*
    Check all statements
*/
extend lang::rascalcore::check::CheckerCommon;

import lang::rascalcore::check::CollectExpression;
import lang::rascalcore::check::CollectPattern;
import lang::rascalcore::check::CollectDeclaration;

import lang::rascal::\syntax::Rascal;
 
import List;
import Set;
import String;

// Rascal statements

// ---- assert ----------------------------------------------------------------

void collect(current: (Statement) `assert <Expression expression>;`, Collector c){
    c.fact(current, abool());
    c.requireEqual(expression, abool(), error(expression, "Assertion should be `bool`, found %t", expression));
    collect(expression, c);
} 

void collect(current: (Statement) `assert <Expression expression> : <Expression message> ;`, Collector c){
   c.fact(current, abool());
   c.requireEqual(expression, abool(), error(expression, "Assertion should be `bool`, found %t", expression));
   c.requireEqual(message, astr(), error(message, "Assertion message should be `str`, found %t", message));
   collect(expression, message, c);
} 
     
// ---- expression ------------------------------------------------------------

// TODO: Nearly duplicate of code in RascalExpression:
bool mayIntroduceVars((Expression) `<Pattern pat> := <Expression rhs>`) = true;
bool mayIntroduceVars((Expression) `<Pattern pat> !:= <Expression rhs>`) = true;
bool mayIntroduceVars((Expression) `<Pattern pattern> \<- <Expression expression>`) = true; 
default bool mayIntroduceVars(Expression e) = false;

void collect(current: (Statement) `<Expression expression>;`, Collector c){
    c.fact(current, expression);
    if(mayIntroduceVars(expression)){
        c.enterScope(current);
            collect(expression, c);
        c.leaveScope(current);
    } else {
        collect(expression, c);
    }
}

// ---- visit statement and insert ------------------------------------------------------

void collect(current: (Statement) `<Label label> <Visit vst>`, Collector c){
    c.enterScope(current);
        scope = c.getScope();
        c.setScopeInfo(scope, visitOrSwitchScope(), visitOrSwitchInfo(vst.subject, true));
        if(label is \default){
            c.define("<label.name>", labelId(), label.name, defType(avoid()));
        }
        c.require("non-void", vst.subject, [], makeNonVoidRequirement(vst.subject, "Subject of visit"));
        c.fact(current, vst.subject);
        collect(vst, c);
       
        // TODO: experiment
        //casePatterns = [ cs.patternWithAction.pattern | cs <- vst.cases, cs is patternWithAction ];
        //c.require("cases from specific to general", current, casePatterns, void(Solver s){
        //    caseType = [ s.getType(cpat) | cpat <- casePatterns ];
        //    for(int i <- index(casePatterns), int j <- index(casePatterns)){
        //        if(i < j && asubtype(caseType[j], caseType[i])){
        //            s.report(warning(casePatterns[j], "Case pattern has more specific type %t than previous case with type %t", caseType[j], caseType[i]));
        //        }
        //    }
        //
        //});
        
    c.leaveScope(current);
}

// ---- visit expression

void collect(current: (Expression) `<Label label> <Visit vst>`, Collector c){
    c.enterScope(current);
        scope = c.getScope();
        c.setScopeInfo(scope, visitOrSwitchScope(), visitOrSwitchInfo(vst.subject, true));
        if(label is \default){
            c.define(prettyPrintName(label.name), labelId(), label.name, defType(avoid()));
        }
        c.calculate("visit", current, [vst.subject], 
            AType(Solver s){ 
                checkNonVoid(vst.subject, s, "Subject of visit");
                return s.getType(vst.subject); 
            });
        collect(vst, c);
        
        //TODO: experiment
        //casePatterns = [ cs.patternWithAction.pattern | cs <- vst.cases, cs is patternWithAction ];
        //c.require("cases from specific to general", current, casePatterns, void(Solver s){
        //    caseType = [ s.getType(cpat) | cpat <- casePatterns ];
        //    for(int i <- index(casePatterns), int j <- index(casePatterns)){
        //        if(i < j && asubtype(caseType[j], caseType[i])){
        //            s.report(warning(casePatterns[j], "Case pattern has more specific type %t than previous case with type %t", caseType[j], caseType[i]));
        //        }
        //    }
        //
        //});
    c.leaveScope(current);
}

void collect(Visit current, Collector c){
    collect(current.subject, current.cases, c);
}

void collect(current: (Case) `case <PatternWithAction patternWithAction>`, Collector c){
    collect(patternWithAction, c);
}

void collect(current: (Case) `default: <Statement statement>`, Collector c){
    collect(statement, c);
}

data replacementInfo = replacementInfo(Pattern pattern);

void collect(current: (PatternWithAction) `<Pattern pattern> =\> <Replacement replacement>`,  Collector c){
    visitOrSwitchScopes = c.getScopeInfo(visitOrSwitchScope());
    for(<scope, scopeInfo> <- visitOrSwitchScopes){
        if(visitOrSwitchInfo(Expression _, bool isVisit) := scopeInfo){
            if(isVisit){
                c.enterScope(current);
                beginPatternScope("pattern-with-action", c);
                    scope = c.getScope();
                    c.setScopeInfo(scope, replacementScope(), replacementInfo(pattern));
                    // force type calculation of pattern
                    //c.calculate("pattern", pattern, [], AType(Solver s){ return getPatternType(pattern, avalue(), scope, s); });
                    c.require("pattern", pattern, [], void(Solver s){ getPatternType(pattern, avalue(), scope, s); });
                    
                    conditions = [];
                    if(replacement is conditional){
                        conditions = [(Expression)`(<Expression e>)` := c ? e : c | Expression c <- replacement.conditions];
                    }
                    //TODO: simplified for compiler from: conditions = replacement is conditional ? [(Expression)`(<Expression e>)` := c ? e : c | Expression c <- replacement.conditions] : [];
                    
                    if(replacement is conditional){
                       storeAllowUseBeforeDef(current, replacement.replacementExpression, c);
                       c.require("when conditions in replacement", replacement.conditions, conditions,
                          void (Solver s){ 
                              for(cond <- conditions){
                                  condType = s.getType(cond);
                                  if(!s.isFullyInstantiated(condType)){
                                     s.requireUnify(condType, abool(), error(cond, "Cannot unify %t with `bool`", cond));
                                     condType = s.instantiate(condType);
                                  }
                                  s.requireEqual(cond, abool(), error(cond, "Condition should be `bool`, found %t", cond));
                               }
                            });
                    }
                    
                    c.require("pattern replacement", current, /*replacement.replacementExpression + */conditions,
                       void (Solver s){ 
                           exprType = s.getType(replacement.replacementExpression);
                           patType = getPatternType(pattern, avalue(), scope, s);
                           if(!s.isFullyInstantiated(exprType) || !s.isFullyInstantiated(patType)){
                              s.requireUnify(exprType, patType, error(current, "Cannot unify %t with %t", patType, exprType)); 
                              exprType = s.instantiate(exprType);
                              patType = s.instantiate(patType); 
                           }
                           checkNonVoid(replacement.replacementExpression, exprType, s, "Replacement in visit");
                           s.requireSubType(exprType, patType, error(current, "A pattern of type %t cannot be replaced by %t", patType, exprType));
                         });
              
                        collect(pattern, c);
                    endPatternScope(c);
                    collect(replacement, c);
                c.leaveScope(current);
                return;
             } else {
                c.report(error(current, "Pattern with Action found inside a switch statement"));
                return;
             }
        }
    }
    c.report(error(current, "Pattern with Action found outside switch or visit statement"));
}

void collect(current: (PatternWithAction) `<Pattern pattern>: <Statement statement>`,  Collector c){
    visitOrSwitchScopes = c.getScopeInfo(visitOrSwitchScope());
    for(<scope, scopeInfo> <- visitOrSwitchScopes){
        if(visitOrSwitchInfo(Expression exp, bool isVisit) := scopeInfo){
            if(isVisit){
               c.enterScope(current);
                    scope = c.getScope();
                    c.setScopeInfo(scope, replacementScope(), replacementInfo(pattern));
                    // force type calculation of pattern
                    c.require("pattern", pattern, [], void(Solver s){ getPatternType(pattern, avalue(), scope, s); });
                    beginPatternScope("pattern-with-action", c);
                        collect(pattern, c);
                    endPatternScope(c);
                    collect(statement, c);
              c.leaveScope(current);
              return;
           } else {
              c.enterScope(current);
                    c.require("pattern", pattern, [], 
                        void(Solver s){ 
                            expType = s.getType(exp);
                            patType = getPatternType(pattern, expType, scope, s);
                            if(!s.isFullyInstantiated(patType)){
                                s.requireUnify(expType, patType, error(pattern, "Cannot unify pattern of type %t with switch expression of type %t", patType, expType));
                                patType = s.instantiate(patType); 
                            } 
                            s.requireComparable(exp, patType, error(pattern, "Pattern of type %t should be comparable with type of switch expression of type %t", patType, exp) ); 
                        });                    
                    beginPatternScope("pattern-with-action", c);
                        collect(pattern, c);
                    endPatternScope(c);
                    collect(statement, c);
              c.leaveScope(current);
              return;
           }
       }
    }
    c.report(error(current, "Pattern with Action found outside switch or visit context"));
}

void collect(current: (Replacement) `<Expression replacementExpression> when <{Expression ","}+ conditions>`, Collector c){
    collect(replacementExpression, conditions, c);
}

// TODO actually: "insert" DataTarget dataTarget Statement statement 
void collect(current: (Statement) `insert <Statement expr>`, Collector c){
    replacementScopes = c.getScopeInfo(replacementScope());
    for(<scope, scopeInfo> <- replacementScopes){
      if(replacementInfo(Pattern pat) := scopeInfo){
         c.require("insert expression", expr, [expr], 
             void (Solver s) { exprType = s.getType(expr);
                  patType = getPatternType(pat, avalue(), scope, s);
                  if(!s.isFullyInstantiated(exprType) || !s.isFullyInstantiated(patType)){
                     s.requireUnify(exprType, patType, error(current, "Cannot unify %t with %t", patType, exprType));
                     exprType = s.instantiate(exprType);
                     patType = s.instantiate(patType);
                  }
                  s.requireSubType(exprType, patType, error(expr, "Insert type should be subtype of %t, found %t", patType, exprType));
             });
          c.fact(current, expr);
          collect(expr, c);
          return;
      } else {
        throw rascalCheckerInternalError(getLoc(current), "Inconsistent info from replacement scope: <scopeInfo>");
      }
    }
    c.report(error(current, "Insert found outside replacement context"));
}

// loop statements, append, break and continue

data LoopInfo = loopInfo(str name, list[Tree] appends);

// ---- while -----------------------------------------------------------------

void collect(current: (Statement) `<Label label> while( <{Expression ","}+ conditions> ) <Statement body>`,  Collector c){
    c.enterScope(current);   // body may refer to variables defined in conditions
        loopName = "";
        if(label is \default){
            loopName = prettyPrintName(label.name);
            c.define(loopName, labelId(), label.name, defType(avoid()));
        }
        c.setScopeInfo(c.getScope(), loopScope(), loopInfo(loopName, [])); // record appends in body, initially []
        condList = [cond | Expression cond <- conditions];
        
        c.require("while statement", current, condList + [body], void (Solver s){ checkConditions(condList, s); });
        beginPatternScope("conditions", c);
            collect(condList, c);
        endPatternScope(c);
        collect(body, c);
        computeLoopType("while statement", loopName, current, c);
    c.leaveScope(current);
}

private void computeLoopType(str loopKind, str loopName1, Statement current, Collector c){
    loopScopes = c.getScopeInfo(loopScope());
    
    for(<_, scopeInfo> <- loopScopes){
        if(loopInfo(loopName2, list[Statement] appends) := scopeInfo){
           if(loopName1 == "" || loopName1 == loopName2){
              if(isEmpty(appends)){
                c.fact(current, alist(avoid()));
              } else {
                 c.calculate(loopKind, current, appends, AType(Solver s){ 
                    res = alist(s.lubList([s.getType(app) | app <- appends]));
                    return res;
                     });
              }
              return;
           } else {
           
             throw rascalCheckerInternalError(getLoc(current), "Inconsistent info from loop scope: <scopeInfo>");
           }
        }
    }
    
    throw rascalCheckerInternalError(getLoc(current), "Info for loop scope not found"); 
}

// ---- do --------------------------------------------------------------------

void collect(current: (Statement) `<Label label> do <Statement body> while ( <Expression condition> ) ;`, Collector c){
    c.enterScope(current);   // condition may refer to variables defined in body
        loopName = "";
        if(label is \default){
            loopName = prettyPrintName(label.name);
            c.define(loopName, labelId(), label.name, defType(avoid()));
        }
        c.setScopeInfo(c.getScope(), loopScope(), loopInfo(loopName, [])); // appends in body
        c.require("do statement", current, [body, condition], void (Solver s){ checkConditions([condition], s); });
        
        collect(body, c);
        
        beginPatternScope("conditions", c);
            collect(condition, c);
        endPatternScope(c);
        
        computeLoopType("do statement", loopName, current, c);
    c.leaveScope(current); 
}

// ---- for -------------------------------------------------------------------

void collect(current: (Statement) `<Label label> for( <{Expression ","}+ conditions> ) <Statement body>`,  Collector c){
    c.enterScope(current);   // body may refer to variables defined in conditions
        loopName = "";
        if(label is \default){
            loopName = prettyPrintName(label.name);
            c.define(loopName, labelId(), label.name, defType(avoid()));
        }
        c.setScopeInfo(c.getScope(), loopScope(), loopInfo(loopName, [])); // appends in body
        condList = [cond | Expression cond <- conditions];
        
        c.require("for statement", current, condList + [body], void (Solver s){ checkConditions(condList, s); });
        
        beginPatternScope("conditions", c);
            collect(condList, c);
        endPatternScope(c);
        
        collect(body, c);
        computeLoopType("for statement", loopName, current, c);
    c.leaveScope(current);  
}

// ---- append ----------------------------------------------------------------

void collect(current: (Statement) `append <DataTarget dataTarget> <Statement statement>`, Collector c){
    loopName = "";
    if(dataTarget is labeled){
        loopName = prettyPrintName(dataTarget.label);
        c.use(dataTarget.label, {labelId()});
    }
   
    for(<scope, scopeInfo> <- c.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] appends) := scopeInfo){
            if(loopName == "" || loopName == loopName1){
                c.setScopeInfo(scope, loopScope(), loopInfo(loopName1, appends + [current]));
                //c.calculate("append type", current, [statement], AType(Solver s){ return s.getType(statement); });
                c.fact(current, statement);
                collect(statement, c);
                return;
             }
        } else {
            throw rascalCheckerInternalError(getLoc(current), "Inconsistent info from loop scope: <scopeInfo>");
        }
    }
    c.report(error(current, "Append outside a while/do/for statement"));
}

// ---- break -----------------------------------------------------------------

void collect(Target target, Collector c){
   if(target is labeled){
        c.use(target.name, {labelId()});
    }
}

str getTargetName(Target target) = target is labeled ? prettyPrintName(target.name) : "";

void collect(current:(Statement) `break <Target target>;`, Collector c){
    c.fact(current, avoid());
    loopName = getTargetName(target);
 
    for(<_, scopeInfo> <- c.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] _) := scopeInfo){
            if(loopName == "" || loopName == loopName1){
                collect(target, c);
                return;
             }
        } else {
            throw rascalCheckerInternalError(getLoc(current), "Inconsistent info from loop scope: <scopeInfo>");
        }
    }
    c.report(error(current, "Break outside a while/do/for statement"));
}

// ---- continue --------------------------------------------------------------

void collect(current:(Statement) `continue <Target target>;`, Collector c){
    c.fact(current, avoid());
    loopName = getTargetName(target);
    
    for(<_, scopeInfo> <- c.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] _) := scopeInfo){
            if(loopName == "" || loopName == loopName1){
                 collect(target, c);
                 return;
             }
        } else {
            throw rascalCheckerInternalError(getLoc(current), "Inconsistent info from loop scope: <scopeInfo>");
        }
    }
    c.report(error(current, "Continue outside a while/do/for statement"));
}

// ---- if --------------------------------------------------------------------

void collect(current: (Statement) `<Label label> if( <{Expression ","}+ conditions> ) <Statement thenPart>`,  Collector c){
    c.enterCompositeScope([conditions, thenPart]); // thenPart may refer to variables defined in conditions
        if(label is \default){
            c.define("<label.name>", labelId(), label.name, defType(avoid()));
        }
        condList = [cond | Expression cond <- conditions];
        c.fact(current, avalue());
        
        c.require("if then", current, condList + thenPart, void (Solver s){ checkConditions(condList, s); });
        
        beginPatternScope("conditions", c);
        collect(condList, c);
        endPatternScope(c);
        collect(thenPart, c);
    c.leaveCompositeScope([conditions, thenPart]);   
}

// --- if then else -----------------------------------------------------------

void collect(current: (Statement) `<Label label> if( <{Expression ","}+ conditions> ) <Statement thenPart> else <Statement elsePart>`,  Collector c){
    c.enterCompositeScope([conditions, thenPart]);   // thenPart may refer to variables defined in conditions; elsePart may not
        if(label is \default){
            c.define(prettyPrintName(label.name), labelId(), label.name, defType(avoid()));
        }
        condList = [cond | cond <- conditions];
        
        c.calculate("if then else", current, condList + [thenPart, elsePart],
            AType(Solver s){
                checkConditions(condList, s);
                return s.lub(thenPart, elsePart);
            });
        
        beginPatternScope("conditions", c);
            collect(condList, c);
        endPatternScope(c);
        collect(thenPart, c);
    c.leaveCompositeScope([conditions, thenPart]);     
    collect(elsePart, c);
}

// ---- switch ----------------------------------------------------------------

void collect(current: (Statement) `<Label label> switch ( <Expression e> ) { <Case+ cases> }`, Collector c){
    c.enterScope(current);
        if(label is \default){
            c.define(prettyPrintName(label.name), labelId(), label.name, defType(avoid()));
        }
        scope = c.getScope();
        c.setScopeInfo(scope, visitOrSwitchScope(), visitOrSwitchInfo(e, false));
        c.fact(current, avoid());
        c.require("non-void", e, [], void(Solver s){ checkNonVoid(e, s, "Switch expression"); });
        collect(e, cases, c);
        // TODO: experiment (gives missing constraint for case [1]: ...
        //casePatterns = [ cs.patternWithAction.pattern | cs <- cases, cs is patternWithAction ];
        //c.require("cases from specific to general", current, casePatterns, void(Solver s){
        //    caseType = [ s.getType(cpat) | cpat <- casePatterns ];
        //    for(int i <- index(casePatterns), int j <- index(casePatterns)){
        //        if(i < j && asubtype(caseType[j], caseType[i])){
        //            s.report(warning(casePatterns[j], "Case pattern has more specific type %t than previous case with type %t", caseType[j], caseType[i]));
        //        }
        //    }
        //
        //});
    c.leaveScope(current);
}

data SwitchInfo = switchInfo(Expression e);

// ---- fail ------------------------------------------------------------------

void collect(current: (Statement)`fail <Target target>;`, Collector c){
    if(target is labeled){
        c.use(target.name, {labelId(), functionId()});
    }
    c.fact(current, avoid());
}

// ---- filter ----------------------------------------------------------------

void collect(current: (Statement) `filter;`, Collector c){
    c.fact(current, avoid());
}
// ---- solve -----------------------------------------------------------------

void collect(current: (Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`, Collector c){
    for(v <- variables){
        <qualifier, base> = splitQualifiedName(v);
        if(!isEmpty(qualifier)){
            c.useQualified([qualifier, base], v, variableRoles, {moduleId()} );
        } else {
            c.use(v, variableRoles);
        }
    }
    c.fact(current, body);
    collect(variables, bound, body, c);
}

void collect(Bound current, Collector c){
    if(current is \default){
        c.requireEqual(current.expression, aint(), error(current.expression, "Bound should have type `int`, found %t", current.expression)); 
        c.fact(current, aint());
        collect(current.expression, c);
    } else {
        c.fact(current, avoid());
    }
}

// ---- try, try finally, catch -----------------------------------------------

// ---- try -------------------------------------------------------------------
 
 void collect(current: (Statement) `try <Statement body> <Catch+ handlers>`, Collector c){
    lhandlers = [ h | h <- handlers ];
    c.calculate("try", current, body + lhandlers, AType(Solver _) { return avoid(); } );
    collect(body, handlers, c);
 }
 
// ---- try finally -----------------------------------------------------------

void collect(current: (Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`, Collector c){
    lhandlers = [h | h <- handlers];
    c.calculate("try finally", current, body + lhandlers + finallyBody, AType(Solver _) { return avoid(); } );
    collect(body, handlers, finallyBody, c);
}

// ---- catch -----------------------------------------------------------------

void collect(current: (Catch) `catch: <Statement body>`, Collector c){
    c.fact(current, avoid());
    collect(body, c);
}

void collect(current: (Catch) `catch <Pattern pattern>: <Statement body>`, Collector c){
    c.fact(current, avoid());
    c.enterScope(current);
        beginPatternScope("catch", c);
        collect(pattern, c);
        c.require("pattern", pattern, [], void(Solver s){ getPatternType(pattern, avalue(), getLoc(current), s); });
        
        //c.require("catch pattern", pattern, [],
        //   () { tpat = getType(pattern);
        //        if(!isFullyInstantiated(tpat)){
        //           unify(tpat, avalue()) || reportError(pattern, "Cannot bind pattern");
        //        }
        //      });
        endPatternScope(c);
        collect(body, c);
    c.leaveScope(current);
}

// ---- non-empty block -------------------------------------------------------

void collect(current: (Statement) `<Label label> { <Statement+ statements> }`, Collector c){
    c.enterScope(current);
        if(label is \default){
           c.define("<label.name>", labelId(), label.name, defType(avoid()));
        }
        stats = [ s | Statement s <- statements ];
        c.calculate("non-empty block statement", current, [stats[-1]],  AType(Solver s) { return s.getType(stats[-1]); } );
        collect(stats, c);
    c.leaveScope(current);
}

// ---- empty block -----------------------------------------------------------

void collect(current: (Statement) `;`, Collector c){
    c.fact(current, avoid());
}

// ---- assignment ------------------------------------------------------------

void collect(current: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`, Collector c){
    checkAssignment(current, assignable, "<operator>", statement, c);
    collect(assignable, statement, c);
}

void collect(current:(Assignable) `( <Assignable arg> )`, Collector c){
    collect(arg, c);
}
/*
    | subscript         : Assignable receiver "[" Expression subscript "]" 
    | slice             : Assignable receiver "[" OptionalExpression optFirst ".." OptionalExpression optLast "]" 
    | sliceStep         : Assignable receiver "[" OptionalExpression optFirst "," Expression second ".." OptionalExpression optLast "]"     
    | fieldAccess       : Assignable receiver "." Name field 
    | ifDefinedOrDefault: Assignable receiver "?" Expression defaultExpression 
    | constructor       : Name name "(" {Assignable ","}+ arguments ")"  
    | \tuple            : "\<" {Assignable ","}+ elements "\>" 
    | annotation        : Assignable receiver "@" Name annotation  ;
*/

void collect(current:(Assignable) `<Assignable receiver> [ <Expression subscript> ]`, Collector c){
    collect(receiver, subscript, c);
}

void collect(current:(Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, Collector c){
    collect(receiver, optFirst, optLast, c);
}

void collect(current:(Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, Collector c){
    collect(receiver, optFirst, second, optLast, c);
}

void collect(current:(Assignable) `<Assignable receiver> . <Name field>`, Collector c){
    collect(receiver, field, c);
}

void collect(current:(Assignable) `<Assignable receiver> ? <Expression defaultExpression >`, Collector c){
    collect(receiver, defaultExpression, c);
}

// | constructor       : Name name "(" {Assignable ","}+ arguments ")" 

void collect(current:(Assignable) `\< <{Assignable ","}+ elements> \>`, Collector c){
    collect(elements, c);
}

void collect(current:(Assignable) `<Assignable receiver> @ <Name annotation>`, Collector c){
    collect(receiver, annotation, c);
}
   
private void checkAssignment(Statement current, (Assignable) `( <Assignable arg> )`, str operator, Statement statement, Collector c){
    checkAssignment(current, arg, operator, statement, c);
    //collect(arg, c);
}

private AType computeAssignmentRhsType(Statement current, AType lhsType, "=", AType rhsType, Solver s){
    checkNonVoid(current, rhsType, s, "Righthand side of assignment");
    s.requireComparable(rhsType, lhsType, error(current, "Cannot assign righthand side of type %t to lefthand side of type %t", rhsType, lhsType));
    return rhsType;
}
    
private AType computeAssignmentRhsType(Statement current, AType lhsType, "+=", AType rhsType, Solver s){
    checkNonVoid(current, rhsType, s, "Righthand side of += assignment");
    return computeAdditionType(current, lhsType, rhsType, s);
}  

private AType computeAssignmentRhsType(Statement current, AType lhsType, "-=", AType rhsType, Solver s){
    checkNonVoid(current, rhsType, s, "Righthand side of-= assignment");
    return computeSubtractionType(current, lhsType, rhsType, s); 
}

private AType computeAssignmentRhsType(Statement current, AType lhsType, "*=", AType rhsType, Solver s){
    checkNonVoid(current, rhsType, s, "Righthand side of*= assignment");
    return computeProductType(current, lhsType, rhsType, s); 
}      

private AType computeAssignmentRhsType(Statement current, AType lhsType, "/=", AType rhsType, Solver s){
    checkNonVoid(current, rhsType, s, "Righthand side of /= assignment");
    return computeDivisionType(current, lhsType, rhsType, s);    
}

private AType computeAssignmentRhsType(Statement current, AType lhsType, "&=", AType rhsType, Solver s){
    checkNonVoid(current, rhsType, s, "Righthand side of &= assignment");
    return computeIntersectionType(current, lhsType, rhsType, s);  
}
    
private AType computeAssignmentRhsType(Statement current, AType lhsType, "?=", AType rhsType, Solver s){
    checkNonVoid(current, rhsType, s, "Righthand side of ?= assignment");
    return alub(lhsType, rhsType);
}

private default AType computeAssignmentRhsType(Statement current, AType lhsType, str operator, AType rhsType, Solver s){
    s.report(error(current, "Unsupported operator %s in assignment", operator));
    return avalue();
}

private void checkAssignment(Statement current, (Assignable) `<QualifiedName name>`, str operator,  Statement statement, Collector c){
    <qualifier, base> = splitQualifiedName(name);
    if(!isEmpty(qualifier)){
        c.useQualified([qualifier, base], name, {variableId()}, {moduleId()});
    } else {
        if(operator == "="){
           c.define(base, variableId(), name, defLub([statement], 
            AType(Solver s){ 
                return s.getType(statement); 
            }));
        } else {
            c.define(base, variableId(), name, defLub([statement, name],  AType(Solver s){ 
                return computeAssignmentRhsType(statement, s.getType(name), operator, s.getType(statement), s); 
            }));
        }
    }
    c.calculate("assignment to `<name>`", current, [name, statement],    // TODO: add name to dependencies?
        AType(Solver s) { 
                   nameType = s.getType(name);
         		   asgType = computeAssignmentRhsType(current, nameType, operator, s.getType(statement), s);
                   if(operator == "=") 
                      s.requireComparable(asgType, nameType, error(current, "Incompatible type %t in assignment to %t variable %q", asgType, nameType, "<name>")); 
                   return asgType;   
                 });  
}

private AType computeReceiverType(Statement current, (Assignable) `<QualifiedName name>`, loc scope, Solver s){
    return s.getType(name);
}

private AType computeReceiverType(Statement current, asg: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, loc scope, Solver s){
    receiverType = computeReceiverType(current, receiver, scope, s);
    s.fact(receiver, receiverType);
    subsType = computeSubscriptionType(current, receiverType, [ s.getType(subscript) ], [ subscript ], s);
    s.fact(asg, subsType);
    return subsType;
}
    
private AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, loc scope, Solver s){
    receiverType = computeReceiverType(current, receiver, scope, s);
    s.fact(receiver, receiverType);
    return computeSliceType(current, receiverType, s.getType(optFirst), aint(), s.getType(optLast), s);
}

private AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, loc scope, Solver s){
    receiverType = computeReceiverType(current, receiver, scope, s);
    s.fact(receiver, receiverType);
    return computeSliceType(current, receiverType, s.getType(optFirst),s.getType(second), s.getType(optLast), s);
}

private AType computeReceiverType(Statement current, asg:(Assignable) `<Assignable receiver> . <Name field>`, loc scope, Solver s){
    receiverType = computeReceiverType(current, receiver, scope, s);
    s.fact(receiver, receiverType);
    fieldType = computeFieldTypeWithADT(receiverType, field, scope, s);
    s.fact(asg, fieldType);
    return fieldType;
}

// TODO: Deprecated
private AType computeReceiverType(Statement current, asg: (Assignable) `<Assignable receiver> @ <Name n>`, loc scope, Solver s){
    receiverType = computeReceiverType(current, receiver, scope, s);
    s.fact(receiver, receiverType);
    annoNameType = s.getTypeInScope(n, scope, {annoId()});
    getAnnoType = computeGetAnnotationType(current, receiverType, annoNameType, s);
    s.fact(asg, getAnnoType);
    return getAnnoType;
}

private AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, loc scope, Solver s){
   receiverType = computeReceiverType(current, receiver, scope, s);
   s.fact(receiver, receiverType);
   return receiverType;
}

private AType computeReceiverType(Statement current, (Assignable) `\< <{Assignable ","}+ elements> \>`, loc scope, Solver s){
    receiverType = atuple(atypeList([computeReceiverType(current, element, scope, s) | element <- elements]));
    s.fact(current, receiverType);
    return receiverType;
}

private void checkAssignment(Statement current, asg: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, str operator, Statement rhs, Collector c){
   names = getReceiver(receiver, c);
   
   c.use(names[0], variableRoles);
   scope = c.getScope();
   
   c.calculate("assignable with subscript", current, [subscript, rhs], 
       AType(Solver s){ 
           receiverType = computeReceiverType(current, receiver, scope, s);
           res = computeSubscriptAssignableType(current, receiverType,  subscript, operator, s.getType(rhs), s);
           asgType = isMapAType(res) ? getMapRangeType(res) : getElementType(res);
           s.fact(asg, asgType);
           //s.fact(asg, s.getType(rhs));
           return res;
         });
   collect(receiver, subscript, c);
}

private AType computeSubscriptAssignableType(Statement current, AType receiverType, Expression subscript, str operator, AType rhs, Solver s){

   if(!s.isFullyInstantiated(receiverType)) throw TypeUnavailable();
   if(!s.isFullyInstantiated(rhs)) throw TypeUnavailable();
   
   checkNonVoid(current, rhs, s, "Righthand side of subscript assignment");
   
   if(overloadedAType(rel[loc, IdRole, AType] overloads) := receiverType){
        sub_overloads = {};
        for(<key, idr, tp> <- overloads){ 
            try {
               sub_overloads += <key, idr, computeSubscriptAssignableType(current, tp, subscript, operator, rhs, s)>;
           } catch checkFailed(list[FailMessage] _): /* do nothing and try next overload */;
             catch NoBinding(): /* do nothing and try next overload */;
 //>>        catch e: /* do nothing and try next overload */;
        }
        if(isEmpty(sub_overloads)) s.report(error(current, "Subscript %q of %t cannot be resolved", subscript, receiverType));
        return overloadedAType(sub_overloads);
    }
    
    subscriptType = s.getType(subscript); // TODO: overloaded?
    
    if (isListAType(receiverType)) { 
        if (!isIntAType(subscriptType)) s.report(error(current, "Expected subscript of type `int`, found %t", subscriptType));
        return makeListType(computeAssignmentRhsType(current, getListElementType(receiverType), operator, rhs, s));
    } else if (isNodeAType(receiverType)) {
        if (!isIntAType(subscriptType)) s.report(error(current, "Expected subscript of type `int`, found %t", subscriptType));
        computeAssignmentRhsType(current, avalue(), operator, rhs, s);
        return anode([]);
    } else if (isTupleAType(receiverType)) {
        tupleFields = getTupleFields(receiverType);
        if (!isIntAType(subscriptType)) s.report(error(current, "Expected subscript of type `int`, found %t", subscriptType));
        if ((Expression)`<DecimalIntegerLiteral dil>` := subscript) {
            tupleIndex = toInt("<dil>");
            if (tupleIndex < 0 || tupleIndex >= size(getTupleFields(receiverType))) {
                s.report(error(current, "Tuple index must be between 0 and %v", size(getTupleFields(receiverType))-1));
            } else {
                tupleFields[tupleIndex] = computeAssignmentRhsType(current, tupleFields[tupleIndex], operator, rhs, s);
                return atuple(atypeList(tupleFields));
            }
         } else {
            // This type is as exact as we can get. Assuming the subscript is
            // in range, all we can infer about the resulting type is that, since
            // we could assign to each field, each field could have a type based
            // on the lub of the existing field type and the subject type.
            return atuple(atypeList([ computeAssignmentRhsType(current, tupleFields[idx], operator, rhs, s) | idx <- index(tupleFields) ]));
        }
    } else if (isMapAType(receiverType)) {
        if (!comparable(subscriptType, getMapDomainType(receiverType)))
            s.report(error(current, "Expected subscript of type %t, found %t", getMapDomainType(receiverType), subscriptType));
         return amap(alub(subscriptType, getMapDomainType(receiverType)), computeAssignmentRhsType(current, getMapRangeType(receiverType), operator, rhs, s));
    } else if (isRelAType(receiverType)) {
        relFields = getRelFields(receiverType);
        if (!comparable(subscriptType, relFields[0]))
            s.report(error(current, "Expected subscript of type %t, found %t", relFields[0], subscriptType));
        return arel(atypeList([relFields[0],computeAssignmentRhsType(current, relFields[1], operator, rhs, s)]));
    } else {
        s.report(error(current, "Cannot assign value of type %t to assignable of type %t", rhs, receiverType));
    }
    return avalue();
}

private void checkAssignment(Statement current, asg: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, str operator, Statement rhs, Collector c){
   names = getReceiver(receiver, c);
   if(optFirst is noExpression) c.fact(optFirst, aint());
   if(optLast is noExpression) c.fact(optLast, aint());
   
   c.use(names[0], variableRoles);
   
   scope = c.getScope();
   
   c.calculate("assignable with slice", current, [optFirst, optLast, rhs], 
      AType(Solver s){ 
           res = computeSliceAssignableType(current, computeReceiverType(current, receiver, scope, s),  s.getType(optFirst), aint(), s.getType(optLast), operator, s.getType(rhs), s);
           //s.requireUnify(tau, res, error(current, "Cannot bind type variable for %q", names[0]));
           s.fact(asg, s.getType(rhs));
           return res;
         });
   //collect(receiver, optFirst, optLast, receiver);
}

private void checkAssignment(Statement current, asg: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, str operator, Statement rhs, Collector c){
   names = getReceiver(receiver, c);
   if(optFirst is noExpression) c.fact(optFirst, aint());
   if(optLast is noExpression) c.fact(optLast, aint());

   c.use(names[0], variableRoles);
   scope = c.getScope();
   
   c.calculate("assignable with slice", current, [optFirst, second, optLast, rhs], 
      AType(Solver s){ 
           res = computeSliceAssignableType(current, computeReceiverType(current, receiver, scope, s),  s.getType(optFirst), s.getType(second), s.getType(optLast), operator, s.getType(rhs), s);
           //s.requireUnify(tau, res, error(current, "Cannot bind type variable for %q", names[0]));
           s.fact(asg, s.getType(rhs));
           return res;
         });
   // collect(receiver, optFirst, second, optLast, receiver);
}

private AType computeSliceAssignableType(Statement current, AType receiverType, AType first, AType step, AType last, str operator, AType rhs, Solver s){
    if(!s.isFullyInstantiated(receiverType)) throw TypeUnavailable();
    if(!s.isFullyInstantiated(first)) throw TypeUnavailable();
    if(!s.isFullyInstantiated(step)) throw TypeUnavailable();
    if(!s.isFullyInstantiated(last)) throw TypeUnavailable();
    if(!s.isFullyInstantiated(rhs)) throw TypeUnavailable();
    
    checkNonVoid(current, rhs, s, "Righthand side of slice assignment");

    failures = [];
    if(!isIntAType(first)) failures += error(current, "The first slice index must be of type `int`, found %t", first);
    if(!isIntAType(step)) failures  += error(current, "The slice step must be of type `int`, found %t", step);
    if(!isIntAType(last)) failures  += error(current, "The last slice index must be of type `int`, found %t", last);
    
    if(!isEmpty(failures)) throw s.reports(failures);
    if (isListAType(receiverType)){
        if(isListAType(rhs)){
           return makeListType(computeAssignmentRhsType(current, getListElementType(receiverType), operator, getListElementType(rhs), s));
        } else {
           //if(!subtype(rhs, receiverType)) reportError(current, "Expected <fmt(receiverType)> in slice assignment, found <fmt(rhs)>");
           return receiverType;
        }  
    } else if(isStrAType(receiverType)){ 
        s.requireSubType(rhs, astr(), error(current, "Expected `str` in slice assignment, found %t", rhs));
        return receiverType;
    } else if(isIterType(receiverType)) {
        throw rascalCheckerInternalError(getLoc(current), "Not yet implemented"); // TODO
    } else if (isNodeAType(receiverType)) {
        return makeListType(avalue());
    }
    s.report(error(current, "Cannot assign value of type %t to assignable of type %t", rhs, receiverType));
    return avalue();
}

private void checkAssignment(Statement current, asg: (Assignable) `<Assignable receiver> . <Name field>`, str operator, Statement rhs, Collector c){
   names = getReceiver(receiver, c);
   c.use(names[0], variableRoles);
   scope = c.getScope();
   
   c.calculate("assignable with field", current, [rhs], 
      AType(Solver s){ 
           res = computeFieldAssignableType(current, computeReceiverType(current, receiver, scope, s),  field, operator, s.getType(rhs), scope, s);
           //s.requireUnify(tau, res, error(current, "Cannot bind type variable for %q", names[0]));
           s.fact(asg, s.getType(rhs));
           return res;
         });
   //collect(receiver, c);
}

private AType computeFieldAssignableType(Statement current, AType receiverType, Tree field, str operator, AType rhs, loc scope, Solver s){
    fieldName = unescape("<field>");
    if(isNonTerminalAType(receiverType) && fieldName == "top"){
        return isStartNonTerminalType(receiverType) ? getStartNonTerminalType(receiverType) : receiverType;
    }
    fieldType = s.getTypeInType(receiverType, field, {fieldId(), keywordFieldId()}, scope);
    updatedFieldType = computeAssignmentRhsType(current, fieldType, operator, rhs, s);
    s.requireSubType(updatedFieldType, fieldType, error(current, "Field %q requires %t, found %t", fieldName, fieldType, updatedFieldType));
    return updatedFieldType;
}

private void checkAssignment(Statement current, asg: (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, str operator, Statement rhs, Collector c){
   names = getReceiver(receiver, c);
   c.use(names[0], variableRoles);
   scope = c.getScope();
   
   c.calculate("assignable with default expression", current, [defaultExpression, rhs], 
      AType(Solver s){ 
           res = computeDefaultAssignableType(current, computeReceiverType(current, receiver, scope, s), s.getType(defaultExpression), operator, s.getType(rhs), s);
           s.fact(asg, s.getType(rhs));
           return res;
         });
   //collect(receiver, defaultExpression, c);
}

private AType computeDefaultAssignableType(Statement current, AType receiverType, AType defaultType, str operator, AType rhs, Solver s){
//println("computeDefaultAssignableType: <receiverType>, <defaultType>, <rhs>");
    finalReceiverType = computeAssignmentRhsType(current, alub(receiverType, defaultType), operator, rhs, s);
    finalDefaultType = computeAssignmentRhsType(current, defaultType, operator, rhs, s);
    s.requireComparable(finalReceiverType, finalDefaultType, error(current, "Receiver and default expression lead to incomparable types: %t versus %t",finalReceiverType, finalDefaultType));
    return receiverType;
}

set[str] getNames(Statement s) = {"<nm>" | /QualifiedName nm := s};

private void checkAssignment(Statement current, receiver: (Assignable) `\< <{Assignable ","}+ elements> \>`, str operator, Statement rhs, Collector c){

    // Note we will use a list `taus` of type variables that is accessible in `makeDef` and `checkTupleElemAssignment` in order to make
    // new bindings to `taus` (e.g. changed list elements) visible inside those functions

    AType(Solver _) makeDef(int i) = AType(Solver _) { return taus[i]; };
    
    AType(Solver _) checkTupleElemAssignment(Statement current, list[QualifiedName] names, list[str] flatNames, set[str] namesInRhs, list[Assignable] elms, int i, str operator, Statement rhs, loc scope){
    return
        AType(Solver s){
            //println("checkTupleElemAssignment: <current>");
            rhsType = s.getType(rhs);
            //println("checkTupleElemAssignment: rhsType: <rhsType>");
            if(!isTupleAType(rhsType)) s.report(error(current, "Tuple type required, found %t", rhsType));
            rhsFields = getTupleFields(rhsType);
            //println("checkTupleElemAssignment: rhsFields <rhsFields>");
            //println("#name: <size(names)>, #rhsFields: <size(rhsFields)>");
            if(size(names) != size(rhsFields)) s.report(error(current, "Tuple type required of arity %v, found arity %v", size(names), size(rhsFields))); 
            //println("checkTupleElemAssignment: taus[<i>] : <taus[i]>, rhsFields[<i>]: <rhsFields[i]>");
            if(s.isFullyInstantiated(taus[i]) && tvar(_) !:= taus[i]){
               //println("checkTupleElemAssignment: fullyInstantiated");
               recTypeI  = computeReceiverType(current, elms[i],  scope, s);
               rhsTypeI  = computeAssignmentRhsType(current, recTypeI, operator, rhsFields[i], s);
               s.requireComparable(rhsTypeI, recTypeI, error(names[i], "Value of type %t cannot be assigned to %q of type %t", rhsFields[i],names[i], recTypeI));
                  //if(flatNames[i] in namesInRhs){
                    //taus[i] = s.getType(names[i]);
                    //s.requireUnify(taus[i], rhsTypeI, error(current, "Cannot bind variable %q", "<names[i]>"));
                  //}
             } else {
                  //println("checkTupleElemAssignment: !fullyInstantiated");
                 if(flatNames[i] in namesInRhs){
                    s.requireUnify(taus[i], names[i], error(current, "Cannot bind variable %q","<names[i]>"));
                  } else {
                    s.requireUnify(taus[i], rhsFields[i], error(current, "Cannot bind variable %q", "<names[i]>"));
                 }
                 //println("Assigning to taus[<i>]: <instantiate(taus[i])>");
                 taus[i] =  s.instantiate(taus[i]);
             }
             return taus[i];
        };
   }

   names = getReceiver(receiver, c);
   flatNames = ["<nm>" | nm <- names];
   elms = [elm | elm <- elements];
   namesInRhs = getNames(rhs);
   taus = [c.newTypeVar(nm) | nm <- names];
   for(int i <- index(names), flatNames[i] notin namesInRhs){c.define("<names[i]>", variableId(), names[i], defLub([rhs], makeDef(i)));}
   
   for(name <- names) c.useLub(name, variableRoles);
  
   scope = c.getScope();
   
   for(int i <- index(names)){
     c.calculate("assignable <i> of tuple", names[i], [rhs], checkTupleElemAssignment(current, names, flatNames, namesInRhs, elms, i, operator, rhs, scope));
   }
   c.calculate("assignable tuple", current, [rhs], AType(Solver s) { 
    s.fact(receiver, s.getType(rhs));
    return s.getType(rhs); /*return atuple(atypeList([ getType(tau) | tau <- taus])); */});
    
  //collect(elements, c);
}

// TODO: Deprecated
private void checkAssignment(Statement current, asg: (Assignable) `<Assignable receiver> @ <Name n>`, str operator, Statement rhs, Collector c){
   c.use(n, {annoId()});
   names = getReceiver(receiver, c);
   c.useLub(names[0], variableRoles);
   scope = c.getScope();
   
   c.calculate("assignable with annotation", current, [n, rhs], 
      AType(Solver s){ 
           rt = computeReceiverType(current, receiver, scope, s);
           s.fact(asg, s.getType(rhs));
           return computeAnnoAssignableType(current, rt,  n, operator, s.getType(rhs), scope, s);
         });
   collect(receiver, c);
}

private AType computeAnnoAssignableType(Statement current, AType receiverType, Name annoName, str operator, AType rhs, loc scope, Solver s){
//println("computeAnnoAssignableType: <receiverType>, <annoName>, <operator>, <rhs>");
   
    if(!s.isFullyInstantiated(receiverType)) throw TypeUnavailable();
    if(!s.isFullyInstantiated(rhs)) throw TypeUnavailable();
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := receiverType){
        anno_overloads = {};
        for(<key, idr, tp> <- overloads){ 
            try {
               anno_overloads += <key, idr, computeAnnoAssignableType(current, tp, annoName, operator, rhs, scope, s)>;
           } catch checkFailed(list[FailMessage] fms): /* do nothing and try next overload */;
             catch NoBinding(): /* do nothing and try next overload */;
 //>>        catch e: /* do nothing and try next overload */;
        }
        if(isEmpty(anno_overloads)) s.report(error(current, "Annotation on %t cannot be resolved", receiverType));
        return overloadedAType(anno_overloads);
    }
    annoNameType = s.getTypeInScope(annoName, scope, {annoId()});
    //println("annoNameType: <annoNameType>");
    
    if (isNodeAType(receiverType) || isADTAType(receiverType) || isNonTerminalAType(receiverType)) {
        if(overloadedAType(rel[loc, IdRole, AType] overloads) := annoNameType){
            anno_overloads = {};
            for(<key, annoId(), tp1> <- overloads, aanno(_, onType, tp2) := tp1, asubtype(receiverType, onType)){
               anno_overloads += <key, annoId(), tp1>;
           }
            if(isEmpty(anno_overloads)) s.report(error(current, "Annotation on %t cannot be resolved from %t", receiverType, annoNameType));
            return overloadedAType(anno_overloads);
        } else
        if(aanno(_, onType, annoType) := annoNameType){
           return annoNameType;
        } else
            s.report(error(current, "Invalid annotation type: %t", annoNameType));
    } else
        s.report(error(current, "Invalid type: expected node, ADT, or concrete syntax types, found %t", receiverType));
    return avalue();
}

private list[QualifiedName] getReceiver((Assignable) `<QualifiedName name>`, Collector c){
    c.use(name, variableRoles);
    return [name];
}
private list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <Expression subscript> ]`, Collector c) = getReceiver(receiver, c);
private list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, Collector c) =  getReceiver(receiver, c);
private list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, Collector c) =  getReceiver(receiver, c);
private list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> . <Name field>`, Collector c) = getReceiver(receiver, c);
private list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> @ <Name n>`, Collector c) = getReceiver(receiver, c);
private list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, Collector c) =  getReceiver(receiver, c);
private list[QualifiedName] getReceiver((Assignable) `\< <{Assignable ","}+ elements> \>`, Collector c) = [*getReceiver(element, c) | Assignable element <- elements];

private default list[QualifiedName] getReceiver(Assignable asg, Collector c) { throw rascalCheckerInternalError(getLoc(asg), "Unsupported assignable <asg>"); }

// ---- return, defined in Declarations, close to function declarations -------

// ---- throw -----------------------------------------------------------------

void collect(current:(Statement) `throw <Statement statement>`, Collector c){
    c.calculate("throw", current, [statement], AType(Solver _) { return abool(); });
    collect(statement, c);
}

// ---- function declaration, see Declaration ---------------------------------

void collect(current: (Statement) `<FunctionDeclaration functionDeclaration>`, Collector c){
    collect(functionDeclaration, c);
}

// ---- local variable declaration --------------------------------------------

void collect(current: (Statement) `<Type varType> <{Variable ","}+ variables>;`, Collector c){
    scope = c.getScope();
    c.enterScope(current); // wrap in extra scope to isolate variables declared in complex (function) types
        for(var <- variables){
            c.defineInScope(scope, prettyPrintName(var.name), variableId(), var.name, defType([varType], makeGetSyntaxType(varType)));
            
            if(var is initialized){
                c.enterLubScope(var);
                    initial = var.initial;
                    c.require("initialization of `<var.name>`", initial, [initial, varType], makeVarInitRequirement(var));
                    collect(initial, c); 
                c.leaveScope(var);
            } else {
                c.report(warning(var, "Variable should be initialized"));
            }
        } 
        c.require("non void", varType, [], makeNonVoidRequirement(varType, "Variable declaration"));
        c.fact(current, varType);
        
        collect(varType, c);
    c.leaveScope(current);  
}