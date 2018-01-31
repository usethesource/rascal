module lang::rascalcore::grammar::tests::CharactersTests

import lang::rascalcore::grammar::definition::Characters;
import lang::rascalcore::check::AType;

test bool testFlip() = \new-char-class([range(2,2), range(1,1)]) == \char-class([range(1,2)]);
test bool testMerge() = \new-char-class([range(3,4), range(2,2), range(1,1)]) == \char-class([range(1,4)]);
test bool testEnvelop() = \new-char-class([range(10,20), range(15,20), range(20,30)]) == \char-class([range(10,30)]);
test bool testEnvelop2() = \new-char-class([range(10,20), range(10,19), range(20,30)]) == \char-class([range(10,30)]);

test bool testComp() = complement(\char-class([])) == \char-class([range(1,1114111)]);
test bool testComp2() = complement(\char-class([range(0,0)])) == \char-class([range(1,1114111)]);
test bool testComp3() = complement(\char-class([range(1,1)])) == \char-class([range(2,1114111)]);
test bool testComp4() = complement(\char-class([range(10,20), range(30,40)])) == \char-class([range(1,9),range(21,29),range(41,1114111)]);
test bool testComp5() = complement(\char-class([range(10,35), range(30,40)])) == \char-class([range(1,9),range(41,1114111)]);

test bool testUnion1() = union(\char-class([range(10,20)]), \char-class([range(30, 40)])) == \char-class([range(10,20), range(30,40)]);
test bool testUnion2() = union(\char-class([range(10,25)]), \char-class([range(20, 40)])) == \char-class([range(10,40)]);
 
test bool testInter1() = intersection(\char-class([range(10,20)]), \char-class([range(30, 40)])) == \char-class([]);
test bool testInter2() = intersection(\char-class([range(10,25)]), \char-class([range(20, 40)])) == \char-class([range(20, 25)]);

test bool testDiff1() = difference(\char-class([range(10,30)]), \char-class([range(20,25)])) == \char-class([range(10,19), range(26,30)]);
test bool testDiff2() = difference(\char-class([range(10,30), range(40,50)]), \char-class([range(25,45)])) ==\char-class( [range(10,24), range(46,50)]);


