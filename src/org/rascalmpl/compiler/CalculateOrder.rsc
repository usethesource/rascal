@bootstrapParser
module CalculateOrder

import IO;
import Exception;
import List;
import Set;
import Relation;
import ParseTree;
import String;
import util::Math;
import util::Monitor;
import analysis::graphs::Graph;
import util::PathConfig;
import analysis::statistics::Descriptive;
import lang::rascal::\syntax::Rascal;


Graph[set[loc]] calculateModuleDependencies(list[loc] modules, PathConfig pcfg, int parAmount) {
    return job("calulate and chunk import graph", Graph[set[loc]] (void (str msg, int worked) step) {
        step("Calculating import graph", 1);

        dependencies = moduleDependencies({*modules}, pcfg);
        step("Calculating SCC", 1);
        components = stronglyConnectedComponents(dependencies);
        scc = {<fc, tc> | <loc from, loc to> <- dependencies, fc:{*_, from} <- components, tc:{*_, to} <- components, fc != tc} +
        // add module that have no imports and are not imported
        { <{i}, {}> | i <- {*modules} - (dependencies<0> + dependencies<1>) }
        ;

        step("Bundle into clusters", 1);
        return mergeHighlyConnectedCluster(scc, parAmount);
    }, totalWork = 3);

}

Graph[set[loc]] mergeHighlyConnectedCluster(Graph[set[loc]] scc, int clusterSize) {
    return chunkCluster(scc, clusterSize);
}

