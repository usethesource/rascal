module lang::rascal::tests::functionality::storedParsers::ExampleModule

import lang::pico::\syntax::Main;
import util::Math;
import ValueIO;

int compute((Expression) `<Expression a> + <Expression b>`) = compute(a) + compute(b);
int compute((Expression) `<Natural c>`) = readTextValueString(#int, "<c>");

test bool testCompute() {
    example = (Expression) `1 + 1`;
    return compute(example) == 2;
}