module call_by_reference

function COUNTDOWN[2,n,r] {
	while(muprim("greater_mint_mint", n, 0)) {
		deref r = muprim("addition_mint_mint", deref r, muprim("product_mint_mint", deref r, 2));
		yield n;
		n = muprim("subtraction_mint_mint",n,1);
	};
	return 0; 
}

function main[1,args,coro,count] {
    coro = create(COUNTDOWN,5);
    count = 0;
	coro = init(coro, ref count);
	while(hasNext(coro)) {    
		count = muprim("addition_mint_mint",next(coro),count);
	};
	return count; 
	// 0 + 2*0 + 5 + 2*5 + 4 + 2*19 + 3 + 2*60 + 2 + 2*182 + 1 + 0 = 547
}