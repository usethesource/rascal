module experiments::Compiler::RVM::Run

import experiments::Compiler::RVM::AST;
import experiments::Compiler::RVM::Syntax;
import experiments::Compiler::RVM::Load;

import ParseTree;
//import util::IDE;

//import IO;

@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Execute}
@reflect{Executes RVM programs}
public java tuple[value,int] executeProgram(loc executable,
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
										    bool useJVM);	
										    
										    
@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Execute}
@reflect{Executes RVM programs}
public java tuple[value,int] executeProgram(loc program,
										    list[value] arguments, 
										    bool debug, 
										    bool testsuite,
										    bool profile,
										    bool trackCalls,
										    bool coverage,
										    bool useJVM
										   );						    
										    

//public void execute(experiments::Compiler::RVM::Syntax::RascalVM tree, loc selection) {
//	ast = implode(#experiments::Compiler::RVM::AST::RascalVM, tree);
//	out = executeProgram(ast.directives);
//	println(out);	
//}
//
//set[Contribution] contributions = 
//	{ menu(menu("muRascalVM", [ action("Run", execute) ])) };
//
//@doc{Registers the muRascalVM language, .rvm}
//public void registerLanguage() {
//	registerLanguage("muRascalVM", "rvm", experiments::Compiler::RVM::Syntax::RascalVM (str src, loc l) { return parseRVM(src, l); });
//	registerContributions("muRascalVM", contributions);
//}
