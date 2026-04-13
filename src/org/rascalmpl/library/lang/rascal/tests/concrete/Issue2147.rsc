module lang::rascal::tests::concrete::Issue2147

lexical C = "c"+;

import ParseTree;

test bool issue2147_deepMatch() {
    // these examples are expected
    assert [C] "c" !:= [C]"cc";
    assert /[C] "c" !:= [C]"cc";
    
    // now we upcast to `Tree`
    Tree c = [C]"c";

    // now we get something unusual. This should not match...
    return /c !:= [C]"cc";
}

test bool issue2147_visit() {
    // now we upcast to `Tree`
    Tree c = [C] "c";

    visit ([C] "cc") {
        case c : return false;
    }

    return true;
}