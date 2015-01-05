module experiments::Compiler::RVM::Tests

import experiments::Compiler::RVM::AST;
//import experiments::Compiler::RVM::Implode;
import experiments::Compiler::RVM::Run;

import IO;

public loc exmpl1 = |project://RascalStandardLibrary/src/experiments/Compiler/RVM/programs/Example1.rvm|;

// factorial
public loc exmpl2 = |project://RascalStandardLibrary/src/experiments/Compiler/RVM/programs/Example2.rvm|;

// factorial with a tail call optimization
public loc exmpl3 = |project://RascalStandardLibrary/src/experiments/Compiler/RVM/programs/Example3.rvm|;

// tail recursive, two-parameter version of factorial
public loc exmpl4 = |project://RascalStandardLibrary/src/experiments/Compiler/RVM/programs/Example4.rvm|;

public loc exmpl5 = |project://RascalStandardLibrary/src/experiments/Compiler/RVM/programs/Example5.rvm|;

//public void testit() {
//	RascalVM code = parse(readFile(exmpl5));
//	println("parsed: <code>");
//	tuple[value,int] r = executeProgram(code.directives);
//	println("it results in: <r[0]> and took: <r[1]>");
//}
