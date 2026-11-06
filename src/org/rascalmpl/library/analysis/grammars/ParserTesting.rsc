@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@synopsis{Reusable test functions to run (generated) parsers on a test corpus}
module analysis::grammars::ParserTesting

extend lang::rascal::\syntax::Rascal;

import Exception;
import Grammar;
import IO;
import List;
import Message;
import ParseTree;
import String;
import analysis::grammars::Ambiguity;
import lang::rascal::grammar::definition::Modules;
import lang::rascal::grammar::definition::Symbols;
import util::Monitor;
import util::Reflective;

start syntax ParserTestModule
    = @synopsis{A syntax for files that can contain many small test cases for a parser} 
    Tags tags "module" QualifiedName name Import* imports ParserTestCase* cases;

syntax ParserTestCase
    = "syntax" "test" Name? label "(" Sym symbol ")" "expects" ParserTestExpect expect TagString content;

syntax ParserTestExpect
    = ok: "ok"
    | ambiguous:"ambiguous" 
    | error: "error" 
    ;

alias FileParser[&T] = &T (loc org);


@synopsis{Run the tests embedded in a parser test module file and return true iff all the tests succeeded.}
@benefits{
* all failing tests are reported in standard fashion on stdout
* when grammars remain unchanged on disk, the tests reuse parsers from the previous run
}
@pitfalls{
* parser test module files have to be loaded, as well as Rascal module files, and converted to a parser, which involves critical file IO
and the right ((data:PathConfig)) instance.
}
bool reportParserTestModule(loc m, PathConfig pcfg=pathConfig()) {
    messages = runParserTestModule(m, pcfg=pcfg);
    println(write(messages));
    return /error(_,_) !:= messages;
}

