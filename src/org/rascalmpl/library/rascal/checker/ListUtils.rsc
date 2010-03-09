module rascal::checker::ListUtils

import List;

public &B joinList(list[&A] itemList, &B (&A) itemFun, &B itemSep, &B joinUnit) {
	if (size(itemList) >= 2) return itemFun(head(itemList)) + itemSep + joinList(tail(itemList),itemFun,itemSep,joinUnit);
	else if (size(itemList) >= 1) return itemFun(head(itemList));
	else return joinUnit;	
}
