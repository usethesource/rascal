module Mrange

import Set;
import Relation;
import Graph;

set[int] R = range({<1,10>,<2,20>});

public bool check(){
	return R == {10,20};
}
