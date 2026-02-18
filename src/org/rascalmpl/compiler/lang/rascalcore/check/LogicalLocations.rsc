module lang::rascalcore::check::LogicalLocations

import String;
import lang::rascalcore::check::ATypeUtils;

bool isRascalLogicalLoc(loc l)
    = startsWith(l.scheme, "rascal+");
    
alias MODID = loc;   // module identification

bool isModuleId(loc l) = l.scheme == "rascal+module";

MODID moduleName2moduleId(str mname){
    return |rascal+module:///<replaceAll(unescape(mname), "::", "/")>|;
}

str moduleId2moduleName(loc mloc){
    assert mloc.scheme == "rascal+module" : "moduleId2moduleName: <mloc>";
    path = mloc.path;
    if(path[0] == "/") path = path[1..];
    return replaceAll(path, "/", "::");
}

alias FUNID = loc; // function identification

bool isFunctionId(loc l) = l.scheme == "rascal+function";

alias CONSID = loc; // constructor identification

bool isConstructorId(loc l) = l.scheme == "rascal+constructor";

alias FLDID = loc; // field identification

bool isFieldId(loc l) = l.scheme == "rascal+field";