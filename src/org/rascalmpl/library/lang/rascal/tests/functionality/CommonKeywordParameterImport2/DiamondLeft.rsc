module lang::rascal::tests::functionality::CommonKeywordParameterImport2::DiamondLeft

import lang::rascal::tests::functionality::CommonKeywordParameterImport2::DiamondTop;

data X(int left = 10, int leftsq = left * left); 

test bool Left_Top_x_has_no_left()    = Top_x_has_no_left();
test bool Left_Top_x_has_no_leftsq()  = Top_x_has_no_leftsq();
test bool Left_Top_x_has_no_right()   = Top_x_has_no_right();
test bool Left_Top_x_has_no_rightsq() = Top_x_has_no_rightsq();

test bool Left_x_has_no_right()   = !(x() has right);
test bool Left_x_has_no_rightsq() = !(x() has rightsq);

test bool Left_x_left1() = x() has left;
test bool Left_x_left2() = !x().left?;
test bool Left_x_left3() = x().left == 10;
test bool Left_x_left4() = x(left = 20).left == 20;
@ignoreCompiler{
INCOMPATIBILITY: Is defined operator `?` can only be applied to subscript, keyword parameter, field access, field project or get annotation
}
test bool Left_x_left5() = x(left = 20)?;
test bool Left_x_left6() = x(left = 20).left == 20;

test bool Left_x_leftsq1() = x() has leftsq;
test bool Left_x_leftsq2() = !x().leftsq?;
test bool Left_x_leftsq3() = x().leftsq == 100;
test bool Left_x_leftsq4() = !(x(left = 20).leftsq?);
test bool Left_x_leftsq5() = x(left = 20).leftsq == 400;
