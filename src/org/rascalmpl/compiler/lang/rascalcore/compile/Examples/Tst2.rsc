module lang::rascalcore::compile::Examples::Tst2

int f([int x, _]) = x;

int f(list[int] l) {
    throw "`f` requires list with at least one element";
}

value main() = f([]);
