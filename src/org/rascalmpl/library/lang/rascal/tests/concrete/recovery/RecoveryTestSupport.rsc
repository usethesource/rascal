module lang::rascal::tests::concrete::recovery::RecoveryTestSupport

import ParseTree;
import String;
import IO;
import util::Benchmark;

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

void testRecovery(type[&T] grammar, loc input) {
    TestStats stats = testSingleCharDeletions(grammar, readFile(input));
    printStats(stats);
}