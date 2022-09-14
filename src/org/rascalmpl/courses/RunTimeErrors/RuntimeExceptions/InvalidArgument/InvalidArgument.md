---
title: Invalid Argument
---

.Synopsis
A function or operation is applied to an invalid argument value.

.Types
`data RuntimeException = InvalidArgument() | InvalidArgument(value v) | InvalidArgument(value v, str message);`
       
.Usage
`import Exception;` (only needed when `InvalidArgument` is used in `catch`)

.Description
This error is generated when a function or operation is applied to a value that it does not expect.

Remedies:

*  Check the value or values to which you apply the function or operation.

.Examples

Changing the month of a [DateTime]((Rascal:Values-DateTime)) to an illegal month (13):
```rascal-shell,error
NOW = $2013-01-13T22:16:51.740+01:00$;
NOW.month = 13;
```

Setting the offset in a location to a negative value:
```rascal-shell,error
someLoc = |home:///abc.txt|;
someLoc.offset = -1;
```

.Benefits

.Pitfalls

