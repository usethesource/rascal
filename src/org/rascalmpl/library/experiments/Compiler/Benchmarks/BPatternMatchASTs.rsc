module experiments::Compiler::Benchmarks::BPatternMatchASTs

import Message;
import lang::java::m3::AST;
import analysis::m3::TypeSymbol;
import ValueIO;

//private set[Declaration] getData() 
//	= readBinaryValueFile(#set[Declaration], |compressed+std:///experiments/Compiler/Benchmarks/pdb.values.bin.xz|);
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
//int countCCLikeMetric() {
//	dt = getData();
//	int result = 0;
//	for (i <- [0..100]) {
//		visit (dt) {
//			case \do(_,_) : result += 1;	
//			case \while(_,_) : result += 1;	
//			case \foreach(_,_,_): result += 1;
//			case \for(_,_,_): result += 1;
//			case \for(_,_,_,_): result += 1;
//			case \if(_,_): result += 1;
//			case \if(_,_,_): result += 1;
//			case \case(_): result += 1;
//			case \catch(_,_): result += 1;
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
//
//public value main(list[value] args) {
//  return [countReturnStatementsReducer(), countReturnStatementsVisit(), countCCLikeMetric(), getMethodsWhichThrow(), getVariableNamesTwice(), getVariableNamesOnce(), countZeroes()];
//}
