# Invalid URI

.Synopsis
An invalid URI is created at runtime.

.Types
`data RuntimeException = InvalidURI(str uri);`
       
.Usage
`import Exception;` (only needed when `InvalidURI` is used in `catch`)

.Description
Thrown by operations on [source locations]((Rascal:Values-Location)) that would 
lead to an invalid URI part of a source location value.

.Examples

```rascal-shell,error
someLoc = |home:///abc.txt|;
someLoc.scheme = "a:b";
```

Another well-known example is a missing path when using `//` (wrong) instead of `///` (good):
```rascal-shell,error
|home:///|;
|home://|;
```
