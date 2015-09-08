module experiments::Compiler::RVM::ExecuteProgram

import experiments::Compiler::RVM::AST;

import Type;

@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecuteProgram}
public java void linkProgram(
                    loc executable,
                    RVMProgram program,
                    map[str,map[str,str]] imported_module_tags,
                    map[str,Symbol] imported_types,
                    list[experiments::Compiler::RVM::AST::Declaration] imported_functions,
                    lrel[str name, Symbol funType, str scope, list[str] ofunctions,list[str] oconstructors] imported_overloaded_functions,
                    map[str,int] imported_overloading_resolvers, 
                    bool useJVM);    
                                            
@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecuteProgram}
@reflect{Uses execution context}
public java tuple[value,int] executeProgram(
                    loc executable,
					RVMProgram program,
					map[str,map[str,str]] imported_module_tags,
					map[str,Symbol] imported_types,
                    list[experiments::Compiler::RVM::AST::Declaration] imported_functions,
                    lrel[str name, Symbol funType, str scope, list[str] ofunctions,list[str] oconstructors] imported_overloaded_functions,
                    map[str,int] imported_overloading_resolvers, 
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
public java tuple[value,int] executeProgram(
                    loc program,
					list[value] arguments, 
					bool debug, 
					bool testsuite,
					bool profile,
					bool trackCalls,
					bool coverage,
					bool useJVM);