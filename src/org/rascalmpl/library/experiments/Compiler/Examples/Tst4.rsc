module experiments::Compiler::Examples::Tst4


data F = z(int l = 2) | u();

// ? is not the same as has, has returns whether or not in principle (statically) the current dynamic value has the field or could have the field (in case of keyword parameter)
// while ? computes whether or not currently dynamically the field is set (which is always true in case of named parameters of the right constructor and sometimes true for keyword parameters)
// for constructors it is true that ? implies has, but not for nodes.





value main() {
  e = z();
  e.l?=3; // set l to 3 if the field is not set, otherwise leave it
  return e;
}

