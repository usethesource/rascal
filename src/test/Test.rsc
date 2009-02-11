module Test

import IO;

data F = f(int N);

public void test(){

	switch(f(3)){
	//case f(1): println("case 1");
	//case f(2): {println("case 2");}
	case f(3): {println("case 3"); int X = 4; if(X == 3) return; else fail;}
	//case f(4):  println("case 4");
	}
	println("done");

}