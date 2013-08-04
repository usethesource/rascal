module experiments::CoreRascal::muRascalVM::Tests

import experiments::CoreRascal::muRascalVM::AST;
import experiments::CoreRascal::muRascalVM::Implode;
import experiments::CoreRascal::muRascalVM::Run;

import IO;

public loc exmpl1 = |project://RascalStandardLibrary/src/experiments/CoreRascal/muRascalVM/programs/Example1.rvm|;

// factorial
public loc exmpl2 = |project://RascalStandardLibrary/src/experiments/CoreRascal/muRascalVM/programs/Example2.rvm|;

// factorial with a tail call optimization
public loc exmpl3 = |project://RascalStandardLibrary/src/experiments/CoreRascal/muRascalVM/programs/Example3.rvm|;

// tail recursive, two-parameter version of factorial
public loc exmpl4 = |project://RascalStandardLibrary/src/experiments/CoreRascal/muRascalVM/programs/Example4.rvm|;

public void testit() {
	RascalVM code = parse(readFile(exmpl4));
	println("parsed: <code>");
	tuple[value,int] r = executeProgram(code.directives, 1000);
	println("it results in: <r[0]> and took: <r[1]>");
}
