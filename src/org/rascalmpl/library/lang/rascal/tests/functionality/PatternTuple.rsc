module lang::rascal::tests::functionality::PatternTuple

//  matchTuple
  
test bool matchTuple1a() = <1> := <1>;
test bool matchTuple1b() = <2> !:= <1>;
test bool matchTuple1c() = !(<2> := <1>);
test bool matchTuple1d() = !<2>  := <1>;

test bool matchTuple2a() = <1, "abc">  := <1, "abc">;
test bool matchTuple2b() = <1, "abc"> !:= <1, "def">;
test bool matchTuple2c() = !(<1, "abc"> := <1, "def">);
test bool matchTuple2d() = !<1, "abc"> := <1, "def">;

test bool matchTuple2e() = <_, "abc">  := <1, "abc">;
test bool matchTuple2f() = <1, _>        := <1, "abc">;
test bool matchTuple2g() = <_, _>        := <1, "abc">;

// T is not initialized on purpose here 
test bool matchTupleExternalVar1() {tuple[int,int] T; return T := <1,2> && T[0] == 1 && T[1] == 2;}
    
data D = d1() | d2();

test bool matchTupleADT1a() = <d1()> := <d1()>;
test bool matchTupleADT1b() = <d1()> !:= <d2()>;
test bool matchTupleADT1c() = !(<d1()> := <d2()>);
@ignoreInterpreter{
to be determined
}
test bool matchTupleADT1d() = !<d1()> := <d2()>;

test bool matchTupleList1a() = <[]> := <[]>;
test bool matchTupleList1b() = <[1]> := <[1]>;
test bool matchTupleList1c() = <[1]> !:= <[2]>;
test bool matchTupleList1d() = !(<[1]> := <[2]>);
test bool matchTupleList1e() = !<[1]> := <[2]>;

test bool matchTupleList2a() = <[1, int _, 3]> := <[1,2,3]>;
test bool matchTupleList2b() = <[*int _, *int _, 3]> := <[1,2,3]>;
test bool matchTupleList2c() = <[*int x, *int _, 3]> := <[1,2,3]> && x == [1, 2];
test bool matchTupleList2d() = <[*int _, *int y, 3]> := <[1,2,3]> && y == [1, 2];

test bool matchTupleSet1a() = <{}> := <{}>;
test bool matchTupleSet1b() = <{1}> := <{1}>;
test bool matchTupleSet1c() = <{1}> !:= <{2}>;
test bool matchTupleSet1d() = !(<{1}> := <{2}>);
@ignoreInterpreter{
to be determined
}
test bool matchTupleSet1e() = !<{1}> := <{2}>;

test bool matchTupleSet2a() = <{1, int _, 3}> := <{1,2,3}>;
test bool matchTupleSet2b() = <{*int _, *int _, 3}> := <{1,2,3}>;
test bool matchTupleSet2c() = <{*int x, *int _, 3}> := <{1,2,3}> && x == {1, 2};
test bool matchTupleSet2d() = <{*int _, *int y, 3}> := <{1,2,3}> && y == {1, 2};

test bool matchTupleSet3a() = <{int x, *int _}> := <{1,2,3}> && x == 1;
test bool matchTupleSet3b() = <{int x, *int _}> := <{1,2,3}> && x == 2;
test bool matchTupleSet3c() = <{int x, *int _}> := <{1,2,3}> && x == 3;

test bool matchTupleSet4a() = <{int x, *int _}, {int p, *int _}> := <{1,2}, {10,20}> && x == 1 && p == 10;
test bool matchTupleSet4b() = <{int x, *int _}, {int p, *int _}> := <{1,2}, {10,20}> && x == 1 && p == 20;
test bool matchTupleSet4c() = <{int x, *int _}, {int p, *int _}> := <{1,2}, {10,20}> && x == 2 && p == 10;
test bool matchTupleSet4d() = <{int x, *int _}, {int p, *int _}> := <{1,2}, {10,20}> && x == 2 && p == 20;
