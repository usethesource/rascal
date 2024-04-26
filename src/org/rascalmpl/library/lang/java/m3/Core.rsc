@synopsis{Extends the M3 Core model with Java specific concepts such as inheritance and overriding.}
module lang::java::m3::Core

extend lang::java::m3::TypeSymbol;
import lang::java::m3::AST;

extend analysis::m3::Core;

import analysis::graphs::Graph;
import analysis::m3::Registry;

import IO;
import String;
import Relation;
import Set;
import List;

import util::FileSystem;
import util::Reflective;

@synopsis{Java extensions to the generic M3 model.}
@description{
Notice that this model also contains the core attributes from ((Library:analysis::m3::Core::M3));
in particular `containment`, `declarations`, `modifiers`, `uses`, `types`, and `messages`
are _hot_ for the Java M3 model.

The additional relations represent specifically static semantic links from the object-oriented
programming paradigm that Java belongs to. However, this only contains facts extracted directly
from source code. The actual static semantic interpretation (type hierarchies, call graphs)
requires an additional analysis step with its own design choices.

| Ground truth fact kind about source code           | Description |                
| -------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `rel[loc from, loc to] extends`                    | captures class and interface inheritance, classes extend classes and interfaces extend interfaces. Implicit inheritance (i.e. from java.lang.Object) is _not_ included. |
| `rel[loc from, loc to] implements`                 | which class implements which interfaces (directly, transitive implementation via de extends relation must be derived)
| `rel[loc from, loc to] methodInvocation`           | which method potentially invokes which (virtual) method. For a call graph this must be composed with `methodOverrides` |
| `rel[loc from, loc to] fieldAccess`                | which method (or static block or field initializer) accesss which fields from which classes |
| `rel[loc from, loc to] typeDependency`             | uses of types (literally!) in methods, static blocks and field initializers. |
| `rel[loc from, loc to] methodOverrides`            | captures which methods override which other methods from their parents in the inheritance/implements hierarchy. Useful for approximating call graphs. |
| `rel[loc from, loc to] annotations`                | logs which declarations (classes, interfaces, parameters, methods, variables, etc.) are tagged with which annotation classes or interfaces |

These are the kinds of logical names that can be found in a Java M3 model:
* `|java+class:///|` is a fully resolved and qualified class name
* `|java+interface:///|` is a fully resolved and qualified class name
* (((TODO)))
}
data M3(
	rel[loc from, loc to] extends = {},            
	rel[loc from, loc to] implements = {},         
	rel[loc from, loc to] methodInvocation = {},   
	rel[loc from, loc to] fieldAccess = {},        
	rel[loc from, loc to] typeDependency = {},     
	rel[loc from, loc to] methodOverrides = {},
  rel[loc from, loc to] annotations = {}
);

@synopsis{Extensions for modelling the Java module system}
@description{
The Java module system was introduced with Java 9. A "module" is a group of related
packages, what is typically called a "component" in general programming terms. A module
definition explains:
* what other modules the current module depends on: `moduleRequiresModule`
* what packages are open to reflection by other modules: `moduleOpensPackage`
* what interfaces are available to other modules (the rest is unavailable by default): `moduleExportsInterface`
* the "services" it uses from other modules: `moduleUsesInterface`
* the "services" it offers to other modules: `moduleProvidesImplementation`

And so each of these aspects has their own set of facts in the extended M3 model for Java:
| Facts about Java modules                             | Description                                             |
| ---------------------------------------------------- | ------------------------------------------------------- |
| `rel[loc \module, loc package, loc to] moduleOpensPackage` | what packages are open for reflection in a given module |
| `rel[loc \module, loc service, loc implementation] moduleProvidesImplementation` | what services are implemented by this module            |
| `rel[loc \module, loc requiredModule] moduleRequiresModule`         | which modules each module requires                      |
| `rel[loc \module, loc service] moduleUsesInterface`          | which services are used by every module                 |
| `rel[loc \module, loc service, loc to] moduleExportsInterface`       | which interfaces are exported by every module           |
}
@benefits{
* M3 models with the Module system extensions are composable to generate large queriable databases for entire software ecosystems.
* The same module definitions can be extracted from both bytecode (in jar files) and source code.
}
@pitfalls{
* modules, although they semantically encapsulate packages, interfaces and classes, do not appear in the `containment` relation of the core M3 model.
That is because `containment` represents the static scoping relation of declared source code elements. The relation between modules and what 
is inside them is yet another form of encapsulation; so to avoid conflating them they are stored in different relations.   
}
data M3(
  rel[loc \module, loc package, loc to] moduleOpensPackage = {},
  rel[loc \module, loc service, loc implementation] moduleProvidesImplementation = {},
  rel[loc \module, loc requiredModule]  moduleRequiresModule = {},
  rel[loc \module, loc service]         moduleUsesInterface = {},
  rel[loc \module, loc service, loc to] moduleExportsInterface = {}
);

