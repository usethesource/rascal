@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalStatement

import Prelude;
import lang::rascal::\syntax::Rascal;

import experiments::Compiler::Rascal2muRascal::TmpAndLabel;
import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::Rascal2muRascal::RascalExpression;

import experiments::Compiler::muRascal::AST;
import experiments::Compiler::Rascal2muRascal::TypeUtils;

default MuExp translate((Statement) `<Statement* statements>`) = muBlock([ translate(stat) | stat <- statements ]);

/********************************************************************/
/*                  Statement                                       */
/********************************************************************/
	
MuExp translate(s: (Statement) `assert <Expression expression> ;`) = muCallPrim("assertreport", [translate(expression), muCon(""), muCon(s@\loc)]);

MuExp translate(s: (Statement) `assert <Expression expression> : <Expression message>;`) = muCallPrim("assertreport", [translate(expression), translate(message), muCon(s@\loc)]);

MuExp translate(s: (Statement) `<Expression expression> ;`) = translate(expression);

MuExp translate(s: (Statement) `<Label label> <Visit \visit>`) { throw("visit"); }

MuExp translate(s: (Statement) `<Label label> while ( <{Expression ","}+ conditions> ) <Statement body>`) {
    whilename = getLabel(label);
    tmp = asTmp(whilename);
    enterLoop(whilename);
    code = [ muAssignTmp(tmp, muCallPrim("listwriter_open", [])), 
             muWhile(whilename, muOne([translate(c) | c <-conditions]), [translate(body)]),
             muCallPrim("listwriter_close", [muTmp(tmp)])
           ];
    leaveLoop();
    return muBlock(code);
}

