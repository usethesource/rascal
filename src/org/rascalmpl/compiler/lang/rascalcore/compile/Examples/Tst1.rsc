module lang::rascalcore::compile::Examples::Tst1
   
test bool localTypeInferenceNoEscape() {
    { x = 1; x == 1; }
    x = "1"; return x == "1";
}


//list[int] sort4([*int nums1, int p, *int nums2, int q, *int nums3]) {
//  if (p > q) 
//    return sort4([*nums1, q, *nums2, p, *nums3]); 
//  else 
//    fail sort4;
//}
//
//default list[int] sort4(list[int] x) = x;
//
//value main(){
//    return sort4([2, 1, 5, 7]);
//}

//value main(){
//    if(2 > 3 ){  
//        x = 0; 
//    } else { 
//        x = "abc";
//    }
//    return true;
//}

//
//@javaClass{org.rascalmpl.library.Prelude}
//public java int size(list[&T] lst);
//
//@javaClass{JavaFunction.FakeLib}
//public java int inc(int n, int delta1 = 1, int delta2 = 10);
//
//value main()  { x = 1; return <size([x,2,x]), inc(42,delta1=2)>; }

//int f(int n){ 
//    int x = 0;
//    int g(int m){ x += 1; return m * x; }
//    
//    return g(n);
//}
//
//value main() = f(7);       

//int() f(){
//    int x = 1;
//    int g(){ x += 1; return x; }
//    
//    return g;
//}
////data D = d(int() generator);
//
//value main(){
//    x = [f()];
//    return x[0]();
//}


//int double(int n, int delta = 1) = 2 * n + delta;
//
//int triple(int n, int delta = 1) = 3 * n + delta;
//
//value main() {
//    c = (double o triple);
//    return c(5);
//}
//
//str f() {
//    int n = 8;
//    return "  <if(n > 0){>
//           '  aaa<n>bbb
//           '  <while(n <= 10){> 
//           '     ccc<n += 1;}><n += 1;} else {n -= 10;>
//           '  <n> <}>";
//}
//
//str main() = f();

//value f() {
//   return /int n := [1,2,3, "a"];
//    }

//value f(value v){
//    ones = 0;
//    twos = 0;
//    ints = 0;
//    defs = 0;
//    
//    visit(v){
//    
//    case 1: ones += 1;
//    case 2:  twos += 1;
//    case int x: ints += 1;
//    default:
//        defs +=1;
//    }
//    return <ones, twos, ints, defs>;
//}
//
//value main() = f([1, 2, 4, "a"]);

//int inc(int n, int delta = 1) = n + delta;
//
//value main() = inc(42, delta=3);

//data D(str s = "a") = d1(int n, int m = n+1, bool b = false)
//       | d2(bool b = true)
//       ;
//
//value fun(){
//   x = d1(10);
//   //a = x.n;
//   //b = x.m;
//   //c = x.b;
//   d = x.s;
//   return d;
//}

//value main() { int z = 1; return int z := 1; }  

//int fac(0) = 0;
//int fac(1) = 1;
//default int fac(int n) = n * fac(n - 1);

//int f("abc"(int n)) = n;   
//value main() { b = 1 == 2; if(b) 10; else 20; }

//value main() { x = [1,2..10]; return x; }

//value main() = [1, x*, *int y, 5] := [1, 2,3,4,5];

//value main() = [[ 2*x | int x <- [1,2,3,4,5]],  [3*x | int x <- [1,2,3,4,5]]];

//value main() = [ 2*x | int x <- [5..1]];

//value main() { z = ( x : 2*x | int x <- [1,2,3,4,5], x > 3); return z; }

// value main() { x = 1; y = [x,2,3]; return x; }

//value main(){
//    if(true){
//        int x = 1;
//    } else {
//        int y = "abc";
//    }
//    return true;
//}