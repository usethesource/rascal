module lang::rascalcore::compile::util::Names

import String;
import IO;
import List;
import util::Reflective;

data PathConfig(
    loc generatedSources=|unknown:///|,
    loc resources = |unknown:///|,
    loc testResources =|unknown:///|
);

public /*const*/ str compiled_rascal_package = "rascal";

str removeEmptyLines(str s){
    return visit(s) { case /^\n[ ]*\n/ => "\n" };
}

private list[str] split(str qname)
    = split("::", qname);

private list[str] normalize(list[str] parts)
    = [ replaceAll(replaceAll(p, "-", "_"), "\\", "") | p <- parts ];

private list[str] escapeJavaKeywords(list[str] parts)
    = [ p in javaKeywords ? "$<p>" : p | p <- parts ];
    
private str normalizeQName(str qname){
    parts = escapeJavaKeywords(normalize(split(qname)));
    return intercalate(".", parts);
}

str asQualifiedClassName(str qname){
    return normalizeQName(qname);
}

str asUnqualifiedName(str qname){
    n = findLast(qname, "::");
    res = n >= 0 ? qname[n+2 ..] : qname;
    return res[0] == "\\" ? res[1..] : res;
}

str asClassName(str qname){
    return prefixLast("$", qname);
}

str prefixLast(str pref, str qname){
    qname = normalizeQName(qname);
    parts = split(".", qname);
    parts = parts[0 .. size(parts)-1] + "<pref><parts[-1]>";
    res = intercalate(".", parts);
    //println("prefixLast(<pref>, <qname>) =\> <res>");
    return res;
}

str asClassRef(str qname){
    return "<compiled_rascal_package>.<prefixLast("$", qname)>";;
}

str asPackageName(str qname){
    className = normalizeQName(qname);
    n = findLast(className, ".");
    return n >= 0 ? "<compiled_rascal_package>.<className[0 .. n]>" : compiled_rascal_package;
}

str asPackagePath(str qname){
    className = normalizeQName(qname);
    n = findLast(className, ".");
    return n >= 0 ? "<className[0 .. n]>" : "";
}

loc getDerivedClassesDir(str qualifiedModuleName, PathConfig pcfg){
    return pcfg.bin + compiled_rascal_package + makeDirName(qualifiedModuleName);
}

loc getDerivedSrcsDir(str qualifiedModuleName, PathConfig pcfg){
    return pcfg.generatedSources + compiled_rascal_package + makeDirName(qualifiedModuleName);
}

loc getDerivedResourcesDir(str qualifiedModuleName, PathConfig pcfg){
    return pcfg.resources + compiled_rascal_package + makeDirName(qualifiedModuleName);
}
str makeDirName(str qualifiedModuleName){
    parts =  escapeJavaKeywords(normalize(split(qualifiedModuleName)));
    return isEmpty(parts) ? "" : intercalate("/", parts[0..-1]);
}

str asBaseClassName(str qname){
    qname = normalizeQName(qname);
    n = findLast(qname, ".");
    return n >= 0 ? "$<qname[n+1 ..]>" : "$<qname>";
}

str asBaseInterfaceName(str qname){
    qname = normalizeQName(qname);
    n = findLast(qname, ".");
    return n >= 0 ? "$I_<qname[n+1 ..]>" : "$I_<qname>";
}

str asADTName(str adtName)
    = "ADT_<asJavaName(adtName, completeId=false)>";
    
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
    "const",    "float",    "native",     "super",     "while",  "true", "false", "null"};
    

str asJavaName(str fname, bool completeId = true){
    res = completeId && fname in javaKeywords ? "$<fname>" : replaceAll(replaceAll(fname, "-", "_"), "\\", "");
    return res == "_" ? "$_" : res; //single _ not allowed since Java9
}

str module2class(str qname){
    return asBaseClassName(qname); //replaceAll(qname, "::", ".");
}

str module2field(str qname){
    return "M_" + replaceAll(normalizeQName(qname), ".", "_");
}

str module2interface(str qname){
    className = normalizeQName(qname);
    n = findLast(className, ".");
    //return n >= 0 ? "<className[0 .. n]>.$<className[n+1..]>" : "$<className>";
    return n >= 0 ? "<compiled_rascal_package>.<className[0 .. n]>.$I_<className[n+1..]>" : "<compiled_rascal_package>.$I_<className>";
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