module  lang::rascal::tests::functionality::CommonKeywordParameterImport2::DiamondTop

data X = x();

test bool Top_x_has_no_left()    = !(x() has left);
test bool Top_x_has_no_leftsq()  = !(x() has leftsq);
test bool Top_x_has_no_right()   = !(x() has right);
test bool Top_x_has_no_rightsq() = !(x() has rightsq);
