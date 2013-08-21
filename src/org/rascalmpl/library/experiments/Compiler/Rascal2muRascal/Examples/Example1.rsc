module experiments::Compiler::Rascal2muRascal::Examples::Example1

//import util::Benchmark;
//import IO;


//int work (int n){
//
//   while(n > 0){
//         res = 0;
//         for([*int p, *int x, *int y, *int z] := [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]){ res = res + 1; }
//         n = n - 1;
//   }
//   return 0;
//}

public data DATA = d1(int i) | d2(str s);

value main(list[value] args) { 
//   t1 = getMilliTime();
 //  work(10000);
//   t2 = getMilliTime();
//   println("rascal interpreter [<t2 - t1> msec]");
   return  str n(int x) := d1(2);
  
}