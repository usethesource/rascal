#  Doc Annotations

.Synopsis
A doc annotation attaches an inline concept description to a Rascal declaration.

.Description 
All Rascal declarations can be preceeded by an annotation of the form `@doc{ ... }` where ... may be arbitrary text, 
provided that `{` and `}` characters are balanced and that unbalanced braces are escaped like `\{` or `\}`. 
This text is expanded to a full concept definition when the concept is processed. 


The Tutor supports and expands inline concept descriptions for the following declarations types.

## Module Declaration

The name of this module is extracted.
The header of the concept definition is automatically generated and consists of:

```
# _Module name_
.Usage
_Import declaration needed to use this module_
```

## Function Declaration

The signatures of this function and of all directly following functions with the same name are collected. 
The signatures are placed in an itemized list (unless there is only one).
The header of the concept definition is automatically generated and consists of:

```
# _Function name_
.Function
_Function signature_
.Usage
_Import declaration needed to use this module_
```

## Data Declaration

The signatures of this data declaration and of all directly following data declarations without their own `@doc` annotation 
are collected.
The header of the concept definition is automatically generated and consists of:

```
# _Data declaration name_
.Type
_Data declarations_
.Usage
_Import declaration needed to use this module_
```

## Annotation Declaration

The signature of this annotation declaration is collected.
The header of the concept definition is automatically generated and consists of:

```
# _Annotation declaration name_
.Type
_Annotation declarations_
.Usage
_Import declaration needed to use this module_
```

.Examples
We only give an example of documenting a simple function. Read the source code of Rascal library files for other ones. 

Consider the source code of the now function in the `DateTime` library.

```
  @doc{
  .Synopsis
  Get the current datetime.

  .Examples
```rascal-shell
  import DateTime;
  now();
```
  }
  @javaClass{org.rascalmpl.library.DateTime}
  public java datetime now();
```
  
  
This will be expanded to

```
  # now
  .Function
  `datetime now()`
  .Usage
  `import DateTime;`

  .Synopsis
  Get the current datetime.

  .Examples
```rascal-shell
  import DateTime;
  now();
```
```

and the final result is link:/Libraries#Prelude-DateTime#now[now].

.Benefits
A (small) part of documentation writing is automated.
The information about the name of a function, data or annotation declaration, or its signature is always consistent.

.Pitfalls 
* This approach requires that functions with the same name are grouped together in the source file.
* We do not (yet) provide direct editing of `@doc{ ... }` in Rascal source files. We should!
