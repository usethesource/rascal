@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jouke Stoel - Jouke.Stoel@cwi.nl - CWI}

module lang::rascal::tests::library::DateTimeTests
  
import DateTime;

import Boolean;
import util::Math;
import IO;

test bool createDate_simple(int x)  = // parameter is here to force our random test framework to execute this test function multiple times
		date.year == year && date.month == month && date.day == day
	when 
		int year := genYear() &&
		int month := genMonth() &&
		int day := genDay(month, year) && 
		date := createDate(year, month, day);
	
/* helper functions to generate correct input for tests */
private int genYear() = arbInt(10000);
private int genMonth() = 1 + arbInt(12);
private int genDay(int month, int year) = 1 + arbInt(
	month == 2 ? (isLeapYear(year) ? 29 : 28) :
	month in [1,3,5,7,8,10,12] ? 31 : 30);
private bool isLeapYear(int y) = y % 4 == 0 ? (y % 100 == 0 ? y % 400 == 0 : true) : false; 
	