@synopsis{Combines a set of Java meta models by merging their relations.}
@memo
M3 composeJavaM3(loc id, set[M3] models) = composeM3(id, models);

@synopsis{Returns the difference between the first model and the others.}
@description{
Combines `models[1..]` into a single model and then calculates
the difference between `model[0]` and this new joined model.

The `id` is the identifier for the returned model.
}
@memo
M3 diffJavaM3(loc id, list[M3] models) {
	// Diff the generic M3 relations first
	M3 diff = diffM3(id, models);

	M3 first = models[0];
	M3 others = composeJavaM3(id, toSet(models[1..]));

	// Then the Java-specific ones
	diff.extends = first.extends - others.extends;
	diff.implements = first.implements - others.implements;
	diff.methodInvocation = first.methodInvocation - others.methodInvocation;
	diff.fieldAccess = first.fieldAccess - others.fieldAccess;
	diff.typeDependency = first.typeDependency - others.typeDependency;
	diff.methodOverrides = first.methodOverrides - others.methodOverrides;
  diff.annotations = first.annotations - others.annotations;

	return diff;
}

@synopsis{Creates a M3 from a single files.}
@description{
  Identical to ((createM3sFromFiles)): `createM3sFromFiles({file})`.
}
M3 createM3FromFile(loc file, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], Language javaVersion = JLS13()) {
    result = createM3sFromFiles({file}, errorRecovery = errorRecovery, sourcePath = sourcePath, classPath = classPath, javaVersion = javaVersion);
    if ({oneResult} := result) {
        return oneResult;
    }
    throw "Unexpected number of M3s returned for <file>";
}

@synopsis{For a set of Java files, generates matching M3s.}
@description{
  Each M3 has the `id` filled with a matching location from `files`.
}
@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
java set[M3] createM3sFromFiles(set[loc] files, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], Language javaVersion = JLS13());

@synopsis{For a set of Java files, creates a composed M3.}
@description{
  While ((createM3sFromFiles)) leaves the M3s separated, this function composes them into a single model.
}
M3 createM3FromFiles(loc projectName, set[loc] files, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], Language javaVersion = JLS13())
    = composeJavaM3(projectName, createM3sFromFiles(files, errorRecovery = errorRecovery, sourcePath = sourcePath, classPath = classPath, javaVersion = javaVersion));

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
java tuple[set[M3], set[Declaration]] createM3sAndAstsFromFiles(set[loc] files, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], Language javaVersion = JLS13());

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
java M3 createM3FromString(loc fileName, str contents, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], Language javaVersion = JLS13());

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
java M3 createM3FromJarClass(loc jarClass, list[loc] classPath = []);

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
java M3 createM3FromSingleClass(loc jarClass, str className, list[loc] classPath = []);

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
java M3 createM3FromJarFile(loc jarLoc, list[loc] classPath = []);

@synopsis{Globs for jars, class files and java files in a directory and tries to compile all source files into an M3 model}
M3 createM3FromDirectory(loc project, bool errorRecovery = false, bool includeJarModels=false, Language javaVersion = JLS13(), list[loc] classPath = []) {
    if (!(isDirectory(project))) {
      throw "<project> is not a valid directory";
    }

    list[loc] classPaths = classPath;
    set[str] seen = {};
    for (j <- find(project, "jar"), isFile(j)) {
        hash = md5HashFile(j);
        if (!(hash in seen)) {
            classPaths += j;
            seen += hash;
        }
    }
    sourcePaths = getPaths(project, "java");
    M3 result = composeJavaM3(project, createM3sFromFiles({p | sp <- sourcePaths, p <- find(sp, "java"), isFile(p)}, errorRecovery = errorRecovery, sourcePath = [*findRoots(sourcePaths)], classPath = classPaths, javaVersion = javaVersion));

    registerProject(project, result);
    
    if (includeJarModels) {
      results = composeJavaM3(project, {result, *{createM3FromJar(j, classPath=classPaths) |  j <- classPaths}});
    }
    
    return result;
}

@synopsis{Globs for jars, class files and java files in a directory and tries to compile all source files into an M3 model}
M3 createM3FromMavenProject(loc project, bool errorRecovery = false, bool includeJarModels=false, Language javaVersion = JLS13()) {
    if (!exists(project + "pom.xml")) {
      throw IO("pom.xml not found");
    }

    if (!(isDirectory(project))) {
      throw "<project> is not a valid directory";
    }

    list[loc] classPaths = getProjectPathConfig(project).javaCompilerPath;

    sourcePaths = getPaths(project, "java");
    M3 result = composeJavaM3(project, createM3sFromFiles({p | sp <- sourcePaths, p <- find(sp, "java"), isFile(p)}, errorRecovery = errorRecovery, sourcePath = [*findRoots(sourcePaths)], classPath = classPaths, javaVersion = javaVersion));

    registerProject(project, result);
    
    if (includeJarModels) {
      results = composeJavaM3(project, {result, *{createM3FromJar(j, classPath=classPaths) |  j <- classPaths}});
    }
    
    return result;
}

