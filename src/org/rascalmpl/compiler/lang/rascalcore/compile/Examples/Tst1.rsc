module lang::rascalcore::compile::Examples::Tst1

public map[&K, &V] mapper(map[&K, &V] M, &L (&K) F, &W (&V) G)
{
  return (F(key) : G(M[key]) | &K key <- M);
}

data Maybe[&T] = none() | just(&T arg);

Maybe[&S <: num] mb() { 
    if(3 > 2) return just(3); return just("Abc"); }

//test bool unboundTypeVar1() { Maybe[int] x = mb(); return x == none();}
//test bool unboundTypeVar2() { x = none(); x = just(0); return x == just(0);}
    

//import Exception;
//
//
//
//public alias Undefined  = RuntimeException;
//data Result[&S1, &E1] = Ok(&S1) | Error (&E1);
//
//// PureR (aka return)
//public Result[&S2, &E2] PureR(&S2 s) = Ok(s);
//
//// bindR. first implementation of bind,
//public Result[&S3, &E3] bindR( ( Result[&S3, &E3] ( &Sin3 ) ) f, Result[&Sin3, &E3] xR) {
//    switch (xR) {
//        case Ok(x)    : return f(x);
//        case Error(e) : return Error(e); 
//        default: throw "Cannot happen";
//   } 
//}
//
//// bindR2. Second implementation of bind,
//public Result[&S4, &E4] bindR2( ( Result[&S4, &E4] ( &Sin4 ) ) f, Ok(x)    ) = f(x);
//public Result[&S4, &E4] bindR2( ( Result[&S4, &E4] ( &Sin4 ) ) f, Error(e) ) = Error(e); // <-- (UnexpectedType 1)
//
//
//// partial application.
//public (&T (&X2)) partial( (&T (&X1, &X2)) f, &X1 x1 ) = (&T)( &X2 x2 ) { return f(x1, x2); }; //<-- (UnexpectedType 2)
//
//// Tests BindR
//// test helpers
//data Apple  = Apple(str);
//data Banana = Banana(str);
//int add(int x, int y) = x + y; 
//Result[num, void] fb(&Success <: num x) = Ok(x*3);
//Result[Banana,  Undefined] functionA (Apple apple) = Ok(Banana("banana"));
//
//test bool testBindResult1() = bindR(fb, Ok(3))      == Ok(9);
//test bool testBindResult2() = bindR2(fb, Ok(3))     == Ok(9); // <-- unexpected type checker warning
//test bool testBindResult3() = bindR(fb, Error([]))  == Error([]);
//test bool testBindResult4() = bindR2(fb, Error([])) == Error([]); // (*) <-- Results in runtime Error
//test bool testBindResult5() = bindR(functionA, PureR(Apple("apple")))  == Ok(Banana("banana"));
//test bool testBindResult6() = bindR2(functionA, PureR(Apple("apple"))) == Ok(Banana("banana")); // <-- unexpected type checker warning
//
//// Test partial() on BindR and BindR2
//// Test
//test bool testPartialAdd()        = partial(add, 3)(3) == 6;
//test bool testPartialBindROk()    = partial(bindR,  functionA)(PureR(Apple("apple"))) == Ok(Banana("banana")); // <-- unexpected type checker warning
//test bool testPartialBindRError() = partial(bindR,  functionA)(Error([])) == Error([]); // (**) <-- results in runtime error
//test bool testPartialBindR2()     = partial(bindR2, functionA)(PureR(Apple("apple"))) == Ok(Banana("banana")); // <-- unexpected type checker warning
//test bool testPartialBindR2Error()= partial(bindR2, functionA)(Error([])) == Error([]); // (***) <-- results in runtime error


//=============
//set[int] g(set[set[int]] ns)
//    = { *x | st <- ns, {1, *int x} := st || {2, *int x} := st };