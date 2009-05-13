module test::Test

import IO;

bool p(int X){
	println("p(<X>)");
	return X > 1;
}

public bool test(){
   if(/int N := [1,2,3,2] && p(N))
   		return true;
   	else
   		return false;
}