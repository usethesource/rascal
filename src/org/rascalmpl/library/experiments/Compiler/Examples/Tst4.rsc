module experiments::Compiler::Examples::Tst4

value removeIdPairs(rel[int,int] inp){
   res = inp;
   solve(res) {
        if ( { < a, b >, < b, b >, c* } := res ) res = { *c, < a, b > };
    }
    return res;
 }
 
 test bool removeIdPairs1() = removeIdPairs({}) == {};
 test bool removeIdPairs2() = removeIdPairs({<1,2>,<2,3>}) == {<1,2>,<2,3>};
 test bool removeIdPairs3() = removeIdPairs({<1,2>,<2,3>,<2,2>}) == {<1,2>,<2,3>};
 test bool removeIdPairs4() = removeIdPairs({<1,2>,<2,2>,<2,3>,<3,3>}) == {<1,2>,<2,3>};
 test bool removeIdPairs5() = removeIdPairs({<2,2>,<1,2>,<2,2>,<2,3>,<3,3>}) == {<1,2>,<2,3>};
 test bool removeIdPairs6() = removeIdPairs({<2,2>,<3,3>,<1,2>,<2,2>,<2,3>,<3,3>}) == {<1,2>,<2,3>};
 