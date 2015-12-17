module lang::rascal::tests::basic::TestsForTests

import Exception;

@expected{IO}
test bool testExpected() {
  throw IO("this should be expected");
}

