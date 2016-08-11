@doc{
.Synopsis
extends the M3 [$analysis/m3/Core] with Java specific concepts such as inheritance and overriding.

.Description

For a quick start, go find <<createM3FromEclipseProject>>.
}
module lang::java::m3::Core

extend lang::java::m3::TypeSymbol;
import lang::java::m3::Registry;
import lang::java::m3::AST;

extend analysis::m3::Core;

import analysis::graphs::Graph;
import analysis::m3::Registry;

import IO;
import ValueIO;
import String;
import Relation;
import Set;
import Map;
import Node;
import List;

import util::FileSystem;

anno rel[loc from, loc to] M3@extends;            // classes extending classes and interfaces extending interfaces
anno rel[loc from, loc to] M3@implements;         // classes implementing interfaces
anno rel[loc from, loc to] M3@methodInvocation;   // methods calling each other (including constructors)
anno rel[loc from, loc to] M3@fieldAccess;        // code using data (like fields)
anno rel[loc from, loc to] M3@typeDependency;     // using a type literal in some code (types of variables, annotations)
anno rel[loc from, loc to] M3@methodOverrides;    // which method override which other methods
anno rel[loc declaration, loc annotation] M3@annotations;

data Language(str version="") = java();

public M3 composeJavaM3(loc id, set[M3] models) {
  m = composeM3(id, models);
  
  m@extends = {*model@extends | model <- models};
  m@implements = {*model@implements | model <- models};
  m@methodInvocation = {*model@methodInvocation | model <- models};
  m@fieldAccess = {*model@fieldAccess | model <- models};
  m@typeDependency = {*model@typeDependency | model <- models};
  m@methodOverrides = {*model@methodOverrides | model <- models};
  m@annotations = {*model@annotations | model <- models};
  
  return m;
}

public M3 link(M3 projectModel, set[M3] libraryModels) {
  projectModel@declarations = { <name[authority=projectModel.id.authority], src> | <name, src> <- projectModel@declarations };
  for (libraryModel <- libraryModels) {
    libraryModel@declarations = { <name[authority=libraryModel.id.authority], src> | <name, src> <- libraryModel@declarations }; 
  }
}

public M3 createM3FromFile(loc file, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], str javaVersion = "1.7") {
    result = createM3sFromFiles({file}, errorRecovery = errorRecovery, sourcePath = sourcePath, classPath = classPath, javaVersion = javaVersion);
    if ({oneResult} := result) {
        return oneResult;
    }
    throw "Unexpected number of M3s returned for <file>";
}

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
@reflect
public java set[M3] createM3sFromFiles(set[loc] files, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], str javaVersion = "1.7");

public M3 createM3FromFiles(loc projectName, set[loc] files, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], str javaVersion = "1.7")
    = composeJavaM3(projectName, createM3sFromFiles(files, errorRecovery = errorRecovery, sourcePath = sourcePath, classPath = classPath, javaVersion = javaVersion));

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
@reflect
public java tuple[set[M3], set[Declaration]] createM3sAndAstsFromFiles(set[loc] files, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], str javaVersion = "1.7");

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
@reflect
public java M3 createM3FromString(loc fileName, str contents, bool errorRecovery = false, list[loc] sourcePath = [], list[loc] classPath = [], str javaVersion = "1.7");

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
@reflect
public java M3 createM3FromJarClass(loc jarClass);

@doc{
.Synopsis
globs for jars, class files and java files in a directory and tries to compile all source files into an [$analysis/m3] model
}
public M3 createM3FromDirectory(loc project, bool errorRecovery = false, str javaVersion = "1.7") {
    if (!(isDirectory(project))) {
      throw "<project> is not a valid directory";
    }
    
    list[loc] classPaths = [];
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
    return result;
}


public M3 createM3FromJar(loc jarFile) {
    str jarName = substring(jarFile.path, 0, findFirst(jarFile.path, "!"));
    jarName = substring(jarName, findLast(jarName, "/")+1);
    loc jarLoc = |jar:///|;
    jarLoc.authority = jarName;

    map[str,M3] m3Map = (classPathToStr(jc): createM3FromJarClass(jc) | /file(jc) <- crawl(jarFile), jc.extension == "class");
    
    rel[str,str] inheritsFrom = { *{ <c.path, i.path> | <c, i> <- (model@implements + model@extends),
        c.path in m3Map && i.path in m3Map } | model <- range(m3Map) }+;
    
    map[str, rel[loc from,loc to]] methodOverrides = ( c: m3Map[c]@methodOverrides | c <- m3Map );
    for(<c, sc> <- inheritsFrom) {
	        // this is not	100% correct, since java method overriden allows be on a subtype in the signatures since java6
	        methodSC = { <m.file, m> | <m, p> <- m3Map[sc]@modifiers, (p == \public() || p == \protected()) && m.scheme == "java+method"  };	
	        ownMethods = { <m, m.file> |  m <- methods(m3Map[c]) - constructors(m3Map[c])};
	        methodOverrides[c] += ownMethods o methodSC;
    }
    
    return composeJavaM3(jarLoc, {m3Map[c][@methodOverrides = methodOverrides[c]] | c <- m3Map });
}
private str classPathToStr(loc jarClass) {
    return substring(jarClass.path,findLast(jarClass.path,"!")+1,findLast(jarClass.path,"."));
}

