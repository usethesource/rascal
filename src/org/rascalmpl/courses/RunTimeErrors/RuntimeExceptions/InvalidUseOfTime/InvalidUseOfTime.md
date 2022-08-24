# Invalid Use Of Time

.Synopsis
Invalid use of a time value at runtime


.Types
`data RuntimeException = InvalidUseOfTime(str message);`
       
.Usage
`import Exception;` (only needed when `InvalidUseOfTime` is used in `catch`)

.Description

Thrown by operations on time values that
try to update unavailable information.

.Examples

Setting the `year` field on a time value throws an exception:
[source,rascal-shell,error]
----
NOW = $T20:11:01.463+00:00$;
NOW.year = 2020;
----

NOTE: Exception is not shown properly