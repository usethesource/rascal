/*******************************************************************************
* Copyright (c) 2011 Centrum Wiskunde en Informatica (CWI)
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Davy Landman - wrote and imported functions for calculating various math operations
*******************************************************************************/

package org.rascalmpl.value.impl.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;



public class BigDecimalCalculations {
	/**
	 *  pi in 1000 decimals places 
	 */
	public static final BigDecimal PI = new BigDecimal(
			"3.141592653589793238462643383279502884197169399375105820974944592307" +
			"81640628620899862803482534211706798214808651328230664709384460955058" +
			"22317253594081284811174502841027019385211055596446229489549303819644" +
			"28810975665933446128475648233786783165271201909145648566923460348610" +
			"45432664821339360726024914127372458700660631558817488152092096282925" +
			"40917153643678925903600113305305488204665213841469519415116094330572" +
			"70365759591953092186117381932611793105118548074462379962749567351885" +
			"75272489122793818301194912983367336244065664308602139494639522473719" +
			"07021798609437027705392171762931767523846748184676694051320005681271" +
			"45263560827785771342757789609173637178721468440901224953430146549585" +
			"37105079227968925892354201995611212902196086403441815981362977477130" +
			"99605187072113499999983729780499510597317328160963185950244594553469" +
			"08302642522308253344685035261931188171010003137838752886587533208381" +
			"42061717766914730359825349042875546873115956286388235378759375195778" +
			"18577805321712268066130019278766111959092164201988") ;
	
    public static final BigDecimal halfPI = PI.divide(new BigDecimal(2));
    public static final BigDecimal twoPI = PI.multiply(new BigDecimal(2));

    /**
	 *  e in 1000 decimals places 
	 */
    public static final BigDecimal E = new BigDecimal(
    		"2.718281828459045235360287471352662497757247093699959574966967627724" +
			"07663035354759457138217852516642742746639193200305992181741359662904" +
			"35729003342952605956307381323286279434907632338298807531952510190115" +
			"73834187930702154089149934884167509244761460668082264800168477411853" +
			"74234544243710753907774499206955170276183860626133138458300075204493" +
			"38265602976067371132007093287091274437470472306969772093101416928368" +
			"19025515108657463772111252389784425056953696770785449969967946864454" +
			"90598793163688923009879312773617821542499922957635148220826989519366" +
			"80331825288693984964651058209392398294887933203625094431173012381970" +
			"68416140397019837679320683282376464804295311802328782509819455815301" +
			"75671736133206981125099618188159304169035159888851934580727386673858" +
			"94228792284998920868058257492796104841984443634632449684875602336248" +
			"27041978623209002160990235304369941849146314093431738143640546253152" +
			"09618369088870701676839642437814059271456354906130310720851038375051" +
			"011574770417189861068739696552126715468895703503540");
	
    /**
     * above 500 (/below -500) the approximation slows down more than the normalisation costs.
     * So we normalize first
     */
    private static final BigDecimal sincosNormalizePoint = BigDecimal.valueOf(500);
    
    private static BigInteger maxBigDecimalPowExp = BigInteger.valueOf(999999999); // maximum exponent for BigDecimal.pow
	
    
	/**
     * Compute the sine of x to a given scale
     * @param x 
     *      the value of x
     * @param 
     *      scale the desired scale of the result
     * @return the result value
     */
    public static BigDecimal sin(BigDecimal x, int scale)
    {
        if (x.signum() == 0)
        	return BigDecimal.ZERO;
        if (x.abs().compareTo(sincosNormalizePoint) >= 0 ) {
	        x = x.remainder(twoPI, new MathContext(scale + 1));
        }
        if (x.signum() == -1)
            return sinTaylor(x.negate(), scale).negate();
        else 
        	return sinTaylor(x, scale);
    }
    
    
    private static BigDecimal calculateConvergenceTolerance(int scale) {
    	return BigDecimal.valueOf(5).movePointLeft(scale + 1);
    }
    
    // code based on arctanTaylor by Ronald Mak  
    private static BigDecimal sinTaylor(BigDecimal x, int scale)
    {
        int     sp1     = scale + 1;
        int     i       = 3;
        boolean addFlag = false;

        BigDecimal power = x;
        BigDecimal result   = x;
        BigDecimal fac = new BigDecimal(2*3);
        BigDecimal term;

        BigDecimal tolerance = calculateConvergenceTolerance(scale);

        // Loop until the approximations converge
        // (two successive approximations are within the tolerance).
        do {
            // x^i
            power = power.multiply(x).multiply(x).setScale(sp1, BigDecimal.ROUND_HALF_EVEN);
            // (x^i)/(i!)
            term = power.divide(fac, sp1,BigDecimal.ROUND_HALF_EVEN);
            // result = result +- (x^i)/(i!)
            result = addFlag ? result.add(term) : result.subtract(term);
            
            // state for next round
            i += 2;
            fac = fac.multiply(BigDecimal.valueOf(i - 1)).multiply(BigDecimal.valueOf(i));
            addFlag = !addFlag;

        } while (term.compareTo(tolerance) > 0);

        return result;
    }
    
