module lang::rascal::tests::functionality::CommonKeywordParameterImport3::C

import lang::rascal::tests::functionality::CommonKeywordParameterImport3::A;
import lang::rascal::tests::functionality::CommonKeywordParameterImport3::B;

data X(str z = "abc");

test bool Cx1_y1() = x1() has y;
test bool Cx1_y2() = !x1().y?;
test bool Cx1_y3() = x1().y == 0;
test bool Cx1_y4() = x1(y=10).y?;
test bool Cx1_y5() = x1(y=10).y == 10;

test bool Cx1_z1() = x1() has z;
test bool Cx1_z2() = !x1().z?;
test bool Cx1_z3() = x1().z == "abc";
@ignoreCompiler{
INCOMPATIBILITY: Is defined operator `?` can only be applied to subscript, keyword parameter, field access, field project or get annotation
}
test bool Cx1_z4() = x1(z="def")?;
test bool Cx1_z5() = x1(z="def").z == "def";

test bool Cx2_y1() = x2() has y;
test bool Cx2_y2() = !x2().y?;
test bool Cx2_y3() = x2().y == 0;
test bool Cx2_y4() = x2(y=10).y?;
test bool Cx2_y5() = x2(y=10).y == 10;

test bool Cx2_z1() = x2() has z;
test bool Cx2_z2() = !x2().z?;
test bool Cx2_z3() = x2().z == "abc";
@ignoreCompiler{
INCOMPATIBILITY: Is defined operator `?` can only be applied to subscript, keyword parameter, field access, field project or get annotation
}
test bool Cx2_z4() = x2(z="def")?;
test bool Cx2_z5() = x2(z="def").z == "def";

X normalX() = x1();
X extendedX() = x2(); 
