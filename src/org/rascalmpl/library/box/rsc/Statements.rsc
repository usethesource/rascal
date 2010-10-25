module box::rsc::Statements
import rascal::\old-syntax::Rascal;
import box::Box;
import box::Concrete;
public Box getStatements(Tree q) {
if (Statement a:=q) 
switch(a) {
        case `<Expression expression> ; `:  return H(0, [evPt(expression), L(";")]);
        case `<Assignable assignable> <Assignment operator> <Statement statement> `:
                        return HV(0, [H(0, [evPt(assignable), evPt(operator)]), I([evPt(statement)])]);
        case `<LocalVariableDeclaration declaration> ; `: return H(0, [evPt(declaration), L(";")]);
        }
if (Declarator a:=q) 
switch(a) {
	case `<Type typ> <{Variable ","}+  c > `:  return HV(1,[evPt(typ)]+ getArgsSep(c));
}
return NULL();
}


     /*
	case `solve ( <{QualifiedName ","}+  c > <Bound bound> ) <Statement body> `: return NULL();
	case `<Label label> for ( <{Expression ","}+  c > ) <Statement body> `: return NULL();
	case `<Label label> while ( <{Expression ","}+  c > ) <Statement body> `: return NULL();
	case `<Label label> do <Statement body> while ( <Expression condition> ) ; `: return NULL();
	case `<Label label> if ( <{Expression ","}+  c > ) <Statement thenStatement> else <Statement elseStatement> `: return NULL();
	case `<Label label> if ( <{Expression ","}+  c > ) <Statement thenStatement> <NoElseMayFollow noElseMayFollow> `: return NULL();
	// case `<Label label> switch ( <Expression expres> ) { } `: return NULL();
	case `<Label label> <Visit vis> `: return NULL();
	case `; `: return NULL();
	
	case `<Expression expression> ; `: return NULL();
	case `<Assignable assignable> <Assignment operator> <Statement statement> `: return NULL();
	case `assert <Expression expression> ; `: return NULL();
	case `assert <Expression expression> : <Expression message> ; `: return NULL();
	case `return <Statement statement> `: return NULL();
	case `throw <Statement statement> `: return NULL();
	
	case `return <Statement statement> `: return NULL();
	case `throw <Statement statement> `: return NULL();
	case `insert <DataTarget dataTarget> <Statement statement> `: return NULL();
	case `append <DataTarget dataTarget> <Statement statement> `: return NULL();
	case `<Assignment operator> <Statement statement> `: return NULL();
	case `return <Statement statement> `: return NULL();
	case `throw <Statement statement> `: return NULL();
	case `insert <DataTarget dataTarget> <Statement statement> `: return NULL();
	case `append <DataTarget dataTarget> <Statement statement> `: return NULL();
	case `<FunctionDeclaration functionDeclaration> `: return NULL();
	case `<LocalVariableDeclaration declaration> ; `: return NULL();
	case `insert <DataTarget dataTarget> <Statement statement> `: return NULL();
	case `append <DataTarget dataTarget> <Statement statement> `: return NULL();
	case `break <Target target> ; `: return NULL();
	case `fail <Target target> ; `: return NULL();
	case `continue <Target target> ; `: return NULL();
	case `try <Statement body> `: return NULL();
	case `try <Statement body> finally <Statement finallyBody> `: return NULL();
	case `<Label label> { } `: return NULL();
	case `<FunctionDeclaration functionDeclaration> `: return NULL();
	
	case `global <Type typ> <{QualifiedName ","}+  c > ; `: return NULL();
        */

/*

if (Symbol a:=q) 
switch(a) {
	case `<Symbol symbol> ? `: return NULL();
	case `<Symbol symbol> + `: return NULL();
	case `<Symbol symbol> * `: return NULL();
}
if (Target a:=q) 
switch(a) {
	case ``: return NULL();
	case `<Name name> `: return NULL();
}
if (LocalVariableDeclaration a:=q) 
switch(a) {
	case `<Declarator declarator> `: return NULL();
	case `dynamic <Declarator declarator> `: return NULL();
}

if (DataTarget a:=q) 
switch(a) {
	case ``: return NULL();
	case `<Name label> : `: return NULL();
}
if (Expression a:=q) 
switch(a) {
	case `{ } `: return NULL();
	case `<Label label> <Visit vis> `: return NULL();
}
if (Assignment a:=q) 
switch(a) {
	case `= `: return NULL();
	case `+= `: return NULL();
	case `-= `: return NULL();
	case `*= `: return NULL();
	case `/= `: return NULL();
	case `&= `: return NULL();
	case `?= `: return NULL();
}

if (Assignable a:=q) 
switch(a) {
	case `<QualifiedName qualifiedName> `: return NULL();
	case `<Assignable receiver> [ <Expression subscript> ] `: return NULL();
	case `<Assignable receiver> . <Name field> `: return NULL();
	case `<Assignable receiver> ? <Expression defaultExpression> `: return NULL();
	case `@ <Name annotation> `: return NULL();
	case `< <{Assignable ","}+  c > > `: return NULL();
	case `<Name name> ( <{Assignable ","}+  c > ) `: return NULL();
	case `<Assignable receiver> @ <Name annotation> `: return NULL();
	case `< <{Assignable ","}+  c > > `: return NULL();
	case `<Name name> ( <{Assignable ","}+  c > ) `: return NULL();
}
if (NoElseMayFollow a:=q) 
switch(a) {
	case ``: return NULL();
}
if (Catch a:=q) 
switch(a) {
	case `catch : <Statement body> `: return NULL();
	case `catch <Pattern pattern> : <Statement body> `: return NULL();
}
if (Label a:=q) 
switch(a) {
	case ``: return NULL();
	case `<Name name> : `: return NULL();
}
if (Bound a:=q) 
switch(a) {
	case ``: return NULL();
	case `; <Expression expression> `: return NULL();
}
*/