    /**
     * Compute the cosine of x to a given scale
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    public static BigDecimal cos(BigDecimal x, int scale)
    {
        if (x.signum() == 0)
        	return BigDecimal.ONE;
        if (x.abs().compareTo(sincosNormalizePoint) >= 0 ) {
	        x = x.remainder(twoPI, new MathContext(scale + 1));
        }
        if (x.signum() == -1)
            return cosTaylor(x.negate(), scale);
        else 
        	return cosTaylor(x, scale);
    }
    
    // code based on arctanTaylor by Ronald Mak 
    // same as sin but without the x starting point and +1 in faculty
    private static BigDecimal cosTaylor(BigDecimal x, int scale)
    {
        int     sp1     = scale + 1;
        int     i       = 2;
        boolean addFlag = false;

        BigDecimal power = BigDecimal.ONE;
        BigDecimal result   = BigDecimal.ONE;
        BigDecimal fac = new BigDecimal(2);
        BigDecimal term;

        BigDecimal tolerance = calculateConvergenceTolerance(scale);

        // Loop until the approximations converge
        // (two successive approximations are within the tolerance).
        do {
            // x^i
            power = power.multiply(x).multiply(x).setScale(sp1, BigDecimal.ROUND_HALF_EVEN);
            // (x^i)/(i!)
            term = power.divide(fac, sp1, BigDecimal.ROUND_HALF_EVEN);
            // result = result +- (x^i)/(i!)
            result = addFlag ? result.add(term) : result.subtract(term);
            
            // prepare state for next loop
            i += 2;
            fac = fac.multiply(BigDecimal.valueOf(i -1)).multiply(BigDecimal.valueOf(i));
            addFlag = !addFlag;

        } while (term.compareTo(tolerance) > 0);
        return result;
    }
    
    
	/**
	 * Compute the tangent of x to a given scale, |x| < pi/2
	 * 
	 * @param x
	 *      the value of x  
	 * @param scale
	 *      the desired scale of the result
	 * @return the result value
	 */
	public static BigDecimal tan(BigDecimal x, int scale) {
		if (x.signum() == 0)
			return BigDecimal.ZERO;
		if (x.abs().compareTo(halfPI) > 0)
			throw new ArithmeticException("x should be between -(pi/2) and (pi/2)");
		// easiest implementation of tan (no need for Bernoulli numbers) but this is slower than the other 2
		return sin(x, scale + 1).divide(cos(x, scale + 1), scale, RoundingMode.HALF_UP);
	}
	
	
	/**
	 * Compute x^exponent to a given scale.
	 * 
	 * @param x
	 *            the value x
	 * @param exponent
	 *            the exponent value
	 * @param scale
	 *            the desired scale of the result
	 * @return the result value
	 */
	private static BigDecimal intPower(BigDecimal x, BigInteger exponent, int scale) {
		boolean negativeExponent = exponent.signum() == -1;

		if (negativeExponent) {
			exponent = exponent.negate();
		}

		if (exponent.equals(BigInteger.ZERO)) {
			return BigDecimal.ONE.setScale(scale);
		}

		MathContext mc = new MathContext(scale, RoundingMode.HALF_EVEN);
		
		if (exponent.equals(BigInteger.ONE)) {
			if (negativeExponent) {
				return BigDecimal.ONE.divide(x, mc);
			}
			return x.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
		}
		
		BigDecimal result = BigDecimal.valueOf(1);

		if (exponent.compareTo(maxBigDecimalPowExp) >= 0) {
			BigDecimal maxExpPow = x.pow(maxBigDecimalPowExp.intValue(), mc);
			while (exponent.compareTo(maxBigDecimalPowExp) >= 0) {
				result = result.multiply(maxExpPow);
				exponent = exponent.subtract(maxBigDecimalPowExp);	
			}
		}
		
		result = result.multiply(x.pow(exponent.intValue(), mc));
		
		if (negativeExponent) {
			return BigDecimal.ONE.divide(result, mc);
		}
		
		return result.setScale(scale, RoundingMode.HALF_EVEN);
	}	
	
