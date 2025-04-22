/**
 * Copyright (c) 2024-2025, NWO-I Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/

module lang::rascal::tests::concrete::recovery::RecoveryTestSupport

import lang::rascal::\syntax::Rascal;
import ParseTree;
import util::ParseErrorRecovery;
import String;
import IO;
import util::Benchmark;
import Grammar;
import analysis::statistics::Descriptive;
import util::Math;
import Set;
import List;
import Exception;
import vis::Text;

import lang::rascal::grammar::definition::Modules;

int pruneCount = 10;

public data RecoveryTestConfig = recoveryTestConfig(
    loc syntaxFile = |unknown:///|,
    str topSort = "",
    loc dir = |unknown:///|,
    str ext = "",
    int maxFiles = 1000000,
    int minFileSize = 0,
    int maxFileSize = 1000000000,
    int fromFile = 0,
    loc statFile = |unknown:///|
);

alias FrequencyTable = map[int val, int count];

public data TestMeasurement(loc source=|unknown:///|, int duration=0)
    = successfulParse()
    | recovered(int errorCount=0, int errorSize=0)
    | parseError()
    | successfulDisambiguation()
    | skipped();

public data FileStats = fileStats(
    int totalParses = 0,
    int successfulParses=0,
    int successfulRecoveries=0,
    int successfulDisambiguations=0,
    int failedRecoveries=0,
    int parseErrors=0,
    int slowParses=0,
    FrequencyTable parseTimeRatios=(),
    FrequencyTable errorCounts=(),
    FrequencyTable errorSizes=());

public data TestStats = testStats(
    int filesTested=0,
    int testCount=0,
    FrequencyTable successfulParses=(),
    FrequencyTable successfulRecoveries=(),
    FrequencyTable successfulDisambiguations=(),
    FrequencyTable failedRecoveries=(),
    FrequencyTable parseErrors=(),
    FrequencyTable slowParses=(),
    FrequencyTable parseTimeRatios=(),
    FrequencyTable errorCounts=(),
    FrequencyTable errorSizes=());

@javaClass{org.rascalmpl.library.util.ParseErrorRecovery}
java int countTreeNodes(&T<:Tree tree);

@javaClass{org.rascalmpl.library.util.ParseErrorRecovery}
java int countUniqueTreeNodes(&T<:Tree tree);

@javaClass{org.rascalmpl.library.util.ParseErrorRecovery}
java int countUniqueTreeNodes(Tree tree);
@javaClass{org.rascalmpl.library.util.ParseErrorRecovery}
java int countTreeNodes(Tree tree);

private TestMeasurement testRecovery(RecoveryTestConfig config, &T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, str input, loc source, int referenceParseTime) {
    int startTime = 0;
    int duration = 0;
    int disambDuration = -1;
    int errorCount = 0;
    int errorSize=0;
    str result = "?";
    int nodeCount = 0;
    int nodeCountUnique = 0;
    int disambNodeCount = 0;
    int disambNodeCountUnique = 0;
    str prunedStats = "";

    TestMeasurement measurement = successfulParse();
    try {
        startTime = realTime();
        Tree tree = standardParser(input, source);
        duration = realTime() - startTime;
        measurement = successfulParse(source=source, duration=duration);
        result = "success";
        nodeCount = countTreeNodes(tree);
        nodeCountUnique = countUniqueTreeNodes(tree);
        disambNodeCount = nodeCount;
        disambNodeCountUnique = nodeCountUnique;
    } catch ParseError(_): {
        startTime = realTime();
        try {
            Tree tree = char(0);
            int parseEndTime = startTime;

            Tree t = recoveryParser(input, source);
            tree = t;
            parseEndTime = realTime();
            duration = parseEndTime - startTime;
                        
            if (tree == char(0)) {
                result = "skipped";
                measurement = skipped(source=source);
            } else {
                Tree disambTree = disambiguateParseErrors(tree);
                disambDuration = realTime() - parseEndTime;
                
                nodeCount = countTreeNodes(tree);
                nodeCountUnique = countUniqueTreeNodes(tree);
                disambNodeCount = countTreeNodes(disambTree);
                disambNodeCountUnique = countUniqueTreeNodes(disambTree);

                list[Tree] errors = findAllParseErrors(disambTree);
                errorCount = size(errors);
                if ("<tree>" != input) {
                    throw "Yield of recovered tree does not match the original input";
                }
                if (errors == []) {
                    measurement = successfulDisambiguation(source=source, duration=duration);
                } else {
                    errorSize = (0 | it + size(getErrorText(err)) | err <- errors);
                    measurement = recovered(source=source, duration=duration, errorCount=errorCount, errorSize=errorSize);
                }
                result = "recovery";

                // amb filtering stats
                for (int i <- [0..pruneCount]) {
                    Tree pruned = pruneAmbiguities(tree, maxDepth=i);
                    int unique = countUniqueTreeNodes(pruned);
                    int total= countTreeNodes(pruned);
                    prunedStats += ",<total>,<unique>";
                }
            }
        } catch ParseError(_): {
            result = "error"; 
            duration = realTime() - startTime;
            measurement = parseError(source=source, duration=duration);
        }
    }

    if (config.statFile != |unknown:///|) {
        int ratio = percent(duration, referenceParseTime);
        if (prunedStats == "") {
            for (int i <- [0..pruneCount]) {
                prunedStats += ",<nodeCount>,<nodeCountUnique>";
            }
        }

        appendToFile(config.statFile, "<source>,<size(input)>,<result>,<duration>,<ratio>,<disambDuration>,<errorCount>,<errorSize>,<nodeCount>,<nodeCountUnique>,<disambNodeCount>,<disambNodeCountUnique><prunedStats>\n");
    }

    return measurement;
}

FileStats updateStats(FileStats stats, TestMeasurement measurement, int referenceParseTime, int recoverySuccessLimit) {
    stats.totalParses += 1;

    int ratio = measurement.duration/referenceParseTime;
    int parseTimeRatio = ratio == 0 ? 0 : round(log2(ratio));

    switch (measurement) {
        case successfulParse(): {
            print(".");
            stats.successfulParses += 1;
        }
        case recovered(errorCount=errorCount, errorSize=errorSize): {
            stats.parseTimeRatios = increment(stats.parseTimeRatios, parseTimeRatio);
            stats.errorCounts = increment(stats.errorCounts, errorCount);
            stats.errorSizes = increment(stats.errorSizes, errorSize);
            if (errorSize <= recoverySuccessLimit) {
                print("+");
                stats.successfulRecoveries += 1;
            } else {
                print("-");
                stats.failedRecoveries += 1;
            }
        }
        case successfulDisambiguation(): {
            stats.parseTimeRatios = increment(stats.parseTimeRatios, parseTimeRatio);
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
        parseTimeRatios = mergeFrequencyTables(stats1.parseTimeRatios, stats2.parseTimeRatios),
        errorCounts = mergeFrequencyTables(stats1.errorCounts, stats2.errorCounts),
        errorSizes = mergeFrequencyTables(stats1.errorSizes, stats2.errorSizes)
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
    cumulativeStats.errorCounts = mergeFrequencyTables(cumulativeStats.errorCounts, fileStats.errorCounts);
    cumulativeStats.errorSizes = mergeFrequencyTables(cumulativeStats.errorSizes, fileStats.errorSizes);

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
    stats.errorCounts = mergeFrequencyTables(stats.errorCounts, stats2.errorCounts);
    stats.errorSizes = mergeFrequencyTables(stats.errorSizes, stats2.errorSizes);

    return stats; 
}

// Backwards compatible version
FileStats testSingleCharDeletions(&T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, loc source, str input, int referenceParseTime, int recoverySuccessLimit, int begin=0, int end=-1, loc statFile=|unknown:///|)
    = testSingleCharDeletions(recoveryTestConfig(statFile=statFile), standardParser, recoveryParser, source, input, referenceParseTime, recoverySuccessLimit, begin=begin, end=end);

FileStats testSingleCharDeletions(RecoveryTestConfig config, &T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, loc source, str input, int referenceParseTime, int recoverySuccessLimit, int begin=0, int end=-1) {
    FileStats stats = fileStats();
    int len = size(input);
    int i = begin;

    while (i < len && (end == -1 || i<=end)) {
        str modifiedInput = substring(input, 0, i) + substring(input, i+1);
        source.query = "deletedChar=<i>";
        TestMeasurement measurement = testRecovery(config, standardParser, recoveryParser, modifiedInput, source, referenceParseTime);
        stats = updateStats(stats, measurement, referenceParseTime, recoverySuccessLimit);
        if (i < len && substring(input, i, i+1) == "\n") {
            println();
        }
        i = i+1;
    }

    return stats;
}

// Backwards compatible version
FileStats testDeleteUntilEol(&T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, loc source, str input, int referenceParseTime, int recoverySuccessLimit, int begin=0, int end=-1, loc statFile=|unknown:///|) 
    = testDeleteUntilEol(recoveryTestConfig(statFile=statFile), standardParser, recoveryParser, source, input, referenceParseTime, recoverySuccessLimit, begin=begin, end=end);

str getDeleteUntilEolInput(str input, int begin) {
    int lineStart = 0;
    list[int] lineEndings = findAll(input, "\n");

    int line = 0;
    for (int lineEnd <- lineEndings) {
        line = line+1;
        if (lineEnd < begin) {
            continue;
        }
        lineLength = lineEnd - lineStart;
        for (int pos <- [lineStart..lineEnd]) {
            if (pos < begin) {
                continue;
            }
            if (pos > begin) {
                throw "Position not found";
            }
            return substring(input, 0, pos) + substring(input, lineEnd);
        }
        lineStart = lineEnd+1;
    }

    throw "Line not found";
}

FileStats testDeleteUntilEol(RecoveryTestConfig config, &T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, loc source, str input, int referenceParseTime, int recoverySuccessLimit, int begin=0, int end=-1) {
    FileStats stats = fileStats();
    int lineStart = 0;
    list[int] lineEndings = findAll(input, "\n");

    int line = 0;
    for (int lineEnd <- lineEndings) {
        line = line+1;
        if (lineEnd < begin) {
            continue;
        }
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
            source.query = "deletedUntilEol=<line>:<pos>:<lineEnd>";
            TestMeasurement measurement = testRecovery(config, standardParser, recoveryParser, modifiedInput, source, referenceParseTime);
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
int statFieldWidth = 10;


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
    printStat("Successful disambiguations", fileStats.successfulDisambiguations, failedParses);
    printStat("Failed recoveries", fileStats.failedRecoveries, failedParses);
    printStat("Parse errors", fileStats.parseErrors, failedParses);
    printStat("Slow parses", fileStats.slowParses, failedParses);
    printFrequencyTableHeader();
    printFrequencyTableStats("Parse time ratios", fileStats.parseTimeRatios, unit = "log2(ratio)", printTotal=false);
    printFrequencyTableStats("Parse error count", fileStats.errorCounts, unit="errors");
    printFrequencyTableStats("Error size", fileStats.errorSizes, unit="chars");
}

void printFrequencyTableHeader() {
    print(left("", statLabelWidth+1));
    print(right("mean", statFieldWidth));
    print(right("median", statFieldWidth));
    print(right("95 %", statFieldWidth));
    print(right("min", statFieldWidth));
    print(right("max", statFieldWidth));
    println(right("total", statFieldWidth));
}

void printFrequencyTableStats(str label, FrequencyTable frequencyTable, str unit = "%", bool printTotal=true, bool ignoreZero=false) {
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

        if (!(val == 0 && ignoreZero) && count > medianCount) {
            medianCount = count;
            median = val;
        }
    }

    if (totalCount == 0) {
        print("-");
    } else {
        num mean = round(toReal(total)/totalCount, 0.01);
        print(right("<mean>", statFieldWidth));
        print(right("<median>", statFieldWidth));
        print(right("<ninetyFivePercentile>", statFieldWidth));
        print(right("<minVal>", statFieldWidth));
        print(right("<maxVal>", statFieldWidth));
        println(right("<printTotal ? total : totalCount>", statFieldWidth));
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
    printFrequencyTableStats("Succesful disambiguations", stats.successfulDisambiguations);
    printFrequencyTableStats("Failed recoveries", stats.failedRecoveries);
    printFrequencyTableStats("Parse errors", stats.parseErrors);
    printFrequencyTableStats("Slow parses", stats.slowParses);
    printFrequencyTableStats("Parse time ratios", stats.parseTimeRatios, unit = "log2/%", printTotal=false);
    printFrequencyTableStats("Parse error counts", stats.errorCounts, unit = "errors", ignoreZero=true);
    printFrequencyTableStats("Parse error sizes", stats.errorSizes, unit = "chars", ignoreZero=true);

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

FileStats testErrorRecovery(loc syntaxFile, str topSort, loc testInput) = 
    testErrorRecovery(recoveryTestConfig(syntaxFile=syntaxFile, topSort=topSort), testInput);

FileStats testErrorRecovery(RecoveryTestConfig config, loc testInput) = testErrorRecovery(config, testInput, readFile(testInput));

FileStats testErrorRecovery(RecoveryTestConfig config, loc testInput, str input) {
    Module \module = parse(#start[Module], config.syntaxFile).top;
    str modName = syntaxLocToModuleName(config.syntaxFile);
    Grammar gram = modules2grammar(modName, {\module});

    str topSort = config.topSort;
    if (sym:\start(\sort(topSort)) <- gram.starts) {
        type[value] begin = type(sym, gram.rules);
        standardParser = parser(begin, allowAmbiguity=true, allowRecovery=false);
        recoveryParser = parser(begin, allowAmbiguity=true, allowRecovery=true);

        // Initialization run
        standardParser(input, testInput);

        // Timed run
        int startTime = realTime();
        standardParser(input, testInput);
        int referenceParseTime = max(1, realTime() - startTime);

        recoverySuccessLimit = size(input)/4;

        println("Error recovery of <config.syntaxFile> (<topSort>) on <testInput>, reference parse time: <referenceParseTime> ms.");

        println();
        println("Single char deletions:");
        FileStats singleCharDeletionStats = testSingleCharDeletions(config, standardParser, recoveryParser, testInput, input, referenceParseTime, recoverySuccessLimit);
        printFileStats(singleCharDeletionStats);

        println();
        println("Deletes until end-of-line:");
        FileStats deleteUntilEolStats = testDeleteUntilEol(config, standardParser, recoveryParser, testInput, input, referenceParseTime, recoverySuccessLimit);
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

private int fileNr = 0;


TestStats batchRecoveryTest(RecoveryTestConfig config) {
    fileNr = 0;

    if (config.statFile != |unknown:///|) {
        str pruneHeader = "";
        for (int i <- [0..pruneCount]) {
            pruneHeader += ",prune<i>,uprune<i>";
        }
        writeFile(config.statFile, "source,size,result,duration,ratio,disambiguationDuration,errorCount,errorSize,nodes,unodes,disambNodes,udisambNodes<pruneHeader>\n");
    }

    return runBatchRecoveryTest(config, testStats());
}

list[loc] gatherFiles(loc dir, str ext, int maxFiles, int minFileSize, int maxFileSize, int fromFile) {
    list[loc] files = [];
    for (entry <- listEntries(dir)) {
        loc file = dir + entry;
        if (isFile(file)) {
            if (endsWith(file.path, ext)) {
                str content = readFile(file);
                int contentSize = size(content);
                if (contentSize >= minFileSize && contentSize < maxFileSize) {
                    fileNr += 1;
                    if (fileNr < fromFile) {
                        println("Skipping file #<fileNr>: <file>, (\< <fromFile>)");
                        continue;
                    }
                    files += file;
                }
            }
        } else if (isDirectory(file)) {
            files += gatherFiles(file, ext, maxFiles-size(files), minFileSize, maxFileSize, fromFile);
        }

        if (size(files) >= maxFiles) {
            break;
        }
    }

    return files;
}

TestStats runBatchRecoveryTest(RecoveryTestConfig config, TestStats cumulativeStats) {
    list[loc] files = gatherFiles(config.dir, config.ext, config.maxFiles, config.minFileSize, config.maxFileSize, config.fromFile);
    int fileCount = size(files);
    println("Batch testing <fileCount> files");
    println("Config: <config>");

    int index = 0;
    for (file <- files) {
        str content = readFile(file);
        index += 1;
        println("========================================================================");
        println("Testing file #<index> <file> (<fileCount-cumulativeStats.filesTested> of <fileCount> left)");
        FileStats fileStats = testErrorRecovery(config, file, content);
        cumulativeStats = consolidateStats(cumulativeStats, fileStats);
        println();
        println("------------------------------------------------------------------------");
        println("Cumulative stats after testing <file>:");
        printStats(cumulativeStats);
    }

    return cumulativeStats;
}

str getTestInput(loc testUri) {
    str query = testUri.query;
    loc file = testUri[query = ""];

    str input = readFile(file);
    if (/deletedUntilEol=[0-9]*:<begin:[0-9]*>:<end:[0-9]*>/ := query) {
        println("deleteUntilEol: begin=<begin>, end=<end>");
        return getDeleteUntilEolInput(input, toInt(begin));
    } else if (/deletedChar=<index:[0-9]*>/ := query) {
        int at = toInt(index);
        return substring(input, 0, at) + substring(input, at+1);
    }

    throw "Unsupported test location: <testUri>";
}