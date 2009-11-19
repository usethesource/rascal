module DateTime

import List;

@doc{Get the current datetime.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java now();

@doc{Create a new date.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java createDate(int year, int month, int day);

@doc{Create a new time.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java createTime(int hour, int minute, int second, int millisecond);

@doc{Create a new time with the given numeric timezone offset.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java createTimeWZone(int hour, int minute, int second, int millisecond, 
                                     int timezoneHourOffset, int timezoneMinuteOffset);
                                
@doc{Create a new datetime.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java createDateTime(int year, int month, int day, int hour, int minute, 
                                    int second, int millisecond);

@doc{Create a new datetime with the given numeric timezone offset.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java createDateTimeWZone(int year, int month, int day, int hour, int minute, 
                                         int second, int millisecond, int timezoneHourOffset, 
                                         int timezoneMinuteOffset);

@doc{Create a new datetime by combining a date and a time.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java joinDateAndTime(datetime date, datetime time);

@doc{Split an existing datetime into a tuple with the date and the time.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public tuple[datetime date, datetime time] java splitDateTime(datetime dt);

@doc{Increment the years by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java incrementYearsBy(datetime dt, int n);

@doc{Increment the months by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java incrementMonthsBy(datetime dt, int n);

@doc{Increment the days by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java incrementDaysBy(datetime dt, int n);

@doc{Increment the hours by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java incrementHoursBy(datetime dt, int n);

@doc{Increment the minutes by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java incrementMinutesBy(datetime dt, int n);

@doc{Increment the seconds by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java incrementSecondsBy(datetime dt, int n);

@doc{Increment the milliseconds by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java incrementMillisecondsBy(datetime dt, int n);

@doc{Increment the years by 1.}
public datetime incrementYears(datetime dt) {
  return incrementYearsBy(dt,1);
}

@doc{Increment the months by 1.}
public datetime incrementMonths(datetime dt) {
  return incrementMonthsBy(dt,1);
}

@doc{Increment the days by 1.}
public datetime incrementDays(datetime dt) {
  return incrementDaysBy(dt,1);
}

@doc{Increment the hours by 1.}
public datetime incrementHours(datetime dt) {
  return incrementHoursBy(dt,1);
}

@doc{Increment the minutes by 1.}
public datetime incrementMinutes(datetime dt) {
  return incrementMinutesBy(dt,1);
}

@doc{Increment the seconds by 1.}
public datetime incrementSeconds(datetime dt) {
  return incrementSecondsBy(dt,1);
}

@doc{Increment the milliseconds by 1.}
public datetime incrementMilliseconds(datetime dt) {
  return incrementMillisecondsBy(dt,1);
}

@doc{Decrement the years by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java decrementYearsBy(datetime dt, int n);

@doc{Decrement the months by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java decrementMonthsBy(datetime dt, int n);

@doc{Decrement the days by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java decrementDaysBy(datetime dt, int n);

@doc{Decrement the hours by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java decrementHoursBy(datetime dt, int n);

@doc{Decrement the minutes by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java decrementMinutesBy(datetime dt, int n);

@doc{Decrement the seconds by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java decrementSecondsBy(datetime dt, int n);

@doc{Decrement the milliseconds by a given amount.}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java decrementMillisecondsBy(datetime dt, int n);

@doc{Decrement the years by 1.}
public datetime decrementYears(datetime dt) {
  return decrementYearsBy(dt,1);
}

// Commonly-used versions of the decrement functions
@doc{Decrement the months by 1.}
public datetime decrementMonths(datetime dt) {
  return decrementMonthsBy(dt,1);
}

@doc{Decrement the days by 1.}
public datetime decrementDays(datetime dt) {
  return decrementDaysBy(dt,1);
}

@doc{Decrement the hours by 1.}
public datetime decrementHours(datetime dt) {
  return decrementHoursBy(dt,1);
}  

@doc{Decrement the minutes by 1.}
public datetime decrementMinutes(datetime dt) {
  return decrementMinutesBy(dt,1);
}

@doc{Decrement the seconds by 1.}
public datetime decrementSeconds(datetime dt) {
  return decrementSecondsBy(dt,1);
}

@doc{Decrement the milliseconds by 1.}
public datetime decrementMilliseconds(datetime dt) {
  return decrementMillisecondsBy(dt,1);
}

@doc{A closed interval on the time axis.}
data interval = Interval(datetime start, datetime end);

@doc{Given two datetime values, create an interval.}
// TODO: Question, should we throw here if start > end?
public interval createInterval(datetime start, datetime end) {
	return Interval(start,end);
}

@doc{A duration of time, measured in individual years, months, etc.}
data duration = Duration(int years, int months, int days, 
                         int hours, int minutes, int seconds, int milliseconds);

@javaClass{org.meta_environment.rascal.library.DateTime}
private tuple[int,int,int,int,int,int,int] java createDurationInternal(datetime start, datetime end);

// TODO: Add an exception for the non-matching case
@doc{Create a new duration representing the duration between the start and end dates.}
public duration createDuration(datetime start, datetime end) {	
	switch(createDurationInternal(start,end)) {
	  case <int y,int m,int d,int h,int min,int s,int ms>:
		return Duration(y,m,d,h,min,s,ms);
	}
	return Duration(0,0,0,0,0,0,0);
}

@doc{Given an interval, create a new duration representing the duration between the interval start and end.}
public duration createDurationFromInterval(interval i) {
	return createDuration(i.start,i.end);	
}                         

@doc{Return the number of days in an interval, including the start and end days.}
public int daysInInterval(interval i) {
	return daysBetween(i.start,i.end);
}

@doc{Return the difference between two dates and/or datetimes in days.} 
@javaClass{org.meta_environment.rascal.library.DateTime}
public int java dayDiff(datetime start, datetime end);

@doc{Given an interval i, return a list of days [i.start, ..., i.end]}
public list[datetime] dateRangeByDay(interval i) {
	list[datetime] l = [];
	datetime loopDate = i.end.justDate;
	datetime startDate = i.start.justDate;
	
	while (loopDate >= startDate) {
		l = insertAt(l,0,loopDate);
		loopDate = decrementDays(loopDate);
	}
	
	return l;
}

@doc{Parse an input date given as a string using the given format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java parseDate(str inputDate, str formatString);

@doc{Parse an input date given as a string using a specific locale and format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java parseDateInLocale(str inputDate, str formatString, str locale);

@doc{Parse an input time given as a string using the given format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java parseTime(str inputTime, str formatString);

@doc{Parse an input time given as a string using a specific locale and format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java parseTimeInLocale(str inputTime, str formatString, str locale);

@doc{Parse an input datetime given as a string using the given format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java parseDateTime(str inputDateTime, str formatString);

@doc{Parse an input datetime given as a string using a specific locale and format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public datetime java parseDateTimeInLocale(str inputDateTime, str formatString, str locale);

@doc{Print an input date using the given format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public str java printDate(datetime inputDate, str formatString);

@doc{Print an input date using a default format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public str java printDateDefault(datetime inputDate);

@doc{Print an input date using a specific locale and format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public str java printDateInLocale(datetime inputDate, str formatString, str locale);

@doc{Print an input date using a specific locale and a default format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public str java printDateDefaultInLocale(datetime inputDate, str locale);

@doc{Print an input time using the given format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public str java printTime(datetime inputTime, str formatString);

@doc{Print an input time using a default format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public str java printTimeDefault(datetime inputTime);

@doc{Print an input time using a specific locale and format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public str java printTimeInLocale(datetime inputTime, str formatString, str locale);

@doc{Print an input time using a specific locale and a default format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public str java printTimeDefaultInLocale(datetime inputTime, str locale);

@doc{Print an input datetime using the given format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public str java printDateTime(datetime inputDateTime, str formatString);

@doc{Print an input datetime using a default format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public str java printDateTimeDefault(datetime inputDateTime);

@doc{Print an input datetime using a specific locale and format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public str java printDateTimeInLocale(datetime inputDateTime, str formatString, str locale);

@doc{Print an input datetime using a specific locale and a default format string}
@javaClass{org.meta_environment.rascal.library.DateTime}
public str java printDateTimeDefaultInLocale(datetime inputDateTime, str locale);
