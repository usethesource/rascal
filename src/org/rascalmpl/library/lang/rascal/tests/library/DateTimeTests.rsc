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

test bool createDate_sampled(datetime gen)  =
		date.year == gen.year && date.month == gen.month && date.day == gen.day
	when 
		date := createDate(gen.year, gen.month, gen.day);

test bool createTime_sampled(datetime gen) =
        time.hour == gen.hour && time.minute == gen.minute && time.second == gen.second && time.millisecond == gen.millisecond
	when
		time := createTime(gen.hour, gen.minute, gen.second, gen.millisecond);