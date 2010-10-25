module rascal::checker::ListUtils

import List;

public &B joinList(list[&A] itemList, &B (&A) itemFun, &B itemSep, &B joinUnit) {
	if (size(itemList) >= 2) return itemFun(head(itemList)) + itemSep + joinList(tail(itemList),itemFun,itemSep,joinUnit);
	else if (size(itemList) >= 1) return itemFun(head(itemList));
	else return joinUnit;	
}

public list[tuple[&A,&B]] zip(list[&A] alist, list[&B] blist) {
	if (size(alist) != size(blist)) throw "Zip: both lists must have identical sizes";
	n = 0;
	return while (n < size(alist)) { append(<alist[n],blist[n]>); n += 1; };
}