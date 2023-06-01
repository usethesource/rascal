module lang::rascalcore::compile::ShowTPL

import IO;
import ValueIO;
extend lang::rascalcore::check::CheckerCommon;

void show(loc tplLoc, bool definitions=false){
    tm = readBinaryValueFile(#TModel, tplLoc);
    if(definitions) iprintln(tm.definitions, lineLimit=10000);
    else iprintln(tm, lineLimit=10000);
}
 
 