module lang::rascal::check::Statement

extend analysis::typepal::TypePal;
extend lang::rascal::check::AType;

import lang::rascal::\syntax::Rascal;
extend lang::rascal::check::ConvertType;

import lang::rascal::check::Scope;
import lang::rascal::check::ATypeExceptions;

// Rascal statements

// ---- assert

void collect(current: (Statement) `assert <Expression expression>;`, FRBuilder frb){
    frb.fact(current, abool());
    frb.require("assert statement", current, [expression],
        () { subtype(typeof(expression), abool(), onError(expression, "Assertion should be `bool`, found <fmt(expression)>")); });
    collectParts(current, frb);
} 

void collect(current: (Statement) `assert <Expression expression> : <Expression message> ;`, FRBuilder frb){
   frb.fact(current, abool());
   frb.require("assert statement with message", current, [expression, message],
       () { subtype(typeof(expression), abool(), onError(expression, "Assertion should be `bool`, found <fmt(expression)>"));
            subtype(typeof(message), astr(), onError(message, "Assertion message should be `str`, found <fmt(message)>"));
       });
   collectParts(current, frb);
} 
     
// ---- expression
void collect(current: (Statement) `<Expression expression>;`, FRBuilder frb){
    frb.calculate("expression as statement", current, [expression], AType(){ return typeof(expression); });
    collectParts(current, frb);
}

// ---- visit and insert

void collect(current: (Statement) `<Label label> <Visit vst>`, FRBuilder frb){
    frb.enterScope(current);
        if(label is \default){
            frb.define("<label.name>", labelId(), label.name, noDefInfo());
        }
        frb.calculate("visit statement", vst, [vst.subject], AType(){ return typeof(vst.subject); });
        collectParts(current, frb);
    frb.leaveScope(current);
}

data replacementInfo = replacementInfo(Pattern pattern);

void collect(current: (PatternWithAction) `<Pattern pattern> =\> <Replacement replacement>`,  FRBuilder frb){
    frb.enterScope(current);
        scope = frb.getScope();
        frb.setScopeInfo(scope, replacementScope(), replacementInfo(pattern));
        frb.require("pattern replacement", current, [pattern, replacement],
            (){ subtype(typeof(replacement), typeof(pattern), onError(current, "A pattern of type <fmt(pattern)> cannot be replaced by <fmt(replacement)>")); });
        collectParts(current, frb);
    frb.leaveScope(current);
}

void collect(current: (PatternWithAction) `<Pattern pattern>: <Statement statement>`,  FRBuilder frb){
    frb.enterScope(current);
        scope = frb.getScope();
        frb.setScopeInfo(scope, replacementScope(), replacementInfo(pattern));
        collectParts(current, frb);
    frb.leaveScope(current);
}

void collect(current: (Statement) `insert <Expression expr>;`, FRBuilder frb){
    replacementScopes = frb.getScopeInfo(replacementScope());
    for(<scope, scopeInfo> <- replacementScopes){
      if(replacementInfo(Pattern pat) := scopeInfo){
         frb.requireEager("insert expression", expr, [expr, pat], 
             () { 
                  if(isFullyInstantiated(typeof(expr)) && isFullyInstantiated(typeof(pat))){
                     subtype(typeof(expr), typeof(pat), onError(expr, "Insert type should be subtype of <fmt(pat)>, found <fmt(expr)>"));
                  } else {
                  if(!unify(typeof(expr), patType)){
                     subtype(typeof(expr), patType, onError(expr, "Insert type should be subtype of <fmt(patType)>, found <fmt(expr)>"));
                  }
                }
             });
          collectParts(current, frb);
          return;
      } else {
        throw "Inconsistent info from replacement scope: <info>";
      }
    }
    frb.reportError(current, "Insert found outside replacement context");
}

// loop statements, append, break and continue

data LoopInfo = loopInfo(str name, list[Tree] appends);

// --- while

