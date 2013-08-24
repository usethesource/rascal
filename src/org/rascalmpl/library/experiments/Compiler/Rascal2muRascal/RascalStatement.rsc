@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalStatement

import Prelude;
import experiments::CoreRascal::ReductionWithEvalCtx::AST;
import lang::rascal::\syntax::Rascal;
import experiments::Compiler::Rascal2muRascal::RascalExpression;
import experiments::Compiler::Rascal2muRascal::RascalModule;

import experiments::Compiler::muRascal::AST;
import experiments::Compiler::Rascal2muRascal::TypeUtils;

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
	
list[MuExp] translate(s: (Statement) `assert <Expression expression> ;`) { throw("assert"); }

list[MuExp] translate(s: (Statement) `assert <Expression expression> : <Expression message>;`) { throw("assertWithMessage"); }

list[MuExp] translate(s: (Statement) `<Expression expression> ;`) = translate(expression);

list[MuExp] translate(s: (Statement) `<Label label> <Visit \visit>`) { throw("visit"); }

list[MuExp] translate(s: (Statement) `<Label label> while ( <{Expression ","}+ conditions> ) <Statement body>`) =
    [ muWhile(muOne([*translate(c) | c <-conditions]), translate(body)) ];

list[MuExp] translate(s: (Statement) `<Label label> do <Statement body> while ( <Expression condition> ) ;`) { throw("doWhile"); }

list[MuExp] translate(s: (Statement) `<Label label> for ( <{Expression ","}+ generators> ) <Statement body>`) =
    [ muWhile(muAll([*translate(c) | c <-generators]), translate(body)) ];

list[MuExp] translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement>`) =
    [ muIfelse(muOne([*translate(c) | c <-conditions]), translate(thenStatement), []) ];

list[MuExp] translate(s: (Statement) `<Label label> if ( <{Expression ","}+ conditions> ) <Statement thenStatement> else <Statement elseStatement>`) =
    [ muIfelse(muOne([*translate(c) | c <-conditions]), translate(thenStatement), translate(elseStatement)) ];

list[MuExp] translate(s: (Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`) { throw("switch"); }

list[MuExp] translate(s: (Statement) `fail <Target target> ;`) { throw("fail"); }

list[MuExp] translate(s: (Statement) `break <Target target> ;`) { throw("break"); }

list[MuExp] translate(s: (Statement) `continue <Target target> ;`) { throw("continue"); }

list[MuExp] translate(s: (Statement) `filter ;`) { throw("filter"); }

list[MuExp] translate(s: (Statement) `solve ( <{QualifiedName ","}+ variables> <Bound bound> ) <Statement body>`) { throw("solve"); }

list[MuExp] translate(s: (Statement) `try  <Statement body> <Catch+ handlers>`) { throw("try"); }

list[MuExp] translate(s: (Statement) `try <Statement body> <Catch+ handlers> finally <Statement finallyBody>`) { throw("tryFinally"); }

list[MuExp] translate(s: (Statement) `<Label label> { <Statement+ statements> }`) =
    [*translate(stat) | stat <- statements];

list[MuExp] translate(s: (Statement) `<Assignable assignable> <Assignment operator> <Statement statement>`) { 
  if(assignable is variable){
    var = assignable.qualifiedName;
    return [ mkAssign("<var>", var@\loc, translate(statement)[0]) ];
  } else 
	throw("assignment"); 
}

list[MuExp] translate(s: (Statement) `;`) = [];

list[MuExp] translate(s: (Statement) `global <Type \type> <{QualifiedName ","}+ names> ;`) { throw("globalDirective"); }

list[MuExp] translate(s: (Statement) `return <Statement statement>`) {
  t = translate(statement);
  return [size(t) > 0 ? muReturn(t[0]) : muReturn()];
}

list[MuExp] translate(s: (Statement) `throw <Statement statement>`) { throw("throw"); }

list[MuExp] translate(s: (Statement) `insert <DataTarget dataTarget> <Statement statement>`) { throw("insert"); }

list[MuExp] translate(s: (Statement) `append <DataTarget dataTarget> <Statement statement>`) { throw("append"); }

list[MuExp] translate(s: (Statement) `<FunctionDeclaration functionDeclaration>`) { translate(functionDeclaration); return []; } // we assume globally unique function names for now

list[MuExp] translate(s: (Statement) `<LocalVariableDeclaration declaration> ;`) { 
    tp = declaration.declarator.\type;
    variables = declaration.declarator.variables;
    
    return for(var <- variables){
    			if(var is initialized)
    				append mkAssign("<var.name>", var.name@\loc, translate(var.initial)[0]);
           }
}

/*********************************************************************/
/*                  End of Statements                                */
/*********************************************************************/
