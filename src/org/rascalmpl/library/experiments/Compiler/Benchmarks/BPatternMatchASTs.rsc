module experiments::Compiler::Benchmarks::BPatternMatchASTs

import lang::java::m3::AST;
import analysis::m3::TypeSymbol;
import Message;
import ValueIO;

//private set[Declaration] getData() 
//	= readBinaryValueFile(#set[Declaration], |rascal:///experiments/Compiler/Benchmarks/pdb.values.bin|);
//	
//
//int countReturnStatementsReducer() {
//	dt = getData();
//	int result = 0;
//	for (i <- [0..100]) {
//		result += ( 0 | it + 1 | /\return() := dt);
//        result += ( 0 | it + 1 | /\return(_) := dt);
//	}
//	return result;
//}
//
//int countReturnStatementsVisit() {
//	dt = getData();
//	int result = 0;
//	for (i <- [0..100]) {
//		visit (dt) {
//			case \return() : result += 1;	
//			case \return(_) : result += 1;	
//		}
//	}
//	return result;
//}
//
//
//set[str] getMethodsWhichThrow() {
//	dt = getData();
//	set[str] result = {};
//	for (i <- [0..100]) {
//		result = { mn | /method(_, mn, _, _, /\throw(_)) := dt};	
//	}
//	return result;
//}
//
//list[str] getVariableNamesTwice() {
//	dt = getData();
//	list[str] result = [];
//	for (i <- [0..100]) {
//		result = [ nm | /variable(nm, _) := dt]
//			+ [ nm | /variable(nm, _, _) := dt]
//			;
//	}
//	return result;
//}
//
//list[str] getVariableNamesOnce() {
//	dt = getData();
//	list[str] result = [];
//	for (i <- [0..100]) {
//		result = [ e.name | /Expression e := dt, e is variable];
//	}
//	return result;
//}
//
//int countZeroes() {
//	dt = getData();
//	int result = 0;
//	for (i <- [0..300]) {
//		result += (0 | it + 1 | /number("0") := dt);	
//	}
//	return result;
//}

//public value main(list[value] args) {
//  return [countReturnStatementsReducer()/*, countReturnStatementsVisit(), getMethodsWhichThrow(), getVariableNamesTwice(), getVariableNamesOnce(), countZeroes()*/];
//}