@synopsis{Extract an M3 model from all the class files in a jar}
@description{
We use ((createM3FromJar)) to extract an initial M3 model and 
then a number of steps enrich the M3 towards a model that could
have come from the original source. 

In particular:
* `typeDependency` is enriched by adding `extends` and `implements`
* `methodOverrides` is recovered from `extends` and `implements`, but restricted to the actual overriden methods.
}
M3 createM3FromJar(loc jarFile, list[loc] classPath = []) {
  M3 model = createM3FromJarFile(jarFile, classPath = classPath);
    
  rel[loc,loc] dependsOn = model.extends + model.implements;
  model.typeDependency = model.typeDependency + dependsOn;
	
	rel[loc from, loc to] candidates = rangeR((model.implements + model.extends), classes(model));
	containment = domainR(model.containment, candidates.from) + domainR(model.containment, candidates.to);
	methodContainment = {<c,m> | <c,m> <- containment, isMethod(m)};
	
	for(<from,to> <- candidates) {
		model.methodOverrides += {<m, m.file> | m <- methodContainment[from]} 
			o {<m.file, m> | m <- methodContainment[to]};
	}

	return model;
}

void unregisterJavaProject(loc project) {
  unregisterProjectSchemes(project, {"java+compilationUnit", "java+compilationUnit", "java+class", "java+constructor", "java+initializer", "java+parameter","java+variable","java+field" , "java+interface" , "java+enum", "java+class" , "java+interface","java+enum"});
}

@deprecated{
  Use `.file` on a location instead, i.e. `someLocation.file`.
}
str getMethodSignature(loc method)
	= substring(method.path, findLast(method.path,"/") + 1);

@synopsis{Checks if the logical name of the `entity` is a compilation unit.}
@description{
  A compilation unit is equivalent to a `.java` file in Java.
}
bool isCompilationUnit(loc entity) = entity.scheme == "java+compilationUnit";

@synopsis{Checks if the logical name of the `entity` is a package.}
bool isPackage(loc entity) = entity.scheme == "java+package";

@synopsis{Checks if the logical name of the `entity` is a class.}
bool isClass(loc entity) = entity.scheme == "java+class";

@synopsis{Checks if the logical name of the `entity` is a constructor.}
bool isConstructor(loc entity) = entity.scheme == "java+constructor";

@synopsis{Checks if the logical name of the `entity` is a method.}
@description{
  Constructors and initializers are also considered methods here.
}
@pitfalls{
  If `isConstructor(entity)`, then also `isMethod(entity)`.
  Note that the opposite is not true.
}
bool isMethod(loc entity) = entity.scheme == "java+method" || entity.scheme == "java+constructor" || entity.scheme == "java+initializer";

@synopsis{Checks if the logical name of the `entity` is a parameter.}
bool isParameter(loc entity) = entity.scheme == "java+parameter";

@synopsis{Checks if the logical name of the `entity` is a variable.}
bool isVariable(loc entity) = entity.scheme == "java+variable";

@synopsis{Checks if the logical name of the `entity` is a field.}
bool isField(loc entity) = entity.scheme == "java+field";

@synopsis{Checks if the logical name of the `entity` is an interface.}
bool isInterface(loc entity) = entity.scheme == "java+interface";

@synopsis{Checks if the logical name of the `entity` is an enum.}
bool isEnum(loc entity) = entity.scheme == "java+enum";

@synopsis{Checks if the logical name of the `entity` is a type.}
@description{
  A type is considered to be a class, an interface, or an enum.
}
@pitfalls{
  If `isClass(entity)`, then also `isType(entity)`.
  If `isInterface(entity)`, then also `isType(entity)`.
  If `isEnum(entity)`, then also `isType(entity)`.

  Note that the opposite is not true.
}
bool isType(loc entity) = entity.scheme == "java+class" || entity.scheme == "java+interface" || entity.scheme == "java+enum";

@synopsis{Extracts all fields that are contained in `parent`.}
set[loc] files(rel[loc, loc] containment)
  = {e.lhs | tuple[loc lhs, loc rhs] e <- containment, isCompilationUnit(e.lhs)};

rel[loc, loc] declaredMethods(M3 m, set[Modifier] checkModifiers = {}) {
    declaredTypes = types(m);
    modifiersMap = toMap(m.modifiers);

    return {e | tuple[loc lhs, loc rhs] e <- domainR(m.containment, declaredTypes), isMethod(e.rhs), checkModifiers <= (modifiersMap[e.rhs]? ? modifiersMap[e.rhs] : {}) };
}

