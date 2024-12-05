module lang::rascal::tests::concrete::recovery::PrefixSharingRecoveryTest

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;

layout Layout = [\ ]* !>> [\ ];

start syntax Prog = Expr Expr;

syntax Expr = [s] "+" "1"
    | [s] "+" "2";

test bool prefixSharedOk() = checkRecovery(#Prog, "s+2s+1", []);

// When taking prefix sharing into account when determining end matchers in the error recover,
// prefixSharedError1()  and 2 will succeed.
// However, this costs considerable performance for very little gain in practice,
// so for now we have disabled the prefix sharing support in `addPrefixSharedEndMatchers` 
//test bool prefixSharedError1() = checkRecovery(#Prog, "s-1s+1", ["-1"], visualize=false);
test bool prefixSharedError2() = checkRecovery(#Prog, "s-2s+1", ["-2"], visualize=false);
