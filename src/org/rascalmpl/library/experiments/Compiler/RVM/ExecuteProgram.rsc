module experiments::Compiler::RVM::ExecuteProgram

import experiments::Compiler::RVM::AST;

import Type;
                                            
@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecuteProgram}
@reflect{Uses execution context}
public java value executeProgram(
                    loc rvmProgramLoc,
                    RVMProgram rvmProgram,
					list[value] arguments, 
					bool debug, 
					bool testsuite,
					bool profile,
					bool trackCalls,
					bool coverage,
					bool useJVM,
					bool serialize);	
										    
										    
@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecuteProgram}
@reflect{Uses execution context}
public java value executeProgram(
                    loc rvmExecutableLoc,
					list[value] arguments, 
					bool debug, 
					bool testsuite,
					bool profile,
					bool trackCalls,
					bool coverage,
					bool useJVM);