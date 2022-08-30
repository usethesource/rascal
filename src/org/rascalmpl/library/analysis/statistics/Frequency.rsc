@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@doc{
.Synopsis
Frequency distributions.

.Description

Counting the frequency of events is usually the first step in statistical analysis of raw data.
It involves choosing what are the events to count, how to group them in certain
categories and then quickly counting the frequency of each occurring event. 

This module helps by providing commonly used functions for the purpose of counting events.
The output of these functions can be used to draw (cumulative) histograms, or they can
directly be used for further statistical processing and visualisation. 
}
module analysis::statistics::Frequency

import util::Math;

@doc{
.Synopsis
Compute a distribution: count how many times events are mapped to which bucket.

.Examples
[source,rascal-shell]
----
import analysis::statistics::Frequency;
distribution({<"chicken","animal">,<"bear","animal">,<"oak","plant">,<"tulip","plant">});
distribution({<"alice",2>,<"bob",3>,<"claire",5>},5);
----
}
public map[&T, int] distribution(rel[&U event, &T bucket] input) {
  map[&T,int] result = ();
  for (<&U _, &T bucket> <- input) {
    result[bucket]?0 += 1;
  }
  
  return result;
}

public map[&T <: num, int] distribution(rel[&U event, &T <: num bucket] input, &T <: num bucketSize) {
  map[&T <: num,int] result = ();
  for (<&U _, &T <: num bucket> <- input) {
    result[round(bucket, bucketSize)]?0 += 1;
  }
  return result;
}

public map[&T, int] distribution(map[&U event, &T bucket] input) {
  map[&T,int] result = ();
  for (&U event <- input) {
    result[input[event]]?0 += 1;
  }
  
  return result;
}

public map[&T <: num, int] distribution(map[&U event, &T <: num bucket] input, &T <: num bucketSize) {
  map[&T <: num,int] result = ();
  for (&U event <- input) {
    result[round(input[event], bucketSize)]?0 += 1;
  }
  return result;
}

@doc{
.Synopsis
Cumulative frequency of values less than or equal to a given value.

.Description

Returns the cumulative frequency of values less than or equal to a given numeric or string value.
Returns 0 if the value is not comparable to the values set.

.Examples

[source,rascal-shell]
----
import analysis::statistics::Frequency;
D = [1, 2, 1, 1, 3, 5];
cumFreq(D, 1);
cumFreq(D, 2);
cumFreq(D, 10);
----
}
@javaClass{org.rascalmpl.library.analysis.statistics.Frequencies}
public java int cumFreq(list[value] values, num n);

@javaClass{org.rascalmpl.library.analysis.statistics.Frequencies}
public java int cumFreq(list[value] values, str s);

@doc{
.Synopsis
Cumulative percentage of values less than or equal to a given value.

.Description
Returns the cumulative percentage of values less than or equal to v (as a proportion between 0 and 1).

[source,rascal-shell]
----
import analysis::statistics::Frequency;
D = [1, 2, 1, 1, 3, 5];
cumPct(D, 1);
cumPct(D, 2);
cumPct(D, 10);
----
}
@javaClass{org.rascalmpl.library.analysis.statistics.Frequencies}
public java num cumPct(list[value] values, num n);

@javaClass{org.rascalmpl.library.analysis.statistics.Frequencies}
public java num cumPct(list[value] values, str s);

@doc{
.Synopsis
Percentage of values that are equal to a given value.

.Description
Returns the percentage of values that are equal to v (as a proportion between 0 and 1).
.Examples
[source,rascal-shell]
----
import analysis::statistics::Frequency;
D = [1, 2, 1, 1, 3, 5];
pct(D, 1);
pct(D, 2);
pct(D, 10);
----

}
@javaClass{org.rascalmpl.library.analysis.statistics.Frequencies}
public java num pct(list[value] values, num n);

@javaClass{org.rascalmpl.library.analysis.statistics.Frequencies}
public java num pct(list[value] values, str s);
