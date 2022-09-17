@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@doc{
#### Synopsis

Library functions for date and time.

#### Description

For operators on `datetime` see [DateTime]((Rascal:Values-DateTime)) in the Rascal Language Reference.

The following functions are defined for datetime:
(((TOC)))
}
module DateTime

import List;

@doc{
#### Synopsis

Get the current datetime.

#### Examples

```rascal-shell
import DateTime;
now();
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime now();

@doc{
#### Synopsis

Create a new date.

#### Examples

```rascal-shell
import DateTime;
createDate(2012,1,1);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime createDate(int year, int month, int day);



@doc{
#### Synopsis

Create a new time (with optional timezone offset).

#### Examples

```rascal-shell
import DateTime;
createTime(8,15,30,55);
createTime(8,15,30,55,2,0);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime createTime(int hour, int minute, int second, int millisecond);

// Create a new time with the given numeric timezone offset.
@javaClass{org.rascalmpl.library.Prelude}
public java datetime createTime(int hour, int minute, int second, int millisecond, 
                                int timezoneHourOffset, int timezoneMinuteOffset);
                                
@doc{
#### Synopsis

Create a new datetime (with optional timezone offset).

#### Examples

```rascal-shell
import DateTime;
createDateTime(2012,1,1,8,15,30,55);
createDateTime(2012,1,1,8,15,30,55,2,0);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime createDateTime(int year, int month, int day, int hour, int minute, 
                                    int second, int millisecond);

// Create a new datetime with the given numeric timezone offset.
@javaClass{org.rascalmpl.library.Prelude}
public java datetime createDateTime(int year, int month, int day, int hour, int minute, 
                                    int second, int millisecond, int timezoneHourOffset, 
                                    int timezoneMinuteOffset);

@doc{
#### Synopsis

Create a new datetime by combining a date and a time.

#### Examples

```rascal-shell
import DateTime;
D = createDate(2012, 1, 1);
T = createTime(8, 15, 45, 30);
joinDateAndTime(D, T);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime joinDateAndTime(datetime date, datetime time);

@doc{

#### Synopsis

Split an existing datetime into a tuple with the date and the time.

#### Examples

```rascal-shell
import DateTime;
N = now();
splitDateTime(N);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java tuple[datetime date, datetime time] splitDateTime(datetime dt);


@doc{
#### Synopsis

Increment the years by given amount or by 1.

#### Examples

```rascal-shell
import DateTime;
N = now();
incrementYears(N);
incrementYears(N, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime incrementYears(datetime dt, int n);

// Increment the years by 1.

public datetime incrementYears(datetime dt) {
  return incrementYears(dt,1);
}

@doc{
#### Synopsis

Increment the months by a given amount or by 1.
#### Function

#### Examples

```rascal-shell
import DateTime;
N = now();
incrementMonths(N);
incrementMonths(N, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime incrementMonths(datetime dt, int n);

// Increment the months by 1.
public datetime incrementMonths(datetime dt) {
  return incrementMonths(dt,1);
}

@doc{
#### Synopsis

Increment the days by given amount or by 1.

#### Examples

```rascal-shell
import DateTime;
N = now();
incrementDays(N);
incrementDays(N, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime incrementDays(datetime dt, int n);

// Increment the days by 1.
public datetime incrementDays(datetime dt) {
  return incrementDays(dt,1);
}

@doc{
#### Synopsis

Increment the hours by a given amount or by 1.`

#### Examples

```rascal-shell
import DateTime;
N = now();
incrementHours(N);
incrementHours(N, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime incrementHours(datetime dt, int n);

//  Increment the hours by 1.

public datetime incrementHours(datetime dt) {
  return incrementHours(dt,1);
}

@doc{
#### Synopsis

Increment the minutes by a given amount or by 1.

#### Examples

```rascal-shell
import DateTime;
N = now();
incrementMinutes(N);
incrementMinutes(N, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime incrementMinutes(datetime dt, int n);

// Increment the minutes by 1.

public datetime incrementMinutes(datetime dt) {
  return incrementMinutes(dt,1);
}

@doc{
#### Synopsis

Increment the seconds by a given amount or by 1.

#### Examples

```rascal-shell
import DateTime;
N = now();
incrementSeconds(N);
incrementSeconds(N, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime incrementSeconds(datetime dt, int n);

// Increment the seconds by 1.

public datetime incrementSeconds(datetime dt) {
  return incrementSeconds(dt,1);
}

@doc{
#### Synopsis

Increment the milliseconds by a given amount or by 1.

#### Examples

```rascal-shell
import DateTime;
N = now();
incrementMilliseconds(N);
incrementMilliseconds(N, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime incrementMilliseconds(datetime dt, int n);

// Increment the milliseconds by 1.

public datetime incrementMilliseconds(datetime dt) {
  return incrementMilliseconds(dt,1);
}

@doc{
#### Synopsis

Decrement the years by a given amount or by 1.

#### Examples

```rascal-shell
import DateTime;
N = now();
decrementYears(N);
decrementYears(N, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime decrementYears(datetime dt, int n);

// Decrement the years by 1.
public datetime decrementYears(datetime dt) {
  return decrementYears(dt,1);
}

@doc{
#### Synopsis

Decrement the months by a given amount or by 1.

#### Examples

```rascal-shell
import DateTime;
N = now();
decrementMonths(N);
decrementMonths(N, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime decrementMonths(datetime dt, int n);

// Decrement the months by 1.

public datetime decrementMonths(datetime dt) {
  return decrementMonths(dt,1);
}

@doc{
#### Synopsis

Decrement the days by a given amount or by 1.

#### Examples

```rascal-shell
import DateTime;
N = now();
decrementDays(N);
decrementDays(N, 3);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime decrementDays(datetime dt, int n);

// Decrement the days by 1.
public datetime decrementDays(datetime dt) {
  return decrementDays(dt,1);
}

@doc{
#### Synopsis

Decrement the hours by a given amount or by 1.

#### Examples

```rascal-shell
import DateTime;
N = now();
decrementHours(N);
decrementHours(N, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime decrementHours(datetime dt, int n);

// Decrement the hours by 1.
public datetime decrementHours(datetime dt) {
  return decrementHours(dt,1);
}  

@doc{
#### Synopsis

Decrement the minutes by a given amount or by 1.

#### Examples

```rascal-shell
import DateTime;
N = now();
decrementMinutes(N);
decrementMinutes(N, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime decrementMinutes(datetime dt, int n);


// Decrement the minutes by 1.
public datetime decrementMinutes(datetime dt) {
  return decrementMinutes(dt,1);
}

@doc{
#### Synopsis

Decrement the seconds by a given amount or by 1.

#### Examples

```rascal-shell
import DateTime;
N = now();
decrementSeconds(N);
decrementSeconds(N, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime decrementSeconds(datetime dt, int n);


// Decrement the seconds by 1.
public datetime decrementSeconds(datetime dt) {
  return decrementSeconds(dt,1);
}

@doc{
#### Synopsis

Decrement the milliseconds by a given amount or by 1.

#### Examples

```rascal-shell
import DateTime;
N = now();
decrementMilliseconds(N);
decrementMilliseconds(N, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime decrementMilliseconds(datetime dt, int n);

// Decrement the milliseconds by 1.
public datetime decrementMilliseconds(datetime dt) {
  return decrementMilliseconds(dt,1);
}

@doc{
#### Synopsis

A closed interval on the time axis.
}
data interval = Interval(datetime begin, datetime end);

@doc{
#### Synopsis

Given two datetime values, create an interval.

#### Examples

```rascal-shell
import DateTime;
B = now();
E = incrementDays(B, 2);
createInterval(B, E);
```
}
// TODO: Question, should we throw here if begin > end?
public interval createInterval(datetime begin, datetime end) {
	return Interval(begin,end);
}

@doc{
#### Synopsis

A duration of time, measured in individual years, months, etc.
}
data Duration = duration(int years, int months, int days, int hours, int minutes, int seconds, int milliseconds);

@javaClass{org.rascalmpl.library.Prelude}
private java tuple[int,int,int,int,int,int,int] createDurationInternal(datetime begin, datetime end);

// TODO: Add an exception for the non-matching case
@doc{
#### Synopsis

Create a new duration representing the duration between the begin and end dates.

#### Examples

```rascal-shell
import DateTime;
B = now();
E1 = incrementHours(B);
createDuration(B, E1);
E2 = incrementMinutes(B);
createDuration(B, E2);
```
}
public Duration createDuration(datetime begin, datetime end) {	
	switch(createDurationInternal(begin,end)) {
	  case <int y,int m,int d,int h,int min,int s,int ms>:
		return duration(y,m,d,h,min,s,ms);
	}
	return duration(0,0,0,0,0,0,0);
}


// Given an interval, create a new duration representing the duration between the interval begin and end.
public Duration createDuration(interval i) {
	return createDuration(i.begin,i.end);	
}                         

@doc{
#### Synopsis

Return the number of days in an interval, including the begin and end days.

#### Examples

```rascal-shell
import DateTime;
B = now();
E = incrementDays(B, 2);
I = createInterval(B, E);
daysInInterval(I);
```
}
public int daysInInterval(interval i) {
	return daysDiff(i.begin,i.end);
}

@doc{
#### Synopsis

Return the difference between two dates and/or datetimes in days.

#### Examples

```rascal-shell
import DateTime;
B = now();
E = incrementDays(B, 2);
daysDiff(B, E);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java int daysDiff(datetime begin, datetime end);

@doc{
#### Synopsis

Given an interval, return a list of days.

#### Description

Given an interval `i`, return a list of days `[i.begin, ..., i.end]`.

#### Examples

```rascal-shell
import DateTime;
B = now();
E = incrementDays(B, 2);
I = createInterval(B, E);
dateRangeByDay(I);
```
}
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

@doc{
#### Synopsis

Parse an input date given as a string using the given format string.

#### Examples

```rascal-shell
import DateTime;
parseDate("2011-12-23", "yyyy-MM-dd");
parseDate("20111223", "yyyyMMdd");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime parseDate(str inputDate, str formatString);

@doc{
#### Synopsis

Parse an input date given as a string using a specific locale and format string.
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime parseDateInLocale(str inputDate, str formatString, str locale);

@doc{
#### Synopsis

Parse an input time given as a string using the given format string.

#### Examples

```rascal-shell
import DateTime;
parseTime("11/21/19", "HH/mm/ss");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime parseTime(str inputTime, str formatString);

@doc{
#### Synopsis

Parse an input time given as a string using a specific locale and format string.
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime parseTimeInLocale(str inputTime, str formatString, str locale);

@doc{
#### Synopsis

Parse an input datetime given as a string using the given format string.

#### Examples

```rascal-shell
import DateTime;
parseDateTime("2011/12/23/11/19/54", "YYYY/MM/dd/HH/mm/ss");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime parseDateTime(str inputDateTime, str formatString);

@doc{
#### Synopsis

Parse an input datetime given as a string using a specific locale and format string.
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime parseDateTimeInLocale(str inputDateTime, str formatString, str locale);

@doc{
#### Synopsis

Print an input date using the given format string.

#### Examples

```rascal-shell
import DateTime;
printDate(now());
printDate(now(), "YYYYMMdd");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str printDate(datetime inputDate, str formatString);

// Print an input date using a default format string
@javaClass{org.rascalmpl.library.Prelude}
public java str printDate(datetime inputDate);

@doc{
#### Synopsis

Print an input date using a specific locale and format string.

#### Examples

```rascal-shell
import DateTime;
printDateInLocale(now(), "Europe/Netherlands");
printDateInLocale(now(), "French");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str printDateInLocale(datetime inputDate, str formatString, str locale);

// Print an input date using a specific locale and a default format string
@javaClass{org.rascalmpl.library.Prelude}
public java str printDateInLocale(datetime inputDate, str locale);

@doc{
#### Synopsis

Print an input time using the given format string.

#### Examples

```rascal-shell
import DateTime;
N = now();
printTime(N);
printTime(N, "HH/mm/ss");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str printTime(datetime inputTime, str formatString);

// Print an input time using a default format string
@javaClass{org.rascalmpl.library.Prelude}
public java str printTime(datetime inputTime);

@doc{
#### Synopsis

Print an input time using a specific locale and format string.
}
@javaClass{org.rascalmpl.library.Prelude}
public java str printTimeInLocale(datetime inputTime, str formatString, str locale);

// Print an input time using a specific locale and a default format string
@javaClass{org.rascalmpl.library.Prelude}
public java str printTimeInLocale(datetime inputTime, str locale);

@doc{
#### Synopsis

Print an input datetime using the given format string.

#### Examples

```rascal-shell
import DateTime;
N = now();
printDateTime(N);
printDateTime(N, "yyyy-MM-dd\'T\'HH:mm:ss.SSSZZ");
printDateTime(N, "YYYY/MM/dd/HH/mm/ss");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str printDateTime(datetime inputDateTime, str formatString);

// Print an input datetime using a default format string
@javaClass{org.rascalmpl.library.Prelude}
public java str printDateTime(datetime inputDateTime);

@doc{
#### Synopsis

Print an input datetime using a specific locale and format string.
}
@javaClass{org.rascalmpl.library.Prelude}
public java str printDateTimeInLocale(datetime inputDateTime, str formatString, str locale);

// Print an input datetime using a specific locale and a default format string
@javaClass{org.rascalmpl.library.Prelude}
public java str printDateTimeInLocale(datetime inputDateTime, str locale);

@doc{
#### Synopsis

Create a new arbitrary datetime.

#### Examples

```rascal-shell
import DateTime;
arbDateTime();
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime arbDateTime();