	/**
	 * The functions below this line are based on the Numerical implementations of
	 * Java Number Cruncher: The Java Programmer's Guide 
	 *    to Numerical Computing
	 * by Ronald Mak
	 * 
	 * He has shared the code in the book at the following page:
	 * http://authors.phptr.com/mak/downloads.html
	 * 
	 * And has put the source in the public domain:
	 *      "I wrote all these programs strictly as 
	 *      illustrative examples for my book. 
	 *      You're free to use the source code any 
	 *      way you like, but bear in mind that this is 
	 *      NOT fully tested, commercial-quality code. 
	 *      Neither Prentice Hall PTR nor I can be 
	 *      responsible for anything bad that may happen 
	 *      if you use these programs."
	 * 
	 * The only changes were the removal of call to Thread.yield(), switching to
	 *  and formatting improvements
	 */

	/**
	 * Compute the integral root of x to a given scale, x >= 0. Use Newton's
	 * algorithm.
	 * 
	 * @param x
	 *            the value of x
	 * @param index
	 *            the integral root value
	 * @param scale
	 *            the desired scale of the result
	 * @return the result value
	 */
	public static BigDecimal intRoot(BigDecimal x, BigInteger index, int scale) {
		// Check that x >= 0.
		if (x.signum() < 0) {
			throw new ArithmeticException("x < 0");
		}

		int sp1 = scale + 1;
		BigDecimal n = x;
		BigDecimal i = new BigDecimal(index);
		BigDecimal im1 = i.subtract(BigDecimal.ONE);
		BigDecimal tolerance = BigDecimal.valueOf(5).movePointLeft(sp1);
		BigDecimal xPrev;
		BigInteger indexm1 = index.subtract(BigInteger.ONE);

		// The initial approximation is x/index.
		x = x.divide(i, scale, BigDecimal.ROUND_HALF_EVEN);

		// Loop until the approximations converge
		// (two successive approximations are equal after rounding).
		do {
			// x^(index-1)
			BigDecimal xToIm1 = intPower(x, indexm1, sp1 + 1);
			// x^index
			BigDecimal xToI = x.multiply(xToIm1);
			// n + (index-1)*(x^index)
			BigDecimal numerator = n.add(im1.multiply(xToI));
			// (index*(x^(index-1))
			BigDecimal denominator = i.multiply(xToIm1);
			// x = (n + (index-1)*(x^index)) / (index*(x^(index-1)))
			xPrev = x;
			if (denominator.compareTo(BigDecimal.ZERO) == 0) {
				x = BigDecimal.ZERO.setScale(sp1);
			}
			else {
				x = numerator.divide(denominator, sp1, BigDecimal.ROUND_DOWN);
			}

		} while (x.subtract(xPrev).abs().compareTo(tolerance) > 0);

		return x.setScale(scale, RoundingMode.HALF_EVEN);
	}

	/**
	 * Compute e^x to a given scale. Break x into its whole and fraction parts
	 * and compute (e^(1 + fraction/whole))^whole using Taylor's formula.
	 * 
	 * @param x
	 *            the value of x
	 * @param scale
	 *            the desired scale of the result
	 * @return the result value
	 */
	public static BigDecimal exp(BigDecimal x, int scale) {
		// e^0 = 1
		if (x.signum() == 0) {
			return BigDecimal.valueOf(1);
		}
		
		boolean isNegative = x.signum() == -1;
		
		if (isNegative) {
			x = x.negate();
		}
		
		// Compute the whole part of x.
		BigDecimal xWhole = x.setScale(0, BigDecimal.ROUND_DOWN);

		BigDecimal result;
		
		// If there isn't a whole part, compute and return e^x.
		if (xWhole.signum() == 0) {
			result = expTaylor(x, scale);
		}
		else {
			// Compute the fraction part of x.
			BigDecimal xFraction = x.subtract(xWhole);
	
			// z = 1 + fraction/whole
			BigDecimal z = BigDecimal.valueOf(1).add(
					xFraction.divide(xWhole, scale, BigDecimal.ROUND_HALF_EVEN));
	
			// t = e^z
			BigDecimal t = expTaylor(z, scale);
	
			result = intPower(t, xWhole.toBigInteger(), scale);
		}
		
		if (isNegative) {
			return BigDecimal.ONE.divide(result, new MathContext(scale, RoundingMode.HALF_EVEN));
		}
		
		return result;
	}

