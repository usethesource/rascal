module Ackermann

public int ack(int m, int n)
{
	if(m == 0)
		return n + 1;
	else if(n == 0)
		return ack(m - 1, 1);
	else
		return ack(m - 1, ack(m, n - 1));
}

public bool testAckermann(){
	return
		(ack(2,5) == 13) &&
		(ack(3,4) == 125);
		//(ack(4,1) == 65533);
}