module  lang::rascal::tutor::apidoc::DeclarationInfo

@doc{Representation of documentation-related information extracted from a module.}
data DeclarationInfo(
     str moduleName="", 
     str name=moduleName, 
     loc src = |unknown:///|, 
     str synopsis="", 
     str signature="",
     str doc = "", 
     loc docSrc = src)
     = moduleInfo        (str kind="module")
     | functionInfo      (str kind="function")
     | constructorInfo   (str kind="constructor")
     | dataInfo          (str kind="data")
     | aliasInfo         (str kind="alias")
     | varInfo           (str kind="variable")
     ;
