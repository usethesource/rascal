module experiments::vis2::Tst2

import IO;

import lang::json::IO;
import experiments::vis2::Figure;
import Type;

void main(){

	r1 =  text("Welcome to the Awesome Rascal REPL");
	jj = toJSON(r1);
	
	Figure r2 = fromJSON(#Figure, jj);
	
	println("r1 = <r1>, <typeOf(r1)>");
	println("r2 = <r2>, <typeOf(r2)>");
	println("jj = <jj>");
	println("r1 == r2: <r1 == r2>");
	println("text(_) := r1 ==\> <text(_) := r1>");
	println("text(_) := r2 ==\> <text(_) := r2>");
	
}



//data D = d(int n, str opt = "abc");
//
//void main(){
//
//	r1 = d(3);
//	j = toJSON(r1);
//	r2 = fromJSON(#D, j);
//	
//	println("r1 = <r1>");
//	println("r2 = <r2>");
//	println("r1 == r2: <r1 == r2>");
//	println("j  = <j>");
//}
 