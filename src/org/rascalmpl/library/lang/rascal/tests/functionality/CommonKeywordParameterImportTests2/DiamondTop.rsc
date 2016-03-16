module  lang::rascal::tests::functionality::CommonKeywordParameterImportTests2::DiamondTop

data X = x();

test bool Top_x_left() = !(x() has left);
test bool Top_x_leftsq() = !(x() has leftsq);
test bool Top_x_right() = !(x() has right);
test bool Top_x_rightsq() = !(x() has rightsq);