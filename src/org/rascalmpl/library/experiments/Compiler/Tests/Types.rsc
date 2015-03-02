module experiments::Compiler::Tests::Types

import  experiments::Compiler::Compile;
import  experiments::Compiler::Execute;

import experiments::Compiler::muRascal2RVM::RascalReifiedTypes;
import experiments::Compiler::muRascal2RVM::B;

import ParseTree;
import Map;
import Set;

import IO;

layout LAYOUT = [\ \n\t]*; 
syntax D2 = "d";

data D1 = d1(int \int) | d2(str \str);


value run1(str \type) = execute("module TMP data D1 = d1(int \\int) | d2(str \\str); value main(list[value] args) = #<\type>;",[],recompile=true);
value run2() = execute("module TMP layout LAYOUT = [\\ \\n\\t]*; syntax D2 = \"d\"; value main(list[value] args) = #D2;",[],recompile=true);
value run3() = execute("module TMP import lang::rascal::\\syntax::Rascal; import ParseTree; value main(list[value] args) = #Statement;",[],recompile=true);
value run4() = execute("module TMP import experiments::Compiler::muRascal2RVM::B; import ParseTree; value main(list[value] args) = #A;",[],recompile=true);
value run5() = execute("module TMP import lang::rascal::\\syntax::Rascal; import ParseTree; value main(list[value] args) = #Module;",[],recompile=true);

test bool tst1() = run1("list[int]") == #list[int];
test bool tst2() = run1("lrel[int id]") == #lrel[int id];
test bool tst3() = run1("lrel[int \\int]") == #lrel[int \int];
test bool tst4() = run1("D1") == #D1;
test bool tst5() = run2() == #D2;
test bool tst6() { 
	type[value] v1 = typeCast(#type[value], run3());
	type[value] v2 = getStatementType();
	
	set[Production] alts1 = { a | /a:Production::prod(_,_,_) <- v1.definitions[sort("Statement")].alternatives };
	set[Production] alts2 = { a | /a:Production::prod(_,_,_) <- v2.definitions[sort("Statement")].alternatives };
	
	set[Production] common = alts1 & alts2;
	println(size(common));
	println("1: <alts1 - common>");
	println("2: <alts2 - common>");
	commonAll = v1.definitions & v2.definitions;
	println("Overall v2: <domain(v2.definitions - commonAll)>");
	println("Overall v1: <domain(v1.definitions - commonAll)>");
	bool match = true;
	for(Symbol s <- domain(v1.definitions)) {
		if(s in v2.definitions) {
			println("Match <s>");
			match = match && v1.definitions[s] == v2.definitions[s];
		} else {
			println("WARNING: a definition found in the compiler reifytype that is not in the interpreter one: <s>");
		}
	}
	return match;
}

test bool tst7() = run4() == getA();

test bool tst8() { 
	type[value] v1 = typeCast(#type[value], run5());
	type[value] v2 = getModuleType();
	
	alts1 = { a | /a:Production::prod(_,_,_) <- v1.definitions };
	alts2 = { a | /a:Production::prod(_,_,_) <- v2.definitions };
	
	set[Production] common = alts1 & alts2;
	println(size(common));
	println("1: <alts1 - common>");
	println("2: <alts2 - common>");
	commonAll = v1.definitions & v2.definitions;
	println("Overall v2: <domain(v2.definitions - commonAll)>");
	println("Overall v1: <domain(v1.definitions - commonAll)>");
	bool match = true;
	for(Symbol s <- domain(v1.definitions)) {
		if(s in v2.definitions) {
			println("Match <s>");
			match = match && v1.definitions[s] == v2.definitions[s];
		} else {
			println("WARNING: a definition found in the compiler reifytype that is not in the interpreter one: <s>");
		}
	}
	return match;
}
