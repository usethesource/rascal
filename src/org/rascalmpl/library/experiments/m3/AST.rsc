module experiments::m3::AST

// This is an attempt to simplify the AST definitions, by removing use of Option, and by introducing some generic wrappers 
// (such as typeParameters, and modifiers)

anno loc Declaration@src;
anno loc Declaration@binding;

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

data Declaration 
  = \compilationUnit(list[Declaration] imports, list[Declaration] types)
  | \compilationUnit(Declaration package, list[Declaration] imports, list[Declaration] types)
  | \enum(str name, list[Type] implements, list[Declaration] constants, list[Declaration] body)
  | \enumConstant(str name, list[Declaration] arguments, Declaration class)
  | \enumConstant(str name, list[Declaration] arguments)
  | \class(str name, list[Type] extends, list[Type] implements, list[Declaration] body)
  | \class(list[Declaration] body)
  | \interface(str name, list[Type] extends, list[Type] implements)
  | \field(Type \type, list[Expression] fragments)
  | \initializer(list[Modifier] modifiers, Statement body)
  | \method(Type \return, str name, list[Declaration] parameters, list[Type] exceptions, Statement impl)
  | \abstractMethod(Type \return, str name, list[Declaration] parameters, list[Type] exceptions)
  | \import(str name)
  | \staticImport(str name)
  | \package(str name)
  | \vararg(str name, Type \type)
  | \variables(Type \type, list[Declaration] variables)
  | \variable(str name)
  | \variable(str name, Expression initializer)
  | \typeParameter(str name, list[Type] extendsList)
  | \typeParameters(list[Type] typeParameters, Declaration decl)
  | \modifiers(list[Modifier] modifiers, Declaration decl) 
  | \annotations(Declaration decl, list[Expression] annotations)
  | \annotationType(str name, list[Declaration] body)
  | \annotationTypeMember(Type typeArgument, str name)
  | \annotationTypeMember(Type typeArgument, str name, Statement defaultBlock)
  ;
	
anno loc Expression@src;
anno loc Expression@binding;
	
data Expression 
  = \markerAnnotation(str typeName)
  | \normalAnnotation(str typeName, list[Expression] memberValuePairs)
  | \memberValuePair(str name, Expression \value)				
  | \singleMemberAnnotation(str typeName, Expression \value)
  | \arrayAccess(Expression array, Expression index)
  | \newArray(Type \type, list[Expression] dimensions, Expression init)
  | \newArray(Type \type, list[Expression] dimensions)
  | \arrayInitializer(list[Expression] elements)
  | \assignment(Expression lhs, Expression rhs)
  | \cast(Type \type, Expression expression)
  | \char(str charValue)
  | \newObject(Type \type, list[Type] typeArgs, list[Expression] args, Declaration class)
  | \newObject(Type \type, list[Type] typeArgs, list[Expression] args)
  | \qualifier(Expression qualifier, Expression expression)
  | \conditional(Expression expression, Expression thenBranch, Expression elseBranch)
  | \fieldAccess(Expression expression, str name)
  | \instanceof(Expression leftSide, Type rightSide)
  | \call(list[Type] typeArguments, str name, list[Expression] arguments)
  | \call(Expression receiver, list[Type] typeArguments, str name, list[Expression] arguments)
  | \constructor(Type \type, list[Type] typeArguments, list[Expression] arguments)
  | \variable(str name)
  | \null()
  | \number(num numberValue)
  | \boolean(bool boolValue)
  | \string(str stringValue)
  | \type(Type \type)
  | \bracket(Expression expression)
  | \this()
  | \super()
  | \declaration(Declaration decl)
  | \infix(Expression lhs, str operator, Expression rhs)
  | \postfix(Expression operand, str operator)
  | \prefix(str operator, Expression operand)
  ;						

anno loc Statement@src;
				
data Statement				
  = \assert(Expression expression)
  | \assert(Expression expression, str message)
  | \block(list[Statement] statements)
  | \break()
  | \break(str label)
  | \continue()
  | \continue(str label)
  | \do(Statement body, Expression condition)
  | \empty()
  | \foreach(Declaration parameter, Expression collection, Statement body)
  | \for(list[Declaration] initializers, Expression condition, list[Statement] updaters, Statement body)
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
  | \catch(Type exception, Statement body)
  | \declaration(Declaration declaration)
  | \while(Expression condition, Statement body)
  ;							

anno loc Type@binding;			
data Type 
  = arrayType(Type \type)
  | parameterizedType(Type \type, list[Type] typeParameters)
  | qualifier(Type qualifier, Type \type)
  | simpleType(str name)
  | unionType(list[Type] types)
  | wildcard()
  | upperbound(Type \type, Type bound)
  | lowerbound(Type \type, Type bound)
  | int32()
  | int64()
  | long()
  | float()
  | double()
  | char()
  | string()
  | byte()
  | \void()
  ;																			
