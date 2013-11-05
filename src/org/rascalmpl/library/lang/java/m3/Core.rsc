@doc{
Synopsis: extends the M3 [$analysis/m3/Core] with Java specific concepts such as inheritance and overriding.
}
module lang::java::m3::Core

extend lang::java::m3::TypeSymbol;
import lang::java::m3::Registry;
import lang::java::m3::AST;

extend analysis::m3::Core;

import analysis::graphs::Graph;
import analysis::m3::Registry;

import IO;
import String;
import Relation;
import Set;
import Map;
import Node;
import List;

import util::FileSystem;
import demo::common::Crawl;

data Modifier = \annotation(loc \ann);

anno rel[loc from, loc to] M3@extends;            // classes extending classes and interfaces extending interfaces
anno rel[loc from, loc to] M3@implements;         // classes implementing interfaces
anno rel[loc from, loc to] M3@methodInvocation;   // methods calling each other (including constructors)
anno rel[loc from, loc to] M3@fieldAccess;        // code using data (like fields)
anno rel[loc from, loc to] M3@typeDependency;     // using a type literal in some code (types of variables, annotations)
anno rel[loc from, loc to] M3@methodOverrides;    // which method override which other methods

public M3 composeJavaM3(loc id, set[M3] models) {
  m = composeM3(id, models);
  
  m@extends = {*model@extends | model <- models};
  m@implements = {*model@implements | model <- models};
  m@methodInvocation = {*model@methodInvocation | model <- models};
  m@fieldAccess = {*model@fieldAccess | model <- models};
  m@typeDependency = {*model@typeDependency | model <- models};
  m@methodOverrides = {*model@methodOverrides | model <- models};
  
  return m;
}

public M3 link(M3 projectModel, set[M3] libraryModels) {
  projectModel@declarations = { <name[authority=projectModel.id.authority], src> | <name, src> <- projectModel@declarations };
  for (libraryModel <- libraryModels) {
    libraryModel@declarations = { <name[authority=libraryModel.id.authority], src> | <name, src> <- libraryModel@declarations }; 
  }
}

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
@reflect
public java M3 createM3FromFile(loc file, str javaVersion = "1.7");

@javaClass{org.rascalmpl.library.lang.java.m3.internal.EclipseJavaCompiler}
@reflect
public java M3 createM3FromJarClass(loc jarClass);

@doc{
Synopsis: globs for jars, class files and java files in a directory and tries to compile all source files into an [M3] model
}
public M3 createM3FromDirectory(loc project, str javaVersion = "1.7") {
    if (!(isDirectory(project)))
      throw "<project> is not a valid directory";
    classPaths = getPaths(project, "class") + find(project, "jar");
    sourcePaths = getPaths(project, "java");
    //setEnvironmentOptions(project);
    setEnvironmentOptions(classPaths, sourcePaths);
    M3 result = m3(project);
    for (sp <- sourcePaths) {
      result = composeJavaM3(project, { createM3FromFile(f, javaVersion = javaVersion) | loc f <- find(sp, "java") });
    }
    registerProject(project.authority, result);
    return result;
}

public Declaration getMethodAST(loc methodLoc, M3 model = m3(|unknown:///|)) {
  if (isMethod(methodLoc)) {
    if (isEmpty(model)) {
      model = getModelContaining(methodLoc);
      if (isEmpty(model))
        throw "Declaration for <methodLoc> not found in any models";
    }
    loc file = getFileContaining(methodLoc, model);
    Declaration fileAST = createAstFromFile(file, true);
    visit(fileAST) {
      case Declaration d: {
        if ("decl" in getAnnotations(d) && d@decl == methodLoc)
          return d;
      }
    }
    throw "No declaration matching <methodLoc> found";
  }
  throw "Only methods are supported at the moment";
}

public M3 createM3FromJar(loc jarFile) {
    str jarName = substring(jarFile.path, 0, findFirst(jarFile.path, "!"));
    jarName = substring(jarName, findLast(jarName, "/")+1);
    loc jarLoc = |jar:///|;
    jarLoc.authority = jarName;
    return composeJavaM3(jarLoc , { createM3FromJarClass(jarClass) | loc jarClass <- crawl(jarFile, "class") });
}

public M3 includeJarRelations(M3 project, set[M3] jarRels = {}) {
  set[M3] rels = jarRels;
  if (isEmpty(rels))
    rels = createM3FromProjectJars(project.id);
  
  return composeJavaM3(project.id, rels);
}

public bool isCompilationUnit(loc entity) = entity.scheme == "java+compilationUnit";
public bool isPackage(loc entity) = entity.scheme == "java+package";
public bool isClass(loc entity) = entity.scheme == "java+class";
public bool isMethod(loc entity) = entity.scheme == "java+method" || entity.scheme == "java+constructor";
public bool isParameter(loc entity) = entity.scheme == "java+parameter";
public bool isVariable(loc entity) = entity.scheme == "java+variable";
public bool isField(loc entity) = entity.scheme == "java+field";
public bool isInterface(loc entity) = entity.scheme == "java+interface";

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
  = {e | tuple[loc lhs, loc rhs] e <- m@containment, isClass(e.rhs)} - declaredTopTypes(rels);

@memo public set[loc] classes(M3 m) =  {e | e <- m@declarations<name>, isClass(e)};
@memo public set[loc] interfaces(M3 m) =  {e | e <- m@declarations<name>, isInterface(e)};
@memo public set[loc] packages(M3 m) = {e | e <- m@declarations<name>, isPackage(e)};
@memo public set[loc] variables(M3 m) = {e | e <- m@declarations<name>, isVariable(e)};
@memo public set[loc] parameters(M3 m)  = {e | e <- m@declarations<name>, isParameter(e)};
@memo public set[loc] fields(M3 m) = {e | e <- m@declarations<name>, isField(e)};
@memo public set[loc] methods(M3 m) = {e | e <- m@declarations<name>, isMethod(e)};

public set[loc] elements(M3 m, loc parent) = { e | <parent, e> <- m@containment };

@memo public set[loc] fields(M3 m, loc class) = { e | e <- elements(m, class), isField(e) };
@memo public set[loc] methods(M3 m, loc class) = { e | e <- elements(m, class), isMethod(e) };
@memo public set[loc] nestedClasses(M3 m, loc class) = { e | e <- elements(m, class), isClass(e) };