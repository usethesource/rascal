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
        desiredClusterSize = size(verts(dependencies)) / (parAmount * 4);
        if (desiredClusterSize < 2) {
            desiredClusterSize = 2;
        }
        println(desiredClusterSize);
        return mergeHighlyConnectedCluster(scc, desiredClusterSize);
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



real balancedScore([]) = 0.;
default real balancedScore(list[Graph[set[&T]]] t) = median([ nestedSize(g) | g <- t ]);

// inspired by Highly Connected Subgraphs (HCS) clustering algorithm
private Graph[set[&T]] chunkCluster(Graph[set[&T]] scc, int amountOf) {
    if (nestedSize(scc) < amountOf) {
        // flatten to one chunk
        return {<{*v | v <- verts(scc)}, {}>};
    }


    bestGraphs = [];
    bestRemain = scc;
    // find a minimum cut
    for (i <- [0..20]) {
        <graphs, remain> = splitGraph(scc, amountOf, i);
        if (balancedScore(graphs) > balancedScore(bestGraphs) && size(graphs) > size(bestGraphs)) {
            bestGraphs = graphs;
            bestRemain = remain;
        }
    }

     println("best:");
     iprintln(bestGraphs);
    // iprintln(bestRemain);

    return calculateMergedGraphs(bestGraphs, bestRemain);
}


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

private int sizes(set[set[&T]] sets) = (0 | it + size(s) | s <- sets);


private tuple[list[Graph[set[&T]]], Graph[set[&T]]] splitGraph(Graph[set[&T]] scc, int amountOf, int seed) {
    list[set[set[&T]]] subs = [];
    set[set[&T]] current = {};
    currentSize = 0;

    sccInverted = invert(scc)+;
    candidates = verts(scc);
    int tries = 0;
    while (candidates != {}) {
        vSingle = getOneFrom(candidates);
        //set[set[&T]] importees = { a | <a,{*_, vSingle}> <- scc};// sccInverted[vSingle];
        set[set[&T]] importees = { a | <vSingle, a> <- sccInverted };
        //println("importees: <vSingle> <importees>");
        if (importees & candidates != importees) {
            tries += 1;
            if (tries % 100 == 0) {
                // retry with a different set
                candidates += current;
                current = {};
                currentSize = 0;
            }
            else if (tries > 2000) {
                println("not possible, lets continue");
                break;
            }
            continue;
        }
        v = importees + {vSingle}; // everyone that imports me should be in my set
        candidates -= v;
        if (currentSize + sizes(v) > amountOf && current != {}) {
            // new subgraph
            subs += current;
            current = {};
            currentSize = 0;
        }
        current += v;
        currentSize += size(v);
    }
    if (currentSize > 0) {
        subs += current;
    }
    //iprintln(subs);
    // now split the domain, lets make the subgraphs and reconstuct the cross graph edges
    subg = [ {t | t:<set[&T] from, set[&T] to> <- scc, from in sub && to in sub } | sub <- subs] - [{}];

    if ({} in subg) {
        println("I produced flats!!");
    }
    //iprintln(subg);

    outside = scc - {*g | g <- subg};
    return <subg, outside>;
}


private Graph[set[&T]] calculateMergedGraphs(list[Graph[set[&T]]] subGraphs, Graph[set[&T]] remain) {
    set[set[&T]] newV = {};
    map[set[&T], set[&T]] subV = ();
    for (s <- subGraphs) {
        vs = verts(s);
        fl = {*v | v <- vs}; // flatten
        if (fl == {}) {
            println("flatt??: <s>");
        }
        newV += {fl};
        for (v <- vs) {
            subV[v] = fl;
        }
    }
    connections = { <subV[from]?from, subV[to]?to> | <set[&T] from, set[&T] to> <- remain};
    missing = newV - verts(connections);
    return connections + {<m, {}> | m <- missing};
}