module lang::rascalcore::check::Statement

extend analysis::typepal::TypePal;
extend lang::rascalcore::check::AType;

import lang::rascal::\syntax::Rascal;
extend lang::rascalcore::check::ConvertType;

import lang::rascalcore::check::Scope;
import lang::rascalcore::check::ATypeExceptions;

// Rascal statements

// ---- assert

void collect(current: (Statement) `assert <Expression expression>;`, TBuilder tb){
    tb.fact(current, abool());
    tb.require("assert statement", current, [expression],
        () { subtype(getType(expression), abool(), onError(expression, "Assertion should be `bool`, found <fmt(expression)>")); });
    collectParts(current, tb);
} 

void collect(current: (Statement) `assert <Expression expression> : <Expression message> ;`, TBuilder tb){
   tb.fact(current, abool());
   tb.require("assert statement with message", current, [expression, message],
       () { subtype(getType(expression), abool(), onError(expression, "Assertion should be `bool`, found <fmt(expression)>"));
            subtype(getType(message), astr(), onError(message, "Assertion message should be `str`, found <fmt(message)>"));
       });
   collectParts(current, tb);
} 
     
// ---- expression
void collect(current: (Statement) `<Expression expression>;`, TBuilder tb){
    tb.calculate("expression as statement", current, [expression], AType(){ return getType(expression); });
    collectParts(current, tb);
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
        collectParts(current, tb);
    tb.leaveScope(current);
}

data replacementInfo = replacementInfo(Pattern pattern);