void collect(current: (Statement) `<Label label> while( <{Expression ","}+ conditions> ) <Statement body>`,  FRBuilder frb){
    frb.enterScope(conditions);   // body may refer to variables defined in conditions
        loopName = "";
        if(label is \default){
            loopName = "<label.name>";
            frb.define(loopName, labelId(), label.name, noDefInfo());
        }
        frb.setScopeInfo(frb.getScope(), loopScope(), loopInfo(loopName, [])); // appends in body
        condList = [cond | Expression cond <- conditions];
        
        frb.requireEager("while statement", current, condList + [body], (){ checkConditions(condList); });
        collectParts(current, frb);
        computeLoopType("while statement", loopName, current, frb);
    frb.leaveScope(conditions);
}

void checkConditions(list[Expression] condList){
    for(Expression cond <- condList){
        if(isFullyInstantiated(typeof(cond))){
            subtype(typeof(cond), abool(), onError(cond, "Condition should be `bool`, found <fmt(cond)>"));
        } else {
            if(!unify(typeof(cond), abool())){
                subtype(typeof(cond), abool(), onError(cond, "Condition should be `bool`, found <fmt(cond)>"));
            }
        }
    }
}

void computeLoopType(str loopKind, str loopName1, Statement current, FRBuilder frb){
    loopScopes = frb.getScopeInfo(loopScope());
    
    for(<scope, scopeInfo> <- loopScopes){
        if(loopInfo(loopName2, list[Statement] appends) := scopeInfo){
           if(loopName1 == "" || loopName1 == loopName2){
              if(isEmpty(appends)){
                 frb.fact(current, alist(avoid()));
              } else {
                 frb.calculate(loopKind, current, appends, AType(){ return alist(lub([typeof(app) | app <- appends])); });
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

void collect(current: (Statement) `<Label label> do <Statement body> while ( <Expression condition> ) ;`, FRBuilder frb){
    frb.enterScope(current);   // condition may refer to variables defined in body
        loopName = "";
        if(label is \default){
            loopName = "<label.name>";
            frb.define(loopName, labelId(), label.name, noDefInfo());
        }
        frb.setScopeInfo(frb.getScope(), loopScope(), loopInfo(loopName, [])); // appends in body
        frb.requireEager("do statement", current, [body, condition], (){ checkConditions([condition]); });
        collectParts(current, frb);
        computeLoopType("do statement", loopName, current, frb);
    frb.leaveScope(current); 
}

//---- for

void collect(current: (Statement) `<Label label> for( <{Expression ","}+ conditions> ) <Statement body>`,  FRBuilder frb){
    frb.enterScope(current);   // body may refer to variables defined in conditions
        loopName = "";
        if(label is \default){
            loopName = "<label.name>";
            frb.define(loopName, labelId(), label.name, noDefInfo());
        }
        frb.setScopeInfo(frb.getScope(), loopScope(), loopInfo(loopName, [])); // appends in body
        condList = [cond | Expression cond <- conditions];
        
        frb.requireEager("for statement", current, condList + [body], (){ checkConditions(condList); });
       
        collectParts(current, frb);
        computeLoopType("for statement", loopName, current, frb);
       
    frb.leaveScope(current);  
}

// ---- append

void collect(current: (Statement) `append <DataTarget dataTarget> <Statement statement>`, FRBuilder frb){
    loopName = "";
    if(dataTarget is labeled){
        loopName = "<dataTarget.label>";
        frb.use(dataTarget.label, {labelId()});
    }
   
    for(<scope, scopeInfo> <- frb.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] appends) := scopeInfo){
            if(loopName == "" || loopName == loopName1){
                frb.setScopeInfo(scope, loopScope(), loopInfo(loopName1, appends + [current]));
                frb.calculate("append type", current, [statement], AType(){ return typeof(statement); });
                collectParts(current, frb);
                return;
             }
        } else {
            throw "Inconsistent info from loop scope: <scopeInfo>";
        }
    }
    frb.reportError(current, "Append outside a while/do/for statement");
}

// ---- break

void collect(current:(Statement) `break <Target target>;`, FRBuilder frb){
    frb.fact(current, avoid());
    loopName = "";
    if(target is labeled){
        loopName = "<target.name>";
        frb.use(target.name, {labelId()});
    }
 
    for(<scope, scopeInfo> <- frb.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] appends) := scopeInfo){
            if(loopName == "" || loopName == loopName1){
                collectParts(current, frb);
                return;
             }
        } else {
            throw "Inconsistent info from loop scope: <scopeInfo>";
        }
    }
    frb.reportError(current, "Break outside a while/do/for statement");
}

