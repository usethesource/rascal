module experiments::Compiler::Rascal2muRascal::ParseModule

import ParseTree;
import lang::rascal::\syntax::Rascal;
import util::Reflective;

lang::rascal::\syntax::Rascal::Module parseModuleGetTop(loc moduleLoc) {
    return justParseModule(moduleLoc);
    //return parse(#start[Module], moduleLoc).top;
    //T = parseModule(moduleLoc);
    //if(T has top){
    //  T = T.top;
    //}
    //if(lang::rascal::\syntax::Rascal::Module M := T){
    //   return M;
    //}
    //throw "parseModuleGetTop: cannot parse <moduleLoc>";
}

//parse(#start[Module], moduleLoc).top;

//start[Module] parseModuleAndFragments(str content, loc l) {
//  init = parse(#start[Module], content, l);
//
//}