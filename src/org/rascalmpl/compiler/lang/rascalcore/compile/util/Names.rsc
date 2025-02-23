@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::compile::util::Names

import String;
import List;
import util::Reflective;

data PathConfig(
    loc generatedSources=|unknown:///|,
    loc generatedTestSources=|unknown:///|,
    loc resources = |unknown:///|,
    loc testResources =|unknown:///|
);

public /*const*/ str compiled_rascal_package = "org.rascalmpl"; //"rascal";
public /*const*/ str compiled_rascal_package_as_path = "org/rascalmpl"; //"rascal";

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

str getCompiledPackage(str _qname, PathConfig _pcfg)
    = "rascal";

str asClassRef(str qname, PathConfig pcfg){
    //return prefixLast("$", qname);
    return "<getCompiledPackage(qname, pcfg)>.<prefixLast("$", qname)>";;
}

str asPackageName(str qname, PathConfig pcfg){
    className = normalizeQName(qname);
    n = findLast(className, ".");
    //return n >= 0 ? "<className[0 .. n]>" : ""; //compiled_rascal_package;
    package = getCompiledPackage(qname, pcfg);
    return n >= 0 ? "<package>.<className[0 .. n]>" : package;
}

str asPackagePath(str qname){
    className = normalizeQName(qname);
    n = findLast(className, ".");
    return n >= 0 ? "<className[0 .. n]>" : "";
}

loc getGeneratedClassesDir(str qualifiedModuleName, PathConfig pcfg){
    return pcfg.bin + getCompiledPackage(qualifiedModuleName, pcfg) + makeDirName(qualifiedModuleName);
}

loc getGeneratedSrcsDir(str qualifiedModuleName, PathConfig pcfg){
    return pcfg.generatedSources + getCompiledPackage(qualifiedModuleName, pcfg) + makeDirName(qualifiedModuleName);
}

loc getGeneratedTestSrcsDir(str qualifiedModuleName, PathConfig pcfg){
    return (pcfg.generatedTestSources ? pcfg.generatedSources) + getCompiledPackage(qualifiedModuleName, pcfg) + makeDirName(qualifiedModuleName);
}

loc getGeneratedResourcesDir(str qualifiedModuleName, PathConfig pcfg){
    return pcfg.resources + getCompiledPackage(qualifiedModuleName, pcfg) + makeDirName(qualifiedModuleName);
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

str asBaseModuleName(str qname){
    n = findLast(qname, "::");
    return n >= 0 ? qname[n+2 ..] : qname;
}

str asBaseInterfaceName(str qname){
    qname = normalizeQName(qname);
    n = findLast(qname, ".");
    return n >= 0 ? "$<qname[n+1 ..]>_$I" : "$<qname>_$I";
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

str module2interface(str qname, PathConfig pcfg){
    className = normalizeQName(qname);
    n = findLast(className, ".");
    package = getCompiledPackage(qname, pcfg);
    return n >= 0 ? "<package>.<className[0 .. n]>.$<className[n+1..]>_$I" : "<package>.$<className>_$I";

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

bool isEqualModule(str name1, name2){
    if(contains(name1, "::") && contains(name2, "::")) return name1 == name2;
    name1a = replaceAll(name1, "::", "_");
    name2a = replaceAll(name2, "::", "_");
    res = endsWith(name1a, name2a) || endsWith(name2a, name1a);
    //println("isEqualModule(<name1>, <name2>) =\> <res>");
    return res;
}