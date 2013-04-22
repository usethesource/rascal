module experiments::m3::Core

data M3 = m3(loc project, 
             rel[Definition from, Definition to] containment 
             rel[Definition from, Definition to] inheritance = {},
             rel[Definition from, Definition to] access = {},
             rel[Definition from, Definition to] reference = {},
             rel[Definition from, Definition to] imports = {},
             map[Definition definition, Type typ] types = (),
             map[Definition definition, Definition comments] documentation = (),
             rel[Definition definition, Modifier modifiers] modifiers = {}
          );

/* Java Model */
data Definition 
  = compilationunit(loc src)
  | annotationinstance(loc src, loc name)
  | annotationinstanceattribute(loc src, loc name)
  | annotationtypeattribute(loc src, loc name)
  | method(loc src, loc name)
  | package(loc src, loc name)
  | annotationtype(loc src, loc name)
  | class(loc src, loc name)
  | interface(loc src, loc name)
  | attribute(loc src, loc name)
  | localvariable(loc src, loc name)
  | parameter(loc src, loc name)
  | comments(loc src, str content)
  ;
					
/* Extensions for other OO languages */
data Definition
  = function(loc src, loc name)
  | globalvariable(loc src, loc name)
  | namespace(loc src, loc name)
  | typealias(loc src, loc name)
  ;
  
data Modifier
  = \private()
  | \public()
  | \protected()
  | \friendly()
  | \static()
  | \final()
  | \synchronized()
  | \transient()
  ;

data Type
  = primitive(str name)
  | defined(Definition elem) // ??
  ;

data AST = none();

data AST = compilationUnit(Option[AST] package, list[AST] imports, list[AST] typeDeclarations)
		
				// Declarations
				| anonymousClassDeclaration(list[AST] bodyDeclarations)
				| annotationTypeDeclaration(list[Modifier] modifiers, list[AST] annotations, str name, list[AST] bodyDeclarations)
				| annotationTypeMemberDeclaration(list[Modifier] modifiers, list[AST] annotations, AST typeArgument, str name, Option[AST] defaultBlock)
				| enumDeclaration(list[Modifier] modifiers, list[AST] annotations, str name, list[AST] implements, list[AST] enumConstants, list[AST] bodyDeclarations)
				| enumConstantDeclaration(list[Modifier] modifiers, list[AST] annotations, str name, list[AST] arguments, Option[AST] anonymousClassDeclaration)
				| typeDeclaration(list[Modifier] modifiers, list[AST] annotations, str objectType, str name, list[AST] genericTypes, Option[AST] extends, list[AST] implements, list[AST] bodyDeclarations)
				| fieldDeclaration(list[Modifier] modifiers, list[AST] annotations, AST \type, list[AST] fragments)
				| initializer(list[Modifier] modifiers, list[AST] annotations, AST body)
				| methodDeclaration(list[Modifier] modifiers, list[AST] annotations, list[AST] genericTypes, Option[AST] returnType, str name, list[AST] parameters, list[AST] possibleExceptions, Option[AST] implementation)
				| importDeclaration(str name, bool staticImport, bool onDemand)
				| packageDeclaration(str name, list[AST] annotations)
				| singleVariableDeclaration(str name, list[Modifier] modifiers, list[AST] annotations, AST \type, Option[AST] initializer, bool isVarargs)
				| variableDeclarationFragment(str name, Option[AST] initializer)
				| typeParameter(str name, list[AST] extendsList)

				// Expressions
				| markerAnnotation(str typeName)
				| normalAnnotation(str typeName, list[AST] memberValuePairs)
				| memberValuePair(str name, AST \value)				
				| singleMemberAnnotation(str typeName, AST \value)
				| arrayAccess(AST array, AST index)
				| arrayCreation(AST \type, list[AST] dimensions, Option[AST] initializer)
				| arrayInitializer(list[AST] expressions)
				| assignment(AST leftSide, AST rightSide)
				| booleanLiteral(bool boolValue)
				| castExpression(AST \type, AST expression)
				| characterLiteral(str charValue)
				| classInstanceCreation(Option[AST] optionalExpression, AST \type, list[AST] genericTypes, list[AST] typedArguments, Option[AST] anonymousClassDeclaration)
				| conditionalExpression(AST expression, AST thenBranch, AST elseBranch)
				| fieldAccess(AST expression, str name set[Definition] binding = {})
				| infixExpression(str operator, AST leftSide, AST rightSide, list[AST] extendedOperands)
				| instanceofExpression(AST leftSide, AST rightSide)
				| methodInvocation(Option[AST] optionalExpression, list[AST] genericTypes, str name, list[AST] typedArguments)
				| superMethodInvocation(Option[AST] optionalQualifier, list[AST] genericTypes, str name, list[AST] typedArguments)
				| qualifiedName(AST qualifier, str name)
				| simpleName(str simpleName)
				| nullLiteral()
				| numberLiteral(str number)
				| parenthesizedExpression(AST expression)
				| postfixExpression(AST operand, str operator)
				| prefixExpression(AST operand, str operator)
				| stringLiteral(str stringValue)
				| superFieldAccess(Option[AST] optionalQualifier, str name)
				| thisExpression(Option[AST] optionalQualifier)
				| typeLiteral(AST \type)
				| variableDeclarationExpression(list[Modifier] modifiers, list[AST] annotations, AST \type, list[AST] fragments)
						
				// Statements
				| assertStatement(AST expression, Option[AST] message)
				| block(list[AST] statements)
				| breakStatement(Option[str] label)
				| constructorInvocation(list[AST] genericTypes, list[AST] typedArguments)
				| superConstructorInvocation(Option[AST] optionalExpression, list[AST] genericTypes, list[AST] typedArguments)
				| continueStatement(Option[str] label)
				| doStatement(AST body, AST whileExpression)
				| emptyStatement()
				| enhancedForStatement(AST parameter, AST collectionExpression, AST body)
				| expressionStatement(AST expression)
				| forStatement(list[AST] initializers, Option[AST] optionalBooleanExpression, list[AST] updaters, AST body)
				| ifStatement(AST booleanExpression, AST thenStatement, Option[AST] elseStatement)
				| labeledStatement(str name, AST body)
				| returnStatement(Option[AST] optionalExpression)
				| switchStatement(AST expression, list[AST] statements)
				| switchCase(bool isDefault, Option[AST] optionalExpression)
				| synchronizedStatement(AST expression, AST body)
				| throwStatement(AST expression)
				| tryStatement(AST body, list[AST] catchClauses, Option[AST] \finally)										
				| catchClause(AST exception, AST body)
				| typeDeclarationStatement(AST typeDeclaration)
				| variableDeclarationStatement(list[Modifier] modifiers, list[AST] annotations, AST \type, list[AST] fragments)
				| whileStatement(AST expression, AST body)
							
				// Types
				| arrayType(AST \typeOfArray)
				| parameterizedType(AST \typeOfParam, list[AST] genericTypes)
				| qualifiedType(AST qualifier, str name)
				| primitiveType(PrimitiveType primitive)
				| simpleType(str name)
				| unionType(list[AST] types)
				| wildcardType(Option[AST] bound, Option[str] lowerOrUpper)
																			
				// Comments 
				| blockComment()
				| lineComment()

				// Javadoc
				| javadoc()
				| tagElement()
				| textElement()
				| memberRef()
				| memberRefParameter()
				;
				
data Option[&T] = some(&T opt)
				| none()
				;
