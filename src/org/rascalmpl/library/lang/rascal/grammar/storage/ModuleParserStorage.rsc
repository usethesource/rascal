@synopsis{Functionality for caching module parsers}
@description{
The Rascal interpreter can take a lot of time while loading modules.
In particular in deployed situations (Eclipse and VScode plugins), the 
time it takes to load the parser generator for generating the parsers
which are required for analyzing concrete syntax fragments is prohibitive (20s).
This means that the first syntax highlighting sometimes can only appear
after more than 20s after loading an extension (VScode) or plugin (Eclipse).

This "compiler" takes any number of Rascal modules and extracts a grammar
for each of them, in order to use the ((Library::ParseTree)) module's
functions ((saveParsers)) on them respectively to store each parser
in a `.parsers` file. 

After that the Rascal interpreter has a special mode for using ((loadParsers))
while importing a new module if a cache `.parsers` file is present next to 
the `.rsc` respective file.
}
@benefits{
* loading modules without having to first load and use a parser generator can be up 1000 times faster.
}
@pitfalls{
:::warning
This caching feature is _static_. There is no automated cache clearance.
If your grammars change, any saved `.parsers` files do not change with it. 
It is advised that you programmatically execute this compiler at deployment time
to store the `.parsers` file _only_ in deployed `jar` files. That way, you can not
be bitten by a concrete syntax parser that is out of date at development time.
:::
}
@license{
  Copyright (c) 2009-2023 NWO-I CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@bootstrapParser
module lang::rascal::grammar::storage::ModuleParserStorage

import lang::rascal::grammar::definition::Modules;
import lang::rascal::\syntax::Rascal;
import util::Reflective;
import util::FileSystem;
import Location;
import ParseTree;
import Grammar;
import IO;

@synopsis{For all modules in pcfg.srcs this will produce a `.parsers` stored parser capable of parsing concrete syntax fragment in said module.}
@description{
Use ((loadParsers)) to retrieve the parsers stored by this function. In particular the
Rascal interpreter will use this instead of spinning up its own parser generator.
}
void storeParsersForModules(PathConfig pcfg) {
    storeParsersForModules({*find(src, "rsc") | src <- pcfg.srcs, bprintln("Crawling <src>")}, pcfg);
}
    
void storeParsersForModules(set[loc] moduleFiles, PathConfig pcfg) {
    storeParsersForModules({parseModule(m) | m <- moduleFiles, bprintln("Loading <m>")}, pcfg);
}

void storeParsersForModules(set[Module] modules, PathConfig pcfg) {
    for (m <- modules) {
        storeParserForModule("<m.header.name>", m@\loc, modules, pcfg);
    }
}

void storeParserForModule(str main, loc file, set[Module] modules, PathConfig pcfg) {
    // this has to be done from scratch due to different ways combining layout definitions
    // with import and extend. Each main module has a different grammar because of this.
    def = modules2definition(main, modules);
    gr = fuse(def);
    target = pcfg.bin + relativize(pcfg.srcs, file)[extension="parsers"].path;
    println("Generating parser for <main> at <target>");
    if (type[Tree] rt := type(sort("Tree"), gr.rules)) {
        storeParsers(rt, target);
    }
}