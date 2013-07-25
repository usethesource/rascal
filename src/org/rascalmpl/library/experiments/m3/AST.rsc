module experiments::m3::AST 

anno loc Modifiers@binding;

data Modifiers
	= \private()
	| \public()
	| \protected()
	| \friendly()
	| \static()
	| \final()
	| \synchronized()
	| \transient()
	| \abstract()
	| \native()
	| \volatile()
	| \strictfp()
	| \deprecated()
	| \markerAnnotation(str typeName)
  	| \normalAnnotation(str typeName, list[Modifiers] memberValuePairs)
  	| \memberValuePair(str name, Expression \value)				
  	| \singleMemberAnnotation(str typeName, Expression \value)
  	;

anno loc Declaration@src;
anno loc Declaration@binding;
anno list[Modifiers] Declaration@modifiers;
anno list[Declaration] Declaration@typeParameters;
anno list[str] Declaration@errors;

data Declaration
  	= \compilationUnit(list[Declaration] imports, list[Declaration] types)
	| \compilationUnit(Declaration package, list[Declaration] imports, list[Declaration] types)
	| \enum(str name, list[Type] implements, list[Declaration] constants, list[Declaration] body)
	| \enumConstant(str name, list[Expression] arguments, Declaration class)
	| \enumConstant(str name, list[Expression] arguments)
	| \class(str name, list[Type] extends, list[Type] implements, list[Declaration] body)
	| \class(list[Declaration] body)
	| \interface(str name, list[Type] extends, list[Type] implements, list[Declaration] body)
	| \field(Type \type, list[Expression] fragments)
	| \initializer(Statement initializerBody)
	| \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl)
	| \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions)
	| \method(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl)
	| \import(str name)
	| \staticImport(str name)
	| \package(str name)
	| \variables(Type \type, list[Expression] \variables)
	| \typeParameter(str name, list[Type] extendsList)
	| \annotationType(str name, list[Declaration] body)
	| \annotationTypeMember(Type \type, str name)
	| \annotationTypeMember(Type \type, str name, Expression defaultBlock)
	| \parameter(Type \type, str name, int extraDimensions)
	| \vararg(Type \type, str name)
	;

anno loc Expression@src;
anno loc Expression@binding;
anno list[Declaration] Expression@typeParameters;
	
data Expression 
	= \arrayAccess(Expression array, Expression index)
	| \newArray(Type \type, list[Expression] dimensions, Expression init)
	| \newArray(Type \type, list[Expression] dimensions)
	| \arrayInitializer(list[Expression] elements)
	| \assignment(Expression lhs, Expression rhs)
	| \cast(Type \type, Expression expression)
	| \char(str charValue)
	| \newObject(Expression expr, Type \type, list[Expression] args, Declaration class)
	| \newObject(Expression expr, Type \type, list[Expression] args)
	| \newObject(Type \type, list[Expression] args, Declaration class)
	| \newObject(Type \type, list[Expression] args)
	| \qualifier(Expression qualifier, Expression expression)
	| \conditional(Expression expression, Expression thenBranch, Expression elseBranch)
	| \fieldAccess(Expression expression, str name)
	| \fieldAccess(str name)
	| \instanceof(Expression leftSide, Type rightSide)
	| \call(str name, list[Expression] arguments)
	| \call(Expression receiver, str name, list[Expression] arguments)
	| \null()
	| \char(str charValue)
	| \number(str numberValue)
	| \boolean(bool boolValue)
	| \string(str stringValue)
	| \type(Type \type)
	| \variable(str name, int extraDimensions)
	| \variable(str name, int extraDimensions, Expression initializer)
	| \bracket(Expression expression)
	| \this()
	| \this(Expression thisExpression)
	| \super()
	| \declaration(Declaration decl)
	| \infix(Expression lhs, str operator, Expression rhs)
	| \postfix(Expression operand, str operator)
	| \prefix(str operator, Expression operand)
	| \simpleName(str name)
	| \qualifiedName(Expression qualified, str name)
	| \parenthesis(Expression expr)
	| \declaration(Declaration decl)
	;						
  
anno loc Statement@src;
anno loc Statement@binding;
anno list[Declaration] Statement@typeParameters;

data Statement				
	= \assert(Expression expression)
	| \assert(Expression expression, Expression message)
	| \block(list[Statement] statements)
	| \break()
	| \break(str label)
	| \continue()
	| \continue(str label)
	| \do(Statement body, Expression condition)
	| \empty()
	| \foreach(Declaration parameter, Expression collection, Statement body)
	| \for(list[Expression] initializers, Expression condition, list[Expression] updaters, Statement body)
	| \for(list[Expression] initializers, list[Expression] updaters, Statement body)
	| \if(Expression condition, Statement thenBranch)
	| \if(Expression condition, Statement thenBranch, Statement elseBranch)
	| \label(str name, Statement body)
	| \return(Expression expression)
	| \return()
	| \switch(Expression expression, list[Statement] statements)
	| \case(Expression expression)
	| \defaultCase()
	| \synchronized(Expression lock, Statement body)
	| \throw(Expression expression)
	| \try(Statement body, list[Statement] catchClauses)
	| \try(Statement body, list[Statement] catchClauses, Statement \finally)										
	| \catch(Declaration exception, Statement body)
	| \declaration(Declaration declaration)
	| \while(Expression condition, Statement body)
	| \expression(Expression stmt)
	| \superCall(Expression expr, list[Expression] arguments)
	| \superCall(list[Expression] arguments)
	| \thisCall(list[Expression] arguments)
	;			
  
anno loc Type@binding;				
anno list[Declaration] Type@typeParameters;

data Type 
	= arrayType(Type \type)
	| parameterizedType(Type \type)
	| qualifier(Type qualifier, Type \type)
	| simpleType(str name)
	| unionType(list[Type] types)
	| wildcard()
	| upperbound(Type \type)
	| lowerbound(Type \type)
	| \int()
	| short()
	| long()
	| float()
	| double()
	| char()
	| string()
	| byte()
	| \void()
	| \boolean()
	;
