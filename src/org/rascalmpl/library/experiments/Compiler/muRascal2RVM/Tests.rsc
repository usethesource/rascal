module experiments::Compiler::muRascal2RVM::Tests

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
	<res, tm> = executeProgram(mu2rvm(parse("module TEST function main[1,1,x:1,y:2] { return <s>; }")), true, 1); 
	return res;
}

value body(str s) {
	<res, tm> = executeProgram(mu2rvm(parse("module TEST function main[1,1,x:1,y:2] { <s> }")), true, 1);
	return res; 
}

value prim1(str fun, value lhs) {
	<res, tm> = executeProgram(mu2rvm(parse("module TEST function main[1,1,x:1,y:2] { return prim(\"<fun>\",<lhs>); }")), true, 1);
	return res; 
}

value prim2(str fun, value lhs, value rhs) {
	<res, tm> = executeProgram(mu2rvm(parse("module TEST function main[1,1,x:1,y:2] { return prim(\"<fun>\",<lhs>,<rhs>); }")), true, 1);
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

test bool tst() = <7, 8> :=  ret("\<7, 8\>") ;



test bool tst() = 7 := body("x = 7; return x;");

test bool tstx() = <7, 8> := body("x = 7; y = 8; return \<x, y\>;");
test bool tst() = 7 := body("\<x, y\> = \<7, 8\> return x;");
test bool tst() = 8 := body("\<x, y\> = \<7, 8\> return y;");


test bool tst() = true := prim2("and_bool_bool", true, true);
test bool tst() = false := prim2("and_bool_bool", false, true);

//appendAfter,
//addition_elm_list,
//addition_list_elm,
//addition_list_list,
//addition_map_map,
//addition_elm_set,
//addition_set_elm,
//addition_set_set,
	
test bool tst() = 15 := prim2("addition_num_num", 7, 8);
test bool tst() = "abcdef" := prim2("addition_str_str", "\"abc\"", "\"def\"");
	
//addition_tuple_tuple,
//assign_pair,			// Used by muRascal implode
//composition_lrel_lrel,
//composition_rel_rel,
//composition_map_map,
//division_num_num,
//equals_num_num,
test bool tst() = true := prim2("equals_num_num", 7, 7);
test bool tst() = false := prim2("equals_num_num", 7, 8);
//equivalent_bool_bool,

test bool tst() = true := prim2("greater_num_num", 8, 7);
test bool tst() = false := prim2("greater_num_num", 7, 8);

test bool tst() = true := prim2("greater_equal_num_num", 8, 7);
test bool tst() = false := prim2("greater_equal_num_num", 7, 8);

test bool tst() = 7 := prim1("head_list", [7,8]);

//implies_bool_bool,

test bool tst() = true := prim2("less_num_num", 7, 8);
test bool tst() = false := prim2("less_num_num", 8, 7);

test bool tst() = true := prim2("less_equal_num_num", 7, 8);
test bool tst() = false := prim2("less_equal_num_num", 8, 7);

//make_list,
//make_map,
//make_set,
//make_tuple,
//negative,
//not_bool,
//or_bool_bool,
//println,
//product_num_num,
//size_list,
//subtraction_list_list,
//subtraction_map_map,
//subtraction_num_num,
//subtraction_set_set,
//subscript_list_int, 
//subscript_map,
//tail_list,

test bool tst() = [] := prim1("tail_list", [7]);

//transitive_closure_lrel,
//transitive_closure_rel,
//transitive_reflexive_closure_lrel,
//transitive_reflexive_closure_rel;

void main(){
    MuModule m = parse(Library);
    
	println("parsed: <m>");
	
	rvmP = mu2rvm(m);
	iprintln(rvmP);
	<res, tm> = executeProgram(rvmP, true, 1);
	println("result: <res> [<tm> msec]");
}