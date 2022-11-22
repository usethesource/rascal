module lang::rascal::tests::functionality::CommonKeywordParameterImport3::Tests

import lang::rascal::tests::functionality::CommonKeywordParameterImport3::D;

// https://github.com/cwi-swat/rascal/issues/933
test bool keywordParametersLost933() = normal() && extended();
