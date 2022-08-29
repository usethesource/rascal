# InvalidDateTimeComparison

.Synopsis
Attempt to compare a date with a time.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
[datetime]((Rascal:Values-DateTime)) values may contain two components: date and time.
Datetime values that contain both a date and a time can be compared.
Dates can also be compared with dates, and times with times.
This error is generated when a date is compared with a time.

.Examples
Comparing dates with dates:
```rascal-shell
$2013-07-15$ < $2014-07-15$
```
Or times with times:
```rascal-shell,continue
$T20:03:56.901+01:00$ < $T22:00:56.901+01:00$
```
Or datetimes with datetimes:
```rascal-shell,continue
$2013-01-11T23:03:56.901+01:00$ < $2013-01-11T23:05:00.901+01:00$
```
But mixing dates and times gives errors:
```rascal-shell,error
$2013-07-15$ < $T20:03:56.901+01:00$
```

.Benefits

.Pitfalls

