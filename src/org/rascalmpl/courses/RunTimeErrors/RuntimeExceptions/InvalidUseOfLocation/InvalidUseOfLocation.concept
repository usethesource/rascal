# Invalid Use Of Location

.Synopsis
Invalid use of a source location at runtime.

.Types
`data RuntimeException = InvalidUseOfLocation(str message);`
       
.Usage
`import Exception;` (only needed when `InvalidUseOfLocation` is used in `catch`)

.Description
Thrown by operations on link:/Rascal#Values-Location[source locations] that would 
lead to an inconsistent or incomplete location value.

.Examples

[source,rascal-shell,error]
----
someLoc = |home:///abc.txt|;
someLoc.begin = <1, 2>;
----