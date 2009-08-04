module demo::Ackermann

import UnitTest;

// Ackermann's function: a standard example of a double recursive function.
// See http://en.wikipedia.org/wiki/Ackermann_function

public int ack(int m, int n)
{
	if(m == 0)
		return n + 1;
	else if(n == 0)
		return ack(m - 1, 1);
	else
		return ack(m - 1, ack(m, n - 1));
}

public bool test(){
    assertEqual(ack(2,5), 13);
    assertEqual(ack(3,4), 125);
    return report();
}