Graph[loc] moduleDependencies(set[loc] modules, PathConfig pcfg) {
    done = {};
    todo = modules;
    result = {};
    while (todo != {}) {
        <current, todo> = takeOneFrom(todo);
        newGraph = moduleDependencies(current, pcfg);
        done += current;
        todo += newGraph<1> - done;
        result += newGraph;
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


private set[&T] verts(Graph[&T] G) = G<0> + G<1>;

private Graph[set[&T]] chunkIt(Graph[set[&T]] scc, int amountOf) {
    clusters = connectedComponents(scc);
    return {*chunkCluster({t | t:<set[&T] a, set[&T] b> <- scc, a in c || b in c}, amountOf) | c <- clusters};
}

private int nestedSize(Graph[set[&T]] component) = (0 | it + size(s) | s <- domain(component));
private int nestedSize(set[set[&T]] component) = (0 | it + size(s) | s <- component);
private int nestedSize(list[set[&T]] component) = (0 | it + size(s) | s <- component);

int balancedScore([]) = 10000;
int balancedScore([_]) = 10000;
default int balancedScore(list[set[set[&T]]] t) {
    sizes = [ nestedSize(tt) | tt <- t ];
    return max(sizes) - min(sizes);
}

private Graph[set[&T]] chunkCluster(Graph[set[&T]] scc, int parAmount) {
    return job("Trying random clusters", Graph[set[&T]] (void (str msg, int worked) step) {
        bestSplit = [];
        bestCommon = [verts(scc)];
        for (_ <- [0..1000]) {
            step("", 1);
            <chunks, common> = tryChunk(scc, parAmount);
            if (size(common) < size(bestCommon)) {
                continue;
            }
            if (size(common) == size(bestCommon) && nestedSize(bestCommon) < nestedSize(common)) {
                continue;
            }
            if (balancedScore(bestCommon) > balancedScore(common)) {
                continue;
            }
            // now we know that common is better
            if (balancedScore(chunks) <= balancedScore(bestSplit)) {
                bestSplit = chunks;
                bestCommon = common;
            }
        }
        iprintln(bestSplit);
        iprintln(bestCommon);
        return flatten(bestSplit, bestCommon, scc);
    }, totalWork = 1000);
}

private tuple[list[set[&T]], list[set[&T]]] tryChunk(Graph[&T] scc, int chunkSize) {
    sccFull = scc+;
    candidates = verts(scc) - {{}};
    list[set[&T]] chunks = [ {} | _ <- [0..chunkSize]];
    while (candidates != {}) {
        <c, candidates> = takeOneFrom(candidates);
        // take c and all of it's imports
        chunk = {c} + sccFull[{c}];
        candidates -= chunk;
        chunks[arbInt(chunkSize)] += chunk;
    }
    shared = {s | s <- domain(scc), (0 | it + (s in c ? 1 : 0) | c <- chunks) > 1 };
    shared += sccFull[shared];
    chunks = [newC | c <- chunks, newC := c - shared, newC != {}]; 
    splitShared = [*connectedComponents(scc & (shared*shared))];
    return <chunks, splitShared>;
}

private Graph[set[&T]] flatten(list[set[set[&T]]] chunks, list[set[set[&T]]] common, Graph[set[&T]] scc) {
    newVerts =  { { *c | c <- com } | com <- common} +
        { {*v | v <- vv} | vv <- chunks}
    ;
    lookupV = { <c, v> | v <- newVerts, c <- v };
    Graph[set[&T]] newGraph = {};
    for (<from, to> <- scc) {
        newFrom = lookupV[from];
        newTo = lookupV[to];
        if (newFrom & newTo == {}) {
            // our dependencies are not local to our own clustering
            newGraph += newFrom * newTo;
        }
    }
    return newGraph;
}

// // inspired by Highly Connected Subgraphs (HCS) clustering algorithm
// private Graph[set[&T]] chunkCluster(Graph[set[&T]] scc, int amountOf) {
//     if (nestedSize(scc) < amountOf) {
//         // flatten to one chunk
//         return {<{*v | v <- verts(scc)}, {}>};
//     }


//     // bestGraphs = [];
//     // bestRemain = scc;
//     // // find a minimum cut
//     // for (i <- [0..20]) {
//     //     <graphs, remain> = splitGraph(scc, amountOf, i);
//     //     if (balancedScore(graphs) > balancedScore(bestGraphs) && size(graphs) > size(bestGraphs)) {
//     //         bestGraphs = graphs;
//     //         bestRemain = remain;
//     //     }
//     // }

//     // //  println("best:");
//     // //  iprintln(bestGraphs);
//     // // iprintln(bestRemain);
//     <bestGraphs, bestRemain> = splitGraph(scc, amountOf, 0);

//     return calculateMergedGraphs(bestGraphs, bestRemain);
// }


// private tuple[list[Graph[set[&T]]], Graph[set[&T]]] splitGraph(Graph[set[&T]] scc, int amountOf, int seed) {
//     list[set[set[&T]]] subs = [];
//     set[set[&T]] current = {};
//     currentSize = 0;
//     for (v <- shuffle([*range(scc)], seed)) {
//         if (currentSize + size(v) > amountOf && current != {}) {
//             // new subgraph
//             subs += current;
//             current = {};
//             currentSize = 0;
//         }
//         current += {v};
//         currentSize += size(v);
//     }
//     if (currentSize > 0) {
//         subs += current;
//     }
//     //iprintln(subs);
//     // now split the domain, lets make the subgraphs and reconstuct the cross graph edges
//     subg = [ {t | t:<set[&T] from, set[&T] to> <- scc, from in sub && to in sub } | sub <- subs] - [{}];

//     if ({} in subg) {
//         println("I produced flats!!");
//     }
//     outside = scc - {*g | g <- subg};
//     return <subg, outside>;
// }

//private int sizes(set[set[&T]] sets) = (0 | it + size(s) | s <- sets);


// private list[set[set[&T]]] split(Graph[set[&T]] scc, int amountOf, set[set[&T]] verts) {
//     // inside the layer we can split things up a bit
//     subset = (scc & (verts * verts));
//     clusters = connectedComponents(subset);
//     for (v <- verts, /v !:= clusters) {
//         clusters += {{v}};
//     }

//     list[set[set[&T]]] result = [];
//     set[set[&T]] current = {};
//     currentSize = 0;
//     for (c <- clusters) {
//         if (currentSize + size(c) > amountOf && current != {}) {
//             result += current;
//             current = {};
//             currentSize = 0;
//         }
//         current += c;
//         currentSize += size(c);
//     }
//     if (current != {}) {
//         result += current;
//     }
//     return result;

// }

// private tuple[list[Graph[set[&T]]], Graph[set[&T]]] splitGraph(Graph[set[&T]] scc, int amountOf, int seed) {
//     list[set[set[&T]]] subs = [];
//     set[set[&T]] current = {};
//     currentSize = 0;

//     sccCopy = scc;
//     candidates = verts(scc);
//     while (candidates != {}) {
//         sccInverted = invert(sccCopy);
//         // collect 4 level deep imports
//         leafs = top(sccInverted);
//         sccInverted1 = sccInverted o sccInverted;
//         leafs += top(sccInverted1); 
//         sccInverted1 = sccInverted o sccInverted1;
//         leafs += top(sccInverted1); 
//         sccInverted1 = sccInverted o sccInverted1;
//         leafs += top(sccInverted1); 
//         if (leafs == {}) {
//             println("Nothing left:<candidates>: <sccInverted1>");
//             subs += split(scc, amountOf, candidates);
//             break;
//         }
//         subs += split(scc, amountOf, leafs);
//         candidates -= leafs;
//         sccCopy = {t | t:<set[&T] a, set[&T] b> <- sccCopy, a notin leafs, b notin leafs};
//     }
//     //iprintln(subs);
//     // now split the domain, lets make the subgraphs and reconstuct the cross graph edges
//     subg = [ {t | t:<set[&T] from, set[&T] to> <- scc, from in sub && (to in sub || to == {}) } + (sub * {{}}) | sub <- subs];


//     outside = scc - {*g | g <- subg};
//     return <subg, outside>;
// }


// private Graph[set[&T]] calculateMergedGraphs(list[Graph[set[&T]]] subGraphs, Graph[set[&T]] remain) {
//     set[set[&T]] newV = {};
//     map[set[&T], set[&T]] subV = ();
//     for (s <- subGraphs) {
//         vs = verts(s);
//         fl = {*v | v <- vs, v != {}}; // flatten
//         if (fl == {}) {
//             println("flatt??: <s>");
//         }
//         newV += {fl};
//         for (v <- vs) {
//             subV[v] = fl;
//         }
//     }
//     connections = { <subV[from]?from, subV[to]?to> | <set[&T] from, set[&T] to> <- remain};
//     missing = newV - verts(connections);
//     return (connections + {<m, {}> | m <- missing}) - {<{},{}>};
// }