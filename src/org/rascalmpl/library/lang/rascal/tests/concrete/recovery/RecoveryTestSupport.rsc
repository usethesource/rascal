module lang::rascal::tests::concrete::recovery::RecoveryTestSupport

import lang::rascal::\syntax::Rascal;
import ParseTree;
import String;
import IO;
import util::Benchmark;
import Grammar;
import analysis::statistics::Descriptive;
import util::Math;
import util::Maybe;
import Set;
import List;

import lang::rascal::grammar::definition::Modules;

alias FrequencyTable = map[int val, int count];

public data TestMeasurement(loc source=|unknown:///|, int duration=0) = successfulParse() | recovered(int errorSize=0) | parseError() | successfulDisambiguation();
public data FileStats = fileStats(int totalParses = 0, int successfulParses=0, int successfulRecoveries=0, int successfulDisambiguations=0, int failedRecoveries=0, int parseErrors=0, int slowParses=0, FrequencyTable parseTimeRatios=());

public data TestStats = testStats(int filesTested=0, int testCount=0, FrequencyTable successfulParses=(), FrequencyTable successfulRecoveries=(), FrequencyTable successfulDisambiguations=(), FrequencyTable failedRecoveries=(), FrequencyTable parseErrors=(), FrequencyTable slowParses=(), FrequencyTable parseTimeRatios=());

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
            Maybe[Tree] best = findBestError(t);
            totalDuration = realTime() - startTime;
            println("parse time: <duration>, disamb time: <totalDuration-duration>");
            if (nothing() := best) {
                measurement = successfulDisambiguation(source=source, duration=duration);
            } else {
                errorSize = size(getErrorText(best.val));
                measurement = recovered(source=source, duration=duration, errorSize=errorSize);
            }
        } catch ParseError(_): { 
            duration = realTime() - startTime;
            measurement = parseError(source=source, duration=duration);
        }
    }

    return measurement;
}

FileStats updateStats(FileStats stats, TestMeasurement measurement, int referenceParseTime, int recoverySuccessLimit) {
    stats.totalParses += 1;

    int ratio = referenceParseTime == 0 ? measurement.duration : measurement.duration/referenceParseTime;
    int parseTimeRatio = ratio == 0 ? 0 : round(log2(ratio));

    switch (measurement) {
        case successfulParse(): {
            print(".");
            stats.successfulParses += 1;
        }
        case recovered(errorSize=errorSize): {
            stats.parseTimeRatios = increment(stats.parseTimeRatios, parseTimeRatio);
            if (errorSize <= recoverySuccessLimit) {
                print("+");
                stats.successfulRecoveries += 1;
            } else {
                print("-");
                stats.failedRecoveries += 1;
            }
        }
        case successfulDisambiguation(): {
            print("&");
            stats.successfulDisambiguations += 1;
        }
        case parseError(): {
            stats.parseTimeRatios = increment(stats.parseTimeRatios, parseTimeRatio);
            print("?");
            stats.parseErrors += 1;
        }
    }

    if (measurement.duration > referenceParseTime*10) {
        print("!");
        stats.slowParses += 1;
    }

    return stats;
}

FileStats mergeFileStats(FileStats stats1, FileStats stats2) {
    return fileStats(
        totalParses = stats1.totalParses + stats2.totalParses,
        successfulParses = stats1.successfulParses + stats2.successfulParses,
        successfulRecoveries = stats1.successfulRecoveries + stats2.successfulRecoveries,
        successfulDisambiguations = stats1.successfulDisambiguations + stats2.successfulDisambiguations,
        failedRecoveries = stats1.failedRecoveries + stats2.failedRecoveries,
        parseErrors = stats1.parseErrors + stats2.parseErrors,
        slowParses = stats1.slowParses + stats2.slowParses,
        parseTimeRatios = mergeFrequencyTables(stats1.parseTimeRatios, stats2.parseTimeRatios)
    );
}

FrequencyTable increment(FrequencyTable frequencyTable, int val) {
    if (val in frequencyTable) {
        frequencyTable[val] += 1;
    } else {
        frequencyTable[val] = 1;
    }

    return frequencyTable;
}

TestStats consolidateStats(TestStats cumulativeStats, FileStats fileStats) {
    int totalFailed = fileStats.totalParses - fileStats.successfulParses;

    cumulativeStats.successfulParses = increment(cumulativeStats.successfulParses, percentage(fileStats.successfulParses, fileStats.totalParses));
    cumulativeStats.successfulRecoveries = increment(cumulativeStats.successfulRecoveries, percentage(fileStats.successfulRecoveries, totalFailed));
    cumulativeStats.successfulDisambiguations = increment(cumulativeStats.successfulDisambiguations, percentage(fileStats.successfulDisambiguations, totalFailed));
    cumulativeStats.failedRecoveries = increment(cumulativeStats.failedRecoveries, percentage(fileStats.failedRecoveries, totalFailed));
    cumulativeStats.parseErrors = increment(cumulativeStats.parseErrors, percentage(fileStats.parseErrors, totalFailed));
    cumulativeStats.slowParses = increment(cumulativeStats.slowParses, percentage(fileStats.slowParses, totalFailed));
    cumulativeStats.parseTimeRatios = mergeFrequencyTables(cumulativeStats.parseTimeRatios, fileStats.parseTimeRatios);

    cumulativeStats.filesTested += 1;
    cumulativeStats.testCount += fileStats.totalParses;

    return cumulativeStats; 
}

