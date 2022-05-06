module lang::rascalcore::compile::Examples::Tst0

value main() { //test bool returnOfAnInstantiatedGenericFunction() {
    &S(&U) curry(&S(&T, &U) f, &T t) = &S (&U u) { 
      return f(t, u); 
    };

    int add(int i, int j) = i + j;

    // issue #1467 would not allow this assignment because the 
    // returned closure was not instantiated properly from `&S (&U)` to `int(int)`
    int (int) f = curry(add, 1); 

    return f(1) == 2 && (f o f)(1) == 3;
}

//start syntax XorY = x : "x" | y : "y";
//
//lexical Layout = [.;];
//layout L = Layout* !>> [.;];
//
//value main() //test bool concreteReplaceInLayout() 
//  = visit([start[XorY]] ".x;") {
//    case (Layout)`.` => (Layout)`;`
//  } == [start[XorY]] ";x;";
  
//layout Whitespace = [\ \t\n]*;
//
//start syntax E = "e";
//start syntax ES = {E ","}+ args;
//
//value main(){ //test bool sortES2() 
//   pt = ([ES] "e,e,e");
//   return  {E ","}+ _ := pt.args;
//}

//value main() { //test bool compositeOrCntBTLast() {
//    n = 0;
//    if( 1==1 || 2==2 )  {
//        n = n + 1;
//        fail;
//    }
//    return n;// == 5;
//}   

//import ParseTree;
//layout Whitespace = [\ ]* !>> [\ ];
//lexical IntegerLiteral = [0-9]+; 
//lexical Identifier = [a-z]+;
//
//syntax Exp 
//  = IntegerLiteral  
//  | Identifier        
//  | bracket "(" Exp ")"     
//  > left Exp "*" Exp        
//  > left Exp "+" Exp  
//  | Exp "==" Exp      
//  ;
//
//syntax Stat 
//   = Identifier ":=" Exp
//   | "if" Exp "then" {Stat ";"}* "else" {Stat ";"}* "fi"
//   ;
//
//
//value main() //test bool concreteMatch250() 
//    = [ n | /char(int n) := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ] 
//                               == [105,102,32,120,32,116,104,101,110,32,97,32,58,61,32,49,59,98,58,61,50,32,101,108,115,101,32,99,58,61,51,32,102,105];


//start syntax E = "e";
//start syntax ES = {E ","}+ args;
//
//@javaClass{org.rascalmpl.library.Prelude}
//public java int size(list[&T] lst);
//int cntES({E ","}+ es) = size([e | e <- es ]);
//
////value main() //test bool cntES1() 
////    = ((ES) `e`).args;
//
//value main() //test bool cntES1() 
//    = cntES(((ES) `e`).args) == 1;


//import String;

//@javaClass{org.rascalmpl.library.Prelude}
//public java int size(list[&T] lst);
//
//@javaClass{org.rascalmpl.library.Type}
//public java bool eq(value x, value y);
//
//bool isEqual(list[&T] A, list[&T] B) { 
//    if(size(A) == size(B)){
//        for(int i <-[0 .. size(A)]){
//            if(!eq(A[i],B[i])) 
//                return false;
//        }
//        return true;
//    }
//    return false;
//}   
//
//test bool notEqual2(list[&T] A, list[&T] B) = (A != B) ? !isEqual(A,B) : isEqual(A,B);

//value main() = /[A-Z]/ !:= "abc";

//value main() = [1] !:= [1,2,3];

//value main() = tstToLowerCase("ABC");
//test bool noMatchImpliesUnequal(int a, int b) = (a !:= b) ==> a != b;
//
//value main() 
//test bool escapedPatternName1b() 
//    = x := 3 && x == 3;

//value main() //test bool nodeSetMatch() 
//    = { "a"(1) } := { "a"(1) };

//public int lub(int lf, int lt)
//    = lt when 123 !:= lf &&  456 !:= lt;

//bool f1(){
// return any(x <- {1,2,3}, x > 2);
//}

//public bool isSorted(list[&T] l)
// = !any([*_] := l, 1 < 2);
// 
//bool isSorted()
// = all(int X <- [1 .. 3], X >= 1);
// 
//bool notIsSorted()
// = !all(int X <- [1 .. 3], X >= 1);
//
//
//bool f()
// = any(int X <- [1 .. 3], X >= 1);
// 
//bool notf()
// = !any(int X <- [1 .. 3], X >= 1);
 
 //public bool isSorted(list[int] l)
 //= !any([int a] := l);
 
// public bool isSorted(list[int] l)
// = !any([*_, int a, int b, *_] := l, b < a);
// 
//value main() = isSorted([1,2,3]);

//test bool any20()  = !any(int X <- [10, 20 .. 30], 25 := X);
 
//
//value main() = any(int X <- {1,2,3}, X > 2);
 

//test bool any13()  = !all(<int X, int Y> <- {<1,10>,<30,3>,<2,20>});

 
//bool main() = all(x <- {1,2,3}, x > 2);
  

   
//test bool tstSort(list[int] L) = isSorted(sort(L));

//value main() //test bool lub_intstr()
//    = lub(\int(), \str()) == \value();  

//public list[int] primes(int upTo) = []; // TODO: replaced "p <- ..." by "int p <- ..." to help new typechecker
  
//
//public int arbPrime(int upTo) = ps[0] when list[int] ps := primes(upTo);
//
//test bool commutativeEq2(value x, value y) = (x := y) <==> (y := x);

//test bool tupleExpressions() {
//    //value n = 1; 
//    //value s = "string"; 
//    return int _ := 1; //tuple[int, int] _ := < 1, 1 >;// && tuple[str, str] _ := < s, s > && tuple[int, str] _ := < n , s >;
//}
//
//test bool reflexEq2(value x) = x := 1 && x == 1;
//
//test bool matchTypedAnonymous(){
//    b = [int _] := [1];
//    return b;
//}

//value f(value v){
//    if(int n := v) return n;
//    return 0;
//}

//test bool transEq2(value x, value y, value z) = (x := y && y := z) ==> (x := z);

//test bool subsetOrdering2(set[value] x, set[value] y) = (x <= y) <==> (x == {} || all(e <- x, e in y));

//value main() //test bool interpolation1() 
//    {x = "."; return (/<x>/ !:= "a");}
