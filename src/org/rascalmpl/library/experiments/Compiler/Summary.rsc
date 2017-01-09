module experiments::Compiler::Summary

import util::Reflective;
import Type;
import IO;
import ValueIO;
import Relation;
import lang::rascal::types::AbstractName;
import lang::rascal::types::CheckerConfig;
import lang::rascal::types::CheckTypes;

@doc{
.Synopsis
Summary of a Rascal module for use in IDE

.Description
A `ModuleSummary` summarizes a Rascal module for the benefit of IDE support like
* Show type of current symbol.
* Goto definition.
* Show uses.
* Show documentation.
* Name completion.
}
data ModuleSummary =
     moduleSummary(rel[loc from, Symbol tp] types = {},
                   rel[loc from, loc to] uses = {},
                   rel[str category, str word] vocabulary = {});

private rel[loc from, Symbol tp] getLocationTypes(Configuration c) =
    {<k,c.locationTypes[k]> | k <- c.locationTypes};
     
rel[loc from, loc to] getUseDef(Configuration c){
    definitions = c.definitions;
    uses = c.uses + c.narrowedUses;
    return {<use, def> | <int uid, loc def> <- definitions, loc use <- (uses[uid] ? {})};
}

private rel[str,str] getVocabulary(Configuration c) 
  = {<"any",name> | /RSimpleName(str name) := c};

@doc{
.Synopsis
Make a ModuleSummary.
}
ModuleSummary makeSummary(str qualifiedModuleName, PathConfig pcfg){
   if(<true, cloc> := cachedConfigReadLoc(qualifiedModuleName,pcfg)){
      Configuration c = readBinaryValueFile(#Configuration, cloc);
      return moduleSummary()
        [types=getLocationTypes(c)]
        [uses=getUseDef(c)]
        [vocabulary=getVocabulary(c)];
   } else {
      return moduleSummary();
   }    
   return;            
}

@doc{
.Synopsis
Get all definitions for a given use.
}
set[loc] getDefinitions(ModuleSummary summary, loc use){
    return summary.uses[use] ? {};
}

@doc{
.Synopsis
Get the type for a given use.
}
Symbol getType(ModuleSummary summary, loc use){
    return Symbol s <- summary.types[use] ? s : Symbol::\value();
}

@doc{
.Synopsis
Get all definitions for a given definition.
}
set[loc] getUses(ModuleSummary s, loc def){
    return invert(s.uses)[def];
}

@doc{
.Synopsis
Get the doc string for a given definition.
}
str getDocForDefinition(loc def){
    try {
        d = readFile(def);
        // TODO: Take care of nested  brackets in doc content
        return /@doc\{<content:[^}]*>\}/ := d ? content : "";
    } catch e: {
        return "";
    }
}