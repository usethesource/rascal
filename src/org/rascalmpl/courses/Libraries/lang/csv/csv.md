# csv
.Synopsis
Comma-Separated Values (CSV).

.Description

The http://tools.ietf.org/html/rfc4180[CSV format] was originally intended for exchanging
information between spreadsheets and databases but is today used as an exchange format in many other application domains as well.
A CSV file has the following structure:

*  An optional header line consisting of field names separated by comma's.
*  One or more lines consisting of values separated by comma's.


Some adaptation is possible and includes:

*  Including or excluding the header line (use `"header" : "true"` in the option map for the various functions).
*  Using another separator than a comma (use `"separator" : ";"` in the option map for the various functions, where `";"` can be any single character string).


The following functions are provided in the ((Rascal-Libraries-lang-csv-IO)) library:
loctoc::[1]

.Examples
Here is an example CSV file using the default separator (","):
[source,rascal]
----
field_name1,field_name2,field_name3
aaa,bbb,ccc
zzz,yyy,xxx
----

Here is an example CSV file using a non-default separator (";"):
[source,rascal]
----
position;artist;title;year
1;Eagles;Hotel California;1977
2;Queen;Bohemian rhapsody;1975
3;Boudewijn de Groot;Avond;1997
----
