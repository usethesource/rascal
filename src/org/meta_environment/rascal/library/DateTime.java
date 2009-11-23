package org.meta_environment.rascal.library;

import java.util.Locale;

import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.values.ValueFactoryFactory;

public class DateTime {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();

	private static IValue createNewTimeValue(org.joda.time.DateTime jdt2) {
		int hourOffset = jdt2.getZone().getOffset(jdt2.getMillis())/3600000;
		int minuteOffset = (jdt2.getZone().getOffset(jdt2.getMillis())/60000)%60;				
		return values.time(jdt2.getHourOfDay(), jdt2.getMinuteOfHour(),
				jdt2.getSecondOfMinute(), jdt2.getMillisOfSecond(), 
				hourOffset, minuteOffset);
	}		
	
	
	public static IValue now()
	//@doc{Get the current datetime.}
	{
	   return values.datetime(org.joda.time.DateTimeUtils.currentTimeMillis());
	}

	public static IValue createDate(IInteger year, IInteger month, IInteger day) 
	//@doc{Create a new date.}
	{
		return values.date(year.intValue(), month.intValue(), day.intValue());
	}
	
	public static IValue createTime(IInteger hour, IInteger minute, IInteger second,
			IInteger millisecond)
	//@doc{Create a new time.}
	{
		return values.time(hour.intValue(), minute.intValue(), second.intValue(), millisecond.intValue());
	}

	public static IValue createTimeWZone(IInteger hour, IInteger minute, IInteger second,
			IInteger millisecond, IInteger timezoneHourOffset, IInteger timezoneMinuteOffset)
	//@doc{Create a new time with the given numeric timezone offset.}
	{
		return values.time(hour.intValue(), minute.intValue(), second.intValue(),
				millisecond.intValue(), timezoneHourOffset.intValue(), timezoneMinuteOffset.intValue());
	}
	
	public static IValue createDateTime(IInteger year, IInteger month, IInteger day, 
			IInteger hour, IInteger minute, IInteger second, IInteger millisecond)
	//@doc{Create a new datetime.}
	{
		return values.datetime(year.intValue(), month.intValue(), day.intValue(), hour.intValue(),
				minute.intValue(), second.intValue(), millisecond.intValue());
	}

	
	public static IValue createDateTimeWZone(IInteger year, IInteger month, IInteger day,
			IInteger hour, IInteger minute, IInteger second, IInteger millisecond, 
			IInteger timezoneHourOffset, IInteger timezoneMinuteOffset)
	//@doc{Create a new datetime with the given numeric timezone offset.}
	{
		return values.datetime(year.intValue(), month.intValue(), day.intValue(), hour.intValue(),
				minute.intValue(), second.intValue(), millisecond.intValue(), timezoneHourOffset.intValue(),
				timezoneMinuteOffset.intValue());
	}
		
