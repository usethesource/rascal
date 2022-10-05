module lang::rascal::tests::functionality::CommonKeywordParameterImport3::A

data X(int y = 0) = x1();

test bool Ax1_y1() = x1() has y;
test bool Ax1_y2() = !x1().y?;
test bool Ax1_y3() = x1().y == 0;
test bool Ax1_y4() = x1(y=10).y?;
test bool Ax1_y5() = x1(y=10).y == 10;
