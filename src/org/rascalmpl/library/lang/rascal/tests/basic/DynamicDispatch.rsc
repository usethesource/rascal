module lang::rascal::tests::basic::DynamicDispatch

data D = d();

bool foo(d()) {
  return true;;
}

default bool foo(value n) {
  return false;
}

test bool dispatchOnRuntimeType() {
  value x = d();
  return foo(x) == true;
}
