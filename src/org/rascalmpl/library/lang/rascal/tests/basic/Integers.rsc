module lang::rascal::tests::basic::Integers

import Exception;

@expected{
ArithmeticException
}
test bool divByZero(num x) = x / 0 == 0;

test bool commAdd(int i, int j) = i + j == j + i;

test bool commMul(int i, int j) = i * j == j * i;

test bool assocAdd(int i, int j, int k) = (i + j) + k == i + (j + k);

test bool assocMul(int i, int j, int k) = (i * j) * k == i * (j * k);

test bool idemZero(int i) = 0 * i == 0;

test bool neutralAddZero(int i) = 0 + i == i;

test bool neutralMulOne(int i) = 1 * i == i;

test bool neutralDivOne(int i) = i / 1 == i;

test bool dualMulDiv(int i, int j) = j != 0 ==> ((i * j) / j == i);

test bool MulDivMod(int i, int j) = j != 0 ==> (((i / j) * j) + (i % j) == i);

test bool dualAddSub(int i, int j) = (i - j) + j == i && (i + j) - j == i;

test bool distMulAdd(int i, int j, int k) = i * (j + k) == i*j + i*k;

test bool lessGreater(int i, int j) = i <= j <==> j >= i;

test bool less(int i, int j) = i < j <==> j > i;

test bool greaterEqual(int i, int j) = i >= j <==> i == j || i > j;

test bool transLess(int i, int j, int k) = (i < j && j < k) ==> i < k;

test bool transEq(int i, int j, int k) = (i == j && j == k) ==> i == k;

test bool reflexEq(int i) = i == i;

test bool commEq(int i, int j) = i == j <==> j == i;

test bool plusMinStable(int a, int b, int c, int d) = (a - b) + (c - d) == (a + c) - (b + d);
