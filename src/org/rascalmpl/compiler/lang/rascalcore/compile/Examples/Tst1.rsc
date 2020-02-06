module lang::rascalcore::compile::Examples::Tst1

test bool interpolateIfThenElse() = "abc <if (1 > 0) {> GT <} else {> LT <}> cde" == "abc  GT  cde"; 