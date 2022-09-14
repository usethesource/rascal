---
title: "Location AddSegment"
keywords: "+"
---

.Synopsis
Locations can be concatenated with strings to add segments to the path component

.Syntax
`Loc + Str`

.Types

//

| `Loc` | `Str` | `Loc + Str`  |
| --- | --- | --- |
| `loc`     | `str`     | `loc`                |


.Function

.Description

Adds a segment to the path component of a location.
This concatenation introduces a path separator (`/`) automatically.

.Examples
```rascal-shell
|tmp:///myDir| + "myFile";
```
To get the original back, you can use the `parent` field:
```rascal-shell
(|tmp:///myDir| + "myFile").parent
```
.Benefits

.Pitfalls

