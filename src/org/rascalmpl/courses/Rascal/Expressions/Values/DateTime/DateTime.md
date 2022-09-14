---
title: "DateTime"
keywords: "$,date,datetime,time"
---

#### Synopsis

Date and time values.

#### Syntax

*  `$` `Date` `$`
*  `$` `Time` `$`
*  `$` `DateTime` `$`

#### Types

`datetime`

#### Usage

#### Function

#### Description

Date, time, and datetime values are represented by the `datetime` type.
`datetime` literals start with a `$` and are made up of either a date, given in year, month, day of month order; 
a time, preceded by `T` and given in hour, minute, second, millisecond, (optional) timezone offset order; 
or a datetime, which is a date and a time, in the orders given above, and separated by a `T`. 

The following fields provide access to information about the value, but cannot be set:

* `isDate`: returns `true` if the value is a date value, `false` if the value is a
   datetime or time value.
* `isTime`: returns `true` if the value is a time value, `false` if the value is a
   date or datetime value.
* `isDateTime`: returns `true` if the value is a datetime value, `false` if the value is a
   date or time value.
* `justTime`: returns the date component of a date or datetime value.
* `justDate`: returns the time component of a time or datetime value.
* `century`: returns the century component of a year for date or datetime values.


The following fields provide access to the individual components of date, time and datetime values,
and can be accessed using ((DateTime-FieldSelection)) and be assigned using ((DateTime-FieldSelection)):

*  `year`
*  `month`
*  `day`
*  `hour`
*  `minute`
*  `second`
*  `millisecond`
*  `timezoneOffsetHours`
*  `timezoneOffsetMinutes`


Not all fields are available on all values as indicated by the following table:

| Field                   | `date` | `datetime` | `time`  |
| --- | --- | --- | --- |
| `year`                  |  x     |  x         |        
| `month`                 |  x     |  x         |        
| `day`                   |  x     |  x         |        
| `hour`                  |        |  x         | x       |
| `minute`                |        |  x         | x       |
| `second`                |        |  x         | x       |
| `millisecond`           |        |  x         | x       |
| `timezoneOffsetHours`   |        |  x         | x       |
| `timezoneOffsetMinutes` |        |  x         | x       |


The `isDate`, `isTime`, and `isDateTime` fields can be checked in advance to determine what
kind of value is stored in a variable of type `datetime`.

The following operators are defined for DateTime:
(((TOC)))

The following functions are defined for DateTime:
loctoc::[Rascal/Libraries/Prelude/DateTime,1]

There are also [library functions]((Library:DateTime)) available for DateTime.

#### Examples

Examples of `datetime` values are:
```rascal-shell,continue
$2010-07-15$
$2010-07-15T07:15:23.123+0100$;
```
Now introduce a `datetime` value and assign it to `DT`.
```rascal-shell,continue
DT = $2010-07-15T09:15:23.123+03:00$;
```
Here are examples of some `datatime` fields:
```rascal-shell,continue
DT.isDateTime;
DT.justDate;
DT.justTime;
DT.century;
```

#### Benefits

#### Pitfalls

* In normal parlance, the year 2010 is in the 21th century. The `century` field, however, just returns the century component of a given year, e.g., for 2010 this is 20.
* `DT.justTime` prints a time literal that currently can not be parsed back into a value to due to issue #1443.

