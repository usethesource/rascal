module lang::rascalcore::check::TestShared

import util::UUID;


public loc testRoot = uuid()[scheme="memory"];
public loc testModulesRoot = testRoot + "test-modules";

