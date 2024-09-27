module lang::rascal::tests::concrete::recovery::RecoveryTestSupport

import lang::rascal::\syntax::Rascal;
import ParseTree;
import String;
import IO;
import util::Benchmark;
import Grammar;
import analysis::statistics::Descriptive;
import util::Math;

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
    println("Statistics (average/median/95th percentile):");

    void printStats(str label, list[int] values, str unit) {
        int mean = sum(values)/size(values);
        print(left(label, 40));
        print(": ");
        println("<mean>/<round(median(values))>/<percentile(values, 95)> <unit>");
    }

    list[int] successfulParseTimes = [ duration | successfulParse(duration=duration) <- stats.measurements ];
    list[int] successfulRecoveryTimes = [ duration | recovered(duration=duration, errorSize=errorSize) <- stats.measurements, errorSize <= stats.recoverySuccessLimit ];
    list[int] failedRecoveryTimes = [ duration | recovered(duration=duration, errorSize=errorSize) <- stats.measurements, errorSize > stats.recoverySuccessLimit  ];
    list[int] parseErrorTimes = [ duration | parseError(duration=duration) <- stats.measurements ];

    printStats("Succesful parse time", successfulParseTimes, "ms");
    printStats("Succesful recovery time", successfulRecoveryTimes, "ms");
    printStats("Failed recovery time", failedRecoveryTimes, "ms");
    printStats("Parse error time", parseErrorTimes, "ms");

    list[int] errorSizes = [ errorSize | recovered(errorSize=errorSize) <- stats.measurements  ];
    printStats("Recovery error size", errorSizes, "characters");

    list[int] successfulErrorSizes = [ errorSize | recovered(errorSize=errorSize) <- stats.measurements, errorSize <= stats.recoverySuccessLimit ];
    printStats("Successful recovery size", successfulErrorSizes, "characters");
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

TestStats testErrorRecovery(loc syntaxFile, str topSort, loc testInput) = 
testErrorRecovery(syntaxFile, topSort, testInput, readFile(testInput));

TestStats testErrorRecovery(loc syntaxFile, str topSort, loc testInput, str input, int slowParseLimit=0, int recoverySuccessLimit=0) {
    Module \module = parse(#start[Module], syntaxFile).top;
    str modName = syntaxLocToModuleName(syntaxFile);
    Grammar gram = modules2grammar(modName, {\module});

    if (sym:\start(\sort(topSort)) <- gram.starts) {
        println("==========================================================================");
        println("Error recovery of <syntaxFile> (<topSort>) on <testInput>:");
        type[value] begin = type(sym, gram.rules);
        standardParser = parser(begin, allowAmbiguity=true, allowRecovery=false);
        recoveryParser = parser(begin, allowAmbiguity=true, allowRecovery=true);

        if (slowParseLimit == 0) {
        int startTime = realTime();
        standardParser(input, testInput);
        int referenceDuration = realTime() - startTime;
            slowParseLimit = referenceDuration*10;
        }

        if (recoverySuccessLimit == 0) {
            recoverySuccessLimit = size(input)/4;
        }

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
}TestStats batchRecoveryTest(loc syntaxFile, str topSort, loc dir, str ext, int maxFiles, int maxFileSize) {
    int count = 0;

    int slowParseLimit = 200;
    int recoverySuccessLimit = 100;

    TestStats totalStats = testStats(slowParseLimit, recoverySuccessLimit);

    println("Batch testing in directory <dir>");
    for (entry <- listEntries(dir)) {
        loc file = dir + entry;
        if (isFile(file)) {
            if (endsWith(file.path, ext)) {
                str content = readFile(file);
                if (size(content) <= maxFileSize) {
                    TestStats fileStats = testErrorRecovery(syntaxFile, topSort, file, content, slowParseLimit = slowParseLimit, recoverySuccessLimit = recoverySuccessLimit);
                    mergeStats(totalStats, fileStats);
                    count += 1;
                }
            }
        } else if (isDirectory(file)) {
            TestStats dirStats = batchRecoveryTest(syntaxFile, topSort, file, ext, maxFiles-count, maxFileSize);
            totalStats = mergeStats(totalStats, dirStats);
            count += size(dirStats.measurements);
        }

        if (count > maxFiles) {
            return totalStats;
        }
    }

    return totalStats;
}

