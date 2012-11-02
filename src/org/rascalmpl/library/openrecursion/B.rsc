module openrecursion::B

import openrecursion::A;
import IO;

public test bool test1() {
	str input = "Open recursion test";
	str output = "._._._._._._._._._._._._._._._._._._._";
	str printResult = "";
	(MyData (MyData d) { printResult = printResult + "._"; return d; } o mydata)(input);
	return printResult == output;
}

public test bool test2() {
	list[int] input = [0,1,2,3,4,5,6,7,8];
	str output1 = "01101101121011210131011210131011251011210131011251011210138101121013101125101121013810112101310112513101121013101125101121013810112101310112513101121013101125101121013821";
	str output2 = "111212612624126241201262412072012624120720504012624120720504040320";
	str printResult1 = "";
	for(int i <- input)
		(int (int n) { printResult1 = printResult1 + "<n>"; return n; } o fib)(i);
	str printResult2 = "";
	for(int i <- input)
		( int (int n) { printResult2 = printResult2 + "<n>"; return n; } o int (int n) { 
					switch(n) { 
						case 0: return 0; 
						case 1: return 1; 
						default: return it(n-1) + it(n-2);
					} 
				})(i);
	str printResult3 = "";
	for(int i <- input)
		( int (int n) { printResult3 = printResult3 + "<n>"; return n; } o fact )(i);
	str printResult4 = "";
	for(int i <- input)
		( int (int n) { printResult4 = printResult4 + "<n>"; return n; } o int (int n) { 
					switch(n) { 
						case 0: return 1; 
						case 1: return 1; 
						default: return n*it(n-1); 
					} 
				})(i);
	return (printResult1 == output1) && (printResult2 == output1) && (printResult3 == output2) && (printResult4 == output2);
}

public test bool test3() {
	list[int] input = [0,1,2,3,4,5,6,7,8];
	str output1 = "01123581321";
	str output2 = "112624120720504040320";
	str printResult1 = "";
	for(int i <- input)
		(int (int n) { printResult1 = printResult1 + "<n>"; return n; } o. fib)(i);
	str printResult2 = "";
	for(int i <- input)
		( int (int n) { printResult2 = printResult2 + "<n>"; return n; } o. int (int n) { 
					switch(n) { 
						case 0: return 0; 
						case 1: return 1; 
						default: return it(n-1) + it(n-2);
					} 
				})(i);
	str printResult3 = "";
	for(int i <- input)
		( int (int n) { printResult3 = printResult3 + "<n>"; return n; } o. fact )(i);
	str printResult4 = "";
	for(int i <- input)
		( int (int n) { printResult4 = printResult4 + "<n>"; return n; } o. int (int n) { 
					switch(n) { 
						case 0: return 1; 
						case 1: return 1; 
						default: return n*it(n-1); 
					} 
				})(i);
	return (printResult1 == output1) && (printResult2 == output1) && (printResult3 == output2) && (printResult4 == output2);
}

public test bool test4() {
	list[int] input = [0,1,2,3,4,5,6,7,8,9];
	list[int] output = [ fib(i) | int i <- input ];
	list[int] result = [ 
		int (int n) { 
					switch(n) { 
						case 0: return 0; 
						case 1: return 1; 
						default: return it(n-1) + it(n-2);
					} 
				}(i) |
			int i <- input ];
	
	return output == result;
}
