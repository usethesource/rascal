package benchmark.Factorial;

import java.math.BigInteger;

public class Factorial {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 12;
		String nAsString = new Integer(n).toString();
		int runs = 10000;
		int result = 0;
		BigInteger bigResult = BigInteger.ZERO;
		
		double start1 = System.currentTimeMillis();
		for(int i = 0; i < runs; i++){
			result = fac(n);
		}
		double end1 = System.currentTimeMillis();
		for(int i = 0; i < runs; i++){
			bigResult = BigFac(new BigInteger(nAsString));
		}
		double end2 = System.currentTimeMillis();
		
		System.err.println("fac(" + n + ")    = " + result + " (" + (end1 - start1) + " millis)");
		System.err.println("BigFac(" + n + ") = " + bigResult + " (" + (end2 - end1) + " millis)");
	}
	
	public static int fac(int n)
	{
	   if(n <= 1)
	   		return 1;
	   return n * fac(n - 1);
	}
	
	public static BigInteger BigFac(BigInteger n)
	{
	   if(n.compareTo(BigInteger.ONE) <= 0)
	   		return BigInteger.ONE;
	   return n.multiply(BigFac(n.subtract(BigInteger.ONE)));
	}

}