rel[loc, loc] declaredFields(M3 m, set[Modifier] checkModifiers = {}) {
    declaredTypes = types(m);
    modifiersMap = toMap(m.modifiers);

	return {e | tuple[loc lhs, loc rhs] e <- domainR(m.containment, declaredTypes), isField(e.rhs), checkModifiers <= (modifiersMap[e.rhs]? ? modifiersMap[e.rhs] : {}) };
}

rel[loc, loc] declaredFieldsX(M3 m, set[Modifier] checkModifiers = {}) {
    declaredTypes = types(m);
    modifiersMap = toMap(m.modifiers);

    return {e | tuple[loc lhs, loc rhs] e <- domainR(m.containment, declaredTypes), isField(e.rhs), isEmpty(checkModifiers & (modifiersMap[e.rhs]? ? modifiersMap[e.rhs] : {})) };
}

@synopsis{For all compilation units (left side), gets the types (right side).}
rel[loc, loc] declaredTopTypes(M3 m)
  = {e | tuple[loc lhs, loc rhs] e <- m.containment, isCompilationUnit(e.lhs), isType(e.rhs)};

rel[loc, loc] declaredSubTypes(M3 m)
  = {e | tuple[loc lhs, loc rhs] e <- m.containment, isClass(e.rhs)} - declaredTopTypes(m);


@synopsis{Extracts all classes (logical names) from an M3.}
@description{
  Caches the results in memory.
}
@memo
set[loc] classes(M3 m) =  {e | <e,_> <- m.declarations, isClass(e)};

@synopsis{Extracts all interfaces (logical names) from an M3.}
@description{
  Caches the results in memory.
}
@memo
set[loc] interfaces(M3 m) =  {e | <e,_> <- m.declarations, isInterface(e)};

@synopsis{Extracts all packages (logical names) from an M3.}
@description{
  Caches the results in memory.
}
@memo
set[loc] packages(M3 m) = {e | <e,_> <- m.declarations, isPackage(e)};

@synopsis{Extracts all variables (logical names) from an M3.}
@description{
  Caches the results in memory.
}
@memo
set[loc] variables(M3 m) = {e | <e,_> <- m.declarations, isVariable(e)};

@synopsis{Extracts all parameters (logical names) from an M3.}
@description{
  Caches the results in memory.
}
@memo
set[loc] parameters(M3 m)  = {e | <e,_> <- m.declarations, isParameter(e)};

@synopsis{Extracts all fields (logical names) from an M3.}
@description{
  Caches the results in memory.
}
@memo
set[loc] fields(M3 m) = {e | <e,_> <- m.declarations, isField(e)};

@synopsis{Extracts all methods (logical names) from an M3.}
@description{
  Caches the results in memory.
}
@memo
set[loc] methods(M3 m) = {e | <e,_> <- m.declarations, isMethod(e)};

@synopsis{Extracts all constructors (logical names) from an M3.}
@description{
  Caches the results in memory.
}
@memo
set[loc] constructors(M3 m) = {e | <e,_> <- m.declarations, isConstructor(e)};

@synopsis{Extracts all enums (logical names) from an M3.}
@description{
  Caches the results in memory.
}
@memo
set[loc] enums(M3 m) = {e | <e,_> <- m.declarations, isEnum(e)};

@synopsis{Extracts all types (logical names) from an M3.}
@description{
  Caches the results in memory.
}
@memo
set[loc] types(M3 m) = {e | <e,_> <- m.declarations, isType(e)};

@synopsis{Extracts all elements that are contained in `parent`.}
@description{
  See ((Library:analysis::m3::Core::M3)) `containment` for the definition of contains.
}
set[loc] elements(M3 m, loc parent) = m.containment[parent];


@synopsis{Extracts all fields that are contained in `class`.}
@description{
  Filtered version of ((elements)).
}
@memo
set[loc] fields(M3 m, loc class) = { e | e <- elements(m, class), isField(e) };

@synopsis{Extracts all methods that are contained in `class`.}
@description{
  Filtered version of ((elements)).
}
@memo
set[loc] methods(M3 m, loc class) = { e | e <- elements(m, class), isMethod(e) };

@synopsis{Extracts all constructors that are contained in `class`.}
@description{
  Filtered version of ((elements)).
}
@memo
set[loc] constructors(M3 m, loc class) = { e | e <- elements(m, class), isConstructor(e) };

@synopsis{Extracts all classes that are contained in `class`.}
@description{
  Filtered version of ((elements)).
}
@memo
set[loc] nestedClasses(M3 m, loc class) = { e | e <- elements(m, class), isClass(e) };
