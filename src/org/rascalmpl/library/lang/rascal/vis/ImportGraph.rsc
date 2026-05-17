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

import Exception;
import IO;
import Set;
import String;
import analysis::graphs::Graph;
import lang::rascal::\syntax::Rascal;
import lang::rascal::grammar::definition::Modules;
import util::FileSystem;
import util::IDEServices;
import util::Reflective;
import vis::Graphs;

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

    gClosed = g+;

    list[str] nodeClass(str n) = [
        *["external" | n in    m.external],
        *["project"  | n notin m.external]
    ];

    list[str] edgeClass(str from, str to) = [
        *["extend"     | <from, to> in m.extends],
        *["import"     | <from, to> in m.imports],
        *["transitive" | <from, to> in g o gClosed, <from, from> notin gClosed, <to,to> notin gClosed],
        *["cyclic"     | <from, from> in gClosed]
    ];

    styles = [
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
        ),
        
        cytoStyleOf( 
            selector=\node(\className("hover")),
            style=defaultNodeStyle()[\background-color="yellow"][color="black"]
        ),

        cytoStyleOf( 
            selector=\edge(\className("hover")),
            style=defaultEdgeStyle()[\line-color="red"][\target-arrow-color="red"]
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
            style=defaultEdgeStyle()[\line-color="orange"][\target-arrow-color="orange"]
        ),

        cytoStyleOf( 
            selector=\edge(className("hover-in")),
            style=defaultEdgeStyle()[\line-color="red"][\target-arrow-color="red"]
        ),

        cytoStyleOf( 
            selector=\node(className("hover-source")),
            style=defaultNodeStyle()[\background-color="orange"]
        ),

        cytoStyleOf( 
            selector=\node(className("hover-target")),
            style=defaultNodeStyle()[\background-color="red"]
        )
    ];

    loc modLinker(str name) {
        if (loc x <- m.files[name])
            return x;
        else 
            return |nothing:///|;
    }

    default loc modLinker(value _) = |nothing:///|;

    str modTip(str name) {
        return "module <name> 
               '<if (m.imports[name] != {}) {>
               '<for (str i <- sort(m.imports[name])) {>import <i>;
               '<}><}>
               '<if (m.extends[name] != {}) {><for (str i <- sort(m.extends[name])) {>extend <i>;
               '<}><}>";
    }

    str importTip(str from, str to) {
        kind = <from, to> in m.imports ? "imports" : "extends";
        return "<from>
               '<kind>
               '<to>";
    }

    str modLabel(str name) = split("::", name)[-1];

    cfg = cytoGraphConfig(
        \layout=defaultDagreLayout()[rankSep=100],
        styles=styles,
        title="Rascal Import/Extend Graph",
        nodeTipper=modTip,
        nodeClassifier=nodeClass,
        edgeClassifier=edgeClass,
        edgeTipper=importTip,
        nodeLabeler=modLabel,
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
