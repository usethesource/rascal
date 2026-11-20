@bootstrapParser
module CalculateOrder

import IO;
import Exception;
import List;
import Map;
import Set;
import Relation;
import ParseTree;
import String;
import util::Math;
import util::Monitor;
import analysis::graphs::Graph;
import util::PathConfig;
import lang::rascal::\syntax::Rascal;


Graph[set[loc]] calculateModuleDependencies(list[loc] modules, PathConfig pcfg) {
    return job("calulating import graph", Graph[set[loc]] (void (str msg, int worked) step) {
        step("Calculating import graph", 1);

        dependencies = moduleDependencies({*modules}, pcfg);
        step("Calculating SCC", 1);
        components = stronglyConnectedComponents(dependencies);
        step("Restoring graph", 1);
        return {<fc, tc> | <loc from, loc to> <- dependencies, fc:{*_, from} <- components, tc:{*_, to} <- components, fc != tc} +
            // add module that have no imports and are not imported
            { <{i}, {}> | i <- {*modules} - (dependencies<0> + dependencies<1>) }
            ;
    }, totalWork = 3);

}

list[set[loc]] chunkGraph(Graph[set[loc]] graph, set[loc] preCompute, map[loc, int] fileSizes, int amountOfClusters) {
    return job("calulating parallel chunks", list[set[loc]] (void (str msg, int worked) step) {
        if (preCompute != {}) {
            // add preCompute imports (transitivly) to the set of precomputed modules
            preCompute += { *p | p <- (graph+)[preCompute]};
            // now remove all imports that point to the preCompute set
            // such that they don't influence the chunk calculations
            // as they get scheduled by the runner automatically
            graph = { <fromF, to - preCompute> | <from, to> <- graph, fromF := from - preCompute, fromF != {}};
        }
        imports = graph+;
        allClusters = (graph<0> + graph<1>) - {{}};
        best = [{*c | c <- allClusters}];
        bestScore = (0 | it + fileSizes[f] | c <- allClusters, f <- c);
        bestAllCommonSize = bestScore + 0.;
        bestSomeCommonSize = bestScore + 0.;
        for (_ <- [0..1000]) {
            step("best unbalance: <bestScore> and <bestAllCommonSize> & <bestSomeCommonSize> commonSize", 1);
            option = tryChunk(imports, allClusters, fileSizes, amountOfClusters);
            if (size(option) < amountOfClusters) {
                continue;
            }
            allSizes = sort([ (0 | it + fileSizes[c] | c <- cluster) | cluster <- option]);
            if (allSizes[-1] / 5 > allSizes[-2]) {
                // skipping cases where everything (or at least 5x as much as the 2nd largest) 
                // landed in one bucket as in those cases, it's better to just not use concurrency
                continue;
            }
            // the score is the "spread" of size between the smallest and largest and the max, to optimize for some split
            score = allSizes[-1] - allSizes[0];

            // next factor is how many modules are shared/common between the chunks
            allCommon = (option[0] | it & clus | clus <- option[1..]);
            someCommon = { m | cc <- allClusters, m <- cc, (0 | it + (m in op ? 1 : 0) | op <- option) >= 2 };
            allCommonSize = totalFileSize(allCommon, fileSizes);
            someCommonSize = totalFileSize(someCommon, fileSizes);

            if (score * 5 < bestScore /*if the new one is soo much better */
                ||/* or all the scores have "improved" */ (allCommonSize <= bestAllCommonSize && score < bestScore && someCommonSize <= bestSomeCommonSize)) {
                //println("Better option: <score> <commonSize> <commonSize1> \t <allSizes> \t <[size(b) | b <- option]>");
                best = option;
                bestScore = score;
                bestAllCommonSize = (allCommonSize + 1) * 1.1; // we make the score 10% (and not 0) to leave room for some better options that are very close (but have a better `score`)
                bestSomeCommonSize = (someCommonSize + 1) * 1.1;
            }
        }
        println("Split the work into clusters: <[size(b) | b <- best]>");
        return best;
    }, totalWork = 1000);
}

Graph[loc] moduleDependencies(set[loc] modules, PathConfig pcfg) {
    done = {};
    todo = modules;
    Graph[loc] result = {};
    while (todo != {}) {
        nextRound = {};
        for (current <- todo) {
            result += moduleDependencies(current, pcfg);
        }
        done += todo;
        todo = result<1> - done;
    }
    return result;
}

// this is copy of reflective, as reflective is slow to load
@javaClass{org.rascalmpl.library.util.Reflective}
private java start[Module] parseModuleWithSpaces(loc location);

private str deslash(str s) = replaceAll(s, "\\", "");

private set[str] getImports(start[Module] m) = { deslash("<i.\module.name>") | /Import i := m.top, i has \module };

Graph[loc] moduleDependencies(loc file, PathConfig pcfg) {
    imports = {};
    for (i <- getImports(parseModuleWithSpaces(file))) {
        // if the module is on the source path
        // add it to the graph
        try {
            imports += srcsFile(i, pcfg, fileConfig());
        } catch PathNotFound(_): "external modules are ignored";
    }
    return {file} * imports;
}

private int totalFileSize(set[loc] files, map[loc, int] fileSizes) = (0 | it + fileSizes[f] | f <- files);

@memo
private set[loc] flatten(set[set[loc]] locs) {
    if ({set[loc] single} := locs) {
        return single;
    }
    return { *ll | ll <- locs};
}

list[set[loc]] tryChunk(Graph[set[loc]] imports, set[set[loc]] allClusters, map[loc, int] fileSizes, int amountOfClusters) {
    list[set[loc]] chunks = [ {} | _ <- [0..amountOfClusters]];
    list[int] chunkSizes = [ 0 | _ <- [0..amountOfClusters]];
    candidates = allClusters;
    assigned = 0;
    while (candidates != {}) {
        c = getOneFrom(candidates);
        ci = {c} + imports[{c}];
        candidates -= ci;
        chunk = flatten(ci);
        if (assigned < amountOfClusters) {
            chunks[assigned] += chunk;
            chunkSizes[assigned] = totalFileSize(chunk, fileSizes);
        }
        else {
            smallest = 0;
            if (size(chunk) == 1) {
                // no imports, so throw it in the smallest bucket
                smallest = (0 | chunkSizes[it] < chunkSizes[i] ? it : i  | i <- [1..amountOfClusters]);
            }
            else {
                // look for the smallest new modules added
                // aka the chunk that has the most overlap with the current chunk
                smallest = (<0, size(chunk - chunks[0])> | it[1] < sz ? it : <i, sz> | i <- [1..amountOfClusters], sz := size(chunk - chunks[i]))[0];
            }
            // we keep track of the size such that we can find the smallest
            chunkSizes[smallest] += totalFileSize(chunk - chunks[smallest], fileSizes);
            chunks[smallest] += chunk;
        }
        assigned += 1;
    }
    return chunks;
}