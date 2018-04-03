module lang::rascalcore::check::Statement

extend analysis::typepal::TypePal;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeExceptions;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::ATypeInstantiation;

import lang::rascal::\syntax::Rascal;
extend lang::rascalcore::check::ConvertType;
import lang::rascalcore::check::Pattern;
import lang::rascalcore::check::Expression;
import lang::rascalcore::check::Operators;
import lang::rascalcore::check::Declaration;

import lang::rascalcore::check::TypePalConfig;
 
import Set;
import List;
import Map;
import String;

// Rascal statements

// ---- assert ----------------------------------------------------------------

void collect(current: (Statement) `assert <Expression expression>;`, Collector c){
    c.fact(current, abool());
    c.require("assert statement", current, [expression],
        void (Solver s) { s.requireSubtype(expression, abool(), error(expression, "Assertion should be `bool`, found %t", expression)); });
    collect(expression, c);
} 

void collect(current: (Statement) `assert <Expression expression> : <Expression message> ;`, Collector c){
   c.fact(current, abool());
   c.require("assert statement with message", current, [expression, message],
       void (Solver s) {
            s.requireSubtype(expression, abool(), error(expression, "Assertion should be `bool`, found %t", expression));
            s.requireSubtype(message, astr(), error(message, "Assertion message should be `str`, found %t", message));
       });
   collect(expression, message, c);
} 
     
// ---- expression ------------------------------------------------------------

void collect(current: (Statement) `<Expression expression>;`, Collector c){
    c.calculate("expression as statement", current, [expression], AType(Solver s){ return s.getType(expression); });
    collect(expression, c);
}

// ---- visit and insert ------------------------------------------------------

data VisitOrSwitchInfo = visitOrSwitchInfo(Expression expression, bool isVisit);

