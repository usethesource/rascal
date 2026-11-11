@bootstrapParser
module CalculateOrder

import IO;
import Exception;
import Set;
import ParseTree;
import String;
import analysis::graphs::Graph;
import util::PathConfig;
import lang::rascal::\syntax::Rascal;


Graph[set[loc]] calculateModuleDependencies(list[loc] modules, PathConfig pcfg) {
    dependencies = moduleDependencies({*modules}, pcfg);
    components = stronglyConnectedComponents(dependencies);
    return {<fc, tc> | <loc from, loc to> <- dependencies, fc:{*_, from} <- components, tc:{*_, to} <- components, fc != tc} +
    // add module that have no imports and are not imported
    { <{i}, {}> | i <- {*modules} - (dependencies<0> + dependencies<1>) }
    ;
}

private Graph[loc] moduleDependencies(set[loc] modules, PathConfig pcfg) {
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

private Graph[loc] moduleDependencies(loc file, PathConfig pcfg) {
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