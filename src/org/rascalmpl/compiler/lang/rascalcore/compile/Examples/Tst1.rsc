module lang::rascalcore::compile::Examples::Tst1

import demo::lang::Pico::Abstract;

//public data TYPE = natural() | string(); // <1>
//      
//public alias PicoId = str; // <2>
//      
//public data PROGRAM = // <3>
//  program(list[DECL] decls, list[STATEMENT] stats);
//
//public data DECL =
//  decl(PicoId name, TYPE tp);
//
//public data EXP = 
//       id(PicoId name)
//     | natCon(int iVal)
//     | strCon(str sVal)
//     | add(EXP left, EXP right)
//     | sub(EXP left, EXP right)
//     | conc(EXP left, EXP right)
//     ;
//    
//public data STATEMENT =
//       asgStat(PicoId name, EXP exp)
//     | ifElseStat(EXP exp, list[STATEMENT] thenpart, list[STATEMENT] elsepart)
//     | whileStat(EXP exp, list[STATEMENT] body)
//     ;
//
//anno loc TYPE@location; // <4>
//anno loc PROGRAM@location;
//anno loc DECL@location;
//anno loc EXP@location;
//anno loc STATEMENT@location;

//public alias Occurrence = tuple[loc location, PicoId name, STATEMENT stat]; // <5>
// end::module[]
 
set[Occurrence] usesExp(EXP e, STATEMENT s) =  // <1>
  u:id(PicoId Id1) := e ? {< u@location, Id1, s>}
                        : {< u@location, Id2, s> | /u:id(PicoId Id2) <- e };


//data Tree;
//java &T<:Tree parse(type[&T<:Tree] begin);

//Tree f(&T <: Tree t) = t;
//java void parse(&T<:Tree begin);

//data Tree;
//
//&T <:Tree f(&T <: Tree x) = x;

//bool strange1(&A <: num _, &B <: &A _, &B <: str _){
//  return false;
//}
//bool strange2(&A <: num _, &B <: &A _, &S <: str _, &B <: &S _){
//  return false;
//}
//bool strange4(&A <: &B <: str _, &B <: &A <: num _) = false;


//bool strange(&L <: num _, &R <: &L _){
//  return false;
//}
//
//value main() = strange(3, "abc");

//bool strange2(&L <: num arg1, &L <: str arg2){
//    return false;
//}

//data Result[&S1, &E1] = Ok(&S1) | Error (&E1);
//
//// bindR. first implementation of bind,
//public Result[&S3, &E3] bindR( ( Result[&S3, &E3] ( &Sin3 ) ) f, Result[&Sin3, &E3] xR) {
//    switch (xR) {
//        case Ok(x)    : return f(x);
//        case Error(e) : return Error(e); 
//    } 
//    throw "Cannot happen";
//}
//
//// bindR2. Second implementation of bind,
//public Result[&S4, &E4] bindR2( ( Result[&S4, &E4] ( &Sin4 ) ) f, Ok(x)    ) = f(x);
//public Result[&S4, &E4] bindR2( ( Result[&S4, &E4] ( &Sin4 ) ) _, Error(e) ) = Error(e); // <-- (UnexpectedType 1)
//
//Result[num, void] fb(&Success <: num x) = Ok(x*3);
//
//test bool testBindResult3() = bindR(fb, Error([]))  == Error([]);
//
//test bool testBindResult4() = bindR2(fb, Error([])) == Error([]); // (*) <-- Results in runtime Error



//=============
//set[int] g(set[set[int]] ns)
//    = { *x | st <- ns, {1, *int x} := st || {2, *int x} := st };