void collect(current: (Statement) `<Label label> <Visit vst>`, Collector c){
    c.enterScope(current);
        scope = c.getScope();
        c.setScopeInfo(scope, visitOrSwitchScope(), visitOrSwitchInfo(vst.subject, true));
        if(label is \default){
            c.define("<label.name>", labelId(), label.name, noDefInfo());
        }
        c.calculate("visit statement", vst, [vst.subject], AType(Solver s){ return s.getType(vst.subject); });
        collect(vst, c);
    c.leaveScope(current);
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
                    c.calculateEager("pattern", pattern, [], AType(Solver s){ return getPatternType(pattern, avalue(), scope, s); });
                    
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
                                  s.requireSubtype(cond, abool(), error(cond, "Condition should be `bool`, found %t", cond));
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
                           s.requireSubtype(exprType, patType, error(current, "A pattern of type %t cannot be replaced by %t", patType, exprType));
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
                    c.calculateEager("pattern", pattern, [], AType(Solver s){ return getPatternType(pattern, avalue(), scope, s); });
                    beginPatternScope("pattern-with-action", c);
                        collect(pattern, c);
                    endPatternScope(c);
                    collect(statement, c);
              c.leaveScope(current);
              return;
           } else {
              c.enterScope(current);
                    // force type calculation of pattern
                    c.calculateEager("pattern", pattern, [], AType(Solver s){ 
                        return getPatternType(pattern, s.getType(expression), scope, s); });
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
                  s.requireSubtype(exprType, patType, error(expr, "Insert type should be subtype of %t, found %t", patType, exprType));
                  s.fact(current, exprType);
             });
          collect(expr, c);
          return;
      } else {
        throw rascalCheckerInternalError(at, "Inconsistent info from replacement scope: <info>");
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
            loopName = unescape("<label.name>");
            c.define(loopName, labelId(), label.name, noDefInfo());
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
        s.requireSubtype(tcond, abool(), error(cond, "Condition should be `bool`, found %t", cond));
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
            loopName = unescape("<label.name>");
            c.define(loopName, labelId(), label.name, noDefInfo());
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
            loopName = unescape("<label.name>");
            c.define(loopName, labelId(), label.name, noDefInfo());
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
        loopName = "<dataTarget.label>";
        c.use(dataTarget.label, {labelId()});
    }
   
    for(<scope, scopeInfo> <- c.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] appends) := scopeInfo){
            if(loopName == "" || loopName == loopName1){
                c.setScopeInfo(scope, loopScope(), loopInfo(loopName1, appends + [current]));
                c.calculate("append type", current, [statement], AType(Solver s){ return s.getType(statement); });
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
        loopName = "<target.name>";
        c.use(target.name, {labelId()});
    }
 
    for(<scope, scopeInfo> <- c.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] appends) := scopeInfo){
            if(loopName == "" || loopName == loopName1){
                collectParts(current, c); //<===
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
        loopName = "<target.name>";
        c.use(target.name, {labelId()});
    }
    
    for(<scope, scopeInfo> <- c.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] appends) := scopeInfo){
            if(loopName == "" || loopName == loopName1){
                 collectParts(current, c);
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
            c.define("<label.name>", labelId(), label.name, noDefInfo());
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
    c.enterScope(conditions);   // thenPart may refer to variables defined in conditions; elsePart may not
        if(label is \default){
            c.define("<label.name>", labelId(), label.name, noDefInfo());
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
}

// ---- switch ----------------------------------------------------------------

void collect(current: (Statement) `<Label label> switch ( <Expression e> ) { <Case+ cases> }`, Collector c){
    c.enterScope(current);
        if(label is \default){
            c.define("<label.name>", labelId(), label.name, noDefInfo());
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
        loopName = "<target.name>";
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
        qname = convertName(v);
        if(isQualified(qname)){
            c.useQualified([qname.qualifier, qname.name], name, {variableId()}, {moduleId()} );
        } else {
            c.use(v, {variableId()});
        }
    }
    c.fact(current, avoid());
    collect(variables, bound, body, c);
}

void collect(Bound current, Collector c){
    if(current is \default){
        c.calculate("bound", current, [current.expression],
            AType(Solver s){ 
                s.requireSubtype(current.expression, aint(), error(current.expression, "Bound should have type `int`, found %t", current.expression)); 
                return aint();
            });
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
       c.define("<label.name>", labelId(), label.name, noDefInfo());
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

void checkAssignment(Statement current, (Assignable) `( <Assignable arg> )`, str operator, Statement statement, Collector c){
    checkAssignment(current, arg, operator, statement, c);
}

AType computeAssignmentRhsType(Statement current, AType lhsType, "=", AType rhsType, Solver s){
    return rhsType;
}
    
AType computeAssignmentRhsType(Statement current, AType lhsType, "+=", AType rhsType, Solver s){
    return computeAdditionType(current, lhsType, rhsType, s);
    }

AType computeAssignmentRhsType(Statement current, AType lhsType, "-=", AType rhsType, Solver s){
    res =  computeSubtractionType(current, lhsType, rhsType, s); 
    return res;
    }   

AType computeAssignmentRhsType(Statement current, AType lhsType, "*=", AType rhsType, Solver s)
    = computeProductType(current, lhsType, rhsType, s);       

AType computeAssignmentRhsType(Statement current, AType lhsType, "/=", AType rhsType, Solver s)
    = computeDivisionType(current, lhsType, rhsType, s);    

AType computeAssignmentRhsType(Statement current, AType lhsType, "&=", AType rhsType, Solver s)
    = computeIntersectionType(current, lhsType, rhsType, s);  
    
AType computeAssignmentRhsType(Statement current, AType lhsType, "?=", AType rhsType, Solver s){
    return alub(lhsType, rhsType);  // <===
    }

default AType computeAssignmentRhsType(Statement current, AType lhsType, str operator, AType rhsType, Solver s){
    throw rascalCheckerInternalError(getLoc(current), "<operator> not supported");
}

void checkAssignment(Statement current, (Assignable) `<QualifiedName name>`, str operator,  Statement statement, Collector c){
    qname = convertName(name);
    if(isQualified(qname)){
        c.useQualified([qname.qualifier, qname.name], name, {variableId()}, {moduleId()});
    } else {
        if(operator == "="){
           //c.calculate("name of assignable", name, [statement], AType(Solver s){ return getType(statement); });
           c.define("<name>", variableId(), name, defLub([statement], 
            AType(Solver s){ return s.getType(statement); }));
        } else {
           c.useLub(name, {variableId()});
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

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, loc scope, Solver s){
    return computeSubscriptionType(current, computeReceiverType(current, receiver, scope,s), [ s.getType(subscript) ], [ subscript ], s);
}
    
AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, loc scope, Solver s){
    return computeSliceType(current, computeReceiverType(current, receiver, scope, s), s.getType(optFirst), aint(), s.getType(optLast), s);
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, loc scope, Solver s){
    return computeSliceType(current, computeReceiverType(current, receiver, scope, s), s.getType(optFirst),s.getType(second), s.getType(optLast), s);
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> . <Name field>`, loc scope, Solver s)
    = computeFieldType(current, computeReceiverType(current, receiver, scope, s), field, scope, s);
    
AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> @ <Name n>`, loc scope, Solver s){
    annoNameType = s.getTypeInScope(unescape("<n>"), scope, {annoId()});
    return computeGetAnnotationType(current, computeReceiverType(current, receiver, scope, s), annoNameType, s);
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, loc scope, Solver s){
    return computeReceiverType(current, receiver, scope, s);
}

AType computeReceiverType(Statement current, (Assignable) `\< <{Assignable ","}+ elements> \>`, loc scope, Solver s){
    return atuple(atypeList([computeReceiverType(current, element, scope, s) | element <- elements]));
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, str operator, Statement rhs, Collector c){
   names = getReceiver(receiver, c);
   
   tau = c.newTypeVar(names[0]);
   c.define("<names[0]>", variableId(), names[0], defLub([], AType(Solver s){ return s.getType(tau); }));
   scope = c.getScope();
   
   c.calculate("assignable with subscript", current, [subscript, rhs], 
       AType(Solver s){ 
           res = computeSubscriptAssignableType(current, computeReceiverType(current, receiver, scope, s),  subscript, operator, s.getType(rhs), s);
           s.requireUnify(tau, res, error(current, "Cannot bind type variable for %q", names[0]));
           return res;
         });
}

AType computeSubscriptAssignableType(Statement current, AType receiverType, Expression subscript, str operator, AType rhs, Solver s){

   if(!s.isFullyInstantiated(receiverType) || !s.isFullyInstantiated(rhs)) throw TypeUnavailable();
   
   if(overloadedAType(rel[loc, IdRole, AType] overloads) := receiverType){
        sub_overloads = {};
        for(<key, idr, tp> <- overloads){ 
            try {
               sub_overloads += <key, idr, computeSubscriptAssignableType(current, tp, subscript, operator, rhs, s)>;
           } catch checkFailed(set[Message] msgs): {
                ; // do nothing and try next overload
           } catch e: ; // do nothing
        }
        if(isEmpty(sub_overloads)) s.report(error(current, "Field %q on %t cannot be resolved", fieldName, receiverType));
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

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, str operator, Statement rhs, Collector c){
   names = getReceiver(receiver, c);
   if(optFirst is noExpression) c.fact(optFirst, aint());
   if(optLast is noExpression) c.fact(optLast, aint());
   
   tau = c.newTypeVar(names[0]);
   c.define("<names[0]>", variableId(), names[0], defLub([], AType(Solver s){ return s.getType(tau); }));
   scope = c.getScope();
   
   c.calculate("assignable with slice", current, [optFirst, optLast, rhs], 
      AType(Solver s){ 
           res = computeSliceAssignableType(current, computeReceiverType(current, receiver, scope, s),  s.getType(optFirst), aint(), s.getType(optLast), operator, s.getType(rhs), s);
           s.requireUnify(tau, res, error(current, "Cannot bind type variable for %q", names[0]));
           return res;
         });
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, str operator, Statement rhs, Collector c){
   names = getReceiver(receiver, c);
   if(optFirst is noExpression) c.fact(optFirst, aint());
   if(optLast is noExpression) c.fact(optLast, aint());
   tau = c.newTypeVar(names[0]);
   c.define("<names[0]>", variableId(), names[0], defLub([], AType(Solver s){ return s.getType(tau); }));
   scope = c.getScope();
   
   c.calculate("assignable with slice", current, [optFirst, second, optLast, rhs], 
      AType(Solver s){ 
           res = computeSliceAssignableType(current, computeReceiverType(current, receiver, scope, s),  s.getType(optFirst), s.getType(second), s.getType(optLast), operator, s.getType(rhs), s);
           s.requireUnify(tau, res, error(current, "Cannot bind type variable for %q", names[0]));
           return res;
         });
}

AType computeSliceAssignableType(Statement current, AType receiverType, AType first, AType step, AType last, str operator, AType rhs, Solver s){
    if(!s.isFullyInstantiated(receiverType) || !s.isFullyInstantiated(first) || !s.isFullyInstantiated(step) || !s.isFullyInstantiated(last) || !s.isFullyInstantiated(rhs)) throw TypeUnavailable();

    failures = {};
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
        s.requireSubtype(rhs, astr(), error(current, "Expected `str` in slice assignment, found %t", rhs));
        return receiverType;
    } else if(isNonTerminalIterType(receiverType)) {
        throw rascalCheckerInternalError(getLoc(current), "Not yet implemented"); // TODO
    } else if (isNodeType(receiverType)) {
        return makeListType(avalue());
    }
        s.report(error(current, "Cannot assign value of type %t to assignable of type %t", rhs, receiverType));
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> . <Name field>`, str operator, Statement rhs, Collector c){
   names = getReceiver(receiver, c);
   tau = c.newTypeVar(names[0]);
   c.define("<names[0]>", variableId(), names[0], defLub([], AType(Solver s){ return s.getType(tau); }));
   scope = c.getScope();
   
   c.calculate("assignable with field", current, [rhs], 
      AType(Solver s){ 
           res = computeFieldAssignableType(current, computeReceiverType(current, receiver, scope, s),  field, operator, s.getType(rhs), scope, s);
           s.requireUnify(tau, res, error(current, "Cannot bind type variable for %q", names[0]));
           return res;
         });
}

AType computeFieldAssignableType(Statement current, AType receiverType, Tree field, str operator, AType rhs, loc scope, Solver s){
   fieldName = "<field>";
   if(!s.isFullyInstantiated(receiverType) || !s.isFullyInstantiated(rhs)) throw TypeUnavailable();
    
   if(overloadedAType(rel[loc, IdRole, AType] overloads) := receiverType){
        fld_overloads = {};
        for(<key, idr, tp> <- overloads){ 
            try {
               fld_overloads += <key, idr, computeFieldAssignableType(current, tp, field, operator, rhs, scope, s)>;
           } catch checkFailed(set[Message] msgs): {
                ; // do nothing and try next overload
           } catch e: ; // do nothing
        }
        if(isEmpty(fld_overloads)) s.report(error(current, "Field %q on %t cannot be resolved", fieldName, receiverType));
        return overloadedAType(fld_overloads);
   } else if(isStartNonTerminalType(receiverType)){
        return computeFieldAssignableType(current, getStartNonTerminalType(receiverType), field, operator, rhs, scope, s);
   } else if (aadt(adtName, list[AType] actualTypeParams, _) := receiverType){
        if ((getADTName(receiverType) == "Tree" || isNonTerminalType(receiverType)) && fieldName == "top") {
            return receiverType;
        }   
        fld_overloads = {};
        for(containerDef <- s.getDefinitions(adtName, scope, {dataId(), aliasId()})){    
            try {
                selectorType = s.getTypeInScope("<field>", containerDef.defined, {fieldId()});
                //fld_overloads += <containerDef.defined, containerDef.idRole, selectorType>;
                fld_overloads += <containerDef.defined, containerDef.idRole, rascalInstantiateTypeParameters(field, s.getType(containerDef.defInfo), receiverType, selectorType, s)>;
             } catch TypeUnavailable():; /* ignore */
        }
        fieldType = overloadedAType(fld_overloads);
        if(!isEmpty(fld_overloads)){
            updatedFieldType = computeAssignmentRhsType(current, fieldType, operator, rhs, s);
            s.requireSubtype(updatedFieldType, fieldType, error(current, "Field %q requires %t, found %t", fieldName, fieldType, updatedFieldType));
            return receiverType;
        } 
        
        if (isNonTerminalType(receiverType)){
             return computeFieldAssignableType(current, aadt("Tree", [], contextFreeSyntax()), field, operator, rhs, scope, s);
        }                          
       
        s.report(error(current, "Field %q does not exist on type %t", fieldName, receiverType));
    } else if (isTupleType(receiverType)) {
        if(tupleHasFieldNames(receiverType)){
            tupleFields = getTupleFields(receiverType);
            idx = indexOf(getTupleFieldNames(receiverType), fieldName);
            if(idx >= 0){
                updatedFieldType = computeAssignmentRhsType(current, tupleFields[idx], operator, rhs, s)[label=fieldName];
                s.requireSubtype(updatedFieldType, tupleFields[idx], error(current, "Field %q requires %t, found %t", fieldName, tupleFields[idx], updatedFieldType));
                tupleFields[idx] = updatedFieldType;
                return atuple(atypeList(tupleFields));
            } else
                s.report(error(current, "Field %q does not exist on type %t", fieldName, receiverType));
        } else {
           s.report(error(current, "Field %q does not exist on type %t", fieldName, receiverType));
        }
    } else if (isNodeType(receiverType)) {
        computeAssignmentRhsType(current, avalue(), operator, rhs, s);
        return receiverType; //anode([]);
    
    } else if(isLocType(receiverType) || isDateTimeType(receiverType)){
        if(fieldName in fieldMap[removeLabels(receiverType)]){
            return receiverType;
        }
        s.report(error(current, "No field %q exists on %t", fieldName, receiverType));    
    } else if(isReifiedType(receiverType) || isRelType(receiverType) || isListRelType(receiverType) || isMapType(receiverType)){
        s.report(error(current, "Cannot assign to any field of %t", receiverType));
    } 
    s.report(error(current, "Cannot assign value of type %t to assignable of type %t", rhs, receiverType));
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, str operator, Statement rhs, Collector c){
   names = getReceiver(receiver, c);
   tau = c.newTypeVar(names[0]);
   c.define("<names[0]>", variableId(), names[0], defLub([], AType(Solver s){ return s.getType(tau); }));
   scope = c.getScope();
   
   c.calculate("assignable with default expression", current, [defaultExpression, rhs], 
      AType(Solver s){ 
           res = computeDefaultAssignableType(current, computeReceiverType(current, receiver, scope, s), s.getType(defaultExpression), operator, s.getType(rhs), scope, s);
           return res;
         });
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
            if(size(names) != size(rhsFields)) s.report(error(statement, "Tuple type required of arity %v, found arity %v", size(names), size(rhsFields))); 
            //println("checkTupleElemAssignment: taus[<i>] : <taus[i]>, rhsFields[<i>]: <rhsFields[i]>");
            if(s.isFullyInstantiated(taus[i]) && tvar(l) !:= taus[i]){
               //println("checkTupleElemAssignment: fullyInstantiated");
               recTypeI  = computeReceiverType(current, elms[i],  scope, s);
               rhsTypeI  = computeAssignmentRhsType(current, recTypeI, operator, rhsFields[i], s);
               s.requireComparable(rhsTypeI, recTypeI, error(names[i], "Value of type %t cannot be assigned to %q of type %t", rhsFields[i],names[i], recTypeI));
                  //if(flatNames[i] in namesInRhs){
                    //taus[i] = s.getType(names[i]);
                    s.requireUnify(taus[i], rhsTypeI, error(current, "Cannot bind variable %q", names[i]));
                  //}
             } else {
                  //println("checkTupleElemAssignment: !fullyInstantiated");
                 if(flatNames[i] in namesInRhs){
                    s.requireUnify(taus[i], names[i], error(current, "Cannot bind variable %q", names[i]));
                  } else {
                    s.requireUnify(taus[i], rhsFields[i], error(current, "Cannot bind variable %q", names[i]));
                 }
                 //println("Assigning to taus[<i>]: <instantiate(taus[i])>");
                 taus[i] = s.instantiate(taus[i]);
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
  
   scope = c.getScope();
   
   for(int i <- index(names)){
     c.calculate("assignable <i> of tuple", names[i], [rhs], checkTupleElemAssignment(current, names, flatNames, namesInRhs, elms, i, operator, rhs, scope, c));
   }
   c.calculate("assignable tuple", current, [rhs], AType(Solver s) { 
    return s.getType(rhs); /*return atuple(atypeList([ getType(tau) | tau <- taus])); */});
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> @ <Name n>`, str operator, Statement rhs, Collector c){
   c.use(n, {annoId()});
   names = getReceiver(receiver, c);
   c.useLub(names[0], {variableId()});
   scope = c.getScope();
   
   c.calculate("assignable with annotation", current, [n, rhs], 
      AType(Solver s){ 
           rt = computeReceiverType(current, receiver, scope, s);
           return computeAnnoAssignableType(current, rt,  unescape("<n>"), operator, s.getType(rhs), scope, s);
         });
}

AType computeAnnoAssignableType(Statement current, AType receiverType, str annoName, str operator, AType rhs, loc scope, Solver s){
//println("computeAnnoAssignableType: <receiverType>, <annoName>, <operator>, <rhs>");
   
    if(!s.isFullyInstantiated(receiverType) || !s.isFullyInstantiated(rhs)) throw TypeUnavailable();
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := receiverType){
        anno_overloads = {};
        for(<key, idr, tp> <- overloads){ 
            try {
               anno_overloads += <key, idr, computeAnnoAssignableType(current, tp, annoName, operator, rhs, scope, s)>;
           } catch checkFailed(set[Message] msgs): {
                ; // do nothing and try next overload
           } catch e: ; // do nothing
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
    c.use(name,{variableId()});
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

// ---- local variable declaration --------------------------------------------

void collect(current: (Statement) `<Type varType> <{Variable ","}+ variables>;`, Collector c){
    for(var <- variables){
        c.define(prettyPrintName(var.name), variableId(), var.name, defGetType(varType));
        
        if(var is initialized){
            c.enterLubScope(var);
                c.require("variable initialization", var.initial, [varType], makeVarInitRequirement(var.initial, varType));
                collect(var.initial, c); 
            c.leaveScope(var);
        }
    } 
    c.sameType(current, varType);
    collect(varType, c);  
}