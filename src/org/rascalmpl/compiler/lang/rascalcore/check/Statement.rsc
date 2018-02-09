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

import lang::rascalcore::check::TypePalConfig;

import Set;
import List;
import Map;
import String;

// Rascal statements

// ---- assert

void collect(current: (Statement) `assert <Expression expression>;`, TBuilder tb){
    tb.fact(current, abool());
    tb.require("assert statement", current, [expression],
        () { subtype(getType(expression), abool()) || reportError(expression, "Assertion should be `bool`, found <fmt(expression)>"); });
    collect(expression, tb);
} 

void collect(current: (Statement) `assert <Expression expression> : <Expression message> ;`, TBuilder tb){
   tb.fact(current, abool());
   tb.require("assert statement with message", current, [expression, message],
       () { subtype(getType(expression), abool()) || reportError(expression, "Assertion should be `bool`, found <fmt(expression)>");
            subtype(getType(message), astr()) || reportError(message, "Assertion message should be `str`, found <fmt(message)>");
       });
   collect(expression, message, tb);
} 
     
// ---- expression
void collect(current: (Statement) `<Expression expression>;`, TBuilder tb){
    tb.calculate("expression as statement", current, [expression], AType(){ return getType(expression); });
    collect(expression, tb);
}

// ---- visit and insert

data VisitOrSwitchInfo = visitOrSwitchInfo(Expression expression, bool isVisit);

void collect(current: (Statement) `<Label label> <Visit vst>`, TBuilder tb){
    tb.enterScope(current);
        scope = tb.getScope();
        tb.setScopeInfo(scope, visitOrSwitchScope(), visitOrSwitchInfo(vst.subject, true));
        if(label is \default){
            tb.define(unescape("<label.name>"), labelId(), label.name, noDefInfo());
        }
        tb.calculate("visit statement", vst, [vst.subject], AType(){ return getType(vst.subject); });
        collect(vst, tb);
    tb.leaveScope(current);
}

data replacementInfo = replacementInfo(Pattern pattern);

void collect(current: (PatternWithAction) `<Pattern pattern> =\> <Replacement replacement>`,  TBuilder tb){
    visitOrSwitchScopes = tb.getScopeInfo(visitOrSwitchScope());
    for(<scope, scopeInfo> <- visitOrSwitchScopes){
        if(visitOrSwitchInfo(Expression expression, bool isVisit) := scopeInfo){
            if(isVisit){
                tb.enterScope(current);
                beginPatternScope("pattern-with-action", tb);
                    scope = tb.getScope();
                    tb.setScopeInfo(scope, replacementScope(), replacementInfo(pattern));
                    // force type calculation of pattern
                    tb.calculateEager("pattern", pattern, [], AType (){ return getPatternType(pattern, avalue(), scope); });
                    
                    conditions = replacement is conditional ? [c | Expression c <- replacement.conditions] : [];
                    
                    if(replacement is conditional){
                       storeAllowUseBeforeDef(current, replacement.replacementExpression, tb);
                       tb.requireEager("when conditions in replacement", replacement.conditions, conditions,
                          (){ for(cond <- conditions){
                                  condType = getType(cond);
                                  if(!isFullyInstantiated(condType)){
                                     unify(condType, abool()) || reportError(cond, "Canot unify <fmt(cond)> with `bool`");
                                     condType = instantiate(condType);
                                  }
                                  subtype(getType(cond), abool()) || reportError(cond, "Condition should be `bool`, found <fmt(cond)>");
                               }
                            });
                    }
                    
                    tb.requireEager("pattern replacement", current, /*replacement.replacementExpression + */conditions,
                       (){ 
                           exprType = getType(replacement.replacementExpression);
                           patType = getPatternType(pattern, avalue(), scope);
                           if(!isFullyInstantiated(exprType) || !isFullyInstantiated(patType)){
                              unify(exprType, patType) || reportError(current, "Cannot unify <fmt(patType)> with <fmt(exprType)>"); 
                              exprType = instantiate(exprType);
                              patType = instantiate(patType); 
                           }
                           subtype(exprType, patType) || reportError(current, "A pattern of type <fmt(patType)> cannot be replaced by <fmt(exprType)>");
                         });
              
                    collect(pattern, replacement, tb);
                endPatternScope(tb);
                tb.leaveScope(current);
                return;
             } else {
                tb.reportError(current, "Pattern with Action found inside a switch statement");
                return;
             }
        }
    }
    tb.reportError(current, "Pattern with Action found outside switch or visit statement");
}

void collect(current: (PatternWithAction) `<Pattern pattern>: <Statement statement>`,  TBuilder tb){
    visitOrSwitchScopes = tb.getScopeInfo(visitOrSwitchScope());
    for(<scope, scopeInfo> <- visitOrSwitchScopes){
        if(visitOrSwitchInfo(Expression expression, bool isVisit) := scopeInfo){
            if(isVisit){
               tb.enterScope(current);
                    scope = tb.getScope();
                    tb.setScopeInfo(scope, replacementScope(), replacementInfo(pattern));
                    // force type calculation of pattern
                    tb.calculateEager("pattern", pattern, [], AType (){ return getPatternType(pattern, avalue(), scope); });
                    beginPatternScope("pattern-with-action", tb);
                        collect(pattern, tb);
                    endPatternScope(tb);
                    collect(statement, tb);
              tb.leaveScope(current);
              return;
           } else {
              tb.enterScope(current);
                    // force type calculation of pattern
                    tb.calculateEager("pattern", pattern, [], AType (){ 
                        return getPatternType(pattern, getType(expression), scope); });
                    beginPatternScope("pattern-with-action", tb);
                        collect(pattern, tb);
                    endPatternScope(tb);
                    collect(statement, tb);
              tb.leaveScope(current);
              return;
           }
       }
    }
    tb.reportError(current, "Pattern with Action found outside switch or visit context");
}

