@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalStatement

import Prelude;
import lang::rascal::\syntax::Rascal;

import experiments::Compiler::Rascal2muRascal::TmpAndLabel;
import experiments::Compiler::Rascal2muRascal::RascalExpression;

import experiments::Compiler::muRascal::AST;
import experiments::Compiler::Rascal2muRascal::TypeUtils;


/*********************************************************************/
/*                  Statements                                       */
/*********************************************************************/
	
list[MuExp] translate(s: (Statement) `assert <Expression expression> ;`) { throw("assert"); }

list[MuExp] translate(s: (Statement) `assert <Expression expression> : <Expression message>;`) { throw("assertWithMessage"); }

list[MuExp] translate(s: (Statement) `<Expression expression> ;`) = translate(expression);

list[MuExp] translate(s: (Statement) `<Label label> <Visit \visit>`) { throw("visit"); }

list[MuExp] translate(s: (Statement) `<Label label> while ( <{Expression ","}+ conditions> ) <Statement body>`) {
    loopname = getLabel(label);
    println("loopname = <loopname>");
    tmp = asTmp(loopname);
    enterLoop(loopname);
    code = [ muAssignTmp(tmp, muCallPrim("listwriter_open", [])), 
             muWhile(loopname, muOne([*translate(c) | c <-conditions]), translate(body)),
             muCallPrim("listwriter_close", [muTmp(tmp)])
           ];
    leaveLoop();
    println("while code: <code>");
    return code;
}

list[MuExp] translate(s: (Statement) `<Label label> do <Statement body> while ( <Expression condition> ) ;`) { throw("doWhile"); }

list[MuExp] translate(s: (Statement) `<Label label> for ( <{Expression ","}+ generators> ) <Statement body>`) {
    loopname = getLabel(label);
    tmp = asTmp(loopname);
    enterLoop(loopname);
    code = [  muAssignTmp(tmp, muCallPrim("list_create", [])), muWhile(loopname, muAll([*translate(c) | c <-generators]), translate(body)) ];
    leaveLoop();
    return code;
}

list[MuExp] translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement>`) =
    [ muIfelse(muOne([*translate(c) | c <-conditions]), translate(thenStatement), []) ];

list[MuExp] translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement> else <Statement elseStatement>`) =
    [ muIfelse(muOne([*translate(c) | c <-conditions]), translate(thenStatement), translate(elseStatement)) ];

