# Invalid Use Of Date

.Synopsis
Invalid of of a date at runtime

.Types
`data RuntimeException = InvalidUseOfDate(str message);`
       
.Usage
`import Exception;` (only needed when `InvalidUseOfDate` is used in `catch`)

.Description

Thrown by operations on date values that
try to update unavailable information.

.Examples

Setting the `hour` field on a date value throws an exception:
```rascal-shell,error
NOW = $2016-09-18$;
NOW.hour = 14;
```

NOTE: Exception is not shown properly

