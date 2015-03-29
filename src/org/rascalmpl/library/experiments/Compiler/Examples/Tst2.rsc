
module experiments::Compiler::Examples::Tst2
import Exception;
import List;
import Set;
import Map;
import IO;
import util::Math;
  
data NODEA = fA(int N);
  
data NODEB = fB(int N) | dB(NODEB a, NODEB b);
  
data NODEC = fC(int N) | fin(value V) | dC(NODEC a) | dC(NODEC a, NODEC b);
  
data Exception = divide_by_zero();
  			
value dfin(value v){
	value res = 0;
	try { 
		throw v; 
	} 
	catch int x: { 
		res = x + x; 
	} 
	//catch NODEC x: { 
	//	res = dC(x,x); 
	//} 
	//catch str s: { 
 //		res = s + s; 
	//} 
	//catch: { 
	//	res = v; 
	//} 
	finally { 
		return fin(res); 
	} 
}