void collect(current: (PatternWithAction) `<Pattern pattern> =\> <Replacement replacement>`,  TBuilder tb){
    visitOrSwitchScopes = tb.getScopeInfo(visitOrSwitchScope());
    for(<scope, scopeInfo> <- visitOrSwitchScopes){
        if(visitOrSwitchInfo(Expression expression, bool isVisit) := scopeInfo){
            if(isVisit){
                tb.enterScope(current);
                    scope = tb.getScope();
                    tb.setScopeInfo(scope, replacementScope(), replacementInfo(pattern));
                    tb.require("pattern replacement", current, [pattern, replacement.replacementExpression],
                       (){ subtype(getType(replacement.replacementExpression), getType(pattern), onError(current, "A pattern of type <fmt(pattern)> cannot be replaced by <fmt(replacement.replacementExpression)>")); });
                    
                    if(replacement is conditional){
                       conditions = [c | Expression c <- replacement.conditions];
                       storeAllowUseBeforeDef(current, replacement.replacementExpression, tb);
                       tb.requireEager("when conditions in replacement", replacement.conditions, conditions,
                          (){ for(cond <- conditions){
                                  if(isFullyInstantiated(getType(cond))){
                                     subtype(getType(cond), abool(), onError(cond, "Condition should be `bool`, found <fmt(cond)>"));
                                  } else {
                                    if(!unify(getType(cond), abool())){
                                       subtype(getType(cond), abool(), onError(cond, "Condition should be `bool`, found <fmt(cond)>"));
                                    }
                                 }
                               }
                            });
                    }
              
                    collectParts(current, tb);
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
                    collectParts(current, tb);
              tb.leaveScope(current);
              return;
           } else {
              tb.enterScope(current);
                    // TODO: this is a very reasonable requirements not checked by the exiting type checker,
                    // but a breaks quite some code.
                    //tb.require("pattern with action", current, [expression, pattern],
                    //    (){ subtype(getType(pattern), getType(expression), onError(pattern, "Pattern should be subtype of <fmt(getType(expression))>, found <fmt(getType(pattern))>"));
                    //      });
                    collectParts(current, tb);
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
         tb.requireEager("insert expression", expr, [expr, pat], 
             () { 
                  if(isFullyInstantiated(getType(expr)) && isFullyInstantiated(getType(pat))){
                     subtype(getType(expr), getType(pat), onError(expr, "Insert type should be subtype of <fmt(pat)>, found <fmt(expr)>"));
                  } else {
                  if(!unify(getType(expr), patType)){
                     subtype(getType(expr), patType, onError(expr, "Insert type should be subtype of <fmt(patType)>, found <fmt(expr)>"));
                  }
                }
             });
          collectParts(current, tb);
          return;
      } else {
        throw "Inconsistent info from replacement scope: <info>";
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
        collectParts(current, tb);
        computeLoopType("while statement", loopName, current, tb);
    tb.leaveScope(conditions);
}
 
//void checkConditions(str text, Tree current, list[Expression] condList, TBUilder tb){
//    switch(size(condList)){
//    case 1:
//        tb.requireEager(text, current, condList,
//        () { tcond = getType(condList[0]);
//             if(!unify(abool(), tcond)) reportError(cond, "Condition should be `bool`, found <fmt(tcond)>");
//        });
//    
//    case 2:
//        tb.requireEager(text, current, condList,
//        () { tcond0 = getType(condList[0]);
//             if(!unify(abool(), tcond0)) reportError(cond, "Condition should be `bool`, found <fmt(tcond0)>");
//             tcond1 = getType(condList[1]);
//             if(!unify(abool(), tcond1)) reportError(cond, "Condition should be `bool`, found <fmt(tcond1)>");
//        });
//    default:
//        tb.requireEager(text, current, condList,
//        () {  for(Expression cond <- condList){
//                  tcond = getType(cond);
//                  if(!unify(abool(), tcond)) reportError(cond, "Condition should be `bool`, found <fmt(tcond)>");
//              }
//        });
//    }
// }
 
//void () makeCheckConditions(list[Expression] condList)
//    = () { for(Expression cond <- condList){
//               tcond = getType(cond);
//               if(!unify(abool(), tcond)) reportError(cond, "Condition should be `bool`, found <fmt(tcond)>");
//           }
//         };

void checkConditions(list[Expression] condList){
    for(Expression cond <- condList){
        tcond = getType(cond);
        //if(!unify(abool(), tcond)) reportError(cond, "Condition should be `bool`, found <fmt(tcond)>");
        if(isFullyInstantiated(tcond)){
            subtype(tcond, abool(), onError(cond, "Condition should be `bool`, found <fmt(cond)>"));
        } else {
            if(!unify(tcond, abool())){
                subtype(tcond, abool(), onError(cond, "Condition should be `bool`, found <fmt(cond)>"));
            }
        }
    }
}

 //tb.fact(current, abool());
 //  
 //   tb.requireEager("and", current, [lhs, rhs],
 //       (){ if(!unify(abool(), getType(lhs))) reportError(lhs, "Argument of && should be `bool`, found <fmt(lhs)>");
 //           if(!unify(abool(), getType(rhs))) reportError(rhs, "Argument of && should be `bool`, found <fmt(rhs)>");
 //         });
 //   collectParts(current, tb);

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
             throw "Inconsistent info from loop scope: <scopeInfo>";
           }
        }
    }
    
    throw "Info for loop scope not found"; 
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
        collectParts(current, tb);
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
       
        collectParts(current, tb);
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
                collectParts(current, tb);
                return;
             }
        } else {
            throw "Inconsistent info from loop scope: <scopeInfo>";
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
                collectParts(current, tb);
                return;
             }
        } else {
            throw "Inconsistent info from loop scope: <scopeInfo>";
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
            throw "Inconsistent info from loop scope: <scopeInfo>";
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
        
        tb.requireEager("if then", current, condList, (){ checkConditions(condList); });
        //checkConditions("if then", current, condList, tb);
        collectParts(current, tb);
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
        
        tb.calculateEager("if then else", current, condList + [thenPart, elsePart],
            AType (){
                checkConditions(condList);
                return lub(getType(thenPart), getType(elsePart));
            });
        collectParts(current, tb);
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
        collectParts(current, tb);
    tb.leaveScope(current);
}

data SwitchInfo = switchInfo(Expression e);

