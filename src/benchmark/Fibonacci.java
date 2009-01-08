package benchmark;

public class Fibonacci {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n = 30;
		double start = System.currentTimeMillis();
		int result = fib(n);
		double used = System.currentTimeMillis() - start;
		
		System.err.println("fib(" + n + ") = " + result + " (" + used + " millis)");
	}
	
	public static int fib(int n)
	{
	   if(n == 0)
	   		return 0;
	   if(n == 1)
	    	return 1;
	   return fib(n - 1) + fib(n - 2);
	}

}
