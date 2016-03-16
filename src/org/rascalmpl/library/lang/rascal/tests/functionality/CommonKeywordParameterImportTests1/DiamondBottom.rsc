module  lang::rascal::tests::functionality::CommonKeywordParameterImportTests1::DiamondBottom

import lang::rascal::tests::functionality::CommonKeywordParameterImportTests1::DiamondLeft;
import lang::rascal::tests::functionality::CommonKeywordParameterImportTests1::DiamondRight;
import lang::rascal::tests::functionality::CommonKeywordParameterImportTests1::DiamondTop;

test bool diamondTest() = x().left == x().right; 