// ---- continue

void collect(current:(Statement) `continue <Target target>;`, FRBuilder frb){
    frb.fact(current, avoid());
    loopName = "";
    if(target is labeled){
        loopName = "<target.name>";
        frb.use(target.name, {labelId()});
    }
    
    for(<scope, scopeInfo> <- frb.getScopeInfo(loopScope())){
        if(loopInfo(loopName1, list[Statement] appends) := scopeInfo){
            if(loopName == "" || loopName == loopName1){
                 collectParts(current, frb);
                 return;
             }
        } else {
            throw "Inconsistent info from loop scope: <scopeInfo>";
        }
    }
    frb.reportError(current, "Continue outside a while/do/for statement");
}

// ---- if

void collect(current: (Statement) `<Label label> if( <{Expression ","}+ conditions> ) <Statement thenPart>`,  FRBuilder frb){
    frb.enterScope(conditions); // thenPart may refer to variables defined in conditions
        if(label is \default){
            frb.define("<label.name>", labelId(), label.name, noDefInfo());
        }
        condList = [cond | Expression cond <- conditions];
        frb.fact(current, avalue());
        
        frb.require("if then", current, condList, (){ checkConditions(condList); });
        collectParts(current, frb);
    frb.leaveScope(conditions);   
}

// --- if then else

void collect(current: (Statement) `<Label label> if( <{Expression ","}+ conditions> ) <Statement thenPart> else <Statement elsePart>`,  FRBuilder frb){
    frb.enterScope(conditions);   // thenPart may refer to variables defined in conditions; elsePart may not
        if(label is \default){
            frb.define("<label.name>", labelId(), label.name, noDefInfo());
        }
        condList = [cond | cond <- conditions];
        storeExcludeUse(conditions, elsePart, frb); // variable occurrences in elsePart may not refer to variables defined in conditions
        
        frb.calculateEager("if then else", current, condList + [thenPart, elsePart],
            AType (){
                checkConditions(condList);
                return lub(typeof(thenPart), typeof(elsePart));
            });
        collectParts(current, frb);
    frb.leaveScope(conditions); 
}

// ---- switch TODO

// ---- fail TODO

// ---- filter TODO

// ---- solve

void collect(current: (Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`, FRBuilder frb){
    for(v <- variables){
        qname = convertName(v);
        if(isQualified(qname)){
            frb.use_qual([qname.qualifier, qname.name], name, {variableId()}, {moduleId()} );
        } else {
            frb.use(qname.name, {variableId()});
        }
    }
    collectParts(current, frb);
}

void collect(Bound current, FRBuilder frb){
    if(current is \default){
        frb.calculate("bound", current, [current.expression],
            AType(){ if(subtype(typeof(expression), aint())) return aint();
                     reportError(current.expression, "Bound should have type `int`, found `<fmt(typeof(current.expression))>"); 
                   });
    } else {
        frb.fact(current, avoid());
    }
    collectParts(current, frb);
}

// ---- try, try finally, catch

// ---- try
 
 void collect(current: (Statement) `try <Statement body> <Catch+ handlers>`, FRBuilder frb){
    frb.fact(current, avoid());
    collectParts(current, frb);
 }
 
// ---- try finally

