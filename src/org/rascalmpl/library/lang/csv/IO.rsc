@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

@doc{
.Synopsis
Functions for reading and writing Comma-Separated Values (CSV) files.
.Description
The http://tools.ietf.org/html/rfc4180[CSV format] is used for exchanging
information between spreadsheets and databases. A CSV file has the following structure:

*  An optional header line consisting of field names separated by comma's.
*  One or more lines consisting of values separated by comma's.

The following functions are provided:
(((TOC)))

.Examples
```rascal
field_name1,field_name2,field_name3
aaa,bbb,ccc CRLF
zzz,yyy,xxx CRLF
```



}
module lang::csv::IO

import lang::csv::\syntax::Parse;
import lang::csv::ast::CSV;
import lang::csv::ast::Implode;
import Map;
import List;

@doc{
.Synopsis
Read a relation from a CSV (Comma Separated Values) file.

.Description

Read a CSV file and return a value of a required type.

The `result` argument is the required type of the value that is produced by reading the CSV
that is found at `location`.
Optionally, the following arguments can be supplied:

*  `header = true` specifies that a header is present (default).
*  `header = false` specifies that no header is present.
*  `separator = ","` specifies that `,` is the separator character between fields (default).

The CSV data should conform to the specified type (if any).


If the required type is not specified, it is _inferred_ in three steps:

_Step 1_: The type of each field occurrence is inferred from its contents using the
following rules:

*  An empty value is of type `void`.
*  A field that contains a string that corresponds to a number is numeric.
*  A field that contains `true` or `false` is of type is `bool`.
*  In all other cases the field is of type `str`.


_Step 2_: The type of each field is inferred from the type of all of its occurrences:

*  If all occurrences have a numeric type, then the smallest possible type is used.
*  If the occurrences have a mixed type, i.e., numeric, non-numeric, boolean or string, then the type is `str`.
*  If the requested type for a field is `str` and another type would be inferred by the preceeding two rules, 
its inferred type will be `str`.



Reading the values in fields is straightforward, except for the case that the text in the field is enclosed between double quotes (`"`):

*  the text may include line breaks which are represented as `\n` in the resulting string value of the field.
*  the text may contain escaped double quotes (`""`) which are represented as `\"` in the resulting string value.

.Examples

Given is the follwing file `ex1.csv`:
```rascal
include::{LibDir}Libraries/lang/csv/ex1.csv[]
```

                We can read it in various ways:
```rascal-shell
import lang::csv::IO;
R1 = readCSV(#rel[int position, str artist, str title, int year],  |courses:///Libraries/lang/csv/ex1.csv|, separator = ";");
```
Now we can, for instance, select one of the fields of `R1`:
```rascal-shell,continue
R1.artist;
```
It is also possible to infer the type:
```rascal-shell,continue
R1 = readCSV(|courses:///Libraries/lang/csv/ex1.csv|, separator = ";");
```

}
@javaClass{org.rascalmpl.library.lang.csv.IO}
public java value readCSV(loc location, bool header = true, str separator = ",", str encoding = "UTF8");

@deprecated{use the readCSV with keyword parameters}
public value readCSV(loc location, map[str,str] options) {
	return readCSV(location, header = ((options["header"]?"true") == "true"), separator = options["separator"]?",");
}

@javaClass{org.rascalmpl.library.lang.csv.IO}
public java &T readCSV(type[&T] result, loc location, bool header = true, str separator = ",", str encoding = "UTF8");

@javaClass{org.rascalmpl.library.lang.csv.IO}
public java type[value] getCSVType(loc location, bool header = true, str separator = ",", str encoding = "UTF8");

@doc{
.Synopsis
Write a relation to a CSV (Comma Separated Values) file.

.Description
Write `relation` to a CSV file at `location`.
The options influence the way the actrual CSV file is written:

*  `header`: add or omit a header (based on the labels of the relation).
*  `separator`: defines the separator character between fields (default is `,`).



.Examples
```rascal-shell
import lang::csv::IO;
rel[int position, str artist, str title, int year] R1 = {
  <1,"Eagles","Hotel California",1977>,
  <2,"Queen","Bohemian rhapsody",1975>,
  <3,"Boudewijn de Groot","Avond",1997>
};
writeCSV(#rel[int position, str artist, str title, int year], R1, |courses:///Rascal/Libraries/lang/csv/ex1a.csv|);
writeCSV(rel[int, str, str, int], R1, |courses:///Rascal/Libraries/lang/csv/ex1b.csv|, header = false, separator = ";");
```
will produce the following files:

`ex1a.csv` (with a header line and default separator `,`):
```rascal
include::{LibDir}Rascal/Libraries/lang/csv/ex1a.csv[]
```

                `ex1b.csv` (without a header line with separator `;`):
```rascal
include::{LibDir}Rascal/Libraries/lang/csv/ex1b.csv[]
```

                
}
@javaClass{org.rascalmpl.library.lang.csv.IO}
public java void writeCSV(type[&T] schema, &T relation, loc location, bool header = true, str separator = ",", str encoding = "UTF8");

public lang::csv::ast::CSV::Table loadCSV(loc l) = implodeCSV(parseCSV(l));

public lang::csv::ast::CSV::Table loadNormalizedCSV(loc l) = unquote(loadCSV(l));

@doc{Generator for CSV resources}
@resource{csv}
public str generate(str moduleName, loc uri) {
    map[str,str] options = uri.params;
	
    // We can pass the name of the function to generate. If we did, grab it then remove
    // it from the params.
    str funname = "resourceValue";
    if ("funname" in options) {
        funname = options["funname"];
        options = domainX(options,{"funname"});
    }
        
    type[value] csvType = getCSVType(uri, header = ((options["header"]?"true") == "true"), separator = options["separator"]?",");
    
    optionParams = [];
    if ("header" in options) {
    	optionParams = optionParams + "header=<options["header"]>";
    }
    if ("separator" in options) {
    	optionParams = optionParams + "separator=\"<options["separator"]>\"";
    }
    if ("encoding" in options) {
    	optionParams = optionParams + "encoding=\"<options["encoding"]>\"";
    }
    
    mbody = "module <moduleName>
            'import lang::csv::IO;
            '
            'alias <funname>Type = <csvType>;
            '
            'public <funname>Type <funname>() {
            '   return readCSV(#<csvType>, <uri><if(size(optionParams)>0){>, <intercalate(",",optionParams)><}>);
            '}
            '";
            
    return mbody;
}
