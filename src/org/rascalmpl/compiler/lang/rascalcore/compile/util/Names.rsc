module lang::rascalcore::compile::util::Names

import String;

str compiled_rascal_package = "compiled_rascal";

str removeEmptyLines(str s){
    return visit(s) { case /^\n[ ]*\n/ => "\n" };
}

str replaceColonAndDash(str s) = replaceAll(replaceAll(s, "-", "_"), "::", ".");

str getQualClassName(str qname){
    return replaceColonAndDash(qname);
}

str getClassName(str qname){
    qname = replaceColonAndDash(qname);
    n = findLast(qname, ".");
    return n >= 0 ? qname[n+1 ..] : qname;
}

str getClassRef(str qname, str inModule){
    qname = replaceColonAndDash(qname);
    n = findLast(qname, ".");
    //if(getPackageName(inModule) != compiled_rascal_package){
        qname = "<compiled_rascal_package>.<qname>";
   // }
   return qname;
}

str getPackageName(str qname){
    className = replaceColonAndDash(qname);
    n = findLast(className, ".");
    return n >= 0 ? "<compiled_rascal_package>.<className[0 .. n]>" : compiled_rascal_package;
}

str getClass(str qname){
    qname = replaceColonAndDash(qname);
    n = findLast(qname, ".");
    return n >= 0 ? "<compiled_rascal_package>.<qname[n+1 ..]>" : "<compiled_rascal_package>.<qname>";
}

str getBaseClass(str qname){
    qname = replaceColonAndDash(qname);
    n = findLast(qname, ".");
    return n >= 0 ? qname[n+1 ..] : qname;
}

str getADTName(str adt)
    = "ADT_<getJavaName(adt, completeId=false)>";
    
set[str] javaKeywords = {
    "abstract", "continue", "for",        "new",       "switch",
    "assert",   "default",  "goto",       "package",   "synchronized",
    "boolean",  "do",       "if",         "private",   "this",
    "break",    "double",   "implements", "protected", "throw",
    "byte",     "else",     "import",     "public",    "throws",
    "case",     "enum",     "instanceof", "return",    "transient",
    "catch",     "extends",  "int",        "short",     "try",
    "char",     "final",    "interface",  "static",    "void",
    "class",    "finally",  "long",       "strictfp",  "volatile",
    "const",    "float",    "native",     "super",     "while"};
    

str getJavaName(str fname, bool completeId = true)
    = completeId && fname in javaKeywords ? "$<fname>" : replaceAll(fname, "-", "_");
    
str  module2uqclass(str qname, str inModule){
    qname = replaceAll(qname, "::", ".");
    n = findLast(qname, ".");
    uqname = n >= 0 ? qname[n+1 ..] : qname;
   //if(getPackageName(inModule) != compiled_rascal_package){
        uqname = "<compiled_rascal_package>.<uqname>";
    //}
    return uqname;
}

str module2class(str qname){
    return getBaseClass(qname); //replaceAll(qname, "::", ".");
}

str module2path(str qname){
    path = replaceAll(qname, "::", "/");
    n = findLast(path, "/");
    return n >= 0 ? "<compiled_rascal_package>/<path[0 .. n]>" : compiled_rascal_package;
}

str module2field(str qname){
    return "M_" + replaceAll(replaceColonAndDash(qname), ".", "_");
}

str colon2ul(str s) = replaceAll(replaceAll(s, "::", "_"), "$", ".");

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
