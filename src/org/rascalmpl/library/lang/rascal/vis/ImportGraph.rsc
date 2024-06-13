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

void importGraph(str projectName) {
    importGraph(|project://<projectName>|);
}

void importGraph(loc projectRoot) {
    // we use compiler() mode here to avoid diving into the transitively depended projects.
    importGraph(getProjectPathConfig(projectRoot, mode=compiler()));
}

@synopsis{Visualizes an import/extend graph for all the modules in the srcs roots of the current PathConfig}
void importGraph(PathConfig pcfg) {
    m = getProjectModel(pcfg.srcs);
    
    // let's start with a simple graph and elaborate on details in later versions
    g = { <from, "I", to> | <from, to> <- m.imports}
      + { <from, "E", to> | <from, to> <- m.extends}
      ;
    
    showInteractiveContent(graph(g, \layout=defaultDagreLayout()), title="Rascal Import/Extend Graph");
}

data ProjectModel = projectModel(
    set[str]      modules = {},
    set[str]      external = {},
    rel[str, str] imports = {},
    rel[str, str] extends = {}
);

@synopsis{Collects name, imports and extends for all modules reachable from the `srcs` root folders.}
ProjectModel getProjectModel(list[loc] srcs) {
    allFiles = {*find(src, "rsc") | src <- srcs};
    
    models = {getProjectModel(f) | f <- allFiles};

    wholeWorld = projectModel(
        modules = {*m.modules | m <- models},
        imports = {*m.imports | m <- models},
        extends = {*m.extends | m <- models}
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
            imports = {<name, i> | i <- imps},
            extends = {<name, e> | e <- exts}
        );
    }
    catch ParseError(_) : 
        return projectModel();
}