MuExp translateTemplate((StringTemplate) `while ( <Expression condition> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    whilename = nextLabel();
    result = asTmp(whilename);
    enterLoop(whilename);
    code = [ muAssignTmp(result, muCallPrim("template_open", [])), 
             muWhile(whilename, muOne([translate(condition)]), 
                     [ translate(preStats),  
                        muAssignTmp(result, muCallPrim("template_add", [muTmp(result), translateMiddle(body)])), 
                       translate(postStats)
                     ]),
             muCallPrim("template_close", [muTmp(result)])
           ];
    leaveLoop();
    return muBlock(code);
}

MuExp translate(s: (Statement) `<Label label> do <Statement body> while ( <Expression condition> ) ;`) {  
    doname = getLabel(label);
    tmp = asTmp(doname);
    enterLoop(doname);
    code = [ muAssignTmp(tmp, muCallPrim("listwriter_open", [])), 
             muDo(doname,  [translate(body)], muOne([translate(condition)])),
             muCallPrim("listwriter_close", [muTmp(tmp)])
           ];
    leaveLoop();
    return muBlock(code);
}

MuExp translateTemplate(s: (StringTemplate) `do { < Statement* preStats> <StringMiddle body> <Statement* postStats> } while ( <Expression condition> )`) {  
    doname = nextLabel();
    result = asTmp(doname);
    enterLoop(doname);
    code = [ muAssignTmp(result, muCallPrim("template_open", [])),
             muDo(doname,  [ translate(preStats),
                             muAssignTmp(result, muCallPrim("template_add", [muTmp(result), translateMiddle(body)])),
                             translate(postStats)], 
                  muOne([translate(condition)])
                 ),
             muCallPrim("template_close", [muTmp(result)])
           ];
    leaveLoop();
    return muBlock(code);
}

MuExp translate(s: (Statement) `<Label label> for ( <{Expression ","}+ generators> ) <Statement body>`) {
    forname = getLabel(label);
    tmp = asTmp(forname);
    enterLoop(forname);
    code = [ muAssignTmp(tmp, muCallPrim("listwriter_open", [])), 
             muWhile(forname, muAll([translate(c) | c <-generators]), [ translate(body) ]),
             muCallPrim("listwriter_close", [muTmp(tmp)])
           ];
    leaveLoop();
    return muBlock(code);
}

MuExp translateTemplate((StringTemplate) `for ( <{Expression ","}+ generators> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    forname = nextLabel();
    result = asTmp(forname);
    enterLoop(forname);
    code = [ muAssignTmp(result, muCallPrim("template_open", [])),
             muWhile(forname, muAll([translate(c) | c <-generators]), 
                     [ translate(preStats),  
                       muAssignTmp(result, muCallPrim("template_add", [muTmp(result), translateMiddle(body)])),
                       translate(postStats)
                     ]),
             muCallPrim("template_close", [muTmp(result)])
           ];
    leaveLoop();
    return muBlock(code);
} 

MuExp translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement>`) =
    muIfelse(muOne([translate(c) | c <-conditions]), [translate(thenStatement)], []);
    
MuExp translateTemplate((StringTemplate) `if (<{Expression ","}+ conditions> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`){
    ifname = nextLabel();
    result = asTmp(ifname);
    enterLoop(ifname);
    code = [ muAssignTmp(result, muCallPrim("template_open", [])),
             muIfelse(muOne([translate(c) | c <-conditions]), 
                      [ translate(preStats),
                         muAssignTmp(result, muCallPrim("template_add", [muTmp(result), translateMiddle(body)])),
                        translate(postStats)],
                      []),
               muCallPrim("template_close", [muTmp(result)])
           ];
    leaveLoop();
    return muBlock(code);
}    

MuExp translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement> else <Statement elseStatement>`) =
    muIfelse(muOne([translate(c) | c <-conditions]), [translate(thenStatement)], [translate(elseStatement)]);
    
MuExp translateTemplate((StringTemplate) `if ( <{Expression ","}+ conditions> ) { <Statement* preStatsThen> <StringMiddle thenString> <Statement* postStatsThen> }  else { <Statement* preStatsElse> <StringMiddle elseString> <Statement* postStatsElse> }`){                    
    ifname = nextLabel();
    result = asTmp(ifname);
    enterLoop(ifname);
    code = [ muAssignTmp(result, muCallPrim("template_open", [])),
             muIfelse(muOne([translate(c) | c <-conditions]), 
                      [ translate(preStatsThen), 
                        muAssignTmp(result, muCallPrim("template_add", [muTmp(result), translateMiddle(thenString)])),
                        translate(postStatsThen)
                      ],
                      [ translate(preStatsElse), 
                        muAssignTmp(result, muCallPrim("template_add", [muTmp(result), translateMiddle(elseString)])),
                        translate(postStatsElse)
                      ]),
              muCallPrim("template_close", [muTmp(result)])
           ];
    leaveLoop();
    return muBlock(code);                                             
} 

MuExp translate(s: (Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`) { throw("switch"); }

MuExp translate(s: (Statement) `fail <Target target> ;`) = 
     inBacktrackingScope() ? muFail(target is empty ? currentLoop() : "<target.label>")
                           : muFailReturn();

MuExp translate(s: (Statement) `break <Target target> ;`) = muBreak(target is empty ? currentLoop() : "<target.label>");

MuExp translate(s: (Statement) `continue <Target target> ;`) = muContinue(target is empty ? currentLoop() : "<target.label>");

MuExp translate(s: (Statement) `filter ;`) { throw("filter"); }

MuExp translate(s: (Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`) { throw("solve"); }

MuExp translate(s: (Statement) `try  <Statement body> <Catch+ handlers>`) { throw("try"); }

MuExp translate(s: (Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`) { throw("tryFinally"); }

MuExp translate(s: (Statement) `<Label label> { <Statement+ statements> }`) =
    muBlock([translate(stat) | stat <- statements]);

MuExp translate(s: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`) = translateAssignment(s); 

MuExp translate(s: (Statement) `;`) = muBlock([]);

MuExp translate(s: (Statement) `global <Type \type> <{QualifiedName ","}+ names> ;`) { throw("globalDirective"); }

MuExp translate(s: (Statement) `return <Statement statement>`) {
  t = translate(statement);
  return muReturn(t);
}

MuExp translate(s: (Statement) `throw <Statement statement>`) { throw("throw"); }

MuExp translate(s: (Statement) `insert <DataTarget dataTarget> <Statement statement>`) { throw("insert"); }

MuExp translate(s: (Statement) `append <DataTarget dataTarget> <Statement statement>`) =
   muCallPrim("listwriter_add", [muTmp(asTmp(currentLoop())), translate(statement)]);

MuExp translate(s: (Statement) `<FunctionDeclaration functionDeclaration>`) { translate(functionDeclaration); return muBlock([]); }

MuExp translate(s: (Statement) `<LocalVariableDeclaration declaration> ;`) { 
    tp = declaration.declarator.\type;
    {Variable ","}+ variables = declaration.declarator.variables;
    code = for(var <- variables){
    			if(var is initialized)
    				append mkAssign("<var.name>", var.name@\loc, translate(var.initial));
             }
    return muBlock(code);
}

default MuExp translate(Statement s){
   throw "MISSING CASE FOR STATEMENT: <s>";
}

/*********************************************************************/
/*                  End of Statements                                */
/*********************************************************************/


MuExp translateAssignment(s: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`) =
    assignTo(assignable, applyAssignmentOperator("<operator>", assignable, statement));

// apply assignment operator 
    
MuExp applyAssignmentOperator(str operator, assignable, statement) {
    if(operator == "=")
    	return translate(statement);
    op1 = ("+=" : "add", "-=" : "subtract", "*=" : "product", "/=" : "divide", "&=" : "intersect")[operator];  // missing ?=
    op2 = "<getOuterType(assignable)>_<op1>_<getOuterType(statement)>";
    oldval = getValues(assignable);
    assert size(oldval) == 1;
    return muCallPrim("<op2>", [*oldval, translate(statement)]); 	
}
    
// assignTo: assign the rhs of the assignment (possibly modified by an assign operator) to the assignable
    
MuExp assignTo(a: (Assignable) `<QualifiedName qualifiedName>`, MuExp rhs) {
    return mkAssign("<qualifiedName>", qualifiedName@\loc, rhs);
}

MuExp assignTo(a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`, MuExp rhs) =
     assignTo(receiver, muCallPrim("<getOuterType(receiver)>_update", [*getValues(receiver), translate(subscript), rhs]));
    
MuExp assignTo(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, MuExp rhs) =
    assignTo(receiver, muCallPrim("<getOuterType(receiver)>_replace", [*getValues(receiver), translateOpt(optFirst), muCon(false), translateOpt(optLast), rhs]) );

MuExp assignTo(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`) =
     assignTo(receiver, muCallPrim("<getOuterType(receiver)>_replace", [*getValues(receiver), translateOpt(optFirst), translate(second), translateOpt(optLast), rhs]));

MuExp assignTo(a: (Assignable) `<Assignable receiver> . <Name field>`, MuExp rhs){
    return assignTo(receiver, muCallPrim("<getOuterType(receiver)>_update", [*getValues(receiver), muCon("<field>"), rhs]) );
}

// ifdefined

MuExp assignTo(a: (Assignable) `\<  <{Assignable ","}+ elements> \>`, MuExp rhs) {
	nelems = size_assignables(elements);
    name = nextTmp();
    elems = [ e | e <- elements];	// hack since elements[i] yields a value result;
    return muBlock(
              muAssignTmp(name, rhs) + 
              [ assignTo(elems[i], muCallPrim("tuple_subscript", [muTmp(name), muCon(i)]) )
              | i <- [0 .. nelems]
              ]);
}

MuExp assignTo(a: (Assignable) `<Name name> ( <{Assignable ","}+ arguments> )`, MuExp rhs) { 
    nelems = size_assignables(elements);
    name = nextTmp();
    elems = [ e | e <- elements];	// hack since elements[i] yields a value result;
    return muBlock(
              muAssignTmp(name, rhs) + 
              [ assignTo(elems[i], muCalla("adt_subscript", [muTmp(name), muCon(i)]) )
              | i <- [0 .. nelems]
              ]);
}

MuExp assignTo(a: (Assignable) `<Assignable receiver> @ <Name annotation>`,  MuExp rhs) =
     assignTo(receiver, muCallPrim("annotation_setupdate", [*getValues(receiver), muCon("<field>"), rhs]));

// getValues: get the current value(s) of an assignable

list[MuExp] getValues(a: (Assignable) `<QualifiedName qualifiedName>`) = 
    [ mkVar("<qualifiedName>", qualifiedName@\loc) ];
    
list[MuExp] getValues(a: (Assignable) `<Assignable receiver> [ <Expression subscript> ]`) =
    [ muCallPrim("<getOuterType(receiver)>_subscript", [*getValues(receiver), translate(subscript)]) ];
    
list[MuExp] getValues(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) = 
    translateSlice(getValues(receiver), translateOpt(optFirst), muCon(false),  translateOpt(optLast));
    
list[MuExp] getValues(a: (Assignable) `<Assignable receiver> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`) = 
    translateSlice(getValues(receiver), translateOpt(optFirst), translate(second),  translateOpt(optLast));

list[MuExp] getValues(a:(Assignable) `<Assignable receiver> . <Name field>`) = 
    [ muCallPrim("<getOuterType(receiver)>_field_access", [ *getValues(receiver), muCon("<field>")]) ];

// ifdefined

list[MuExp] getValues(a:(Assignable) `\<  <{Assignable ","}+ elements > \>` ) = [ *getValues(elm) | elm <- elements ];

list[MuExp] getValues(a:(Assignable) `<Name name> ( <{Assignable ","}+ arguments> )` ) = [ *getValues(arg) | arg <- arguments ];

list[MuExp] getValues(a: (Assignable) `<Assignable receiver> @ <Name annotation>`) = 
    [ muCallPrim("annotation_get", [ *getValues(receiver), muCon("<field>")]) ];

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
