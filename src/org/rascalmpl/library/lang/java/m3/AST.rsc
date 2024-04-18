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

@synopsis{Datatype to configure the Java Language Standard compliance level of the parser.}
@description{
This is the Language data-type of core M3 that we use to document the language level, as well
as configure the JDK compiler before extracting the relevant facts.
}
data Language = \java(int level = 13, str version="13", bool preview=true);

Language JLS1()  = \java(level=1, version="1.1");
Language JLS2()  = \java(level=2, version="1.2");
Language JLS3()  = \java(level=3, version="1.3");
Language JLS4()  = \java(level=4, version="1.4");
Language JLS5()  = \java(level=5, version="1.5");
Language JLS6()  = \java(level=6, version="1.6");
Language JLS7()  = \java(level=7, version="1.7");
Language JLS8()  = \java(level=8, version="1.8");
Language JLS9()  = \java(level=9, version="9");
Language JLS10() = \java(level=10, version="10");
Language JLS11() = \java(level=11, version="11");
Language JLS12() = \java(level=12, version="12");
Language JLS13() = \java(level=13, version="13");

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

@synopsis{These declarations types are related to the Java 9 module system}
data Declaration
    = \module(list[Modifier] open, str name, list[Declaration] directives)
    | \opensPackage(str name, list[Expression] modules)
    | \providesImplementations(str name, list[Expression] implementations)
    | \requires(list[Modifier] mods, list[Expression] modules)
    | \uses(Expression interface)
    | \exports(Expression interface)
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
    | \stringLiteral(str stringValue, str literal=stringValue)
    | \textBlock(str stringValue, str literal=stringValue)
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
    | \switch(Expression expression, list[Statement] cases)
    | \markerAnnotation(str typeName)
    | \methodReference(Type \type, list[Type] typeArguments, str name)
    | \methodReference(Expression expression, list[Type] typeArguments, str name)
    | \superMethodReference(list[Type] typeArguments, str name)
    | \normalAnnotation(str typeName, list[Expression] memberValuePairs)
    | \memberValuePair(str name, Expression \value)
    | \singleMemberAnnotation(str typeName, Expression \value)
    | \lambda(list[Declaration] parameters, Statement block)
    | \lambda(list[Declaration] parameters, Expression body)
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
    | \case(list[Expression] expressions)
    | \caseRule(list[Expression] expressions)
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
    | \yield(Expression argument)
    ;

data Type
    = arrayType(Type \type)
    | parameterizedType(Type \type)
    | qualifiedType(Type qualifier, Expression simpleName)
    | simpleType(Expression typeName)
    | unionType(list[Type] types)
    | intersectionType(list[Type] types)
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
    | \open()  // for modules only
    | \transitive() // for module requirements only
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


@synopsis{Creates AST from a single file.}
@description{
Wrapper around ((createAstsFromFiles)) to call it on a single file.
}
public Declaration createAstFromFile(loc file, bool collectBindings, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], Language javaVersion = JLS13()) {
    result = createAstsFromFiles({file}, collectBindings, errorRecovery = errorRecovery, sourcePath = sourcePath, classPath = classPath, javaVersion = javaVersion);
    if ({oneResult} := result) {
        return oneResult;
    }
    throw "Unexpected number of ASTs returned from <file>";
}

@synopsis{Creates ASTs for a set of files using Eclipse JDT compiler.}
@pitfalls{
  While the function takes a set of locations, it ignores the positional information of the location.
  Meaning, that it analyzes the whole file and not just the part that the positional information describes.
}
@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
public java set[Declaration] createAstsFromFiles(set[loc] file, bool collectBindings, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], Language javaVersion = JLS13());

@synopsis{Creates AST from a string using Eclipse JDT compiler.}
@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
public java Declaration createAstFromString(loc fileName, str source, bool collectBinding, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], Language javaVersion = JLS13());

@synopsis{Creates a set ASTs for all Java source files in a project using Eclipse's JDT compiler}
@description{
  The function recursively looks for the `.java` files in the directory.
  The function also looks for the dependencies (`.jar` files) to include them.
  Wraps around ((createAstsFromFiles)).
}
public set[Declaration] createAstsFromDirectory(loc project, bool collectBindings, bool errorRecovery = false, Language javaVersion = JLS13() ) {
    if (!(isDirectory(project))) {
      throw "<project> is not a valid directory";
    }

    classPaths = [ j | j <- find(project, "jar"), isFile(j) ];
    sourcePaths = getPaths(project, "java");
    return createAstsFromFiles({ p | sp <- sourcePaths, p <- find(sp, "java"), isFile(p)}, collectBindings, sourcePath = [*findRoots(sourcePaths)], classPath = classPaths, errorRecovery = errorRecovery, javaVersion = javaVersion);
}

@synopsis{Creates a set of ASTs for all Java source files in a Maven project using Eclipse's JDT compiler.}
@description{
This function uses ((util::Reflective-getProjectPathConfig)), which inspects a `pom.xml` to 
compute the dependencies and concrete locations of jar files that a Maven project depends on.
The location of `project` points to the root of the project to analyze. As a consequence, the `pom.xml`
is expected to be at `project + "pom.xml"`.

Wraps around ((createAstsFromFiles)).
}
public set[Declaration] createAstsFromMavenProject(loc project, bool collectBindings, bool errorRecovery = false, Language javaVersion = JLS13() ) {
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
