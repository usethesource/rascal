module lang::rascal::tests::concrete::recovery::bugs::MinimalMultiError

import lang::rascal::tests::concrete::recovery::RecoveryTestSupport;

layout Layout = [\ ]* !>> [\ ];

syntax S = T;

syntax T = AB AB AB End;
syntax AB = 'a' 'b';
syntax End = "$";

test bool multiOk() = checkRecovery(#S, "ababab$", []);

test bool multiOneError() = checkRecovery(#S, "acabab$", ["c"]);

test bool multiTwoError() = checkRecovery(#S, "acacab$", ["c","c"]);
