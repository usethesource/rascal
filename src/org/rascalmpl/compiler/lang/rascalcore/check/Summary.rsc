@bootstrapParser
module lang::rascalcore::check::Summary

extend lang::rascalcore::check::CheckerCommon;
//extend lang::rascalcore::check::ATypeUtils;
//import lang::rascalcore::check::BasicRascalConfig;
//
//extend analysis::typepal::TypePal;

import lang::rascalcore::check::Import;
import analysis::typepal::TModel;

import util::Reflective;

import IO;
import Relation;
import String;
import ValueIO;


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

private map[loc from, str tp] getLocationTypes(TModel tm)
    = (key : prettyAType(tm.specializedFacts[key] ? tm.facts[key]) | key <- tm.facts);
    
ModuleSummary makeSummary(TModel tm, str qualifiedModuleName) {
    // Extract @doc tags
    map[loc def, str synopsis] synopses = ();
    map[loc def, loc docloc] docLocs = ();
    for(def <- tm.defines){
        tags = def.defInfo.tags;

        if(tags["doc"]?){
            //println("<def>: <def.defInfo.tags>");
            docContent = tags["doc"] ? "";
            if(!isEmpty(docContent)){
                synopsis = getSynopsis(docContent);
                if(!isEmpty(synopsis)){
                    synopses[def.defined] = synopsis;
                }
            }
            docloc = replaceAll(qualifiedModuleName, "::", "/") + "#" + def.id;
            docLocs[def.defined] = |courses:///<docloc>|;
        }
    }
    // Synthesize the summary  
    return moduleSummary()
        [locationTypes=getLocationTypes(tm)]
        [useDef=getUseDef(tm)]
        [vocabulary=getVocabulary(tm)]
        [synopses=synopses]
        [docLocs=docLocs];
    //return visit(result) {
    //    case loc l => l[fragment=""] // clean the timestamps of the locs
    //};
}    
    
@doc{
.Synopsis
Make a ModuleSummary.
}
ModuleSummary makeSummary(str qualifiedModuleName, PathConfig pcfg){
    if(<true, tplLoc> := getTPLReadLoc(qualifiedModuleName, pcfg)){
        try {
            return makeSummary(readBinaryValueFile(#TModel, tplLoc), qualifiedModuleName);
        } catch IO(_): {
            return moduleSummary();
        }
    }
    else {
        return moduleSummary();
    }
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
    return makeSummary("experiments::Compiler::Examples::Fac", p1);
}

value main(){
    p1 = pathConfig(srcs=[|file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/|]);
    summary = makeSummary("experiments::Compiler::Examples::Fac", p1);
    println(summary);
    return true;
}
