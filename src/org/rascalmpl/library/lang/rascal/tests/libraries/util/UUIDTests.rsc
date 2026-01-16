module lang::rascal::tests::libraries::util::UUIDTests

import util::UUID;

test bool uniqueness() = uuid() != uuid();
