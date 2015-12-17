module lang::rascal::tests::basic::TestsForTests

@expected{IO}
test bool testExpected() {
  throw IO("this should be expected");
}

