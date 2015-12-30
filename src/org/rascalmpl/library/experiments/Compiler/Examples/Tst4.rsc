module experiments::Compiler::Examples::Tst4

import experiments::Compiler::Rascal2muRascal::ParseModule;
import util::Reflective;
import lang::rascal::types::CheckerConfig;
import lang::rascal::types::CheckTypes;

Configuration doCheck(loc mloc){
    return checkModule(parseModuleGetTop(mloc), newConfiguration(pathConfig()));
}