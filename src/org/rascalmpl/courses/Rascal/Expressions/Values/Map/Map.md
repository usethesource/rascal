# Map

.Synopsis
Map values.

.Index
( : )

.Syntax
`( KeyExp~1~ : ValExp~1~, KeyExp~2~ : ValExp~2~, ... )`

.Types


| `_KeyExp~1~_` | `_ValExp~1~_` | `_KeyExp~2~_` | `_ValExp~2~_` | ... | `( _KeyExp~1~_ : _ValExp~1~_, _KeyExp~2~_ : _ValExp~2~_, ... )`   |
| --- | --- | --- | --- | --- | --- |
| `_TK~1~_`     |  `_TV~1~_`    |  `_TK~2~_`    | `_TV~2~_`     | ... | `map[lub(_TK~1~_, _TK~2~_, ... ) , lub(_TV~1~_, _TV~2~_, ... )]`  |


.Usage

.Function

.Details

.Description
A map is a set of key/value pairs and has the following properties:

*  Key and value may have different static types.

*  A key can only occur once.


Maps resemble functions rather than relations in the sense that only a single value can be associated with each key.

The following functions are provided for maps:

(((TOC)))

.Examples
```rascal-shell
("pear" : 1, "apple" : 3, "banana" : 0);
```

.Benefits

.Pitfalls

