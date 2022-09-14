---
title: Unavailable Information
---

.Synopsis
Requested information is unavailable at runtime


.Types
`data RuntimeException = UnavailableInformation(str message);`
       
.Usage
`import Exception;` (only needed when `UnavailableInformation` is used in `catch`)

.Description

Several datetypes may contain optional information that is not always available:

* In a [datetime]((Rascal:Values-DateTime)) value the date or the time may be missing.
* In a [location]((Rascal:Values-Location)) value, various fields are optional, 
  e.g., `port`, `offset`, `begin` and `end`.
  
This exception is thrown when optional information is not available.

.Examples

```rascal-shell,error
$2016-09-14$.hour;
someLoc = |home:///abc.txt|;
someLoc.offset;
```
