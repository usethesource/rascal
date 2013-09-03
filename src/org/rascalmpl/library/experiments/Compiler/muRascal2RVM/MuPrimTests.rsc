module experiments::Compiler::muRascal2RVM::MuPrimTests

import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Implode;

import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal2RVM::mu2rvm;
import  experiments::Compiler::RVM::Run;
 
 
import Ambiguity;

import Prelude;
import ParseTree;
import IO;

public loc Library = |std:///experiments/Compiler/muRascal2RVM/Test.mu|;

value ret(str s) {
	<res, tm> = executeProgram(mu2rvm(parse("module TEST function main[1,arg,x,y] { return <s>; }")), true, 1); 
	return res;
}

value body(str s) {
	<res, tm> = executeProgram(mu2rvm(parse("module TEST function main[1,arg,x,y] { <s> }")), true, 1);
	return res; 
}

value prim1(str fun, value lhs) {
	<res, tm> = executeProgram(mu2rvm(parse("module TEST function main[1,arg,x,y] { return muprim(\"<fun>\",<lhs>); }")), true, 1);
	return res; 
}

value prim2(str fun, value lhs, value rhs) {
	<res, tm> = executeProgram(mu2rvm(parse("module TEST function main[1,arg,x,y] { return muprim(\"<fun>\",<lhs>,<rhs>); }")), true, 1);
	return res; 
}
value prim3(str fun, value arg1, value arg2, value arg3) {
	<res, tm> = executeProgram(mu2rvm(parse("module TEST function main[1,arg,x,y] { return muprim(\"<fun>\",<arg1>,<arg2>,<arg3>); }")), true, 1);
	return res; 
}


test bool tst() = true :=  ret("true") ;
test bool tst() = false :=  ret("false") ;
test bool tst() = "abc" :=  ret("\"abc\"") ;
test bool tst() = 42 :=  ret("42") ;
test bool tst() = [] :=  ret("[]") ;
test bool tst() = [7] :=  ret("[7]") ;
test bool tst() = [7, 8] :=  ret("[7, 8]") ;
test bool tst() = [7, 8, 9] :=  ret("[7, 8, 9]") ;

test bool tst() = [7, 8] :=  ret("[7, 8]") ;
test bool tst() = 7 :=  ret("get [7, 8][0]") ;


test bool tst() = 7 := body("x = 7; return x;");
test bool tstx() = 7 := body("x = 7; y = 8; return x;");
test bool tstx() = 8 := body("x = 7; y = 8; return y;");

test bool tstx() = [7, 8] := body("x = 7; y = 8; return [x, y];");
test bool tst() = 7 := body("[x, y] = [7, 8]; return x;");
test bool tst() = 8 := body("[x, y] = [7, 8]; return y;");


test bool tst() = true := prim2("and_bool_bool", true, true);
test bool tst() = false := prim2("and_bool_bool", false, true);
test bool tst() = 15 := prim2("addition_num_num", 7, 8);

//assign_pair,			// Used by muRascal implode


test bool tst() = true := prim2("equas_num_num", 7, 7);
test bool tst() = false := prim2("equal_num_num", 7, 8);
test bool tst() = true := prim2("equal_str_str", "\"abc\"", "\"abc\"");

test bool tst() = true := prim2("greater_num_num", 8, 7);
test bool tst() = false := prim2("greater_num_num", 7, 8);

test bool tst() = true := prim2("greater_equal_num_num", 8, 7);
test bool tst() = false := prim2("greater_equal_num_num", 7, 8);

test bool tst() = true := prim2("less_num_num", 7, 8);
test bool tst() = false := prim2("less_num_num", 8, 7);

test bool tst() = true := prim2("less_equal_num_num", 7, 8);
test bool tst() = false := prim2("less_equal_num_num", 8, 7);

//make_list,
test bool tst() = [1, 2] := prim2("make_array", 1, 2);

//make_map,
//make_set,
//make_tuple,
//negative,

test bool tst() = 0 := prim1("size_array", []);
test bool tst() = 3 := prim1("size_array", [1,2,3]);
test bool tst() = [2, 3] := prim3("sublist", [1,2,3,4], 1, 2);

test bool tst() = 7 := prim2("subtraction_num_num", 12, 5);

test bool tst() = 1 := prim2("subscript_array_int", [1,2,3], 0);
test bool tst() = 2 := prim2("subscript_array_int", [1,2,3], 1);
test bool tst() = 3 := prim2("subscript_array_int", [1,2,3], 2);


void main(){
    MuModule m = parse(Library);
    
	println("parsed: <m>");
	
	rvmP = mu2rvm(m);
	//iprintln(rvmP);
	<res, tm> = executeProgram(rvmP, true, 1);
	println("result: <res> [<tm> msec]");
}
