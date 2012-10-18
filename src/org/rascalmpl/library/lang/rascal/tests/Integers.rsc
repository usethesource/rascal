module lang::rascal::tests::Integers

public test bool commAdd(int i, int j) = i + j == j + i;

public test bool commMul(int i, int j) = i * j == j * i;

public test bool assocAdd(int i, int j, int k) = (i + j) + k == i + (j + k);

public test bool assocMul(int i, int j, int k) = (i * j) * k == i * (j * k);

public test bool idemZero(int i) = 0 * i == 0;

public test bool neutralAddZero(int i) = 0 + i == i;

public test bool neutralMulOne(int i) = 1 * i == i;

public test bool neutralDivOne(int i) = i / 1 == i;

public test bool dualMulDiv(int i, int j) = (i * j) / j == i;

public test bool MulDivMod(int i, int j) = ((i / j) * j) + (i % j) == i;

public test bool dualAddSub(int i, int j) = (i - j) + j == i && (i + j) - j == i;

public test bool distMulAdd(int i, int j, int k) = i * (j + k) == i*j + i*k;

public test bool lessGreater(int i, int j) = i <= j <==> j >= i;

public test bool less(int i, int j) = i < j <==> j > i;

public test bool greaterEqual(int i, int j) = i >= j <==> i == j || i > j;

public test bool transLess(int i, int j, int k) = (i < j && j < k) ==> i < k;

public test bool transEq(int i, int j, int k) = (i == j && j == k) ==> i == k;

public test bool reflexEq(int i) = i == i;

public test bool commEq(int i, int j) = i == j <==> j == i;

public test bool plusMinStable(int a, int b, int c, int d) = (a - b) + (c - d) == (a + c) - (b + d);
