module experiments::Compiler::Rascal2Info::Query

import experiments::Compiler::Rascal2Info::DeclarationInfo;
import experiments::Compiler::Rascal2Info::Generate;

import util::Reflective;
import IO;
import Type;
import String;
import Node;

str report(DeclarationInfo di, bool showDoc = false, bool showSource = false){

    constructorName = getName(di);
    role = ("functionInfo":    "Function:    ", 
            "constructorInfo": "Constructor: ", 
            "dataInfo":        "Data:        ", 
            "moduleInfo":      "Module:      ", 
            "varInfo":         "Variable:    ")[constructorName] ? constructorName;
           
    return "Module:       <di.moduleName>
           '<role> <di.signature>
           '<showDoc ? di.doc : "Synopsis:     <di.synopsis>">
           '<showSource ? "Source:\n<readFile(di.src)>" : "">
           '";
}
str infoFunction(str name, set[DeclarationInfo] info, bool showDoc = false, bool showSource = false, bool fuzzy = false){
   result = "";
   name1 = toLowerCase(name);
   for(/fi: functionInfo(str moduleName, name2, Symbol declType, str signature, loc src) := info, (fuzzy ? contains(toLowerCase(name2), name1) : toLowerCase(name2) == name1)){
      result += report(fi, showDoc=showDoc, showSource=showSource);
   }
   for(/ci: constructorInfo(str moduleName, name2, Symbol declType, str signature, loc src) := info, (fuzzy ? contains(toLowerCase(name2), name1) : toLowerCase(name2) == name1)){
      result += report(ci, showDoc=showDoc, showSource=showSource);
   }
   return result;
}

str infoModule(str moduleName, set[DeclarationInfo] info){
    result = "";
    for(/fi: functionInfo(moduleName, name2, Symbol declType, str signature, loc src) := info){
       result += "<signature>\n";
    }
    return result;
}

str infoReturns(str returnType, set[DeclarationInfo] info,  bool showDoc = false, bool showSource = false){
    result = "";
    for(di <- info){
        if(di has declType){
           switch(di.declType){
           case \func(Symbol ret, list[Symbol] parameters):
                if(startsWith("<ret>", returnType)){
                   result += report(di, showDoc=showDoc, showSource=showSource);
                }
           case \cons(Symbol \adt, str name, list[Symbol] parameters): {
           
               //println("<adt.name>, <di>");
               
               if(startsWith("<adt.name>", returnType)){
                  result += report(di, showDoc=showDoc, showSource=showSource);
               }
               }
           }
       }

   }
   return result;
}

set[DeclarationInfo] getStdLibInfo(){

    info1 = generate(["Boolean", "DateTime", "Exception", "Grammar", "IO", "List", "ListRelation", "Map", "Message", 
                     "Node", "ParseTree", "Relation", "Set", "String", "ToString", "Tuple", "Type", "ValueIO"], pathConfig());
    info2 = generate(["util::Benchmark", "util::Brackets", "util::FileSystem", "util::Highlight", "util::Loc", 
                      "util::Math", "util::Maybe", "util::Monitor", "util::PriorityQueue", "util::REPL", 
                      "util::Reflective", "util::ShellExec", "util::SystemAPI",
                     "util::UUID", "util::Webserver"], pathConfig());
    return info1 + info2;
}
    

value main(){
   info = generate("experiments::Compiler::Examples::Tst2", pathConfig());
   println(getFunctions("f", info, showSource=true));
   return true;
}