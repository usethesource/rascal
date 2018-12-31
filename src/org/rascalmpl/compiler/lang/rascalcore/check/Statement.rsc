@bootstrapParser
module lang::rascalcore::check::Statement

extend analysis::typepal::TypePal;

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ConvertType;
extend lang::rascalcore::check::Declaration;
extend lang::rascalcore::check::Expression;
extend lang::rascalcore::check::Operators;
extend lang::rascalcore::check::Pattern;

import analysis::typepal::FailMessage;

import lang::rascalcore::check::ATypeExceptions;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::ATypeInstantiation;

import lang::rascal::\syntax::Rascal;

import lang::rascalcore::check::TypePalConfig;
 
import Set;
import List;
import Map;
import String;

// Rascal statements

// ---- assert ----------------------------------------------------------------

void collect(current: (Statement) `assert <Expression expression>;`, Collector c){
    c.fact(current, abool());
    c.requireSubType(expression, abool(), error(expression, "Assertion should be `bool`, found %t", expression));
    collect(expression, c);
} 

void collect(current: (Statement) `assert <Expression expression> : <Expression message> ;`, Collector c){
   c.fact(current, abool());
   c.requireSubType(expression, abool(), error(expression, "Assertion should be `bool`, found %t", expression));
   c.requireSubType(message, astr(), error(message, "Assertion message should be `str`, found %t", message));
   collect(expression, message, c);
} 
     
// ---- expression ------------------------------------------------------------

void collect(current: (Statement) `<Expression expression>;`, Collector c){
    c.fact(current, expression);
    collect(expression, c);
}

// ---- visit and insert ------------------------------------------------------

data VisitOrSwitchInfo = visitOrSwitchInfo(Expression expression, bool isVisit);

