module lang::rascalcore::compile::Examples::Tst1

import IO;


syntax A = [abcABC];
syntax AStar = A* as;
syntax APlus = A+ as1;
syntax AComma = {A ","}* acommas;
syntax ACommaPlus = {A ","}+ acommas;
layout L = [ ]*;

value main(){ //test bool cfgIllegalSliceSep() { ([AComma] "a,b,c").acommas[0 .. 0]; return false; }
    A =  ([ACommaPlus] "a,b,c").acommas [0..0];
   
    iprintln(A);
 
    
    return A;
}

//@ignoreInterpreter{Incorrect/not implemented}
//test bool lexSliceSep3() = ([XComma] "x,y,z").xcommas[0..2] == ([XComma] "x,y").xcommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool lexSliceSep4() = ([XComma] "x,y,z").xcommas[0..3] == ([XComma] "x,y,z").xcommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool lexSliceSep5() = ([XComma] "x,y,z").xcommas [1..1] == ([XComma] "").xcommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool lexSliceSep6() = ([XComma] "x,y,z").xcommas [1..2] == ([XComma] "y").xcommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool lexSliceSep7() = ([XComma] "x,y,z").xcommas [1..3] == ([XComma] "y,z").xcommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool lexSliceSep8() = ([XComma] "x,y,z").xcommas [2..3] == ([XComma] "z").xcommas;
//
//@ignoreInterpreter{Incorrect/not implemented}
//test bool lexSliceSepStep1() = ([XComma] "x,y,z,X,Y,Z,x,y,z").xcommas[0,2..] == ([XComma] "x,z,Y,x,z").xcommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool lexSliceSepStep2() = ([XComma] "x,y,z,X,Y,Z,x,y,z").xcommas[0,3..] == ([XComma] "x,X,x").xcommas;
//
//@ignoreInterpreter{Incorrect/not implemented}
//test bool lexSliceSepNegStep1() = ([XComma] "x,y,z,X,Y,Z,x,y,z").xcommas[8,6..] == ([XComma] "z,x,Y,z,x").xcommas;
//
//@ignoreInterpreter{Incorrect/not implemented}
//@expected{IllegalArgument}
//test bool lexIllegalSlice() { ([XPlus] "xyz").xs1[0 .. 0]; return false; }
//
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSubscript1() = ([AStar] "abc").as [0] == [A] "a"; 
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSubscript2() = ([AStar] "abc").as [1] == [A] "b";
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSubscript3() = ([AStar] "abc").as [2] == [A] "c";
//
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSubscript4() = ([AStar] "abc").as [-1] == [A] "c"; 
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSubscript5() = ([AStar] "abc").as [-2] == [A] "b"; 
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSubscript6() = ([AStar] "abc").as [-3] == [A] "a"; 
//
//@ignoreInterpreter{Incorrect/not implemented}
//@expected{IndexOutOfBounds}
//test bool cfgSubscript7() = ([AStar] "abc").as [3] == [A] "a"; 
//@ignoreInterpreter{Incorrect/not implemented}
//@expected{IndexOutOfBounds}
//test bool cfgSubscript8() = ([AStar] "abc").as [-4] == [A] "a"; 
//
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSubscriptSep1() = ([AComma] "a,b,c").acommas [0] == [A] "a";
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSubscriptSep2() = ([AComma] "a,b,c").acommas [1] == [A] "b";
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSubscriptSep3() = ([AComma] "a,b,c").acommas [2] == [A] "c";
//
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSubscriptSep4() = ([AComma] "a,b,c").acommas [-1] == [A] "c";
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSubscriptSep5() = ([AComma] "a,b,c").acommas [-2] == [A] "b";
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSubscriptSep6() = ([AComma] "a,b,c").acommas [-3] == [A] "a";
//
//@ignoreInterpreter{Incorrect/not implemented}
//@expected{IndexOutOfBounds}
//test bool cfgSubscriptSep7() = ([AComma] "a,b,c").acommas [3] == [A] "c";
//@ignoreInterpreter{Incorrect/not implemented}
//@expected{IndexOutOfBounds}
//test bool cfgSubscriptSep8() = ([AComma] "a,b,c").acommas [-4] == [A] "c";
//
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSlice1() = ([AStar] "abc").as [0..0] == ([AStar] "").as;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSlice2() = ([AStar] "abc").as [0..1] == ([AStar] "a").as;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSlice3() = ([AStar] "abc").as [0..2] == ([AStar] "ab").as;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSlice4() = ([AStar] "abc").as [0..3] == ([AStar] "abc").as;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSlice5() = ([AStar] "abc").as [1..1] == ([AStar] "").as;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSlice6() = ([AStar] "abc").as [1..2] == ([AStar] "b").as;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSlice7() = ([AStar] "abc").as [1..3] == ([AStar] "bc").as;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSlice8() = ([AStar] "abc").as [2..3] == ([AStar] "c").as;
//
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceStep1() = ([AStar] "abcABCabc").as [0,2..] == ([AStar] "acBac").as;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceStep2() = ([AStar] "abcABCabc").as [0,3..] == ([AStar] "aAa").as;
//
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceNegStep1() = ([AStar] "abcABCabc").as [8,6..] == ([AStar] "caBca").as;
//
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceSep1() = ([AComma] "a,b,c").acommas [0..0] == ([AComma] "").acommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceSep2() = ([AComma] "a,b,c").acommas [0..1] == ([AComma] "a").acommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceSep3() = ([AComma] "a,b,c").acommas [0..2] == ([AComma] "a,b").acommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceSep4() = ([AComma] "a,b,c").acommas [0..3] == ([AComma] "a,b,c").acommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceSep5() = ([AComma] "a,b,c").acommas [1..1] == ([AComma] "").acommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceSep6() = ([AComma] "a,b,c").acommas [1..2] == ([AComma] "b").acommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceSep7() = ([AComma] "a,b,c").acommas [1..3] == ([AComma] "b,c").acommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceSep8() = ([AComma] "a,b,c").acommas [2..3] == ([AComma] "c").acommas;
//
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceSepStep1() = ([AComma] "a,b,c,A,B,C,a,b,c").acommas [0,2..] == ([AComma] "a,c,B,a,c").acommas;
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceSepStep2() = ([AComma] "a,b,c,A,B,C,a,b,c").acommas [0,3..] == ([AComma] "a,A,a").acommas;
//
//@ignoreInterpreter{Incorrect/not implemented}
//test bool cfgSliceSepNegStep1() = ([AComma] "a,b,c,A,B,C,a,b,c").acommas [8,6..] == ([AComma] "c,a,B,c,a").acommas;
//
//@ignoreInterpreter{Incorrect/not implemented}
//@expected{IllegalArgument}
//test bool cfgIllegalSlice() { ([APlus] "abc").as1[0 .. 0]; return false; }
