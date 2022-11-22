@license{
  Copyright (c) 2009-2015 CWI
 All rights reserved. This program and the accompanying materials
 are made available under the terms of the Eclipse License v1.0
 which accompanies this distribution, and is available at
 http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl - CWI}
module lang::rascal::tests::library::Math

import util::Math;

real eps = 0.000001;

// abs
  
test bool abs1() = abs(0) == 0;
test bool abs2() = abs(0r) == 0r;
test bool abs3() = abs(-1) == 1;
test bool abs4() = abs(-1r1) == 1r1;
test bool abs5() = abs(1) == 1;
test bool abs6() = abs(1.5) == 1.5;
test bool abs7() = abs(3r2) == 3r2;
test bool abs8() = abs(-1.5) == 1.5;
test bool abs9() = abs(-3r2) == 3r2;
  
// arbInt
  
test bool absInt1() {int N = arbInt(10); return (N >= 0) && (N < 10);}
test bool absInt2() {int _ = arbInt(); return true;}
        
// arbReal
  
test bool arbReal1() {real D = arbReal(); return (D >= 0.0) && (D <= 1.0);}
 
// arbRat
 
test bool arbRat1() {rat R = arbRat(10, 100); return (R >= 0) && (R <= 10);}

// ceil

test bool ceil1() = ceil(-3) == -3;
test bool ceil2() = ceil(0) == 0;
test bool ceil3() = ceil(3) == 3;

test bool ceil4() = ceil(-3.3) == -3;
test bool ceil5() = ceil(-3.0) == -3;
test bool ceil6() = ceil(-0.001) == 0;
test bool ceil7() = ceil(0.0) == 0;
test bool ceil8() = ceil(0.001) == 1;
test bool ceil9() = ceil(3.0) == 3.0;
test bool ceil10() = ceil(3.3) == 4;

// cos

test bool cosTest1() = abs(cos(0) - 1) <  eps;

test bool cosTest2() = abs(cos(PI()/2)) < eps;

test bool cosTest3() = abs(cos(PI())+1) < eps;

test bool cosTest4() = abs(cos(3*PI()/2)) < eps;

test bool cosTest5() = abs(cos(2*PI())-1) < eps;

// denominator
	
test bool denominatorTest1() = denominator(2r3) == 3;

test bool denominatorTest2() = denominator(4r6) == 3;

test bool denominatorTest3() = denominator(-2r3) == 3;
 
// E   
 
test bool ETest() = E() > 2.7 && E() < 2.8;

// exp

test bool ExpTest1() = abs(exp(0) - 1) < eps;

test bool ExpTest2() = abs(exp(1) - E()) < eps;

test bool ExpTest3() = abs(exp(2) - E() * E()) < eps;

// floor

test bool floor1() = floor(-3) == -3;

test bool floor2() = floor(0) == 0;

test bool floor3() = floor(3) == 3;
        
test bool floor4() = floor(0.0) == 0;

test bool floor5() = floor(1.0) == 1;

test bool floor6() = floor(1.1) == 1;

test bool floor7() = floor(1.5) == 1;

test bool floor8() = floor(1.9) == 1;

test bool floor9() = floor(-1.0) == -1;

test bool floor10() = floor(-1.1) == -2;

test bool floor11() = floor(-1.5) == -2;

test bool floor12() = floor(-1.9) == -2;

// ln

test bool lnTest1() {real D = ln(exp(2)); return abs(D - 2) < 0.000001;}

test bool lnTest2() {real D = ln(exp(3.5)); return abs(D - 3.5) < 0.000001;}

// log

test bool logTest1() {real D = log(9,3); return abs(D - 2) < 0.000001;}

test bool logTest2() {real D = log(81,9); return abs(D - 2) < 0.000001;}

test bool logTest3() {real D = log(343,7); return abs(D - 3) < 0.000001;}

// log10

test bool log10Test1() {real D = log10(10); return abs(D - 1) < 0.000001;}

test bool log10Test2() {real D = log10(100); return abs(D - 2) < 0.000001;}

test bool log10Test3() {real D = log10(pow(10,5)); return abs(D - 5) < 0.000001;}

// log2

test bool log2Test1() {real D = log2(4); return abs(D - 2) < 0.000001;}

