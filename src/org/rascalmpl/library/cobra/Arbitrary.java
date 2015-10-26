/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Wietse Venema - wietsevenema@gmail.com - CWI
 *******************************************************************************/
package org.rascalmpl.library.cobra;

import java.util.Calendar;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class Arbitrary {

	private final Random random;
	private final IValueFactory values;

	public Arbitrary(IValueFactory values) {
		this.random = new Random();
		this.values = values;

	}

	// TODO: this is broken!
	public IValue arbDateTime() {
		Calendar cal = Calendar.getInstance();
		int milliOffset = random.nextInt(1000) * (random.nextBoolean() ? -1 : 1);
		cal.roll(Calendar.MILLISECOND, milliOffset);
		int second = random.nextInt(60) * (random.nextBoolean() ? -1 : 1);
		cal.roll(Calendar.SECOND, second);
		int minute = random.nextInt(60) * (random.nextBoolean() ? -1 : 1);
		cal.roll(Calendar.MINUTE, minute);
		int hour = random.nextInt(60) * (random.nextBoolean() ? -1 : 1);
		cal.roll(Calendar.HOUR_OF_DAY, hour);
		int day = random.nextInt(30) * (random.nextBoolean() ? -1 : 1);
		cal.roll(Calendar.DAY_OF_MONTH, day);
		int month = random.nextInt(12) * (random.nextBoolean() ? -1 : 1);
		cal.roll(Calendar.MONTH, month);
		
		// make sure we do not go over the 4 digit year limit, which breaks things
		int year = random.nextInt(5000) * (random.nextBoolean() ? -1 : 1);
		
		// make sure we don't go into negative territory
		if (cal.get(Calendar.YEAR) + year < 1)
			cal.add(Calendar.YEAR, 1);
		else
			cal.add(Calendar.YEAR, year);
		
		return values.datetime(cal.getTimeInMillis());
	}

	public IValue arbInt() {
		return values.integer(random.nextInt());
	}

	/**
	 * Generate random integer between min (inclusive) and max (exclusive).
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public IValue arbInt(IInteger min, IInteger max) {
		return values.integer(arbInt(min.intValue(), max.intValue()));
	}

	private int arbInt(int min, int max) {
		return random.nextInt(max - min) + min;
	}

	public IValue arbReal(IReal min, IReal max) {
		double minD = min.doubleValue();
		double maxD = max.doubleValue();
		return values
				.real((random.nextDouble() * Math.abs(maxD - minD)) + minD);
	}

	public IValue arbString(IInteger length) {
		return values.string(RandomStringUtils.random(length.intValue()));
	}

	public IValue arbStringAlphabetic(IInteger length) {
		return values.string(RandomStringUtils.randomAlphabetic(length
				.intValue()));
	}

	public IValue arbStringAlphanumeric(IInteger length) {
		return values.string(RandomStringUtils.randomAlphanumeric(length
				.intValue()));
	}

	public IValue arbStringAscii(IInteger length) {
		return values.string(RandomStringUtils.randomAscii(length.intValue()));
	}

	public IValue arbStringNumeric(IInteger length) {
		return values
				.string(RandomStringUtils.randomNumeric(length.intValue()));
	}

	public IValue arbStringWhitespace(IInteger length) {
		char[] cs = { (char) Integer.parseInt("0009", 16),
				(char) Integer.parseInt("000A", 16),
				(char) Integer.parseInt("000B", 16),
				(char) Integer.parseInt("000C", 16),
				(char) Integer.parseInt("000D", 16),
				(char) Integer.parseInt("0020", 16),
				(char) Integer.parseInt("0085", 16),
				(char) Integer.parseInt("00A0", 16),
				(char) Integer.parseInt("1680", 16),
				(char) Integer.parseInt("180E", 16),
				(char) Integer.parseInt("2000", 16),
				(char) Integer.parseInt("2001", 16),
				(char) Integer.parseInt("2002", 16),
				(char) Integer.parseInt("2003", 16),
				(char) Integer.parseInt("2004", 16),
				(char) Integer.parseInt("2005", 16),
				(char) Integer.parseInt("2006", 16),
				(char) Integer.parseInt("2007", 16),
				(char) Integer.parseInt("2008", 16),
				(char) Integer.parseInt("2009", 16),
				(char) Integer.parseInt("200A", 16),
				(char) Integer.parseInt("2028", 16),
				(char) Integer.parseInt("2029", 16),
				(char) Integer.parseInt("202F", 16),
				(char) Integer.parseInt("205F", 16),
				(char) Integer.parseInt("3000", 16) };
		return values.string(RandomStringUtils.random(length.intValue(), cs));
	}

}