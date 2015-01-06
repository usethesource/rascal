module lang::java::m3::TypeHierarchy

import lang::java::m3::Core;
import lang::java::m3::TypeSymbol;

rel[loc from, loc to] getDeclaredTypeHierarchy(M3 model) {
  typeHierarchy = model.extends + model.implements;
  typesWithoutParent = classes(model) + interfaces(model) - typeHierarchy<from>;
  
  return typesWithoutParent * {|java+class:///java/lang/Object|} + typeHierarchy;
}

