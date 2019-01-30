module lang::rascalcore::compile::util::Names

import String;

str removeEmptyLines(str s){
    return visit(s) { case /^\n[ ]*\n/ => "\n" };
}

str getBaseClass(str qname){
    qname = replaceAll(qname, "::", ".");
    n = findLast(qname, ".");
    return n >= 0 ? qname[n+1 ..] : qname;
}
str  module2uqclass(str qname){
    return getBaseClass(replaceAll(qname, "::", "."));
}

str module2class(str qname){
    return replaceAll(qname, "::", ".");
}

str module2field(str qname){
    return "M_" + replaceAll(qname, "::", "_");
}

str colon2ul(str s) = replaceAll(replaceAll(s, "::", "_"), "$", ".");

//str module2class(str moduleName){
//    return module2uqclass(moduleName);
//}
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

// Is inner location textually contained in outer location?
bool containedIn(loc inner, loc outer){
    return inner.path == outer.path && (!outer.offset? || inner.offset >= outer.offset && inner.offset + inner.length <= outer.offset + outer.length);
}
