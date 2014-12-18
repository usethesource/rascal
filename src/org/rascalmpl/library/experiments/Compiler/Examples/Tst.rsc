module experiments::Compiler::Examples::Tst


import experiments::Compiler::Coverage;   
import util::ResourceMarkers; 
import Message;

import IO;   

void addMarkers(set[loc] covered){
	addMessageMarkers({ info("Executed", src) | src <- covered });
}

int f(int n) = n + 1;

int g(int n) = n * 2;

value main(list[value] args){
	x = 10;
	y = x + 20;
	startCoverage();
	
	if(1 > 2) f(1); else f(2);
	
	y = y + 20;
	if(1 > 2) {
		f(3);	
		f(4);
	} else {
		f(5);
		for(int n <- [0..10]){
			f(6);
		}
	}	
		
	stopCoverage();
	
	if(1 > 2) g(6); else g(7);
	
	startCoverage();
	
	if(1 > 2) f(8); else f(9);
	
	stopCoverage();
	cov = getCoverage();
	addMarkers(cov);
	return true;
}