test bool log2Test2() {real D = log2(16); return abs(D - 4) < 0.000001;}

// max
  
test bool max1() = max(3, 10) == 10;

test bool max2() = max(10, 10) == 10;

test bool max3() = max(2r3, 2r4) == 2r3;

test bool max4() = max(2r3, 2r3) == 2r3;

test bool max5() = max(-2r3, 2r4) == 2r4;

test bool max6() = max(3.0, 10.0) == 10.0;

test bool max7() = max(10.0, 10.0) == 10.0;
        
test bool max8() = max(3.5, 10) == 10;

test bool max9() = max(3, 10.5) == 10.5;
  
// min
  
test bool min1() = min(3, 10) == 3;

test bool min2() = min(10, 10) == 10;

test bool min3() = min(2r3, 2r4) == 2r4;

test bool min4() = min(2r3, 2r3) == 2r3;

test bool min5() = min(-2r3, 2r4) == -2r3;
     
test bool min6() = min(3.0, 10.0) == 3.0;

test bool min7() = min(3.0, 10.0) == 3.0;

test bool min8() = min(10.0, 10.0) == 10.0;
        
test bool min9() = min(3.5, 10) == 3.5;

test bool min10() = min(3, 10.5) == 3;

// numerator

test bool numerator1() = numerator(2r3)== 2;

test bool numerator2() = numerator(4r6)== 2;

test bool numerator3() = numerator(-2r3)== -2;

test bool numerator4() = numerator(-4r6)== -2;

// nroot

test bool nroot1() {real D = nroot(10,1); return abs(D - 10)     < 0.000001;}

test bool nroot2() {real D = nroot(10,2); return abs(D*D - 10)   < 0.000001;}

test bool nroot3() {real D = nroot(10,3); return abs(D*D*D - 10) < 0.000001;}

// PI

test bool PI1() = PI() > 3.14;

test bool PI2() = PI() < 3.15;

// pow

test bool pow1() {real D = pow(7,0); return abs(D - 1)      < 0.000001;}

test bool pow2() {real D = pow(7,1); return abs(D - 7)      < 0.000001;}

test bool pow3() {real D = pow(7,2); return abs(D - 7*7)    < 0.000001;}

test bool pow4() {real D = pow(7,3); return abs(D - 7*7*7)  < 0.000001;}

// remainder

test bool remainder1() = remainder(2r3)== 2;

test bool remainder2() = remainder(3r2)== 1;

test bool remainder3() = remainder(4r2)== 0;

test bool remainder4() = remainder(-2r3)== -2;

// sin

test bool sin1() {real D = sin(0);        return abs(D)     < 0.000001;}
test bool sin2() {real D = sin(PI()/2);   return abs(D - 1) < 0.000001;}
test bool sin3() {real D = sin(PI());     return abs(D)     < 0.000001;}
test bool sin4() {real D = sin(3*PI()/2); return abs(D + 1) < 0.000001;}
test bool sin5() {real D = sin(2*PI());   return abs(D)     < 0.000001;}


// sqrt

//TODO: handling 0 gracefully 
//test bool sqrt1 () {real D = sqrt(0); return abs(D)     < 0.000001;}

test bool sqrt1 () {real D = sqrt(1); return abs(D - 1) < 0.000001;}

test bool sqrt2 () {real D = sqrt(2); return abs(D*D - 2) < 0.000001;}

// tan

// TODO: arg < -pi/2 or > pi/2
test bool tan1 () {real D = tan(0);        return abs(D)     < 0.000001;}

test bool tan2 () {real D = tan(PI()/4);   return abs(D - 1) < 0.000001;}

test bool tan3 () {real D = tan(-PI()/4);  return abs(D + 1) < 0.000001;}


// toInt
  
test bool toInt1() = toInt(3) == 3;

test bool toInt2() = toInt(3.14) == 3;

test bool toInt3() = toInt(3r2) == 1;

test bool toInt4() = toInt(4r2) == 2;
  
// toReal
  
test bool toReal1() =  toReal(3) == 3.0;

test bool toReal2() = toReal(3.14) == 3.14;

// toString
  
test bool testToString7() = toString(314) == "314";

test bool testToString8() = toString(3.14) == "3.14";

test bool testToString9() = toString(4r8) == "1r2";
  
  
