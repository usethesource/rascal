module lang::rascal::tutor::apidoc::GenerateMarkdown

import List;
import String;
import util::Reflective;
import Location;
import Message;

import lang::rascal::tutor::apidoc::DeclarationInfo; 
import lang::rascal::tutor::apidoc::ExtractInfo;
import lang::rascal::tutor::Output;
import lang::rascal::tutor::Indexer;
import lang::rascal::tutor::Compiler;
import lang::rascal::tutor::repl::TutorCommandExecutor;
import lang::rascal::tutor::Names;
import IO;
import Node;

@synopsis{Generate markdown documentation from the declarations extracted from a Rascal module.}
@description{
    This function takes Rascal files as input, first extracts all declarations including their
    embedded (markdown) documentation tags, and then generates on-the-fly the output markdown
    as a list of lines and error messages. 
    
    This generator reuses the markdown compiler 
    to implement Rascal shell execution and concept linking, etc. This compilation is applied inside of the
    documentation tags that are written by the author of the Rascal code. The trick is to track the
    current line number inside those documentation tags to provide valuable feedback to the user
    of the tutor compiler.
}
list[Output] generateAPIMarkdown(str parent, loc moduleLoc, PathConfig pcfg, CommandExecutor exec, Index ind) {
    try {
        dinfo = extractInfo(moduleLoc);

        // filter the tests
        tests = [t | t:testInfo() <- dinfo];

        isDemo = DeclarationInfo k <- dinfo && k is moduleInfo && k.demo;

        // remove the tests
        dinfo -= tests;

        dtls = sort(dup(["<capitalize(pcfg.currentRoot.file)>:<i.kind>:<i.moduleName>::<i.name>" | DeclarationInfo i <- dinfo, !(i is moduleInfo)]));

        // TODO: this overloading collection should happen in ExtractInfo
        //
        // A declaration is a *secondary* overload (merged into the primary's signature list) when:
        //   (a) another declaration with the same (kind, name) has already appeared, AND
        //   (b) it carries no @synopsis of its own.
        // A declaration with a @synopsis always gets its own entry (primary), even if another
        // primary with the same name exists.
        //
        // Pre-pass: collect secondary signatures per (kind, name) so that non-consecutive
        // overloads separated by helper functions are not missed (fixes the consecutive-only bug).
        map[tuple[str,str], list[str]] secondarySigs = ();
        set[tuple[str,str]] seenInPrepass = {};
        for (DeclarationInfo d <- dinfo, d has name) {
            tuple[str,str] key = <getName(d), d.name>;
            if (key in seenInPrepass && d.synopsis == "") {
                str sig = (isDemo && d.fullFunction?) ? d.fullFunction : d.signature;
                secondarySigs[key] = (secondarySigs[key] ? []) + [sig];
            }
            seenInPrepass += {key};
        }

        // Emit pass: primaries are emitted with their collected secondary signatures appended;
        // secondaries are skipped.
        // Note: documentation tags on secondary overloads are still not surfaced — fixing that
        // requires merging them in ExtractInfo before this stage.
        set[tuple[str,str]] emitted = {};
        res = [];
        for (DeclarationInfo d <- dinfo) {
            if (d has name) {
                tuple[str,str] key = <getName(d), d.name>;
                bool isSecondary = key in emitted && d.synopsis == "";
                if (!isSecondary) {
                    str sig = (isDemo && d.fullFunction?) ? d.fullFunction : d.signature;
                    list[str] overloads = [sig] + (secondarySigs[key] ? []);
                    res += declInfo2Doc(parent, d, overloads, pcfg, exec, ind, d is moduleInfo ? dtls : [], isDemo);
                    emitted += {key};
                }
            } else {
                res += declInfo2Doc(parent, d, [], pcfg, exec, ind, d is moduleInfo ? dtls : [], isDemo);
            }
        }

        if (tests != []) {
            res += line("# Tests");
        }

        for (di <- tests) {
            res += declInfo2Doc(parent, di, [], pcfg, exec, ind, [], isDemo);
        }

        return res;
    }
    catch Java(_,_):
      return [err(error("parse error in source file", moduleLoc))];
    catch ParseError(loc l):
        return [err(error("parse error in source file", l))];
}

private map[str,str] escapes = ("\\": "\\\\", "\"": "\\\"");

list[Output] declInfo2Doc(str parent, d:moduleInfo(), list[str] overloads, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, bool demo) =
    [
        out("---"),
        out("title: \"module <"<[d.moduleName]>"[2..-2]>\""), // we make sure to escape backslashes here (e.g. lang::pico::\syntax::Main)
        out("id: <d.name>"),
        out("slug: <parent>/<d.name>"),
        out("---"),
        Output::empty(),
        out("\<div class=\"theme-doc-version-badge badge badge--secondary\"\>rascal-<getRascalVersion()>\</div\><if (pcfg.isPackageCourse) {> \<div class=\"theme-doc-version-badge badge badge--secondary\"\><pcfg.packageName>-<pcfg.packageVersion>\</div\><}>"),
        Output::empty(),
        *[out(synopsis.content) | synopsis:docTag(label="synopsis") <- d.docs],
        out("#### Usage"),
        Output::empty(),
        out("```rascal"),
        out("import <replaceAll(d.moduleName, "/", "::")>;"),
        out("```"),
        Output::empty(),
        *[out("#### Source code"),
          out("<(pcfg.sources + relativize(pcfg.packageRoot, pcfg.currentRoot).path) + relativize(pcfg.currentRoot, d.src).path>"[1..-1]),
          Output::empty() | pcfg.isPackageCourse, pcfg.sources?, pcfg.packageRoot?
        ],
        *[
            out("#### Dependencies"),
            out("```rascal"),
            *[ out(dep) | dep <- d.dependencies],
            out("```")
        | d.dependencies != []
        ],
        Output::empty(),
        *tags2Markdown(d.docs, pcfg, exec, ind, dtls, demo, descriptionHeader=((pcfg.sources? && pcfg.packageRoot?) || d.dependencies != [])),
        Output::empty()
    ];

