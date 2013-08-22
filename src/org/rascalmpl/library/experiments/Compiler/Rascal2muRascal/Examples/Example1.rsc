module experiments::Compiler::Rascal2muRascal::Examples::Example1

//import util::Benchmark;
import List;

//void work(int n){
//
//  while(n > 0){
//    res = 0;
// 	for([*int a, *int b, *int c, *int d] := [1,2,3,4,5,6,7,8,9]) { res = res + 1; }
// 	n = n - 1;
//  }
//  return;
//
//}

data D = d1(int n) | d2(list[int] ns);

value main(list[value] args) { 
//    t1 = getMilliTime();
//	work(10000);
//	t2 = getMilliTime();
//	println("rascal interpreter [<t2 - t1> msec]");
// 	return 0;
 	
 	z = (/str x := d2([1,2,3])) ? x : 0;
 	return z;
}