void collect(current: (Statement) `insert <Expression expr>;`, TBuilder tb){
    replacementScopes = tb.getScopeInfo(replacementScope());
    for(<scope, scopeInfo> <- replacementScopes){
      if(replacementInfo(Pattern pat) := scopeInfo){
         tb.requireEager("insert expression", expr, [expr], 
             () { exprType = getType(expr);
                  patType = getPatternType(pat, avalue(), scope);
                  if(!isFullyInstantiated(exprType) || !isFullyInstantiated(patType)){
                     unify(exprType, patType) || reportError(current, "Cannot unify <fmt(patType)> with <fmt(exprType)>");
                     exprType = instantiate(exprType);
                     patType = instantiate(patType);
                  }
                  subtype(exprType, patType) || reportError(expr, "Insert type should be subtype of <fmt(patType)>, found <fmt(exprType)>");
                  fact(current, exprType);
             });
          collect(expr, tb);
          return;
      } else {
        throw rascalCheckerInternalError(at, "Inconsistent info from replacement scope: <info>");
      }
    }
    tb.reportError(current, "Insert found outside replacement context");
}

// loop statements, append, break and continue

data LoopInfo = loopInfo(str name, list[Tree] appends);

// --- while

void collect(current: (Statement) `<Label label> while( <{Expression ","}+ conditions> ) <Statement body>`,  TBuilder tb){
    tb.enterScope(conditions);   // body may refer to variables defined in conditions
        loopName = "";
        if(label is \default){
            loopName = unescape("<label.name>");
            tb.define(loopName, labelId(), label.name, noDefInfo());
        }
        tb.setScopeInfo(tb.getScope(), loopScope(), loopInfo(loopName, [])); // record appends in body, initially []
        condList = [cond | Expression cond <- conditions];
        
        tb.requireEager("while statement", current, condList + [body], (){ checkConditions(condList); });
        beginPatternScope("conditions", tb);
        collect(condList, tb);
        endPatternScope(tb);
        collect(body, tb);
        computeLoopType("while statement", loopName, current, tb);
    tb.leaveScope(conditions);
}

void checkConditions(list[Expression] condList){
    for(Expression cond <- condList){
        tcond = getType(cond);
        if(!isFullyInstantiated(tcond)){
            unify(abool(), tcond) || reportError(cond, "Cannot unify <fmt(cond)> with `bool`");
            tcond = instantiate(tcond); 
        } 
        subtype(tcond, abool()) || reportError(cond, "Condition should be `bool`, found <fmt(cond)>");
    }
}

void computeLoopType(str loopKind, str loopName1, Statement current, TBuilder tb){
    loopScopes = tb.getScopeInfo(loopScope());
    
    for(<scope, scopeInfo> <- loopScopes){
        if(loopInfo(loopName2, list[Statement] appends) := scopeInfo){
           if(loopName1 == "" || loopName1 == loopName2){
              if(isEmpty(appends)){
                 tb.fact(current, alist(avoid()));
              } else {
                 tb.calculate(loopKind, current, appends, AType(){ 
                    res = alist(lub([getType(app) | app <- appends]));
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

// ---- do

void collect(current: (Statement) `<Label label> do <Statement body> while ( <Expression condition> ) ;`, TBuilder tb){
    tb.enterScope(current);   // condition may refer to variables defined in body
        loopName = "";
        if(label is \default){
            loopName = unescape("<label.name>");
            tb.define(loopName, labelId(), label.name, noDefInfo());
        }
        tb.setScopeInfo(tb.getScope(), loopScope(), loopInfo(loopName, [])); // appends in body
        tb.requireEager("do statement", current, [body, condition], (){ checkConditions([condition]); });
        
        collect(body, tb);
        beginPatternScope("conditions", tb);
        collect(condition, tb);
        endPatternScope(tb);
        computeLoopType("do statement", loopName, current, tb);
    tb.leaveScope(current); 
}

//---- for

void collect(current: (Statement) `<Label label> for( <{Expression ","}+ conditions> ) <Statement body>`,  TBuilder tb){
    tb.enterScope(current);   // body may refer to variables defined in conditions
        loopName = "";
        if(label is \default){
            loopName = unescape("<label.name>");
            tb.define(loopName, labelId(), label.name, noDefInfo());
        }
        tb.setScopeInfo(tb.getScope(), loopScope(), loopInfo(loopName, [])); // appends in body
        condList = [cond | Expression cond <- conditions];
        
        tb.requireEager("for statement", current, condList + [body], (){ checkConditions(condList); });
        
        beginPatternScope("conditions", tb);
        collect(condList, tb);
        endPatternScope(tb);
        collect(body, tb);
        computeLoopType("for statement", loopName, current, tb);
       
    tb.leaveScope(current);  
}

// ---- append

void collect(current: (Statement) `append <DataTarget dataTarget> <Statement statement>`, TBuilder tb){
    loopName = "";
    if(dataTarget is labeled){
        loopName = "<dataTarget.label>";
        tb.use(dataTarget.label, {labelId()});
    }
   
    for(<scope, scopeInfo> <- tb.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] appends) := scopeInfo){
            if(loopName == "" || loopName == loopName1){
                tb.setScopeInfo(scope, loopScope(), loopInfo(loopName1, appends + [current]));
                tb.calculate("append type", current, [statement], AType(){ return getType(statement); });
                collect(statement, tb);
                return;
             }
        } else {
            throw rascalCheckerInternalError(getLoc(current), "Inconsistent info from loop scope: <scopeInfo>");
        }
    }
    tb.reportError(current, "Append outside a while/do/for statement");
}

// ---- break

void collect(current:(Statement) `break <Target target>;`, TBuilder tb){
    tb.fact(current, avoid());
    loopName = "";
    if(target is labeled){
        loopName = "<target.name>";
        tb.use(target.name, {labelId()});
    }
 
    for(<scope, scopeInfo> <- tb.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] appends) := scopeInfo){
            if(loopName == "" || loopName == loopName1){
                collectParts(current, tb); //<===
                return;
             }
        } else {
            throw rascalCheckerInternalError(getLoc(current), "Inconsistent info from loop scope: <scopeInfo>");
        }
    }
    tb.reportError(current, "Break outside a while/do/for statement");
}

// ---- continue

void collect(current:(Statement) `continue <Target target>;`, TBuilder tb){
    tb.fact(current, avoid());
    loopName = "";
    if(target is labeled){
        loopName = "<target.name>";
        tb.use(target.name, {labelId()});
    }
    
    for(<scope, scopeInfo> <- tb.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] appends) := scopeInfo){
            if(loopName == "" || loopName == loopName1){
                 collectParts(current, tb);
                 return;
             }
        } else {
            throw rascalCheckerInternalError(getLoc(current), "Inconsistent info from loop scope: <scopeInfo>");
        }
    }
    tb.reportError(current, "Continue outside a while/do/for statement");
}