void collect(current: (Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`, FRBuilder frb){
    frb.fact(current, avoid());
    collectParts(current, frb);
}

// ---- catch

void collect(current: (Catch) `catch: <Statement body>`, FRBuilder frb){
    frb.fact(current, avoid());
    collectParts(current, frb);
}

void collect(current: (Catch) `catch <Pattern pattern>: <Statement body>`, FRBuilder frb){
    frb.fact(current, avoid());
    frb.enterScope(current);
        collectParts(current, frb);
    frb.leaveScope(current);
}

// ---- non-empty block

void collect(current: (Statement) `<Label label> { <Statement+ statements> }`, FRBuilder frb){
    if(label is \default){
       frb.define("<label.name>", labelId(), label.name, noDefInfo());
    }
    stats = [ s | Statement s <- statements ];
    frb.calculate("non-empty block statement", current, [stats[-1]],  AType() { return typeof(stats[-1]); } );
    collectParts(current, frb);
}

// ---- empty block

void collect(current: (Statement) `;`, FRBuilder frb){
    frb.fact(current, avoid());
}

// ---- assignment

void collect(current: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`, FRBuilder frb){
    checkAssignment(current, assignable, "<operator>", statement, frb);
    collectParts(current, frb);
}

void checkAssignment(Statement current, (Assignable) `( <Assignable arg> )`, str operator, Statement statement, FRBuilder frb){
    checkAssignment(current, arg, operator, statement, frb);
}

AType computeOperatorType(Statement current, AType asgType, "=", AType rhsType)
    = rhsType;
    
AType computeOperatorType(Statement current, AType asgType, "+=", AType rhsType)
    = computeAdditionType(current, asgType, rhsType);

AType computeOperatorType(Statement current, AType asgType, "-=", AType rhsType)
    = computeSubtractionType(current, asgType, rhsType);    

AType computeOperatorType(Statement current, AType asgType, "*=", AType rhsType)
    = computeProductType(current, asgType, rhsType);       

AType computeOperatorType(Statement current, AType asgType, "/=", AType rhsType)
    = computeDivisionType(current, asgType, rhsType);    

AType computeOperatorType(Statement current, AType asgType, "&=", AType rhsType)
    = computeIntersectionType(current, asgType, rhsType);  
    
AType computeOperatorType(Statement current, AType asgType, "?=", AType rhsType)
    = lub(asgType, rhsType);  
   
// <<=

default AType computeOperatorType(Statement current, AType asgType, str operator, AType rhsType){
    throw "<operator> not supported";
}

void checkAssignment(Statement current, (Assignable) `<QualifiedName name>`, str operator,  Statement statement, FRBuilder frb){
    //frb.calculate("name assignable", current, [statement], AType(){ return typeof(statement); });
    qname = convertName(name);
    if(isQualified(qname)){
        frb.use_qual([qname.qualifier, qname.name], name, {variableId()}, {moduleId()});
    } else {
        frb.calculate("name of assignable", name, [statement], AType(){ return typeof(statement); });
        frb.define("<name>", variableId(), name, defLub([statement], AType(){ return typeof(statement); }));
    }
    frb.calculate("assignment to`<name>`", current, [statement],
        AType () { asgType = computeOperatorType(current, typeof(name), operator, typeof(statement));
                   subtype(typeof(statement), asgType, onError(current, "Incompatible type <fmt(asgType)> in assignment to variable `<name>`, found <fmt(statement)>")); 
                   return asgType;   
                 });  
}

AType computeReceiverType(Statement current, (Assignable) `<QualifiedName name>`)
    = typeof(name);

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <Expression subscript> ]`)
    = computeSubscriptionType(current, computeReceiverType(current, receiver), [typeof(subscript)]);
    
AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`){
    return computeSliceType(computeReceiverType(current, receiver), typeof(optFirst), aint(), typeof(optLast));
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`){
    return computeSliceType(computeReceiverType(current, receiver), typeof(optFirst), typeof(second), typeof(optLast));
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> . <Name field>`){
    return computeFieldType(computeReceiverType(current, receiver), "<field>", typeof(field), frb.getScope());
}

