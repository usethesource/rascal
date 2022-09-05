module lang::rascal::tutor::apidoc::ExtractDoc

import List;
import String;

import lang::rascal::tutor::apidoc::DeclarationInfo;
import lang::rascal::tutor::apidoc::ExtractInfo;

@doc{Extract documentation from a Rascal module and convert it into AsciiDoc markup.}
tuple[str moduleDoc, list[DeclarationInfo] declarationInfo] extractDoc(str parent, loc moduleLoc){
    dinfo = extractInfo(moduleLoc);
    res = "";
    int i = 0;
    while(i < size(dinfo)){
        j = i + 1;
        list[str] overloads = [];
        if(dinfo[i] has name){
           overloads = [dinfo[i].signature];
           // TODO: this only collects consecutive overloads. if a utility function interupts the flow,
           // then we do not get to see the other overloads with the current group. Rewrite to use a "group-by" query.
           while(j < size(dinfo) && dinfo[i].name == dinfo[j].name){
             overloads += dinfo[j].signature;
             j += 1;
           }
        }
        res += declInfo2Doc(parent, dinfo[i], overloads);
        i = j;
    }
    return <res, dinfo>;
}

public str basename(str cn){
  return (/^.*::<base:[A-Za-z0-9\-\_]+>$/ := cn) ? base : cn;
}

private str fragment(str moduleName) = "#<replaceAll(moduleName, "::", "-")>";

str makeName(str name){
  return "# <basename(name)>";
}

str makeUsage(str name) =
    "### Usage
    '
    '`import <replaceAll(name, "/", "::")>;`
    '";

str makeSignature(list[str] overloads) =
    size(overloads) == 1 ? "`<overloads[0]>`" : "<for(s <- overloads){>* ``<s>``\n<}>"; 
    
str declInfo2Doc(str parent, moduleInfo(str moduleName, loc src, str synopsis, str doc), list[str] overloads) =
    "
    '## <moduleName> {#<fragment(moduleName)>}
    '<makeName(moduleName)>
    '<makeUsage(moduleName)>
    '
    '<doc>
    '";

str declInfo2Doc(str parent, functionInfo(str moduleName, str name, str signature, loc src, str synopsis, str doc), list[str] overloads) =
    "
    '## <name> {<fragment(moduleName)>-<name>}
    '
    '### Signature
    '
    '<makeSignature(overloads)>
    '
    '<doc>
    '";  
 
 str declInfo2Doc(str parent, constructorInfo(str moduleName, str name, str signature, loc src), list[str] overloads) =
     "";
    
 str declInfo2Doc(str parent, dataInfo(str moduleName, str name, str signature, loc src, str synopsis, str doc), list[str] overloads) =
    "
    '## <name> {<fragment(moduleName)>-<name>}
    '
    '```rascal
    '<for(ov <- overloads){><ov>\n<}>
    '```
    '<doc>
    "; 

str declInfo2Doc(str parent, aliasInfo(str moduleName, str name, str signature, loc src, str synopsis, str doc), list[str] overloads) =
    "
    '## <name> {<fragment(moduleName)>-<name>}
    '
    '<makeSignature(overloads)>
    '
    '<doc>
    '";
       
default str declInfo2Doc(str parent, DeclarationInfo d, list[str] overloads) = "EMPTY:<d>";
