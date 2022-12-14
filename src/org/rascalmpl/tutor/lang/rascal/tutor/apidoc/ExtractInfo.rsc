@bootstrapParser
module  lang::rascal::tutor::apidoc::ExtractInfo

import IO;
import String;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import util::Reflective;

import lang::rascal::tutor::apidoc::DeclarationInfo;

@synopsis{Extract declaration information from a Rascal module at given location.}
list[DeclarationInfo] extractInfo(loc moduleLoc)
  = doExtractInfo(moduleLoc, lastModified(moduleLoc));

@memo
private list[DeclarationInfo] doExtractInfo(loc moduleLoc, datetime _/*lastModified*/){
    M = parseModuleWithSpaces(moduleLoc).top;
    return extractModule(M);
}

list[DeclarationInfo] extractModule(m: (Module) `<Header header> <Body body>`) {
    moduleName = "<header.name>";
    tags = getTagContents(header.tags);
    
    tls = [ *extractTopLevel(moduleName, tl) |  tl <- body.toplevels ];

    synopsis = getSynopsis(tags);

    return moduleInfo(moduleName=moduleName, src=m@\loc, synopsis=synopsis, docs=sortedDocTags(tags)) + tls;
}

/********************************************************************/
/*                  Process declarations in a module                */
/********************************************************************/
    
list[DeclarationInfo] extractTopLevel(str moduleName, (Toplevel) `<Declaration decl>`) = extractDecl(moduleName, decl);

// -- variable declaration ------------------------------------------

list[DeclarationInfo]  extractDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> <Type tp> <{Variable ","}+ variables> ;`)
  = [];

// -- miscellaneous declarations ------------------------------------

list[DeclarationInfo]  extractDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType>@<Name name> ;`) 
  = [];

list[DeclarationInfo]  extractDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType user> = <Type base> ;`) {
     dtags = getTagContents(tags);
     return [ aliasInfo(moduleName=moduleName, name="<user>", signature="<base>", src=d@\loc, synopsis=getSynopsis(dtags), docs=sortedDocTags(dtags))];
}

list[DeclarationInfo]  extractDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> tag <Kind kind> <Name name> on <{Type ","}+ types> ;`)  
  = [ ];

list[DeclarationInfo]  extractDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> ;`)  
  = [ ];

str align({Variant "|"}+ variants){
   res = "";
   sep = "\n     = ";
   for(v <- variants){
       res += sep + trim("<v>");
       sep = "\n     | ";
   }
   return res + "\n     ;";
}

list[DeclarationInfo]  extractDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> ;`) { 
    dtags = getTagContents(tags);
    adtName = "<user.name>";
    
    return [dataInfo(moduleName=moduleName, name=adtName, signature="data <user> <commonKeywordParameters>",
      src=d@\loc, synopsis=getSynopsis(dtags), docs=sortedDocTags(dtags))];
}

list[DeclarationInfo]  extractDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`) { 
    dtags = getTagContents(tags);
    adtName = "<user.name>";
    infoVariants = [ genVariant(moduleName, variant) | variant <- variants ];
    
    return dataInfo(moduleName=moduleName, name=adtName, signature="data <user> <commonKeywordParameters> <align(variants)>",
                                       src=d@\loc, synopsis=getSynopsis(dtags), docs=sortedDocTags(dtags)) + infoVariants;
}

DeclarationInfo genVariant(str moduleName, v: (Variant) `<Name name>(<{TypeArg ","}* _> <KeywordFormals _>)`) {
    signature = "<v>";
    return constructorInfo(moduleName=moduleName, name="<name>", signature="<v>", src=v@\loc);
}

list[DeclarationInfo]  extractDecl(str moduleName, d: (Declaration) `<FunctionDeclaration functionDeclaration>`) 
  = [ extractTestDecl(moduleName, functionDeclaration) ] when /FunctionModifier m := functionDeclaration.signature, (FunctionModifier) `test` := m;

default list[DeclarationInfo]  extractDecl(str moduleName, d: (Declaration) `<FunctionDeclaration functionDeclaration>`) 
  = [ extractFunDecl(moduleName, functionDeclaration) ];

// -- function declaration ------------------------------------------

DeclarationInfo extractFunDecl(str moduleName, fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> ;`)
  = extractFunctionDeclaration(moduleName, fd);