// ---- if

void collect(current: (Statement) `<Label label> if( <{Expression ","}+ conditions> ) <Statement thenPart>`,  TBuilder tb){
    tb.enterScope(conditions); // thenPart may refer to variables defined in conditions
        if(label is \default){
            tb.define(unescape("<label.name>"), labelId(), label.name, noDefInfo());
        }
        condList = [cond | Expression cond <- conditions];
        tb.fact(current, avalue());
        
        tb.requireEager("if then", current, condList + thenPart, (){ checkConditions(condList); });
        
        beginPatternScope("conditions", tb);
        collect(condList, tb);
        endPatternScope(tb);
        collect(thenPart, tb);
    tb.leaveScope(conditions);   
}

// --- if then else

void collect(current: (Statement) `<Label label> if( <{Expression ","}+ conditions> ) <Statement thenPart> else <Statement elsePart>`,  TBuilder tb){
    tb.enterScope(conditions);   // thenPart may refer to variables defined in conditions; elsePart may not
        if(label is \default){
            tb.define(unescape("<label.name>"), labelId(), label.name, noDefInfo());
        }
        condList = [cond | cond <- conditions];
        storeExcludeUse(conditions, elsePart, tb); // variable occurrences in elsePart may not refer to variables defined in conditions
        
        tb.calculate("if then else", current, condList + [thenPart, elsePart],
            AType (){
                checkConditions(condList);
                return lub(getType(thenPart), getType(elsePart));
            });
        
        beginPatternScope("conditions", tb);
        collect(condList, tb);
        endPatternScope(tb);
        collect(thenPart, elsePart, tb);
    tb.leaveScope(conditions); 
}

// ---- switch

void collect(current: (Statement) `<Label label> switch ( <Expression e> ) { <Case+ cases> }`, TBuilder tb){
    tb.enterScope(current);
        if(label is \default){
            tb.define(unescape("<label.name>"), labelId(), label.name, noDefInfo());
        }
        scope = tb.getScope();
        tb.setScopeInfo(scope, visitOrSwitchScope(), visitOrSwitchInfo(e, false));
        tb.fact(current, avoid());
        collect(e, cases, tb);
    tb.leaveScope(current);
}

data SwitchInfo = switchInfo(Expression e);

// ---- fail
void collect(current: (Statement)`fail <Target target>;`, TBuilder tb){
    loopName = "";
    if(target is labeled){
        loopName = "<target.name>";
        tb.use(target.name, {labelId(), functionId()});
    }
    tb.fact(current, avoid());
}

// ---- filter

void collect(current: (Statement) `filter;`, TBuilder tb){
    tb.fact(current, avoid());
}
// ---- solve

void collect(current: (Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`, TBuilder tb){
    for(v <- variables){
        qname = convertName(v);
        if(isQualified(qname)){
            tb.useQualified([qname.qualifier, qname.name], name, {variableId()}, {moduleId()} );
        } else {
            tb.use(v, {variableId()});
        }
    }
    tb.fact(current, avoid());
    collect(variables, bound, body, tb);
}

void collect(Bound current, TBuilder tb){
    if(current is \default){
        tb.calculate("bound", current, [current.expression],
            AType(){ if(subtype(getType(current.expression), aint())) return aint();
                     reportError(current.expression, "Bound should have type `int`, found `<fmt(getType(current.expression))>"); 
                   });
        collect(current.expression, tb);
    } else {
        tb.fact(current, avoid());
    }
}

// ---- try, try finally, catch

// ---- try
 
 void collect(current: (Statement) `try <Statement body> <Catch+ handlers>`, TBuilder tb){
    lhandlers = [ h | h <- handlers ];
    tb.calculate("try", current, body + lhandlers, AType() { return avoid(); } );
    collect(body, handlers, tb);
 }
 
// ---- try finally

