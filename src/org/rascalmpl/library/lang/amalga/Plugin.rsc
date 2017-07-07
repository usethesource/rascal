module lang::amalga::Plugin

import lang::amalga::Syntax;
import lang::amalga::executionLang::Syntax;
import lang::amalga::Parser;
import util::IDE;
import ParseTree;
import IO;

// scratch
// data JupyterContribution = executor(str name, str (str code) executor);

//anno str PrimId@category;

private str LANGUAGE_NAME="Amalga";
private str LANGUAGE_EXTENSION="amalga";

void main() {
  registerLanguage(LANGUAGE_NAME, LANGUAGE_EXTENSION, parseAlg);
  //registerAnnotator(LANGUAGE_NAME, builtinAnnotator);
  registerContributions(LANGUAGE_NAME, LANGUAGE_CONTRIBS);
}


//public Tree builtinAnnotator(Tree x){
//// TODO: change this list of keywords for the list of Keywords declared in the amalga syntax file
//    key = {"function", "zeros", "ones", "identity", "return", "loadImage" ,"randomFloat","randomUint","randomInt", "size", "stack", "floor", "identity", "transpose"};
//    x = visit(x) {
//    		case PrimId command => { 
//    				if("<command>"in key) command[@category="MetaKeyword"];else command;
//    		}
//    }
//    return x;
//}

start[Program] parseAlg(str input, loc src) {
   return parse(#start[Program], input, src, allowAmbiguity=true);
}

void gen(loc src, loc out) {
	p = parse(#start[Program], src);
	r = eval2(p.top);
	appendToFile(out,r);
}

void gen2(Tree t, loc s) {
	
	p = parse(#start[Program], |project://amalga/src/lang/matrix/examples/blur2.amalga|);
	r = eval2(p.top);
	appendToFile(|project://amalga/src/lang/matrix/results/test1.cpp|,r);
	//createProcess("java -version");
	//createProcess("g++ ../results/tes1.cpp -g  -I ../include -I ../../../tools -L ../bin -lHalide `libpng-config --cflags --ldflags` -lpthread -ldl -o t1 -std=c++11");
}

void test1(Tree t, loc s){
	ans = evalProgram(t.top);
	appendToFile(|project://amalga/src/lang/matrix/results/outputTest.txt|, ans);
}

void test2() {
	p = parse(#start[Program], |project://amalga/src/lang/amalga/examples/Experiments/experiment3.amalga|);
	r = evalProgram(p.top);
	appendToFile(|project://amalga/src/lang/amalga/results/result2.cpp|,r);
}

void test3(){
	p = parse(#start[Program], |project://amalga/src/lang/amalga/examples/Experiments/experiment4.amalga|).top;
	r = evalProgram(p);
	appendToFile(|project://amalga/src/lang/amalga/results/result3.txt|,r);
}

Program amalgaR(str input){
	program = parse(#start[Program],input);
	r = evalProgram(program.top);
	writeFile(|file:///Users/Mauricio/Downloads/halide/tutorial/example.cpp|,r);
	return program.top;
}

//public set[Contribution] LANGUAGE_CONTRIBS = {
//	popup(
//		menu("Amalga",[
//		    action("Generate Halide Code", gen2),
//		    action("Test", test1)
//	    ])
//  	)
//};