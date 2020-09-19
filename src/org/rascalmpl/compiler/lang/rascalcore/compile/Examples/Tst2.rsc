module lang::rascalcore::compile::Examples::Tst2

data X = x2();

test bool B_x2_y1() = !(x2() has y || x2() has z);