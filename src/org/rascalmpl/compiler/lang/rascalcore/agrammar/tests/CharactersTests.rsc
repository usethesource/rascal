@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::agrammar::tests::CharactersTests

import lang::rascalcore::agrammar::definition::Characters;
import lang::rascalcore::check::AType;

test bool testFlip() = \new-char-class([arange(2,2), arange(1,1)]) == \achar-class([arange(1,2)]);
test bool testMerge() = \new-char-class([arange(3,4), arange(2,2), arange(1,1)]) == \achar-class([arange(1,4)]);
test bool testEnvelop() = \new-char-class([arange(10,20), arange(15,20), arange(20,30)]) == \achar-class([arange(10,30)]);
test bool testEnvelop2() = \new-char-class([arange(10,20), arange(10,19), arange(20,30)]) == \achar-class([arange(10,30)]);

test bool testComp() = complement(\achar-class([])) == \achar-class([arange(1,1114111)]);
test bool testComp2() = complement(\achar-class([arange(0,0)])) == \achar-class([arange(1,1114111)]);
test bool testComp3() = complement(\achar-class([arange(1,1)])) == \achar-class([arange(2,1114111)]);
test bool testComp4() = complement(\achar-class([arange(10,20), arange(30,40)])) == \achar-class([arange(1,9),arange(21,29),arange(41,1114111)]);
test bool testComp5() = complement(\achar-class([arange(10,35), arange(30,40)])) == \achar-class([arange(1,9),arange(41,1114111)]);

test bool testUnion1() = union(\achar-class([arange(10,20)]), \achar-class([arange(30, 40)])) == \achar-class([arange(10,20), arange(30,40)]);
test bool testUnion2() = union(\achar-class([arange(10,25)]), \achar-class([arange(20, 40)])) == \achar-class([arange(10,40)]);
 
test bool testInter1() = intersection(\achar-class([arange(10,20)]), \achar-class([arange(30, 40)])) == \achar-class([]);
test bool testInter2() = intersection(\achar-class([arange(10,25)]), \achar-class([arange(20, 40)])) == \achar-class([arange(20, 25)]);

test bool testDiff1() = difference(\achar-class([arange(10,30)]), \achar-class([arange(20,25)])) == \achar-class([arange(10,19), arange(26,30)]);
test bool testDiff2() = difference(\achar-class([arange(10,30), arange(40,50)]), \achar-class([arange(25,45)])) ==\achar-class( [arange(10,24), arange(46,50)]);


