/**
* Copyright (c) 2025, NWO-I Centrum Wiskunde & Informatica (CWI)
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
*
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
*
* 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
**/
module lang::rascal::tests::concrete::recovery::ParseBenchmark

import IO;
import String;
import ValueIO;
import ParseTree;
import Grammar;
import util::Benchmark;
import lang::rascal::\syntax::Rascal;
import lang::rascal::grammar::definition::Modules;

public data ParseBenchmarkConfig = parseBenchmarkConfig(
    loc syntaxFile = |std:///lang/rascal/syntax/Rascal.rsc|,
    str topSort = "Module",
    str extension = ".rsc",
    loc files = |unknown:///|,
    loc statFile = |tmp:///parse-benchmark-stats.csv|,
    int warmupIterations = 100,
    int parseIterations = 10
);

list[loc] gatherFiles(loc dir, str ext) {
    list[loc] files = [];
    for (entry <- listEntries(dir)) {
        loc file = dir + entry;
        if (isFile(file)) {
            if (endsWith(file.path, ext)) {
                files += file;
            }
        } else if (isDirectory(file)) {
            files += gatherFiles(file, ext);
        }
    }

    return files;
}

private void warmupParser(&T (value input, loc origin) benchmarkParser, list[loc] files, int iterations) {
    println("Warming up parser (<iterations>)");
    for (int i <- [0..iterations]) {
        loc file = files[i % size(files)];
        try {
            benchmarkParser(readFile(file), file);
        } catch ParseError(_): {
            println("Skipping warmup for file with parse errors: <file>");
        }
    }
}

private void runBenchmark(ParseBenchmarkConfig config, &T (value input, loc origin) benchmarkParser, loc file) {
    println("Benchmarking <file>");
    str content = readFile(file);
    int contentSize = size(content);
    int iterations = config.parseIterations;
    int i = 0;
    try {
        int begin = realTime();
        while (i<iterations) {
            benchmarkParser(content, file);
            i += 1;
        }
        int duration = realTime() - begin;

        appendToFile(config.statFile, "<file>,<contentSize>,<duration>\n");
    } catch ParseError(_): {
        println("Ignoring file with parse errors: <file>");
    }
}

private void runBenchmark(ParseBenchmarkConfig config, &T (value input, loc origin) benchmarkParser, list[loc] files) {
    for (loc file <- files) {
        runBenchmark(config, benchmarkParser, file);
    }
}

private str syntaxLocToModuleName(loc syntaxFile) {
    str path = replaceLast(substring(syntaxFile.path, 1), ".rsc", "");
    return replaceAll(path, "/", "::");
}


private void benchmark(ParseBenchmarkConfig config) {
    writeFile(config.statFile, "input,size,duration\n");
    Module \module = parse(#start[Module], config.syntaxFile).top;
    str modName = syntaxLocToModuleName(config.syntaxFile);
    Grammar gram = modules2grammar(modName, {\module});

    str topSort = config.topSort;
    if (sym:\start(\sort(topSort)) <- gram.starts) {
        type[value] begin = type(sym, gram.rules);
        benchmarkParser = parser(begin);
        list[loc] files = gatherFiles(config.files, config.extension);

        warmupParser(benchmarkParser, files, config.warmupIterations);
        runBenchmark(config, benchmarkParser, files);
    } else {
        throw "Cannot find top sort <topSort> in <gram>";
    }

}

int main(list[str] args) {
    ParseBenchmarkConfig config = parseBenchmarkConfig();

    for (str arg <- args) {
        if (/<name:[^=]*>=<val:.*>/ := arg) {
            switch (toLowerCase(name)) {
                case "syntax": config.syntaxFile = readTextValueString(#loc, val);
                case "sort": config.topSort = val;
                case "files": config.files = readTextValueString(#loc, val);
                case "ext": config.extension = val;
                case "stats": config.statFile = readTextValueString(#loc, val);
                case "warmup": config.warmupIterations = toInt(val);
                case "parses": config.parseIterations = toInt(val);
            }
        }
    }

    benchmark(config);

    return 0;
}

public int benchmarkRascal() = main(["files=|std:///|"]);
