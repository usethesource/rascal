module experiments::Compiler::Examples::Tst1

import experiments::Compiler::Profile; 
 
void f(){
	x = 13;
	for(int i <- [0 .. 100]){
		x = x * x;
	}
}  

void g(){
	x = "a";
	for(int i <- [0 .. 100]){
		y = x + x;
	}
} 
                                                             
public value main(list[value] args) {
  f();
  g();
  f();
  g();
}