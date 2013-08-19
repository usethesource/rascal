module Coroutines

function COUNTDOWN_IN_RANGE[1,2,to,from] {
	while(prim("less_num_num", from, to)) {
		yield to;
		to = prim("subtraction_num_num",to,1);
	};
	return from; 
}

function COUNTDOWN_FROM[2,1,to,coro] {
	coro = create(COUNTDOWN_IN_RANGE,to);
	return coro;
}

function main[3,1,args,coro,coro1,coro2,count] {
    coro = COUNTDOWN_FROM(10);
	coro1 = init(coro,0);
	coro2 = init(coro,5);
	count = 0;
	while(hasNext(coro1)) {
		count = prim("addition_num_num",count,next(coro1));
		//prim("println",count);
		while(hasNext(coro2)) {
			count = prim("addition_num_num",count,next(coro2));
			//prim("println",count);
		};
	};
	return count; // 10 + 9 + 8 + 7 + 6 + 5 + ( 10 + 9 + 8 + 7 + 6 + 5 + 4 + 3 + 2 + 1) == 100
}