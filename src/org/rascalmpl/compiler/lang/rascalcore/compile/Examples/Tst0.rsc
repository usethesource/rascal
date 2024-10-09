module lang::rascalcore::compile::Examples::Tst0

&T bar(&T() f) = f();

str foo()
    = bar(int() {
        return 0;
    });