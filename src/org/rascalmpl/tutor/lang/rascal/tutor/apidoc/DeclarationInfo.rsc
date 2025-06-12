module  lang::rascal::tutor::apidoc::DeclarationInfo

@doc{Representation of documentation-related information extracted from a module.}
data DeclarationInfo(
     str moduleName="", 
     str name=moduleName, 
     loc src = |unknown:///|, 
     str synopsis="",  
     str signature="",
     list[DocTag] docs = [], 
     loc docSrc = src)
     = moduleInfo        (str kind="module", bool demo=false, list[str] dependencies=[])
     | functionInfo      (str kind="function", str fullFunction="")
     | testInfo          (str kind="test", str fullTest="")
     | constructorInfo   (str kind="constructor")
     | dataInfo          (str kind="data", list[str] overloads=[])
     | aliasInfo         (str kind="alias")
     | varInfo           (str kind="variable")
     | syntaxInfo        (str kind="syntax")
     ;

data DocTag(str label="", loc src=|unknown:///|, str content="")
     = docTag();
     
