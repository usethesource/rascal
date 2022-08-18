module  lang::rascal::tutor::apidoc::DeclarationInfo

@doc{Representation of documentation-related information extracted from a module.}
data DeclarationInfo
     = moduleInfo(str moduleName, loc src, str synopsis, str doc)
     | functionInfo(str moduleName, str name, str signature, loc src, str synopsis, str doc)
     | constructorInfo(str moduleName, str name, str signature, loc src)
     | dataInfo(str moduleName, str name, str signature, loc src, str synopsis, str doc)
     | aliasInfo(str moduleName, str name, str signature, loc src, str synopsis, str doc)
     | varInfo(str moduleName, str name, str signature, loc src)
     ;
