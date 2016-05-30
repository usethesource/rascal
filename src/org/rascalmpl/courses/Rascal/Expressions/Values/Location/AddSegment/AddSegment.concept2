# Location AddSegment

.Synopsis
Locations can be concatenated with strings to add segments to the path component

.Index
+

.Syntax
`_Loc_ + _Str_`

.Types

//

|====
| `_Loc_` | `_Str_` | `_Loc_ + _Str_` 

| `loc`     | `str`     | `loc`               
|====

.Function

.Details



.Description

Adds a segment to the path component of a location.
This concatenation introduces a path separator (`/`) automatically.

.Examples
[source,rascal-shell]
----
|tmp:///myDir| + "myFile";
----
To get the original back, you can use the `parent` field:
[source,rascal-shell]
----
(|tmp:///myDir| + "myFile").parent
----
.Benefits

.Pitfalls

