/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/

/*******************************************************************************
 * 
 * Warning this file is an experiment to determine the effect of collecting all
 * classes used by the Prelude in a single class. Overall effect seems to be circa 10%
 * reduction of import time.
 * 
 * Do not edit/change this code, but use the original code instead.
 * 
 */
package org.rascalmpl.library;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.CharSetUtils;
import org.apache.commons.lang.WordUtils;
import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.eclipse.imp.pdb.facts.io.BinaryValueReader;
import org.eclipse.imp.pdb.facts.io.BinaryValueWriter;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

public class Prelude {
	private final TypeFactory types ;
	private final IValueFactory values;
	private final Random random;
	
	public Prelude(IValueFactory values){
		super();
		
		this.values = values;
		this.types = TypeFactory.getInstance();
		this.tr = new TypeReifier(values);
		random = new Random();
	}
	
	/*
	 * Boolean
	 */
	
	public IValue arbBool()  // get an arbitrary boolean value.}
	{
	  return values.bool(random.nextInt(2) == 1);
	}
	
	/*
	 * DateTime
	 */
	
	private IValue createNewTimeValue(Calendar jdt2) {
		int hourOffset = jdt2.getTimeZone().getOffset(jdt2.getTimeInMillis())/3600000;
		int minuteOffset = (jdt2.getTimeZone().getOffset(jdt2.getTimeInMillis())/60000)%60;				
		return values.time(jdt2.get(Calendar.HOUR_OF_DAY), jdt2.get(Calendar.MINUTE),
				jdt2.get(Calendar.SECOND), jdt2.get(Calendar.MILLISECOND), 
				hourOffset, minuteOffset);
	}		
	
	public IValue now()
	//@doc{Get the current datetime.}
	{
	   return values.datetime(Calendar.getInstance().getTimeInMillis());
	}

	public IValue createDate(IInteger year, IInteger month, IInteger day) 
	//@doc{Create a new date.}
	{
		return values.date(year.intValue(), month.intValue(), day.intValue());
	}
	
	public IValue createTime(IInteger hour, IInteger minute, IInteger second,
			IInteger millisecond)
	//@doc{Create a new time.}
	{
		return values.time(hour.intValue(), minute.intValue(), second.intValue(), millisecond.intValue());
	}

	public IValue createTime(IInteger hour, IInteger minute, IInteger second,
			IInteger millisecond, IInteger timezoneHourOffset, IInteger timezoneMinuteOffset)
	//@doc{Create a new time with the given numeric timezone offset.}
	{
		return values.time(hour.intValue(), minute.intValue(), second.intValue(),
				millisecond.intValue(), timezoneHourOffset.intValue(), timezoneMinuteOffset.intValue());
	}
	
	public IValue createDateTime(IInteger year, IInteger month, IInteger day, 
			IInteger hour, IInteger minute, IInteger second, IInteger millisecond)
	//@doc{Create a new datetime.}
	{
		return values.datetime(year.intValue(), month.intValue(), day.intValue(), hour.intValue(),
				minute.intValue(), second.intValue(), millisecond.intValue());
	}

	
	public IValue createDateTime(IInteger year, IInteger month, IInteger day,
			IInteger hour, IInteger minute, IInteger second, IInteger millisecond, 
			IInteger timezoneHourOffset, IInteger timezoneMinuteOffset)
	//@doc{Create a new datetime with the given numeric timezone offset.}
	{
		return values.datetime(year.intValue(), month.intValue(), day.intValue(), hour.intValue(),
				minute.intValue(), second.intValue(), millisecond.intValue(), timezoneHourOffset.intValue(),
				timezoneMinuteOffset.intValue());
	}
		
