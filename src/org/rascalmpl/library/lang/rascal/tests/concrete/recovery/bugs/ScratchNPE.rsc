module lang::rascal::tests::concrete::recovery::bugs::ScratchNPE

import ParseTree;
import util::ParseErrorRecovery;
import Exception;

start syntax Ambiguous
    = "foo"
    | [f] [o] [o]
    ;


void testFoo() {
    bool ambExceptionThrown = false;
    try {
        parse(#Ambiguous, "foo", allowRecovery=true, allowAmbiguity=false);
    } catch Ambiguity(l, nonterminal, sentence): {
        ambExceptionThrown = true;
        assert nonterminal == "Ambiguous";
        assert sentence == "foo";
    }

    assert ambExceptionThrown : "Ambiguity exception should have been thrown";

    Tree t = parse(#Ambiguous, "foo", allowRecovery=true, allowAmbiguity=true);
    assert amb({_,_}) := t;
}