	/**
	 * Compute e^x to a given scale by the Taylor series.
	 * 
	 * @param x
	 *            the value of x
	 * @param scale
	 *            the desired scale of the result
	 * @return the result value
	 */
	private static BigDecimal expTaylor(BigDecimal x, int scale) {
		BigDecimal factorial = BigDecimal.valueOf(1);
		BigDecimal xPower = x;
		BigDecimal sumPrev;

		// 1 + x
		BigDecimal sum = x.add(BigDecimal.valueOf(1));
		// Loop until the sums converge
		// (two successive sums are equal after rounding).
		int i = 2;
		do {
			// x^i
			xPower = xPower.multiply(x).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
			// i!
			factorial = factorial.multiply(BigDecimal.valueOf(i));
			// x^i/i!
			BigDecimal term = xPower.divide(factorial, scale, BigDecimal.ROUND_HALF_EVEN);
			// sum = sum + x^i/i!
			sumPrev = sum;
			sum = sum.add(term);

			++i;
		} while (sum.compareTo(sumPrev) != 0);

		return sum;
	}

	/**
	 * Compute the natural logarithm of x to a given scale, x > 0.
	 * @param x
	 * 		the value of x
	 *  @param scale
	 *  	the desired scale of the result
	 *  @return
	 *  	the natural logarithm of x
	 */
	public static BigDecimal ln(BigDecimal x, int scale) {
		// Check that x > 0.
		if (x.signum() <= 0) {
			throw new ArithmeticException("x <= 0");
		}
		// The number of digits to the left of the decimal point.
		int magnitude = x.toString().length() - x.scale() - 1;
		if (magnitude < 3) {
			return lnNewton(x, scale);
		}
		// Compute magnitude*ln(x^(1/magnitude)).
		else {
			// x^(1/magnitude)
			BigDecimal root = intRoot(x, BigInteger.valueOf(magnitude), scale);
			// ln(x^(1/magnitude))
			BigDecimal lnRoot = lnNewton(root, scale);
			// magnitude*ln(x^(1/magnitude))
			return BigDecimal.valueOf(magnitude).multiply(lnRoot)
					.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
		}
	}

	/**
	 * Compute the natural logarithm of x to a given scale, x > 0. Use Newton's
	 * algorithm.
	 */
	private static BigDecimal lnNewton(BigDecimal x, int scale) {
		int sp1 = scale + 1;
		BigDecimal n = x;
		BigDecimal term;

		// Convergence tolerance = 5*(10^-(scale+1))
		BigDecimal tolerance = BigDecimal.valueOf(5).movePointLeft(sp1);
		// Loop until the approximations converge
		// (two successive approximations are within the tolerance).
		do {
			// e^x
			BigDecimal eToX = exp(x, sp1);
			// (e^x - n)/e^x
			if (eToX.compareTo(BigDecimal.ZERO) == 0) {
				break;
			}
			term = eToX.subtract(n).divide(eToX, sp1, BigDecimal.ROUND_DOWN);
			// x - (e^x - n)/e^x
			x = x.subtract(term);

		} while (term.compareTo(tolerance) > 0);

		return x.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * Compute the square root of x to a given scale, x >= 0. Use Newton's
	 * algorithm.
	 * 
	 * @param x
	 *            the value of x
	 * @param scale
	 *            the desired scale of the result
	 * @return the result value
	 */
	public static BigDecimal sqrt(BigDecimal x, int scale) {
		// Check that x >= 0.
		if (x.signum() < 0) {
			throw new ArithmeticException("x < 0");
		}
		if (x.signum()==0) {
			return BigDecimal.ZERO.setScale(scale);
		}

		// n = x*(10^(2*scale))
		BigInteger n = x.movePointRight(scale << 1).toBigInteger();

		// The first approximation is the upper half of n.
		int bits = (n.bitLength() + 1) >> 1;
		BigInteger ix = n.shiftRight(bits);
		BigInteger ixPrev;

		// Loop until the approximations converge
		// (two successive approximations are equal after rounding).
		do {
			ixPrev = ix;
			// x = (x + n/x)/2
			ix = ix.add(n.divide(ix)).shiftRight(1);

		} while (ix.compareTo(ixPrev) != 0);

		return new BigDecimal(ix, scale);
	}
	
	public static BigDecimal pow(BigDecimal a, BigDecimal b, int scale) {
		if (a.signum() == -1) {
			throw new ArithmeticException("x < 0");
		}
		if (a.equals(BigDecimal.ZERO)) {
			return BigDecimal.ZERO;
		}
		scale = Math.max(Math.max(a.precision(), b.precision()), scale) + 1;
		MathContext mc = new MathContext(scale, RoundingMode.HALF_UP);
		BigDecimal remainer = b.remainder(BigDecimal.ONE, mc);
		if (remainer.equals(BigDecimal.ZERO)) {
			return a.pow(b.intValue(), mc);
		}
		// else we have to do the more expansive route:
		// a^b=exp(b*ln(a)) 
		return exp(b.multiply(ln(a, scale), mc), scale).setScale(scale - 1, RoundingMode.HALF_EVEN);
	}

}