void collect(current: (Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`, TBuilder tb){
    lhandlers = [h | h <- handlers];
    tb.calculate("try finally", current, body + lhandlers + finallyBody, AType() { return avoid(); } );
    collect(body, handlers, finallyBody, tb);
}

// ---- catch

void collect(current: (Catch) `catch: <Statement body>`, TBuilder tb){
    tb.fact(current, avoid());
    collect(body, tb);
}

void collect(current: (Catch) `catch <Pattern pattern>: <Statement body>`, TBuilder tb){
    tb.fact(current, avoid());
    tb.enterScope(current);
        beginPatternScope("catch", tb);
        collect(pattern, tb);
        //tb.requireEager("catch pattern", pattern, [],
        //   () { tpat = getType(pattern);
        //        if(!isFullyInstantiated(tpat)){
        //           unify(tpat, avalue()) || reportError(pattern, "Cannot bind pattern");
        //        }
        //      });
        endPatternScope(tb);
        collect(body, tb);
    tb.leaveScope(current);
}

// ---- non-empty block

void collect(current: (Statement) `<Label label> { <Statement+ statements> }`, TBuilder tb){
    if(label is \default){
       tb.define(unescape("<label.name>"), labelId(), label.name, noDefInfo());
    }
    stats = [ s | Statement s <- statements ];
    tb.calculate("non-empty block statement", current, [stats[-1]],  AType() { return getType(stats[-1]); } );
    collect(stats, tb);
}

// ---- empty block

void collect(current: (Statement) `;`, TBuilder tb){
    tb.fact(current, avoid());
}

// ---- assignment

void collect(current: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`, TBuilder tb){
    checkAssignment(current, assignable, "<operator>", statement, tb);
    collect(assignable, statement, tb);
}

void checkAssignment(Statement current, (Assignable) `( <Assignable arg> )`, str operator, Statement statement, TBuilder tb){
    checkAssignment(current, arg, operator, statement, tb);
}

AType computeAssignmentRhsType(Statement current, AType lhsType, "=", AType rhsType){
    return rhsType;
}
    
AType computeAssignmentRhsType(Statement current, AType lhsType, "+=", AType rhsType){
    return computeAdditionType(current, lhsType, rhsType);
    }

AType computeAssignmentRhsType(Statement current, AType lhsType, "-=", AType rhsType){
    res =  computeSubtractionType(current, lhsType, rhsType); 
    return res;
    }   

AType computeAssignmentRhsType(Statement current, AType lhsType, "*=", AType rhsType)
    = computeProductType(current, lhsType, rhsType);       

AType computeAssignmentRhsType(Statement current, AType lhsType, "/=", AType rhsType)
    = computeDivisionType(current, lhsType, rhsType);    

AType computeAssignmentRhsType(Statement current, AType lhsType, "&=", AType rhsType)
    = computeIntersectionType(current, lhsType, rhsType);  
    
AType computeAssignmentRhsType(Statement current, AType lhsType, "?=", AType rhsType){
    return lub(lhsType, rhsType);  // <===
    }

default AType computeAssignmentRhsType(Statement current, AType lhsType, str operator, AType rhsType){
    throw rascalCheckerInternalError(getLoc(current), "<operator> not supported");
}

void checkAssignment(Statement current, (Assignable) `<QualifiedName name>`, str operator,  Statement statement, TBuilder tb){
    qname = convertName(name);
    if(isQualified(qname)){
        tb.useQualified([qname.qualifier, qname.name], name, {variableId()}, {moduleId()});
    } else {
        if(operator == "="){
           //tb.calculate("name of assignable", name, [statement], AType(){ return getType(statement); });
           tb.define(unescape("<name>"), variableId(), name, defLub([statement], AType(){ 
            return  getType(statement); 
            }));
        } else {
           tb.useLub(name, {variableId()});
        }
    }
    tb.calculate("assignment to `<name>`", current, [name, statement],    // TODO: add name to dependencies?
        AType () { nameType = getType(name);
                   asgType = computeAssignmentRhsType(current, nameType, operator, getType(statement));
                   if(operator == "=") 
                      comparable(asgType, nameType) || reportError(current, "Incompatible type <fmt(asgType)> in assignment to <fmt(nameType)> variable `<name>`"); 
                   return asgType;   
                 });  
}

AType computeReceiverType(Statement current, (Assignable) `<QualifiedName name>`, Key scope, TBuilder tb){
    return getType(name); //expandUserTypes(getType(name), scope);
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, Key scope, TBuilder tb){
    return computeSubscriptionType(current, computeReceiverType(current, receiver, scope, tb), [ getType(subscript) ], [ subscript ]);
}
    
AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, Key scope, TBuilder tb){
    return computeSliceType(current, computeReceiverType(current, receiver, scope, tb), getType(optFirst), aint(), getType(optLast));
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, Key scope, TBuilder tb){
    return computeSliceType(current, computeReceiverType(current, receiver, scope, tb), getType(optFirst), getType(second), getType(optLast));
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> . <Name field>`, Key scope, TBuilder tb)
    = computeFieldType(current, computeReceiverType(current, receiver, scope, tb), "<field>", scope, tb);
    
AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> @ <Name n>`, Key scope, TBuilder tb){
    annoNameType = expandUserTypes(getType(unescape("<n>"), scope, {annoId()}), scope);
    return computeGetAnnotationType(current, computeReceiverType(current, receiver, scope, tb), annoNameType);
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, Key scope, TBuilder tb){
    return computeReceiverType(current, receiver, scope, tb);
}

AType computeReceiverType(Statement current, (Assignable) `\< <{Assignable ","}+ elements> \>`, Key scope, TBuilder tb){
    return atuple(atypeList([computeReceiverType(current, element, scope, tb) | element <- elements]));
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, str operator, Statement rhs, TBuilder tb){
   names = getReceiver(receiver, tb);
   
   tau = tb.newTypeVar(names[0]);
   tb.define(unescape("<names[0]>"), variableId(), names[0], defLub([], AType(){ return getType(tau); }));
   scope = tb.getScope();
   
   tb.calculate("assignable with subscript", current, [subscript, rhs], 
       AType (){ 
           res = computeSubscriptAssignableType(current, computeReceiverType(current, receiver, scope, tb),  subscript, operator, getType(rhs));
           unify(tau, res) || reportError(current, "Cannot bind type variable for <fmt("<names[0]>")>");
           return res;
         });
}

