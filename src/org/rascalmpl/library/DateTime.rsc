@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module DateTime

import List;

@doc{Get the current datetime.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime now();

@doc{Create a new date.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime createDate(int year, int month, int day);

@doc{Create a new time.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime createTime(int hour, int minute, int second, int millisecond);

@doc{Create a new time with the given numeric timezone offset.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime createTime(int hour, int minute, int second, int millisecond, 
                                int timezoneHourOffset, int timezoneMinuteOffset);
                                
@doc{Create a new datetime.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime createDateTime(int year, int month, int day, int hour, int minute, 
                                    int second, int millisecond);

@doc{Create a new datetime with the given numeric timezone offset.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime createDateTime(int year, int month, int day, int hour, int minute, 
                                    int second, int millisecond, int timezoneHourOffset, 
                                    int timezoneMinuteOffset);

@doc{Create a new datetime by combining a date and a time.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime joinDateAndTime(datetime date, datetime time);

@doc{Split an existing datetime into a tuple with the date and the time.}
@javaClass{org.rascalmpl.library.DateTime}
public java tuple[datetime date, datetime time] splitDateTime(datetime dt);

@doc{Increment the years by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime incrementYears(datetime dt, int n);

@doc{Increment the months by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime incrementMonths(datetime dt, int n);

@doc{Increment the days by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime incrementDays(datetime dt, int n);

@doc{Increment the hours by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime incrementHours(datetime dt, int n);

@doc{Increment the minutes by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime incrementMinutes(datetime dt, int n);

@doc{Increment the seconds by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime incrementSeconds(datetime dt, int n);

@doc{Increment the milliseconds by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime incrementMilliseconds(datetime dt, int n);

@doc{Increment the years by 1.}
public datetime incrementYears(datetime dt) {
  return incrementYears(dt,1);
}

@doc{Increment the months by 1.}
public datetime incrementMonths(datetime dt) {
  return incrementMonths(dt,1);
}

@doc{Increment the days by 1.}
public datetime incrementDays(datetime dt) {
  return incrementDays(dt,1);
}

@doc{Increment the hours by 1.}
public datetime incrementHours(datetime dt) {
  return incrementHours(dt,1);
}

@doc{Increment the minutes by 1.}
public datetime incrementMinutes(datetime dt) {
  return incrementMinutes(dt,1);
}

@doc{Increment the seconds by 1.}
public datetime incrementSeconds(datetime dt) {
  return incrementSeconds(dt,1);
}

@doc{Increment the milliseconds by 1.}
public datetime incrementMilliseconds(datetime dt) {
  return incrementMilliseconds(dt,1);
}

@doc{Decrement the years by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime decrementYears(datetime dt, int n);

@doc{Decrement the months by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime decrementMonths(datetime dt, int n);

@doc{Decrement the days by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime decrementDays(datetime dt, int n);

@doc{Decrement the hours by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime decrementHours(datetime dt, int n);

@doc{Decrement the minutes by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime decrementMinutes(datetime dt, int n);

@doc{Decrement the seconds by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime decrementSeconds(datetime dt, int n);

@doc{Decrement the milliseconds by a given amount.}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime decrementMilliseconds(datetime dt, int n);

@doc{Decrement the years by 1.}
public datetime decrementYears(datetime dt) {
  return decrementYears(dt,1);
}

// Commonly-used versions of the decrement functions
@doc{Decrement the months by 1.}
public datetime decrementMonths(datetime dt) {
  return decrementMonths(dt,1);
}

@doc{Decrement the days by 1.}
public datetime decrementDays(datetime dt) {
  return decrementDays(dt,1);
}

@doc{Decrement the hours by 1.}
public datetime decrementHours(datetime dt) {
  return decrementHours(dt,1);
}  

@doc{Decrement the minutes by 1.}
public datetime decrementMinutes(datetime dt) {
  return decrementMinutes(dt,1);
}

@doc{Decrement the seconds by 1.}
public datetime decrementSeconds(datetime dt) {
  return decrementSeconds(dt,1);
}

@doc{Decrement the milliseconds by 1.}
public datetime decrementMilliseconds(datetime dt) {
  return decrementMilliseconds(dt,1);
}

@doc{A closed interval on the time axis.}
data interval = Interval(datetime begin, datetime end);

@doc{Given two datetime values, create an interval.}
// TODO: Question, should we throw here if begin > end?
public interval createInterval(datetime begin, datetime end) {
	return Interval(begin,end);
}

@doc{A duration of time, measured in individual years, months, etc.}
data duration = Duration(int years, int months, int days, 
                         int hours, int minutes, int seconds, int milliseconds);

@javaClass{org.rascalmpl.library.DateTime}
private java tuple[int,int,int,int,int,int,int] createDurationInternal(datetime begin, datetime end);

// TODO: Add an exception for the non-matching case
@doc{Create a new duration representing the duration between the begin and end dates.}
public duration createDuration(datetime begin, datetime end) {	
	switch(createDurationInternal(begin,end)) {
	  case <int y,int m,int d,int h,int min,int s,int ms>:
		return Duration(y,m,d,h,min,s,ms);
	}
	return Duration(0,0,0,0,0,0,0);
}

@doc{Given an interval, create a new duration representing the duration between the interval begin and end.}
public duration createDuration(interval i) {
	return createDuration(i.begin,i.end);	
}                         

@doc{Return the number of days in an interval, including the begin and end days.}
public int daysInInterval(interval i) {
	return daysDiff(i.begin,i.end);
}

@doc{Return the difference between two dates and/or datetimes in days.} 
@javaClass{org.rascalmpl.library.DateTime}
public java int daysDiff(datetime begin, datetime end);

@doc{Given an interval i, return a list of days [i.begin, ..., i.end]}
public list[datetime] dateRangeByDay(interval i) {
	list[datetime] l = [];
	datetime loopDate = i.end.justDate;
	datetime beginDate = i.begin.justDate;
	
	while (loopDate >= beginDate) {
		l = insertAt(l,0,loopDate);
		loopDate = decrementDays(loopDate);
	}
	
	return l;
}

@doc{Parse an input date given as a string using the given format string}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime parseDate(str inputDate, str formatString);

@doc{Parse an input date given as a string using a specific locale and format string}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime parseDateInLocale(str inputDate, str formatString, str locale);

@doc{Parse an input time given as a string using the given format string}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime parseTime(str inputTime, str formatString);

@doc{Parse an input time given as a string using a specific locale and format string}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime parseTimeInLocale(str inputTime, str formatString, str locale);

@doc{Parse an input datetime given as a string using the given format string}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime parseDateTime(str inputDateTime, str formatString);

@doc{Parse an input datetime given as a string using a specific locale and format string}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime parseDateTimeInLocale(str inputDateTime, str formatString, str locale);

@doc{Print an input date using the given format string}
@javaClass{org.rascalmpl.library.DateTime}
public java str printDate(datetime inputDate, str formatString);

@doc{Print an input date using a default format string}
@javaClass{org.rascalmpl.library.DateTime}
public java str printDate(datetime inputDate);

@doc{Print an input date using a specific locale and format string}
@javaClass{org.rascalmpl.library.DateTime}
public java str printDateInLocale(datetime inputDate, str formatString, str locale);

@doc{Print an input date using a specific locale and a default format string}
@javaClass{org.rascalmpl.library.DateTime}
public java str printDateInLocale(datetime inputDate, str locale);

@doc{Print an input time using the given format string}
@javaClass{org.rascalmpl.library.DateTime}
public java str printTime(datetime inputTime, str formatString);

@doc{Print an input time using a default format string}
@javaClass{org.rascalmpl.library.DateTime}
public java str printTime(datetime inputTime);

@doc{Print an input time using a specific locale and format string}
@javaClass{org.rascalmpl.library.DateTime}
public java str printTimeInLocale(datetime inputTime, str formatString, str locale);

@doc{Print an input time using a specific locale and a default format string}
@javaClass{org.rascalmpl.library.DateTime}
public java str printTimeInLocale(datetime inputTime, str locale);

@doc{Print an input datetime using the given format string}
@javaClass{org.rascalmpl.library.DateTime}
public java str printDateTime(datetime inputDateTime, str formatString);

@doc{Print an input datetime using a default format string}
@javaClass{org.rascalmpl.library.DateTime}
public java str printDateTime(datetime inputDateTime);

@doc{Print an input datetime using a specific locale and format string}
@javaClass{org.rascalmpl.library.DateTime}
public java str printDateTimeInLocale(datetime inputDateTime, str formatString, str locale);

@doc{Print an input datetime using a specific locale and a default format string}
@javaClass{org.rascalmpl.library.DateTime}
public java str printDateTimeInLocale(datetime inputDateTime, str locale);
