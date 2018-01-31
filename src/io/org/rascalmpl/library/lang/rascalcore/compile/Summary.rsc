module lang::rascalcore::compile::Summary

import util::Reflective;
import Type;
import IO;
import ValueIO;
import Relation;
import String;
import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::types::CheckerConfig;
import lang::rascal::types::CheckTypes;

import lang::rascalcore::compile::RVM::AST;

// TODO duplicate from Compile:
loc RVMModuleWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "rvm", pcfg);

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
     moduleSummary(map[loc from, str tp] locationTypes = (),
                   rel[loc from, loc to] useDef = {},
                   set[str] vocabulary = {},
                   map[loc def, str synopsis] synopses = (),
                   map[loc def, loc docLoc] docLocs = ());

private map[loc from, str tp] getLocationTypes(Configuration c) =
    (from : prettyPrintType(c.locationTypes[from]) | from <- c.locationTypes);
    
private rel[loc from, loc to] getUseDef(Configuration c){
    mod_use_def = {<name@at, at> | int uid <- c.store, m:\module(RName name, loc at) := c.store[uid], name@at?};
    definitions = c.definitions;
    uses = invert(c.uses + c.narrowedUses);
    return mod_use_def + (uses o definitions); 
}

private set[str] getVocabulary(Configuration c) = {name | /RSimpleName(str name) := c};

@doc{
.Synopsis
Make a ModuleSummary.
}
ModuleSummary makeSummary(str qualifiedModuleName, PathConfig pcfg){
   ms = moduleSummary();
   if(<true, cloc> := cachedConfigReadLoc(qualifiedModuleName,pcfg)){
      try {
      Configuration c = readBinaryValueFile(#Configuration, cloc);
      ms = moduleSummary()
        [locationTypes=getLocationTypes(c)]
        [useDef=getUseDef(c)]
        [vocabulary=getVocabulary(c)];
      } catch IO(str msg): {
        /* ignore read failure */;
      }
   }
   rvmModuleLoc = RVMModuleWriteLoc(qualifiedModuleName, pcfg);
   if(exists(rvmModuleLoc)){
        try {
            rvmMod = readBinaryValueFile(#RVMModule, rvmModuleLoc); 
            map[loc def, str synopsis] synopses = ();
            map[loc def, loc docloc] docLocs = ();
            for(RVMDeclaration rvmDecl <- rvmMod.declarations){
                if(rvmDecl has tags){
                   docContent = rvmDecl.tags["doc"] ? "";
                   if(!isEmpty(docContent)){
                       synopsis = getSynopsis(docContent);
                       if(!isEmpty(synopsis)){
                        synopses[rvmDecl.src] = synopsis;
                       }
                       docloc = replaceAll(rvmDecl.qname[0 .. findFirst(rvmDecl.qname, "/")], "::", "/") + "#" + rvmDecl.uqname;
                       docLocs[rvmDecl.src] = |courses:///<docloc>|;
                   }
                }
            }
            ms = ms[synopses=synopses][docLocs=docLocs];
        } catch IO(str msg): {
         /* ignore read failure */;
        }
   }
   return ms;           
}

@doc{
.Synopsis
Get all definitions for a given use.
}
set[loc] getDefinitions(ModuleSummary summary, loc use){
    return summary.useDef[use] ? {};
}

@doc{
.Synopsis
Get the (pretty printed) type for a given use.
}
str getType(ModuleSummary summary, loc use){
    return summary.locationTypes[use] ? "";
}

@doc{
.Synopsis
Get all definitions for a given definition.
}
set[loc] getUses(ModuleSummary s, loc def){
    return invert(s.useDef)[def];
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

str getSynopsis(str docContents){
    s = trim(docContents);
    synopsisHeader = ".Synopsis\n";
    if(startsWith(s, synopsisHeader)){
        s = s[size(synopsisHeader) ..];
    }
    n = findFirst(s, "\n");
    return trim(n < 0 ? s : s [ .. n]);
}

ModuleSummary example1() {
    p1 = pathConfig(srcs=[|file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/|]);
    return makeSummary("lang::rascalcore::compile::Examples::Fac", p1);
}

value main(){
    p1 = pathConfig(srcs=[|file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/|]);
    summary = makeSummary("lang::rascalcore::compile::Examples::Fac", p1);
    println(summary);
    return true;
}
