module lang::rascal::boot::BuildBootFiles

/********************************************************************************/
/*          Build Boot files for Rascal deployment                              */
/*                                                                              */
/* Compile and serialize:                                                       */
/* - (Selected parts of the) Rascal standard library                            */
/* - MuLibrary: part of the runtime system written in muRascal                  */
/* - ParserGenerator                                                            */
/* - Kernel: Rascal's compile and execute kernel                                */
/*                                                                              */
/* Usage:                                                                       */
/* - Adjust BOOT, where the compiled boot files will be stored                  */
/* - Adjust SHELLSCRIPT, a simple shell script that will overwrite the current  */
/*   boot files for Kernel, MuLibrary and ParserGenerator                       */
/* [ Optional steps only needed when muLibrary or RVM have been changed:        */
/*   - buildMuLibrary()                                                         */
/*   - execute SHELLSCRIPT                                                      */
/* ]                                                                            */
/* - build() (or main() if you prefer)                                          */
/* - if all went well: execute SHELLSCRIPT                                      */
/*                                                                              */
/* The final structure of BOOT will be:                                         */
/* BOOT/Kernel.rvm.ser.gz                                                       */
/* BOOT/MuLibrary.rvm.gz                                                        */
/* BOOT/ParserGenerator.rvm.ser.gz                                              */
/* BOOT/stdlib/*                                                                */
/*                                                                              */
/*                                                                              */
/* TODO:                                                                        */
/* - Add generated Java code for Rascal Parser (at the moment we reuse          */
/*   org.rascalmpl.library.lang.rascal.syntax.RascalParser                      */
/* - Better handling of BOOT and SHELLSCRIPT (keyword parameters of main)?       */
/********************************************************************************/

import IO;
import ValueIO;
import String;
import util::Reflective;
import util::FileSystem;
import experiments::Compiler::Execute;
import experiments::Compiler::Compile;
import experiments::Compiler::CompileMuLibrary; 

//loc BOOT = |home:///Workspaces/Rascal/rascal/src/boot/|;
loc BOOT = |file:///Users/paulklint/git/rascal/src/boot|;
loc BINBOOT = |home:///bin-boot|;
loc SHELLSCRIPT = |home:///install.sh|;

// Library modules that will be included in boot/stdlib

list[str] libraryModules =
[
    //"APIGen",
    "Ambiguity",
    "Boolean",
    "DateTime",
    "Exception",
    "Grammar",
    "IO",
    "List",
    "ListRelation",
    "Map",
    "Message",
    "Node",
    //"Origins",
    "ParseTree",
    "Prelude",
    "Relation",
    "Set",
    "String",
    "ToString",
    //"Traversal",
    "Tuple",
    "Type",
    "ValueIO",

    "util::Benchmark",
    "util::Brackets",
    "util::Cursor",
    "util::Eval",
    "util::FileSystem",
    "util::Highlight",
//    "util::LOC", data type name Output conflicts with Rascal.rsc
    "util::Math",
    "util::Maybe",
    "util::Monitor",
    "util::PriorityQueue",
    "util::REPL",
    "util::Reflective",
    "util::ShellExec",
    "util::SystemAPI",
    "util::UUID",
    "util::Webserver"    
];

str moduleName2Bin(str moduleName, str ext) =
    replaceAll(moduleName, "::", "/") + "." + ext;

// Compile and serialize a module and generate a command to move the result to the root of the BOOT directory

str serialize(str moduleName, PathConfig pcfg, bool jvm=true){
     report("Compiling <moduleName>");
     compileAndLink(moduleName, pcfg, verbose=true, jvm=jvm);
     serialized = getDerivedWriteLoc(moduleName, "rvm.ser.gz", pcfg);
     cmd = "cp .<serialized.path> <(BOOT + moduleName2Bin(moduleName, "rvm.ser.gz")).path>\n";
     return cmd;
}

// Fancy reporting

void report(str msg){
    println("**** <msg> ****");
}

// Build the MuLibrary separately.
// Danger: when running 'build' it uses the currently installed MuLibrary.
// After serious changes to that library it is therefore necessary to first
// build the MuLibrary, install it, end then do the complete build.

void buildMuLibrary(){
     pcfg = pathConfig(srcs=[|std:///|], bin=BINBOOT, libs=[BINBOOT]);
     commands = "#!/bin/sh\n";
     report("Compiling MuLibrary");
     //compileMuLibrary(pcfg, verbose=true);
     compileMuLibrary(pcfg.srcs, pcfg.libs, pcfg.boot, pcfg.bin)
     muLib = getMuLibraryCompiledWriteLoc(pcfg);
     commands += "cp .<muLib.path> <(BOOT + muLib.file).path>\n";
     writeFile(SHELLSCRIPT, commands);
     report("Commands written to <SHELLSCRIPT>");
}

// Build MuLibrary, standard library, ParserGenerator and Kernel
// Maybe run buildMuLibrary first!

value build(bool jvm=true, bool full=true){
     println("build: full = <full>, jvm = <jvm>");

     pcfg = pathConfig(srcs=[|std:///|], bin=BINBOOT, libs=[BINBOOT]);
     
     if(full){
        report("Removing current compiled boot files <BINBOOT>");
        remove(BINBOOT);
     }
     
     commands = "#!/bin/sh\n";
     
     report("Compiling MuLibrary");
     compileMuLibrary(pcfg, verbose=true, jvm=jvm);
     muLib = getMuLibraryCompiledWriteLoc(pcfg);
     commands += "cp .<muLib.path> <(BOOT + muLib.file).path>\n";
 
     report("Compiling standard library modules");
     for(moduleName <- libraryModules){
         compile(moduleName, pcfg, recompile=true, verbose=true, jvm=jvm);
     }
    
     
     commands += serialize("lang::rascal::grammar::ParserGenerator", pcfg, jvm=jvm);
     commands += serialize("lang::rascal::boot::Kernel", pcfg, jvm=jvm);
    
     writeFile(SHELLSCRIPT, commands);
     report("Commands written to <SHELLSCRIPT>");
     return true;
}

value main() = build();
