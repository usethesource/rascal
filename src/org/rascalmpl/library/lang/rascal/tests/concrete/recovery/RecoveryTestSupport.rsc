
@bootstrapParser
module lang::rascal::tests::concrete::recovery::RecoveryTestSupport

import lang::rascal::\syntax::Rascal;
import ParseTree;
import String;
import IO;
import util::Benchmark;
import Grammar;

import lang::rascal::grammar::definition::Modules;


public data TestStats = testStats(int totalAttempts=0, int successfulParses=0, int successfulRecoveries=0, int failedRecoveries=0, int parseErrors=0, list[tuple[loc,int]] slowParses = []);

private TestStats testRecovery(&T (value input, loc origin) parser, str input, loc source) {
    TestStats stats = testStats(totalAttempts=1);

    int begin = realTime();
    try {
        Tree t = parser(input, source);
        if (hasErrors(t)) {
            stats.successfulRecoveries = 1;
            print("+");

            //errors = findAllErrors(t);
            //count = size(errors);
            //best = findBestError(t);
            //println("successful recovery, <count> errors found, best: <getSkipped(best)>, loc: <best@\src>");
            //for (err <- errors) {
            //    println("<getSkipped(err)>");
            //}
        } else {
            stats.successfulParses = 1;
            print(".");            
        }
    } catch ParseError(_): { 
        stats.parseErrors = 1;
        print("?");
    }
    int duration = realTime() - begin;
        
    slowParse = [];
    if (duration > 200) {
        stats.slowParses = [<source, duration>];
        print("!");
    }

    return stats;
}

TestStats testSingleCharDeletions(type[&T] grammar, str input) = testSingleCharDeletions(parser(grammar, allowAmbiguity=true, allowRecovery=true), input);

TestStats testSingleCharDeletions(&T (value input, loc origin) parser, str input) {
    TestStats totalStats = testStats();
    int len = size(input);
    int i = 0;

    while (i < len) {
        str modifiedInput = substring(input, 0, i) + substring(input, i+1);
        TestStats singleRunStats = testRecovery(parser, modifiedInput, |uknown:///?deleted=<"<i>">|);
        totalStats = mergeStats(totalStats, singleRunStats);
        i = i+1;
    }

    return totalStats;
}

TestStats mergeStats(TestStats stats1, TestStats stats2) {
    TestStats result = stats1;
    result.totalAttempts += stats2.totalAttempts;
    result.successfulParses += stats2.successfulParses;
    result.successfulRecoveries += stats2.successfulRecoveries;
    result.failedRecoveries += stats2.failedRecoveries;
    result.parseErrors += stats2.parseErrors;
    result.slowParses += stats2.slowParses;
    return result;
}

private int percentage(int number, int total) {
    return (100*number)/total;
}

void printStats(TestStats stats) {
    println();
    println("Total parses:         <stats.totalAttempts>");
    println("Succesful parses:     <stats.successfulParses> (<percentage(stats.successfulParses, stats.totalAttempts)> % of total)");
    int totalFailed = stats.totalAttempts - stats.successfulParses;
    println("Succesful recoveries: <stats.successfulRecoveries> (<percentage(stats.successfulRecoveries, totalFailed)> % of failed)");
    println("Failed recoveries:    <stats.failedRecoveries> (<percentage(stats.failedRecoveries, totalFailed)> % of failed)");
    println("Parse errors:         <stats.parseErrors> (<percentage(stats.parseErrors, totalFailed)> % of failed)");

    if (stats.slowParses == []) {
        println("No slow parses.");
    } else {
        println("<size(stats.slowParses)> slow parses:");
        for (<source,duration> <- stats.slowParses) {
            println("<source>: <duration> ms.");
        }
    }
}

private str syntaxLocToModuleName(loc syntaxFile) {
    str path = replaceLast(substring(syntaxFile.path, 1), ".rsc", "");
    return replaceAll(path, "/", "::");
}

loc zippedFile(str zip, str path) {
    loc res = getResource("m3/snakes-and-ladders-project-source.zip");
    loc zipFile = res[scheme="jar+<res.scheme>"][path=res.path + "!/"];
    return zipFile + path;
}

void testErrorRecovery(loc syntaxFile, str topSort, loc testInput) {
    Module \module = parse(#start[Module], syntaxFile).top;
    str modName = syntaxLocToModuleName(syntaxFile);
    gram = modules2grammar(modName, {\module});

    if (sym:\start(\sort(topSort)) <- gram.starts) {
        println("Error recovery of <syntaxFile> (<topSort>) on <testInput>:");
        TestStats stats = testSingleCharDeletions(type(sym, gram.rules), readFile(testInput));
    printStats(stats);
    } else {
        println("Cannot find top sort <topSort> in <gram>");
    }
}