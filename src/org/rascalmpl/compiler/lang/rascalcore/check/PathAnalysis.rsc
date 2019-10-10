@bootstrapParser
module lang::rascalcore::check::PathAnalysis

extend lang::rascalcore::check::AType;
extend analysis::typepal::TypePal;
import lang::rascal::\syntax::Rascal;

/********************************************************************/
/*       Return path analysis                                       */
/********************************************************************/

bool returnsViaAllPath(FunctionBody fb, str fname, Collector c)
    = returnsViaAllPath([ statement | statement <- fb.statements ], fname, c);

bool returnsViaAllPath((Statement) `<Label label> <Visit vis>`, str fname,  Collector c){
   return  all(Case cs <- vis.cases, returnsViaAllPath(cs, fname, c)) && any(Case cs <- vis.cases, cs is \default);
}
bool returnsViaAllPath((Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`, str fname,  Collector c)
    = all(Case cs <- cases, returnsViaAllPath(cs, fname, c)) && any(Case cs <- cases, cs is \default);

bool returnsViaAllPath((Case) `case <PatternWithAction patternWithAction>`, str fname,  Collector c){
    return patternWithAction is arbitrary && returnsViaAllPath([patternWithAction.statement], fname, c);
}

bool returnsViaAllPath((Case) `default: <Statement statement>`, str fname,  Collector c)
    = returnsViaAllPath(statement, fname, c);

bool returnsViaAllPath((Statement) `try <Statement body> <Catch+ handlers>`, str fname,  Collector c)
    =  returnsViaAllPath(body, fname, c) 
    && all(h <- handlers, returnsViaAllPath(h.body, fname, c));
    
bool returnsViaAllPath((Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`, str fname,  Collector c)
    =  returnsViaAllPath(body, fname, c) 
    && all(h <- handlers, returnsViaAllPath(h.body, fname, c))
    || returnsViaAllPath(finallyBody, fname, c);
    

bool returnsViaAllPath((Statement) `<Label label> while( <{Expression ","}+ conditions> ) <Statement body>`, str fname,  Collector c){
    returnsViaAllPath(body, fname, c); 
    return false;
}

bool returnsViaAllPath((Statement) `<Label label> do <Statement body> while ( <Expression condition> ) ;`, str fname,  Collector c){
    returnsViaAllPath(body, fname, c);
    return false;
}

bool returnsViaAllPath((Statement) `<Label label> for( <{Expression ","}+ generators> ) <Statement body>`, str fname,  Collector c){
    returnsViaAllPath(body, fname, c);
    return false;
}

bool returnsViaAllPath((Statement) `<Label label> if( <{Expression ","}+ conditions> ) <Statement thenStatement>`, str fname,  Collector c){
    returnsViaAllPath(thenStatement, fname,  c);
    return false;
}
bool returnsViaAllPath((Statement) `<Label label> if( <{Expression ","}+ conditions> ) <Statement thenStatement> else <Statement elseStatement>`, str fname,  Collector c)
    = returnsViaAllPath(thenStatement, fname, c) && returnsViaAllPath(elseStatement, fname, c);
 
bool returnsViaAllPath((Statement) `return <Statement statement>`, str fname,  Collector c) = returnsValue(statement, fname, c) && (returnsViaAllPath(statement, fname, c) || true);
bool returnsViaAllPath((Statement) `throw <Statement statement>`, str fname,  Collector c) = returnsValue(statement,fname, c);
bool returnsViaAllPath((Statement) `fail <Target target>;`, str fname,  Collector c) = isEmpty("<target>") || "<target>" == fname;
bool returnsViaAllPath((Statement) `insert <DataTarget dataTarget> <Statement statement>`, str fname,  Collector c) = true;

bool returnsViaAllPath((Statement) `<Label label> { <Statement+ statements> }`, str fname,  Collector c)
    = returnsViaAllPath([ statement | statement <- statements ], fname, c);
    
bool returnsViaAllPath((Statement) `{ <Statement+ statements> }`, str fname,  Collector c)
    = returnsViaAllPath([ statement | statement <- statements ], fname, c);
    
bool returnsViaAllPath(list[Statement] statements, str fname,  Collector c){
    int nstats = size(statements);
    for(int i <- index(statements)){
        statement = statements[i];
        if(returnsViaAllPath(statement, fname, c)){
            reportDeadCode(statements[i+1 ..], c);
            return true;
        } else if(i == nstats - 1){
            return false;
        }
        if(leavesBlock(statement)){
            reportDeadCode(statements[i+1 ..], c);
            return false;
        }
    }
    return false;
}
 
default bool returnsViaAllPath(Statement s, str fname, Collector c) = false;

bool returnsValue((Statement) `<Label label> while( <{Expression ","}+ conditions> ) <Statement body>`, str fname, Collector c) = true;
bool returnsValue((Statement) `<Label label> do <Statement body> while ( <Expression condition> ) ;`, str fname, Collector c) = true;
bool returnsValue((Statement) `<Label label> for( <{Expression ","}+ generators> ) <Statement body>`, str fname, Collector c) = true;
bool returnsValue(stat:(Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`, str fname,  Collector c) = returnsViaAllPath(stat, fname, c);
bool returnsValue(stat:(Statement) `<Label label> <Visit vis>`, str fname, Collector c) = returnsViaAllPath(stat, fname, c) || true;

bool returnsValue((Statement) `<Label label> { <Statement+ statements> }`,  str fname, Collector c)
    = returnsValue([stat | stat <- statements][-1], fname, c);

default bool returnsValue(Statement s,  str fname, Collector c) = s is expression || s is \visit || s is \assert || s is assertWithMessage || s is \return;

bool leavesBlock((Statement) `fail <Target target> ;`) = true;
bool leavesBlock((Statement) `break <Target target> ;`) = true;
bool leavesBlock((Statement) `continue <Target target> ;`) = true;
bool leavesBlock((Statement) `insert <DataTarget dataTarget> <Statement statement>`) = true;
default bool leavesBlock(Statement s) = false;

void reportDeadCode(list[Statement] statements, Collector c){
    for(stat <- statements){
        if("<stat>" != ";") c.report(error(stat, "Dead code"));
    }
}