list[Output] declInfo2Doc(str parent, d:functionInfo(), list[str] overloads, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, bool demo) =
    [
        out("## function <d.name> {<moduleFragment(d.moduleName)>-<d.name>}"),
        *[Output::empty(), out(synopsis.content) | synopsis:docTag(label="synopsis") <- d.docs],
        empty(),
        out("```rascal"),
        *([ *[out(defLine) | str defLine <- split("\n", ov)], empty() | ov <- overloads][..-1]),
        out("```"),
        Output::empty(),
        *tags2Markdown(d.docs, pcfg, exec, ind, dtls, demo)
    ];   

list[Output] declInfo2Doc(str parent, d:testInfo(), list[str] overloads, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, bool demo) =
    [
        out("## test <d.name> {<moduleFragment(d.moduleName)>-<d.name>}"),
        *[Output::empty(), out(synopsis.content) | synopsis:docTag(label="synopsis") <- d.docs],
        Output::empty(),
        out("```rascal"),
        *[out(defLine) | str defLine <- split("\n", d.fullTest)],
        out("```"),
        Output::empty(),
        *tags2Markdown(d.docs, pcfg, exec, ind, dtls, demo)
    ];       
 
 list[Output] declInfo2Doc(str parent, constructorInfo(), list[str] overloads, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, bool demo) =
     [];
    
 list[Output] declInfo2Doc(str parent, d:dataInfo(), list[str] overloads, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, bool demo) =
    [
        out("## data <d.name> {<moduleFragment(d.moduleName)>-<d.name>}"),
        *[out(synopsis.content) | synopsis:docTag(label="synopsis") <- d.docs],
        empty(),
        *[
            out("```rascal"),
            *[out(defLine) | str defLine <- split("\n", ov)], 
            out("```"),
            empty()
        | ov <- overloads
        ],
         *tags2Markdown(d.docs, pcfg, exec, ind, dtls, demo)
    ]; 

list[Output] declInfo2Doc(str parent, d:syntaxInfo(), list[str] overloads, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, bool demo) =
    [
        out("## syntax <d.name> {<moduleFragment(d.moduleName)>-<d.name>}"),
        *[Output::empty(), out(synopsis.content) | synopsis:docTag(label="synopsis") <- d.docs],
        empty(),
        *[
            out("```rascal"),
            *[out(defLine) | str defLine <- split("\n", ov)], 
            out("```"),
            empty()
        | ov := d.signature
        ],
         *tags2Markdown(d.docs, pcfg, exec, ind, dtls, demo)
    ]; 

list[Output] declInfo2Doc(str parent, d:aliasInfo(), list[str] overloads, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, bool demo) =
    [
        out("## alias <d.name> {<moduleFragment(d.moduleName)>-<d.name>}"),
        *[Output::empty(), out(synopsis.content) | synopsis:docTag(label="synopsis") <- d.docs],
        empty(),
        out("```rascal"),
        *[out(removeNewlines(ov)), empty() | ov <- overloads][..-1],
        out("```"),
        empty(),
        *tags2Markdown(d.docs, pcfg, exec, ind, dtls, demo)
    ];
       
default list[Output] declInfo2Doc(str parent, DeclarationInfo d, list[str] overloads, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, bool demo) 
    = [err(info("No content generated for <d>", d.src))];

list[Output] tags2Markdown(list[DocTag] tags, PathConfig pcfg, CommandExecutor exec, Index ind, list[str] dtls, bool _demo, bool descriptionHeader=false) 
    = [
        // every doc tag has its own header title, except the "doc" tag which may contain them all (backward compatibility)
        // and description starts without a header to improver the ratio between content and structure in the documentation for smaller functions and modules
        *(l notin {"doc", (!descriptionHeader) ? "description" : ""} ? [out("#### <capitalize(l)>"), empty()] : []),
        
        // here is where we get the origin information into the right place for error reporting:
        *compileMarkdown(split("\n", c), s.begin.line, s.offset, pcfg, exec, ind, dtls),

        empty() 

        // this assumes that the doc tags have been ordered correctly already by the extraction stage
        | docTag(label=str l, src=s, content=str c) <- tags, l != "synopsis"
    ];

public str basename(str cn){
  return (/^.*::<base:[A-Za-z0-9\-\_]+>$/ := cn) ? base : cn;
}

str removeNewlines(str x) = visit(x) {
  case /\n/ => " "
};



