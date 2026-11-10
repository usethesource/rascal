module lang::rascal::tests::imports::M10

// Functions whose names clash with constructors declared in Imports10,
// but that can never produce a value of that ADT.

void qcons(int _) { }

value qval(int _) = 42;

node qnode(int _) = "qnode"(42);
