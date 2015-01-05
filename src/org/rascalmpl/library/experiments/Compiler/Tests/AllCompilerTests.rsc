module experiments::Compiler::Tests::AllCompilerTests

/*
 * A suite of tests for the Rascal compiler:
 * - import this module in a RascalShell
 * - Type :test at the command line.
 * - Go and drink some latte ;-)
 */
 
import experiments::Compiler::Compile;
import experiments::Compiler::Execute;

import util::ShellExec;
import IO;

// Note: Type commented out since it uses a definition of datatype D that is incompatible with TestUtils
// extend experiments::Compiler::Tests::Types;
extend experiments::Compiler::Tests::Booleans;
extend experiments::Compiler::Tests::Expressions;
extend experiments::Compiler::Tests::Statements;
extend experiments::Compiler::Tests::Patterns;
extend experiments::Compiler::Tests::StringTemplates;

extend experiments::Compiler::Examples::Run;

// extend experiments::Compiler::Tests::GetGrammarTest;

// This could work if we get the working directory of the created proces right ... or if we add a remove file function
//void clean(){
// command = "/bin/ls"; // will become "/bin/rm"
// for(subdir <- ["", "util", "experiments/Compiler/Benchmarks", "experiments/Compiler/Examples", "experiments/Compiler/Tests", "experiments/Compiler/muRascal2RVM"]){ 
//     dir = |home:///Documents/workspace-level1/rascal/src/org/rascampl/library/| + subdir;
//     pid = createProcess(command, [l.path | l <- listEntries(dir), l.extension == "rvm"], dir);
//     println(readEntireerrStream(pid));
//  }
//}