list[MuExp] translate(s: (Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`) { throw("switch"); }

list[MuExp] translate(s: (Statement) `fail <Target target> ;`) = [ muFail(target is empty ? currentLoop() : "<target.label>") ];

list[MuExp] translate(s: (Statement) `break <Target target> ;`) = [ muBreak(target is empty ? currentLoop() : "<target.label>") ];

list[MuExp] translate(s: (Statement) `continue <Target target> ;`) = [ muContinue(target is empty ? currentLoop() : "<target.label>") ];

list[MuExp] translate(s: (Statement) `filter ;`) { throw("filter"); }

list[MuExp] translate(s: (Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`) { throw("solve"); }

list[MuExp] translate(s: (Statement) `try  <Statement body> <Catch+ handlers>`) { throw("try"); }

list[MuExp] translate(s: (Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`) { throw("tryFinally"); }

list[MuExp] translate(s: (Statement) `<Label label> { <Statement+ statements> }`) =
    [*translate(stat) | stat <- statements];

list[MuExp] translate(s: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`) = translateAssignment(s); 

list[MuExp] translate(s: (Statement) `;`) = [];

list[MuExp] translate(s: (Statement) `global <Type \type> <{QualifiedName ","}+ names> ;`) { throw("globalDirective"); }

list[MuExp] translate(s: (Statement) `return <Statement statement>`) {
  t = translate(statement);
  return (size(t) > 0) ? t[0 .. -1] + muReturn(t[-1]) : [ muReturn() ];
}

list[MuExp] translate(s: (Statement) `throw <Statement statement>`) { throw("throw"); }

list[MuExp] translate(s: (Statement) `insert <DataTarget dataTarget> <Statement statement>`) { throw("insert"); }

list[MuExp] translate(s: (Statement) `append <DataTarget dataTarget> <Statement statement>`) =
   [ muCallPrim("listwriter_add", [muTmp(asTmp(currentLoop())), *translate(statement)]) ];

list[MuExp] translate(s: (Statement) `<FunctionDeclaration functionDeclaration>`) { translate(functionDeclaration); return []; } // we assume globally unique function names for now

list[MuExp] translate(s: (Statement) `<LocalVariableDeclaration declaration> ;`) { 
    tp = declaration.declarator.\type;
    variables = declaration.declarator.variables;
    
    return for(var <- variables){
    			if(var is initialized)
    				append mkAssign("<var.name>", var.name@\loc, translate(var.initial)[0]);
           }
}

default list[MuExp] translate(Statement s){
   throw "MISSING CASE FOR STATEMENT: <s>";
}

/*********************************************************************/
/*                  End of Statements                                */
/*********************************************************************/
	

list[MuExp] translateAssignment(s: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`) =
    assignTo(assignable, applyAssignmentOperator("<operator>", assignable, statement));

// apply assignment operator 
    
list[MuExp] applyAssignmentOperator(str operator, assignable, statement) {
    if(operator == "=")
    	return translate(statement);
    op = ("+=" : "addition", "-=" : "subtraction", "*=" : "product", "/=" : "division", "&=" : "intersection")[operator];  // missing ?=
    op += "_<getOuterType(assignable)>_<getOuterType(statement)>";
    oldval = getValues(assignable);
    assert size(oldval) == 1;
    return [muCallPrim("<op>", [*oldval, *translate(statement)])]; 	
}
    
// assignTo: assign the rhs of the assignment (possibly modified by an assign operator) to the assignable
    
list[MuExp] assignTo(a: (Assignable) `<QualifiedName qualifiedName>`, list[MuExp] rhs) {
    assert size(rhs) == 1; 
    return [ mkAssign("<qualifiedName>", qualifiedName@\loc, rhs[0]) ];
}

list[MuExp] assignTo(a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, list[MuExp] rhs) =
     assignTo(receiver, [ muCallPrim("<getOuterType(receiver)>_update", [*getValues(receiver), *translate(subscript), *rhs]) ]);
    
list[MuExp] assignTo(a: (Assignable) `<Name name> ( <{Assignable ","}+ arguments> )`, list[MuExp] rhs) { 
    nelems = size_assignables(elements);
    name = nextTmp();
    elems = [ e | e <- elements];	// hack since elements[i] yields a value result;
    return muAssignTmp(name, rhs[0]) + 
              [ *assignTo(elems[i], [ muCalla("adt_subscript", [muTmp(name), muCon(i)]) ])
              | i <- [0 .. nelems]
              ];
}

list[MuExp] assignTo(a: (Assignable) `<Assignable receiver> . <Name field>`, list[MuExp] rhs){
    return assignTo(receiver, [ muCallPrim("<getOuterType(receiver)>_update", [*getValues(receiver), muCon("<field>"), *rhs]) ]);
}

list[MuExp] assignTo(a: (Assignable) `\<  <{Assignable ","}+ elements> \>`, list[MuExp] rhs) {
	nelems = size_assignables(elements);
    name = nextTmp();
    elems = [ e | e <- elements];	// hack since elements[i] yields a value result;
    return muAssignTmp(name, rhs[0]) + 
              [ *assignTo(elems[i], [ muCallPrim("tuple_subscript", [muTmp(name), muCon(i)]) ])
              | i <- [0 .. nelems]
              ];
}

// slice
// annotation
// ifdefined

// getValues: get the current value(s) of an assignable

list[MuExp] getValues(a: (Assignable) `<QualifiedName qualifiedName>`) = 
    [ mkVar("<qualifiedName>", qualifiedName@\loc) ];
    
list[MuExp] getValues(a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`) =
    [ muCallPrim("<getOuterType(receiver)>_subscript", [*getValues(receiver), *translate(subscript)]) ];

list[MuExp] getValues(a:(Assignable) `\<  <{Assignable ","}+ elements > \>` ) = [ *getValues(elm) | elm <- elements ];

list[MuExp] getValues(a:(Assignable) `<Name name> ( <{Assignable ","}+ arguments> )` ) = [ *getValues(arg) | arg <- arguments ];

list[MuExp] getValues(a:(Assignable) `<Assignable receiver> . <Name field>`) = [ muCallPrim("<getOuterType(receiver)>_field_access", [ *getValues(receiver), muCon("<field>")]) ];

// slice
// annotation
// ifdefined

// getReceiver: get the final receiver of an assignable

Assignable getReceiver(a: (Assignable) `<QualifiedName qualifiedName>`) = a;
Assignable getReceiver(a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`) = getReceiver(receiver);
Assignable getReceiver(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) = getReceiver(receiver);
Assignable getReceiver(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`) = getReceivers(receiver);  
Assignable getReceiver(a: (Assignable) `<Assignable receiver> . <Name field>`) = getReceiver(receiver); 
Assignable getReceives(a: (Assignable) `<Assignable receiver> ? <Expression defaultExpression>`) = getReceiver(receiver); 
Assignable getReceiver(a: (Assignable) `<Name name> ( <{Assignable ","}+ arguments> )`) = a;
Assignable getReceiver(a: (Assignable) `\< <{Assignable ","}+ elements> \>`) =  a;
Assignable getReceiver(a: (Assignable) `<Assignable receiver> @ <Name annotation>`) = getReceiver(receiver); 


