# ModuleImport

.Synopsis
A Rascal module could not be imported.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
Rascal programs are split up in modules.
An [import ]((Rascal:Statements-Import)) statement is used to include one module in another.
This error indicates that a module import failed.
This can be caused by 

*  A misspelled name in the import statement.
*  Starting a Rascal console from the wrong directory.


Remedies:

*  Correct the mispelled name.
*  In Eclipse the safest way to execute a Rascal module is to select it in the Package Explorer, 
right click on it and then select `Run as Rascal Application`.

*  At the command line, change directory to where the toplevel module of your program is located and then execute the Rascal Shell.

.Examples
Here is a correct import of the library module `List`:
```rascal-shell
import List;
```
Here is the effect of a misspelled module name (assuming that you do not have a module `Lis`):
```rascal-shell,error
import Lis;
```

.Benefits

.Pitfalls

