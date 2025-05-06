@license{
  Copyright (c) 2009-2020 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::tests::basic::Booleans

import Boolean;
import Exception;
// Operators

test bool sanity() = true != false;

test bool or(bool b) { if (true || b == true, b || true == true, false || false == false) return true; else return false; }  
  
test bool and(bool b) { if ((false && b) == false, (b && false) == false, (true && true) == true) return true; else return false; }

test bool not1(bool b) = !!b == b;

test bool not2() = (!true == false) && (!false == true);

test bool equiv(bool b1, bool b2) = (b1 <==> b2) <==> (!b1 && !b2 || b1 && b2);

test bool impl(bool b1, bool b2) = (b1 ==> b2) <==> !(b1 && !b2);

test bool assignAnd(bool b1, bool b2) { 
    int n = b1 && b2 ? 10 : 20;
    if(b1 && b2) return n == 10; else return n == 20;
}

test bool assignOr(bool b1, bool b2) { 
    int n = b1 || b2 ? 10 : 20;
    if(b1 || b2) return n == 10; else return n == 20;
}

// Library functions

test bool tstArbBool() { b = arbBool() ; return b == true || b == false; }

test bool fromString1() = fromString("true") == true && fromString("false") == false;

@expected{IllegalArgument}
test bool fromString2(str s) = fromString(s); // will fail in the rare situation that "true" or "false" are passed as argument.

test bool tstToInt() = toInt(false) == 0 && toInt(true) == 1;

test bool tstToReal() = toReal(false) == 0.0 && toInt(true) == 1.0;

test bool tstToString() = toString(false) == "false" && toString(true) == "true";

@ignoreCompiler{Undetermined}
test bool shortCircuiting() { 
	try { return false ==> (1/0 == 0) && true || (1/0 == 0) && !(false && (1/0 == 0)); }
	catch ArithmeticException(str _): { return false; }
	}

test bool matchAnonymous(){
    b = [_] := [1];
    return b;
}

test bool matchAnonymousReturn(){
    return [_] := [1];
}   

test bool matchTypedAnonymous(){
    b = [int _] := [1];
    return b;
}

test bool matchTypedAnonymousReturn(){
    return [int _] := [1];
}

test bool matchVar(){
    b = [_] := [1];
    return b;
}

test bool matchVarReturn(){
    return [_] := [1];
}

test bool matchTypedVar(){
    b = [int _] := [1];
    return b;
}

test bool matchTypedVarReturn(){
    return [int _] := [1];
}

test bool matchAnonymousListVar1(){
    b = [*_] := [1];
    return b;
}
  
test bool matchAnonymousListVar1Return(){
    return [*_] := [1];
}

test bool matchAnonymousListVar2(){
    b = [*_] := [1];
    return b;
}

test bool matchAnonymousListVar2Return(){
    return [*_] := [1];
}

test bool matchTypedAnonymousListVar(){
    b = [*int _] := [1];
    return b;
}

test bool matchTypedAnonymousListVarReturn(){
    return [*int _] := [1];
}

test bool matchListVar1(){
    b = [*_] := [1];
    return b;
}

test bool matchListVar1Return(){
    return [*_] := [1];
}

test bool matchListVar2(){
    b = [*_] := [1];
    return b;
}

test bool matchListVar2Return(){
    return [*_] := [1];
}

test bool matchTypedListVar(){
    b = [*int _] := [1];
    return b;
}

test bool matchTypedListVarReturn(){
    return [*int _] := [1];
}

test bool matchTypedVarAndTrue(){
    ten = 10;
    b = [int _] := [1] && ten > 9;
    return b;
}

test bool matchTypedVarAndFalse(){
    ten = 10;
    b = [int _] := [1] && 9 > ten;
    return !b;
}

test bool matchTypedListVarAnd(){
    ten = 10;
    b = [*int _] := [1] && ten > 9;
    return b;
}

test bool matchTypedListVarAndFalse(){
    ten = 10;
    b = [*int _] := [1] && 9 > ten;
    return !b;
}

test bool compositeAnd() {
    ten = 10;
    b = [*int _, int  _, *int _] := [1,2,3] && ten > 9;
    return b;
}

test bool compositeAndFalse() {
    ten = 10;
    b = [*int _, int  _, *int _] := [1,2,3] && 9 > ten;
    return !b;
}

test bool compositeAndCnt() {
    n = 0;
    ten = 10;
    if( [*int _, int  _, *int _] := [1,2,3] && ten > 9 )  {
        n = n + 1;
        fail;
    }
    return n == 3;
}

test bool compositeAndCommaCnt() {
    n = 0;
    ten = 10;
    if( [*int _, int  _, *int _] := [1,2,3] , ten > 9 )  {
        n = n + 1;
        fail;
    }
    return n == 3;
}

test bool compositeAndBTLast() {
    ten = 10;
    return ten > 9 && [*int _, int  _, *int _] := [1,2,3];
}

test bool compositeAndCntBTLast() {
    n = 0;
    ten = 10;
    if( ten > 9 && [*int _, int  _, *int _] := [1,2,3] )  {
        n = n + 1;
        fail;
    }
    return n == 3;
}

test bool compositeAndBothBT() {
    return [*int _, int  _, *int _] := [1,2,3] && [*int _, int  _, *int _] := [4, 5, 6];
}

test bool compositeAndBothBTCnt() {
    n = 0;
    if( [*int _, int  _, *int _] := [1,2,3] && [*int _, int  _, *int _] := [4, 5, 6] )  {
        n = n + 1;
        fail;
    }
    return n == 9;
}

@ignoreCompiler{Undetermined cause}
test bool compositeOrCntBTLast() {
    n = 0;
    if( int _ := 3 || ([*int _,*int _] := [4,5,6]) )  {
        n = n + 1;
        fail;
    }
    return n == 5;
}   

test bool compositeOrCntBTFirst() {
    n = 0;
    if( [*int _,*int _] := [4,5,6] || int _ := 3)  {
        n = n + 1;
        fail;
    }
    return n == 5;
}    

test bool compositeAndOrCnt() {
    n = 0;
    if( ([*int _,*int _] := [1,2,3] && int _ := 3) || ([*int _,*int _] := [4,5,6] && int _ := 4) )  {
        n = n + 1;
        fail;
    }
    return n == 8;
}

// implies ==>

@ignoreCompiler{Backtracking in arguments of ==>}
test bool compositeImplTrue(){
  ten = 10;
  b = [*int _, int  _, *int _] := [1,2,3] ==> ten > 9;
  return b;
}

@ignoreCompiler{Backtracking in arguments of ==>}
test bool compositeImplFalse(){
  ten = 10;
  b = [*int _, int  _, *int _] := [1,2,3] ==> 9 > ten;
  return !b;
}

@ignoreCompiler{Backtracking in arguments of ==>}
test bool compositeImplBTLast1(){
  ten = 10;
  b = 9 > ten ==> [*int _, int  _, *int _] := [1,2,3];
  return b;
}

@ignoreCompiler{Backtracking in arguments of ==>}
test bool compositeImplBTLast2(){
  ten = 10;
  b = ten > 9 ==> [*int _, int  _, *int _] := [1,2,3];
  return b;
}

@ignoreCompiler{Backtracking in arguments of ==>}
test bool compositeImplVarBothBT(){
  ten = 10;
  b = [*int _, int  _, *int _] := [1,2,3] ==> ([*int _, *int _] := [4,5,6] && int _ := 4);
  return b;
}

@ignoreCompiler{Backtracking in arguments of ==>}
test bool compositeImplCnt() {
    n = 0;
    ten = 10;
    if( [*int _, int  _, *int _] := [1,2,3] ==> ten > 9 )  {
        n = n + 1;
        fail;
    }
    return n == 1;
}

@ignoreCompiler{Backtracking in arguments of ==>}
test bool compositeImplCntBTLast1() {
    ten = 10;
    return ten > 9 ==> [*int _, int  _, *int _] := [1,2,3];
}

@ignoreCompiler{Backtracking in arguments of ==>}
test bool compositeImplCntBTLast2() {
    n = 0;
    ten = 10;
    if( ten > 9 ==> [*int _, int  _, *int _] := [1,2,3] )  {
        n = n + 1;
        fail;
    }
    return n == 3;
}

@ignoreCompiler{Backtracking in arguments of ==>}
test bool compositeImplReturnBothBT() {
    return [*int _, int  _, *int _] := [1,2,3] ==> [*int _, int  _, *int _] := [4, 5, 6];
}

@ignoreCompiler{Backtracking in arguments of ==>}
test bool compositeImplBothBTCnt() {
    n = 0;
    if( [*int _, int  _, *int _] := [1,2,3] ==> [*int _, int  _, *int _] := [4, 5, 6] )  {
        n = n + 1;
        fail;
    }
    return n == 3;
}


// equivalent <==>

@ignoreCompiler{Backtracking in arguments of <==>}
test bool compositeEquivTrue(){
  ten = 10;
  b = [*int _, int  _, *int _] := [1,2,3] <==> ten > 9;
  return b;
}

@ignoreCompiler{Backtracking in arguments of <==>}
test bool compositeEquivFalse(){
  ten = 10;
  b = [*int _, int  _, *int _] := [1,2,3]<==> 9 > ten;
  return !b;
}

@ignoreCompiler{Backtracking in arguments of <==>}
test bool compositeEquivBTLast1(){
  ten = 10;
  b = 9 > ten <==> [*int _, int  _, *int _] := [1,2,3];
  return !b;
}

@ignoreCompiler{Backtracking in arguments of <==>}
test bool compositeEquivBTLast2(){
  ten = 10;
  b = ten > 9 <==> [*int _, int  _, *int _] := [1,2,3];
  return b;
}

@ignoreCompiler{Backtracking in arguments of <==>}
test bool compositeEquivVarBothBT(){
  ten = 10;
  b = [*int _, int  _, *int _] := [1,2,3] <==> ([*int _, *int _] := [4,5,6] && int _ := 4);
  return b;
}

@ignoreCompiler{Backtracking in arguments of <==>}
test bool compositeEquivCnt() {
    n = 0;
    ten = 10;
    if( [*int _, int  _, *int _] := [1,2,3] <==> ten > 9 )  {
        n = n + 1;
        fail;
    }
    return n == 3;
}

@ignoreCompiler{Backtracking in arguments of <==>}
test bool compositeEquivCntBTLast1() {
    ten = 10;
    return ten > 9 <==> [*int _, int  _, *int _] := [1,2,3];
}

@ignoreCompiler{Backtracking in arguments of <==>}
test bool compositeEquivCntBTLast2() {
    n = 0;
    ten = 10;
    if( ten > 9 <==> [*int _, int  _, *int _] := [1,2,3] )  {
        n = n + 1;
        fail;
    }
    return n == 3;
}

@ignoreCompiler{Backtracking in arguments of <==>}
test bool compositeEquivReturnBothBT() {
    return [*int _, int  _, *int _] := [1,2,3] <==> [*int _, int  _, *int _] := [4, 5, 6];
}

@ignoreCompiler{Backtracking in arguments of <==>}
test bool compositeEquivBothBTCnt() {
    n = 0;
    if( [*int _, int  _, *int _] := [1,2,3] <==> [*int _, int  _, *int _] := [4, 5, 6] )  {
        n = n + 1;
        fail;
    }
    return n == 3;
}

////

data AnotherAndData = a();

anno list[int] AnotherAndData@l;

test bool anotherAnd() {
    v = a()[@l = [1,2,3]];
    list[list[int]] res = [];
    if(v@l? && [*int x,*int y] := v@l) {
       res = res + [ x, y ];
       fail;
    }
    return res ==  [[],
  					[1,2,3],
  					[1],
  					[2,3],
  					[1,2],
  					[3],
  					[1,2,3],
  					[]
					];
}