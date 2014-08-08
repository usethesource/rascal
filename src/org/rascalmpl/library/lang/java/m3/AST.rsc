@doc{
Synopsis: defines AST node types for Java
}
module lang::java::m3::AST

extend analysis::m3::AST;
import util::FileSystem;
import lang::java::m3::TypeSymbol;
import IO;
import Set;
import String;
 
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
    | \constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl)
    | \import(str name)
    | \package(str name)
    | \package(Declaration parentPackage, str name)
    | \variables(Type \type, list[Expression] \fragments)
    | \typeParameter(str name, list[Type] extendsList)
    | \annotationType(str name, list[Declaration] body)
    | \annotationTypeMember(Type \type, str name)
    | \annotationTypeMember(Type \type, str name, Expression defaultBlock)
    // initializers missing in parameter, is it needed in vararg?
    | \parameter(Type \type, str name, int extraDimensions)
    | \vararg(Type \type, str name)
    ;


data Expression 
    = \arrayAccess(Expression array, Expression index)
    | \newArray(Type \type, list[Expression] dimensions, Expression init)
    | \newArray(Type \type, list[Expression] dimensions)
    | \arrayInitializer(list[Expression] elements)
    | \assignment(Expression lhs, str operator, Expression rhs)
    | \cast(Type \type, Expression expression)
    | \characterLiteral(str charValue)
    | \newObject(Expression expr, Type \type, list[Expression] args, Declaration class)
    | \newObject(Expression expr, Type \type, list[Expression] args)
    | \newObject(Type \type, list[Expression] args, Declaration class)
    | \newObject(Type \type, list[Expression] args)
    | \qualifiedName(Expression qualifier, Expression expression)
    | \conditional(Expression expression, Expression thenBranch, Expression elseBranch)
    | \fieldAccess(bool isSuper, Expression expression, str name)
    | \fieldAccess(bool isSuper, str name)
    | \instanceof(Expression leftSide, Type rightSide)
    | \methodCall(bool isSuper, str name, list[Expression] arguments)
    | \methodCall(bool isSuper, Expression receiver, str name, list[Expression] arguments)
    | \null()
    | \number(str numberValue)
    | \booleanLiteral(bool boolValue)
    | \stringLiteral(str stringValue)
    | \type(Type \type)
    | \variable(str name, int extraDimensions)
    | \variable(str name, int extraDimensions, Expression \initializer)
    | \bracket(Expression expression)
    | \this()
    | \this(Expression thisExpression)
    | \super()
    | \declarationExpression(Declaration decl)
    | \infix(Expression lhs, str operator, Expression rhs)
    | \postfix(Expression operand, str operator)
    | \prefix(str operator, Expression operand)
    | \simpleName(str name)
    | \markerAnnotation(str typeName)
    | \normalAnnotation(str typeName, list[Expression] memberValuePairs)
    | \memberValuePair(str name, Expression \value)             
    | \singleMemberAnnotation(str typeName, Expression \value)
    ;                       
  
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
    | \synchronizedStatement(Expression lock, Statement body)
    | \throw(Expression expression)
    | \try(Statement body, list[Statement] catchClauses)
    | \try(Statement body, list[Statement] catchClauses, Statement \finally)                                        
    | \catch(Declaration exception, Statement body)
    | \declarationStatement(Declaration declaration)
    | \while(Expression condition, Statement body)
    | \expressionStatement(Expression stmt)
    | \constructorCall(bool isSuper, Expression expr, list[Expression] arguments)
    | \constructorCall(bool isSuper, list[Expression] arguments)
    ;           
  
data Type 
    = arrayType(Type \type)
    | parameterizedType(Type \type)
    | qualifiedType(Type qualifier, Expression simpleName)
    | simpleType(Expression name)
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
 
data Modifier
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
    | \annotation(Expression \anno)
    | \onDemand()
    ;

@memo
set[loc] getPaths(loc dir, str suffix) { 
   bool containsFile(loc d) = isDirectory(d) ? (x <- d.ls && x.extension == suffix) : false;
   return find(dir, containsFile);
}

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
@reflect
java void setEnvironmentOptions(set[loc] classPathEntries, set[loc] sourcePathEntries);

@memo
set[loc] findRoots(loc project, set[loc] allPaths) {
  set[loc] result = {};
  while (!isEmpty(allPaths)) {
    loc oneLoc = getOneFrom(allPaths);
    allPaths -= oneLoc;
    loc parent = project + replaceLast(oneLoc.path, "/" + oneLoc.file, "");
    if (parent != project && parent != project+"/") {
      allPaths += parent;
    } else {
      result += oneLoc;
    }
  }
  return result;
}
      
@doc{
Synopsis: Creates AST from a file

Description: useful for analyzing raw source code on disk, but if you have an Eclipse project you should have a look at [lang/java/jdt/m3] instead.
}
@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
@reflect
public java Declaration createAstFromFile(loc file, bool collectBindings, str javaVersion = "1.7");

@doc{
  Creates ASTs from an input string
}
@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
@reflect
public java Declaration createAstFromString(loc fileName, str source, bool collectBinding, str javaVersion = "1.7");

@doc{Creates ASTs from a project}
public set[Declaration] createAstsFromDirectory(loc project, bool collectBindings, str javaVersion = "1.7" ) {
   classPaths = getPaths(project, "class") + find(project, "jar");
   sourcePaths = getPaths(project, "java");
   setEnvironmentOptions(classPaths, findRoots(project, sourcePaths));
   return { createAstFromFile(f, collectBindings, javaVersion = javaVersion) | sp <- sourcePaths, loc f <- find(sp, "java") };
}
