@bootstrapParser
module experiments::Compiler::Rascal2Info::Collect

import IO;
import Map;
import Set;
import String;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import util::Reflective;
//import experiments::Compiler::Rascal2muRascal::ParseModule;
import experiments::Compiler::Rascal2muRascal::TypeUtils;


import lang::rascal::types::AbstractName;
import lang::rascal::types::CheckTypes;

import experiments::Compiler::Rascal2muRascal::ModuleInfo;
import experiments::Compiler::Rascal2muRascal::TypeUtils;
import experiments::Compiler::Rascal2muRascal::TypeReifier;

import experiments::Compiler::Rascal2Info::DeclarationInfo;

import util::FileSystem;

//set[DeclarationInfo] crawl(loc startLoc, PathConfig pcfg){
//    return {*generate(mloc, pcfg) | mloc <- find(startLoc, bool(loc l) { return l.extension == "rsc"; }) };
//}

set[DeclarationInfo] collectInfo(list[str] qualifiedModuleNames, PathConfig pcfg){
    return {*collectInfo(moduleName, pcfg) | moduleName <- qualifiedModuleNames};
}

set[DeclarationInfo] collectInfo(str qualifiedModuleName, PathConfig pcfg){
    return collectInfo(getModuleLocation(qualifiedModuleName, pcfg), pcfg);
}

set[DeclarationInfo] collectInfo(loc moduleLoc, PathConfig pcfg){
    Configuration config;
    try {
        println("collectInfo: <moduleLoc>");
        M = parseModule(moduleLoc);
        config  = checkModule(M, newConfiguration(pcfg));
        resetModuleInfo();
      
        // Extract scoping information available from the configuration returned by the type checker  
        extractScopes(config); 
      
        // Extract all declarations for the benefit of the type reifier
        extractDeclarationInfo(config);
        return collectModule(M);
    } catch e: {
        throw e;
    } finally {
        resetModuleInfo();
        resetScopeExtraction();
    }
}

set[DeclarationInfo] collectModule(m: (Module) `<Header header> <Body body>`) {
    moduleName = "<header.name>";
    tags = getTags(header.tags);
    tls = { *collectTopLevel(moduleName, tl) |  tl <- body.toplevels };
    content = trim(tags["doc"] ? "");
    return moduleInfo(moduleName, m@\loc, getSynopis(content), content) + tls;
}

/********************************************************************/
/*                  Process declarations in a module                */
/********************************************************************/
    
set[DeclarationInfo] collectTopLevel(str moduleName, t: (Toplevel) `<Declaration decl>`) = collectDecl(moduleName, decl);

// -- variable declaration ------------------------------------------

set[DeclarationInfo]  collectDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> <Type tp> <{Variable ","}+ variables> ;`) {
    
return {};
}       

// -- miscellaneous declarations that can be skipped since they are handled during type checking ------------------

set[DeclarationInfo]  collectDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType>@<Name name> ;`) = { /*skip: translation has nothing to do here */ };
set[DeclarationInfo]  collectDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType user> = <Type base> ;`)   = { /* skip: translation has nothing to do here */ };
set[DeclarationInfo]  collectDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> tag <Kind kind> <Name name> on <{Type ","}+ types> ;`)  = { };

set[DeclarationInfo]  collectDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> ;`)  = { /* skip: translation has nothing to do here */ };

set[DeclarationInfo]  collectDecl(str moduleName, d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`) { 
    dtags = getTags(tags);
    name = "<user.name>";
    adtData = \adt(name, []);   // TODO is wrong
    infoVariants = { genVariant(moduleName, adtData, variant) | variant <- variants };
    content = trim(dtags["doc"] ? "");
    return dataInfo(moduleName, name, d@\loc, getSynopis(content), content) + infoVariants;
}

DeclarationInfo genVariant(str moduleName, Symbol adtData, v: (Variant) `<Name name>(<{TypeArg ","}* arguments> <KeywordFormals keywordArguments>)`){
    signature = "<v>";
    argTypes = [translateType(arg.\type) | arg <- arguments];
    return constructorInfo(moduleName, "<name>", \cons(adtData, "<name>", argTypes), "<v>", v@\loc);
}

set[DeclarationInfo]  collectDecl(str moduleName, d: (Declaration) `<FunctionDeclaration functionDeclaration>`) = { collectFunDecl(moduleName, functionDeclaration) };

// -- function declaration ------------------------------------------

DeclarationInfo collectFunDecl(str moduleName, fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> ;`)   {
  return collectFunctionDeclaration(moduleName, fd);
}

DeclarationInfo collectFunDecl(str moduleName, fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> ;`){
  return collectFunctionDeclaration(moduleName, fd);
}

DeclarationInfo collectFunDecl(str moduleName, fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> when <{Expression ","}+ conditions>;`){
  return collectFunctionDeclaration(moduleName, fd); 
}

DeclarationInfo collectFunDecl(str moduleName, fd: (FunctionDeclaration) `<Tags tags>  <Visibility visibility> <Signature signature> <FunctionBody body>`){
  return collectFunctionDeclaration(moduleName, fd);
}

private DeclarationInfo collectFunctionDeclaration(str moduleName, FunctionDeclaration fd){

  tags =  getTags(fd.tags);
  mods = getModifiers(fd.signature.modifiers);
 
  name = "<fd.signature.name>";
  ftype = getFunctionType(fd@\loc);
  signature =  "<fd.signature>";
  if(startsWith(signature, "java")){
    signature = signature[size("java")+1 .. ];
  }
  
  content = trim(tags["doc"] ? "");
  return functionInfo(moduleName, name, ftype, signature, fd@\loc, getSynopis(content), content);
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