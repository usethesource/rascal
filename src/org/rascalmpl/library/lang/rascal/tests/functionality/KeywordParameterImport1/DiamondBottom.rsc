module  lang::rascal::tests::functionality::KeywordParameterImport1::DiamondBottom

import lang::rascal::tests::functionality::KeywordParameterImport1::DiamondLeft;
import lang::rascal::tests::functionality::KeywordParameterImport1::DiamondRight;
import lang::rascal::tests::functionality::KeywordParameterImport1::DiamondTop;

test bool Bottom_ly_left1() = ly() has left;
test bool Bottom_ly_left2() = !ly().left?;
test bool Bottom_ly_left3() = ly().left == 10;
test bool Bottom_ly_left4() = ly(left = 20).left == 20;
@ignoreCompiler{
INCOMPATIBILITY: Is defined operator `?` can only be applied to subscript, keyword parameter, field access, field project or get annotation
}
test bool Bottom_ly_left5() = ly(left = 20)?;
test bool Bottom_ly_left6() = ly(left = 20).left == 20;

test bool Bottom_ly_leftsq1() = ly() has leftsq;
test bool Bottom_ly_leftsq2() = !ly().leftsq?;
test bool Bottom_ly_leftsq3() = ly().leftsq == 100;
test bool Bottom_ly_leftsq4() = !(ly(left = 20).leftsq?);
test bool Bottom_ly_leftsq5() = ly(left = 20).leftsq == 400;

test bool Bottom_ly_leftcb1() = ly() has leftcb;
test bool Bottom_ly_leftcb2() = !ly().leftcb?;
test bool Bottom_ly_leftcb3() = ly().leftcb == 1000;
test bool Bottom_ly_leftcb4() = !(ly(left = 20).leftcb?);
test bool Bottom_ly_leftcb5() = ly(left = 20).leftcb == 8000;

test bool Bottom_ry_right1() = ry() has right;
test bool Bottom_ry_right2() = !ry().right?;
test bool Bottom_ry_right3() = ry().right == 10;
test bool Bottom_ry_right4() = ry(right = 20).right == 20;
@ignoreCompiler{
INCOMPATIBILITY: Is defined operator `?` can only be applied to subscript, keyword parameter, field access, field project or get annotation
}
test bool Bottom_ry_right5() = ry(right = 20)?;
test bool Bottom_ry_right6() = ry(right = 20).right == 20;

test bool Bottom_ry_rightsq1() = ry() has rightsq;
test bool Bottom_ry_rightsq2() = !ry().rightsq?;
test bool Bottom_ry_rightsq3() = ry().rightsq == 100;
test bool Bottom_ry_rightsq4() = !(ry(right = 20).rightsq?);
test bool Bottom_ry_rightsq5() = ry(right = 20).rightsq == 400;

test bool Bottom_ry_rightcb1() = ry() has rightcb;
test bool Bottom_ry_rightcb2() = !ry().rightcb?;
test bool Bottom_ry_rightcb3() = ry().rightcb == 1000;
test bool Bottom_ry_rightcb4() = !(ry(right = 20).rightcb?);
test bool Bottom_ry_rightcb5() = ry(right = 20).rightcb == 8000;

test bool Bottom_Left_ly_has_no_right() = Left_ly_has_no_right();
test bool Bottom_Left_ly_has_no_rightsq() = Left_ly_has_no_rightsq();
test bool Bottom_Left_ly_has_no_rightcb() = Left_ly_has_no_rightcb();

test bool Bottom_Right_ry_has_no_left() = Right_ry_has_no_left();
test bool Bottom_Right_ry_has_no_leftsq() = Right_ry_has_no_leftsq();
test bool Bottom_Right_ry_has_no_leftcb() = Right_ry_has_no_leftcb();
