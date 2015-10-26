package org.rascalmpl.value;

import java.net.URISyntaxException;

import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.INumber;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.TypeFactory;

import junit.framework.TestCase;

abstract public class BaseTestBasicValues extends TestCase {
	protected IValueFactory vf;
	protected TypeFactory tf = TypeFactory.getInstance();

	// TODO add more test cases
	protected void setUp(IValueFactory factory) throws Exception {
		super.setUp();
		vf = factory;
	}
	
	protected void assertEqual(IValue l, IValue r) {
		assertTrue("Expected " + l + " got " + r, l.isEqual(r));
	}

	public void testRationalToReal() {
		assertTrue(vf.rational(1, 4).toReal(3).isEqual(vf.real(0.25)));
	}
	
	public void testStringRepresentation() {
		assertTrue(vf.string("\uD83C\uDF5D").isEqual(vf.string("🍝")));
		assertTrue(vf.string(new String(Character.toChars(0x1F35D))).isEqual(vf.string("🍝")));
	}
	
	public void testStringLength() {
		assertTrue(vf.string("\uD83C\uDF5D").length() == 1);
		assertTrue(vf.string("\uD83C\uDF5D\uD83C\uDF5D").length() == 2);
		assertTrue(vf.string("🍝").length() == 1);
		assertTrue(vf.string("🍝🍝").length() == 2);
		assertTrue(vf.string("é").length() == 1);
		assertTrue(vf.string("").length() == 0);
	}
	
	public void testStringReverse() {
		assertTrue(vf.string("").reverse().isEqual(vf.string("")));
		assertTrue(vf.string("🍝").reverse().isEqual(vf.string("🍝")));
		assertTrue(vf.string("🍝🍝").reverse().isEqual(vf.string("🍝🍝")));
		assertTrue(vf.string("🍝x🍝").reverse().isEqual(vf.string("🍝x🍝")));
		assertTrue(vf.string("🍝🍞").reverse().getValue().equals("🍞🍝"));
	}
	
	public void testStringSubString() {
		assertTrue(vf.string("").substring(0,0).isEqual(vf.string("")));
		assertTrue(vf.string("🍝").substring(0,1).isEqual(vf.string("🍝")));
		assertTrue(vf.string("🍝🍝").substring(0,1).isEqual(vf.string("🍝")));
		assertTrue(vf.string("🍝x🍝").substring(1,2).isEqual(vf.string("x")));
		assertTrue(vf.string("🍝x🍝").substring(1,3).isEqual(vf.string("x🍝")));
	}
	
	public void testStringCharAt() {
		assertTrue(vf.string("🍝").charAt(0) == 0x1F35D);
		assertTrue(vf.string("🍝🍞").charAt(1) == 0x1F35E);
		assertTrue(vf.string("🍝x🍝").charAt(1) == 'x');
		assertTrue(vf.string("🍝x🍞").charAt(2) == 0x1F35E);
	}
	
	public void testStringConcat() {
		assertTrue(vf.string("").concat(vf.string("")).isEqual(vf.string("")));
		assertTrue(vf.string("x").concat(vf.string("y")).isEqual(vf.string("xy")));
		assertTrue(vf.string("🍝").concat(vf.string("y")).isEqual(vf.string("🍝y")));
		assertTrue(vf.string("x").concat(vf.string("🍝")).isEqual(vf.string("x🍝")));
		assertTrue(vf.string("🍝").concat(vf.string("🍝")).isEqual(vf.string("🍝🍝")));
	}
	
	public void testStringReplace() {
		assertTrue(vf.string("").replace(0, 1, 0, vf.string("x")).isEqual(vf.string("x")));
		assertTrue(vf.string("x").replace(0, 1, 0, vf.string("")).isEqual(vf.string("x")));
		assertTrue(vf.string("xy").replace(0, 1, 1, vf.string("p")).isEqual(vf.string("py")));
		assertTrue(vf.string("xy").replace(1, 1, 0, vf.string("p")).isEqual(vf.string("xp")));
		assertTrue(vf.string("xy").replace(0, 1, 1, vf.string("pq")).isEqual(vf.string("pqy")));
		assertTrue(vf.string("xy").replace(1, 1, 0, vf.string("pq")).isEqual(vf.string("xqp")));
		assertTrue(vf.string("xy").replace(0, 1, 0, vf.string("pq")).isEqual(vf.string("pqxy")));
		assertTrue(vf.string("xy").replace(1, 1, 1, vf.string("pq")).isEqual(vf.string("xpqy")));
		
		assertTrue(vf.string("🍝y").replace(0, 1, 1, vf.string("p")).isEqual(vf.string("py")));
		assertTrue(vf.string("🍝y").replace(1, 1, 0, vf.string("p")).isEqual(vf.string("🍝p")));	
		assertTrue(vf.string("xy").replace(0, 1, 1, vf.string("🍝")).isEqual(vf.string("🍝y")));
		assertTrue(vf.string("").replace(0, 1, 0, vf.string("🍝")).isEqual(vf.string("🍝")));
		assertTrue(vf.string("🍝").replace(0, 1, 0, vf.string("")).isEqual(vf.string("🍝")));
		assertTrue(vf.string("🍝y").replace(0, 1, 1, vf.string("p")).isEqual(vf.string("py")));
		assertTrue(vf.string("🍝y").replace(1, 1, 0, vf.string("p")).isEqual(vf.string("🍝p")));
		assertTrue(vf.string("x🍝").replace(0, 1, 1, vf.string("p")).isEqual(vf.string("p🍝")));
		assertTrue(vf.string("x🍝").replace(1, 1, 0, vf.string("p")).isEqual(vf.string("xp")));
		assertTrue(vf.string("🍝y").replace(0, 1, 1, vf.string("p🍝")).isEqual(vf.string("p🍝y")));
		assertTrue(vf.string("🍝y").replace(1, 1, 0, vf.string("p🍝")).isEqual(vf.string("🍝🍝p")));
		assertTrue(vf.string("🍝y").replace(0, 1, 0, vf.string("🍝q")).isEqual(vf.string("🍝q🍝y")));
		assertTrue(vf.string("x🍝").replace(1, 1, 1, vf.string("🍝q")).isEqual(vf.string("x🍝q🍝")));
		assertTrue(vf.string("🍝y🍝").replace(1, 1, 2, vf.string("🍝")).isEqual(vf.string("🍝🍝🍝")));
	}
	
	
	
