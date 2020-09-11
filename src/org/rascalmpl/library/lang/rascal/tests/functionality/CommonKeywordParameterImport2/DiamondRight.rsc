module  lang::rascal::tests::functionality::CommonKeywordParameterImport2::DiamondRight

import  lang::rascal::tests::functionality::CommonKeywordParameterImport2::DiamondTop;

data X(int right = 10, int rightsq = right * right);

test bool Right_Top_x_has_no_left()    = Top_x_has_no_left();
test bool Right_Top_x_has_no_leftsq()  = Top_x_has_no_leftsq();
test bool Right_Top_x_has_no_right()   = Top_x_has_no_right();
test bool Right_Top_x_has_no_rightsq() = Top_x_has_no_rightsq();

test bool Right_x_has_no_left()   = !(x() has left);
test bool Right_x_has_no_leftsq() = !(x() has leftsq);

test bool Right_x_right1() = x() has right;
test bool Right_x_right2() = !x().right?;
test bool Right_x_right3() = x().right == 10;
test bool Right_x_right4() = x(right = 20).right == 20;
//test bool Right_x_right5() = x(right = 20)?;
test bool Right_x_right6() = x(right = 20).right == 20;

test bool Right_x_rightsq1() = x() has rightsq;
test bool Right_x_rightsq2() = !x().rightsq?;
test bool Right_x_rightsq3() = x().rightsq == 100;
test bool Right_x_rightsq4() = !(x(right = 20).rightsq?);
test bool Right_x_rightsq5() = x(right = 20).rightsq == 400;
