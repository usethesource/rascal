@doc{
Name: Frequency
Synopsis: Frequency distributions.
}


module analysis::statistics::Frequency

import util::Math;

@doc{
Synopsis: Compute a distribution: count how many times events are mapped to which bucket.

Examples:
<screen>
import analysis::statistics::Frequency;
distribution({<"chicken","animal">,<"bear","animal">,<"oak","plant">,<"tulip","plant">});
distribution({<"alice",2>,<"bob",3>,<"claire",5>},5);
</screen>
}
public map[&T, int] distribution(rel[&U event, &T bucket] input) {
  map[&T,int] result = ();
  for (<&U event, &T bucket> <- input) {
    result[bucket]?0 += 1;
  }
  
  return result;
}

public map[&T <: num, int] distribution(rel[&U event, &T <: num bucket] input, &T <: num bucketSize) {
  map[&T,int] result = ();
  for (<&U event, &T bucket> <- input) {
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
  map[&T,int] result = ();
  for (&U event <- input) {
    result[round(input[event], bucketSize)]?0 += 1;
  }
  return result;
}

@doc{
Synopsis: Cumulative frequency of values less than or equal to a given value.

Description:

Returns the cumulative frequency of values less than or equal to a given numeric or string value.
Returns 0 if the value is not comparable to the values set.

Examples:

<screen>
import analysis::statistics::Frequency;

D = [1, 2, 1, 1, 3, 5];
cumFreq(D, 1);
cumFreq(D, 2);
cumFreq(D, 10);
</screen>
}
@javaClass{org.rascalmpl.library.analysis.statistics.Frequencies}
public java int cumFreq(list[value] values, num n);

@javaClass{org.rascalmpl.library.analysis.statistics.Frequencies}
public java int cumFreq(list[value] values, str s);

@doc{
Synopsis: Cumulative percentage of values less than or equal to a given value.

Description:
Returns the cumulative percentage of values less than or equal to v (as a proportion between 0 and 1).

<screen>
import analysis::statistics::Frequency;
D = [1, 2, 1, 1, 3, 5];
cumPct(D, 1);
cumPct(D, 2);
cumPct(D, 10);
</screen>
}
 
@javaClass{org.rascalmpl.library.analysis.statistics.Frequencies}
public java num cumPct(list[value] values, num n);

@javaClass{org.rascalmpl.library.analysis.statistics.Frequencies}
public java num cumPct(list[value] values, str s);

@doc{
Synopsis: Percentage of values that are equal to a given value.

Description:
Returns the percentage of values that are equal to v (as a proportion between 0 and 1).
Examples:
<screen>
import analysis::statistics::Frequency;
D = [1, 2, 1, 1, 3, 5];
pct(D, 1);
pct(D, 2);
pct(D, 10);
</screen>

}

@javaClass{org.rascalmpl.library.analysis.statistics.Frequencies}
public java num pct(list[value] values, num n);

@javaClass{org.rascalmpl.library.analysis.statistics.Frequencies}
public java num pct(list[value] values, str s);