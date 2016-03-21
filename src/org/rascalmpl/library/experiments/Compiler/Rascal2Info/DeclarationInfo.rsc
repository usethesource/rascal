module experiments::Compiler::Rascal2Info::DeclarationInfo

import Type;

data DeclarationInfo
    = 
      moduleInfo(str moduleName, loc src, str synopsis, str doc)
    | functionInfo(str moduleName, str name, Symbol declType, str signature, loc src, str synopsis, str doc)
    | constructorInfo(str moduleName, str name, Symbol declType, str signature, loc src)
    | dataInfo(str moduleName, str name, loc src, str synopsis, str doc)
    | varInfo(str moduleName, str name, Symbol declType, str signature, loc src)
    ;