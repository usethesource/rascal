module lang::rascalcore::compile::Examples::Tst1

//import lang::rascalcore::check::tests::StaticTestingUtils;
import lang::rascal::\syntax::Rascal;

value main() = (Type) `void`@\loc;

//test bool ifThenElseError1() = unexpectedType("if(\"abc\") {n = 4;} else {n=5;}");
//test bool testUtils12() = checkOK("size([1,2,3]);", importedModules=["Exception", "List"]);
//test bool testAppendHasLexicalScopingClosure() =
//    illegalUse("{ f = () { append 3; }; for (x \<- [1,2,3]) { f(); } } == [3,3,3];");
//
//value main() = testAppendHasLexicalScopingClosure();
//bool  solver(bool (value, value) unify1) = true;
//   
//bool unify(int x, str y) = true;
//bool unify(int x, str y, bool b) = b; //<<< uncomment en krijg CallFailed
//
//bool newSolver(){
//    return solver(unify);
//}
//
//value main() = newSolver();

//test bool transEq1(value x, value y, value z) = (x == y && y == z) ==> (x == z);
                                          
//data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)|g(int N)|h(int N)| f(DATA left, DATA right);
//
//value main()//test bool matchListSet7() 
//    = ([a(), f({a(), b(), DATA X6})] := [a(), f({a(),b(),c()})]) && (X6 == c());
//test bool matchListSet9() = ([a(), f({a(), b(), DATA X8}), *DATA Y8] := [a(), f({a(),b(),c()}), b()]) && (X8 == c() && Y8 == [b()]);


//data F = f(F left, F right) | g(int N);
//test bool descendant33() = [1, [F] /f(/g(2), F _), 3] := [1, f(g(1),f(g(2),g(3))), 3];

//value main() // test bool descendant34() 
//    = [1, /f(/g(2),/g(3)), 3] := [1, f(g(1),f(g(2),g(3))), 3];
    
//test bool descendant37() = [1, /g(int N2), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N2 == 2;
//test bool descendant38() = [1, /g(int N3), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N3 == 3;


   
//////////
//value main() = {<int a, int b>, <b, b>, *c} := {<1, 2>, <2, 2>, <3,4>};
//value main() {list[int] L; return ([1, *L, 4, 5] := [1, 2, 3, 4, 5] && L == [2, 3]);}


// int f11(int c = 10){
//        int g11(int d = 100){
//            return c + d;
//        }
//        return g11();
//    }
//    
//value main(){ //test bool keywordParam92(){
//    return f11();
//}

//int f13(int n, str s = "") = n when s == "";
//int f13(int n, str s = "") = -n when s != "";
//
//value main() // test bool when1() 
//    = f13(10);// == 10;

//data D[&T] = d1(&T fld);
//
//value main() = #type[D[int]]
 

//syntax Sym
//    =   
//     empty: "(" ")"
//    ;
//
//syntax Type
//    = bracket \bracket: "(" Type type ")" 
//    | symbol: Sym symbol
//    ;
//    
//value main() = true;
//
//
//data Symbol     // <2>
//     = \label(str name, Symbol symbol)
//     ;


