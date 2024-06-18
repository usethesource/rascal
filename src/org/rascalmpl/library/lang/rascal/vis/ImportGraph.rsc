@synopsis{Visualize the import and extend structure of a Rascal project using a hierarchical graph layout}
@description{
The so-called "import graph" for Rascal modules stems from the times of the ASF+SDF Meta-Environment.
There it was the core UI for interacting with a loaded modular language specification. We recreated
it here for Rascal, but in an on-demand fashion. You can look at the import graph but it is not kept up-to-date
with the state of the files in the IDE. It is more of a snapshot of the current situation.
}
@benefits{
* A visual representation can help avoid overly complex dependencies, including unnecessary cycles.
* Visual analysis may help get an overview of a complex Rascal application.
* Modular and extensible language implementations often provide nice pictures where each layer
is clearly visible.
}
@pitfalls{
* The visualization is a static snapshot and does not change automatically when files are saved.
}
@bootstrapParser
module lang::rascal::vis::ImportGraph

import util::Reflective;
import vis::Graphs;
import lang::rascal::grammar::definition::Modules;
import lang::rascal::\syntax::Rascal;
import Exception;
import util::FileSystem;
import util::IDEServices;
import IO;
import analysis::graphs::Graph;
import Set;

@synopsis{If `projectName` is an open project in the current IDE, the visualize its import/extend graph.}
void importGraph(str projectName, bool hideExternals=true) {
    importGraph(|project://<projectName>|, hideExternals=hideExternals);
}

@synopsis{Given an arbitrary root folder of a Rascal project, visualize its import/extend graph.}
void importGraph(loc projectRoot, bool hideExternals=true) {
    // we use compiler() mode here to avoid diving into the transitively depended projects.
    pcfg = getProjectPathConfig(projectRoot, mode=compiler());
    importGraph(pcfg, hideExternals=hideExternals);
}

@synopsis{Visualizes an import/extend graph for all the modules in the srcs roots of the current PathConfig}
void importGraph(PathConfig pcfg, bool hideExternals=true) {
    m = getProjectModel(pcfg.srcs);
    
    // let's start with a simple graph and elaborate on details in later versions
    g = { <from, to> | <from, to> <- sort(m.imports), hideExternals ==> to notin m.external}
      + { <from, to> | <from, to> <- sort(m.extends), hideExternals ==> to notin m.external}
      + { <"_" , to> |  to <- top(m.imports + m.extends) } // pull up the top modules
      ;

    str nodeClass(str n) = "rascal.project" when n notin m.external;
    str nodeClass(str n) = "rascal.external" when n in m.external;

    str edgeClass(str from, str to) = "rascal.import" when <from, to> in m.imports;
    str edgeClass(str from, str to) = "rascal.extend" when <from, to> in m.extends;

    nonTransitiveEdges = transitiveReduction(m.imports + m.extends);
    cyclicNodes        = { x | <x,x> <- (m.imports + m.extends)+};
    transitiveEdges    = {<x,y> | <x,y> <- (m.imports + m.extends - nonTransitiveEdges), x notin cyclicNodes, y notin cyclicNodes};
    
    styles = [
        cytoStyleOf( 
            selector=or([
                and([\node(), id("_")]), // the top node
                and([\edge(), equal("source", "_")]) // edges from the top node
            ]), 
            style=defaultNodeStyle()[visibility="hidden"] // hide it all
        ),
        cytoStyleOf(
            selector=or([
                and([\edge(), equal("label", "E")])]), // extend edges
            style=defaultEdgeStyle()[\line-style="dashed"] // are dashed
        ),
        *[ cytoStyleOf(
            selector=and([\edge(),equal("source", f),equal("target", t)]), // any transitive edge
            style=defaultEdgeStyle()[opacity=".25"][\line-opacity="0.25"]  // will be made 25% opaque 
        )
            | <f,t> <- transitiveEdges
        ]
    ];

    loc modLinker(str name) {
        if (loc x <- m.files[name])
            return x;
        else 
            return |nothing:///|;
    }

    default loc modLinker(value _) = |nothing:///|;

    cfg = cytoGraphConfig(
        \layout=dagreLayout(ranker=\tight-tree()),
        styles=styles,
        title="Rascal Import/Extend Graph",
        nodeClassifier=nodeClass,
        edgeClassifier=edgeClass,
        nodeLinker=modLinker
    );

    showInteractiveContent(graph(g, cfg=cfg), title=cfg.title);
}

@synopsis{Container for everything we need to know about the modules in a project to visualize it.}
data ProjectModel = projectModel(
    set[str]      modules = {},
    set[str]      external = {},
    rel[str, str] imports = {},
    rel[str, str] extends = {},
    rel[str, loc] files = {}
);

@synopsis{Collects name, imports and extends for all modules reachable from the `srcs` root folders.}
ProjectModel getProjectModel(list[loc] srcs) {
    allFiles = {*find(src, "rsc") | src <- srcs};
    
    models = {getProjectModel(f) | f <- allFiles};

    wholeWorld = projectModel(
        modules = {*m.modules | m <- models},
        imports = {*m.imports | m <- models},
        extends = {*m.extends | m <- models},
        files   = {*m.files   | m <- models}
    );

    wholeWorld.external = wholeWorld.imports<1> + wholeWorld.extends<1> - wholeWorld.modules;

    return wholeWorld;
}

@synopsis{Collects name, imports and extends for a single Rascal module}
ProjectModel getProjectModel(loc file) {
    try {
        Module m = parseModule(file);

        <name, imps, exts> = getModuleMetaInf(m);

        return projectModel(
            modules = {name},
            imports = {name} * imps,
            extends = {name} * exts,
            files   = {<name, file>}
        );
    }
    catch ParseError(_) : 
        return projectModel();
}