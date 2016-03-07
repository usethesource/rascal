module lang::rascal::tests::basic::modules:: D

import lang::rascal::tests::basic::modules::A;
import lang::rascal::tests::basic::modules::C; 

test bool normal() = normalX().y == 0;
test bool extended() = extendedX().y == 0; 