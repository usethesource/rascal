module lang::rascalcore::compile::Examples::Tst1


test bool transLTESame(&Same <: node x, &Same <: node y, &Same <: node z) = (x <= y && y <= z) ==> x <= z;