// ---- fail
void collect(current: (Statement)`fail <Target target>;`, TBuilder tb){
    loopName = "";
    if(target is labeled){
        loopName = "<target.name>";
        tb.use(target.name, {labelId()});
    }
    tb.fact(current, avoid());
    collectParts(current, tb);
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
            tb.use_qual([qname.qualifier, qname.name], name, {variableId()}, {moduleId()} );
        } else {
            tb.use(v, {variableId()});
        }
    }
    collectParts(current, tb);
}

void collect(Bound current, TBuilder tb){
    if(current is \default){
        tb.calculate("bound", current, [current.expression],
            AType(){ if(subtype(getType(expression), aint())) return aint();
                     reportError(current.expression, "Bound should have type `int`, found `<fmt(getType(current.expression))>"); 
                   });
    } else {
        tb.fact(current, avoid());
    }
    collectParts(current, tb);
}

// ---- try, try finally, catch

// ---- try
 
 void collect(current: (Statement) `try <Statement body> <Catch+ handlers>`, TBuilder tb){
    tb.fact(current, avoid());
    collectParts(current, tb);
 }
 
// ---- try finally

void collect(current: (Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`, TBuilder tb){
    tb.fact(current, avoid());
    collectParts(current, tb);
}

// ---- catch

void collect(current: (Catch) `catch: <Statement body>`, TBuilder tb){
    tb.fact(current, avoid());
    collectParts(current, tb);
}

void collect(current: (Catch) `catch <Pattern pattern>: <Statement body>`, TBuilder tb){
    tb.fact(current, avoid());
    tb.enterScope(current);
        collectParts(current, tb);
    tb.leaveScope(current);
}

// ---- non-empty block

void collect(current: (Statement) `<Label label> { <Statement+ statements> }`, TBuilder tb){
    if(label is \default){
       tb.define(unescape("<label.name>"), labelId(), label.name, noDefInfo());
    }
    stats = [ s | Statement s <- statements ];
    tb.calculate("non-empty block statement", current, [stats[-1]],  AType() { return getType(stats[-1]); } );
    collectParts(current, tb);
}

// ---- empty block

void collect(current: (Statement) `;`, TBuilder tb){
    tb.fact(current, avoid());
}

// ---- assignment

void collect(current: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`, TBuilder tb){
    checkAssignment(current, assignable, "<operator>", statement, tb);
    collectParts(current, tb);
}

void checkAssignment(Statement current, (Assignable) `( <Assignable arg> )`, str operator, Statement statement, TBuilder tb){
    checkAssignment(current, arg, operator, statement, tb);
}

AType computeAssignmentRhsType(Statement current, AType lhsType, "=", AType rhsType)
    = rhsType;
    
AType computeAssignmentRhsType(Statement current, AType lhsType, "+=", AType rhsType)
    = computeAdditionType(current, lhsType, rhsType);

AType computeAssignmentRhsType(Statement current, AType lhsType, "-=", AType rhsType)
    = computeSubtractionType(current, lhsType, rhsType);    

AType computeAssignmentRhsType(Statement current, AType lhsType, "*=", AType rhsType)
    = computeProductType(current, lhsType, rhsType);       

AType computeAssignmentRhsType(Statement current, AType lhsType, "/=", AType rhsType)
    = computeDivisionType(current, lhsType, rhsType);    

AType computeAssignmentRhsType(Statement current, AType lhsType, "&=", AType rhsType)
    = computeIntersectionType(current, lhsType, rhsType);  
    
AType computeAssignmentRhsType(Statement current, AType lhsType, "?=", AType rhsType)
    = lub(lhsType, rhsType);  
   
// <<=

default AType computeAssignmentRhsType(Statement current, AType lhsType, str operator, AType rhsType){
    throw "<operator> not supported";
}

void checkAssignment(Statement current, (Assignable) `<QualifiedName name>`, str operator,  Statement statement, TBuilder tb){
    qname = convertName(name);
    if(isQualified(qname)){
        tb.use_qual([qname.qualifier, qname.name], name, {variableId()}, {moduleId()});
    } else {
        if(operator == "="){
           tb.calculate("name of assignable", name, [statement], AType(){ return getType(statement); });
           tb.define(unescape("<name>"), variableId(), name, defLub([statement], AType(){ 
            return  getType(statement); }));
        } else {
           tb.useLub(name, {variableId()});
        }
    }
    tb.calculate("assignment to `<name>`", current, [statement],
        AType () { 
            asgType = computeAssignmentRhsType(current, getType(name), operator, getType(statement));
                   if(operator == "=") subtype(getType(statement), asgType, onError(current, "Incompatible type <fmt(asgType)> in assignment to variable `<name>`, found <fmt(statement)>")); 
                   return asgType;   
                 });  
}

AType computeReceiverType(Statement current, (Assignable) `<QualifiedName name>`, Key scope)
    = getType(name);

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, Key scope)
    = computeSubscriptionType(current, computeReceiverType(current, receiver, scope), [getType(subscript)]);
    
AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, Key scope){
    return computeSliceType(computeReceiverType(current, receiver, scope), getType(optFirst), aint(), getType(optLast));
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, Key scope){
    return computeSliceType(computeReceiverType(current, receiver, scope), getType(optFirst), getType(second), getType(optLast));
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> . <Name field>`, Key scope)
    = computeFieldType(current, computeReceiverType(current, receiver, scope), "<field>", scope);

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, Key scope){
    return computeReceiverType(current, receiver);
}

AType computeReceiverType(Statement current, (Assignable) `\< <{Assignable ","}+ elements> \>`, Key scope){
    return atuple(atypeList([computeReceiverType(current, element, scope) | element <- elements]));
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, str operator, Statement rhs, TBuilder tb){
   names = getReceiver(receiver, tb);
   
   tau = tb.newTypeVar();
   tb.define(unescape("<names[0]>"), variableId(), names[0], defLub([], AType(){ return getType(tau); }));
   scope = tb.getScope();
   
   tb.calculate("assignable with subscript", current, [subscript, rhs], 
       AType (){ 
           res = computeSubscriptAssignableType(current, computeReceiverType(current, receiver, scope),  subscript, operator, getType(rhs));
           unify(tau, res, onError(current, "Cannot bind type variable for <fmt("<names[0]>")>"));
           return res;
         });
}

AType computeSubscriptAssignableType(Statement current, AType receiverType, Expression subscript, str operator, AType rhs){
    subscriptType = getType(subscript);
    if (isListType(receiverType)) { 
        if (!isIntType(subscriptType)) reportError(current, "Expected subscript of type `int`, not <fmt(subscriptType)>");
        return makeListType(computeAssignmentRhsType(current, getListElementType(receiverType), operator, rhs));
    } else if (isNodeType(receiverType)) {
        if (!isIntType(subscriptType)) reportError(current, "Expected subscript of type `int`, not <fmt(subscriptType)>");
        computeAssignmentRhsType(current, avalue(), operator, rhs);
        return anode();
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
        return arel([relFields[0],computeAssignmentRhsType(current, relFields[1], operator, rhs)]);
    } else {
        throw "Cannot assign value of type <fmt(rhs)> to assignable of type <fmt(receiverType)>";
    }
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, str operator, Statement rhs, TBuilder tb){
   names = getReceiver(receiver, tb);
   if(optFirst is noExpression) tb.fact(optFirst, aint());
   if(optLast is noExpression) tb.fact(optLast, aint());
   
   tau = tb.newTypeVar();
   tb.define(unescape("<names[0]>"), variableId(), names[0], defLub([], AType(){ return getType(tau); }));
   scope = tb.getScope();
   
   tb.calculate("assignable with slice", current, [optFirst, optLast, rhs], 
      AType (){ 
           res = computeSliceAssignableType(current, computeReceiverType(current, receiver, scope),  getType(optFirst), aint(), getType(optLast), operator, getType(rhs));
           unify(tau, res, onError(current, "Cannot bind type variable for <fmt("<names[0]>")>"));
           return res;
         });
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, str operator, Statement rhs, TBuilder tb){
   names = getReceiver(receiver, tb);
   if(optFirst is noExpression) tb.fact(optFirst, aint());
   if(optLast is noExpression) tb.fact(optLast, aint());
   tau = tb.newTypeVar();
   tb.define(unescape("<names[0]>"), variableId(), names[0], defLub([], AType(){ return getType(tau); }));
   scope = tb.getScope();
   
   tb.calculate("assignable with slice", current, [optFirst, second, optLast, rhs], 
      AType (){ 
           res = computeSliceAssignableType(current, computeReceiverType(current, receiver, scope),  getType(optFirst), getType(second), getType(optLast), operator, getType(rhs));
           unify(tau, res, onError(current, "Cannot bind type variable for <fmt("<names[0]>")>"));
           return res;
         });
}

AType computeSliceAssignableType(Statement current, AType receiverType, AType first, AType step, AType last, str operator, AType rhs){
    failures = {};
    if(!isIntType(first)) failures += error(current, "The first slice index must be of type `int`, found <fmt(first)>");
    if(!isIntType(step)) failures  += error(current, "The slice step must be of type `int`, found <fmt(step)>");
    if(!isIntType(last)) failures  += error(current, "The last slice index must be of type `int`, found <fmt(last)>");
    
    if(!isEmpty(failures)) throw reportErrors(failures);
    if (isListType(receiverType)){
        return makeListType(computeAssignmentRhsType(current, getListElementType(receiverType), operator, rhs));
    } else if(isStrType(receiverType)){ 
        if(!subtype(rhs, astr())) reportError(current, "Expected `str` in slice assignment, found <fmt(rhs)>");
        return receiverType;
    } else if(isNonTerminalIterType(receiverType)) {
        throw "Not yet implemented"; // TODO
    } else if (isNodeType(receiverType)) {
        return makeListType(avalue());
    }
    throw "Cannot assign value of type <fmt(rhs)> to assignable of type <fmt(receiverType)>";
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> . <Name field>`, str operator, Statement rhs, TBuilder tb){
   names = getReceiver(receiver, tb);
   tau = tb.newTypeVar();
   tb.define(unescape("<names[0]>"), variableId(), names[0], defLub([], AType(){ return getType(tau); }));
   scope = tb.getScope();
   
   tb.calculate("assignable with field", current, [rhs], 
      AType (){ 
           res = computeFieldAssignableType(current, computeReceiverType(current, receiver, scope),  "<field>", operator, getType(rhs), scope);
           unify(tau, res, onError(current, "Cannot bind type variable for <fmt("<names[0]>")>"));
           return res;
         });
}

