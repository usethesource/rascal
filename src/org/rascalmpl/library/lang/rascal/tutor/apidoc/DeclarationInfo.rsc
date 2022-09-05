module  lang::rascal::tutor::apidoc::DeclarationInfo

@doc{Representation of documentation-related information extracted from a module.}
data DeclarationInfo
     = moduleInfo(str moduleName, loc src, str synopsis, str doc, str kind ="module")
     | functionInfo(str moduleName, str name, str signature, loc src, str synopsis, str doc, str kind="function")
     | constructorInfo(str moduleName, str name, str signature, loc src, str kind="constructor")
     | dataInfo(str moduleName, str name, str signature, loc src, str synopsis, str doc, str kind="data")
     | aliasInfo(str moduleName, str name, str signature, loc src, str synopsis, str doc str kind="alias")
     | varInfo(str moduleName, str name, str signature, loc src, str kind="variable")
     ;
