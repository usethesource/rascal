module experiments::Compiler::RVM::Interpreter::ExecuteProgram

import experiments::Compiler::RVM::AST;

import Type;

@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecuteProgram}
public java value linkAndSerializeProgram(
                    loc rvmProgramLoc,
                    RVMProgram rvmProgram,
                    bool jvm, 
                    map[str,str] classRenamings /* for bootstrapping to a refactored RVM hierarchy */);
                                            
@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecuteProgram}
@reflect{Uses execution context}
public java value executeProgram(
                    RVMProgram rvmProgram,
					map[str,value] keywordArguments, 
					bool debug, 
					bool debugRVM, 
					bool testsuite,
					bool profile,
					bool trace,
					bool coverage,
					bool jvm);	
										    
										    
@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecuteProgram}
@reflect{Uses execution context}
public java value executeProgram(
                    loc rvmExecutableLoc,
					map[str,value] keywordArguments, 
					bool debug, 
					bool debugRVM, 
					bool testsuite,
					bool profile,
					bool trace,
					bool coverage,
					bool jvm);