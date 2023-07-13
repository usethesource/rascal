module lang::rascal::tests::functionality::KeywordParameterImport1::DiamondLeft

import lang::rascal::tests::functionality::KeywordParameterImport1::DiamondTop;

data X(int left = 10, int leftsq = left * left) = ly(int leftcb = leftsq * left);

test bool Left_ly_left1() = ly() has left;
test bool Left_ly_left2() = !ly().left?;
test bool Left_ly_left3() = ly().left == 10;
test bool Left_ly_left4() = ly(left = 20).left == 20;
@ignoreCompiler{INCOMPATIBILITY: Is defined operator `?` can only be applied to subscript, keyword parameter, field access, field project or get annotation}
test bool Left_ly_left5() = ly(left = 20)?;
test bool Left_ly_left6() = ly(left = 20).left == 20;

test bool Left_ly_leftsq1() = ly() has leftsq;
test bool Left_ly_leftsq2() = !ly().leftsq?;
test bool Left_ly_leftsq3() = ly().leftsq == 100;
test bool Left_ly_leftsq4() = !(ly(left = 20).leftsq?);
test bool Left_ly_leftsq5() = ly(left = 20).leftsq == 400;

test bool Left_ly_left7() = ly() has left;
test bool Left_ly_leftsq6() = ly() has leftsq;

test bool Left_ly_leftcb1() = ly() has leftcb;
test bool Left_ly_leftcb2() = !(ly().leftcb?);
test bool Left_ly_leftcb3() = ly().leftcb == 1000;
test bool Left_ly_leftcb4() = ly(left=20).leftcb == 8000;

test bool Left_ly_has_no_right() = !(ly() has right);
test bool Left_ly_has_no_rightsq() = !(ly() has rightsq);
test bool Left_ly_has_no_rightcb() = !(ly() has rightcb);