AType computeFieldAssignableType(Statement current, AType receiverType, str fieldName, str operator, AType rhs, Key scope){
   if (aadt(adtName, list[AType] actualTypeParams) := receiverType){
        try {
            if (getADTName(receiverType) == "Tree" && fieldName == "top") {
                return receiverType;
            }
            fieldType = expandUserTypes(getType(fieldName, scope, {fieldId()}), scope);
            declaredInfo = getDefinitions(adtName, scope, {dataId(), nonterminalId()});
            declaredType = getType(adtName, scope, {dataId(), nonterminalId()});
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
            
            fieldType = filterFieldType(fieldType, declaredInfo, scope); 
            
            updatedFieldType = computeAssignmentRhsType(current, fieldType, operator, rhs);
            if(!subtype(updatedFieldType, fieldType)) reportError(current, "Field <fmt(fieldName)> requires <fmt(fieldType)>, found <fmt(updatedFieldType)>");     
            
            for(def <- declaredInfo){
               if(fieldName in domain(def.defInfo.constructorFields)){
                    return receiverType;
               }
            }                           
            
            reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(receiverType)>");
        } catch TypeUnavailable(): {
            reportError(current, "Cannot compute type of field <fmt(fieldName)>, user type <fmt(receiverType)> has not been declared or is out of scope"); 
        }
    } else if (isTupleType(receiverType)) {
        tupleFields = getTupleFields(receiverType);
        idx = indexOf(getTupleFieldNames(receiverType), fieldName);
        if(idx >= 0){
            updatedFieldType = computeAssignmentRhsType(current, tupleFields[idx], operator, rhs)[label=fieldName];
            if(!subtype(updatedFieldType, tupleFields[idx])) reportError(current, "Field <fmt(fieldName)> requires <fmt(tupleFields[idx])>, found <fmt(updatedFieldType)>");
            tupleFields[idx] = updatedFieldType;
            return atuple(atypeList(tupleFields));
        } else
            reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(receiverType)>");
    } else if (isNodeType(receiverType)) {
        computeAssignmentRhsType(current, avalue(), operator, rhs);
        return anode();
    } else if(isLocType(receiverType) || isDateTimeType(receiverType) || isReifiedType(receiverType) || isRelType(receiverType) || isListRelType(receiverType) || isMapType(receiverType)){
        reportError(current, "Cannot assign to any field of <fmt(receiverType)>");
    } 
    throw "Cannot assign value of type <fmt(rhs)> to assignable of type <fmt(receiverType)>";
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, str operator, Statement rhs, TBuilder tb){

    names = getReceiver(receiver, tb);
   tau = tb.newTypeVar();
   tb.define(unescape("<names[0]>"), variableId(), names[0], defLub([], AType(){ return getType(tau); }));
   scope = tb.getScope();
   
   tb.calculate("assignable with default expression", current, [defaultExpression, rhs], 
      AType (){ 
           res = computeDefaultAssignableType(current, computeReceiverType(current, receiver, scope), getType(defaultExpression), operator, getType(rhs), scope);
           unify(tau, res, onError(current, "Cannot bind type variable for <fmt("<names[0]>")>"));
           return res;
         });
}