	public static IValue joinDateAndTime(IDateTime date, IDateTime time)
	//@doc{Create a new datetime by combining a date and a time.}
	{
		return values.datetime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
				time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute(),
				time.getMillisecondsOfSecond(), time.getTimezoneOffsetHours(), time.getTimezoneOffsetMinutes());
	}

	public static IValue splitDateTime(IDateTime dt)
	//@doc{Split an existing datetime into a tuple with the date and the time.}
	{
		return values.tuple(values.date(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth()),
				values.time(dt.getHourOfDay(), dt.getMinuteOfHour(), dt.getSecondOfMinute(),
						dt.getMillisecondsOfSecond(), dt.getTimezoneOffsetHours(), dt.getTimezoneOffsetMinutes()));
	}
	
	
	public static IValue incrementYearsBy(IDateTime dt, IInteger n)
	//@doc{Increment the years by a given amount.}
	{
		if (!dt.isTime()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.plusYears(n.intValue());
			if (dt.isDate()) {
				return values.date(jdt2.getYear(), jdt2.getMonthOfYear(), jdt2.getDayOfMonth());
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot increment the years on a time value.", null, null);
	}
	
	public static IValue incrementMonthsBy(IDateTime dt, IInteger n)
	//@doc{Increment the months by a given amount.}
	{
		if (!dt.isTime()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.plusMonths(n.intValue());
			if (dt.isDate()) {
				return values.date(jdt2.getYear(), jdt2.getMonthOfYear(), jdt2.getDayOfMonth());
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot increment the months on a time value.", null, null);
	}

	public static IValue incrementDaysBy(IDateTime dt, IInteger n)
	//@doc{Increment the days by a given amount.}
	{
		if (!dt.isTime()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.plusDays(n.intValue());
			if (dt.isDate()) {
				return values.date(jdt2.getYear(), jdt2.getMonthOfYear(), jdt2.getDayOfMonth());
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot increment the days on a time value.", null, null);
	}

	public static IValue incrementHoursBy(IDateTime dt, IInteger n)
	//@doc{Increment the hours by a given amount.}
	{
		if (!dt.isDate()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.plusHours(n.intValue());
			if (dt.isTime()) {
				return createNewTimeValue(jdt2);
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot increment the hours on a date value.", null, null);
	}		

	public static IValue incrementMinutesBy(IDateTime dt, IInteger n)
	//@doc{Increment the minutes by a given amount.}
	{
		if (!dt.isDate()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.plusMinutes(n.intValue());
			if (dt.isTime()) {
				return createNewTimeValue(jdt2);
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot increment the minutes on a date value.", null, null);
	}		
	
	public static IValue incrementSecondsBy(IDateTime dt, IInteger n)
	//@doc{Increment the seconds by a given amount.}
	{
		if (!dt.isDate()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.plusSeconds(n.intValue());
			if (dt.isTime()) {
				return createNewTimeValue(jdt2);
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot increment the seconds on a date value.", null, null);
	}
	
	public static IValue incrementMillisecondsBy(IDateTime dt, IInteger n)
	//@doc{Increment the milliseconds by a given amount.}
	{
		if (!dt.isDate()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.plusMillis(n.intValue());
			if (dt.isTime()) {
				return createNewTimeValue(jdt2);
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot increment the milliseconds on a date value.", null, null);
	}

	public static IValue decrementYearsBy(IDateTime dt, IInteger n)
	//@doc{Decrement the years by a given amount.}
	{
		if (!dt.isTime()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.minusYears(n.intValue());
			if (dt.isDate()) {
				return values.date(jdt2.getYear(), jdt2.getMonthOfYear(), jdt2.getDayOfMonth());
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot decrement the years on a time value.", null, null);
	}		

	public static IValue decrementMonthsBy(IDateTime dt, IInteger n)
	//@doc{Decrement the months by a given amount.}
	{
		if (!dt.isTime()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.minusMonths(n.intValue());
			if (dt.isDate()) {
				return values.date(jdt2.getYear(), jdt2.getMonthOfYear(), jdt2.getDayOfMonth());
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot decrement the months on a time value.", null, null);
	}	

	public static IValue decrementDaysBy(IDateTime dt, IInteger n)
	//@doc{Decrement the days by a given amount.}
	{
		if (!dt.isTime()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.minusDays(n.intValue());
			if (dt.isDate()) {
				return values.date(jdt2.getYear(), jdt2.getMonthOfYear(), jdt2.getDayOfMonth());
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot decrement the days on a time value.", null, null);
	}
	
	public static IValue decrementHoursBy(IDateTime dt, IInteger n)
	//@doc{Decrement the hours by a given amount.}
	{
		if (!dt.isDate()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.minusHours(n.intValue());
			if (dt.isTime()) {
				return createNewTimeValue(jdt2);
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot decrement the hours on a date value.", null, null);
	}		

	public static IValue decrementMinutesBy(IDateTime dt, IInteger n)
	//@doc{Decrement the minutes by a given amount.}
	{
		if (!dt.isDate()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.minusMinutes(n.intValue());
			if (dt.isTime()) {
				return createNewTimeValue(jdt2);
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot decrement the minutes on a date value.", null, null);
	}		

	public static IValue decrementSecondsBy(IDateTime dt, IInteger n)
	//@doc{Decrement the seconds by a given amount.}
	{
		if (!dt.isDate()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.minusSeconds(n.intValue());
			if (dt.isTime()) {
				return createNewTimeValue(jdt2);
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot decrement the seconds on a date value.", null, null);
	}		

	public static IValue decrementMillisecondsBy(IDateTime dt, IInteger n)
	//@doc{Decrement the milliseconds by a given amount.}
	{
		if (!dt.isDate()) {
			org.joda.time.DateTime jdt = new org.joda.time.DateTime(dt.getInstant());
			org.joda.time.DateTime jdt2 = jdt.minusMillis(n.intValue());
			if (dt.isTime()) {
				return createNewTimeValue(jdt2);
			}
			return values.datetime(jdt2.getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot decrement the milliseconds on a date value.", null, null);
	}		

	public static IValue createDurationInternal(IDateTime dStart, IDateTime dEnd) {
		// dStart and dEnd both have to be dates, times, or datetimes
		IValue duration = null;
		if (dStart.isDate()) {
			if (dEnd.isDate()) {
				Period p = new Period(dStart.getInstant(), dEnd.getInstant());
				duration = values.tuple(values.integer(p.getYears()),
						values.integer(p.getMonths()), values.integer((p.getWeeks()*7)+p.getDays()),
						values.integer(0), values.integer(0), values.integer(0),
						values.integer(0));
			} else if (dEnd.isTime()) {
				throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot determine the duration between a date with no time and a time with no date.", null, null);	
			} else {
				throw RuntimeExceptionFactory.invalidUseOfDateTimeException("Cannot determine the duration between a date with no time and a datetime.", null, null);					
			}
		} else if (dStart.isTime()) {
			if (dEnd.isTime()) {
				Period p = new Period(dStart.getInstant(), dEnd.getInstant());
				duration = values.tuple(values.integer(0), values.integer(0), 
						values.integer(0), values.integer(p.getHours()), 
						values.integer(p.getMinutes()), values.integer(p.getSeconds()),
						values.integer(p.getMillis()));
			} else if (dEnd.isDate()) {
				throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot determine the duration between a time with no date and a date with no time.", null, null);	
			} else {
				throw RuntimeExceptionFactory.invalidUseOfDateTimeException("Cannot determine the duration between a time with no date and a datetime.", null, null);					
			}
		} else {
			if (dEnd.isDateTime()) {
				Period p = new Period(dStart.getInstant(), dEnd.getInstant());
				duration = values.tuple(values.integer(p.getYears()), 
						values.integer(p.getMonths()), values.integer((p.getWeeks()*7)+p.getDays()), 
						values.integer(p.getHours()), values.integer(p.getMinutes()), 
						values.integer(p.getSeconds()), values.integer(p.getMillis()));
			} else if (dEnd.isDate()) {
				throw RuntimeExceptionFactory.invalidUseOfDateException("Cannot determine the duration between a datetime and a date with no time.", null, null);	
			} else {
				throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot determine the duration between a datetime and a time with no date.", null, null);					
			}
		}
		return duration;
	}
	
	public static IValue parseDate(IString inputDate, IString formatString)
	//@doc{Parse an input date given as a string using the given format string}
	{	
		try {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString.getValue());
			org.joda.time.DateTime dt = fmt.parseDateTime(inputDate.getValue());
			return values.date(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input date: " + inputDate.getValue() + 
					" using format string: " + formatString.getValue(), null, null);
		}
	}
	
	public static IValue parseDateInLocale(IString inputDate, IString formatString, IString locale) 
	//@doc{Parse an input date given as a string using a specific locale and format string}
	{
		try {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString.getValue()).withLocale(new Locale(locale.getValue()));
			org.joda.time.DateTime dt = fmt.parseDateTime(inputDate.getValue());
			return values.date(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth());
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input date: " + inputDate.getValue() + 
					" using format string: " + formatString.getValue() + " in locale: " + locale.getValue(), null, null);
		}
	}

	public static IValue parseTime(IString inputTime, IString formatString) 
	//@doc{Parse an input time given as a string using the given format string}
	{
		try {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString.getValue());
			org.joda.time.DateTime dt = fmt.parseDateTime(inputTime.getValue());
			return createNewTimeValue(dt);
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input time: " + inputTime.getValue() + 
					" using format string: " + formatString.getValue(), null, null);
		}
	}
	
	public static IValue parseTimeInLocale(IString inputTime, IString formatString, IString locale) 
	//@doc{Parse an input time given as a string using a specific locale and format string}
	{
		try {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString.getValue()).withLocale(new Locale(locale.getValue()));
			org.joda.time.DateTime dt = fmt.parseDateTime(inputTime.getValue());
			return createNewTimeValue(dt);
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input time: " + inputTime.getValue() + 
					" using format string: " + formatString.getValue() + " in locale: " + locale.getValue(), null, null);
		}
	}

	public static IValue parseDateTime(IString inputDateTime, IString formatString) 
	//@doc{Parse an input datetime given as a string using the given format string}
	{
		try {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString.getValue());
			org.joda.time.DateTime dt = fmt.parseDateTime(inputDateTime.getValue());
			return values.datetime(dt.getMillis());
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input date: " + inputDateTime.getValue() + 
					" using format string: " + formatString.getValue(), null, null);
		}			
	}
	
	public static IValue parseDateTimeInLocale(IString inputDateTime, IString formatString, IString locale) 
	//@doc{Parse an input datetime given as a string using a specific locale and format string}
	{
		try {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString.getValue()).withLocale(new Locale(locale.getValue()));
			org.joda.time.DateTime dt = fmt.parseDateTime(inputDateTime.getValue());
			return values.datetime(dt.getMillis());
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimeParsingError("Cannot parse input datetime: " + inputDateTime.getValue() + 
					" using format string: " + formatString.getValue() + " in locale: " + locale.getValue(), null, null);
		}
	}

	public static IValue printDate(IDateTime inputDate, IString formatString) 
	//@doc{Print an input date using the given format string}
	{
		try {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString.getValue());
			org.joda.time.DateTime dt = new org.joda.time.DateTime(inputDate.getInstant()); 
			return values.string(dt.toString(fmt));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print date using format string: " + formatString.getValue(), null, null);
		}
	}

	public static IValue printDateDefault(IDateTime inputDate) 
	//@doc{Print an input date using a default format string}
	{
		DateTimeFormatter fmt = ISODateTimeFormat.date();
		org.joda.time.DateTime dt = new org.joda.time.DateTime(inputDate.getInstant()); 
		return values.string(dt.toString(fmt));
	}
	
	public static IValue printDateInLocale(IDateTime inputDate, IString formatString, IString locale) 
	//@doc{Print an input date using a specific locale and format string}
	{
		try {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString.getValue()).withLocale(new Locale(locale.getValue()));
			org.joda.time.DateTime dt = new org.joda.time.DateTime(inputDate.getInstant()); 
			return values.string(dt.toString(fmt));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print date using format string: " + formatString.getValue() +
					" in locale: " + locale.getValue(), null, null);
		}
	}

	public static IValue printDateDefaultInLocale(IDateTime inputDate, IString locale) 
	//@doc{Print an input date using a specific locale and a default format string}
	{
		DateTimeFormatter fmt = ISODateTimeFormat.date().withLocale(new Locale(locale.getValue()));
		org.joda.time.DateTime dt = new org.joda.time.DateTime(inputDate.getInstant()); 
		return values.string(dt.toString(fmt));
	}

	public static IValue printTime(IDateTime inputTime, IString formatString) 
	//@doc{Print an input time using the given format string}
	{
		try {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString.getValue());
			org.joda.time.DateTime dt = new org.joda.time.DateTime(inputTime.getInstant()); 
			return values.string(dt.toString(fmt));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print time using format string: " + formatString.getValue(), null, null);
		}			
	}
	
	public static IValue printTimeDefault(IDateTime inputTime) 
	//@doc{Print an input time using a default format string}
	{
		DateTimeFormatter fmt = ISODateTimeFormat.time();
		org.joda.time.DateTime dt = new org.joda.time.DateTime(inputTime.getInstant()); 
		return values.string(dt.toString(fmt));
	}
	
	public static IValue printTimeInLocale(IDateTime inputTime, IString formatString, IString locale) 
	//@doc{Print an input time using a specific locale and format string}
	{
		try {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString.getValue()).withLocale(new Locale(locale.getValue()));
			org.joda.time.DateTime dt = new org.joda.time.DateTime(inputTime.getInstant()); 
			return values.string(dt.toString(fmt));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print time using format string: " + formatString.getValue() +
					" in locale: " + locale.getValue(), null, null);
		}
	}

	public static IValue printTimeDefaultInLocale(IDateTime inputTime, IString locale) 
	//@doc{Print an input time using a specific locale and a default format string}
	{
		DateTimeFormatter fmt = ISODateTimeFormat.time().withLocale(new Locale(locale.getValue()));
		org.joda.time.DateTime dt = new org.joda.time.DateTime(inputTime.getInstant()); 
		return values.string(dt.toString(fmt));
	}

	public static IValue printDateTime(IDateTime inputDateTime, IString formatString) 
	//@doc{Print an input datetime using the given format string}
	{
		try {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString.getValue());
			org.joda.time.DateTime dt = new org.joda.time.DateTime(inputDateTime.getInstant()); 
			return values.string(dt.toString(fmt));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print datetime using format string: " + formatString.getValue(), null, null);
		}			
	}

	public static IValue printDateTimeDefault(IDateTime inputDateTime) 
	//@doc{Print an input datetime using a default format string}
	{
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		org.joda.time.DateTime dt = new org.joda.time.DateTime(inputDateTime.getInstant()); 
		return values.string(dt.toString(fmt));
	}
	
	public static IValue printDateTimeInLocale(IDateTime inputDateTime, IString formatString, IString locale) 
	//@doc{Print an input datetime using a specific locale and format string}
	{
		try {
			DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString.getValue()).withLocale(new Locale(locale.getValue()));
			org.joda.time.DateTime dt = new org.joda.time.DateTime(inputDateTime.getInstant()); 
			return values.string(dt.toString(fmt));
		} catch (IllegalArgumentException iae) {
			throw RuntimeExceptionFactory.dateTimePrintingError("Cannot print datetime using format string: " + formatString.getValue() +
					" in locale: " + locale.getValue(), null, null);
		}
	}

	public static IValue printDateTimeDefaultInLocale(IDateTime inputDateTime, IString locale) 
	//@doc{Print an input datetime using a specific locale and a default format string}
	{
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime().withLocale(new Locale(locale.getValue()));
		org.joda.time.DateTime dt = new org.joda.time.DateTime(inputDateTime.getInstant()); 
		return values.string(dt.toString(fmt));
	}

	public static IValue dayDiff(IDateTime dtStart, IDateTime dtEnd)
	//@doc{Increment the years by a given amount.}
	{
		if (!(dtStart.isTime() || dtEnd.isTime())) {
			org.joda.time.Interval iv = new org.joda.time.Interval(dtStart.getInstant(), dtEnd.getInstant());
			return values.integer(iv.toPeriod(org.joda.time.PeriodType.days()).getDays());
		}
		throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot calculate the days between two time values.", null, null);
	}	
}