AType computeSubscriptAssignableType(Statement current, AType receiverType, Expression subscript, str operator, AType rhs){

   if(!isFullyInstantiated(receiverType) || !isFullyInstantiated(rhs)) throw TypeUnavailable();
   
   if(overloadedAType(rel[Key, IdRole, AType] overloads) := receiverType){
        sub_overloads = {};
        for(<key, idr, tp> <- overloads){ 
            try {
               sub_overloads += <key, idr, computeSubscriptAssignableType(current, tp, subscript, operator, rhs)>;
           } catch checkFailed(set[Message] msgs): {
                ; // do nothing and try next overload
           } catch e: ; // do nothing
        }
        if(isEmpty(sub_overloads)) reportError(current, "Field <fmt(fieldName)> on <fmt(receiverType)> cannot be resolved");
        return overloadedAType(sub_overloads);
    }
    
    subscriptType = getType(subscript); // TODO: overloaded?
    
    if (isListType(receiverType)) { 
        if (!isIntType(subscriptType)) reportError(current, "Expected subscript of type `int`, not <fmt(subscriptType)>");
        return makeListType(computeAssignmentRhsType(current, getListElementType(receiverType), operator, rhs));
    } else if (isNodeType(receiverType)) {
        if (!isIntType(subscriptType)) reportError(current, "Expected subscript of type `int`, not <fmt(subscriptType)>");
        computeAssignmentRhsType(current, avalue(), operator, rhs);
        return anode([]);
    } else if (isTupleType(receiverType)) {
        tupleFields = getTupleFields(receiverType);
        if (!isIntType(subscriptType)) reportError(current, "Expected subscript of type `int`, not <fmt(subscriptType)>");
        if ((Expression)`<DecimalIntegerLiteral dil>` := subscript) {
            tupleIndex = toInt("<dil>");
            if (tupleIndex < 0 || tupleIndex >= size(getTupleFields(receiverType))) {
                reportError(current, "Tuple index must be between 0 and <size(getTupleFields(receiverType))-1>");
            } else {
                tupleFields[tupleIndex] = computeAssignmentRhsType(current, tupleFields[tupleIndex], operator, rhs);
                return atuple(atypeList(tupleFields));
            }
         } else {
            // This type is as exact as we can get. Assuming the subscript is
            // in range, all we can infer about the resulting type is that, since
            // we could assign to each field, each field could have a type based
            // on the lub of the existing field type and the subject type.
            return atuple(atypeList([ computeAssignmentRhsType(current, tupleFields[idx], operator, rhs) | idx <- index(tupleFields) ]));
        }
    } else if (isMapType(receiverType)) {
        if (!comparable(subscriptType, getMapDomainType(receiverType)))
            reportError(current, "Expected subscript of type <fmt(getMapDomainType(receiverType))>, not <fmt(subscriptType)>");
        return amap(lub(subscriptType, getMapDomainType(receiverType)), computeAssignmentRhsType(current, getMapRangeType(receiverType), operator, rhs));
    } else if (isRelType(receiverType)) {
        relFields = getRelFields(receiverType);
        if (!comparable(subscriptType, relFields[0]))
            reportError(current, "Expected subscript of type <fmt(relFields[0])>, not <fmt(subscriptType)>");
        return arel(atypeList([relFields[0],computeAssignmentRhsType(current, relFields[1], operator, rhs)]));
    } else {
        reportError(current, "Cannot assign value of type <fmt(rhs)> to assignable of type <fmt(receiverType)>");
    }
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, str operator, Statement rhs, TBuilder tb){
   names = getReceiver(receiver, tb);
   if(optFirst is noExpression) tb.fact(optFirst, aint());
   if(optLast is noExpression) tb.fact(optLast, aint());
   
   tau = tb.newTypeVar(names[0]);
   tb.define(unescape("<names[0]>"), variableId(), names[0], defLub([], AType(){ return getType(tau); }));
   scope = tb.getScope();
   
   tb.calculate("assignable with slice", current, [optFirst, optLast, rhs], 
      AType (){ 
           res = computeSliceAssignableType(current, computeReceiverType(current, receiver, scope, tb),  getType(optFirst), aint(), getType(optLast), operator, getType(rhs));
           unify(tau, res) || reportError(current, "Cannot bind type variable for <fmt("<names[0]>")>");
           return res;
         });
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, str operator, Statement rhs, TBuilder tb){
   names = getReceiver(receiver, tb);
   if(optFirst is noExpression) tb.fact(optFirst, aint());
   if(optLast is noExpression) tb.fact(optLast, aint());
   tau = tb.newTypeVar(names[0]);
   tb.define(unescape("<names[0]>"), variableId(), names[0], defLub([], AType(){ return getType(tau); }));
   scope = tb.getScope();
   
   tb.calculate("assignable with slice", current, [optFirst, second, optLast, rhs], 
      AType (){ 
           res = computeSliceAssignableType(current, computeReceiverType(current, receiver, scope, tb),  getType(optFirst), getType(second), getType(optLast), operator, getType(rhs));
           unify(tau, res) || reportError(current, "Cannot bind type variable for <fmt("<names[0]>")>");
           return res;
         });
}

