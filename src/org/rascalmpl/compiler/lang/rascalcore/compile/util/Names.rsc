module lang::rascalcore::compile::util::Names

import String;
import IO;
import List;

str compiled_rascal_package = "compiled_rascal";

str removeEmptyLines(str s){
    return visit(s) { case /^\n[ ]*\n/ => "\n" };
}

list[str] split(str qname)
    = split("::", qname);

list[str] normalize(list[str] parts)
    = [ replaceAll(replaceAll(p, "-", "_"), "\\", "") | p <- parts ];

list[str] escapeJavaKeywords(list[str] parts)
    = [ p in javaKeywords ? "$<p>" : p | p <- parts ];
    
str normalizeQName(str qname)
    = intercalate(".", escapeJavaKeywords(normalize(split(qname))));

str replaceColonAndDash(str s) = replaceAll(replaceAll(replaceAll(s, "-", "_"), "::", "."), "\\", "");

str getQualClassName(str qname){
    return normalizeQName(qname); //replaceColonAndDash(qname);
}

str getUnqualifiedName(str qname){
    if("<qname>" == ""){
        println("getUnqualifiedName: <qname>");
    }
    n = findLast(qname, "::");
    res = n >= 0 ? qname[n+2 ..] : qname;
    return res[0] == "\\" ? res[1..] : res;
}

str getQualifier(str qname){
    n = findLast(qname, "::");
    res = n >= 0 ? qname[..n] : "";
    return (res != "" && res[0] == "\\") ? res[1..] : res;
}

str getClassName(str qname){
    qname = normalizeQName(qname); // replaceColonAndDash(qname);
    n = findLast(qname, ".");
    return n >= 0 ? qname[n+1 ..] : qname;
}

str getClassRef(str qname, str inModule){
    qname = normalizeQName(qname); // replaceColonAndDash(qname);
    n = findLast(qname, ".");
    //if(getPackageName(inModule) != compiled_rascal_package){
        qname = "<compiled_rascal_package>.<qname>";
   // }
   return qname;
}

str getPackageName(str qname){
    className = normalizeQName(qname); // replaceColonAndDash(qname);
    n = findLast(className, ".");
    return n >= 0 ? "<compiled_rascal_package>.<className[0 .. n]>" : compiled_rascal_package;
}

str getClass(str qname){
    qname = normalizeQName(qname); // replaceColonAndDash(qname);
    n = findLast(qname, ".");
    return n >= 0 ? "<compiled_rascal_package>.<qname[n+1 ..]>" : "<compiled_rascal_package>.<qname>";
}

str getBaseClass(str qname){
    qname = normalizeQName(qname); // replaceColonAndDash(qname);
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
    "catch",    "extends",  "int",        "short",     "try",
    "char",     "final",    "interface",  "static",    "void",
    "class",    "finally",  "long",       "strictfp",  "volatile",
    "const",    "float",    "native",     "super",     "while"};
    

str getJavaName(str fname, bool completeId = true){
    res = completeId && fname in javaKeywords ? "$<fname>" : replaceAll(replaceAll(fname, "-", "_"), "\\", "");
    return res;
}
    
str  module2uqclass(str qname, str inModule){
    qname = normalizeQName(qname); // replaceAll(qname, "::", ".");
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

    pieces = split("::", qname);
    pieces = [getJavaName(x) | x <- pieces];
    
    return size(pieces) > 1 ? "<compiled_rascal_package>/<intercalate("/", pieces[..-1])>": compiled_rascal_package;
}

str module2field(str qname){
    return "M_" + replaceAll(normalizeQName(qname) /*replaceColonAndDash(qname)*/, ".", "_");
}

str colon2ul(str s) = replaceAll(replaceAll(s, "::", "_"), "$", ".");

str module2interface(str moduleName){
    className = module2class(moduleName);
    n = findLast(className, ".");
    return n >= 0 ? "<className[0 .. n]>.$<className[n+1..]>" : "$<className>";
}

str escapeAsJavaString(str s){
  return
    visit(s){
        case /^[\u0008]/ => "\\b"
        case /^[\t]/ => "\\t"
        case /^[\n]/ => "\\n"
        case /^[\f]/ => "\\f"
        case /^[\r]/ => "\\r"
        case /^[\"]/ => "\\\""
        case /^[\']/ => "\\\'"
        case /^[\\]/ => "\\\\"
    
        // \u{0000-FFFF}
        //\{0-377}
    };
}

str unescapeAndStandardize(str s){
  return replaceAll(replaceAll(s, "\\", ""), "-", "_");
}

str unescapeName(str s){
    return s[0] ==  "\\" ? s[1..] : s;
}

    

