module lang::java::m3::TypeHierarchy

import lang::java::m3::Core;
import lang::java::m3::Facts;
import lang::java::m3::TypeSymbol;

rel[loc from, loc to] getDeclaredTypeHierarchy(M3 model) {
  allClasses = {e | e <- model@declarations<name>, isClass(e)};
  typeHierarchy = model@extends + model@implements;
  classesWithoutParent = allClasses - typeHierarchy<from>;
  
  return classesWithoutParent * {|java+class:///java/lang/Object|} + typeHierarchy;
}

rel[TypeSymbol sub, TypeSymbol sup] getTypeHierarchy(M3 model) 
  = { <model@types[from], model@types[to]> | <from, to> <- getDeclaredTypeHierarchy(model) };