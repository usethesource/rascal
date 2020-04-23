module lang::rascalcore::compile::Examples::Tst1



data Result[&S1, &E1] = Ok(&S1) | Error (&E1);

//// bindR. first implementation of bind,
//public Result[&S3, &E3] bindR( ( Result[&S3, &E3] ( &Sin3 ) ) f, Result[&Sin3, &E3] xR) {
//    switch (xR) {
//        case Ok(x)    : return f(x);
//        case Error(e) : return Error(e); 
//    } 
//    throw "Cannot happen";
//}

// bindR2. Second implementation of bind,
//public Result[&S4, &E4] bindR2( ( Result[&S4, &E4] ( &Sin4 ) ) f, Ok(&S4 x)    ) = f(x);
public Result[&S4, &E4] bindR2( ( Result[&S4, &E4] ( &Sin4 ) ) _, Error(e) ) = Error(e); // <-- (UnexpectedType 1)

Result[num, void] fb(&Success <: num x) = Ok(x*3);

//test bool testBindResult3() = bindR(fb, Error([]))  == Error([]);

test bool testBindResult4() = bindR2(fb, Error([])) == Error([]); // (*) <-- Results in runtime Error



//=============
//set[int] g(set[set[int]] ns)
//    = { *x | st <- ns, {1, *int x} := st || {2, *int x} := st };