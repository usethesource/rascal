module experiments::Compiler::Examples::ProfileExample

import experiments::Compiler::Profile;   
import IO; 
import Message;
import util::ResourceMarkers; 

void addMarkers(lrel[loc src, int ticks] profData){
	// TODO: add multiple ticks per line
	addMessageMarkers({ info("<tup.ticks> ticks", tup.src) | tup <- profData });
}

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
                                                             
public value main(list[value] args) {
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
  addMarkers(prof);
  return true;
}