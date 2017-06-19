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

import org.rascalmpl.library.cobra.util.RandomUtil;

import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class Arbitrary {

    private final Random random;
    private final IValueFactory values;

    public Arbitrary(IValueFactory values) {
        this.random = new Random();
        this.values = values;

    }

    public static IValue arbDateTime(IValueFactory values, Random random) {
        Calendar cal = Calendar.getInstance();
        try {
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
        catch (IllegalArgumentException e) {
            // this may happen if the generated random time does
            // not exist due to timezone shifting or due to historical
            // calendar standardization changes
            // So, we just try again until we hit a better random date
            return arbDateTime(values, random);
            // of continued failure before we run out of stack are low.
        }
    }


    // TODO: this is broken!
    public IValue arbDateTime() {
        return arbDateTime(values, random);
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
        return values.real((random.nextDouble() * Math.abs(maxD - minD)) + minD);
    }

    public IValue arbString(IInteger length) {
        return values.string(RandomUtil.string(random, length.intValue()));
    }

    public IValue arbStringAlphabetic(IInteger length) {
        return values.string(RandomUtil.stringAlpha(random, length.intValue()));
    }

    public IValue arbStringAlphanumeric(IInteger length) {
        return values.string(RandomUtil.stringAlphaNumeric(random, length.intValue()));
    }

    public IValue arbStringAscii(IInteger length) {
        return values.string(RandomUtil.stringAlpha(random, length.intValue()));
    }

    public IValue arbStringNumeric(IInteger length) {
        return values.string(RandomUtil.stringNumeric(random, length.intValue()));
    }

    public IValue arbStringWhitespace(IInteger length) {
        return values.string(RandomUtil.stringAllKindsOfWhitespace(random, length.intValue()));
    }

}