public bool isCompilationUnit(loc entity) = entity.scheme == "java+compilationUnit";
public bool isPackage(loc entity) = entity.scheme == "java+package";
public bool isClass(loc entity) = entity.scheme == "java+class";
public bool isConstructor(loc entity) = entity.scheme == "java+constructor";
public bool isMethod(loc entity) = entity.scheme == "java+method" || entity.scheme == "java+constructor";
public bool isParameter(loc entity) = entity.scheme == "java+parameter";
public bool isVariable(loc entity) = entity.scheme == "java+variable";
public bool isField(loc entity) = entity.scheme == "java+field";
public bool isInterface(loc entity) = entity.scheme == "java+interface";
public bool isEnum(loc entity) = entity.scheme == "java+enum";

public set[loc] files(rel[loc, loc] containment) 
  = {e.lhs | tuple[loc lhs, loc rhs] e <- containment, isCompilationUnit(e.lhs)};

public rel[loc, loc] declaredMethods(M3 m, set[Modifier] checkModifiers = {}) {
    declaredClasses = classes(m);
    methodModifiersMap = toMap(m@modifiers);
    
    return {e | tuple[loc lhs, loc rhs] e <- domainR(m@containment, declaredClasses), isMethod(e.rhs), checkModifiers <= (methodModifiersMap[e.rhs]? ? methodModifiersMap[e.rhs] : {}) };
}

public rel[loc, loc] declaredFields(M3 m, set[Modifier] checkModifiers = {}) {
    declaredClasses = classes(m);
    methodModifiersMap = toMap(m@modifiers);
    
    return {e | tuple[loc lhs, loc rhs] e <- domainR(m@containment, declaredClasses), isField(e.rhs), checkModifiers <= (methodModifiersMap[e.rhs]? ? methodModifiersMap[e.rhs] : {}) };
}

public rel[loc, loc] declaredFieldsX(M3 m, set[Modifier] checkModifiers = {}) {
    declaredClasses = classes(m);
    methodModifiersMap = toMap(m@modifiers);
    
    return {e | tuple[loc lhs, loc rhs] e <- domainR(m@containment, declaredClasses), isField(e.rhs), isEmpty(checkModifiers & (methodModifiersMap[e.rhs]? ? methodModifiersMap[e.rhs] : {})) };
} 
 
public rel[loc, loc] declaredTopTypes(M3 m)  
  = {e | tuple[loc lhs, loc rhs] e <- m@containment, isCompilationUnit(e.lhs), isClass(e.rhs) || isInterface(e.rhs)}; 

public rel[loc, loc] declaredSubTypes(M3 m) 
  = {e | tuple[loc lhs, loc rhs] e <- m@containment, isClass(e.rhs)} - declaredTopTypes(m);

@memo public set[loc] classes(M3 m) =  {e | <e,_> <- m@declarations, isClass(e)};
@memo public set[loc] interfaces(M3 m) =  {e | <e,_> <- m@declarations, isInterface(e)};
@memo public set[loc] packages(M3 m) = {e | <e,_> <- m@declarations, isPackage(e)};
@memo public set[loc] variables(M3 m) = {e | <e,_> <- m@declarations, isVariable(e)};
@memo public set[loc] parameters(M3 m)  = {e | <e,_> <- m@declarations, isParameter(e)};
@memo public set[loc] fields(M3 m) = {e | <e,_> <- m@declarations, isField(e)};
@memo public set[loc] methods(M3 m) = {e | <e,_> <- m@declarations, isMethod(e)};
@memo public set[loc] constructors(M3 m) = {e | <e,_> <- m@declarations, isConstructor(e)};
@memo public set[loc] enums(M3 m) = {e | <e,_> <- m@declarations, isEnum(e)};

public set[loc] elements(M3 m, loc parent) = m@containment[parent];

@memo public set[loc] fields(M3 m, loc class) = { e | e <- elements(m, class), isField(e) };
@memo public set[loc] methods(M3 m, loc class) = { e | e <- elements(m, class), isMethod(e) };
@memo public set[loc] constructors(M3 m, loc class) = { e | e <- elements(m, class), isConstructor(e) };
@memo public set[loc] nestedClasses(M3 m, loc class) = { e | e <- elements(m, class), isClass(e) };