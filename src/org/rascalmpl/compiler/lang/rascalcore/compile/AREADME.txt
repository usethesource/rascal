README for Rascal Compiler

The Rascal compiler translates Rascal code to code for the Rascal Virtual Machine (RVM) and JVM

Used file extensions:

rsc			Rascal source code
rvm			Compressed RVM code for single module
rvmx		Serialized and compressed RVM code for linked Rascal application

Conventions:

By default, all compiled code is written to the directory |home:///bin|.

The main functions that are provided are (outdated):

compile			RVMProgram compile(					// Compile a Rascal file
					loc moduleLoc, 					// source location of module to be compiled
					loc bindir = |home:///bin|)		// directory where binaries reside
					
execute			value execute(						// Execute a Rascal file
					loc rascalSource, 				// source location of top level module to be executed
					list[value] arguments, 			// list of arguments for `main`
					bool debug=false, 				// Print each RVM instruction that is executed
					bool testsuite=false,			// Execute as testsuite
					bool recompile=false, 			// Recompile before executing
					bool profile=false, 			// Profile execution time
					bool trackCalls=false,  		// Print a traca of called functions and their arguments
					bool coverage=false, 			// Measure code coverage
					bool useJVM=false, 				// Generate JVM code
					bool serialize=false,			// Write serialized version of linked RVM code
					loc bindir = |home:///bin|)		// directory where binaries reside

inspect			void inspect(						// Inspect a compiled Rascal file
					loc srcLoc,                		// Location of Rascal source file
          			loc bindir = |home:///bin|,   	// Location where binaries reside
         			Query select = none(),     		// Query to define what to show
          			int line = -1,					// Select line of function to be shown
          			bool listing = false)         	// Show instruction listing of selected functions

config			void config(						// Inspect type checker configuration file
					loc src,                		// location of Rascal source file
            		loc bindir = |home:///bin|,		// directory where binaries reside
            		Query select = none())			// Query to define what to show
Examples:

compile(|std:///experiments/Compiler/Examples/Fac.rsc|);

execute(|std:///experiments/Compiler/Examples/Fac.rsc|, []);
execute(|std:///experiments/Compiler/Examples/Fac.rsc|, [], recompile=true);
execute(|std:///experiments/Compiler/Examples/Fac.rsc|, [], profile=true);

inspect(|std:///experiments/Compiler/Examples/Fac.rsc|)


Directory structure:

AREADME			This file
AToDoList		Todo list for compiler project

Compile.rsc		Compile a Rascal module
Execute.rsc		Execute a Rascal module
Coverage.rsc	Coverage measurement and reporting
Profile.rsc		Profile measurement and reporting
Inspect.rsc		Inspect RVM and config (type checker) files

Subdirectories:

Benchmarks		Examples for benchmarking
Commands        Commands including rascal, rascalc and rascalTests for running Rascal from the command line
Examples		Other examples
RVM				Defines the RVM (Rascal Virtual Machine) and its implementation
Rascal2muRascal	Translator Rascal -> muRascal
Tests			Compiler tests and drivers to run all standard Rascal tests
muRascal		Defined the muRascal language
muRascal2RVM	Translator muRascal -> RVM