module lang::rascal::tests::library::util::UUIDTests

import util::UUID;

test bool uniqueness() = uuid() != uuid();
