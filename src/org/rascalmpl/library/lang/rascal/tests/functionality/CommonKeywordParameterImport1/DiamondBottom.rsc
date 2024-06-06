module  lang::rascal::tests::functionality::CommonKeywordParameterImport1::DiamondBottom

import lang::rascal::tests::functionality::CommonKeywordParameterImport1::DiamondLeft;
import lang::rascal::tests::functionality::CommonKeywordParameterImport1::DiamondRight;
import lang::rascal::tests::functionality::CommonKeywordParameterImport1::DiamondTop;

test bool diamondTest() = x().left == x().right;  
