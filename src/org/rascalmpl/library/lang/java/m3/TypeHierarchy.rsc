module lang::java::m3::TypeHierarchy

import lang::java::m3::Core;

public rel[loc from, loc to] getDeclaredTypeHierarchy(M3 model) {
  set[loc] allClasses = {e | e <- domain(model@declarations), e.scheme == "java+class" || e.scheme == "java+interface"};
  set[loc] classesWithoutParent = allClasses - carrier(model@extends);
  rel[loc, loc] typeHierarchy = model@extends;
  
  classesWithoutParent += top(invert(typeHierarchy));
  
  loc javaObject = |java+class:///java/lang/Object|;
  
  for (loc topClass <- classesWithoutParent) {
    javaObject.authority = topClass.authority;
    typeHierarchy += <topClass, javaObject>;
  }
  
  return typeHierarchy;
}
