package org.rascalmpl.library.experiments.scm;

import java.util.Date;

import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Timer {

	private static final IValueFactory VF = ValueFactoryFactory.getValueFactory();
	private static Date lastTimer;
	
	public Timer(IValueFactory factory) {

	}
	
	public static IDateTime startTimer() {
		if (lastTimer != null) {
			new IllegalStateException("Another timer is already started at " + lastTimer).printStackTrace();
		}
		lastTimer = new Date();
		return VF.datetime(lastTimer.getTime());
	}
	
	public static INumber stopTimer() {
		if (lastTimer == null) {
			System.out.println("Timer wasn't started, so we return 0!");
			return VF.integer(0);
		}
		long diff = new Date().getTime() - lastTimer.getTime();
		lastTimer = null;
		return VF.integer(diff);
	} 
	
	public static INumber currentDate() {
		return VF.integer(new Date().getTime());
	}
	
	//@doc{Returns the amount of hours between the two datetimes}
	public static IValue hoursDiff(IDateTime dtStart, IDateTime dtEnd) {
		if (!(dtStart.isTime() || dtEnd.isTime())) {
			org.joda.time.Interval iv = new org.joda.time.Interval(dtStart.getInstant(), dtEnd.getInstant());
			return ScmTypes.VF.integer(iv.toPeriod(org.joda.time.PeriodType.hours()).getHours());
		}
		throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot calculate the hours between two time values.", null, null);
	}	
	
	//@doc{Returns the amount of minutes between the two datetimes}
	public static IValue minutesDiff(IDateTime dtStart, IDateTime dtEnd) {
		if (!(dtStart.isTime() || dtEnd.isTime())) {
			org.joda.time.Interval iv = new org.joda.time.Interval(dtStart.getInstant(), dtEnd.getInstant());
			return ScmTypes.VF.integer(iv.toPeriod(org.joda.time.PeriodType.minutes()).getMinutes());
		}
		throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot calculate the seconds between two time values.", null, null);
	}
	
	//@doc{Returns the amount of seconds between the two datetimes}
	public static IValue secondsDiff(IDateTime dtStart, IDateTime dtEnd) {
		if (!(dtStart.isTime() || dtEnd.isTime())) {
			org.joda.time.Interval iv = new org.joda.time.Interval(dtStart.getInstant(), dtEnd.getInstant());
			return ScmTypes.VF.integer(iv.toPeriod(org.joda.time.PeriodType.seconds()).getSeconds());
		}
		throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot calculate the minutes between two time values.", null, null);
	}
	
	//@doc{Returns the amount of milliseconds between the two datetimes}
	public static IValue millisDiff(IDateTime dtStart, IDateTime dtEnd) {
		if (!(dtStart.isTime() || dtEnd.isTime())) {
			org.joda.time.Interval iv = new org.joda.time.Interval(dtStart.getInstant(), dtEnd.getInstant());
			return ScmTypes.VF.integer(iv.toPeriod(org.joda.time.PeriodType.millis()).getMillis());
		}
		throw RuntimeExceptionFactory.invalidUseOfTimeException("Cannot calculate the millseconds between two time values.", null, null);
	}	
}

