module  lang::rascal::tests::functionality::KeywordParameterImport1::DiamondRight

import  lang::rascal::tests::functionality::KeywordParameterImport1::DiamondTop;

data X(int right = 10, int rightsq = right * right) = ry(int rightcb = rightsq * right);

test bool Right_ry_right1() = ry() has right;
test bool Right_ry_right2() = !ry().right?;
test bool Right_ry_right3() = ry().right == 10;
test bool Right_ry_right4() = ry(right = 20).right == 20;
@ignoreCompiler{INCOMPATIBILITY: Is defined operator `?` can only be applied to subscript, keyword parameter, field access, field project or get annotation}
test bool Right_ry_right5() = ry(right = 20)?;
test bool Right_ry_right6() = ry(right = 20).right == 20;

test bool Right_ry_rightsq1() = ry() has rightsq;
test bool Right_ry_rightsq2() = !ry().rightsq?;
test bool Right_ry_rightsq3() = ry().rightsq == 100;
test bool Right_ry_rightsq4() = !(ry(right = 20).rightsq?);
test bool Right_ry_rightsq5() = ry(right = 20).rightsq == 400;

test bool Right_ry_rightcb1() = ry() has rightcb;
test bool Right_ry_rightcb2() = !ry().rightcb?;
test bool Right_ry_rightcb3() = ry().rightcb == 1000;
test bool Right_ry_rightcb4() = !(ry(right = 20).rightcb?);
test bool Right_ry_rightcb5() = ry(right = 20).rightcb == 8000;

test bool Right_ry_has_no_left() = !(ry() has left);
test bool Right_ry_has_no_leftsq() = !(ry() has leftsq);
test bool Right_ry_has_no_leftcb() = !(ry() has leftcb);