AType computeDefaultAssignableType(Statement current, AType receiverType, AType defaultType, str operator, AType rhs, Key scope){
    finalReceiverType = computeAssignmentRhsType(current, receiverType, operator, rhs);
    finalDefaultType = computeAssignmentRhsType(current, defaultType, operator, rhs);
    if(!comparable(finalReceiverType, finalDefaultType)) reportError(current, "Receiver and default expression lead to incomparable types: <fmt(finalReceiverType)> versus <fmt(finalDefaultType)>");
    return receiverType;
}

AType() makeDef(list[AType] taus, int i) = AType() { return taus[i]; };

set[str] getNames(Statement s) = {"<nm>" | /QualifiedName nm := s};

void checkAssignment(Statement current, receiver: (Assignable) `\< <{Assignable ","}+ elements> \>`, str operator, Statement rhs, TBuilder tb){
   names = getReceiver(receiver, tb);
   flatNames = ["<nm>" | nm <- names];
   namesInRhs = getNames(rhs);
   taus = [tb.newTypeVar() | nm <- names];
   for(int i <- index(names), flatNames[i] notin namesInRhs){tb.define(unescape("<names[i]>"), variableId(), names[i], defLub([rhs], makeDef(taus, i)));}
  
   scope = tb.getScope();
   
   tb.calculate("assignable with tuple", current, [rhs], 
       AType (){ 
           recType  = computeReceiverType(current, receiver, scope);
           rhsType  = computeAssignmentRhsType(current, recType, operator, getType(rhs));
           if(!isTupleType(rhsType)) reportError(current, "Tuple type required, found <fmt(rhsType)>");
           rhsFields = getTupleFields(rhsType);
           if(size(names) != size(rhsFields)) reportError(statement, "Tuple type required of arity <size(names)>, found arity <size(rhsFields)>");
           for(int i <- index(names)){
               if(isFullyInstantiated(getType(names[i]))){
                  subtype(rhsFields[i], getType(names[i]), onError(names[i], "Value of type <fmt(rhsFields[i])> cannot be assigned to <fmt("<names[i]>")> of type <fmt(getType(names[i]))>"));
                  if(flatNames[i] in namesInRhs){
                    taus[i] = getType(names[i]);
                  }
               } else {
                 if(flatNames[i] in namesInRhs){
                    unify(taus[i], typeof(names[i]), onError(current, "Cannot bind variable <fmt("<names[i]>")>"));
                 } else 
                    unify(taus[i], rhsFields[i], onError(current, "Cannot bind variable <fmt("<names[i]>")>"));
               }
            }
           return atuple(atypeList(taus));
         });
}

