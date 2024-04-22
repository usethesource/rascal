@synopsis{AST node declarations for Java}
@description{
It helps to start reading in ((analysis::m3::AST)) to find out what we use to model abstract syntax trees 
in Rascal, namely algebraic data types with specific properties.

The "M3" label stands for a standardized set of names of types and their fields that are used similarly
for different programming languages.

This M3 AST model of Java features:
* For Java the model below contains Declarations, Statements, Expressions, Types and Modifiers. The abstract grammar
below describes an _over approximation_ of the abstract syntax of Java. This means that you could construct
more kinds of syntax trees programmatically than there are stictly exist Java sentences. It also means that every
Java program in existence can be mapped to this simplified tree format, for downstream analysis.
* Java 1 to 13 support
* Name analysis, where every definition of a name and every use of a name are annotated with a fully qualified logical source location, e.g. `decl=|java+interface:///java/util/List<T>`
* Type analysis, where every definition of a type and every expression that produces a type is annotated with `typ=TypeSymbol`
* Annotations, all available in the syntax tree.

For a more global overview, a database, of what is declarared in Java and what related to what, see the ((lang::java::m3::Core)) model.
There you will also find fact extractors from bytecode and jar files with .class files in them.
}
@benefits{
* Every AST modelled using M3-style is usually recognizable, even if you are an expert in a different language;
* **HiFi**: This Java AST format is _complete_ and completely informative about Java. For every language construct in existence
there is a node in the tree. Also every node has a `src` attribute to point at the exact location in the source file
where every node originated.
* You can use handy pattern matching primitives like constructor matching, list matching and deep matching for fast analysis.
* `src` and `decl` fields on AST nodes correspond to the M3 Core model's `declarations` and `uses` relations, and others. Combining
AST analysis with lookups in an M3 core model is usually very handy.
* One AST format for all kinds of Java versions.
}
@pitfalls{
* Confusing the AST type for `Type` syntax with the symbolic representation of types in M3: `TypeSymbol`.
* Writing algorithms that "should" work for any programming language: **don't do it**. Although Rascal M3 ASTs are a _uniform_ format for abstract Syntax
trees, they are _not_ a _unified_ abstract syntax tree formalism. In other words an `\if` statement could have a different semantic in
one language than in another. Frequently this is the case. AST nodes have the same name (between different programming languages) if they have the same 
general intention, but their _semantics_ is typically different.
* Abstracting from abstract syntax. (Abstract) Syntax is the bread and butter of (static) code analysis algorithms. If you
introduce functional or object-oriented abstraction layers to hide this intrinsic complexity, the entire algorithm becomes harder to understand
and harder to maintain. 
   * It's almost always best to _repeat syntactic constructs_ in patterns for pattern matching, and to repeat cases
several times in different contexts, than to introduce ``reusable''
boolean predicates yourself. 
   * Such reuse is typically accidentally possible and not intrinsic to the language or the algorithm. Rascal will also help with maintenance if the constructors change over time, by providing warnings and errors.
   * If find yourself writing many case distinctions over and over again, it's time to consider using or introducing a new intermediate language like `TypeSymbol`.
* AST instances for older version of Java may contain empty list nodes in locations where a feature was added later (say type parameters of generics).
Analysis algorithms must ignore those values, and probably should know which version they are analysing for. Example:
   * Before Java 6 there were no generics and `List` with an empty list of non-existent type parameters just means the list type.
   * After Java 6 there were generics and now `List` with an empty list of type-parameters means the "raw type" for List.
   * Conclusion: Type compatibility rules are subtly different, while the abstract syntax for both instances is the same.
   * Just like between programming languages, between programming language versions: just because two things look the same, 
     does not mean they mean the same thing.
}
module lang::java::m3::AST

extend analysis::m3::AST;
extend analysis::m3::Core; // NB! contains necessary declarations for initializing EclipseJavaCompiler object even if unused here
extend lang::java::m3::TypeSymbol; // NB! necessary for initializing EclipseJavaCompiler object even if unused here

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

@synopsis{All kind of declarations in Java}
data Declaration
    = \compilationUnit(list[Declaration] imports, list[Declaration] types)
    | \compilationUnit(Declaration \module)
    | \compilationUnit(Declaration package, list[Declaration] imports, list[Declaration] types)
    | \enum(list[Modifier] modifiers, Expression name, list[Type] implements, list[Declaration] constants, list[Declaration] body)
    | \enumConstant(list[Modifier] modifiers, Expression name, list[Expression] arguments, Declaration class)
    | \enumConstant(list[Modifier] modifiers, Expression name, list[Expression] arguments)
    | \class(list[Modifier] modifiers, Expression name, list[Type] typeParameters, list[Type] extends, list[Type] implements, list[Declaration] body)
    | \class(list[Modifier] modifiers, list[Declaration] body)
    | \interface(list[Modifier] modifiers, Expression name, list[Type] typeParameters, list[Type] extends, list[Type] implements, list[Declaration] body)
    | \field(list[Modifier] modifiers, Type \type, list[Expression] fragments)
    | \initializer(list[Modifier] modifiers, Statement initializerBody)
    | \method(list[Modifier] modifiers, list[Type] typeParameters, Type \return, Expression name, list[Declaration] parameters, list[Expression] exceptions, Statement impl)
    | \method(list[Modifier] modifiers, list[Type] typeParameters, Type \return, Expression name, list[Declaration] parameters, list[Expression] exceptions)
    | \constructor(list[Modifier] modifiers, Expression name, list[Declaration] parameters, list[Expression] exceptions, Statement impl)
    | \import(Expression name)
    | \package(list[Modifier] modifiers, Expression name)
    | \variables(list[Modifier] modifiers, Type \type, list[Expression] \fragments)
    | \typeParameter(Expression name, list[Type] extendsList) // TODO: this seems unused at the moment, relation to wildcard, super/extends unknown
    | \annotationType(list[Modifier] modifiers, Expression name, list[Declaration] body)
    | \annotationTypeMember(list[Modifier] modifiers, Type \type, Expression name)
    | \annotationTypeMember(list[Modifier] modifiers, Type \type, Expression name, Expression defaultBlock)
    | \parameter(list[Modifier] modifiers, Type \type, Expression name, list[Declaration] dimensions)
    | \dimension(list[Modifier] annotations)
    | \vararg(list[Modifier] modifiers, Type \type, Expression name)
    ;

