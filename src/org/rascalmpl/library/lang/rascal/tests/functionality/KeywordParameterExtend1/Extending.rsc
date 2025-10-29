module lang::rascal::tests::functionality::KeywordParameterExtend1::Extending

import lang::rascal::tests::functionality::KeywordParameterExtend1::Extended;

data Vis = publicVis();

data DefInfo(Vis vis = publicVis());

// issue 2497
bool extendedCommonKeyParamNotOverwritten()
    = noDefInfo().x == 42;

// issue 2497
bool extendingCommonKeyParamNotOverwritten()
    = noDefInfo().vis == publicVis();


