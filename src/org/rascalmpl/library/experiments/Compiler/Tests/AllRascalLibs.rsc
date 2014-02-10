module experiments::Compiler::Tests::AllRascalLibs

import Prelude;
import experiments::Compiler::Compile;

/*
 * Results of compiling Rascal library modules.
 */

list[str] libs = [

/*
"Boolean", 			// OK
"DateTime",			// OK
"Exception", 		// OK
"Grammar", 			// OK
"IO",				// OK
"List", 			// OK
"ListRelation",		// OK
"Map", 				// OK
"Message", 			// OK
"Node",				// OK
"Origins",			// OK
"ParseTree", 		// OK		
"Prelude",			// OK	
"Relation",			// OK
"Set",				// OK
"String",			// OK
"Time", 			// OK
"Type", 			// OK
"ToString", 		// OK
"Traversal",		// OK
"Tuple", 			// OK
"ValueIO", 			// OK
"util::Benchmark",	// OK
"util::Eval",		// OK
"util::FileSystem", // OK
"util::Highlight",	// OK
"util::Math",		// OK
"util::Maybe",		// OK
"util::Monitor",	// OK
"util::PriorityQueue",// OK
"util::Reflective", 	// OK
"util::ShellExec",	// OK
"util::Webserver",		// OK

// Not yet OK

"Ambiguity",			// #483
						//|rascal://lang::rascal::types::CheckTypes|(31671,1,<634,13>,<634,14>): Expected map[RName, int], but got map[RName, value]     	


"APIGen", 			 	// #482
						//error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3641,16,<89,62>,<89,78>))
						//error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3281,33,<80,24>,<80,57>))
						//error("Name cs is not in scope",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7837,2,<207,39>,<207,41>))
						//error("Expected type bool, found fail",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7726,25,<206,46>,<206,71>))
						//error("Multiple constructors and/or productions match this pattern, add additional type annotations",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(2134,15,<61,11>,<61,26>))
						//error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(2134,15,<61,11>,<61,26>))
						//error("Field definitions does not exist on type type",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7738,13,<206,58>,<206,71>))
						//error("Name t2 is not in scope",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3093,2,<73,87>,<73,89>))
						//error("Function of type fun str(str) cannot be called with argument types (inferred(11))",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(2159,18,<61,36>,<61,54>))
						//error("Multiple constructors and/or productions match this pattern, add additional type annotations",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(6028,12,<166,11>,<166,23>))
						//error("Function of type fun str(Symbol) cannot be called with argument types (inferred(41))",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3603,21,<89,24>,<89,45>))
						//error("Function of type fun str(Symbol) cannot be called with argument types (value)",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3468,22,<85,26>,<85,48>))
						//error("Field definitions does not exist on type type",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7769,13,<206,89>,<206,102>))
						//error("Expected type str, found fail",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7795,48,<206,115>,<207,45>))
						//error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(6028,12,<166,11>,<166,23>))
						//error("Expected type bool, found fail",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7753,39,<206,73>,<206,112>))
						//error("Invalid return type str, expected return type void",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3323,61,<81,5>,<81,66>))
						//error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3259,56,<80,2>,<80,58>))

// "Number"				// DEPRECATED: TC gives errors


"util::LOC"			// #394
						// error("Field top does not exist on type Tree",|std:///util/LOC.rsc|(943,5,<44,8>,<44,13>))
						*/

"analysis::formalconcepts::FCA",
"analysis::graphs::Graph",				
"analysis::graphs::LabeledGraph",
"analysis::linearprogramming::LinearProgramming",
"analysis::m3::Core",
"analysis::m3::Registry",
"analysis::statistics::Correlation",
"analysis::statistics::Descriptive",
"analysis::statistics::Frequency",
"analysis::statistics::Inference",
"analysis::statistics::SimpleRegression",
"demo::basic::Ackermann",
"demo::basic::Bottles",
"demo::basic::Bubble",
"demo::basic::BubbleTest",
"demo::basic::Factorial",
"demo::basic::FactorialTest",
"demo::basic::FizzBuzz",
"demo::basic::Hello",
"demo::basic::Quine",
"demo::basic::Squares",
"demo::common::WordCount::CountInLine1",
"demo::common::WordCount::CountInLine2",
"demo::common::WordCount::CountInLine3",
"demo::common::WordCount::WordCount",
//"demo::common::Calls",
"demo::common::ColoredTrees",
"demo::common::ColoredTreesTest",
"demo::common::CountConstructors",
"demo::common::Crawl",
"demo::common::Cycles",
"demo::common::Derivative",
"demo::common::Lift",
"demo::common::LiftTest",
"demo::common::StringTemplate",
"demo::common::StringTemplateTest",
"demo::common::Trans",
"demo::common::WordReplacement",
"demo::common::WordReplacementTest",
"demo::lang::Exp::Abstract::Eval",
"demo::lang::Exp::Combined::Automatic::Eval",
"demo::lang::Exp::Combined::Manual::Eval",
"demo::lang::Exp::Concrete::NoLayout::Eval",
"demo::lang::Exp::Concrete::WithLayout::Eval",
"demo::lang::Pico::Compile",
"demo::lang::Pico::ControlFlow",
"demo::lang::Pico::Eval",
"demo::lang::Pico::Typecheck",
"demo::lang::Pico::Uninit",
"demo::lang::Pico::UseDef",
"demo::lang::Pico::Visualize"
/*
**** Compiling demo::lang::Pico::Compile ****
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(2074,9,<81,29>,<81,38>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(1747,43,<69,0>,<69,43>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(1416,72,<56,19>,<57,52>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(988,146,<41,19>,<43,53>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(421,38,<18,7>,<18,45>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(309,13,<14,18>,<14,31>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(382,2,<16,18>,<16,20>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(624,20,<24,18>,<24,38>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(969,166,<41,0>,<43,54>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(536,19,<21,18>,<21,37>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(1767,22,<69,20>,<69,42>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(364,32,<16,0>,<16,32>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(252,13,<12,18>,<12,31>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(439,3,<18,25>,<18,28>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(1397,92,<56,0>,<57,53>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(893,27,<38,19>,<38,46>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(874,47,<38,0>,<38,47>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(252,6,<12,18>,<12,24>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(439,19,<18,25>,<18,44>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(291,32,<14,0>,<14,32>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(1869,37,<74,0>,<74,37>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(893,7,<38,19>,<38,26>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(624,4,<24,18>,<24,22>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(536,3,<21,18>,<21,21>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(988,10,<41,19>,<41,29>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(309,6,<14,18>,<14,24>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(382,13,<16,18>,<16,31>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(234,32,<12,0>,<12,32>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(1416,9,<56,19>,<56,28>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(518,38,<21,0>,<21,38>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(2052,32,<81,7>,<81,39>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(1889,16,<74,20>,<74,36>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Compile.rsc|(606,39,<24,0>,<24,39>))
**** Compiling demo::lang::Pico::ControlFlow ****
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(1206,41,<36,0>,<36,41>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(1005,41,<30,18>,<30,59>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(1504,31,<44,7>,<44,38>))
error("On constructor definitions, either all fields should be labeled or no fields should be labeled",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(278,29,<11,3>,<11,32>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(1225,21,<36,19>,<36,40>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(987,60,<30,0>,<30,60>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(462,7,<16,20>,<16,27>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(1525,9,<44,28>,<44,37>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(610,10,<21,18>,<21,28>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(460,29,<16,18>,<16,47>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(610,177,<21,18>,<23,53>))
error("On constructor definitions, either all fields should be labeled or no fields should be labeled",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(311,39,<12,3>,<12,42>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(592,196,<21,0>,<23,54>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(1005,9,<30,18>,<30,27>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/ControlFlow.rsc|(442,48,<16,0>,<16,48>))
**** Compiling demo::lang::Pico::Eval ****
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1763,9,<49,19>,<49,28>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(344,8,<14,37>,<14,45>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(2162,16,<68,15>,<68,31>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(429,46,<18,0>,<18,46>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(325,17,<14,18>,<14,35>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(2311,27,<73,7>,<73,34>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1500,172,<43,0>,<46,23>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(451,2,<18,22>,<18,24>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1405,32,<38,14>,<38,46>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(2040,8,<59,39>,<59,47>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(2016,22,<59,15>,<59,37>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1410,7,<38,19>,<38,26>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(580,23,<21,18>,<21,41>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(2328,9,<73,24>,<73,33>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(584,3,<21,22>,<21,25>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1758,77,<49,14>,<50,52>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(447,17,<18,18>,<18,35>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1851,8,<51,14>,<51,22>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(466,8,<18,37>,<18,45>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(851,8,<26,43>,<26,51>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1391,57,<38,0>,<38,57>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(329,6,<14,22>,<14,28>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1121,53,<31,0>,<31,53>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(390,6,<16,22>,<16,28>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(830,3,<26,22>,<26,25>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1744,116,<49,0>,<51,23>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(2147,32,<68,0>,<68,32>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1519,10,<43,19>,<43,29>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(2001,48,<59,0>,<59,48>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(808,52,<26,0>,<26,52>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1143,4,<31,22>,<31,26>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(368,46,<16,0>,<16,46>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1165,8,<31,44>,<31,52>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(405,8,<16,37>,<16,45>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(605,8,<21,43>,<21,51>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1663,8,<46,14>,<46,22>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1514,133,<43,14>,<45,53>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1439,8,<38,48>,<38,56>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(307,46,<14,0>,<14,46>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(1139,24,<31,18>,<31,42>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(562,52,<21,0>,<21,52>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(386,17,<16,18>,<16,35>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Eval.rsc|(826,23,<26,18>,<26,41>))
**** Compiling demo::lang::Pico::Typecheck ****
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(210,39,<9,0>,<9,39>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1828,7,<44,20>,<44,27>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1090,23,<29,14>,<29,37>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1555,59,<37,0>,<37,59>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(2419,120,<61,0>,<63,26>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(650,52,<19,0>,<19,52>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1573,4,<37,18>,<37,22>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(491,17,<16,14>,<16,31>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(3060,9,<84,25>,<84,34>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(2434,77,<61,15>,<62,52>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(510,8,<16,33>,<16,41>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(2077,10,<51,20>,<51,30>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(668,6,<19,18>,<19,24>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1359,8,<33,39>,<33,47>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1125,8,<29,49>,<29,57>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(664,17,<19,14>,<19,31>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(693,8,<19,43>,<19,51>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1569,24,<37,14>,<37,38>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(413,7,<12,22>,<12,29>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(495,6,<16,18>,<16,24>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(2711,8,<70,40>,<70,48>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(2687,22,<70,16>,<70,38>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(2057,224,<51,0>,<54,24>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(3042,28,<84,7>,<84,35>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(2859,33,<79,0>,<79,33>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(2072,183,<51,15>,<53,53>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1808,58,<44,0>,<44,58>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1320,58,<33,0>,<33,58>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(828,8,<22,43>,<22,51>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(404,7,<12,13>,<12,20>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(2439,9,<61,20>,<61,29>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1605,8,<37,50>,<37,58>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1094,3,<29,18>,<29,21>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(785,52,<22,0>,<22,52>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(2530,8,<63,17>,<63,25>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1115,8,<29,39>,<29,47>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(683,8,<19,33>,<19,41>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1823,32,<44,15>,<44,47>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(799,17,<22,14>,<22,31>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(391,30,<12,0>,<12,30>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(818,8,<22,33>,<22,41>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1334,23,<33,14>,<33,37>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(314,6,<11,13>,<11,19>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1595,8,<37,40>,<37,48>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1338,3,<33,18>,<33,21>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(2272,8,<54,15>,<54,23>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(2875,16,<79,16>,<79,32>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(803,2,<22,18>,<22,20>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1076,58,<29,0>,<29,58>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(301,29,<11,0>,<11,29>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(2671,49,<70,0>,<70,49>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(520,8,<16,43>,<16,51>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1857,8,<44,49>,<44,57>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(224,8,<9,14>,<9,22>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(477,52,<16,0>,<16,52>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Typecheck.rsc|(1369,8,<33,49>,<33,57>))
**** Compiling demo::lang::Pico::Uninit ****
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Uninit.rsc|(375,9,<13,37>,<13,46>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Uninit.rsc|(227,20,<10,39>,<10,59>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Uninit.rsc|(216,9,<10,28>,<10,37>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Uninit.rsc|(195,53,<10,7>,<10,60>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Uninit.rsc|(345,40,<13,7>,<13,47>))
**** Compiling demo::lang::Pico::UseDef ****
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(879,9,<27,28>,<27,37>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(370,146,<13,0>,<15,50>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(298,53,<11,0>,<11,53>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(325,7,<11,27>,<11,34>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(858,31,<27,7>,<27,38>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(570,95,<18,0>,<19,50>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(147,5,<7,24>,<7,29>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(595,69,<18,25>,<19,49>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(123,43,<7,0>,<7,43>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(703,48,<22,0>,<22,48>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(323,27,<11,25>,<11,52>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(395,120,<13,25>,<15,49>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(154,11,<7,31>,<7,42>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(817,9,<25,28>,<25,37>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(729,21,<22,26>,<22,47>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(796,31,<25,7>,<25,38>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(398,10,<13,28>,<13,38>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/UseDef.rsc|(598,9,<18,28>,<18,37>))
**** Compiling demo::lang::Pico::Visualize ****
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1265,9,<36,24>,<36,33>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1289,7,<36,48>,<36,55>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(327,29,<15,0>,<15,29>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1105,6,<33,24>,<33,30>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(207,23,<12,0>,<12,23>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(336,3,<15,9>,<15,12>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(216,6,<12,9>,<12,15>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1505,4,<42,10>,<42,14>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1495,17,<42,0>,<42,17>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(454,20,<17,9>,<17,29>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(249,6,<13,9>,<13,15>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1626,28,<47,21>,<47,49>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(278,41,<14,9>,<14,50>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1463,19,<41,10>,<41,29>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1612,43,<47,7>,<47,50>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(269,51,<14,0>,<14,51>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(445,30,<17,0>,<17,30>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(541,26,<21,0>,<21,26>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1453,30,<41,0>,<41,30>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(964,31,<30,0>,<30,31>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(395,3,<16,9>,<16,12>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(859,5,<27,24>,<27,29>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(395,19,<16,9>,<16,28>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(558,8,<21,17>,<21,25>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(979,15,<30,15>,<30,30>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(835,44,<27,0>,<27,44>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(988,4,<30,24>,<30,28>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1523,27,<43,0>,<43,27>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(850,28,<27,15>,<27,43>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(386,29,<16,0>,<16,29>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(278,30,<14,9>,<14,39>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1463,5,<41,10>,<41,15>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1256,61,<36,15>,<36,76>))
error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1541,8,<43,18>,<43,26>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1096,38,<33,15>,<33,53>))
error("Constructor or production name is not in scope",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(454,4,<17,9>,<17,13>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(249,13,<13,9>,<13,22>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(240,23,<13,0>,<13,23>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1081,54,<33,0>,<33,54>))
error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1241,77,<36,0>,<36,77>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(336,19,<15,9>,<15,28>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(216,13,<12,9>,<12,22>))
error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/demo/lang/Pico/Visualize.rsc|(1505,6,<42,10>,<42,16>))

ERRORS:

analysis::formalconcepts::FCA: Module contains errors!
analysis::linearprogramming::LinearProgramming: Module contains errors!
analysis::m3::Core: Module contains errors!
analysis::m3::Registry: Module contains errors!
analysis::statistics::Frequency: Module contains errors!
demo::common::ColoredTreesTest: Module contains errors!
demo::common::Crawl: Module contains errors!
demo::lang::Exp::Abstract::Eval: Module contains errors!
demo::lang::Exp::Combined::Automatic::Eval: Module contains errors!
demo::lang::Exp::Combined::Manual::Eval: Module contains errors!
demo::lang::Pico::Compile: Module contains errors!
demo::lang::Pico::ControlFlow: Module contains errors!
demo::lang::Pico::Eval: Module contains errors!
demo::lang::Pico::Typecheck: Module contains errors!
demo::lang::Pico::Uninit: Module contains errors!
demo::lang::Pico::UseDef: Module contains errors!
demo::lang::Pico::Visualize: Module contains errors!
*/

];

value main(list[value] args){
  crashes = [];
  for(lib <- libs){
    println("**** Compiling <lib> ****");
    try {
	    //compile("module TMP  extend Exception; extend <lib>;", recompile=true);
	    
    	compile(|project://rascal/src/org/rascalmpl/library/| + (replaceAll(lib, "::", "/") + ".rsc"), recompile=true);
    } catch e: {
      crashes += <lib, "<e>">;
    }
  }
  if(size(crashes) > 0){
    println("\nERRORS:\n");
     for(<lib, msg> <- crashes){
       println("<lib>: <msg>");
    }
  }
  return true;
}