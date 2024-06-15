@bootstrapParser
module lang::rascalcore::check::Summary

/*
    Generate a summary for the benefit of an IDE
    TODO: check out-of-date?
*/

extend lang::rascalcore::check::CheckerCommon;

import lang::rascalcore::check::ModuleStatus;
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
    tm = convertTModel2PhysicalLocs(tm);
    // Extract @doc and @synopsis tags
    map[loc def, str synopsis] synopses = ();
    map[loc def, loc docloc] docLocs = ();
    for(def <- tm.defines){
        tags = def.defInfo.tags;

        if(tags["synopsis"]? || tags["doc"]?){
            docContent = tags["synopsis"]? ? tags["synopsis"] : tags["doc"];
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
    n = findFirst(s, "\n");
    return trim(n < 0 ? s : s [ .. n]);
}

// Example and tests

ModuleSummary example1() {
    pcfg = pathConfig(
            srcs=[|std:///|], 
            bin = |project://rascal-core/target/test-classes|,
            generatedSources = |project://rascal-core/target/generated-test-sources|,
            resources = |project://rascal-core/target/generated-test-resources|,
            libs = []);
    return makeSummary("Boolean", pcfg);
}

value main(){
    iprintln(example1());
    return true;
}

// Simple sanity tests. Any change in Boolean.rsc will break these tests.
test bool synopsis1()
    = example1().synopses[ |project://rascal/src/org/rascalmpl/library/Boolean.rsc|(1538,254,<80,0>,<94,1>)] == "Convert Boolean value to string.";

test bool vocabulary1()
    = example1().vocabulary == {"Boolean","toString","toInt","toReal","fromString","arbBool"};

test bool usedef1() 
    = example1().useDef[|project://rascal/src/org/rascalmpl/library/Boolean.rsc|(935,1,<39,6>,<39,7>)] 
      == {|project://rascal/src/org/rascalmpl/library/Boolean.rsc|(923,1,<37,27>,<37,28>)};

test bool locationTypes1()
    = example1().locationTypes[|project://rascal/src/org/rascalmpl/library/Boolean.rsc|(1023,48,<45,8>,<45,56>)] == "RuntimeException";