AType computeSliceAssignableType(Statement current, AType receiverType, AType first, AType step, AType last, str operator, AType rhs){
    if(!isFullyInstantiated(receiverType) || !isFullyInstantiated(first) || !isFullyInstantiated(step) || !isFullyInstantiated(last) || !isFullyInstantiated(rhs)) throw TypeUnavailable();

    failures = {};
    if(!isIntType(first)) failures += error(current, "The first slice index must be of type `int`, found <fmt(first)>");
    if(!isIntType(step)) failures  += error(current, "The slice step must be of type `int`, found <fmt(step)>");
    if(!isIntType(last)) failures  += error(current, "The last slice index must be of type `int`, found <fmt(last)>");
    
    if(!isEmpty(failures)) throw reportErrors(failures);
    if (isListType(receiverType)){
        if(isListType(rhs)){
           return makeListType(computeAssignmentRhsType(current, getListElementType(receiverType), operator, getListElementType(rhs)));
        } else {
           //if(!subtype(rhs, receiverType)) reportError(current, "Expected <fmt(receiverType)> in slice assignment, found <fmt(rhs)>");
           return receiverType;
        }  
    } else if(isStrType(receiverType)){ 
        if(!subtype(rhs, astr())) reportError(current, "Expected `str` in slice assignment, found <fmt(rhs)>");
        return receiverType;
    } else if(isNonTerminalIterType(receiverType)) {
        throw rascalCheckerInternalError(getLoc(current), "Not yet implemented"); // TODO
    } else if (isNodeType(receiverType)) {
        return makeListType(avalue());
    }
        reportError(current, "Cannot assign value of type <fmt(rhs)> to assignable of type <fmt(receiverType)>");
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> . <Name field>`, str operator, Statement rhs, TBuilder tb){
   names = getReceiver(receiver, tb);
   tau = tb.newTypeVar(names[0]);
   tb.define(unescape("<names[0]>"), variableId(), names[0], defLub([], AType(){ return getType(tau); }));
   scope = tb.getScope();
   
   tb.calculate("assignable with field", current, [rhs], 
      AType (){ 
           res = computeFieldAssignableType(current, computeReceiverType(current, receiver, scope, tb),  unescape("<field>"), operator, getType(rhs), scope);
           unify(tau, res) || reportError(current, "Cannot bind type variable for <fmt("<names[0]>")>");
           return res;
         });
}

AType computeFieldAssignableType(Statement current, AType receiverType, str fieldName, str operator, AType rhs, Key scope){
   
   if(!isFullyInstantiated(receiverType) || !isFullyInstantiated(rhs)) throw TypeUnavailable();
    
   if(auser(adtName, list[AType] actualTypeParams) := receiverType){
      return computeFieldAssignableType(current, expandUserTypes(receiverType, scope), fieldName, operator, rhs, scope);
   } else if(overloadedAType(rel[Key, IdRole, AType] overloads) := receiverType){
        fld_overloads = {};
        for(<key, idr, tp> <- overloads){ 
            try {
               fld_overloads += <key, idr, computeFieldAssignableType(current, tp, fieldName, operator, rhs, scope)>;
           } catch checkFailed(set[Message] msgs): {
                ; // do nothing and try next overload
           } catch e: ; // do nothing
        }
        if(isEmpty(fld_overloads)) reportError(current, "Field <fmt(fieldName)> on <fmt(receiverType)> cannot be resolved");
        return overloadedAType(fld_overloads);
   } else if(isStartNonTerminalType(receiverType)){
        return computeFieldAssignableType(current, getStartNonTerminalType(receiverType), fieldName, operator, rhs, scope);
   } else if (aadt(adtName, list[AType] actualTypeParams, _) := receiverType){
        try {
            if ((getADTName(receiverType) == "Tree" || isNonTerminalType(receiverType)) && fieldName == "top") {
                return receiverType;
            }            
            fieldType = getType(fieldName, scope, {/*formalId(),*/ fieldId()});
            
            try {
                fieldType = expandUserTypes(fieldType, scope);
            } catch TypeUnavailable():
                reportError(current, "Cannot expand type of field <fmt(fieldType)>, missing import of field type?");
                
            declaredInfo = getDefinitions(adtName, scope, dataOrSyntaxIds);
            declaredType = getType(adtName, scope, dataOrSyntaxIds);
            declaredTypeParams = getADTTypeParameters(declaredType);
            
            if (size(declaredTypeParams) > 0) {
                if (size(declaredTypeParams) != size(actualTypeParams)) {
                    reportError(current, "Invalid ADT type, the number of type parameters is inconsistent");
                } else {
                    map[str, AType] bindings = ( getRascalTypeParamName(declaredTypeParams[idx]) : actualTypeParams[idx] | idx <- index(declaredTypeParams));
                    try {
                        fieldType = instantiateRascalTypeParams(fieldType, bindings);
                    } catch invalidInstantiation(str msg): {
                        reportError(current, "Failed to instantiate type parameters in field type <fmt(fieldType)>");
                    }                       
                }
            }
            
            fieldType = filterFieldType(fieldName, fieldType, declaredInfo, scope); 
            
            if(overloadedAType({}) !:= fieldType){
                updatedFieldType = computeAssignmentRhsType(current, fieldType, operator, rhs);
                subtype(updatedFieldType, fieldType) || reportError(current, "Field <fmt(fieldName)> requires <fmt(fieldType)>, found <fmt(updatedFieldType)>");     
            
                return receiverType;
            }                           
            if (isNonTerminalType(declaredType)){
                 return computeFieldAssignableType(current, aadt("Tree", [], contextFreeSyntax()), fieldName, operator, rhs, scope);
            }
            reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(receiverType)>");
        } catch TypeUnavailable(): {
            throw TypeUnavailable();
            //reportError(current, "Cannot compute type of field <fmt(fieldName)>, user type <fmt(receiverType)> has not been declared or is out of scope"); 
        }
    } else if (isTupleType(receiverType)) {
        if(tupleHasFieldNames(receiverType)){
            tupleFields = getTupleFields(receiverType);
            idx = indexOf(getTupleFieldNames(receiverType), fieldName);
            if(idx >= 0){
                updatedFieldType = computeAssignmentRhsType(current, tupleFields[idx], operator, rhs)[label=fieldName];
                subtype(updatedFieldType, tupleFields[idx]) || reportError(current, "Field <fmt(fieldName)> requires <fmt(tupleFields[idx])>, found <fmt(updatedFieldType)>");
                tupleFields[idx] = updatedFieldType;
                return atuple(atypeList(tupleFields));
            } else
                reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(receiverType)>");
        } else {
            reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(receiverType)>");
        }
    } else if (isNodeType(receiverType)) {
        computeAssignmentRhsType(current, avalue(), operator, rhs);
        return receiverType; //anode([]);
    
    } else if(isLocType(receiverType) || isDateTimeType(receiverType)){
        if(fieldName in fieldMap[receiverType]){
            return receiverType;
        }
        reportError(current, "No field <fmt(fieldName)> exists on <fmt(receiverType)>");    
    } else if(isReifiedType(receiverType) || isRelType(receiverType) || isListRelType(receiverType) || isMapType(receiverType)){
        reportError(current, "Cannot assign to any field of <fmt(receiverType)>");
    } 
        reportError(current, "Cannot assign value of type <fmt(rhs)> to assignable of type <fmt(receiverType)>");
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, str operator, Statement rhs, TBuilder tb){
   names = getReceiver(receiver, tb);
   tau = tb.newTypeVar(names[0]);
   tb.define(unescape("<names[0]>"), variableId(), names[0], defLub([], AType(){ return getType(tau); }));
   scope = tb.getScope();
   
   tb.calculate("assignable with default expression", current, [defaultExpression, rhs], 
      AType (){ 
           res = computeDefaultAssignableType(current, computeReceiverType(current, receiver, scope, tb), getType(defaultExpression), operator, getType(rhs), scope);
           return res;
         });
}

AType computeDefaultAssignableType(Statement current, AType receiverType, AType defaultType, str operator, AType rhs, Key scope){
//println("computeDefaultAssignableType: <receiverType>, <defaultType>, <rhs>");
    finalReceiverType = computeAssignmentRhsType(current, lub(receiverType, defaultType), operator, rhs);
    finalDefaultType = computeAssignmentRhsType(current, defaultType, operator, rhs);
    comparable(finalReceiverType, finalDefaultType) || reportError(current, "Receiver and default expression lead to incomparable types: <fmt(finalReceiverType)> versus <fmt(finalDefaultType)>");
    return receiverType;
}

set[str] getNames(Statement s) = {"<nm>" | /QualifiedName nm := s};



void checkAssignment(Statement current, receiver: (Assignable) `\< <{Assignable ","}+ elements> \>`, str operator, Statement rhs, TBuilder tb){

    // Note we will use a list `taus` of type variables that is accessible in `makeDef` and `checkTupleElemAssignment` in order to make
    // new bindings to `taus` (e.g. changed list elements) visible inside those functions

    AType() makeDef(int i) = AType() { return taus[i]; };
    
    AType() checkTupleElemAssignment(Statement current, list[QualifiedName] names, list[str] flatNames, set[str] namesInRhs, list[Assignable] elms, int i, str operator, Statement rhs, Key scope, TBuilder tb){
    return
        AType (){
            //println("checkTupleElemAssignment: <current>");
            rhsType = getType(rhs);
            //println("checkTupleElemAssignment: rhsType: <rhsType>");
            if(!isTupleType(rhsType)) reportError(current, "Tuple type required, found <fmt(rhsType)>");
            rhsFields = getTupleFields(rhsType);
            //println("checkTupleElemAssignment: rhsFields <rhsFields>");
            //println("#name: <size(names)>, #rhsFields: <size(rhsFields)>");
            if(size(names) != size(rhsFields)) reportError(statement, "Tuple type required of arity <size(names)>, found arity <size(rhsFields)>"); 
            //println("checkTupleElemAssignment: taus[<i>] : <taus[i]>, rhsFields[<i>]: <rhsFields[i]>");
            if(isFullyInstantiated(taus[i]) && tvar(l) !:= taus[i]){
               //println("checkTupleElemAssignment: fullyInstantiated");
               recTypeI  = computeReceiverType(current, elms[i],  scope, tb);
               rhsTypeI  = computeAssignmentRhsType(current, recTypeI, operator, rhsFields[i]);
               comparable(rhsTypeI, recTypeI) || reportError(names[i], "Value of type <fmt(rhsFields[i])> cannot be assigned to <fmt("<names[i]>")> of type <fmt(recTypeI)>");
                  //if(flatNames[i] in namesInRhs){
                    //taus[i] = getType(names[i]);
                    unify(taus[i], rhsTypeI) || reportError(current, "Cannot bind variable <fmt("<names[i]>")>");
                  //}
             } else {
                  //println("checkTupleElemAssignment: !fullyInstantiated");
                 if(flatNames[i] in namesInRhs){
                    unify(taus[i], getType(names[i])) || reportError(current, "Cannot bind variable <fmt("<names[i]>")>");
                  } else {
                    unify(taus[i], rhsFields[i]) || reportError(current, "Cannot bind variable <fmt("<names[i]>")>");
                 }
                 //println("Assigning to taus[<i>]: <instantiate(taus[i])>");
                 taus[i] = instantiate(taus[i]);
             }
             return taus[i];
        };
   }

   names = getReceiver(receiver, tb);
   flatNames = ["<nm>" | nm <- names];
   elms = [elm | elm <- elements];
   namesInRhs = getNames(rhs);
   taus = [tb.newTypeVar(nm) | nm <- names];
   for(int i <- index(names), flatNames[i] notin namesInRhs){tb.define(unescape("<names[i]>"), variableId(), names[i], defLub([rhs], makeDef(i)));}
  
   scope = tb.getScope();
   
   for(int i <- index(names)){
     tb.calculate("assignable <i> of tuple", names[i], [rhs], checkTupleElemAssignment(current, names, flatNames, namesInRhs, elms, i, operator, rhs, scope, tb));
   }
   tb.calculate("assignable tuple", current, [rhs], AType() { 
    return getType(rhs); /*return atuple(atypeList([ getType(tau) | tau <- taus])); */});
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> @ <Name n>`, str operator, Statement rhs, TBuilder tb){
   tb.use(n, {annoId()});
   names = getReceiver(receiver, tb);
   tb.useLub(names[0], {variableId()});
   scope = tb.getScope();
   
   tb.calculate("assignable with annotation", current, [n, rhs], 
      AType (){ 
           rt = computeReceiverType(current, receiver, scope, tb);
           return computeAnnoAssignableType(current, rt,  unescape("<n>"), operator, getType(rhs), scope);
         });
}

