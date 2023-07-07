module lang::rascal::tests::concrete::SubscriptAndSlice

import Exception;
import Node;

lexical X = [xyzXYZ];
lexical XStar = X* xs;
lexical XPlus = X+ xs1;
lexical XComma = {X ","}* xcommas;

syntax A = [abcABC];
syntax AStar = A* as;
syntax APlus = A+ as1;
syntax AComma = {A ","}* acommas;
syntax ACommaPlus = {A ","}+ acommas;
layout L = [ ]*;

bool eqNoSrc(Tree t, Tree u) = unsetRec(t, "src") == unsetRec(u, "src");

@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSubscript1() = eqNoSrc(([XStar] "xyz").xs[0], [X] "x");
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSubscript2() = eqNoSrc(([XStar] "xyz").xs[1], [X] "y");
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSubscript3() = eqNoSrc(([XStar] "xyz").xs[2], [X] "z");

@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSubscript4() = eqNoSrc(([XStar] "xyz").xs[-1], [X] "z");
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSubscript5() = eqNoSrc(([XStar] "xyz").xs[-2], [X] "y");
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSubscript6() = eqNoSrc(([XStar] "xyz").xs[-3], [X] "x");

@ignoreInterpreter{
Incorrect/not implemented
}
@expected{
IndexOutOfBounds
}
test bool lexSubscript7() = eqNoSrc(([XStar] "xyz").xs[3], [X] "x");
@ignoreInterpreter{
Incorrect/not implemented
}
@expected{
IndexOutOfBounds
}
test bool lexSubscript8() = eqNoSrc(([XStar] "xyz").xs[-4], [X] "x");

@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSubscriptSep1() = eqNoSrc(([XComma] "x,y,z").xcommas[0], [X] "x");
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSubscriptSep2() = eqNoSrc(([XComma] "x,y,z").xcommas[1], [X] "y");
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSubscriptSep3() = eqNoSrc(([XComma] "x,y,z").xcommas[2], [X] "z");

@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSubscriptSep4() = eqNoSrc(([XComma] "x,y,z").xcommas[-1], [X] "z");
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSubscriptSep5() = eqNoSrc(([XComma] "x,y,z").xcommas[-2], [X] "y");
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSubscriptSep6() = eqNoSrc(([XComma] "x,y,z").xcommas[-3], [X] "x");

@ignoreInterpreter{
Incorrect/not implemented
}
@expected{
IndexOutOfBounds
}
test bool lexSubscriptSep7() = eqNoSrc(([XComma] "x,y,z").xcommas[3], [X] "x");
@ignoreInterpreter{
Incorrect/not implemented
}
@expected{
IndexOutOfBounds
}
test bool lexSubscriptSep8() = eqNoSrc(([XComma] "x,y,z").xcommas[-4], [X] "x");

@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSlice1() = eqNoSrc(([XStar] "xyz").xs[0..0], ([XStar] "").xs);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSlice2() = eqNoSrc(([XStar] "xyz").xs[0..1], ([XStar] "x").xs);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSlice3() = eqNoSrc(([XStar] "xyz").xs[0..2], ([XStar] "xy").xs);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSlice4() = eqNoSrc(([XStar] "xyz").xs[0..3], ([XStar] "xyz").xs);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSlice5() = eqNoSrc(([XStar] "xyz").xs[1..1], ([XStar] "").xs);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSlice6() = eqNoSrc(([XStar] "xyz").xs[1..2], ([XStar] "y").xs);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSlice7() = eqNoSrc(([XStar] "xyz").xs[1..3], ([XStar] "yz").xs);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSlice8() = eqNoSrc(([XStar] "xyz").xs[2..3], ([XStar] "z").xs);

@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceNeg1() = eqNoSrc(([XStar] "xyz").xs[2..1], ([XStar] "z").xs);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceNeg2() = eqNoSrc(([XStar] "xyz").xs[2..0], ([XStar] "zy").xs);

@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceStep1() = eqNoSrc(([XStar] "xyzXYZxyz").xs [0,2..], ([XStar] "xzYxz").xs);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceStep2() = eqNoSrc(([XStar] "xyzXYZxyz").xs [0,3..], ([XStar] "xXx").xs);

@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceNegStep1() = eqNoSrc(([XStar] "xyzXYZxyz").xs [8,6..], ([XStar] "zxYzx").xs);