@synopsis{Run the tests embedded in a parser test module file and return true iff all the tests succeeded.}
@benefits{
* all failing tests are reported in standard fashion as a `list[Message]`, which can be processed further downstream
by communicating to IDE diagnostics views, or as editor annotations, for example.
* when grammars remain unchanged on disk, the tests reuse parsers from the previous run
}
@pitfalls{
* parser test module files have to be loaded, as well as Rascal module files, and converted to a parser, which involves critical file IO
and the right ((data:PathConfig)) instance.
}
list[Message] runParserTestModule(loc m, PathConfig pcfg=pathConfig()) {
    try {
        return doRunParserTestModule(parse(#start[ParserTestModule], m).top, pcfg=pcfg);
    }
    catch ParseError(loc l): {
        return [error("parse error in test module", l)];
    }
}

@synopsis{The workhorse parses the test specifications, composes the main grammar, and iterates over each individual test}
private list[Message] doRunParserTestModule(ParserTestModule m, PathConfig pcfg=pathConfig()) {
    str caseLabel (ParserTestCase w) { 
        return "<w.label>@<w.src>"; 
    }

    try {
        set[Module] rascalModules = collectRascalModules(m.imports, {}, pcfg=pcfg);
        QualifiedName modName = m.name;
        Import* imports = m.imports;
        Module rascalMain = (Module) `module <QualifiedName modName> <Import* imports>`;
        Grammar gr = modules2grammar(deslash("<modName>"), {rascalMain, *rascalModules});

        if (type[Tree] rt := type(sort("Tree"), gr.rules)) {
            p = parsers(rt, allowAmbiguity=false, allowRecovery=false);
        
            return loopJob([c | c <- m.cases], Message (ParserTestCase c) {
                return runParserTestCase(c, Tree (type[Tree] nt, str input) { return p(nt, input, m.src.top); });
            }, 
            stepLabel = caseLabel);
        }
        else {
            return [error("unexpected error while generating parsers for <m.name>", m.src)];
        }
    }
    catch value e: {
        return [error("unexpected error while trying to test <m.name>: <e>", m.src)];
    }
}

private str deslash(str input) = visit(input) {
    case /\\/ => ""
};

private set[Module] collectRascalModules(Import* imports, set[str] done, PathConfig pcfg=pathConfig()) 
    = { *collectRascalModules(i , done, pcfg=pcfg) | i <- imports };

private set[Module] collectRascalModules((Import) `extend <QualifiedName name>;`, set[str] done, PathConfig pcfg=pathConfig()) 
    = collectRascalModules((Import) `import <QualifiedName name>;`, done, pcfg=pcfg);

private set[Module] collectRascalModules((Import) `import <QualifiedName name>;`, set[str] done, PathConfig pcfg=pathConfig()) {
    if (deslash("<name>") in done) {
        return {};
    }
    loc f = srcsFile(deslash("<name>"), pcfg.srcs, fileConfig());
    Module m = parseModule(f);

    return {m} + collectRascalModules(m.header.imports, done + deslash("<name>"), pcfg=pcfg);
}

private default set[Module] collectRascalModules(Import _, set[str] done, PathConfig pcfg=pathConfig()) 
    = {};

private Message runParserTestCase(ParserTestCase testCase, &T (type[&T <: Tree], str) theParser) {
    expect = testCase.expect;
    sym = testCase.symbol;
    content = "<testCase.content>";

    content = content[1..-1]; // drop the outermost { }
    content = replaceAll(content, "\\}", "}"); // deescape \}
    content = replaceAll(content, "\\{", "{"); // deescapoe \{

    if (!(sym is \start)) {
        // only start non-terminals can start and end with whitespace or comments
        content = trim(content);
    }

    // need number of escaped curlies to shift error positions in the spec
    int countEscapedCurlies(int off) {
        int result = 0;
        visit ("<testCase.content>"[1..off]) {
            case "\\{": result += 1;
            case "\\}": result += 1;
        }

        return result;
    }
    // shifts a parse error in an example string to the location of the spec in the module
    // TODO: account for de-escaped \{ and \} for `e.offset`
    loc errorLoc(loc e) = testCase.src.top(testCase.content.src.offset + 1 /* skip { */ + e.offset + countEscapedCurlies(e.offset), 1);

    try {
        if (type[Tree] nt := type(sym2symbol(sym), ())) {
            _forget = theParser(nt, content);
            if (expect is ok) {
                return info("test <testCase.label> ok", testCase.src);
            }
            else {
                return error("test <testCase.label> is unexpectedly ok. Was waiting for an <expect>.", testCase.src);
            }
        }
    }
    catch ParseError(loc l): {
        if (expect is error) {
            return info("test <testCase.label> has an expected error.", errorLoc(l));
        }
        else {
            return error("unexpected parse error in <testCase.label> test.", errorLoc(l));
        }
    }
    catch Ambiguity(loc l, _, _): {
        if (expect is ambiguous) {
            return info("test <testCase.label> has an expected ambiguity.", errorLoc(l));
        }
        else {
            return error("unexpected parse error in <testCase.label> test.", errorLoc(l));    
        }
    }
    
    return error("unexpected nil outcome of <testCase.label> test.", testCase.src);
}

@synopsis{Collect files of a specific extension from a folder recursively, while filtering ignored files and folders}
list[loc] constructParsingTestCorpus(loc root, set[str] extensions, set[loc] ignores={}) {
    if (root in ignores) {
        return [];
    }
    else if (isFile(root) && root.extension in extensions) {
        return [root];
    }
    else if (isDirectory(root)) {
        return [*constructParsingTestCorpus(e, extensions, ignores=ignores) | e <- root.ls];
    }
    else {
        return [];
    }
}

@synopsis{Run a generated parser on a corpus and print errors, diagnostics and summary statistics}
@description{
This functions returns true if and only if none of the files in the corpus exhibit a parse error or an ambiguity for the given grammar
}
@pitfalls{
* the grammar is best if it's a start symbol (since we are parsing files we need spaces before and after the top sort).
}
bool testCorpusForParseErrorsAndAmbiguity(type[&T <: Tree] grammar, list[loc] corpus, str jobLabel="Testing <grammar>", bool abort=false) {
    &T (value,loc) generated = parser(grammar, allowAmbiguity=false, allowRecovery=false);
    FileParser[&T] wrapped = &T (loc src) { 
        return generated(src, src); 
    };

    return testCorpusForParseErrorsAndAmbiguity(wrapped, corpus, jobLabel=jobLabel, abort=abort);
}

@synopsis{Run a parser on a corpus and print errors, diagnostics and summary statistics}
@description{
This functions returns true if and only if none of the files in the corpus exhibit a parse error or an ambiguity
}
@benefits{
* this is a typical simple task for every project that has a grammar
* ambiguity reported here can be analyzed using analysis::grammars::Ambiguity or the drambiguity project
}
@pitfalls{
* the parser must not `allowAmbiguity=true` 
* the parser must not `allowRecovery=true` either
* the parser must accept whitespace and comments at the beginning and end of the file
}
bool testCorpusForParseErrorsAndAmbiguity(FileParser[&T] parser, list[loc] corpus, str jobLabel="Testing parser ", bool abort=false) { 
    int success = 0;
    int errors = 0;
    int ambs = 0;
    int total = 0;
    
    try {
        loopVoidJob(corpus, (loc src) {
            try {
                total += 1;
                pt = parser(src);
                success += 1;
            }
            catch Ambiguity(loc l, str sym, str txt): {
                println("[ERROR] ambiguity: <l>");
                println("[INFO]    symbol : <sym>");
                println("[INFO]    source : `<txt>`");
                
                ambs += 1;

                if (abort) {
                    throw "aborted";
                }
            }
            catch ParseError(loc l): {
                println("[ERROR] parse error: <l>");
                errors += 1;

                if (abort) {
                    throw "aborted";
                }
            }
        }, label=jobLabel);
    }
    catch "aborted": {
        println("[INFO] aborted after <total> of <size(corpus)> files.");
    }
    
    println("[INFO] #total  : <total>");
    println("[INFO] #ok     : <success>");
    println("[INFO] #errors : <errors>");
    println("[INFO] #ambs   : <ambs>");

    return errors == 0 && ambs == 0;
}