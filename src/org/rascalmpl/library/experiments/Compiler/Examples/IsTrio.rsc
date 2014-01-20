module experiments::Compiler::Examples::IsTrio

public bool isTrio(list[int] L) {
    switch(L) {
        case [*int L1, *int L2, *int L3]:
            if((L1 == L2) && (L2 == L3)) {
                return true;
            } else {
                fail;
            }
        default:
            return false;
    }
}

public bool main(list[value] args) = isTrio([1]) == false;