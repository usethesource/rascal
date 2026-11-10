@bootstrapParser
module CalculateOrder

import IO;
import Exception;
import Set;
import ParseTree;
import analysis::graphs::Graph;
import util::PathConfig;
import lang::rascal::\syntax::Rascal;
import lang::rascal::grammar::definition::Modules;

Graph[set[loc]] calculateModuleDependencies(list[loc] modules, PathConfig pcfg) {
    dependencies = moduleDependencies({*modules}, pcfg);
    components = stronglyConnectedComponents(dependencies);
    return {<fc, tc> | <loc from, loc to> <- dependencies, fc:{*_, from} <- components, tc:{*_, to} <- components, fc != tc};
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

private Graph[loc] moduleDependencies(loc file, PathConfig pcfg) {
    Module m = parse(#start[Module], file).top;
    <_, imps, exts> = getModuleMetaInf(m);
    imports = {};
    for (i <- imps + exts) {
        // if the module is on the source path
        // add it to the graph
        try {
            imports += srcsFile(i, pcfg, fileConfig());
        } catch PathNotFound(_): "external modules are ignored";
    }
    return {file} * imports;
}