AType computeAnnoAssignableType(Statement current, AType receiverType, str annoName, str operator, AType rhs, Key scope){
//println("computeAnnoAssignableType: <receiverType>, <annoName>, <operator>, <rhs>");
   
    if(!isFullyInstantiated(receiverType) || !isFullyInstantiated(rhs)) throw TypeUnavailable();
    
    if(overloadedAType(rel[Key, IdRole, AType] overloads) := receiverType){
        anno_overloads = {};
        for(<key, idr, tp> <- overloads){ 
            try {
               anno_overloads += <key, idr, computeAnnoAssignableType(current, tp, annoName, operator, rhs, scope)>;
           } catch checkFailed(set[Message] msgs): {
                ; // do nothing and try next overload
           } catch e: ; // do nothing
        }
        if(isEmpty(anno_overloads)) reportError(current, "Annotation on <fmt(receiverType)> cannot be resolved");
        return overloadedAType(anno_overloads);
    }
    annoNameType = expandUserTypes(getType(annoName, scope, {annoId()}), scope);
    //println("annoNameType: <annoNameType>");
    
    if (isNodeType(receiverType) || isADTType(receiverType) || isNonTerminalType(receiverType)) {
        if(overloadedAType(rel[Key, IdRole, AType] overloads) := annoNameType){
            anno_overloads = {};
            for(<key, annoId(), tp1> <- overloads, aanno(_, onType, tp2) := tp1, subtype(receiverType, onType)){
               anno_overloads += <key, annoId(), tp1>;
           }
            if(isEmpty(anno_overloads)) reportError(current, "Annotation on <fmt(receiverType)> cannot be resolved from <fmt(annoNameType)>");
            return overloadedAType(anno_overloads);
        } else
        if(aanno(_, onType, annoType) := annoNameType){
           return annoNameType;
        } else
            reportError(current, "Invalid annotation type: <fmt(annoNameType)>");
    } else
        reportError(current, "Invalid type: expected node, ADT, or concrete syntax types, found <fmt(receiverType)>");
}



