@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::checker::ListUtils

import List;

public list[&B] joinList(list[&A] itemList, &B (&A) itemFun, &B itemSep, &B joinUnit) {
	if (size(itemList) >= 2) return [itemFun(head(itemList))] + itemSep + joinList(tail(itemList),itemFun,itemSep,joinUnit);
	else if (size(itemList) >= 1) return [itemFun(head(itemList))];
	else return [joinUnit];	
}

public list[str] joinStrList(list[str] items, str sep) {
    return joinList(items, str(str x) { return x; }, sep, ""); 
}

public list[tuple[&A,&B]] zip(list[&A] alist, list[&B] blist) {
	if (size(alist) != size(blist)) throw "Zip: both lists must have identical sizes";
	n = 0;
	return while (n < size(alist)) { append(<alist[n],blist[n]>); n += 1; };
}
