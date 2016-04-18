# readRSF

.Synopsis
Read a file in Rigi Standard Format (RSF). 

.Usage

.Types

.Function
`map[str, rel[str,str]] readRSF(str nameRSFFile) throws IO(str msg)`

.Details

.Description
Since an RSF file may define more than one relation,
a mapping from relation name to relation value is returned.

.Examples
For the RSF file:
[source,rascal]
----
call    main          printf  
call    main          listcreate  
data    main          FILE  
data    listcreate    List
----
`readRSF` will create the following map:
[source,rascal]
----
("call" : {<"main", "printf">, <"main", "listcreate">},
 "data" : {<"main", "FILE">, <"listcreate", "List">})
----

.Benefits

.Pitfalls