list[QualifiedName] getReceiver((Assignable) `<QualifiedName name>`, TBuilder tb){
    tb.use(name,{variableId()});
    return [name];
}
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <Expression subscript> ]`, TBuilder tb) = getReceiver(receiver, tb);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, TBuilder tb) =  getReceiver(receiver, tb);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, TBuilder tb) =  getReceiver(receiver, tb);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> . <Name field>`, TBuilder tb) = getReceiver(receiver, tb);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> @ <Name n>`, TBuilder tb) = getReceiver(receiver, tb);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, TBuilder tb) =  getReceiver(receiver, tb);
list[QualifiedName] getReceiver((Assignable) `\< <{Assignable ","}+ elements> \>`, TBuilder tb) = [*getReceiver(element, tb) | Assignable element <- elements];

default list[QualifiedName] getReceiver(Assignable asg, TBuilder tb) { throw rascalCheckerInternalError(getLoc(asg), "Unsupported assignable <asg>"); }

// ---- return, defined in Declarations, close to function declarations

// ---- throw

void collect(current:(Statement) `throw <Statement statement>`, TBuilder tb){
    //tb.fact(current, avoid());
    tb.calculate("throw", current, [statement], AType() { return abool(); });
    collect(statement, tb);
}

// ---- function declaration, see Declaration

// ---- local variable declaration

void collect(current: (Statement) `<Type tp> <{Variable ","}+ variables>;`, TBuilder tb){
    declaredType = convertType(tp, tb);
    declaredTypeParams = collectAndUnlabelRascalTypeParams(declaredType);
    scope = tb.getScope();
    AType tau = declaredType;
    if(isEmpty(declaredTypeParams)){
       tb.calculate("variable declaration", current, [], AType(){ return expandUserTypes(declaredType, scope); });
    } else {
       if(size([v | v <- variables]) > 1){
          tb.reportError(current, "Parameterized declared type not allowed with multiple initializations");
       }
       tau = tb.newTypeVar(tp);
    }
    
    for(v <- variables){
        if(v is initialized){
            if(isEmpty(declaredTypeParams)){ 
               tb.define(unescape("<v.name>"), variableId(), v, defType([], AType() { return expandUserTypes(tau, scope); })); 
               tb.calculate("declaration of variable `<v.name>`", v, [v.initial],   
                   AType (){ 
                       initialType = getType(v.initial); 
                       initialTypeParams = collectAndUnlabelRascalTypeParams(initialType);
                       declaredType = expandUserTypes(declaredType, scope);
                       if(!isEmpty(initialTypeParams)){
                          try {
                            Bindings bindings = matchRascalTypeParams(initialType, declaredType, (), bindIdenticalVars=true);
                            initialType = instantiateRascalTypeParams(initialType, bindings);
                          } catch invalidMatch(str reason): {
                                reportError(v, reason);
                          } catch invalidInstantiation(str msg): {
                                reportError(v, msg);
                          }
                       }
                       comparable(initialType, declaredType) || reportError(v, "Incompatible type <fmt(initialType)> in initialization of <fmt("<v.name>")>, expected <fmt(declaredType)>");
                       return declaredType;                  
                   });
            } else {
               tb.define(unescape("<v.name>"), variableId(), v, defType(tau)); 
               tb.calculate("declaration of variable `<v.name>`, declared with parametrized type", v.name, [v.initial],
                   AType () { 
                       initialType = getType(v.initial); 
                       initialTypeParams = collectAndUnlabelRascalTypeParams(initialType);
                       try {
                         declaredType = expandUserTypes(declaredType, scope);
                         Bindings bindings = matchRascalTypeParams(declaredType, initialType, (), bindIdenticalVars=true);
                         if(!isEmpty(bindings)){
                            declaredType = instantiateRascalTypeParams(declaredType, bindings);
                            initialType = instantiateRascalTypeParams(initialType, bindings);
                          }
                       } catch invalidMatch(str reason): {
                            reportError(v, reason);
                        } catch invalidInstantiation(str msg): {
                            reportError(v, msg);
                       }
                       unify(tau, declaredType);   // bind tau to instantiated declaredType
                       comparable(initialType, declaredType) || reportError(v, "Incompatible type in initialization of <fmt("<v.name>")>, expected <fmt(initialType)>");
                       return declaredType;
                   }); 
            } 
        } else {
          tb.define(unescape("<v.name>"), variableId(), v, defType([], AType() { return expandUserTypes(tau, scope); }));
        }
    }
    collect(variables, tb);
}