DeclarationInfo extractFunDecl(str moduleName, fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> ;`)
  = extractFunctionDeclaration(moduleName, fd);

DeclarationInfo extractFunDecl(str moduleName, fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> when <{Expression ","}+ conditions>;`) 
  = extractFunctionDeclaration(moduleName, fd); 


DeclarationInfo extractFunDecl(str moduleName, fd: (FunctionDeclaration) `<Tags tags>  <Visibility visibility> <Signature signature> <FunctionBody body>`)
  = extractFunctionDeclaration(moduleName, fd);
 
private DeclarationInfo extractFunctionDeclaration(str moduleName, FunctionDeclaration fd) {
  fname = "<fd.signature.name>";
  
  signature =  "<fd.signature>";
  if(startsWith(signature, "java")){
    signature = signature[size("java")+1 .. ];
  }
   
  tags =  getTagContents(fd.tags);
  
  return functionInfo(moduleName=moduleName, name=fname, signature=signature, src=fd@\loc, synopsis=getSynopsis(tags), docs=sortedDocTags(tags));
}

DeclarationInfo extractTestDecl(str moduleName, FunctionDeclaration fd) {
  fname = "<fd.signature.name>";
  
  signature =  "<fd.signature>";
  if(startsWith(signature, "java")){
    signature = signature[size("java")+1 .. ];
  }

  tags =  getTagContents(fd.tags);
  
  return testInfo(moduleName=moduleName, name=fname, src=fd@\loc, synopsis=getSynopsis(tags), fullTest="<fd>");
}

str getSynopsis(rel[str, DocTag] tags) {
    if (docTag(content=str docContents) <- tags["doc"]) {
      if ([*_, /^.Synopsis\s+<rest:.*>$/, *str cont, /^.[A-Za-z].*$/, *str _] := split("\n", docContents)) {
        return intercalate(" ", [rest, *cont]);
      }
      else if ([*_, /^#+\s*Synopsis\s+<rest:.*>$/, *str cont, /^.[A-Za-z].*$/, *str _] := split("\n", docContents)) {
        return intercalate(" ", [rest, *cont]);
      }
    }

    if (docTag(content=str docContents) <- tags["synopsis"]) {
      return trim(intercalate(" ", split("\n", docContents)));
    }
    else {
      return "";
    }
}


bool isTutorTag(str label) = label in {"doc", "synopsis", "syntax", "types", "details", "description", "examples", "benefits", "pitfalls"};

@synopsis{extracts the contents of _all_ tags from a declaration syntax tree and stores origin information}
rel[str, DocTag] getTagContents(Tags tags){
  m = {};
  for (tg <- tags.tags){
    str name = "<tg.name>";
    if (!isTutorTag(name)) {
      continue;
    }

    if (tg is \default) {
      cont = "<tg.contents>"[1 .. -1];
      m += <name, docTag(label=name, content=cont, src=tg.src)>;
    } else if (tg is empty) {
      m += <name, docTag(label=name, content="", src=tg.src)>;
    } else {
      m += <name, docTag(label=name, content="<tg.expression>"[1 .. -1], src=tg.src)>;
    }
  }

  return m;
}

@synopsis{lists the supported documentation tags in the prescribed order}
list[DocTag] sortedDocTags(rel[str, DocTag] tags) 
  = [ *tags["doc"],
      *tags["synopsis"],
      *tags["syntax"],
      *tags["types"],
      *tags["details"],
      *tags["description"],
      *tags["examples"],
      *tags["benefits"],
      *tags["pitfalls"]
    ];