	public void testIntAddition() {
		assertTrue(vf.integer(1).add(vf.integer(1)).isEqual(vf.integer(2)));
	}
	
	public void testReal() {
		assertTrue(vf.real("1.5").floor().isEqual(vf.real("1")));
		assertTrue(vf.real("1.5").round().isEqual(vf.real("2")));
	}
	
	public void testNumberSubTypes() {
		assertTrue(tf.integerType().isSubtypeOf(tf.numberType()));
		assertFalse(tf.numberType().isSubtypeOf(tf.integerType()));
		assertTrue(tf.realType().isSubtypeOf(tf.numberType()));
		assertFalse(tf.numberType().isSubtypeOf(tf.realType()));
		assertTrue(tf.rationalType().isSubtypeOf(tf.numberType()));
		assertFalse(tf.numberType().isSubtypeOf(tf.rationalType()));
		
		assertTrue(tf.integerType().lub(tf.realType()).equivalent(tf.numberType()));
		assertTrue(tf.integerType().lub(tf.rationalType()).equivalent(tf.numberType()));
		assertTrue(tf.integerType().lub(tf.numberType()).equivalent(tf.numberType()));
		assertTrue(tf.realType().lub(tf.numberType()).equivalent(tf.numberType()));
		assertTrue(tf.rationalType().lub(tf.integerType()).equivalent(tf.numberType()));
		assertTrue(tf.rationalType().lub(tf.realType()).equivalent(tf.numberType()));
		assertTrue(tf.rationalType().lub(tf.numberType()).equivalent(tf.numberType()));
	}
	
	public void testNumberArithmatic() {
		INumber i1 = vf.integer(1);
		INumber i2 = vf.integer(2);
		INumber r1 = vf.real(1.0);
		INumber r2 = vf.real(2.0);
		INumber q1 = vf.rational(1, 1);
		INumber q2 = vf.rational(2, 1);
		
		assertEqual(i1.add(i2),vf.integer(3));
		assertEqual(i1.add(r2),vf.real(3));
		assertEqual(i1.add(q2),vf.rational(3, 1));
		assertEqual(q1.add(i2),vf.rational(3, 1));
		assertEqual(q1.add(q2),vf.rational(3, 1));
		assertEqual(r1.add(r2),vf.real(3));
		assertEqual(r1.add(i2),vf.real(3));
		assertEqual(r1.add(q2),vf.real(3));
		
		assertEqual(i1.subtract(i2),vf.integer(-1));
		assertEqual(i1.subtract(r2),vf.real(-1));
		assertEqual(r1.subtract(r2),vf.real(-1));
		assertEqual(r1.subtract(i2),vf.real(-1));
		assertEqual(q1.subtract(q2),vf.rational(-1,1));
		assertEqual(q1.subtract(r2),vf.real(-1));
		assertEqual(q1.subtract(i2),vf.rational(-1,1));
		assertEqual(r1.subtract(q2),vf.real(-1));
		
		IInteger i5 =  vf.integer(5);
		assertEqual(i5.divide(i2, 80*80),vf.real(2.5));
		assertEqual(i5.divide(i2.toRational()),vf.rational(5, 2));
		
		assertEqual(vf.integer(0), vf.integer(0).abs());
		assertEqual(vf.rational(0, 1), vf.rational(0, 1).abs());
		assertEqual(vf.real(0), vf.real(0).abs());
	}
	
	
	public void testPreciseRealDivision() {
		IReal e100 = vf.real("1E100");
		IReal maxDiff = vf.real("1E-6300");
		IReal r9 = vf.real("9");
		assertTrue(e100.subtract(e100.divide(r9,80*80).multiply(r9)).lessEqual(maxDiff).getValue());
	}
	
	public void testDateTimeLongConversion() {
		long l = 1156521600000L;
		IDateTime dt = vf.datetime(l);
		assertEqual(dt, vf.datetime(dt.getInstant()));
	}
	
	public void testDateTimeLongConversionWithTimezone() {
		IDateTime dt = vf.datetime(2014, 10, 13, 10, 7, 50, 1, 7, 0);
		assertEqual(dt, vf.datetime(dt.getInstant(), dt.getTimezoneOffsetHours(), dt.getTimezoneOffsetMinutes()));
	}
	
	public void testLocationTop() throws URISyntaxException {
		ISourceLocation l = vf.sourceLocation("tmp","","/file.txt");
		assertTrue(l.top() == l);
		
		ISourceLocation m = vf.sourceLocation(l, 10, 20);
		assertEquals(m.top(), l);
	}
}
