module lang::rascalcore::compile::Examples::Tst2

value main() = [*int L] := [1] && L == [1];
 
//public bool isDuo1(list[int] L)
//{
//    switch(L){
//    case [int L1, int L2]:
//        if(L1 == L2){
//            return true;
//        } else {
//            fail;
//        }
//    default:
//        return false;
//      }
//}
//
//value main() = isDuo1([1,2]) == false;

//public int sum([int hd, *int tl])
//    = hd;

 

//import Map;
//test bool delete1(&K k) = isEmpty(delete((),k));

//value main() {int n3 = 1; return (n3 !:= 2) && (n3 == 1);}
//value main() = /int N := [1,2,3,2] && N > 2;

//value main() =  {*int X, *real Y} := { 1, 5.5, 2, 6.5};// && (X == {1,2} && Y == {5.5, 6.5});

//value main(){
//    res = {};
//    for({6, *int a, int x, *int b, int y, 2, *int c} := {1,2,3,4,5,6,7}) { res = res + {{a,b,c}}; }
//    return res;
//}

//public int ModVar44 = 44;
//public set[int] ModVarSet_41_42_43 = {41, 42, 43};
//
//@ignoreInterpreter{Seems to be a bug in the interpreter}
//value main() = {ModVar44, ModVarSet_41_42_43} := {ModVar44, ModVarSet_41_42_43};

// test bool matchSet23() {int N = 3; return {N, 2, 1} := {1,2,3};}
//value main() = {*int X, 3, *int Y, *int Z} := {} && X == {} && Y == {} && Z == {};

//value main() = ({int N, 2, N} := {1,2,"a"});


                                                
//value main() = {*int X, *int Y, *int Z} := {} && X == {} && Y == {} && Z == {};
