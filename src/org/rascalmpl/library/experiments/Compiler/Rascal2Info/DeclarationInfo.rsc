module experiments::Compiler::Rascal2Info::DeclarationInfo

import Type;

data DeclarationInfo(str synopsis = "", str doc = "")
    = 
      moduleInfo(str moduleName, loc src)
    | functionInfo(str moduleName, str name, Symbol declType, str signature, loc src)
    | constructorInfo(str moduleName, str name, Symbol declType, str signature, loc src)
    | dataInfo(str moduleName, str name, loc src)
    | varInfo(str moduleName, str name, Symbol declType, str signature, loc src)
    ;