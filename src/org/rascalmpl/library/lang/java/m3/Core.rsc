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

data M3(
	rel[loc from, loc to] extends = {},            // classes extending classes and interfaces extending interfaces
	rel[loc from, loc to] implements = {},         // classes implementing interfaces
	rel[loc from, loc to] methodInvocation = {},   // methods calling each other (including constructors)
	rel[loc from, loc to] fieldAccess = {},        // code using data (like fields)
	rel[loc from, loc to] typeDependency = {},     // using a type literal in some code (types of variables, annotations)
	rel[loc from, loc to] methodOverrides = {},    // which method override which other methods
	rel[loc declaration, loc annotation] annotations = {}
);

data Language(str version="") = java();

@memo
M3 composeJavaM3(loc id, set[M3] models) {
  // Compose the generic M3 relations first
  M3 comp = composeM3(id, models);

  // Then the Java-specific ones
  comp.extends = {*model.extends | model <- models};
  comp.implements = {*model.implements | model <- models};
  comp.methodInvocation = {*model.methodInvocation | model <- models};
  comp.fieldAccess = {*model.fieldAccess | model <- models};
  comp.typeDependency = {*model.typeDependency | model <- models};
  comp.methodOverrides = {*model.methodOverrides | model <- models};
  comp.annotations = {*model.annotations | model <- models};

  return comp;
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

M3 createM3FromFile(loc file, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], str javaVersion = "1.7") {
    result = createM3sFromFiles({file}, errorRecovery = errorRecovery, sourcePath = sourcePath, classPath = classPath, javaVersion = javaVersion);
    if ({oneResult} := result) {
        return oneResult;
    }
    throw "Unexpected number of M3s returned for <file>";
}

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
java set[M3] createM3sFromFiles(set[loc] files, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], str javaVersion = "1.7");

M3 createM3FromFiles(loc projectName, set[loc] files, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], str javaVersion = "1.7")
    = composeJavaM3(projectName, createM3sFromFiles(files, errorRecovery = errorRecovery, sourcePath = sourcePath, classPath = classPath, javaVersion = javaVersion));

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
java tuple[set[M3], set[Declaration]] createM3sAndAstsFromFiles(set[loc] files, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], str javaVersion = "1.7");

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
java M3 createM3FromString(loc fileName, str contents, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], str javaVersion = "1.7");

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
java M3 createM3FromJarClass(loc jarClass, list[loc] classPath = []);

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
java M3 createM3FromSingleClass(loc jarClass, str className);

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
java M3 createM3FromJarFile(loc jarLoc, list[loc] classPath = []);

@synopsis{Globs for jars, class files and java files in a directory and tries to compile all source files into an M3 model}
M3 createM3FromDirectory(loc project, bool errorRecovery = false, bool includeJarModels=false, str javaVersion = "1.7", list[loc] classPath = []) {
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
M3 createM3FromMavenProject(loc project, bool errorRecovery = false, bool includeJarModels=false, str javaVersion = "1.7", list[loc] classPath = []) {
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
		model.methodOverrides += {<m, getMethodSignature(m)> | m <- methodContainment[from]} 
			o {<getMethodSignature(m), m> | m <- methodContainment[to]};
	}

	return model;
}

void unregisterJavaProject(loc project) {
  unregisterProjectSchemes(project, {"java+compilationUnit", "java+compilationUnit", "java+class", "java+constructor", "java+initializer", "java+parameter","java+variable","java+field" , "java+interface" , "java+enum", "java+class" , "java+interface","java+enum"});
}

str getMethodSignature(loc method) 
	= substring(method.path, findLast(method.path,"/") + 1);

bool isCompilationUnit(loc entity) = entity.scheme == "java+compilationUnit";
bool isPackage(loc entity) = entity.scheme == "java+package";
bool isClass(loc entity) = entity.scheme == "java+class";
bool isConstructor(loc entity) = entity.scheme == "java+constructor";
bool isMethod(loc entity) = entity.scheme == "java+method" || entity.scheme == "java+constructor" || entity.scheme == "java+initializer";
bool isParameter(loc entity) = entity.scheme == "java+parameter";
bool isVariable(loc entity) = entity.scheme == "java+variable";
bool isField(loc entity) = entity.scheme == "java+field";
bool isInterface(loc entity) = entity.scheme == "java+interface";
bool isEnum(loc entity) = entity.scheme == "java+enum";
bool isType(loc entity) = entity.scheme == "java+class" || entity.scheme == "java+interface" || entity.scheme == "java+enum";

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
 
rel[loc, loc] declaredTopTypes(M3 m)  
  = {e | tuple[loc lhs, loc rhs] e <- m.containment, isCompilationUnit(e.lhs), isType(e.rhs)}; 

rel[loc, loc] declaredSubTypes(M3 m) 
  = {e | tuple[loc lhs, loc rhs] e <- m.containment, isClass(e.rhs)} - declaredTopTypes(m);


@memo set[loc] classes(M3 m) =  {e | <e,_> <- m.declarations, isClass(e)};
@memo set[loc] interfaces(M3 m) =  {e | <e,_> <- m.declarations, isInterface(e)};
@memo set[loc] packages(M3 m) = {e | <e,_> <- m.declarations, isPackage(e)};
@memo set[loc] variables(M3 m) = {e | <e,_> <- m.declarations, isVariable(e)};
@memo set[loc] parameters(M3 m)  = {e | <e,_> <- m.declarations, isParameter(e)};
@memo set[loc] fields(M3 m) = {e | <e,_> <- m.declarations, isField(e)};
@memo set[loc] methods(M3 m) = {e | <e,_> <- m.declarations, isMethod(e)};
@memo set[loc] constructors(M3 m) = {e | <e,_> <- m.declarations, isConstructor(e)};
@memo set[loc] enums(M3 m) = {e | <e,_> <- m.declarations, isEnum(e)};
@memo set[loc] types(M3 m) = {e | <e,_> <- m.declarations, isType(e)};

set[loc] elements(M3 m, loc parent) = m.containment[parent];

@memo set[loc] fields(M3 m, loc class) = { e | e <- elements(m, class), isField(e) };
@memo set[loc] methods(M3 m, loc class) = { e | e <- elements(m, class), isMethod(e) };
@memo set[loc] constructors(M3 m, loc class) = { e | e <- elements(m, class), isConstructor(e) };
@memo set[loc] nestedClasses(M3 m, loc class) = { e | e <- elements(m, class), isClass(e) };
