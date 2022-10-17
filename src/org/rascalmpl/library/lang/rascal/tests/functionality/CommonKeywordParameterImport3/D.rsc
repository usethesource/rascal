module lang::rascal::tests::functionality::CommonKeywordParameterImport3::D

import lang::rascal::tests::functionality::CommonKeywordParameterImport3::A;
import lang::rascal::tests::functionality::CommonKeywordParameterImport3::C; 

test bool Dx1_y1() = x1() has y;
test bool Dx1_y2() = !x1().y?;
test bool Dx1_y3() = x1().y == 0;
test bool Dx1_y4() = x1(y=10).y?;
test bool Dx1_y5() = x1(y=10).y == 10;

test bool Dx1_z1() = x1() has z;
test bool Dx1_z2() = !x1().z?;
test bool Dx1_z3() = x1().z == "abc";
test bool Dx1_z4() = x1(z="def").z?;
test bool Dx1_z5() = x1(z="def").z == "def";

test bool normal() = normalX().y == 0;
test bool extended() = extendedX().y == 0; 
