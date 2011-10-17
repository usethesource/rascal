module demo::lang::MissGrant::Parse

import demo::lang::MissGrant::MissGrant;
import ParseTree;

public Controller parse(str src, loc origin) = parse(#Controller, src, origin);

public Controller parse(loc file) = parse(#Controller, file);

