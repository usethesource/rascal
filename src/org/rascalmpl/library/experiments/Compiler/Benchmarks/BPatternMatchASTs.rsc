module experiments::Compiler::Benchmarks::BPatternMatchASTs

import Message;
import lang::java::m3::AST;
import analysis::m3::TypeSymbol;
import ValueIO;
import util::Benchmark;
import IO;
import util::Math;



private set[Declaration] getData() 
	= readBinaryValueFile(#set[Declaration], |compressed+std:///experiments/Compiler/Benchmarks/pdb.values.bin.xz|);
	

int countReturnStatementsReducer(int n) {
	dt = getData();
	int result = 0;
	for (i <- [0..n]) {
		result += ( 0 | it + 1 | /\return() := dt);
        result += ( 0 | it + 1 | /\return(_) := dt);
	}
	return result;
}

int countReturnStatementsVisit(int n) {
	dt = getData();
	int result = 0;
	for (i <- [0..n]) {
		visit (dt) {
			case \return() : result += 1;	
			case \return(_) : result += 1;	
		}
	}
	return result;
}
int countCCLikeMetric(int n) {
	dt = getData();
	int result = 0;
	for (i <- [0..n]) {
		visit (dt) {
			case \do(_,_) : result += 1;	
			case \while(_,_) : result += 1;	
			case \foreach(_,_,_): result += 1;
			case \for(_,_,_): result += 1;
			case \for(_,_,_,_): result += 1;
			case \if(_,_): result += 1;
			case \if(_,_,_): result += 1;
			case \case(_): result += 1;
			case \catch(_,_): result += 1;
		}
	}
	return result;
}


set[str] getMethodsWhichThrow(int n) {
	dt = getData();
	set[str] result = {};
	for (i <- [0..n]) {
		result = { mn | /method(_, mn, _, _, /\throw(_)) := dt};	
	}
	return result;
}

list[str] getVariableNamesTwice(int n) {
	dt = getData();
	list[str] result = [];
	for (i <- [0..n]) {
		result = [ nm | /variable(nm, _) := dt]
			+ [ nm | /variable(nm, _, _) := dt]
			;
	}
	return result;
}

list[str] getVariableNamesOnce(int n) {
	dt = getData();
	list[str] result = [];
	for (i <- [0..n]) {
		result = [ e.name | /Expression e := dt, e is variable];
	}
	return result;
}

int countZeroes(int n) {
	dt = getData();
	int result = 0;
	for (i <- [0..n]) {
		result += (0 | it + 1 | /number("0") := dt);	
	}
	return result;
}

map[str name,  value(int n) job] jobs = (
"countReturnStatementsReducer":	countReturnStatementsReducer,
"countReturnStatementsVisit":	countReturnStatementsVisit,
"countCCLikeMetric": 			countCCLikeMetric,
"getMethodsWhichThrow":			getMethodsWhichThrow,
"getVariableNamesTwice":		getVariableNamesTwice,
"getVariableNamesOnce":			getVariableNamesOnce,
"countZeroes": countZeroes
);

int main(list[value] args){
	total = 0;
	for(jb <- jobs){
		  t1 = cpuTime();
		  n = jobs[jb](10);
		  t2 = cpuTime();
		  duration = (t2 - t1)/1000000;
		  total += duration;
		  println("<jb>: time <duration> ms, output: <n>");
	}
	println("Total time: <round(total/1000.0, 0.1)> sec");
	return total;
}

//public value main(list[value] args) {
//  return [countReturnStatementsReducer(), countReturnStatementsVisit(), countCCLikeMetric(), getMethodsWhichThrow(), getVariableNamesTwice(), getVariableNamesOnce(), countZeroes()];
//}
