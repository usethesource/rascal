@synopsis{AST node declarations for Java}
module lang::java::m3::AST

extend analysis::m3::AST;
extend analysis::m3::Core; // necessary for initializing EclipseJavaCompiler class
extend lang::java::m3::TypeSymbol; // necessary for initializing EclipseJavaCompiler class to express type annotations

import util::FileSystem;
import util::Reflective;
import IO;
import String;
import List;
 
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
    | \declarationExpression(Declaration declaration)
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
    | simpleType(Expression typeName)
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
    | \default()
    ;

@memo
set[loc] getPaths(loc dir, str suffix) { 
   bool containsFile(loc d) = isDirectory(d) ? (x <- d.ls && x.extension == suffix) : false;
   return find(dir, containsFile);
}

@memo
set[loc] findRoots(set[loc] folders) {
  set[loc] result = {};
  for (folder <- folders) {
    // only consult one java file per package tree
    top-down-break visit (crawl(folder)) {
      case directory(d, contents): {
        set[loc] roots = {};
        for (file(f) <- contents, toLowerCase(f.extension) == "java") {
          try {
            for (/package[ \t][ \t]*<p:[$0-9A-Z_a-z \t\.]*>;/ := readFile(f)) {
              packagedepth = size(split(".", trim(p)));
              roots += { d[path = intercalate("/", split("/", d.path)[..-packagedepth])] };
            }
            
            if (roots == {}) { // no package declaration means d is a root 
              roots += { d }; 
            }
            
            break;            
          } catch: ;          
        }
        
        if (roots != {}) {
          result += roots;
        }
        else {
          fail; // continue searching subdirectories
        }
      }
    }
  }
  
  return result;
}
      
@doc{
#### Synopsis

Creates AST from a file

#### Description

}
public Declaration createAstFromFile(loc file, bool collectBindings, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], str javaVersion = "1.7") {
    result = createAstsFromFiles({file}, collectBindings, errorRecovery = errorRecovery, sourcePath = sourcePath, classPath = classPath, javaVersion = javaVersion);
    if ({oneResult} := result) {
        return oneResult;
    }
    throw "Unexpected number of ASTs returned from <file>";
}

@synopsis{Creates AST from a file using Eclipse JDT compiler}
@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
public java set[Declaration] createAstsFromFiles(set[loc] file, bool collectBindings, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], str javaVersion = "1.7");

@synopsis{Creates AST from a string using Eclipse JDT compiler}
@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
public java Declaration createAstFromString(loc fileName, str source, bool collectBinding, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], str javaVersion = "1.7");

@doc{Creates a set ASTs for all Java source files in a project using Eclipse's JDT compiler}
public set[Declaration] createAstsFromDirectory(loc project, bool collectBindings, bool errorRecovery = false, str javaVersion = "1.7" ) {
    if (!(isDirectory(project))) {
      throw "<project> is not a valid directory";
    }
    
    classPaths = [ j | j <- find(project, "jar"), isFile(j) ];
    sourcePaths = getPaths(project, "java");
    return createAstsFromFiles({ p | sp <- sourcePaths, p <- find(sp, "java"), isFile(p)}, collectBindings, sourcePath = [*findRoots(sourcePaths)], classPath = classPaths, errorRecovery = errorRecovery, javaVersion = javaVersion);
}

@doc{Creates a set ASTs for all Java source files in a Maven project using Eclipse's JDT compiler}
@description{
This function uses ((util::Reflective-getProjectPathConfig)), which inspects a `pom.xml` to 
compute the dependencies and concrete locations of jar files that a Maven project depends on.
}
public set[Declaration] createAstsFromMavenProject(loc project, bool collectBindings, bool errorRecovery = false, str javaVersion = "1.7" ) {
    if (!exists(project + "pom.xml")) {
      throw IO("pom.xml not found");
    }

    if (!(isDirectory(project))) {
      throw "<project> is not a valid directory";
    }
    
    classPaths = getProjectPathConfig(project).javaCompilerPath;
    sourcePaths = getPaths(project, "java");
    return createAstsFromFiles({ p | sp <- sourcePaths, p <- find(sp, "java"), isFile(p)}, collectBindings, sourcePath = [*findRoots(sourcePaths)], classPath = classPaths, errorRecovery = errorRecovery, javaVersion = javaVersion);
}

