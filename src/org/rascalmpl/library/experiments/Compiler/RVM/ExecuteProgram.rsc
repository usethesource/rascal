module experiments::Compiler::RVM::ExecuteProgram

import experiments::Compiler::RVM::AST;

import Type;
                                            
@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecuteProgram}
@reflect{Uses execution context}
public java value executeProgram(
                    loc executable,
                    RVMProgram program,
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
                    loc program,
					list[value] arguments, 
					bool debug, 
					bool testsuite,
					bool profile,
					bool trackCalls,
					bool coverage,
					bool useJVM);