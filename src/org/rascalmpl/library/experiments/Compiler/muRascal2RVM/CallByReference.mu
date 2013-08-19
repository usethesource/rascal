module call_by_reference

function COUNTDOWN[1,2,n,r] {
	while(prim("greater_num_num", n, 0)) {
		&r = prim("addition_num_num", &r, prim("product_num_num", &r, 2));
		yield n;
		n = prim("subtraction_num_num",n,1);
	};
	return 0; 
}

function main[2,1,args,coro,count] {
    coro = create(COUNTDOWN,5);
    count = 0;
	coro = init(coro, ref count);
	while(hasNext(coro)) {    
		count = prim("addition_num_num",next(coro),count);
		//prim("println",count);
	};
	return count; 
	// 0 + 2*0 + 5 + 2*5 + 4 + 2*19 + 3 + 2*60 + 2 + 2*182 + 1 + 0 = 547
}