list[QualifiedName] getReceiver((Assignable) `<QualifiedName name>`, TBuilder tb){
    tb.use(name,{variableId()});
    return [name];
}
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <Expression subscript> ]`, TBuilder tb) = getReceiver(receiver, tb);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, TBuilder tb) =  getReceiver(receiver, tb);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, TBuilder tb) =  getReceiver(receiver, tb);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> . <Name field>`, TBuilder tb) = getReceiver(receiver, tb);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, TBuilder tb) =  getReceiver(receiver, tb);
list[QualifiedName] getReceiver((Assignable) `\< <{Assignable ","}+ elements> \>`, TBuilder tb) = [*getReceiver(element, tb) | Assignable element <- elements];

default list[QualifiedName] getReceiver(Assignable asg, TBuilder tb) { throw "Unsupported assignable <asg>"; }

// ---- return, defined in Declarations, close to function declarations

// ---- throw

void collect(current:(Statement) `throw <Statement statement>`, TBuilder tb){
    tb.fact(current, avoid());
    collectParts(current, tb);
}

// ---- function declaration, see Declaration

// ---- local variable declaration

void collect(current: (Statement) `<Type tp> <{Variable ","}+ variables>;`, TBuilder tb){
    declaredType = convertType(tp, tb);
    declaredTypeParams = collectRascalTypeParams(declaredType);
    scope = tb.getScope();
    AType tau = declaredType;
    if(isEmpty(declaredTypeParams)){
       tb.calculate("variable declaration", current, [], AType(){ return expandUserTypes(declaredType, scope); });
    } else {
       if(size([v | v <- variables]) > 1){
          tb.reportError(current, "Parameterized declared type not allowed with multiple initializations");
       }
       tau = tb.newTypeVar();
    }
    
    for(v <- variables){
        if(v is initialized){
            if(isEmpty(declaredTypeParams)){ 
               tb.define(unescape("<v.name>"), variableId(), v, defType([], AType() { return expandUserTypes(tau, scope); })); 
               tb.calculate("declaration of variable `<v.name>`", v, [v.initial],   
                   AType (){ 
                       initialType = getType(v.initial); 
                       initialTypeParams = collectRascalTypeParams(initialType);
                       declaredType = expandUserTypes(declaredType, scope);
                       if(!isEmpty(initialTypeParams)){
                          try {
                            Bindings bindings = matchRascalTypeParams(declaredType, initialType, (), bindIdenticalVars=true);
                            initialType = instantiateRascalTypeParams(initialType, bindings);
                          } catch invalidMatch(str reason): {
                                reportError(v, reason);
                          } catch invalidInstantiation(str msg): {
                                reportError(v, msg);
                          }
                       }
                       subtype(initialType, declaredType, onError(v, "Incompatible type <fmt(initialType)> in initialization of <fmt("<v.name>")>, expected <fmt(declaredType)>"));
                       return declaredType;                  
                   });
            } else {
               tb.define(unescape("<v.name>"), variableId(), v, defType(tau)); 
               tb.calculate("declaration of variable `<v.name>`, declared with parametrized type", v.name, [v.initial],
                   AType () { 
                       initialType = getType(v.initial); 
                       initialTypeParams = collectRascalTypeParams(initialType);
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
                       subtype(initialType, declaredType, onError(v, "Incompatible type in initialization of <fmt("<v.name>")>, expected <fmt(initialType)>"));
                       return declaredType;
                   }); 
            } 
        } else {
          tb.define(unescape("<v.name>"), variableId(), v, defType([], AType() { return expandUserTypes(tau, scope); }));
        }
    }
    collectParts(current, tb);
}