	public IValue joinDateAndTime(IDateTime date, IDateTime time)
	//@doc{Create a new datetime by combining a date and a time.}
	{
		return values.datetime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
				time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute(),
				time.getMillisecondsOfSecond(), time.getTimezoneOffsetHours(), time.getTimezoneOffsetMinutes());
	}

	public IValue splitDateTime(IDateTime dt)
	//@doc{Split an existing datetime into a tuple with the date and the time.}
	{
		return values.tuple(values.date(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth()),
				values.time(dt.getHourOfDay(), dt.getMinuteOfHour(), dt.getSecondOfMinute(),
						dt.getMillisecondsOfSecond(), dt.getTimezoneOffsetHours(), dt.getTimezoneOffsetMinutes()));
	}
	
	
	public IValue incrementYears(IDateTime dt, IInteger n)
	//@doc{Increment the years by a given amount.}
	{
		return incrementDate(dt, Calendar.YEAR, "years", n);	
	}
	
	public IValue incrementMonths(IDateTime dt, IInteger n)
	//@doc{Increment the months by a given amount.}
	{
		return incrementDate(dt, Calendar.MONTH, "months", n);	
	}

	public IValue incrementDays(IDateTime dt, IInteger n)
	//@doc{Increment the days by a given amount.}
	{
		return incrementDate(dt, Calendar.DAY_OF_MONTH, "days", n);	
	}

	private IValue incrementTime(IDateTime dt, int field, String fieldName, IInteger amount) {
		if (!dt.isDate()) {
			long millis = dt.getInstant();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(millis);
			calendar.add(field, amount.intValue());
			if (dt.isTime()) {
				return createNewTimeValue(calendar);
			}
			return values.datetime(calendar.getTimeInMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot increment the " + fieldName + " on a date value.", null, null); 
	}
	
	public IValue incrementHours(IDateTime dt, IInteger n)
	//@doc{Increment the hours by a given amount.}
	{
		return incrementTime(dt, Calendar.HOUR_OF_DAY, "hours", n);
	}		

	public IValue incrementMinutes(IDateTime dt, IInteger n)
	//@doc{Increment the minutes by a given amount.}
	{
		return incrementTime(dt, Calendar.MINUTE, "minutes", n);
	}		
	
	public IValue incrementSeconds(IDateTime dt, IInteger n)
	//@doc{Increment the seconds by a given amount.}
	{
		return incrementTime(dt, Calendar.SECOND, "seconds", n);
	}
	
	public IValue incrementMilliseconds(IDateTime dt, IInteger n)
	//@doc{Increment the milliseconds by a given amount.}
	{
		return incrementTime(dt, Calendar.MILLISECOND, "milliseconds", n);
	}

	private IValue incrementDate(IDateTime dt, int field, String fieldName, IInteger amount) {
		if (!dt.isTime()) {
			long millis = dt.getInstant();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(millis);
			calendar.add(field, amount.intValue());
			if (dt.isDate()) {
				return values.date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			}
			return values.datetime(calendar.getTimeInMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot increment the " + fieldName + " on a time value.", null, null); 

	}
	
	public IValue decrementYears(IDateTime dt, IInteger n)
	//@doc{Decrement the years by a given amount.}
	{
		return incrementDate(dt, Calendar.YEAR, "years", n.negate());
	}		

	public IValue decrementMonths(IDateTime dt, IInteger n)
	//@doc{Decrement the months by a given amount.}
	{
		return incrementDate(dt, Calendar.MONTH, "months", n.negate());	}	

	public IValue decrementDays(IDateTime dt, IInteger n)
	//@doc{Decrement the days by a given amount.}
	{
		return incrementDate(dt, Calendar.DAY_OF_MONTH, "days", n.negate());
	}

	public IValue decrementHours(IDateTime dt, IInteger n)
	//@doc{Decrement the hours by a given amount.}
	{
		return incrementTime(dt, Calendar.HOUR_OF_DAY, "hours", n.negate());
	}		

	public IValue decrementMinutes(IDateTime dt, IInteger n)
	//@doc{Decrement the minutes by a given amount.}
	{
		return incrementTime(dt, Calendar.MINUTE, "minutes", n.negate());
	}		

	public IValue decrementSeconds(IDateTime dt, IInteger n)
	//@doc{Decrement the seconds by a given amount.}
	{
		return incrementTime(dt, Calendar.SECOND, "seconds", n.negate());	
	}		

	public IValue decrementMilliseconds(IDateTime dt, IInteger n)
	//@doc{Decrement the milliseconds by a given amount.}
	{
		return incrementTime(dt, Calendar.MILLISECOND, "milliseconds", n.negate());
	}		

	public IValue createDurationInternal(IDateTime dStart, IDateTime dEnd) {
		// dStart and dEnd both have to be dates, times, or datetimes
		Calendar startCal = Calendar.getInstance();
		startCal.setTimeInMillis(dStart.getInstant());
		Calendar endCal = Calendar.getInstance();
		endCal.setTimeInMillis(dEnd.getInstant());
		
		IValue duration = null;
		
		if (dStart.isDate()) {
			if (dEnd.isDate()) {
				duration = values.tuple(
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.YEAR)),
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.MONTH)),
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.DAY_OF_MONTH)),
						values.integer(0), values.integer(0), values.integer(0),
						values.integer(0));
			} else if (dEnd.isTime()) {
				throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot determine the duration between a date with no time and a time with no date.", null, null);	
			} else {
				throw RuntimeExceptionFactory.invalidUseOfDateTimeException("Cannot determine the duration between a date with no time and a datetime.", null, null);					
			}
		} else if (dStart.isTime()) {
			if (dEnd.isTime()) {
				duration = values.tuple(
						values.integer(0),
						values.integer(0),
						values.integer(0),
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.HOUR_OF_DAY)),
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.MINUTE)),
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.SECOND)),
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.MILLISECOND)));
			} else if (dEnd.isDate()) {
				throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot determine the duration between a time with no date and a date with no time.", null, null);	
			} else {
				throw RuntimeExceptionFactory.invalidUseOfDateTimeException("Cannot determine the duration between a time with no date and a datetime.", null, null);					
			}
		} else {
			if (dEnd.isDateTime()) {
				duration = values.tuple(
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.YEAR)),
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.MONTH)),
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.DAY_OF_MONTH)),
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.HOUR_OF_DAY)),
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.MINUTE)),
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.SECOND)),
						values.integer(startCal.fieldDifference(endCal.getTime(), Calendar.MILLISECOND)));
			} else if (dEnd.isDate()) {
				throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot determine the duration between a datetime and a date with no time.", null, null);	
			} else {
				throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot determine the duration between a datetime and a time with no date.", null, null);					
			}
		}
		return duration;
	}
	
	public IValue parseDate(IString inputDate, IString formatString)
	//@doc{Parse an input date given as a string using the given format string}
	{	
		try {
			SimpleDateFormat fmt = new SimpleDateFormat(formatString.getValue());
			Date dt = fmt.parse(inputDate.getValue());
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			return values.date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input date: " + inputDate.getValue() + 
					" using format string: " + formatString.getValue(), null, null);
		} catch (ParseException e) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input date: " + inputDate.getValue() + 
					" using format string: " + formatString.getValue(), null, null);
		}
	}
	
	public IValue parseDateInLocale(IString inputDate, IString formatString, IString locale) 
	//@doc{Parse an input date given as a string using a specific locale and format string}
	{
		try {
			SimpleDateFormat fmt = new SimpleDateFormat(formatString.getValue(), new Locale(locale.getValue()));
			Date dt = fmt.parse(inputDate.getValue());
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			return values.date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input date: " + inputDate.getValue() + 
					" using format string: " + formatString.getValue() + " in locale: " + locale.getValue(), null, null);
		} catch (ParseException e) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input date: " + inputDate.getValue() + 
					" using format string: " + formatString.getValue() + " in locale: " + locale.getValue(), null, null);
		}
	}

	public IValue parseTime(IString inputTime, IString formatString) 
	//@doc{Parse an input time given as a string using the given format string}
	{
		try {
			SimpleDateFormat fmt = new SimpleDateFormat(formatString.getValue());
			Date dt = fmt.parse(inputTime.getValue());
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			// The value for zone offset comes back in milliseconds. The number of
			// hours is thus milliseconds / 1000 (to get to seconds) / 60 (to get to minutes)
			// / 60 (to get to hours). Minutes is this except for the last division,
			// but then we use mod 60 since this gives us total # of minutes, including
			// the hours we have already computed.
			int zoneHours = cal.get(Calendar.ZONE_OFFSET) / (1000 * 60 * 60);
			int zoneMinutes = (cal.get(Calendar.ZONE_OFFSET) / (1000 * 60)) % 60; 
			return values.time(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND), zoneHours, zoneMinutes);
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input date: " + inputTime.getValue() + 
					" using format string: " + formatString.getValue(), null, null);
		} catch (ParseException e) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input date: " + inputTime.getValue() + 
					" using format string: " + formatString.getValue(), null, null);
		}
	}
	
	public IValue parseTimeInLocale(IString inputTime, IString formatString, IString locale) 
	//@doc{Parse an input time given as a string using a specific locale and format string}
	{
		try {
			SimpleDateFormat fmt = new SimpleDateFormat(formatString.getValue(), new ULocale(locale.getValue()));
			Date dt = fmt.parse(inputTime.getValue());
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			// The value for zone offset comes back in milliseconds. The number of
			// hours is thus milliseconds / 1000 (to get to seconds) / 60 (to get to minutes)
			// / 60 (to get to hours). Minutes is this except for the last division,
			// but then we use mod 60 since this gives us total # of minutes, including
			// the hours we have already computed.
			int zoneHours = cal.get(Calendar.ZONE_OFFSET) / (1000 * 60 * 60);
			int zoneMinutes = (cal.get(Calendar.ZONE_OFFSET) / (1000 * 60)) % 60; 
			return values.time(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND), zoneHours, zoneMinutes);
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input time: " + inputTime.getValue() + 
					" using format string: " + formatString.getValue() + " in locale: " + locale.getValue(), null, null);
		} catch (ParseException e) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input time: " + inputTime.getValue() + 
					" using format string: " + formatString.getValue() + " in locale: " + locale.getValue(), null, null);
		}
	}

	public IValue parseDateTime(IString inputDateTime, IString formatString) 
	//@doc{Parse an input datetime given as a string using the given format string}
	{
		try {
			SimpleDateFormat fmt = new SimpleDateFormat(formatString.getValue());
			Date dt = fmt.parse(inputDateTime.getValue());
			return values.datetime(dt.getTime());
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input datetime: " + inputDateTime.getValue() + 
					" using format string: " + formatString.getValue(), null, null);
		} catch (ParseException e) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input datetime: " + inputDateTime.getValue() + 
					" using format string: " + formatString.getValue(), null, null);
		}			
	}
	
	public IValue parseDateTimeInLocale(IString inputDateTime, IString formatString, IString locale) 
	//@doc{Parse an input datetime given as a string using a specific locale and format string}
	{
		try {
			SimpleDateFormat fmt = new SimpleDateFormat(formatString.getValue(), new ULocale(locale.getValue()));
			Date dt = fmt.parse(inputDateTime.getValue());
			return values.datetime(dt.getTime());
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input datetime: " + inputDateTime.getValue() + 
					" using format string: " + formatString.getValue() + " in locale: " + locale.getValue(), null, null);
		} catch (ParseException e) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input datetime: " + inputDateTime.getValue() + 
					" using format string: " + formatString.getValue() + " in locale: " + locale.getValue(), null, null);
		}
	}

	public IValue printDate(IDateTime inputDate, IString formatString) 
	//@doc{Print an input date using the given format string}
	{
		try {
			SimpleDateFormat sd = new SimpleDateFormat(formatString.getValue()); 
			return values.string(sd.format(new Date(inputDate.getInstant())));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print time with format " + formatString.getValue(), null, null);
		}
	}

	public IValue printDate(IDateTime inputDate) 
	//@doc{Print an input date using a default format string}
	{
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd"); 
		return values.string(sd.format(new Date(inputDate.getInstant())));
	}
	
	public IValue printDateInLocale(IDateTime inputDate, IString formatString, IString locale) 
	//@doc{Print an input date using a specific locale and format string}
	{
		try {
			SimpleDateFormat sd = new SimpleDateFormat(formatString.getValue(),new ULocale(locale.getValue())); 
			return values.string(sd.format(new Date(inputDate.getInstant())));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print time with format " + formatString.getValue() + ", in locale: " + locale.getValue(), null, null);
		}
	}

	public IValue printDateInLocale(IDateTime inputDate, IString locale) 
	//@doc{Print an input date using a specific locale and a default format string}
	{
		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd",new ULocale(locale.getValue())); 
			return values.string(sd.format(new Date(inputDate.getInstant())));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print time in locale: " + locale.getValue(), null, null);
		}
	}

	public IValue printTime(IDateTime inputTime, IString formatString) 
	//@doc{Print an input time using the given format string}
	{
		try {
			SimpleDateFormat sd = new SimpleDateFormat(formatString.getValue()); 
			return values.string(sd.format(new Date(inputTime.getInstant())));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print time with format: " + formatString.getValue(), null, null);
		}			
	}
	
	public IValue printTime(IDateTime inputTime) 
	//@doc{Print an input time using a default format string}
	{
		SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss.SSSZ"); 
		return values.string(sd.format(new Date(inputTime.getInstant())));
	}
	
	public IValue printTimeInLocale(IDateTime inputTime, IString formatString, IString locale) 
	//@doc{Print an input time using a specific locale and format string}
	{
		try {
			SimpleDateFormat sd = new SimpleDateFormat(formatString.getValue(),new ULocale(locale.getValue())); 
			return values.string(sd.format(new Date(inputTime.getInstant())));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print time in locale: " + locale.getValue(), null, null);
		}
	}

	public IValue printTimeInLocale(IDateTime inputTime, IString locale) 
	//@doc{Print an input time using a specific locale and a default format string}
	{
		try {
			SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss.SSSZ",new ULocale(locale.getValue())); 
			return values.string(sd.format(new Date(inputTime.getInstant())));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print time in locale: " + locale.getValue(), null, null);
		}
	}

	public IValue printDateTime(IDateTime inputDateTime, IString formatString) 
	//@doc{Print an input datetime using the given format string}
	{
		try {
			SimpleDateFormat sd = new SimpleDateFormat(formatString.getValue()); 
			return values.string(sd.format(new Date(inputDateTime.getInstant())));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print datetime using format string: " + formatString.getValue(), null, null);
		}		
	}

	public IValue printDateTime(IDateTime inputDateTime) 
	//@doc{Print an input datetime using a default format string}
	{
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ"); 
		return values.string(sd.format(new Date(inputDateTime.getInstant())));
	}
	
	public IValue printDateTimeInLocale(IDateTime inputDateTime, IString formatString, IString locale) 
	//@doc{Print an input datetime using a specific locale and format string}
	{
		try {
			SimpleDateFormat sd = new SimpleDateFormat(formatString.getValue(),new ULocale(locale.getValue())); 
			return values.string(sd.format(new Date(inputDateTime.getInstant())));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print datetime using format string: " + formatString.getValue() +
					" in locale: " + locale.getValue(), null, null);
		}
	}

	public IValue printDateTimeInLocale(IDateTime inputDateTime, IString locale) 
	//@doc{Print an input datetime using a specific locale and a default format string}
	{
		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ",new ULocale(locale.getValue())); 
			return values.string(sd.format(new Date(inputDateTime.getInstant())));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print datetime in locale: " + locale.getValue(), null, null);
		}
	}
	
	/*
	 * Graph
	 */
	
	private HashMap<IValue,Distance> distance;
	private HashMap<IValue, IValue> pred;
	private HashSet<IValue> settled;
	private PriorityQueue<IValue> Q;
	private int MAXDISTANCE = 10000;
	
	private HashMap<IValue, LinkedList<IValue>> adjacencyList;
	
	private void buildAdjacencyListAndDistance(IRelation G){
		adjacencyList = new HashMap<IValue, LinkedList<IValue>> ();
		distance = new HashMap<IValue, Distance>();
		
		for(IValue v : G){
			ITuple tup = (ITuple) v;
			IValue from = tup.get(0);
			IValue to = tup.get(1);
			
			if(distance.get(from) == null)
				distance.put(from, new Distance(MAXDISTANCE));
			if(distance.get(to) == null)
				distance.put(to, new Distance(MAXDISTANCE));
			
			LinkedList<IValue> adjacencies = adjacencyList.get(from);
			if(adjacencies == null)
				adjacencies = new LinkedList<IValue>();
			adjacencies.add(to);
			adjacencyList.put(from, adjacencies);
		}
	}
	
	public IValue shortestPathPair(IRelation G, IValue From, IValue To){
		buildAdjacencyListAndDistance(G);
		distance.put(From, new Distance(0));
		
		pred = new HashMap<IValue, IValue>();
		settled = new HashSet<IValue>();
		Q = new PriorityQueue<IValue>(G.size(), new NodeComparator(distance));
		Q.add(From);
		
		while(!Q.isEmpty()){
			IValue u = Q.remove();
			if(u.isEqual(To))	
				return extractPath(From, u);
			settled.add(u);
			relaxNeighbours(u);
		}
		return values.list();
	}
	
	private void relaxNeighbours(IValue u){
		LinkedList<IValue> adjacencies = adjacencyList.get(u);
		if(adjacencies != null) {
			for(IValue v : adjacencyList.get(u)){
				if(!settled.contains(v)){
					Distance dv = distance.get(v);
					Distance du = distance.get(u);
					if(dv.intval > du.intval + 1){  // 1 is default weight of each edge
						dv.intval = du.intval + 1;
						pred.put(v,u);
						Q.add(v);
					}
				}
			}
		}
	}
	
	private IList extractPath(IValue start, IValue u){
		Type listType = types.listType(start.getType());
		IListWriter w = listType.writer(values);
		
		if(!start.isEqual(u)){
			w.insert(u);
			while(!pred.get(u).isEqual(start)){
				u = pred.get(u);
				w.insert(u);
			}
			// TODO Check if a path was found at all; it could be that we just hit the root of the graph.
		}
		w.insert(start);
		return w.done();
	}
	
	public void print(IValue arg, IEvaluatorContext eval){
		PrintWriter currentOutStream = eval.getStdOut();
		
		try{
			if(arg.getType().isStringType()){
				currentOutStream.print(((IString) arg).getValue().toString());
			}
			else if(arg.getType().isSubtypeOf(Factory.Tree)){
				currentOutStream.print(TreeAdapter.yield((IConstructor) arg));
			}
			else if (arg.getType().isSubtypeOf(Factory.Type)) {
				currentOutStream.print(SymbolAdapter.toString((IConstructor) ((IConstructor) arg).get("symbol")));
			}
			else{
				currentOutStream.print(arg.toString());
			}
		}finally{
			currentOutStream.flush();
		}
	}
	
	public void iprint(IValue arg, IEvaluatorContext eval){
		StandardTextWriter w = new StandardTextWriter(true, 2);
		
		try {
			w.write(arg, eval.getStdOut());
		} 
		catch (IOException e) {
			RuntimeExceptionFactory.io(values.string("Could not print indented value"), eval.getCurrentAST(), eval.getStackTrace());
		}
		finally{
			eval.getStdOut().flush();
		}
	}
	
	public void iprintln(IValue arg, IEvaluatorContext eval){
		StandardTextWriter w = new StandardTextWriter(true, 2);
		
		try {
			w.write(arg, eval.getStdOut());
			eval.getStdOut().println();
		} 
		catch (IOException e) {
			RuntimeExceptionFactory.io(values.string("Could not print indented value"), eval.getCurrentAST(), eval.getStackTrace());
		}
		finally{
			eval.getStdOut().flush();
		}
	}
	
	public void println(IEvaluatorContext eval) {
		eval.getStdOut().println();
		eval.getStdOut().flush();
	}
	
	public void println(IValue arg, IEvaluatorContext eval){
		PrintWriter currentOutStream = eval.getStdOut();
		
		try{
			if(arg.getType().isStringType()){
				currentOutStream.print(((IString) arg).getValue());
			}
			else if(arg.getType().isSubtypeOf(Factory.Tree)){
				currentOutStream.print(TreeAdapter.yield((IConstructor) arg));
			}
			else if (arg.getType().isSubtypeOf(Factory.Type)) {
				currentOutStream.print(SymbolAdapter.toString((IConstructor) ((IConstructor) arg).get("symbol")));
			}
			else{
				currentOutStream.print(arg.toString());
			}
			currentOutStream.println();
		}finally{
			currentOutStream.flush();
		}
	}
	
	public void rprintln(IValue arg, IEvaluatorContext eval){
		PrintWriter currentOutStream = eval.getStdOut();
		
		try{
			currentOutStream.print(arg.toString());
			currentOutStream.println();
		}finally{
			currentOutStream.flush();
		}
	}
	
	public void rprint(IValue arg, IEvaluatorContext eval){
		PrintWriter currentOutStream = eval.getStdOut();
		
		try{
			currentOutStream.print(arg.toString());
		}finally{
			currentOutStream.flush();
		}
	}

	@Deprecated
	public IValue readFile(IString filename){
		IListWriter w = types.listType(types.stringType()).writer(values);
		
		BufferedReader in = null;
		try{
			in = new BufferedReader(new FileReader(filename.getValue()));
			java.lang.String line;

			do {
				line = in.readLine();
				if(line != null){
					w.append(values.string(line));
				}
			} while (line != null);
		}catch(FileNotFoundException fnfex){
			throw RuntimeExceptionFactory.pathNotFound(values.sourceLocation(filename.getValue()), null, null);
		}catch(IOException ioex){
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
		return w.done();
	}
	
	public IValue exists(ISourceLocation sloc, IEvaluatorContext ctx) {
		return values.bool(ctx.getResolverRegistry().exists(sloc.getURI()));
	}
	
	public IValue lastModified(ISourceLocation sloc, IEvaluatorContext ctx) {
		try {
			return values.datetime(ctx.getResolverRegistry().lastModified(sloc.getURI()));
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), ctx.getCurrentAST(), ctx.getStackTrace());
		}
	}
	
	public IValue isDirectory(ISourceLocation sloc, IEvaluatorContext ctx) {
		return values.bool(ctx.getResolverRegistry().isDirectory(sloc.getURI()));
	}
	
	public IValue isFile(ISourceLocation sloc, IEvaluatorContext ctx) {
		return values.bool(ctx.getResolverRegistry().isFile(sloc.getURI()));
	}
	
	public void mkDirectory(ISourceLocation sloc, IEvaluatorContext ctx) throws IOException {
		ctx.getResolverRegistry().mkDirectory(sloc.getURI());
	}
	
	public IValue listEntries(ISourceLocation sloc, IEvaluatorContext ctx) {
		try {
			java.lang.String [] entries = ctx.getResolverRegistry().listEntries(sloc.getURI());
			IListWriter w = values.listWriter(types.stringType());
			for(java.lang.String entry : entries){
				w.append(values.string(entry));
			}
			return w.done();
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), ctx.getCurrentAST(), ctx.getStackTrace());
		} 
	}
	
	
	public IValue readFile(ISourceLocation sloc, IEvaluatorContext ctx){
		StringBuilder result = new StringBuilder(1024 * 1024);
		
		InputStream in = null;
		try{
			in = ctx.getResolverRegistry().getInputStream(sloc.getURI());
			byte[] buf = new byte[4096];
			int count;

			while((count = in.read(buf)) != -1){
				result.append(new java.lang.String(buf, 0, count));
			}
			
			java.lang.String str = result.toString();
			
			if(sloc.hasOffsetLength() && sloc.getOffset() != -1){
				str = str.substring(sloc.getOffset(), sloc.getOffset() + sloc.getLength());
			}
			
			return values.string(str);
		}catch(FileNotFoundException fnfex){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
		}catch(IOException ioex){
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
	
	public void writeFile(ISourceLocation sloc, IList V, IEvaluatorContext ctx) {
		writeFile(sloc, V, false, ctx);
	}
	
	private void writeFile(ISourceLocation sloc, IList V, boolean append, IEvaluatorContext ctx){
		OutputStream out = null;
		try{
			out = ctx.getResolverRegistry().getOutputStream(sloc.getURI(), append);
			
			for(IValue elem : V){
				if (elem.getType().isStringType()){
					out.write(((IString) elem).getValue().toString().getBytes());
				}else if (elem.getType().isSubtypeOf(Factory.Tree)) {
					out.write(TreeAdapter.yield((IConstructor) elem).getBytes());
				}else{
					out.write(elem.toString().getBytes());
				}
			}
		}catch(FileNotFoundException fnfex){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
		}catch(IOException ioex){
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
		}finally{
			if(out != null){
				try{
					out.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}

		return;
	}
	
	public void appendToFile(ISourceLocation sloc, IList V, IEvaluatorContext ctx){
		writeFile(sloc, V, true, ctx);
	}
	
	public IList readFileLines(ISourceLocation sloc, IEvaluatorContext ctx){
		IListWriter w = types.listType(types.stringType()).writer(values);
		
		BufferedReader in = null;
		try{
			InputStream stream = ctx.getResolverRegistry().getInputStream(sloc.getURI());
			in = new BufferedReader(new InputStreamReader(stream));
			java.lang.String line;
			
			int i = 0;
//			int offset = sloc.getOffset();
			int beginLine = sloc.hasLineColumn() ? sloc.getBeginLine() : -1;
			int beginColumn = sloc.hasLineColumn() ? sloc.getBeginColumn() : -1;
			int endLine = sloc.hasLineColumn() ? sloc.getEndLine() : -1;
			int endColumn = sloc.hasLineColumn() ? sloc.getEndColumn() : -1;

			do{
				line = in.readLine();
				i++;
				if(line != null){
					if(!sloc.hasOffsetLength()){
						w.append(values.string(line));
					}else{
						if(!sloc.hasLineColumn()){
							endColumn = line.length();
						}
						if(i == beginLine){
							if(i == endLine){
								w.append(values.string(line.substring(beginColumn, endColumn)));
							}else{
								w.append(values.string(line.substring(beginColumn)));
							}
						}else if(i > beginLine){
							if(i == endLine){
								w.append(values.string(line.substring(0, endColumn)));
							}
							else if(i < endLine){
								w.append(values.string(line));
							}
						}
					}
				}
			}while(line != null);
		}catch(MalformedURLException e){
		    throw RuntimeExceptionFactory.malformedURI(sloc.toString(), null, null);
		}catch(FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
		}catch(IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}

		return w.done();
	}
	
	public IList readFileBytes(ISourceLocation sloc, IEvaluatorContext ctx){
		IListWriter w = types.listType(types.integerType()).writer(values);
		
		BufferedInputStream in = null;
		try{
			InputStream stream = ctx.getResolverRegistry().getInputStream(sloc.getURI());
			in = new BufferedInputStream(stream);
			int read;
			final int size = 256;
			byte bytes[] = new byte[size];
			
			do{
				read = in.read(bytes);
				for (int i = 0; i < read; i++) {
					w.append(values.integer(bytes[i] & 0xff));
				}
			}while(read != -1);
		}catch(FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
		}catch(IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}

		return w.done();
	}
	
	/*
	 * List
	 */
	
	private WeakReference<IList> indexes;

	public IList sort(IList l, IValue cmpv){
		final ICallableValue cmp = (ICallableValue) cmpv;
		final IValue[] argArr = new IValue[2]; // this creates less garbage
		FunctionType ftype = (FunctionType) cmpv.getType();
		Type argTypes = ftype.getArgumentTypes();
		final Type[] typeArr = 
				new Type[] {argTypes.getFieldType(0),argTypes.getFieldType(1)};
		
		IValue[] tmpArr = new IValue[l.length()];
		for(int i = 0 ; i < l.length() ; i++){
			tmpArr[i] = l.get(i);
		}
		Comparator<IValue> cmpj = new Comparator<IValue>() {

			@Override
			public int compare(IValue lhs, IValue rhs) {
				if(lhs == rhs){
					return 0;
				} else {
					argArr[0] = lhs;
					argArr[1] = rhs;
					Result<IValue> res = cmp.call(typeArr,argArr);
					boolean leq = ((IBool)res.getValue()).getValue();
					return leq ? -1 : 1;
				}
			}
		};
		Arrays.sort(tmpArr,cmpj);
		
		IListWriter writer = values.listWriter(l.getElementType());
		for(IValue v : tmpArr){
			writer.append(v);
		}
		return writer.done();
	}
	
	private IList makeUpTill(int from,int len){
		IListWriter writer = values.listWriter(types.integerType());
		for(int i = from ; i < len; i++){
			writer.append(values.integer(i));
		}
		return writer.done();
	}
	
	public IValue delete(IList lst, IInteger n)
	// @doc{delete -- delete nth element from list}
	{
		try {
			return lst.delete(n.intValue());
		} catch (IndexOutOfBoundsException e){
			 throw RuntimeExceptionFactory.indexOutOfBounds(n, null, null);
		}
	}
	
	public IValue domain(IList lst)
	//@doc{domain -- a list of all legal index values for a list}
	{
		ISetWriter w = values.setWriter(types.integerType());
		int len = lst.length();
		for (int i = 0; i < len; i++){
			w.insert(values.integer(i));
		}
		return w.done();
	}
	
	public IValue head(IList lst)
	// @doc{head -- get the first element of a list}
	{
	   if(lst.length() > 0){
	      return lst.get(0);
	   }
	   
	   throw RuntimeExceptionFactory.emptyList(null, null);
	}

	public IValue head(IList lst, IInteger n)
	  throws IndexOutOfBoundsException
	// @doc{head -- get the first n elements of a list}
	{
	   try {
	      return lst.sublist(0, n.intValue());
	   } catch(IndexOutOfBoundsException e){
		   IInteger end = values.integer(n.intValue() - 1);
	      throw RuntimeExceptionFactory.indexOutOfBounds(end, null, null);
	   }
	}

	public IValue getOneFrom(IList lst)
	//@doc{getOneFrom -- get an arbitrary element from a list}
	{
		int n = lst.length();
		if(n > 0){
			return lst.get(random.nextInt(n));
		}
		
		throw RuntimeExceptionFactory.emptyList(null, null);
	}

	public IValue insertAt(IList lst, IInteger n, IValue elm)
	  throws IndexOutOfBoundsException
	 //@doc{insertAt -- add an element at a specific position in a list}
	 {
	 	IListWriter w = values.listWriter(elm.getType().lub(lst.getElementType()));
	 	
	 	int k = n.intValue();
	    if(k >= 0 && k <= lst.length()){
	      if(k == lst.length()){
	      	w.insert(elm);
	      }
	      for(int i = lst.length()-1; i >= 0; i--) {
	        w.insert(lst.get(i));
	        if(i == k){
	        	w.insert(elm);
	        }
	      }
	      return w.done();
	    }
	    
		throw RuntimeExceptionFactory.indexOutOfBounds(n, null, null);
	 }
	
	public IValue isEmpty(IList lst)
	//@doc{isEmpty -- is list empty?}
	{
		return values.bool(lst.length() == 0);
	}

	public IValue reverse(IList lst)
	//@doc{reverse -- elements of a list in reverse order}
	{
		return lst.reverse();
	}

	public IValue size(IList lst)
	//@doc{size -- number of elements in a list}
	{
	   return values.integer(lst.length());
	}

	 public IValue slice(IList lst, IInteger start, IInteger len)
	 //@doc{slice -- sublist from start of length len}
	 {
		try {
			return lst.sublist(start.intValue(), len.intValue());
		} catch (IndexOutOfBoundsException e){
			IInteger end = values.integer(start.intValue() + len.intValue());
			throw RuntimeExceptionFactory.indexOutOfBounds(end, null, null);
		}
	 }

	 public IValue tail(IList lst)
	 //@doc{tail -- all but the first element of a list}
	 {
	 	try {
	 		return lst.sublist(1, lst.length()-1);
	 	} catch (IndexOutOfBoundsException e){
	 		throw RuntimeExceptionFactory.emptyList(null, null);
	 	}
	 }
	 
	  public IValue tail(IList lst, IInteger len)
	 //@doc{tail -- last n elements of a list}
	 {
	 	int lenVal = len.intValue();
	 	int lstLen = lst.length();
	 
	 	try {
	 		return lst.sublist(lstLen - lenVal, lenVal);
	 	} catch (IndexOutOfBoundsException e){
	 		IInteger end = values.integer(lenVal - lstLen);
	 		throw RuntimeExceptionFactory.indexOutOfBounds(end, null, null);
	 	}
	 }
	  
	public IValue take(IInteger len, IList lst) {
	   //@doc{take -- take n elements of from front of a list}
		int lenVal = len.intValue();
		int lstLen = lst.length();
		if(lenVal >= lstLen){
			return lst;
		} else {
			return lst.sublist(0, lenVal);
		}
	}

	public IValue drop(IInteger len, IList lst) {
	   //@doc{drop -- remove n elements of from front of a list}
		int lenVal = len.intValue();
		int lstLen = lst.length();
		if(lenVal >= lstLen){
			return values.list();
		} else {
			return lst.sublist(lenVal, lstLen - lenVal);
		}
	}
	
	public IValue upTill(IInteger ni) {
		//@doc{Returns the list 0..n, this is slightly faster than [0,1..n], since the returned values are shared}
		int n = ni.intValue();
		if(indexes == null || indexes.get() == null) {
			IList l = makeUpTill(0, n);
			indexes = new WeakReference<IList>(l);
			return indexes.get();
		} else {
			IList l = indexes.get(); // strong ref
			if(l == null || n >= l.length()){ 
				l = makeUpTill(0,n);
				indexes =  new WeakReference<IList>(l);
				return l;
			}
			return l.sublist(0, n);
		} 
	}
	
	public IValue prefix(IList lst) {
		   //@doc{Return all but the last element of a list}
			int lstLen = lst.length();
			if(lstLen <= 1){
				return values.list();
			} else {
				return lst.sublist(0, lstLen - 1);
			}
		}


	 
	public IValue takeOneFrom(IList lst)
	//@doc{takeOneFrom -- remove an arbitrary element from a list, returns the element and the modified list}
	{
	   int n = lst.length();
	   
	   if(n > 0){
	   	  int k = random.nextInt(n);
	   	  IValue pick = lst.get(0);
	   	  IListWriter w = lst.getType().writer(values);
	  
	      for(int i = n - 1; i >= 0; i--) {
	         if(i == k){
	         	pick = lst.get(i);
	         } else {
	            w.insert(lst.get(i));
	         }
	      }
	      return values.tuple(pick, w.done());
	   	}
	   
	   throw RuntimeExceptionFactory.emptyList(null, null);
	}
	
	public IMap toMap(IList lst)
	// @doc{toMap -- convert a list of tuples to a map; first value in old tuples is associated with a set of second values}
	{
		Type tuple = lst.getElementType();
		Type keyType = tuple.getFieldType(0);
		Type valueType = tuple.getFieldType(1);
		Type valueSetType = types.setType(valueType);

		HashMap<IValue,ISetWriter> hm = new HashMap<IValue,ISetWriter>();

		for (IValue v : lst) {
			ITuple t = (ITuple) v;
			IValue key = t.get(0);
			IValue val = t.get(1);
			ISetWriter wValSet = hm.get(key);
			if(wValSet == null){
				wValSet = valueSetType.writer(values);
				hm.put(key, wValSet);
			}
			wValSet.insert(val);
		}
		
		Type resultType = types.mapType(keyType, valueSetType);
		IMapWriter w = resultType.writer(values);
		for(IValue v : hm.keySet()){
			w.put(v, hm.get(v).done());
		}
		return w.done();
	}
	
	public IValue toMapUnique(IList lst)
	//@doc{toMapUnique -- convert a list of tuples to a map; result should be a map}
	{
	   if(lst.length() == 0){
	      return values.map(types.voidType(), types.voidType());
	   }
	   Type tuple = lst.getElementType();
	   Type resultType = types.mapType(tuple.getFieldType(0), tuple.getFieldType(1));
	  
	   IMapWriter w = resultType.writer(values);
	   HashSet<IValue> seenKeys = new HashSet<IValue>();
	   for(IValue v : lst){
		   ITuple t = (ITuple) v;
		   IValue key = t.get(0);
		   if(seenKeys.contains(key)) 
				throw RuntimeExceptionFactory.MultipleKey(key, null, null);
		   seenKeys.add(key);
	     w.put(key, t.get(1));
	   }
	   return w.done();
	}

	public IValue toSet(IList lst)
	//@doc{toSet -- convert a list to a set}
	{
	  Type resultType = types.setType(lst.getElementType());
	  ISetWriter w = resultType.writer(values);
	  
	  for(IValue v : lst){
	    w.insert(v);
	  }
		
	  return w.done();
	}

	public IValue toString(IList lst)
	//@doc{toString -- convert a list to a string}
	{
		return values.string(lst.toString());
	}
	
	/*
	 * Map
	 */
	
	public IValue domain(IMap M)
	//@doc{domain -- return the domain (keys) of a map}

	{
	  Type keyType = M.getKeyType();
	  
	  Type resultType = types.setType(keyType);
	  ISetWriter w = resultType.writer(values);
	  Iterator<Entry<IValue,IValue>> iter = M.entryIterator();
	  while (iter.hasNext()) {
	    Entry<IValue,IValue> entry = iter.next();
	    w.insert(entry.getKey());
	  }
	  return w.done();
	}

	public IValue getOneFrom(IMap m)  
	//@doc{getOneFrom -- return arbitrary key of a map}
	{
	   int i = 0;
	   int sz = m.size();
	   if(sz == 0){
	      throw RuntimeExceptionFactory.emptyMap(null, null);
	   }
	   int k = random.nextInt(sz);
	   Iterator<Entry<IValue,IValue>> iter = m.entryIterator();
	  
	   while(iter.hasNext()){
	      if(i == k){
	      	return (iter.next()).getKey();
	      }
	      iter.next();
	      i++;
	   }
	   return null;
	}
	
	public IValue invertUnique(IMap M)
	//@doc{invertUnique -- return map with key and value inverted; values are unique}
	{
		Type keyType = M.getKeyType();
		Type valueType = M.getValueType();
		Type resultType = types.mapType(valueType, keyType);
		IMapWriter w = resultType.writer(values);
		HashSet<IValue> seenValues = new HashSet<IValue>();
		Iterator<Entry<IValue,IValue>> iter = M.entryIterator();
		while (iter.hasNext()) {
			Entry<IValue,IValue> entry = iter.next();
			IValue key = entry.getKey();
			IValue val = entry.getValue();
			if(seenValues.contains(val)) 
					throw RuntimeExceptionFactory.MultipleKey(val, null, null);
			seenValues.add(val);
			w.put(val, key);
		}
		return w.done();
	}
	
	public IValue invert(IMap M)
	//@doc{invert -- return map with key and value inverted; values are not unique and are collected in a set}
	{
		Type keyType = M.getKeyType();
		Type valueType = M.getValueType();
		Type keySetType = types.setType(keyType);
	
		HashMap<IValue,ISetWriter> hm = new HashMap<IValue,ISetWriter>();
		Iterator<Entry<IValue,IValue>> iter = M.entryIterator();
		while (iter.hasNext()) {
			Entry<IValue,IValue> entry = iter.next();
			IValue key = entry.getKey();
			IValue val = entry.getValue();
			ISetWriter wKeySet = hm.get(val);
			if(wKeySet == null){
				wKeySet = keySetType.writer(values);
				hm.put(val, wKeySet);
			}
			wKeySet.insert(key);
		}
		
		Type resultType = types.mapType(valueType, keySetType);
		IMapWriter w = resultType.writer(values);
		
		iter = M.entryIterator();
		for(IValue v : hm.keySet()){
			w.put(v, hm.get(v).done());
		}
		return w.done();
	}
	
	public IValue isEmpty(IMap M)
	//@doc{isEmpty -- is map empty?}
	{
		return values.bool(M.size() == 0);
	}

	public IValue range(IMap M)
	//@doc{range -- return the range (values) of a map}
	{
	  Type valueType = M.getValueType();
	  
	  Type resultType = types.setType(valueType);
	  ISetWriter w = resultType.writer(values);
	  Iterator<Entry<IValue,IValue>> iter = M.entryIterator();
	  while (iter.hasNext()) {
	    Entry<IValue,IValue> entry = iter.next();
	    w.insert(entry.getValue());
	  }
	  return w.done();
	}

	public IValue size(IMap M)
	{
		return values.integer(M.size());
	}

	public IValue toList(IMap M)
	//@doc{toList -- convert a map to a list}
	{
	  Type keyType = M.getKeyType();
	  Type valueType = M.getValueType();
	  Type elementType = types.tupleType(keyType,valueType);
	  
	  Type resultType = types.listType(elementType);
	  IListWriter w = resultType.writer(values);
	  Iterator<Entry<IValue,IValue>> iter = M.entryIterator();
	  while (iter.hasNext()) {
	    Entry<IValue,IValue> entry = iter.next();
	    w.insert(values.tuple(entry.getKey(), entry.getValue()));
	  }
	  return w.done();
	}

	public IValue toRel(IMap M)
	//@doc{toRel -- convert a map to a relation}
	{
	  Type keyType = M.getKeyType();
	  Type valueType = M.getValueType();
	  
	  Type resultType = types.relType(keyType, valueType);
	  IRelationWriter w = resultType.writer(values);
	  Iterator<Entry<IValue,IValue>> iter = M.entryIterator();
	  while (iter.hasNext()) {
	    Entry<IValue,IValue> entry = iter.next();
	    w.insert(values.tuple(entry.getKey(), entry.getValue()));
	  }
	  return w.done();
	}
	  
	public IValue toString(IMap M)
	{
	  return values.string(M.toString());
	}

	/*
	 * Node
	 */

	public IValue arity(INode T)
	//@doc{arity -- number of children of a node}
	{
	   return values.integer(T.arity());
	}

	public IValue getChildren(INode T)
	//@doc{getChildren -- get the children of a node}
	{
		Type resultType = types.listType(types.valueType());
		IListWriter w = resultType.writer(values);
		
		for(IValue v : T.getChildren()){
			w.append(v);
		}
		return w.done();
	}

	public IValue getName(INode T)
	//@doc{getName -- get the function name of a node}
	{
		return values.string(T.getName());
	}

	public IValue makeNode(IString N, IList V)
	//@doc{makeNode -- create a node given its function name and arguments}
	{
	    IList argList = V;
		IValue args[] = new IValue[argList.length()];
		int i = 0;
		for(IValue v : argList){
			args[i++] = v;
		}
		return values.node(N.getValue(), args);
	}
	
	public IValue readATermFromFile(IString fileName){
	//@doc{readATermFromFile -- read an ATerm from a named file}
		ATermReader atr = new ATermReader();
		try {
			FileInputStream stream = new FileInputStream(fileName.getValue());
			IValue result = atr.read(values, stream);
			stream.close();
			return result;
		} catch (FactTypeUseException e) {
			e.printStackTrace();
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		} catch (IOException e) {
			e.printStackTrace();
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);

		}
	}
	
	public IValue toString(INode T)
	//@doc{toString -- convert a node to a string}
	{
		return values.string(T.toString());
	}
	
	public IMap getAnnotations(INode node) {
		java.util.Map<java.lang.String,IValue> map = node.getAnnotations();
		IMapWriter w = values.mapWriter(types.stringType(), types.valueType());
		
		for (Entry<java.lang.String,IValue> entry : map.entrySet()) {
			w.put(values.string(entry.getKey()), entry.getValue());
		}
		
		return w.done();
	}
	
	public INode setAnnotations(INode node, IMap annotations) {
		java.util.Map<java.lang.String,IValue> map = new HashMap<java.lang.String,IValue>();
		
		for (IValue key : annotations) {
			IValue value = annotations.get(key);
			map.put(((IString) key).getValue(), value);
		}
		
		return node.setAnnotations(map);
	}
	
	public INode delAnnotations(INode node) {
		return node.removeAnnotations();
	}
	
	public INode delAnnotation(INode node, IString label) {
		return node.removeAnnotation(label.getValue());
	}
	
	/*
	 * ParseTree
	 */
	
	private final TypeReifier tr;

	public IValue parse(IValue start, ISourceLocation input, IEvaluatorContext ctx) {
		return parse(start, values.mapWriter().done(), input, ctx);
	}
	
	public IValue parse(IValue start, IMap robust, ISourceLocation input, IEvaluatorContext ctx) {
		Type reified = start.getType();
		IConstructor startSort = checkPreconditions(start, reified);
		
		try {
			IConstructor pt = ctx.getEvaluator().parseObject(ctx.getEvaluator().getMonitor(), startSort, robust, input.getURI());

			if (TreeAdapter.isAppl(pt)) {
				if (SymbolAdapter.isStart(TreeAdapter.getType(pt))) {
					pt = (IConstructor) TreeAdapter.getArgs(pt).get(1);
				}
			}
			return pt;
		}
		catch (ParseError pe) {
			ISourceLocation errorLoc = values.sourceLocation(pe.getLocation(), pe.getOffset(), pe.getLength(), pe.getBeginLine() + 1, pe.getEndLine() + 1, pe.getBeginColumn(), pe.getEndColumn());
			throw RuntimeExceptionFactory.parseError(errorLoc, ctx.getCurrentAST(), ctx.getStackTrace());
		}
	}

	public IValue parse(IValue start, IString input, IEvaluatorContext ctx) {
		return parse(start, values.mapWriter().done(), input, ctx);
	}
	
	public IValue parse(IValue start, IMap robust, IString input, IEvaluatorContext ctx) {
		Type reified = start.getType();
		IConstructor startSort = checkPreconditions(start, reified);
		try {
			IConstructor pt = ctx.getEvaluator().parseObject(ctx.getEvaluator().getMonitor(), startSort, robust, input.getValue());

			if (TreeAdapter.isAppl(pt)) {
				if (SymbolAdapter.isStart(TreeAdapter.getType(pt))) {
					pt = (IConstructor) TreeAdapter.getArgs(pt).get(1);
				}
			}

			return pt;
		}
		catch (ParseError pe) {
			ISourceLocation errorLoc = values.sourceLocation(pe.getLocation(), pe.getOffset(), pe.getLength(), pe.getBeginLine() + 1, pe.getEndLine() + 1, pe.getBeginColumn(), pe.getEndColumn());
			throw RuntimeExceptionFactory.parseError(errorLoc, ctx.getCurrentAST(), ctx.getStackTrace());
		}
	}
	
	public IValue parse(IValue start, IString input, ISourceLocation loc, IEvaluatorContext ctx) {
		return parse(start, values.mapWriter().done(), input, loc, ctx);
	}
	
	public IValue parse(IValue start, IMap robust, IString input, ISourceLocation loc, IEvaluatorContext ctx) {
		Type reified = start.getType();
		IConstructor startSort = checkPreconditions(start, reified);
		try {
			IConstructor pt = ctx.getEvaluator().parseObject(ctx.getEvaluator().getMonitor(), startSort, robust, input.getValue(), loc);

			if (TreeAdapter.isAppl(pt)) {
				if (SymbolAdapter.isStart(TreeAdapter.getType(pt))) {
					pt = (IConstructor) TreeAdapter.getArgs(pt).get(1);
				}
			}

			return pt;
		}
		catch (ParseError pe) {
			ISourceLocation errorLoc = values.sourceLocation(pe.getLocation(), pe.getOffset(), pe.getLength(), pe.getBeginLine(), pe.getEndLine(), pe.getBeginColumn(), pe.getEndColumn());
			throw RuntimeExceptionFactory.parseError(errorLoc, ctx.getCurrentAST(), ctx.getStackTrace());
		}
	}
	
	public IString unparse(IConstructor tree) {
		return values.string(TreeAdapter.yield(tree));
	}
	
	private IConstructor makeConstructor(String name, IEvaluatorContext ctx,  IValue ...args) {
		IValue value = ctx.getEvaluator().call(name, args);
		Type type = value.getType();
		if (type.isAbstractDataType()) {
			return (IConstructor)value;
		}
		throw RuntimeExceptionFactory.implodeError("Calling of constructor " + name + " did not return a constructor", 
				ctx.getCurrentAST(), ctx.getStackTrace());
	}
	
	private java.lang.String unescapedConsName(IConstructor tree) {
		java.lang.String x = TreeAdapter.getConstructorName(tree);
		if (x != null) {
			x = x.replaceAll("\\\\", "");
		}
		return x;
	}

	private Set<Type> findConstructors(Type type, java.lang.String constructorName, int arity,  TypeStore store) {
		Set<Type> constructors = new HashSet<Type>();
		
		for (Type constructor : store.lookupConstructor(type, constructorName)) {
			if (constructor.getArity() == arity)
				constructors.add(constructor);
		}
		
		return constructors;
	}

	
//	private Type findConstructor(Type type, java.lang.String constructorName, int arity,  TypeStore store) {
//		for (Type candidate: store.lookupConstructor(type, constructorName)) {
//			// It finds the first with suitable arity, so this is inaccurate
//			// if there are overloaded constructors with the same arity
//			if (arity == candidate.getArity()) {
//				return candidate;
//			}
//		}
//		return null;
//	}

	public IValue implode(IValue reifiedType, IConstructor tree, IEvaluatorContext ctx) {
		TypeStore store = new TypeStore();
		Type type = tr.valueToType((IConstructor) reifiedType, store);
		try {
			return implode(store, type, tree, false, ctx);
		}
		catch (Backtrack b) {
			throw b.exception;
		}
	}

	@SuppressWarnings("serial")
	private static class Backtrack extends RuntimeException {
		Throw exception;
		public Backtrack(Throw exception) {
			this.exception = exception;
		}
		@Override
		public synchronized Throwable fillInStackTrace() {
			return this;
		}
	}
	
	private IValue[] implodeArgs(TypeStore store, Type type, IList args, IEvaluatorContext ctx) {
		int length = args.length();
		IValue implodedArgs[] = new IValue[length];
		for (int i = 0; i < length; i++) {
			implodedArgs[i] = implode(store, type.getFieldType(i), (IConstructor)args.get(i), false, ctx);
		}
		return implodedArgs;
	}
	
	
	private IValue implode(TypeStore store, Type type, IConstructor tree, boolean splicing, IEvaluatorContext ctx) {
		if (TreeAdapter.isLexical(tree)) {
			java.lang.String constructorName = unescapedConsName(tree);
			java.lang.String yield = TreeAdapter.yield(tree);
			if (constructorName != null) {
				// make a single argument constructor  with yield as argument
				// if there is a singleton constructor with a str argument
				if (!type.isAbstractDataType()) {
					throw RuntimeExceptionFactory.illegalArgument(tree, null, null, "Constructor (" + constructorName + ") should match with abstract data type and not with " + type);
				}
				Set<Type> conses = findConstructors(type, constructorName, 1, store);
				Iterator<Type> iter = conses.iterator();
				while (iter.hasNext()) {
					try {
						@SuppressWarnings("unused")
						Type cons = iter.next();
						ISourceLocation loc = TreeAdapter.getLocation(tree);
						IConstructor ast = makeConstructor(constructorName, ctx, values.string(yield));
						return ast.setAnnotation("location", loc);
					}
					catch (Backtrack b) {
						continue;
					}
				}
				throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, null, null, "Cannot find a constructor " + type));
			}
			if (type.isIntegerType()) {
				return values.integer(yield);
			}
			if (type.isRealType()) {
				return values.real(yield);
			}
			if (type.isBoolType()) {
				if (yield.equals("true")) {
					return values.bool(true);
				}
				if (yield.equals("false")) {
					return values.bool(false);
				}
				throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, null, null, "Bool type does not match with " + yield));
			}
			if (type.isStringType()) {
				return values.string(yield);
			}
			
			throw RuntimeExceptionFactory.illegalArgument(tree, null, null, "Missing lexical constructor");
		}
		
		//Set implementation added here by Jurgen at 19/07/12 16:45
		if (TreeAdapter.isList(tree)) {
			if (type.isListType() || splicing) {
				Type elementType = splicing ? type : type.getElementType();
				IListWriter w = values.listWriter(elementType);
				for (IValue arg: TreeAdapter.getListASTArgs(tree)) {
					w.append(implode(store, elementType, (IConstructor) arg, false, ctx));
				}
				return w.done();
			}
			else if (type.isSetType()) {
				Type elementType = splicing ? type : type.getElementType();
				ISetWriter w = values.setWriter(elementType);
				for (IValue arg: TreeAdapter.getListASTArgs(tree)) {
					w.insert(implode(store, elementType, (IConstructor) arg, false, ctx));
				}
				return w.done();
			}
			else {
				throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, null, null, "Cannot match list with " + type));
			}
		}
		//Changes end here
		
		if (TreeAdapter.isOpt(tree) && type.isBoolType()) {
			IList args = TreeAdapter.getArgs(tree);
			if (args.isEmpty()) {
				return values.bool(false);
			}
			return values.bool(true);
		}
		
		if (TreeAdapter.isOpt(tree)) {
			if (!type.isListType()) {
				throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, null, null, "Optional should match with a list and not " + type));
			}
			Type elementType = type.getElementType();
			IListWriter w = values.listWriter(elementType);
			for (IValue arg: TreeAdapter.getASTArgs(tree)) {
				IValue implodedArg = implode(store, elementType, (IConstructor) arg, true, ctx);
				if (implodedArg instanceof IList) {
					// splicing
					for (IValue nextArg: (IList)implodedArg) {
						w.append(nextArg);
					}
				}
				else {
					w.append(implodedArg);
				}
				// opts should have one argument (if any at all)
				break;
			}
			return w.done();
		}
		
		if (TreeAdapter.isAmb(tree)) {
			if (!type.isSetType()) {
				throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, null, null, "Ambiguous node should match with set and not " + type));
			}
			Type elementType = type.getElementType();
			ISetWriter w = values.setWriter(elementType);
			for (IValue arg: TreeAdapter.getAlternatives(tree)) {
				w.insert(implode(store, elementType, (IConstructor) arg, false, ctx));
			}
			return w.done();
		}
		
		if (ProductionAdapter.hasAttribute(TreeAdapter.getProduction(tree), Factory.Attribute_Bracket)) {
			return implode(store, type, (IConstructor) TreeAdapter.getASTArgs(tree).get(0), false, ctx);
		}
		
		if (TreeAdapter.isAppl(tree)) {
			IList args = TreeAdapter.getASTArgs(tree);
			// this could be optimized.
			int i = 0;
			int length = args.length();
			while (i < length) {
				if (TreeAdapter.isEmpty((IConstructor) args.get(i))) {
					length--;
					args = args.delete(i);
				}
				else {
					i++;
				}
			}
			
			
			java.lang.String constructorName = unescapedConsName(tree);			
			
			if (constructorName == null) {
				if (length == 1) {
					// jump over injection
					return implode(store, type, (IConstructor) args.get(0), splicing, ctx);
				}
				
				// make a tuple
				if (!type.isTupleType()) {
					throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, null, null, "Constructor does not match with " + type));
				}
				
				if (length != type.getArity()) {
					throw new Backtrack(RuntimeExceptionFactory.arityMismatch(type.getArity(), length, null, null));
				}

				return values.tuple(implodeArgs(store, type, args, ctx));
			}
			
			
			// make a constructor
			if (!type.isAbstractDataType()) {
				throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, null, null, "Constructor (" + constructorName + ") should match with abstract data type and not with " + type));
			}

			Set<Type> conses = findConstructors(type, constructorName, length, store);
			Iterator<Type> iter = conses.iterator();
			while (iter.hasNext()) {
				try {
					Type cons = iter.next();
					ISourceLocation loc = TreeAdapter.getLocation(tree);
					IValue[] implodedArgs = implodeArgs(store, cons, args, ctx);
					IConstructor ast = makeConstructor(constructorName, ctx, implodedArgs);
					return ast.setAnnotation("location", loc);
				}
				catch (Backtrack b) {
					continue;
				}
			}
			
		}
		
		throw new Backtrack(RuntimeExceptionFactory.illegalArgument(tree, null, null, "Cannot find a constructor " + type));
	}
	
	
	
	
	private static IConstructor checkPreconditions(IValue start, Type reified) {
		if (!(reified instanceof ReifiedType)) {
		   throw RuntimeExceptionFactory.illegalArgument(start, null, null, "A reified type is required instead of " + reified);
		}
		
		Type nt = reified.getTypeParameters().getFieldType(0);
		
		if (!(nt instanceof NonTerminalType)) {
			throw RuntimeExceptionFactory.illegalArgument(start, null, null, "A non-terminal type is required instead of  " + nt);
		}
		
		IConstructor symbol = ((NonTerminalType) nt).getSymbol();
		
		return symbol;
	}
	
	/*
	 * Rational
	 */

	public IValue numerator(IRational n)
	{
		return n.numerator();
	}

	public IValue denominator(IRational n)
	{
	  return n.denominator();
	}

	public IValue remainder(IRational n)
	{
	  return n.remainder();
	}
	
	/*
	 * Relation
	 */
	
	/*
	 * Set
	 */
	public IValue getOneFrom(ISet st)
	// @doc{getOneFrom -- pick a random element from a set}
	{
		int sz = st.size();

		if (sz == 0) {
			throw RuntimeExceptionFactory.emptySet(null, null);
		}
		int k = random.nextInt(sz);
		int i = 0;

		for (IValue v : st) {
			if (i == k) {
				return v;
			}
			i++;
		}
		
		throw RuntimeExceptionFactory.emptySet(null, null);
	}

	public IValue isEmpty(ISet st)
	//@doc{isEmpty -- is set empty?}
	{
		return values.bool(st.size() == 0);
	}
	
	public IValue size(ISet st)
	// @doc{size -- number of elements in a set}
	{
		return values.integer(st.size());
	}
	
	public IMap index(IRelation s) {
		Map<IValue, ISetWriter> map = new HashMap<IValue, ISetWriter>(s.size());
		
		for (IValue t : s) {
			ITuple tuple = (ITuple) t;
			IValue key = tuple.get(0);
			IValue value = tuple.get(1);
			
			ISetWriter writer = map.get(key);
			if (writer == null) {
				writer = values.setWriter();
				map.put(key, writer);
			}
			writer.insert(value);
		}
		
		IMapWriter mapWriter = values.mapWriter();
		for (IValue key : map.keySet()) {
			mapWriter.put(key, map.get(key).done());
		}
		
		return mapWriter.done();
	}

	public IValue takeOneFrom(ISet st)
	// @doc{takeOneFrom -- remove an arbitrary element from a set,
	//      returns the element and the modified set}
	{
		int n = st.size();

		if (n > 0) {
			int i = 0;
			int k = random.nextInt(n);
			IValue pick = null;
			ISetWriter w = st.getType().writer(values);

			for (IValue v : st) {
				if (i == k) {
					pick = v;
				} else {
					w.insert(v);
				}
				i++;
			}
			return values.tuple(pick, w.done());
		}
		throw RuntimeExceptionFactory.emptySet(null, null);
	}

	public IValue toList(ISet st)
	// @doc{toList -- convert a set to a list}
	{
		Type resultType = types.listType(st.getElementType());
		IListWriter w = resultType.writer(values);

		for (IValue v : st) {
			w.insert(v);
		}

		return w.done();
	}

	public IValue toMap(IRelation st)
	// @doc{toMap -- convert a set of tuples to a map; value in old map is associated with a set of keys in old map}
	{
		Type tuple = st.getElementType();
		Type keyType = tuple.getFieldType(0);
		Type valueType = tuple.getFieldType(1);
		Type valueSetType = types.setType(valueType);

		HashMap<IValue,ISetWriter> hm = new HashMap<IValue,ISetWriter>();

		for (IValue v : st) {
			ITuple t = (ITuple) v;
			IValue key = t.get(0);
			IValue val = t.get(1);
			ISetWriter wValSet = hm.get(key);
			if(wValSet == null){
				wValSet = valueSetType.writer(values);
				hm.put(key, wValSet);
			}
			wValSet.insert(val);
		}
		
		Type resultType = types.mapType(keyType, valueSetType);
		IMapWriter w = resultType.writer(values);
		for(IValue v : hm.keySet()){
			w.put(v, hm.get(v).done());
		}
		return w.done();
	}
	
	public IValue toMapUnique(IRelation st)
	// @doc{toMapUnique -- convert a set of tuples to a map; keys are unique}
	{
		Type tuple = st.getElementType();
		Type resultType = types.mapType(tuple.getFieldType(0), tuple
				.getFieldType(1));

		IMapWriter w = resultType.writer(values);
		HashSet<IValue> seenKeys = new HashSet<IValue>();

		for (IValue v : st) {
			ITuple t = (ITuple) v;
			IValue key = t.get(0);
			IValue val = t.get(1);
			if(seenKeys.contains(key)) 
				throw RuntimeExceptionFactory.MultipleKey(key, null, null);
			seenKeys.add(key);
			w.put(key, val);
		}
		return w.done();
	}

	public IValue toString(ISet st)
	// @doc{toString -- convert a set to a string}
	{
		return values.string(st.toString());
	}
	
	/*
	 * String
	 */
	
	public IBool isValidCharacter(IInteger i) {
		return values.bool(Character.isValidCodePoint(i.intValue()));
	}
	
	public IValue stringChar(IInteger i) {
		int intValue = i.intValue();
		if (Character.isValidCodePoint(intValue)) {
			return values.string(intValue);
		}
		else {
			throw RuntimeExceptionFactory.illegalCharacter(i, null, null);
		}
	}
	
	public IValue stringChars(IList lst){
		int[] chars = new int[lst.length()];
		
		for (int i = 0; i < lst.length(); i ++) {
			chars[i] = ((IInteger) lst.get(i)).intValue();
			if (!Character.isValidCodePoint(chars[i])) {
				throw RuntimeExceptionFactory.illegalCharacter(values.integer(chars[i]), null, null);
			}
		}
		
		return values.string(chars);
	}
	
	public IValue charAt(IString s, IInteger i) throws IndexOutOfBoundsException
	//@doc{charAt -- return the character at position i in string s.}
	{
	  try {
		return values.integer(s.charAt(i.intValue()));
	  }
	  catch (IndexOutOfBoundsException e) {
	    throw RuntimeExceptionFactory.indexOutOfBounds(i, null, null);
	  }
	}

	public IValue endsWith(IString s, IString suffix)
	//@doc{endWith -- returns true if string s ends with given string suffix.}
	{
	  return values.bool(s.getValue().endsWith(suffix.getValue()));
	}
	
	public IString trim(IString s) {
		return values.string(s.getValue().trim());
	}
	
	public IString squeeze(IString src, IString charSet) {
		//@{http://commons.apache.org/lang/api-2.6/index.html?org/apache/commons/lang/text/package-summary.html}
		String s = CharSetUtils.squeeze(src.getValue(), charSet.getValue());
		return values.string(s);
	}
	
	public IString capitalize(IString src) {
		return values.string(WordUtils.capitalize(src.getValue()));
	}
	
	public IList split(IString sep, IString src) {
		String[] lst = src.getValue().split(Pattern.quote(sep.getValue()));
		IListWriter lw = values.listWriter(types.stringType());
		for (String s: lst) {
			lw.append(values.string(s));
		}
		return lw.done();
	}
	
	public IString wrap(IString src, IInteger wrapLength) {
		String s = WordUtils.wrap(src.getValue(), wrapLength.intValue());
		return values.string(s);
	}

	public IValue format(IString s, IString dir, IInteger n, IString pad)
	//@doc{format -- return string of length n, with s placed according to dir (left/center/right) and padded with pad}
	{
	    StringBuffer res = new StringBuffer();
	    int sLen = s.getValue().length();
	    int nVal = n.intValue();
	    if(sLen > nVal){
	       return s;
	    }
	    int padLen = pad.getValue().length();
	    java.lang.String dirVal = dir.getValue();
	    int start;
	    
	    if(dirVal.equals("right"))
	       start = nVal - sLen;
	    else if(dirVal.equals("center"))
	       start = (nVal - sLen)/2;
	    else
	       start = 0;
	    
	    int i = 0;
	    while(i < start){
	         if(i + padLen < start){
	         	res.append(pad.getValue());
	         	i+= padLen;
	         } else {
	         	res.append(pad.getValue().substring(0, start - i));
	         	i += start -i;
	         }
	    }
	    res.append(s.getValue());
	    i = start + sLen;
	    while(i < nVal){
	         if(i + padLen < nVal){
	         	res.append(pad.getValue());
	         	i += padLen;
	         } else {
	         	res.append(pad.getValue().substring(0, nVal - i));
	         	i += nVal - i;
	         }
	    }
	    return values.string(res.toString());
	}
	
	public IValue isEmpty(IString s)
	//@doc{isEmpty -- is string empty?}
	{
		return values.bool(s.getValue().length() == 0);
	}

	public IValue reverse(IString s)
	//@doc{reverse -- return string with all characters in reverse order.}
	{
	   return s.reverse();
	}

	public IValue size(IString s)
	//@doc{size -- return the length of string s.}
	{
	  return values.integer(s.length());
	}

	public IValue startsWith(IString s, IString prefix)
	//@doc{startsWith -- return true if string s starts with the string prefix.}
	{
	  return values.bool(s.getValue().startsWith(prefix.getValue()));
	}

	public IValue substring(IString s, IInteger begin) {
		try {
			return s.substring(begin.intValue());
		} catch (IndexOutOfBoundsException e) {
			throw RuntimeExceptionFactory.indexOutOfBounds(begin, null, null);
		}
	}
	
	public IValue substring(IString s, IInteger begin, IInteger end) {
		try {
			return s.substring(begin.intValue(),end.intValue());
		} catch (IndexOutOfBoundsException e) {
			int bval = begin.intValue();
			IInteger culprit = (bval < 0 || bval >= s.length()) ? begin : end;
		    throw RuntimeExceptionFactory.indexOutOfBounds(culprit, null, null);
		}
	
	}
	
	public IValue toInt(IString s)
	//@doc{toInt -- convert a string s to integer}
	{
		try {
			java.lang.String sval = s.getValue();
			boolean isNegative = false;
			int radix = 10;
			
			if (sval.equals("0")) {
				return values.integer(0);
			}
			
			if (sval.startsWith("-")) {
				isNegative = true;
				sval = sval.substring(1);
			}
			if (sval.startsWith("0x") || sval.startsWith("0X")) {
				radix = 16;
				sval = sval.substring(2);
			} else if (sval.startsWith("0")) {
				radix = 8;
				sval = sval.substring(1);
			}
			BigInteger bi = new BigInteger(isNegative ? "-" + sval : sval, radix);
			return values.integer(bi.toString());
		}
		catch (NumberFormatException e){
			throw RuntimeExceptionFactory.illegalArgument(null, null);
		}
	}
	
	public IValue toInt(IString s, IInteger r)
	{
		try {
			java.lang.String sval = s.getValue();
			boolean isNegative = false;
			int radix = r.intValue();
			
			if (sval.equals("0")) {
				return values.integer(0);
			}
			
			if (sval.startsWith("-")) {
				isNegative = true;
				sval = sval.substring(1);
			}
			BigInteger bi = new BigInteger(isNegative ? "-" + sval : sval, radix);
			return values.integer(bi.toString());
		}
		catch (NumberFormatException e){
			throw RuntimeExceptionFactory.illegalArgument(null, null);
		}
	}
	
	public IValue toReal(IString s)
	//@doc{toReal -- convert a string s to a real}
	{
		try {
			return values.real(s.getValue());
		}
		catch (NumberFormatException e){
			throw RuntimeExceptionFactory.illegalArgument(null, null);
		}
	}

	public IValue toLowerCase(IString s)
	//@doc{toLowerCase -- convert all characters in string s to lowercase.}
	{
	  return values.string(s.getValue().toLowerCase());
	}

	public IValue toUpperCase(IString s)
	//@doc{toUpperCase -- convert all characters in string s to uppercase.}
	{
	  return values.string(s.getValue().toUpperCase());
	}
	
	private boolean match(char[] subject, int i, char [] pattern){
		if(i + pattern.length > subject.length)
			return false;
		for(int k = 0; k < pattern.length; k++){
			if(subject[i] != pattern[k])
				return false;
			i++;
		}
		return true;
	}
	
	public IValue replaceAll(IString str, IString find, IString replacement){
		StringBuilder b = new StringBuilder(str.getValue().length() * 2); 
		char [] input = str.getValue().toCharArray();
		char [] findChars = find.getValue().toCharArray();
		char [] replChars = replacement.getValue().toCharArray();
		
		int i = 0;
		boolean matched = false;
		while(i < input.length){
			if(match(input,i,findChars)){
				matched = true;
				b.append(replChars);
				i += findChars.length;
			} else {
				b.append(input[i]);
				i++;
			}
		}
		return (!matched) ? str : values.string(b.toString());
	}
	
	public IValue replaceFirst(IString str, IString find, IString replacement){
		StringBuilder b = new StringBuilder(str.getValue().length() * 2); 
		char [] input = str.getValue().toCharArray();
		char [] findChars = find.getValue().toCharArray();
		char [] replChars = replacement.getValue().toCharArray();
		
		int i = 0;
		boolean matched = false;
		while(i < input.length){
			if(!matched && match(input,i,findChars)){
				matched = true;
				b.append(replChars);
				i += findChars.length;
				
			} else {
				b.append(input[i]);
				i++;
			}
		}
		return (!matched) ? str : values.string(b.toString());
	}
	
	public IValue replaceLast(IString str, IString find, IString replacement){
		StringBuilder b = new StringBuilder(str.getValue().length() * 2); 
		char [] input = str.getValue().toCharArray();
		char [] findChars = find.getValue().toCharArray();
		char [] replChars = replacement.getValue().toCharArray();
		
		int i = input.length - findChars.length;
		while(i >= 0){
			if(match(input,i,findChars)){
				for(int j = 0; j < i; j++)
					b.append(input[j]);
				b.append(replChars);
				for(int j = i + findChars.length; j < input.length; j++)
					b.append(input[j]);
				return values.string(b.toString());
			}
			i--;
		}
		return str;
	}
	
	
	public IValue escape(IString str, IMap substitutions) {
		StringBuilder b = new StringBuilder(str.getValue().length() * 2); 
		char[] input = str.getValue().toCharArray();
		
		for (char c : input) {
			IString sub = (IString) substitutions.get(values.string(Character.toString(c)));
			if (sub != null) {
				b.append(sub.getValue());
			}
			else {
				b.append(c);
			}
		}
		return values.string(b.toString());
	}
	
	public IValue contains(IString str, IString find){
		return values.bool(str.getValue().indexOf(find.getValue()) >= 0);
	}
	
	public IValue findAll(IString str, IString find){
		char[] input = str.getValue().toCharArray();
		char [] findChars = find.getValue().toCharArray();
		IListWriter w = values.listWriter(types.integerType());
		
		for(int i = 0; i <= input.length - findChars.length; i++){
			if(match(input, i, findChars)){
				w.append(values.integer(i));
			}
		}
		return w.done();
	}
	
	public IValue findFirst(IString str, IString find){
		char[] input = str.getValue().toCharArray();
		char [] findChars = find.getValue().toCharArray();
		
		for(int i = 0; i <= input.length - findChars.length; i++){
			if(match(input, i, findChars)){
				 return values.integer(i);
			}
		}
		return values.integer(-1);
	}
	
	public IValue findLast(IString str, IString find){
		char[] input = str.getValue().toCharArray();
		char [] findChars = find.getValue().toCharArray();
		
		for(int i = input.length - findChars.length; i >= 0; i--){
			if(match(input, i, findChars)){
				 return values.integer(i);
			}
		}
		return values.integer(-1);
	}
	

		
	/*
	 * ToString
	 */
	
	public IString toString(IValue value)
	{
		if (value.getType() == Factory.Tree) {
			return values.string(TreeAdapter.yield((IConstructor) value));
		}
		else if (value.getType().isSubtypeOf(Factory.Type)) {
			return values.string(SymbolAdapter.toString((IConstructor) ((IConstructor) value).get("symbol")));
		}
		if (value.getType().isStringType()) {
			return (IString) value;
		}
		return values.string(value.toString());
	}
	
	/*
	 *  !!EXPERIMENTAL!!
	 * Tuple
	 */
	
	public IList fieldsOf(IValue v){
		if(!v.getType().isTupleType())
			throw RuntimeExceptionFactory.illegalArgument(v, null, null, "argument of type tuple is required");
		ITuple tp = (ITuple) v;
		Type tt = tp.getType();
		int a = tt.getArity();
		Type listType = types.listType(types.stringType());
		IListWriter w = listType.writer(values);
		for(int i = 0; i < a; i++){
			String fname = tt.getFieldName(i);
			if(fname == null)
				fname = "";
			w.append(values.string(fname));
		}
		return w.done();
	}
	
	/*
	 * ValueIO
	 */
	
	public IValue readBinaryValueFile(IValue type, ISourceLocation loc, IEvaluatorContext ctx){
		TypeStore store = new TypeStore();
		Type start = tr.valueToType((IConstructor) type, store);
		
		InputStream in = null;
		try{
			in = ctx.getResolverRegistry().getInputStream(loc.getURI());
			return new BinaryValueReader().read(values, store, start, in);
		}catch(IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}catch(Exception e){
			e.printStackTrace();
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
	
	public IValue readTextValueFile(IValue type, ISourceLocation loc, IEvaluatorContext ctx){
		TypeStore store = new TypeStore();
		Type start = tr.valueToType((IConstructor) type, store);
		
		InputStream in = null;
		try{
			in = ctx.getResolverRegistry().getInputStream(loc.getURI());
			return new StandardTextReader().read(values, store, start, new InputStreamReader(in, "UTF8"));
		}catch(IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
	
	public IValue readTextValueString(IValue type, IString input) {
		TypeStore store = new TypeStore();
		Type start = tr.valueToType((IConstructor) type, store);
		
		StringReader in = new StringReader(input.getValue());
		try {
			return new StandardTextReader().read(values, store, start, in);
		} catch (FactTypeUseException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	public void writeBinaryValueFile(ISourceLocation loc, IValue value, IEvaluatorContext ctx){
		OutputStream out = null;
		try{
			out = ctx.getResolverRegistry().getOutputStream(loc.getURI(), false); 
			new BinaryValueWriter().write(value, out);
		}catch (IOException ioex){
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
		}finally{
			if(out != null){
				try{
					out.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
	
	public void writeTextValueFile(ISourceLocation loc, IValue value, IEvaluatorContext ctx){
		OutputStream out = null;
		try{
			out = ctx.getResolverRegistry().getOutputStream(loc.getURI(), false);
			new StandardTextWriter().write(value, new OutputStreamWriter(out, "UTF8"));
		}
		 catch(IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				}
				catch(IOException ioex) {
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), ctx.getCurrentAST(), ctx.getStackTrace());
				}
			}
		}
	}
	
	public IBool rexpMatch(IString s, IString re) {
		if(Pattern.matches(re.getValue(), s.getValue()))
			return values.bool(true);
		else
			return values.bool(false);
	}
}

// Utilities used by Graph
//TODO: Why is this code in the library? This should be done in pure Rascal.

class Distance{
	public int intval;
	
	Distance(int n){
		intval = n;
	}
}

class NodeComparator implements Comparator<IValue> {
	private final HashMap<IValue,Distance> distance;
	
	NodeComparator(HashMap<IValue,Distance> distance){
		this.distance = distance;
	}

	public int compare(IValue arg0, IValue arg1) {
		int d0 = distance.get(arg0).intval;
		int d1 = distance.get(arg1).intval;
		
		return d0 < d1 ? -1 : ((d0 == d1) ? 0 : 1);
	}
}
