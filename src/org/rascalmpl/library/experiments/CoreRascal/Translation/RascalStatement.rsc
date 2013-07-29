@bootstrapParser
module experiments::CoreRascal::Translation::RascalStatement

import Prelude;
import experiments::CoreRascal::ReductionWithEvalCtx::AST;
import lang::rascal::\syntax::Rascal;
import experiments::CoreRascal::Translation::RascalExpression;

/*
syntax Assignment
	= ifDefined: "?=" 
	| division: "/=" 
	| product: "*=" 
	| intersection: "&=" 
	| subtraction: "-=" 
	| \default: "=" 
	| addition: "+=" 
	| \append: "\<\<="
	;

syntax Assignable
	= bracket \bracket   : "(" Assignable arg ")"
	| variable          : QualifiedName qualifiedName
    | subscript         : Assignable receiver "[" Expression subscript "]" 
    | slice             : Assignable receiver "[" OptionalExpression optFirst ".." OptionalExpression optLast "]" 
    | sliceStep         : Assignable receiver "[" OptionalExpression optFirst "," Expression second ".." OptionalExpression optLast "]"     
	| fieldAccess       : Assignable receiver "." Name field 
	| ifDefinedOrDefault: Assignable receiver "?" Expression defaultExpression 
	| constructor       : Name name "(" {Assignable ","}+ arguments ")"  
	| \tuple             : "\<" {Assignable ","}+ elements "\>" 
	| annotation        : Assignable receiver "@" Name annotation  ;
*/

/*********************************************************************/
/*                  Statements                                       */
/*********************************************************************/
	
str translate(s: (Statement) `assert <Expression expression> ;`) { throw("assert"); }

str translate(s: (Statement) `assert <Expression expression> : <Expression message>;`) { throw("assertWithMessage"); }

str translate(s: (Statement) `<Expression expression> ;`) = translate(expression);

str translate(s: (Statement) `<Label label> <Visit \visit>`) { throw("visit"); }

str translate(s: (Statement) `<Label label> while ( <{Expression ","}+ conditions> ) <Statement body>`) { throw("while"); }

str translate(s: (Statement) `<Label label> do <Statement body> while ( <Expression condition> ) ;`) { throw("doWhile"); }

str translate(s: (Statement) `<Label label> for ( <{Expression ","}+ generators> ) <Statement body>`) { throw("for"); }

str translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement>`) { throw("ifThen"); }

str translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement> else <Statement elseStatement>`) { throw("ifThenElse"); }

str translate(s: (Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`) { throw("switch"); }

str translate(s: (Statement) `fail <Target target> ;`) { throw("fail"); }

str translate(s: (Statement) `break <Target target> ;`) { throw("break"); }

str translate(s: (Statement) `continue <Target target> ;`) { throw("continue"); }

str translate(s: (Statement) `filter ;`) { throw("filter"); }

str translate(s: (Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`) { throw("solve"); }

str translate(s: (Statement) `try  <Statement body> <Catch+ handlers>`) { throw("try"); }

str translate(s: (Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`) { throw("tryFinally"); }

str translate(s: (Statement) `<Label label> { <Statement+ statements> }`) { throw("nonEmptyBlock"); }

str translate(s: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`) { throw("assignment"); }

str translate(s: (Statement) `;`) = "";

str translate(s: (Statement) `global <Type \type> <{QualifiedName ","}+ names> ;`) { throw("globalDirective"); }

str translate(s: (Statement) `return <Statement statement>`) { throw("return"); }

str translate(s: (Statement) `throw <Statement statement>`) { throw("throw"); }

str translate(s: (Statement) `insert <DataTarget dataTarget> <Statement statement>`) { throw("insert"); }

str translate(s: (Statement) `append <DataTarget dataTarget> <Statement statement>`) { throw("append"); }

str translate(s: (Statement) `<FunctionDeclaration functionDeclaration>`) { throw("functionDeclaration"); }

str translate(s: (Statement) `<LocalVariableDeclaration declaration> ;`) { throw("variableDeclaration"); }

/*********************************************************************/
/*                  End of Statements                                */
/*********************************************************************/