@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceSep1() = eqNoSrc(([XComma] "x,y,z").xcommas[0..0], ([XComma] "").xcommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceSep2() = eqNoSrc(([XComma] "x,y,z").xcommas[0..1], ([XComma] "x").xcommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceSep3() = eqNoSrc(([XComma] "x,y,z").xcommas[0..2], ([XComma] "x,y").xcommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceSep4() = eqNoSrc(([XComma] "x,y,z").xcommas[0..3], ([XComma] "x,y,z").xcommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceSep5() = eqNoSrc(([XComma] "x,y,z").xcommas [1..1], ([XComma] "").xcommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceSep6() = eqNoSrc(([XComma] "x,y,z").xcommas [1..2], ([XComma] "y").xcommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceSep7() = eqNoSrc(([XComma] "x,y,z").xcommas [1..3], ([XComma] "y,z").xcommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceSep8() = eqNoSrc(([XComma] "x,y,z").xcommas [2..3], ([XComma] "z").xcommas);

@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceSepStep1() = eqNoSrc(([XComma] "x,y,z,X,Y,Z,x,y,z").xcommas[0,2..], ([XComma] "x,z,Y,x,z").xcommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceSepStep2() = eqNoSrc(([XComma] "x,y,z,X,Y,Z,x,y,z").xcommas[0,3..], ([XComma] "x,X,x").xcommas);

@ignoreInterpreter{
Incorrect/not implemented
}
test bool lexSliceSepNegStep1() = eqNoSrc(([XComma] "x,y,z,X,Y,Z,x,y,z").xcommas[8,6..], ([XComma] "z,x,Y,z,x").xcommas);

@ignoreInterpreter{
Incorrect/not implemented
}
@expected{
IllegalArgument
}
test bool lexIllegalSlice() { ([XPlus] "xyz").xs1[0 .. 0]; return false; }

@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSubscript1() = eqNoSrc(([AStar] "abc").as [0], [A] "a"); 
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSubscript2() = eqNoSrc(([AStar] "abc").as [1], [A] "b");
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSubscript3() = eqNoSrc(([AStar] "abc").as [2], [A] "c");

@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSubscript4() = eqNoSrc(([AStar] "abc").as [-1], [A] "c"); 
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSubscript5() = eqNoSrc(([AStar] "abc").as [-2], [A] "b"); 
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSubscript6() = eqNoSrc(([AStar] "abc").as [-3], [A] "a"); 

@ignoreInterpreter{
Incorrect/not implemented
}
@expected{
IndexOutOfBounds
}
test bool cfgSubscript7() = eqNoSrc(([AStar] "abc").as [3], [A] "a"); 
@ignoreInterpreter{
Incorrect/not implemented
}
@expected{
IndexOutOfBounds
}
test bool cfgSubscript8() = eqNoSrc(([AStar] "abc").as [-4], [A] "a"); 

@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSubscriptSep1() = eqNoSrc(([AComma] "a,b,c").acommas [0], [A] "a");
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSubscriptSep2() = eqNoSrc(([AComma] "a,b,c").acommas [1], [A] "b");
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSubscriptSep3() = eqNoSrc(([AComma] "a,b,c").acommas [2], [A] "c");

@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSubscriptSep4() = eqNoSrc(([AComma] "a,b,c").acommas [-1], [A] "c");
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSubscriptSep5() = eqNoSrc(([AComma] "a,b,c").acommas [-2], [A] "b");
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSubscriptSep6() = eqNoSrc(([AComma] "a,b,c").acommas [-3], [A] "a");

@ignoreInterpreter{
Incorrect/not implemented
}
@expected{
IndexOutOfBounds
}
test bool cfgSubscriptSep7() = eqNoSrc(([AComma] "a,b,c").acommas [3], [A] "c");
@ignoreInterpreter{
Incorrect/not implemented
}
@expected{
IndexOutOfBounds
}
test bool cfgSubscriptSep8() = eqNoSrc(([AComma] "a,b,c").acommas [-4], [A] "c");

@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSlice1() = eqNoSrc(([AStar] "abc").as [0..0], ([AStar] "").as);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSlice2() = eqNoSrc(([AStar] "abc").as [0..1], ([AStar] "a").as);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSlice3() = eqNoSrc(([AStar] "abc").as [0..2], ([AStar] "ab").as);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSlice4() = eqNoSrc(([AStar] "abc").as [0..3], ([AStar] "abc").as);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSlice5() = eqNoSrc(([AStar] "abc").as [1..1], ([AStar] "").as);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSlice6() = eqNoSrc(([AStar] "abc").as [1..2], ([AStar] "b").as);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSlice7() = eqNoSrc(([AStar] "abc").as [1..3], ([AStar] "bc").as);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSlice8() = eqNoSrc(([AStar] "abc").as [2..3], ([AStar] "c").as);

@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceStep1() = eqNoSrc(([AStar] "abcABCabc").as [0,2..], ([AStar] "acBac").as);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceStep2() = eqNoSrc(([AStar] "abcABCabc").as [0,3..], ([AStar] "aAa").as);

@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceNegStep1() = eqNoSrc(([AStar] "abcABCabc").as [8,6..], ([AStar] "caBca").as);

@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceSep1() = eqNoSrc(([AComma] "a,b,c").acommas [0..0], ([AComma] "").acommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceSep2() = eqNoSrc(([AComma] "a,b,c").acommas [0..1], ([AComma] "a").acommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceSep3() = eqNoSrc(([AComma] "a,b,c").acommas [0..2], ([AComma] "a,b").acommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceSep4() = eqNoSrc(([AComma] "a,b,c").acommas [0..3], ([AComma] "a,b,c").acommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceSep5() = eqNoSrc(([AComma] "a,b,c").acommas [1..1], ([AComma] "").acommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceSep6() = eqNoSrc(([AComma] "a,b,c").acommas [1..2], ([AComma] "b").acommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceSep7() = eqNoSrc(([AComma] "a,b,c").acommas [1..3], ([AComma] "b,c").acommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceSep8() = eqNoSrc(([AComma] "a,b,c").acommas [2..3], ([AComma] "c").acommas);

@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceSepStep1() = eqNoSrc(([AComma] "a,b,c,A,B,C,a,b,c").acommas [0,2..], ([AComma] "a,c,B,a,c").acommas);
@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceSepStep2() = eqNoSrc(([AComma] "a,b,c,A,B,C,a,b,c").acommas [0,3..], ([AComma] "a,A,a").acommas);

@ignoreInterpreter{
Incorrect/not implemented
}
test bool cfgSliceSepNegStep1() = eqNoSrc(([AComma] "a,b,c,A,B,C,a,b,c").acommas [8,6..], ([AComma] "c,a,B,c,a").acommas);

@ignoreInterpreter{
Incorrect/not implemented
}
@expected{
IllegalArgument
}
test bool cfgIllegalSlice() { ([APlus] "abc").as1[0 .. 0]; return false; }

@ignoreInterpreter{
Incorrect/not implemented
}
@expected{
IllegalArgument
}
test bool cfgIllegalSliceSep() { ([ACommaPlus] "a,b,c").acommas[0 .. 0]; return false; }