map[int,int] mergeFrequencyTables(map[int,int] hist1, map[int,int] hist2) {
    for (int pct <- hist2) {
        if (pct in hist1) {
            hist1[pct] += hist2[pct];
        } else {
            hist1[pct] = hist2[pct];
        }
    }

    return hist1;
}

TestStats mergeStats(TestStats stats, TestStats stats2) {
    stats.filesTested += stats2.filesTested;
    stats.testCount += stats2.testCount;
    stats.successfulParses = mergeFrequencyTables(stats.successfulParses, stats2.successfulParses);
    stats.successfulRecoveries = mergeFrequencyTables(stats.successfulRecoveries, stats2.successfulRecoveries);
    stats.successfulDisambiguations = mergeFrequencyTables(stats.successfulDisambiguations, stats2.successfulDisambiguations);
    stats.failedRecoveries = mergeFrequencyTables(stats.failedRecoveries, stats2.failedRecoveries);
    stats.parseErrors = mergeFrequencyTables(stats.parseErrors, stats2.parseErrors);
    stats.slowParses = mergeFrequencyTables(stats.slowParses, stats2.slowParses);
    stats.parseTimeRatios = mergeFrequencyTables(stats.parseTimeRatios, stats2.parseTimeRatios);

    return stats; 
}

FileStats testSingleCharDeletions(&T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, str input, int referenceParseTime, int recoverySuccessLimit, int begin=0, int end=-1) {
    FileStats stats = fileStats();
    int len = size(input);
    int i = begin;

    while (i < len && (end == -1 || i<=end)) {
        str modifiedInput = substring(input, 0, i) + substring(input, i+1);
        TestMeasurement measurement = testRecovery(standardParser, recoveryParser, modifiedInput, |unknown:///?deleted=<"<i>">|);
        stats = updateStats(stats, measurement, referenceParseTime, recoverySuccessLimit);
        if (i < len && substring(input, i, i+1) == "\n") {
            println();
        }
        i = i+1;
    }

    return stats;
}

FileStats testDeleteUntilEol(&T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, str input, int referenceParseTime, int recoverySuccessLimit, int begin=0, int end=-1) {
    FileStats stats = fileStats();
    int lineStart = begin;
    list[int] lineEndings = findAll(input, "\n");

    for (int lineEnd <- lineEndings) {
        lineLength = lineEnd - lineStart;
        for (int pos <- [lineStart..lineEnd]) {
            // Check boundaries (only used for quick bug testing)
            if (end != -1 && end < pos) {
                return stats;
            }
            if (pos < begin) {
                continue;
            }

            modifiedInput = substring(input, 0, pos) + substring(input, lineEnd);
            TestMeasurement measurement = testRecovery(standardParser, recoveryParser, modifiedInput, |unknown:///?deletedUntilEol=<"<pos>,<lineEnd>">|);
            stats = updateStats(stats, measurement, referenceParseTime, recoverySuccessLimit);
        }
        lineStart = lineEnd+1;
        println();
    }

    return stats;
}

private int percentage(int number, int total) {
    return total == 0 ? 0 : (100*number)/total;
}

int statLabelWidth = 40;
int statFieldWidth = 8;


void printFileStats(FileStats fileStats) {
    void printStat(str label, int stat, int total, bool prints=true) {
        int pct = total == 0 ? 0 : stat*100/total;
        print(left(label + ":", statLabelWidth));
        str pctStr = prints ? " (<pct>%)" : "";
        println(left("<stat><pctStr>", statFieldWidth));
    }

    println();
    printStat("Total parses", fileStats.totalParses, fileStats.totalParses);
    printStat("Successful parses", fileStats.successfulParses, fileStats.totalParses);
    int failedParses = fileStats.totalParses - fileStats.successfulParses;
    printStat("Successful recoveries", fileStats.successfulRecoveries, failedParses);
    printStat("Failed recoveries", fileStats.failedRecoveries, failedParses);
    printStat("Parse errors", fileStats.parseErrors, failedParses);
    printStat("Slow parses", fileStats.slowParses, failedParses);
    printFrequencyTableHeader();
    printFrequencyTableStats("Parse time ratios", fileStats.parseTimeRatios, unit = "log2(ratio)");
}

void printFrequencyTableHeader() {
    print(left("", statLabelWidth+1));
    print(right("mean", statFieldWidth));
    print(right("median", statFieldWidth));
    print(right("95%", statFieldWidth));
    print(right("min", statFieldWidth));
    print(right("max", statFieldWidth));
    println(right("total", statFieldWidth));
}

void printFrequencyTableStats(str label, FrequencyTable frequencyTable, str unit = "%") {
    print(left(label + " (<unit>):", statLabelWidth));

    int totalCount = (0 | it+frequencyTable[val] | val <- frequencyTable);

    int total = 0;
    int median = 0;
    int medianCount = 0;
    int cumulativeCount = 0;
    int ninetyFivePercentileLimit = round(totalCount * 0.95);
    int ninetyFivePercentile = -1;
    int minVal = 1000000000;
    int maxVal = -1000000000;

    for (val <- sort(toList(frequencyTable.val))) {
        minVal = min(minVal, val);
        maxVal = max(maxVal, val);

        int count = frequencyTable[val];
        cumulativeCount += count;

        if (ninetyFivePercentile == -1 && cumulativeCount >= ninetyFivePercentileLimit) {
            ninetyFivePercentile = val;
        }

        total += val*count;

        if (count > medianCount) {
            medianCount = count;
            median = val;
        }
    }

    if (totalCount == 0) {
        print("-");
    } else {
        int mean = total/totalCount;
        print(right("<mean>", statFieldWidth));
        print(right("<median>", statFieldWidth));
        print(right("<ninetyFivePercentile>", statFieldWidth));
        print(right("<minVal>", statFieldWidth));
        print(right("<maxVal>", statFieldWidth));
        println(right("<totalCount>", statFieldWidth));
    }
}

void printStats(TestStats stats) {
    if (stats.filesTested != 1) {
        println("Files tested:         <stats.filesTested>");
    }
    println("Total parses:         <stats.testCount>");
    printFrequencyTableHeader();
    printFrequencyTableStats("Succesful parses", stats.successfulParses);
    printFrequencyTableStats("Succesful recoveries", stats.successfulRecoveries);
    printFrequencyTableStats("Succesful disambiguations", stats.successfulRecoveries);
    printFrequencyTableStats("Failed recoveries", stats.failedRecoveries);
    printFrequencyTableStats("Parse errors", stats.parseErrors);
    printFrequencyTableStats("Slow parses", stats.slowParses);
    printFrequencyTableStats("Parse time ratios", stats.parseTimeRatios, unit = "log2/%");

    println();
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

FileStats testErrorRecovery(loc syntaxFile, str topSort, loc testInput) = testErrorRecovery(syntaxFile, topSort, testInput, readFile(testInput));

FileStats testErrorRecovery(loc syntaxFile, str topSort, loc testInput, str input) {
    Module \module = parse(#start[Module], syntaxFile).top;
    str modName = syntaxLocToModuleName(syntaxFile);
    Grammar gram = modules2grammar(modName, {\module});

    if (sym:\start(\sort(topSort)) <- gram.starts) {
        type[value] begin = type(sym, gram.rules);
        standardParser = parser(begin, allowAmbiguity=true, allowRecovery=false);
        recoveryParser = parser(begin, allowAmbiguity=true, allowRecovery=true);

        int startTime = realTime();
        standardParser(input, testInput);
        int referenceParseTime = realTime() - startTime;

        recoverySuccessLimit = size(input)/4;

        println("Error recovery of <syntaxFile> (<topSort>) on <testInput>, reference parse time: <referenceParseTime> ms.");

        println();
        println("Single char deletions:");
        FileStats singleCharDeletionStats = testSingleCharDeletions(standardParser, recoveryParser, input, referenceParseTime, recoverySuccessLimit);
        printFileStats(singleCharDeletionStats);

        println();
        println("Deletes until end-of-line:");
        FileStats deleteUntilEolStats = testDeleteUntilEol(standardParser, recoveryParser, input, referenceParseTime, recoverySuccessLimit);
        printFileStats(deleteUntilEolStats);

        FileStats stats = mergeFileStats(singleCharDeletionStats, deleteUntilEolStats);
        println();
        println("-----------------------------------------------------------");
        println("Total test stats for <testInput>:");
        printFileStats(stats);
        return stats;
    } else {
        throw "Cannot find top sort <topSort> in <gram>";
    }
}

TestStats batchRecoveryTest(loc syntaxFile, str topSort, loc dir, str ext, int maxFiles, int maxFileSize, TestStats cumulativeStats=testStats()) {
    println("Batch testing in directory <dir> (maxFiles=<maxFiles>, maxFileSize=<maxFileSize>)");
    for (entry <- listEntries(dir)) {
        loc file = dir + entry;
        if (isFile(file)) {
            if (endsWith(file.path, ext)) {
                str content = readFile(file);
                if (size(content) <= maxFileSize) {
                    println("========================================================================");
                    println("Testing file #<cumulativeStats.filesTested> <file> (<maxFiles-cumulativeStats.filesTested> of <maxFiles> left)");
                    FileStats fileStats = testErrorRecovery(syntaxFile, topSort, file, content);
                    cumulativeStats = consolidateStats(cumulativeStats, fileStats);
                    println();
                    println("------------------------------------------------------------------------");
                    println("Cumulative stats after testing <file>:");
                    printStats(cumulativeStats);
                }
            }
        } else if (isDirectory(file)) {
            cumulativeStats = batchRecoveryTest(syntaxFile, topSort, file, ext, maxFiles, maxFileSize, cumulativeStats=cumulativeStats);
        }

        if (cumulativeStats.filesTested >= maxFiles) {
            break;
        }
    }

    return cumulativeStats;
}

