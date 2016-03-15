module  lang::rascal::tests::basic::modules::DiamondBottom

import lang::rascal::tests::basic::modules::DiamondLeft;
import lang::rascal::tests::basic::modules::DiamondRight;
import lang::rascal::tests::basic::modules::DiamondTop;

test bool diamondTest() = x().left == x().right; 