module test

int called = 0;

int add(int a, int b) {
  called = called + 1;
  return a + b;
}

int calledAdd() {
  return called;
}