@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jouke Stoel - Jouke.Stoel@cwi.nl - CWI}
module lang::rascal::tests::library::DateTime
  
import DateTime;

import String;

test bool createDate_sampled(datetime gen)  =
		date.year == gen.year && date.month == gen.month && date.day == gen.day
	when 
		date := createDate(gen.year, gen.month, gen.day);

test bool createTime_sampled(datetime gen) =
        time.hour == gen.hour && time.minute == gen.minute && time.second == gen.second && time.millisecond == gen.millisecond
	when
		time := createTime(gen.hour, gen.minute, gen.second, gen.millisecond);
		
test bool printDate_simpleFormat(datetime gen) =
    printDate(gen) == formattedDate(gen);
    
test bool printTime_simpleFormat(datetime gen) =
    printTime(gen) == formattedTime(gen);
    
test bool printDateTime_simpleFormat(datetime gen) =
    printDateTime(gen) == "<formattedDate(gen)> <formattedTime(gen)>";

test bool incrementDays_withOneDay(datetime gen) =
  gen.year > 1751 ? incrementDays(createDate(gen.year, gen.month, gen.day)) == incDateByOneDay(gen) : true;
  // TIL; apparently before the year 1752 the US was still on the Julian calendar which calculated a leap year every 4 years. The algorithm used here only works for the Gregorian calendar 

// Increment a date by a day according to the Gregorian calendar algorithm for leap year calculation
datetime incDateByOneDay(datetime dt) {
	if (dt.day < 28) {
		return createDate(dt.year, dt.month, dt.day+1);
	}
	else if (dt.month in {1,3,5,7,8,10,12}) {
		if (dt.day < 31) {
			return createDate(dt.year, dt.month, dt.day+1);
		} else if (dt.month < 12) {
			return createDate(dt.year, dt.month+1, 1);
		} else {
			return createDate(dt.year+1, 1, 1);
		}
	} else if (dt.month == 2) {
		if (dt.day < 27) {
			return createDate(dt.year, dt.month, dt.day+1);
		} else if (dt.day == 28 && ((dt.year % 400 == 0) || (dt.year % 4 == 0 && dt.year % 100 > 0))) { // leap year
			return createDate(dt.year, dt.month, dt.day+1);
		} else {
			return createDate(dt.year, dt.month+1, 1);
		}
	} else {
		if (dt.day < 30) {
			return createDate(dt.year, dt.month, dt.day+1);
		} else {
			return createDate(dt.year, dt.month+1, 1);
		}
	}
	
}

str formattedDate(datetime dt) =
    "<fill(dt.year, 4)>-<fill(dt.month)>-<fill(dt.day)>";
    
str formattedTime(datetime dt) =
    "<fill(dt.hour)>:<fill(dt.minute)>:<fill(dt.second)>.<fill(dt.millisecond,3)>+<fill(dt.timezoneOffsetHours)><fill(dt.timezoneOffsetMinutes)>";        

private str fill(int val) = fill(val, 2);
private str fill(int val, int n) = right("<val>", n, "0");
    
