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
void importGraph(PathConfig pcfg, bool hideExternals=true, bool hideTestModules=true) {
    m = getProjectModel(pcfg.srcs);
    
    // let's start with a simple graph and elaborate on details in later versions
    g = { <from, to> | <from, to> <- sort(m.imports), hideExternals ==> to notin m.external, hideTestModules ==> {from, to} & m.tests == {}}
      + { <from, to> | <from, to> <- sort(m.extends), hideExternals ==> to notin m.external, hideTestModules ==> {from, to} & m.tests == {}}
      ;

    list[str] nodeClass(str n) = [
        *["external" | n in    m.external],
        *["project"  | n notin m.external]
    ];

    int edgeWeight(str from, str to) {
        if (<from, to> in g o gClosed, <from, from> notin gClosed, <to, to> notin gClosed) {
            return 1; // transitive edges should not influence things.
        }
        else if (<from, to> in m.extends) {
            // extend structure is very important
            return 1;
        }
        else {
            return 1;
        }
    }
    
    gClosed = g+;

    list[str] edgeClass(str from, str to) = [
        *["nottop"    | from != "_"],
        *["extend"     | <from, to> in m.extends],
        *["import"     | <from, to> in m.imports],
        *["transitive" | <from, to> in g o gClosed, <from, from> notin gClosed, <to,to> notin gClosed],
        *["cyclic"     | <from, from> in gClosed]
    ];

    styles = [
        cytoStyleOf( 
            selector=\node(\className("hover")),
            style=defaultNodeStyle()[\background-color="yellow"][color="black"]
        ),

        cytoStyleOf( 
            selector=\node(className("hover-in")),
            style=defaultNodeStyle()[\background-color="red"][color="black"] 
        ),

        cytoStyleOf( 
            selector=\node(className("hover-out")),
            style=defaultNodeStyle()[\background-color="orange"][color="black"]
        ),

        cytoStyleOf( 
            selector=\edge(className("hover-out")),
            style=defaultEdgeStyle()[\line-color="orange"]
        ),

        cytoStyleOf( 
            selector=\node(id("_")),
            style=defaultNodeStyle()[visibility="hidden"]
        ),

        cytoStyleOf(
            selector=\edge(className("extend")),
            style=defaultEdgeStyle()[\line-style="dashed"] 
        ),

        cytoStyleOf(
            selector=\edge(className("transitive")),               
            style=defaultEdgeStyle()[opacity=".25"][\line-opacity="0.50"]  
        )
,
        cytoStyleOf(
            selector=\edge(className("cyclic")),               
            style=defaultEdgeStyle()[opacity="1"][\line-opacity="1"][\width=10]  
        )
    ];

    loc modLinker(str name) {
        if (loc x <- m.files[name])
            return x;
        else 
            return |nothing:///|;
    }

    default loc modLinker(value _) = |nothing:///|;

    cfg = cytoGraphConfig(
        \layout=defaultDagreLayout()[ranker=\network-simplex()][rankSep=200][ranker=\longest-path()][debugDagreEdgeControlPoints=true],
        styles=styles,
        title="Rascal Import/Extend Graph",
        nodeClassifier=nodeClass,
        edgeClassifier=edgeClass,
        edgeWeigher=edgeWeight,
        nodeLinker=modLinker
    );

    showInteractiveContent(graph(g, cfg=cfg), title=cfg.title);
}

@synopsis{Container for everything we need to know about the modules in a project to visualize it.}
data ProjectModel = projectModel(
    set[str]      modules = {},
    set[str]      tests = {},
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
        files   = {*m.files   | m <- models},
        tests  =  {*m.tests   | m <- models}
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
            files   = {<name, file>},
            tests   = {name | /test/ := file.path}
        );
    }
    catch ParseError(_) : 
        return projectModel();
}
