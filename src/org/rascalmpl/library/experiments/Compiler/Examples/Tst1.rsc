module experiments::Compiler::Examples::Tst1
import Type;
test bool comparabilityImpliesEquivalence(value x, value y) = comparable(typeOf(x),typeOf(y)) ==> (eq(x,y) <==> x == y);