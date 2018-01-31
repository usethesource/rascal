module Coroutines

function COUNTDOWN_IN_RANGE[2,to,from] {
	while(muprim("less_mint_mint", from, to)) {
		yield to;
		to = muprim("subtraction_mint_mint",to,1);
	};
	return from; 
}

function COUNTDOWN_FROM[1,to,coro] {
	coro = create(COUNTDOWN_IN_RANGE,to);
	return coro;
}

function main[1,args,coro,coro1,coro2,count] {
    coro = COUNTDOWN_FROM(10);
	coro1 = init(coro,0);
	coro2 = init(coro,5);
	count = 0;
	while(hasNext(coro1)) {
		count = muprim("addition_mint_mint",count,next(coro1));
		//prim("println",count);
		while(hasNext(coro2)) {
			count = muprim("addition_mint_mint",count,next(coro2));
		};
	};
	return count; // 10 + 9 + 8 + 7 + 6 + 5 + ( 10 + 9 + 8 + 7 + 6 + 5 + 4 + 3 + 2 + 1) == 100
}