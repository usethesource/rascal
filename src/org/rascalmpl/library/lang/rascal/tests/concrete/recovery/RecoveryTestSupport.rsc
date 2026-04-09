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
import ValueIO;
import util::Eval;


import lang::rascal::grammar::definition::Modules;

public data RecoveryTestConfig = recoveryTestConfig(
    loc syntaxFile = |unknown:///|,
    str syntaxModule = "",
    str topSort = "",
    int maxAmbDepth = 2,
    loc dir = |unknown:///|,
    str ext = "",
    int maxFiles = 1000000,
    int minFileSize = 0,
    int maxFileSize = 1000000000,
    int maxRecoveryAttempts = 50,
    int maxRecoveryTokens = 3,
    int fromFile = 0,
    int sampleWindow = 1,
    bool countNodes = false,
    bool verifyResult = false,
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

@javaClass{org.rascalmpl.library.util.ParseErrorRecovery}
java &T<:Tree pruneAmbiguities(&T<:Tree t, int maxDepth=3);


private TestMeasurement testRecovery(RecoveryTestConfig config, &T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, str input, loc source, int referenceParseTime, int referenceNodeCount, int referenceNodeCountUnique) {
    int startTime = 0;
    int duration = 0;
    int disambDuration = -1;
    int errorCount = -1;
    int errorSize = -1;
    str result = "?";
    int nodeCount = -1;
    int nodeCountUnique = -1;
    int disambNodeCount = -1;
    int disambNodeCountUnique = -1;

    TestMeasurement measurement = successfulParse();
    startTime = realTime();
    try {
        Tree tree = recoveryParser(input, source);
        int parseEndTime = realTime();

        duration = parseEndTime - startTime;
        if (config.countNodes) {
            nodeCount = countTreeNodes(tree);
            nodeCountUnique = countUniqueTreeNodes(tree);
        }

        if (hasParseErrors(tree)) {
            if (config.countNodes) {
                Tree disambTree = disambiguateParseErrors(tree);
                disambDuration = realTime() - parseEndTime;
                disambNodeCount = nodeCount;
                disambNodeCountUnique = nodeCountUnique;

                list[Tree] errors = findAllParseErrors(disambTree);
                errorCount = size(errors);
                errorSize = (0 | it + size(getErrorText(err)) | err <- errors);
            }

            measurement = recovered(source=source, duration=duration, errorCount=errorCount, errorSize=errorSize);
            result = "recovery";

            if (config.verifyResult && "<tree>" != input) {
                throw "Yield of recovered tree does not match the original input";
            }
        } else {
            measurement = successfulParse(source=source, duration=duration);
            result = "success";
        }
    } catch ParseError(_): {
        result = "error"; 
        duration = realTime() - startTime;
        measurement = parseError(source=source, duration=duration);
    }

    if (config.statFile != |unknown:///|) {
        int durationRatio = percent(duration, referenceParseTime);
        if (config.countNodes) {
            int nodeRatio = percent(nodeCount, referenceNodeCount);
            int unodeRatio = percent(nodeCountUnique, referenceNodeCountUnique);

            appendToFile(config.statFile, "<source>,<size(input)>,<result>,<duration>,<durationRatio>,<nodeRatio>,<unodeRatio>,<disambDuration>,<errorCount>,<errorSize>,<nodeCount>,<nodeCountUnique>,<disambNodeCount>,<disambNodeCountUnique>\n");
        } else {
            appendToFile(config.statFile, "<source>,<size(input)>,<result>,<duration>,<durationRatio>\n");
        }
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
FileStats testSingleCharDeletions(&T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, loc source, str input, int referenceParseTime, int referenceNodeCount, int referenceNodeCountUnique, int recoverySuccessLimit, int begin=0, int end=-1, loc statFile=|unknown:///|)
    = testSingleCharDeletions(recoveryTestConfig(statFile=statFile), standardParser, recoveryParser, source, input, referenceParseTime, referenceNodeCount, referenceNodeCountUnique, recoverySuccessLimit, begin=begin, end=end);

FileStats testSingleCharDeletions(RecoveryTestConfig config, &T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, loc source, str input, int referenceParseTime, int referenceNodeCount, int referenceNodeCountUnique, int recoverySuccessLimit, int begin=0, int end=-1) {
    FileStats stats = fileStats();
    int len = size(input);
    int i = begin;

    while (i < len && (end == -1 || i<=end)) {
        str modifiedInput = input[..i] + input[i+1..];

        source.query = "deletedChar=<i>";
        TestMeasurement measurement = testRecovery(config, standardParser, recoveryParser, modifiedInput, source, referenceParseTime, referenceNodeCount, referenceNodeCountUnique);
        stats = updateStats(stats, measurement, referenceParseTime, recoverySuccessLimit);
        int skip = 1 + arbInt(config.sampleWindow);
        int next = min(i+skip, len);
        for (contains(substring(input, i, next), "\n")) {
            println();
        }
        i += 1 + arbInt(config.sampleWindow);
    }

    return stats;
}

// Backwards compatible version
FileStats testDeleteUntilEol(&T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, loc source, str input, int referenceParseTime, int referenceNodeCount, int referenceNodeCountUnique, int recoverySuccessLimit, int begin=0, int end=-1, loc statFile=|unknown:///|) 
    = testDeleteUntilEol(recoveryTestConfig(statFile=statFile), standardParser, recoveryParser, source, input, referenceParseTime, referenceNodeCount, referenceNodeCountUnique, recoverySuccessLimit, begin=begin, end=end);

str getDeleteUntilEolInput(str input, int begin) {
    int lineStart = 0;
    list[int] lineEndings = findAll(input, "\n");

    int line = 0;
    for (int lineEnd <- lineEndings) {
        line = line+1;
        if (lineEnd < begin) {
            continue;
        }
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

FileStats testDeleteUntilEol(RecoveryTestConfig config, &T (value input, loc origin) standardParser, &T (value input, loc origin) recoveryParser, loc source, str input, int referenceParseTime, int referenceNodeCount, int referenceNodeCountUnique, int recoverySuccessLimit, int begin=0, int end=-1) {
    FileStats stats = fileStats();
    int lineStart = 0;
    list[int] lineEndings = findAll(input, "\n");

    int line = 0;
    int pos = begin;
    for (int lineEnd <- lineEndings) {
        line = line+1;
        if (lineEnd < begin) {
            continue;
        }
        while (pos <= lineEnd) {
            // Check boundaries (used for quick bug testing)
            if (end != -1 && end < pos) {
                return stats;
            }
            if (pos < begin) {
                pos += 1+arbInt(config.sampleWindow);
                continue;
            }
            str modifiedInput = input[..pos] + input[lineEnd..];
            source.query = "deletedUntilEol=<line>:<pos>:<lineEnd>";
            TestMeasurement measurement = testRecovery(config, standardParser, recoveryParser, modifiedInput, source, referenceParseTime, referenceNodeCount, referenceNodeCountUnique);
            stats = updateStats(stats, measurement, referenceParseTime, recoverySuccessLimit);
            pos += 1 + arbInt(config.sampleWindow);
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


void printFileStats(RecoveryTestConfig config, FileStats fileStats) {
    void printStat(str label, int stat, int total, bool prints=true) {
        int pct = percentage(stat, total);
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
    printFrequencyTableStats("Parse time ratios", fileStats.parseTimeRatios, unit = "log2(ratio)", printTotal=false);
    if (config.countNodes) {
        printFrequencyTableStats("Parse error count", fileStats.errorCounts, unit="errors");
        printFrequencyTableStats("Error size", fileStats.errorSizes, unit="chars");
    }
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

void printStats(RecoveryTestConfig config, TestStats stats) {
    if (stats.filesTested != 1) {
        println("Files tested:         <stats.filesTested>");
    }
    println("Total parses:         <stats.testCount>");
    printFrequencyTableHeader();
    printFrequencyTableStats("Succesful parses", stats.successfulParses);
    printFrequencyTableStats("Succesful recoveries", stats.successfulRecoveries);
    printFrequencyTableStats("Failed recoveries", stats.failedRecoveries);
    printFrequencyTableStats("Parse errors", stats.parseErrors);
    printFrequencyTableStats("Slow parses", stats.slowParses);
    printFrequencyTableStats("Parse time ratios", stats.parseTimeRatios, unit = "log2/%", printTotal=false);
    if (config.countNodes) {
        printFrequencyTableStats("Parse error counts", stats.errorCounts, unit = "errors", ignoreZero=true);
        printFrequencyTableStats("Parse error sizes", stats.errorSizes, unit = "chars", ignoreZero=true);
    }

    println();
}

private str getTopSort(RecoveryTestConfig config) {
    if (contains(config.topSort, "::")) {
        // Fully qualified 
        int last = findLast(config.topSort, "::");
        return substring(config.topSort, last+2);
    }

    return config.topSort;
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
    type[value] begin = type(Symbol::\void(), ());
    str topSort = getTopSort(config);
    if (config.syntaxModule != "") {
        // Use module
        println ("Loading start type from syntax module: <config.syntaxModule>, topSort=<topSort>");
        RascalRuntime rt = createRascalRuntime();
        rt.eval(#void, "import <config.syntaxModule>;");
        begin = rt.eval(#type[value], "type[value] begin = #start[<topSort>];").val;
    } else {
        // Use file
        Module \module = parse(#start[Module], config.syntaxFile).top;
        str modName = replaceAll("<\module.header.name>",  "\\", "");
        println("Loading start type from file <config.syntaxFile>, module name: <modName>, topSort: <topSort>");
        Grammar gram = modules2grammar(modName, {\module});

        if (sym:\start(\sort(topSort)) <- gram.starts) {
            begin = type(sym, gram.rules);
        } else {
            throw "Cannot find top sort <topSort> in <gram>";
        }
    }

    value(str,loc) standardParser = parser(begin, allowAmbiguity=true, allowRecovery=false);
    recoveryParser = parser(begin, allowAmbiguity=true, maxAmbDepth=config.maxAmbDepth, allowRecovery=true, maxRecoveryAttempts=config.maxRecoveryAttempts, maxRecoveryTokens=config.maxRecoveryTokens);

    // Initialization run
    standardParser(input, testInput);

    // Timed run
    int startTime = realTime();
    value t = standardParser(input, testInput);
    int referenceParseTime = max(1, realTime() - startTime);
    int referenceNodeCount = 0;
    int referenceNodeCountUnique = 0;
    if (Tree tree := t) {
        referenceNodeCount = countTreeNodes(tree);
        referenceNodeCountUnique = countUniqueTreeNodes(tree);
    } else {
        throw "Not a tree? <t>";
    }

    recoverySuccessLimit = 2048; //size(input)/4;

    str syntaxSpec = config.syntaxModule == "" ? "<config.syntaxFile>" : config.syntaxModule;
    println("Error recovery of <syntaxSpec> (<topSort>) on <testInput>, reference parse time: <referenceParseTime> ms.");
    println("Configuration: <config>");

    println();
    println("Single char deletions:");
    FileStats singleCharDeletionStats = testSingleCharDeletions(config, standardParser, recoveryParser, testInput, input, referenceParseTime, referenceNodeCount, referenceNodeCountUnique, recoverySuccessLimit);
    printFileStats(config, singleCharDeletionStats);

    println();
    println("Deletes until end-of-line:");
    FileStats deleteUntilEolStats = testDeleteUntilEol(config, standardParser, recoveryParser, testInput, input, referenceParseTime, referenceNodeCount, referenceNodeCountUnique, recoverySuccessLimit);
    printFileStats(config, deleteUntilEolStats);

    FileStats stats = mergeFileStats(singleCharDeletionStats, deleteUntilEolStats);
    println();
    println("-----------------------------------------------------------");
    println("Total test stats for <testInput>:");
    printFileStats(config, stats);
    return stats;
}

private int fileNr = 0;


void batchRecoveryTest(RecoveryTestConfig config) {
    println("Running batch test with config <config>");

    fileNr = 0;

    if (config.statFile != |unknown:///|) {
        if (config.countNodes) {
            writeFile(config.statFile, "source,size,result,duration,durationRatio,nodeRatio,unodeRatio,disambiguationDuration,errorCount,errorSize,nodes,unodes,disambNodes,udisambNodes\n");
        } else {
            writeFile(config.statFile, "source,size,result,duration,durationRatio\n");
        }
    }

    int startTime = realTime();
    TestStats stats = runBatchRecoveryTest(config, testStats());
    int duration = realTime() - startTime;

    println();
    println("==================================================================");
    println("Recovery batch test done in <duration/1000> seconds, total result:");
    printStats(config, stats);
}

private list[loc] gatherFiles(loc dir, str ext, int maxFiles, int minFileSize, int maxFileSize, int fromFile) {
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

private TestStats runBatchRecoveryTest(RecoveryTestConfig config, TestStats cumulativeStats) {
    list[loc] files = gatherFiles(config.dir, config.ext, config.maxFiles, config.minFileSize, config.maxFileSize, config.fromFile);
    int fileCount = size(files);
    println("Batch testing <fileCount> files");

    int index = 0;
    for (file <- files) {
        str content = readFile(file);
        index += 1;
        println("========================================================================");
        println("Testing file #<index> <file> (<fileCount-cumulativeStats.filesTested> of <fileCount> left)");
        try {
            FileStats fileStats = testErrorRecovery(config, file, content);
            cumulativeStats = consolidateStats(cumulativeStats, fileStats);
            println();
            println("------------------------------------------------------------------------");
            println("Cumulative stats after testing <file>:");
            printStats(config, cumulativeStats);
        } catch ParseError(l): println("Ignoring file because it cannot be parsed: <l>");
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

RecoveryTestConfig createRecoveryTestConfig(list[str] args) {
    loc sourceLoc = |unknown:///|;
    str syntaxSpec = "rascal";
    int maxAmbDepth = 2;
    int maxFiles = 1000;
    int maxFileSize = 1000000;
    int minFileSize = 0;
    int maxRecoveryAttempts = 50;
    int maxRecoveryTokens = 3;
    int fromFile = 0;
    int sampleWindow = 1;
    bool countNodes = false;
    loc statFile = |tmp:///error-recovery-test.stats|; // |unknown:///| to disable stat writing

    for (str arg <- args) {
        if (/<name:[^=]*>=<val:.*>/ := arg) {
            switch (toLowerCase(name)) {
                case "syntax": syntaxSpec = val;
                case "source-loc": sourceLoc = readTextValueString(#loc, val);
                case "max-amb-depth": maxAmbDepth = toInt(val);
                case "max-files": maxFiles = toInt(val);
                case "min-file-size": minFileSize = toInt(val);
                case "max-file-size": maxFileSize = toInt(val);
                case "max-recovery-attempts": maxRecoveryAttempts = toInt(val);
                case "max-recovery-tokens": maxRecoveryTokens = toInt(val);
                case "from-file": fromFile = toInt(val);
                case "stat-file": statFile = readTextValueString(#loc, val);
                case "sample-window": sampleWindow = toInt(val);
                case "random-seed": arbSeed(toInt(val));
                case "count-nodes": countNodes = val == "true" || val == "1";
                default: { throw IllegalArgument(arg, "Unknown argument"); }
            }
        }
    }

    RecoveryTestConfig config = createSyntaxConfig(syntaxSpec);
    config.maxAmbDepth=maxAmbDepth;
    config.dir=sourceLoc;
    config.maxFiles=maxFiles;
    config.minFileSize=minFileSize;
    config.maxFileSize=maxFileSize;
    config.maxRecoveryAttempts=maxRecoveryAttempts;
    config.maxRecoveryTokens=maxRecoveryTokens;
    config.fromFile=fromFile;
    config.sampleWindow=sampleWindow;
    config.countNodes = countNodes;
    config.statFile=statFile;

    return config;
}

private RecoveryTestConfig createSyntaxConfig(str spec) {
    println("Determining test config based on syntax spec: <spec>");
    switch (spec) {
        case "rascal": return recoveryTestConfig(syntaxFile=|std:///lang/rascal/syntax/Rascal.rsc|, topSort="Module", ext=".rsc");
        case "java18": return recoveryTestConfig(syntaxFile=|cwd:///src/lang/java/syntax/Java18.rsc|, topSort="CompilationUnit", ext=".java");
        case "java15": return recoveryTestConfig(syntaxFile=|cwd:///src/lang/java/syntax/Java15.rsc|, topSort="CompilationUnit", ext=".java");
        case "cobol": return recoveryTestConfig(syntaxModule="lang::vscobolii::Main", topSort="VSCobolII", ext=".CBL");
        default: throw IllegalArgument(spec, "Unknown syntax spec"); // TODO: parse as syntax file, top sort, and extension spec
    }
}