void collect(current: (Statement) `<Label label> <Visit vst>`, Collector c){
    c.enterScope(current);
        scope = c.getScope();
        c.setScopeInfo(scope, visitOrSwitchScope(), visitOrSwitchInfo(vst.subject, true));
        if(label is \default){
            c.define("<label.name>", labelId(), label.name, defType(avoid()));
        }
        c.fact(current, vst.subject);
        collect(vst, c);
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
        if(visitOrSwitchInfo(Expression expression, bool isVisit) := scopeInfo){
            if(isVisit){
                c.enterScope(current);
                beginPatternScope("pattern-with-action", c);
                    scope = c.getScope();
                    c.setScopeInfo(scope, replacementScope(), replacementInfo(pattern));
                    // force type calculation of pattern
                    //c.calculateEager("pattern", pattern, [], AType(Solver s){ return getPatternType(pattern, avalue(), scope, s); });
                    c.require("pattern", pattern, [], void(Solver s){ getPatternType(pattern, avalue(), scope, s); });
                    
                    conditions = replacement is conditional ? [c | Expression c <- replacement.conditions] : [];
                    
                    if(replacement is conditional){
                       storeAllowUseBeforeDef(current, replacement.replacementExpression, c);
                       c.requireEager("when conditions in replacement", replacement.conditions, conditions,
                          void (Solver s){ for(cond <- conditions){
                                  condType = s.getType(cond);
                                  if(!s.isFullyInstantiated(condType)){
                                     s.requireUnify(condType, abool(), error(cond, "Canot unify %t with `bool`", cond));
                                     condType = s.instantiate(condType);
                                  }
                                  s.requireSubType(cond, abool(), error(cond, "Condition should be `bool`, found %t", cond));
                               }
                            });
                    }
                    
                    c.requireEager("pattern replacement", current, /*replacement.replacementExpression + */conditions,
                       void (Solver s){ 
                           exprType = s.getType(replacement.replacementExpression);
                           patType = getPatternType(pattern, avalue(), scope, s);
                           if(!s.isFullyInstantiated(exprType) || !s.isFullyInstantiated(patType)){
                              s.requireUnify(exprType, patType, error(current, "Cannot unify %t with %t", patType, exprType)); 
                              exprType = s.instantiate(exprType);
                              patType = s.instantiate(patType); 
                           }
                           s.requireSubType(exprType, patType, error(current, "A pattern of type %t cannot be replaced by %t", patType, exprType));
                         });
              
                    collect(pattern, replacement, c);
                endPatternScope(c);
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
        if(visitOrSwitchInfo(Expression expression, bool isVisit) := scopeInfo){
            if(isVisit){
               c.enterScope(current);
                    scope = c.getScope();
                    c.setScopeInfo(scope, replacementScope(), replacementInfo(pattern));
                    // force type calculation of pattern
                    //c.calculateEager("pattern", pattern, [], AType(Solver s){ return getPatternType(pattern, avalue(), scope, s); });
                    c.require("pattern", pattern, [], void(Solver s){ getPatternType(pattern, avalue(), scope, s); });
                    beginPatternScope("pattern-with-action", c);
                        collect(pattern, c);
                    endPatternScope(c);
                    collect(statement, c);
              c.leaveScope(current);
              return;
           } else {
              c.enterScope(current);
                    // force type calculation of pattern
                    //c.calculateEager("pattern", pattern, [], AType(Solver s){ return getPatternType(pattern, s.getType(expression), scope, s); });
                    c.require("pattern", pattern, [], void(Solver s){ getPatternType(pattern, s.getType(expression), scope, s); });
                    beginPatternScope("pattern-with-action", c);
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

void collect(current: (Statement) `insert <Expression expr>;`, Collector c){
    replacementScopes = c.getScopeInfo(replacementScope());
    for(<scope, scopeInfo> <- replacementScopes){
      if(replacementInfo(Pattern pat) := scopeInfo){
         c.requireEager("insert expression", expr, [expr], 
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
        throw rascalCheckerInternalError(getLoc(current), "Inconsistent info from replacement scope: <info>");
      }
    }
    c.report(error(current, "Insert found outside replacement context"));
}

// loop statements, append, break and continue

data LoopInfo = loopInfo(str name, list[Tree] appends);

// ---- while -----------------------------------------------------------------

void collect(current: (Statement) `<Label label> while( <{Expression ","}+ conditions> ) <Statement body>`,  Collector c){
    c.enterScope(conditions);   // body may refer to variables defined in conditions
        loopName = "";
        if(label is \default){
            loopName = prettyPrintName(label.name);
            c.define(loopName, labelId(), label.name, defType(avoid()));
        }
        c.setScopeInfo(c.getScope(), loopScope(), loopInfo(loopName, [])); // record appends in body, initially []
        condList = [cond | Expression cond <- conditions];
        
        c.requireEager("while statement", current, condList + [body], void (Solver s){ checkConditions(condList, s); });
        beginPatternScope("conditions", c);
            collect(condList, c);
        endPatternScope(c);
        collect(body, c);
        computeLoopType("while statement", loopName, current, c);
    c.leaveScope(conditions);
}

void checkConditions(list[Expression] condList, Solver s){
    for(Expression cond <- condList){
        tcond = s.getType(cond);
        if(!s.isFullyInstantiated(tcond)){
            s.requireUnify(abool(), tcond, error(cond, "Cannot unify %t with `bool`", cond));
            tcond = s.instantiate(tcond); 
        } 
        s.requireSubType(tcond, abool(), error(cond, "Condition should be `bool`, found %t", cond));
    }
}

void computeLoopType(str loopKind, str loopName1, Statement current, Collector c){
    loopScopes = c.getScopeInfo(loopScope());
    
    for(<scope, scopeInfo> <- loopScopes){
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
        c.requireEager("do statement", current, [body, condition], void (Solver s){ checkConditions([condition], s); });
        
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
        
        c.requireEager("for statement", current, condList + [body], void (Solver s){ checkConditions(condList, s); });
        
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

void collect(current:(Statement) `break <Target target>;`, Collector c){
    c.fact(current, avoid());
    loopName = "";
    if(target is labeled){
        loopName = prettyPrintName(target.name);
        c.use(target.name, {labelId()});
    }
 
    for(<scope, scopeInfo> <- c.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] appends) := scopeInfo){
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
    loopName = "";
    if(target is labeled){
        loopName = prettyPrintName(target.name);
        c.use(target.name, {labelId()});
    }
    
    for(<scope, scopeInfo> <- c.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] appends) := scopeInfo){
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
    c.enterScope(conditions); // thenPart may refer to variables defined in conditions
        if(label is \default){
            c.define("<label.name>", labelId(), label.name, defType(avoid()));
        }
        condList = [cond | Expression cond <- conditions];
        c.fact(current, avalue());
        
        c.requireEager("if then", current, condList + thenPart, void (Solver s){ checkConditions(condList, s); });
        
        beginPatternScope("conditions", c);
        collect(condList, c);
        endPatternScope(c);
        collect(thenPart, c);
    c.leaveScope(conditions);   
}

// --- if then else -----------------------------------------------------------

void collect(current: (Statement) `<Label label> if( <{Expression ","}+ conditions> ) <Statement thenPart> else <Statement elsePart>`,  Collector c){
    //c.enterScope(current);
    c.enterScope(conditions);   // thenPart may refer to variables defined in conditions; elsePart may not
        if(label is \default){
            c.define(prettyPrintName(label.name), labelId(), label.name, defType(avoid()));
        }
        condList = [cond | cond <- conditions];
        storeExcludeUse(conditions, elsePart, c); // variable occurrences in elsePart may not refer to variables defined in conditions
        
        c.calculate("if then else", current, condList + [thenPart, elsePart],
            AType(Solver s){
                checkConditions(condList, s);
                return s.lub(thenPart, elsePart);
            });
        
        beginPatternScope("conditions", c);
        collect(condList, c);
        endPatternScope(c);
        c.enterScope(thenPart);
            collect(thenPart, c);
        c.leaveScope(thenPart);
        c.enterScope(elsePart);
            collect(elsePart, c);
        c.leaveScope(elsePart);
    c.leaveScope(conditions); 
    //c.leaveScope(current);
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
        collect(e, cases, c);
    c.leaveScope(current);
}

data SwitchInfo = switchInfo(Expression e);

// ---- fail ------------------------------------------------------------------

void collect(current: (Statement)`fail <Target target>;`, Collector c){
    loopName = "";
    if(target is labeled){
        loopName = prettyPrintName(target.name);
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
            c.useQualified([qualifier, base], name, variableRoles, {moduleId()} );
        } else {
            c.use(v, variableRoles);
        }
    }
    c.fact(current, body);
    collect(variables, bound, body, c);
}

void collect(Bound current, Collector c){
    if(current is \default){
        c.requireSubType(current.expression, aint(), error(current.expression, "Bound should have type `int`, found %t", current.expression)); 
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
    c.calculate("try", current, body + lhandlers, AType(Solver s) { return avoid(); } );
    collect(body, handlers, c);
 }
 
// ---- try finally -----------------------------------------------------------

void collect(current: (Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`, Collector c){
    lhandlers = [h | h <- handlers];
    c.calculate("try finally", current, body + lhandlers + finallyBody, AType(Solver s) { return avoid(); } );
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
        
        //c.requireEager("catch pattern", pattern, [],
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
    if(label is \default){
       c.define("<label.name>", labelId(), label.name, defType(avoid()));
    }
    stats = [ s | Statement s <- statements ];
    c.calculate("non-empty block statement", current, [stats[-1]],  AType(Solver s) { return s.getType(stats[-1]); } );
    collect(stats, c);
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
    collect(receiver, c);
}

void collect(current:(Assignable) `<Assignable receiver> ? <Expression defaultExpression >`, Collector c){
    collect(receiver, defaultExpression, c);
}

// | constructor       : Name name "(" {Assignable ","}+ arguments ")" 

void collect(current:(Assignable) `\< <{Assignable ","}+ elements> \>`, Collector c){
    collect(elements, c);
}

void collect(current:(Assignable) `<Assignable receiver> @ <Name annotation>`, Collector c){
    collect(receiver, c);
}
   
void checkAssignment(Statement current, (Assignable) `( <Assignable arg> )`, str operator, Statement statement, Collector c){
    checkAssignment(current, arg, operator, statement, c);
    //collect(arg, c);
}

AType computeAssignmentRhsType(Statement current, AType lhsType, "=", AType rhsType, Solver s)
    = rhsType;
    
AType computeAssignmentRhsType(Statement current, AType lhsType, "+=", AType rhsType, Solver s)
    = computeAdditionType(current, lhsType, rhsType, s);

AType computeAssignmentRhsType(Statement current, AType lhsType, "-=", AType rhsType, Solver s)
    = computeSubtractionType(current, lhsType, rhsType, s); 

AType computeAssignmentRhsType(Statement current, AType lhsType, "*=", AType rhsType, Solver s)
    = computeProductType(current, lhsType, rhsType, s);       

AType computeAssignmentRhsType(Statement current, AType lhsType, "/=", AType rhsType, Solver s)
    = computeDivisionType(current, lhsType, rhsType, s);    

AType computeAssignmentRhsType(Statement current, AType lhsType, "&=", AType rhsType, Solver s)
    = computeIntersectionType(current, lhsType, rhsType, s);  
    
AType computeAssignmentRhsType(Statement current, AType lhsType, "?=", AType rhsType, Solver s)
    = alub(lhsType, rhsType);

default AType computeAssignmentRhsType(Statement current, AType lhsType, str operator, AType rhsType, Solver s){
    throw rascalCheckerInternalError(getLoc(current), "<operator> not supported");
}

void checkAssignment(Statement current, (Assignable) `<QualifiedName name>`, str operator,  Statement statement, Collector c){
    <qualifier, base> = splitQualifiedName(name);
    if(!isEmpty(qualifier)){
        c.useQualified([qualifier, base], name, {variableId()}, {moduleId()});
    } else {
        if(operator == "="){
           c.define(base, variableId(), name, defLub([statement], AType(Solver s){ return s.getType(statement); }));
        } else {
           c.useLub(name, variableRoles);
        }
    }
    c.calculate("assignment to `<name>`", current, [name, statement],    // TODO: add name to dependencies?
        AType(Solver s) { nameType = s.getType(name);
         		   asgType = computeAssignmentRhsType(current, nameType, operator, s.getType(statement), s);
                   if(operator == "=") 
                      s.requireComparable(asgType, nameType, error(current, "Incompatible type %t in assignment to %t variable %q", asgType, nameType, "<name>")); 
                   return asgType;   
                 });  
}

AType computeReceiverType(Statement current, (Assignable) `<QualifiedName name>`, loc scope, Solver s){
    return s.getType(name);
}

AType computeReceiverType(Statement current, asg: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, loc scope, Solver s){
    receiverType = computeReceiverType(current, receiver, scope, s);
    s.fact(receiver, receiverType);
    subsType = computeSubscriptionType(current, receiverType, [ s.getType(subscript) ], [ subscript ], s);
    s.fact(asg, subsType);
    return subsType;
}
    
AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, loc scope, Solver s){
    receiverType = computeReceiverType(current, receiver, scope, s);
    s.fact(receiver, receiverType);
    return computeSliceType(current, receiverType, s.getType(optFirst), aint(), s.getType(optLast), s);
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, loc scope, Solver s){
    receiverType = computeReceiverType(current, receiver, scope, s);
    s.fact(receiver, receiverType);
    return computeSliceType(current, receiverType, s.getType(optFirst),s.getType(second), s.getType(optLast), s);
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> . <Name field>`, loc scope, Solver s){
    receiverType = computeReceiverType(current, receiver, scope, s);
    s.fact(receiver, receiverType);
    return computeFieldTypeWithADT(receiverType, field, scope, s);
}
    
AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> @ <Name n>`, loc scope, Solver s){
    receiverType = computeReceiverType(current, receiver, scope, s);
    s.fact(receiver, receiverType);
    annoNameType = s.getTypeInScope(n, scope, {annoId()});
    return computeGetAnnotationType(current, receiverType, annoNameType, s);
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, loc scope, Solver s)
    = computeReceiverType(current, receiver, scope, s);

AType computeReceiverType(Statement current, (Assignable) `\< <{Assignable ","}+ elements> \>`, loc scope, Solver s){
    receiverType = atuple(atypeList([computeReceiverType(current, element, scope, s) | element <- elements]));
    s.fact(current, receiverType);
    return receiverType;
}

void checkAssignment(Statement current, asg: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, str operator, Statement rhs, Collector c){
   names = getReceiver(receiver, c);
   
   c.use(names[0], variableRoles);
   scope = c.getScope();
   
   c.calculate("assignable with subscript", current, [subscript, rhs], 
       AType(Solver s){ 
           receiverType = computeReceiverType(current, receiver, scope, s);
           res = computeSubscriptAssignableType(current, receiverType,  subscript, operator, s.getType(rhs), s);
           s.fact(asg, s.getType(rhs));
           return res;
         });
   collect(receiver, subscript, c);
}

AType computeSubscriptAssignableType(Statement current, AType receiverType, Expression subscript, str operator, AType rhs, Solver s){

   if(!s.isFullyInstantiated(receiverType)) throw TypeUnavailable();
   if(!s.isFullyInstantiated(rhs)) throw TypeUnavailable();
   
   if(overloadedAType(rel[loc, IdRole, AType] overloads) := receiverType){
        sub_overloads = {};
        for(<key, idr, tp> <- overloads){ 
            try {
               sub_overloads += <key, idr, computeSubscriptAssignableType(current, tp, subscript, operator, rhs, s)>;
           } catch checkFailed(list[FailMessage] fms): /* do nothing and try next overload */;
             catch NoBinding(): /* do nothing and try next overload */;
 //>>        catch e: /* do nothing and try next overload */;
        }
        if(isEmpty(sub_overloads)) s.report(error(current, "Subscript %q of %t cannot be resolved", subscript, receiverType));
        return overloadedAType(sub_overloads);
    }
    
    subscriptType = s.getType(subscript); // TODO: overloaded?
    
    if (isListType(receiverType)) { 
        if (!isIntType(subscriptType)) s.report(error(current, "Expected subscript of type `int`, not %t", subscriptType));
        return makeListType(computeAssignmentRhsType(current, getListElementType(receiverType), operator, rhs, s));
    } else if (isNodeType(receiverType)) {
        if (!isIntType(subscriptType)) s.report(error(current, "Expected subscript of type `int`, not %t", subscriptType));
        computeAssignmentRhsType(current, avalue(), operator, rhs, s);
        return anode([]);
    } else if (isTupleType(receiverType)) {
        tupleFields = getTupleFields(receiverType);
        if (!isIntType(subscriptType)) s.report(error(current, "Expected subscript of type `int`, not %t", subscriptType));
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
    } else if (isMapType(receiverType)) {
        if (!comparable(subscriptType, getMapDomainType(receiverType)))
            s.report(error(current, "Expected subscript of type %t, not %t", getMapDomainType(receiverType), subscriptType));
         return amap(alub(subscriptType, getMapDomainType(receiverType)), computeAssignmentRhsType(current, getMapRangeType(receiverType), operator, rhs, s));
    } else if (isRelType(receiverType)) {
        relFields = getRelFields(receiverType);
        if (!comparable(subscriptType, relFields[0]))
            s.report(error(current, "Expected subscript of type %t, not %t", relFields[0], subscriptType));
        return arel(atypeList([relFields[0],computeAssignmentRhsType(current, relFields[1], operator, rhs, s)]));
    } else {
        s.report(error(current, "Cannot assign value of type %t to assignable of type %t", rhs, receiverType));
    }
}

void checkAssignment(Statement current, asg: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, str operator, Statement rhs, Collector c){
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

void checkAssignment(Statement current, asg: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, str operator, Statement rhs, Collector c){
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

AType computeSliceAssignableType(Statement current, AType receiverType, AType first, AType step, AType last, str operator, AType rhs, Solver s){
    if(!s.isFullyInstantiated(receiverType)) throw TypeUnavailable();
    if(!s.isFullyInstantiated(first)) throw TypeUnavailable();
    if(!s.isFullyInstantiated(step)) throw TypeUnavailable();
    if(!s.isFullyInstantiated(last)) throw TypeUnavailable();
    if(!s.isFullyInstantiated(rhs)) throw TypeUnavailable();

    failures = [];
    if(!isIntType(first)) failures += error(current, "The first slice index must be of type `int`, found %t", first);
    if(!isIntType(step)) failures  += error(current, "The slice step must be of type `int`, found %t", step);
    if(!isIntType(last)) failures  += error(current, "The last slice index must be of type `int`, found %t", last);
    
    if(!isEmpty(failures)) throw s.reports(failures);
    if (isListType(receiverType)){
        if(isListType(rhs)){
           return makeListType(computeAssignmentRhsType(current, getListElementType(receiverType), operator, getListElementType(rhs), s));
        } else {
           //if(!subtype(rhs, receiverType)) reportError(current, "Expected <fmt(receiverType)> in slice assignment, found <fmt(rhs)>");
           return receiverType;
        }  
    } else if(isStrType(receiverType)){ 
        s.requireSubType(rhs, astr(), error(current, "Expected `str` in slice assignment, found %t", rhs));
        return receiverType;
    } else if(isNonTerminalIterType(receiverType)) {
        throw rascalCheckerInternalError(getLoc(current), "Not yet implemented"); // TODO
    } else if (isNodeType(receiverType)) {
        return makeListType(avalue());
    }
        s.report(error(current, "Cannot assign value of type %t to assignable of type %t", rhs, receiverType));
}

void checkAssignment(Statement current, asg: (Assignable) `<Assignable receiver> . <Name field>`, str operator, Statement rhs, Collector c){
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

AType computeFieldAssignableType(Statement current, AType receiverType, Tree field, str operator, AType rhs, loc scope, Solver s){
    fieldName = unescape("<field>");
    if(isNonTerminalType(receiverType) && fieldName == "top"){
        return receiverType;
    }
    fieldType = s.getTypeInType(receiverType, field, {fieldId(), keywordFieldId()}, scope);
    updatedFieldType = computeAssignmentRhsType(current, fieldType, operator, rhs, s);
    s.requireSubType(updatedFieldType, fieldType, error(current, "Field %q requires %t, found %t", fieldName, fieldType, updatedFieldType));
    return updatedFieldType;
}

void checkAssignment(Statement current, asg: (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, str operator, Statement rhs, Collector c){
   names = getReceiver(receiver, c);
   c.use(names[0], variableRoles);
   scope = c.getScope();
   
   c.calculate("assignable with default expression", current, [defaultExpression, rhs], 
      AType(Solver s){ 
           res = computeDefaultAssignableType(current, computeReceiverType(current, receiver, scope, s), s.getType(defaultExpression), operator, s.getType(rhs), scope, s);
           s.fact(asg, s.getType(rhs));
           return res;
         });
   //collect(receiver, defaultExpression, c);
}

AType computeDefaultAssignableType(Statement current, AType receiverType, AType defaultType, str operator, AType rhs, loc scope, Solver s){
//println("computeDefaultAssignableType: <receiverType>, <defaultType>, <rhs>");
    finalReceiverType = computeAssignmentRhsType(current, alub(receiverType, defaultType), operator, rhs, s);
    finalDefaultType = computeAssignmentRhsType(current, defaultType, operator, rhs, s);
    s.requireComparable(finalReceiverType, finalDefaultType, error(current, "Receiver and default expression lead to incomparable types: %t versus %t",finalReceiverType, finalDefaultType));
    return receiverType;
}

set[str] getNames(Statement s) = {"<nm>" | /QualifiedName nm := s};

void checkAssignment(Statement current, receiver: (Assignable) `\< <{Assignable ","}+ elements> \>`, str operator, Statement rhs, Collector c){

    // Note we will use a list `taus` of type variables that is accessible in `makeDef` and `checkTupleElemAssignment` in order to make
    // new bindings to `taus` (e.g. changed list elements) visible inside those functions

    AType(Solver s) makeDef(int i) = AType(Solver s) { return taus[i]; };
    
    AType(Solver s) checkTupleElemAssignment(Statement current, list[QualifiedName] names, list[str] flatNames, set[str] namesInRhs, list[Assignable] elms, int i, str operator, Statement rhs, loc scope, Collector c){
    return
        AType(Solver s){
            //println("checkTupleElemAssignment: <current>");
            rhsType = s.getType(rhs);
            //println("checkTupleElemAssignment: rhsType: <rhsType>");
            if(!isTupleType(rhsType)) s.report(error(current, "Tuple type required, found %t", rhsType));
            rhsFields = getTupleFields(rhsType);
            //println("checkTupleElemAssignment: rhsFields <rhsFields>");
            //println("#name: <size(names)>, #rhsFields: <size(rhsFields)>");
            if(size(names) != size(rhsFields)) s.report(error(current, "Tuple type required of arity %v, found arity %v", size(names), size(rhsFields))); 
            //println("checkTupleElemAssignment: taus[<i>] : <taus[i]>, rhsFields[<i>]: <rhsFields[i]>");
            if(s.isFullyInstantiated(taus[i]) && tvar(l) !:= taus[i]){
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
   
   for(name <- names) c.use(name, variableRoles);
  
   scope = c.getScope();
   
   for(int i <- index(names)){
     c.calculate("assignable <i> of tuple", names[i], [rhs], checkTupleElemAssignment(current, names, flatNames, namesInRhs, elms, i, operator, rhs, scope, c));
   }
   c.calculate("assignable tuple", current, [rhs], AType(Solver s) { 
    s.fact(receiver, s.getType(rhs));
    return s.getType(rhs); /*return atuple(atypeList([ getType(tau) | tau <- taus])); */});
    
  //collect(elements, c);
}

void checkAssignment(Statement current, asg: (Assignable) `<Assignable receiver> @ <Name n>`, str operator, Statement rhs, Collector c){
   c.use(n, {annoId()});
   names = getReceiver(receiver, c);
   c.useLub(names[0], variableRoles);
   scope = c.getScope();
   
   c.calculate("assignable with annotation", current, [n, rhs], 
      AType(Solver s){ 
           rt = computeReceiverType(current, receiver, scope, s);
           return computeAnnoAssignableType(current, rt,  n, operator, s.getType(rhs), scope, s);
         });
   //collect(receiver, c);
}

AType computeAnnoAssignableType(Statement current, AType receiverType, Name annoName, str operator, AType rhs, loc scope, Solver s){
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
    
    if (isNodeType(receiverType) || isADTType(receiverType) || isNonTerminalType(receiverType)) {
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
}



list[QualifiedName] getReceiver((Assignable) `<QualifiedName name>`, Collector c){
    c.use(name, variableRoles);
    return [name];
}
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <Expression subscript> ]`, Collector c) = getReceiver(receiver, c);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, Collector c) =  getReceiver(receiver, c);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, Collector c) =  getReceiver(receiver, c);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> . <Name field>`, Collector c) = getReceiver(receiver, c);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> @ <Name n>`, Collector c) = getReceiver(receiver, c);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, Collector c) =  getReceiver(receiver, c);
list[QualifiedName] getReceiver((Assignable) `\< <{Assignable ","}+ elements> \>`, Collector c) = [*getReceiver(element, c) | Assignable element <- elements];

default list[QualifiedName] getReceiver(Assignable asg, Collector c) { throw rascalCheckerInternalError(getLoc(asg), "Unsupported assignable <asg>"); }

// ---- return, defined in Declarations, close to function declarations -------

// ---- throw -----------------------------------------------------------------

void collect(current:(Statement) `throw <Statement statement>`, Collector c){
    c.calculate("throw", current, [statement], AType(Solver s) { return abool(); });
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
            c.defineInScope(scope, prettyPrintName(var.name), variableId(), var.name, defType([varType], makeSyntaxType(varType)));
            
            if(var is initialized){
                c.enterLubScope(var);
                    initial = var.initial;
                    c.require("initialization of `<var.name>`", initial, [initial, varType], makeVarInitRequirement(var));
                    collect(initial, c); 
                c.leaveScope(var);
            }
        } 
        c.fact(current, varType);
        collect(varType, c);
    c.leaveScope(current);  
}