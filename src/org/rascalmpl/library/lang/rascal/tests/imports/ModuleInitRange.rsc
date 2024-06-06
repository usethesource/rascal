module lang::rascal::tests::imports::ModuleInitRange

list[int] ints = [0 .. 10];

test bool rangeInit() = ints == [0 .. 10];