AType computeReceiverType(Statement current, (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`){
    return computeReceiverType(current, receiver);
}

AType computeReceiverType(Statement current, (Assignable) `\< <{Assignable ","}+ elements> \>`){
    return atuple(atypeList([computeReceiverType(current, element) | element <- elements]));
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, str operator, Statement statement, FRBuilder frb){
   names = getReceiver(receiver, frb);
   frb.calculate("assignable with subscript", current, [*names, subscript, statement], 
       AType (){ 
           recType  = computeReceiverType(current, receiver);
           asgType  = computeOperatorType(current, recType, operator, typeof(statement));
           subsType = computeSubscriptionType(current, recType, [typeof(subscript)]);
           subtype(typeof(statement), subsType, onError(current, "Element of type <fmt(typeof(statement))> cannot be assigned to <receiver> of type <fmt(recType)>"));
           return subsType;
         });
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, str operator, Statement statement, FRBuilder frb){
   names = getReceiver(receiver, frb);
   if(optFirst is noExpression) frb.fact(optFirst, aint());
   if(optLast is noExpression) frb.fact(optLast, aint());
   frb.calculate("assignable with slice", current, [*names, optFirst, optLast, statement], 
       AType (){ 
           recType  = computeReceiverType(current, receiver);
           asgType  = computeOperatorType(current, recType, operator, typeof(statement));
           sliceType = computeSliceType(recType, typeof(optFirst), aint(), typeof(optLast));
           if(!subtype(asgType, sliceType)){
              reportError(current, "Sliced assignment to <fmt(sliceType)> of <fmt(typeof(statement))> not allowed");
           }
           return sliceType;
         });
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, str operator, Statement statement, FRBuilder frb){
   names = getReceiver(receiver, frb);
   if(optFirst is noExpression) frb.fact(optFirst, aint());
   if(optLast is noExpression) frb.fact(optLast, aint());
   frb.calculate("assignable with slice", current, [*names, optFirst, second, optLast, statement], 
       AType (){ 
           recType  = computeReceiverType(current, receiver);
           asgType  = computeOperatorType(current, recType, operator, typeof(statement));
           sliceType = computeSliceType(recType, typeof(optFirst), typeof(second), typeof(optLast));
           if(!subtype(asgType, sliceType)){
              reportError(current, "Sliced assignment to <fmt(sliceType)> of <fmt(typeof(statement))> not allowed");
           }
           return sliceType;
         });
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> . <Name field>`, str operator, Statement statement, FRBuilder frb){
   names = getReceiver(receiver, frb);
   frb.use(field, {fieldId()});
   scope = frb.getScope();
   frb.calculate("assignable with field", current, [*names, statement], 
       AType (){ 
           recType  = computeReceiverType(current, receiver);
           asgType  = computeOperatorType(current, recType, operator, typeof(statement));
           fieldType = computeFieldType(current, recType, "<field>", typeof(field), scope);
           
           subtype(typeof(statement), fieldType, onError(current, "Value of type <fmt(typeof(statement))> cannot be assigned to <receiver> of type <fmt(recType)>"));
           return fieldType;
         });
}

void checkAssignment(Statement current, (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, str operator, Statement statement, FRBuilder frb){
   names = getReceiver(receiver, frb);
   frb.calculate("assignable with default expression", current, [*names, defaultExpression, statement], 
       AType (){ 
           recType    = computeReceiverType(current, receiver);
           asgType    = computeOperatorType(current, recType, operator, typeof(statement));
           asgTypeDef = computeOperatorType(current, typeof(defaultExpression), operator, typeof(statement));
           subtype(typeof(statement), recType, onError(current, "Value of type <fmt(typeof(statement))> cannot be assigned to <receiver> of type <fmt(recType)>"));
           subtype(typeof(defaultExpression), recType, onError(current, "Defaults of type <fmt(typeof(defaultExpression))> cannot be assigned to <receiver> of type <fmt(recType)>"));
           return lub(asgType, asgTypeDef);
         });
}

AType() makeDef(list[AType] taus, int i) = AType() { return taus[i]; };

void checkAssignment(Statement current, receiver: (Assignable) `\< <{Assignable ","}+ elements> \>`, str operator, Statement statement, FRBuilder frb){
   names = getReceiver(receiver, frb);
   taus = [frb.newTypeVar() | nm <- names];
   for(int i <- index(names)){frb.define("<names[i]>", variableId(), names[i], defLub([statement], makeDef(taus, i)));}
   
   frb.calculate("assignable with tuple", current, [/* *names, */ statement], 
       AType (){ 
           recType  = computeReceiverType(current, receiver);
           asgType  = computeOperatorType(current, recType, operator, typeof(statement));
           if(!isTupleType(asgType)) reportError(statement, "Tuple type required, found <fmt(asgType)>");
           asgFields = getTupleFields(asgType);
           if(size(names) != size(asgFields)) reportError(statement, "Tuple type required of arity <size(names)>, found arity <size(asgFields)>");
           for(int i <- index(names)){
               if(isFullyInstantiated(typeof(names[i]))){
                  subtype(asgFields[i], typeof(names[i]), onError(names[i], "Value of type <fmt(asgFields[i])> cannot be assigned to <fmt("<names[i]>")> of type <fmt(typeof(names[i]))>"));
               } else {
                 unify(taus[i], asgFields[i], onError(current, "Cannot bind variable <fmt("<names[i]>")>"));
               }
            }
           return asgType;
         });
}

list[QualifiedName] getReceiver((Assignable) `<QualifiedName name>`, FRBuilder frb){
    frb.use(name,{variableId()});
    return [name];
}
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <Expression subscript> ]`, FRBuilder frb) = getReceiver(receiver, frb);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, FRBuilder frb) =  getReceiver(receiver, frb);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, FRBuilder frb) =  getReceiver(receiver, frb);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> . <Name field>`, FRBuilder frb) = getReceiver(receiver, frb);
list[QualifiedName] getReceiver((Assignable) `<Assignable receiver> ? <Expression defaultExpression>`, FRBuilder frb) =  getReceiver(receiver, frb);
list[QualifiedName] getReceiver((Assignable) `\< <{Assignable ","}+ elements> \>`, FRBuilder frb) = [*getReceiver(element, frb) | Assignable element <- elements];

