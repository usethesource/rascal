@bootstrapParser
module experiments::Compiler::Rascal2Info::Generate

import IO;
import Map;
import Set;
import String;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import util::Reflective;
import experiments::Compiler::Rascal2muRascal::ParseModule;
import experiments::Compiler::Rascal2muRascal::TypeUtils;


import lang::rascal::types::AbstractName;
import lang::rascal::types::CheckTypes;

import experiments::Compiler::Rascal2muRascal::ModuleInfo;
import experiments::Compiler::Rascal2muRascal::TypeUtils;
import experiments::Compiler::Rascal2muRascal::TypeReifier;

import experiments::Compiler::Rascal2Info::DeclarationInfo;

import util::FileSystem;

set[DeclarationInfo] crawl(loc startLoc, PathConfig pcfg){
    return {*generate(mloc, pcfg) | mloc <- find(startLoc, bool(loc l) { return l.extension == "rsc"; }) };
}

set[DeclarationInfo] generate(list[str] qualifiedModuleNames, PathConfig pcfg){
    return {*generate(moduleName, pcfg) | moduleName <- qualifiedModuleNames};
}

set[DeclarationInfo] generate(str qualifiedModuleName, PathConfig pcfg){
    return generate(getModuleLocation(qualifiedModuleName, pcfg), pcfg);
}

set[DeclarationInfo] generate(loc moduleLoc, PathConfig pcfg){
    Configuration config;
    try {
        println("generate: <moduleLoc>");
        M = parseModuleGetTop(moduleLoc);
        config  = checkModule(M, newConfiguration(pcfg));
        resetModuleInfo();
      
        // Extract scoping information available from the configuration returned by the type checker  
        extractScopes(config); 
      
        // Extract all declarations for the benefit of the type reifier
        extractDeclarationInfo(config);
        return genModule(M);
    } catch e: {
        throw e;
    } finally {
        resetModuleInfo();
        resetScopeExtraction();
    }
}

set[DeclarationInfo] genModule(m: (Module) `<Header header> <Body body>`) {
    moduleName = "<header.name>";
    tags = getTags(header.tags);
    tls = { *genTopLevel(moduleName, tl) |  tl <- body.toplevels };
    if(tags["doc"]?){
        content = trim(tags["doc"]);
        return moduleInfo(moduleName, m@\loc, doc=content, synopsis=getSynopis(content)) + tls;
    } else {
        return moduleInfo(moduleName, m@\loc) + tls;
    }
}

/********************************************************************/
/*                  Process declarations in a module                */
/********************************************************************/
    
set[DeclarationInfo] genTopLevel(str moduleName, t: (Toplevel) `<Declaration decl>`) = genDecl(moduleName, decl);

// -- variable declaration ------------------------------------------

set[DeclarationInfo]  genDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> <Type tp> <{Variable ","}+ variables> ;`) {
    
return {};
}       

// -- miscellaneous declarations that can be skipped since they are handled during type checking ------------------

set[DeclarationInfo]  genDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType>@<Name name> ;`) = { /*skip: translation has nothing to do here */ };
set[DeclarationInfo]  genDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType user> = <Type base> ;`)   = { /* skip: translation has nothing to do here */ };
set[DeclarationInfo]  genDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> tag <Kind kind> <Name name> on <{Type ","}+ types> ;`)  = { };

set[DeclarationInfo]  genDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> ;`)  = { /* skip: translation has nothing to do here */ };

set[DeclarationInfo]  genDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`) { 
    dtags = getTags(tags);
    name = "<user.name>";
    adtData = \adt(name, []);   // TODO is wrong
    infoVariants = { genVariant(moduleName, adtData, variant) | variant <- variants };
    if(dtags["doc"]?){
       content = trim(dtags["doc"]);
       return dataInfo(moduleName, name, d@\loc, doc=content, synopsis=getSynopis(content)) + infoVariants;
    } else {
      return dataInfo(moduleName, name, d@\loc) + infoVariants;
    }
}

DeclarationInfo genVariant(str moduleName, Symbol adtData, v: (Variant) `<Name name>(<{TypeArg ","}* arguments> <KeywordFormals keywordArguments>)`){
    signature = "<v>";
    argTypes = [translateType(arg.\type) | arg <- arguments];
    return constructorInfo(moduleName, "<name>", \cons(adtData, "<name>", argTypes), "<v>", v@\loc);
}

set[DeclarationInfo]  genDecl(str moduleName, d: (Declaration) `<FunctionDeclaration functionDeclaration>`) = { genFunDecl(moduleName, functionDeclaration) };

// -- function declaration ------------------------------------------

DeclarationInfo genFunDecl(str moduleName, fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> ;`)   {
  return genFunctionDeclaration(moduleName, fd);
}

DeclarationInfo genFunDecl(str moduleName, fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> ;`){
  return genFunctionDeclaration(moduleName, fd);
}

DeclarationInfo genFunDecl(str moduleName, fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> when <{Expression ","}+ conditions>;`){
  return genFunctionDeclaration(moduleName, fd); 
}

DeclarationInfo genFunDecl(str moduleName, fd: (FunctionDeclaration) `<Tags tags>  <Visibility visibility> <Signature signature> <FunctionBody body>`){
  return genFunctionDeclaration(moduleName, fd);
}

private DeclarationInfo genFunctionDeclaration(str moduleName, FunctionDeclaration fd){

  tags =  getTags(fd.tags);
  mods = getModifiers(fd.signature.modifiers);
 
  name = "<fd.signature.name>";
  ftype = getFunctionType(fd@\loc);
  signature =  "<fd.signature>";
  if(startsWith(signature, "java")){
    signature = signature[size("java")+1 .. ];
  }
  
  if(tags["doc"]?){
    content = trim(tags["doc"]);
    return functionInfo(moduleName, name, ftype, signature, fd@\loc, doc=content, synopsis=getSynopis(content));
  } else {
    return functionInfo(moduleName, name, ftype, signature, fd@\loc);
  }
}

/********************************************************************/
/*      Get tags in a function declaration                          */
/********************************************************************/

str getSynopis(str docContents){
    s = trim(docContents);
    synopsis = "Synopsis: ";
    if(startsWith(s, synopsis)){
        s = s[size(synopsis) ..];
        return trim(s[ .. findFirst(s, "\n")]);
    }
    return s [ .. findFirst(s, "\n")];
}
public map[str,str] getTags(Tags tags){
   m = ();
   for(tg <- tags.tags){
     str name = "<tg.name>";
     if(tg is \default){
        cont = "<tg.contents>"[1 .. -1];
        m[name] = cont;
     } else if (tg is empty)
        m[name] = "";
     else
        m[name] = "<tg.expression>"[1 .. -1];
   }
   return m;
}

/********************************************************************/
/*       Get the modifiers in a function declaration          */
/********************************************************************/

private list[str] getModifiers(FunctionModifiers modifiers){
   lst = [];
   for(m <- modifiers.modifiers){
     if(m is \java) 
       lst += "java";
     else if(m is \test)
       lst += "test";
     else
       lst += "default";
   }
   return lst;
} 