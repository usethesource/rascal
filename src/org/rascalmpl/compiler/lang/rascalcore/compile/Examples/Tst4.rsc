module lang::rascalcore::compile::Examples::Tst4
 
//value main() {
//    if({*int x} := {1,2,3} || {*int x} := {10,20,30}) return x; else return 0;
//}
//
// value main1() = {*int x} := {1,2,3} || {*int x} := {10,20,30};
// value main2() = [+int x] := [1,2,3] || [+int x] := [10,20,30];
//  value main3() = [*int x] := [1,2,3] || [*int x] := [10,20,30];
  value main() = [+int x] := [1,2,3] || [+int x] := [10,20,30];
 //value main5() = [int x: "a"] := [1] || [x: 10] := [10];