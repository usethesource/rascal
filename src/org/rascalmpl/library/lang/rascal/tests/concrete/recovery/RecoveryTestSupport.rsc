module lang::rascal::tests::concrete::recovery::RecoveryTestSupport

import lang::rascal::\syntax::Rascal;
import ParseTree;
import String;
import IO;
import util::Benchmark;
import Grammar;
import analysis::statistics::Descriptive;

import lang::rascal::grammar::definition::Modules;

public data TestMeasurement(loc source=|unknown:///|, int duration=0) = successfulParse() | recovered(int errorSize=0) | parseError();
public data TestStats = testStats(int slowParseLimit, int recoverySuccessLimit, int successfulParses=0, int successfulRecoveries=0, int failedRecoveries=0, int parseErrors=0, int slowParses=0, list[TestMeasurement] measurements=[]);

private TestMeasurement testRecovery(&T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, str input, loc source) {
    int startTime = 0;
    int duration = 0;
    TestMeasurement measurement = successfulParse();
    try {
        startTime = realTime();
        Tree t = standardParser(input, source);
        duration = realTime() - startTime;
        measurement = successfulParse(source=source, duration=duration);
    } catch ParseError(_): {
        startTime = realTime();
        try {
            Tree t = recoveryParser(input, source);
            duration = realTime() - startTime;
            Tree best = findBestError(t);
            errorSize = size(getErrorText(best));
            measurement = recovered(source=source, duration=duration, errorSize=errorSize);
        } catch ParseError(_): { 
            duration = realTime() - startTime;
            measurement = parseError(source=source, duration=duration);
        }
    }

    return measurement;
}

TestStats updateStats(TestStats stats, TestMeasurement measurement) {
    switch (measurement) {
        case successfulParse(): {
            print(".");
            stats.successfulParses += 1;
        }
        case recovered(errorSize=errorSize): 
            if (errorSize <= stats.recoverySuccessLimit) {
            print("+");
                stats.successfulRecoveries += 1;
        } else {
                print("-");
                stats.failedRecoveries += 1;
        }
        case parseError(): {
        print("?");
            stats.parseErrors += 1;
        }
    }
        
    if (measurement.duration > stats.slowParseLimit) {
        print("!");
        stats.slowParses += 1;
    }

    stats.measurements = stats.measurements + measurement;

    return stats;
}

TestStats mergeStats(TestStats stats1, TestStats stats2) {
    return testStats(
        stats1.slowParseLimit, stats1.recoverySuccessLimit,
        successfulParses = stats1.successfulParses + stats2.successfulParses,
        successfulRecoveries = stats1.successfulRecoveries + stats2.successfulRecoveries,
        failedRecoveries = stats1.failedRecoveries + stats2.failedRecoveries,
        parseErrors = stats1.parseErrors + stats2.parseErrors,
        slowParses = stats1.slowParses + stats2.slowParses,
        measurements = stats1.measurements + stats2.measurements);
}

TestStats testSingleCharDeletions(&T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, str input, int slowParseLimit, int recoverySuccessLimit) {
    TestStats stats = testStats(slowParseLimit, recoverySuccessLimit);
    int len = size(input);
    int i = 0;

    while (i < len) {
        str modifiedInput = substring(input, 0, i) + substring(input, i+1);
        TestMeasurement measurement = testRecovery(standardParser, recoveryParser, modifiedInput, |unknown:///?deleted=<"<i>">|);
        stats = updateStats(stats, measurement);
        i = i+1;
    }

    return stats;
}

TestStats testDeleteUntilEol(&T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, str input, int slowParseLimit, int recoverySuccessLimit) {
    TestStats stats = testStats(slowParseLimit, recoverySuccessLimit);
    int lineStart = 0;
    list[int] lineEndings = findAll(input, "\n");

    for (int lineEnd <- lineEndings) {
        lineLength = lineEnd - lineStart;
        for (int pos <- [lineStart..lineEnd]) {
            modifiedInput = substring(input, 0, pos) + substring(input, lineEnd);
            TestMeasurement measurement = testRecovery(standardParser, recoveryParser, modifiedInput, |unknown:///?deletedUntilEol=<"<pos>,<lineEnd>">|);
            stats = updateStats(stats, measurement);
        }
        lineStart = lineEnd+1;
    }

    return stats;
}

private int percentage(int number, int total) {
    return (100*number)/total;
}

void printStats(TestStats stats) {
    println();
    int measurementCount = size(stats.measurements);
    println("Total parses:         <measurementCount>");
    println("Succesful parses:     <stats.successfulParses> (<percentage(stats.successfulParses, measurementCount)> % of total)");
    int totalFailed = measurementCount - stats.successfulParses;
    println("Succesful recoveries: <stats.successfulRecoveries> (<percentage(stats.successfulRecoveries, totalFailed)> % of failed)");
    println("Failed recoveries:    <stats.failedRecoveries> (<percentage(stats.failedRecoveries, totalFailed)> % of failed)");
    println("Parse errors:         <stats.parseErrors> (<percentage(stats.parseErrors, totalFailed)> % of failed)");

    if (stats.slowParses == 0) {
        println("No slow parses.");
    } else {
        slowest = (getFirstFrom(stats.measurements) | it.duration < e.duration ? it : e | e <- stats.measurements);
        println("<stats.slowParses> slow parses, slowest parse: <slowest.source> (<slowest.duration> ms)");
    }

    println();
    println("95th percentiles:");

    list[int] successfulParseTimes = [ duration | successfulParse(duration=duration) <- stats.measurements ];
    list[int] successfulRecoveryTimes = [ duration | recovered(duration=duration, errorSize=errorSize) <- stats.measurements, errorSize <= stats.recoverySuccessLimit ];
    list[int] failedRecoveryTimes = [ duration | recovered(duration=duration, errorSize=errorSize) <- stats.measurements, errorSize > stats.recoverySuccessLimit  ];
    list[int] parseErrorTimes = [ duration | parseError(duration=duration) <- stats.measurements ];

    println("Succesful parse time:     <percentile(successfulParseTimes, 95)> ms");
    println("Succesful recovery time:  <percentile(successfulRecoveryTimes, 95)> ms");
    println("Failed recovery time:     <percentile(failedRecoveryTimes, 95)> ms");
    println("Parse error time:         <percentile(parseErrorTimes, 95)> ms");

    list[int] errorSizes = [ errorSize | recovered(errorSize=errorSize) <- stats.measurements  ];
    println("Recovery error size       <percentile(errorSizes, 95)> characters");

    list[int] successfulErrorSizes = [ errorSize | recovered(errorSize=errorSize) <- stats.measurements, errorSize <= stats.recoverySuccessLimit ];
    println("Successful recovery size: <percentile(successfulErrorSizes, 95)> characters");
}

private str syntaxLocToModuleName(loc syntaxFile) {
    str path = replaceLast(substring(syntaxFile.path, 1), ".rsc", "");
    return replaceAll(path, "/", "::");
}

loc zippedFile(str zip, str path) {
    loc res = getResource(zip);
    loc zipFile = res[scheme="jar+<res.scheme>"][path=res.path + "!/"];
    return zipFile + path;
}

TestStats testErrorRecovery(loc syntaxFile, str topSort, loc testInput) {
    Module \module = parse(#start[Module], syntaxFile).top;
    str modName = syntaxLocToModuleName(syntaxFile);
    Grammar gram = modules2grammar(modName, {\module});

    if (sym:\start(\sort(topSort)) <- gram.starts) {
        println("==========================================================================");
        println("Error recovery of <syntaxFile> (<topSort>) on <testInput>:");
        type[value] begin = type(sym, gram.rules);
        standardParser = parser(begin, allowAmbiguity=true, allowRecovery=false);
        recoveryParser = parser(begin, allowAmbiguity=true, allowRecovery=true);
        str input = readFile(testInput);

        int startTime = realTime();
        standardParser(input, testInput);
        int referenceDuration = realTime() - startTime;
        int slowParseLimit = referenceDuration*100;
        int recoverySuccessLimit = size(input)/4;

        println();
        println("Single char deletions:");
        TestStats singleCharDeletionStats = testSingleCharDeletions(standardParser, recoveryParser, input, slowParseLimit, recoverySuccessLimit);
        printStats(singleCharDeletionStats);
        TestStats totalStats = singleCharDeletionStats;

        println();
        println("Deletes until end-of-line:");
        TestStats deleteUntilEolStats = testDeleteUntilEol(standardParser, recoveryParser, input, slowParseLimit, recoverySuccessLimit);
        printStats(deleteUntilEolStats);
        totalStats = mergeStats(totalStats, deleteUntilEolStats);

        println();
        println("Overall stats");
        print("-------------");
        printStats(totalStats);
        println();

        return totalStats;
    } else {
        throw "Cannot find top sort <topSort> in <gram>";
    }
}