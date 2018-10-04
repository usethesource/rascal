module lang::rascalcore::compile::Examples::ProfileExample

import lang::rascalcore::compile::Profile;   
import IO; 
import Message;

int fac(int n) = (n <= 1) ? 1 : n * fac(n-1);
 
void f(){
	for(int j <- [0..10]){
		x = 13;
		for(int i <- [0 .. 10]){
			x = x * x;
		}
	}
}  

void g(){
	x = "a";
	for(int i <- [0 .. 1000]){
		y = x + x;
	}
} 
                                                             
public value main() {
println("Start of program");

  fac(24);
  startProfile();
  f();
  g();
  f();
  stopProfile();
  
  fac(24);
  
  prof = getProfile();
  reportProfile();
  reportProfile(prof);
  fac(24);
  return true;
}