module experiments::Compiler::Examples::Tst1

//import ParseTree;
//syntax AB = [ab];
//syntax ABs = {AB ","}+ abs;
//
//bool containsA({AB ","}+ abs)
//    = any(ab <- abs, "<ab>" == "a");
//
//value work(){
//    if({AB ","}+ abs :=
//    appl(
//  regular(\iter-seps(
//      sort("AB"),
//      [
//        layouts("$default$"),
//        lit(","),
//        layouts("$default$")
//      ])),
//  [appl(
//      prod(
//        sort("AB"),
//        [\char-class([range(97,98)])],
//        {}),
//      [char(98)])[
//      @\loc=|prompt:///|(0,1,<1,0>,<1,1>)
//    ],appl(
//      prod(
//        layouts("$default$"),
//        [],
//        {}),
//      [])[
//      @\loc=|prompt:///|(1,0,<1,1>,<1,1>)
//    ],appl(
//      prod(
//        lit(","),
//        [\char-class([range(44,44)])],
//        {}),
//      [char(44)]),appl(
//      prod(
//        layouts("$default$"),
//        [],
//        {}),
//      [])[
//      @\loc=|prompt:///|(2,0,<1,2>,<1,2>)
//    ],appl(
//      prod(
//        sort("AB"),
//        [\char-class([range(97,98)])],
//        {}),
//      [char(97)])[
//      @\loc=|prompt:///|(2,1,<1,2>,<1,3>)
//    ],appl(
//      prod(
//        layouts("$default$"),
//        [],
//        {}),
//      [])[
//      @\loc=|prompt:///|(3,0,<1,3>,<1,3>)
//    ],appl(
//      prod(
//        lit(","),
//        [\char-class([range(44,44)])],
//        {}),
//      [char(44)]),appl(
//      prod(
//        layouts("$default$"),
//        [],
//        {}),
//      [])[
//      @\loc=|prompt:///|(4,0,<1,4>,<1,4>)
//    ],appl(
//      prod(
//        sort("AB"),
//        [\char-class([range(97,98)])],
//        {}),
//      [char(98)])[
//      @\loc=|prompt:///|(4,1,<1,4>,<1,5>)
//    ]])[
//  @\loc=|prompt:///|(0,5,<1,0>,<1,5>)
//]){
//    return containsA(abs);
//}   
//    return false;
//}
//
//value main() = work();

value main() = all(int x <- []) == true;


//"f"(1) := "f"(1) ? 10 : 20;


//a/ := "a" ? 10 : 20;


//[1] := [1] ? 10 : 20;


//{ int n = 0; for(x <- {1,2,3}, y <- [10,20,30]) n += x+y; return n;}