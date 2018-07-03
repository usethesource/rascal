module lang::java::m3::TypeHierarchy

import lang::java::m3::Core;
import lang::java::m3::TypeSymbol;

rel[loc from, loc to] getDeclaredTypeHierarchy(M3 model) {
  typeHierarchy = model.extends + model.implements;
  typesWithoutParent = classes(model) + interfaces(model) - typeHierarchy<from>;
  enumsWithoutParent = enums(model) - typeHierarchy<from>;
  
  return typeHierarchy
    + (typesWithoutParent - {|java+class:///java/lang/Object|}) * {|java+class:///java/lang/Object|}
    + (enumsWithoutParent - {|java+class:///java/lang/Object|, |java+class:///java/lang/Enum|}) * {|java+class:///java/lang/Enum|}
    + ((enums(model) != {}) ? {<|java+class:///java/lang/Enum|, |java+lang:///java/lang/Object|>} : {})
    ;
}

