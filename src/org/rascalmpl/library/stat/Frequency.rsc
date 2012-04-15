@doc{
Name: Frequency
Synopsis: Frequency distributions.
}


module stat::Frequency

@doc{
Synopsis: Cumulative frequency of values less than or equal to a given value.

Description:

Returns the cumulative frequency of values less than or equal to a given numeric or string value.
Returns 0 if the value is not comparable to the values set.

Examples:

<screen>
import stat::Frequency;

D = [1, 2, 1, 1, 3, 5];
cumFreq(D, 1);
cumFreq(D, 2);
cumFreq(D, 10);
</screen>
}
@javaClass{org.rascalmpl.library.stat.Frequencies}
public java int cumFreq(list[value] values, num n);

@javaClass{org.rascalmpl.library.stat.Frequencies}
public java int cumFreq(list[value] values, str s);

@doc{
Synopsis: Cumulative percentage of values less than or equal to a given value.

Description:
Returns the cumulative percentage of values less than or equal to v (as a proportion between 0 and 1).

<screen>
import stat::Frequency;
D = [1, 2, 1, 1, 3, 5];
cumPct(D, 1);
cumPct(D, 2);
cumPct(D, 10);
</screen>
}
 
@javaClass{org.rascalmpl.library.stat.Frequencies}
public java num cumPct(list[value] values, num n);

@javaClass{org.rascalmpl.library.stat.Frequencies}
public java num cumPct(list[value] values, str s);

@doc{
Synopsis: Percentage of values that are equal to a given value.

Description:
Returns the percentage of values that are equal to v (as a proportion between 0 and 1).
Examples:
<screen>
import stat::Frequency;
D = [1, 2, 1, 1, 3, 5];
pct(D, 1);
pct(D, 2);
pct(D, 10);
</screen>

}

@javaClass{org.rascalmpl.library.stat.Frequencies}
public java num pct(list[value] values, num n);

@javaClass{org.rascalmpl.library.stat.Frequencies}
public java num pct(list[value] values, str s);