default list[QualifiedName] getReceiver(Assignable asg, FRBuilder frb) { throw "Unsupported assignable <asg>"; }

// ---- return, defined in Declarations, close to function declarations

// ---- throw

void collect(current:(Statement) `throw <Statement statement>`, FRBuilder frb){
    frb.fact(current, avoid());
    collectParts(current, frb);
}

// ---- function declaration, see Declaration

// ---- local variable declaration

void collect(current: (Statement) `<Type tp> <{Variable ","}+ variables>;`, FRBuilder frb){
    declaredType = convertType(tp, frb);
    declaredTypeParams = collectRascalTypeParams(declaredType);
    scope = frb.getScope();
    AType tau = declaredType;
    if(isEmpty(declaredTypeParams)){
       frb.calculate("variable declaration", current, [], AType(){ return expandUserTypes(declaredType, scope); });
    } else {
       if(size([v | v <- variables]) > 1){
          frb.reportError(current, "Parameterized declared type not allowed with multiple initializations");
       }
       tau = frb.newTypeVar();
    }
    
    for(v <- variables){
        if(v is initialized){
            if(isEmpty(declaredTypeParams)){ 
               frb.define("<v.name>", variableId(), v, defType([], AType() { return expandUserTypes(tau, scope); })); 
               frb.calculate("declaration of variable `<v.name>`", v, [v.initial],   
                   AType (){ 
                       initialType = typeof(v.initial); 
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
               frb.define("<v.name>", variableId(), v, defType(tau)); 
               frb.calculate("declaration of variable `<v.name>`, declared with parametrized type", v.name, [v.initial],
                   AType () { 
                       initialType = typeof(v.initial); 
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
          frb.define("<v.name>", variableId(), v, defType([], AType() { return expandUserTypes(tau, scope); }));
        }
    }
    collectParts(current, frb);
}
