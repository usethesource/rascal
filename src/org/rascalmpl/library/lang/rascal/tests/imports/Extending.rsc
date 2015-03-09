module lang::rascal::tests::imports::Extending

extend ParseTree;
extend lang::rascal::tests::imports::Extended;

test bool extending1() {
    return size([0..666]) + f(999) == 1665;
}