@synopsis{These declarations types are related to the Java 9 module system}
data Declaration
    = \module(list[Modifier] open, Expression \moduleName, list[Declaration] directives)
    | \opensPackage(Expression packageName, list[Expression] openedToModules)
    | \providesImplementations(Expression interface, list[Expression] implementations)
    | \requires(list[Modifier] mods, Expression \moduleName)
    | \uses(Expression interface)
    | \exports(Expression interface)
    ;


@synopsis{Java Expressions all have a `typ`.}
data Expression(TypeSymbol typ=\unresolved())
    = \arrayAccess(Expression array, Expression index)
    | \newArray(Type \type, list[Expression] dimensions, Expression init)
    | \newArray(Type \type, list[Expression] dimensions)
    | \arrayInitializer(list[Expression] elements)
    | \assignment(Expression lhs, str operator, Expression rhs)
    | \cast(Type \type, Expression expression)
    | \characterLiteral(str charValue)
    | \newObject(Expression expr, Type \type, list[Declaration] typeParameters, list[Expression] args, Declaration class)
    | \newObject(Expression expr, Type \type, list[Declaration] typeParameters, list[Expression] args)
    | \newObject(Type \type, list[Declaration] typeParameters, list[Expression] args, Declaration class)
    | \newObject(Type \type, list[Declaration] typeParameters, list[Expression] args)
    | \qualifiedName(Expression qualifier, Expression expression)
    | \conditional(Expression expression, Expression thenBranch, Expression elseBranch)
    | \fieldAccess(Expression expression, Expression name)
    | \superFieldAccess(Expression expression, Expression name)
    | \instanceof(Expression leftSide, Type rightSide)
    | \methodCall(Expression receiver, str identifier, list[Expression] arguments)
    | \superMethodCall(Expression receiver, str identifier, list[Expression] arguments)
    | \null()
    | \number(str numberValue)
    | \booleanLiteral(str boolValue)
    | \stringLiteral(str stringValue, str literal=stringValue)
    | \textBlock(str stringValue, str literal=stringValue)
    | \type(Type \type)
    | \variable(str identifier, list[Declaration] dimensionTypes) 
    | \variable(str identifier, list[Declaration] dimensionTypes, Expression \initializer) 
    | \bracket(Expression expression)
    | \this()
    | \this(Expression thisExpression)
    | \super()
    | \declarationExpression(Declaration declaration)
    | \infix(Expression lhs, str operator, Expression rhs)
    | \postfix(Expression operand, str operator)
    | \prefix(str operator, Expression operand)
    | \simpleName(str identifier)
    | \switch(Expression expression, list[Statement] cases)
    | \markerAnnotation(Expression name)
    | \methodReference(Type \type, list[Type] typeArguments, Expression name)
    | \methodReference(Expression expression, list[Type] typeArguments, Expression name)
    | \superMethodReference(list[Type] typeArguments, Expression name)
    | \normalAnnotation(Expression name, list[Expression] memberValuePairs)
    | \memberValuePair(Expression name, Expression \value)
    | \singleMemberAnnotation(str typeName, Expression \value)
    | \lambda(list[Declaration] parameters, Statement block)
    | \lambda(list[Declaration] parameters, Expression body)
    ;

@synopsis{These are the Statement types of Java}
data Statement
    = \assert(Expression expression)
    | \assert(Expression expression, Expression message)
    | \block(list[Statement] statements)
    | \break()
    | \break(Expression label)
    | \continue()
    | \continue(Expression label)
    | \do(Statement body, Expression condition)
    | \empty()
    | \foreach(Declaration parameter, Expression collection, Statement body)
    | \for(list[Expression] initializers, Expression condition, list[Expression] updaters, Statement body)
    | \for(list[Expression] initializers, list[Expression] updaters, Statement body)
    | \if(Expression condition, Statement thenBranch)
    | \if(Expression condition, Statement thenBranch, Statement elseBranch)
    | \label(str identifier, Statement body)
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
    | \constructorCall(Expression expr, list[Expression] arguments)
    | \constructorCall(list[Expression] arguments)
    | \superConstructorCall(Expression expr, list[Expression] arguments)
    | \superConstructorCall(list[Expression] arguments)
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
    | super(Type \type)
    | extends(Type \type)
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
