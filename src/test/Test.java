package test;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int a = 9;
		a += (a = 3);									// first example
		System.out.println(a);
		int b = 9;
		b = b + (b = 3);									// second example
		System.out.println(b);
		int[] ar = new int[10];
		int i = 1;
		ar[i] = (i=3);
		System.out.println(ar[1]);

	}

}
