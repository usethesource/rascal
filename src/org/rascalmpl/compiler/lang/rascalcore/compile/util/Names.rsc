module lang::rascalcore::compile::util::Names

import String;

str removeEmptyLines(str s){
    return visit(s) { case /^\n[ ]*\n/ => "\n" };
}

str getBaseClass(str dottedName){
    n = findLast(dottedName, ".");
    return n >= 0 ? dottedName[n+1 ..] : dottedName;
}
str  module2uqclass(str qname){
    return getBaseClass(replaceAll(qname, "::", "."));
}

str module2class(str qname){
    return replaceAll(qname, "::", ".");
}

str module2ul(str qname){
    return replaceAll(qname, "::", "_");
}

str colon2ul(str s) = replaceAll(replaceAll(s, "::", "_"), "$", ".");

str module2class(str moduleName){
    return module2uqclass(moduleName);
}
str module2package(str moduleName){
    className = module2class(moduleName);
    n = findLast(className, ".");
    return n >= 0 ? className[0 .. n] : "";
}

str module2interface(str moduleName){
    className = module2class(moduleName);
    n = findLast(className, ".");
    return n >= 0 ? "<className[0 .. n]>.$<className[n+1..]>" : "$<className>";
}

str escapeAsJavaString(str s){
  return replaceAll(s, "\n", "\\n");    //TODO make precise
}

bool containedIn(loc inner, loc outer){
    return inner.path == outer.path && inner.offset >= outer.offset && inner.offset + inner.length <= outer.offset + outer.length;
}
